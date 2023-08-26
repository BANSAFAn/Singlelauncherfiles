/*     */ package org.apache.http.conn.ssl;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.ldap.LdapName;
/*     */ import javax.naming.ldap.Rdn;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.util.DomainType;
/*     */ import org.apache.http.conn.util.InetAddressUtils;
/*     */ import org.apache.http.conn.util.PublicSuffixMatcher;
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
/*     */ @Immutable
/*     */ public final class DefaultHostnameVerifier
/*     */   implements HostnameVerifier
/*     */ {
/*     */   static final int DNS_NAME_TYPE = 2;
/*     */   static final int IP_ADDRESS_TYPE = 7;
/*     */   
/*     */   enum TYPE
/*     */   {
/*  67 */     IPv4, IPv6, DNS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  72 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final PublicSuffixMatcher publicSuffixMatcher;
/*     */   
/*     */   public DefaultHostnameVerifier(PublicSuffixMatcher publicSuffixMatcher) {
/*  77 */     this.publicSuffixMatcher = publicSuffixMatcher;
/*     */   }
/*     */   
/*     */   public DefaultHostnameVerifier() {
/*  81 */     this(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean verify(String host, SSLSession session) {
/*     */     try {
/*  87 */       Certificate[] certs = session.getPeerCertificates();
/*  88 */       X509Certificate x509 = (X509Certificate)certs[0];
/*  89 */       verify(host, x509);
/*  90 */       return true;
/*  91 */     } catch (SSLException ex) {
/*  92 */       if (this.log.isDebugEnabled()) {
/*  93 */         this.log.debug(ex.getMessage(), ex);
/*     */       }
/*  95 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void verify(String host, X509Certificate cert) throws SSLException {
/* 101 */     TYPE hostFormat = TYPE.DNS;
/* 102 */     if (InetAddressUtils.isIPv4Address(host)) {
/* 103 */       hostFormat = TYPE.IPv4;
/*     */     } else {
/* 105 */       String s = host;
/* 106 */       if (s.startsWith("[") && s.endsWith("]")) {
/* 107 */         s = host.substring(1, host.length() - 1);
/*     */       }
/* 109 */       if (InetAddressUtils.isIPv6Address(s)) {
/* 110 */         hostFormat = TYPE.IPv6;
/*     */       }
/*     */     } 
/* 113 */     int subjectType = (hostFormat == TYPE.IPv4 || hostFormat == TYPE.IPv6) ? 7 : 2;
/* 114 */     List<String> subjectAlts = extractSubjectAlts(cert, subjectType);
/* 115 */     if (subjectAlts != null && !subjectAlts.isEmpty()) {
/* 116 */       switch (hostFormat) {
/*     */         case IPv4:
/* 118 */           matchIPAddress(host, subjectAlts);
/*     */           return;
/*     */         case IPv6:
/* 121 */           matchIPv6Address(host, subjectAlts);
/*     */           return;
/*     */       } 
/* 124 */       matchDNSName(host, subjectAlts, this.publicSuffixMatcher);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 129 */       X500Principal subjectPrincipal = cert.getSubjectX500Principal();
/* 130 */       String cn = extractCN(subjectPrincipal.getName("RFC2253"));
/* 131 */       if (cn == null) {
/* 132 */         throw new SSLException("Certificate subject for <" + host + "> doesn't contain " + "a common name and does not have alternative names");
/*     */       }
/*     */       
/* 135 */       matchCN(host, cn, this.publicSuffixMatcher);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void matchIPAddress(String host, List<String> subjectAlts) throws SSLException {
/* 140 */     for (int i = 0; i < subjectAlts.size(); i++) {
/* 141 */       String subjectAlt = subjectAlts.get(i);
/* 142 */       if (host.equals(subjectAlt)) {
/*     */         return;
/*     */       }
/*     */     } 
/* 146 */     throw new SSLException("Certificate for <" + host + "> doesn't match any " + "of the subject alternative names: " + subjectAlts);
/*     */   }
/*     */ 
/*     */   
/*     */   static void matchIPv6Address(String host, List<String> subjectAlts) throws SSLException {
/* 151 */     String normalisedHost = normaliseAddress(host);
/* 152 */     for (int i = 0; i < subjectAlts.size(); i++) {
/* 153 */       String subjectAlt = subjectAlts.get(i);
/* 154 */       String normalizedSubjectAlt = normaliseAddress(subjectAlt);
/* 155 */       if (normalisedHost.equals(normalizedSubjectAlt)) {
/*     */         return;
/*     */       }
/*     */     } 
/* 159 */     throw new SSLException("Certificate for <" + host + "> doesn't match any " + "of the subject alternative names: " + subjectAlts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void matchDNSName(String host, List<String> subjectAlts, PublicSuffixMatcher publicSuffixMatcher) throws SSLException {
/* 165 */     String normalizedHost = host.toLowerCase(Locale.ROOT);
/* 166 */     for (int i = 0; i < subjectAlts.size(); i++) {
/* 167 */       String subjectAlt = subjectAlts.get(i);
/* 168 */       String normalizedSubjectAlt = subjectAlt.toLowerCase(Locale.ROOT);
/* 169 */       if (matchIdentityStrict(normalizedHost, normalizedSubjectAlt, publicSuffixMatcher)) {
/*     */         return;
/*     */       }
/*     */     } 
/* 173 */     throw new SSLException("Certificate for <" + host + "> doesn't match any " + "of the subject alternative names: " + subjectAlts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void matchCN(String host, String cn, PublicSuffixMatcher publicSuffixMatcher) throws SSLException {
/* 179 */     if (!matchIdentityStrict(host, cn, publicSuffixMatcher)) {
/* 180 */       throw new SSLException("Certificate for <" + host + "> doesn't match " + "common name of the certificate subject: " + cn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean matchDomainRoot(String host, String domainRoot) {
/* 186 */     if (domainRoot == null) {
/* 187 */       return false;
/*     */     }
/* 189 */     return (host.endsWith(domainRoot) && (host.length() == domainRoot.length() || host.charAt(host.length() - domainRoot.length() - 1) == '.'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchIdentity(String host, String identity, PublicSuffixMatcher publicSuffixMatcher, boolean strict) {
/* 196 */     if (publicSuffixMatcher != null && host.contains(".") && 
/* 197 */       !matchDomainRoot(host, publicSuffixMatcher.getDomainRoot(identity, DomainType.ICANN))) {
/* 198 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     int asteriskIdx = identity.indexOf('*');
/* 208 */     if (asteriskIdx != -1) {
/* 209 */       String prefix = identity.substring(0, asteriskIdx);
/* 210 */       String suffix = identity.substring(asteriskIdx + 1);
/* 211 */       if (!prefix.isEmpty() && !host.startsWith(prefix)) {
/* 212 */         return false;
/*     */       }
/* 214 */       if (!suffix.isEmpty() && !host.endsWith(suffix)) {
/* 215 */         return false;
/*     */       }
/*     */       
/* 218 */       if (strict) {
/* 219 */         String remainder = host.substring(prefix.length(), host.length() - suffix.length());
/*     */         
/* 221 */         if (remainder.contains(".")) {
/* 222 */           return false;
/*     */         }
/*     */       } 
/* 225 */       return true;
/*     */     } 
/* 227 */     return host.equalsIgnoreCase(identity);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean matchIdentity(String host, String identity, PublicSuffixMatcher publicSuffixMatcher) {
/* 232 */     return matchIdentity(host, identity, publicSuffixMatcher, false);
/*     */   }
/*     */   
/*     */   static boolean matchIdentity(String host, String identity) {
/* 236 */     return matchIdentity(host, identity, null, false);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean matchIdentityStrict(String host, String identity, PublicSuffixMatcher publicSuffixMatcher) {
/* 241 */     return matchIdentity(host, identity, publicSuffixMatcher, true);
/*     */   }
/*     */   
/*     */   static boolean matchIdentityStrict(String host, String identity) {
/* 245 */     return matchIdentity(host, identity, null, true);
/*     */   }
/*     */   
/*     */   static String extractCN(String subjectPrincipal) throws SSLException {
/* 249 */     if (subjectPrincipal == null) {
/* 250 */       return null;
/*     */     }
/*     */     try {
/* 253 */       LdapName subjectDN = new LdapName(subjectPrincipal);
/* 254 */       List<Rdn> rdns = subjectDN.getRdns();
/* 255 */       for (int i = rdns.size() - 1; i >= 0; i--) {
/* 256 */         Rdn rds = rdns.get(i);
/* 257 */         Attributes attributes = rds.toAttributes();
/* 258 */         Attribute cn = attributes.get("cn");
/* 259 */         if (cn != null) {
/*     */           
/* 261 */           try { Object value = cn.get();
/* 262 */             if (value != null) {
/* 263 */               return value.toString();
/*     */             } }
/* 265 */           catch (NoSuchElementException ignore) {  }
/* 266 */           catch (NamingException ignore) {}
/*     */         }
/*     */       } 
/*     */       
/* 270 */       return null;
/* 271 */     } catch (InvalidNameException e) {
/* 272 */       throw new SSLException(subjectPrincipal + " is not a valid X500 distinguished name");
/*     */     } 
/*     */   }
/*     */   
/*     */   static List<String> extractSubjectAlts(X509Certificate cert, int subjectType) {
/* 277 */     Collection<List<?>> c = null;
/*     */     try {
/* 279 */       c = cert.getSubjectAlternativeNames();
/* 280 */     } catch (CertificateParsingException ignore) {}
/*     */     
/* 282 */     List<String> subjectAltList = null;
/* 283 */     if (c != null) {
/* 284 */       for (List<?> aC : c) {
/* 285 */         List<?> list = aC;
/* 286 */         int type = ((Integer)list.get(0)).intValue();
/* 287 */         if (type == subjectType) {
/* 288 */           String s = (String)list.get(1);
/* 289 */           if (subjectAltList == null) {
/* 290 */             subjectAltList = new ArrayList<String>();
/*     */           }
/* 292 */           subjectAltList.add(s);
/*     */         } 
/*     */       } 
/*     */     }
/* 296 */     return subjectAltList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String normaliseAddress(String hostname) {
/* 303 */     if (hostname == null) {
/* 304 */       return hostname;
/*     */     }
/*     */     try {
/* 307 */       InetAddress inetAddress = InetAddress.getByName(hostname);
/* 308 */       return inetAddress.getHostAddress();
/* 309 */     } catch (UnknownHostException unexpected) {
/* 310 */       return hostname;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\ssl\DefaultHostnameVerifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */