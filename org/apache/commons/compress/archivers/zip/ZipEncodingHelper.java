/*    */ package org.apache.commons.compress.archivers.zip;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.charset.UnsupportedCharsetException;
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
/*    */ public abstract class ZipEncodingHelper
/*    */ {
/*    */   static final String UTF8 = "UTF8";
/* 41 */   static final ZipEncoding UTF8_ZIP_ENCODING = getZipEncoding("UTF8");
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
/*    */   public static ZipEncoding getZipEncoding(String name) {
/* 56 */     Charset cs = Charset.defaultCharset();
/* 57 */     if (name != null) {
/*    */       try {
/* 59 */         cs = Charset.forName(name);
/* 60 */       } catch (UnsupportedCharsetException unsupportedCharsetException) {}
/*    */     }
/*    */     
/* 63 */     boolean useReplacement = isUTF8(cs.name());
/* 64 */     return new NioZipEncoding(cs, useReplacement);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static boolean isUTF8(String charsetName) {
/* 73 */     String actual = (charsetName != null) ? charsetName : Charset.defaultCharset().name();
/* 74 */     if (StandardCharsets.UTF_8.name().equalsIgnoreCase(actual)) {
/* 75 */       return true;
/*    */     }
/* 77 */     return StandardCharsets.UTF_8.aliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(actual));
/*    */   }
/*    */   
/*    */   static ByteBuffer growBufferBy(ByteBuffer buffer, int increment) {
/* 81 */     buffer.limit(buffer.position());
/* 82 */     buffer.rewind();
/*    */     
/* 84 */     ByteBuffer on = ByteBuffer.allocate(buffer.capacity() + increment);
/*    */     
/* 86 */     on.put(buffer);
/* 87 */     return on;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\ZipEncodingHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */