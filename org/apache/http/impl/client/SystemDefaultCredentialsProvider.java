/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.net.Authenticator;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.NTCredentials;
/*     */ import org.apache.http.auth.UsernamePasswordCredentials;
/*     */ import org.apache.http.client.CredentialsProvider;
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
/*     */ @ThreadSafe
/*     */ public class SystemDefaultCredentialsProvider
/*     */   implements CredentialsProvider
/*     */ {
/*  57 */   private static final Map<String, String> SCHEME_MAP = new ConcurrentHashMap<String, String>(); static {
/*  58 */     SCHEME_MAP.put("Basic".toUpperCase(Locale.ROOT), "Basic");
/*  59 */     SCHEME_MAP.put("Digest".toUpperCase(Locale.ROOT), "Digest");
/*  60 */     SCHEME_MAP.put("NTLM".toUpperCase(Locale.ROOT), "NTLM");
/*  61 */     SCHEME_MAP.put("Negotiate".toUpperCase(Locale.ROOT), "SPNEGO");
/*  62 */     SCHEME_MAP.put("Kerberos".toUpperCase(Locale.ROOT), "Kerberos");
/*     */   }
/*     */   private final BasicCredentialsProvider internal;
/*     */   private static String translateScheme(String key) {
/*  66 */     if (key == null) {
/*  67 */       return null;
/*     */     }
/*  69 */     String s = SCHEME_MAP.get(key);
/*  70 */     return (s != null) ? s : key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SystemDefaultCredentialsProvider() {
/*  80 */     this.internal = new BasicCredentialsProvider();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCredentials(AuthScope authscope, Credentials credentials) {
/*  85 */     this.internal.setCredentials(authscope, credentials);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static PasswordAuthentication getSystemCreds(AuthScope authscope, Authenticator.RequestorType requestorType) {
/*  91 */     String hostname = authscope.getHost();
/*  92 */     int port = authscope.getPort();
/*  93 */     HttpHost origin = authscope.getOrigin();
/*  94 */     String protocol = (origin != null) ? origin.getSchemeName() : ((port == 443) ? "https" : "http");
/*     */     
/*  96 */     return Authenticator.requestPasswordAuthentication(hostname, null, port, protocol, null, translateScheme(authscope.getScheme()), null, requestorType);
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
/*     */   public Credentials getCredentials(AuthScope authscope) {
/* 109 */     Args.notNull(authscope, "Auth scope");
/* 110 */     Credentials localcreds = this.internal.getCredentials(authscope);
/* 111 */     if (localcreds != null) {
/* 112 */       return localcreds;
/*     */     }
/* 114 */     if (authscope.getHost() != null) {
/* 115 */       PasswordAuthentication systemcreds = getSystemCreds(authscope, Authenticator.RequestorType.SERVER);
/*     */       
/* 117 */       if (systemcreds == null) {
/* 118 */         systemcreds = getSystemCreds(authscope, Authenticator.RequestorType.PROXY);
/*     */       }
/*     */       
/* 121 */       if (systemcreds != null) {
/* 122 */         String domain = System.getProperty("http.auth.ntlm.domain");
/* 123 */         if (domain != null) {
/* 124 */           return (Credentials)new NTCredentials(systemcreds.getUserName(), new String(systemcreds.getPassword()), null, domain);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 129 */         if ("NTLM".equalsIgnoreCase(authscope.getScheme()))
/*     */         {
/* 131 */           return (Credentials)new NTCredentials(systemcreds.getUserName(), new String(systemcreds.getPassword()), null, null);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 136 */         return (Credentials)new UsernamePasswordCredentials(systemcreds.getUserName(), new String(systemcreds.getPassword()));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 143 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 148 */     this.internal.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\SystemDefaultCredentialsProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */