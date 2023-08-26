/*    */ package org.tlauncher.tlauncher.minecraft.user.xb.xsts;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import java.io.IOException;
/*    */ import java.util.Locale;
/*    */ import org.apache.http.client.fluent.Request;
/*    */ import org.apache.http.entity.ContentType;
/*    */ import org.apache.log4j.LogManager;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.tlauncher.tlauncher.minecraft.user.HttpClientRequester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.InvalidStatusCodeException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Requester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthStrategy;
/*    */ import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthenticationResponse;
/*    */ 
/*    */ public class XSTSAuthenticator
/*    */   extends XboxServiceAuthStrategy {
/* 20 */   private static final Logger LOGGER = LogManager.getLogger(XSTSAuthenticator.class);
/*    */   
/*    */   public XSTSAuthenticator() {
/* 23 */     super(LOGGER, (Requester)new HttpClientRequester(xboxLiveToken -> Request.Post("https://xsts.auth.xboxlive.com/xsts/authorize").bodyString(String.format(Locale.ROOT, "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"%s\"]},\"RelyingParty\": \"rp://api.minecraftservices.com/\",\"TokenType\": \"JWT\"}", new Object[] { xboxLiveToken }), ContentType.APPLICATION_JSON)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   XSTSAuthenticator(Requester<String> requester) {
/* 30 */     super(LOGGER, requester);
/*    */   }
/*    */   
/*    */   private static XSTSAuthenticationException parseXErr(JsonObject response) {
/* 34 */     if (response.has("XErr")) {
/* 35 */       JsonElement xErr = response.get("XErr");
/* 36 */       if (xErr instanceof com.google.gson.JsonPrimitive) {
/* 37 */         String code = xErr.getAsString();
/* 38 */         switch (code) {
/*    */           case "2148916233":
/* 40 */             return new NoXboxAccountException();
/*    */           case "2148916238":
/* 42 */             return new ChildAccountException();
/*    */         } 
/*    */       } 
/*    */     } 
/* 46 */     return null;
/*    */   }
/*    */   
/*    */   public XboxServiceAuthenticationResponse xstsAuthenticate(String xboxLiveToken) throws XSTSAuthenticationException, IOException {
/*    */     try {
/* 51 */       return (XboxServiceAuthenticationResponse)requestAndParse(xboxLiveToken);
/* 52 */     } catch (InvalidResponseException e) {
/* 53 */       if (e instanceof InvalidStatusCodeException && ((InvalidStatusCodeException)e)
/* 54 */         .getStatusCode() == 401) {
/* 55 */         JsonObject response = e.getResponseAsJson();
/* 56 */         if (response != null) {
/* 57 */           XSTSAuthenticationException e1 = parseXErr(response);
/* 58 */           if (e1 != null)
/* 59 */             throw e1; 
/*    */         } 
/*    */       } 
/* 62 */       throw new XSTSAuthenticationException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\xb\xsts\XSTSAuthenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */