/*     */ package com.google.gson;
/*     */ 
/*     */ import com.google.gson.internal.LinkedTreeMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JsonObject
/*     */   extends JsonElement
/*     */ {
/*  33 */   private final LinkedTreeMap<String, JsonElement> members = new LinkedTreeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonObject deepCopy() {
/*  42 */     JsonObject result = new JsonObject();
/*  43 */     for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)this.members.entrySet()) {
/*  44 */       result.add(entry.getKey(), ((JsonElement)entry.getValue()).deepCopy());
/*     */     }
/*  46 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(String property, JsonElement value) {
/*  58 */     this.members.put(property, (value == null) ? JsonNull.INSTANCE : value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonElement remove(String property) {
/*  69 */     return (JsonElement)this.members.remove(property);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProperty(String property, String value) {
/*  80 */     add(property, (value == null) ? JsonNull.INSTANCE : new JsonPrimitive(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProperty(String property, Number value) {
/*  91 */     add(property, (value == null) ? JsonNull.INSTANCE : new JsonPrimitive(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProperty(String property, Boolean value) {
/* 102 */     add(property, (value == null) ? JsonNull.INSTANCE : new JsonPrimitive(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProperty(String property, Character value) {
/* 113 */     add(property, (value == null) ? JsonNull.INSTANCE : new JsonPrimitive(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, JsonElement>> entrySet() {
/* 123 */     return this.members.entrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/* 133 */     return this.members.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 142 */     return this.members.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean has(String memberName) {
/* 152 */     return this.members.containsKey(memberName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonElement get(String memberName) {
/* 162 */     return (JsonElement)this.members.get(memberName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonPrimitive getAsJsonPrimitive(String memberName) {
/* 172 */     return (JsonPrimitive)this.members.get(memberName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonArray getAsJsonArray(String memberName) {
/* 182 */     return (JsonArray)this.members.get(memberName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonObject getAsJsonObject(String memberName) {
/* 192 */     return (JsonObject)this.members.get(memberName);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 197 */     return (o == this || (o instanceof JsonObject && ((JsonObject)o).members
/* 198 */       .equals(this.members)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 203 */     return this.members.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\JsonObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */