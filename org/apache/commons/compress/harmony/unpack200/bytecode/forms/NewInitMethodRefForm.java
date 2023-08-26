/*    */ package org.apache.commons.compress.harmony.unpack200.bytecode.forms;
/*    */ 
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
/*    */ public class NewInitMethodRefForm
/*    */   extends InitMethodReferenceForm
/*    */ {
/*    */   public NewInitMethodRefForm(int opcode, String name, int[] rewrite) {
/* 32 */     super(opcode, name, rewrite);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String context(OperandManager operandManager) {
/* 37 */     return operandManager.getNewClass();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setNestedEntries(ByteCode byteCode, OperandManager operandManager, int offset) throws Pack200Exception {
/* 43 */     SegmentConstantPool globalPool = operandManager.globalConstantPool();
/* 44 */     ClassFileEntry[] nested = null;
/*    */     
/* 46 */     nested = new ClassFileEntry[] { (ClassFileEntry)globalPool.getInitMethodPoolEntry(11, offset, context(operandManager)) };
/* 47 */     byteCode.setNested(nested);
/* 48 */     byteCode.setNestedPositions(new int[][] { { 0, 2 } });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\forms\NewInitMethodRefForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */