/*    */ package org.tlauncher.tlauncher.ui.util;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidateUtil
/*    */ {
/*    */   private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
/*    */   
/*    */   public static boolean validateEmail(String line) {
/* 12 */     return Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(line).matches();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\util\ValidateUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */