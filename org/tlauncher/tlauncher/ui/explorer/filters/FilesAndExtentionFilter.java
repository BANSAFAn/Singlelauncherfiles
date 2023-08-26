/*    */ package org.tlauncher.tlauncher.ui.explorer.filters;
/*    */ 
/*    */ import java.io.File;
/*    */ import javax.swing.filechooser.FileNameExtensionFilter;
/*    */ 
/*    */ public class FilesAndExtentionFilter
/*    */   extends FileFilter {
/*    */   private FileNameExtensionFilter extensionFilter;
/*    */   private String s1;
/*    */   
/*    */   public FilesAndExtentionFilter(String s1, String... s2) {
/* 12 */     this.s1 = s1;
/* 13 */     this.extensionFilter = new FileNameExtensionFilter(s1, s2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean accept(File f) {
/* 19 */     if (f.isDirectory())
/* 20 */       return true; 
/* 21 */     if (!super.accept(f))
/* 22 */       return false; 
/* 23 */     return this.extensionFilter.accept(f);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File dir, String name) {
/* 28 */     if ((new File(dir, name)).isDirectory())
/* 29 */       return true; 
/* 30 */     if (!super.accept(dir, name))
/* 31 */       return false; 
/* 32 */     return this.extensionFilter.accept(new File(dir, name));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 37 */     return this.s1;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\explorer\filters\FilesAndExtentionFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */