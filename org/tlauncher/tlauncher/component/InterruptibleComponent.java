/*    */ package org.tlauncher.tlauncher.component;
/*    */ 
/*    */ import java.util.concurrent.Semaphore;
/*    */ import org.tlauncher.tlauncher.managers.ComponentManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class InterruptibleComponent
/*    */   extends RefreshableComponent
/*    */ {
/*    */   protected final boolean[] refreshList;
/*    */   private int lastRefreshID;
/* 16 */   protected final Semaphore semaphore = new Semaphore(1);
/*    */   
/*    */   protected InterruptibleComponent(ComponentManager manager) throws Exception {
/* 19 */     this(manager, 64);
/*    */   }
/*    */   protected boolean lastResult;
/*    */   
/*    */   private InterruptibleComponent(ComponentManager manager, int listSize) throws Exception {
/* 24 */     super(manager);
/*    */     
/* 26 */     if (listSize < 1) {
/* 27 */       throw new IllegalArgumentException("Invalid list size: " + listSize + " < 1");
/*    */     }
/*    */     
/* 30 */     this.refreshList = new boolean[listSize];
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final boolean refresh() {
/* 37 */     if (this.semaphore.tryAcquire()) {
/*    */       try {
/* 39 */         return this.lastResult = refresh(nextID());
/*    */       } finally {
/* 41 */         this.semaphore.release();
/*    */       } 
/*    */     }
/*    */     
/*    */     try {
/* 46 */       this.semaphore.acquire();
/* 47 */       return this.lastResult;
/* 48 */     } catch (InterruptedException e) {
/* 49 */       e.printStackTrace();
/* 50 */       return false;
/*    */     } finally {
/* 52 */       this.semaphore.release();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void stopRefresh() {
/* 60 */     for (int i = 0; i < this.refreshList.length; i++)
/* 61 */       this.refreshList[i] = false; 
/*    */   }
/*    */   
/*    */   protected synchronized int nextID() {
/* 65 */     int listSize = this.refreshList.length, next = this.lastRefreshID++;
/*    */     
/* 67 */     if (next >= listSize) {
/* 68 */       next = 0;
/*    */     }
/* 70 */     this.lastRefreshID = next;
/* 71 */     return next;
/*    */   }
/*    */   
/*    */   protected boolean isCancelled(int refreshID) {
/* 75 */     return !this.refreshList[refreshID];
/*    */   }
/*    */   
/*    */   protected abstract boolean refresh(int paramInt);
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\component\InterruptibleComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */