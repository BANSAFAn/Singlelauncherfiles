/*    */ package ch.qos.logback.core.pattern.util;
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
/*    */ public class RestrictedEscapeUtil
/*    */   implements IEscapeUtil
/*    */ {
/*    */   public void escape(String escapeChars, StringBuffer buf, char next, int pointer) {
/* 24 */     if (escapeChars.indexOf(next) >= 0) {
/* 25 */       buf.append(next);
/*    */     }
/*    */     else {
/*    */       
/* 29 */       buf.append("\\");
/*    */       
/* 31 */       buf.append(next);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\patter\\util\RestrictedEscapeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */