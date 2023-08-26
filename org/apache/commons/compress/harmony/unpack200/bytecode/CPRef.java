/*     */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
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
/*     */ public abstract class CPRef
/*     */   extends ConstantPoolEntry
/*     */ {
/*     */   CPClass className;
/*     */   transient int classNameIndex;
/*     */   protected CPNameAndType nameAndType;
/*     */   transient int nameAndTypeIndex;
/*     */   protected String cachedToString;
/*     */   
/*     */   public CPRef(byte type, CPClass className, CPNameAndType descriptor, int globalIndex) {
/*  44 */     super(type, globalIndex);
/*  45 */     this.className = Objects.<CPClass>requireNonNull(className, "className");
/*  46 */     this.nameAndType = Objects.<CPNameAndType>requireNonNull(descriptor, "descriptor");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  51 */     if (this == obj) {
/*  52 */       return true;
/*     */     }
/*  54 */     if (obj == null) {
/*  55 */       return false;
/*     */     }
/*  57 */     if (getClass() != obj.getClass()) {
/*  58 */       return false;
/*     */     }
/*  60 */     if (hashCode() != obj.hashCode()) {
/*  61 */       return false;
/*     */     }
/*  63 */     CPRef other = (CPRef)obj;
/*  64 */     if (!this.className.equals(other.className)) {
/*  65 */       return false;
/*     */     }
/*  67 */     if (!this.nameAndType.equals(other.nameAndType)) {
/*  68 */       return false;
/*     */     }
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassFileEntry[] getNestedClassFileEntries() {
/*  75 */     ClassFileEntry[] entries = new ClassFileEntry[2];
/*  76 */     entries[0] = this.className;
/*  77 */     entries[1] = this.nameAndType;
/*  78 */     return entries;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resolve(ClassConstantPool pool) {
/*  83 */     super.resolve(pool);
/*  84 */     this.nameAndTypeIndex = pool.indexOf(this.nameAndType);
/*  85 */     this.classNameIndex = pool.indexOf(this.className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  92 */     if (this.cachedToString == null) {
/*     */       String type;
/*  94 */       if (getTag() == 9) {
/*  95 */         type = "FieldRef";
/*  96 */       } else if (getTag() == 10) {
/*  97 */         type = "MethoddRef";
/*  98 */       } else if (getTag() == 11) {
/*  99 */         type = "InterfaceMethodRef";
/*     */       } else {
/* 101 */         type = "unknown";
/*     */       } 
/* 103 */       this.cachedToString = type + ": " + this.className + "#" + this.nameAndType;
/*     */     } 
/* 105 */     return this.cachedToString;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBody(DataOutputStream dos) throws IOException {
/* 110 */     dos.writeShort(this.classNameIndex);
/* 111 */     dos.writeShort(this.nameAndTypeIndex);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\CPRef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */