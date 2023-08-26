/*    */ package org.tlauncher.tlauncher.minecraft.auth;
/*    */ 
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class GameProfile {
/*  6 */   public static final GameProfile DEFAULT_PROFILE = new GameProfile("0", "(Default)");
/*    */   
/*    */   private final String id;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private GameProfile(String id, String name) {
/* 13 */     if (StringUtils.isBlank(id) && StringUtils.isBlank(name)) {
/* 14 */       throw new IllegalArgumentException("Name and ID cannot both be blank");
/*    */     }
/*    */     
/* 17 */     this.id = id;
/* 18 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 22 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 26 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean isComplete() {
/* 30 */     return (StringUtils.isNotBlank(getId()) && 
/* 31 */       StringUtils.isNotBlank(getName()));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 36 */     if (this == o)
/* 37 */       return true; 
/* 38 */     if (o == null || getClass() != o.getClass()) {
/* 39 */       return false;
/*    */     }
/* 41 */     GameProfile that = (GameProfile)o;
/*    */     
/* 43 */     if (!this.id.equals(that.id)) {
/* 44 */       return false;
/*    */     }
/* 46 */     return this.name.equals(that.name);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 52 */     int result = this.id.hashCode();
/* 53 */     result = 31 * result + this.name.hashCode();
/* 54 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return "GameProfile{id='" + this.id + '\'' + ", name='" + this.name + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\GameProfile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */