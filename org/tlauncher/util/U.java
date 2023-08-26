/*     */ package org.tlauncher.util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.function.Function;
/*     */ import org.apache.commons.lang3.time.DateUtils;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.ConsoleAppender;
/*     */ import org.apache.log4j.FileAppender;
/*     */ import org.apache.log4j.Layout;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.PatternLayout;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ import org.tlauncher.exceptions.CheckedFunction;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
/*     */ import org.tlauncher.tlauncher.rmo.Bootstrapper;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.async.ExtendedThread;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class U
/*     */ {
/*     */   public static final String PROGRAM_PACKAGE = "org.tlauncher";
/*     */   public static final int CONNECTION_TIMEOUT = 30000;
/*  49 */   public static final PatternLayout LOG_LAYOUT = new PatternLayout("%m%n"); private static final int ST_TOTAL = 100;
/*     */   private static final int ST_PROGRAM = 10;
/*  51 */   public static String FLUSH_MESSAGE = "flush now";
/*     */ 
/*     */   
/*     */   private static Logger logField;
/*     */ 
/*     */   
/*     */   private static FileAppender appender;
/*     */ 
/*     */   
/*     */   private static ConsoleAppender console;
/*     */ 
/*     */   
/*     */   public static void log(Object... what) {
/*  64 */     hlog(null, what);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void plog(Object... what) {
/*  71 */     hlog(null, what);
/*     */   }
/*     */   
/*     */   private static void hlog(String prefix, Object[] append) {
/*  75 */     if (Objects.nonNull(logField)) {
/*  76 */       logField.info(toLog(prefix, append));
/*     */     } else {
/*  78 */       System.out.println(toLog(prefix, append));
/*     */     } 
/*     */   }
/*     */   private static String toLog(String prefix, Object... append) {
/*  82 */     StringBuilder b = new StringBuilder();
/*  83 */     boolean first = true;
/*     */     
/*  85 */     if (prefix != null) {
/*  86 */       b.append(prefix);
/*  87 */       first = false;
/*     */     } 
/*     */     
/*  90 */     if (append != null)
/*  91 */     { for (Object e : append) {
/*  92 */         if (e != null)
/*  93 */         { if (e.getClass().isArray())
/*  94 */           { if (!first) {
/*  95 */               b.append(" ");
/*     */             }
/*  97 */             if (e instanceof Object[]) {
/*  98 */               b.append(toLog((Object[])e));
/*     */             } else {
/* 100 */               b.append(arrayToLog(e));
/*     */             }
/*     */              }
/* 103 */           else if (e instanceof Throwable)
/* 104 */           { if (!first)
/* 105 */               b.append("\n"); 
/* 106 */             b.append(stackTrace((Throwable)e));
/* 107 */             b.append("\n"); }
/*     */           else
/*     */           
/* 110 */           { if (e instanceof File) {
/* 111 */               if (!first)
/* 112 */                 b.append(" "); 
/* 113 */               File file = (File)e;
/* 114 */               String absPath = file.getAbsolutePath();
/*     */               
/* 116 */               b.append(absPath);
/*     */               
/* 118 */               if (file.isDirectory() && !absPath.endsWith(File.separator))
/* 119 */                 b.append(File.separator); 
/* 120 */             } else if (e instanceof Iterator) {
/* 121 */               Iterator<?> i = (Iterator)e;
/* 122 */               while (i.hasNext()) {
/* 123 */                 b.append(" ");
/* 124 */                 b.append(toLog(new Object[] { i.next() }));
/*     */               } 
/* 126 */             } else if (e instanceof Enumeration) {
/* 127 */               Enumeration<?> en = (Enumeration)e;
/* 128 */               while (en.hasMoreElements()) {
/* 129 */                 b.append(" ");
/* 130 */                 b.append(toLog(new Object[] { en.nextElement() }));
/*     */               } 
/*     */             } else {
/* 133 */               if (!first)
/* 134 */                 b.append(" "); 
/* 135 */               b.append(e);
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 143 */             if (first)
/* 144 */               first = false;  }  } else { if (!first) b.append(" ");  b.append("null"); if (first) first = false;  }
/*     */       
/*     */       }  }
/* 147 */     else { b.append("null"); }
/*     */     
/* 149 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static String toLog(Object... append) {
/* 153 */     return toLog(null, append);
/*     */   }
/*     */   
/*     */   private static String arrayToLog(Object e) {
/* 157 */     if (!e.getClass().isArray()) {
/* 158 */       throw new IllegalArgumentException("Given object is not an array!");
/*     */     }
/* 160 */     StringBuilder b = new StringBuilder();
/* 161 */     boolean first = true;
/*     */     
/* 163 */     if (e instanceof Object[]) {
/* 164 */       for (Object i : (Object[])e) {
/* 165 */         if (!first) {
/* 166 */           b.append(" ");
/*     */         } else {
/* 168 */           first = false;
/* 169 */         }  b.append(i);
/*     */       } 
/* 171 */     } else if (e instanceof int[]) {
/* 172 */       for (int i : (int[])e) {
/* 173 */         if (!first) {
/* 174 */           b.append(" ");
/*     */         } else {
/* 176 */           first = false;
/* 177 */         }  b.append(i);
/*     */       } 
/* 179 */     } else if (e instanceof boolean[]) {
/* 180 */       for (boolean i : (boolean[])e) {
/* 181 */         if (!first) {
/* 182 */           b.append(" ");
/*     */         } else {
/* 184 */           first = false;
/* 185 */         }  b.append(i);
/*     */       } 
/* 187 */     } else if (e instanceof long[]) {
/* 188 */       for (long i : (long[])e) {
/* 189 */         if (!first) {
/* 190 */           b.append(" ");
/*     */         } else {
/* 192 */           first = false;
/* 193 */         }  b.append(i);
/*     */       } 
/* 195 */     } else if (e instanceof float[]) {
/* 196 */       for (float i : (float[])e) {
/* 197 */         if (!first) {
/* 198 */           b.append(" ");
/*     */         } else {
/* 200 */           first = false;
/* 201 */         }  b.append(i);
/*     */       } 
/* 203 */     } else if (e instanceof double[]) {
/* 204 */       for (double i : (double[])e) {
/* 205 */         if (!first) {
/* 206 */           b.append(" ");
/*     */         } else {
/* 208 */           first = false;
/* 209 */         }  b.append(i);
/*     */       } 
/* 211 */     } else if (e instanceof byte[]) {
/* 212 */       for (byte i : (byte[])e) {
/* 213 */         if (!first) {
/* 214 */           b.append(" ");
/*     */         } else {
/* 216 */           first = false;
/* 217 */         }  b.append(i);
/*     */       } 
/* 219 */     } else if (e instanceof short[]) {
/* 220 */       for (short i : (short[])e) {
/* 221 */         if (!first) {
/* 222 */           b.append(" ");
/*     */         } else {
/* 224 */           first = false;
/* 225 */         }  b.append(i);
/*     */       } 
/* 227 */     } else if (e instanceof char[]) {
/* 228 */       for (char i : (char[])e) {
/* 229 */         if (!first) {
/* 230 */           b.append(" ");
/*     */         } else {
/* 232 */           first = false;
/* 233 */         }  b.append(i);
/*     */       } 
/*     */     } 
/* 236 */     if (b.length() == 0) {
/* 237 */       throw new UnknownError("Unknown array type given.");
/*     */     }
/* 239 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static void setLoadingStep(Bootstrapper.LoadingStep step) {
/* 243 */     if (step == null) {
/* 244 */       throw new NullPointerException();
/*     */     }
/* 246 */     plog(new Object[] { "[Loading]", step.toString() });
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean ok(int d) {
/* 251 */     return ((new Random(System.currentTimeMillis())).nextInt(d) == 0);
/*     */   }
/*     */   
/*     */   public static double getAverage(double[] d) {
/* 255 */     double a = 0.0D;
/* 256 */     int k = 0;
/*     */     
/* 258 */     for (double curd : d) {
/* 259 */       if (curd != 0.0D) {
/*     */         
/* 261 */         a += curd;
/* 262 */         k++;
/*     */       } 
/*     */     } 
/* 265 */     if (k == 0)
/* 266 */       return 0.0D; 
/* 267 */     return a / k;
/*     */   }
/*     */   
/*     */   public static double getAverage(double[] d, int max) {
/* 271 */     double a = 0.0D;
/* 272 */     int k = 0;
/*     */     
/* 274 */     for (double curd : d) {
/* 275 */       a += curd;
/* 276 */       k++;
/* 277 */       if (k == max) {
/*     */         break;
/*     */       }
/*     */     } 
/* 281 */     if (k == 0)
/* 282 */       return 0.0D; 
/* 283 */     return a / k;
/*     */   }
/*     */   
/*     */   public static double getSum(double[] d) {
/* 287 */     double a = 0.0D;
/*     */     
/* 289 */     for (double curd : d)
/* 290 */       a += curd; 
/* 291 */     return a;
/*     */   }
/*     */   
/*     */   public static int getMaxMultiply(int i, int max) {
/* 295 */     if (i <= max)
/* 296 */       return 1; 
/* 297 */     for (int x = max; x > 1; x--) {
/* 298 */       if (i % x == 0)
/* 299 */         return x; 
/* 300 */     }  return (int)Math.ceil((i / max));
/*     */   }
/*     */   
/*     */   private static String stackTrace(Throwable e) {
/* 304 */     StringBuilder trace = rawStackTrace(e);
/*     */     
/* 306 */     ExtendedThread currentAsExtended = getAs(Thread.currentThread(), ExtendedThread.class);
/* 307 */     if (currentAsExtended != null) {
/* 308 */       trace.append("\nThread called by: ").append(rawStackTrace((Throwable)currentAsExtended.getCaller()));
/*     */     }
/* 310 */     return trace.toString();
/*     */   }
/*     */   
/*     */   private static StringBuilder rawStackTrace(Throwable e) {
/* 314 */     if (e == null) {
/* 315 */       return null;
/*     */     }
/* 317 */     StackTraceElement[] elems = e.getStackTrace();
/* 318 */     int programElements = 0, totalElements = 0;
/*     */     
/* 320 */     StringBuilder builder = new StringBuilder();
/* 321 */     builder.append(e.toString());
/*     */     
/* 323 */     for (StackTraceElement elem : elems) {
/* 324 */       totalElements++;
/*     */       
/* 326 */       String description = elem.toString();
/*     */       
/* 328 */       if (description.startsWith("org.tlauncher")) {
/* 329 */         programElements++;
/*     */       }
/* 331 */       builder.append("\nat ").append(description);
/*     */       
/* 333 */       if (totalElements == 100 || programElements == 10) {
/*     */         
/* 335 */         int remain = elems.length - totalElements;
/*     */         
/* 337 */         if (remain != 0) {
/* 338 */           builder.append("\n... and ").append(remain).append(" more");
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 344 */     Throwable cause = e.getCause();
/* 345 */     if (cause != null) {
/* 346 */       builder.append("\nCaused by: ").append(rawStackTrace(cause));
/*     */     }
/* 348 */     return builder;
/*     */   }
/*     */   
/*     */   public static long getUsingSpace() {
/* 352 */     return getTotalSpace() - getFreeSpace();
/*     */   }
/*     */   
/*     */   public static long getFreeSpace() {
/* 356 */     return Runtime.getRuntime().freeMemory() / 1048576L;
/*     */   }
/*     */   
/*     */   public static long getTotalSpace() {
/* 360 */     return Runtime.getRuntime().totalMemory() / 1048576L;
/*     */   }
/*     */   
/*     */   public static String memoryStatus() {
/* 364 */     return getUsingSpace() + " / " + getTotalSpace() + " MB";
/*     */   }
/*     */   
/*     */   public static void gc() {
/* 368 */     log(new Object[] { "Starting garbage collector: " + memoryStatus() });
/* 369 */     System.gc();
/* 370 */     log(new Object[] { "Garbage collector completed: " + memoryStatus() });
/*     */   }
/*     */   
/*     */   public static void sleepFor(long millis) {
/*     */     try {
/* 375 */       Thread.sleep(millis);
/* 376 */     } catch (Exception e) {
/* 377 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static URL makeURL(String p) {
/*     */     try {
/* 383 */       return new URL(p);
/* 384 */     } catch (Exception e) {
/* 385 */       log(new Object[] { "Cannot make URL from string: " + p + ".", e });
/*     */       
/* 387 */       return null;
/*     */     } 
/*     */   }
/*     */   public static URI makeURI(URL url) {
/*     */     try {
/* 392 */       return url.toURI();
/* 393 */     } catch (Exception e) {
/* 394 */       log(new Object[] { "Cannot make URI from URL: " + url + ".", e });
/*     */ 
/*     */       
/* 397 */       return null;
/*     */     } 
/*     */   }
/*     */   public static URI makeURI(String p) {
/* 401 */     return makeURI(makeURL(p));
/*     */   }
/*     */   
/*     */   private static int fitInterval(int val, int min, int max) {
/* 405 */     if (val > max)
/* 406 */       return max; 
/* 407 */     if (val < min)
/* 408 */       return min; 
/* 409 */     return val;
/*     */   }
/*     */   
/*     */   public static long m() {
/* 413 */     return System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public static long n() {
/* 417 */     return System.nanoTime();
/*     */   }
/*     */   
/*     */   public static int getReadTimeout() {
/* 421 */     return getConnectionTimeout();
/*     */   }
/*     */   
/*     */   public static int getConnectionTimeout() {
/* 425 */     TLauncher t = TLauncher.getInstance();
/* 426 */     if (t == null) {
/* 427 */       return 30000;
/*     */     }
/* 429 */     ConnectionQuality quality = t.getConfiguration().getConnectionQuality();
/* 430 */     if (quality == null) {
/* 431 */       return 30000;
/*     */     }
/* 433 */     return quality.getTimeout();
/*     */   }
/*     */   
/*     */   public static Proxy getProxy() {
/* 437 */     return Proxy.NO_PROXY;
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
/*     */   public static <K, E> LinkedHashMap<K, E> sortMap(Map<K, E> map, K[] sortedKeys) {
/* 470 */     if (map == null) {
/* 471 */       return null;
/*     */     }
/* 473 */     if (sortedKeys == null) {
/* 474 */       throw new NullPointerException("Keys cannot be NULL!");
/*     */     }
/* 476 */     LinkedHashMap<K, E> result = new LinkedHashMap<>();
/*     */     
/* 478 */     for (K key : sortedKeys) {
/* 479 */       for (Map.Entry<K, E> entry : map.entrySet()) {
/* 480 */         K entryKey = entry.getKey();
/* 481 */         E value = entry.getValue();
/*     */         
/* 483 */         if (key == null && entryKey == null) {
/* 484 */           result.put(null, value);
/*     */           
/*     */           break;
/*     */         } 
/* 488 */         if (key == null)
/*     */           continue; 
/* 490 */         if (!key.equals(entryKey)) {
/*     */           continue;
/*     */         }
/* 493 */         result.put(key, value);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 498 */     return result;
/*     */   }
/*     */   
/*     */   public static Color shiftColor(Color color, int bits) {
/* 502 */     if (color == null) {
/* 503 */       return null;
/*     */     }
/* 505 */     if (bits == 0) {
/* 506 */       return color;
/*     */     }
/* 508 */     int newRed = fitInterval(color.getRed() + bits, 0, 255);
/* 509 */     int newGreen = fitInterval(color.getGreen() + bits, 0, 255);
/* 510 */     int newBlue = fitInterval(color.getBlue() + bits, 0, 255);
/*     */     
/* 512 */     return new Color(newRed, newGreen, newBlue, color.getAlpha());
/*     */   }
/*     */   
/*     */   public static Color shiftAlpha(Color color, int bits) {
/* 516 */     if (color == null) {
/* 517 */       return null;
/*     */     }
/* 519 */     if (bits == 0) {
/* 520 */       return color;
/*     */     }
/* 522 */     int newAlpha = fitInterval(color.getAlpha() + bits, 0, 255);
/*     */     
/* 524 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), newAlpha);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static <T> T getAs(Object o, Class<T> classOfT) {
/* 529 */     return Reflect.cast(o, classOfT);
/*     */   }
/*     */   
/*     */   public static boolean equal(Object a, Object b) {
/* 533 */     if (a == b) {
/* 534 */       return true;
/*     */     }
/* 536 */     if (a != null) {
/* 537 */       return a.equals(b);
/*     */     }
/* 539 */     return false;
/*     */   }
/*     */   
/*     */   public static void close(Closeable c) {
/*     */     try {
/* 544 */       c.close();
/* 545 */     } catch (Throwable e) {
/* 546 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T> int find(T obj, T[] array) {
/* 551 */     if (obj == null)
/* 552 */     { for (int i = 0; i < array.length; i++) {
/* 553 */         if (array[i] == null)
/* 554 */           return i; 
/*     */       }  }
/* 556 */     else { for (int i = 0; i < array.length; i++) {
/* 557 */         if (obj.equals(array[i]))
/* 558 */           return i; 
/*     */       }  }
/*     */     
/* 561 */     return -1;
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
/*     */   public static void initializeLoggerU(File minecraftFolder, String type) {
/* 575 */     if (minecraftFolder == null) {
/* 576 */       minecraftFolder = MinecraftUtil.getDefaultWorkingDirectory();
/*     */     }
/* 578 */     SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
/* 579 */     String separator = System.getProperty("file.separator");
/*     */     
/* 581 */     File path = new File(minecraftFolder, separator + "logs" + separator + "tlauncher" + separator + type + "_" + formatter.format(new Date()) + ".log");
/* 582 */     appender = new FileAppender() {
/*     */         protected boolean shouldFlush(LoggingEvent event) {
/* 584 */           if (!(event.getMessage() instanceof String))
/* 585 */             return false; 
/* 586 */           return U.FLUSH_MESSAGE.equalsIgnoreCase((String)event.getMessage());
/*     */         }
/*     */       };
/* 589 */     appender.setName("fileAppender");
/* 590 */     appender.setLayout((Layout)LOG_LAYOUT);
/* 591 */     appender.setFile(path.getAbsolutePath());
/* 592 */     appender.setThreshold((Priority)Level.INFO);
/* 593 */     appender.activateOptions();
/* 594 */     appender.setBufferedIO(true);
/* 595 */     appender.setEncoding(TlauncherUtil.LOG_CHARSET);
/* 596 */     Logger.getRootLogger().addAppender((Appender)appender);
/* 597 */     logField = Logger.getLogger("main");
/*     */     
/* 599 */     console = new ConsoleAppender();
/* 600 */     console.setName("console");
/* 601 */     console.setLayout((Layout)new PatternLayout("%m%n"));
/* 602 */     console.setThreshold((Priority)Level.INFO);
/* 603 */     console.activateOptions();
/* 604 */     console.setEncoding(TlauncherUtil.LOG_CHARSET);
/* 605 */     Logger.getRootLogger().addAppender((Appender)console);
/* 606 */     Logger.getRootLogger().setLevel(Level.INFO);
/*     */     
/* 608 */     if ("tlauncher".equalsIgnoreCase(type)) {
/*     */       try {
/* 610 */         Files.walk(Paths.get(minecraftFolder.getAbsolutePath(), new String[] { "logs" }), new java.nio.file.FileVisitOption[0]).filter(x$0 -> Files.isRegularFile(x$0, new java.nio.file.LinkOption[0]))
/* 611 */           .filter(p -> ((Boolean)uncheckCall(())).booleanValue())
/*     */ 
/*     */ 
/*     */           
/* 615 */           .forEach(p -> FileUtil.deleteFile(p.toFile()));
/* 616 */       } catch (Throwable e) {
/* 617 */         log(new Object[] { e });
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void debug(Object... ob) {
/* 627 */     if (!TLauncher.DEBUG)
/*     */       return; 
/* 629 */     plog(new Object[] { "[DEBUG] ----- ", ob });
/*     */   }
/*     */ 
/*     */   
/*     */   public static URI fixInvallidLink(String link) {
/*     */     try {
/* 635 */       if (link.contains("|")) {
/* 636 */         debug(new Object[] { "U", "replace |" });
/* 637 */         return new URI(link.replace("|", "%7C"));
/*     */       } 
/* 639 */     } catch (Exception e1) {
/* 640 */       e1.printStackTrace();
/*     */     } 
/*     */     try {
/* 643 */       if (link.contains("|")) {
/* 644 */         debug(new Object[] { "U", "replace |" });
/* 645 */         link = link.replace("|", "%7C");
/*     */       } 
/* 647 */       if (link.contains("?"))
/* 648 */         return new URI(link.substring(0, link.indexOf("?"))); 
/* 649 */     } catch (Exception e1) {
/* 650 */       e1.printStackTrace();
/*     */     } 
/* 652 */     return null;
/*     */   }
/*     */   
/*     */   public static <T> void classNameLog(Class<T> cl, Object message) {
/* 656 */     log(new Object[] { "[" + cl.getSimpleName() + "] ", message });
/*     */   }
/*     */   
/*     */   public static String readFileLog() {
/* 660 */     logField.info(FLUSH_MESSAGE);
/*     */     try {
/* 662 */       return FileUtil.readFile(new File(appender.getFile()), TlauncherUtil.LOG_CHARSET);
/* 663 */     } catch (IOException e) {
/* 664 */       logField.warn("can't read log file", e);
/* 665 */       return "can't read log file";
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void removeConsoleAppender() {
/* 670 */     Logger.getRootLogger().removeAppender((Appender)console);
/*     */   }
/*     */   
/*     */   static <T, R> Function<T, R> wrap(CheckedFunction<T, R> checkedFunction) {
/* 674 */     return t -> {
/*     */         try {
/*     */           return checkedFunction.apply(t);
/* 677 */         } catch (Exception e) {
/*     */           throw new RuntimeException(e);
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   public static <T> T uncheckCall(Callable<T> callable) {
/*     */     try {
/* 685 */       return callable.call();
/* 686 */     } catch (Exception e) {
/* 687 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\U.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */