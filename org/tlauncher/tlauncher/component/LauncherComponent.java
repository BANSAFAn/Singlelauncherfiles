/*    */ package org.tlauncher.tlauncher.component;
/*    */ 
/*    */ import org.tlauncher.tlauncher.managers.ComponentManager;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class LauncherComponent
/*    */ {
/*    */   protected final ComponentManager manager;
/*    */   
/*    */   public LauncherComponent(ComponentManager manager) throws Exception {
/* 14 */     if (manager == null) {
/* 15 */       throw new NullPointerException();
/*    */     }
/* 17 */     this.manager = manager;
/*    */   }
/*    */   
/*    */   protected void log(Object... w) {
/* 21 */     U.log(new Object[] { "[" + getClass().getSimpleName() + "]", w });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\component\LauncherComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */