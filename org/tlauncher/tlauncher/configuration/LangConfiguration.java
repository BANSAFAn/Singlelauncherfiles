/*     */ package org.tlauncher.tlauncher.configuration;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.Properties;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ public class LangConfiguration
/*     */   extends SimpleConfiguration
/*     */ {
/*     */   private final Locale[] locales;
/*     */   private final Properties[] prop;
/*     */   private int i;
/*     */   
/*     */   public LangConfiguration(Locale[] locales, Locale select, String folder) throws IOException {
/*  20 */     if (locales == null) {
/*  21 */       throw new NullPointerException();
/*     */     }
/*  23 */     int size = locales.length;
/*     */     
/*  25 */     this.locales = locales;
/*  26 */     this.prop = new Properties[size];
/*     */     
/*  28 */     for (int i = 0; i < size; i++) {
/*  29 */       Locale locale = locales[i];
/*     */       
/*  31 */       if (locale == null) {
/*  32 */         throw new NullPointerException("Locale at #" + i + " is NULL!");
/*     */       }
/*  34 */       String localeName = locale.toString();
/*  35 */       InputStream stream = getClass().getResourceAsStream(folder + localeName);
/*     */       
/*  37 */       if (stream == null) {
/*  38 */         throw new IOException("Cannot find locale file for: " + localeName);
/*     */       }
/*     */       
/*  41 */       this.prop[i] = loadFromStream(stream);
/*     */       
/*  43 */       if (localeName.equals("en_US"))
/*     */       {
/*  45 */         copyProperties(this.prop[i], this.properties, true);
/*     */       }
/*     */     } 
/*  48 */     int defLocale = -1;
/*     */     int j;
/*  50 */     for (j = 0; j < size; j++) {
/*  51 */       if (locales[j].toString().equals("ru_RU")) {
/*  52 */         defLocale = j;
/*     */         break;
/*     */       } 
/*     */     } 
/*  56 */     if (defLocale != -1) {
/*     */       
/*  58 */       for (Object key : this.prop[defLocale].keySet()) {
/*  59 */         for (int k = 0; k < size; k++) {
/*  60 */           if (k != defLocale)
/*     */           {
/*  62 */             if (!this.prop[k].containsKey(key) && !TLauncher.DEBUG)
/*  63 */               U.log(new Object[] { "Locale", locales[k], "doesn't contain key", key });  } 
/*     */         } 
/*     */       } 
/*  66 */       for (j = 0; j < size; j++) {
/*  67 */         if (j != defLocale) {
/*     */           
/*  69 */           int numberLine = 0;
/*  70 */           for (Object key : this.prop[j].keySet()) {
/*  71 */             numberLine++;
/*  72 */             if (!this.prop[defLocale].containsKey(key)) {
/*  73 */               U.log(new Object[] { "Locale", locales[j], "contains redundant key line is " + numberLine + ", key is " + key });
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  80 */     setSelected(select);
/*     */   }
/*     */   
/*     */   public Locale[] getLocales() {
/*  84 */     return this.locales;
/*     */   }
/*     */   
/*     */   public Locale getSelected() {
/*  88 */     return this.locales[this.i];
/*     */   }
/*     */   
/*     */   public void setSelected(Locale select) {
/*  92 */     if (select == null) {
/*  93 */       throw new NullPointerException();
/*     */     }
/*  95 */     for (int i = 0; i < this.locales.length; i++) {
/*  96 */       if (this.locales[i].equals(select)) {
/*  97 */         this.i = i;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 102 */     throw new IllegalArgumentException("Cannot find Locale:" + select);
/*     */   }
/*     */   
/*     */   public String nget(String key) {
/* 106 */     if (key == null) {
/* 107 */       return null;
/*     */     }
/* 109 */     String value = this.prop[this.i].getProperty(key);
/*     */     
/* 111 */     if (value == null) {
/* 112 */       return getDefault(key);
/*     */     }
/* 114 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(String key) {
/* 119 */     String value = nget(key);
/*     */     
/* 121 */     if (value == null) {
/* 122 */       return key;
/*     */     }
/* 124 */     return value;
/*     */   }
/*     */   
/*     */   public String nget(String key, Object... vars) {
/* 128 */     String value = get(key);
/*     */     
/* 130 */     if (value == null) {
/* 131 */       return null;
/*     */     }
/* 133 */     String[] variables = checkVariables(vars);
/*     */     
/* 135 */     for (int i = 0; i < variables.length; i++) {
/* 136 */       value = value.replace("%" + i, variables[i]);
/*     */     }
/* 138 */     return value;
/*     */   }
/*     */   
/*     */   public String ngetKeys(String key, Object... keys) {
/* 142 */     String value = get(key);
/*     */     
/* 144 */     if (value == null) {
/* 145 */       return null;
/*     */     }
/* 147 */     String[] variables = checkVariables(keys);
/*     */     
/* 149 */     for (int i = 0; i < variables.length; i++) {
/* 150 */       value = value.replace("%" + i, Localizable.get(variables[i]));
/*     */     }
/* 152 */     return value;
/*     */   }
/*     */   
/*     */   public String get(String key, Object... vars) {
/* 156 */     String value = nget(key, vars);
/*     */     
/* 158 */     if (value == null) {
/* 159 */       return key;
/*     */     }
/* 161 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(String key, Object value) {
/* 167 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefault(String key) {
/* 172 */     return super.get(key);
/*     */   }
/*     */   
/*     */   private static String[] checkVariables(Object[] check) {
/* 176 */     if (check == null) {
/* 177 */       throw new NullPointerException();
/*     */     }
/* 179 */     if (check.length == 1 && check[0] == null) {
/* 180 */       return new String[0];
/*     */     }
/* 182 */     String[] string = new String[check.length];
/*     */     
/* 184 */     for (int i = 0; i < check.length; i++) {
/* 185 */       if (check[i] == null) {
/* 186 */         throw new NullPointerException("Variable at index " + i + " is NULL!");
/*     */       }
/* 188 */       string[i] = check[i].toString();
/*     */     } 
/*     */     
/* 191 */     return string;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\LangConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */