/*    */ package org.tlauncher.tlauncher.ui.explorer.filters;
/*    */ 
/*    */ import java.io.File;
/*    */ import javax.swing.filechooser.FileFilter;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ 
/*    */ public class ExtensionFileFilter
/*    */   extends FileFilter {
/*    */   private final String extension;
/*    */   private final boolean acceptNull;
/*    */   
/*    */   public ExtensionFileFilter(String extension, boolean acceptNullExtension) {
/* 14 */     if (extension == null) {
/* 15 */       throw new NullPointerException("Extension is NULL!");
/*    */     }
/* 17 */     if (extension.isEmpty()) {
/* 18 */       throw new IllegalArgumentException("Extension is empty!");
/*    */     }
/* 20 */     this.extension = extension;
/*    */     
/* 22 */     this.acceptNull = acceptNullExtension;
/*    */   }
/*    */   
/*    */   public ExtensionFileFilter(String extension) {
/* 26 */     this(extension, true);
/*    */   }
/*    */   
/*    */   public String getExtension() {
/* 30 */     return this.extension;
/*    */   }
/*    */   
/*    */   public boolean acceptsNull() {
/* 34 */     return this.acceptNull;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accept(File f) {
/* 39 */     String currentExtension = FileUtil.getExtension(f);
/*    */     
/* 41 */     if (this.acceptNull && currentExtension == null) {
/* 42 */       return true;
/*    */     }
/* 44 */     return this.extension.equals(currentExtension);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 49 */     return Localizable.get("explorer.extension.format", new Object[] { this.extension.toUpperCase() });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\explorer\filters\ExtensionFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */