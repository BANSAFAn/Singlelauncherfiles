/*     */ package org.tlauncher.tlauncher.ui.swing;
/*     */ import java.awt.Color;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ 
/*     */ public class AccountCellRenderer implements ListCellRenderer<Account> {
/*  11 */   public static final Account EMPTY = Account.randomAccount(); public static final Account MANAGE = Account.randomAccount();
/*  12 */   public static final Color DARK_COLOR_TEXT = new Color(77, 77, 77);
/*  13 */   public static final Color OVER_ITEM = new Color(235, 235, 235);
/*  14 */   private static final Icon MANAGE_ICON = (Icon)ImageCache.getIcon("gear.png");
/*  15 */   private static final Icon MOJANG_USER_ICON = (Icon)ImageCache.getIcon("mojang-user.png");
/*  16 */   private static final Icon TLAUNCHER_USER_ICON = (Icon)ImageCache.getIcon("tlauncher-user.png");
/*  17 */   private static final Icon MICROSOFT_USER_ICON = (Icon)ImageCache.getIcon("microsoft-user.png");
/*  18 */   private static final Icon FREE_USER_ICON = (Icon)ImageCache.getIcon("free-user.png");
/*  19 */   private static final Color FOREGROUND_EDITOR = new Color(74, 74, 73);
/*     */   private final DefaultListCellRenderer defaultRenderer;
/*     */   private AccountCellType type;
/*     */   
/*     */   public AccountCellRenderer(AccountCellType type) {
/*  24 */     if (type == null) {
/*  25 */       throw new NullPointerException("CellType cannot be NULL!");
/*     */     }
/*  27 */     this.defaultRenderer = new DefaultListCellRenderer();
/*  28 */     this.type = type;
/*     */   }
/*     */   
/*     */   public AccountCellRenderer() {
/*  32 */     this(AccountCellType.PREVIEW);
/*     */   }
/*     */   
/*     */   public AccountCellType getType() {
/*  36 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(AccountCellType type) {
/*  40 */     if (type == null) {
/*  41 */       throw new NullPointerException("CellType cannot be NULL!");
/*     */     }
/*  43 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Component getListCellRendererComponent(JList<? extends Account> list, Account value, int index, boolean isSelected, boolean cellHasFocus) {
/*  51 */     JLabel renderer = (JLabel)this.defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
/*     */     
/*  53 */     if (isSelected) {
/*  54 */       renderer.setBackground(OVER_ITEM);
/*     */     } else {
/*  56 */       renderer.setBackground(Color.white);
/*     */     } 
/*  58 */     renderer.setForeground(DARK_COLOR_TEXT);
/*     */     
/*  60 */     renderer.setAlignmentY(0.5F);
/*  61 */     renderer.setIconTextGap(7);
/*     */     
/*  63 */     if (value == null || value.equals(EMPTY))
/*  64 */     { renderer.setText(Localizable.get("account.empty")); }
/*  65 */     else if (value.equals(MANAGE))
/*  66 */     { renderer.setText(Localizable.get("account.manage"));
/*  67 */       renderer.setIcon(MANAGE_ICON); }
/*     */     else
/*     */     
/*  70 */     { Icon icon = null;
/*     */       
/*  72 */       switch (value.getType()) {
/*     */         case EDITOR:
/*  74 */           icon = TLAUNCHER_USER_ICON;
/*     */           break;
/*     */         case null:
/*  77 */           icon = MOJANG_USER_ICON;
/*     */           break;
/*     */         case null:
/*  80 */           icon = MICROSOFT_USER_ICON;
/*     */           break;
/*     */         case null:
/*  83 */           icon = FREE_USER_ICON;
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/*  88 */       if (icon != null) {
/*  89 */         renderer.setIcon(icon);
/*  90 */         renderer.setFont(renderer.getFont().deriveFont(1));
/*     */       } 
/*     */       
/*  93 */       switch (this.type)
/*     */       { case EDITOR:
/*  95 */           configEditLabel(renderer, isSelected);
/*  96 */           if (!value.hasUsername()) {
/*  97 */             renderer.setText(Localizable.get("account.creating"));
/*     */           } else {
/*  99 */             renderer.setText(value.getDisplayName());
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 108 */           renderer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0));
/* 109 */           renderer.setOpaque(true);
/* 110 */           return renderer; }  configEditLabel(renderer, isSelected); renderer.setText(value.getDisplayName()); }  renderer.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 0)); renderer.setOpaque(true); return renderer;
/*     */   }
/*     */   
/*     */   private void configEditLabel(JLabel renderer, boolean isSelected) {
/* 114 */     renderer.setFont(renderer.getFont().deriveFont(0, 12.0F));
/* 115 */     renderer.setForeground(FOREGROUND_EDITOR);
/* 116 */     renderer.setBorder(BorderFactory.createEmptyBorder(1, 0, 1, 0));
/* 117 */     if (isSelected)
/* 118 */       renderer.setBackground(new Color(235, 235, 235)); 
/*     */   }
/*     */   
/*     */   public enum AccountCellType
/*     */   {
/* 123 */     PREVIEW, EDITOR;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\AccountCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */