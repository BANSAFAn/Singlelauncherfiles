/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
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
/*     */ public class FileCleaningTracker
/*     */ {
/*  51 */   ReferenceQueue<Object> q = new ReferenceQueue();
/*     */ 
/*     */ 
/*     */   
/*  55 */   final Collection<Tracker> trackers = Collections.synchronizedSet(new HashSet<>());
/*     */ 
/*     */ 
/*     */   
/*  59 */   final List<String> deleteFailures = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   volatile boolean exitWhenFinished;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Thread reaper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void track(File file, Object marker) {
/*  79 */     track(file, marker, (FileDeleteStrategy)null);
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
/*     */   
/*     */   public void track(File file, Object marker, FileDeleteStrategy deleteStrategy) {
/*  93 */     Objects.requireNonNull(file, "file");
/*  94 */     addTracker(file.getPath(), marker, deleteStrategy);
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
/*     */   public void track(String path, Object marker) {
/* 107 */     track(path, marker, (FileDeleteStrategy)null);
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
/*     */   
/*     */   public void track(String path, Object marker, FileDeleteStrategy deleteStrategy) {
/* 121 */     Objects.requireNonNull(path, "path");
/* 122 */     addTracker(path, marker, deleteStrategy);
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
/*     */   private synchronized void addTracker(String path, Object marker, FileDeleteStrategy deleteStrategy) {
/* 135 */     if (this.exitWhenFinished) {
/* 136 */       throw new IllegalStateException("No new trackers can be added once exitWhenFinished() is called");
/*     */     }
/* 138 */     if (this.reaper == null) {
/* 139 */       this.reaper = new Reaper();
/* 140 */       this.reaper.start();
/*     */     } 
/* 142 */     this.trackers.add(new Tracker(path, deleteStrategy, marker, this.q));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTrackCount() {
/* 152 */     return this.trackers.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getDeleteFailures() {
/* 162 */     return this.deleteFailures;
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
/*     */   public synchronized void exitWhenFinished() {
/* 189 */     this.exitWhenFinished = true;
/* 190 */     if (this.reaper != null) {
/* 191 */       synchronized (this.reaper) {
/* 192 */         this.reaper.interrupt();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class Reaper
/*     */     extends Thread
/*     */   {
/*     */     Reaper() {
/* 203 */       super("File Reaper");
/* 204 */       setPriority(10);
/* 205 */       setDaemon(true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 215 */       while (!FileCleaningTracker.this.exitWhenFinished || !FileCleaningTracker.this.trackers.isEmpty()) {
/*     */         
/*     */         try {
/* 218 */           FileCleaningTracker.Tracker tracker = (FileCleaningTracker.Tracker)FileCleaningTracker.this.q.remove();
/* 219 */           FileCleaningTracker.this.trackers.remove(tracker);
/* 220 */           if (!tracker.delete()) {
/* 221 */             FileCleaningTracker.this.deleteFailures.add(tracker.getPath());
/*     */           }
/* 223 */           tracker.clear();
/* 224 */         } catch (InterruptedException e) {}
/*     */       } 
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
/*     */   private static final class Tracker
/*     */     extends PhantomReference<Object>
/*     */   {
/*     */     private final String path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final FileDeleteStrategy deleteStrategy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Tracker(String path, FileDeleteStrategy deleteStrategy, Object marker, ReferenceQueue<? super Object> queue) {
/* 255 */       super(marker, queue);
/* 256 */       this.path = path;
/* 257 */       this.deleteStrategy = (deleteStrategy == null) ? FileDeleteStrategy.NORMAL : deleteStrategy;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getPath() {
/* 266 */       return this.path;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean delete() {
/* 276 */       return this.deleteStrategy.deleteQuietly(new File(this.path));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\FileCleaningTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */