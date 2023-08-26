/*    */ package org.tlauncher.tlauncher.configuration;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.tlauncher.exceptions.ParseException;
/*    */ import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
/*    */ import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
/*    */ import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
/*    */ import org.tlauncher.util.IntegerArray;
/*    */ import org.tlauncher.util.StringUtil;
/*    */ 
/*    */ 
/*    */ class PlainParser
/*    */ {
/*    */   public static void parse(Object plainValue, Object defaultValue) throws ParseException {
/* 15 */     if (defaultValue == null) {
/*    */       return;
/*    */     }
/* 18 */     if (plainValue == null) {
/* 19 */       throw new ParseException("Value is NULL");
/*    */     }
/* 21 */     String value = plainValue.toString();
/*    */     
/*    */     try {
/* 24 */       if (defaultValue instanceof Integer) {
/* 25 */         Integer.parseInt(value);
/*    */       }
/* 27 */       else if (defaultValue instanceof Boolean) {
/* 28 */         StringUtil.parseBoolean(value);
/*    */       }
/* 30 */       else if (defaultValue instanceof Double) {
/* 31 */         Double.parseDouble(value);
/*    */       }
/* 33 */       else if (defaultValue instanceof Long) {
/* 34 */         Long.parseLong(value);
/*    */       }
/* 36 */       else if (defaultValue instanceof IntegerArray) {
/* 37 */         IntegerArray.parseIntegerArray(value);
/*    */       }
/* 39 */       else if (defaultValue instanceof ActionOnLaunch) {
/* 40 */         if (!ActionOnLaunch.parse(value))
/* 41 */           throw new ParseException("Cannot parse ActionOnLaunch"); 
/* 42 */       } else if (defaultValue instanceof ConsoleType) {
/* 43 */         if (!ConsoleType.parse(value))
/* 44 */           throw new ParseException("Cannot parse ConsoleType"); 
/* 45 */       } else if (defaultValue instanceof ConnectionQuality) {
/* 46 */         if (!ConnectionQuality.parse(value))
/* 47 */           throw new ParseException("Cannot parse ConnectionQuality"); 
/* 48 */       } else if (defaultValue instanceof UUID) {
/* 49 */         UUID.fromString(value);
/*    */       } 
/* 51 */     } catch (Exception e) {
/* 52 */       if (e instanceof ParseException) {
/* 53 */         throw (ParseException)e;
/*    */       }
/* 55 */       throw new ParseException("Cannot parse input value!", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\PlainParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */