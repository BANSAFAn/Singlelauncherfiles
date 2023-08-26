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
/*    */ public class CPInt
/*    */   extends CPConstant<CPInt>
/*    */ {
/*    */   private final int theInt;
/*    */   
/*    */   public CPInt(int theInt) {
/* 27 */     this.theInt = theInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(CPInt obj) {
/* 32 */     return Integer.compare(this.theInt, obj.theInt);
/*    */   }
/*    */   
/*    */   public int getInt() {
/* 36 */     return this.theInt;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\CPInt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */