/*     */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ public class NewAttribute
/*     */   extends BCIRenumberedAttribute
/*     */ {
/*  29 */   private final List<Integer> lengths = new ArrayList<>();
/*  30 */   private final List<Object> body = new ArrayList();
/*     */   private ClassConstantPool pool;
/*     */   private final int layoutIndex;
/*     */   
/*     */   public NewAttribute(CPUTF8 attributeName, int layoutIndex) {
/*  35 */     super(attributeName);
/*  36 */     this.layoutIndex = layoutIndex;
/*     */   }
/*     */   
/*     */   public int getLayoutIndex() {
/*  40 */     return this.layoutIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getLength() {
/*  50 */     int length = 0;
/*  51 */     for (Integer len : this.lengths) {
/*  52 */       length += len.intValue();
/*     */     }
/*  54 */     return length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody(DataOutputStream dos) throws IOException {
/*  64 */     for (int i = 0; i < this.lengths.size(); i++) {
/*  65 */       int length = ((Integer)this.lengths.get(i)).intValue();
/*  66 */       Object obj = this.body.get(i);
/*  67 */       long value = 0L;
/*  68 */       if (obj instanceof Long) {
/*  69 */         value = ((Long)obj).longValue();
/*  70 */       } else if (obj instanceof ClassFileEntry) {
/*  71 */         value = this.pool.indexOf((ClassFileEntry)obj);
/*  72 */       } else if (obj instanceof BCValue) {
/*  73 */         value = ((BCValue)obj).actualValue;
/*     */       } 
/*     */       
/*  76 */       if (length == 1) {
/*  77 */         dos.writeByte((int)value);
/*  78 */       } else if (length == 2) {
/*  79 */         dos.writeShort((int)value);
/*  80 */       } else if (length == 4) {
/*  81 */         dos.writeInt((int)value);
/*  82 */       } else if (length == 8) {
/*  83 */         dos.writeLong(value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     return this.attributeName.underlyingString();
/*     */   }
/*     */   
/*     */   public void addInteger(int length, long value) {
/*  99 */     this.lengths.add(Integer.valueOf(length));
/* 100 */     this.body.add(Long.valueOf(value));
/*     */   }
/*     */   
/*     */   public void addBCOffset(int length, int value) {
/* 104 */     this.lengths.add(Integer.valueOf(length));
/* 105 */     this.body.add(new BCOffset(value));
/*     */   }
/*     */   
/*     */   public void addBCIndex(int length, int value) {
/* 109 */     this.lengths.add(Integer.valueOf(length));
/* 110 */     this.body.add(new BCIndex(value));
/*     */   }
/*     */   
/*     */   public void addBCLength(int length, int value) {
/* 114 */     this.lengths.add(Integer.valueOf(length));
/* 115 */     this.body.add(new BCLength(value));
/*     */   }
/*     */   
/*     */   public void addToBody(int length, Object value) {
/* 119 */     this.lengths.add(Integer.valueOf(length));
/* 120 */     this.body.add(value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resolve(ClassConstantPool pool) {
/* 125 */     super.resolve(pool);
/* 126 */     for (Object element : this.body) {
/* 127 */       if (element instanceof ClassFileEntry) {
/* 128 */         ((ClassFileEntry)element).resolve(pool);
/*     */       }
/*     */     } 
/* 131 */     this.pool = pool;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassFileEntry[] getNestedClassFileEntries() {
/* 136 */     int total = 1;
/* 137 */     for (Object element : this.body) {
/* 138 */       if (element instanceof ClassFileEntry) {
/* 139 */         total++;
/*     */       }
/*     */     } 
/* 142 */     ClassFileEntry[] nested = new ClassFileEntry[total];
/* 143 */     nested[0] = getAttributeName();
/* 144 */     int i = 1;
/* 145 */     for (Object element : this.body) {
/* 146 */       if (element instanceof ClassFileEntry) {
/* 147 */         nested[i] = (ClassFileEntry)element;
/* 148 */         i++;
/*     */       } 
/*     */     } 
/* 151 */     return nested;
/*     */   }
/*     */   
/*     */   private static class BCOffset
/*     */     extends BCValue {
/*     */     private final int offset;
/*     */     private int index;
/*     */     
/*     */     public BCOffset(int offset) {
/* 160 */       this.offset = offset;
/*     */     }
/*     */     
/*     */     public void setIndex(int index) {
/* 164 */       this.index = index;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class BCIndex
/*     */     extends BCValue
/*     */   {
/*     */     private final int index;
/*     */     
/*     */     public BCIndex(int index) {
/* 174 */       this.index = index;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class BCLength
/*     */     extends BCValue {
/*     */     private final int length;
/*     */     
/*     */     public BCLength(int length) {
/* 183 */       this.length = length;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class BCValue {
/*     */     int actualValue;
/*     */     
/*     */     private BCValue() {}
/*     */     
/*     */     public void setActualValue(int value) {
/* 193 */       this.actualValue = value;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int[] getStartPCs() {
/* 201 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void renumber(List<Integer> byteCodeOffsets) {
/* 206 */     if (!this.renumbered) {
/* 207 */       Object previous = null;
/* 208 */       for (Object obj : this.body) {
/* 209 */         if (obj instanceof BCIndex) {
/* 210 */           BCIndex bcIndex = (BCIndex)obj;
/* 211 */           bcIndex.setActualValue(((Integer)byteCodeOffsets.get(bcIndex.index)).intValue());
/* 212 */         } else if (obj instanceof BCOffset) {
/* 213 */           BCOffset bcOffset = (BCOffset)obj;
/* 214 */           if (previous instanceof BCIndex) {
/* 215 */             int index = ((BCIndex)previous).index + bcOffset.offset;
/* 216 */             bcOffset.setIndex(index);
/* 217 */             bcOffset.setActualValue(((Integer)byteCodeOffsets.get(index)).intValue());
/* 218 */           } else if (previous instanceof BCOffset) {
/* 219 */             int index = ((BCOffset)previous).index + bcOffset.offset;
/* 220 */             bcOffset.setIndex(index);
/* 221 */             bcOffset.setActualValue(((Integer)byteCodeOffsets.get(index)).intValue());
/*     */           } else {
/*     */             
/* 224 */             bcOffset.setActualValue(((Integer)byteCodeOffsets.get(bcOffset.offset)).intValue());
/*     */           } 
/* 226 */         } else if (obj instanceof BCLength) {
/*     */           
/* 228 */           BCLength bcLength = (BCLength)obj;
/* 229 */           BCIndex prevIndex = (BCIndex)previous;
/* 230 */           int index = prevIndex.index + bcLength.length;
/* 231 */           int actualLength = ((Integer)byteCodeOffsets.get(index)).intValue() - prevIndex.actualValue;
/* 232 */           bcLength.setActualValue(actualLength);
/*     */         } 
/* 234 */         previous = obj;
/*     */       } 
/* 236 */       this.renumbered = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\NewAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */