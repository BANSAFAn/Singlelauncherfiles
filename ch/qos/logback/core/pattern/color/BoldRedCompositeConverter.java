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
/*    */ public class BoldRedCompositeConverter<E>
/*    */   extends ForegroundCompositeConverterBase<E>
/*    */ {
/*    */   protected String getForegroundColorCode(E event) {
/* 28 */     return "1;31";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\pattern\color\BoldRedCompositeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */