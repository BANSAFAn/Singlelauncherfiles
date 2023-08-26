/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonFormat;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ @JsonFormat(shape = JsonFormat.Shape.OBJECT)
/*    */ public enum StateGameElement
/*    */ {
/* 12 */   NO_ACTIVE,
/* 13 */   ACTIVE,
/* 14 */   BLOCK;
/*    */ 
/*    */ 
/*    */   
/*    */   @JsonCreator
/*    */   public static StateGameElement createCategory(String value) {
/* 20 */     return valueOf(value.toUpperCase(Locale.ROOT));
/*    */   }
/*    */   
/*    */   @JsonValue
/*    */   public String toString() {
/* 25 */     return name().toLowerCase(Locale.ROOT);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\StateGameElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */