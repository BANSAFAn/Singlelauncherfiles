/*      */ package com.google.gson;
/*      */ 
/*      */ import com.google.gson.internal.ConstructorConstructor;
/*      */ import com.google.gson.internal.Excluder;
/*      */ import com.google.gson.internal.Primitives;
/*      */ import com.google.gson.internal.Streams;
/*      */ import com.google.gson.internal.bind.ArrayTypeAdapter;
/*      */ import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
/*      */ import com.google.gson.internal.bind.DateTypeAdapter;
/*      */ import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
/*      */ import com.google.gson.internal.bind.JsonTreeReader;
/*      */ import com.google.gson.internal.bind.JsonTreeWriter;
/*      */ import com.google.gson.internal.bind.MapTypeAdapterFactory;
/*      */ import com.google.gson.internal.bind.ObjectTypeAdapter;
/*      */ import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
/*      */ import com.google.gson.internal.bind.SqlDateTypeAdapter;
/*      */ import com.google.gson.internal.bind.TimeTypeAdapter;
/*      */ import com.google.gson.internal.bind.TypeAdapters;
/*      */ import com.google.gson.reflect.TypeToken;
/*      */ import com.google.gson.stream.JsonReader;
/*      */ import com.google.gson.stream.JsonToken;
/*      */ import com.google.gson.stream.JsonWriter;
/*      */ import com.google.gson.stream.MalformedJsonException;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Type;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.concurrent.atomic.AtomicLongArray;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Gson
/*      */ {
/*      */   static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
/*      */   static final boolean DEFAULT_LENIENT = false;
/*      */   static final boolean DEFAULT_PRETTY_PRINT = false;
/*      */   static final boolean DEFAULT_ESCAPE_HTML = true;
/*      */   static final boolean DEFAULT_SERIALIZE_NULLS = false;
/*      */   static final boolean DEFAULT_COMPLEX_MAP_KEYS = false;
/*      */   static final boolean DEFAULT_SPECIALIZE_FLOAT_VALUES = false;
/*  114 */   private static final TypeToken<?> NULL_KEY_SURROGATE = TypeToken.get(Object.class);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  124 */   private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls = new ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>>();
/*      */ 
/*      */   
/*  127 */   private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache = new ConcurrentHashMap<TypeToken<?>, TypeAdapter<?>>();
/*      */ 
/*      */   
/*      */   private final ConstructorConstructor constructorConstructor;
/*      */ 
/*      */   
/*      */   private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
/*      */ 
/*      */   
/*      */   final List<TypeAdapterFactory> factories;
/*      */ 
/*      */   
/*      */   final Excluder excluder;
/*      */ 
/*      */   
/*      */   final FieldNamingStrategy fieldNamingStrategy;
/*      */ 
/*      */   
/*      */   final Map<Type, InstanceCreator<?>> instanceCreators;
/*      */ 
/*      */   
/*      */   final boolean serializeNulls;
/*      */ 
/*      */   
/*      */   final boolean complexMapKeySerialization;
/*      */ 
/*      */   
/*      */   final boolean generateNonExecutableJson;
/*      */ 
/*      */   
/*      */   final boolean htmlSafe;
/*      */ 
/*      */   
/*      */   final boolean prettyPrinting;
/*      */ 
/*      */   
/*      */   final boolean lenient;
/*      */ 
/*      */   
/*      */   final boolean serializeSpecialFloatingPointValues;
/*      */ 
/*      */   
/*      */   final String datePattern;
/*      */ 
/*      */   
/*      */   final int dateStyle;
/*      */ 
/*      */   
/*      */   final int timeStyle;
/*      */ 
/*      */   
/*      */   final LongSerializationPolicy longSerializationPolicy;
/*      */   
/*      */   final List<TypeAdapterFactory> builderFactories;
/*      */   
/*      */   final List<TypeAdapterFactory> builderHierarchyFactories;
/*      */ 
/*      */   
/*      */   public Gson() {
/*  186 */     this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, 
/*  187 */         Collections.emptyMap(), false, false, false, true, false, false, false, LongSerializationPolicy.DEFAULT, null, 2, 2, 
/*      */ 
/*      */ 
/*      */         
/*  191 */         Collections.emptyList(), Collections.emptyList(), 
/*  192 */         Collections.emptyList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Gson(Excluder excluder, FieldNamingStrategy fieldNamingStrategy, Map<Type, InstanceCreator<?>> instanceCreators, boolean serializeNulls, boolean complexMapKeySerialization, boolean generateNonExecutableGson, boolean htmlSafe, boolean prettyPrinting, boolean lenient, boolean serializeSpecialFloatingPointValues, LongSerializationPolicy longSerializationPolicy, String datePattern, int dateStyle, int timeStyle, List<TypeAdapterFactory> builderFactories, List<TypeAdapterFactory> builderHierarchyFactories, List<TypeAdapterFactory> factoriesToBeAdded) {
/*  203 */     this.excluder = excluder;
/*  204 */     this.fieldNamingStrategy = fieldNamingStrategy;
/*  205 */     this.instanceCreators = instanceCreators;
/*  206 */     this.constructorConstructor = new ConstructorConstructor(instanceCreators);
/*  207 */     this.serializeNulls = serializeNulls;
/*  208 */     this.complexMapKeySerialization = complexMapKeySerialization;
/*  209 */     this.generateNonExecutableJson = generateNonExecutableGson;
/*  210 */     this.htmlSafe = htmlSafe;
/*  211 */     this.prettyPrinting = prettyPrinting;
/*  212 */     this.lenient = lenient;
/*  213 */     this.serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValues;
/*  214 */     this.longSerializationPolicy = longSerializationPolicy;
/*  215 */     this.datePattern = datePattern;
/*  216 */     this.dateStyle = dateStyle;
/*  217 */     this.timeStyle = timeStyle;
/*  218 */     this.builderFactories = builderFactories;
/*  219 */     this.builderHierarchyFactories = builderHierarchyFactories;
/*      */     
/*  221 */     List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>();
/*      */ 
/*      */     
/*  224 */     factories.add(TypeAdapters.JSON_ELEMENT_FACTORY);
/*  225 */     factories.add(ObjectTypeAdapter.FACTORY);
/*      */ 
/*      */     
/*  228 */     factories.add(excluder);
/*      */ 
/*      */     
/*  231 */     factories.addAll(factoriesToBeAdded);
/*      */ 
/*      */     
/*  234 */     factories.add(TypeAdapters.STRING_FACTORY);
/*  235 */     factories.add(TypeAdapters.INTEGER_FACTORY);
/*  236 */     factories.add(TypeAdapters.BOOLEAN_FACTORY);
/*  237 */     factories.add(TypeAdapters.BYTE_FACTORY);
/*  238 */     factories.add(TypeAdapters.SHORT_FACTORY);
/*  239 */     TypeAdapter<Number> longAdapter = longAdapter(longSerializationPolicy);
/*  240 */     factories.add(TypeAdapters.newFactory(long.class, Long.class, longAdapter));
/*  241 */     factories.add(TypeAdapters.newFactory(double.class, Double.class, 
/*  242 */           doubleAdapter(serializeSpecialFloatingPointValues)));
/*  243 */     factories.add(TypeAdapters.newFactory(float.class, Float.class, 
/*  244 */           floatAdapter(serializeSpecialFloatingPointValues)));
/*  245 */     factories.add(TypeAdapters.NUMBER_FACTORY);
/*  246 */     factories.add(TypeAdapters.ATOMIC_INTEGER_FACTORY);
/*  247 */     factories.add(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
/*  248 */     factories.add(TypeAdapters.newFactory(AtomicLong.class, atomicLongAdapter(longAdapter)));
/*  249 */     factories.add(TypeAdapters.newFactory(AtomicLongArray.class, atomicLongArrayAdapter(longAdapter)));
/*  250 */     factories.add(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
/*  251 */     factories.add(TypeAdapters.CHARACTER_FACTORY);
/*  252 */     factories.add(TypeAdapters.STRING_BUILDER_FACTORY);
/*  253 */     factories.add(TypeAdapters.STRING_BUFFER_FACTORY);
/*  254 */     factories.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
/*  255 */     factories.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
/*  256 */     factories.add(TypeAdapters.URL_FACTORY);
/*  257 */     factories.add(TypeAdapters.URI_FACTORY);
/*  258 */     factories.add(TypeAdapters.UUID_FACTORY);
/*  259 */     factories.add(TypeAdapters.CURRENCY_FACTORY);
/*  260 */     factories.add(TypeAdapters.LOCALE_FACTORY);
/*  261 */     factories.add(TypeAdapters.INET_ADDRESS_FACTORY);
/*  262 */     factories.add(TypeAdapters.BIT_SET_FACTORY);
/*  263 */     factories.add(DateTypeAdapter.FACTORY);
/*  264 */     factories.add(TypeAdapters.CALENDAR_FACTORY);
/*  265 */     factories.add(TimeTypeAdapter.FACTORY);
/*  266 */     factories.add(SqlDateTypeAdapter.FACTORY);
/*  267 */     factories.add(TypeAdapters.TIMESTAMP_FACTORY);
/*  268 */     factories.add(ArrayTypeAdapter.FACTORY);
/*  269 */     factories.add(TypeAdapters.CLASS_FACTORY);
/*      */ 
/*      */     
/*  272 */     factories.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
/*  273 */     factories.add(new MapTypeAdapterFactory(this.constructorConstructor, complexMapKeySerialization));
/*  274 */     this.jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(this.constructorConstructor);
/*  275 */     factories.add(this.jsonAdapterFactory);
/*  276 */     factories.add(TypeAdapters.ENUM_FACTORY);
/*  277 */     factories.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, fieldNamingStrategy, excluder, this.jsonAdapterFactory));
/*      */ 
/*      */     
/*  280 */     this.factories = Collections.unmodifiableList(factories);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GsonBuilder newBuilder() {
/*  290 */     return new GsonBuilder(this);
/*      */   }
/*      */   
/*      */   public Excluder excluder() {
/*  294 */     return this.excluder;
/*      */   }
/*      */   
/*      */   public FieldNamingStrategy fieldNamingStrategy() {
/*  298 */     return this.fieldNamingStrategy;
/*      */   }
/*      */   
/*      */   public boolean serializeNulls() {
/*  302 */     return this.serializeNulls;
/*      */   }
/*      */   
/*      */   public boolean htmlSafe() {
/*  306 */     return this.htmlSafe;
/*      */   }
/*      */   
/*      */   private TypeAdapter<Number> doubleAdapter(boolean serializeSpecialFloatingPointValues) {
/*  310 */     if (serializeSpecialFloatingPointValues) {
/*  311 */       return TypeAdapters.DOUBLE;
/*      */     }
/*  313 */     return new TypeAdapter<Number>() {
/*      */         public Double read(JsonReader in) throws IOException {
/*  315 */           if (in.peek() == JsonToken.NULL) {
/*  316 */             in.nextNull();
/*  317 */             return null;
/*      */           } 
/*  319 */           return Double.valueOf(in.nextDouble());
/*      */         }
/*      */         public void write(JsonWriter out, Number value) throws IOException {
/*  322 */           if (value == null) {
/*  323 */             out.nullValue();
/*      */             return;
/*      */           } 
/*  326 */           double doubleValue = value.doubleValue();
/*  327 */           Gson.checkValidFloatingPoint(doubleValue);
/*  328 */           out.value(value);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private TypeAdapter<Number> floatAdapter(boolean serializeSpecialFloatingPointValues) {
/*  334 */     if (serializeSpecialFloatingPointValues) {
/*  335 */       return TypeAdapters.FLOAT;
/*      */     }
/*  337 */     return new TypeAdapter<Number>() {
/*      */         public Float read(JsonReader in) throws IOException {
/*  339 */           if (in.peek() == JsonToken.NULL) {
/*  340 */             in.nextNull();
/*  341 */             return null;
/*      */           } 
/*  343 */           return Float.valueOf((float)in.nextDouble());
/*      */         }
/*      */         public void write(JsonWriter out, Number value) throws IOException {
/*  346 */           if (value == null) {
/*  347 */             out.nullValue();
/*      */             return;
/*      */           } 
/*  350 */           float floatValue = value.floatValue();
/*  351 */           Gson.checkValidFloatingPoint(floatValue);
/*  352 */           out.value(value);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static void checkValidFloatingPoint(double value) {
/*  358 */     if (Double.isNaN(value) || Double.isInfinite(value)) {
/*  359 */       throw new IllegalArgumentException(value + " is not a valid double value as per JSON specification. To override this behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy) {
/*  366 */     if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
/*  367 */       return TypeAdapters.LONG;
/*      */     }
/*  369 */     return new TypeAdapter<Number>() {
/*      */         public Number read(JsonReader in) throws IOException {
/*  371 */           if (in.peek() == JsonToken.NULL) {
/*  372 */             in.nextNull();
/*  373 */             return null;
/*      */           } 
/*  375 */           return Long.valueOf(in.nextLong());
/*      */         }
/*      */         public void write(JsonWriter out, Number value) throws IOException {
/*  378 */           if (value == null) {
/*  379 */             out.nullValue();
/*      */             return;
/*      */           } 
/*  382 */           out.value(value.toString());
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static TypeAdapter<AtomicLong> atomicLongAdapter(final TypeAdapter<Number> longAdapter) {
/*  388 */     return (new TypeAdapter<AtomicLong>() {
/*      */         public void write(JsonWriter out, AtomicLong value) throws IOException {
/*  390 */           longAdapter.write(out, Long.valueOf(value.get()));
/*      */         }
/*      */         public AtomicLong read(JsonReader in) throws IOException {
/*  393 */           Number value = longAdapter.read(in);
/*  394 */           return new AtomicLong(value.longValue());
/*      */         }
/*  396 */       }).nullSafe();
/*      */   }
/*      */   
/*      */   private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(final TypeAdapter<Number> longAdapter) {
/*  400 */     return (new TypeAdapter<AtomicLongArray>() {
/*      */         public void write(JsonWriter out, AtomicLongArray value) throws IOException {
/*  402 */           out.beginArray();
/*  403 */           for (int i = 0, length = value.length(); i < length; i++) {
/*  404 */             longAdapter.write(out, Long.valueOf(value.get(i)));
/*      */           }
/*  406 */           out.endArray();
/*      */         }
/*      */         public AtomicLongArray read(JsonReader in) throws IOException {
/*  409 */           List<Long> list = new ArrayList<Long>();
/*  410 */           in.beginArray();
/*  411 */           while (in.hasNext()) {
/*  412 */             long value = ((Number)longAdapter.read(in)).longValue();
/*  413 */             list.add(Long.valueOf(value));
/*      */           } 
/*  415 */           in.endArray();
/*  416 */           int length = list.size();
/*  417 */           AtomicLongArray array = new AtomicLongArray(length);
/*  418 */           for (int i = 0; i < length; i++) {
/*  419 */             array.set(i, ((Long)list.get(i)).longValue());
/*      */           }
/*  421 */           return array;
/*      */         }
/*  423 */       }).nullSafe();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> TypeAdapter<T> getAdapter(TypeToken<T> type) {
/*  434 */     TypeAdapter<?> cached = this.typeTokenCache.get((type == null) ? NULL_KEY_SURROGATE : type);
/*  435 */     if (cached != null) {
/*  436 */       return (TypeAdapter)cached;
/*      */     }
/*      */     
/*  439 */     Map<TypeToken<?>, FutureTypeAdapter<?>> threadCalls = this.calls.get();
/*  440 */     boolean requiresThreadLocalCleanup = false;
/*  441 */     if (threadCalls == null) {
/*  442 */       threadCalls = new HashMap<TypeToken<?>, FutureTypeAdapter<?>>();
/*  443 */       this.calls.set(threadCalls);
/*  444 */       requiresThreadLocalCleanup = true;
/*      */     } 
/*      */ 
/*      */     
/*  448 */     FutureTypeAdapter<T> ongoingCall = (FutureTypeAdapter<T>)threadCalls.get(type);
/*  449 */     if (ongoingCall != null) {
/*  450 */       return ongoingCall;
/*      */     }
/*      */     
/*      */     try {
/*  454 */       FutureTypeAdapter<T> call = new FutureTypeAdapter<T>();
/*  455 */       threadCalls.put(type, call);
/*      */       
/*  457 */       for (TypeAdapterFactory factory : this.factories) {
/*  458 */         TypeAdapter<T> candidate = factory.create(this, type);
/*  459 */         if (candidate != null) {
/*  460 */           call.setDelegate(candidate);
/*  461 */           this.typeTokenCache.put(type, candidate);
/*  462 */           return candidate;
/*      */         } 
/*      */       } 
/*  465 */       throw new IllegalArgumentException("GSON (2.8.8) cannot handle " + type);
/*      */     } finally {
/*  467 */       threadCalls.remove(type);
/*      */       
/*  469 */       if (requiresThreadLocalCleanup) {
/*  470 */         this.calls.remove();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory skipPast, TypeToken<T> type) {
/*      */     JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory;
/*  528 */     if (!this.factories.contains(skipPast)) {
/*  529 */       jsonAdapterAnnotationTypeAdapterFactory = this.jsonAdapterFactory;
/*      */     }
/*      */     
/*  532 */     boolean skipPastFound = false;
/*  533 */     for (TypeAdapterFactory factory : this.factories) {
/*  534 */       if (!skipPastFound) {
/*  535 */         if (factory == jsonAdapterAnnotationTypeAdapterFactory) {
/*  536 */           skipPastFound = true;
/*      */         }
/*      */         
/*      */         continue;
/*      */       } 
/*  541 */       TypeAdapter<T> candidate = factory.create(this, type);
/*  542 */       if (candidate != null) {
/*  543 */         return candidate;
/*      */       }
/*      */     } 
/*  546 */     throw new IllegalArgumentException("GSON cannot serialize " + type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> TypeAdapter<T> getAdapter(Class<T> type) {
/*  556 */     return getAdapter(TypeToken.get(type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonElement toJsonTree(Object src) {
/*  573 */     if (src == null) {
/*  574 */       return JsonNull.INSTANCE;
/*      */     }
/*  576 */     return toJsonTree(src, src.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonElement toJsonTree(Object src, Type typeOfSrc) {
/*  596 */     JsonTreeWriter writer = new JsonTreeWriter();
/*  597 */     toJson(src, typeOfSrc, (JsonWriter)writer);
/*  598 */     return writer.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toJson(Object src) {
/*  615 */     if (src == null) {
/*  616 */       return toJson(JsonNull.INSTANCE);
/*      */     }
/*  618 */     return toJson(src, src.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toJson(Object src, Type typeOfSrc) {
/*  637 */     StringWriter writer = new StringWriter();
/*  638 */     toJson(src, typeOfSrc, writer);
/*  639 */     return writer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void toJson(Object src, Appendable writer) throws JsonIOException {
/*  657 */     if (src != null) {
/*  658 */       toJson(src, src.getClass(), writer);
/*      */     } else {
/*  660 */       toJson(JsonNull.INSTANCE, writer);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void toJson(Object src, Type typeOfSrc, Appendable writer) throws JsonIOException {
/*      */     try {
/*  682 */       JsonWriter jsonWriter = newJsonWriter(Streams.writerForAppendable(writer));
/*  683 */       toJson(src, typeOfSrc, jsonWriter);
/*  684 */     } catch (IOException e) {
/*  685 */       throw new JsonIOException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void toJson(Object src, Type typeOfSrc, JsonWriter writer) throws JsonIOException {
/*  696 */     TypeAdapter<?> adapter = getAdapter(TypeToken.get(typeOfSrc));
/*  697 */     boolean oldLenient = writer.isLenient();
/*  698 */     writer.setLenient(true);
/*  699 */     boolean oldHtmlSafe = writer.isHtmlSafe();
/*  700 */     writer.setHtmlSafe(this.htmlSafe);
/*  701 */     boolean oldSerializeNulls = writer.getSerializeNulls();
/*  702 */     writer.setSerializeNulls(this.serializeNulls);
/*      */     try {
/*  704 */       adapter.write(writer, src);
/*  705 */     } catch (IOException e) {
/*  706 */       throw new JsonIOException(e);
/*  707 */     } catch (AssertionError e) {
/*  708 */       AssertionError error = new AssertionError("AssertionError (GSON 2.8.8): " + e.getMessage());
/*  709 */       error.initCause(e);
/*  710 */       throw error;
/*      */     } finally {
/*  712 */       writer.setLenient(oldLenient);
/*  713 */       writer.setHtmlSafe(oldHtmlSafe);
/*  714 */       writer.setSerializeNulls(oldSerializeNulls);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toJson(JsonElement jsonElement) {
/*  726 */     StringWriter writer = new StringWriter();
/*  727 */     toJson(jsonElement, writer);
/*  728 */     return writer.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void toJson(JsonElement jsonElement, Appendable writer) throws JsonIOException {
/*      */     try {
/*  741 */       JsonWriter jsonWriter = newJsonWriter(Streams.writerForAppendable(writer));
/*  742 */       toJson(jsonElement, jsonWriter);
/*  743 */     } catch (IOException e) {
/*  744 */       throw new JsonIOException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonWriter newJsonWriter(Writer writer) throws IOException {
/*  752 */     if (this.generateNonExecutableJson) {
/*  753 */       writer.write(")]}'\n");
/*      */     }
/*  755 */     JsonWriter jsonWriter = new JsonWriter(writer);
/*  756 */     if (this.prettyPrinting) {
/*  757 */       jsonWriter.setIndent("  ");
/*      */     }
/*  759 */     jsonWriter.setSerializeNulls(this.serializeNulls);
/*  760 */     return jsonWriter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonReader newJsonReader(Reader reader) {
/*  767 */     JsonReader jsonReader = new JsonReader(reader);
/*  768 */     jsonReader.setLenient(this.lenient);
/*  769 */     return jsonReader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void toJson(JsonElement jsonElement, JsonWriter writer) throws JsonIOException {
/*  777 */     boolean oldLenient = writer.isLenient();
/*  778 */     writer.setLenient(true);
/*  779 */     boolean oldHtmlSafe = writer.isHtmlSafe();
/*  780 */     writer.setHtmlSafe(this.htmlSafe);
/*  781 */     boolean oldSerializeNulls = writer.getSerializeNulls();
/*  782 */     writer.setSerializeNulls(this.serializeNulls);
/*      */     try {
/*  784 */       Streams.write(jsonElement, writer);
/*  785 */     } catch (IOException e) {
/*  786 */       throw new JsonIOException(e);
/*  787 */     } catch (AssertionError e) {
/*  788 */       AssertionError error = new AssertionError("AssertionError (GSON 2.8.8): " + e.getMessage());
/*  789 */       error.initCause(e);
/*  790 */       throw error;
/*      */     } finally {
/*  792 */       writer.setLenient(oldLenient);
/*  793 */       writer.setHtmlSafe(oldHtmlSafe);
/*  794 */       writer.setSerializeNulls(oldSerializeNulls);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
/*  817 */     Object object = fromJson(json, classOfT);
/*  818 */     return Primitives.wrap(classOfT).cast(object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
/*  842 */     if (json == null) {
/*  843 */       return null;
/*      */     }
/*  845 */     StringReader reader = new StringReader(json);
/*  846 */     T target = fromJson(reader, typeOfT);
/*  847 */     return target;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
/*  869 */     JsonReader jsonReader = newJsonReader(json);
/*  870 */     Object object = fromJson(jsonReader, classOfT);
/*  871 */     assertFullConsumption(object, jsonReader);
/*  872 */     return Primitives.wrap(classOfT).cast(object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
/*  896 */     JsonReader jsonReader = newJsonReader(json);
/*  897 */     T object = fromJson(jsonReader, typeOfT);
/*  898 */     assertFullConsumption(object, jsonReader);
/*  899 */     return object;
/*      */   }
/*      */   
/*      */   private static void assertFullConsumption(Object obj, JsonReader reader) {
/*      */     try {
/*  904 */       if (obj != null && reader.peek() != JsonToken.END_DOCUMENT) {
/*  905 */         throw new JsonIOException("JSON document was not fully consumed.");
/*      */       }
/*  907 */     } catch (MalformedJsonException e) {
/*  908 */       throw new JsonSyntaxException(e);
/*  909 */     } catch (IOException e) {
/*  910 */       throw new JsonIOException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
/*  924 */     boolean isEmpty = true;
/*  925 */     boolean oldLenient = reader.isLenient();
/*  926 */     reader.setLenient(true);
/*      */     try {
/*  928 */       reader.peek();
/*  929 */       isEmpty = false;
/*  930 */       TypeToken<T> typeToken = TypeToken.get(typeOfT);
/*  931 */       TypeAdapter<T> typeAdapter = getAdapter(typeToken);
/*  932 */       T object = typeAdapter.read(reader);
/*  933 */       return object;
/*  934 */     } catch (EOFException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  939 */       if (isEmpty) {
/*  940 */         return null;
/*      */       }
/*  942 */       throw new JsonSyntaxException(e);
/*  943 */     } catch (IllegalStateException e) {
/*  944 */       throw new JsonSyntaxException(e);
/*  945 */     } catch (IOException e) {
/*      */       
/*  947 */       throw new JsonSyntaxException(e);
/*  948 */     } catch (AssertionError e) {
/*  949 */       AssertionError error = new AssertionError("AssertionError (GSON 2.8.8): " + e.getMessage());
/*  950 */       error.initCause(e);
/*  951 */       throw error;
/*      */     } finally {
/*  953 */       reader.setLenient(oldLenient);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
/*  975 */     Object object = fromJson(json, classOfT);
/*  976 */     return Primitives.wrap(classOfT).cast(object);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T fromJson(JsonElement json, Type typeOfT) throws JsonSyntaxException {
/* 1000 */     if (json == null) {
/* 1001 */       return null;
/*      */     }
/* 1003 */     return fromJson((JsonReader)new JsonTreeReader(json), typeOfT);
/*      */   }
/*      */   
/*      */   static class FutureTypeAdapter<T> extends TypeAdapter<T> {
/*      */     private TypeAdapter<T> delegate;
/*      */     
/*      */     public void setDelegate(TypeAdapter<T> typeAdapter) {
/* 1010 */       if (this.delegate != null) {
/* 1011 */         throw new AssertionError();
/*      */       }
/* 1013 */       this.delegate = typeAdapter;
/*      */     }
/*      */     
/*      */     public T read(JsonReader in) throws IOException {
/* 1017 */       if (this.delegate == null) {
/* 1018 */         throw new IllegalStateException();
/*      */       }
/* 1020 */       return this.delegate.read(in);
/*      */     }
/*      */     
/*      */     public void write(JsonWriter out, T value) throws IOException {
/* 1024 */       if (this.delegate == null) {
/* 1025 */         throw new IllegalStateException();
/*      */       }
/* 1027 */       this.delegate.write(out, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1033 */     return "{serializeNulls:" + this.serializeNulls + 
/* 1034 */       ",factories:" + 
/* 1035 */       this.factories + ",instanceCreators:" + 
/* 1036 */       this.constructorConstructor + "}";
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\Gson.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */