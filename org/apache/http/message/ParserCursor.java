/*    */ package org.apache.http.message;
/*    */ 
/*    */ import org.apache.http.annotation.NotThreadSafe;
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
/*    */ @NotThreadSafe
/*    */ public class ParserCursor
/*    */ {
/*    */   private final int lowerBound;
/*    */   private final int upperBound;
/*    */   private int pos;
/*    */   
/*    */   public ParserCursor(int lowerBound, int upperBound) {
/* 50 */     if (lowerBound < 0) {
/* 51 */       throw new IndexOutOfBoundsException("Lower bound cannot be negative");
/*    */     }
/* 53 */     if (lowerBound > upperBound) {
/* 54 */       throw new IndexOutOfBoundsException("Lower bound cannot be greater then upper bound");
/*    */     }
/* 56 */     this.lowerBound = lowerBound;
/* 57 */     this.upperBound = upperBound;
/* 58 */     this.pos = lowerBound;
/*    */   }
/*    */   
/*    */   public int getLowerBound() {
/* 62 */     return this.lowerBound;
/*    */   }
/*    */   
/*    */   public int getUpperBound() {
/* 66 */     return this.upperBound;
/*    */   }
/*    */   
/*    */   public int getPos() {
/* 70 */     return this.pos;
/*    */   }
/*    */   
/*    */   public void updatePos(int pos) {
/* 74 */     if (pos < this.lowerBound) {
/* 75 */       throw new IndexOutOfBoundsException("pos: " + pos + " < lowerBound: " + this.lowerBound);
/*    */     }
/* 77 */     if (pos > this.upperBound) {
/* 78 */       throw new IndexOutOfBoundsException("pos: " + pos + " > upperBound: " + this.upperBound);
/*    */     }
/* 80 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public boolean atEnd() {
/* 84 */     return (this.pos >= this.upperBound);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     StringBuilder buffer = new StringBuilder();
/* 90 */     buffer.append('[');
/* 91 */     buffer.append(Integer.toString(this.lowerBound));
/* 92 */     buffer.append('>');
/* 93 */     buffer.append(Integer.toString(this.pos));
/* 94 */     buffer.append('>');
/* 95 */     buffer.append(Integer.toString(this.upperBound));
/* 96 */     buffer.append(']');
/* 97 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\ParserCursor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */