/*    */ package org.apache.commons.compress.harmony.unpack200.bytecode;
/*    */ 
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class RuntimeVisibleorInvisibleAnnotationsAttribute
/*    */   extends AnnotationsAttribute
/*    */ {
/*    */   private final int num_annotations;
/*    */   private final AnnotationsAttribute.Annotation[] annotations;
/*    */   
/*    */   public RuntimeVisibleorInvisibleAnnotationsAttribute(CPUTF8 name, AnnotationsAttribute.Annotation[] annotations) {
/* 34 */     super(name);
/* 35 */     this.num_annotations = annotations.length;
/* 36 */     this.annotations = annotations;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getLength() {
/* 41 */     int length = 2;
/* 42 */     for (int i = 0; i < this.num_annotations; i++) {
/* 43 */       length += this.annotations[i].getLength();
/*    */     }
/* 45 */     return length;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void resolve(ClassConstantPool pool) {
/* 50 */     super.resolve(pool);
/* 51 */     for (AnnotationsAttribute.Annotation annotation : this.annotations) {
/* 52 */       annotation.resolve(pool);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeBody(DataOutputStream dos) throws IOException {
/* 58 */     int size = dos.size();
/* 59 */     dos.writeShort(this.num_annotations);
/* 60 */     for (int i = 0; i < this.num_annotations; i++) {
/* 61 */       this.annotations[i].writeBody(dos);
/*    */     }
/* 63 */     if (dos.size() - size != getLength()) {
/* 64 */       throw new Error();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     return this.attributeName.underlyingString() + ": " + this.num_annotations + " annotations";
/*    */   }
/*    */ 
/*    */   
/*    */   protected ClassFileEntry[] getNestedClassFileEntries() {
/* 75 */     List<Object> nested = new ArrayList();
/* 76 */     nested.add(this.attributeName);
/* 77 */     for (AnnotationsAttribute.Annotation annotation : this.annotations) {
/* 78 */       nested.addAll(annotation.getClassFileEntries());
/*    */     }
/* 80 */     return nested.<ClassFileEntry>toArray(ClassFileEntry.NONE);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\bytecode\RuntimeVisibleorInvisibleAnnotationsAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */