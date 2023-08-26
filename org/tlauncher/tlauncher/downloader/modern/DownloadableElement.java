/*    */ package org.tlauncher.tlauncher.downloader.modern;
/*    */ 
/*    */ import java.nio.file.Path;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DownloadableElement
/*    */ {
/*    */   private List<String> inList;
/*    */   private Path out;
/*    */   
/*    */   public DownloadableElement(List<String> outList, Path in) {
/* 16 */     this.inList = outList;
/* 17 */     this.out = in;
/*    */   }
/*    */   
/*    */   public List<String> getInList() {
/* 21 */     return this.inList;
/*    */   }
/*    */   
/*    */   public void setInList(List<String> inList) {
/* 25 */     this.inList = inList;
/*    */   }
/*    */   
/*    */   public Path getOut() {
/* 29 */     return this.out;
/*    */   }
/*    */   
/*    */   public void setOut(Path out) {
/* 33 */     this.out = out;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\modern\DownloadableElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */