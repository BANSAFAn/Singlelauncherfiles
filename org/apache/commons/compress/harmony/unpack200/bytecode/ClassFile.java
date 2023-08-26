/*    */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
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
/*    */ public class ClassFile
/*    */ {
/*    */   public int major;
/*    */   public int minor;
/* 29 */   private final int magic = -889275714;
/* 30 */   public ClassConstantPool pool = new ClassConstantPool();
/*    */   public int accessFlags;
/*    */   public int thisClass;
/*    */   public int superClass;
/*    */   public int[] interfaces;
/*    */   public ClassFileEntry[] fields;
/*    */   public ClassFileEntry[] methods;
/*    */   public Attribute[] attributes;
/*    */   
/*    */   public void write(DataOutputStream dos) throws IOException {
/* 40 */     dos.writeInt(-889275714);
/* 41 */     dos.writeShort(this.minor);
/* 42 */     dos.writeShort(this.major);
/* 43 */     dos.writeShort(this.pool.size() + 1);
/* 44 */     for (int i = 1; i <= this.pool.size(); i++) {
/*    */       ConstantPoolEntry entry;
/* 46 */       (entry = (ConstantPoolEntry)this.pool.get(i)).doWrite(dos);
/*    */ 
/*    */       
/* 49 */       if (entry.getTag() == 6 || entry.getTag() == 5) {
/* 50 */         i++;
/*    */       }
/*    */     } 
/* 53 */     dos.writeShort(this.accessFlags);
/* 54 */     dos.writeShort(this.thisClass);
/* 55 */     dos.writeShort(this.superClass);
/* 56 */     dos.writeShort(this.interfaces.length);
/* 57 */     for (int element : this.interfaces) {
/* 58 */       dos.writeShort(element);
/*    */     }
/* 60 */     dos.writeShort(this.fields.length);
/* 61 */     for (ClassFileEntry field : this.fields) {
/* 62 */       field.write(dos);
/*    */     }
/* 64 */     dos.writeShort(this.methods.length);
/* 65 */     for (ClassFileEntry method : this.methods) {
/* 66 */       method.write(dos);
/*    */     }
/* 68 */     dos.writeShort(this.attributes.length);
/* 69 */     for (Attribute attribute : this.attributes)
/* 70 */       attribute.write(dos); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\ClassFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */