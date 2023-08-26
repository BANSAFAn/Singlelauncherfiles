/*    */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.Objects;
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
/*    */ 
/*    */ public class ConstantValueAttribute
/*    */   extends Attribute
/*    */ {
/*    */   private int constantIndex;
/*    */   private final ClassFileEntry entry;
/*    */   private static CPUTF8 attributeName;
/*    */   
/*    */   public static void setAttributeName(CPUTF8 cpUTF8Value) {
/* 35 */     attributeName = cpUTF8Value;
/*    */   }
/*    */   
/*    */   public ConstantValueAttribute(ClassFileEntry entry) {
/* 39 */     super(attributeName);
/* 40 */     this.entry = Objects.<ClassFileEntry>requireNonNull(entry, "entry");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 45 */     if (this == obj) {
/* 46 */       return true;
/*    */     }
/* 48 */     if (!super.equals(obj)) {
/* 49 */       return false;
/*    */     }
/* 51 */     if (getClass() != obj.getClass()) {
/* 52 */       return false;
/*    */     }
/* 54 */     ConstantValueAttribute other = (ConstantValueAttribute)obj;
/* 55 */     if (this.entry == null) {
/* 56 */       if (other.entry != null) {
/* 57 */         return false;
/*    */       }
/* 59 */     } else if (!this.entry.equals(other.entry)) {
/* 60 */       return false;
/*    */     } 
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getLength() {
/* 67 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ClassFileEntry[] getNestedClassFileEntries() {
/* 72 */     return new ClassFileEntry[] { getAttributeName(), this.entry };
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 77 */     int PRIME = 31;
/* 78 */     int result = super.hashCode();
/* 79 */     result = 31 * result + ((this.entry == null) ? 0 : this.entry.hashCode());
/* 80 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void resolve(ClassConstantPool pool) {
/* 85 */     super.resolve(pool);
/* 86 */     this.entry.resolve(pool);
/* 87 */     this.constantIndex = pool.indexOf(this.entry);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     return "Constant:" + this.entry;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeBody(DataOutputStream dos) throws IOException {
/* 97 */     dos.writeShort(this.constantIndex);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\ConstantValueAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */