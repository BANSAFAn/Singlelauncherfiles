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
/*    */ public class BoldGreenCompositeConverter<E>
/*    */   extends ForegroundCompositeConverterBase<E>
/*    */ {
/*    */   protected String getForegroundColorCode(E event) {
/* 30 */     return "1;32";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\pattern\color\BoldGreenCompositeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */