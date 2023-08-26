/*    */ package org.tlauncher.tlauncher.minecraft.auth;
/*    */ 
/*    */ public class UserProperty {
/*    */   private String name;
/*    */   private String value;
/*    */   
/*    */   public UserProperty(String name, String value) {
/*  8 */     this.name = name;
/*  9 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 13 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 17 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 22 */     return "Property{" + this.name + " = " + this.value + "}";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\UserProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */