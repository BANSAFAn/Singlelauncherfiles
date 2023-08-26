/*    */ package org.tlauncher.tlauncher.minecraft.user.preq;
/*    */ import java.io.IOException;
/*    */ import org.apache.http.client.fluent.Request;
/*    */ import org.tlauncher.tlauncher.minecraft.user.GsonParser;
/*    */ import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Parser;
/*    */ import org.tlauncher.tlauncher.minecraft.user.RequestAndParseStrategy;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Requester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;
/*    */ 
/*    */ public class MinecraftProfileRequester extends RequestAndParseStrategy<MinecraftServicesToken, MinecraftOAuthProfile> {
/* 12 */   private static final Logger LOGGER = LogManager.getLogger(MinecraftProfileRequester.class);
/*    */   
/*    */   public MinecraftProfileRequester() {
/* 15 */     this((Requester<MinecraftServicesToken>)new HttpClientRequester(token -> Request.Get("https://api.minecraftservices.com/minecraft/profile").addHeader("Authorization", "Bearer " + token.getAccessToken())));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   MinecraftProfileRequester(Requester<MinecraftServicesToken> requester) {
/* 21 */     this(requester, (Parser<MinecraftOAuthProfile>)GsonParser.withDashlessUUIDAdapter(MinecraftOAuthProfile.class));
/*    */   }
/*    */   
/*    */   MinecraftProfileRequester(Requester<MinecraftServicesToken> requester, Parser<MinecraftOAuthProfile> parser) {
/* 25 */     super(LOGGER, requester, parser);
/*    */   }
/*    */   
/*    */   public MinecraftOAuthProfile requestProfile(MinecraftServicesToken token) throws MinecraftProfileRequestException, IOException {
/*    */     try {
/* 30 */       return (MinecraftOAuthProfile)requestAndParse(token);
/* 31 */     } catch (InvalidResponseException e) {
/* 32 */       throw new MinecraftProfileRequestException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\preq\MinecraftProfileRequester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */