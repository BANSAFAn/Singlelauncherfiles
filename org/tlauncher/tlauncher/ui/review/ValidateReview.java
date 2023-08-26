/*    */ package org.tlauncher.tlauncher.ui.review;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ import org.tlauncher.tlauncher.entity.Review;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidateReview
/*    */ {
/*    */   private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
/*    */   
/*    */   public static boolean validate(Review review) {
/* 15 */     if (review.getDescription().isEmpty()) {
/* 16 */       Alert.showWarning("", Localizable.get().get("review.message.fill") + " " + 
/* 17 */           Localizable.get().get("review.message.description"));
/* 18 */       return false;
/* 19 */     }  if (review.getDescription().equals(Localizable.get("review.description"))) {
/* 20 */       Alert.showWarning("", Localizable.get().get("review.message.fill") + " " + 
/* 21 */           Localizable.get().get("review.message.description"));
/* 22 */       return false;
/*    */     } 
/* 24 */     String mail = review.getMailReview();
/* 25 */     if (mail.isEmpty()) {
/* 26 */       Alert.showLocWarning("review.message.email.invalid");
/* 27 */       return false;
/*    */     } 
/* 29 */     if (mail.startsWith("https://vk.com") || mail
/* 30 */       .startsWith("http://vk.com") || mail
/* 31 */       .startsWith("vk.com"))
/* 32 */       return true; 
/* 33 */     if (!Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(review.getMailReview()).matches()) {
/* 34 */       Alert.showWarning("", Localizable.get().get("review.message.email.invalid"));
/* 35 */       return false;
/*    */     } 
/* 37 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\review\ValidateReview.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */