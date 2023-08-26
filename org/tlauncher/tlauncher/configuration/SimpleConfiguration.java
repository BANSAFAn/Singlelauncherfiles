/*     */ package org.tlauncher.tlauncher.configuration;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import org.tlauncher.tlauncher.entity.ConfigEnum;
/*     */ import org.tlauncher.util.StringUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class SimpleConfiguration implements AbstractConfiguration {
/*  20 */   protected final Properties properties = new Properties(); protected Object input; protected String comments;
/*     */   public SimpleConfiguration() {}
/*     */   
/*     */   public SimpleConfiguration(InputStream stream) throws IOException {
/*  24 */     this();
/*  25 */     loadFromStream(this.properties, stream);
/*     */     
/*  27 */     this.input = stream;
/*     */   }
/*     */   
/*     */   public SimpleConfiguration(File file) {
/*  31 */     this();
/*     */     
/*     */     try {
/*  34 */       loadFromFile(this.properties, file);
/*  35 */     } catch (FileNotFoundException e) {
/*  36 */       log(new Object[] { file + " file not exists" });
/*  37 */     } catch (Exception e) {
/*  38 */       log(new Object[] { "Error loading config from file:", e });
/*     */     } 
/*     */     
/*  41 */     this.input = file;
/*     */   }
/*     */   
/*     */   public SimpleConfiguration(URL url) throws IOException {
/*  45 */     this();
/*  46 */     loadFromURL(this.properties, url);
/*     */     
/*  48 */     this.input = url;
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(String key) {
/*  53 */     return getStringOf(this.properties.getProperty(key));
/*     */   }
/*     */   
/*     */   protected String getStringOf(Object obj) {
/*     */     String s;
/*  58 */     if (obj == null) {
/*  59 */       s = null;
/*     */     } else {
/*  61 */       s = obj.toString();
/*  62 */       if (s.isEmpty())
/*  63 */         s = null; 
/*     */     } 
/*  65 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(String key, Object value, boolean flush) {
/*  71 */     if (key == null) {
/*  72 */       throw new NullPointerException();
/*     */     }
/*  74 */     if (value == null) {
/*  75 */       this.properties.remove(key);
/*     */     } else {
/*  77 */       this.properties.setProperty(key, value.toString());
/*     */     } 
/*  79 */     if (flush && isSaveable()) {
/*  80 */       store();
/*     */     }
/*     */   }
/*     */   
/*     */   public void set(String key, Object value) {
/*  85 */     set(key, value, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(ConfigEnum key, Object value) {
/*  90 */     set(key.name(), value);
/*     */   }
/*     */   
/*     */   public void set(Map<String, Object> map, boolean flush) {
/*  94 */     for (Map.Entry<String, Object> en : map.entrySet()) {
/*  95 */       String key = en.getKey();
/*  96 */       Object value = en.getValue();
/*     */       
/*  98 */       if (value == null) {
/*  99 */         this.properties.remove(key); continue;
/*     */       } 
/* 101 */       this.properties.setProperty(key, value.toString());
/*     */     } 
/*     */     
/* 104 */     if (flush && isSaveable())
/* 105 */       store(); 
/*     */   }
/*     */   
/*     */   public void set(Map<String, Object> map) {
/* 109 */     set(map, false);
/*     */   }
/*     */   
/*     */   public Set<String> getKeys() {
/* 113 */     Set<String> set = new HashSet<>();
/*     */     
/* 115 */     for (Object obj : this.properties.keySet()) {
/* 116 */       set.add(getStringOf(obj));
/*     */     }
/* 118 */     return Collections.unmodifiableSet(set);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefault(String key) {
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   public int getInteger(String key, int def) {
/* 127 */     return getIntegerOf(get(key), 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInteger(String key) {
/* 132 */     return getInteger(key, 0);
/*     */   }
/*     */   
/*     */   protected int getIntegerOf(Object obj, int def) {
/*     */     try {
/* 137 */       return Integer.parseInt(obj.toString());
/* 138 */     } catch (Exception e) {
/* 139 */       return def;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDouble(String key) {
/* 145 */     return getDoubleOf(get(key), 0.0D);
/*     */   }
/*     */   
/*     */   protected double getDoubleOf(Object obj, double def) {
/*     */     try {
/* 150 */       return Double.parseDouble(obj.toString());
/* 151 */     } catch (Exception e) {
/* 152 */       return def;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloat(String key) {
/* 158 */     return getFloatOf(get(key), 0.0F);
/*     */   }
/*     */   
/*     */   protected float getFloatOf(Object obj, float def) {
/*     */     try {
/* 163 */       return Float.parseFloat(obj.toString());
/* 164 */     } catch (Exception e) {
/* 165 */       return def;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(String key) {
/* 171 */     return getLongOf(get(key), 0L);
/*     */   }
/*     */   
/*     */   protected long getLongOf(Object obj, long def) {
/*     */     try {
/* 176 */       return Long.parseLong(obj.toString());
/* 177 */     } catch (Exception e) {
/* 178 */       return def;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String key) {
/* 184 */     return getBooleanOf(get(key), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(ConfigEnum key) {
/* 189 */     return getBoolean(key.name());
/*     */   }
/*     */   
/*     */   protected boolean getBooleanOf(Object obj, boolean def) {
/*     */     try {
/* 194 */       return StringUtil.parseBoolean(obj.toString());
/* 195 */     } catch (Exception e) {
/* 196 */       return def;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultInteger(String key) {
/* 202 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDefaultDouble(String key) {
/* 207 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDefaultFloat(String key) {
/* 212 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDefaultLong(String key) {
/* 217 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getDefaultBoolean(String key) {
/* 222 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/* 227 */     if (!isSaveable()) {
/* 228 */       throw new UnsupportedOperationException();
/*     */     }
/* 230 */     File file = (File)this.input;
/* 231 */     this.properties.store(new FileOutputStream(file), this.comments);
/*     */   }
/*     */   
/*     */   public void store() {
/*     */     try {
/* 236 */       save();
/* 237 */     } catch (IOException e) {
/* 238 */       log(new Object[] { "Cannot store values!", e });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 244 */     this.properties.clear();
/*     */   }
/*     */   
/*     */   public boolean isSaveable() {
/* 248 */     return this.input instanceof File;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void loadFromStream(Properties properties, InputStream stream) throws IOException {
/* 253 */     if (stream == null) {
/* 254 */       throw new NullPointerException();
/*     */     }
/*     */     
/* 257 */     Reader reader = new InputStreamReader(new BufferedInputStream(stream), Charset.forName("UTF-8"));
/* 258 */     properties.clear();
/* 259 */     properties.load(reader);
/* 260 */     reader.close();
/*     */   }
/*     */   
/*     */   static Properties loadFromStream(InputStream stream) throws IOException {
/* 264 */     Properties properties = new Properties();
/* 265 */     loadFromStream(properties, stream);
/*     */     
/* 267 */     return properties;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void loadFromFile(Properties properties, File file) throws IOException {
/* 272 */     if (file == null) {
/* 273 */       throw new NullPointerException();
/*     */     }
/* 275 */     FileInputStream stream = new FileInputStream(file);
/* 276 */     loadFromStream(properties, stream);
/*     */   }
/*     */   
/*     */   protected static Properties loadFromFile(File file) throws IOException {
/* 280 */     Properties properties = new Properties();
/* 281 */     loadFromFile(properties, file);
/*     */     
/* 283 */     return properties;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void loadFromURL(Properties properties, URL url) throws IOException {
/* 288 */     if (url == null) {
/* 289 */       throw new NullPointerException();
/*     */     }
/* 291 */     InputStream connection = url.openStream();
/* 292 */     loadFromStream(properties, connection);
/*     */   }
/*     */   
/*     */   protected static Properties loadFromURL(URL url) throws IOException {
/* 296 */     Properties properties = new Properties();
/* 297 */     loadFromURL(properties, url);
/*     */     
/* 299 */     return properties;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void copyProperties(Properties src, Properties dest, boolean wipe) {
/* 304 */     if (src == null) {
/* 305 */       throw new NullPointerException("src is NULL");
/*     */     }
/* 307 */     if (dest == null) {
/* 308 */       throw new NullPointerException("dest is NULL");
/*     */     }
/* 310 */     if (wipe) {
/* 311 */       dest.clear();
/*     */     }
/* 313 */     for (Map.Entry<Object, Object> en : src.entrySet()) {
/* 314 */       String key = (en.getKey() == null) ? null : en.getKey().toString();
/* 315 */       String value = (en.getKey() == null) ? null : en.getValue().toString();
/* 316 */       dest.setProperty(key, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static Properties copyProperties(Properties src) {
/* 321 */     Properties properties = new Properties();
/* 322 */     copyProperties(src, properties, false);
/*     */     
/* 324 */     return properties;
/*     */   }
/*     */   
/*     */   void log(Object... o) {
/* 328 */     U.log(new Object[] { "[" + getClass().getSimpleName() + "]", o });
/*     */   }
/*     */   
/*     */   public boolean isUSSRLocale() {
/* 332 */     String locale = get("locale");
/* 333 */     return ("ru_RU".equals(locale) || "uk_UA".equals(locale));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\SimpleConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */