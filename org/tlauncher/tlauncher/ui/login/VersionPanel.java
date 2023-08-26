/*    */ package org.tlauncher.tlauncher.ui.login;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Font;
/*    */ import java.awt.event.ItemListener;
/*    */ import javax.swing.SpringLayout;
/*    */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import net.minecraft.launcher.versions.Version;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.tlauncher.listeners.auth.LoginProcessListener;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.block.BlockablePanel;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
/*    */ import org.tlauncher.tlauncher.ui.swing.CheckBoxListener;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ public class VersionPanel extends BlockablePanel implements LoginProcessListener {
/*    */   public final VersionComboBox version;
/*    */   public final LocalizableCheckbox forceupdate;
/*    */   public final LoginForm loginForm;
/*    */   private boolean state;
/*    */   
/*    */   public VersionPanel(LoginForm lf) {
/* 28 */     this.loginForm = lf;
/* 29 */     this.version = new VersionComboBox(lf);
/* 30 */     ((ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class)).addGameListener(GameType.MODPACK, this.version);
/* 31 */     ((ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class)).addGameListener(GameType.MOD, this.version);
/* 32 */     SpringLayout springLayout = new SpringLayout();
/* 33 */     setLayout(springLayout);
/*    */     
/* 35 */     springLayout.putConstraint("North", (Component)this.version, 11, "North", (Component)this);
/* 36 */     springLayout.putConstraint("West", (Component)this.version, 0, "West", (Component)this);
/* 37 */     springLayout.putConstraint("South", (Component)this.version, 35, "North", (Component)this);
/* 38 */     springLayout.putConstraint("East", (Component)this.version, 0, "East", (Component)this);
/*    */     
/* 40 */     add((Component)this.version);
/*    */     
/* 42 */     this.forceupdate = new LocalizableCheckbox("loginform.checkbox.forceupdate");
/* 43 */     this.forceupdate.setForeground(Color.WHITE);
/* 44 */     this.forceupdate.setIconTextGap(14);
/* 45 */     this.forceupdate.setFocusPainted(false);
/*    */     
/* 47 */     springLayout.putConstraint("North", (Component)this.forceupdate, 44, "North", (Component)this);
/* 48 */     springLayout.putConstraint("South", (Component)this.forceupdate, -11, "South", (Component)this);
/* 49 */     springLayout.putConstraint("West", (Component)this.forceupdate, -4, "West", (Component)this);
/* 50 */     springLayout.putConstraint("East", (Component)this.forceupdate, 0, "East", (Component)this);
/* 51 */     add((Component)this.forceupdate);
/*    */     
/* 53 */     if (!OS.is(new OS[] { OS.WINDOWS })) {
/* 54 */       Font f = this.version.getFont();
/* 55 */       this.forceupdate.setFont(new Font(f.getFamily(), f.getStyle(), 11));
/*    */     } 
/*    */     
/* 58 */     this.forceupdate.addItemListener((ItemListener)new CheckBoxListener()
/*    */         {
/*    */           public void itemStateChanged(boolean newstate)
/*    */           {
/* 62 */             VersionPanel.this.state = newstate;
/* 63 */             VersionPanel.this.loginForm.play.updateState();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public void validatePreGameLaunch() throws LoginException {
/* 70 */     VersionSyncInfo syncInfo = this.loginForm.versions.getVersion();
/*    */     
/* 72 */     if (syncInfo == null) {
/*    */       return;
/*    */     }
/* 75 */     boolean supporting = syncInfo.hasRemote(), installed = syncInfo.isInstalled();
/* 76 */     Version version = syncInfo.getLocal();
/* 77 */     if (version instanceof CompleteVersion && ((CompleteVersion)version).isModpack()) {
/* 78 */       supporting = true;
/*    */     }
/*    */     
/* 81 */     if (this.state) {
/* 82 */       if (!supporting) {
/* 83 */         Alert.showLocError("forceupdate.local");
/* 84 */         throw new LoginException("Cannot update local version!");
/*    */       } 
/*    */       
/* 87 */       if (installed && !Alert.showLocQuestion("forceupdate.question"))
/* 88 */         throw new LoginException("User has cancelled force updating."); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\VersionPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */