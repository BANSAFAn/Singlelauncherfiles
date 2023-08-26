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
/*     */ public class LookupSwitchForm
/*     */   extends SwitchForm
/*     */ {
/*     */   public LookupSwitchForm(int opcode, String name) {
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
/*  43 */     int[] case_values = new int[case_count];
/*  44 */     Arrays.setAll(case_values, i -> operandManager.nextCaseValues());
/*  45 */     int[] case_pcs = new int[case_count];
/*  46 */     Arrays.setAll(case_pcs, i -> operandManager.nextLabel());
/*     */     
/*  48 */     int[] labelsArray = new int[case_count + 1];
/*  49 */     labelsArray[0] = default_pc;
/*  50 */     System.arraycopy(case_pcs, 0, labelsArray, 1, case_count + 1 - 1);
/*  51 */     byteCode.setByteCodeTargets(labelsArray);
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
/*  65 */     int padLength = 3 - codeLength % 4;
/*  66 */     int rewriteSize = 1 + padLength + 4 + 4 + 4 * case_values.length + 4 * case_pcs.length;
/*     */ 
/*     */ 
/*     */     
/*  70 */     int[] newRewrite = new int[rewriteSize];
/*  71 */     int rewriteIndex = 0;
/*     */ 
/*     */ 
/*     */     
/*  75 */     newRewrite[rewriteIndex++] = byteCode.getOpcode();
/*     */ 
/*     */     
/*  78 */     for (int index = 0; index < padLength; index++) {
/*  79 */       newRewrite[rewriteIndex++] = 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  84 */     newRewrite[rewriteIndex++] = -1;
/*  85 */     newRewrite[rewriteIndex++] = -1;
/*  86 */     newRewrite[rewriteIndex++] = -1;
/*  87 */     newRewrite[rewriteIndex++] = -1;
/*     */ 
/*     */     
/*  90 */     int npairsIndex = rewriteIndex;
/*  91 */     setRewrite4Bytes(case_values.length, npairsIndex, newRewrite);
/*  92 */     rewriteIndex += 4;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  97 */     for (int case_value : case_values) {
/*     */       
/*  99 */       setRewrite4Bytes(case_value, rewriteIndex, newRewrite);
/* 100 */       rewriteIndex += 4;
/*     */       
/* 102 */       newRewrite[rewriteIndex++] = -1;
/* 103 */       newRewrite[rewriteIndex++] = -1;
/* 104 */       newRewrite[rewriteIndex++] = -1;
/* 105 */       newRewrite[rewriteIndex++] = -1;
/*     */     } 
/* 107 */     byteCode.setRewrite(newRewrite);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\forms\LookupSwitchForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */