/*    */ package org.tlauncher.tlauncher.ui.modpack;
/*    */ 
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.time.Instant;
/*    */ import java.time.LocalDateTime;
/*    */ import java.time.ZoneId;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.share.ParsedElementDTO;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ public class AddedButtonOldVersion
/*    */   extends AddedModpackStuffFrame
/*    */ {
/*    */   protected final UpdaterButton oldAdditionButton;
/*    */   protected final ModpackManager manager;
/*    */   private static final long serialVersionUID = 5115926326682859514L;
/* 33 */   private String dateTimeFormat = "dd.MM.YYYY HH:MM:ss";
/*    */   
/*    */   public AddedButtonOldVersion(JFrame parent, String title, String message1, final Long id, final GameType type) {
/* 36 */     super(parent, title, message1);
/* 37 */     final Color backgroundOldButtonColor = new Color(213, 213, 213);
/* 38 */     this.oldAdditionButton = new UpdaterButton(backgroundOldButtonColor, "explorer.button.update");
/* 39 */     this.manager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/* 40 */     this.spring.putConstraint("South", (Component)this.oldAdditionButton, 150, "North", (Component)this.message);
/* 41 */     this.spring.putConstraint("West", (Component)this.oldAdditionButton, 250, "West", this.panel);
/* 42 */     this.panel.add((Component)this.oldAdditionButton);
/* 43 */     this.oldAdditionButton.addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mouseEntered(MouseEvent e) {
/* 46 */             AddedButtonOldVersion.this.oldAdditionButton.setBackground(new Color(160, 160, 160));
/*    */           }
/*    */ 
/*    */           
/*    */           public void mouseExited(MouseEvent e) {
/* 51 */             AddedButtonOldVersion.this.oldAdditionButton.setBackground(backgroundOldButtonColor);
/*    */           }
/*    */         });
/* 54 */     this.oldAdditionButton.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 57 */             CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */               
/* 72 */               .exceptionally(t -> {
/*    */                   SwingUtilities.invokeLater(());
/*    */                   U.log(new Object[] { t });
/*    */                   return null;
/*    */                 });
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\AddedButtonOldVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */