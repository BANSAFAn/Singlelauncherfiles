/*     */ package com.google.gson.internal.bind;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonIOException;
/*     */ import com.google.gson.JsonNull;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonPrimitive;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import com.google.gson.TypeAdapter;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.gson.annotations.SerializedName;
/*     */ import com.google.gson.internal.LazilyParsedNumber;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.gson.stream.JsonReader;
/*     */ import com.google.gson.stream.JsonToken;
/*     */ import com.google.gson.stream.JsonWriter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.InetAddress;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Calendar;
/*     */ import java.util.Currency;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicIntegerArray;
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
/*     */ public final class TypeAdapters
/*     */ {
/*     */   private TypeAdapters() {
/*  68 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*  72 */   public static final TypeAdapter<Class> CLASS = (new TypeAdapter<Class>()
/*     */     {
/*     */       public void write(JsonWriter out, Class value) throws IOException {
/*  75 */         throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + value
/*  76 */             .getName() + ". Forgot to register a type adapter?");
/*     */       }
/*     */       
/*     */       public Class read(JsonReader in) throws IOException {
/*  80 */         throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
/*     */       }
/*  83 */     }).nullSafe();
/*     */   
/*  85 */   public static final TypeAdapterFactory CLASS_FACTORY = newFactory(Class.class, CLASS);
/*     */   
/*  87 */   public static final TypeAdapter<BitSet> BIT_SET = (new TypeAdapter<BitSet>() {
/*     */       public BitSet read(JsonReader in) throws IOException {
/*  89 */         BitSet bitset = new BitSet();
/*  90 */         in.beginArray();
/*  91 */         int i = 0;
/*  92 */         JsonToken tokenType = in.peek();
/*  93 */         while (tokenType != JsonToken.END_ARRAY) {
/*     */           boolean set; String stringValue;
/*  95 */           switch (tokenType) {
/*     */             case NUMBER:
/*  97 */               set = (in.nextInt() != 0);
/*     */               break;
/*     */             case BOOLEAN:
/* 100 */               set = in.nextBoolean();
/*     */               break;
/*     */             case STRING:
/* 103 */               stringValue = in.nextString();
/*     */               try {
/* 105 */                 set = (Integer.parseInt(stringValue) != 0);
/* 106 */               } catch (NumberFormatException e) {
/* 107 */                 throw new JsonSyntaxException("Error: Expecting: bitset number value (1, 0), Found: " + stringValue);
/*     */               } 
/*     */               break;
/*     */             
/*     */             default:
/* 112 */               throw new JsonSyntaxException("Invalid bitset value type: " + tokenType);
/*     */           } 
/* 114 */           if (set) {
/* 115 */             bitset.set(i);
/*     */           }
/* 117 */           i++;
/* 118 */           tokenType = in.peek();
/*     */         } 
/* 120 */         in.endArray();
/* 121 */         return bitset;
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, BitSet src) throws IOException {
/* 125 */         out.beginArray();
/* 126 */         for (int i = 0, length = src.length(); i < length; i++) {
/* 127 */           int value = src.get(i) ? 1 : 0;
/* 128 */           out.value(value);
/*     */         } 
/* 130 */         out.endArray();
/*     */       }
/* 132 */     }).nullSafe();
/*     */   
/* 134 */   public static final TypeAdapterFactory BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET);
/*     */   
/* 136 */   public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>()
/*     */     {
/*     */       public Boolean read(JsonReader in) throws IOException {
/* 139 */         JsonToken peek = in.peek();
/* 140 */         if (peek == JsonToken.NULL) {
/* 141 */           in.nextNull();
/* 142 */           return null;
/* 143 */         }  if (peek == JsonToken.STRING)
/*     */         {
/* 145 */           return Boolean.valueOf(Boolean.parseBoolean(in.nextString()));
/*     */         }
/* 147 */         return Boolean.valueOf(in.nextBoolean());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Boolean value) throws IOException {
/* 151 */         out.value(value);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 159 */   public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() {
/*     */       public Boolean read(JsonReader in) throws IOException {
/* 161 */         if (in.peek() == JsonToken.NULL) {
/* 162 */           in.nextNull();
/* 163 */           return null;
/*     */         } 
/* 165 */         return Boolean.valueOf(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Boolean value) throws IOException {
/* 169 */         out.value((value == null) ? "null" : value.toString());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 174 */   public static final TypeAdapterFactory BOOLEAN_FACTORY = newFactory(boolean.class, (Class)Boolean.class, (TypeAdapter)BOOLEAN);
/*     */   
/* 176 */   public static final TypeAdapter<Number> BYTE = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 179 */         if (in.peek() == JsonToken.NULL) {
/* 180 */           in.nextNull();
/* 181 */           return null;
/*     */         } 
/*     */         try {
/* 184 */           int intValue = in.nextInt();
/* 185 */           return Byte.valueOf((byte)intValue);
/* 186 */         } catch (NumberFormatException e) {
/* 187 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 192 */         out.value(value);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 197 */   public static final TypeAdapterFactory BYTE_FACTORY = newFactory(byte.class, (Class)Byte.class, (TypeAdapter)BYTE);
/*     */   
/* 199 */   public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 202 */         if (in.peek() == JsonToken.NULL) {
/* 203 */           in.nextNull();
/* 204 */           return null;
/*     */         } 
/*     */         try {
/* 207 */           return Short.valueOf((short)in.nextInt());
/* 208 */         } catch (NumberFormatException e) {
/* 209 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 214 */         out.value(value);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 219 */   public static final TypeAdapterFactory SHORT_FACTORY = newFactory(short.class, (Class)Short.class, (TypeAdapter)SHORT);
/*     */   
/* 221 */   public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 224 */         if (in.peek() == JsonToken.NULL) {
/* 225 */           in.nextNull();
/* 226 */           return null;
/*     */         } 
/*     */         try {
/* 229 */           return Integer.valueOf(in.nextInt());
/* 230 */         } catch (NumberFormatException e) {
/* 231 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 236 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 240 */   public static final TypeAdapterFactory INTEGER_FACTORY = newFactory(int.class, (Class)Integer.class, (TypeAdapter)INTEGER);
/*     */   
/* 242 */   public static final TypeAdapter<AtomicInteger> ATOMIC_INTEGER = (new TypeAdapter<AtomicInteger>() {
/*     */       public AtomicInteger read(JsonReader in) throws IOException {
/*     */         try {
/* 245 */           return new AtomicInteger(in.nextInt());
/* 246 */         } catch (NumberFormatException e) {
/* 247 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       public void write(JsonWriter out, AtomicInteger value) throws IOException {
/* 251 */         out.value(value.get());
/*     */       }
/* 253 */     }).nullSafe();
/*     */   
/* 255 */   public static final TypeAdapterFactory ATOMIC_INTEGER_FACTORY = newFactory(AtomicInteger.class, ATOMIC_INTEGER);
/*     */   
/* 257 */   public static final TypeAdapter<AtomicBoolean> ATOMIC_BOOLEAN = (new TypeAdapter<AtomicBoolean>() {
/*     */       public AtomicBoolean read(JsonReader in) throws IOException {
/* 259 */         return new AtomicBoolean(in.nextBoolean());
/*     */       }
/*     */       public void write(JsonWriter out, AtomicBoolean value) throws IOException {
/* 262 */         out.value(value.get());
/*     */       }
/* 264 */     }).nullSafe();
/*     */   
/* 266 */   public static final TypeAdapterFactory ATOMIC_BOOLEAN_FACTORY = newFactory(AtomicBoolean.class, ATOMIC_BOOLEAN);
/*     */   
/* 268 */   public static final TypeAdapter<AtomicIntegerArray> ATOMIC_INTEGER_ARRAY = (new TypeAdapter<AtomicIntegerArray>() {
/*     */       public AtomicIntegerArray read(JsonReader in) throws IOException {
/* 270 */         List<Integer> list = new ArrayList<Integer>();
/* 271 */         in.beginArray();
/* 272 */         while (in.hasNext()) {
/*     */           try {
/* 274 */             int integer = in.nextInt();
/* 275 */             list.add(Integer.valueOf(integer));
/* 276 */           } catch (NumberFormatException e) {
/* 277 */             throw new JsonSyntaxException(e);
/*     */           } 
/*     */         } 
/* 280 */         in.endArray();
/* 281 */         int length = list.size();
/* 282 */         AtomicIntegerArray array = new AtomicIntegerArray(length);
/* 283 */         for (int i = 0; i < length; i++) {
/* 284 */           array.set(i, ((Integer)list.get(i)).intValue());
/*     */         }
/* 286 */         return array;
/*     */       }
/*     */       public void write(JsonWriter out, AtomicIntegerArray value) throws IOException {
/* 289 */         out.beginArray();
/* 290 */         for (int i = 0, length = value.length(); i < length; i++) {
/* 291 */           out.value(value.get(i));
/*     */         }
/* 293 */         out.endArray();
/*     */       }
/* 295 */     }).nullSafe();
/*     */   
/* 297 */   public static final TypeAdapterFactory ATOMIC_INTEGER_ARRAY_FACTORY = newFactory(AtomicIntegerArray.class, ATOMIC_INTEGER_ARRAY);
/*     */   
/* 299 */   public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 302 */         if (in.peek() == JsonToken.NULL) {
/* 303 */           in.nextNull();
/* 304 */           return null;
/*     */         } 
/*     */         try {
/* 307 */           return Long.valueOf(in.nextLong());
/* 308 */         } catch (NumberFormatException e) {
/* 309 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 314 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 318 */   public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 321 */         if (in.peek() == JsonToken.NULL) {
/* 322 */           in.nextNull();
/* 323 */           return null;
/*     */         } 
/* 325 */         return Float.valueOf((float)in.nextDouble());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 329 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 333 */   public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 336 */         if (in.peek() == JsonToken.NULL) {
/* 337 */           in.nextNull();
/* 338 */           return null;
/*     */         } 
/* 340 */         return Double.valueOf(in.nextDouble());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 344 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 348 */   public static final TypeAdapter<Number> NUMBER = new TypeAdapter<Number>()
/*     */     {
/*     */       public Number read(JsonReader in) throws IOException {
/* 351 */         JsonToken jsonToken = in.peek();
/* 352 */         switch (jsonToken) {
/*     */           case NULL:
/* 354 */             in.nextNull();
/* 355 */             return null;
/*     */           case NUMBER:
/*     */           case STRING:
/* 358 */             return (Number)new LazilyParsedNumber(in.nextString());
/*     */         } 
/* 360 */         throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, Number value) throws IOException {
/* 365 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 369 */   public static final TypeAdapterFactory NUMBER_FACTORY = newFactory(Number.class, NUMBER);
/*     */   
/* 371 */   public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>()
/*     */     {
/*     */       public Character read(JsonReader in) throws IOException {
/* 374 */         if (in.peek() == JsonToken.NULL) {
/* 375 */           in.nextNull();
/* 376 */           return null;
/*     */         } 
/* 378 */         String str = in.nextString();
/* 379 */         if (str.length() != 1) {
/* 380 */           throw new JsonSyntaxException("Expecting character, got: " + str);
/*     */         }
/* 382 */         return Character.valueOf(str.charAt(0));
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Character value) throws IOException {
/* 386 */         out.value((value == null) ? null : String.valueOf(value));
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 391 */   public static final TypeAdapterFactory CHARACTER_FACTORY = newFactory(char.class, (Class)Character.class, (TypeAdapter)CHARACTER);
/*     */   
/* 393 */   public static final TypeAdapter<String> STRING = new TypeAdapter<String>()
/*     */     {
/*     */       public String read(JsonReader in) throws IOException {
/* 396 */         JsonToken peek = in.peek();
/* 397 */         if (peek == JsonToken.NULL) {
/* 398 */           in.nextNull();
/* 399 */           return null;
/*     */         } 
/*     */         
/* 402 */         if (peek == JsonToken.BOOLEAN) {
/* 403 */           return Boolean.toString(in.nextBoolean());
/*     */         }
/* 405 */         return in.nextString();
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, String value) throws IOException {
/* 409 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 413 */   public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
/*     */       public BigDecimal read(JsonReader in) throws IOException {
/* 415 */         if (in.peek() == JsonToken.NULL) {
/* 416 */           in.nextNull();
/* 417 */           return null;
/*     */         } 
/*     */         try {
/* 420 */           return new BigDecimal(in.nextString());
/* 421 */         } catch (NumberFormatException e) {
/* 422 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, BigDecimal value) throws IOException {
/* 427 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 431 */   public static final TypeAdapter<BigInteger> BIG_INTEGER = new TypeAdapter<BigInteger>() {
/*     */       public BigInteger read(JsonReader in) throws IOException {
/* 433 */         if (in.peek() == JsonToken.NULL) {
/* 434 */           in.nextNull();
/* 435 */           return null;
/*     */         } 
/*     */         try {
/* 438 */           return new BigInteger(in.nextString());
/* 439 */         } catch (NumberFormatException e) {
/* 440 */           throw new JsonSyntaxException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, BigInteger value) throws IOException {
/* 445 */         out.value(value);
/*     */       }
/*     */     };
/*     */   
/* 449 */   public static final TypeAdapterFactory STRING_FACTORY = newFactory(String.class, STRING);
/*     */   
/* 451 */   public static final TypeAdapter<StringBuilder> STRING_BUILDER = new TypeAdapter<StringBuilder>()
/*     */     {
/*     */       public StringBuilder read(JsonReader in) throws IOException {
/* 454 */         if (in.peek() == JsonToken.NULL) {
/* 455 */           in.nextNull();
/* 456 */           return null;
/*     */         } 
/* 458 */         return new StringBuilder(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, StringBuilder value) throws IOException {
/* 462 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 467 */   public static final TypeAdapterFactory STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);
/*     */   
/* 469 */   public static final TypeAdapter<StringBuffer> STRING_BUFFER = new TypeAdapter<StringBuffer>()
/*     */     {
/*     */       public StringBuffer read(JsonReader in) throws IOException {
/* 472 */         if (in.peek() == JsonToken.NULL) {
/* 473 */           in.nextNull();
/* 474 */           return null;
/*     */         } 
/* 476 */         return new StringBuffer(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, StringBuffer value) throws IOException {
/* 480 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 485 */   public static final TypeAdapterFactory STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);
/*     */   
/* 487 */   public static final TypeAdapter<URL> URL = new TypeAdapter<URL>()
/*     */     {
/*     */       public URL read(JsonReader in) throws IOException {
/* 490 */         if (in.peek() == JsonToken.NULL) {
/* 491 */           in.nextNull();
/* 492 */           return null;
/*     */         } 
/* 494 */         String nextString = in.nextString();
/* 495 */         return "null".equals(nextString) ? null : new URL(nextString);
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, URL value) throws IOException {
/* 499 */         out.value((value == null) ? null : value.toExternalForm());
/*     */       }
/*     */     };
/*     */   
/* 503 */   public static final TypeAdapterFactory URL_FACTORY = newFactory(URL.class, URL);
/*     */   
/* 505 */   public static final TypeAdapter<URI> URI = new TypeAdapter<URI>()
/*     */     {
/*     */       public URI read(JsonReader in) throws IOException {
/* 508 */         if (in.peek() == JsonToken.NULL) {
/* 509 */           in.nextNull();
/* 510 */           return null;
/*     */         } 
/*     */         try {
/* 513 */           String nextString = in.nextString();
/* 514 */           return "null".equals(nextString) ? null : new URI(nextString);
/* 515 */         } catch (URISyntaxException e) {
/* 516 */           throw new JsonIOException(e);
/*     */         } 
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, URI value) throws IOException {
/* 521 */         out.value((value == null) ? null : value.toASCIIString());
/*     */       }
/*     */     };
/*     */   
/* 525 */   public static final TypeAdapterFactory URI_FACTORY = newFactory(URI.class, URI);
/*     */   
/* 527 */   public static final TypeAdapter<InetAddress> INET_ADDRESS = new TypeAdapter<InetAddress>()
/*     */     {
/*     */       public InetAddress read(JsonReader in) throws IOException {
/* 530 */         if (in.peek() == JsonToken.NULL) {
/* 531 */           in.nextNull();
/* 532 */           return null;
/*     */         } 
/*     */         
/* 535 */         return InetAddress.getByName(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, InetAddress value) throws IOException {
/* 539 */         out.value((value == null) ? null : value.getHostAddress());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 544 */   public static final TypeAdapterFactory INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
/*     */   
/* 546 */   public static final TypeAdapter<UUID> UUID = new TypeAdapter<UUID>()
/*     */     {
/*     */       public UUID read(JsonReader in) throws IOException {
/* 549 */         if (in.peek() == JsonToken.NULL) {
/* 550 */           in.nextNull();
/* 551 */           return null;
/*     */         } 
/* 553 */         return UUID.fromString(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, UUID value) throws IOException {
/* 557 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 561 */   public static final TypeAdapterFactory UUID_FACTORY = newFactory(UUID.class, UUID);
/*     */   
/* 563 */   public static final TypeAdapter<Currency> CURRENCY = (new TypeAdapter<Currency>()
/*     */     {
/*     */       public Currency read(JsonReader in) throws IOException {
/* 566 */         return Currency.getInstance(in.nextString());
/*     */       }
/*     */       
/*     */       public void write(JsonWriter out, Currency value) throws IOException {
/* 570 */         out.value(value.getCurrencyCode());
/*     */       }
/* 572 */     }).nullSafe();
/* 573 */   public static final TypeAdapterFactory CURRENCY_FACTORY = newFactory(Currency.class, CURRENCY);
/*     */   
/* 575 */   public static final TypeAdapterFactory TIMESTAMP_FACTORY = new TypeAdapterFactory()
/*     */     {
/*     */       public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 578 */         if (typeToken.getRawType() != Timestamp.class) {
/* 579 */           return null;
/*     */         }
/*     */         
/* 582 */         final TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);
/* 583 */         return (TypeAdapter)new TypeAdapter<Timestamp>() {
/*     */             public Timestamp read(JsonReader in) throws IOException {
/* 585 */               Date date = (Date)dateTypeAdapter.read(in);
/* 586 */               return (date != null) ? new Timestamp(date.getTime()) : null;
/*     */             }
/*     */             
/*     */             public void write(JsonWriter out, Timestamp value) throws IOException {
/* 590 */               dateTypeAdapter.write(out, value);
/*     */             }
/*     */           };
/*     */       }
/*     */     };
/*     */   
/* 596 */   public static final TypeAdapter<Calendar> CALENDAR = new TypeAdapter<Calendar>()
/*     */     {
/*     */       private static final String YEAR = "year";
/*     */       private static final String MONTH = "month";
/*     */       private static final String DAY_OF_MONTH = "dayOfMonth";
/*     */       private static final String HOUR_OF_DAY = "hourOfDay";
/*     */       private static final String MINUTE = "minute";
/*     */       private static final String SECOND = "second";
/*     */       
/*     */       public Calendar read(JsonReader in) throws IOException {
/* 606 */         if (in.peek() == JsonToken.NULL) {
/* 607 */           in.nextNull();
/* 608 */           return null;
/*     */         } 
/* 610 */         in.beginObject();
/* 611 */         int year = 0;
/* 612 */         int month = 0;
/* 613 */         int dayOfMonth = 0;
/* 614 */         int hourOfDay = 0;
/* 615 */         int minute = 0;
/* 616 */         int second = 0;
/* 617 */         while (in.peek() != JsonToken.END_OBJECT) {
/* 618 */           String name = in.nextName();
/* 619 */           int value = in.nextInt();
/* 620 */           if ("year".equals(name)) {
/* 621 */             year = value; continue;
/* 622 */           }  if ("month".equals(name)) {
/* 623 */             month = value; continue;
/* 624 */           }  if ("dayOfMonth".equals(name)) {
/* 625 */             dayOfMonth = value; continue;
/* 626 */           }  if ("hourOfDay".equals(name)) {
/* 627 */             hourOfDay = value; continue;
/* 628 */           }  if ("minute".equals(name)) {
/* 629 */             minute = value; continue;
/* 630 */           }  if ("second".equals(name)) {
/* 631 */             second = value;
/*     */           }
/*     */         } 
/* 634 */         in.endObject();
/* 635 */         return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, Calendar value) throws IOException {
/* 640 */         if (value == null) {
/* 641 */           out.nullValue();
/*     */           return;
/*     */         } 
/* 644 */         out.beginObject();
/* 645 */         out.name("year");
/* 646 */         out.value(value.get(1));
/* 647 */         out.name("month");
/* 648 */         out.value(value.get(2));
/* 649 */         out.name("dayOfMonth");
/* 650 */         out.value(value.get(5));
/* 651 */         out.name("hourOfDay");
/* 652 */         out.value(value.get(11));
/* 653 */         out.name("minute");
/* 654 */         out.value(value.get(12));
/* 655 */         out.name("second");
/* 656 */         out.value(value.get(13));
/* 657 */         out.endObject();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/* 662 */   public static final TypeAdapterFactory CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, (Class)GregorianCalendar.class, CALENDAR);
/*     */   
/* 664 */   public static final TypeAdapter<Locale> LOCALE = new TypeAdapter<Locale>()
/*     */     {
/*     */       public Locale read(JsonReader in) throws IOException {
/* 667 */         if (in.peek() == JsonToken.NULL) {
/* 668 */           in.nextNull();
/* 669 */           return null;
/*     */         } 
/* 671 */         String locale = in.nextString();
/* 672 */         StringTokenizer tokenizer = new StringTokenizer(locale, "_");
/* 673 */         String language = null;
/* 674 */         String country = null;
/* 675 */         String variant = null;
/* 676 */         if (tokenizer.hasMoreElements()) {
/* 677 */           language = tokenizer.nextToken();
/*     */         }
/* 679 */         if (tokenizer.hasMoreElements()) {
/* 680 */           country = tokenizer.nextToken();
/*     */         }
/* 682 */         if (tokenizer.hasMoreElements()) {
/* 683 */           variant = tokenizer.nextToken();
/*     */         }
/* 685 */         if (country == null && variant == null)
/* 686 */           return new Locale(language); 
/* 687 */         if (variant == null) {
/* 688 */           return new Locale(language, country);
/*     */         }
/* 690 */         return new Locale(language, country, variant);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, Locale value) throws IOException {
/* 695 */         out.value((value == null) ? null : value.toString());
/*     */       }
/*     */     };
/*     */   
/* 699 */   public static final TypeAdapterFactory LOCALE_FACTORY = newFactory(Locale.class, LOCALE);
/*     */   
/* 701 */   public static final TypeAdapter<JsonElement> JSON_ELEMENT = new TypeAdapter<JsonElement>() { public JsonElement read(JsonReader in) throws IOException { String number; JsonArray array;
/*     */         JsonObject object;
/* 703 */         switch (in.peek()) {
/*     */           case STRING:
/* 705 */             return (JsonElement)new JsonPrimitive(in.nextString());
/*     */           case NUMBER:
/* 707 */             number = in.nextString();
/* 708 */             return (JsonElement)new JsonPrimitive((Number)new LazilyParsedNumber(number));
/*     */           case BOOLEAN:
/* 710 */             return (JsonElement)new JsonPrimitive(Boolean.valueOf(in.nextBoolean()));
/*     */           case NULL:
/* 712 */             in.nextNull();
/* 713 */             return (JsonElement)JsonNull.INSTANCE;
/*     */           case BEGIN_ARRAY:
/* 715 */             array = new JsonArray();
/* 716 */             in.beginArray();
/* 717 */             while (in.hasNext()) {
/* 718 */               array.add(read(in));
/*     */             }
/* 720 */             in.endArray();
/* 721 */             return (JsonElement)array;
/*     */           case BEGIN_OBJECT:
/* 723 */             object = new JsonObject();
/* 724 */             in.beginObject();
/* 725 */             while (in.hasNext()) {
/* 726 */               object.add(in.nextName(), read(in));
/*     */             }
/* 728 */             in.endObject();
/* 729 */             return (JsonElement)object;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 735 */         throw new IllegalArgumentException(); }
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(JsonWriter out, JsonElement value) throws IOException {
/* 740 */         if (value == null || value.isJsonNull()) {
/* 741 */           out.nullValue();
/* 742 */         } else if (value.isJsonPrimitive()) {
/* 743 */           JsonPrimitive primitive = value.getAsJsonPrimitive();
/* 744 */           if (primitive.isNumber()) {
/* 745 */             out.value(primitive.getAsNumber());
/* 746 */           } else if (primitive.isBoolean()) {
/* 747 */             out.value(primitive.getAsBoolean());
/*     */           } else {
/* 749 */             out.value(primitive.getAsString());
/*     */           }
/*     */         
/* 752 */         } else if (value.isJsonArray()) {
/* 753 */           out.beginArray();
/* 754 */           for (JsonElement e : value.getAsJsonArray()) {
/* 755 */             write(out, e);
/*     */           }
/* 757 */           out.endArray();
/*     */         }
/* 759 */         else if (value.isJsonObject()) {
/* 760 */           out.beginObject();
/* 761 */           for (Map.Entry<String, JsonElement> e : (Iterable<Map.Entry<String, JsonElement>>)value.getAsJsonObject().entrySet()) {
/* 762 */             out.name(e.getKey());
/* 763 */             write(out, e.getValue());
/*     */           } 
/* 765 */           out.endObject();
/*     */         } else {
/*     */           
/* 768 */           throw new IllegalArgumentException("Couldn't write " + value.getClass());
/*     */         } 
/*     */       } }
/*     */   ;
/*     */ 
/*     */   
/* 774 */   public static final TypeAdapterFactory JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);
/*     */   
/*     */   private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
/* 777 */     private final Map<String, T> nameToConstant = new HashMap<String, T>();
/* 778 */     private final Map<T, String> constantToName = new HashMap<T, String>();
/*     */     
/*     */     public EnumTypeAdapter(Class<T> classOfT) {
/*     */       try {
/* 782 */         for (Field field : classOfT.getDeclaredFields()) {
/* 783 */           if (field.isEnumConstant())
/*     */           
/*     */           { 
/* 786 */             AccessController.doPrivileged(new PrivilegedAction<Void>() {
/*     */                   public Void run() {
/* 788 */                     field.setAccessible(true);
/* 789 */                     return null;
/*     */                   }
/*     */                 });
/*     */             
/* 793 */             Enum enum_ = (Enum)field.get(null);
/* 794 */             String name = enum_.name();
/* 795 */             SerializedName annotation = field.<SerializedName>getAnnotation(SerializedName.class);
/* 796 */             if (annotation != null) {
/* 797 */               name = annotation.value();
/* 798 */               for (String alternate : annotation.alternate()) {
/* 799 */                 this.nameToConstant.put(alternate, (T)enum_);
/*     */               }
/*     */             } 
/* 802 */             this.nameToConstant.put(name, (T)enum_);
/* 803 */             this.constantToName.put((T)enum_, name); } 
/*     */         } 
/* 805 */       } catch (IllegalAccessException e) {
/* 806 */         throw new AssertionError(e);
/*     */       } 
/*     */     }
/*     */     public T read(JsonReader in) throws IOException {
/* 810 */       if (in.peek() == JsonToken.NULL) {
/* 811 */         in.nextNull();
/* 812 */         return null;
/*     */       } 
/* 814 */       return this.nameToConstant.get(in.nextString());
/*     */     }
/*     */     
/*     */     public void write(JsonWriter out, T value) throws IOException {
/* 818 */       out.value((value == null) ? null : this.constantToName.get(value));
/*     */     }
/*     */   }
/*     */   
/* 822 */   public static final TypeAdapterFactory ENUM_FACTORY = new TypeAdapterFactory()
/*     */     {
/*     */       public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 825 */         Class<? super T> rawType = typeToken.getRawType();
/* 826 */         if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
/* 827 */           return null;
/*     */         }
/* 829 */         if (!rawType.isEnum()) {
/* 830 */           rawType = rawType.getSuperclass();
/*     */         }
/* 832 */         return new TypeAdapters.EnumTypeAdapter<T>(rawType);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactory(final TypeToken<TT> type, final TypeAdapter<TT> typeAdapter) {
/* 838 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 841 */           return typeToken.equals(type) ? typeAdapter : null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactory(final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
/* 848 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 851 */           return (typeToken.getRawType() == type) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 854 */           return "Factory[type=" + type.getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactory(final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
/* 861 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 864 */           Class<? super T> rawType = typeToken.getRawType();
/* 865 */           return (rawType == unboxed || rawType == boxed) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 868 */           return "Factory[type=" + boxed.getName() + "+" + unboxed
/* 869 */             .getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> base, final Class<? extends TT> sub, final TypeAdapter<? super TT> typeAdapter) {
/* 876 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 879 */           Class<? super T> rawType = typeToken.getRawType();
/* 880 */           return (rawType == base || rawType == sub) ? typeAdapter : null;
/*     */         }
/*     */         public String toString() {
/* 883 */           return "Factory[type=" + base.getName() + "+" + sub
/* 884 */             .getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T1> TypeAdapterFactory newTypeHierarchyFactory(final Class<T1> clazz, final TypeAdapter<T1> typeAdapter) {
/* 895 */     return new TypeAdapterFactory()
/*     */       {
/*     */         public <T2> TypeAdapter<T2> create(Gson gson, TypeToken<T2> typeToken) {
/* 898 */           final Class<? super T2> requestedType = typeToken.getRawType();
/* 899 */           if (!clazz.isAssignableFrom(requestedType)) {
/* 900 */             return null;
/*     */           }
/* 902 */           return new TypeAdapter<T1>() {
/*     */               public void write(JsonWriter out, T1 value) throws IOException {
/* 904 */                 typeAdapter.write(out, value);
/*     */               }
/*     */               
/*     */               public T1 read(JsonReader in) throws IOException {
/* 908 */                 T1 result = (T1)typeAdapter.read(in);
/* 909 */                 if (result != null && !requestedType.isInstance(result)) {
/* 910 */                   throw new JsonSyntaxException("Expected a " + requestedType.getName() + " but was " + result
/* 911 */                       .getClass().getName());
/*     */                 }
/* 913 */                 return result;
/*     */               }
/*     */             };
/*     */         }
/*     */         public String toString() {
/* 918 */           return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + typeAdapter + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\internal\bind\TypeAdapters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */