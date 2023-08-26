/*     */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.compress.harmony.unpack200.SegmentUtils;
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
/*     */ public class CPNameAndType
/*     */   extends ConstantPoolEntry
/*     */ {
/*     */   CPUTF8 descriptor;
/*     */   transient int descriptorIndex;
/*     */   CPUTF8 name;
/*     */   transient int nameIndex;
/*     */   private boolean hashcodeComputed;
/*     */   private int cachedHashCode;
/*     */   
/*     */   public CPNameAndType(CPUTF8 name, CPUTF8 descriptor, int globalIndex) {
/*  47 */     super((byte)12, globalIndex);
/*  48 */     this.name = Objects.<CPUTF8>requireNonNull(name, "name");
/*  49 */     this.descriptor = Objects.<CPUTF8>requireNonNull(descriptor, "descriptor");
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassFileEntry[] getNestedClassFileEntries() {
/*  54 */     return new ClassFileEntry[] { this.name, this.descriptor };
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resolve(ClassConstantPool pool) {
/*  59 */     super.resolve(pool);
/*  60 */     this.descriptorIndex = pool.indexOf(this.descriptor);
/*  61 */     this.nameIndex = pool.indexOf(this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody(DataOutputStream dos) throws IOException {
/*  71 */     dos.writeShort(this.nameIndex);
/*  72 */     dos.writeShort(this.descriptorIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  77 */     return "NameAndType: " + this.name + "(" + this.descriptor + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateHashCode() {
/*  84 */     this.hashcodeComputed = true;
/*  85 */     int PRIME = 31;
/*  86 */     int result = 1;
/*  87 */     result = 31 * result + this.descriptor.hashCode();
/*  88 */     result = 31 * result + this.name.hashCode();
/*  89 */     this.cachedHashCode = result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  94 */     if (!this.hashcodeComputed) {
/*  95 */       generateHashCode();
/*     */     }
/*  97 */     return this.cachedHashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 102 */     if (this == obj) {
/* 103 */       return true;
/*     */     }
/* 105 */     if (obj == null) {
/* 106 */       return false;
/*     */     }
/* 108 */     if (getClass() != obj.getClass()) {
/* 109 */       return false;
/*     */     }
/* 111 */     CPNameAndType other = (CPNameAndType)obj;
/* 112 */     if (!this.descriptor.equals(other.descriptor)) {
/* 113 */       return false;
/*     */     }
/* 115 */     if (!this.name.equals(other.name)) {
/* 116 */       return false;
/*     */     }
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int invokeInterfaceCount() {
/* 128 */     return 1 + SegmentUtils.countInvokeInterfaceArgs(this.descriptor.underlyingString());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\CPNameAndType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */