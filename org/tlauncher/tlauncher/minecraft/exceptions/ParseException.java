/*    */ package org.tlauncher.tlauncher.minecraft.exceptions;
/*    */ 
/*    */ public class ParseException extends RuntimeException {
/*    */   private static final long serialVersionUID = -3231272464953548141L;
/*    */   
/*    */   public ParseException(String string) {
/*  7 */     super(string);
/*    */   }
/*    */   
/*    */   public ParseException(String message, Throwable cause) {
/* 11 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public ParseException(Throwable cause) {
/* 15 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\exceptions\ParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */