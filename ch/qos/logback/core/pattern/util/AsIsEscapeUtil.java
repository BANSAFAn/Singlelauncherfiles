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
/*    */ public class AsIsEscapeUtil
/*    */   implements IEscapeUtil
/*    */ {
/*    */   public void escape(String escapeChars, StringBuffer buf, char next, int pointer) {
/* 30 */     buf.append("\\");
/*    */     
/* 32 */     buf.append(next);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\patter\\util\AsIsEscapeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */