/*    */ package org.tlauncher.tlauncher.minecraft.auth;
/*    */ import java.io.IOException;
/*    */ import org.tlauncher.tlauncher.exceptions.auth.AuthenticatorException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.MicrosoftOAuthExchangeCode;
/*    */ import org.tlauncher.tlauncher.minecraft.user.MinecraftAuthenticationException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.MinecraftOAuthAuthenticate;
/*    */ import org.tlauncher.tlauncher.minecraft.user.MinecraftProfileConverter;
/*    */ import org.tlauncher.tlauncher.minecraft.user.MinecraftUser;
/*    */ import org.tlauncher.tlauncher.minecraft.user.RedirectUrl;
/*    */ import org.tlauncher.tlauncher.minecraft.user.oauth.OAuthApplication;
/*    */ import org.tlauncher.tlauncher.minecraft.user.oauth.exchange.MicrosoftOAuthCodeExchanger;
/*    */ import org.tlauncher.tlauncher.minecraft.user.oauth.exchange.MicrosoftOAuthCodeExchangerImpl;
/*    */ import org.tlauncher.tlauncher.minecraft.user.oauth.exchange.MicrosoftOAuthRefreshCodeExchangerIMpl;
/*    */ import org.tlauncher.tlauncher.minecraft.user.preq.MinecraftProfileRequester;
/*    */ import org.tlauncher.tlauncher.minecraft.user.xb.auth.XboxLiveAuthenticator;
/*    */ import org.tlauncher.tlauncher.minecraft.user.xb.xsts.XSTSAuthenticator;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class MicrosoftAuthenticator extends Authenticator {
/* 20 */   public static final OAuthApplication microsoftAuth = OAuthApplication.TLAUNCHER_PARAMETERS;
/*    */   
/*    */   public MicrosoftAuthenticator(Account account) {
/* 23 */     super(account);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void pass() throws AuthenticatorException {
/*    */     try {
/* 29 */       if (Objects.isNull(this.account.getAccessToken())) {
/*    */         
/* 31 */         MicrosoftOAuthCodeExchangerImpl microsoftOAuthCodeExchangerImpl = new MicrosoftOAuthCodeExchangerImpl(microsoftAuth.getClientId(), microsoftAuth.getTokenURL());
/* 32 */         MicrosoftOAuthExchangeCode o = new MicrosoftOAuthExchangeCode(this.account.getPassword(), new RedirectUrl(microsoftAuth.getRedirectURL()));
/* 33 */         doRequest((MicrosoftOAuthCodeExchanger)microsoftOAuthCodeExchangerImpl, o);
/* 34 */       } else if (this.account.getMicrosoftOAuthToken().isExpired()) {
/*    */         
/* 36 */         MicrosoftOAuthRefreshCodeExchangerIMpl microsoftOAuthRefreshCodeExchangerIMpl = new MicrosoftOAuthRefreshCodeExchangerIMpl(microsoftAuth.getClientId(), microsoftAuth.getTokenURL());
/*    */         
/* 38 */         MicrosoftOAuthExchangeCode o = new MicrosoftOAuthExchangeCode(this.account.getMicrosoftOAuthToken().getRefreshToken(), new RedirectUrl(microsoftAuth.getRedirectURL()));
/* 39 */         doRequest((MicrosoftOAuthCodeExchanger)microsoftOAuthRefreshCodeExchangerIMpl, o);
/*    */       } 
/* 41 */     } catch (IOException|MinecraftAuthenticationException e) {
/* 42 */       U.log(new Object[] { e });
/* 43 */       throw new AuthenticatorException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void doRequest(MicrosoftOAuthCodeExchanger microsoftOAuthCodeExchanger, MicrosoftOAuthExchangeCode payload) throws IOException, MinecraftAuthenticationException {
/* 50 */     XboxLiveAuthenticator xboxLiveAuthenticator = new XboxLiveAuthenticator(microsoftAuth);
/* 51 */     MinecraftOAuthAuthenticate minecraftOAuthAuthenticate = new MinecraftOAuthAuthenticate(microsoftOAuthCodeExchanger, xboxLiveAuthenticator, new XSTSAuthenticator(), new MinecraftServicesAuthenticator(), new GameOwnershipValidator(), new MinecraftProfileRequester(), new MinecraftProfileConverter());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 57 */     MinecraftUser user = minecraftOAuthAuthenticate.authenticate(payload);
/* 58 */     this.account.setUUID(UUIDTypeAdapter.toUUID(user.getUUID().toString()));
/* 59 */     this.account.setDisplayName(user.getDisplayName());
/* 60 */     this.account.setType(Account.AccountType.MICROSOFT);
/* 61 */     this.account.setMicrosoftOAuthToken(user.getMicrosoftToken());
/* 62 */     this.account.setMinecraftServicesToken(user.getMinecraftToken());
/* 63 */     this.account.setPassword(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\MicrosoftAuthenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */