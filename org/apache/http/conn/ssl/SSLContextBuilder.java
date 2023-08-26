/*     */ package org.apache.http.conn.ssl;
/*     */ 
/*     */ import java.net.Socket;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509KeyManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public class SSLContextBuilder
/*     */ {
/*     */   static final String TLS = "TLS";
/*     */   static final String SSL = "SSL";
/*     */   private String protocol;
/*  77 */   private final Set<KeyManager> keymanagers = new LinkedHashSet<KeyManager>();
/*  78 */   private final Set<TrustManager> trustmanagers = new LinkedHashSet<TrustManager>();
/*     */   private SecureRandom secureRandom;
/*     */   
/*     */   public SSLContextBuilder useTLS() {
/*  82 */     this.protocol = "TLS";
/*  83 */     return this;
/*     */   }
/*     */   
/*     */   public SSLContextBuilder useSSL() {
/*  87 */     this.protocol = "SSL";
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public SSLContextBuilder useProtocol(String protocol) {
/*  92 */     this.protocol = protocol;
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public SSLContextBuilder setSecureRandom(SecureRandom secureRandom) {
/*  97 */     this.secureRandom = secureRandom;
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(KeyStore truststore, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
/* 104 */     TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*     */     
/* 106 */     tmfactory.init(truststore);
/* 107 */     TrustManager[] tms = tmfactory.getTrustManagers();
/* 108 */     if (tms != null) {
/* 109 */       if (trustStrategy != null) {
/* 110 */         for (int i = 0; i < tms.length; i++) {
/* 111 */           TrustManager tm = tms[i];
/* 112 */           if (tm instanceof X509TrustManager) {
/* 113 */             tms[i] = new TrustManagerDelegate((X509TrustManager)tm, trustStrategy);
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 118 */       for (TrustManager tm : tms) {
/* 119 */         this.trustmanagers.add(tm);
/*     */       }
/*     */     } 
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(KeyStore truststore) throws NoSuchAlgorithmException, KeyStoreException {
/* 127 */     return loadTrustMaterial(truststore, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(KeyStore keystore, char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
/* 134 */     loadKeyMaterial(keystore, keyPassword, null);
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(KeyStore keystore, char[] keyPassword, PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
/* 143 */     KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
/*     */     
/* 145 */     kmfactory.init(keystore, keyPassword);
/* 146 */     KeyManager[] kms = kmfactory.getKeyManagers();
/* 147 */     if (kms != null) {
/* 148 */       if (aliasStrategy != null) {
/* 149 */         for (int i = 0; i < kms.length; i++) {
/* 150 */           KeyManager km = kms[i];
/* 151 */           if (km instanceof X509KeyManager) {
/* 152 */             kms[i] = new KeyManagerDelegate((X509KeyManager)km, aliasStrategy);
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 157 */       for (KeyManager km : kms) {
/* 158 */         this.keymanagers.add(km);
/*     */       }
/*     */     } 
/* 161 */     return this;
/*     */   }
/*     */   
/*     */   public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
/* 165 */     SSLContext sslcontext = SSLContext.getInstance((this.protocol != null) ? this.protocol : "TLS");
/*     */     
/* 167 */     sslcontext.init(!this.keymanagers.isEmpty() ? this.keymanagers.<KeyManager>toArray(new KeyManager[this.keymanagers.size()]) : null, !this.trustmanagers.isEmpty() ? this.trustmanagers.<TrustManager>toArray(new TrustManager[this.trustmanagers.size()]) : null, this.secureRandom);
/*     */ 
/*     */ 
/*     */     
/* 171 */     return sslcontext;
/*     */   }
/*     */   
/*     */   static class TrustManagerDelegate
/*     */     implements X509TrustManager
/*     */   {
/*     */     private final X509TrustManager trustManager;
/*     */     private final TrustStrategy trustStrategy;
/*     */     
/*     */     TrustManagerDelegate(X509TrustManager trustManager, TrustStrategy trustStrategy) {
/* 181 */       this.trustManager = trustManager;
/* 182 */       this.trustStrategy = trustStrategy;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/* 188 */       this.trustManager.checkClientTrusted(chain, authType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/* 194 */       if (!this.trustStrategy.isTrusted(chain, authType)) {
/* 195 */         this.trustManager.checkServerTrusted(chain, authType);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public X509Certificate[] getAcceptedIssuers() {
/* 201 */       return this.trustManager.getAcceptedIssuers();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class KeyManagerDelegate
/*     */     implements X509KeyManager
/*     */   {
/*     */     private final X509KeyManager keyManager;
/*     */     private final PrivateKeyStrategy aliasStrategy;
/*     */     
/*     */     KeyManagerDelegate(X509KeyManager keyManager, PrivateKeyStrategy aliasStrategy) {
/* 213 */       this.keyManager = keyManager;
/* 214 */       this.aliasStrategy = aliasStrategy;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getClientAliases(String keyType, Principal[] issuers) {
/* 220 */       return this.keyManager.getClientAliases(keyType, issuers);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
/* 226 */       Map<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
/* 227 */       for (String keyType : keyTypes) {
/* 228 */         String[] aliases = this.keyManager.getClientAliases(keyType, issuers);
/* 229 */         if (aliases != null) {
/* 230 */           for (String alias : aliases) {
/* 231 */             validAliases.put(alias, new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
/*     */           }
/*     */         }
/*     */       } 
/*     */       
/* 236 */       return this.aliasStrategy.chooseAlias(validAliases, socket);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getServerAliases(String keyType, Principal[] issuers) {
/* 242 */       return this.keyManager.getServerAliases(keyType, issuers);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
/* 248 */       Map<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
/* 249 */       String[] aliases = this.keyManager.getServerAliases(keyType, issuers);
/* 250 */       if (aliases != null) {
/* 251 */         for (String alias : aliases) {
/* 252 */           validAliases.put(alias, new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
/*     */         }
/*     */       }
/*     */       
/* 256 */       return this.aliasStrategy.chooseAlias(validAliases, socket);
/*     */     }
/*     */ 
/*     */     
/*     */     public X509Certificate[] getCertificateChain(String alias) {
/* 261 */       return this.keyManager.getCertificateChain(alias);
/*     */     }
/*     */ 
/*     */     
/*     */     public PrivateKey getPrivateKey(String alias) {
/* 266 */       return this.keyManager.getPrivateKey(alias);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\ssl\SSLContextBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */