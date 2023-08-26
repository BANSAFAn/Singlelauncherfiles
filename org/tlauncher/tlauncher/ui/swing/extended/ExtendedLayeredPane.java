/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.event.ComponentEvent;
/*    */ import java.awt.event.ComponentListener;
/*    */ import javax.swing.JComponent;
/*    */ import org.tlauncher.tlauncher.ui.block.BlockableLayeredPane;
/*    */ import org.tlauncher.tlauncher.ui.swing.ResizeableComponent;
/*    */ 
/*    */ 
/*    */ public abstract class ExtendedLayeredPane
/*    */   extends BlockableLayeredPane
/*    */   implements ResizeableComponent
/*    */ {
/*    */   private static final long serialVersionUID = -1L;
/* 16 */   private Integer LAYER_COUNT = Integer.valueOf(0);
/*    */   protected final JComponent parent;
/*    */   
/*    */   protected ExtendedLayeredPane() {
/* 20 */     this.parent = null;
/*    */   }
/*    */   
/*    */   protected ExtendedLayeredPane(JComponent parent) {
/* 24 */     this.parent = parent;
/*    */     
/* 26 */     if (parent == null) {
/*    */       return;
/*    */     }
/* 29 */     parent.addComponentListener(new ComponentListener()
/*    */         {
/*    */           public void componentResized(ComponentEvent e) {
/* 32 */             ExtendedLayeredPane.this.onResize();
/*    */           }
/*    */ 
/*    */ 
/*    */           
/*    */           public void componentMoved(ComponentEvent e) {}
/*    */ 
/*    */           
/*    */           public void componentShown(ComponentEvent e) {
/* 41 */             ExtendedLayeredPane.this.onResize();
/*    */           }
/*    */ 
/*    */ 
/*    */           
/*    */           public void componentHidden(ComponentEvent e) {}
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public Component add(Component comp) {
/* 52 */     Integer integer1 = this.LAYER_COUNT, integer2 = this.LAYER_COUNT = Integer.valueOf(this.LAYER_COUNT.intValue() + 1); add(comp, integer1);
/* 53 */     return comp;
/*    */   }
/*    */   
/*    */   public void add(Component... components) {
/* 57 */     if (components == null) {
/* 58 */       throw new NullPointerException();
/*    */     }
/* 60 */     for (Component comp : components) {
/* 61 */       add(comp);
/*    */     }
/*    */   }
/*    */   
/*    */   public void onResize() {
/* 66 */     if (this.parent == null) {
/*    */       return;
/*    */     }
/* 69 */     setSize(this.parent.getWidth(), this.parent.getHeight());
/*    */     
/* 71 */     for (Component comp : getComponents()) {
/* 72 */       if (comp instanceof ResizeableComponent)
/* 73 */         ((ResizeableComponent)comp).onResize(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\ExtendedLayeredPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */