/*     */ package org.tlauncher.tlauncher.ui.swing;
/*     */ 
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextPopup
/*     */   extends MouseAdapter
/*     */ {
/*     */   public void mouseClicked(MouseEvent e) {
/*  25 */     if (e.getModifiers() != 4) {
/*     */       return;
/*     */     }
/*  28 */     Object source = e.getSource();
/*     */     
/*  30 */     if (!(source instanceof JTextComponent)) {
/*     */       return;
/*     */     }
/*  33 */     JPopupMenu popup = getPopup(e, (JTextComponent)source);
/*     */     
/*  35 */     if (popup == null) {
/*     */       return;
/*     */     }
/*  38 */     popup.show(e.getComponent(), e.getX(), e.getY() - (popup.getSize()).height);
/*     */   }
/*     */   
/*     */   protected JPopupMenu getPopup(MouseEvent e, final JTextComponent comp) {
/*     */     Action copyAll;
/*  43 */     if (!comp.isEnabled()) {
/*  44 */       return null;
/*     */     }
/*     */     
/*  47 */     boolean isEditable = comp.isEditable();
/*  48 */     boolean isSelected = (comp.getSelectedText() != null);
/*  49 */     boolean hasValue = StringUtils.isNotEmpty(comp.getText());
/*     */     
/*  51 */     boolean pasteAvailable = (isEditable && Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).isDataFlavorSupported(DataFlavor.stringFlavor));
/*     */     
/*  53 */     JPopupMenu menu = new JPopupMenu();
/*     */ 
/*     */     
/*  56 */     Action cut = isEditable ? selectAction(comp, "cut-to-clipboard", "cut") : null;
/*  57 */     final Action copy = selectAction(comp, "copy-to-clipboard", "copy");
/*  58 */     Action paste = pasteAvailable ? selectAction(comp, "paste-from-clipboard", "paste") : null;
/*  59 */     final Action selectAll = hasValue ? selectAction(comp, "select-all", "selectAll") : null;
/*     */ 
/*     */     
/*  62 */     if (selectAll != null && copy != null) {
/*  63 */       copyAll = new EmptyAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  66 */             selectAll.actionPerformed(e);
/*  67 */             copy.actionPerformed(e);
/*     */             
/*  69 */             comp.setSelectionStart(comp.getSelectionEnd());
/*     */           }
/*     */         };
/*     */     } else {
/*  73 */       copyAll = null;
/*     */     } 
/*  75 */     if (cut != null) {
/*  76 */       menu.add(cut).setText(Localizable.get("popup.cut"));
/*     */     }
/*  78 */     if (isSelected && copy != null) {
/*  79 */       menu.add(copy).setText(Localizable.get("popup.copy"));
/*     */     }
/*  81 */     if (paste != null) {
/*  82 */       menu.add(paste).setText(Localizable.get("popup.paste"));
/*     */     }
/*  84 */     if (selectAll != null) {
/*     */       
/*  86 */       if (menu
/*  87 */         .getComponentCount() > 0 && 
/*     */         
/*  89 */         !(menu.getComponent(menu.getComponentCount() - 1) instanceof JPopupMenu.Separator))
/*     */       {
/*  91 */         menu.addSeparator();
/*     */       }
/*  93 */       menu.add(selectAll).setText(Localizable.get("popup.selectall"));
/*     */     } 
/*     */     
/*  96 */     if (copyAll != null) {
/*  97 */       menu.add(copyAll).setText(Localizable.get("popup.copyall"));
/*     */     }
/*  99 */     if (menu.getComponentCount() == 0) {
/* 100 */       return null;
/*     */     }
/* 102 */     if (menu.getComponent(0) instanceof JPopupMenu.Separator) {
/* 103 */       menu.remove(0);
/*     */     }
/* 105 */     if (menu.getComponent(menu.getComponentCount() - 1) instanceof JPopupMenu.Separator) {
/* 106 */       menu.remove(menu.getComponentCount() - 1);
/*     */     }
/* 108 */     return menu;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Action selectAction(JTextComponent comp, String general, String fallback) {
/* 115 */     Action action = comp.getActionMap().get(general);
/*     */     
/* 117 */     if (action == null) {
/* 118 */       action = comp.getActionMap().get(fallback);
/*     */     }
/* 120 */     return action;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\TextPopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */