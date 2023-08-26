/*    */ package org.tlauncher.tlauncher.ui.explorer;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.io.File;
/*    */ import javax.swing.JFileChooser;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.filechooser.FileFilter;
/*    */ import javax.swing.filechooser.FileSystemView;
/*    */ import org.tlauncher.tlauncher.ui.explorer.filters.CommonFilter;
/*    */ 
/*    */ public class FileExplorer
/*    */   implements FileChooser {
/* 13 */   private JFileChooser fileChooser = new JFileChooser();
/*    */   
/*    */   public synchronized void setFileFilter(CommonFilter filter) {
/* 16 */     this.fileChooser.setFileFilter((FileFilter)filter);
/*    */   }
/*    */ 
/*    */   
/*    */   public int showSaveDialog(Component component) {
/* 21 */     return this.fileChooser.showSaveDialog(component);
/*    */   }
/*    */ 
/*    */   
/*    */   public File getSelectedFile() {
/* 26 */     return this.fileChooser.getSelectedFile();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSelectedFile(File file) {
/* 31 */     this.fileChooser.setSelectedFile(file);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCurrentDirectory(File dir) {
/* 36 */     if (dir == null)
/* 37 */       dir = FileSystemView.getFileSystemView().getDefaultDirectory(); 
/* 38 */     this.fileChooser.setCurrentDirectory(dir);
/*    */   }
/*    */   
/*    */   public int showDialog(Component parent) {
/* 42 */     return showDialog(parent, 
/* 43 */         UIManager.getString("FileChooser.directoryOpenButtonText"));
/*    */   }
/*    */ 
/*    */   
/*    */   public File[] getSelectedFiles() {
/* 48 */     File[] selectedFiles = this.fileChooser.getSelectedFiles();
/*    */     
/* 50 */     if (selectedFiles.length > 0) {
/* 51 */       return selectedFiles;
/*    */     }
/* 53 */     File selectedFile = this.fileChooser.getSelectedFile();
/*    */     
/* 55 */     if (selectedFile == null) {
/* 56 */       return null;
/*    */     }
/* 58 */     return new File[] { selectedFile };
/*    */   }
/*    */ 
/*    */   
/*    */   public void setMultiSelectionEnabled(boolean b) {
/* 63 */     this.fileChooser.setMultiSelectionEnabled(b);
/*    */   }
/*    */ 
/*    */   
/*    */   public int showDialog(Component component, String s) {
/* 68 */     return this.fileChooser.showDialog(component, s);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDialogTitle(String s) {
/* 73 */     this.fileChooser.setDialogTitle(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setFileSelectionMode(int mode) {
/* 78 */     this.fileChooser.setFileSelectionMode(mode);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\explorer\FileExplorer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */