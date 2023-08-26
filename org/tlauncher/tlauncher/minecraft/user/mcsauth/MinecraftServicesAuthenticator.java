/*    */ package org.tlauncher.tlauncher.minecraft.user.mcsauth;
/*    */ import java.util.Locale;
/*    */ import org.apache.http.client.fluent.Request;
/*    */ import org.apache.http.entity.ContentType;
/*    */ import org.apache.log4j.LogManager;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.tlauncher.tlauncher.minecraft.user.HttpClientRequester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Parser;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Requester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthenticationResponse;
/*    */ 
/*    */ public class MinecraftServicesAuthenticator extends RequestAndParseStrategy<XboxServiceAuthenticationResponse, MinecraftServicesToken> {
/* 14 */   private static final Logger LOGGER = LogManager.getLogger(MinecraftServicesAuthenticator.class);
/*    */   
/*    */   public MinecraftServicesAuthenticator() {
/* 17 */     this((Requester<XboxServiceAuthenticationResponse>)new HttpClientRequester(r -> Request.Post("https://api.minecraftservices.com/authentication/login_with_xbox").bodyString(String.format(Locale.ROOT, "{\"identityToken\":\"XBL3.0 x=%s;%s\"}", new Object[] { r.getUHS(), r.getToken() }), ContentType.APPLICATION_JSON)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   MinecraftServicesAuthenticator(Requester<XboxServiceAuthenticationResponse> requester) {
/* 24 */     this(requester, (Parser<MinecraftServicesToken>)GsonParser.lowerCaseWithUnderscores(MinecraftServicesToken.class));
/*    */   }
/*    */   
/*    */   MinecraftServicesAuthenticator(Requester<XboxServiceAuthenticationResponse> requester, Parser<MinecraftServicesToken> parser) {
/* 28 */     super(LOGGER, requester, parser);
/*    */   }
/*    */   
/*    */   public MinecraftServicesToken minecraftServicesAuthenticate(XboxServiceAuthenticationResponse xstsResponse) throws MinecraftServicesAuthenticationException, IOException {
/*    */     try {
/* 33 */       return (MinecraftServicesToken)requestAndParse(xstsResponse);
/* 34 */     } catch (InvalidResponseException e) {
/* 35 */       throw new MinecraftServicesAuthenticationException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\mcsauth\MinecraftServicesAuthenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */