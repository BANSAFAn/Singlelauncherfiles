/*    */ package org.tlauncher.tlauncher.ui.scenes;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import org.tlauncher.tlauncher.ui.MainPane;
/*    */ import org.tlauncher.tlauncher.ui.versions.VersionHandler;
/*    */ 
/*    */ public class VersionManagerScene extends PseudoScene {
/*    */   private static final long serialVersionUID = 758826812081732720L;
/*    */   final VersionHandler handler;
/*    */   
/*    */   public VersionManagerScene(MainPane main) {
/* 12 */     super(main);
/*    */     
/* 14 */     this.handler = new VersionHandler(this);
/* 15 */     add((Component)this.handler.list);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onResize() {
/* 20 */     super.onResize();
/*    */     
/* 22 */     this.handler.list.setLocation(
/* 23 */         getWidth() / 2 - this.handler.list.getWidth() / 2, 
/* 24 */         getHeight() / 2 - this.handler.list.getHeight() / 2);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\VersionManagerScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */