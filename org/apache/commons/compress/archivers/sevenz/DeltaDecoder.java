/*    */ package org.apache.commons.compress.archivers.sevenz;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import org.tukaani.xz.DeltaOptions;
/*    */ import org.tukaani.xz.FinishableOutputStream;
/*    */ import org.tukaani.xz.FinishableWrapperOutputStream;
/*    */ import org.tukaani.xz.UnsupportedOptionsException;
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
/*    */ class DeltaDecoder
/*    */   extends CoderBase
/*    */ {
/*    */   DeltaDecoder() {
/* 30 */     super(new Class[] { Number.class });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
/* 36 */     return (new DeltaOptions(getOptionsFromCoder(coder))).getInputStream(in);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   OutputStream encode(OutputStream out, Object options) throws IOException {
/* 42 */     int distance = numberOptionOrDefault(options, 1);
/*    */     try {
/* 44 */       return (OutputStream)(new DeltaOptions(distance)).getOutputStream((FinishableOutputStream)new FinishableWrapperOutputStream(out));
/* 45 */     } catch (UnsupportedOptionsException ex) {
/* 46 */       throw new IOException(ex.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   byte[] getOptionsAsProperties(Object options) {
/* 52 */     return new byte[] {
/* 53 */         (byte)(numberOptionOrDefault(options, 1) - 1)
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   Object getOptionsFromCoder(Coder coder, InputStream in) {
/* 59 */     return Integer.valueOf(getOptionsFromCoder(coder));
/*    */   }
/*    */   
/*    */   private int getOptionsFromCoder(Coder coder) {
/* 63 */     if (coder.properties == null || coder.properties.length == 0) {
/* 64 */       return 1;
/*    */     }
/* 66 */     return (0xFF & coder.properties[0]) + 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\sevenz\DeltaDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */