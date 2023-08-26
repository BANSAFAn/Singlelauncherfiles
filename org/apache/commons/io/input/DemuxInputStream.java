/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.commons.io.IOUtils;
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
/*    */ public class DemuxInputStream
/*    */   extends InputStream
/*    */ {
/* 30 */   private final InheritableThreadLocal<InputStream> inputStreamLocal = new InheritableThreadLocal<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InputStream bindStream(InputStream input) {
/* 39 */     InputStream oldValue = this.inputStreamLocal.get();
/* 40 */     this.inputStreamLocal.set(input);
/* 41 */     return oldValue;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 52 */     IOUtils.close(this.inputStreamLocal.get());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 64 */     InputStream inputStream = this.inputStreamLocal.get();
/* 65 */     if (null != inputStream) {
/* 66 */       return inputStream.read();
/*    */     }
/* 68 */     return -1;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\DemuxInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */