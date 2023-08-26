/*    */ package by.gdev.http.download.model;
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
/*    */ public enum Headers
/*    */ {
/* 14 */   SHA1("SHA-1"),
/* 15 */   ETAG("ETag"),
/* 16 */   CONTENTLENGTH("Content-Length"),
/* 17 */   LASTMODIFIED("Last-Modified");
/*    */   public String getValue() {
/* 19 */     return this.value;
/*    */   }
/*    */   
/*    */   Headers(String value) {
/*    */     this.value = value;
/*    */   }
/*    */   
/*    */   private final String value;
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\model\Headers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */