/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonFormat;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ import java.util.Locale;
/*    */ 
/*    */ @JsonFormat(shape = JsonFormat.Shape.OBJECT)
/*    */ public enum VersionMaturity
/*    */ {
/* 11 */   PROMO_LATEST, PROMO_RECOMMEDED, FA_BUG;
/*    */ 
/*    */   
/*    */   @JsonValue
/*    */   public String toString() {
/* 16 */     return name().toLowerCase(Locale.ROOT);
/*    */   }
/*    */   
/*    */   public String toWebParam() {
/* 20 */     return name().toUpperCase(Locale.ROOT);
/*    */   }
/*    */   
/*    */   @JsonCreator
/*    */   public static Type createCategory(String value) {
/* 25 */     return Type.valueOf(value.toUpperCase());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\VersionMaturity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */