/*    */ package net.minecraft.launcher.process;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.concurrent.locks.ReadWriteLock;
/*    */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*    */ 
/*    */ public class LimitedCapacityList<T> {
/*    */   private final T[] items;
/*    */   private final Class<? extends T> clazz;
/* 10 */   private final ReadWriteLock locks = new ReentrantReadWriteLock();
/*    */   
/*    */   private int size;
/*    */   private int head;
/*    */   
/*    */   public LimitedCapacityList(Class<? extends T> clazz, int maxSize) {
/* 16 */     this.clazz = clazz;
/* 17 */     this.items = (T[])Array.newInstance(clazz, maxSize);
/*    */   }
/*    */   
/*    */   public T add(T value) {
/* 21 */     this.locks.writeLock().lock();
/*    */     
/* 23 */     this.items[this.head] = value;
/* 24 */     this.head = (this.head + 1) % getMaxSize();
/* 25 */     if (this.size < getMaxSize()) {
/* 26 */       this.size++;
/*    */     }
/* 28 */     this.locks.writeLock().unlock();
/* 29 */     return value;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 33 */     this.locks.readLock().lock();
/* 34 */     int result = this.size;
/* 35 */     this.locks.readLock().unlock();
/* 36 */     return result;
/*    */   }
/*    */   
/*    */   int getMaxSize() {
/* 40 */     this.locks.readLock().lock();
/* 41 */     int result = this.items.length;
/* 42 */     this.locks.readLock().unlock();
/* 43 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public T[] getItems() {
/* 48 */     T[] result = (T[])Array.newInstance(this.clazz, this.size);
/*    */     
/* 50 */     this.locks.readLock().lock();
/* 51 */     for (int i = 0; i < this.size; i++) {
/* 52 */       int pos = (this.head - this.size + i) % getMaxSize();
/* 53 */       if (pos < 0)
/* 54 */         pos += getMaxSize(); 
/* 55 */       result[i] = this.items[pos];
/*    */     } 
/* 57 */     this.locks.readLock().unlock();
/*    */     
/* 59 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\process\LimitedCapacityList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */