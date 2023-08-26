/*    */ package org.tlauncher.tlauncher.minecraft.user.oauth.exchange;
/*    */ import org.apache.http.client.fluent.Form;
/*    */ import org.apache.http.client.fluent.Request;
/*    */ import org.tlauncher.tlauncher.minecraft.user.GsonParser;
/*    */ import org.tlauncher.tlauncher.minecraft.user.HttpClientRequester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthExchangeCode;
/*    */ import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthToken;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Parser;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Requester;
/*    */ 
/*    */ public class MicrosoftOAuthRefreshCodeExchangerIMpl extends MicrosoftOAuthCodeExchanger {
/* 12 */   private static final Logger LOGGER = LogManager.getLogger(MicrosoftOAuthRefreshCodeExchangerIMpl.class);
/*    */   
/*    */   public MicrosoftOAuthRefreshCodeExchangerIMpl(String clientId, String url) {
/* 15 */     super((Requester<MicrosoftOAuthExchangeCode>)new HttpClientRequester(code -> Request.Post(url).bodyForm(Form.form().add("client_id", clientId).add("refresh_token", code.getCode()).add("grant_type", "refresh_token").add("redirect_uri", code.getRedirectUrl().getUrl().toString()).build())), 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 24 */         (Parser<MicrosoftOAuthToken>)GsonParser.lowerCaseWithUnderscores(MicrosoftOAuthToken.class));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\oauth\exchange\MicrosoftOAuthRefreshCodeExchangerIMpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */