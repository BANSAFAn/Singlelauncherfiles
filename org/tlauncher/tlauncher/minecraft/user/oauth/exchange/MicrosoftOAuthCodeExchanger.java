/*    */ package org.tlauncher.tlauncher.minecraft.user.oauth.exchange;
/*    */ import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthExchangeCode;
/*    */ import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthToken;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Parser;
/*    */ import org.tlauncher.tlauncher.minecraft.user.RequestAndParseStrategy;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Requester;
/*    */ 
/*    */ public class MicrosoftOAuthCodeExchanger extends RequestAndParseStrategy<MicrosoftOAuthExchangeCode, MicrosoftOAuthToken> {
/* 10 */   private static final Logger LOGGER = LogManager.getLogger(MicrosoftOAuthCodeExchanger.class);
/*    */   
/*    */   public MicrosoftOAuthCodeExchanger(Requester<MicrosoftOAuthExchangeCode> requester, Parser<MicrosoftOAuthToken> parser) {
/* 13 */     super(LOGGER, requester, parser);
/*    */   }
/*    */   
/*    */   public MicrosoftOAuthToken exchangeMicrosoftOAuthCode(MicrosoftOAuthExchangeCode payload) throws MicrosoftOAuthCodeExchangeException, IOException {
/*    */     try {
/* 18 */       return (MicrosoftOAuthToken)requestAndParse(payload);
/* 19 */     } catch (InvalidResponseException e) {
/* 20 */       throw new MicrosoftOAuthCodeExchangeException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\oauth\exchange\MicrosoftOAuthCodeExchanger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */