/*    */ package ch.qos.logback.core.pattern.color;
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
/*    */ public class BoldYellowCompositeConverter<E>
/*    */   extends ForegroundCompositeConverterBase<E>
/*    */ {
/*    */   protected String getForegroundColorCode(E event) {
/* 30 */     return "1;33";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\pattern\color\BoldYellowCompositeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */