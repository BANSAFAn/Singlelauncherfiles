/*    */ package org.apache.http.cookie;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.util.Args;
/*    */ import org.apache.http.util.TextUtils;
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
/*    */ @Immutable
/*    */ public final class CookieOrigin
/*    */ {
/*    */   private final String host;
/*    */   private final int port;
/*    */   private final String path;
/*    */   private final boolean secure;
/*    */   
/*    */   public CookieOrigin(String host, int port, String path, boolean secure) {
/* 51 */     Args.notBlank(host, "Host");
/* 52 */     Args.notNegative(port, "Port");
/* 53 */     Args.notNull(path, "Path");
/* 54 */     this.host = host.toLowerCase(Locale.ROOT);
/* 55 */     this.port = port;
/* 56 */     if (!TextUtils.isBlank(path)) {
/* 57 */       this.path = path;
/*    */     } else {
/* 59 */       this.path = "/";
/*    */     } 
/* 61 */     this.secure = secure;
/*    */   }
/*    */   
/*    */   public String getHost() {
/* 65 */     return this.host;
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 69 */     return this.path;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 73 */     return this.port;
/*    */   }
/*    */   
/*    */   public boolean isSecure() {
/* 77 */     return this.secure;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     StringBuilder buffer = new StringBuilder();
/* 83 */     buffer.append('[');
/* 84 */     if (this.secure) {
/* 85 */       buffer.append("(secure)");
/*    */     }
/* 87 */     buffer.append(this.host);
/* 88 */     buffer.append(':');
/* 89 */     buffer.append(Integer.toString(this.port));
/* 90 */     buffer.append(this.path);
/* 91 */     buffer.append(']');
/* 92 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\cookie\CookieOrigin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */