/*    */ package org.tlauncher.tlauncher.ui.explorer;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.FileDialog;
/*    */ import java.io.File;
/*    */ import java.io.FilenameFilter;
/*    */ import java.util.Objects;
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.JFrame;
/*    */ import org.tlauncher.tlauncher.ui.explorer.filters.CommonFilter;
/*    */ 
/*    */ public class FileWrapper
/*    */   implements FileChooser {
/*    */   private CommonFilter filter;
/*    */   private FileDialog dialog;
/*    */   private File directory;
/*    */   private File selectedFile;
/*    */   private boolean multiSelectionMode = false;
/*    */   private String title;
/*    */   
/*    */   private FileDialog create(Component component) {
/* 22 */     if (component instanceof JFrame)
/* 23 */     { this.dialog = new FileDialog((JFrame)component); }
/* 24 */     else if (component instanceof JDialog)
/* 25 */     { this.dialog = new FileDialog((JDialog)component); }
/* 26 */     else { this.dialog = new FileDialog(new JFrame()); }
/*    */     
/* 28 */     this.dialog.setAlwaysOnTop(true);
/* 29 */     this.dialog.setFilenameFilter((FilenameFilter)this.filter);
/* 30 */     if (Objects.nonNull(this.selectedFile))
/* 31 */       this.dialog.setFile(this.selectedFile.getAbsolutePath()); 
/* 32 */     if (Objects.nonNull(this.directory))
/* 33 */       this.dialog.setDirectory(this.directory.getAbsolutePath()); 
/* 34 */     this.dialog.setMultipleMode(this.multiSelectionMode);
/* 35 */     if (Objects.nonNull(this.title)) {
/* 36 */       this.dialog.setTitle(this.title);
/*    */     }
/* 38 */     return this.dialog;
/*    */   }
/*    */   
/*    */   public int showSaveDialog(Component component) {
/* 42 */     this.dialog = create(component);
/* 43 */     this.dialog.setMode(1);
/* 44 */     this.dialog.show();
/* 45 */     if (Objects.nonNull(this.dialog.getFile()))
/* 46 */       return 0; 
/* 47 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public File getSelectedFile() {
/* 52 */     return new File(this.dialog.getDirectory(), this.dialog.getFile());
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSelectedFile(File file) {
/* 57 */     this.selectedFile = file;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCurrentDirectory(File file) {
/* 62 */     this.directory = file;
/*    */   }
/*    */   
/*    */   public int showDialog(Component parent) {
/* 66 */     this.dialog = create(parent);
/* 67 */     this.dialog.setMode(0);
/* 68 */     this.dialog.show();
/* 69 */     if (Objects.nonNull(this.dialog.getFile()))
/* 70 */       return 0; 
/* 71 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public File[] getSelectedFiles() {
/* 76 */     return this.dialog.getFiles();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFileFilter(CommonFilter filter) {
/* 81 */     this.filter = filter;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setMultiSelectionEnabled(boolean b) {
/* 86 */     this.multiSelectionMode = b;
/*    */   }
/*    */ 
/*    */   
/*    */   public int showDialog(Component component, String s) {
/* 91 */     setDialogTitle(s);
/* 92 */     return showDialog(component);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDialogTitle(String s) {
/* 97 */     this.title = s;
/*    */   }
/*    */   
/*    */   public void setFileSelectionMode(int mode) {}
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\explorer\FileWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */