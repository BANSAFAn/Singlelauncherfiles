/*     */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.commons.compress.harmony.unpack200.Segment;
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
/*     */ public class CodeAttribute
/*     */   extends BCIRenumberedAttribute
/*     */ {
/*  28 */   public List<Attribute> attributes = new ArrayList<>();
/*     */   
/*  30 */   public List<Integer> byteCodeOffsets = new ArrayList<>();
/*  31 */   public List<ByteCode> byteCodes = new ArrayList<>();
/*     */   
/*     */   public int codeLength;
/*     */   public List<ExceptionTableEntry> exceptionTable;
/*     */   public int maxLocals;
/*     */   public int maxStack;
/*     */   private static CPUTF8 attributeName;
/*     */   
/*     */   public CodeAttribute(int maxStack, int maxLocals, byte[] codePacked, Segment segment, OperandManager operandManager, List<ExceptionTableEntry> exceptionTable) {
/*  40 */     super(attributeName);
/*  41 */     this.maxLocals = maxLocals;
/*  42 */     this.maxStack = maxStack;
/*  43 */     this.codeLength = 0;
/*  44 */     this.exceptionTable = exceptionTable;
/*  45 */     this.byteCodeOffsets.add(Integer.valueOf(0));
/*  46 */     int byteCodeIndex = 0;
/*  47 */     for (int i = 0; i < codePacked.length; i++) {
/*  48 */       ByteCode byteCode = ByteCode.getByteCode(codePacked[i] & 0xFF);
/*     */ 
/*     */       
/*  51 */       byteCode.setByteCodeIndex(byteCodeIndex);
/*  52 */       byteCodeIndex++;
/*  53 */       byteCode.extractOperands(operandManager, segment, this.codeLength);
/*  54 */       this.byteCodes.add(byteCode);
/*  55 */       this.codeLength += byteCode.getLength();
/*  56 */       int lastBytecodePosition = ((Integer)this.byteCodeOffsets.get(this.byteCodeOffsets.size() - 1)).intValue();
/*     */ 
/*     */ 
/*     */       
/*  60 */       if (byteCode.hasMultipleByteCodes()) {
/*  61 */         this.byteCodeOffsets.add(Integer.valueOf(lastBytecodePosition + 1));
/*  62 */         byteCodeIndex++;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  67 */       if (i < codePacked.length - 1) {
/*  68 */         this.byteCodeOffsets.add(Integer.valueOf(lastBytecodePosition + byteCode.getLength()));
/*     */       }
/*  70 */       if (byteCode.getOpcode() == 196)
/*     */       {
/*     */ 
/*     */         
/*  74 */         i++;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     for (ByteCode byteCode : this.byteCodes) {
/*  82 */       byteCode.applyByteCodeTargetFixup(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLength() {
/*  88 */     int attributesSize = 0;
/*  89 */     for (Attribute attribute : this.attributes) {
/*  90 */       attributesSize += attribute.getLengthIncludingHeader();
/*     */     }
/*  92 */     return 8 + this.codeLength + 2 + this.exceptionTable.size() * 8 + 2 + attributesSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassFileEntry[] getNestedClassFileEntries() {
/*  97 */     List<ClassFileEntry> nestedEntries = new ArrayList<>(this.attributes.size() + this.byteCodes.size() + 10);
/*  98 */     nestedEntries.add(getAttributeName());
/*  99 */     nestedEntries.addAll((Collection)this.byteCodes);
/* 100 */     nestedEntries.addAll((Collection)this.attributes);
/*     */     
/* 102 */     for (ExceptionTableEntry entry : this.exceptionTable) {
/* 103 */       CPClass catchType = entry.getCatchType();
/*     */ 
/*     */ 
/*     */       
/* 107 */       if (catchType != null) {
/* 108 */         nestedEntries.add(catchType);
/*     */       }
/*     */     } 
/* 111 */     return nestedEntries.<ClassFileEntry>toArray(ClassFileEntry.NONE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resolve(ClassConstantPool pool) {
/* 116 */     super.resolve(pool);
/* 117 */     this.attributes.forEach(attribute -> attribute.resolve(pool));
/* 118 */     this.byteCodes.forEach(byteCode -> byteCode.resolve(pool));
/* 119 */     this.exceptionTable.forEach(byteCode -> byteCode.resolve(pool));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 124 */     return "Code: " + getLength() + " bytes";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBody(DataOutputStream dos) throws IOException {
/* 129 */     dos.writeShort(this.maxStack);
/* 130 */     dos.writeShort(this.maxLocals);
/*     */     
/* 132 */     dos.writeInt(this.codeLength);
/* 133 */     for (ByteCode byteCode : this.byteCodes) {
/* 134 */       byteCode.write(dos);
/*     */     }
/*     */     
/* 137 */     dos.writeShort(this.exceptionTable.size());
/* 138 */     for (ExceptionTableEntry entry : this.exceptionTable) {
/* 139 */       entry.write(dos);
/*     */     }
/*     */     
/* 142 */     dos.writeShort(this.attributes.size());
/* 143 */     for (Attribute attribute : this.attributes) {
/* 144 */       attribute.write(dos);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addAttribute(Attribute attribute) {
/* 149 */     this.attributes.add(attribute);
/* 150 */     if (attribute instanceof LocalVariableTableAttribute) {
/* 151 */       ((LocalVariableTableAttribute)attribute).setCodeLength(this.codeLength);
/*     */     }
/* 153 */     if (attribute instanceof LocalVariableTypeTableAttribute) {
/* 154 */       ((LocalVariableTypeTableAttribute)attribute).setCodeLength(this.codeLength);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int[] getStartPCs() {
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renumber(List<Integer> byteCodeOffsets) {
/* 166 */     this.exceptionTable.forEach(entry -> entry.renumber(byteCodeOffsets));
/*     */   }
/*     */   
/*     */   public static void setAttributeName(CPUTF8 attributeName) {
/* 170 */     CodeAttribute.attributeName = attributeName;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\CodeAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */