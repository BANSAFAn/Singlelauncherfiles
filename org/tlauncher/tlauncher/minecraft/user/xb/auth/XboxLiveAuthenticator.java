/*    */ package org.tlauncher.tlauncher.minecraft.user.xb.auth;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Locale;
/*    */ import org.apache.http.client.fluent.Request;
/*    */ import org.apache.http.entity.ContentType;
/*    */ import org.apache.log4j.LogManager;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.tlauncher.tlauncher.minecraft.user.HttpClientRequester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthToken;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Requester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.oauth.OAuthApplication;
/*    */ import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthStrategy;
/*    */ import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthenticationResponse;
/*    */ 
/*    */ public class XboxLiveAuthenticator
/*    */   extends XboxServiceAuthStrategy {
/* 19 */   private static final Logger LOGGER = LogManager.getLogger(XboxLiveAuthenticator.class);
/*    */   
/*    */   public XboxLiveAuthenticator(OAuthApplication application) {
/* 22 */     super(LOGGER, (Requester)new HttpClientRequester(accessToken -> Request.Post("https://user.auth.xboxlive.com/user/authenticate").bodyString(String.format(Locale.ROOT, "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"%s\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}", new Object[] { (application.isUseWeirdXboxTokenPrefix() ? "d=" : "") + accessToken }), ContentType.APPLICATION_JSON)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   XboxLiveAuthenticator(Requester<String> requester) {
/* 29 */     super(LOGGER, requester);
/*    */   }
/*    */   
/*    */   public XboxServiceAuthenticationResponse xboxLiveAuthenticate(String accessToken) throws XboxLiveAuthenticationException, IOException {
/*    */     try {
/* 34 */       return (XboxServiceAuthenticationResponse)requestAndParse(accessToken);
/* 35 */     } catch (InvalidResponseException e) {
/* 36 */       throw new XboxLiveAuthenticationException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public XboxServiceAuthenticationResponse xboxLiveAuthenticate(MicrosoftOAuthToken token) throws XboxLiveAuthenticationException, IOException {
/* 41 */     return xboxLiveAuthenticate(token.getAccessToken());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\xb\auth\XboxLiveAuthenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */