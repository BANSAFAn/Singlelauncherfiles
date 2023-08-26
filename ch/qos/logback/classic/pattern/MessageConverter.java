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
/*    */ public class MessageConverter
/*    */   extends ClassicConverter
/*    */ {
/*    */   public String convert(ILoggingEvent event) {
/* 26 */     return event.getFormattedMessage();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\classic\pattern\MessageConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */