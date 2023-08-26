/*     */ package org.tlauncher.util;
/*     */ 
/*     */ import com.sun.management.OperatingSystemMXBean;
/*     */ import java.awt.Desktop;
/*     */ import java.io.File;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
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
/*     */ public enum OS
/*     */ {
/*  35 */   LINUX(new String[] { "linux", "unix" }), WINDOWS(new String[] { "win" }), OSX(new String[] { "mac" }), SOLARIS(new String[] { "solaris", "sunos" }), UNKNOWN(new String[] { "unknown" }); private static final String[] browsers; private final String[] aliases; private final String name; private static final Map<String, String> systemInfo;
/*     */   static {
/*  37 */     NAME = System.getProperty("os.name"); VERSION = System.getProperty("os.version");
/*     */     
/*  39 */     JAVA_VERSION = getJavaVersion();
/*  40 */     CURRENT = getCurrent();
/*  41 */     systemInfo = new HashMap<>();
/*     */ 
/*     */     
/*  44 */     browsers = new String[] { "google-chrome", "firefox", "opera", "konqueror", "mozilla" };
/*     */   }
/*     */   public static final OS CURRENT; public static final double JAVA_VERSION; public static final String VERSION; public static final String NAME;
/*     */   OS(String... aliases) {
/*  48 */     if (aliases == null) {
/*  49 */       throw new NullPointerException();
/*     */     }
/*  51 */     this.name = toString().toLowerCase(Locale.ROOT);
/*  52 */     this.aliases = aliases;
/*     */   }
/*     */   
/*     */   private static OS getCurrent() {
/*  56 */     String osName = NAME.toLowerCase(Locale.ROOT);
/*     */     
/*  58 */     for (OS os : values()) {
/*  59 */       for (String alias : os.aliases) {
/*  60 */         if (osName.contains(alias))
/*  61 */           return os; 
/*     */       } 
/*  63 */     }  return UNKNOWN;
/*     */   }
/*     */   
/*     */   private static double getJavaVersion() {
/*  67 */     String version = System.getProperty("java.version");
/*  68 */     int count = 0;
/*     */     int pos;
/*  70 */     for (pos = 0; pos < version.length() && count < 2; pos++) {
/*  71 */       if (version.charAt(pos) == '.') {
/*  72 */         count++;
/*     */       }
/*     */     } 
/*     */     
/*  76 */     pos--;
/*     */     
/*  78 */     String doubleVersion = version.substring(0, pos);
/*  79 */     return Double.parseDouble(doubleVersion);
/*     */   }
/*     */   
/*     */   public static boolean isJava8() {
/*  83 */     return "8".equals(getJavaNumber());
/*     */   }
/*     */   
/*     */   public static boolean is(OS... any) {
/*  87 */     if (any == null) {
/*  88 */       throw new NullPointerException();
/*     */     }
/*  90 */     if (any.length == 0) {
/*  91 */       return false;
/*     */     }
/*  93 */     for (OS compare : any) {
/*  94 */       if (CURRENT == compare)
/*  95 */         return true; 
/*     */     } 
/*  97 */     return false;
/*     */   }
/*     */   
/*     */   public static String getJavaPathByHome(boolean appendBinFolder) {
/* 101 */     String path = System.getProperty("java.home");
/* 102 */     if (appendBinFolder) {
/* 103 */       path = appendToJVM(path);
/*     */     }
/* 105 */     return path;
/*     */   }
/*     */   
/*     */   public static String appendToJVM(String path) {
/* 109 */     char separator = File.separatorChar;
/* 110 */     StringBuilder b = new StringBuilder(path);
/* 111 */     b.append(separator);
/* 112 */     b.append("bin").append(separator).append("java");
/* 113 */     if (CURRENT == WINDOWS)
/* 114 */       b.append("w.exe"); 
/* 115 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static String appendBootstrapperJvm(String path) {
/* 119 */     StringBuilder b = new StringBuilder();
/* 120 */     if (CURRENT == OSX && !path.toLowerCase().endsWith("jre") && !path.toLowerCase().endsWith("home")) {
/* 121 */       b.append("Contents").append(File.separatorChar).append("Home").append(File.separatorChar).append("jre")
/* 122 */         .append(File.separatorChar);
/*     */     }
/* 124 */     return appendToJVM((new File(path, b.toString())).getPath());
/*     */   }
/*     */   
/*     */   public static String appendBootstrapperJvm1(String path) {
/* 128 */     StringBuilder b = new StringBuilder();
/* 129 */     if (CURRENT == OSX && !path.toLowerCase().endsWith("jre") && !path.toLowerCase().endsWith("home")) {
/* 130 */       b.append("jre.bundle").append(File.separatorChar).append("Contents").append(File.separatorChar)
/* 131 */         .append("Home").append(File.separatorChar);
/*     */     }
/* 133 */     return appendToJVM((new File(path, b.toString())).getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String appendBootstrapperJvm2(String path) {
/* 140 */     StringBuilder b = new StringBuilder();
/* 141 */     if (CURRENT == OSX) {
/* 142 */       b.append("Contents").append(File.separatorChar).append("Home").append(File.separatorChar);
/*     */     }
/* 144 */     return appendToJVM((new File(path, b.toString())).getPath());
/*     */   }
/*     */   
/*     */   public static String getSummary() {
/* 148 */     Configuration c = TLauncher.getInstance().getConfiguration();
/* 149 */     String bitMessage = c.get("memory.problem.message");
/*     */     
/* 151 */     String options = Objects.nonNull(System.getenv("_java_options")) ? (System.lineSeparator() + "_java_options " + System.getenv("_java_options")) : "";
/*     */ 
/*     */     
/* 154 */     StringBuilder builder = new StringBuilder();
/* 155 */     builder.append("-------------------------------------------------------").append(System.lineSeparator());
/* 156 */     builder.append(NAME).append(" ").append(VERSION).append(", Java").append(" ")
/* 157 */       .append(System.getProperty("java.version")).append(", jvm bit ").append(getJavaBit()).append(", ")
/* 158 */       .append(Arch.TOTAL_RAM_MB).append(" MB RAM");
/* 159 */     if (bitMessage != null && Arch.CURRENT == Arch.x32) {
/* 160 */       builder.append(", ").append(bitMessage);
/*     */     }
/* 162 */     builder.append(System.lineSeparator());
/* 163 */     builder.append("java path=").append(getJavaPathByHome(true));
/* 164 */     builder.append(options);
/* 165 */     String processInfo = c.get("process.info");
/* 166 */     if (Objects.nonNull(processInfo))
/* 167 */       builder.append(System.lineSeparator()).append(processInfo); 
/* 168 */     String gpu = c.get("gpu.info.full");
/* 169 */     if (Objects.nonNull(gpu))
/* 170 */       builder.append(System.lineSeparator()).append(gpu); 
/* 171 */     builder.append(System.lineSeparator()).append("-------------------------------------------------------");
/* 172 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Arch getJavaBit() {
/* 176 */     String res = System.getProperty("sun.arch.data.model");
/* 177 */     if (res != null && res.equalsIgnoreCase("64"))
/* 178 */       return Arch.x64; 
/* 179 */     return Arch.x32;
/*     */   }
/*     */   
/*     */   private static void rawOpenLink(URI uri) throws Throwable {
/* 183 */     if (!Desktop.isDesktopSupported()) {
/* 184 */       log(new Object[] { "Your system doesnt'have a Desktop object" });
/*     */       return;
/*     */     } 
/* 187 */     Desktop.getDesktop().browse(uri);
/*     */   }
/*     */   
/*     */   public static boolean openLink(URI uri, boolean alertError) {
/* 191 */     log(new Object[] { "Trying to open link with default browser:", uri });
/*     */     
/*     */     try {
/* 194 */       if (is(new OS[] { LINUX })) {
/* 195 */         Runtime.getRuntime().exec("gnome-open " + uri);
/* 196 */         return true;
/*     */       } 
/* 198 */       if (!Desktop.isDesktopSupported()) {
/* 199 */         log(new Object[] { "Your system doesnt'have a Desktop object" });
/* 200 */         return false;
/*     */       } 
/* 202 */       Desktop.getDesktop().browse(uri);
/* 203 */     } catch (Throwable e) {
/*     */       try {
/* 205 */         showDocument(uri.toString());
/* 206 */         return true;
/* 207 */       } catch (Throwable t) {
/* 208 */         log(new Object[] { "Failed to open link with default browser:", uri, t });
/*     */         
/* 210 */         if (alertError) {
/* 211 */           SwingUtilities.invokeLater(() -> Alert.showLocError("ui.error.openlink", uri));
/*     */         }
/* 213 */         return false;
/*     */       } 
/* 215 */     }  return true;
/*     */   }
/*     */   
/*     */   public static boolean openLink(URI uri) {
/* 219 */     return openLink(uri, true);
/*     */   }
/*     */   
/*     */   public static boolean openLink(URL url, boolean alertError) {
/* 223 */     log(new Object[] { "Trying to open URL with default browser:", url });
/*     */     
/* 225 */     URI uri = null;
/*     */     
/*     */     try {
/* 228 */       uri = url.toURI();
/* 229 */     } catch (URISyntaxException e) {
/* 230 */       U.log(new Object[] { "error", e });
/*     */     } 
/* 232 */     return openLink(uri, alertError);
/*     */   }
/*     */   
/*     */   public static boolean openLink(URL url) {
/* 236 */     return openLink(url, true);
/*     */   }
/*     */   
/*     */   public static boolean openLink(String url) {
/*     */     try {
/* 241 */       return openLink(new URI(url), true);
/* 242 */     } catch (URISyntaxException e) {
/* 243 */       U.log(new Object[] { e });
/* 244 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void openPath(File path, boolean appendSeparator) throws Throwable {
/* 249 */     String cmdArr[], cmd, absPath = path.getAbsolutePath() + File.separatorChar;
/* 250 */     Runtime r = Runtime.getRuntime();
/* 251 */     Throwable t = null;
/*     */     
/* 253 */     switch (CURRENT) {
/*     */       case x64:
/* 255 */         cmdArr = new String[] { "/usr/bin/open", absPath };
/*     */         
/*     */         try {
/* 258 */           r.exec(cmdArr);
/*     */           return;
/* 260 */         } catch (Throwable e) {
/* 261 */           t = e;
/* 262 */           log(new Object[] { "Cannot open folder using:\n", cmdArr, e });
/*     */           break;
/*     */         } 
/*     */       case x32:
/* 266 */         cmd = String.format("cmd.exe /C start \"Open path\" \"%s\"", new Object[] { absPath });
/*     */         
/*     */         try {
/* 269 */           r.exec(cmd);
/*     */           return;
/* 271 */         } catch (Throwable e) {
/* 272 */           t = e;
/*     */           
/* 274 */           log(new Object[] { "Cannot open folder using CMD.exe:\n", cmd, e });
/*     */           break;
/*     */         } 
/*     */       case null:
/* 278 */         if (Desktop.isDesktopSupported()) {
/* 279 */           Desktop.getDesktop().open(new File(absPath));
/*     */           return;
/*     */         } 
/*     */       default:
/* 283 */         log(new Object[] { "... will use desktop" });
/*     */         break;
/*     */     } 
/*     */     
/*     */     try {
/* 288 */       rawOpenLink(path.toURI());
/* 289 */     } catch (Throwable e) {
/* 290 */       t = e;
/*     */     } 
/*     */     
/* 293 */     if (t == null)
/*     */       return; 
/* 295 */     throw t;
/*     */   }
/*     */   
/*     */   public static boolean openFolder(File folder, boolean alertError) {
/* 299 */     log(new Object[] { "Trying to open folder:", folder });
/*     */     
/* 301 */     if (!folder.isDirectory()) {
/* 302 */       log(new Object[] { "This path is not a directory, sorry." });
/* 303 */       return false;
/*     */     } 
/*     */     
/*     */     try {
/* 307 */       openPath(folder, true);
/* 308 */     } catch (Throwable e) {
/* 309 */       log(new Object[] { "Failed to open folder:", e });
/*     */       
/* 311 */       if (alertError) {
/* 312 */         Alert.showLocError("ui.error.openfolder", folder);
/*     */       }
/* 314 */       return false;
/*     */     } 
/*     */     
/* 317 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean openFolder(File folder) {
/* 321 */     return openFolder(folder, true);
/*     */   }
/*     */   
/*     */   public static boolean openFile(File file, boolean alertError) {
/* 325 */     log(new Object[] { "Trying to open file:", file });
/*     */     
/* 327 */     if (!file.isFile()) {
/* 328 */       log(new Object[] { "This path is not a file, sorry." });
/* 329 */       return false;
/*     */     } 
/*     */     
/*     */     try {
/* 333 */       openPath(file, false);
/* 334 */     } catch (Throwable e) {
/* 335 */       log(new Object[] { "Failed to open file:", e });
/*     */       
/* 337 */       if (alertError) {
/* 338 */         Alert.showLocError("ui.error.openfolder", file);
/*     */       }
/* 340 */       return false;
/*     */     } 
/*     */     
/* 343 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean openFile(File file) {
/* 347 */     return openFile(file, true);
/*     */   }
/*     */   
/*     */   protected static void log(Object... o) {
/* 351 */     U.log(new Object[] { "[OS]", o });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getJavaNumber() {
/* 358 */     if (String.valueOf(JAVA_VERSION).startsWith("10"))
/* 359 */       return "10"; 
/* 360 */     if (String.valueOf(JAVA_VERSION).startsWith("11"))
/* 361 */       return "11"; 
/* 362 */     if (JAVA_VERSION > 2.0D) {
/* 363 */       return String.valueOf(JAVA_VERSION).substring(0, 1);
/*     */     }
/* 365 */     return String.valueOf(JAVA_VERSION).substring(2, 3);
/*     */   }
/*     */   
/*     */   public static void fillSystemInfo() {
/*     */     try {
/* 370 */       if (getCurrent() == WINDOWS) {
/* 371 */         Process p = Runtime.getRuntime().exec("cmd.exe /C chcp 437 & systeminfo");
/* 372 */         p.waitFor(30L, TimeUnit.SECONDS);
/* 373 */         String res = FileUtil.readStream(p.getInputStream(), "cp866");
/*     */         
/* 375 */         String[] array = res.split("\r\n");
/* 376 */         for (int i = 0; i < array.length; i++) {
/* 377 */           String r = array[i];
/* 378 */           if (!r.isEmpty()) {
/*     */             
/* 380 */             int first = r.indexOf(":");
/* 381 */             if (first > 0) {
/* 382 */               U.debug(new Object[] { r });
/* 383 */               if (r.substring(0, first).equalsIgnoreCase("Processor(s)"))
/* 384 */               { systemInfo.put(r.substring(0, first), r.substring(first + 1) + array[++i]); }
/*     */               else
/*     */               
/* 387 */               { systemInfo.put(r.substring(0, first), r.substring(first + 1)); } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 392 */     } catch (Throwable e) {
/* 393 */       U.log(new Object[] { e });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String executeByTerminal(String command, int time) {
/* 398 */     String res = "";
/*     */     try {
/* 400 */       if (getCurrent() == WINDOWS) {
/* 401 */         Process p = Runtime.getRuntime().exec("cmd.exe /C chcp 437 & " + command);
/* 402 */         p.waitFor(time, TimeUnit.SECONDS);
/* 403 */         res = FileUtil.readStream(p.getInputStream(), "IBM437");
/* 404 */       } else if (is(new OS[] { LINUX })) {
/* 405 */         Process p = Runtime.getRuntime().exec(command);
/* 406 */         p.waitFor(time, TimeUnit.SECONDS);
/* 407 */         res = FileUtil.readStream(p.getInputStream());
/* 408 */       } else if (is(new OS[] { OSX })) {
/* 409 */         Process p = Runtime.getRuntime().exec(command);
/* 410 */         p.waitFor(time, TimeUnit.SECONDS);
/* 411 */         res = FileUtil.readStream(p.getInputStream());
/*     */       }
/*     */     
/* 414 */     } catch (Throwable e) {
/* 415 */       log(new Object[] { e });
/*     */     } 
/* 417 */     return res;
/*     */   }
/*     */   
/*     */   public static String executeByTerminal(String command) {
/* 421 */     return executeByTerminal(command, 30);
/*     */   }
/*     */   
/*     */   public static String getSystemInfo(String key) {
/* 425 */     return systemInfo.get(key);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 429 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isUnsupported() {
/* 433 */     return (this == UNKNOWN);
/*     */   }
/*     */   
/*     */   public boolean isCurrent() {
/* 437 */     return (this == CURRENT);
/*     */   }
/*     */   
/*     */   private static void showDocument(String var1) {
/*     */     try {
/* 442 */       if (is(new OS[] { OSX })) {
/* 443 */         Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", new Class[] { String.class }).invoke(null, new Object[] { var1 });
/*     */       }
/* 445 */       else if (is(new OS[] { WINDOWS })) {
/* 446 */         Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + var1);
/*     */       } else {
/* 448 */         String var3 = null;
/* 449 */         String[] var4 = browsers;
/* 450 */         int var5 = var4.length;
/*     */         
/* 452 */         for (int var6 = 0; var6 < var5; var6++) {
/* 453 */           String var7 = var4[var6];
/* 454 */           if (var3 == null && Runtime.getRuntime().exec(new String[] { "which", var7 }).getInputStream()
/* 455 */             .read() != -1) {
/* 456 */             Runtime var10000 = Runtime.getRuntime();
/* 457 */             String[] var10001 = new String[2];
/* 458 */             var3 = var7;
/* 459 */             var10001[0] = var7;
/* 460 */             var10001[1] = var1;
/* 461 */             var10000.exec(var10001);
/*     */           } 
/*     */         } 
/*     */         
/* 465 */         if (var3 == null) {
/* 466 */           throw new Exception("No web browser found");
/*     */         }
/*     */       } 
/* 469 */     } catch (Exception var8) {
/* 470 */       var8.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum Arch {
/* 475 */     x32, x64, UNKNOWN; private final int asInt; private final String asString; public static final int PREFERRED_MEMORY; public static final int MAX_MEMORY; public static final int MIN_MEMORY = 512; public static final int TOTAL_RAM_MB;
/*     */     public static final long TOTAL_RAM;
/* 477 */     public static final Arch CURRENT = getCurrent();
/*     */     static {
/* 479 */       TOTAL_RAM = getTotalRam();
/* 480 */       TOTAL_RAM_MB = (int)(TOTAL_RAM / 1024L / 1024L);
/*     */       
/* 482 */       MAX_MEMORY = getMaximumMemory();
/* 483 */       PREFERRED_MEMORY = getPreferWrapper();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Arch() {
/* 489 */       this.asString = toString().substring(1);
/*     */       
/* 491 */       int asInt_temp = 0;
/*     */       try {
/* 493 */         asInt_temp = Integer.parseInt(this.asString);
/* 494 */       } catch (RuntimeException runtimeException) {}
/*     */ 
/*     */       
/* 497 */       this.asInt = asInt_temp;
/*     */     }
/*     */     
/*     */     private static Arch getCurrent() {
/* 501 */       String curArch = System.getProperty("sun.arch.data.model");
/*     */       
/* 503 */       for (Arch arch : values()) {
/* 504 */         if (arch.asString.equals(curArch))
/* 505 */           return arch; 
/*     */       } 
/* 507 */       return UNKNOWN;
/*     */     }
/*     */     
/*     */     private static long getTotalRam() {
/* 511 */       long m = 0L;
/*     */       
/*     */       try {
/* 514 */         m = ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
/* 515 */       } catch (Throwable e) {
/* 516 */         U.log(new Object[] { "[ERROR] Cannot allocate total physical memory size!", e });
/*     */       } finally {
/* 518 */         if (m == 0L)
/* 519 */           m = 10000000000L; 
/*     */       } 
/* 521 */       return m;
/*     */     }
/*     */     
/*     */     private static int getPreferWrapper() {
/* 525 */       switch (CURRENT) {
/*     */         case x64:
/* 527 */           if (TOTAL_RAM_MB > 6000)
/* 528 */             return 3000; 
/* 529 */           if (TOTAL_RAM_MB > 3000)
/* 530 */             return TOTAL_RAM_MB - 1024; 
/* 531 */           if (TOTAL_RAM_MB > 2000)
/* 532 */             return TOTAL_RAM_MB - 512; 
/* 533 */           return TOTAL_RAM_MB;
/*     */         case x32:
/* 535 */           if (TOTAL_RAM_MB > 1500)
/* 536 */             return 1500; 
/* 537 */           if (TOTAL_RAM_MB > 1024)
/* 538 */             return 750;  break;
/*     */       } 
/* 540 */       return 512;
/*     */     }
/*     */     
/*     */     private static int getMaximumMemory() {
/* 544 */       switch (CURRENT) {
/*     */         case x64:
/* 546 */           return TOTAL_RAM_MB;
/*     */         case x32:
/* 548 */           if (TOTAL_RAM_MB > 1500)
/* 549 */             return 1024;  break;
/*     */       } 
/* 551 */       return 512;
/*     */     }
/*     */ 
/*     */     
/*     */     public String asString() {
/* 556 */       return (this == UNKNOWN) ? toString() : this.asString;
/*     */     }
/*     */     
/*     */     public int asInteger() {
/* 560 */       return this.asInt;
/*     */     }
/*     */     
/*     */     public boolean isCurrent() {
/* 564 */       return (this == CURRENT);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static String buildJVMKey() {
/* 570 */     StringBuilder b = new StringBuilder();
/* 571 */     switch (CURRENT) {
/*     */       case x32:
/*     */       case null:
/* 574 */         b.append(CURRENT.name);
/*     */         break;
/*     */       case x64:
/* 577 */         b.append("mac-os");
/*     */         break;
/*     */     } 
/*     */     
/* 581 */     switch (Arch.CURRENT) {
/*     */       case x32:
/* 583 */         if (CURRENT.equals(LINUX)) {
/* 584 */           b.append("-i386"); break;
/* 585 */         }  if (CURRENT.equals(WINDOWS))
/* 586 */           b.append("-x86"); 
/*     */         break;
/*     */       case x64:
/* 589 */         if (CURRENT.equals(WINDOWS)) {
/* 590 */           b.append("-x64");
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 595 */     return b.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Path buildJAVAFolder() {
/* 600 */     Path p = null;
/* 601 */     switch (CURRENT) {
/*     */       case x32:
/* 603 */         switch (Arch.CURRENT) {
/*     */           case x32:
/* 605 */             p = Paths.get("C:\\Program Files (x86)\\Java", new String[0]);
/*     */             break;
/*     */           case x64:
/* 608 */             p = Paths.get("C:\\Program Files\\Java", new String[0]);
/*     */             break;
/*     */         } 
/*     */         break;
/*     */       case null:
/* 613 */         p = Paths.get("/usr/lib/jvm", new String[0]);
/*     */         break;
/*     */       case x64:
/* 616 */         p = Paths.get("/Library/Java/JavaVirtualMachines", new String[0]);
/*     */         break;
/*     */     } 
/* 619 */     if (Files.notExists(p, new java.nio.file.LinkOption[0]) || Objects.isNull(p)) {
/* 620 */       return Paths.get(".", new String[0]);
/*     */     }
/* 622 */     return p;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\OS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */