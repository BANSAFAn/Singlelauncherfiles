/*     */ package com.google.gson;
/*     */ 
/*     */ import com.google.gson.internal.Streams;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class JsonStreamParser
/*     */   implements Iterator<JsonElement>
/*     */ {
/*     */   private final JsonReader parser;
/*     */   private final Object lock;
/*     */   
/*     */   public JsonStreamParser(String json) {
/*  62 */     this(new StringReader(json));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonStreamParser(Reader reader) {
/*  70 */     this.parser = new JsonReader(reader);
/*  71 */     this.parser.setLenient(true);
/*  72 */     this.lock = new Object();
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
/*     */   public JsonElement next() throws JsonParseException {
/*  85 */     if (!hasNext()) {
/*  86 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     try {
/*  90 */       return Streams.parse(this.parser);
/*  91 */     } catch (StackOverflowError e) {
/*  92 */       throw new JsonParseException("Failed parsing JSON source to Json", e);
/*  93 */     } catch (OutOfMemoryError e) {
/*  94 */       throw new JsonParseException("Failed parsing JSON source to Json", e);
/*  95 */     } catch (JsonParseException e) {
/*  96 */       throw (e.getCause() instanceof java.io.EOFException) ? new NoSuchElementException() : e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 107 */     synchronized (this.lock) {
/*     */       
/* 109 */       return (this.parser.peek() != JsonToken.END_DOCUMENT);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 124 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\JsonStreamParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */