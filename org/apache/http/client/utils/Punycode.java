/*    */ package org.apache.http.client.utils;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Deprecated
/*    */ @Immutable
/*    */ public class Punycode
/*    */ {
/*    */   private static final Idn impl;
/*    */   
/*    */   static {
/*    */     Idn idn;
/*    */     try {
/* 46 */       idn = new JdkIdn();
/* 47 */     } catch (Exception e) {
/* 48 */       idn = new Rfc3492Idn();
/*    */     } 
/* 50 */     impl = idn;
/*    */   }
/*    */   
/*    */   public static String toUnicode(String punycode) {
/* 54 */     return impl.toUnicode(punycode);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\clien\\utils\Punycode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */