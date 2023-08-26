/*     */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
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
/*     */ public class ExceptionsAttribute
/*     */   extends Attribute
/*     */ {
/*     */   private static CPUTF8 attributeName;
/*     */   private transient int[] exceptionIndexes;
/*     */   private final CPClass[] exceptions;
/*     */   
/*     */   private static int hashCode(Object[] array) {
/*  31 */     int prime = 31;
/*  32 */     if (array == null) {
/*  33 */       return 0;
/*     */     }
/*  35 */     int result = 1;
/*  36 */     for (Object element : array) {
/*  37 */       result = 31 * result + ((element == null) ? 0 : element.hashCode());
/*     */     }
/*  39 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExceptionsAttribute(CPClass[] exceptions) {
/*  47 */     super(attributeName);
/*  48 */     this.exceptions = exceptions;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  53 */     if (this == obj) {
/*  54 */       return true;
/*     */     }
/*  56 */     if (!super.equals(obj)) {
/*  57 */       return false;
/*     */     }
/*  59 */     if (getClass() != obj.getClass()) {
/*  60 */       return false;
/*     */     }
/*  62 */     ExceptionsAttribute other = (ExceptionsAttribute)obj;
/*  63 */     if (!Arrays.equals((Object[])this.exceptions, (Object[])other.exceptions)) {
/*  64 */       return false;
/*     */     }
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLength() {
/*  71 */     return 2 + 2 * this.exceptions.length;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassFileEntry[] getNestedClassFileEntries() {
/*  76 */     ClassFileEntry[] result = new ClassFileEntry[this.exceptions.length + 1];
/*  77 */     System.arraycopy(this.exceptions, 0, result, 0, this.exceptions.length);
/*  78 */     result[this.exceptions.length] = getAttributeName();
/*  79 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  84 */     int prime = 31;
/*  85 */     int result = super.hashCode();
/*  86 */     result = 31 * result + hashCode((Object[])this.exceptions);
/*  87 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resolve(ClassConstantPool pool) {
/*  92 */     super.resolve(pool);
/*  93 */     this.exceptionIndexes = new int[this.exceptions.length];
/*  94 */     for (int i = 0; i < this.exceptions.length; i++) {
/*  95 */       this.exceptions[i].resolve(pool);
/*  96 */       this.exceptionIndexes[i] = pool.indexOf(this.exceptions[i]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 102 */     StringBuilder sb = new StringBuilder();
/* 103 */     sb.append("Exceptions: ");
/* 104 */     for (CPClass exception : this.exceptions) {
/* 105 */       sb.append(exception);
/* 106 */       sb.append(' ');
/*     */     } 
/* 108 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBody(DataOutputStream dos) throws IOException {
/* 113 */     dos.writeShort(this.exceptionIndexes.length);
/* 114 */     for (int element : this.exceptionIndexes) {
/* 115 */       dos.writeShort(element);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setAttributeName(CPUTF8 cpUTF8Value) {
/* 120 */     attributeName = cpUTF8Value;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\ExceptionsAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */