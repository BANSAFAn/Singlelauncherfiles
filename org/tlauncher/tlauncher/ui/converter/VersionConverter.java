/*    */ package org.tlauncher.tlauncher.ui.converter;
/*    */ 
/*    */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*    */ import net.minecraft.launcher.versions.ReleaseType;
/*    */ import org.tlauncher.tlauncher.managers.VersionManager;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;
/*    */ 
/*    */ 
/*    */ public class VersionConverter
/*    */   extends LocalizableStringConverter<VersionSyncInfo>
/*    */ {
/* 13 */   private static final VersionSyncInfo LOADING = VersionSyncInfo.createEmpty();
/* 14 */   private static final VersionSyncInfo EMPTY = VersionSyncInfo.createEmpty();
/*    */   private final VersionManager vm;
/*    */   
/*    */   public VersionConverter(VersionManager vm) {
/* 18 */     super(null);
/*    */     
/* 20 */     if (vm == null) {
/* 21 */       throw new NullPointerException();
/*    */     }
/* 23 */     this.vm = vm;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(VersionSyncInfo from) {
/* 28 */     if (from == null)
/* 29 */       return null; 
/* 30 */     if (from.equals(LOADING))
/* 31 */       return Localizable.get("versions.loading"); 
/* 32 */     if (from.equals(EMPTY)) {
/* 33 */       return Localizable.get("versions.notfound.tip");
/*    */     }
/* 35 */     String id = from.getID();
/* 36 */     ReleaseType type = from.getLatestVersion().getReleaseType();
/*    */     
/* 38 */     if (type == null || type.equals(ReleaseType.UNKNOWN)) {
/* 39 */       return id;
/*    */     }
/* 41 */     String typeF = type.toString().toLowerCase();
/* 42 */     String formatted = Localizable.get().nget("version." + typeF, new Object[] { id });
/*    */     
/* 44 */     if (formatted == null)
/* 45 */       return id; 
/* 46 */     return formatted;
/*    */   }
/*    */ 
/*    */   
/*    */   public VersionSyncInfo fromString(String from) {
/* 51 */     return this.vm.getVersionSyncInfo(from);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toValue(VersionSyncInfo from) {
/* 56 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toPath(VersionSyncInfo from) {
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<VersionSyncInfo> getObjectClass() {
/* 66 */     return VersionSyncInfo.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\VersionConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */