/*    */ package org.tlauncher.tlauncher.ui.explorer.filters;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.regex.Pattern;
/*    */ import javax.swing.filechooser.FileFilter;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ 
/*    */ public class ImageFileFilter
/*    */   extends FileFilter {
/* 11 */   public static final Pattern extensionPattern = Pattern.compile("^(?:jp(?:e|)g|png)$", 2);
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean accept(File f) {
/* 16 */     String extension = FileUtil.getExtension(f);
/*    */     
/* 18 */     if (extension == null) {
/* 19 */       return true;
/*    */     }
/* 21 */     return extensionPattern.matcher(extension).matches();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDescription() {
/* 26 */     return Localizable.get("explorer.type.image");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\explorer\filters\ImageFileFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */