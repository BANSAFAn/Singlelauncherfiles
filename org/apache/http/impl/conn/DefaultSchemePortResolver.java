/*    */ package org.apache.http.impl.conn;
/*    */ 
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.conn.SchemePortResolver;
/*    */ import org.apache.http.conn.UnsupportedSchemeException;
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
/*    */ @Immutable
/*    */ public class DefaultSchemePortResolver
/*    */   implements SchemePortResolver
/*    */ {
/* 43 */   public static final DefaultSchemePortResolver INSTANCE = new DefaultSchemePortResolver();
/*    */ 
/*    */   
/*    */   public int resolve(HttpHost host) throws UnsupportedSchemeException {
/* 47 */     Args.notNull(host, "HTTP host");
/* 48 */     int port = host.getPort();
/* 49 */     if (port > 0) {
/* 50 */       return port;
/*    */     }
/* 52 */     String name = host.getSchemeName();
/* 53 */     if (name.equalsIgnoreCase("http"))
/* 54 */       return 80; 
/* 55 */     if (name.equalsIgnoreCase("https")) {
/* 56 */       return 443;
/*    */     }
/* 58 */     throw new UnsupportedSchemeException(name + " protocol is not supported");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\DefaultSchemePortResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */