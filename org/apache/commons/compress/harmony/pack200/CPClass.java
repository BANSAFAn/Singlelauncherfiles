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
/*    */ public class CPClass
/*    */   extends CPConstant<CPClass>
/*    */ {
/*    */   private final String className;
/*    */   private final CPUTF8 utf8;
/*    */   private final boolean isInnerClass;
/*    */   
/*    */   public CPClass(CPUTF8 utf8) {
/* 29 */     this.utf8 = utf8;
/* 30 */     this.className = utf8.getUnderlyingString();
/* 31 */     char[] chars = this.className.toCharArray();
/* 32 */     for (char element : chars) {
/* 33 */       if (element <= '-') {
/* 34 */         this.isInnerClass = true;
/*    */         return;
/*    */       } 
/*    */     } 
/* 38 */     this.isInnerClass = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(CPClass arg0) {
/* 43 */     return this.className.compareTo(arg0.className);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 48 */     return this.className;
/*    */   }
/*    */   
/*    */   public int getIndexInCpUtf8() {
/* 52 */     return this.utf8.getIndex();
/*    */   }
/*    */   
/*    */   public boolean isInnerClass() {
/* 56 */     return this.isInnerClass;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\CPClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */