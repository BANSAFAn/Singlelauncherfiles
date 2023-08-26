/*     */ package org.apache.commons.compress.archivers.sevenz;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.CipherInputStream;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.apache.commons.compress.PasswordRequiredException;
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
/*     */ class AES256SHA256Decoder
/*     */   extends CoderBase
/*     */ {
/*     */   AES256SHA256Decoder() {
/*  34 */     super(new Class[0]);
/*     */   }
/*     */   
/*     */   InputStream decode(final String archiveName, final InputStream in, long uncompressedLength, final Coder coder, final byte[] passwordBytes, int maxMemoryLimitInKb) {
/*  38 */     return new InputStream() {
/*     */         private boolean isInitialized;
/*     */         
/*     */         private CipherInputStream init() throws IOException {
/*     */           byte[] aesKeyBytes;
/*  43 */           if (this.isInitialized) {
/*  44 */             return this.cipherInputStream;
/*     */           }
/*  46 */           if (coder.properties == null) {
/*  47 */             throw new IOException("Missing AES256 properties in " + archiveName);
/*     */           }
/*  49 */           if (coder.properties.length < 2) {
/*  50 */             throw new IOException("AES256 properties too short in " + archiveName);
/*     */           }
/*  52 */           int byte0 = 0xFF & coder.properties[0];
/*  53 */           int numCyclesPower = byte0 & 0x3F;
/*  54 */           int byte1 = 0xFF & coder.properties[1];
/*  55 */           int ivSize = (byte0 >> 6 & 0x1) + (byte1 & 0xF);
/*  56 */           int saltSize = (byte0 >> 7 & 0x1) + (byte1 >> 4);
/*  57 */           if (2 + saltSize + ivSize > coder.properties.length) {
/*  58 */             throw new IOException("Salt size + IV size too long in " + archiveName);
/*     */           }
/*  60 */           byte[] salt = new byte[saltSize];
/*  61 */           System.arraycopy(coder.properties, 2, salt, 0, saltSize);
/*  62 */           byte[] iv = new byte[16];
/*  63 */           System.arraycopy(coder.properties, 2 + saltSize, iv, 0, ivSize);
/*     */           
/*  65 */           if (passwordBytes == null) {
/*  66 */             throw new PasswordRequiredException(archiveName);
/*     */           }
/*     */           
/*  69 */           if (numCyclesPower == 63) {
/*  70 */             aesKeyBytes = new byte[32];
/*  71 */             System.arraycopy(salt, 0, aesKeyBytes, 0, saltSize);
/*  72 */             System.arraycopy(passwordBytes, 0, aesKeyBytes, saltSize, 
/*  73 */                 Math.min(passwordBytes.length, aesKeyBytes.length - saltSize));
/*     */           } else {
/*     */             MessageDigest digest;
/*     */             try {
/*  77 */               digest = MessageDigest.getInstance("SHA-256");
/*  78 */             } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
/*  79 */               throw new IOException("SHA-256 is unsupported by your Java implementation", noSuchAlgorithmException);
/*     */             } 
/*     */             
/*  82 */             byte[] extra = new byte[8]; long j;
/*  83 */             for (j = 0L; j < 1L << numCyclesPower; j++) {
/*  84 */               digest.update(salt);
/*  85 */               digest.update(passwordBytes);
/*  86 */               digest.update(extra);
/*  87 */               for (int k = 0; k < extra.length; k++) {
/*  88 */                 extra[k] = (byte)(extra[k] + 1);
/*  89 */                 if (extra[k] != 0) {
/*     */                   break;
/*     */                 }
/*     */               } 
/*     */             } 
/*  94 */             aesKeyBytes = digest.digest();
/*     */           } 
/*     */           
/*  97 */           SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
/*     */           try {
/*  99 */             Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
/* 100 */             cipher.init(2, aesKey, new IvParameterSpec(iv));
/* 101 */             this.cipherInputStream = new CipherInputStream(in, cipher);
/* 102 */             this.isInitialized = true;
/* 103 */             return this.cipherInputStream;
/* 104 */           } catch (GeneralSecurityException generalSecurityException) {
/* 105 */             throw new IOException("Decryption error (do you have the JCE Unlimited Strength Jurisdiction Policy Files installed?)", generalSecurityException);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         private CipherInputStream cipherInputStream;
/*     */         
/*     */         public int read() throws IOException {
/* 113 */           return init().read();
/*     */         }
/*     */ 
/*     */         
/*     */         public int read(byte[] b, int off, int len) throws IOException {
/* 118 */           return init().read(b, off, len);
/*     */         }
/*     */ 
/*     */         
/*     */         public void close() throws IOException {
/* 123 */           if (this.cipherInputStream != null)
/* 124 */             this.cipherInputStream.close(); 
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\sevenz\AES256SHA256Decoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */