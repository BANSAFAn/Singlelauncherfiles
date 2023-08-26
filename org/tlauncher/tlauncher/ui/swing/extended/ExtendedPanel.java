/*     */ package org.tlauncher.tlauncher.ui.swing.extended;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Component;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedPanel
/*     */   extends JPanel
/*     */ {
/*     */   private static final long serialVersionUID = 3562102753301440454L;
/*     */   private final List<MouseListener> mouseListeners;
/*     */   private Insets insets;
/*  29 */   private float opacity = 1.0F;
/*     */   
/*     */   private AlphaComposite aComp;
/*     */   
/*     */   public ExtendedPanel(LayoutManager layout, boolean isDoubleBuffered) {
/*  34 */     super(layout, isDoubleBuffered);
/*     */     
/*  36 */     this.mouseListeners = new ArrayList<>();
/*     */     
/*  38 */     setOpaque(false);
/*     */   }
/*     */   
/*     */   public ExtendedPanel(LayoutManager layout) {
/*  42 */     this(layout, true);
/*     */   }
/*     */   
/*     */   public ExtendedPanel(boolean isDoubleBuffered) {
/*  46 */     this(new FlowLayout(), isDoubleBuffered);
/*     */   }
/*     */   
/*     */   public ExtendedPanel() {
/*  50 */     this(true);
/*     */   }
/*     */   
/*     */   public float getOpacity() {
/*  54 */     return this.opacity;
/*     */   }
/*     */   
/*     */   public void setOpacity(float f) {
/*  58 */     if (f < 0.0F || f > 1.0F) {
/*  59 */       throw new IllegalArgumentException("opacity must be in [0;1]");
/*     */     }
/*  61 */     this.opacity = f;
/*  62 */     this.aComp = AlphaComposite.getInstance(3, f);
/*     */     
/*  64 */     repaint();
/*     */   }
/*     */ 
/*     */   
/*     */   public Insets getInsets() {
/*  69 */     return (this.insets == null) ? super.getInsets() : this.insets;
/*     */   }
/*     */   
/*     */   public void setInsets(Insets insets) {
/*  73 */     this.insets = insets;
/*     */   }
/*     */ 
/*     */   
/*     */   public Component add(Component comp) {
/*  78 */     super.add(comp);
/*     */     
/*  80 */     return processListener(comp);
/*     */   }
/*     */ 
/*     */   
/*     */   public Component add(Component comp, int index) {
/*  85 */     super.add(comp, index);
/*  86 */     return processListener(comp);
/*     */   }
/*     */   
/*     */   private Component processListener(Component comp) {
/*  90 */     if (comp == null) {
/*  91 */       return null;
/*     */     }
/*  93 */     MouseListener[] compareListeners = comp.getMouseListeners();
/*     */     
/*  95 */     for (MouseListener listener : this.mouseListeners) {
/*  96 */       MouseListener add = listener;
/*     */       
/*  98 */       for (MouseListener compareListener : compareListeners) {
/*  99 */         if (listener.equals(compareListener)) {
/* 100 */           add = null;
/*     */           break;
/*     */         } 
/*     */       } 
/* 104 */       if (add == null)
/*     */         continue; 
/* 106 */       comp.addMouseListener(add);
/*     */     } 
/*     */     
/* 109 */     return comp;
/*     */   }
/*     */   
/*     */   public void add(Component... components) {
/* 113 */     if (components == null) {
/* 114 */       throw new NullPointerException();
/*     */     }
/* 116 */     for (Component comp : components)
/* 117 */       add(comp); 
/*     */   }
/*     */   
/*     */   public void add(Component component0, Component component1) {
/* 121 */     add(new Component[] { component0, component1 });
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void addMouseListener(MouseListener listener) {
/* 126 */     if (listener == null) {
/*     */       return;
/*     */     }
/* 129 */     this.mouseListeners.add(listener);
/*     */     
/* 131 */     for (Component comp : getComponents())
/* 132 */       comp.addMouseListener(listener); 
/*     */   }
/*     */   
/*     */   public synchronized void addMouseListenerOriginally(MouseListener listener) {
/* 136 */     super.addMouseListener(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void removeMouseListener(MouseListener listener) {
/* 141 */     if (listener == null) {
/*     */       return;
/*     */     }
/* 144 */     this.mouseListeners.remove(listener);
/*     */     
/* 146 */     for (Component comp : getComponents())
/* 147 */       comp.removeMouseListener(listener); 
/*     */   }
/*     */   
/*     */   protected synchronized void removeMouseListenerOriginally(MouseListener listener) {
/* 151 */     super.removeMouseListener(listener);
/*     */   }
/*     */   
/*     */   public boolean contains(Component comp) {
/* 155 */     if (comp == null) {
/* 156 */       return false;
/*     */     }
/* 158 */     for (Component c : getComponents()) {
/* 159 */       if (comp.equals(c))
/* 160 */         return true; 
/*     */     } 
/* 162 */     return false;
/*     */   }
/*     */   
/*     */   public Insets setInsets(int top, int left, int bottom, int right) {
/* 166 */     Insets insets = new Insets(top, left, bottom, right);
/* 167 */     setInsets(insets);
/*     */     
/* 169 */     return insets;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g0) {
/* 174 */     if (this.opacity == 1.0F) {
/* 175 */       super.paintComponent(g0);
/*     */       
/*     */       return;
/*     */     } 
/* 179 */     Graphics2D g = (Graphics2D)g0;
/* 180 */     g.setComposite(this.aComp);
/*     */     
/* 182 */     super.paintComponent(g0);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\ExtendedPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */