/*    */ package org.tlauncher.tlauncher.ui.label;
/*    */ 
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.swing.OwnImageCheckBox;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CheckBoxBlockAction
/*    */   extends OwnImageCheckBox
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Object object;
/*    */   private Executor executor;
/*    */   
/*    */   public void setObject(Object object) {
/* 22 */     this.object = object;
/*    */   } public void setExecutor(Executor executor) {
/* 24 */     this.executor = executor;
/*    */   }
/*    */   
/*    */   public CheckBoxBlockAction(String selectedIcon, String diselectedIcon) {
/* 28 */     super("", selectedIcon, diselectedIcon);
/* 29 */     addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mousePressed(MouseEvent e) {
/* 32 */             SwingUtilities.invokeLater(() -> {
/*    */                   if (SwingUtilities.isLeftMouseButton(e)) {
/*    */                     CompletableFuture.runAsync((), CheckBoxBlockAction.this.executor).exceptionally(());
/*    */                   }
/*    */                 });
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void executeWithBlock() {
/* 56 */     synchronized (this.object) {
/* 57 */       executeRequest();
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void executeRequest();
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\label\CheckBoxBlockAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */