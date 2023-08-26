/*     */ package org.apache.http.ssl;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.Socket;
/*     */ import java.net.URL;
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
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509ExtendedKeyManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class SSLContextBuilder
/*     */ {
/*     */   static final String TLS = "TLS";
/*     */   private String protocol;
/*     */   private final Set<KeyManager> keymanagers;
/*     */   private final Set<TrustManager> trustmanagers;
/*     */   private SecureRandom secureRandom;
/*     */   
/*     */   public static SSLContextBuilder create() {
/*  87 */     return new SSLContextBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLContextBuilder() {
/*  92 */     this.keymanagers = new LinkedHashSet<KeyManager>();
/*  93 */     this.trustmanagers = new LinkedHashSet<TrustManager>();
/*     */   }
/*     */   
/*     */   public SSLContextBuilder useProtocol(String protocol) {
/*  97 */     this.protocol = protocol;
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public SSLContextBuilder setSecureRandom(SecureRandom secureRandom) {
/* 102 */     this.secureRandom = secureRandom;
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(KeyStore truststore, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
/* 109 */     TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*     */     
/* 111 */     tmfactory.init(truststore);
/* 112 */     TrustManager[] tms = tmfactory.getTrustManagers();
/* 113 */     if (tms != null) {
/* 114 */       if (trustStrategy != null) {
/* 115 */         for (int i = 0; i < tms.length; i++) {
/* 116 */           TrustManager tm = tms[i];
/* 117 */           if (tm instanceof X509TrustManager) {
/* 118 */             tms[i] = new TrustManagerDelegate((X509TrustManager)tm, trustStrategy);
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 123 */       for (TrustManager tm : tms) {
/* 124 */         this.trustmanagers.add(tm);
/*     */       }
/*     */     } 
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
/* 132 */     return loadTrustMaterial((KeyStore)null, trustStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(File file, char[] storePassword, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 139 */     Args.notNull(file, "Truststore file");
/* 140 */     KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
/* 141 */     FileInputStream instream = new FileInputStream(file);
/*     */     try {
/* 143 */       trustStore.load(instream, storePassword);
/*     */     } finally {
/* 145 */       instream.close();
/*     */     } 
/* 147 */     return loadTrustMaterial(trustStore, trustStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(File file, char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 153 */     return loadTrustMaterial(file, storePassword, (TrustStrategy)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(File file) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 158 */     return loadTrustMaterial(file, (char[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(URL url, char[] storePassword, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 165 */     Args.notNull(url, "Truststore URL");
/* 166 */     KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
/* 167 */     InputStream instream = url.openStream();
/*     */     try {
/* 169 */       trustStore.load(instream, storePassword);
/*     */     } finally {
/* 171 */       instream.close();
/*     */     } 
/* 173 */     return loadTrustMaterial(trustStore, trustStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadTrustMaterial(URL url, char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
/* 179 */     return loadTrustMaterial(url, storePassword, (TrustStrategy)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(KeyStore keystore, char[] keyPassword, PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
/* 187 */     KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
/*     */     
/* 189 */     kmfactory.init(keystore, keyPassword);
/* 190 */     KeyManager[] kms = kmfactory.getKeyManagers();
/* 191 */     if (kms != null) {
/* 192 */       if (aliasStrategy != null) {
/* 193 */         for (int i = 0; i < kms.length; i++) {
/* 194 */           KeyManager km = kms[i];
/* 195 */           if (km instanceof X509ExtendedKeyManager) {
/* 196 */             kms[i] = new KeyManagerDelegate((X509ExtendedKeyManager)km, aliasStrategy);
/*     */           }
/*     */         } 
/*     */       }
/* 200 */       for (KeyManager km : kms) {
/* 201 */         this.keymanagers.add(km);
/*     */       }
/*     */     } 
/* 204 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(KeyStore keystore, char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
/* 210 */     return loadKeyMaterial(keystore, keyPassword, (PrivateKeyStrategy)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(File file, char[] storePassword, char[] keyPassword, PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
/* 218 */     Args.notNull(file, "Keystore file");
/* 219 */     KeyStore identityStore = KeyStore.getInstance(KeyStore.getDefaultType());
/* 220 */     FileInputStream instream = new FileInputStream(file);
/*     */     try {
/* 222 */       identityStore.load(instream, storePassword);
/*     */     } finally {
/* 224 */       instream.close();
/*     */     } 
/* 226 */     return loadKeyMaterial(identityStore, keyPassword, aliasStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(File file, char[] storePassword, char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
/* 233 */     return loadKeyMaterial(file, storePassword, keyPassword, (PrivateKeyStrategy)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(URL url, char[] storePassword, char[] keyPassword, PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
/* 241 */     Args.notNull(url, "Keystore URL");
/* 242 */     KeyStore identityStore = KeyStore.getInstance(KeyStore.getDefaultType());
/* 243 */     InputStream instream = url.openStream();
/*     */     try {
/* 245 */       identityStore.load(instream, storePassword);
/*     */     } finally {
/* 247 */       instream.close();
/*     */     } 
/* 249 */     return loadKeyMaterial(identityStore, keyPassword, aliasStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContextBuilder loadKeyMaterial(URL url, char[] storePassword, char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
/* 256 */     return loadKeyMaterial(url, storePassword, keyPassword, (PrivateKeyStrategy)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initSSLContext(SSLContext sslcontext, Collection<KeyManager> keyManagers, Collection<TrustManager> trustManagers, SecureRandom secureRandom) throws KeyManagementException {
/* 264 */     sslcontext.init(!keyManagers.isEmpty() ? keyManagers.<KeyManager>toArray(new KeyManager[keyManagers.size()]) : null, !trustManagers.isEmpty() ? trustManagers.<TrustManager>toArray(new TrustManager[trustManagers.size()]) : null, secureRandom);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
/* 271 */     SSLContext sslcontext = SSLContext.getInstance((this.protocol != null) ? this.protocol : "TLS");
/*     */     
/* 273 */     initSSLContext(sslcontext, this.keymanagers, this.trustmanagers, this.secureRandom);
/* 274 */     return sslcontext;
/*     */   }
/*     */   
/*     */   static class TrustManagerDelegate
/*     */     implements X509TrustManager
/*     */   {
/*     */     private final X509TrustManager trustManager;
/*     */     private final TrustStrategy trustStrategy;
/*     */     
/*     */     TrustManagerDelegate(X509TrustManager trustManager, TrustStrategy trustStrategy) {
/* 284 */       this.trustManager = trustManager;
/* 285 */       this.trustStrategy = trustStrategy;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/* 291 */       this.trustManager.checkClientTrusted(chain, authType);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/* 297 */       if (!this.trustStrategy.isTrusted(chain, authType)) {
/* 298 */         this.trustManager.checkServerTrusted(chain, authType);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public X509Certificate[] getAcceptedIssuers() {
/* 304 */       return this.trustManager.getAcceptedIssuers();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class KeyManagerDelegate
/*     */     extends X509ExtendedKeyManager
/*     */   {
/*     */     private final X509ExtendedKeyManager keyManager;
/*     */     private final PrivateKeyStrategy aliasStrategy;
/*     */     
/*     */     KeyManagerDelegate(X509ExtendedKeyManager keyManager, PrivateKeyStrategy aliasStrategy) {
/* 316 */       this.keyManager = keyManager;
/* 317 */       this.aliasStrategy = aliasStrategy;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getClientAliases(String keyType, Principal[] issuers) {
/* 323 */       return this.keyManager.getClientAliases(keyType, issuers);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, PrivateKeyDetails> getClientAliasMap(String[] keyTypes, Principal[] issuers) {
/* 328 */       Map<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
/* 329 */       for (String keyType : keyTypes) {
/* 330 */         String[] aliases = this.keyManager.getClientAliases(keyType, issuers);
/* 331 */         if (aliases != null) {
/* 332 */           for (String alias : aliases) {
/* 333 */             validAliases.put(alias, new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
/*     */           }
/*     */         }
/*     */       } 
/*     */       
/* 338 */       return validAliases;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<String, PrivateKeyDetails> getServerAliasMap(String keyType, Principal[] issuers) {
/* 343 */       Map<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
/* 344 */       String[] aliases = this.keyManager.getServerAliases(keyType, issuers);
/* 345 */       if (aliases != null) {
/* 346 */         for (String alias : aliases) {
/* 347 */           validAliases.put(alias, new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
/*     */         }
/*     */       }
/*     */       
/* 351 */       return validAliases;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
/* 357 */       Map<String, PrivateKeyDetails> validAliases = getClientAliasMap(keyTypes, issuers);
/* 358 */       return this.aliasStrategy.chooseAlias(validAliases, socket);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getServerAliases(String keyType, Principal[] issuers) {
/* 364 */       return this.keyManager.getServerAliases(keyType, issuers);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
/* 370 */       Map<String, PrivateKeyDetails> validAliases = getServerAliasMap(keyType, issuers);
/* 371 */       return this.aliasStrategy.chooseAlias(validAliases, socket);
/*     */     }
/*     */ 
/*     */     
/*     */     public X509Certificate[] getCertificateChain(String alias) {
/* 376 */       return this.keyManager.getCertificateChain(alias);
/*     */     }
/*     */ 
/*     */     
/*     */     public PrivateKey getPrivateKey(String alias) {
/* 381 */       return this.keyManager.getPrivateKey(alias);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseEngineClientAlias(String[] keyTypes, Principal[] issuers, SSLEngine sslEngine) {
/* 387 */       Map<String, PrivateKeyDetails> validAliases = getClientAliasMap(keyTypes, issuers);
/* 388 */       return this.aliasStrategy.chooseAlias(validAliases, null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine sslEngine) {
/* 394 */       Map<String, PrivateKeyDetails> validAliases = getServerAliasMap(keyType, issuers);
/* 395 */       return this.aliasStrategy.chooseAlias(validAliases, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\ssl\SSLContextBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */