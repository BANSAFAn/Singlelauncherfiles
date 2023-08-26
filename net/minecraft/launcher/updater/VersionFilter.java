/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.launcher.versions.ReleaseType;
/*    */ import net.minecraft.launcher.versions.Version;
/*    */ 
/*    */ public class VersionFilter
/*    */ {
/* 11 */   private final Set<ReleaseType> types = new HashSet<>(ReleaseType.valuesCollection());
/* 12 */   private final Set<ReleaseType.SubType> subTypes = new HashSet<>(ReleaseType.SubType.valuesCollection());
/*    */   
/*    */   public Set<ReleaseType> getTypes() {
/* 15 */     return this.types;
/*    */   }
/*    */   
/*    */   public Set<ReleaseType.SubType> getSubTypes() {
/* 19 */     return this.subTypes;
/*    */   }
/*    */   
/*    */   public VersionFilter onlyForType(ReleaseType... types) {
/* 23 */     this.types.clear();
/* 24 */     include(types);
/* 25 */     return this;
/*    */   }
/*    */   
/*    */   public VersionFilter onlyForType(ReleaseType.SubType... subTypes) {
/* 29 */     this.subTypes.clear();
/* 30 */     include(subTypes);
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public VersionFilter include(ReleaseType... types) {
/* 35 */     if (types != null)
/* 36 */       Collections.addAll(this.types, types); 
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public VersionFilter include(ReleaseType.SubType... types) {
/* 41 */     if (types != null)
/* 42 */       Collections.addAll(this.subTypes, types); 
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public VersionFilter exclude(ReleaseType... types) {
/* 47 */     if (types != null) {
/* 48 */       for (ReleaseType type : types) {
/* 49 */         this.types.remove(type);
/*    */       }
/*    */     }
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public VersionFilter exclude(ReleaseType.SubType... types) {
/* 56 */     if (types != null) {
/* 57 */       for (ReleaseType.SubType type : types) {
/* 58 */         this.subTypes.remove(type);
/*    */       }
/*    */     }
/* 61 */     return this;
/*    */   }
/*    */   
/*    */   public boolean satisfies(Version v) {
/* 65 */     ReleaseType releaseType = v.getReleaseType();
/*    */     
/* 67 */     if (releaseType == null) {
/* 68 */       return true;
/*    */     }
/* 70 */     if (!this.types.contains(releaseType)) {
/* 71 */       return false;
/*    */     }
/* 73 */     ReleaseType.SubType subType = ReleaseType.SubType.get(v);
/*    */     
/* 75 */     if (subType == null) {
/* 76 */       return true;
/*    */     }
/* 78 */     return this.subTypes.contains(subType);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 83 */     return "VersionFilter" + this.types;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launche\\updater\VersionFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */