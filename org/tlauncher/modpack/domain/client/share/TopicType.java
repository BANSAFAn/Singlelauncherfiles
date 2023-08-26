/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonFormat;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ import java.util.Locale;
/*    */ 
/*    */ @JsonFormat(shape = JsonFormat.Shape.OBJECT)
/*    */ public enum TopicType
/*    */ {
/* 10 */   GAME_ENTITY, SUB_COMMENT;
/*    */ 
/*    */   
/*    */   @JsonValue
/*    */   public String toString() {
/* 15 */     return name().toLowerCase(Locale.ROOT);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\TopicType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */