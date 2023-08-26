/*    */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.apache.commons.compress.harmony.pack200.Pack200Exception;
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
/*    */ public abstract class BCIRenumberedAttribute
/*    */   extends Attribute
/*    */ {
/*    */   protected boolean renumbered;
/*    */   
/*    */   public boolean hasBCIRenumbering() {
/* 40 */     return true;
/*    */   }
/*    */   
/*    */   public BCIRenumberedAttribute(CPUTF8 attributeName) {
/* 44 */     super(attributeName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract int getLength();
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void writeBody(DataOutputStream paramDataOutputStream) throws IOException;
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract String toString();
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract int[] getStartPCs();
/*    */ 
/*    */ 
/*    */   
/*    */   public void renumber(List<Integer> byteCodeOffsets) throws Pack200Exception {
/* 67 */     if (this.renumbered) {
/* 68 */       throw new Error("Trying to renumber a line number table that has already been renumbered");
/*    */     }
/* 70 */     this.renumbered = true;
/* 71 */     int[] startPCs = getStartPCs();
/* 72 */     Arrays.setAll(startPCs, i -> ((Integer)byteCodeOffsets.get(startPCs[i])).intValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\BCIRenumberedAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */