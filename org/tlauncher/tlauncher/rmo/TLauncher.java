/*     */ package org.tlauncher.tlauncher.rmo;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.inject.Guice;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Module;
/*     */ import com.google.inject.assistedinject.Assisted;
/*     */ import com.google.inject.name.Named;
/*     */ import com.sothawo.mapjfx.cache.OfflineCache;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.text.DateFormat;
/*     */ import java.time.LocalDate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import joptsimple.OptionSet;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*     */ import org.tlauncher.tlauncher.configuration.ArgumentParser;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.configuration.InnerConfiguration;
/*     */ import org.tlauncher.tlauncher.configuration.LangConfiguration;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
/*     */ import org.tlauncher.tlauncher.configuration.test.environment.JavaBitTestEnvironment;
/*     */ import org.tlauncher.tlauncher.configuration.test.environment.TestEnvironment;
/*     */ import org.tlauncher.tlauncher.downloader.Downloader;
/*     */ import org.tlauncher.tlauncher.downloader.DownloaderListener;
/*     */ import org.tlauncher.tlauncher.entity.server.RemoteServer;
/*     */ import org.tlauncher.tlauncher.handlers.ExceptionHandler;
/*     */ import org.tlauncher.tlauncher.managers.AdditionalAssetsComponent;
/*     */ import org.tlauncher.tlauncher.managers.ComponentManager;
/*     */ import org.tlauncher.tlauncher.managers.ComponentManagerListenerHelper;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.managers.ProfileManager;
/*     */ import org.tlauncher.tlauncher.managers.ServerListManager;
/*     */ import org.tlauncher.tlauncher.managers.TLauncherManager;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.managers.VersionManagerListener;
/*     */ import org.tlauncher.tlauncher.managers.popup.menu.HotServerManager;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftExtendedAdapter;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListenerAdapter;
/*     */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*     */ import org.tlauncher.tlauncher.site.play.SitePlay;
/*     */ import org.tlauncher.tlauncher.ui.TLauncherFrame;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.browser.BrowserHolder;
/*     */ import org.tlauncher.tlauncher.ui.browser.JFXStartPageLoader;
/*     */ import org.tlauncher.tlauncher.ui.console.Console;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileExplorer;
/*     */ import org.tlauncher.tlauncher.ui.listener.MinecraftUIListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.progress.ProgressFrame;
/*     */ import org.tlauncher.tlauncher.ui.scenes.PseudoScene;
/*     */ import org.tlauncher.tlauncher.updater.client.AutoUpdater;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.TestInstallVersions;
/*     */ import org.tlauncher.util.Time;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.AsyncThread;
/*     */ import org.tlauncher.util.guice.GuiceModule;
/*     */ import org.tlauncher.util.guice.MinecraftLauncherFactory;
/*     */ import org.tlauncher.util.statistics.GameRunningListener;
/*     */ import org.tlauncher.util.statistics.InstallVersionListener;
/*     */ import org.tlauncher.util.statistics.StatisticsUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TLauncher
/*     */ {
/*     */   public static boolean DEBUG = false;
/*     */   private static volatile TLauncher instance;
/*     */   private static String[] sargs;
/*     */   private static File directory;
/*     */   private static Gson gson;
/*     */   private static InnerConfiguration innerSettings;
/*     */   
/*     */   static {
/*     */     try {
/* 107 */       innerSettings = new InnerConfiguration(FileUtil.getResourceAppStream("/inner.tlauncher.properties"));
/* 108 */     } catch (IOException e) {
/* 109 */       e.printStackTrace();
/* 110 */       System.exit(-1);
/*     */     } 
/*     */   }
/*     */   private static String family;
/*     */   private static Injector injector;
/*     */   @Named("console")
/*     */   @Inject
/*     */   private Console console;
/*     */   private final String defaultPrefix;
/*     */   private LangConfiguration lang;
/*     */   private final Configuration configuration;
/*     */   private final Downloader downloader;
/*     */   private final AutoUpdater updater;
/*     */   private TLauncherFrame frame;
/*     */   private final ComponentManager manager;
/*     */   private final VersionManager versionManager;
/*     */   private final ProfileManager profileManager;
/*     */   private final TLauncherManager tlauncherManager;
/*     */   private MinecraftLauncher launcher;
/*     */   private final MinecraftUIListener minecraftListener;
/*     */   private final AdditionalAssetsComponent additionalAssetsComponent;
/*     */   @Inject
/*     */   private MinecraftLauncherFactory minecraftLauncherFactory;
/*     */   private boolean ready;
/*     */   
/*     */   @Inject
/*     */   public TLauncher(@Assisted Configuration configuration) throws Exception {
/* 137 */     this.configuration = configuration;
/* 138 */     this.defaultPrefix = getPagePrefix();
/* 139 */     Time.start(this);
/* 140 */     instance = this;
/* 141 */     gson = new Gson();
/*     */     
/* 143 */     File oldConfig = MinecraftUtil.getSystemRelatedDirectory(innerSettings.get("settings"));
/* 144 */     File newConfig = MinecraftUtil.getSystemRelatedDirectory(innerSettings.get("settings.new"));
/*     */     
/* 146 */     if (!oldConfig.isFile()) {
/* 147 */       oldConfig = MinecraftUtil.getSystemRelatedDirectory(innerSettings.get("tlauncher.folder"));
/*     */     }
/* 149 */     if (oldConfig.isFile() && !newConfig.isFile()) {
/* 150 */       boolean copied = true;
/*     */       
/*     */       try {
/* 153 */         FileUtil.createFile(newConfig);
/* 154 */         FileUtil.copyFile(oldConfig, newConfig, true);
/* 155 */       } catch (IOException ioE) {
/* 156 */         U.log(new Object[] { "Cannot copy old configuration to the new place", oldConfig, newConfig, ioE });
/* 157 */         copied = false;
/*     */       } 
/*     */       
/* 160 */       if (copied) {
/* 161 */         U.log(new Object[] { "Old configuration successfully moved to the new place:", newConfig });
/* 162 */         FileUtil.deleteFile(oldConfig);
/*     */       } 
/*     */     } 
/*     */     
/* 166 */     U.setLoadingStep(Bootstrapper.LoadingStep.LOADING_CONFIGURATION);
/* 167 */     U.log(new Object[] { "Machine info:", OS.getSummary() });
/*     */     
/* 169 */     reloadLocale();
/* 170 */     U.setLoadingStep(Bootstrapper.LoadingStep.LOADING_CONSOLE);
/*     */     
/* 172 */     SwingUtil.init();
/*     */     
/* 174 */     if (!configuration.getLocale().getLanguage().equals((new Locale("zh")).getLanguage())) {
/* 175 */       family = "Roboto";
/*     */     }
/*     */     
/* 178 */     if (configuration.get("ssl.deactivate.date") != null && 
/* 179 */       LocalDate.parse(configuration.get("ssl.deactivate.date")).isAfter(LocalDate.now())) {
/* 180 */       TlauncherUtil.deactivateSSL();
/*     */     }
/*     */ 
/*     */     
/* 184 */     this.downloader = new Downloader(configuration.getConnectionQuality());
/*     */     
/* 186 */     this.minecraftListener = new MinecraftUIListener(this);
/* 187 */     this.updater = new AutoUpdater(this);
/* 188 */     this.updater.asyncFindUpdate();
/*     */ 
/*     */     
/* 191 */     JFXStartPageLoader.getInstance();
/* 192 */     BrowserHolder.getInstance();
/*     */     
/* 194 */     U.setLoadingStep(Bootstrapper.LoadingStep.LOADING_MANAGERS);
/* 195 */     this.manager = new ComponentManager(this);
/*     */     
/* 197 */     this.tlauncherManager = (TLauncherManager)this.manager.loadComponent(TLauncherManager.class);
/* 198 */     this.versionManager = (VersionManager)this.manager.loadComponent(VersionManager.class);
/*     */     
/* 200 */     this.profileManager = (ProfileManager)this.manager.loadComponent(ProfileManager.class);
/* 201 */     this.profileManager.refresh();
/*     */     
/* 203 */     ((ServerListManager)this.manager.loadComponent(ServerListManager.class)).asyncRefresh();
/*     */     
/* 205 */     this.manager.loadComponent(ComponentManagerListenerHelper.class);
/*     */     
/* 207 */     this.additionalAssetsComponent = (AdditionalAssetsComponent)this.manager.loadComponent(AdditionalAssetsComponent.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getFamily() {
/* 212 */     return family;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 219 */       OptionSet set = ArgumentParser.parseArgs(args);
/* 220 */       Configuration configuration = Configuration.createConfiguration(set);
/* 221 */       U.initializeLoggerU(MinecraftUtil.getWorkingDirectory(configuration.get("minecraft.gamedir")), "tlauncher");
/*     */       
/* 223 */       U.setLoadingStep(Bootstrapper.LoadingStep.INITIALIZING);
/* 224 */       ExceptionHandler handler = ExceptionHandler.getInstance();
/*     */       
/* 226 */       Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)handler);
/* 227 */       Thread.currentThread().setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)handler);
/*     */       
/* 229 */       UIManager.put("FileChooser.useSystemExtensionHiding", Boolean.valueOf(false));
/* 230 */       System.setProperty("java.net.preferIPv4Stack", "true");
/* 231 */       Class.forName("org.apache.log4j.helpers.NullEnumeration");
/* 232 */       Class.forName("org.apache.http.impl.conn.CPool");
/* 233 */       TlauncherUtil.createTimeStart();
/* 234 */       initLookAndFeel();
/* 235 */       initUrlCache();
/*     */       
/* 237 */       launch(configuration);
/*     */ 
/*     */       
/* 240 */       Configuration configuration1 = getInstance().getConfiguration();
/* 241 */       configuration1.set("memory.problem.message", null, false);
/* 242 */       if (OS.Arch.CURRENT != OS.Arch.x64) {
/* 243 */         AsyncThread.execute(() -> {
/*     */               OS.fillSystemInfo();
/*     */ 
/*     */               
/*     */               List<TestEnvironment> list = new ArrayList<TestEnvironment>()
/*     */                 {
/*     */                 
/*     */                 };
/*     */ 
/*     */               
/*     */               for (TestEnvironment t : list) {
/*     */                 if (!t.testEnvironment()) {
/*     */                   t.fix();
/*     */                 }
/*     */               } 
/*     */               
/*     */               c.store();
/*     */             });
/*     */       }
/* 262 */     } catch (Throwable e) {
/* 263 */       U.log(new Object[] { "Error launching TLauncher:" });
/* 264 */       U.log(new Object[] { e });
/* 265 */       if (Localizable.exists()) {
/* 266 */         TlauncherUtil.showCriticalProblem(
/* 267 */             Localizable.get("alert.error.not.run") + "<br><br>" + TlauncherUtil.getStringError(e));
/*     */       } else {
/* 269 */         TlauncherUtil.showCriticalProblem(e);
/*     */       } 
/*     */     } 
/* 272 */     Configuration conf = ((TLauncher)injector.getInstance(TLauncher.class)).getConfiguration();
/* 273 */     if (OS.is(new OS[] { OS.WINDOWS })) {
/* 274 */       String pr = OS.executeByTerminal("wmic CPU get NAME");
/* 275 */       String[] array = pr.split(System.lineSeparator());
/* 276 */       if (array.length > 2) {
/* 277 */         pr = array[2];
/* 278 */         conf.set("process.info", pr.trim());
/*     */       } 
/* 280 */       String pr1 = OS.executeByTerminal("set processor");
/* 281 */       Lists.newArrayList((Object[])pr1.split(System.lineSeparator())).stream().filter(e -> e.contains("="))
/* 282 */         .filter(e -> "PROCESSOR_ARCHITECTURE".equals(e.split("=")[0])).map(e -> e.split("=")[1]).findFirst()
/* 283 */         .ifPresent(e -> conf.set("processor.architecture", e));
/*     */     } 
/*     */ 
/*     */     
/* 287 */     TlauncherUtil.fillGPUInfo(conf, true);
/*     */     
/* 289 */     final Configuration c = getInstance().getConfiguration();
/* 290 */     StatisticsUtil.startSending("save/run/tlauncher", null, Maps.newHashMap());
/* 291 */     if (!enterGap(Long.valueOf(c.getLong("sending.tlauncher.unique")))) {
/* 292 */       StatisticsUtil.sendMachineInfo(conf);
/* 293 */       c.set("sending.tlauncher.unique", Long.valueOf(System.currentTimeMillis()));
/*     */     } 
/*     */     
/* 296 */     TlauncherUtil.testNet();
/*     */     
/* 298 */     if (OS.is(new OS[] { OS.WINDOWS })) {
/* 299 */       boolean KB4515384Exists = OS.executeByTerminal("wmic qfe get HotFixID", 5).contains("KB4515384");
/* 300 */       if (KB4515384Exists)
/* 301 */         conf.set("block.updater.message", Boolean.valueOf(true)); 
/*     */     } 
/* 303 */     TlauncherUtil.clearTimeLabel();
/* 304 */     TestInstallVersions.install(conf);
/*     */   }
/*     */   
/*     */   private static void initUrlCache() throws IOException {
/* 308 */     OfflineCache c = OfflineCache.INSTANCE;
/* 309 */     Path urlCache = MinecraftUtil.getTLauncherFile("cache" + File.separator + "requests").toPath();
/* 310 */     FileUtil.createFolder(urlCache.toFile());
/* 311 */     c.setCacheDirectory(urlCache);
/* 312 */     c.setCacheFilters(Sets.newHashSet((Object[])new String[] { ".*png$", ".*gif$" }));
/* 313 */     c.setActive(true);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initLookAndFeel() {
/* 318 */     LookAndFeel defaultLookAndFeel = null;
/*     */     try {
/* 320 */       defaultLookAndFeel = UIManager.getLookAndFeel();
/* 321 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/* 322 */       new FileExplorer();
/* 323 */     } catch (Throwable t) {
/* 324 */       U.log(new Object[] { "problem with ", t });
/* 325 */       if (Objects.nonNull(defaultLookAndFeel))
/*     */         try {
/* 327 */           UIManager.setLookAndFeel(defaultLookAndFeel);
/* 328 */         } catch (Throwable e) {
/* 329 */           U.log(new Object[] { "problem with look and feel ", e });
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Gson getGson() {
/* 335 */     return gson;
/*     */   }
/*     */   
/*     */   public static void kill() {
/* 339 */     U.log(new Object[] { "Good bye!" });
/* 340 */     TlauncherUtil.clearTimeLabel();
/*     */     
/* 342 */     try { Class<?> cl = Class.forName("org.tlauncher.tlauncher.managers.TLauncherManager$3");
/* 343 */       if (Objects.nonNull(cl) && Objects.nonNull(getInstance()) && 
/* 344 */         Objects.nonNull((getInstance()).tlauncherManager)) {
/* 345 */         String value = getInstance().getConfiguration().get("minecraft.onlaunch");
/* 346 */         if (!ActionOnLaunch.EXIT.name().equalsIgnoreCase(value))
/* 347 */           (getInstance()).tlauncherManager.cleanMods(); 
/*     */       }  }
/* 349 */     catch (ClassNotFoundException classNotFoundException) {  }
/* 350 */     catch (Throwable e)
/* 351 */     { U.log(new Object[] { e }); }
/*     */     
/* 353 */     Logger.getLogger("main").info(U.FLUSH_MESSAGE);
/* 354 */     System.exit(0);
/*     */   }
/*     */   
/*     */   private static void launch(Configuration configuration) {
/* 358 */     GuiceModule guiceModule = new GuiceModule(configuration, innerSettings);
/* 359 */     injector = Guice.createInjector(new Module[] { (Module)guiceModule });
/* 360 */     guiceModule.setInjector(injector);
/*     */ 
/*     */     
/* 363 */     U.log(new Object[] { String.format("Starting TLauncher %s %s", new Object[] { innerSettings.get("version"), innerSettings.get("type") }) });
/* 364 */     U.log(new Object[] { "For more information, visit https://tlauncher.org/" });
/* 365 */     U.log(new Object[] { "Startup time:", DateFormat.getDateTimeInstance().format(new Date()) });
/* 366 */     U.log(new Object[] { "Running folder " + Paths.get("", new String[0]).toAbsolutePath().toString() });
/*     */     
/* 368 */     U.log(new Object[] { "---" });
/* 369 */     ProgressFrame customBar = new ProgressFrame(innerSettings.get("version"));
/* 370 */     TLauncher t = (TLauncher)injector.getInstance(TLauncher.class);
/* 371 */     U.setLoadingStep(Bootstrapper.LoadingStep.LOADING_WINDOW);
/* 372 */     t.frame = new TLauncherFrame(t);
/* 373 */     t.init(customBar);
/* 374 */     t.getProfileManager().fireProfileRefreshed();
/*     */   }
/*     */   
/*     */   private static void log(String line) {
/* 378 */     U.log(new Object[] { "[TLauncher] " + line });
/*     */   }
/*     */   
/*     */   public static InnerConfiguration getInnerSettings() {
/* 382 */     return innerSettings;
/*     */   }
/*     */   
/*     */   public static File getDirectory() {
/* 386 */     if (directory == null)
/* 387 */       directory = new File("."); 
/* 388 */     return directory;
/*     */   }
/*     */   
/*     */   public static TLauncher getInstance() {
/* 392 */     return instance;
/*     */   }
/*     */   
/*     */   public static double getVersion() {
/* 396 */     return innerSettings.getDouble("version");
/*     */   }
/*     */   
/*     */   public static String getFolder() {
/* 400 */     return innerSettings.get("folder");
/*     */   }
/*     */   
/*     */   public static String[] getUpdateRepos() {
/* 404 */     return innerSettings.getArrayAccess("update.repo");
/*     */   }
/*     */   
/*     */   public static Injector getInjector() {
/* 408 */     return injector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean enterGap(Long last) {
/* 416 */     if (last.longValue() == 0L)
/* 417 */       return false; 
/* 418 */     Calendar start = Calendar.getInstance();
/* 419 */     start.set(10, 0);
/* 420 */     start.set(12, 0);
/* 421 */     start.set(13, 0);
/* 422 */     Calendar end = Calendar.getInstance();
/* 423 */     end.set(10, 23);
/* 424 */     end.set(12, 59);
/* 425 */     end.set(13, 59);
/* 426 */     return (start.getTimeInMillis() < last.longValue() && last.longValue() < end.getTimeInMillis());
/*     */   }
/*     */   
/*     */   public Downloader getDownloader() {
/* 430 */     return this.downloader;
/*     */   }
/*     */   
/*     */   public LangConfiguration getLang() {
/* 434 */     return this.lang;
/*     */   }
/*     */   
/*     */   public Configuration getConfiguration() {
/* 438 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public AutoUpdater getUpdater() {
/* 442 */     return this.updater;
/*     */   }
/*     */   
/*     */   public TLauncherFrame getFrame() {
/* 446 */     return this.frame;
/*     */   }
/*     */   
/*     */   public ComponentManager getManager() {
/* 450 */     return this.manager;
/*     */   }
/*     */   
/*     */   public VersionManager getVersionManager() {
/* 454 */     return this.versionManager;
/*     */   }
/*     */   
/*     */   public ProfileManager getProfileManager() {
/* 458 */     return this.profileManager;
/*     */   }
/*     */   
/*     */   public TLauncherManager getTLauncherManager() {
/* 462 */     return this.tlauncherManager;
/*     */   }
/*     */   
/*     */   public MinecraftLauncher getLauncher() {
/* 466 */     return this.launcher;
/*     */   }
/*     */   
/*     */   public boolean isReady() {
/* 470 */     return this.ready;
/*     */   }
/*     */   
/*     */   public void reloadLocale() throws IOException {
/* 474 */     Locale locale = this.configuration.getLocale();
/* 475 */     U.log(new Object[] { "Selected locale: " + locale });
/*     */     
/* 477 */     if (this.lang == null) {
/* 478 */       this.lang = new LangConfiguration(this.configuration.getLocales(), locale, "/lang/tlauncher/");
/*     */     } else {
/* 480 */       this.lang.setSelected(locale);
/*     */     } 
/* 482 */     Localizable.setLang(this.lang);
/* 483 */     Alert.prepareLocal();
/*     */     
/* 485 */     if (this.console != null) {
/* 486 */       this.console.setName(this.lang.get("console"));
/*     */     }
/*     */   }
/*     */   
/*     */   public void launch(MinecraftListener listener, RemoteServer server, boolean forceupdate) {
/* 491 */     this.launcher = this.minecraftLauncherFactory.create(this, forceupdate);
/*     */     
/* 493 */     this.launcher.addListener((MinecraftListener)this.minecraftListener);
/* 494 */     this.launcher.addListener(listener);
/* 495 */     this.launcher.addListener((MinecraftListener)this.frame.mp.getProgress());
/* 496 */     this.launcher.addListener((MinecraftListener)getInstance().getTLauncherManager());
/* 497 */     this.launcher.addListener((MinecraftListener)injector.getInstance(HotServerManager.class));
/*     */     
/* 499 */     this.launcher.addListener((MinecraftListener)new GameRunningListener(this.launcher));
/* 500 */     this.launcher.addListener((MinecraftListener)new MinecraftListenerAdapter()
/*     */         {
/*     */           public void onMinecraftKnownError(MinecraftException e) {
/* 503 */             if (e.getLangPath().equalsIgnoreCase("start")) {
/* 504 */               TLauncher.this.frame.mp.setScene((PseudoScene)TLauncher.this.frame.mp.settingsScene);
/*     */             }
/*     */           }
/*     */         });
/* 508 */     this.launcher.setServer(server);
/*     */     
/* 510 */     this.launcher.addListener((MinecraftListener)new MinecraftExtendedAdapter()
/*     */         {
/*     */           public void onMinecraftDownloading() {
/* 513 */             if (TLauncher.this.launcher.getVersion().isModpack()) {
/* 514 */               String mask = "download";
/*     */               try {
/* 516 */                 ModpackVersionDTO v = (ModpackVersionDTO)TLauncher.this.launcher.getVersion().getModpack().getVersion();
/* 517 */                 boolean cleanOldDownloadFile = false;
/* 518 */                 for (GameType type : GameType.getSubEntities()) {
/* 519 */                   if (type.equals(GameType.MAP))
/*     */                     continue; 
/* 521 */                   for (int i = 0; i < v.getByType(type).size(); i++) {
/* 522 */                     GameEntityDTO b = v.getByType(type).get(i);
/* 523 */                     if (b.getVersion().getMetadata().getPath().endsWith(mask)) {
/*     */                       
/* 525 */                       GameEntityDTO replacer = (GameEntityDTO)((ModpackManager)TLauncher.injector.getInstance(ModpackManager.class)).readFromServer(b.getClass(), b, b.getVersion());
/* 526 */                       U.log(new Object[] { String.format("replace broken element %s %s", new Object[] { b
/* 527 */                                 .getVersion().getMetadata().getPath(), replacer
/* 528 */                                 .getVersion().getMetadata().getPath() }) });
/* 529 */                       b.getVersion().setMetadata(replacer.getVersion().getMetadata());
/* 530 */                       cleanOldDownloadFile = true;
/*     */                     } 
/*     */                   } 
/* 533 */                   if (cleanOldDownloadFile) {
/* 534 */                     Files.deleteIfExists(
/* 535 */                         Paths.get(ModpackUtil.getPath(TLauncher.this.launcher.getVersion(), type).toString(), new String[] { mask }));
/*     */                   }
/*     */                 } 
/* 538 */               } catch (Throwable e) {
/* 539 */                 U.log(new Object[] { "got problem with fixing download link", e });
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/* 544 */     this.launcher.addListener((MinecraftListener)injector.getInstance(ModpackManager.class));
/* 545 */     this.launcher.start();
/*     */   }
/*     */   
/*     */   public boolean isLauncherWorking() {
/* 549 */     return (this.launcher != null && this.launcher.isWorking());
/*     */   }
/*     */   
/*     */   public void hide() {
/* 553 */     if (this.frame != null) {
/* 554 */       boolean doAgain = true;
/*     */       
/* 556 */       while (doAgain) {
/*     */         try {
/* 558 */           this.frame.setVisible(false);
/* 559 */           doAgain = false;
/* 560 */         } catch (Exception exception) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 565 */     U.log(new Object[] { "I'm hiding!" });
/*     */   }
/*     */   
/*     */   public void show() {
/* 569 */     if (this.frame != null) {
/* 570 */       boolean doAgain = true;
/*     */       
/* 572 */       while (doAgain) {
/*     */         try {
/* 574 */           this.frame.setVisible(true);
/* 575 */           doAgain = false;
/* 576 */         } catch (Exception e) {
/* 577 */           U.log(new Object[] { e });
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 582 */     U.log(new Object[] { "Here I am!" });
/*     */   }
/*     */   
/*     */   public String getPagePrefix() {
/* 586 */     if (this.defaultPrefix != null) {
/* 587 */       return this.defaultPrefix;
/*     */     }
/*     */     
/* 590 */     if (TlauncherUtil.hostAvailabilityCheck("https://page.tlauncher.org") == 200)
/* 591 */       return "https://page.tlauncher.org/update/downloads/configs/client/"; 
/* 592 */     if (TlauncherUtil.hostAvailabilityCheck("https://repo.tlauncher.org") == 200)
/* 593 */       return "https://repo.tlauncher.org/update/downloads/configs/client/"; 
/* 594 */     if (TlauncherUtil.hostAvailabilityCheck("https://advancedrepository.com") == 200) {
/* 595 */       return "https://advancedrepository.com/update/downloads/configs/client/";
/*     */     }
/* 597 */     return "127.0.0.1";
/*     */   }
/*     */ 
/*     */   
/*     */   public void init(ProgressFrame customBar) {
/* 602 */     this.console.init(this.configuration, (this.configuration.getConsoleType() == ConsoleType.GLOBAL));
/* 603 */     this.frame.afterInitProfile();
/* 604 */     U.setLoadingStep(Bootstrapper.LoadingStep.REFRESHING_INFO);
/*     */     
/* 606 */     this.versionManager.addListener((VersionManagerListener)injector.getInstance(SitePlay.class));
/* 607 */     this.versionManager.addListener((VersionManagerListener)injector.getInstance(HotServerManager.class));
/*     */     
/* 609 */     this.versionManager.asyncRefresh();
/* 610 */     this.additionalAssetsComponent.asyncRefresh();
/*     */     
/* 612 */     this.downloader.addListener((DownloaderListener)new InstallVersionListener());
/* 613 */     ModpackManager modpackManager = (ModpackManager)injector.getInstance(ModpackManager.class);
/*     */     
/* 615 */     U.log(new Object[] { "Started! (" + Time.stop(this) + " ms.)" });
/*     */     
/* 617 */     this.ready = true;
/* 618 */     U.setLoadingStep(Bootstrapper.LoadingStep.SUCCESS);
/*     */     
/* 620 */     customBar.setVisible(false);
/* 621 */     customBar.dispose();
/* 622 */     this.frame.setVisible(true);
/* 623 */     log("show tlauncher!!!");
/*     */     
/* 625 */     this.frame.showTips();
/* 626 */     if (!StringUtils.equals(System.getProperty("console"), Boolean.toString(true))) {
/* 627 */       U.removeConsoleAppender();
/*     */     }
/*     */   }
/*     */   
/*     */   public AdditionalAssetsComponent getAdditionalAssetsComponent() {
/* 632 */     return this.additionalAssetsComponent;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\rmo\TLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */