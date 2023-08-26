/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.gos.GameOwnershipValidator;
/*    */ import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesAuthenticator;
/*    */ import org.tlauncher.tlauncher.minecraft.user.mcsauth.MinecraftServicesToken;
/*    */ import org.tlauncher.tlauncher.minecraft.user.oauth.exchange.MicrosoftOAuthCodeExchanger;
/*    */ import org.tlauncher.tlauncher.minecraft.user.preq.MinecraftOAuthProfile;
/*    */ import org.tlauncher.tlauncher.minecraft.user.preq.MinecraftProfileRequester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.xb.XboxServiceAuthenticationResponse;
/*    */ import org.tlauncher.tlauncher.minecraft.user.xb.auth.XboxLiveAuthenticator;
/*    */ import org.tlauncher.tlauncher.minecraft.user.xb.xsts.XSTSAuthenticator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MinecraftOAuthAuthenticate
/*    */ {
/*    */   private final MicrosoftOAuthCodeExchanger microsoftOAuthCodeExchanger;
/*    */   private final XboxLiveAuthenticator xboxLiveAuthenticator;
/*    */   private final XSTSAuthenticator xstsAuthenticator;
/*    */   private final MinecraftServicesAuthenticator minecraftServicesAuthenticator;
/*    */   private final GameOwnershipValidator gameOwnershipValidator;
/*    */   private final MinecraftProfileRequester minecraftProfileRequester;
/*    */   private final MinecraftProfileConverter minecraftProfileConverter;
/*    */   
/*    */   public MinecraftOAuthAuthenticate(MicrosoftOAuthCodeExchanger microsoftOAuthCodeExchanger, XboxLiveAuthenticator xboxLiveAuthenticator, XSTSAuthenticator xstsAuthenticator, MinecraftServicesAuthenticator minecraftServicesAuthenticator, GameOwnershipValidator gameOwnershipValidator, MinecraftProfileRequester minecraftProfileRequester, MinecraftProfileConverter minecraftProfileConverter) {
/* 33 */     this.microsoftOAuthCodeExchanger = microsoftOAuthCodeExchanger;
/* 34 */     this.xboxLiveAuthenticator = xboxLiveAuthenticator;
/* 35 */     this.xstsAuthenticator = xstsAuthenticator;
/* 36 */     this.minecraftServicesAuthenticator = minecraftServicesAuthenticator;
/* 37 */     this.gameOwnershipValidator = gameOwnershipValidator;
/* 38 */     this.minecraftProfileRequester = minecraftProfileRequester;
/* 39 */     this.minecraftProfileConverter = minecraftProfileConverter;
/*    */   }
/*    */   
/*    */   public MinecraftUser authenticate(MicrosoftOAuthExchangeCode code) throws MinecraftAuthenticationException, IOException {
/* 43 */     MicrosoftOAuthToken oaex = this.microsoftOAuthCodeExchanger.exchangeMicrosoftOAuthCode(code);
/* 44 */     XboxServiceAuthenticationResponse xbAuth = this.xboxLiveAuthenticator.xboxLiveAuthenticate(oaex.getAccessToken());
/* 45 */     XboxServiceAuthenticationResponse xbXsts = this.xstsAuthenticator.xstsAuthenticate(xbAuth.getToken());
/* 46 */     MinecraftServicesToken mcsToken = this.minecraftServicesAuthenticator.minecraftServicesAuthenticate(xbXsts);
/* 47 */     this.gameOwnershipValidator.checkGameOwnership(mcsToken);
/* 48 */     MinecraftOAuthProfile preq = this.minecraftProfileRequester.requestProfile(mcsToken);
/* 49 */     return this.minecraftProfileConverter.convertToMinecraftUser(oaex, mcsToken, preq);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\MinecraftOAuthAuthenticate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */