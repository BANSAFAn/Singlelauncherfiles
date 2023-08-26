/*    */ package org.tlauncher.tlauncher.ui.listener.mods;
/*    */ 
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.Timer;
/*    */ import javax.swing.event.DocumentEvent;
/*    */ import javax.swing.event.DocumentListener;
/*    */ 
/*    */ public class DeferredDocumentListener
/*    */   implements DocumentListener {
/*    */   private final Timer timer;
/*    */   
/*    */   public DeferredDocumentListener(int timeOut, ActionListener listener, boolean repeats) {
/* 13 */     this.timer = new Timer(timeOut, listener);
/* 14 */     this.timer.setRepeats(repeats);
/*    */   }
/*    */   
/*    */   public void start() {
/* 18 */     this.timer.start();
/*    */   }
/*    */   
/*    */   public void stop() {
/* 22 */     this.timer.stop();
/*    */   }
/*    */ 
/*    */   
/*    */   public void insertUpdate(DocumentEvent e) {
/* 27 */     this.timer.restart();
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeUpdate(DocumentEvent e) {
/* 32 */     this.timer.restart();
/*    */   }
/*    */ 
/*    */   
/*    */   public void changedUpdate(DocumentEvent e) {
/* 37 */     this.timer.restart();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\mods\DeferredDocumentListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */