/*    */ package ch.qos.logback.core.joran.conditional;
/*    */ 
/*    */ import ch.qos.logback.core.joran.event.SaxEvent;
/*    */ import java.util.List;
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
/*    */ public class ThenAction
/*    */   extends ThenOrElseActionBase
/*    */ {
/*    */   void registerEventList(IfAction ifAction, List<SaxEvent> eventList) {
/* 24 */     ifAction.setThenSaxEventList(eventList);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\joran\conditional\ThenAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */