/*    */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ public abstract class CPConstant
/*    */   extends ConstantPoolEntry
/*    */ {
/*    */   private final Object value;
/*    */   
/*    */   public CPConstant(byte tag, Object value, int globalIndex) {
/* 37 */     super(tag, globalIndex);
/* 38 */     this.value = Objects.requireNonNull(value, "value");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 43 */     if (this == obj) {
/* 44 */       return true;
/*    */     }
/* 46 */     if (obj == null) {
/* 47 */       return false;
/*    */     }
/* 49 */     if (getClass() != obj.getClass()) {
/* 50 */       return false;
/*    */     }
/* 52 */     CPConstant other = (CPConstant)obj;
/* 53 */     if (this.value == null) {
/* 54 */       if (other.value != null) {
/* 55 */         return false;
/*    */       }
/* 57 */     } else if (!this.value.equals(other.value)) {
/* 58 */       return false;
/*    */     } 
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 65 */     int PRIME = 31;
/* 66 */     int result = 1;
/* 67 */     result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
/* 68 */     return result;
/*    */   }
/*    */   
/*    */   protected Object getValue() {
/* 72 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\CPConstant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */