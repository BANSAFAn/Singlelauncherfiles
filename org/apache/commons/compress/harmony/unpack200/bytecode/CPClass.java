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
/*     */ public class CPClass
/*     */   extends ConstantPoolEntry
/*     */ {
/*     */   private int index;
/*     */   public String name;
/*     */   private final CPUTF8 utf8;
/*     */   private boolean hashcodeComputed;
/*     */   private int cachedHashCode;
/*     */   
/*     */   public CPClass(CPUTF8 name, int globalIndex) {
/*  42 */     super((byte)7, globalIndex);
/*  43 */     this.name = ((CPUTF8)Objects.<CPUTF8>requireNonNull(name, "name")).underlyingString();
/*  44 */     this.utf8 = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  49 */     if (this == obj) {
/*  50 */       return true;
/*     */     }
/*  52 */     if (obj == null) {
/*  53 */       return false;
/*     */     }
/*  55 */     if (getClass() != obj.getClass()) {
/*  56 */       return false;
/*     */     }
/*  58 */     CPClass other = (CPClass)obj;
/*  59 */     return this.utf8.equals(other.utf8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassFileEntry[] getNestedClassFileEntries() {
/*  64 */     return new ClassFileEntry[] { this.utf8 };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateHashCode() {
/*  71 */     this.hashcodeComputed = true;
/*  72 */     this.cachedHashCode = this.utf8.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  77 */     if (!this.hashcodeComputed) {
/*  78 */       generateHashCode();
/*     */     }
/*  80 */     return this.cachedHashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resolve(ClassConstantPool pool) {
/*  85 */     super.resolve(pool);
/*  86 */     this.index = pool.indexOf(this.utf8);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  91 */     return "Class: " + getName();
/*     */   }
/*     */   
/*     */   public String getName() {
/*  95 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBody(DataOutputStream dos) throws IOException {
/* 100 */     dos.writeShort(this.index);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\CPClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */