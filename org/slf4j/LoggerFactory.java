/*     */ package org.slf4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.slf4j.helpers.NOPLoggerFactory;
/*     */ import org.slf4j.helpers.SubstituteLoggerFactory;
/*     */ import org.slf4j.helpers.Util;
/*     */ import org.slf4j.impl.StaticLoggerBinder;
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
/*     */ public final class LoggerFactory
/*     */ {
/*     */   static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";
/*     */   static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";
/*     */   static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";
/*     */   static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";
/*     */   static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";
/*     */   static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";
/*     */   static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final int UNINITIALIZED = 0;
/*     */   static final int ONGOING_INITIALIZATION = 1;
/*     */   static final int FAILED_INITIALIZATION = 2;
/*     */   static final int SUCCESSFUL_INITIALIZATION = 3;
/*     */   static final int NOP_FALLBACK_INITIALIZATION = 4;
/*  73 */   static int INITIALIZATION_STATE = 0;
/*  74 */   static SubstituteLoggerFactory TEMP_FACTORY = new SubstituteLoggerFactory();
/*  75 */   static NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   private static final String[] API_COMPATIBILITY_LIST = new String[] { "1.6", "1.7" };
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
/*     */   static void reset() {
/* 102 */     INITIALIZATION_STATE = 0;
/* 103 */     TEMP_FACTORY = new SubstituteLoggerFactory();
/*     */   }
/*     */   
/*     */   private static final void performInitialization() {
/* 107 */     bind();
/* 108 */     if (INITIALIZATION_STATE == 3) {
/* 109 */       versionSanityCheck();
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean messageContainsOrgSlf4jImplStaticLoggerBinder(String msg) {
/* 114 */     if (msg == null)
/* 115 */       return false; 
/* 116 */     if (msg.indexOf("org/slf4j/impl/StaticLoggerBinder") != -1)
/* 117 */       return true; 
/* 118 */     if (msg.indexOf("org.slf4j.impl.StaticLoggerBinder") != -1)
/* 119 */       return true; 
/* 120 */     return false;
/*     */   }
/*     */   
/*     */   private static final void bind() {
/*     */     try {
/* 125 */       Set staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
/* 126 */       reportMultipleBindingAmbiguity(staticLoggerBinderPathSet);
/*     */       
/* 128 */       StaticLoggerBinder.getSingleton();
/* 129 */       INITIALIZATION_STATE = 3;
/* 130 */       reportActualBinding(staticLoggerBinderPathSet);
/* 131 */       emitSubstituteLoggerWarning();
/* 132 */     } catch (NoClassDefFoundError ncde) {
/* 133 */       String msg = ncde.getMessage();
/* 134 */       if (messageContainsOrgSlf4jImplStaticLoggerBinder(msg)) {
/* 135 */         INITIALIZATION_STATE = 4;
/* 136 */         Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
/* 137 */         Util.report("Defaulting to no-operation (NOP) logger implementation");
/* 138 */         Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
/*     */       } else {
/*     */         
/* 141 */         failedBinding(ncde);
/* 142 */         throw ncde;
/*     */       } 
/* 144 */     } catch (NoSuchMethodError nsme) {
/* 145 */       String msg = nsme.getMessage();
/* 146 */       if (msg != null && msg.indexOf("org.slf4j.impl.StaticLoggerBinder.getSingleton()") != -1) {
/* 147 */         INITIALIZATION_STATE = 2;
/* 148 */         Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
/* 149 */         Util.report("Your binding is version 1.5.5 or earlier.");
/* 150 */         Util.report("Upgrade your binding to version 1.6.x.");
/*     */       } 
/* 152 */       throw nsme;
/* 153 */     } catch (Exception e) {
/* 154 */       failedBinding(e);
/* 155 */       throw new IllegalStateException("Unexpected initialization failure", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void failedBinding(Throwable t) {
/* 160 */     INITIALIZATION_STATE = 2;
/* 161 */     Util.report("Failed to instantiate SLF4J LoggerFactory", t);
/*     */   }
/*     */   
/*     */   private static final void emitSubstituteLoggerWarning() {
/* 165 */     List<String> loggerNameList = TEMP_FACTORY.getLoggerNameList();
/* 166 */     if (loggerNameList.size() == 0) {
/*     */       return;
/*     */     }
/* 169 */     Util.report("The following loggers will not work because they were created");
/* 170 */     Util.report("during the default configuration phase of the underlying logging system.");
/* 171 */     Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
/* 172 */     for (int i = 0; i < loggerNameList.size(); i++) {
/* 173 */       String loggerName = loggerNameList.get(i);
/* 174 */       Util.report(loggerName);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final void versionSanityCheck() {
/*     */     try {
/* 180 */       String requested = StaticLoggerBinder.REQUESTED_API_VERSION;
/*     */       
/* 182 */       boolean match = false;
/* 183 */       for (int i = 0; i < API_COMPATIBILITY_LIST.length; i++) {
/* 184 */         if (requested.startsWith(API_COMPATIBILITY_LIST[i])) {
/* 185 */           match = true;
/*     */         }
/*     */       } 
/* 188 */       if (!match) {
/* 189 */         Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + Arrays.<String>asList(API_COMPATIBILITY_LIST).toString());
/*     */ 
/*     */         
/* 192 */         Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
/*     */       } 
/* 194 */     } catch (NoSuchFieldError nsfe) {
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 199 */     catch (Throwable e) {
/*     */       
/* 201 */       Util.report("Unexpected problem occured during version sanity check", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 207 */   private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set findPossibleStaticLoggerBinderPathSet() {
/* 212 */     Set<URL> staticLoggerBinderPathSet = new LinkedHashSet(); try {
/*     */       Enumeration<URL> paths;
/* 214 */       ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
/*     */ 
/*     */       
/* 217 */       if (loggerFactoryClassLoader == null) {
/* 218 */         paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
/*     */       } else {
/* 220 */         paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
/*     */       } 
/*     */       
/* 223 */       while (paths.hasMoreElements()) {
/* 224 */         URL path = paths.nextElement();
/* 225 */         staticLoggerBinderPathSet.add(path);
/*     */       } 
/* 227 */     } catch (IOException ioe) {
/* 228 */       Util.report("Error getting resources from path", ioe);
/*     */     } 
/* 230 */     return staticLoggerBinderPathSet;
/*     */   }
/*     */   
/*     */   private static boolean isAmbiguousStaticLoggerBinderPathSet(Set staticLoggerBinderPathSet) {
/* 234 */     return (staticLoggerBinderPathSet.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reportMultipleBindingAmbiguity(Set staticLoggerBinderPathSet) {
/* 243 */     if (isAmbiguousStaticLoggerBinderPathSet(staticLoggerBinderPathSet)) {
/* 244 */       Util.report("Class path contains multiple SLF4J bindings.");
/* 245 */       Iterator<URL> iterator = staticLoggerBinderPathSet.iterator();
/* 246 */       while (iterator.hasNext()) {
/* 247 */         URL path = iterator.next();
/* 248 */         Util.report("Found binding in [" + path + "]");
/*     */       } 
/* 250 */       Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void reportActualBinding(Set staticLoggerBinderPathSet) {
/* 255 */     if (isAmbiguousStaticLoggerBinderPathSet(staticLoggerBinderPathSet)) {
/* 256 */       Util.report("Actual binding is of type [" + StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr() + "]");
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
/*     */   public static Logger getLogger(String name) {
/* 269 */     ILoggerFactory iLoggerFactory = getILoggerFactory();
/* 270 */     return iLoggerFactory.getLogger(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(Class clazz) {
/* 281 */     return getLogger(clazz.getName());
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
/*     */   public static ILoggerFactory getILoggerFactory() {
/* 293 */     if (INITIALIZATION_STATE == 0) {
/* 294 */       INITIALIZATION_STATE = 1;
/* 295 */       performInitialization();
/*     */     } 
/* 297 */     switch (INITIALIZATION_STATE) {
/*     */       case 3:
/* 299 */         return StaticLoggerBinder.getSingleton().getLoggerFactory();
/*     */       case 4:
/* 301 */         return (ILoggerFactory)NOP_FALLBACK_FACTORY;
/*     */       case 2:
/* 303 */         throw new IllegalStateException("org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit");
/*     */ 
/*     */       
/*     */       case 1:
/* 307 */         return (ILoggerFactory)TEMP_FACTORY;
/*     */     } 
/* 309 */     throw new IllegalStateException("Unreachable code");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\LoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */