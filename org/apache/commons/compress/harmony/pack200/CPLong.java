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
/*    */ public class CPLong
/*    */   extends CPConstant<CPLong>
/*    */ {
/*    */   private final long theLong;
/*    */   
/*    */   public CPLong(long theLong) {
/* 27 */     this.theLong = theLong;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(CPLong obj) {
/* 32 */     return Long.compare(this.theLong, obj.theLong);
/*    */   }
/*    */   
/*    */   public long getLong() {
/* 36 */     return this.theLong;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 41 */     return "" + this.theLong;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\CPLong.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */