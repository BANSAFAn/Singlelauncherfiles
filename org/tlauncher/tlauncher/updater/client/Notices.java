/*     */ package org.tlauncher.tlauncher.updater.client;
/*     */ 
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonParseException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Notices
/*     */ {
/*  30 */   private final Map<String, NoticeList> map = new HashMap<>(), unmodifiable = Collections.unmodifiableMap(this.map);
/*     */   
/*     */   public final Map<String, NoticeList> getMap() {
/*  33 */     return this.unmodifiable;
/*     */   }
/*     */   
/*     */   protected final Map<String, NoticeList> map() {
/*  37 */     return this.map;
/*     */   }
/*     */   
/*     */   public final NoticeList getByName(String name) {
/*  41 */     return this.map.get(name);
/*     */   }
/*     */   
/*     */   protected void add(NoticeList list) {
/*  45 */     if (list == null) {
/*  46 */       throw new NullPointerException("list");
/*     */     }
/*  48 */     this.map.put(list.name, list);
/*     */   }
/*     */   
/*     */   protected void add(String listName, Notice notice) {
/*  52 */     if (notice == null) {
/*  53 */       throw new NullPointerException("notice");
/*     */     }
/*  55 */     NoticeList list = this.map.get(listName);
/*  56 */     boolean add = (list == null);
/*     */     
/*  58 */     if (add) {
/*  59 */       list = new NoticeList(listName);
/*     */     }
/*  61 */     list.add(notice);
/*     */     
/*  63 */     if (add) {
/*  64 */       add(list);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/*  69 */     return getClass().getSimpleName() + this.map;
/*     */   }
/*     */   
/*     */   public static class NoticeList
/*     */   {
/*     */     private final String name;
/*  75 */     private final List<Notices.Notice> list = new ArrayList<>(); private final List<Notices.Notice> unmodifiable = Collections.unmodifiableList(this.list);
/*     */     
/*  77 */     private final Notices.Notice[] chances = new Notices.Notice[100];
/*  78 */     private int totalChance = 0;
/*     */     
/*     */     public NoticeList(String name) {
/*  81 */       if (name == null) {
/*  82 */         throw new NullPointerException("name");
/*     */       }
/*  84 */       if (name.isEmpty()) {
/*  85 */         throw new IllegalArgumentException("name is empty");
/*     */       }
/*  87 */       this.name = name;
/*     */     }
/*     */     
/*     */     public final String getName() {
/*  91 */       return this.name;
/*     */     }
/*     */     
/*     */     public final List<Notices.Notice> getList() {
/*  95 */       return this.unmodifiable;
/*     */     }
/*     */     
/*     */     protected final List<Notices.Notice> list() {
/*  99 */       return this.list;
/*     */     }
/*     */     
/*     */     public final Notices.Notice getRandom() {
/* 103 */       return this.chances[(new Random()).nextInt(100)];
/*     */     }
/*     */     
/*     */     protected void add(Notices.Notice notice) {
/* 107 */       if (notice == null) {
/* 108 */         throw new NullPointerException();
/*     */       }
/* 110 */       if (this.totalChance + notice.chance > 100) {
/* 111 */         throw new IllegalArgumentException("chance overflow: " + (this.totalChance + notice.chance));
/*     */       }
/* 113 */       this.list.add(notice);
/*     */       
/* 115 */       Arrays.fill((Object[])this.chances, this.totalChance, this.totalChance + notice.chance, notice);
/* 116 */       this.totalChance += notice.chance;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 121 */       return getClass().getSimpleName() + list();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Notice
/*     */   {
/*     */     private String content;
/* 128 */     private int chance = 100;
/* 129 */     private Notices.NoticeType type = Notices.NoticeType.NOTICE;
/* 130 */     private int[] size = new int[2];
/*     */     private String image;
/*     */     
/*     */     public final int getChance() {
/* 134 */       return this.chance;
/*     */     }
/*     */     
/*     */     public final void setChance(int chance) {
/* 138 */       if (chance < 1 || chance > 100)
/* 139 */         throw new IllegalArgumentException("illegal chance: " + chance); 
/* 140 */       this.chance = chance;
/*     */     }
/*     */     
/*     */     public final String getContent() {
/* 144 */       return this.content;
/*     */     }
/*     */     
/*     */     public final void setContent(String content) {
/* 148 */       if (StringUtils.isBlank(content))
/* 149 */         throw new IllegalArgumentException("content is empty or is null"); 
/* 150 */       this.content = content;
/*     */     }
/*     */     
/*     */     public final Notices.NoticeType getType() {
/* 154 */       return this.type;
/*     */     }
/*     */     
/*     */     public final void setType(Notices.NoticeType type) {
/* 158 */       this.type = type;
/*     */     }
/*     */     
/*     */     public final int[] getSize() {
/* 162 */       return (int[])this.size.clone();
/*     */     }
/*     */     
/*     */     public final void setSize(int[] size) {
/* 166 */       if (size == null) {
/* 167 */         throw new NullPointerException();
/*     */       }
/* 169 */       if (size.length != 2) {
/* 170 */         throw new IllegalArgumentException("illegal length");
/*     */       }
/* 172 */       setWidth(size[0]);
/* 173 */       setHeight(size[1]);
/*     */     }
/*     */     
/*     */     public final int getWidth() {
/* 177 */       return this.size[0];
/*     */     }
/*     */     
/*     */     public final void setWidth(int width) {
/* 181 */       if (width < 1)
/* 182 */         throw new IllegalArgumentException("width must be greater than 0"); 
/* 183 */       this.size[0] = width;
/*     */     }
/*     */     
/*     */     public final int getHeight() {
/* 187 */       return this.size[1];
/*     */     }
/*     */     
/*     */     public final void setHeight(int height) {
/* 191 */       if (height < 1)
/* 192 */         throw new IllegalArgumentException("height must be greater than 0"); 
/* 193 */       this.size[1] = height;
/*     */     }
/*     */     
/*     */     public final String getImage() {
/* 197 */       return this.image;
/*     */     }
/*     */     
/*     */     public final void setImage(String image) {
/* 201 */       this.image = StringUtils.isBlank(image) ? null : Notices.parseImage(image);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 206 */       StringBuilder builder = new StringBuilder();
/*     */       
/* 208 */       builder
/* 209 */         .append(getClass().getSimpleName())
/* 210 */         .append("{")
/* 211 */         .append("size=").append(this.size[0]).append('x').append(this.size[1]).append(';')
/* 212 */         .append("chance=").append(this.chance).append(';')
/* 213 */         .append("content=\"");
/*     */       
/* 215 */       if (this.content.length() < 50) {
/* 216 */         builder.append(this.content);
/*     */       } else {
/* 218 */         builder
/* 219 */           .append(this.content.substring(0, 46))
/* 220 */           .append("...");
/*     */       } 
/* 222 */       builder
/* 223 */         .append("\";")
/* 224 */         .append("image=");
/*     */       
/* 226 */       if (this.image != null && this.image.length() > 24) {
/* 227 */         builder
/* 228 */           .append(this.image.substring(0, 22))
/* 229 */           .append("...");
/*     */       } else {
/* 231 */         builder.append(this.image);
/*     */       } 
/* 233 */       builder.append('}');
/*     */       
/* 235 */       return builder.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public enum NoticeType {
/* 240 */     NOTICE(false), WARNING(false), AD_SERVER, AD_YOUTUBE, AD_OTHER;
/*     */     
/*     */     private final boolean advert;
/*     */     
/*     */     NoticeType(boolean advert) {
/* 245 */       this.advert = advert;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isAdvert() {
/* 253 */       return this.advert;
/*     */     }
/*     */   }
/*     */   
/*     */   private static String parseImage(String image) {
/* 258 */     if (image == null) {
/* 259 */       return null;
/*     */     }
/* 261 */     if (image.startsWith("data:image")) {
/* 262 */       return image;
/*     */     }
/* 264 */     URL url = ImageCache.getRes(image);
/* 265 */     return (url == null) ? null : url.toString();
/*     */   }
/*     */   
/*     */   public static class Deserializer
/*     */     implements JsonDeserializer<Notices> {
/*     */     public Notices deserialize(JsonElement root, Type type, JsonDeserializationContext context) throws JsonParseException {
/*     */       try {
/* 272 */         return deserialize0(root);
/* 273 */       } catch (Exception e) {
/* 274 */         U.log(new Object[] { "Cannot parse notices:", e });
/*     */         
/* 276 */         return new Notices();
/*     */       } 
/*     */     }
/*     */     
/*     */     private Notices deserialize0(JsonElement root) throws JsonParseException {
/*     */       // Byte code:
/*     */       //   0: new org/tlauncher/tlauncher/updater/client/Notices
/*     */       //   3: dup
/*     */       //   4: invokespecial <init> : ()V
/*     */       //   7: astore_2
/*     */       //   8: aload_1
/*     */       //   9: invokevirtual getAsJsonObject : ()Lcom/google/gson/JsonObject;
/*     */       //   12: astore_3
/*     */       //   13: aload_3
/*     */       //   14: invokevirtual entrySet : ()Ljava/util/Set;
/*     */       //   17: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   22: astore #4
/*     */       //   24: aload #4
/*     */       //   26: invokeinterface hasNext : ()Z
/*     */       //   31: ifeq -> 299
/*     */       //   34: aload #4
/*     */       //   36: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   41: checkcast java/util/Map$Entry
/*     */       //   44: astore #5
/*     */       //   46: aload #5
/*     */       //   48: invokeinterface getKey : ()Ljava/lang/Object;
/*     */       //   53: checkcast java/lang/String
/*     */       //   56: astore #6
/*     */       //   58: aload #5
/*     */       //   60: invokeinterface getValue : ()Ljava/lang/Object;
/*     */       //   65: checkcast com/google/gson/JsonElement
/*     */       //   68: invokevirtual getAsJsonArray : ()Lcom/google/gson/JsonArray;
/*     */       //   71: astore #7
/*     */       //   73: aload #7
/*     */       //   75: invokevirtual iterator : ()Ljava/util/Iterator;
/*     */       //   78: astore #8
/*     */       //   80: aload #8
/*     */       //   82: invokeinterface hasNext : ()Z
/*     */       //   87: ifeq -> 296
/*     */       //   90: aload #8
/*     */       //   92: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   97: checkcast com/google/gson/JsonElement
/*     */       //   100: astore #9
/*     */       //   102: aload #9
/*     */       //   104: invokevirtual getAsJsonObject : ()Lcom/google/gson/JsonObject;
/*     */       //   107: astore #10
/*     */       //   109: aload #10
/*     */       //   111: ldc 'version'
/*     */       //   113: invokevirtual has : (Ljava/lang/String;)Z
/*     */       //   116: ifeq -> 155
/*     */       //   119: aload #10
/*     */       //   121: ldc 'version'
/*     */       //   123: invokevirtual get : (Ljava/lang/String;)Lcom/google/gson/JsonElement;
/*     */       //   126: invokevirtual getAsString : ()Ljava/lang/String;
/*     */       //   129: astore #11
/*     */       //   131: aload #11
/*     */       //   133: invokestatic compile : (Ljava/lang/String;)Ljava/util/regex/Pattern;
/*     */       //   136: astore #12
/*     */       //   138: aload #12
/*     */       //   140: invokestatic getVersion : ()D
/*     */       //   143: invokestatic valueOf : (D)Ljava/lang/String;
/*     */       //   146: invokevirtual matcher : (Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
/*     */       //   149: invokevirtual matches : ()Z
/*     */       //   152: ifeq -> 80
/*     */       //   155: new org/tlauncher/tlauncher/updater/client/Notices$Notice
/*     */       //   158: dup
/*     */       //   159: invokespecial <init> : ()V
/*     */       //   162: astore #11
/*     */       //   164: aload #11
/*     */       //   166: aload #10
/*     */       //   168: ldc 'content'
/*     */       //   170: invokevirtual get : (Ljava/lang/String;)Lcom/google/gson/JsonElement;
/*     */       //   173: invokevirtual getAsString : ()Ljava/lang/String;
/*     */       //   176: invokevirtual setContent : (Ljava/lang/String;)V
/*     */       //   179: aload #11
/*     */       //   181: aload #10
/*     */       //   183: ldc 'size'
/*     */       //   185: invokevirtual get : (Ljava/lang/String;)Lcom/google/gson/JsonElement;
/*     */       //   188: invokevirtual getAsString : ()Ljava/lang/String;
/*     */       //   191: bipush #120
/*     */       //   193: invokestatic parseIntegerArray : (Ljava/lang/String;C)Lorg/tlauncher/util/IntegerArray;
/*     */       //   196: invokevirtual toArray : ()[I
/*     */       //   199: invokevirtual setSize : ([I)V
/*     */       //   202: aload #10
/*     */       //   204: ldc 'chance'
/*     */       //   206: invokevirtual has : (Ljava/lang/String;)Z
/*     */       //   209: ifeq -> 227
/*     */       //   212: aload #11
/*     */       //   214: aload #10
/*     */       //   216: ldc 'chance'
/*     */       //   218: invokevirtual get : (Ljava/lang/String;)Lcom/google/gson/JsonElement;
/*     */       //   221: invokevirtual getAsInt : ()I
/*     */       //   224: invokevirtual setChance : (I)V
/*     */       //   227: aload #10
/*     */       //   229: ldc 'type'
/*     */       //   231: invokevirtual has : (Ljava/lang/String;)Z
/*     */       //   234: ifeq -> 260
/*     */       //   237: aload #11
/*     */       //   239: ldc org/tlauncher/tlauncher/updater/client/Notices$NoticeType
/*     */       //   241: aload #10
/*     */       //   243: ldc 'type'
/*     */       //   245: invokevirtual get : (Ljava/lang/String;)Lcom/google/gson/JsonElement;
/*     */       //   248: invokevirtual getAsString : ()Ljava/lang/String;
/*     */       //   251: invokestatic parseEnum : (Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
/*     */       //   254: checkcast org/tlauncher/tlauncher/updater/client/Notices$NoticeType
/*     */       //   257: invokevirtual setType : (Lorg/tlauncher/tlauncher/updater/client/Notices$NoticeType;)V
/*     */       //   260: aload #10
/*     */       //   262: ldc 'image'
/*     */       //   264: invokevirtual has : (Ljava/lang/String;)Z
/*     */       //   267: ifeq -> 285
/*     */       //   270: aload #11
/*     */       //   272: aload #10
/*     */       //   274: ldc 'image'
/*     */       //   276: invokevirtual get : (Ljava/lang/String;)Lcom/google/gson/JsonElement;
/*     */       //   279: invokevirtual getAsString : ()Ljava/lang/String;
/*     */       //   282: invokevirtual setImage : (Ljava/lang/String;)V
/*     */       //   285: aload_2
/*     */       //   286: aload #6
/*     */       //   288: aload #11
/*     */       //   290: invokevirtual add : (Ljava/lang/String;Lorg/tlauncher/tlauncher/updater/client/Notices$Notice;)V
/*     */       //   293: goto -> 80
/*     */       //   296: goto -> 24
/*     */       //   299: aload_2
/*     */       //   300: ldc 'uk_UA'
/*     */       //   302: invokevirtual getByName : (Ljava/lang/String;)Lorg/tlauncher/tlauncher/updater/client/Notices$NoticeList;
/*     */       //   305: ifnonnull -> 366
/*     */       //   308: aload_2
/*     */       //   309: ldc 'ru_RU'
/*     */       //   311: invokevirtual getByName : (Ljava/lang/String;)Lorg/tlauncher/tlauncher/updater/client/Notices$NoticeList;
/*     */       //   314: ifnull -> 366
/*     */       //   317: aload_2
/*     */       //   318: ldc 'ru_RU'
/*     */       //   320: invokevirtual getByName : (Ljava/lang/String;)Lorg/tlauncher/tlauncher/updater/client/Notices$NoticeList;
/*     */       //   323: invokevirtual getList : ()Ljava/util/List;
/*     */       //   326: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   331: astore #4
/*     */       //   333: aload #4
/*     */       //   335: invokeinterface hasNext : ()Z
/*     */       //   340: ifeq -> 366
/*     */       //   343: aload #4
/*     */       //   345: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   350: checkcast org/tlauncher/tlauncher/updater/client/Notices$Notice
/*     */       //   353: astore #5
/*     */       //   355: aload_2
/*     */       //   356: ldc 'uk_UA'
/*     */       //   358: aload #5
/*     */       //   360: invokevirtual add : (Ljava/lang/String;Lorg/tlauncher/tlauncher/updater/client/Notices$Notice;)V
/*     */       //   363: goto -> 333
/*     */       //   366: aload_2
/*     */       //   367: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #280	-> 0
/*     */       //   #282	-> 8
/*     */       //   #284	-> 13
/*     */       //   #285	-> 46
/*     */       //   #286	-> 58
/*     */       //   #288	-> 73
/*     */       //   #289	-> 102
/*     */       //   #291	-> 109
/*     */       //   #292	-> 119
/*     */       //   #293	-> 131
/*     */       //   #295	-> 138
/*     */       //   #302	-> 155
/*     */       //   #304	-> 164
/*     */       //   #305	-> 179
/*     */       //   #306	-> 185
/*     */       //   #307	-> 196
/*     */       //   #305	-> 199
/*     */       //   #310	-> 202
/*     */       //   #311	-> 212
/*     */       //   #313	-> 227
/*     */       //   #314	-> 237
/*     */       //   #316	-> 260
/*     */       //   #317	-> 270
/*     */       //   #319	-> 285
/*     */       //   #320	-> 293
/*     */       //   #321	-> 296
/*     */       //   #323	-> 299
/*     */       //   #324	-> 317
/*     */       //   #325	-> 355
/*     */       //   #328	-> 366
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   131	24	11	version	Ljava/lang/String;
/*     */       //   138	17	12	pattern	Ljava/util/regex/Pattern;
/*     */       //   109	184	10	ntObj	Lcom/google/gson/JsonObject;
/*     */       //   164	129	11	notice	Lorg/tlauncher/tlauncher/updater/client/Notices$Notice;
/*     */       //   102	191	9	elem	Lcom/google/gson/JsonElement;
/*     */       //   58	238	6	listName	Ljava/lang/String;
/*     */       //   73	223	7	ntArray	Lcom/google/gson/JsonArray;
/*     */       //   46	250	5	entry	Ljava/util/Map$Entry;
/*     */       //   355	8	5	notice	Lorg/tlauncher/tlauncher/updater/client/Notices$Notice;
/*     */       //   0	368	0	this	Lorg/tlauncher/tlauncher/updater/client/Notices$Deserializer;
/*     */       //   0	368	1	root	Lcom/google/gson/JsonElement;
/*     */       //   8	360	2	notices	Lorg/tlauncher/tlauncher/updater/client/Notices;
/*     */       //   13	355	3	rootObject	Lcom/google/gson/JsonObject;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   46	250	5	entry	Ljava/util/Map$Entry<Ljava/lang/String;Lcom/google/gson/JsonElement;>;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\client\Notices.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */