/*    */ package org.apache.commons.io;
/*    */ 
/*    */ import java.nio.charset.Charset;
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
/*    */ public enum StandardLineSeparator
/*    */ {
/* 33 */   CR("\r"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   CRLF("\r\n"),
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 43 */   LF("\n");
/*    */ 
/*    */ 
/*    */   
/*    */   private final String lineSeparator;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   StandardLineSeparator(String lineSeparator) {
/* 53 */     this.lineSeparator = Objects.<String>requireNonNull(lineSeparator, "lineSeparator");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] getBytes(Charset charset) {
/* 63 */     return this.lineSeparator.getBytes(charset);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getString() {
/* 72 */     return this.lineSeparator;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\StandardLineSeparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */