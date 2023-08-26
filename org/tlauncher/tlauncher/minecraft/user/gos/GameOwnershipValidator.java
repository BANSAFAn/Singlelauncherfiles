/*    */ package org.tlauncher.tlauncher.minecraft.user.gos;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.apache.http.client.fluent.Request;
/*    */ import org.tlauncher.tlauncher.minecraft.user.GsonParser;
/*    */ import org.tlauncher.tlauncher.minecraft.user.InvalidResponseException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Parser;
/*    */ import org.tlauncher.tlauncher.minecraft.user.RequestAndParseStrategy;
/*    */ import org.tlauncher.tlauncher.minecraft.user.Requester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;
/*    */ 
/*    */ public class GameOwnershipValidator extends RequestAndParseStrategy<MinecraftServicesToken, MinecraftUserGameOwnershipResponse> {
/* 13 */   private static final Logger LOGGER = LogManager.getLogger(GameOwnershipValidator.class);
/*    */   
/*    */   public GameOwnershipValidator() {
/* 16 */     this((Requester<MinecraftServicesToken>)new HttpClientRequester(token -> Request.Get("https://api.minecraftservices.com/entitlements/mcstore").addHeader("Authorization", "Bearer " + token.getAccessToken())));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   GameOwnershipValidator(Requester<MinecraftServicesToken> requester) {
/* 22 */     this(requester, (Parser<MinecraftUserGameOwnershipResponse>)GsonParser.defaultParser(MinecraftUserGameOwnershipResponse.class));
/*    */   }
/*    */   
/*    */   GameOwnershipValidator(Requester<MinecraftServicesToken> requester, Parser<MinecraftUserGameOwnershipResponse> parser) {
/* 26 */     super(LOGGER, requester, parser);
/*    */   }
/*    */   
/*    */   public void checkGameOwnership(MinecraftServicesToken token) throws GameOwnershipValidationException, IOException {
/*    */     MinecraftUserGameOwnershipResponse response;
/*    */     try {
/* 32 */       response = (MinecraftUserGameOwnershipResponse)requestAndParse(token);
/* 33 */     } catch (InvalidResponseException e) {
/* 34 */       throw new GameOwnershipValidationException(e);
/*    */     } 
/* 36 */     List<MinecraftUserGameOwnershipResponse.Item> items = response.getItems();
/* 37 */     if (items.isEmpty())
/* 38 */       throw new GameOwnershipValidationException("no ownership found"); 
/* 39 */     if (items.stream().noneMatch(item -> "product_minecraft".equals(item.getName())))
/* 40 */       throw new GameOwnershipValidationException("no \"product_minecraft\""); 
/* 41 */     if (items.stream().noneMatch(item -> "game_minecraft".equals(item.getName())))
/* 42 */       throw new GameOwnershipValidationException("no \"game_minecraft\""); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\gos\GameOwnershipValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */