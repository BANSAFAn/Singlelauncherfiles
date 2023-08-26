/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
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
/*     */ public class FileDeleteStrategy
/*     */ {
/*     */   static class ForceFileDeleteStrategy
/*     */     extends FileDeleteStrategy
/*     */   {
/*     */     ForceFileDeleteStrategy() {
/*  43 */       super("Force");
/*     */     }
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
/*     */     protected boolean doDelete(File fileToDelete) throws IOException {
/*  60 */       FileUtils.forceDelete(fileToDelete);
/*  61 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static final FileDeleteStrategy NORMAL = new FileDeleteStrategy("Normal");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final FileDeleteStrategy FORCE = new ForceFileDeleteStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FileDeleteStrategy(String name) {
/*  86 */     this.name = name;
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
/*     */   public void delete(File fileToDelete) throws IOException {
/* 101 */     if (fileToDelete.exists() && !doDelete(fileToDelete)) {
/* 102 */       throw new IOException("Deletion failed: " + fileToDelete);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean deleteQuietly(File fileToDelete) {
/* 118 */     if (fileToDelete == null || !fileToDelete.exists()) {
/* 119 */       return true;
/*     */     }
/*     */     try {
/* 122 */       return doDelete(fileToDelete);
/* 123 */     } catch (IOException ex) {
/* 124 */       return false;
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
/*     */   protected boolean doDelete(File file) throws IOException {
/* 147 */     FileUtils.delete(file);
/* 148 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     return "FileDeleteStrategy[" + this.name + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\FileDeleteStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */