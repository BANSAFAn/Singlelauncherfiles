/*    */ package org.tlauncher.tlauncher.ui.versions;
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.util.Iterator;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JList;
/*    */ import javax.swing.border.Border;
/*    */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageIcon;
/*    */ import org.tlauncher.tlauncher.ui.swing.VersionCellRenderer;
/*    */ import org.tlauncher.tlauncher.ui.swing.border.VersionBorder;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ import org.tlauncher.util.swing.FontTL;
/*    */ 
/*    */ public class VersionListCellRenderer extends VersionCellRenderer {
/*    */   private final VersionHandler handler;
/* 18 */   private static final Color FOREGROUND_COLOR = new Color(76, 75, 74); private final ImageIcon downloading;
/* 19 */   private static final Color BACKGROUND_COLOR = new Color(235, 235, 235);
/*    */   
/*    */   VersionListCellRenderer(VersionList list) {
/* 22 */     this.handler = list.handler;
/*    */     
/* 24 */     this.downloading = ImageCache.getIcon("down.png", 16, 16);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Component getListCellRendererComponent(JList<? extends VersionSyncInfo> list, VersionSyncInfo value, int index, boolean isSelected, boolean cellHasFocus) {
/* 31 */     if (value == null)
/* 32 */       return null; 
/* 33 */     JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/* 34 */     if (!isSelected) {
/* 35 */       if (value.isInstalled()) {
/* 36 */         label.setBackground(Color.WHITE);
/*    */       } else {
/* 38 */         label.setBackground(BACKGROUND_COLOR);
/*    */       } 
/*    */     }
/* 41 */     SwingUtil.changeFontFamily(label, FontTL.ROBOTO_BOLD, 14, FOREGROUND_COLOR);
/* 42 */     label.setBorder((Border)new VersionBorder(10, 20, 10, 0, VersionBorder.SEPARATOR_COLOR));
/* 43 */     if (value.isInstalled() && !value.isUpToDate()) {
/* 44 */       label.setText(label.getText() + ' ' + Localizable.get("version.list.needsupdate"));
/*    */     }
/* 46 */     if (this.handler.downloading != null) {
/* 47 */       Iterator<VersionSyncInfo> iterator = this.handler.downloading.iterator(); if (iterator.hasNext()) { VersionSyncInfo compare = iterator.next();
/* 48 */         ImageIcon icon = compare.equals(value) ? this.downloading : null;
/*    */         
/* 50 */         label.setIcon((Icon)icon);
/* 51 */         label.setDisabledIcon((Icon)icon); }
/*    */     
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 57 */     return label;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\versions\VersionListCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */