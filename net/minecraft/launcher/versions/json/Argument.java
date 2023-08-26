/*    */ package net.minecraft.launcher.versions.json;
/*    */ import com.google.gson.JsonArray;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.launcher.versions.Rule;
/*    */ 
/*    */ public class Argument {
/*    */   private final String[] values;
/*    */   
/*    */   public Argument(String[] values, List<Rule> rules) {
/* 18 */     this.values = values;
/* 19 */     this.rules = rules;
/*    */   }
/*    */   private final List<Rule> rules;
/*    */   public boolean appliesToCurrentEnvironment() {
/* 23 */     if (this.rules == null || this.rules.isEmpty()) {
/* 24 */       return true;
/*    */     }
/* 26 */     Rule.Action lastAction = Rule.Action.DISALLOW;
/* 27 */     for (Rule compatibilityRule : this.rules) {
/* 28 */       Rule.Action action = compatibilityRule.getAppliedAction();
/* 29 */       if (action != null) {
/* 30 */         lastAction = action;
/*    */       }
/*    */     } 
/* 33 */     return (lastAction == Rule.Action.ALLOW);
/*    */   }
/*    */   
/*    */   public String[] getValues() {
/* 37 */     return this.values;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return "Argument{values=" + 
/* 43 */       Arrays.toString((Object[])this.values) + '}';
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Serializer
/*    */     implements JsonDeserializer<Argument>
/*    */   {
/*    */     public Argument deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 51 */       if (json.isJsonPrimitive()) {
/* 52 */         return new Argument(new String[] { json.getAsString() }, null);
/*    */       }
/* 54 */       if (json.isJsonObject()) {
/* 55 */         String[] values; JsonObject obj = json.getAsJsonObject();
/* 56 */         JsonElement value = obj.get("value");
/* 57 */         if (value == null) {
/* 58 */           value = obj.get("values");
/*    */         }
/*    */         
/* 61 */         if (value.isJsonPrimitive()) {
/* 62 */           values = new String[] { value.getAsString() };
/*    */         } else {
/* 64 */           JsonArray array = value.getAsJsonArray();
/* 65 */           values = new String[array.size()];
/* 66 */           for (int i = 0; i < array.size(); i++) {
/* 67 */             values[i] = array.get(i).getAsString();
/*    */           }
/*    */         } 
/* 70 */         List<Rule> rules = new ArrayList<>();
/* 71 */         if (obj.has("rules")) {
/* 72 */           JsonArray array = obj.getAsJsonArray("rules");
/* 73 */           for (JsonElement element : array) {
/* 74 */             rules.add(context.deserialize(element, Rule.class));
/*    */           }
/*    */         } 
/* 77 */         return new Argument(values, rules);
/*    */       } 
/* 79 */       throw new JsonParseException("Invalid argument, must be object or string");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\json\Argument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */