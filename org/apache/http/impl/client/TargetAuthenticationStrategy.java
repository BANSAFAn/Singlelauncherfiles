/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Queue;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.auth.AuthScheme;
/*    */ import org.apache.http.auth.MalformedChallengeException;
/*    */ import org.apache.http.client.config.RequestConfig;
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
/*    */ @Immutable
/*    */ public class TargetAuthenticationStrategy
/*    */   extends AuthenticationStrategyImpl
/*    */ {
/* 46 */   public static final TargetAuthenticationStrategy INSTANCE = new TargetAuthenticationStrategy();
/*    */   
/*    */   public TargetAuthenticationStrategy() {
/* 49 */     super(401, "WWW-Authenticate");
/*    */   }
/*    */ 
/*    */   
/*    */   Collection<String> getPreferredAuthSchemes(RequestConfig config) {
/* 54 */     return config.getTargetPreferredAuthSchemes();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\TargetAuthenticationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */