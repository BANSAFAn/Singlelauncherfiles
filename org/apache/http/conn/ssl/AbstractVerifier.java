/*     */ package org.apache.http.conn.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.conn.util.InetAddressUtils;
/*     */ import org.apache.http.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class AbstractVerifier
/*     */   implements X509HostnameVerifier
/*     */ {
/*  60 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*  62 */   static final String[] BAD_COUNTRY_2LDS = new String[] { "ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg", "ne", "net", "or", "org" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  68 */     Arrays.sort((Object[])BAD_COUNTRY_2LDS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void verify(String host, SSLSocket ssl) throws IOException {
/*  74 */     Args.notNull(host, "Host");
/*  75 */     SSLSession session = ssl.getSession();
/*  76 */     if (session == null) {
/*     */ 
/*     */ 
/*     */       
/*  80 */       InputStream in = ssl.getInputStream();
/*  81 */       in.available();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 100 */       session = ssl.getSession();
/* 101 */       if (session == null) {
/*     */ 
/*     */         
/* 104 */         ssl.startHandshake();
/*     */ 
/*     */ 
/*     */         
/* 108 */         session = ssl.getSession();
/*     */       } 
/*     */     } 
/*     */     
/* 112 */     Certificate[] certs = session.getPeerCertificates();
/* 113 */     X509Certificate x509 = (X509Certificate)certs[0];
/* 114 */     verify(host, x509);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean verify(String host, SSLSession session) {
/*     */     try {
/* 120 */       Certificate[] certs = session.getPeerCertificates();
/* 121 */       X509Certificate x509 = (X509Certificate)certs[0];
/* 122 */       verify(host, x509);
/* 123 */       return true;
/* 124 */     } catch (SSLException ex) {
/* 125 */       if (this.log.isDebugEnabled()) {
/* 126 */         this.log.debug(ex.getMessage(), ex);
/*     */       }
/* 128 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void verify(String host, X509Certificate cert) throws SSLException {
/* 135 */     boolean ipv4 = InetAddressUtils.isIPv4Address(host);
/* 136 */     boolean ipv6 = InetAddressUtils.isIPv6Address(host);
/* 137 */     int subjectType = (ipv4 || ipv6) ? 7 : 2;
/* 138 */     List<String> subjectAlts = DefaultHostnameVerifier.extractSubjectAlts(cert, subjectType);
/* 139 */     X500Principal subjectPrincipal = cert.getSubjectX500Principal();
/* 140 */     String cn = DefaultHostnameVerifier.extractCN(subjectPrincipal.getName("RFC2253"));
/* 141 */     (new String[1])[0] = cn; verify(host, (cn != null) ? new String[1] : null, (subjectAlts != null && !subjectAlts.isEmpty()) ? subjectAlts.<String>toArray(new String[subjectAlts.size()]) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void verify(String host, String[] cns, String[] subjectAlts, boolean strictWithSubDomains) throws SSLException {
/* 151 */     String cn = (cns != null && cns.length > 0) ? cns[0] : null;
/* 152 */     List<String> subjectAltList = (subjectAlts != null && subjectAlts.length > 0) ? Arrays.<String>asList(subjectAlts) : null;
/*     */     
/* 154 */     String normalizedHost = InetAddressUtils.isIPv6Address(host) ? DefaultHostnameVerifier.normaliseAddress(host.toLowerCase(Locale.ROOT)) : host;
/*     */ 
/*     */     
/* 157 */     if (subjectAltList != null) {
/* 158 */       for (String subjectAlt : subjectAltList) {
/* 159 */         String normalizedAltSubject = InetAddressUtils.isIPv6Address(subjectAlt) ? DefaultHostnameVerifier.normaliseAddress(subjectAlt) : subjectAlt;
/*     */         
/* 161 */         if (matchIdentity(normalizedHost, normalizedAltSubject, strictWithSubDomains)) {
/*     */           return;
/*     */         }
/*     */       } 
/* 165 */       throw new SSLException("Certificate for <" + host + "> doesn't match any " + "of the subject alternative names: " + subjectAltList);
/*     */     } 
/* 167 */     if (cn != null) {
/* 168 */       String normalizedCN = InetAddressUtils.isIPv6Address(cn) ? DefaultHostnameVerifier.normaliseAddress(cn) : cn;
/*     */       
/* 170 */       if (matchIdentity(normalizedHost, normalizedCN, strictWithSubDomains)) {
/*     */         return;
/*     */       }
/* 173 */       throw new SSLException("Certificate for <" + host + "> doesn't match " + "common name of the certificate subject: " + cn);
/*     */     } 
/*     */     
/* 176 */     throw new SSLException("Certificate subject for <" + host + "> doesn't contain " + "a common name and does not have alternative names");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchIdentity(String host, String identity, boolean strict) {
/* 182 */     if (host == null) {
/* 183 */       return false;
/*     */     }
/* 185 */     String normalizedHost = host.toLowerCase(Locale.ROOT);
/* 186 */     String normalizedIdentity = identity.toLowerCase(Locale.ROOT);
/*     */ 
/*     */ 
/*     */     
/* 190 */     String[] parts = normalizedIdentity.split("\\.");
/* 191 */     boolean doWildcard = (parts.length >= 3 && parts[0].endsWith("*") && (!strict || validCountryWildcard(parts)));
/*     */     
/* 193 */     if (doWildcard) {
/*     */       boolean match;
/* 195 */       String firstpart = parts[0];
/* 196 */       if (firstpart.length() > 1) {
/* 197 */         String prefix = firstpart.substring(0, firstpart.length() - 1);
/* 198 */         String suffix = normalizedIdentity.substring(firstpart.length());
/* 199 */         String hostSuffix = normalizedHost.substring(prefix.length());
/* 200 */         match = (normalizedHost.startsWith(prefix) && hostSuffix.endsWith(suffix));
/*     */       } else {
/* 202 */         match = normalizedHost.endsWith(normalizedIdentity.substring(1));
/*     */       } 
/* 204 */       return (match && (!strict || countDots(normalizedHost) == countDots(normalizedIdentity)));
/*     */     } 
/* 206 */     return normalizedHost.equals(normalizedIdentity);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean validCountryWildcard(String[] parts) {
/* 211 */     if (parts.length != 3 || parts[2].length() != 2) {
/* 212 */       return true;
/*     */     }
/* 214 */     return (Arrays.binarySearch((Object[])BAD_COUNTRY_2LDS, parts[1]) < 0);
/*     */   }
/*     */   
/*     */   public static boolean acceptableCountryWildcard(String cn) {
/* 218 */     return validCountryWildcard(cn.split("\\."));
/*     */   }
/*     */   
/*     */   public static String[] getCNs(X509Certificate cert) {
/* 222 */     String subjectPrincipal = cert.getSubjectX500Principal().toString();
/*     */     try {
/* 224 */       String cn = DefaultHostnameVerifier.extractCN(subjectPrincipal);
/* 225 */       (new String[1])[0] = cn; return (cn != null) ? new String[1] : null;
/* 226 */     } catch (SSLException ex) {
/* 227 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getDNSSubjectAlts(X509Certificate cert) {
/* 248 */     List<String> subjectAlts = DefaultHostnameVerifier.extractSubjectAlts(cert, 2);
/*     */     
/* 250 */     return (subjectAlts != null && !subjectAlts.isEmpty()) ? subjectAlts.<String>toArray(new String[subjectAlts.size()]) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int countDots(String s) {
/* 260 */     int count = 0;
/* 261 */     for (int i = 0; i < s.length(); i++) {
/* 262 */       if (s.charAt(i) == '.') {
/* 263 */         count++;
/*     */       }
/*     */     } 
/* 266 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\ssl\AbstractVerifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */