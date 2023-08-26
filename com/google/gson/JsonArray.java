/*     */ package com.google.gson;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public final class JsonArray
/*     */   extends JsonElement
/*     */   implements Iterable<JsonElement>
/*     */ {
/*     */   private final List<JsonElement> elements;
/*     */   
/*     */   public JsonArray() {
/*  40 */     this.elements = new ArrayList<JsonElement>();
/*     */   }
/*     */   
/*     */   public JsonArray(int capacity) {
/*  44 */     this.elements = new ArrayList<JsonElement>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonArray deepCopy() {
/*  53 */     if (!this.elements.isEmpty()) {
/*  54 */       JsonArray result = new JsonArray(this.elements.size());
/*  55 */       for (JsonElement element : this.elements) {
/*  56 */         result.add(element.deepCopy());
/*     */       }
/*  58 */       return result;
/*     */     } 
/*  60 */     return new JsonArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Boolean bool) {
/*  69 */     this.elements.add((bool == null) ? JsonNull.INSTANCE : new JsonPrimitive(bool));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Character character) {
/*  78 */     this.elements.add((character == null) ? JsonNull.INSTANCE : new JsonPrimitive(character));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Number number) {
/*  87 */     this.elements.add((number == null) ? JsonNull.INSTANCE : new JsonPrimitive(number));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(String string) {
/*  96 */     this.elements.add((string == null) ? JsonNull.INSTANCE : new JsonPrimitive(string));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(JsonElement element) {
/* 105 */     if (element == null) {
/* 106 */       element = JsonNull.INSTANCE;
/*     */     }
/* 108 */     this.elements.add(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(JsonArray array) {
/* 117 */     this.elements.addAll(array.elements);
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
/*     */   public JsonElement set(int index, JsonElement element) {
/* 129 */     return this.elements.set(index, element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(JsonElement element) {
/* 140 */     return this.elements.remove(element);
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
/*     */   public JsonElement remove(int index) {
/* 153 */     return this.elements.remove(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(JsonElement element) {
/* 163 */     return this.elements.contains(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 172 */     return this.elements.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 181 */     return this.elements.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JsonElement> iterator() {
/* 191 */     return this.elements.iterator();
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
/*     */   public JsonElement get(int i) {
/* 203 */     return this.elements.get(i);
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
/* 216 */     if (this.elements.size() == 1) {
/* 217 */       return ((JsonElement)this.elements.get(0)).getAsNumber();
/*     */     }
/* 219 */     throw new IllegalStateException();
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
/* 232 */     if (this.elements.size() == 1) {
/* 233 */       return ((JsonElement)this.elements.get(0)).getAsString();
/*     */     }
/* 235 */     throw new IllegalStateException();
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
/* 248 */     if (this.elements.size() == 1) {
/* 249 */       return ((JsonElement)this.elements.get(0)).getAsDouble();
/*     */     }
/* 251 */     throw new IllegalStateException();
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
/* 265 */     if (this.elements.size() == 1) {
/* 266 */       return ((JsonElement)this.elements.get(0)).getAsBigDecimal();
/*     */     }
/* 268 */     throw new IllegalStateException();
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
/* 282 */     if (this.elements.size() == 1) {
/* 283 */       return ((JsonElement)this.elements.get(0)).getAsBigInteger();
/*     */     }
/* 285 */     throw new IllegalStateException();
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
/* 298 */     if (this.elements.size() == 1) {
/* 299 */       return ((JsonElement)this.elements.get(0)).getAsFloat();
/*     */     }
/* 301 */     throw new IllegalStateException();
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
/* 314 */     if (this.elements.size() == 1) {
/* 315 */       return ((JsonElement)this.elements.get(0)).getAsLong();
/*     */     }
/* 317 */     throw new IllegalStateException();
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
/* 330 */     if (this.elements.size() == 1) {
/* 331 */       return ((JsonElement)this.elements.get(0)).getAsInt();
/*     */     }
/* 333 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 338 */     if (this.elements.size() == 1) {
/* 339 */       return ((JsonElement)this.elements.get(0)).getAsByte();
/*     */     }
/* 341 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public char getAsCharacter() {
/* 346 */     if (this.elements.size() == 1) {
/* 347 */       return ((JsonElement)this.elements.get(0)).getAsCharacter();
/*     */     }
/* 349 */     throw new IllegalStateException();
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
/* 362 */     if (this.elements.size() == 1) {
/* 363 */       return ((JsonElement)this.elements.get(0)).getAsShort();
/*     */     }
/* 365 */     throw new IllegalStateException();
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
/* 378 */     if (this.elements.size() == 1) {
/* 379 */       return ((JsonElement)this.elements.get(0)).getAsBoolean();
/*     */     }
/* 381 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 386 */     return (o == this || (o instanceof JsonArray && ((JsonArray)o).elements.equals(this.elements)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 391 */     return this.elements.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\JsonArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */