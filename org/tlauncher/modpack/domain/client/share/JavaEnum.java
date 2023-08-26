/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonFormat;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ 
/*    */ 
/*    */ @JsonFormat(shape = JsonFormat.Shape.OBJECT)
/*    */ public enum JavaEnum
/*    */ {
/* 11 */   JAVA_10, JAVA_11, JAVA_9, JAVA_8, JAVA_7, JAVA_6;
/*    */   @JsonCreator
/*    */   public static JavaEnum create(String value) {
/* 14 */     return valueOf(value.toUpperCase());
/*    */   }
/*    */ 
/*    */   
/*    */   @JsonValue
/*    */   public String toString() {
/* 20 */     return super.toString().toLowerCase();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\JavaEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */