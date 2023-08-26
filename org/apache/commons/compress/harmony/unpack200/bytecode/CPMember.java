/*     */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class CPMember
/*     */   extends ClassFileEntry
/*     */ {
/*     */   List<Attribute> attributes;
/*     */   short flags;
/*     */   CPUTF8 name;
/*     */   transient int nameIndex;
/*     */   protected final CPUTF8 descriptor;
/*     */   transient int descriptorIndex;
/*     */   
/*     */   public CPMember(CPUTF8 name, CPUTF8 descriptor, long flags, List<Attribute> attributes) {
/*  47 */     this.name = Objects.<CPUTF8>requireNonNull(name, "name");
/*  48 */     this.descriptor = Objects.<CPUTF8>requireNonNull(descriptor, "descriptor");
/*  49 */     this.flags = (short)(int)flags;
/*  50 */     this.attributes = (attributes == null) ? Collections.EMPTY_LIST : attributes;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ClassFileEntry[] getNestedClassFileEntries() {
/*  55 */     int attributeCount = this.attributes.size();
/*  56 */     ClassFileEntry[] entries = new ClassFileEntry[attributeCount + 2];
/*  57 */     entries[0] = this.name;
/*  58 */     entries[1] = this.descriptor;
/*  59 */     for (int i = 0; i < attributeCount; i++) {
/*  60 */       entries[i + 2] = this.attributes.get(i);
/*     */     }
/*  62 */     return entries;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resolve(ClassConstantPool pool) {
/*  67 */     super.resolve(pool);
/*  68 */     this.nameIndex = pool.indexOf(this.name);
/*  69 */     this.descriptorIndex = pool.indexOf(this.descriptor);
/*  70 */     this.attributes.forEach(attribute -> attribute.resolve(pool));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  75 */     int PRIME = 31;
/*  76 */     int result = 1;
/*  77 */     result = 31 * result + this.attributes.hashCode();
/*  78 */     result = 31 * result + this.descriptor.hashCode();
/*  79 */     result = 31 * result + this.flags;
/*  80 */     result = 31 * result + this.name.hashCode();
/*  81 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  86 */     return "CPMember: " + this.name + "(" + this.descriptor + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  91 */     if (this == obj) {
/*  92 */       return true;
/*     */     }
/*  94 */     if (obj == null) {
/*  95 */       return false;
/*     */     }
/*  97 */     if (getClass() != obj.getClass()) {
/*  98 */       return false;
/*     */     }
/* 100 */     CPMember other = (CPMember)obj;
/* 101 */     if (!this.attributes.equals(other.attributes)) {
/* 102 */       return false;
/*     */     }
/* 104 */     if (!this.descriptor.equals(other.descriptor)) {
/* 105 */       return false;
/*     */     }
/* 107 */     if (this.flags != other.flags) {
/* 108 */       return false;
/*     */     }
/* 110 */     if (!this.name.equals(other.name)) {
/* 111 */       return false;
/*     */     }
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doWrite(DataOutputStream dos) throws IOException {
/* 118 */     dos.writeShort(this.flags);
/* 119 */     dos.writeShort(this.nameIndex);
/* 120 */     dos.writeShort(this.descriptorIndex);
/* 121 */     int attributeCount = this.attributes.size();
/* 122 */     dos.writeShort(attributeCount);
/* 123 */     for (int i = 0; i < attributeCount; i++) {
/* 124 */       Attribute attribute = this.attributes.get(i);
/* 125 */       attribute.doWrite(dos);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\CPMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */