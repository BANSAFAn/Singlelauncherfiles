/*    */ package org.tlauncher.tlauncher.ui.menu;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.Objects;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JMenuItem;
/*    */ import javax.swing.JPopupMenu;
/*    */ import org.tlauncher.modpack.domain.client.share.CategoryDTO;
/*    */ import org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer;
/*    */ import org.tlauncher.util.SwingUtil;
/*    */ import org.tlauncher.util.swing.FontTL;
/*    */ 
/*    */ public class ModpackCategoryPopupMenu
/*    */   extends JPopupMenu {
/*    */   public ModpackCategoryPopupMenu(CategoryDTO c, JLabel label) {
/* 18 */     if (Objects.isNull(c))
/*    */       return; 
/* 20 */     setBorder(BorderFactory.createLineBorder(ModpackComboxRenderer.LINE));
/* 21 */     ModpackPopup.ModpackMenuItem localizableMenuItem = new ModpackPopup.ModpackMenuItem("modpack." + c.getName());
/* 22 */     SwingUtil.changeFontFamily((JComponent)localizableMenuItem, FontTL.ROBOTO_REGULAR, 12);
/* 23 */     localizableMenuItem.setBackground(Color.WHITE);
/* 24 */     add((JMenuItem)localizableMenuItem);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\menu\ModpackCategoryPopupMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */