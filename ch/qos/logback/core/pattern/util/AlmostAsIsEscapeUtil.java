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
/*    */ public class AlmostAsIsEscapeUtil
/*    */   extends RestrictedEscapeUtil
/*    */ {
/*    */   public void escape(String escapeChars, StringBuffer buf, char next, int pointer) {
/* 42 */     super.escape("%)", buf, next, pointer);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\patter\\util\AlmostAsIsEscapeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */