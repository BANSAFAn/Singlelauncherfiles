/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.awt.event.MouseListener;
/*    */ 
/*    */ public class ControllableMouseAdapter implements MouseListener {
/*    */   private MouseEventHandler click;
/*    */   private MouseEventHandler press;
/*    */   
/*    */   public MouseEventHandler getOnClick() {
/* 11 */     return this.click;
/*    */   }
/*    */   private MouseEventHandler release; private MouseEventHandler enter; private MouseEventHandler exit;
/*    */   public ControllableMouseAdapter setOnClick(MouseEventHandler handler) {
/* 15 */     this.click = handler;
/* 16 */     return this;
/*    */   }
/*    */   
/*    */   public MouseEventHandler getOnPress() {
/* 20 */     return this.press;
/*    */   }
/*    */   
/*    */   public ControllableMouseAdapter setOnPress(MouseEventHandler handler) {
/* 24 */     this.press = handler;
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public MouseEventHandler getOnRelease() {
/* 29 */     return this.release;
/*    */   }
/*    */   
/*    */   public ControllableMouseAdapter setOnRelease(MouseEventHandler handler) {
/* 33 */     this.release = handler;
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public MouseEventHandler getOnEnter() {
/* 38 */     return this.enter;
/*    */   }
/*    */   
/*    */   public ControllableMouseAdapter setOnEnter(MouseEventHandler handler) {
/* 42 */     this.enter = handler;
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public MouseEventHandler getOnExit() {
/* 47 */     return this.exit;
/*    */   }
/*    */   
/*    */   public ControllableMouseAdapter setOnExit(MouseEventHandler handler) {
/* 51 */     this.exit = handler;
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void mouseClicked(MouseEvent e) {
/* 57 */     if (this.click != null) {
/* 58 */       this.click.handleEvent(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public final void mousePressed(MouseEvent e) {
/* 63 */     if (this.press != null) {
/* 64 */       this.press.handleEvent(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public final void mouseReleased(MouseEvent e) {
/* 69 */     if (this.release != null) {
/* 70 */       this.release.handleEvent(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public final void mouseEntered(MouseEvent e) {
/* 75 */     if (this.enter != null) {
/* 76 */       this.enter.handleEvent(e);
/*    */     }
/*    */   }
/*    */   
/*    */   public final void mouseExited(MouseEvent e) {
/* 81 */     if (this.exit != null)
/* 82 */       this.exit.handleEvent(e); 
/*    */   }
/*    */   
/*    */   public static interface MouseEventHandler {
/*    */     void handleEvent(MouseEvent param1MouseEvent);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\ControllableMouseAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */