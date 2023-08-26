/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonFormat;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ import java.util.Locale;
/*    */ 
/*    */ @JsonFormat(shape = JsonFormat.Shape.OBJECT)
/*    */ public enum Type
/*    */ {
/* 11 */   BETA, RELEASE;
/*    */   
/*    */   @JsonValue
/*    */   public String toString() {
/* 15 */     return name().toLowerCase(Locale.ROOT);
/*    */   }
/*    */   public String toWebParam() {
/* 18 */     return name().toUpperCase(Locale.ROOT);
/*    */   }
/*    */   @JsonCreator
/*    */   public static Type createCategory(String value) {
/* 22 */     return valueOf(value.toUpperCase());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */