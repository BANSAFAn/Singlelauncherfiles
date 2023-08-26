/*     */ package com.google.gson;
/*     */ 
/*     */ import com.google.gson.internal.;
/*     */ import com.google.gson.internal.LazilyParsedNumber;
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
/*     */ public final class JsonPrimitive
/*     */   extends JsonElement
/*     */ {
/*     */   private final Object value;
/*     */   
/*     */   public JsonPrimitive(Boolean bool) {
/*  43 */     this.value = .Gson.Preconditions.checkNotNull(bool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonPrimitive(Number number) {
/*  52 */     this.value = .Gson.Preconditions.checkNotNull(number);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonPrimitive(String string) {
/*  61 */     this.value = .Gson.Preconditions.checkNotNull(string);
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
/*     */   public JsonPrimitive(Character c) {
/*  73 */     this.value = ((Character).Gson.Preconditions.checkNotNull(c)).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonPrimitive deepCopy() {
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBoolean() {
/*  91 */     return this.value instanceof Boolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAsBoolean() {
/* 101 */     if (isBoolean()) {
/* 102 */       return ((Boolean)this.value).booleanValue();
/*     */     }
/*     */     
/* 105 */     return Boolean.parseBoolean(getAsString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNumber() {
/* 114 */     return this.value instanceof Number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Number getAsNumber() {
/* 125 */     return (this.value instanceof String) ? (Number)new LazilyParsedNumber((String)this.value) : (Number)this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isString() {
/* 134 */     return this.value instanceof String;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getAsString() {
/* 144 */     if (isNumber())
/* 145 */       return getAsNumber().toString(); 
/* 146 */     if (isBoolean()) {
/* 147 */       return ((Boolean)this.value).toString();
/*     */     }
/* 149 */     return (String)this.value;
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
/*     */   public double getAsDouble() {
/* 161 */     return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal getAsBigDecimal() {
/* 172 */     return (this.value instanceof BigDecimal) ? (BigDecimal)this.value : new BigDecimal(this.value.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigInteger getAsBigInteger() {
/* 183 */     return (this.value instanceof BigInteger) ? 
/* 184 */       (BigInteger)this.value : new BigInteger(this.value.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAsFloat() {
/* 195 */     return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getAsLong() {
/* 206 */     return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getAsShort() {
/* 217 */     return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAsInt() {
/* 228 */     return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getAsByte() {
/* 233 */     return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
/*     */   }
/*     */ 
/*     */   
/*     */   public char getAsCharacter() {
/* 238 */     return getAsString().charAt(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 243 */     if (this.value == null) {
/* 244 */       return 31;
/*     */     }
/*     */     
/* 247 */     if (isIntegral(this)) {
/* 248 */       long value = getAsNumber().longValue();
/* 249 */       return (int)(value ^ value >>> 32L);
/*     */     } 
/* 251 */     if (this.value instanceof Number) {
/* 252 */       long value = Double.doubleToLongBits(getAsNumber().doubleValue());
/* 253 */       return (int)(value ^ value >>> 32L);
/*     */     } 
/* 255 */     return this.value.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 260 */     if (this == obj) {
/* 261 */       return true;
/*     */     }
/* 263 */     if (obj == null || getClass() != obj.getClass()) {
/* 264 */       return false;
/*     */     }
/* 266 */     JsonPrimitive other = (JsonPrimitive)obj;
/* 267 */     if (this.value == null) {
/* 268 */       return (other.value == null);
/*     */     }
/* 270 */     if (isIntegral(this) && isIntegral(other)) {
/* 271 */       return (getAsNumber().longValue() == other.getAsNumber().longValue());
/*     */     }
/* 273 */     if (this.value instanceof Number && other.value instanceof Number) {
/* 274 */       double a = getAsNumber().doubleValue();
/*     */ 
/*     */       
/* 277 */       double b = other.getAsNumber().doubleValue();
/* 278 */       return (a == b || (Double.isNaN(a) && Double.isNaN(b)));
/*     */     } 
/* 280 */     return this.value.equals(other.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isIntegral(JsonPrimitive primitive) {
/* 288 */     if (primitive.value instanceof Number) {
/* 289 */       Number number = (Number)primitive.value;
/* 290 */       return (number instanceof BigInteger || number instanceof Long || number instanceof Integer || number instanceof Short || number instanceof Byte);
/*     */     } 
/*     */     
/* 293 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\JsonPrimitive.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */