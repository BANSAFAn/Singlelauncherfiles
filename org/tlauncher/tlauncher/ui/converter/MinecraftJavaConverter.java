/*    */ package org.tlauncher.tlauncher.ui.converter;
/*    */ 
/*    */ import com.google.inject.Inject;
/*    */ import java.util.Objects;
/*    */ import org.tlauncher.tlauncher.controller.JavaMinecraftController;
/*    */ import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ 
/*    */ public class MinecraftJavaConverter
/*    */   implements StringConverter<MinecraftJava.CompleteMinecraftJava>
/*    */ {
/*    */   @Inject
/*    */   private JavaMinecraftController controller;
/*    */   
/*    */   public MinecraftJava.CompleteMinecraftJava fromString(String from) {
/* 16 */     if (Objects.isNull(from))
/* 17 */       return null; 
/* 18 */     return this.controller.getById(Long.valueOf(from));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(MinecraftJava.CompleteMinecraftJava from) {
/* 23 */     if (Objects.isNull(from))
/* 24 */       return null; 
/* 25 */     return Localizable.get(from.getName());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toValue(MinecraftJava.CompleteMinecraftJava from) {
/* 30 */     if (Objects.isNull(from))
/* 31 */       return null; 
/* 32 */     return "" + from.getId();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<MinecraftJava.CompleteMinecraftJava> getObjectClass() {
/* 37 */     return MinecraftJava.CompleteMinecraftJava.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\MinecraftJavaConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */