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
/*    */ public class CPString
/*    */   extends CPConstant<CPString>
/*    */ {
/*    */   private final String string;
/*    */   private final CPUTF8 utf8;
/*    */   
/*    */   public CPString(CPUTF8 utf8) {
/* 28 */     this.utf8 = utf8;
/* 29 */     this.string = utf8.getUnderlyingString();
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(CPString arg0) {
/* 34 */     return this.string.compareTo(arg0.string);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 39 */     return this.string;
/*    */   }
/*    */   
/*    */   public int getIndexInCpUtf8() {
/* 43 */     return this.utf8.getIndex();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\CPString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */