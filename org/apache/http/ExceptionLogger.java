/*    */ package org.apache.http;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ExceptionLogger
/*    */ {
/* 34 */   public static final ExceptionLogger NO_OP = new ExceptionLogger()
/*    */     {
/*    */       public void log(Exception ex) {}
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public static final ExceptionLogger STD_ERR = new ExceptionLogger()
/*    */     {
/*    */       public void log(Exception ex)
/*    */       {
/* 46 */         ex.printStackTrace();
/*    */       }
/*    */     };
/*    */   
/*    */   void log(Exception paramException);
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\ExceptionLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */