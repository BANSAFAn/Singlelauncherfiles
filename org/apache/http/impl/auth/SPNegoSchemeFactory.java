/*    */ package org.apache.http.impl.auth;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthScheme;
/*    */ import org.apache.http.auth.AuthSchemeFactory;
/*    */ import org.apache.http.auth.AuthSchemeProvider;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ @Immutable
/*    */ public class SPNegoSchemeFactory
/*    */   implements AuthSchemeFactory, AuthSchemeProvider
/*    */ {
/*    */   private final boolean stripPort;
/*    */   private final boolean useCanonicalHostname;
/*    */   
/*    */   public SPNegoSchemeFactory(boolean stripPort, boolean useCanonicalHostname) {
/* 54 */     this.stripPort = stripPort;
/* 55 */     this.useCanonicalHostname = useCanonicalHostname;
/*    */   }
/*    */ 
/*    */   
/*    */   public SPNegoSchemeFactory(boolean stripPort) {
/* 60 */     this.stripPort = stripPort;
/* 61 */     this.useCanonicalHostname = true;
/*    */   }
/*    */   
/*    */   public SPNegoSchemeFactory() {
/* 65 */     this(true, true);
/*    */   }
/*    */   
/*    */   public boolean isStripPort() {
/* 69 */     return this.stripPort;
/*    */   }
/*    */   
/*    */   public boolean isUseCanonicalHostname() {
/* 73 */     return this.useCanonicalHostname;
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthScheme newInstance(HttpParams params) {
/* 78 */     return (AuthScheme)new SPNegoScheme(this.stripPort, this.useCanonicalHostname);
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthScheme create(HttpContext context) {
/* 83 */     return (AuthScheme)new SPNegoScheme(this.stripPort, this.useCanonicalHostname);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\auth\SPNegoSchemeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */