/*    */ package org.tlauncher.exceptions;
/*    */ 
/*    */ public class ParseException extends RuntimeException {
/*    */   public ParseException(String string) {
/*  5 */     super(string);
/*    */   }
/*    */   private static final long serialVersionUID = -3231272464953548141L;
/*    */   public ParseException(String message, Throwable cause) {
/*  9 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public ParseException(Throwable cause) {
/* 13 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\exceptions\ParseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */