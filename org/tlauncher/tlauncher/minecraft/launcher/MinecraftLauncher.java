/*      */ package org.tlauncher.tlauncher.minecraft.launcher;
/*      */ 
/*      */ import ch.jamiete.mcping.MinecraftPing;
/*      */ import ch.jamiete.mcping.MinecraftPingOptions;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.gson.Gson;
/*      */ import com.google.inject.Inject;
/*      */ import com.google.inject.assistedinject.Assisted;
/*      */ import com.google.inject.name.Named;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.UnknownHostException;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.TreeSet;
/*      */ import java.util.zip.ZipEntry;
/*      */ import java.util.zip.ZipFile;
/*      */ import java.util.zip.ZipInputStream;
/*      */ import java.util.zip.ZipOutputStream;
/*      */ import javax.swing.SwingUtilities;
/*      */ import net.minecraft.launcher.process.JavaProcess;
/*      */ import net.minecraft.launcher.process.JavaProcessLauncher;
/*      */ import net.minecraft.launcher.process.JavaProcessListener;
/*      */ import net.minecraft.launcher.updater.AssetIndex;
/*      */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*      */ import net.minecraft.launcher.versions.CompleteVersion;
/*      */ import net.minecraft.launcher.versions.ExtractRules;
/*      */ import net.minecraft.launcher.versions.Library;
/*      */ import net.minecraft.launcher.versions.LogClient;
/*      */ import net.minecraft.launcher.versions.Version;
/*      */ import net.minecraft.launcher.versions.json.Argument;
/*      */ import net.minecraft.launcher.versions.json.ArgumentType;
/*      */ import net.minecraft.launcher.versions.json.DateTypeAdapter;
/*      */ import org.apache.commons.io.FileUtils;
/*      */ import org.apache.commons.io.filefilter.FileFilterUtils;
/*      */ import org.apache.commons.io.filefilter.IOFileFilter;
/*      */ import org.apache.commons.io.filefilter.TrueFileFilter;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.apache.commons.lang3.text.StrSubstitutor;
/*      */ import org.tlauncher.tlauncher.component.LogClientConfigurationComponent;
/*      */ import org.tlauncher.tlauncher.component.minecraft.JavaMinecraftComponent;
/*      */ import org.tlauncher.tlauncher.configuration.Configuration;
/*      */ import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
/*      */ import org.tlauncher.tlauncher.downloader.AbortedDownloadException;
/*      */ import org.tlauncher.tlauncher.downloader.DefaultDownloadableContainerHandler;
/*      */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*      */ import org.tlauncher.tlauncher.downloader.DownloadableContainerHandler;
/*      */ import org.tlauncher.tlauncher.downloader.Downloader;
/*      */ import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
/*      */ import org.tlauncher.tlauncher.entity.server.RemoteServer;
/*      */ import org.tlauncher.tlauncher.entity.server.Server;
/*      */ import org.tlauncher.tlauncher.managers.AssetsManager;
/*      */ import org.tlauncher.tlauncher.managers.ComponentManager;
/*      */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*      */ import org.tlauncher.tlauncher.managers.ProfileManager;
/*      */ import org.tlauncher.tlauncher.managers.VersionManager;
/*      */ import org.tlauncher.tlauncher.managers.VersionSyncInfoContainer;
/*      */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*      */ import org.tlauncher.tlauncher.minecraft.crash.Crash;
/*      */ import org.tlauncher.tlauncher.minecraft.crash.CrashDescriptor;
/*      */ import org.tlauncher.tlauncher.minecraft.launcher.assitent.AdditionalFileAssistanceFactory;
/*      */ import org.tlauncher.tlauncher.minecraft.launcher.assitent.BackupWorldAssistant;
/*      */ import org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantInterface;
/*      */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*      */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*      */ import org.tlauncher.tlauncher.service.XmlLogDeserialization;
/*      */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*      */ import org.tlauncher.tlauncher.ui.alert.Notification;
/*      */ import org.tlauncher.tlauncher.ui.console.Console;
/*      */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*      */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*      */ import org.tlauncher.tlauncher.ui.swing.notification.skin.SkinNotification;
/*      */ import org.tlauncher.util.FileUtil;
/*      */ import org.tlauncher.util.MinecraftUtil;
/*      */ import org.tlauncher.util.OS;
/*      */ import org.tlauncher.util.U;
/*      */ import org.tlauncher.util.guice.LanguageAssistFactory;
/*      */ import org.tlauncher.util.guice.SoundAssistFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MinecraftLauncher
/*      */   implements JavaProcessListener
/*      */ {
/*      */   private final Thread parentThread;
/*      */   private final Gson gson;
/*      */   private final DateTypeAdapter dateAdapter;
/*      */   private final Downloader downloader;
/*      */   private final Configuration settings;
/*      */   private final boolean forceUpdate;
/*      */   private final boolean assistLaunch;
/*      */   private final VersionManager vm;
/*      */   private final AssetsManager am;
/*      */   private final ProfileManager pm;
/*      */   private final List<MinecraftListener> listeners;
/*      */   private final List<MinecraftExtendedListener> extListeners;
/*  122 */   private final List<MinecraftLauncherAssistantInterface> assistants = new ArrayList<>(); private boolean working; private boolean killed; @Named("console")
/*      */   @Inject
/*      */   private Console console; private final CrashDescriptor descriptor; private MinecraftLauncherStep step; @Inject
/*      */   private ModpackManager modpackManager; @Inject
/*      */   private SoundAssistFactory soundAssistFactory; @Inject
/*      */   private AdditionalFileAssistanceFactory additionalFileAssistanceFactory; @Inject
/*      */   private LanguageAssistFactory languageAssistFactory; @Inject
/*      */   JavaMinecraftComponent javaMinecraftComponent; @Inject
/*      */   private BackupWorldAssistant backupWorldAssistant; @Inject
/*      */   private LogClientConfigurationComponent logClientConfigurationComponent;
/*      */   @Inject
/*      */   private XmlLogDeserialization xmlLogDeserialization;
/*      */   private String versionName;
/*      */   private VersionSyncInfo versionSync;
/*      */   private CompleteVersion version;
/*      */   private CompleteVersion deJureVersion;
/*      */   private Account account;
/*      */   private MinecraftJava.CompleteMinecraftJava java;
/*      */   private File javaDir;
/*      */   private File gameDir;
/*      */   private File runningMinecraftDir;
/*      */   private File localAssetsDir;
/*      */   private File nativeDir;
/*      */   private File globalAssetsDir;
/*      */   private File assetsIndexesDir;
/*      */   private File assetsObjectsDir;
/*      */   private int[] windowSize;
/*      */   private boolean fullScreen;
/*  150 */   private int javaVersion = 8; private int ramSize; private JavaProcessLauncher launcher; private String javaArgs; private String programArgs; private volatile boolean minecraftWorking; private long startupTime; private int exitCode; private RemoteServer server; private boolean clearNatives; private JavaProcess process; public int getJavaVersion() { return this.javaVersion; } public void setJavaVersion(int javaVersion) {
/*  151 */     this.javaVersion = javaVersion;
/*      */   }
/*  153 */   public MinecraftJava.CompleteMinecraftJava getJava() { return this.java; } public void setJava(MinecraftJava.CompleteMinecraftJava java) {
/*  154 */     this.java = java;
/*      */   }
/*  156 */   public File getJavaDir() { return this.javaDir; } public void setJavaDir(File javaDir) {
/*  157 */     this.javaDir = javaDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getStartupTime() {
/*  167 */     return this.startupTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean firstLine = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private MinecraftLauncher(ComponentManager manager, Downloader downloader, Configuration configuration, boolean forceUpdate, ConsoleVisibility visibility, boolean exit) {
/*  179 */     if (manager == null) {
/*  180 */       throw new NullPointerException("Ti sovsem s duba ruhnul?");
/*      */     }
/*  182 */     if (downloader == null) {
/*  183 */       throw new NullPointerException("Downloader is NULL!");
/*      */     }
/*  185 */     if (configuration == null) {
/*  186 */       throw new NullPointerException("Configuration is NULL!");
/*      */     }
/*  188 */     if (visibility == null) {
/*  189 */       throw new NullPointerException("ConsoleVisibility is NULL!");
/*      */     }
/*  191 */     this.parentThread = Thread.currentThread();
/*      */     
/*  193 */     this.gson = new Gson();
/*  194 */     this.dateAdapter = new DateTypeAdapter();
/*      */     
/*  196 */     this.downloader = downloader;
/*  197 */     this.settings = configuration;
/*      */     
/*  199 */     this.vm = (VersionManager)manager.getComponent(VersionManager.class);
/*  200 */     this.am = (AssetsManager)manager.getComponent(AssetsManager.class);
/*  201 */     this.pm = (ProfileManager)manager.getComponent(ProfileManager.class);
/*      */     
/*  203 */     this.forceUpdate = forceUpdate;
/*  204 */     this.assistLaunch = !exit;
/*      */     
/*  206 */     this.descriptor = new CrashDescriptor(this);
/*      */     
/*  208 */     this.listeners = Collections.synchronizedList(new ArrayList<>());
/*  209 */     this.extListeners = Collections.synchronizedList(new ArrayList<>());
/*      */     
/*  211 */     this.step = MinecraftLauncherStep.NONE;
/*      */     
/*  213 */     log(new Object[] { "Running under TLauncher " + TLauncher.getVersion() });
/*      */   }
/*      */   
/*      */   @Inject
/*      */   public MinecraftLauncher(@Assisted TLauncher t, @Assisted boolean forceUpdate) {
/*  218 */     this(t.getManager(), t.getDownloader(), t.getConfiguration(), forceUpdate, t
/*  219 */         .getConfiguration().getConsoleType().getVisibility(), 
/*  220 */         (t.getConfiguration().getActionOnLaunch() == ActionOnLaunch.EXIT));
/*      */   }
/*      */   
/*      */   @Inject
/*      */   public void postInit() {
/*  225 */     this.assistants.add(this.soundAssistFactory.create(this));
/*  226 */     this.assistants.add(this.additionalFileAssistanceFactory.create(this));
/*  227 */     this.assistants.add(this.languageAssistFactory.create(this));
/*  228 */     this.assistants.add(this.javaMinecraftComponent);
/*  229 */     this.assistants.add(this.backupWorldAssistant);
/*      */   }
/*      */   
/*      */   public Downloader getDownloader() {
/*  233 */     return this.downloader;
/*      */   }
/*      */   
/*      */   public Configuration getConfiguration() {
/*  237 */     return this.settings;
/*      */   }
/*      */   
/*      */   public boolean isForceUpdate() {
/*  241 */     return this.forceUpdate;
/*      */   }
/*      */   
/*      */   public boolean isLaunchAssist() {
/*  245 */     return this.assistLaunch;
/*      */   }
/*      */   
/*      */   public CrashDescriptor getCrashDescriptor() {
/*  249 */     return this.descriptor;
/*      */   }
/*      */   
/*      */   public MinecraftLauncherStep getStep() {
/*  253 */     return this.step;
/*      */   }
/*      */   
/*      */   public boolean isWorking() {
/*  257 */     return this.working;
/*      */   }
/*      */   
/*      */   public void addListener(MinecraftListener listener) {
/*  261 */     if (listener == null) {
/*  262 */       throw new NullPointerException();
/*      */     }
/*  264 */     if (listener instanceof MinecraftExtendedListener) {
/*  265 */       this.extListeners.add((MinecraftExtendedListener)listener);
/*      */     }
/*  267 */     this.listeners.add(listener);
/*      */   }
/*      */   
/*      */   public void start() {
/*  271 */     checkWorking();
/*  272 */     this.working = true;
/*      */     
/*      */     try {
/*  275 */       collectInfo();
/*  276 */       downloadResources();
/*  277 */       checkExtraConditions();
/*  278 */       constructProcess();
/*  279 */       launchMinecraft();
/*  280 */       postLaunch();
/*  281 */     } catch (Throwable e) {
/*  282 */       log(new Object[] { "Error occurred:", e });
/*  283 */       if (e instanceof MinecraftException) {
/*      */         
/*  285 */         if (!this.settings.getBoolean("memory.notification.off") && 
/*  286 */           !FileUtil.checkFreeSpace(MinecraftUtil.getWorkingDirectory(), FileUtil.SIZE_300.longValue())) {
/*  287 */           String message = Localizable.get("memory.notification.message").replace("disk", 
/*  288 */               MinecraftUtil.getWorkingDirectory().toPath().getRoot().toString());
/*  289 */           Alert.showCustomMonolog(Localizable.get("memory.notification.title"), new Notification(message, "memory.notification.off"));
/*      */         } 
/*      */         
/*  292 */         MinecraftException me = (MinecraftException)e;
/*  293 */         for (MinecraftListener listener : this.listeners) {
/*  294 */           listener.onMinecraftKnownError(me);
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  303 */       else if (e instanceof MinecraftLauncherAborted) {
/*      */ 
/*      */         
/*  306 */         for (MinecraftListener listener : this.listeners) {
/*  307 */           listener.onMinecraftAbort();
/*      */         }
/*      */       } else {
/*      */         
/*  311 */         for (MinecraftListener listener : this.listeners) {
/*  312 */           listener.onMinecraftError(e);
/*      */         }
/*      */       } 
/*      */     } 
/*  316 */     this.working = false;
/*  317 */     this.step = MinecraftLauncherStep.NONE;
/*      */     
/*  319 */     log(new Object[] { "Launcher exited." });
/*      */   }
/*      */   
/*      */   public void stop() {
/*  323 */     if (this.step == MinecraftLauncherStep.NONE) {
/*  324 */       throw new IllegalStateException();
/*      */     }
/*  326 */     if (this.step == MinecraftLauncherStep.DOWNLOADING) {
/*  327 */       this.downloader.stopDownload();
/*      */     }
/*  329 */     this.working = false;
/*      */   }
/*      */   
/*      */   public String getVersionName() {
/*  333 */     return this.versionName;
/*      */   }
/*      */   
/*      */   public void setVersionName(String versionName) {
/*  337 */     this.versionName = versionName;
/*      */   }
/*      */   
/*      */   public CompleteVersion getVersion() {
/*  341 */     return this.version;
/*      */   }
/*      */   
/*      */   public int getExitCode() {
/*  345 */     return this.exitCode;
/*      */   }
/*      */   
/*      */   public Server getServer() {
/*  349 */     return (Server)this.server;
/*      */   }
/*      */   
/*      */   public void setServer(RemoteServer server) {
/*  353 */     checkWorking();
/*      */     
/*  355 */     this.server = server;
/*      */   }
/*      */   
/*      */   private void collectInfo() throws MinecraftException {
/*  359 */     checkStep(MinecraftLauncherStep.NONE, MinecraftLauncherStep.COLLECTING);
/*  360 */     log(new Object[] { "Collecting info..." });
/*      */     
/*  362 */     for (MinecraftListener listener : this.listeners) {
/*  363 */       listener.onMinecraftPrepare();
/*      */     }
/*  365 */     for (MinecraftExtendedListener listener : this.extListeners) {
/*  366 */       listener.onMinecraftCollecting();
/*      */     }
/*  368 */     log(new Object[] { "Force update:", Boolean.valueOf(this.forceUpdate) });
/*      */     
/*  370 */     this.versionName = this.settings.get("login.version.game");
/*  371 */     if (this.versionName == null || this.versionName.isEmpty()) {
/*  372 */       throw new IllegalArgumentException("Version name is NULL or empty!");
/*      */     }
/*  374 */     log(new Object[] { "Selected version:", this.versionName });
/*  375 */     this.account = this.pm.getSelectedAccount();
/*  376 */     if (Objects.isNull(this.account) || this.account.getUsername().isEmpty()) {
/*  377 */       throw new IllegalArgumentException("login is NULL or empty!");
/*      */     }
/*  379 */     log(new Object[] { "Selected account:", this.account.toDebugString() });
/*      */     
/*  381 */     this.versionSync = this.vm.getVersionSyncInfo(this.versionName);
/*      */     
/*  383 */     if (this.versionSync == null) {
/*  384 */       throw new IllegalArgumentException("Cannot find version " + this.version);
/*      */     }
/*  386 */     log(new Object[] { "Version sync info:", this.versionSync });
/*      */ 
/*      */     
/*      */     try {
/*  390 */       if (this.versionSync.getLocal() != null && ((CompleteVersion)this.versionSync.getLocal()).getDownloads() == null && this.versionSync
/*  391 */         .getRemote() != null) {
/*  392 */         this.versionSync.setLocal(null);
/*      */       }
/*      */       
/*  395 */       this.deJureVersion = this.versionSync.resolveCompleteVersion(this.vm, this.forceUpdate);
/*  396 */     } catch (IOException e) {
/*  397 */       Alert.showMessage(Localizable.get("version.manager.resolve.title"), 
/*  398 */           Localizable.get("version.manager.resolve.message"));
/*  399 */       throw new MinecraftLauncherAborted(e);
/*      */     } 
/*      */     
/*  402 */     if (this.deJureVersion == null)
/*  403 */       throw new NullPointerException("Complete version is NULL"); 
/*  404 */     this.version = this.deJureVersion;
/*      */     
/*  406 */     this.gameDir = new File(this.settings.get("minecraft.gamedir"));
/*      */     
/*      */     try {
/*  409 */       FileUtil.createFolder(this.gameDir);
/*  410 */     } catch (IOException e) {
/*  411 */       throw new MinecraftException("Cannot createScrollWrapper working directory!", "folder-not-found", e);
/*      */     } 
/*  413 */     if (this.version.isModpack()) {
/*  414 */       this.runningMinecraftDir = ModpackUtil.getPathByVersion(this.version).toFile();
/*      */     } else {
/*  416 */       this.runningMinecraftDir = this.gameDir;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  421 */     File serverResourcePacks = new File(this.runningMinecraftDir, "server-resource-packs");
/*      */     
/*      */     try {
/*  424 */       FileUtil.createFolder(serverResourcePacks);
/*  425 */     } catch (IOException e) {
/*  426 */       throw new RuntimeException("Can't create " + serverResourcePacks, e);
/*      */     } 
/*  428 */     File modsFolder = new File(this.runningMinecraftDir, "mods");
/*      */     
/*      */     try {
/*  431 */       FileUtil.createFolder(modsFolder);
/*  432 */     } catch (IOException e) {
/*  433 */       throw new RuntimeException("Can't create " + modsFolder, e);
/*      */     } 
/*      */     
/*  436 */     this.globalAssetsDir = new File(this.gameDir, "assets");
/*      */     try {
/*  438 */       FileUtil.createFolder(this.globalAssetsDir);
/*  439 */     } catch (IOException e) {
/*  440 */       throw new RuntimeException("Cannot createScrollWrapper assets directory!", e);
/*      */     } 
/*      */     
/*  443 */     this.assetsIndexesDir = new File(this.globalAssetsDir, "indexes");
/*      */     try {
/*  445 */       FileUtil.createFolder(this.assetsIndexesDir);
/*  446 */     } catch (IOException e) {
/*  447 */       throw new RuntimeException("Cannot createScrollWrapper assets indexes directory!", e);
/*      */     } 
/*      */     
/*  450 */     this.assetsObjectsDir = new File(this.globalAssetsDir, "objects");
/*      */     try {
/*  452 */       FileUtil.createFolder(this.assetsObjectsDir);
/*  453 */     } catch (IOException e) {
/*  454 */       throw new RuntimeException("Cannot createScrollWrapper assets objects directory!", e);
/*      */     } 
/*      */     
/*  457 */     this.nativeDir = new File(this.gameDir, "versions/" + this.version.getID() + "/natives");
/*  458 */     if (!OS.is(new OS[] { OS.WINDOWS
/*  459 */         }) && this.nativeDir.getPath().chars()
/*  460 */       .anyMatch(e -> Character.UnicodeBlock.of(e).equals(Character.UnicodeBlock.CYRILLIC))) {
/*      */       try {
/*  462 */         this.nativeDir = Files.createTempDirectory("natives", (FileAttribute<?>[])new FileAttribute[0]).toFile();
/*  463 */         this.nativeDir.deleteOnExit();
/*  464 */         this.clearNatives = true;
/*  465 */       } catch (IOException e) {
/*  466 */         throw new RuntimeException("Cannot createScrollWrapper native files directory!", e);
/*      */       } 
/*      */     }
/*      */     
/*      */     try {
/*  471 */       FileUtil.createFolder(this.nativeDir);
/*  472 */     } catch (IOException e) {
/*  473 */       throw new RuntimeException("Cannot createScrollWrapper native files directory!", e);
/*      */     } 
/*  475 */     this.programArgs = this.settings.get("minecraft.args");
/*  476 */     if (this.programArgs != null && this.programArgs.isEmpty())
/*  477 */       this.programArgs = null; 
/*  478 */     this.javaArgs = this.settings.get("minecraft.javaargs");
/*  479 */     if (this.javaArgs != null && this.javaArgs.isEmpty()) {
/*  480 */       this.javaArgs = null;
/*      */     }
/*  482 */     this.windowSize = this.settings.getClientWindowSize();
/*      */     
/*  484 */     if (this.windowSize[0] < 1) {
/*  485 */       throw new IllegalArgumentException("Invalid window width!");
/*      */     }
/*  487 */     if (this.windowSize[1] < 1) {
/*  488 */       throw new IllegalArgumentException("Invalid window height!");
/*      */     }
/*  490 */     this.fullScreen = this.settings.getBoolean("minecraft.fullscreen");
/*  491 */     if (this.version.isModpack() && this.version.getModpack().isModpackMemory()) {
/*  492 */       this.ramSize = this.version.getModpack().getMemory();
/*      */     } else {
/*  494 */       this.ramSize = this.settings.getInteger("minecraft.memory.ram2");
/*      */     } 
/*      */     
/*  497 */     if (this.ramSize < 512) {
/*  498 */       throw new IllegalArgumentException("Invalid RAM size!");
/*      */     }
/*  500 */     for (MinecraftLauncherAssistantInterface assistant : this.assistants) {
/*  501 */       assistant.collectInfo(this);
/*      */     }
/*  503 */     log(new Object[] { "Checking conditions..." });
/*      */     
/*  505 */     if (!this.version.appliesToCurrentEnvironment()) {
/*  506 */       Alert.showLocWarning("launcher.warning.title", "launcher.warning.incompatible.environment");
/*      */     }
/*  508 */     if (this.version.getMinecraftArguments() == null && this.version.getArguments() == null)
/*  509 */       throw new MinecraftException("Can't run version, missing minecraftArguments", "noArgs", new Object[0]); 
/*  510 */     if (this.version.getMinimumCustomLauncherVersion() > TLauncher.getInnerSettings().getInteger("tlauncher.game.version.compatible")) {
/*  511 */       throw new MinecraftException("Alternative launcher is incompatible with launching version!", "incompatible", new Object[0]);
/*      */     }
/*      */ 
/*      */     
/*  515 */     String[] sh = (new File(this.runningMinecraftDir, "shaderpacks")).list();
/*  516 */     if (Objects.nonNull(sh) && sh.length > 0) {
/*  517 */       StringBuilder b = new StringBuilder("shaderpacks:");
/*  518 */       print(sh, b);
/*      */     } 
/*  520 */     String[] rs = (new File(this.runningMinecraftDir, "resourcepacks")).list();
/*  521 */     if (Objects.nonNull(rs) && rs.length > 0) {
/*  522 */       StringBuilder b = new StringBuilder("resourcepacks:");
/*  523 */       print(rs, b);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void print(String[] sh, StringBuilder b) {
/*  528 */     for (String f : sh)
/*  529 */       b.append(f).append(","); 
/*  530 */     log(new Object[] { b.substring(0, b.length() - 1) });
/*      */   }
/*      */   private void downloadResources() throws MinecraftException {
/*      */     VersionSyncInfoContainer versionSyncInfoContainer;
/*  534 */     checkStep(MinecraftLauncherStep.COLLECTING, MinecraftLauncherStep.DOWNLOADING);
/*      */     
/*  536 */     for (MinecraftExtendedListener listener : this.extListeners) {
/*  537 */       listener.onMinecraftComparingAssets();
/*      */     }
/*  539 */     List<AssetIndex.AssetObject> assets = compareAssets();
/*      */     
/*  541 */     for (MinecraftExtendedListener listener : this.extListeners) {
/*  542 */       listener.onMinecraftDownloading();
/*      */     }
/*      */     
/*      */     try {
/*  546 */       versionSyncInfoContainer = this.vm.downloadVersion(this.versionSync, this.settings.getBoolean("skin.status.checkbox.state"), this.forceUpdate);
/*      */     }
/*  548 */     catch (IOException e) {
/*  549 */       throw new MinecraftException("Cannot download version!", "download-jar", e);
/*      */     } 
/*  551 */     versionSyncInfoContainer.addHandler((DownloadableContainerHandler)new DefaultDownloadableContainerHandler());
/*  552 */     DownloadableContainer assetsContainer = this.am.downloadResources(this.version, assets, this.forceUpdate);
/*  553 */     assetsContainer.addHandler((DownloadableContainerHandler)new DefaultDownloadableContainerHandler());
/*      */     
/*  555 */     DownloadableContainer modPackContainer = this.modpackManager.getContainer(this.version, this.forceUpdate);
/*  556 */     modPackContainer.addHandler((DownloadableContainerHandler)new DefaultDownloadableContainerHandler());
/*      */     
/*  558 */     for (MinecraftLauncherAssistantInterface assistant : this.assistants) {
/*  559 */       assistant.collectResources(this);
/*      */     }
/*  561 */     DownloadableContainer logContainer = this.logClientConfigurationComponent.getContainer(this.version, this.gameDir, this.forceUpdate);
/*      */     
/*  563 */     this.downloader.add(logContainer);
/*      */     
/*  565 */     this.downloader.add(assetsContainer);
/*  566 */     this.downloader.add((DownloadableContainer)versionSyncInfoContainer);
/*  567 */     this.downloader.add(modPackContainer);
/*  568 */     List<DownloadableContainer> list = Lists.newArrayList(this.downloader.getDownloadableContainers());
/*      */     
/*  570 */     this.downloader.startDownloadAndWait();
/*  571 */     for (DownloadableContainer container : list) {
/*  572 */       if (container.isAborted())
/*  573 */         throw new MinecraftLauncherAborted(new AbortedDownloadException()); 
/*  574 */       if (!container.getErrors().isEmpty() && 
/*  575 */         container.isRequireAllFiles()) {
/*      */         
/*  577 */         String message = "download";
/*  578 */         if (Objects.nonNull(((Throwable)container.getErrors().get(0)).getCause()) && ((Throwable)container
/*  579 */           .getErrors().get(0)).getCause() instanceof java.nio.file.FileSystemException)
/*  580 */           message = "delete.file"; 
/*  581 */         throw new MinecraftException(" ", message, (Throwable)container.getErrors().get(0));
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  586 */     for (DownloadableContainer container : list) {
/*  587 */       if (!container.getErrors().isEmpty() && 
/*  588 */         !container.isRequireAllFiles()) {
/*  589 */         Alert.showWarning(Localizable.get("version.error.message.title"), 
/*  590 */             Localizable.getByKeys("version.error.message", new Object[0]), ((Throwable)container.getErrors().get(0)).getMessage() + " ->" + ((Throwable)container
/*  591 */             .getErrors().get(0)).getCause().getMessage());
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  597 */       this.vm.getLocalList().saveVersion(this.deJureVersion);
/*  598 */     } catch (IOException e) {
/*  599 */       log(new Object[] { "Cannot save version!", e });
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void constructProcess() throws MinecraftException {
/*  605 */     this.version = TLauncher.getInstance().getTLauncherManager().createUpdatedVersion(this.version, true);
/*  606 */     for (MinecraftExtendedListener listener : this.extListeners) {
/*  607 */       listener.onMinecraftReconstructingAssets();
/*      */     }
/*      */     try {
/*  610 */       this.localAssetsDir = reconstructAssets();
/*  611 */     } catch (IOException e) {
/*  612 */       throw new MinecraftException("Cannot recounstruct assets!", "download-jar", e);
/*      */     } 
/*      */     
/*  615 */     for (MinecraftExtendedListener listener : this.extListeners) {
/*  616 */       listener.onMinecraftUnpackingNatives();
/*      */     }
/*      */     try {
/*  619 */       unpackNatives(this.forceUpdate);
/*  620 */     } catch (IOException e) {
/*  621 */       throw new MinecraftException("Cannot unpack natives!", "unpack-natives", e);
/*      */     } 
/*      */     
/*  624 */     checkAborted();
/*      */     
/*  626 */     for (MinecraftExtendedListener listener : this.extListeners) {
/*  627 */       listener.onMinecraftDeletingEntries();
/*      */     }
/*      */     try {
/*  630 */       deleteEntries();
/*  631 */     } catch (IOException e) {
/*  632 */       throw new MinecraftException("Cannot delete entries!", "delete-entries", e);
/*      */     } 
/*      */     
/*      */     try {
/*  636 */       deleteLibraryEntries();
/*  637 */     } catch (Exception e) {
/*  638 */       throw new MinecraftException("Cannot delete library entries!", "delete-entries", e);
/*      */     } 
/*      */     
/*  641 */     checkAborted();
/*      */     
/*  643 */     log(new Object[] { "Constructing process..." });
/*  644 */     for (MinecraftExtendedListener listener : this.extListeners) {
/*  645 */       listener.onMinecraftConstructing();
/*      */     }
/*  647 */     this.launcher = new JavaProcessLauncher(this.javaDir.getAbsolutePath(), new String[0]);
/*  648 */     this.launcher.directory(this.runningMinecraftDir);
/*      */     
/*  650 */     if (OS.OSX.isCurrent()) {
/*  651 */       File icon = null;
/*      */       
/*      */       try {
/*  654 */         icon = getAssetObject("icons/minecraft.icns");
/*  655 */       } catch (IOException e) {
/*  656 */         log(new Object[] { "Cannot get icon file from assets.", e });
/*      */       } 
/*      */       
/*  659 */       if (icon != null) {
/*  660 */         this.launcher.addCommand("-Xdock:icon=\"" + icon.getAbsolutePath() + "\"", "-Xdock:name=Minecraft");
/*      */       }
/*      */     } 
/*  663 */     this.launcher.addCommands((Object[])getJVMArguments());
/*  664 */     if (this.javaArgs != null) {
/*  665 */       this.launcher.addSplitCommands(this.javaArgs);
/*      */     }
/*  667 */     for (MinecraftLauncherAssistantInterface assistant : this.assistants) {
/*  668 */       assistant.constructJavaArguments(this);
/*      */     }
/*  670 */     this.launcher.addCommand(this.version.getMainClass());
/*      */     
/*  672 */     this.launcher.addCommands((Object[])getMinecraftArguments());
/*      */     
/*  674 */     if (this.fullScreen) {
/*  675 */       this.launcher.addCommand("--fullscreen");
/*      */     }
/*  677 */     if (this.server != null) {
/*      */       
/*  679 */       String[] address = StringUtils.split(this.server.getAddress(), ':');
/*  680 */       address = checkAndReplaceWrongAddress(address);
/*  681 */       switch (address.length) {
/*      */         case 2:
/*  683 */           this.launcher.addCommand("--port", address[1]);
/*      */         
/*      */         case 1:
/*  686 */           this.launcher.addCommand("--server", address[0]);
/*      */           break;
/*      */         default:
/*  689 */           log(new Object[] { "Cannot recognize server:", this.server }); break;
/*      */       } 
/*  691 */       if (this.server.getName() == null) {
/*  692 */         this.server = null;
/*      */       }
/*      */     } 
/*  695 */     if (this.programArgs != null) {
/*  696 */       this.launcher.addSplitCommands(this.programArgs);
/*      */     }
/*  698 */     for (MinecraftLauncherAssistantInterface assistant : this.assistants) {
/*  699 */       assistant.constructProgramArguments(this);
/*      */     }
/*  701 */     log(new Object[] { "Full command: " + this.launcher.getCommandsAsString() });
/*      */   }
/*      */ 
/*      */   
/*      */   private String[] checkAndReplaceWrongAddress(String[] address) {
/*      */     try {
/*  707 */       InetAddress.getByName(address[0]);
/*  708 */     } catch (UnknownHostException e) {
/*  709 */       MinecraftPing p = new MinecraftPing();
/*  710 */       MinecraftPingOptions options = (new MinecraftPingOptions()).setHostname(address[0]);
/*  711 */       if (address.length == 2)
/*  712 */         options.setPort(Integer.parseInt(address[1])); 
/*      */       try {
/*  714 */         p.resolveDNS(options);
/*  715 */         return new String[] { options.getHostname(), String.valueOf(options.getPort()) };
/*  716 */       } catch (Throwable e1) {
/*  717 */         U.log(new Object[] { e1 });
/*      */       } 
/*      */     } 
/*  720 */     return address;
/*      */   }
/*      */   
/*      */   private void checkExtraConditions() {
/*  724 */     checkStep(MinecraftLauncherStep.DOWNLOADING, MinecraftLauncherStep.CONSTRUCTING);
/*  725 */     Configuration conf = TLauncher.getInstance().getConfiguration();
/*  726 */     boolean skinVersion = TLauncher.getInstance().getTLauncherManager().useTLauncherAccount((Version)this.version);
/*  727 */     if (skinVersion && !conf.getBoolean("skin.notification.off")) {
/*  728 */       if (!conf.getBoolean("skin.notification.off.temp")) {
/*  729 */         conf.set("skin.notification.off.temp", Boolean.valueOf(true));
/*  730 */         SwingUtilities.invokeLater(SkinNotification::showMessage);
/*  731 */         TLauncher.getInstance().getVersionManager().startRefresh(true);
/*  732 */         throw new MinecraftLauncherAborted("shown skin message");
/*      */       } 
/*  734 */       conf.set("skin.notification.off.temp", Boolean.valueOf(false));
/*      */     }
/*  736 */     else if (this.account.getType().equals(Account.AccountType.TLAUNCHER) && !skinVersion && 
/*  737 */       !conf.getBoolean("skin.not.work.notification.hide")) {
/*  738 */       String message = String.format(Localizable.get("skin.not.work.notification"), new Object[] {
/*  739 */             ImageCache.getRes("tlauncher-user.png").toExternalForm(), 
/*  740 */             ImageCache.getRes("need-tl-version-for-skins.png").toExternalForm() });
/*  741 */       if (Alert.showWarningMessageWithCheckBox("skin.notification.title", message, 500))
/*  742 */         conf.set("skin.not.work.notification.hide", Boolean.valueOf(true)); 
/*      */     } 
/*  744 */     if (Objects.nonNull(this.server) && this.server.isOfficialAccount() && 
/*  745 */       !Account.AccountType.OFFICIAL_ACCOUNTS.contains(this.account.getType())) {
/*  746 */       int res = Alert.showConfirmDialog(0, 2, "", 
/*  747 */           Localizable.get("account.not.proper.warn"), null, Localizable.get("ui.go.no.matter"), 
/*  748 */           Localizable.get("ui.no"));
/*  749 */       if (res == 1)
/*  750 */         throw new MinecraftLauncherAborted("shown mojang skin message"); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private File reconstructAssets() throws IOException {
/*  755 */     String assetVersion = (this.version.getAssets() == null) ? "legacy" : this.version.getAssets();
/*  756 */     File indexFile = new File(this.assetsIndexesDir, assetVersion + ".json");
/*  757 */     File virtualRoot = new File(new File(this.globalAssetsDir, "virtual"), assetVersion);
/*      */     
/*  759 */     if (!indexFile.isFile()) {
/*  760 */       log(new Object[] { "No assets index file " + virtualRoot + "; can't reconstruct assets" });
/*  761 */       return virtualRoot;
/*      */     } 
/*      */     
/*  764 */     AssetIndex index = (AssetIndex)this.gson.fromJson(FileUtil.readFile(indexFile), AssetIndex.class);
/*  765 */     if (index.isVirtual()) {
/*  766 */       log(new Object[] { "Reconstructing virtual assets folder at " + virtualRoot });
/*  767 */       for (Map.Entry<String, AssetIndex.AssetObject> entry : (Iterable<Map.Entry<String, AssetIndex.AssetObject>>)index.getFileMap().entrySet()) {
/*      */         
/*  769 */         checkAborted();
/*      */         
/*  771 */         File target = new File(virtualRoot, entry.getKey());
/*      */         
/*  773 */         File original = new File(new File(this.assetsObjectsDir, ((AssetIndex.AssetObject)entry.getValue()).getHash().substring(0, 2)), ((AssetIndex.AssetObject)entry.getValue()).getHash());
/*      */         
/*  775 */         if (!original.isFile()) {
/*  776 */           log(new Object[] { "Skipped reconstructing:", original }); continue;
/*  777 */         }  if (this.forceUpdate || !target.isFile()) {
/*  778 */           FileUtils.copyFile(original, target, false);
/*  779 */           log(new Object[] { original, "->", target });
/*      */         } 
/*      */       } 
/*      */       
/*  783 */       FileUtil.writeFile(new File(virtualRoot, ".lastused"), this.dateAdapter.toString(new Date()));
/*      */     } 
/*  785 */     return virtualRoot;
/*      */   }
/*      */   
/*      */   private File getAssetObject(String name) throws IOException {
/*  789 */     String assetVersion = this.version.getAssets();
/*  790 */     File indexFile = new File(this.assetsIndexesDir, assetVersion + ".json");
/*      */     
/*  792 */     AssetIndex index = (AssetIndex)this.gson.fromJson(FileUtil.readFile(indexFile), AssetIndex.class);
/*      */     
/*  794 */     if (index.getFileMap() == null)
/*  795 */       throw new IOException("Cannot get filemap!"); 
/*  796 */     if (Objects.isNull(index.getFileMap().get(name)))
/*  797 */       return null; 
/*  798 */     String hash = ((AssetIndex.AssetObject)index.getFileMap().get(name)).getHash();
/*  799 */     return new File(this.assetsObjectsDir, hash.substring(0, 2) + "/" + hash);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void unpackNatives(boolean force) throws IOException {
/*  805 */     log(new Object[] { "Unpacking natives..." });
/*  806 */     Collection<Library> libraries = this.version.getRelevantLibraries();
/*      */     
/*  808 */     OS os = OS.CURRENT;
/*      */ 
/*      */ 
/*      */     
/*  812 */     if (force) {
/*  813 */       this.nativeDir.delete();
/*      */     }
/*  815 */     for (Library library : libraries) {
/*  816 */       Map<OS, String> nativesPerOs = library.getNatives();
/*      */       
/*  818 */       if (nativesPerOs != null && nativesPerOs.get(os) != null) {
/*      */         ZipFile zip;
/*  820 */         File file = new File(MinecraftUtil.getWorkingDirectory(), "libraries/" + library.getArtifactPath(nativesPerOs.get(os)));
/*      */         
/*  822 */         if (!file.isFile()) {
/*  823 */           throw new IOException("Required archive doesn't exist: " + file.getAbsolutePath());
/*      */         }
/*      */         try {
/*  826 */           zip = new ZipFile(file);
/*  827 */         } catch (IOException e) {
/*  828 */           throw new IOException("Error opening ZIP archive: " + file.getAbsolutePath(), e);
/*      */         } 
/*      */         
/*  831 */         ExtractRules extractRules = library.getExtractRules();
/*  832 */         Enumeration<? extends ZipEntry> entries = zip.entries();
/*      */         
/*  834 */         while (entries.hasMoreElements()) {
/*  835 */           ZipEntry entry = entries.nextElement();
/*  836 */           if (extractRules == null || extractRules.shouldExtract(entry.getName())) {
/*  837 */             File targetFile = new File(this.nativeDir, entry.getName());
/*      */             
/*  839 */             if (!force && targetFile.isFile()) {
/*      */               continue;
/*      */             }
/*  842 */             if (targetFile.getParentFile() != null) {
/*  843 */               targetFile.getParentFile().mkdirs();
/*      */             }
/*  845 */             if (!entry.isDirectory()) {
/*  846 */               BufferedInputStream inputStream = new BufferedInputStream(zip.getInputStream(entry));
/*      */               
/*  848 */               byte[] buffer = new byte[2048];
/*  849 */               FileOutputStream outputStream = new FileOutputStream(targetFile);
/*  850 */               BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
/*      */               
/*      */               int length;
/*  853 */               while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
/*  854 */                 bufferedOutputStream.write(buffer, 0, length);
/*      */               }
/*  856 */               inputStream.close();
/*  857 */               bufferedOutputStream.close();
/*      */             } 
/*      */           } 
/*      */         } 
/*  861 */         zip.close();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void deleteEntries() throws IOException {
/*  867 */     List<String> entries = this.version.getDeleteEntries();
/*      */     
/*  869 */     if (entries == null || entries.size() == 0)
/*      */       return; 
/*  871 */     log(new Object[] { "Removing entries..." });
/*      */     
/*  873 */     File file = this.version.getFile(this.gameDir);
/*  874 */     removeFrom(file, entries);
/*      */   }
/*      */   
/*      */   private void deleteLibraryEntries() throws IOException {
/*  878 */     for (Library lib : this.version.getLibraries()) {
/*  879 */       List<String> entries = lib.getDeleteEntriesList();
/*      */       
/*  881 */       if (entries == null || entries.isEmpty()) {
/*      */         continue;
/*      */       }
/*  884 */       log(new Object[] { "Processing entries of", lib.getName() });
/*  885 */       removeFrom(new File(this.gameDir, "libraries/" + lib.getArtifactPath()), entries);
/*      */     } 
/*      */   }
/*      */   
/*      */   private String constructClassPath(CompleteVersion version) throws MinecraftException {
/*  890 */     log(new Object[] { "Constructing classpath..." });
/*      */     
/*  892 */     StringBuilder result = new StringBuilder();
/*  893 */     Collection<File> classPath = version.getClassPath(OS.CURRENT, this.gameDir);
/*  894 */     String separator = System.getProperty("path.separator");
/*      */     
/*  896 */     for (File file : classPath) {
/*  897 */       if (!file.isFile())
/*  898 */         throw new MinecraftException("Classpath is not found: " + file, "classpath", new Object[] { file }); 
/*  899 */       if (result.length() > 0)
/*  900 */         result.append(separator); 
/*  901 */       result.append(file.getAbsolutePath());
/*      */     } 
/*      */     
/*  904 */     return result.toString();
/*      */   }
/*      */   
/*      */   private String[] getMinecraftArguments() {
/*  908 */     log(new Object[] { "Getting Minecraft arguments..." });
/*  909 */     List<String> list = new ArrayList<>();
/*  910 */     List<Argument> arguments = (List<Argument>)this.version.getArguments().get(ArgumentType.GAME);
/*  911 */     for (Argument argument : arguments) {
/*  912 */       if (argument.appliesToCurrentEnvironment()) {
/*  913 */         list.addAll(Arrays.asList(argument.getValues()));
/*      */       }
/*      */     } 
/*  916 */     Map<String, String> map = new HashMap<>();
/*  917 */     String assets = this.version.getAssets();
/*  918 */     map.put("auth_username", this.account.getUsername());
/*  919 */     map.put("auth_session", String.format("token:%s:%s", new Object[] { this.account.getAccessToken(), this.account.getProfile().getId() }));
/*      */     
/*  921 */     if (Account.AccountType.OFFICIAL_ACCOUNTS.contains(this.account.getType())) {
/*  922 */       map.put("auth_access_token", this.account.getAccessToken());
/*      */     } else {
/*  924 */       map.put("auth_access_token", "null");
/*  925 */     }  map.put("user_properties", this.gson.toJson(this.account.getProperties()));
/*  926 */     if (this.settings.getBoolean("skip.account.property"))
/*  927 */       map.put("user_properties", "{}"); 
/*  928 */     map.put("auth_uuid", this.account.getUUID());
/*  929 */     if (Account.AccountType.MICROSOFT.equals(this.account.getType())) {
/*  930 */       map.put("user_type", "msa");
/*      */     } else {
/*  932 */       map.put("user_type", "mojang");
/*  933 */     }  map.put("profile_name", this.account.getProfile().getName());
/*  934 */     map.put("auth_player_name", this.account.getDisplayName());
/*  935 */     map.put("version_type", this.version.getReleaseType().toString());
/*  936 */     map.put("version_name", this.version.getID());
/*  937 */     map.put("game_directory", this.runningMinecraftDir.getAbsolutePath());
/*  938 */     map.put("game_assets", this.localAssetsDir.getAbsolutePath());
/*      */     
/*  940 */     map.put("assets_root", this.globalAssetsDir.getAbsolutePath());
/*  941 */     map.put("assets_index_name", (assets == null) ? "legacy" : assets);
/*  942 */     map.put("resolution_width", String.valueOf(this.windowSize[0]));
/*  943 */     map.put("resolution_height", String.valueOf(this.windowSize[1]));
/*      */     
/*  945 */     StrSubstitutor substitutor = new StrSubstitutor(map);
/*  946 */     String[] split = list.<String>toArray(new String[0]);
/*  947 */     for (int i = 0; i < split.length; i++) {
/*  948 */       split[i] = substitutor.replace(split[i]);
/*      */     }
/*  950 */     return split;
/*      */   }
/*      */ 
/*      */   
/*      */   private String[] getJVMArguments() throws MinecraftException {
/*  955 */     List<String> list = new ArrayList<>();
/*  956 */     Map<String, String> map = new HashMap<>();
/*  957 */     if (OS.CURRENT == OS.WINDOWS && System.getProperty("os.version").startsWith("10.")) {
/*  958 */       list.add("-Dos.name=Windows 10");
/*  959 */       list.add("-Dos.version=10.0");
/*      */     } 
/*  961 */     List<Argument> arguments = (List<Argument>)this.version.getArguments().get(ArgumentType.JVM);
/*  962 */     for (Argument argument : arguments) {
/*  963 */       if (argument.appliesToCurrentEnvironment()) {
/*  964 */         list.addAll(Arrays.asList(argument.getValues()));
/*      */       }
/*      */     } 
/*  967 */     list.add("-Xmx" + this.ramSize + "M");
/*      */     
/*  969 */     map.put("game_directory", this.runningMinecraftDir.getAbsolutePath());
/*      */     
/*  971 */     if (OS.Arch.TOTAL_RAM_MB < 5000 && getJavaVersion() == 8) {
/*  972 */       list.add("-XX:+UseConcMarkSweepGC");
/*  973 */     } else if (Objects.nonNull(this.java)) {
/*  974 */       list.addAll(this.java.getArgs());
/*      */     } else {
/*  976 */       MinecraftUtil.configureG1GC(list);
/*      */     } 
/*  978 */     list.add("-Dminecraft.applet.TargetDirectory=${game_directory}");
/*      */     
/*  980 */     if (this.logClientConfigurationComponent.isNotNullLogClient(this.version)) {
/*  981 */       LogClient f = this.logClientConfigurationComponent.getLogClient(this.version);
/*  982 */       list.add(f.getArgument());
/*  983 */       map.put("path", this.logClientConfigurationComponent.buildPathLogXmlConfiguration(this.gameDir, f).toString());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  988 */     if (this.version.isModpack()) {
/*  989 */       list.add("-DLibLoader.modsFolder=" + ModpackUtil.getPathByVersion((Version)this.version, new String[] { "mods" }));
/*      */     }
/*  991 */     String[] split = list.<String>toArray(new String[0]);
/*      */     
/*  993 */     map.put("natives_directory", this.nativeDir.toString());
/*  994 */     String classpath = constructClassPath(this.version);
/*  995 */     map.put("classpath", classpath);
/*  996 */     map.put("legacyClassPath", classpath);
/*  997 */     map.put("launcher_name", "minecraft-launcher");
/*  998 */     map.put("launcher_version", "2.3.173");
/*  999 */     map.put("classpath_separator", File.pathSeparator);
/* 1000 */     map.put("library_directory", String.join(File.separator, new CharSequence[] { this.gameDir.getAbsolutePath(), "libraries" }));
/* 1001 */     map.put("version_name", this.versionName);
/*      */     
/* 1003 */     StrSubstitutor substitutor = new StrSubstitutor(map);
/* 1004 */     for (int i = 0; i < split.length; i++) {
/* 1005 */       split[i] = substitutor.replace(split[i]);
/*      */     }
/* 1007 */     return split;
/*      */   }
/*      */   
/*      */   private List<AssetIndex.AssetObject> compareAssets() {
/* 1011 */     migrateOldAssets();
/*      */     
/* 1013 */     log(new Object[] { "Comparing assets..." });
/* 1014 */     long start = System.currentTimeMillis();
/*      */     
/* 1016 */     List<AssetIndex.AssetObject> result = this.am.checkResources(this.version, !this.forceUpdate);
/*      */     
/* 1018 */     log(new Object[] { "finished comparing assets: " + (System.currentTimeMillis() - start) + " ms." });
/*      */     
/* 1020 */     return result;
/*      */   }
/*      */   
/*      */   private void migrateOldAssets() {
/* 1024 */     if (!this.globalAssetsDir.isDirectory()) {
/*      */       return;
/*      */     }
/* 1027 */     File skinsDir = new File(this.globalAssetsDir, "skins");
/*      */     
/* 1029 */     if (skinsDir.isDirectory()) {
/* 1030 */       FileUtil.deleteDirectory(skinsDir);
/*      */     }
/*      */ 
/*      */     
/* 1034 */     IOFileFilter migratableFilter = FileFilterUtils.notFileFilter(FileFilterUtils.or(new IOFileFilter[] { FileFilterUtils.nameFileFilter("indexes"), FileFilterUtils.nameFileFilter("log_configs"), 
/* 1035 */             FileFilterUtils.nameFileFilter("objects"), FileFilterUtils.nameFileFilter("virtual") }));
/* 1036 */     for (File file : new TreeSet(FileUtils.listFiles(this.globalAssetsDir, TrueFileFilter.TRUE, migratableFilter))) {
/* 1037 */       String hash = FileUtil.getDigest(file, "SHA-1", 40);
/* 1038 */       if (hash != null) {
/* 1039 */         File destinationFile = new File(this.assetsObjectsDir, hash.substring(0, 2) + "/" + hash);
/*      */         
/* 1041 */         if (!destinationFile.exists()) {
/* 1042 */           log(new Object[] { "Migrated old asset", file, "into", destinationFile });
/*      */           try {
/* 1044 */             FileUtils.copyFile(file, destinationFile);
/* 1045 */           } catch (IOException e) {
/* 1046 */             log(new Object[] { "Couldn't migrate old asset", e });
/*      */           } 
/*      */         } 
/*      */       } 
/* 1050 */       FileUtils.deleteQuietly(file);
/*      */     } 
/*      */     
/* 1053 */     File[] assets = this.globalAssetsDir.listFiles();
/* 1054 */     if (assets != null) {
/* 1055 */       for (File file : assets) {
/* 1056 */         if (!file.getName().equals("indexes") && !file.getName().equals("objects") && 
/* 1057 */           !file.getName().equals("log_configs") && !file.getName().equals("virtual")) {
/* 1058 */           log(new Object[] { "Cleaning up old assets directory", file, "after migration" });
/* 1059 */           FileUtils.deleteQuietly(file);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private void launchMinecraft() throws MinecraftException {
/* 1066 */     checkStep(MinecraftLauncherStep.CONSTRUCTING, MinecraftLauncherStep.LAUNCHING);
/* 1067 */     log(new Object[] { "Launching Minecraft..." });
/*      */     
/* 1069 */     for (MinecraftListener listener : this.listeners) {
/* 1070 */       listener.onMinecraftLaunch();
/*      */     }
/* 1072 */     log(new Object[] { "skin system is activated: " + getConfiguration().getBoolean("skin.status.checkbox.state") });
/* 1073 */     log(new Object[] { "Starting Minecraft " + this.versionName + "..." });
/* 1074 */     log(new Object[] { "Launching in:", this.runningMinecraftDir.getAbsolutePath() });
/*      */     
/* 1076 */     this.startupTime = System.currentTimeMillis();
/* 1077 */     this.console.setLauncherToKillProcess(this);
/*      */     
/* 1079 */     U.gc();
/*      */     try {
/* 1081 */       this.launcher.setListener(this);
/* 1082 */       this.process = this.launcher.start();
/*      */       
/* 1084 */       this.minecraftWorking = true;
/* 1085 */     } catch (Exception e) {
/* 1086 */       notifyClose();
/* 1087 */       throw new MinecraftException("Cannot start the game!", "start", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void postLaunch() {
/* 1092 */     checkStep(MinecraftLauncherStep.LAUNCHING, MinecraftLauncherStep.POSTLAUNCH);
/* 1093 */     log(new Object[] { "Processing post-launch actions. Assist launch:", Boolean.valueOf(this.assistLaunch) });
/*      */     
/* 1095 */     for (MinecraftExtendedListener listener : this.extListeners) {
/* 1096 */       listener.onMinecraftPostLaunch();
/*      */     }
/* 1098 */     if (this.assistLaunch) {
/*      */       
/* 1100 */       waitForClose();
/*      */     } else {
/* 1102 */       U.sleepFor(30000L);
/*      */       
/* 1104 */       if (this.minecraftWorking)
/* 1105 */         TLauncher.kill(); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void killProcess() {
/* 1110 */     if (!this.minecraftWorking) {
/* 1111 */       throw new IllegalStateException();
/*      */     }
/* 1113 */     log(new Object[] { "Killing Minecraft forcefully" });
/*      */     
/* 1115 */     this.killed = true;
/* 1116 */     this.process.stop();
/*      */   }
/*      */   
/*      */   public void log(Object... o) {
/* 1120 */     U.log(new Object[] { "[Launcher]", o });
/*      */   }
/*      */   
/*      */   private void checkThread() {
/* 1124 */     if (!Thread.currentThread().equals(this.parentThread))
/* 1125 */       throw new IllegalStateException("Illegal thread!"); 
/*      */   }
/*      */   
/*      */   private void checkStep(MinecraftLauncherStep prevStep, MinecraftLauncherStep currentStep) {
/* 1129 */     checkAborted();
/*      */     
/* 1131 */     if (prevStep == null || currentStep == null) {
/* 1132 */       throw new NullPointerException("NULL: " + prevStep + " " + currentStep);
/*      */     }
/*      */     
/* 1135 */     if (!this.step.equals(prevStep)) {
/* 1136 */       throw new IllegalStateException("Called from illegal step: " + this.step);
/*      */     }
/*      */     
/* 1139 */     checkThread();
/*      */     
/* 1141 */     this.step = currentStep;
/*      */   }
/*      */   
/*      */   private void checkAborted() {
/* 1145 */     if (!this.working) {
/* 1146 */       throw new MinecraftLauncherAborted("Aborted at step: " + this.step);
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkWorking() {
/* 1151 */     if (this.working) {
/* 1152 */       throw new IllegalStateException("Launcher is working!");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onJavaProcessLog(JavaProcess jp, String line) {
/* 1158 */     if (this.firstLine) {
/* 1159 */       this.firstLine = false;
/*      */       
/* 1161 */       U.plog(new Object[] { "===============================================================================================" });
/*      */     } 
/* 1163 */     this.xmlLogDeserialization.addToLog(line);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onJavaProcessEnded(JavaProcess jp) {
/* 1169 */     notifyClose();
/* 1170 */     this.console.setLauncherToKillProcess(null);
/*      */     
/* 1172 */     int exit = jp.getExitCode();
/*      */     
/* 1174 */     log(new Object[] { "Minecraft closed with exit code: " + exit });
/*      */     
/* 1176 */     this.exitCode = exit;
/* 1177 */     Crash crash = this.killed ? null : this.descriptor.scan();
/* 1178 */     if (crash == null) {
/*      */       
/* 1180 */       if (!this.assistLaunch)
/* 1181 */         TLauncher.kill(); 
/*      */     } else {
/* 1183 */       this.console.show(true);
/* 1184 */       for (MinecraftListener listener : this.listeners) {
/* 1185 */         listener.onMinecraftCrash(crash);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onJavaProcessError(JavaProcess jp, Throwable e) {
/* 1191 */     notifyClose();
/*      */     
/* 1193 */     for (MinecraftListener listener : this.listeners)
/* 1194 */       listener.onMinecraftError(e); 
/*      */   }
/*      */   
/*      */   private synchronized void waitForClose() {
/* 1198 */     while (this.minecraftWorking) {
/*      */       try {
/* 1200 */         wait();
/* 1201 */       } catch (InterruptedException interruptedException) {}
/*      */     } 
/*      */   }
/*      */   
/*      */   private synchronized void notifyClose() {
/* 1206 */     this.minecraftWorking = false;
/*      */     
/* 1208 */     if (System.currentTimeMillis() - this.startupTime < 5000L) {
/* 1209 */       U.sleepFor(1000L);
/*      */     }
/*      */     
/* 1212 */     notifyAll();
/*      */     
/* 1214 */     for (MinecraftListener listener : this.listeners)
/* 1215 */       listener.onMinecraftClose(); 
/* 1216 */     if (this.clearNatives && Files.exists(this.nativeDir.toPath(), new java.nio.file.LinkOption[0])) {
/* 1217 */       FileUtil.deleteDirectory(this.nativeDir);
/*      */     }
/*      */   }
/*      */   
/*      */   private void removeFrom(File zipFile, List<String> entries) throws IOException {
/* 1222 */     File tempFile = File.createTempFile(zipFile.getName(), null);
/* 1223 */     tempFile.delete();
/* 1224 */     tempFile.deleteOnExit();
/* 1225 */     Path moved = Files.move(zipFile.toPath(), tempFile.toPath(), new java.nio.file.CopyOption[0]);
/* 1226 */     if (Files.notExists(moved, new java.nio.file.LinkOption[0])) {
/* 1227 */       throw new IOException("Could not move the file " + zipFile
/* 1228 */           .getAbsolutePath() + " -> " + tempFile.getAbsolutePath());
/*      */     }
/* 1230 */     log(new Object[] { "Removing entries from", zipFile });
/*      */     
/* 1232 */     byte[] buf = new byte[1024];
/*      */     
/* 1234 */     ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(tempFile)), StandardCharsets.UTF_8);
/*      */     
/* 1236 */     ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)), StandardCharsets.UTF_8);
/*      */     
/* 1238 */     ZipEntry entry = zin.getNextEntry();
/*      */     
/* 1240 */     while (entry != null) {
/* 1241 */       String name = entry.getName();
/*      */       
/* 1243 */       if (entries.contains(name)) {
/* 1244 */         log(new Object[] { "Removed:", name });
/*      */       } else {
/* 1246 */         zout.putNextEntry(new ZipEntry(name));
/*      */         
/*      */         int len;
/* 1249 */         while ((len = zin.read(buf)) > 0) {
/* 1250 */           zout.write(buf, 0, len);
/*      */         }
/*      */       } 
/*      */       
/* 1254 */       entry = zin.getNextEntry();
/*      */     } 
/*      */     
/* 1257 */     zin.close();
/* 1258 */     zout.close();
/* 1259 */     tempFile.delete();
/*      */   }
/*      */   
/*      */   public enum MinecraftLauncherStep {
/* 1263 */     NONE, COLLECTING, DOWNLOADING, CONSTRUCTING, LAUNCHING, POSTLAUNCH;
/*      */   }
/*      */   
/*      */   public enum ConsoleVisibility {
/* 1267 */     ALWAYS, ON_CRASH, NONE;
/*      */   }
/*      */   
/*      */   public static class MinecraftLauncherAborted extends RuntimeException {
/*      */     private static final long serialVersionUID = -3001265213093607559L;
/*      */     
/*      */     MinecraftLauncherAborted(String message) {
/* 1274 */       super(message);
/*      */     }
/*      */     
/*      */     MinecraftLauncherAborted(Throwable cause) {
/* 1278 */       super(cause);
/*      */     }
/*      */   }
/*      */   
/*      */   public File getRunningMinecraftDir() {
/* 1283 */     return this.runningMinecraftDir;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\MinecraftLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */