/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
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
/*    */ public class ContextNameConverter
/*    */   extends ClassicConverter
/*    */ {
/*    */   public String convert(ILoggingEvent event) {
/* 29 */     return event.getLoggerContextVO().getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\classic\pattern\ContextNameConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */