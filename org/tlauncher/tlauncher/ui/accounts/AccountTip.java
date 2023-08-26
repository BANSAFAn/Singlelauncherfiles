/*     */ package org.tlauncher.tlauncher.ui.accounts;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*     */ import org.tlauncher.tlauncher.ui.scenes.AccountEditorScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.editor.EditorPane;
/*     */ import org.tlauncher.util.OS;
/*     */ 
/*     */ public class AccountTip extends CenterPanel implements LocalizableComponent {
/*     */   public static final int WIDTH = 510;
/*     */   public final Tip mojangTip;
/*     */   public final Tip tlauncherTip;
/*     */   public final Tip microsoftTip;
/*     */   public final Tip freeTip;
/*     */   private final AccountEditorScene scene;
/*     */   private Tip tip;
/*     */   private final EditorPane content;
/*     */   
/*     */   public AccountTip(AccountEditorScene sc) {
/*  28 */     super(smallSquareInsets);
/*     */     
/*  30 */     this.scene = sc;
/*     */     
/*  32 */     this.content = new EditorPane();
/*  33 */     this.content.addMouseListener(new MouseListener()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/*  36 */             if (!AccountTip.this.isVisible()) {
/*  37 */               e.consume();
/*     */             }
/*     */           }
/*     */           
/*     */           public void mousePressed(MouseEvent e) {
/*  42 */             if (!AccountTip.this.isVisible()) {
/*  43 */               e.consume();
/*     */             }
/*     */           }
/*     */           
/*     */           public void mouseReleased(MouseEvent e) {
/*  48 */             if (!AccountTip.this.isVisible()) {
/*  49 */               e.consume();
/*     */             }
/*     */           }
/*     */           
/*     */           public void mouseEntered(MouseEvent e) {
/*  54 */             if (!AccountTip.this.isVisible()) {
/*  55 */               e.consume();
/*     */             }
/*     */           }
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/*  60 */             if (!AccountTip.this.isVisible())
/*  61 */               e.consume(); 
/*     */           }
/*     */         });
/*  64 */     add((Component)this.content);
/*     */     
/*  66 */     this.freeTip = new Tip(Account.AccountType.FREE, ImageCache.getRes("free-user.png"));
/*  67 */     this.mojangTip = new Tip(Account.AccountType.MOJANG, ImageCache.getRes("mojang-user.png"));
/*  68 */     this.tlauncherTip = new Tip(Account.AccountType.TLAUNCHER, ImageCache.getRes("tlauncher-user.png"));
/*  69 */     this.microsoftTip = new Tip(Account.AccountType.MICROSOFT, ImageCache.getRes("microsoft-user.png"));
/*  70 */     setTip((Tip)null);
/*     */   }
/*     */   
/*     */   public void setAccountType(Account.AccountType type) {
/*  74 */     if (type != null) {
/*  75 */       switch (type) {
/*     */         case TLAUNCHER:
/*  77 */           setTip(this.tlauncherTip);
/*     */           return;
/*     */         case FREE:
/*  80 */           setTip(this.freeTip);
/*     */           return;
/*     */         case MOJANG:
/*  83 */           setTip(this.mojangTip);
/*     */           return;
/*     */         case MICROSOFT:
/*  86 */           setTip(this.microsoftTip);
/*     */           return;
/*     */       } 
/*     */     
/*     */     }
/*  91 */     setTip((Tip)null);
/*     */   }
/*     */   
/*     */   public Tip getTip() {
/*  95 */     return this.tip;
/*     */   }
/*     */   
/*     */   public void setTip(Tip tip) {
/*  99 */     this.tip = tip;
/*     */     
/* 101 */     if (tip == null) {
/* 102 */       setVisible(false);
/*     */       
/*     */       return;
/*     */     } 
/* 106 */     setVisible(true);
/*     */     
/* 108 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 110 */     builder
/* 111 */       .append("<table width=\"")
/* 112 */       .append(510 - (getInsets()).left - (getInsets()).right)
/* 113 */       .append("\" height=\"")
/* 114 */       .append(tip.getHeight())
/* 115 */       .append("\"><tr><td align=\"center\" valign=\"center\">");
/*     */     
/* 117 */     if (tip.image != null) {
/* 118 */       builder
/* 119 */         .append("<img src=\"")
/* 120 */         .append(tip.image)
/* 121 */         .append("\" /></td><td align=\"center\" valign=\"center\" width=\"100%\">");
/*     */     }
/* 123 */     builder
/* 124 */       .append(Localizable.get(tip.path));
/*     */     
/* 126 */     builder
/* 127 */       .append("</td></tr></table>");
/*     */     
/* 129 */     setContent(builder.toString(), 510, tip.getHeight());
/*     */   }
/*     */   
/*     */   void setContent(String text, int width, int height) {
/* 133 */     if (width < 1 || height < 1) {
/* 134 */       throw new IllegalArgumentException();
/*     */     }
/* 136 */     this.content.setText(text);
/*     */     
/* 138 */     if (OS.CURRENT == OS.LINUX) {
/* 139 */       width = (int)(width * 1.2D);
/* 140 */       height = (int)(height * 1.2D);
/*     */     } 
/*     */     
/* 143 */     setSize(width, height + (getInsets()).top + (getInsets()).bottom);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLocale() {
/* 148 */     setTip(this.tip);
/*     */   }
/*     */ 
/*     */   
/*     */   public class Tip
/*     */   {
/*     */     private final Account.AccountType type;
/*     */     private final String path;
/*     */     private final URL image;
/*     */     
/*     */     Tip(Account.AccountType type, URL image) {
/* 159 */       this.type = type;
/*     */       
/* 161 */       this.path = "auth.tip." + type.toString().toLowerCase(Locale.ROOT);
/* 162 */       this.image = image;
/*     */     }
/*     */     
/*     */     public int getHeight() {
/* 166 */       return AccountTip.this.tlauncher.getLang().getInteger(this.path + ".height");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\accounts\AccountTip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */