/*     */ package org.tlauncher.tlauncher.ui.swing;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import javax.swing.DefaultListCellRenderer;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import net.minecraft.launcher.updater.LatestVersionSyncInfo;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import net.minecraft.launcher.versions.ReleaseType;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class VersionCellRenderer implements ListCellRenderer<VersionSyncInfo> {
/*  17 */   public static final VersionSyncInfo LOADING = VersionSyncInfo.createEmpty(); public static final VersionSyncInfo EMPTY = VersionSyncInfo.createEmpty();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  23 */   public static final Color DARK_COLOR_TEXT = new Color(77, 77, 77);
/*  24 */   public static final Color OVER_ITEM = new Color(235, 235, 235);
/*     */   
/*  26 */   private static final Icon TLAUNCHER_ICON = (Icon)ImageCache.getIcon("tlauncher-user.png");
/*  27 */   private static final Icon TLAUNCHER_USER_ICON_GRAY = (Icon)ImageCache.getIcon("tlauncher-user-gray.png");
/*     */ 
/*     */   
/*  30 */   private final DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
/*  31 */   private final int averageColor = (new Color(128, 128, 128, 255)).getRGB();
/*     */   
/*     */   public static String getLabelFor(VersionSyncInfo value) {
/*     */     String id;
/*  35 */     LatestVersionSyncInfo asLatest = (value instanceof LatestVersionSyncInfo) ? (LatestVersionSyncInfo)value : null;
/*     */ 
/*     */     
/*  38 */     ReleaseType type = value.getAvailableVersion().getReleaseType();
/*     */ 
/*     */     
/*  41 */     if (asLatest == null) {
/*  42 */       id = value.getID();
/*  43 */       label = "version." + type;
/*     */     } else {
/*  45 */       id = asLatest.getVersionID();
/*  46 */       label = "version.latest." + type;
/*     */     } 
/*     */     
/*  49 */     String label = Localizable.nget(label);
/*  50 */     if (type != null) {
/*  51 */       switch (type) {
/*     */         case OLD_ALPHA:
/*  53 */           id = id.startsWith("a") ? id.substring(1) : id;
/*     */           break;
/*     */         case OLD_BETA:
/*  56 */           id = id.substring(1);
/*     */           break;
/*     */       } 
/*     */ 
/*     */     
/*     */     }
/*  62 */     return (label != null) ? (label + " " + id) : id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Component getListCellRendererComponent(JList<? extends VersionSyncInfo> list, VersionSyncInfo value, int index, boolean isSelected, boolean cellHasFocus) {
/*  70 */     JLabel mainText = (JLabel)this.defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/*     */ 
/*     */     
/*  73 */     mainText.setAlignmentY(0.5F);
/*  74 */     mainText.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));
/*  75 */     mainText.setOpaque(true);
/*  76 */     if (isSelected) {
/*  77 */       mainText.setBackground(OVER_ITEM);
/*     */     } else {
/*  79 */       mainText.setBackground(Color.white);
/*     */     } 
/*  81 */     mainText.setForeground(DARK_COLOR_TEXT);
/*     */     
/*  83 */     if (value == null) {
/*  84 */       mainText.setText("(null)");
/*  85 */       return mainText;
/*     */     } 
/*     */     
/*  88 */     if (value.equals(LOADING)) {
/*  89 */       mainText.setText(Localizable.get("versions.loading"));
/*  90 */     } else if (value.equals(EMPTY)) {
/*  91 */       mainText.setText(Localizable.get("versions.notfound.tip"));
/*     */     } else {
/*     */       
/*  94 */       mainText.setText(getLabelFor(value));
/*  95 */       if (!value.isInstalled())
/*  96 */         mainText.setBackground(U.shiftColor(mainText.getBackground(), 
/*  97 */               (mainText.getBackground().getRGB() < this.averageColor) ? 32 : -32)); 
/*     */     } 
/*  99 */     if (Objects.nonNull(value.getAvailableVersion())) {
/* 100 */       boolean skin = TLauncher.getInstance().getTLauncherManager().useTLauncherAccount(value.getAvailableVersion());
/* 101 */       boolean checkbox = TLauncher.getInstance().getConfiguration().getBoolean("skin.status.checkbox.state");
/* 102 */       if (skin && !checkbox) {
/* 103 */         mainText.setIcon(TLAUNCHER_USER_ICON_GRAY);
/* 104 */       } else if (skin) {
/* 105 */         mainText.setIcon(TLAUNCHER_ICON);
/*     */       } 
/*     */     } 
/* 108 */     return mainText;
/*     */   }
/*     */   
/*     */   public boolean getShowTLauncherVersions() {
/* 112 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\VersionCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */