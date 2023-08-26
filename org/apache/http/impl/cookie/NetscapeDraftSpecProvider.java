/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.annotation.Obsolete;
/*    */ import org.apache.http.cookie.CookieSpec;
/*    */ import org.apache.http.cookie.CookieSpecProvider;
/*    */ import org.apache.http.protocol.HttpContext;
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
/*    */ @Obsolete
/*    */ @Immutable
/*    */ public class NetscapeDraftSpecProvider
/*    */   implements CookieSpecProvider
/*    */ {
/*    */   private final String[] datepatterns;
/*    */   private volatile CookieSpec cookieSpec;
/*    */   
/*    */   public NetscapeDraftSpecProvider(String[] datepatterns) {
/* 56 */     this.datepatterns = datepatterns;
/*    */   }
/*    */   
/*    */   public NetscapeDraftSpecProvider() {
/* 60 */     this(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public CookieSpec create(HttpContext context) {
/* 65 */     if (this.cookieSpec == null) {
/* 66 */       synchronized (this) {
/* 67 */         if (this.cookieSpec == null) {
/* 68 */           this.cookieSpec = new NetscapeDraftSpec(this.datepatterns);
/*    */         }
/*    */       } 
/*    */     }
/* 72 */     return this.cookieSpec;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\NetscapeDraftSpecProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */