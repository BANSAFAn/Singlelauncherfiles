/*    */ package org.tlauncher.util.salf.connection;
/*    */ 
/*    */ public class ArchiveFilesDescription
/*    */ {
/*    */   private String nameFileArchive;
/*    */   private int countFiles;
/*    */   private int[] sizeFiles;
/*    */   
/*    */   public ArchiveFilesDescription() {}
/*    */   
/*    */   public ArchiveFilesDescription(String nameFileArchive, int countFiles, int[] sizeFiles) {
/* 12 */     this.nameFileArchive = nameFileArchive;
/* 13 */     this.countFiles = countFiles;
/* 14 */     this.sizeFiles = sizeFiles;
/*    */   }
/*    */   
/*    */   public String getNameFileArchive() {
/* 18 */     return this.nameFileArchive;
/*    */   }
/*    */   
/*    */   public void setNameFileArchive(String nameFileArchive) {
/* 22 */     this.nameFileArchive = nameFileArchive;
/*    */   }
/*    */   
/*    */   public int getCountFiles() {
/* 26 */     return this.countFiles;
/*    */   }
/*    */   
/*    */   public void setCountFiles(int countFiles) {
/* 30 */     this.countFiles = countFiles;
/*    */   }
/*    */   
/*    */   public int[] getSizeFiles() {
/* 34 */     return this.sizeFiles;
/*    */   }
/*    */   
/*    */   public void setSizeFiles(int[] sizeFiles) {
/* 38 */     this.sizeFiles = sizeFiles;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\salf\connection\ArchiveFilesDescription.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */