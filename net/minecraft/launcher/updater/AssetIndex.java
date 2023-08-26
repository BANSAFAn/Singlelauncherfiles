/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AssetIndex
/*    */ {
/*    */   public static final String DEFAULT_ASSET_NAME = "legacy";
/* 14 */   private Map<String, AssetObject> objects = new LinkedHashMap<>();
/*    */   private boolean virtual;
/*    */   
/*    */   public Map<String, AssetObject> getFileMap() {
/* 18 */     return this.objects;
/*    */   }
/*    */   
/*    */   public Set<AssetObject> getUniqueObjects() {
/* 22 */     return new HashSet<>(this.objects.values());
/*    */   }
/*    */   
/*    */   public boolean isVirtual() {
/* 26 */     return this.virtual;
/*    */   }
/*    */ 
/*    */   
/*    */   public class AssetObject
/*    */   {
/*    */     private String filename;
/*    */     
/*    */     private String hash;
/*    */     private long size;
/*    */     
/*    */     public String getHash() {
/* 38 */       return this.hash;
/*    */     }
/*    */     
/*    */     public long getSize() {
/* 42 */       return this.size;
/*    */     }
/*    */     
/*    */     public String getFilename() {
/* 46 */       if (this.filename == null)
/* 47 */         this.filename = getHash().substring(0, 2) + "/" + getHash(); 
/* 48 */       return this.filename;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean equals(Object o) {
/* 53 */       if (this == o) {
/* 54 */         return true;
/*    */       }
/* 56 */       if (o == null || getClass() != o.getClass()) {
/* 57 */         return false;
/*    */       }
/* 59 */       AssetObject that = (AssetObject)o;
/*    */       
/* 61 */       if (this.size != that.size) {
/* 62 */         return false;
/*    */       }
/* 64 */       return this.hash.equals(that.hash);
/*    */     }
/*    */ 
/*    */     
/*    */     public int hashCode() {
/* 69 */       int result = this.hash.hashCode();
/* 70 */       result = 31 * result + (int)(this.size ^ this.size >>> 32L);
/* 71 */       return result;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launche\\updater\AssetIndex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */