/*    */ package org.tlauncher.tlauncher.ui.loc.modpack;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.awt.CardLayout;
/*    */ import java.awt.Container;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.JButton;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*    */ import org.tlauncher.tlauncher.ui.listener.BlockClickListener;
/*    */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*    */ 
/*    */ public abstract class ModpackActButton extends ExtendedPanel implements BlockClickListener {
/*    */   public static final String INSTALL = "INSTALL";
/*    */   public static final String REMOVE = "REMOVE";
/*    */   public static final String PROCESSING = "PROCESSING";
/*    */   public static final String DENIED_OPERATION = "DENIED";
/*    */   protected JButton installButton;
/*    */   protected JButton removeButton;
/*    */   protected JButton processButton;
/*    */   protected String last;
/*    */   protected ModpackComboBox localmodpacks;
/*    */   protected boolean firstDeniedState;
/*    */   protected GameType type;
/*    */   protected GameEntityDTO entity;
/*    */   
/*    */   public ModpackActButton(GameEntityDTO entity, GameType type, ModpackComboBox localmodpacks) {
/* 32 */     this.localmodpacks = localmodpacks;
/* 33 */     this.type = type;
/* 34 */     this.entity = entity;
/* 35 */     setLayout(new CardLayout(0, 0));
/*    */   }
/*    */   
/*    */   protected List<? extends GameEntityDTO> getSelectedModpackData() {
/* 39 */     if (this.localmodpacks.getSelectedIndex() > 0 && this.type != GameType.MODPACK)
/* 40 */       return Lists.newArrayList(((ModpackVersionDTO)((CompleteVersion)this.localmodpacks
/* 41 */           .getSelectedValue()).getModpack().getVersion()).getByType(this.type)); 
/* 42 */     if (this.type == GameType.MODPACK) {
/* 43 */       return Lists.newArrayList(this.localmodpacks.getModpacks());
/*    */     }
/* 45 */     return new ArrayList<>();
/*    */   }
/*    */   
/*    */   public void setTypeButton(String name) {
/* 49 */     if (this.firstDeniedState && "INSTALL".equals(name)) {
/* 50 */       name = "DENIED";
/*    */     }
/* 52 */     if (!name.equals("PROCESSING"))
/* 53 */       this.last = name; 
/* 54 */     ((CardLayout)getLayout()).show((Container)this, name);
/*    */   }
/*    */   
/*    */   public abstract void initButton();
/*    */   
/*    */   public void reset() {
/* 60 */     setTypeButton(this.last);
/*    */   }
/*    */   
/*    */   public GameEntityDTO getEntity() {
/* 64 */     return this.entity;
/*    */   }
/*    */   
/*    */   public GameType getType() {
/* 68 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\modpack\ModpackActButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */