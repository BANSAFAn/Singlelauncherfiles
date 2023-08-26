/*     */ package org.apache.commons.compress.harmony.unpack200.bytecode.forms;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.ByteCode;
/*     */ import org.apache.commons.compress.harmony.unpack200.bytecode.OperandManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TableSwitchForm
/*     */   extends SwitchForm
/*     */ {
/*     */   public TableSwitchForm(int opcode, String name) {
/*  27 */     super(opcode, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setByteCodeOperands(ByteCode byteCode, OperandManager operandManager, int codeLength) {
/*  41 */     int case_count = operandManager.nextCaseCount();
/*  42 */     int default_pc = operandManager.nextLabel();
/*  43 */     int case_value = -1;
/*  44 */     case_value = operandManager.nextCaseValues();
/*     */     
/*  46 */     int[] case_pcs = new int[case_count];
/*  47 */     Arrays.setAll(case_pcs, i -> operandManager.nextLabel());
/*     */     
/*  49 */     int[] labelsArray = new int[case_count + 1];
/*  50 */     labelsArray[0] = default_pc;
/*  51 */     System.arraycopy(case_pcs, 0, labelsArray, 1, case_count + 1 - 1);
/*  52 */     byteCode.setByteCodeTargets(labelsArray);
/*     */     
/*  54 */     int lowValue = case_value;
/*  55 */     int highValue = lowValue + case_count - 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     int padLength = 3 - codeLength % 4;
/*  69 */     int rewriteSize = 1 + padLength + 4 + 4 + 4 + 4 * case_pcs.length;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     int[] newRewrite = new int[rewriteSize];
/*  75 */     int rewriteIndex = 0;
/*     */ 
/*     */ 
/*     */     
/*  79 */     newRewrite[rewriteIndex++] = byteCode.getOpcode();
/*     */ 
/*     */     
/*  82 */     for (int index = 0; index < padLength; index++) {
/*  83 */       newRewrite[rewriteIndex++] = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  88 */     newRewrite[rewriteIndex++] = -1;
/*  89 */     newRewrite[rewriteIndex++] = -1;
/*  90 */     newRewrite[rewriteIndex++] = -1;
/*  91 */     newRewrite[rewriteIndex++] = -1;
/*     */ 
/*     */     
/*  94 */     int lowbyteIndex = rewriteIndex;
/*  95 */     setRewrite4Bytes(lowValue, lowbyteIndex, newRewrite);
/*  96 */     rewriteIndex += 4;
/*     */ 
/*     */     
/*  99 */     int highbyteIndex = rewriteIndex;
/* 100 */     setRewrite4Bytes(highValue, highbyteIndex, newRewrite);
/* 101 */     rewriteIndex += 4;
/*     */ 
/*     */ 
/*     */     
/* 105 */     for (int i = 0; i < case_count; i++) {
/*     */       
/* 107 */       newRewrite[rewriteIndex++] = -1;
/* 108 */       newRewrite[rewriteIndex++] = -1;
/* 109 */       newRewrite[rewriteIndex++] = -1;
/* 110 */       newRewrite[rewriteIndex++] = -1;
/*     */     } 
/* 112 */     byteCode.setRewrite(newRewrite);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\forms\TableSwitchForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */