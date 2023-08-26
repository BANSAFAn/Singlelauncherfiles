/*    */ package org.tlauncher.tlauncher.ui.scenes;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.server.BackPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UniverseBackPanel
/*    */   extends BackPanel
/*    */ {
/*    */   public UniverseBackPanel(String titleName) {
/* 17 */     super(titleName, new MouseAdapter()
/*    */         {
/*    */           public void mousePressed(MouseEvent e) {
/* 20 */             if (SwingUtilities.isLeftMouseButton(e)) {
/* 21 */               (TLauncher.getInstance().getFrame()).mp.openDefaultScene();
/*    */             }
/*    */           }
/* 24 */         },  ImageCache.getIcon("back-arrow.png"));
/*    */   }
/*    */   public UniverseBackPanel(Color color) {
/* 27 */     super("", new MouseAdapter()
/*    */         {
/*    */           public void mousePressed(MouseEvent e) {
/* 30 */             if (SwingUtilities.isLeftMouseButton(e)) {
/* 31 */               (TLauncher.getInstance().getFrame()).mp.openDefaultScene();
/*    */             }
/*    */           }
/* 34 */         },  ImageCache.getIcon("back-arrow.png"));
/* 35 */     setBackground(color);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\UniverseBackPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */