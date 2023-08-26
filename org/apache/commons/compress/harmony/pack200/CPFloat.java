/*    */ package org.apache.commons.compress.harmony.pack200;
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
/*    */ public class CPFloat
/*    */   extends CPConstant<CPFloat>
/*    */ {
/*    */   private final float theFloat;
/*    */   
/*    */   public CPFloat(float theFloat) {
/* 27 */     this.theFloat = theFloat;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(CPFloat obj) {
/* 32 */     return Float.compare(this.theFloat, obj.theFloat);
/*    */   }
/*    */   
/*    */   public float getFloat() {
/* 36 */     return this.theFloat;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\CPFloat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */