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
/*     */ public class RuntimeVisibleorInvisibleParameterAnnotationsAttribute
/*     */   extends AnnotationsAttribute
/*     */ {
/*     */   private final int num_parameters;
/*     */   private final ParameterAnnotation[] parameter_annotations;
/*     */   
/*     */   public RuntimeVisibleorInvisibleParameterAnnotationsAttribute(CPUTF8 name, ParameterAnnotation[] parameter_annotations) {
/*  35 */     super(name);
/*  36 */     this.num_parameters = parameter_annotations.length;
/*  37 */     this.parameter_annotations = parameter_annotations;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getLength() {
/*  42 */     int length = 1;
/*  43 */     for (int i = 0; i < this.num_parameters; i++) {
/*  44 */       length += this.parameter_annotations[i].getLength();
/*     */     }
/*  46 */     return length;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resolve(ClassConstantPool pool) {
/*  51 */     super.resolve(pool);
/*  52 */     for (ParameterAnnotation parameter_annotation : this.parameter_annotations) {
/*  53 */       parameter_annotation.resolve(pool);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBody(DataOutputStream dos) throws IOException {
/*  59 */     dos.writeByte(this.num_parameters);
/*  60 */     for (int i = 0; i < this.num_parameters; i++) {
/*  61 */       this.parameter_annotations[i].writeBody(dos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  67 */     return this.attributeName.underlyingString() + ": " + this.num_parameters + " parameter annotations";
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ParameterAnnotation
/*     */   {
/*     */     private final AnnotationsAttribute.Annotation[] annotations;
/*     */     
/*     */     private final int num_annotations;
/*     */ 
/*     */     
/*     */     public ParameterAnnotation(AnnotationsAttribute.Annotation[] annotations) {
/*  79 */       this.num_annotations = annotations.length;
/*  80 */       this.annotations = annotations;
/*     */     }
/*     */     
/*     */     public void writeBody(DataOutputStream dos) throws IOException {
/*  84 */       dos.writeShort(this.num_annotations);
/*  85 */       for (AnnotationsAttribute.Annotation annotation : this.annotations) {
/*  86 */         annotation.writeBody(dos);
/*     */       }
/*     */     }
/*     */     
/*     */     public void resolve(ClassConstantPool pool) {
/*  91 */       for (AnnotationsAttribute.Annotation annotation : this.annotations) {
/*  92 */         annotation.resolve(pool);
/*     */       }
/*     */     }
/*     */     
/*     */     public int getLength() {
/*  97 */       int length = 2;
/*  98 */       for (AnnotationsAttribute.Annotation annotation : this.annotations) {
/*  99 */         length += annotation.getLength();
/*     */       }
/* 101 */       return length;
/*     */     }
/*     */     
/*     */     public List<Object> getClassFileEntries() {
/* 105 */       List<Object> nested = new ArrayList();
/* 106 */       for (AnnotationsAttribute.Annotation annotation : this.annotations) {
/* 107 */         nested.addAll(annotation.getClassFileEntries());
/*     */       }
/* 109 */       return nested;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassFileEntry[] getNestedClassFileEntries() {
/* 116 */     List<Object> nested = new ArrayList();
/* 117 */     nested.add(this.attributeName);
/* 118 */     for (ParameterAnnotation parameter_annotation : this.parameter_annotations) {
/* 119 */       nested.addAll(parameter_annotation.getClassFileEntries());
/*     */     }
/* 121 */     return nested.<ClassFileEntry>toArray(ClassFileEntry.NONE);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\RuntimeVisibleorInvisibleParameterAnnotationsAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */