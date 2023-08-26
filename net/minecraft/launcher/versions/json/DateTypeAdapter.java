/*    */ package net.minecraft.launcher.versions.json;
/*    */ 
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateTypeAdapter
/*    */   implements JsonDeserializer<Date>, JsonSerializer<Date>
/*    */ {
/* 24 */   private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
/* 25 */   private final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 31 */     if (!(json instanceof JsonPrimitive)) {
/* 32 */       throw new JsonParseException("The date should be a string value");
/*    */     }
/* 34 */     Date date = toDate(json.getAsString());
/*    */     
/* 36 */     if (typeOfT == Date.class) {
/* 37 */       return date;
/*    */     }
/* 39 */     throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
/* 46 */     synchronized (this.enUsFormat) {
/* 47 */       return (JsonElement)new JsonPrimitive(toString(src));
/*    */     } 
/*    */   }
/*    */   
/*    */   public String toString(Date date) {
/* 52 */     synchronized (this.enUsFormat) {
/* 53 */       String result = this.iso8601Format.format(date);
/* 54 */       return result.substring(0, 22) + ":" + result.substring(22);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Date toDate(String string) {
/* 59 */     synchronized (this.enUsFormat) {
/*    */       
/* 61 */       return this.enUsFormat.parse(string);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\json\DateTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */