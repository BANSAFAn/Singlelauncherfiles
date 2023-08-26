/*    */ package com.sothawo.mapjfx.cache;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
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
/*    */ class WriteCacheFileInputStream
/*    */   extends FilterInputStream
/*    */ {
/*    */   private final OutputStream out;
/*    */   private Runnable notifyOnClose;
/*    */   
/*    */   protected WriteCacheFileInputStream(InputStream in, OutputStream out) {
/* 48 */     super(in);
/* 49 */     this.out = out;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 54 */     int numBytes = super.read(b, off, len);
/* 55 */     if (null != this.out && numBytes > 0) {
/* 56 */       this.out.write(b, off, numBytes);
/*    */     }
/* 58 */     return numBytes;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 63 */     super.close();
/* 64 */     if (null != this.out) {
/* 65 */       this.out.flush();
/* 66 */       this.out.close();
/*    */     } 
/* 68 */     if (null != this.notifyOnClose) {
/* 69 */       this.notifyOnClose.run();
/*    */     }
/*    */   }
/*    */   
/*    */   public void onInputStreamClose(Runnable r) {
/* 74 */     this.notifyOnClose = r;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\sothawo\mapjfx\cache\WriteCacheFileInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */