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
/*    */ public class LoggerConverter
/*    */   extends NamedConverter
/*    */ {
/*    */   protected String getFullyQualifiedName(ILoggingEvent event) {
/* 21 */     return event.getLoggerName();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\classic\pattern\LoggerConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */