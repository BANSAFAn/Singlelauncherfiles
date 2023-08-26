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
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ public class PatternTypeAdapter
/*    */   implements JsonSerializer<Pattern>, JsonDeserializer<Pattern>
/*    */ {
/*    */   public Pattern deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 19 */     String string = json.getAsString();
/* 20 */     return StringUtils.isBlank(string) ? null : Pattern.compile(string);
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement serialize(Pattern src, Type typeOfSrc, JsonSerializationContext context) {
/* 25 */     return (JsonElement)new JsonPrimitive((src == null) ? null : src.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\json\PatternTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */