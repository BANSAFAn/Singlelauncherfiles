/*     */ package org.tlauncher.tlauncher.rmo;
/*     */ import com.google.gson.Gson;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.AccessDeniedException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.UIManager;
/*     */ import net.minecraft.launcher.process.JavaProcess;
/*     */ import net.minecraft.launcher.process.JavaProcessLauncher;
/*     */ import org.apache.log4j.LogManager;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.configuration.LangConfiguration;
/*     */ import org.tlauncher.tlauncher.configuration.SimpleConfiguration;
/*     */ import org.tlauncher.tlauncher.downloader.Downloader;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.PreparedEnvironmentComponent;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.PreparedEnvironmentComponentImpl;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedBootInfo;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.model.JavaConfig;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.model.LibraryConfig;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.view.DownloadingFrameElement;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public final class Bootstrapper {
/*     */   private static final String linkUpdateErrorRu = "https://tlauncher.org/ru/error-kb4515384.html";
/*  39 */   public static final File directory = new File("."); private static final String linkUpdateErrorEn = "https://tlauncher.org/en/error-kb4515384.html";
/*     */   private static final String msiAfterBurnerRu = "https://tlauncher.org/ru/crash-afterburner.html";
/*     */   private static final String msiAfterBurnerEn = "https://tlauncher.org/en/crash-afterburner.html";
/*     */   public static SimpleConfiguration innerConfig;
/*     */   public static LangConfiguration langConfiguration;
/*     */   private static SimpleConfiguration launcherConfig;
/*     */   private static JavaConfig javaConfig;
/*     */   private static LibraryConfig libraryConfig;
/*     */   private final BootstrapperListener listener;
/*     */   private JavaProcessLauncher processLauncher;
/*     */   private static final String PROTECTION = "protection.txt";
/*     */   private JavaProcess process;
/*     */   private boolean started;
/*     */   private PreparedEnvironmentComponent preparedEnvironmentComponent;
/*     */   private File jvmFolder;
/*     */   private final String[] args;
/*     */   private FileLock lock;
/*  56 */   private static int i = 0;
/*     */   
/*     */   public Bootstrapper(String[] args) {
/*  59 */     this.args = args;
/*  60 */     this.listener = new BootstrapperListener();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  67 */     File jvmFolder = null;
/*     */     try {
/*  69 */       System.setProperty("java.net.preferIPv4Stack", "true");
/*  70 */       initConfig();
/*  71 */       U.initializeLoggerU(MinecraftUtil.getWorkingDirectory(launcherConfig.get("minecraft.gamedir")), "boot");
/*  72 */       U.log(new Object[] { "" });
/*  73 */       U.log(new Object[] { "-------------------------------------------------------------------" });
/*  74 */       Class.forName("org.apache.log4j.helpers.NullEnumeration");
/*     */       try {
/*  76 */         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*  77 */       } catch (ExceptionInInitializerError error) {
/*  78 */         if (error.getCause() instanceof IllegalArgumentException && 
/*  79 */           error.getCause().getMessage().contains("Text-specific LCD")) {
/*  80 */           String link = "https://tlauncher.org/ru/font-error.html";
/*  81 */           boolean ussr = launcherConfig.isUSSRLocale();
/*  82 */           if (!ussr)
/*  83 */             link = "https://tlauncher.org/en/font-error.html"; 
/*  84 */           OS.openLink(link);
/*  85 */           System.exit(-1);
/*     */         }
/*     */       
/*  88 */       } catch (Throwable ex) {
/*  89 */         U.log(new Object[] { ex });
/*     */       } 
/*     */       
/*  92 */       if (!testDoubleRunning()) {
/*  93 */         System.exit(0);
/*     */       }
/*  95 */       if (!checkFreeSpace(FileUtil.SIZE_200.longValue())) {
/*  96 */         showDiskProblem();
/*     */       }
/*  98 */       createAndValidateWorkDir();
/*  99 */       validateTempDir();
/* 100 */       valitdateKB4515384();
/*     */       
/* 102 */       boolean properJRE = false;
/* 103 */       if (launcherConfig.getBoolean("not.work.jfxwebkit.dll") || launcherConfig
/* 104 */         .getBoolean("fixed.gpu.jre.error"))
/* 105 */         properJRE = true; 
/* 106 */       File jvm = getJVM(properJRE);
/* 107 */       jvmFolder = jvm.getParentFile();
/* 108 */       Bootstrapper bootstrapper = new Bootstrapper(args);
/* 109 */       bootstrapper.activeDoublePreparingJVM();
/* 110 */       Downloader downloader = new Downloader(ConnectionQuality.NORMAL);
/* 111 */       DownloadingFrameElement downloadingBarElement = new DownloadingFrameElement(langConfiguration);
/*     */       
/* 113 */       PreparedEnvironmentComponentImpl preparedEnvironmentComponentImpl = new PreparedEnvironmentComponentImpl(libraryConfig, javaConfig, getWorkFolder(), jvmFolder, downloader);
/*     */       
/* 115 */       downloader.addListener((DownloaderListener)downloadingBarElement);
/* 116 */       DownloadedBootInfo info = preparedEnvironmentComponentImpl.validateLibraryAndJava();
/* 117 */       preparedEnvironmentComponentImpl.preparedLibrariesAndJava(info);
/*     */ 
/*     */       
/* 120 */       bootstrapper.setPreparedEnvironmentComponent((PreparedEnvironmentComponent)preparedEnvironmentComponentImpl);
/* 121 */       bootstrapper.setJVMFolder(jvm);
/* 122 */       bootstrapper.diactivateDoublePreparingJVM();
/* 123 */       bootstrapper.start();
/* 124 */     } catch (AccessDeniedException e) {
/* 125 */       Alert.showErrorHtml("", langConfiguration.get("alert.access.denied.message", new Object[] { e
/* 126 */               .getFile(), e.getOtherFile() }));
/* 127 */       TLauncher.kill();
/* 128 */     } catch (Throwable e) {
/* 129 */       if (e instanceof IOException && Objects.nonNull(jvmFolder) && jvmFolder
/* 130 */         .toString().contains("jvms")) {
/* 131 */         fixedOnce(args, jvmFolder);
/* 132 */       } else if (e instanceof java.nio.charset.UnsupportedCharsetException) {
/* 133 */         Alert.showErrorHtml("not proper UnsupportedCharsetException", langConfiguration
/* 134 */             .get("not proper UnsupportedCharsetException"));
/* 135 */         TLauncher.kill();
/*     */       } 
/* 137 */       e.printStackTrace();
/* 138 */       String message = e.getMessage();
/* 139 */       if (Objects.nonNull(message) && message.contains("GetIpAddrTable")) {
/* 140 */         Alert.showErrorHtml("", langConfiguration.get("addr.table.error"));
/* 141 */         TLauncher.kill();
/*     */       } 
/* 143 */       U.log(new Object[] { "problem with preparing boostrapper" });
/* 144 */       TlauncherUtil.showCriticalProblem(e);
/* 145 */       TLauncher.kill();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean testDoubleRunning() throws InterruptedException {
/* 150 */     int value = TlauncherUtil.checkDoubleRunning();
/* 151 */     if (value > 0) {
/* 152 */       showWarningMessage(langConfiguration.get("double.running.title"), String.format(langConfiguration.get("double.running"), new Object[] { Integer.valueOf(value) }));
/* 153 */       return false;
/*     */     } 
/* 155 */     TlauncherUtil.createTimeStart();
/* 156 */     return true;
/*     */   }
/*     */   
/*     */   private static void valitdateKB4515384() {
/* 160 */     if (launcherConfig.getBoolean("block.updater.message")) {
/*     */       return;
/*     */     }
/* 163 */     if (!launcherConfig.getBoolean("retest.update")) {
/* 164 */       launcherConfig.set("retest.update", Boolean.valueOf(true), true);
/*     */       return;
/*     */     } 
/* 167 */     findNotProperUpdater();
/*     */   }
/*     */   
/*     */   private static boolean findNotProperUpdater() {
/* 171 */     if (OS.is(new OS[] { OS.WINDOWS })) {
/*     */       
/* 173 */       boolean KB4515384Exists = OS.executeByTerminal("wmic qfe get HotFixID", 5).contains("KB4515384");
/* 174 */       if (KB4515384Exists) {
/* 175 */         showUpdateWinError();
/* 176 */         Alert.showErrorHtml(langConfiguration.get("warning.KB4515384.problem"), 500);
/*     */       } 
/* 178 */       return KB4515384Exists;
/*     */     } 
/* 180 */     return false;
/*     */   }
/*     */   
/*     */   private static void validateTempDir() throws IOException {
/*     */     try {
/* 185 */       Files.createTempFile("test", "txt", (FileAttribute<?>[])new FileAttribute[0]);
/* 186 */     } catch (IOException e) {
/*     */       try {
/* 188 */         if (Objects.nonNull(System.getProperty("java.io.tmpdir"))) {
/* 189 */           Path folder = Paths.get(System.getProperty("java.io.tmpdir"), new String[0]);
/* 190 */           if (Files.isRegularFile(folder, new java.nio.file.LinkOption[0]))
/* 191 */             Files.delete(folder); 
/* 192 */           if (!Files.exists(folder, new java.nio.file.LinkOption[0]))
/* 193 */             FileUtil.createFolder(folder.toFile()); 
/*     */         } 
/* 195 */       } catch (IOException e1) {
/* 196 */         if (e1.getMessage().contains("createScrollWrapper")) {
/* 197 */           Alert.showWarning("", langConfiguration.get("temp.dir.error"));
/* 198 */           System.exit(-1);
/*     */         } 
/* 200 */         throw e1;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void fixedOnce(String[] args, File jvmFolder) {
/* 206 */     if (i != 0)
/*     */       return; 
/* 208 */     i++;
/* 209 */     FileUtil.deleteDirectory(jvmFolder);
/* 210 */     Alert.showErrorHtml("", langConfiguration.get("run.again.launcher"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void createAndValidateWorkDir() throws AccessDeniedException {
/* 216 */     File workDir = getWorkFolder();
/* 217 */     if (workDir.exists()) {
/*     */ 
/*     */       
/* 220 */       if (!Files.isWritable(workDir.toPath()) || !Files.isReadable(workDir.toPath())) {
/* 221 */         int random_number1 = 1 + (int)(Math.random() * 100.0D);
/* 222 */         String workDirectory = String.valueOf(workDir) + random_number1;
/* 223 */         File correctFolder = new File(workDirectory);
/* 224 */         launcherConfig.set("minecraft.gamedir", correctFolder);
/* 225 */         throw new AccessDeniedException(String.valueOf(workDir), String.valueOf(correctFolder), "");
/*     */       } 
/*     */     } else {
/* 228 */       File file = new File(String.valueOf(workDir));
/* 229 */       if (!file.mkdir()) {
/* 230 */         launcherConfig.set("minecraft.gamedir", null);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static File getJVM(boolean properJVM) {
/* 237 */     File tlauncherFolder = MinecraftUtil.getSystemRelatedDirectory("tlauncher");
/* 238 */     return TlauncherUtil.getJVMFolder(javaConfig, tlauncherFolder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void showDiskProblem() {
/* 246 */     String minecraftGamedir = launcherConfig.get("minecraft.gamedir");
/*     */     
/* 248 */     File minecraftFolder = (minecraftGamedir == null) ? MinecraftUtil.getSystemRelatedDirectory(innerConfig.get("folder")) : new File(minecraftGamedir);
/*     */ 
/*     */     
/* 251 */     String url = launcherConfig.isUSSRLocale() ? "http://www.inetkomp.ru/uroki/488-osvobodit-mesto-na-diske-c.html" : "https://www.windowscentral.com/best-7-ways-free-hard-drive-space-windows-10";
/*     */ 
/*     */     
/* 254 */     String path = minecraftFolder.toPath().getRoot().toString();
/*     */     
/* 256 */     String message = langConfiguration.get("place.disk.warning", new Object[] { path }) + "<br><br>" + langConfiguration.get("alert.start.message", new Object[] { url });
/* 257 */     U.log(new Object[] { message });
/* 258 */     TlauncherUtil.showCriticalProblem(message);
/* 259 */     TLauncher.kill();
/*     */   }
/*     */   
/*     */   public static JavaProcessLauncher restartLauncher() {
/* 263 */     initConfig();
/* 264 */     File directory = new File(".");
/* 265 */     String path = OS.getJavaPathByHome(true);
/* 266 */     JavaProcessLauncher processLauncher = new JavaProcessLauncher(path, new String[0]);
/* 267 */     log(new Object[] { "choose jvm for restart:" + path });
/* 268 */     String classPath = FileUtil.getRunningJar().getPath();
/*     */     
/* 270 */     processLauncher.directory(directory);
/* 271 */     processLauncher.addCommand("-cp");
/* 272 */     processLauncher.addCommand(classPath + System.getProperty("path.separator"));
/* 273 */     processLauncher.addCommand(innerConfig.get("bootstrapper.class"));
/* 274 */     U.debug(new Object[] { processLauncher });
/* 275 */     return processLauncher;
/*     */   }
/*     */ 
/*     */   
/*     */   private static File getWorkFolder() {
/* 280 */     String minecraftGamedir = launcherConfig.get("minecraft.gamedir");
/* 281 */     return (minecraftGamedir == null) ? 
/* 282 */       MinecraftUtil.getSystemRelatedDirectory(innerConfig.get("folder")) : new File(minecraftGamedir);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initConfig() {
/* 287 */     Gson g = new Gson();
/*     */     
/*     */     try {
/*     */       try {
/* 291 */         innerConfig = new SimpleConfiguration(Bootstrapper.class.getResourceAsStream("/inner.tlauncher.properties"));
/* 292 */       } catch (NullPointerException ex) {
/* 293 */         String path = FileUtil.getRunningJar().toString();
/* 294 */         if (path.contains("!" + File.separator)) {
/* 295 */           Alert.showError("Error", String.format("Java can't work with path that contains symbol '!', create new local user without characters '!'(use new local user for game) and use path to TLauncher without '!' characters \r\ncurrent: %s\r\n\r\nДжава не работает c путями в которых содержится восклицательный знак '!' , создайте новую учетную запись без '!' знаков(используйте её для игры) и используйте путь к файлу TLauncher без '!'\r\n текущий: %s", new Object[] { path, path }));
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 300 */           System.exit(-2);
/*     */         } 
/*     */       } 
/*     */       
/* 304 */       launcherConfig = new SimpleConfiguration(new File(MinecraftUtil.getSystemRelatedDirectory(innerConfig.get("settings.new")).getCanonicalPath()));
/* 305 */       String locale = launcherConfig.get("locale");
/* 306 */       Locale locale2 = Locale.getDefault();
/* 307 */       if (locale != null) {
/* 308 */         locale2 = Configuration.getLocaleOf(locale);
/*     */       }
/* 310 */       List<Locale> listLocales = Configuration.getDefaultLocales(innerConfig);
/* 311 */       Locale selected = Configuration.findSuitableLocale(locale2, listLocales);
/*     */       
/* 313 */       langConfiguration = new LangConfiguration(listLocales.<Locale>toArray(new Locale[0]), selected, innerConfig.get("bootstrapper.language.folder"));
/*     */       
/* 315 */       libraryConfig = (LibraryConfig)g.fromJson(new InputStreamReader(Bootstrapper.class.getResourceAsStream("/bootstrapper.libraries.json"), StandardCharsets.UTF_8), LibraryConfig.class);
/*     */       
/* 317 */       String fileName = "/bootstrapper.jre.json";
/* 318 */       javaConfig = (JavaConfig)g.fromJson(new InputStreamReader(Bootstrapper.class.getResourceAsStream(fileName), StandardCharsets.UTF_8), JavaConfig.class);
/*     */     }
/* 320 */     catch (Throwable e1) {
/* 321 */       TlauncherUtil.showCriticalProblem(e1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int showQuestion(String title, String messae, String button, String button2) {
/* 326 */     return JOptionPane.showOptionDialog(null, messae, title, 0, 2, null, new Object[] { button, button2 }, button2);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void showWarningMessage(String title, String message) {
/* 331 */     JOptionPane.showOptionDialog(null, message, title, 1, 1, null, new Object[] { "ok"
/* 332 */         }, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   private static boolean checkFreeSpace(long size) {
/* 336 */     String minecraftGamedir = launcherConfig.get("minecraft.gamedir");
/*     */     
/* 338 */     File minecraftFolder = (minecraftGamedir == null) ? MinecraftUtil.getSystemRelatedDirectory(innerConfig.get("folder")) : new File(minecraftGamedir);
/*     */     
/* 340 */     return FileUtil.checkFreeSpace(minecraftFolder, size);
/*     */   }
/*     */   
/*     */   private static void log(Object... s) {
/* 344 */     U.log(new Object[] { "[Bootstrapper]", s });
/*     */   }
/*     */   
/*     */   private JavaProcessLauncher createLauncher(String[] args) {
/* 348 */     log(new Object[] { "createLauncher" });
/* 349 */     String jvm = OS.appendBootstrapperJvm(this.jvmFolder.getPath());
/* 350 */     log(new Object[] { "choose jvm:" + jvm });
/* 351 */     JavaProcessLauncher processLauncher = new JavaProcessLauncher(jvm, new String[0]);
/*     */     
/* 353 */     processLauncher.directory(directory);
/* 354 */     processLauncher.addCommand("-Xmx" + innerConfig.get("max.memory") + "m");
/* 355 */     processLauncher.addCommand("-Dfile.encoding=UTF8");
/*     */     
/* 357 */     String classPath = FileUtil.getRunningJar().getPath();
/*     */ 
/*     */     
/* 360 */     String separator = File.pathSeparator;
/* 361 */     log(new Object[] { "validate files" });
/*     */ 
/*     */     
/*     */     try {
/* 365 */       classPath = classPath + separator;
/* 366 */       classPath = classPath + StringUtil.convertListToString(separator, this.preparedEnvironmentComponent.getLibrariesForRunning());
/* 367 */     } catch (Throwable e) {
/* 368 */       U.log(new Object[] { e });
/* 369 */       StringBuilder builder = new StringBuilder(langConfiguration.get("updater.download.fail", new Object[] { langConfiguration
/* 370 */               .get("java.reinstall") }));
/* 371 */       builder.append("<br>");
/* 372 */       builder = new StringBuilder(builder.toString().replace("- problem1", ""));
/* 373 */       TlauncherUtil.showCriticalProblem(builder.toString());
/* 374 */       System.exit(-1);
/*     */     } 
/* 376 */     log(new Object[] { "end validated files" });
/* 377 */     processLauncher.addCommand("-cp", classPath);
/* 378 */     processLauncher.addCommand(innerConfig.get("main.class"));
/*     */     
/* 380 */     if (args != null && args.length > 0) {
/* 381 */       processLauncher.addCommands((Object[])args);
/*     */     }
/* 383 */     return processLauncher;
/*     */   }
/*     */   
/*     */   private void die(int status) {
/* 387 */     log(new Object[] { "I can be terminated now: " + status });
/*     */     
/* 389 */     if (!this.started && this.process.isRunning()) {
/* 390 */       log(new Object[] { "...started instance also will be terminated." });
/* 391 */       this.process.stop();
/*     */     } 
/*     */     
/* 394 */     LogManager.shutdown();
/* 395 */     System.exit(status);
/*     */   }
/*     */   
/*     */   public void start() throws IOException {
/* 399 */     this.processLauncher = createLauncher(this.args);
/* 400 */     log(new Object[] { "Starting launcher..." });
/* 401 */     this.processLauncher.setListener(this.listener);
/*     */     
/* 403 */     TlauncherUtil.clearTimeLabel();
/* 404 */     this.process = this.processLauncher.start();
/*     */   }
/*     */   
/*     */   private void setPreparedEnvironmentComponent(PreparedEnvironmentComponent preparedEnvironmentComponent) {
/* 408 */     this.preparedEnvironmentComponent = preparedEnvironmentComponent;
/*     */   }
/*     */   
/*     */   private void setJVMFolder(File jvmFolder) {
/* 412 */     this.jvmFolder = jvmFolder;
/*     */   }
/*     */   
/*     */   public enum LoadingStep {
/* 416 */     INITIALIZING(21), LOADING_CONFIGURATION(35), LOADING_CONSOLE(41), LOADING_MANAGERS(51), LOADING_WINDOW(62),
/* 417 */     PREPARING_MAINPANE(77), POSTINIT_GUI(82), REFRESHING_INFO(91), SUCCESS(100);
/*     */     
/*     */     public static final String LOADING_PREFIX = "[Loading]";
/*     */     
/*     */     private final int percentage;
/*     */     
/*     */     LoadingStep(int percentage) {
/* 424 */       this.percentage = percentage;
/*     */     }
/*     */     
/*     */     public int getPercentage() {
/* 428 */       return this.percentage;
/*     */     }
/*     */   }
/*     */   
/*     */   private void activeDoublePreparingJVM() throws IOException {
/* 433 */     File tlauncherFolder = MinecraftUtil.getSystemRelatedDirectory("tlauncher");
/* 434 */     File f = new File(tlauncherFolder, "protection.txt");
/* 435 */     FileUtil.createFile(f);
/* 436 */     if (f.exists()) {
/* 437 */       FileChannel ch = FileChannel.open(f.toPath(), new OpenOption[] { StandardOpenOption.WRITE, StandardOpenOption.CREATE });
/* 438 */       this.lock = ch.tryLock();
/* 439 */       if (Objects.isNull(this.lock)) {
/* 440 */         LogManager.shutdown();
/* 441 */         System.exit(4);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void diactivateDoublePreparingJVM() throws IOException {
/* 447 */     if (Objects.nonNull(this.lock))
/* 448 */       this.lock.release(); 
/*     */   }
/*     */   
/*     */   private static void showUpdateWinError() {
/* 452 */     boolean ussr = launcherConfig.isUSSRLocale();
/* 453 */     if (ussr)
/* 454 */     { OS.openLink("https://tlauncher.org/ru/error-kb4515384.html"); }
/* 455 */     else { OS.openLink("https://tlauncher.org/en/error-kb4515384.html"); }
/*     */   
/*     */   }
/*     */   
/* 459 */   private class BootstrapperListener implements JavaProcessListener { private final StringBuffer buffer = new StringBuffer();
/*     */ 
/*     */     
/*     */     public void onJavaProcessLog(JavaProcess jp, String line) {
/* 463 */       U.plog(new Object[] { Character.valueOf('>'), line });
/* 464 */       this.buffer.append(line).append('\n');
/*     */       
/* 466 */       if (line.startsWith("[Loading]")) {
/* 467 */         if (line.length() < "[Loading]".length() + 2) {
/* 468 */           Bootstrapper.log(new Object[] { "Cannot parse line: content is empty." });
/*     */           
/*     */           return;
/*     */         } 
/* 472 */         String content = line.substring("[Loading]".length() + 1);
/* 473 */         Bootstrapper.LoadingStep step = (Bootstrapper.LoadingStep)Reflect.parseEnum(Bootstrapper.LoadingStep.class, content);
/* 474 */         if (step == null) {
/* 475 */           Bootstrapper.log(new Object[] { "Cannot parse line: cannot parse step" });
/*     */           
/*     */           return;
/*     */         } 
/* 479 */         if (step.getPercentage() == 100) {
/* 480 */           Bootstrapper.this.started = true;
/* 481 */           Bootstrapper.this.die(0);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onJavaProcessEnded(JavaProcess jp) {
/* 488 */       int exit = jp.getExitCode();
/* 489 */       if (exit == 1 && Bootstrapper.this.jvmFolder.toString().contains("jvms"))
/* 490 */         Bootstrapper.fixedOnce(Bootstrapper.this.args, Bootstrapper.this.jvmFolder); 
/* 491 */       switch (exit) {
/*     */         case -1073740791:
/* 493 */           TlauncherUtil.showCriticalProblem(Bootstrapper.langConfiguration.get("alert.start.message", new Object[] { "https://tlauncher.org/ru/closed-minecraft-1073740791.html" }));
/*     */           
/* 495 */           TlauncherUtil.showCriticalProblem(Bootstrapper.langConfiguration.get("alert.start.message", new Object[] { "https://tlauncher.org/en/closed-minecraft-1073740791.html" }));
/*     */           
/* 497 */           Bootstrapper.this.die(exit);
/*     */           break;
/*     */         case -1073740771:
/* 500 */           if (!Bootstrapper.findNotProperUpdater()) {
/* 501 */             boolean ussr = Bootstrapper.launcherConfig.isUSSRLocale();
/* 502 */             if (ussr)
/* 503 */             { OS.openLink("https://tlauncher.org/ru/crash-afterburner.html"); }
/* 504 */             else { OS.openLink("https://tlauncher.org/en/crash-afterburner.html"); }
/* 505 */              TlauncherUtil.showCriticalProblem(Bootstrapper.langConfiguration.get("msi.after.burner.block"));
/*     */           } 
/* 507 */           Bootstrapper.this.die(exit);
/*     */           break;
/*     */       } 
/* 510 */       if (exit != 0) {
/* 511 */         TlauncherUtil.showCriticalProblem(this.buffer.toString());
/*     */       }
/* 513 */       Bootstrapper.this.die(exit);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onJavaProcessError(JavaProcess jp, Throwable e) {
/* 518 */       Bootstrapper.log(new Object[] { e });
/*     */     }
/*     */     
/*     */     private BootstrapperListener() {} }
/*     */ 
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\rmo\Bootstrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */