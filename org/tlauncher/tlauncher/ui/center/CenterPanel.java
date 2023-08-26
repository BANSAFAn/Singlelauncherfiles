/*     */ package org.tlauncher.tlauncher.ui.center;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import javax.swing.BoxLayout;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.configuration.LangConfiguration;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.block.BlockablePanel;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.swing.Del;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.UnblockablePanel;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class CenterPanel extends BlockablePanel {
/*  20 */   public static final CenterPanelTheme defaultTheme = new DefaultCenterPanelTheme(), tipTheme = new TipPanelTheme(); private static final long serialVersionUID = -1975869198322761508L;
/*  21 */   public static final CenterPanelTheme loadingTheme = new LoadingPanelTheme(), settingsTheme = new SettingsPanelTheme();
/*  22 */   public static final CenterPanelTheme updateTheme = new UpdateTheme();
/*     */   
/*  24 */   public static final Insets defaultInsets = new Insets(5, 24, 18, 24);
/*     */   
/*  26 */   public static final Insets squareInsets = new Insets(11, 15, 11, 15), ACCOUNT_LOGIN_INSETS = new Insets(11, 19, 11, 0);
/*  27 */   public static final Insets accountInsets = new Insets(0, 20, 1, 10);
/*  28 */   public static final Insets VERSION_LOGIN_INSETS = new Insets(10, 20, 10, 0), PLAY_INSETS = new Insets(11, 0, 11, 0);
/*  29 */   public static final Insets SERVER_SQUARE_INSETS = new Insets(1, 10, 10, 10);
/*     */   
/*  31 */   public static final Insets smallSquareInsets = new Insets(7, 7, 7, 7), smallSquareNoTopInsets = new Insets(5, 15, 5, 15);
/*  32 */   public static final Insets noInsets = new Insets(0, 0, 0, 0); public static final Insets ISETS_20 = new Insets(20, 20, 20, 20); public static final Insets MIDDLE_PANEL = new Insets(20, 20, 5, 20);
/*     */   
/*     */   protected static final int ARC_SIZE = 24;
/*     */   
/*     */   private final Insets insets;
/*     */   
/*     */   private final CenterPanelTheme theme;
/*     */   
/*     */   protected final ExtendedPanel messagePanel;
/*     */   protected final LocalizableLabel messageLabel;
/*     */   public final TLauncher tlauncher;
/*     */   public final Configuration global;
/*     */   public final LangConfiguration lang;
/*     */   
/*     */   public CenterPanel() {
/*  47 */     this((CenterPanelTheme)null, (Insets)null);
/*     */   }
/*     */   
/*     */   public CenterPanel(Insets insets) {
/*  51 */     this((CenterPanelTheme)null, insets);
/*     */   }
/*     */   
/*     */   public CenterPanel(CenterPanelTheme theme) {
/*  55 */     this(theme, (Insets)null);
/*     */   }
/*     */   
/*     */   public CenterPanel(CenterPanelTheme theme, Insets insets) {
/*  59 */     this.tlauncher = TLauncher.getInstance();
/*  60 */     this.global = this.tlauncher.getConfiguration();
/*  61 */     this.lang = this.tlauncher.getLang();
/*     */     
/*  63 */     this.theme = theme = (theme == null) ? defaultTheme : theme;
/*  64 */     this.insets = insets = (insets == null) ? defaultInsets : insets;
/*     */     
/*  66 */     setLayout(new BoxLayout((Container)this, 3));
/*  67 */     setBackground(theme.getPanelBackground());
/*     */     
/*  69 */     this.messageLabel = new LocalizableLabel("  ");
/*  70 */     this.messageLabel.setFont(getFont().deriveFont(1));
/*  71 */     this.messageLabel.setVerticalAlignment(0);
/*  72 */     this.messageLabel.setHorizontalTextPosition(0);
/*  73 */     this.messageLabel.setAlignmentX(0.5F);
/*     */     
/*  75 */     this.messagePanel = new ExtendedPanel();
/*  76 */     this.messagePanel.setLayout(new BoxLayout((Container)this.messagePanel, 1));
/*  77 */     this.messagePanel.setAlignmentX(0.5F);
/*  78 */     this.messagePanel.setInsets(new Insets(3, 0, 3, 0));
/*  79 */     this.messagePanel.add((Component)this.messageLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CenterPanelTheme getTheme() {
/* 112 */     return this.theme;
/*     */   }
/*     */ 
/*     */   
/*     */   public Insets getInsets() {
/* 117 */     return this.insets;
/*     */   }
/*     */   
/*     */   protected Del del(int aligment) {
/* 121 */     return new Del(1, aligment, this.theme.getBorder());
/*     */   }
/*     */   
/*     */   protected Del del(int aligment, int width, int height) {
/* 125 */     return new Del(1, aligment, width, height, this.theme.getBorder());
/*     */   }
/*     */   
/*     */   public void defocus() {
/* 129 */     requestFocusInWindow();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setError(String message) {
/* 134 */     this.messageLabel.setForeground(this.theme.getFailure());
/* 135 */     this.messageLabel.setText((message == null || message.length() == 0) ? " " : message);
/* 136 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean setMessage(String message, Object... vars) {
/* 140 */     this.messageLabel.setForeground(this.theme.getFocus());
/* 141 */     this.messageLabel.setText((message == null || message.length() == 0) ? " " : message, vars);
/* 142 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean setMessage(String message) {
/* 146 */     return setMessage(message, Localizable.EMPTY_VARS);
/*     */   }
/*     */   
/*     */   public static BlockablePanel sepPan(LayoutManager manager, Component... components) {
/* 150 */     BlockablePanel panel = new BlockablePanel(manager)
/*     */       {
/*     */         private static final long serialVersionUID = 1L;
/*     */ 
/*     */         
/*     */         public Insets getInsets() {
/* 156 */           return CenterPanel.noInsets;
/*     */         }
/*     */       };
/*     */     
/* 160 */     panel.add(components);
/*     */     
/* 162 */     return panel;
/*     */   }
/*     */   
/*     */   public static BlockablePanel sepPan(Component... components) {
/* 166 */     return sepPan(new GridLayout(0, 1), components);
/*     */   }
/*     */   
/*     */   public static UnblockablePanel uSepPan(LayoutManager manager, Component... components) {
/* 170 */     UnblockablePanel panel = new UnblockablePanel(manager)
/*     */       {
/*     */         private static final long serialVersionUID = 1L;
/*     */ 
/*     */         
/*     */         public Insets getInsets() {
/* 176 */           return CenterPanel.noInsets;
/*     */         }
/*     */       };
/*     */     
/* 180 */     panel.add(components);
/*     */     
/* 182 */     return panel;
/*     */   }
/*     */   
/*     */   public static UnblockablePanel uSepPan(Component... components) {
/* 186 */     return uSepPan(new GridLayout(0, 1), components);
/*     */   }
/*     */   
/*     */   protected void log(Object... o) {
/* 190 */     U.log(new Object[] { "[" + getClass().getSimpleName() + "]", o });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\center\CenterPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */