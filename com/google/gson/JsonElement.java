/*     */ package com.google.gson;
/*     */ 
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JsonElement
/*     */ {
/*     */   public abstract JsonElement deepCopy();
/*     */   
/*     */   public boolean isJsonArray() {
/*  47 */     return this instanceof JsonArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJsonObject() {
/*  56 */     return this instanceof JsonObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJsonPrimitive() {
/*  65 */     return this instanceof JsonPrimitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJsonNull() {
/*  75 */     return this instanceof JsonNull;
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
/*     */   
/*     */   public JsonObject getAsJsonObject() {
/*  88 */     if (isJsonObject()) {
/*  89 */       return (JsonObject)this;
/*     */     }
/*  91 */     throw new IllegalStateException("Not a JSON Object: " + this);
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
/*     */   
/*     */   public JsonArray getAsJsonArray() {
/* 104 */     if (isJsonArray()) {
/* 105 */       return (JsonArray)this;
/*     */     }
/* 107 */     throw new IllegalStateException("Not a JSON Array: " + this);
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
/*     */   
/*     */   public JsonPrimitive getAsJsonPrimitive() {
/* 120 */     if (isJsonPrimitive()) {
/* 121 */       return (JsonPrimitive)this;
/*     */     }
/* 123 */     throw new IllegalStateException("Not a JSON Primitive: " + this);
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
/*     */ 
/*     */   
/*     */   public JsonNull getAsJsonNull() {
/* 137 */     if (isJsonNull()) {
/* 138 */       return (JsonNull)this;
/*     */     }
/* 140 */     throw new IllegalStateException("Not a JSON Null: " + this);
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
/*     */   
/*     */   public boolean getAsBoolean() {
/* 153 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */   
/*     */   public Number getAsNumber() {
/* 166 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */   
/*     */   public String getAsString() {
/* 179 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */   
/*     */   public double getAsDouble() {
/* 192 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */   
/*     */   public float getAsFloat() {
/* 205 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */   
/*     */   public long getAsLong() {
/* 218 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */   
/*     */   public int getAsInt() {
/* 231 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 245 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public char getAsCharacter() {
/* 263 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */ 
/*     */   
/*     */   public BigDecimal getAsBigDecimal() {
/* 277 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */ 
/*     */   
/*     */   public BigInteger getAsBigInteger() {
/* 291 */     throw new UnsupportedOperationException(getClass().getSimpleName());
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
/*     */   
/*     */   public short getAsShort() {
/* 304 */     throw new UnsupportedOperationException(getClass().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     try {
/* 313 */       StringWriter stringWriter = new StringWriter();
/* 314 */       JsonWriter jsonWriter = new JsonWriter(stringWriter);
/* 315 */       jsonWriter.setLenient(true);
/* 316 */       Streams.write(this, jsonWriter);
/* 317 */       return stringWriter.toString();
/* 318 */     } catch (IOException e) {
/* 319 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\JsonElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */