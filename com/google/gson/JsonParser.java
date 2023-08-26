/*     */ package com.google.gson;
/*     */ 
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.MalformedJsonException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
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
/*     */ 
/*     */ public final class JsonParser
/*     */ {
/*     */   public static JsonElement parseString(String json) throws JsonSyntaxException {
/*  47 */     return parseReader(new StringReader(json));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonElement parseReader(Reader reader) throws JsonIOException, JsonSyntaxException {
/*     */     try {
/*  59 */       JsonReader jsonReader = new JsonReader(reader);
/*  60 */       JsonElement element = parseReader(jsonReader);
/*  61 */       if (!element.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
/*  62 */         throw new JsonSyntaxException("Did not consume the entire document.");
/*     */       }
/*  64 */       return element;
/*  65 */     } catch (MalformedJsonException e) {
/*  66 */       throw new JsonSyntaxException(e);
/*  67 */     } catch (IOException e) {
/*  68 */       throw new JsonIOException(e);
/*  69 */     } catch (NumberFormatException e) {
/*  70 */       throw new JsonSyntaxException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonElement parseReader(JsonReader reader) throws JsonIOException, JsonSyntaxException {
/*  82 */     boolean lenient = reader.isLenient();
/*  83 */     reader.setLenient(true);
/*     */     try {
/*  85 */       return Streams.parse(reader);
/*  86 */     } catch (StackOverflowError e) {
/*  87 */       throw new JsonParseException("Failed parsing JSON source: " + reader + " to Json", e);
/*  88 */     } catch (OutOfMemoryError e) {
/*  89 */       throw new JsonParseException("Failed parsing JSON source: " + reader + " to Json", e);
/*     */     } finally {
/*  91 */       reader.setLenient(lenient);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonElement parse(String json) throws JsonSyntaxException {
/*  98 */     return parseString(json);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonElement parse(Reader json) throws JsonIOException, JsonSyntaxException {
/* 104 */     return parseReader(json);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonElement parse(JsonReader json) throws JsonIOException, JsonSyntaxException {
/* 110 */     return parseReader(json);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\JsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */