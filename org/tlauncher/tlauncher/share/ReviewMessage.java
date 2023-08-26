/*    */ package org.tlauncher.tlauncher.share;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class ReviewMessage
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 5899496321871739561L;
/*    */   private String title;
/*    */   private String description;
/*    */   private String mailReview;
/*    */   private String typeReview;
/*    */   private List<String> listNamesOfFiles;
/*    */   
/*    */   public ReviewMessage(String title, String description, String mailReview, String typeReview, List<String> listNamesOfFiles) {
/* 19 */     this.title = title;
/* 20 */     this.description = description;
/* 21 */     this.mailReview = mailReview;
/* 22 */     this.typeReview = typeReview;
/* 23 */     this.listNamesOfFiles = listNamesOfFiles;
/*    */   }
/*    */ 
/*    */   
/*    */   public ReviewMessage() {
/* 28 */     this.listNamesOfFiles = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public String getTitle() {
/* 32 */     return this.title;
/*    */   }
/*    */   
/*    */   public void setTitle(String title) {
/* 36 */     this.title = title;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 40 */     return this.description;
/*    */   }
/*    */   
/*    */   public void setDescription(String description) {
/* 44 */     this.description = description;
/*    */   }
/*    */   
/*    */   public String getMailReview() {
/* 48 */     return this.mailReview;
/*    */   }
/*    */   
/*    */   public void setMailReview(String mailReview) {
/* 52 */     this.mailReview = mailReview;
/*    */   }
/*    */   
/*    */   public String getTypeReview() {
/* 56 */     return this.typeReview;
/*    */   }
/*    */   
/*    */   public void setTypeReview(String typeReview) {
/* 60 */     this.typeReview = typeReview;
/*    */   }
/*    */   
/*    */   public List<String> getListNamesOfFiles() {
/* 64 */     return this.listNamesOfFiles;
/*    */   }
/*    */   
/*    */   public void setListNamesOfFiles(List<String> listNamesOfFiles) {
/* 68 */     this.listNamesOfFiles = listNamesOfFiles;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\share\ReviewMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */