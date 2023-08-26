/*    */ package org.tlauncher.tlauncher.minecraft.user.preq;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class MinecraftOAuthProfile
/*    */   implements Validatable
/*    */ {
/*    */   private UUID id;
/*    */   private String name;
/*    */   
/*    */   public MinecraftOAuthProfile(UUID id, String name) {
/* 13 */     this.id = id;
/* 14 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public MinecraftOAuthProfile() {}
/*    */   
/*    */   public boolean equals(Object o) {
/* 21 */     if (this == o)
/* 22 */       return true; 
/* 23 */     if (o == null || getClass() != o.getClass())
/* 24 */       return false; 
/* 25 */     MinecraftOAuthProfile that = (MinecraftOAuthProfile)o;
/* 26 */     if (!Objects.equals(this.id, that.id))
/* 27 */       return false; 
/* 28 */     return Objects.equals(this.name, that.name);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 32 */     int result = (this.id != null) ? this.id.hashCode() : 0;
/* 33 */     return 31 * result + ((this.name != null) ? this.name.hashCode() : 0);
/*    */   }
/*    */   
/*    */   public UUID getId() {
/* 37 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 41 */     return this.name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 45 */     return "MinecraftOAuthProfile{id='" + this.id + '\'' + ", name='" + this.name + '\'' + '}';
/*    */   }
/*    */   
/*    */   public void validate() {
/* 49 */     Validatable.notNull(this.id, "id");
/* 50 */     Validatable.notEmpty(this.name, "name");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\preq\MinecraftOAuthProfile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */