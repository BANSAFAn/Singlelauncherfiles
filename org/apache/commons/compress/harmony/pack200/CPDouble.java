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
/*    */ public class CPDouble
/*    */   extends CPConstant<CPDouble>
/*    */ {
/*    */   private final double theDouble;
/*    */   
/*    */   public CPDouble(double theDouble) {
/* 27 */     this.theDouble = theDouble;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(CPDouble obj) {
/* 32 */     return Double.compare(this.theDouble, obj.theDouble);
/*    */   }
/*    */   
/*    */   public double getDouble() {
/* 36 */     return this.theDouble;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\CPDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */