/*    */ package org.tlauncher.tlauncher.ui.converter;
/*    */ 
/*    */ import net.minecraft.launcher.versions.ReleaseType;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;
/*    */ 
/*    */ public class ReleaseTypeConverter
/*    */   extends LocalizableStringConverter<ReleaseType>
/*    */ {
/*    */   public ReleaseTypeConverter() {
/* 10 */     super("version.description");
/*    */   }
/*    */ 
/*    */   
/*    */   public ReleaseType fromString(String from) {
/* 15 */     if (from == null) {
/* 16 */       return ReleaseType.UNKNOWN;
/*    */     }
/* 18 */     for (ReleaseType type : ReleaseType.values()) {
/* 19 */       if (type.toString().equals(from))
/* 20 */         return type; 
/* 21 */     }  return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toValue(ReleaseType from) {
/* 26 */     if (from == null) {
/* 27 */       return ReleaseType.UNKNOWN.toString();
/*    */     }
/* 29 */     return from.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   protected String toPath(ReleaseType from) {
/* 34 */     if (from == null) {
/* 35 */       return ReleaseType.UNKNOWN.toString();
/*    */     }
/* 37 */     return toValue(from);
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<ReleaseType> getObjectClass() {
/* 42 */     return ReleaseType.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\ReleaseTypeConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */