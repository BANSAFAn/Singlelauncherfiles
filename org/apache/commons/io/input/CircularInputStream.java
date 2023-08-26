/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CircularInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private long byteCount;
/*    */   
/*    */   private static byte[] validate(byte[] repeatContent) {
/* 45 */     Objects.requireNonNull(repeatContent, "repeatContent");
/* 46 */     for (byte b : repeatContent) {
/* 47 */       if (b == -1) {
/* 48 */         throw new IllegalArgumentException("repeatContent contains the end-of-stream marker -1");
/*    */       }
/*    */     } 
/* 51 */     return repeatContent;
/*    */   }
/*    */ 
/*    */   
/* 55 */   private int position = -1;
/*    */ 
/*    */   
/*    */   private final byte[] repeatedContent;
/*    */ 
/*    */   
/*    */   private final long targetByteCount;
/*    */ 
/*    */ 
/*    */   
/*    */   public CircularInputStream(byte[] repeatContent, long targetByteCount) {
/* 66 */     this.repeatedContent = validate(repeatContent);
/* 67 */     if (repeatContent.length == 0) {
/* 68 */       throw new IllegalArgumentException("repeatContent is empty.");
/*    */     }
/* 70 */     this.targetByteCount = targetByteCount;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() {
/* 75 */     if (this.targetByteCount >= 0L) {
/* 76 */       if (this.byteCount == this.targetByteCount) {
/* 77 */         return -1;
/*    */       }
/* 79 */       this.byteCount++;
/*    */     } 
/* 81 */     this.position = (this.position + 1) % this.repeatedContent.length;
/* 82 */     return this.repeatedContent[this.position] & 0xFF;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\CircularInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */