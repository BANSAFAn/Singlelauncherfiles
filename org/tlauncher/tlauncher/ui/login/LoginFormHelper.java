/*     */ package org.tlauncher.tlauncher.ui.login;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.JComponent;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
/*     */ import org.tlauncher.tlauncher.ui.center.LoginHelperTheme;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.scenes.DefaultScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.ResizeableComponent;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedLayeredPane;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoginFormHelper
/*     */   extends ExtendedLayeredPane
/*     */   implements LocalizableComponent
/*     */ {
/*     */   private static final int EDGE = 100;
/*     */   private final DefaultScene defaultScene;
/*     */   private final LoginForm loginForm;
/*     */   private final LoginFormHelperTip[] tips;
/*     */   private volatile LoginFormHelperState state;
/*     */   
/*     */   public LoginFormHelper(DefaultScene scene) {
/*  42 */     this.defaultScene = scene;
/*  43 */     this.loginForm = scene.loginForm;
/*     */     
/*  45 */     this.tips = new LoginFormHelperTip[] { new LoginFormHelperTip("username", (JComponent)this.loginForm.accountPanel, Position.UP), new LoginFormHelperTip("versions", (JComponent)this.loginForm.versionPanel, Position.UP), new LoginFormHelperTip("play", (JComponent)this.loginForm.playPanel, Position.UP) };
/*     */ 
/*     */     
/*  48 */     for (LoginFormHelperTip tip : this.tips) {
/*  49 */       tip.addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mouseClicked(MouseEvent e) {
/*  52 */               LoginFormHelper.this.setState(LoginFormHelper.LoginFormHelperState.NONE);
/*     */             }
/*     */           });
/*     */     } 
/*  56 */     add((Component[])this.tips);
/*  57 */     setState(LoginFormHelperState.NONE);
/*  58 */     MouseAdapter adapter = new MouseAdapter()
/*     */       {
/*     */         public void mousePressed(MouseEvent e) {
/*  61 */           LoginFormHelper.this.setState(LoginFormHelper.LoginFormHelperState.NONE);
/*     */         }
/*     */       };
/*  64 */     this.loginForm.play.addMouseListener(adapter);
/*  65 */     this.loginForm.buttons.refresh.addMouseListener(adapter);
/*  66 */     this.loginForm.buttons.folder.addMouseListener(adapter);
/*  67 */     this.loginForm.buttons.settings.addMouseListener(adapter);
/*     */   }
/*     */   
/*     */   public LoginFormHelperState getState() {
/*  71 */     return this.state;
/*     */   }
/*     */   
/*     */   public void setState(LoginFormHelperState state) {
/*  75 */     if (state == null)
/*  76 */       throw new NullPointerException(); 
/*  77 */     this.state = state;
/*  78 */     updateTips();
/*  79 */     setVisible((state != LoginFormHelperState.NONE));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onResize() {
/*  85 */     setSize(this.loginForm.getWidth(), 100);
/*  86 */     setLocation(this.loginForm.getX(), this.loginForm.getY() - 100);
/*  87 */     for (Component comp : getComponents()) {
/*  88 */       if (comp instanceof ResizeableComponent)
/*  89 */         ((ResizeableComponent)comp).onResize(); 
/*     */     } 
/*  91 */     updateTips();
/*     */   }
/*     */   
/*     */   protected void updateTips() {
/*  95 */     for (LoginFormHelperTip tip : this.tips) {
/*     */       
/*  97 */       tip.setVisible(false);
/*     */       
/*  99 */       if (this.state != LoginFormHelperState.NONE) {
/*     */ 
/*     */ 
/*     */         
/* 103 */         tip.label.setText("loginform.helper." + tip.name);
/* 104 */         tip.label.setVerticalAlignment(0);
/* 105 */         tip.label.setHorizontalAlignment(0);
/* 106 */         Dimension dim = tip.getPreferredSize();
/*     */ 
/*     */         
/* 109 */         Point pP = SwingUtil.getRelativeLocation((Component)this.loginForm, tip.component);
/*     */         
/* 111 */         int x = pP.x - dim.width + (tip.component.getPreferredSize()).width;
/* 112 */         int y = 100 - dim.height;
/* 113 */         tip.setBounds(x, y, dim.width, dim.height);
/* 114 */         tip.setVisible(true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateLocale() {
/* 120 */     updateTips();
/*     */   }
/*     */   
/*     */   public enum LoginFormHelperState {
/* 124 */     NONE, SHOWN;
/*     */   }
/*     */   
/*     */   public enum Position {
/* 128 */     UP, DOWN;
/*     */   }
/*     */   
/* 131 */   private static final LoginHelperTheme loginHelperTheme = new LoginHelperTheme();
/* 132 */   private static final Insets loginHelperInsets = new Insets(6, 10, 6, 10);
/*     */   
/*     */   private class LoginFormHelperTip
/*     */     extends CenterPanel {
/*     */     private static final int TRIANGGLE_WIDTH = 10;
/*     */     final String name;
/*     */     final JComponent component;
/*     */     final LocalizableLabel label;
/*     */     
/*     */     LoginFormHelperTip(String name, JComponent comp, LoginFormHelper.Position pos) {
/* 142 */       super((CenterPanelTheme)LoginFormHelper.loginHelperTheme, noInsets);
/* 143 */       setLayout(new BorderLayout(0, 0));
/* 144 */       if (name == null) {
/* 145 */         throw new NullPointerException("Name is NULL!");
/*     */       }
/* 147 */       if (name.isEmpty()) {
/* 148 */         throw new IllegalArgumentException("Name is empty!");
/*     */       }
/* 150 */       if (comp == null) {
/* 151 */         throw new NullPointerException("Component is NULL!");
/*     */       }
/* 153 */       if (pos == null) {
/* 154 */         throw new NullPointerException("Position is NULL!");
/*     */       }
/* 156 */       this.name = name.toLowerCase();
/*     */       
/* 158 */       this.component = comp;
/* 159 */       ExtendedPanel p = new ExtendedPanel(new BorderLayout(0, 0));
/* 160 */       p.setInsets(LoginFormHelper.loginHelperInsets);
/* 161 */       p.setOpaque(true);
/*     */       
/* 163 */       p.setBackground(LoginFormHelper.loginHelperTheme.getBackground());
/*     */       
/* 165 */       this.label = new LocalizableLabel();
/* 166 */       this.label.setFont(new Font(this.label.getFont().getFamily(), 0, 12));
/* 167 */       this.label.setForeground(LoginFormHelper.loginHelperTheme.getForeground());
/* 168 */       p.add((Component)this.label, "Center");
/*     */       
/* 170 */       add((Component)p, "Center");
/* 171 */       add((Component)new LoginFormHelper.TipTriangle(10, LoginFormHelper.loginHelperTheme.getBorder(), LoginFormHelper.BORDER_POS.UP), "South");
/*     */     }
/*     */   }
/*     */   
/*     */   class TipTriangle
/*     */     extends ExtendedPanel
/*     */   {
/*     */     public static final int DEFAULT_WIDTH = 10;
/*     */     private int width;
/* 180 */     private int gap = 24;
/*     */     
/*     */     private final LoginFormHelper.BORDER_POS pos;
/*     */     
/*     */     private Color triangleColor;
/*     */ 
/*     */     
/*     */     public TipTriangle(int width, Color triangle, LoginFormHelper.BORDER_POS pos) {
/* 188 */       this.width = width;
/* 189 */       this.pos = pos;
/* 190 */       this.triangleColor = triangle;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected void paintComponent(Graphics g) {
/* 196 */       Graphics2D g2 = (Graphics2D)g;
/* 197 */       g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
/* 198 */       g2.setColor(this.triangleColor);
/* 199 */       Rectangle rec = getVisibleRect();
/* 200 */       int[] xT = new int[0], yT = new int[0];
/*     */ 
/*     */ 
/*     */       
/* 204 */       int startX = rec.x + rec.width + -this.gap;
/* 205 */       int startY = rec.y;
/* 206 */       xT = new int[] { startX, startX - this.width, startX - this.width * 2 };
/* 207 */       yT = new int[] { startY, startY + this.width, startY };
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
/* 224 */       g2.fillPolygon(xT, yT, 3);
/*     */       
/* 226 */       g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum BORDER_POS {
/* 231 */     UP, BOTTOM, LEFT, RIGHT;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\LoginFormHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */