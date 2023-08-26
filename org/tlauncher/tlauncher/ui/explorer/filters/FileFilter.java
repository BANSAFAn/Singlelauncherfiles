/*    */ package org.tlauncher.tlauncher.ui.explorer.filters;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ public class FileFilter
/*    */   extends CommonFilter {
/*    */   public boolean accept(File dir, String name) {
/*  8 */     return (new File(dir, name)).isFile();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File f) {
/* 13 */     return f.isFile();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 18 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\explorer\filters\FileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */