/*    */ package org.tlauncher.tlauncher.ui.explorer.filters;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ public class FolderFilter
/*    */   extends CommonFilter {
/*    */   public boolean accept(File pathname) {
/*  8 */     return pathname.isDirectory();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File dir, String name) {
/* 13 */     return (new File(dir, name)).isDirectory();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 18 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\explorer\filters\FolderFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */