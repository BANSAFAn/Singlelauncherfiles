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
/*    */ public class BoldWhiteCompositeConverter<E>
/*    */   extends ForegroundCompositeConverterBase<E>
/*    */ {
/*    */   protected String getForegroundColorCode(E event) {
/* 30 */     return "1;37";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\pattern\color\BoldWhiteCompositeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */