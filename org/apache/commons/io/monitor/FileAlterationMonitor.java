/*     */ package org.apache.commons.io.monitor;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FileAlterationMonitor
/*     */   implements Runnable
/*     */ {
/*  35 */   private static final FileAlterationObserver[] EMPTY_ARRAY = new FileAlterationObserver[0];
/*     */   
/*     */   private final long interval;
/*  38 */   private final List<FileAlterationObserver> observers = new CopyOnWriteArrayList<>();
/*     */   
/*     */   private Thread thread;
/*     */   
/*     */   private ThreadFactory threadFactory;
/*     */   
/*     */   private volatile boolean running;
/*     */   
/*     */   public FileAlterationMonitor() {
/*  47 */     this(10000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationMonitor(long interval) {
/*  57 */     this.interval = interval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationMonitor(long interval, Collection<FileAlterationObserver> observers) {
/*  70 */     this(interval, 
/*     */         
/*  72 */         (FileAlterationObserver[])((Collection)Optional.<Collection>ofNullable(observers)
/*  73 */         .orElse(Collections.emptyList()))
/*  74 */         .toArray((Object[])EMPTY_ARRAY));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAlterationMonitor(long interval, FileAlterationObserver... observers) {
/*  87 */     this(interval);
/*  88 */     if (observers != null) {
/*  89 */       for (FileAlterationObserver observer : observers) {
/*  90 */         addObserver(observer);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInterval() {
/* 101 */     return this.interval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setThreadFactory(ThreadFactory threadFactory) {
/* 110 */     this.threadFactory = threadFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addObserver(FileAlterationObserver observer) {
/* 119 */     if (observer != null) {
/* 120 */       this.observers.add(observer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeObserver(FileAlterationObserver observer) {
/* 130 */     if (observer != null) {
/* 131 */       while (this.observers.remove(observer));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterable<FileAlterationObserver> getObservers() {
/* 144 */     return this.observers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() throws Exception {
/* 153 */     if (this.running) {
/* 154 */       throw new IllegalStateException("Monitor is already running");
/*     */     }
/* 156 */     for (FileAlterationObserver observer : this.observers) {
/* 157 */       observer.initialize();
/*     */     }
/* 159 */     this.running = true;
/* 160 */     if (this.threadFactory != null) {
/* 161 */       this.thread = this.threadFactory.newThread(this);
/*     */     } else {
/* 163 */       this.thread = new Thread(this);
/*     */     } 
/* 165 */     this.thread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop() throws Exception {
/* 174 */     stop(this.interval);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop(long stopInterval) throws Exception {
/* 186 */     if (!this.running) {
/* 187 */       throw new IllegalStateException("Monitor is not running");
/*     */     }
/* 189 */     this.running = false;
/*     */     try {
/* 191 */       this.thread.interrupt();
/* 192 */       this.thread.join(stopInterval);
/* 193 */     } catch (InterruptedException e) {
/* 194 */       Thread.currentThread().interrupt();
/*     */     } 
/* 196 */     for (FileAlterationObserver observer : this.observers) {
/* 197 */       observer.destroy();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 206 */     while (this.running) {
/* 207 */       for (FileAlterationObserver observer : this.observers) {
/* 208 */         observer.checkAndNotify();
/*     */       }
/* 210 */       if (!this.running) {
/*     */         break;
/*     */       }
/*     */       try {
/* 214 */         Thread.sleep(this.interval);
/* 215 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\monitor\FileAlterationMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */