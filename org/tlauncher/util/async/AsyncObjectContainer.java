/*    */ package org.tlauncher.util.async;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsyncObjectContainer<T>
/*    */ {
/* 15 */   private final List<AsyncObject<T>> objects = new ArrayList<>();
/* 16 */   private final Map<AsyncObject<T>, T> values = new LinkedHashMap<>();
/*    */   private boolean executionLock;
/*    */   
/*    */   public AsyncObjectContainer(AsyncObject<T>[] asyncObjects) {
/* 20 */     this();
/*    */     
/* 22 */     for (AsyncObject<T> object : asyncObjects)
/* 23 */       add(object); 
/*    */   }
/*    */   public AsyncObjectContainer() {}
/*    */   public Map<AsyncObject<T>, T> execute() {
/* 27 */     this.executionLock = true;
/* 28 */     this.values.clear();
/*    */     
/* 30 */     synchronized (this.objects) {
/* 31 */       int i = 0, size = this.objects.size();
/*    */       
/* 33 */       for (AsyncObject<T> object : this.objects) {
/* 34 */         object.start();
/*    */       }
/* 36 */       while (i < size) {
/* 37 */         for (AsyncObject<T> object : this.objects) {
/*    */           
/* 39 */           try { if (this.values.containsKey(object)) {
/*    */               continue;
/*    */             }
/* 42 */             this.values.put(object, object.getValue());
/* 43 */             i++; }
/* 44 */           catch (AsyncObjectNotReadyException asyncObjectNotReadyException) {  }
/* 45 */           catch (AsyncObjectGotErrorException ignored0)
/* 46 */           { this.values.put(object, null);
/* 47 */             i++; }
/*    */         
/*    */         } 
/*    */       } 
/*    */     } 
/* 52 */     this.executionLock = false;
/* 53 */     return this.values;
/*    */   }
/*    */   
/*    */   public void add(AsyncObject<T> object) {
/* 57 */     if (object == null) {
/* 58 */       throw new NullPointerException();
/*    */     }
/* 60 */     synchronized (this.objects) {
/* 61 */       if (this.executionLock) {
/* 62 */         throw new AsyncContainerLockedException();
/*    */       }
/* 64 */       this.objects.add(object);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void remove(AsyncObject<T> object) {
/* 69 */     if (object == null) {
/* 70 */       throw new NullPointerException();
/*    */     }
/* 72 */     synchronized (this.objects) {
/* 73 */       if (this.executionLock) {
/* 74 */         throw new AsyncContainerLockedException();
/*    */       }
/* 76 */       this.objects.remove(object);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void removeAll() {
/* 81 */     synchronized (this.objects) {
/* 82 */       if (this.executionLock) {
/* 83 */         throw new AsyncContainerLockedException();
/*    */       }
/* 85 */       this.objects.clear();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\async\AsyncObjectContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */