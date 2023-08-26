/*    */ package org.tlauncher.tlauncher.ui.converter;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;
/*    */ import org.tlauncher.util.Direction;
/*    */ import org.tlauncher.util.Reflect;
/*    */ 
/*    */ public class DirectionConverter
/*    */   extends LocalizableStringConverter<Direction> {
/*    */   public DirectionConverter() {
/* 10 */     super("settings.direction");
/*    */   }
/*    */ 
/*    */   
/*    */   public Direction fromString(String from) {
/* 15 */     return (Direction)Reflect.parseEnum(Direction.class, from);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toValue(Direction from) {
/* 20 */     if (from == null)
/* 21 */       return null; 
/* 22 */     return from.toString().toLowerCase();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<Direction> getObjectClass() {
/* 27 */     return Direction.class;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String toPath(Direction from) {
/* 32 */     return toValue(from);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\DirectionConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */