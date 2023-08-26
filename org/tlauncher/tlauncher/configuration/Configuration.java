/*     */ package org.tlauncher.tlauncher.configuration;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.UUID;
/*     */ import joptsimple.OptionSet;
/*     */ import net.minecraft.launcher.updater.VersionFilter;
/*     */ import net.minecraft.launcher.versions.ReleaseType;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
/*     */ import org.tlauncher.tlauncher.entity.ConfigEnum;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.Direction;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.IntegerArray;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.Reflect;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Configuration
/*     */   extends SimpleConfiguration
/*     */ {
/*     */   private ConfigurationDefaults defaults;
/*     */   private Map<String, Object> constants;
/*     */   private List<Locale> defaultLocales;
/*     */   private List<Locale> supportedLocales;
/*     */   private boolean firstRun;
/*     */   
/*     */   private Configuration(URL url, OptionSet set) throws IOException {
/*  45 */     super(url);
/*  46 */     init(set);
/*     */   }
/*     */   
/*     */   private Configuration(File file, OptionSet set) {
/*  50 */     super(file);
/*  51 */     init(set);
/*     */   }
/*     */   public static Configuration createConfiguration(OptionSet set) throws IOException {
/*     */     File file;
/*  55 */     Object path = (set != null) ? set.valueOf("settings") : null;
/*     */ 
/*     */     
/*  58 */     InnerConfiguration inner = TLauncher.getInnerSettings();
/*     */     
/*  60 */     if (path == null) {
/*  61 */       file = MinecraftUtil.getSystemRelatedDirectory(inner.get("settings.new"));
/*     */     } else {
/*  63 */       file = new File(path.toString());
/*     */     } 
/*     */     
/*  66 */     boolean doesntExist = !file.isFile();
/*     */     
/*  68 */     if (doesntExist) {
/*  69 */       U.log(new Object[] { "Creating configuration file..." });
/*  70 */       FileUtil.createFile(file);
/*     */     } 
/*     */     
/*  73 */     U.log(new Object[] { "Loading configuration from file:", file });
/*     */     
/*  75 */     Configuration config = new Configuration(file, set);
/*     */     
/*  77 */     config.firstRun = doesntExist;
/*     */     
/*  79 */     return config;
/*     */   }
/*     */   
/*     */   public static Configuration createConfiguration() throws IOException {
/*  83 */     return createConfiguration((OptionSet)null);
/*     */   }
/*     */   
/*     */   public static List<Locale> getDefaultLocales(SimpleConfiguration simpleConfiguration) {
/*  87 */     List<Locale> l = new ArrayList<>();
/*  88 */     String[] languages = simpleConfiguration.get("languages").split(",");
/*  89 */     for (String string : languages) {
/*  90 */       l.add(getLocaleOf(string));
/*     */     }
/*  92 */     return l;
/*     */   }
/*     */   
/*     */   public boolean isFirstRun() {
/*  96 */     return this.firstRun;
/*     */   }
/*     */   
/*     */   public boolean isSaveable(String key) {
/* 100 */     return !this.constants.containsKey(key);
/*     */   }
/*     */   
/*     */   public Locale getLocale() {
/* 104 */     return getLocaleOf(get("locale"));
/*     */   }
/*     */   
/*     */   public Locale[] getLocales() {
/* 108 */     Locale[] locales = new Locale[this.supportedLocales.size()];
/* 109 */     return this.supportedLocales.<Locale>toArray(locales);
/*     */   }
/*     */   
/*     */   public ActionOnLaunch getActionOnLaunch() {
/* 113 */     return ActionOnLaunch.get(get("minecraft.onlaunch"));
/*     */   }
/*     */   
/*     */   public ConsoleType getConsoleType() {
/* 117 */     return ConsoleType.get(get("gui.console"));
/*     */   }
/*     */   
/*     */   public ConnectionQuality getConnectionQuality() {
/* 121 */     return ConnectionQuality.get(get("connection"));
/*     */   }
/*     */   
/*     */   public int[] getClientWindowSize() {
/* 125 */     String plainValue = get("minecraft.size");
/* 126 */     int[] value = new int[2];
/*     */     
/* 128 */     if (plainValue == null) {
/* 129 */       return new int[2];
/*     */     }
/*     */     try {
/* 132 */       IntegerArray arr = IntegerArray.parseIntegerArray(plainValue);
/* 133 */       value[0] = arr.get(0);
/* 134 */       value[1] = arr.get(1);
/* 135 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 138 */     return value;
/*     */   }
/*     */   
/*     */   public int[] getLauncherWindowSize() {
/* 142 */     String plainValue = get("gui.size");
/* 143 */     int[] value = new int[2];
/*     */     
/* 145 */     if (plainValue == null) {
/* 146 */       return new int[2];
/*     */     }
/*     */     try {
/* 149 */       IntegerArray arr = IntegerArray.parseIntegerArray(plainValue);
/* 150 */       value[0] = arr.get(0);
/* 151 */       value[1] = arr.get(1);
/* 152 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/* 155 */     return value;
/*     */   }
/*     */   
/*     */   public int[] getDefaultClientWindowSize() {
/* 159 */     String plainValue = getDefault("minecraft.size");
/* 160 */     return IntegerArray.parseIntegerArray(plainValue).toArray();
/*     */   }
/*     */   
/*     */   public int[] getDefaultLauncherWindowSize() {
/* 164 */     String plainValue = getDefault("gui.size");
/* 165 */     return IntegerArray.parseIntegerArray(plainValue).toArray();
/*     */   }
/*     */   
/*     */   public VersionFilter getVersionFilter() {
/* 169 */     VersionFilter filter = new VersionFilter();
/*     */     
/* 171 */     for (ReleaseType type : ReleaseType.getDefinable()) {
/* 172 */       boolean include = getBoolean("minecraft.versions." + type);
/*     */       
/* 174 */       if (!include) {
/* 175 */         filter.exclude(new ReleaseType[] { type });
/*     */       }
/*     */     } 
/* 178 */     for (ReleaseType.SubType type : ReleaseType.SubType.values()) {
/* 179 */       boolean include = getBoolean("minecraft.versions.sub." + type);
/*     */       
/* 181 */       if (!include) {
/* 182 */         filter.exclude(new ReleaseType.SubType[] { type });
/*     */       }
/*     */     } 
/* 185 */     return filter;
/*     */   }
/*     */   
/*     */   public Direction getDirection(String key) {
/* 189 */     return (Direction)Reflect.parseEnum(Direction.class, get(key));
/*     */   }
/*     */   
/*     */   public Proxy getProxy() {
/* 193 */     return Proxy.NO_PROXY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UUID getClient() {
/*     */     try {
/* 204 */       return UUID.fromString(get("client"));
/* 205 */     } catch (Exception e) {
/* 206 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefault(String key) {
/* 212 */     return getStringOf(this.defaults.get(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultInteger(String key) {
/* 217 */     return getIntegerOf(this.defaults.get(key), 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDefaultDouble(String key) {
/* 222 */     return getDoubleOf(this.defaults.get(key), 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDefaultFloat(String key) {
/* 227 */     return getFloatOf(this.defaults.get(key), 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDefaultLong(String key) {
/* 232 */     return getLongOf(this.defaults.get(key), 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getDefaultBoolean(String key) {
/* 237 */     return getBooleanOf(this.defaults.get(key), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(String key, Object value, boolean flush) {
/* 242 */     if (this.constants.containsKey(key)) {
/*     */       return;
/*     */     }
/* 245 */     super.set(key, value, flush);
/*     */   }
/*     */   
/*     */   public void setForcefully(String key, Object value, boolean flush) {
/* 249 */     super.set(key, value, flush);
/*     */   }
/*     */   
/*     */   public void setForcefully(String key, Object value) {
/* 253 */     setForcefully(key, value, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/* 258 */     if (!isSaveable()) {
/* 259 */       throw new UnsupportedOperationException();
/*     */     }
/* 261 */     Properties temp = copyProperties(this.properties);
/* 262 */     for (String key : this.constants.keySet()) {
/* 263 */       temp.remove(key);
/*     */     }
/* 265 */     File file = (File)this.input;
/* 266 */     temp.store(new FileOutputStream(file), this.comments);
/*     */   }
/*     */   
/*     */   public File getFile() {
/* 270 */     if (!isSaveable())
/* 271 */       return null; 
/* 272 */     return (File)this.input;
/*     */   }
/*     */ 
/*     */   
/*     */   private void init(OptionSet set) {
/* 277 */     this.comments = " TLauncher configuration file\n Version " + TLauncher.getVersion();
/* 278 */     InnerConfiguration inner = TLauncher.getInnerSettings();
/* 279 */     this.defaults = new ConfigurationDefaults(inner);
/* 280 */     this.constants = ArgumentParser.parse(set);
/*     */     
/* 282 */     set(this.constants, false);
/*     */     
/* 284 */     log(new Object[] { "Constant values:", this.constants });
/*     */     
/* 286 */     int version = ConfigurationDefaults.getVersion();
/*     */     
/* 288 */     if (getDouble("settings.version") != version) {
/* 289 */       clear();
/*     */     }
/* 291 */     set("settings.version", Integer.valueOf(version), false);
/*     */ 
/*     */     
/* 294 */     for (Map.Entry<String, Object> curen : this.defaults.getMap().entrySet()) {
/* 295 */       String key = curen.getKey();
/*     */       
/* 297 */       if (this.constants.containsKey(key)) {
/* 298 */         log(new Object[] { "Key \"" + key + "\" is unsaveable!" });
/*     */         
/*     */         continue;
/*     */       } 
/* 302 */       String value = get(key);
/* 303 */       Object defvalue = curen.getValue();
/*     */       
/* 305 */       if (defvalue != null) {
/*     */         try {
/* 307 */           PlainParser.parse(value, defvalue);
/* 308 */         } catch (Exception e) {
/* 309 */           log(new Object[] { "Key \"" + key + "\" is invalid!" });
/* 310 */           set(key, defvalue, false);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 315 */     this.defaultLocales = getDefaultLocales(inner);
/* 316 */     this.supportedLocales = this.defaultLocales;
/* 317 */     Locale selected = getLocaleOf(get("locale"));
/*     */     
/* 319 */     if (selected == null) {
/* 320 */       log(new Object[] { "Selected locale is not supported, rolling back to default one" });
/*     */       
/* 322 */       selected = Locale.getDefault();
/*     */       
/* 324 */       if (selected == getLocaleOf("uk_UA")) {
/* 325 */         selected = getLocaleOf("ru_RU");
/*     */       }
/*     */     } 
/* 328 */     selected = findSuitableLocale(selected, this.supportedLocales);
/*     */     
/* 330 */     set("locale", selected, false);
/*     */     
/* 332 */     if (get("chooser.type.account") == null)
/* 333 */       set("chooser.type.account", Boolean.valueOf(false), false); 
/* 334 */     if (get("skin.status.checkbox.state") == null) {
/* 335 */       set("skin.status.checkbox.state", Boolean.valueOf(true), false);
/*     */     }
/* 337 */     if (getInteger("minecraft.memory.ram2") > OS.Arch.MAX_MEMORY) {
/* 338 */       U.log(new Object[] { String.format("decreased memory from  %s to %s", new Object[] { Integer.valueOf(getInteger("minecraft.memory.ram2")), 
/* 339 */                 Integer.valueOf(OS.Arch.MAX_MEMORY) }) });
/* 340 */       set("minecraft.memory.ram2", Integer.valueOf(OS.Arch.MAX_MEMORY));
/*     */     } 
/* 342 */     if (getInteger("minecraft.memory.ram2") < 512) {
/* 343 */       set("minecraft.memory.ram2", Integer.valueOf(512));
/*     */     }
/* 345 */     set(ConfigEnum.UPDATER_LAUNCHER, Boolean.valueOf(false));
/*     */     
/* 347 */     if (isSaveable()) {
/*     */       try {
/* 349 */         save();
/* 350 */       } catch (IOException e) {
/* 351 */         log(new Object[] { "Cannot save value!", e });
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static Locale getLocaleOf(String locale) {
/* 357 */     if (locale == null) {
/* 358 */       return null;
/*     */     }
/* 360 */     for (Locale cur : Locale.getAvailableLocales()) {
/* 361 */       if (cur.toString().equals(locale))
/* 362 */         return cur; 
/*     */     } 
/* 364 */     return null;
/*     */   }
/*     */   
/*     */   public static Locale findSuitableLocale(Locale selected, List<Locale> supportedLocales) {
/* 368 */     for (Locale l : supportedLocales) {
/* 369 */       if (l.getLanguage().equals(selected.getLanguage()))
/* 370 */         return l; 
/* 371 */     }  U.log(new Object[] { "Default locale is not supported, rolling back to global default one" });
/* 372 */     return Locale.US;
/*     */   }
/*     */   
/*     */   public boolean isExist(String key) {
/* 376 */     return (get(key) != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\Configuration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */