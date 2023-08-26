/*    */ package org.apache.http.auth.params;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.protocol.HTTP;
/*    */ import org.apache.http.util.Args;
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
/*    */ public final class AuthParams
/*    */ {
/*    */   public static String getCredentialCharset(HttpParams params) {
/* 61 */     Args.notNull(params, "HTTP parameters");
/* 62 */     String charset = (String)params.getParameter("http.auth.credential-charset");
/*    */     
/* 64 */     if (charset == null) {
/* 65 */       charset = HTTP.DEF_PROTOCOL_CHARSET.name();
/*    */     }
/* 67 */     return charset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setCredentialCharset(HttpParams params, String charset) {
/* 78 */     Args.notNull(params, "HTTP parameters");
/* 79 */     params.setParameter("http.auth.credential-charset", charset);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\auth\params\AuthParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */