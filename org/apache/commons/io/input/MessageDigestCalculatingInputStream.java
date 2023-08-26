/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
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
/*     */ public class MessageDigestCalculatingInputStream
/*     */   extends ObservableInputStream
/*     */ {
/*     */   private final MessageDigest messageDigest;
/*     */   
/*     */   public static class MessageDigestMaintainingObserver
/*     */     extends ObservableInputStream.Observer
/*     */   {
/*     */     private final MessageDigest messageDigest;
/*     */     
/*     */     public MessageDigestMaintainingObserver(MessageDigest messageDigest) {
/*  45 */       this.messageDigest = messageDigest;
/*     */     }
/*     */ 
/*     */     
/*     */     public void data(int input) throws IOException {
/*  50 */       this.messageDigest.update((byte)input);
/*     */     }
/*     */ 
/*     */     
/*     */     public void data(byte[] input, int offset, int length) throws IOException {
/*  55 */       this.messageDigest.update(input, offset, length);
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
/*     */   public MessageDigestCalculatingInputStream(InputStream inputStream, MessageDigest messageDigest) {
/*  67 */     super(inputStream, new ObservableInputStream.Observer[] { new MessageDigestMaintainingObserver(messageDigest) });
/*  68 */     this.messageDigest = messageDigest;
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
/*     */   public MessageDigestCalculatingInputStream(InputStream inputStream, String algorithm) throws NoSuchAlgorithmException {
/*  82 */     this(inputStream, MessageDigest.getInstance(algorithm));
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
/*     */   public MessageDigestCalculatingInputStream(InputStream inputStream) throws NoSuchAlgorithmException {
/*  94 */     this(inputStream, MessageDigest.getInstance("MD5"));
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
/*     */   public MessageDigest getMessageDigest() {
/* 106 */     return this.messageDigest;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\MessageDigestCalculatingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */