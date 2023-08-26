/*    */ package org.apache.commons.compress.harmony.unpack200.bytecode.forms;
/*    */ 
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
/*    */ public class SuperInitMethodRefForm
/*    */   extends InitMethodReferenceForm
/*    */ {
/*    */   public SuperInitMethodRefForm(int opcode, String name, int[] rewrite) {
/* 25 */     super(opcode, name, rewrite);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected String context(OperandManager operandManager) {
/* 31 */     return operandManager.getSuperClass();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\forms\SuperInitMethodRefForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */