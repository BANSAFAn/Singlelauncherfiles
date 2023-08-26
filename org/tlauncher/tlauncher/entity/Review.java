/*    */ package org.tlauncher.tlauncher.entity;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Review
/*    */ {
/*    */   private String title;
/*    */   private String description;
/*    */   private String mailReview;
/*    */   private String typeReview;
/*    */   private List<File> listFiles;
/*    */   
/*    */   public Review() {}
/*    */   
/*    */   public Review(String title, String description, String mailReview, String typeReview, List<File> listFiles) {
/* 19 */     this.title = title;
/* 20 */     this.description = description;
/* 21 */     this.mailReview = mailReview;
/* 22 */     this.typeReview = typeReview;
/* 23 */     this.listFiles = listFiles;
/*    */   }
/*    */   public String getTitle() {
/* 26 */     return this.title;
/*    */   }
/*    */   public void setTitle(String title) {
/* 29 */     this.title = title;
/*    */   }
/*    */   public String getDescription() {
/* 32 */     return this.description;
/*    */   }
/*    */   public void setDescription(String description) {
/* 35 */     this.description = description;
/*    */   }
/*    */   public String getMailReview() {
/* 38 */     return this.mailReview;
/*    */   }
/*    */   public void setMailReview(String mailReview) {
/* 41 */     this.mailReview = mailReview;
/*    */   }
/*    */   public String getTypeReview() {
/* 44 */     return this.typeReview;
/*    */   }
/*    */   public void setTypeReview(String typeReview) {
/* 47 */     this.typeReview = typeReview;
/*    */   }
/*    */   public List<File> getListFiles() {
/* 50 */     return this.listFiles;
/*    */   }
/*    */   public void setListFiles(List<File> listFiles) {
/* 53 */     this.listFiles = listFiles;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\Review.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */