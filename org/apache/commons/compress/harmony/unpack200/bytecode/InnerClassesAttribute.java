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
/*     */ public class InnerClassesAttribute
/*     */   extends Attribute
/*     */ {
/*     */   private static CPUTF8 attributeName;
/*     */   
/*     */   public static void setAttributeName(CPUTF8 cpUTF8Value) {
/*  32 */     attributeName = cpUTF8Value;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class InnerClassesEntry
/*     */   {
/*     */     CPClass inner_class_info;
/*     */     CPClass outer_class_info;
/*     */     CPUTF8 inner_class_name;
/*  41 */     int inner_class_info_index = -1;
/*  42 */     int outer_class_info_index = -1;
/*  43 */     int inner_name_index = -1;
/*  44 */     int inner_class_access_flags = -1;
/*     */ 
/*     */     
/*     */     public InnerClassesEntry(CPClass innerClass, CPClass outerClass, CPUTF8 innerName, int flags) {
/*  48 */       this.inner_class_info = innerClass;
/*  49 */       this.outer_class_info = outerClass;
/*  50 */       this.inner_class_name = innerName;
/*  51 */       this.inner_class_access_flags = flags;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void resolve(ClassConstantPool pool) {
/*  60 */       if (this.inner_class_info != null) {
/*  61 */         this.inner_class_info.resolve(pool);
/*  62 */         this.inner_class_info_index = pool.indexOf(this.inner_class_info);
/*     */       } else {
/*  64 */         this.inner_class_info_index = 0;
/*     */       } 
/*     */       
/*  67 */       if (this.inner_class_name != null) {
/*  68 */         this.inner_class_name.resolve(pool);
/*  69 */         this.inner_name_index = pool.indexOf(this.inner_class_name);
/*     */       } else {
/*  71 */         this.inner_name_index = 0;
/*     */       } 
/*     */       
/*  74 */       if (this.outer_class_info != null) {
/*  75 */         this.outer_class_info.resolve(pool);
/*  76 */         this.outer_class_info_index = pool.indexOf(this.outer_class_info);
/*     */       } else {
/*  78 */         this.outer_class_info_index = 0;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void write(DataOutputStream dos) throws IOException {
/*  83 */       dos.writeShort(this.inner_class_info_index);
/*  84 */       dos.writeShort(this.outer_class_info_index);
/*  85 */       dos.writeShort(this.inner_name_index);
/*  86 */       dos.writeShort(this.inner_class_access_flags);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  91 */   private final List<InnerClassesEntry> innerClasses = new ArrayList<>();
/*  92 */   private final List<ConstantPoolEntry> nestedClassFileEntries = new ArrayList<>();
/*     */   
/*     */   public InnerClassesAttribute(String name) {
/*  95 */     super(attributeName);
/*  96 */     this.nestedClassFileEntries.add(getAttributeName());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 101 */     if (this == obj) {
/* 102 */       return true;
/*     */     }
/* 104 */     if (!super.equals(obj)) {
/* 105 */       return false;
/*     */     }
/* 107 */     if (getClass() != obj.getClass()) {
/* 108 */       return false;
/*     */     }
/* 110 */     InnerClassesAttribute other = (InnerClassesAttribute)obj;
/* 111 */     if (getAttributeName() == null) {
/* 112 */       if (other.getAttributeName() != null) {
/* 113 */         return false;
/*     */       }
/* 115 */     } else if (!getAttributeName().equals(other.getAttributeName())) {
/* 116 */       return false;
/*     */     } 
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLength() {
/* 123 */     return 2 + 8 * this.innerClasses.size();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassFileEntry[] getNestedClassFileEntries() {
/* 128 */     return this.nestedClassFileEntries.<ClassFileEntry>toArray(ClassFileEntry.NONE);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 133 */     int PRIME = 31;
/* 134 */     int result = super.hashCode();
/* 135 */     result = 31 * result + ((getAttributeName() == null) ? 0 : getAttributeName().hashCode());
/* 136 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resolve(ClassConstantPool pool) {
/* 141 */     super.resolve(pool);
/* 142 */     for (InnerClassesEntry entry : this.innerClasses) {
/* 143 */       entry.resolve(pool);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 149 */     return "InnerClasses: " + getAttributeName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doWrite(DataOutputStream dos) throws IOException {
/* 155 */     super.doWrite(dos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBody(DataOutputStream dos) throws IOException {
/* 160 */     dos.writeShort(this.innerClasses.size());
/*     */     
/* 162 */     for (InnerClassesEntry entry : this.innerClasses) {
/* 163 */       entry.write(dos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInnerClassesEntry(CPClass innerClass, CPClass outerClass, CPUTF8 innerName, int flags) {
/* 169 */     if (innerClass != null) {
/* 170 */       this.nestedClassFileEntries.add(innerClass);
/*     */     }
/* 172 */     if (outerClass != null) {
/* 173 */       this.nestedClassFileEntries.add(outerClass);
/*     */     }
/* 175 */     if (innerName != null) {
/* 176 */       this.nestedClassFileEntries.add(innerName);
/*     */     }
/* 178 */     addInnerClassesEntry(new InnerClassesEntry(innerClass, outerClass, innerName, flags));
/*     */   }
/*     */   
/*     */   private void addInnerClassesEntry(InnerClassesEntry innerClassesEntry) {
/* 182 */     this.innerClasses.add(innerClassesEntry);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\InnerClassesAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */