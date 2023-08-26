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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AnnotationsAttribute
/*     */   extends Attribute
/*     */ {
/*     */   public static class Annotation
/*     */   {
/*     */     private final int num_pairs;
/*     */     private final CPUTF8[] element_names;
/*     */     private final AnnotationsAttribute.ElementValue[] element_values;
/*     */     private final CPUTF8 type;
/*     */     private int type_index;
/*     */     private int[] name_indexes;
/*     */     
/*     */     public Annotation(int num_pairs, CPUTF8 type, CPUTF8[] element_names, AnnotationsAttribute.ElementValue[] element_values) {
/*  45 */       this.num_pairs = num_pairs;
/*  46 */       this.type = type;
/*  47 */       this.element_names = element_names;
/*  48 */       this.element_values = element_values;
/*     */     }
/*     */     
/*     */     public int getLength() {
/*  52 */       int length = 4;
/*  53 */       for (int i = 0; i < this.num_pairs; i++) {
/*  54 */         length += 2;
/*  55 */         length += this.element_values[i].getLength();
/*     */       } 
/*  57 */       return length;
/*     */     }
/*     */     
/*     */     public void resolve(ClassConstantPool pool) {
/*  61 */       this.type.resolve(pool);
/*  62 */       this.type_index = pool.indexOf(this.type);
/*  63 */       this.name_indexes = new int[this.num_pairs];
/*  64 */       for (int i = 0; i < this.element_names.length; i++) {
/*  65 */         this.element_names[i].resolve(pool);
/*  66 */         this.name_indexes[i] = pool.indexOf(this.element_names[i]);
/*  67 */         this.element_values[i].resolve(pool);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void writeBody(DataOutputStream dos) throws IOException {
/*  72 */       dos.writeShort(this.type_index);
/*  73 */       dos.writeShort(this.num_pairs);
/*  74 */       for (int i = 0; i < this.num_pairs; i++) {
/*  75 */         dos.writeShort(this.name_indexes[i]);
/*  76 */         this.element_values[i].writeBody(dos);
/*     */       } 
/*     */     }
/*     */     
/*     */     public List<Object> getClassFileEntries() {
/*  81 */       List<Object> entries = new ArrayList();
/*  82 */       for (int i = 0; i < this.element_names.length; i++) {
/*  83 */         entries.add(this.element_names[i]);
/*  84 */         entries.addAll(this.element_values[i].getClassFileEntries());
/*     */       } 
/*  86 */       entries.add(this.type);
/*  87 */       return entries;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ElementValue
/*     */   {
/*     */     private final Object value;
/*     */     
/*     */     private final int tag;
/*  97 */     private int constant_value_index = -1;
/*     */     
/*     */     public ElementValue(int tag, Object value) {
/* 100 */       this.tag = tag;
/* 101 */       this.value = value;
/*     */     }
/*     */     
/*     */     public List<Object> getClassFileEntries() {
/* 105 */       List<Object> entries = new ArrayList(1);
/* 106 */       if (this.value instanceof CPNameAndType) {
/*     */         
/* 108 */         entries.add(((CPNameAndType)this.value).name);
/* 109 */         entries.add(((CPNameAndType)this.value).descriptor);
/* 110 */       } else if (this.value instanceof ClassFileEntry) {
/*     */         
/* 112 */         entries.add(this.value);
/* 113 */       } else if (this.value instanceof ElementValue[]) {
/* 114 */         ElementValue[] values = (ElementValue[])this.value;
/* 115 */         for (ElementValue value2 : values) {
/* 116 */           entries.addAll(value2.getClassFileEntries());
/*     */         }
/* 118 */       } else if (this.value instanceof AnnotationsAttribute.Annotation) {
/* 119 */         entries.addAll(((AnnotationsAttribute.Annotation)this.value).getClassFileEntries());
/*     */       } 
/* 121 */       return entries;
/*     */     }
/*     */     
/*     */     public void resolve(ClassConstantPool pool) {
/* 125 */       if (this.value instanceof CPConstant) {
/* 126 */         ((CPConstant)this.value).resolve(pool);
/* 127 */         this.constant_value_index = pool.indexOf((CPConstant)this.value);
/* 128 */       } else if (this.value instanceof CPClass) {
/* 129 */         ((CPClass)this.value).resolve(pool);
/* 130 */         this.constant_value_index = pool.indexOf((CPClass)this.value);
/* 131 */       } else if (this.value instanceof CPUTF8) {
/* 132 */         ((CPUTF8)this.value).resolve(pool);
/* 133 */         this.constant_value_index = pool.indexOf((CPUTF8)this.value);
/* 134 */       } else if (this.value instanceof CPNameAndType) {
/* 135 */         ((CPNameAndType)this.value).resolve(pool);
/* 136 */       } else if (this.value instanceof AnnotationsAttribute.Annotation) {
/* 137 */         ((AnnotationsAttribute.Annotation)this.value).resolve(pool);
/* 138 */       } else if (this.value instanceof ElementValue[]) {
/* 139 */         ElementValue[] nestedValues = (ElementValue[])this.value;
/* 140 */         for (ElementValue nestedValue : nestedValues) {
/* 141 */           nestedValue.resolve(pool);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public void writeBody(DataOutputStream dos) throws IOException {
/* 147 */       dos.writeByte(this.tag);
/* 148 */       if (this.constant_value_index != -1) {
/* 149 */         dos.writeShort(this.constant_value_index);
/* 150 */       } else if (this.value instanceof CPNameAndType) {
/* 151 */         ((CPNameAndType)this.value).writeBody(dos);
/* 152 */       } else if (this.value instanceof AnnotationsAttribute.Annotation) {
/* 153 */         ((AnnotationsAttribute.Annotation)this.value).writeBody(dos);
/* 154 */       } else if (this.value instanceof ElementValue[]) {
/* 155 */         ElementValue[] nestedValues = (ElementValue[])this.value;
/* 156 */         dos.writeShort(nestedValues.length);
/* 157 */         for (ElementValue nestedValue : nestedValues) {
/* 158 */           nestedValue.writeBody(dos);
/*     */         }
/*     */       } else {
/* 161 */         throw new Error("");
/*     */       } 
/*     */     } public int getLength() {
/*     */       int length;
/*     */       ElementValue[] nestedValues;
/* 166 */       switch (this.tag) {
/*     */         case 66:
/*     */         case 67:
/*     */         case 68:
/*     */         case 70:
/*     */         case 73:
/*     */         case 74:
/*     */         case 83:
/*     */         case 90:
/*     */         case 99:
/*     */         case 115:
/* 177 */           return 3;
/*     */         case 101:
/* 179 */           return 5;
/*     */         case 91:
/* 181 */           length = 3;
/* 182 */           nestedValues = (ElementValue[])this.value;
/* 183 */           for (ElementValue nestedValue : nestedValues) {
/* 184 */             length += nestedValue.getLength();
/*     */           }
/* 186 */           return length;
/*     */         case 64:
/* 188 */           return 1 + ((AnnotationsAttribute.Annotation)this.value).getLength();
/*     */       } 
/* 190 */       return 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public AnnotationsAttribute(CPUTF8 attributeName) {
/* 195 */     super(attributeName);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\AnnotationsAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */