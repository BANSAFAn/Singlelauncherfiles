/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.security.Principal;
/*    */ import javax.net.ssl.SSLSession;
/*    */ import org.apache.http.HttpConnection;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthScheme;
/*    */ import org.apache.http.auth.AuthState;
/*    */ import org.apache.http.auth.Credentials;
/*    */ import org.apache.http.client.UserTokenHandler;
/*    */ import org.apache.http.client.protocol.HttpClientContext;
/*    */ import org.apache.http.conn.ManagedHttpClientConnection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class DefaultUserTokenHandler
/*    */   implements UserTokenHandler
/*    */ {
/* 60 */   public static final DefaultUserTokenHandler INSTANCE = new DefaultUserTokenHandler();
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getUserToken(HttpContext context) {
/* 65 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*    */     
/* 67 */     Principal userPrincipal = null;
/*    */     
/* 69 */     AuthState targetAuthState = clientContext.getTargetAuthState();
/* 70 */     if (targetAuthState != null) {
/* 71 */       userPrincipal = getAuthPrincipal(targetAuthState);
/* 72 */       if (userPrincipal == null) {
/* 73 */         AuthState proxyAuthState = clientContext.getProxyAuthState();
/* 74 */         userPrincipal = getAuthPrincipal(proxyAuthState);
/*    */       } 
/*    */     } 
/*    */     
/* 78 */     if (userPrincipal == null) {
/* 79 */       HttpConnection conn = clientContext.getConnection();
/* 80 */       if (conn.isOpen() && conn instanceof ManagedHttpClientConnection) {
/* 81 */         SSLSession sslsession = ((ManagedHttpClientConnection)conn).getSSLSession();
/* 82 */         if (sslsession != null) {
/* 83 */           userPrincipal = sslsession.getLocalPrincipal();
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 88 */     return userPrincipal;
/*    */   }
/*    */   
/*    */   private static Principal getAuthPrincipal(AuthState authState) {
/* 92 */     AuthScheme scheme = authState.getAuthScheme();
/* 93 */     if (scheme != null && scheme.isComplete() && scheme.isConnectionBased()) {
/* 94 */       Credentials creds = authState.getCredentials();
/* 95 */       if (creds != null) {
/* 96 */         return creds.getUserPrincipal();
/*    */       }
/*    */     } 
/* 99 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\DefaultUserTokenHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */