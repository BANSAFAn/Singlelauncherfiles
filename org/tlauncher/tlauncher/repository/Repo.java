/*     */ package org.tlauncher.tlauncher.repository;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.launcher.Http;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.tlauncher.util.Time;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Repo
/*     */ {
/*  19 */   private static final List<String> notShowLogs = new ArrayList<String>()
/*     */     {
/*     */     
/*     */     };
/*     */   private final String name;
/*     */   private final List<String> repos;
/*     */   private int primaryTimeout;
/*     */   private int selected;
/*     */   private boolean isSelected;
/*     */   
/*     */   public Repo(String[] urls, String name) {
/*  30 */     if (urls == null) {
/*  31 */       throw new NullPointerException("URL array is NULL!");
/*     */     }
/*  33 */     this.name = name;
/*  34 */     this.repos = Collections.synchronizedList(new ArrayList<>());
/*     */     
/*  36 */     this.primaryTimeout = U.getConnectionTimeout();
/*  37 */     Collections.addAll(this.repos, urls);
/*     */   }
/*     */   
/*     */   private String getUrl0(String uri) throws IOException {
/*  41 */     boolean canSelect = isSelectable();
/*     */     
/*  43 */     if (!canSelect) {
/*  44 */       return getRawUrl(uri);
/*     */     }
/*  46 */     Object lock = new Object();
/*     */     
/*  48 */     IOException e = null;
/*  49 */     int i = 0, attempt = 0;
/*  50 */     Time.start(lock);
/*  51 */     while (i < 3) {
/*  52 */       i++;
/*  53 */       int timeout = this.primaryTimeout * i;
/*     */       
/*  55 */       for (int x = 0; x < getCount(); x++) {
/*  56 */         attempt++;
/*  57 */         String url = getRepo(x);
/*  58 */         if (notShowLogs.stream().noneMatch(url::endsWith)) {
/*  59 */           log(new Object[] { "Attempt #" + attempt + "; timeout: " + timeout + " ms; url: " + url });
/*     */         }
/*     */         try {
/*  62 */           String result = Http.performGet(new URL(url + uri), timeout, timeout);
/*  63 */           if (!StringUtils.isEmpty(result))
/*     */           
/*  65 */           { setSelected(x);
/*     */             
/*  67 */             log(new Object[] { "Success: Reached the repo in", Long.valueOf(Time.stop(lock)), "ms." });
/*  68 */             return result; } 
/*  69 */         } catch (IOException e0) {
/*  70 */           if (notShowLogs.stream().noneMatch(url::endsWith))
/*  71 */             log(new Object[] { "request to url = " + url + uri }); 
/*  72 */           log(new Object[] { "Failed: Repo is not reachable!", e0 });
/*  73 */           e = e0;
/*     */         } 
/*     */       } 
/*     */     } 
/*  77 */     Time.stop(lock);
/*  78 */     log(new Object[] { "Failed: All repos are unreachable." });
/*  79 */     throw (IOException)Objects.requireNonNull(e);
/*     */   }
/*     */   
/*     */   public String getUrl(String uri) throws IOException {
/*  83 */     return getUrl0(uri);
/*     */   }
/*     */   
/*     */   public String getUrl() throws IOException {
/*  87 */     return getUrl0("");
/*     */   }
/*     */   
/*     */   private String getRawUrl(String uri) throws IOException {
/*  91 */     String url = getSelectedRepo() + Http.encode(uri);
/*     */     try {
/*  93 */       return Http.performGet(new URL(url));
/*  94 */     } catch (IOException e) {
/*  95 */       log(new Object[] { "Cannot get raw:", url });
/*  96 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getSelected() {
/* 101 */     return this.selected;
/*     */   }
/*     */   
/*     */   public synchronized void selectNext() {
/* 105 */     if (++this.selected >= getCount())
/* 106 */       this.selected = 0; 
/*     */   }
/*     */   
/*     */   public String getSelectedRepo() {
/* 110 */     return this.repos.get(this.selected);
/*     */   }
/*     */   
/*     */   public String getRepo(int pos) {
/* 114 */     return this.repos.get(pos);
/*     */   }
/*     */   
/*     */   public List<String> getList() {
/* 118 */     return this.repos;
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 122 */     return this.repos.size();
/*     */   }
/*     */   
/*     */   boolean isSelected() {
/* 126 */     return this.isSelected;
/*     */   }
/*     */   
/*     */   void setSelected(int pos) {
/* 130 */     if (!isSelectable()) {
/* 131 */       throw new IllegalStateException();
/*     */     }
/* 133 */     this.isSelected = true;
/* 134 */     this.selected = pos;
/*     */   }
/*     */   
/*     */   public boolean isSelectable() {
/* 138 */     return !this.repos.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     return this.name;
/*     */   }
/*     */   
/*     */   private void log(Object... obj) {
/* 147 */     U.log(new Object[] { "[REPO][" + this.name + "]", obj });
/*     */   }
/*     */   
/*     */   public String getName() {
/* 151 */     return this.name;
/*     */   }
/*     */   
/*     */   public void reorderedRepoSetFirstProxy() {
/* 155 */     this.repos.sort((e, e1) -> e.contains("resource.fastrepo.org/file") ? -1 : 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\repository\Repo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */