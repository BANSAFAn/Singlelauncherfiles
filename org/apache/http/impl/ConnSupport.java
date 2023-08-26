/*    */ package org.apache.http.impl;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetDecoder;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ import java.nio.charset.CodingErrorAction;
/*    */ import org.apache.http.config.ConnectionConfig;
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
/*    */ public final class ConnSupport
/*    */ {
/*    */   public static CharsetDecoder createDecoder(ConnectionConfig cconfig) {
/* 44 */     if (cconfig == null) {
/* 45 */       return null;
/*    */     }
/* 47 */     Charset charset = cconfig.getCharset();
/* 48 */     CodingErrorAction malformed = cconfig.getMalformedInputAction();
/* 49 */     CodingErrorAction unmappable = cconfig.getUnmappableInputAction();
/* 50 */     if (charset != null) {
/* 51 */       return charset.newDecoder().onMalformedInput((malformed != null) ? malformed : CodingErrorAction.REPORT).onUnmappableCharacter((unmappable != null) ? unmappable : CodingErrorAction.REPORT);
/*    */     }
/*    */ 
/*    */     
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public static CharsetEncoder createEncoder(ConnectionConfig cconfig) {
/* 60 */     if (cconfig == null) {
/* 61 */       return null;
/*    */     }
/* 63 */     Charset charset = cconfig.getCharset();
/* 64 */     if (charset != null) {
/* 65 */       CodingErrorAction malformed = cconfig.getMalformedInputAction();
/* 66 */       CodingErrorAction unmappable = cconfig.getUnmappableInputAction();
/* 67 */       return charset.newEncoder().onMalformedInput((malformed != null) ? malformed : CodingErrorAction.REPORT).onUnmappableCharacter((unmappable != null) ? unmappable : CodingErrorAction.REPORT);
/*    */     } 
/*    */ 
/*    */     
/* 71 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\ConnSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */