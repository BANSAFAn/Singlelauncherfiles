/*    */ package org.tlauncher.tlauncher.component;
/*    */ 
/*    */ import org.tlauncher.tlauncher.managers.ComponentManager;
/*    */ import org.tlauncher.util.async.AsyncThread;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RefreshableComponent
/*    */   extends LauncherComponent
/*    */ {
/*    */   public RefreshableComponent(ComponentManager manager) throws Exception {
/* 14 */     super(manager);
/*    */   }
/*    */   
/*    */   public boolean refreshComponent() {
/* 18 */     return refresh();
/*    */   }
/*    */   
/*    */   public void asyncRefresh() {
/* 22 */     AsyncThread.execute(this::refresh);
/*    */   }
/*    */   
/*    */   protected abstract boolean refresh();
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\component\RefreshableComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */