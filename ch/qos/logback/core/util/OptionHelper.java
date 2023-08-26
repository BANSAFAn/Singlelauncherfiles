/*     */ package ch.qos.logback.core.util;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.PropertyContainer;
/*     */ import ch.qos.logback.core.spi.ScanException;
/*     */ import ch.qos.logback.core.subst.NodeToStringTransformer;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Properties;
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
/*     */ public class OptionHelper
/*     */ {
/*     */   static final String DELIM_START = "${";
/*     */   static final char DELIM_STOP = '}';
/*     */   static final String DELIM_DEFAULT = ":-";
/*     */   static final int DELIM_START_LEN = 2;
/*     */   static final int DELIM_STOP_LEN = 1;
/*     */   static final int DELIM_DEFAULT_LEN = 2;
/*     */   static final String _IS_UNDEFINED = "_IS_UNDEFINED";
/*     */   
/*     */   public static Object instantiateByClassName(String className, Class<?> superClass, Context context) throws IncompatibleClassException, DynamicClassLoadingException {
/*  33 */     ClassLoader classLoader = Loader.getClassLoaderOfObject(context);
/*  34 */     return instantiateByClassName(className, superClass, classLoader);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object instantiateByClassNameAndParameter(String className, Class<?> superClass, Context context, Class<?> type, Object param) throws IncompatibleClassException, DynamicClassLoadingException {
/*  39 */     ClassLoader classLoader = Loader.getClassLoaderOfObject(context);
/*  40 */     return instantiateByClassNameAndParameter(className, superClass, classLoader, type, param);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object instantiateByClassName(String className, Class<?> superClass, ClassLoader classLoader) throws IncompatibleClassException, DynamicClassLoadingException {
/*  45 */     return instantiateByClassNameAndParameter(className, superClass, classLoader, (Class<?>)null, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object instantiateByClassNameAndParameter(String className, Class<?> superClass, ClassLoader classLoader, Class<?> type, Object parameter) throws IncompatibleClassException, DynamicClassLoadingException {
/*  51 */     if (className == null) {
/*  52 */       throw new NullPointerException();
/*     */     }
/*     */     try {
/*  55 */       Class<?> classObj = null;
/*  56 */       classObj = classLoader.loadClass(className);
/*  57 */       if (!superClass.isAssignableFrom(classObj)) {
/*  58 */         throw new IncompatibleClassException(superClass, classObj);
/*     */       }
/*  60 */       if (type == null) {
/*  61 */         return classObj.newInstance();
/*     */       }
/*  63 */       Constructor<?> constructor = classObj.getConstructor(new Class[] { type });
/*  64 */       return constructor.newInstance(new Object[] { parameter });
/*     */     }
/*  66 */     catch (IncompatibleClassException ice) {
/*  67 */       throw ice;
/*  68 */     } catch (Throwable t) {
/*  69 */       throw new DynamicClassLoadingException("Failed to instantiate type " + className, t);
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
/*     */   public static String substVars(String val, PropertyContainer pc1) {
/* 104 */     return substVars(val, pc1, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String substVars(String input, PropertyContainer pc0, PropertyContainer pc1) {
/*     */     try {
/* 112 */       return NodeToStringTransformer.substituteVariable(input, pc0, pc1);
/* 113 */     } catch (ScanException e) {
/* 114 */       throw new IllegalArgumentException("Failed to parse input [" + input + "]", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String propertyLookup(String key, PropertyContainer pc1, PropertyContainer pc2) {
/* 119 */     String value = null;
/*     */     
/* 121 */     value = pc1.getProperty(key);
/*     */ 
/*     */     
/* 124 */     if (value == null && pc2 != null) {
/* 125 */       value = pc2.getProperty(key);
/*     */     }
/*     */     
/* 128 */     if (value == null) {
/* 129 */       value = getSystemProperty(key, null);
/*     */     }
/* 131 */     if (value == null) {
/* 132 */       value = getEnv(key);
/*     */     }
/* 134 */     return value;
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
/*     */   public static String getSystemProperty(String key, String def) {
/*     */     try {
/* 148 */       return System.getProperty(key, def);
/* 149 */     } catch (SecurityException e) {
/* 150 */       return def;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getEnv(String key) {
/*     */     try {
/* 162 */       return System.getenv(key);
/* 163 */     } catch (SecurityException e) {
/* 164 */       return null;
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
/*     */   public static String getSystemProperty(String key) {
/*     */     try {
/* 177 */       return System.getProperty(key);
/* 178 */     } catch (SecurityException e) {
/* 179 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setSystemProperties(ContextAware contextAware, Properties props) {
/* 184 */     for (Object o : props.keySet()) {
/* 185 */       String key = (String)o;
/* 186 */       String value = props.getProperty(key);
/* 187 */       setSystemProperty(contextAware, key, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setSystemProperty(ContextAware contextAware, String key, String value) {
/*     */     try {
/* 193 */       System.setProperty(key, value);
/* 194 */     } catch (SecurityException e) {
/* 195 */       contextAware.addError("Failed to set system property [" + key + "]", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Properties getSystemProperties() {
/*     */     try {
/* 207 */       return System.getProperties();
/* 208 */     } catch (SecurityException e) {
/* 209 */       return new Properties();
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
/*     */   public static String[] extractDefaultReplacement(String key) {
/* 222 */     String[] result = new String[2];
/* 223 */     if (key == null) {
/* 224 */       return result;
/*     */     }
/* 226 */     result[0] = key;
/* 227 */     int d = key.indexOf(":-");
/* 228 */     if (d != -1) {
/* 229 */       result[0] = key.substring(0, d);
/* 230 */       result[1] = key.substring(d + 2);
/*     */     } 
/* 232 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean toBoolean(String value, boolean dEfault) {
/* 243 */     if (value == null) {
/* 244 */       return dEfault;
/*     */     }
/*     */     
/* 247 */     String trimmedVal = value.trim();
/*     */     
/* 249 */     if ("true".equalsIgnoreCase(trimmedVal)) {
/* 250 */       return true;
/*     */     }
/*     */     
/* 253 */     if ("false".equalsIgnoreCase(trimmedVal)) {
/* 254 */       return false;
/*     */     }
/*     */     
/* 257 */     return dEfault;
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(String str) {
/* 261 */     return (str == null || "".equals(str));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\cor\\util\OptionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */