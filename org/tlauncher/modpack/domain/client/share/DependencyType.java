/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonCreator;
/*    */ import com.fasterxml.jackson.annotation.JsonFormat;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ 
/*    */ @JsonFormat(shape = JsonFormat.Shape.OBJECT)
/*    */ public enum DependencyType {
/*  9 */   EMBEDDED,
/* 10 */   OPTIONAL,
/* 11 */   REQUIRED,
/* 12 */   TOOLS,
/* 13 */   INCOMPATIBLE,
/* 14 */   INCLUDED;
/*    */ 
/*    */   
/*    */   @JsonValue
/*    */   public String toString() {
/* 19 */     return super.toString().toLowerCase();
/*    */   }
/*    */   
/*    */   @JsonCreator
/*    */   public static DependencyType createCategory(String value) {
/* 24 */     return valueOf(value.toUpperCase());
/*    */   }
/*    */   
/*    */   public static DependencyType[] properParserList() {
/* 28 */     return new DependencyType[] {
/* 29 */         REQUIRED, 
/* 30 */         OPTIONAL, 
/* 31 */         INCLUDED, 
/* 32 */         EMBEDDED, 
/* 33 */         TOOLS, 
/* 34 */         INCOMPATIBLE
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\DependencyType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */