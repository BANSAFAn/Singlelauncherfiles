/*    */ package ch.qos.logback.core.pattern;
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
/*    */ public class IdentityCompositeConverter<E>
/*    */   extends CompositeConverter<E>
/*    */ {
/*    */   protected String transform(E event, String in) {
/* 20 */     return in;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\pattern\IdentityCompositeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */