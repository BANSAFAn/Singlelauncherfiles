/*    */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
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
/*    */ public class CPUTF8
/*    */   extends ConstantPoolEntry
/*    */ {
/*    */   private final String utf8;
/*    */   private boolean hashcodeComputed;
/*    */   private int cachedHashCode;
/*    */   
/*    */   public CPUTF8(String utf8, int globalIndex) {
/* 38 */     super((byte)1, globalIndex);
/* 39 */     this.utf8 = Objects.<String>requireNonNull(utf8, "utf8");
/*    */   }
/*    */   
/*    */   public CPUTF8(String string) {
/* 43 */     this(string, -1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 48 */     if (this == obj) {
/* 49 */       return true;
/*    */     }
/* 51 */     if (obj == null) {
/* 52 */       return false;
/*    */     }
/* 54 */     if (getClass() != obj.getClass()) {
/* 55 */       return false;
/*    */     }
/* 57 */     CPUTF8 other = (CPUTF8)obj;
/* 58 */     return this.utf8.equals(other.utf8);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void generateHashCode() {
/* 65 */     this.hashcodeComputed = true;
/* 66 */     int PRIME = 31;
/* 67 */     this.cachedHashCode = 31 + this.utf8.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 72 */     if (!this.hashcodeComputed) {
/* 73 */       generateHashCode();
/*    */     }
/* 75 */     return this.cachedHashCode;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 80 */     return "UTF8: " + this.utf8;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeBody(DataOutputStream dos) throws IOException {
/* 85 */     dos.writeUTF(this.utf8);
/*    */   }
/*    */   
/*    */   public String underlyingString() {
/* 89 */     return this.utf8;
/*    */   }
/*    */   
/*    */   public void setGlobalIndex(int index) {
/* 93 */     this.globalIndex = index;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\CPUTF8.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */