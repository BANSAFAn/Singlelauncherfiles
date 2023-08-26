/*    */ package org.apache.commons.compress.harmony.unpack200.bytecode.forms;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import org.apache.commons.compress.harmony.pack200.Pack200Exception;
/*    */ import org.apache.commons.compress.harmony.unpack200.SegmentConstantPool;
/*    */ import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
/*    */ import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
/*    */ import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;
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
/*    */ public abstract class ReferenceForm
/*    */   extends ByteCodeForm
/*    */ {
/*    */   public ReferenceForm(int opcode, String name, int[] rewrite) {
/* 33 */     super(opcode, name, rewrite);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract int getPoolID();
/*    */   
/*    */   protected abstract int getOffset(OperandManager paramOperandManager);
/*    */   
/*    */   protected void setNestedEntries(ByteCode byteCode, OperandManager operandManager, int offset) throws Pack200Exception {
/* 42 */     SegmentConstantPool globalPool = operandManager.globalConstantPool();
/* 43 */     ClassFileEntry[] nested = null;
/* 44 */     nested = new ClassFileEntry[] { (ClassFileEntry)globalPool.getConstantPoolEntry(getPoolID(), offset) };
/* 45 */     Objects.requireNonNull(nested[0], "Null nested entries are not allowed");
/* 46 */     byteCode.setNested(nested);
/* 47 */     byteCode.setNestedPositions(new int[][] { { 0, 2 } });
/*    */   }
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
/*    */   public void setByteCodeOperands(ByteCode byteCode, OperandManager operandManager, int codeLength) {
/* 62 */     int offset = getOffset(operandManager);
/*    */     try {
/* 64 */       setNestedEntries(byteCode, operandManager, offset);
/* 65 */     } catch (Pack200Exception ex) {
/* 66 */       throw new Error("Got a pack200 exception. What to do?");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\forms\ReferenceForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */