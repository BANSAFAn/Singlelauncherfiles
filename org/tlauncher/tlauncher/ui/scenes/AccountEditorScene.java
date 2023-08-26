/*     */ package org.tlauncher.tlauncher.ui.scenes;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.tlauncher.tlauncher.managers.ProfileManager;
/*     */ import org.tlauncher.tlauncher.managers.ProfileManagerListener;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.MainPane;
/*     */ import org.tlauncher.tlauncher.ui.accounts.AccountEditor;
/*     */ import org.tlauncher.tlauncher.ui.accounts.AccountList;
/*     */ import org.tlauncher.tlauncher.ui.accounts.AccountTip;
/*     */ import org.tlauncher.tlauncher.ui.accounts.helper.AccountEditorHelper;
/*     */ import org.tlauncher.tlauncher.ui.accounts.helper.HelperState;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.server.BackPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ 
/*     */ 
/*     */ public class AccountEditorScene
/*     */   extends PseudoScene
/*     */ {
/*     */   private static final long serialVersionUID = -151325577614420989L;
/*  39 */   private final int WIDTH = 562;
/*  40 */   private final int HEIGHT = 365;
/*     */   
/*     */   public final AccountEditor editor;
/*     */   
/*     */   public final AccountList list;
/*     */   
/*     */   public final AccountTip tip;
/*     */   public final AccountEditorHelper helper;
/*     */   public final ExtendedPanel panel;
/*  49 */   public static final Color BACKGROUND_ACCOUNT_COLOR = new Color(248, 246, 244);
/*     */   
/*     */   private final FlexibleEditorPanel flex;
/*     */   private final ProfileManager profileManager;
/*     */   
/*     */   public AccountEditorScene(MainPane main) {
/*  55 */     super(main);
/*     */     
/*  57 */     this.panel = new ExtendedPanel(new BorderLayout(0, 0));
/*  58 */     this.flex = new FlexibleEditorPanel("text/html", "auth.tip.tlauncher", 537);
/*  59 */     ExtendedPanel middlePanel = new ExtendedPanel();
/*  60 */     this.profileManager = TLauncher.getInstance().getProfileManager();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     BackPanel backPanel = new BackPanel("account.config", new MouseAdapter() { public void mousePressed(MouseEvent e) { if (SwingUtilities.isLeftMouseButton(e)) { AccountEditorScene.this.profileManager.refresh(); AccountEditorScene.this.getMainPane().openDefaultScene(); }  } }, ImageCache.getIcon("back-arrow.png"));
/*     */     
/*  71 */     this.panel.setOpaque(true);
/*  72 */     this.panel.setSize(562, 365);
/*     */     
/*  74 */     this.flex.setPreferredSize(new Dimension(546, 99));
/*  75 */     this.flex.setMargin(new Insets(20, 20, 22, 16));
/*  76 */     backPanel.setPreferredSize(new Dimension(562, 25));
/*  77 */     this.panel.setBackground(BACKGROUND_ACCOUNT_COLOR);
/*  78 */     middlePanel.setLayout(new BoxLayout((Container)middlePanel, 0));
/*  79 */     middlePanel.setPreferredSize(new Dimension(562, 191));
/*  80 */     middlePanel.setInsets(new Insets(20, 20, 0, 16));
/*     */     
/*  82 */     ExtendedPanel extendedPanel1 = new ExtendedPanel(new FlowLayout(0, 0, 0));
/*  83 */     JPanel gap = new JPanel(new FlowLayout(1, 0, 0));
/*     */     
/*  85 */     gap.setPreferredSize(new Dimension(1, 170));
/*  86 */     gap.setBackground(new Color(172, 171, 170));
/*     */     
/*  88 */     extendedPanel1.setPreferredSize(new Dimension(41, 171));
/*  89 */     extendedPanel1.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));
/*     */     
/*  91 */     extendedPanel1.add(Box.createHorizontalStrut(20));
/*  92 */     extendedPanel1.add(gap);
/*  93 */     extendedPanel1.add(Box.createHorizontalStrut(20));
/*     */     
/*  95 */     this.editor = new AccountEditor(this, this.flex);
/*  96 */     this.editor.setOpaque(true);
/*  97 */     this.list = new AccountList(this);
/*  98 */     this.profileManager.addListener((ProfileManagerListener)this.list);
/*     */     
/* 100 */     middlePanel.add((Component)this.editor);
/* 101 */     middlePanel.add((Component)extendedPanel1);
/* 102 */     middlePanel.add((Component)this.list);
/*     */     
/* 104 */     this.panel.add((Component)backPanel, "North");
/* 105 */     this.panel.add((Component)middlePanel, "Center");
/* 106 */     this.panel.add((Component)this.flex, "South");
/* 107 */     add((Component)this.panel);
/*     */     
/* 109 */     this.tip = new AccountTip(this);
/* 110 */     this.tip.setAccountType(Account.AccountType.TLAUNCHER);
/* 111 */     this.editor.setSelectedAccountType(Account.AccountType.TLAUNCHER);
/*     */     
/* 113 */     addComponentListener(new ComponentListener()
/*     */         {
/*     */           public void componentShown(ComponentEvent e)
/*     */           {
/* 117 */             if (AccountEditorScene.this.list.model.getSize() == 0) {
/* 118 */               AccountEditorScene.this.list.addTempToList();
/*     */             }
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void componentResized(ComponentEvent e) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void componentMoved(ComponentEvent e) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void componentHidden(ComponentEvent e) {}
/*     */         });
/* 135 */     this.tip.setVisible(true);
/* 136 */     this.helper = new AccountEditorHelper(this);
/* 137 */     add((Component)this.helper);
/* 138 */     this.helper.setState(HelperState.NONE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShownAccountHelper(boolean shown, boolean animate) {
/* 146 */     setShown(shown, animate);
/*     */     
/* 148 */     if (!shown || !this.list.model.isEmpty())
/*     */     {
/*     */ 
/*     */       
/* 152 */       this.helper.setState(HelperState.NONE);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onResize() {
/* 157 */     super.onResize();
/* 158 */     int hw = getWidth() / 2, hh = getHeight() / 2;
/*     */     
/* 160 */     this.panel.setLocation(hw - this.panel.getWidth() / 2, hh - this.panel.getHeight() / 2);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\AccountEditorScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */