/*     */ package org.tlauncher.tlauncher.downloader;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.tlauncher.tlauncher.ui.console.Console;
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
/*     */ public class DownloadableContainer
/*     */ {
/*  23 */   final List<Downloadable> list = Collections.synchronizedList(new ArrayList<>());
/*  24 */   private final List<DownloadableContainerHandler> handlers = Collections.synchronizedList(new ArrayList<>());
/*  25 */   private final List<Throwable> errors = Collections.synchronizedList(new ArrayList<>());
/*     */   private boolean requireAllFiles = true;
/*  27 */   private final AtomicInteger sum = new AtomicInteger();
/*     */   private Console console;
/*     */   
/*     */   public List<Downloadable> getList() {
/*  31 */     return Collections.unmodifiableList(this.list);
/*     */   }
/*     */   private boolean locked; private boolean aborted;
/*     */   public void add(Downloadable d) {
/*  35 */     if (d == null) {
/*  36 */       throw new NullPointerException();
/*     */     }
/*  38 */     checkLocked();
/*     */     
/*  40 */     if (this.list.contains(d)) {
/*     */       return;
/*     */     }
/*  43 */     this.list.add(d);
/*  44 */     d.setContainer(this);
/*     */     
/*  46 */     this.sum.incrementAndGet();
/*     */   }
/*     */   
/*     */   public void addAll(Downloadable... ds) {
/*  50 */     if (ds == null) {
/*  51 */       throw new NullPointerException();
/*     */     }
/*  53 */     for (int i = 0; i < ds.length; i++) {
/*  54 */       if (ds[i] == null) {
/*  55 */         throw new NullPointerException("Downloadable at " + i + " is NULL!");
/*     */       }
/*     */       
/*  58 */       if (!this.list.contains(ds[i])) {
/*     */ 
/*     */         
/*  61 */         this.list.add(ds[i]);
/*  62 */         ds[i].setContainer(this);
/*     */         
/*  64 */         this.sum.incrementAndGet();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void addAll(Collection<Downloadable> coll) {
/*  69 */     if (coll == null) {
/*  70 */       throw new NullPointerException();
/*     */     }
/*  72 */     int i = -1;
/*     */     
/*  74 */     for (Downloadable d : coll) {
/*  75 */       i++;
/*     */       
/*  77 */       if (d == null) {
/*  78 */         throw new NullPointerException("Downloadable at" + i + " is NULL!");
/*     */       }
/*     */       
/*  81 */       this.list.add(d);
/*  82 */       d.setContainer(this);
/*     */       
/*  84 */       this.sum.incrementAndGet();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addHandler(DownloadableContainerHandler handler) {
/*  89 */     if (handler == null) {
/*  90 */       throw new NullPointerException();
/*     */     }
/*  92 */     checkLocked();
/*     */     
/*  94 */     this.handlers.add(handler);
/*     */   }
/*     */   
/*     */   public List<Throwable> getErrors() {
/*  98 */     return Collections.unmodifiableList(this.errors);
/*     */   }
/*     */   
/*     */   public Console getConsole() {
/* 102 */     return this.console;
/*     */   }
/*     */   
/*     */   public void setConsole(Console console) {
/* 106 */     checkLocked();
/*     */     
/* 108 */     this.console = console;
/*     */   }
/*     */   
/*     */   public boolean isAborted() {
/* 112 */     return this.aborted;
/*     */   }
/*     */   
/*     */   private static void removeDuplicates(DownloadableContainer a, DownloadableContainer b) {
/* 116 */     if (a.locked) {
/* 117 */       throw new IllegalStateException("First conatiner is already locked!");
/*     */     }
/* 119 */     if (b.locked) {
/* 120 */       throw new IllegalStateException("Second container is already locked!");
/*     */     }
/* 122 */     a.locked = true;
/* 123 */     b.locked = true;
/*     */     
/*     */     try {
/* 126 */       List<Downloadable> bList = b.list, deleteList = new ArrayList<>();
/*     */       
/* 128 */       for (Downloadable aDownloadable : a.list) {
/* 129 */         for (Downloadable bDownloadable : bList) {
/* 130 */           if (aDownloadable.equals(bDownloadable))
/* 131 */             deleteList.add(bDownloadable); 
/*     */         } 
/* 133 */       }  bList.removeAll(deleteList);
/*     */     } finally {
/*     */       
/* 136 */       a.locked = false;
/* 137 */       b.locked = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isRequireAllFiles() {
/* 142 */     return this.requireAllFiles;
/*     */   }
/*     */   
/*     */   public void setRequireAllFiles(boolean requireAllFiles) {
/* 146 */     this.requireAllFiles = requireAllFiles;
/*     */   }
/*     */   
/*     */   void onComplete(Downloadable d) throws RetryDownloadException {
/* 150 */     for (DownloadableContainerHandler handler : this.handlers) {
/* 151 */       handler.onComplete(this, d);
/*     */     }
/* 153 */     if (this.sum.decrementAndGet() > 0) {
/*     */       return;
/*     */     }
/* 156 */     for (DownloadableContainerHandler handler : this.handlers)
/* 157 */       handler.onFullComplete(this); 
/*     */   }
/*     */   
/*     */   void onAbort(Downloadable d) {
/* 161 */     this.aborted = true;
/* 162 */     this.errors.add(d.getError());
/*     */     
/* 164 */     if (this.sum.decrementAndGet() > 0) {
/*     */       return;
/*     */     }
/* 167 */     for (DownloadableContainerHandler handler : this.handlers)
/* 168 */       handler.onAbort(this); 
/*     */   }
/*     */   
/*     */   void onError(Downloadable d, Throwable e) {
/* 172 */     this.errors.add(e);
/*     */     
/* 174 */     for (DownloadableContainerHandler handler : this.handlers)
/* 175 */       handler.onError(this, d, e); 
/*     */   }
/*     */   
/*     */   private void checkLocked() {
/* 179 */     if (this.locked)
/* 180 */       throw new IllegalStateException("Downloadable is locked!"); 
/*     */   }
/*     */   
/*     */   public static void removeDuplicates(List<? extends DownloadableContainer> list) {
/* 184 */     if (list == null) {
/* 185 */       throw new NullPointerException();
/*     */     }
/* 187 */     if (list.size() < 2) {
/*     */       return;
/*     */     }
/* 190 */     for (int i = 0; i < list.size() - 1; i++) {
/* 191 */       for (int k = i + 1; k < list.size(); k++)
/* 192 */         removeDuplicates(list.get(i), list.get(k)); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\DownloadableContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */