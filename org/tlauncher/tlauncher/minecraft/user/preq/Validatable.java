/*    */ package org.tlauncher.tlauncher.minecraft.user.preq;
/*    */ 
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public interface Validatable {
/*    */   static void notNull(Object o, String name) {
/*  7 */     if (o == null)
/*  8 */       throw new NullPointerException(name); 
/*    */   }
/*    */   
/*    */   static void notEmpty(String s, String name) {
/* 12 */     if (s == null)
/* 13 */       throw new NullPointerException(name); 
/* 14 */     if (StringUtils.isEmpty(s))
/* 15 */       throw new IllegalArgumentException("blank " + name); 
/*    */   }
/*    */   
/*    */   static void notNegative(int i, String name) {
/* 19 */     if (i < 0)
/* 20 */       throw new IllegalArgumentException("negative " + name); 
/*    */   }
/*    */   
/*    */   void validate();
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\preq\Validatable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */