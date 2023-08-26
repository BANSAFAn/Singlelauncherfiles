/*     */ package org.apache.http.conn.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.util.Args;
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
/*     */ @ThreadSafe
/*     */ public final class PublicSuffixMatcherLoader
/*     */ {
/*     */   private static volatile PublicSuffixMatcher DEFAULT_INSTANCE;
/*     */   
/*     */   private static PublicSuffixMatcher load(InputStream in) throws IOException {
/*  53 */     List<PublicSuffixList> lists = (new PublicSuffixListParser()).parseByType(new InputStreamReader(in, Consts.UTF_8));
/*     */     
/*  55 */     return new PublicSuffixMatcher(lists);
/*     */   }
/*     */   
/*     */   public static PublicSuffixMatcher load(URL url) throws IOException {
/*  59 */     Args.notNull(url, "URL");
/*  60 */     InputStream in = url.openStream();
/*     */     try {
/*  62 */       return load(in);
/*     */     } finally {
/*  64 */       in.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static PublicSuffixMatcher load(File file) throws IOException {
/*  69 */     Args.notNull(file, "File");
/*  70 */     InputStream in = new FileInputStream(file);
/*     */     try {
/*  72 */       return load(in);
/*     */     } finally {
/*  74 */       in.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicSuffixMatcher getDefault() {
/*  81 */     if (DEFAULT_INSTANCE == null) {
/*  82 */       synchronized (PublicSuffixMatcherLoader.class) {
/*  83 */         if (DEFAULT_INSTANCE == null) {
/*  84 */           URL url = PublicSuffixMatcherLoader.class.getResource("/mozilla/public-suffix-list.txt");
/*     */           
/*  86 */           if (url != null) {
/*     */             try {
/*  88 */               DEFAULT_INSTANCE = load(url);
/*  89 */             } catch (IOException ex) {
/*     */               
/*  91 */               Log log = LogFactory.getLog(PublicSuffixMatcherLoader.class);
/*  92 */               if (log.isWarnEnabled()) {
/*  93 */                 log.warn("Failure loading public suffix list from default resource", ex);
/*     */               }
/*     */             } 
/*     */           } else {
/*  97 */             DEFAULT_INSTANCE = new PublicSuffixMatcher(Arrays.asList(new String[] { "com" }, ), null);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 102 */     return DEFAULT_INSTANCE;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\con\\util\PublicSuffixMatcherLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */