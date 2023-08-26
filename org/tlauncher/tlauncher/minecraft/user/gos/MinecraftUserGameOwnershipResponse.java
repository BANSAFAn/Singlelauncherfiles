/*    */ package org.tlauncher.tlauncher.minecraft.user.gos;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;
/*    */ 
/*    */ public class MinecraftUserGameOwnershipResponse
/*    */   implements Validatable
/*    */ {
/*    */   private List<Item> items;
/*    */   
/*    */   public MinecraftUserGameOwnershipResponse(List<Item> items) {
/* 13 */     this.items = items;
/*    */   }
/*    */ 
/*    */   
/*    */   public MinecraftUserGameOwnershipResponse() {}
/*    */   
/*    */   public boolean equals(Object o) {
/* 20 */     if (this == o)
/* 21 */       return true; 
/* 22 */     if (o == null || getClass() != o.getClass())
/* 23 */       return false; 
/* 24 */     MinecraftUserGameOwnershipResponse that = (MinecraftUserGameOwnershipResponse)o;
/* 25 */     return Objects.equals(this.items, that.items);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 29 */     return (this.items != null) ? this.items.hashCode() : 0;
/*    */   }
/*    */   
/*    */   public List<Item> getItems() {
/* 33 */     return this.items;
/*    */   }
/*    */   
/*    */   public void validate() {
/* 37 */     Validatable.notNull(this.items, "items");
/* 38 */     for (Item item : this.items)
/* 39 */       item.validate(); 
/*    */   }
/*    */   
/*    */   public String toString() {
/* 43 */     return "MinecraftUserGameOwnershipResponse{items=" + this.items + '}';
/*    */   }
/*    */   
/*    */   public static class Item
/*    */     implements Validatable {
/*    */     private String name;
/*    */     private String signature;
/*    */     
/*    */     public String getName() {
/* 52 */       return this.name;
/*    */     }
/*    */     
/*    */     public boolean equals(Object o) {
/* 56 */       if (this == o)
/* 57 */         return true; 
/* 58 */       if (o == null || getClass() != o.getClass())
/* 59 */         return false; 
/* 60 */       Item item = (Item)o;
/* 61 */       if (!this.name.equals(item.name))
/* 62 */         return false; 
/* 63 */       return this.signature.equals(item.signature);
/*    */     }
/*    */     
/*    */     public int hashCode() {
/* 67 */       int result = this.name.hashCode();
/* 68 */       return 31 * result + this.signature.hashCode();
/*    */     }
/*    */     
/*    */     public String toString() {
/* 72 */       return "Item{name='" + this.name + '\'' + ", signature='" + this.signature + '\'' + '}';
/*    */     }
/*    */     
/*    */     public void validate() {
/* 76 */       Validatable.notEmpty(this.name, "name");
/* 77 */       Validatable.notEmpty(this.signature, "signature");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\gos\MinecraftUserGameOwnershipResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */