/*      */ package org.tlauncher.tlauncher.managers;
/*      */ 
/*      */ import by.gdev.http.download.service.GsonService;
/*      */ import by.gdev.util.model.download.Repo;
/*      */ import com.github.junrar.Archive;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.gson.Gson;
/*      */ import com.google.gson.reflect.TypeToken;
/*      */ import com.google.inject.Inject;
/*      */ import com.google.inject.Singleton;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.io.File;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.CopyOption;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.NoSuchFileException;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.Paths;
/*      */ import java.nio.file.StandardCopyOption;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.zip.ZipFile;
/*      */ import javax.inject.Named;
/*      */ import javax.swing.SwingUtilities;
/*      */ import net.minecraft.launcher.Http;
/*      */ import net.minecraft.launcher.versions.CompleteVersion;
/*      */ import net.minecraft.launcher.versions.Version;
/*      */ import org.apache.commons.io.FileUtils;
/*      */ import org.apache.commons.io.FilenameUtils;
/*      */ import org.apache.commons.io.IOUtils;
/*      */ import org.apache.commons.io.filefilter.DirectoryFileFilter;
/*      */ import org.apache.commons.io.filefilter.FileFilterUtils;
/*      */ import org.apache.commons.io.filefilter.IOFileFilter;
/*      */ import org.apache.commons.io.filefilter.TrueFileFilter;
/*      */ import org.apache.commons.lang3.StringUtils;
/*      */ import org.apache.http.HttpEntity;
/*      */ import org.apache.http.HttpResponse;
/*      */ import org.apache.http.client.ClientProtocolException;
/*      */ import org.apache.http.client.config.RequestConfig;
/*      */ import org.apache.http.client.methods.CloseableHttpResponse;
/*      */ import org.apache.http.client.methods.HttpDelete;
/*      */ import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
/*      */ import org.apache.http.client.methods.HttpGet;
/*      */ import org.apache.http.client.methods.HttpPatch;
/*      */ import org.apache.http.client.methods.HttpPost;
/*      */ import org.apache.http.client.methods.HttpPut;
/*      */ import org.apache.http.client.methods.HttpRequestBase;
/*      */ import org.apache.http.client.methods.HttpUriRequest;
/*      */ import org.apache.http.entity.ContentType;
/*      */ import org.apache.http.entity.StringEntity;
/*      */ import org.apache.http.impl.client.CloseableHttpClient;
/*      */ import org.apache.http.util.EntityUtils;
/*      */ import org.tlauncher.exceptions.ParseModPackException;
/*      */ import org.tlauncher.modpack.domain.client.ForgeVersionDTO;
/*      */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*      */ import org.tlauncher.modpack.domain.client.GameEntityDependencyDTO;
/*      */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*      */ import org.tlauncher.modpack.domain.client.MapDTO;
/*      */ import org.tlauncher.modpack.domain.client.ModDTO;
/*      */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*      */ import org.tlauncher.modpack.domain.client.PictureType;
/*      */ import org.tlauncher.modpack.domain.client.ResourcePackDTO;
/*      */ import org.tlauncher.modpack.domain.client.ShaderpackDTO;
/*      */ import org.tlauncher.modpack.domain.client.SubModpackDTO;
/*      */ import org.tlauncher.modpack.domain.client.share.CategoryDTO;
/*      */ import org.tlauncher.modpack.domain.client.share.DependencyType;
/*      */ import org.tlauncher.modpack.domain.client.share.GameEntitySort;
/*      */ import org.tlauncher.modpack.domain.client.share.GameType;
/*      */ import org.tlauncher.modpack.domain.client.share.InfoMod;
/*      */ import org.tlauncher.modpack.domain.client.share.MinecraftVersionDTO;
/*      */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*      */ import org.tlauncher.modpack.domain.client.share.ParsedElementDTO;
/*      */ import org.tlauncher.modpack.domain.client.share.StateGameElement;
/*      */ import org.tlauncher.modpack.domain.client.site.CommonPage;
/*      */ import org.tlauncher.modpack.domain.client.version.MapMetadataDTO;
/*      */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*      */ import org.tlauncher.modpack.domain.client.version.ModVersionDTO;
/*      */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*      */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*      */ import org.tlauncher.tlauncher.configuration.InnerConfiguration;
/*      */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*      */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*      */ import org.tlauncher.tlauncher.downloader.DownloadableContainerHandler;
/*      */ import org.tlauncher.tlauncher.downloader.mods.GameEntityHandler;
/*      */ import org.tlauncher.tlauncher.downloader.mods.MapDownloader;
/*      */ import org.tlauncher.tlauncher.downloader.mods.UnzipEntityDownloader;
/*      */ import org.tlauncher.tlauncher.entity.MinecraftInstance;
/*      */ import org.tlauncher.tlauncher.exceptions.GameEntityNotFound;
/*      */ import org.tlauncher.tlauncher.exceptions.RequiredRemoteVersionError;
/*      */ import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
/*      */ import org.tlauncher.tlauncher.exceptions.SameMapFoldersException;
/*      */ import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
/*      */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*      */ import org.tlauncher.tlauncher.minecraft.crash.Crash;
/*      */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*      */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
/*      */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*      */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*      */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*      */ import org.tlauncher.tlauncher.ui.MainPane;
/*      */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.GameEntityAdapter;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
/*      */ import org.tlauncher.tlauncher.ui.listener.mods.UpdateFavoriteValueListener;
/*      */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*      */ import org.tlauncher.tlauncher.ui.modpack.HandleInstallModpackElementFrame;
/*      */ import org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame;
/*      */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*      */ import org.tlauncher.tlauncher.ui.scenes.PseudoScene;
/*      */ import org.tlauncher.util.FileUtil;
/*      */ import org.tlauncher.util.OS;
/*      */ import org.tlauncher.util.TlauncherUtil;
/*      */ import org.tlauncher.util.U;
/*      */ import org.tlauncher.util.async.AsyncThread;
/*      */ 
/*      */ 
/*      */ @Singleton
/*      */ public class ModpackManager
/*      */   implements VersionManagerListener, MinecraftListener, ItemListener
/*      */ {
/*  139 */   private final Map<GameType, List<GameEntityListener>> gameListeners = Collections.synchronizedMap(new HashMap<GameType, List<GameEntityListener>>()
/*      */       {
/*      */       
/*      */       });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  148 */   private final File STATUS_MODPACK_FILE = FileUtil.getRelativeConfigFile("status.modpack");
/*      */   @Inject
/*      */   @Named("GsonCompleteVersion")
/*      */   private Gson gson;
/*      */   @Inject
/*      */   private TLauncher tLauncher;
/*      */   @Inject
/*      */   private CloseableHttpClient closeableHttpClient;
/*      */   @Inject
/*      */   private GsonService gsonService;
/*      */   @Inject
/*      */   @Named("anyVersionType")
/*      */   private NameIdDTO anyVersionType;
/*  161 */   private final InnerConfiguration innerConfiguration = TLauncher.getInnerSettings();
/*      */   
/*      */   private InfoMod infoMod;
/*  164 */   private Set<Long> statusModpackElement = new HashSet<>();
/*  165 */   private final AtomicBoolean addedVersionListener = new AtomicBoolean(false); private String modpackApiURL; @Inject
/*      */   private CloseableHttpClient client; @Inject
/*      */   private RequestConfig requestConfig; @Inject
/*      */   @Named("modpackExecutorService")
/*      */   private ExecutorService modpackExecutorService;
/*      */   public ExecutorService getModpackExecutorService() {
/*  171 */     return this.modpackExecutorService;
/*      */   }
/*      */   
/*      */   public Map<GameType, List<CategoryDTO>> getMap() {
/*  175 */     return this.map;
/*      */   }
/*  177 */   private Map<GameType, List<CategoryDTO>> map = Collections.synchronizedMap(new HashMap<>()); public List<NameIdDTO> getMinecraftVersionTypes() {
/*  178 */     return this.minecraftVersionTypes;
/*  179 */   } private List<NameIdDTO> minecraftVersionTypes = Collections.synchronizedList(new ArrayList<>()); public Map<NameIdDTO, List<GameVersionDTO>> getGameVersions() {
/*  180 */     return this.gameVersions;
/*  181 */   } private Map<NameIdDTO, List<GameVersionDTO>> gameVersions = Collections.synchronizedMap(new HashMap<>());
/*  182 */   private Map<String, Set<Long>> favoriteGameEntityIds = Collections.synchronizedMap(new HashMap<>());
/*      */   
/*      */   public ModpackManager() {
/*  185 */     GameEntityAdapter listener = new GameEntityAdapter()
/*      */       {
/*      */         public void installEntity(CompleteVersion e)
/*      */         {
/*  189 */           update();
/*      */         }
/*      */         
/*      */         private void update() {
/*  193 */           TLauncher.getInstance().getVersionManager().getLocalList().refreshVersions();
/*      */         }
/*      */ 
/*      */         
/*      */         public void removeCompleteVersion(CompleteVersion e) {
/*  198 */           update();
/*      */         }
/*      */       };
/*  201 */     ((List<GameEntityAdapter>)this.gameListeners.get(GameType.MODPACK)).add(listener);
/*  202 */     this.modpackApiURL = this.innerConfiguration.get("modpack.operation.url");
/*      */   }
/*      */   
/*      */   private void log(Object... s) {
/*  206 */     U.log(new Object[] { "[Modpack] ", s });
/*      */   }
/*      */   
/*      */   public synchronized void loadInfo() {
/*  210 */     this.infoMod = new InfoMod();
/*      */     
/*  212 */     (this.tLauncher.getFrame()).mp.modpackScene.prepareView(getModpackVersions());
/*      */     
/*  214 */     if (!this.addedVersionListener.get()) {
/*  215 */       this.addedVersionListener.set(true);
/*  216 */       this.tLauncher.getVersionManager().addListener(this);
/*      */     } 
/*  218 */     readStatusGameElement();
/*  219 */     if (!this.statusModpackElement.isEmpty()) {
/*      */       try {
/*  221 */         importUserGameEntities((List<GameEntityDTO>)this.statusModpackElement.stream().map(e -> {
/*      */                 GameEntityDTO d = new GameEntityDTO();
/*      */                 d.setId(e);
/*      */                 return d;
/*  225 */               }).collect(Collectors.toList()));
/*  226 */         SwingUtilities.invokeLater(() -> Alert.showLocMessage("export.old.favorite.elements"));
/*  227 */       } catch (Throwable e) {
/*  228 */         log(new Object[] { "error", e });
/*      */       } 
/*      */     }
/*  231 */     getFavoriteGameEntities();
/*      */   }
/*      */   
/*      */   public void fillVersionTypesAndGameVersion() throws IOException {
/*  235 */     if (this.minecraftVersionTypes.size() < 2)
/*  236 */       this.minecraftVersionTypes.addAll(getMinecraftVersionTypesRemote()); 
/*  237 */     if (this.gameVersions.isEmpty()) {
/*  238 */       for (NameIdDTO nid : getMinecraftVersionTypes()) {
/*  239 */         List<GameVersionDTO> list = getGameVersionsRemote(nid);
/*  240 */         this.gameVersions.put(nid, list);
/*  241 */         if (nid.getId().equals(Long.valueOf(1L))) {
/*  242 */           this.gameVersions.put(this.anyVersionType, list);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public void fillCategories() throws IOException {
/*  249 */     if (this.map.isEmpty())
/*  250 */       this.map.putAll((Map<? extends GameType, ? extends List<CategoryDTO>>)this.gsonService.getObjectWithoutSaving(this.modpackApiURL + "client/categories", (new TypeToken<Map<GameType, List<CategoryDTO>>>() {
/*      */             
/*  252 */             }).getType())); 
/*      */   }
/*      */   
/*      */   public DownloadableContainer getContainer(CompleteVersion version, boolean force) {
/*  256 */     DownloadableContainer container = new DownloadableContainer();
/*  257 */     Path versionFolder = ModpackUtil.getPathByVersion(version);
/*      */     
/*  259 */     if (version.getModpack() != null && version.getModpack().getVersion() != null) {
/*  260 */       ModpackVersionDTO versionDTO = (ModpackVersionDTO)version.getModpack().getVersion();
/*  261 */       List<MetadataDTO> list = checkResources(versionDTO, force, versionFolder);
/*      */       
/*  263 */       for (MetadataDTO m : list) {
/*  264 */         m.setLocalDestination(new File(versionFolder.toFile(), m.getPath()));
/*  265 */         Downloadable d = new Downloadable(ClientInstanceRepo.createModpackRepo(), m, force);
/*  266 */         container.add(d);
/*      */       } 
/*      */       
/*  269 */       list = checkCompositeResources(versionDTO, force, versionFolder);
/*  270 */       for (MetadataDTO m : list) {
/*  271 */         m.setLocalDestination(new File(versionFolder.toFile(), m.getPath()));
/*  272 */         MapDownloader mapDownloader = new MapDownloader(force, m);
/*  273 */         container.add((Downloadable)mapDownloader);
/*      */       } 
/*  275 */       if (Objects.nonNull(versionDTO.getAdditionalFile())) {
/*  276 */         MetadataDTO metadataDTO = versionDTO.getAdditionalFile();
/*  277 */         Path additionalFile = ModpackUtil.getPathByVersion((Version)version, new String[] { metadataDTO.getPath() });
/*  278 */         metadataDTO.setLocalDestination(additionalFile.toFile());
/*  279 */         if (Files.notExists(additionalFile, new java.nio.file.LinkOption[0]) || 
/*  280 */           !metadataDTO.getSha1().equals(FileUtil.getChecksum(metadataDTO.getLocalDestination()))) {
/*  281 */           container.add((Downloadable)new UnzipEntityDownloader(force, metadataDTO));
/*      */         }
/*      */       } 
/*  284 */       container.addHandler((DownloadableContainerHandler)new GameEntityHandler());
/*      */     } 
/*  286 */     return container;
/*      */   }
/*      */   
/*      */   private List<MetadataDTO> checkResources(ModpackVersionDTO version, boolean force, Path versionFolder) {
/*  290 */     log(new Object[] { "check resources" });
/*  291 */     List<MetadataDTO> list = new ArrayList<>();
/*  292 */     for (ModDTO mod : version.getMods()) {
/*  293 */       if (!mod.isUserInstall() && mod.getStateGameElement() != StateGameElement.NO_ACTIVE && 
/*  294 */         notExistOrCorrect(versionFolder, (GameEntityDTO)mod, force) && 
/*  295 */         !fillFromCache(GameType.MOD, (GameEntityDTO)mod, version, versionFolder)) {
/*  296 */         list.add(mod.getVersion().getMetadata());
/*      */       }
/*      */     } 
/*  299 */     for (ResourcePackDTO resourcePack : version.getResourcePacks()) {
/*  300 */       if (!resourcePack.isUserInstall() && resourcePack.getStateGameElement() != StateGameElement.NO_ACTIVE && 
/*  301 */         notExistOrCorrect(versionFolder, (GameEntityDTO)resourcePack, force) && 
/*  302 */         !fillFromCache(GameType.RESOURCEPACK, (GameEntityDTO)resourcePack, version, versionFolder)) {
/*  303 */         list.add(resourcePack.getVersion().getMetadata());
/*      */       }
/*      */     } 
/*  306 */     for (ShaderpackDTO shader : version.getShaderpacks()) {
/*  307 */       if (!shader.isUserInstall() && shader.getStateGameElement() != StateGameElement.NO_ACTIVE && 
/*  308 */         notExistOrCorrect(versionFolder, (GameEntityDTO)shader, force) && 
/*  309 */         !fillFromCache(GameType.SHADERPACK, (GameEntityDTO)shader, version, versionFolder)) {
/*  310 */         list.add(shader.getVersion().getMetadata());
/*      */       }
/*      */     } 
/*  313 */     return list;
/*      */   }
/*      */   
/*      */   private boolean notExistOrCorrect(Path versionFolder, GameEntityDTO e, boolean hash) {
/*  317 */     Path path = Paths.get(versionFolder.toString(), new String[] { e.getVersion().getMetadata().getPath() });
/*  318 */     if (Files.notExists(path, new java.nio.file.LinkOption[0]))
/*  319 */       return true; 
/*  320 */     if (hash) {
/*  321 */       return !FileUtil.getChecksum(path.toFile()).equals(e.getVersion().getMetadata().getSha1());
/*      */     }
/*  323 */     return false;
/*      */   }
/*      */   
/*      */   private List<MetadataDTO> checkCompositeResources(ModpackVersionDTO version, boolean force, Path versionFolder) {
/*  327 */     log(new Object[] { "check CompositeResources" });
/*  328 */     List<MetadataDTO> list = new ArrayList<>();
/*  329 */     for (MapDTO map : version.getMaps()) {
/*  330 */       if (!map.isUserInstall())
/*  331 */         list.add(map.getVersion().getMetadata()); 
/*      */     } 
/*  333 */     Iterator<MetadataDTO> it = list.iterator();
/*  334 */     while (it.hasNext()) {
/*  335 */       MetadataDTO meta = it.next();
/*  336 */       U.debug(new Object[] { meta });
/*  337 */       if (((MapMetadataDTO)meta).getFolders() == null || ((MapMetadataDTO)meta).getFolders().size() == 0)
/*      */         continue; 
/*  339 */       String folder = versionFolder.toString() + "/saves/" + (String)((MapMetadataDTO)meta).getFolders().get(0);
/*  340 */       if ((new File(folder)).exists())
/*  341 */         it.remove(); 
/*      */     } 
/*  343 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   public void backupModPack(List<CompleteVersion> list, File saveFolder, ModpackBackupFrame.HandleListener handleListener) {
/*  348 */     AsyncThread.execute(() -> {
/*      */           log(new Object[] { "backuping modpack" });
/*      */           
/*      */           File versionFolder = FileUtil.getRelative("versions").toFile();
/*      */           List<File> files = new ArrayList<>();
/*      */           for (CompleteVersion v : list) {
/*      */             File version = new File(versionFolder, v.getID());
/*      */             files.addAll(findCopiedFiles(v, version));
/*      */           } 
/*      */           Map<String, String> map = new HashMap<>();
/*      */           List<String> names = new ArrayList<>();
/*      */           for (CompleteVersion v : list) {
/*      */             map.put(v.getID() + ".json", this.gson.toJson(v));
/*      */             names.add(v.getID());
/*      */           } 
/*      */           try {
/*      */             if (saveFolder.exists() && !saveFolder.delete()) {
/*      */               throw new IOException("can't delete old version of the file");
/*      */             }
/*      */             FileUtil.backupModpacks(map, files, versionFolder.toPath(), saveFolder, names);
/*      */             handleListener.operationSuccess();
/*  369 */           } catch (IOException e) {
/*      */             log(new Object[] { e });
/*      */             handleListener.processError(e);
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<File> findCopiedFiles(CompleteVersion completeVersion, File version) {
/*  380 */     IOFileFilter filter = FileFilterUtils.notFileFilter(FileFilterUtils.or(new IOFileFilter[] { FileFilterUtils.nameFileFilter("natives"), 
/*  381 */             FileFilterUtils.nameFileFilter(FileUtil.GameEntityFolder.SAVES.toString()), 
/*  382 */             FileFilterUtils.nameFileFilter(FileUtil.GameEntityFolder.MODS.toString()), 
/*  383 */             FileFilterUtils.nameFileFilter(FileUtil.GameEntityFolder.RESOURCEPACKS.toString()) }));
/*      */     
/*  385 */     IOFileFilter filesFilter = FileFilterUtils.notFileFilter(FileFilterUtils.or(new IOFileFilter[] { FileFilterUtils.nameFileFilter(completeVersion.getID() + ".jar"), 
/*  386 */             FileFilterUtils.nameFileFilter(completeVersion.getID() + ".jar.bak") }));
/*      */     
/*  388 */     List<File> list = (List<File>)FileUtils.listFiles(version, filesFilter, filter);
/*  389 */     if (TLauncher.DEBUG) {
/*  390 */       U.log(new Object[] { "filter by IOFileFilter" });
/*  391 */       for (File f : list) {
/*  392 */         U.log(new Object[] { f });
/*      */       } 
/*  394 */     }  ModpackVersionDTO modpackVersion = (ModpackVersionDTO)completeVersion.getModpack().getVersion();
/*      */     
/*  396 */     for (GameEntityDTO en : modpackVersion.getMaps()) {
/*  397 */       File map = new File(version, "saves/" + FilenameUtils.getBaseName(en.getVersion().getMetadata().getPath()));
/*      */       
/*  399 */       if (map.exists()) {
/*  400 */         list.addAll(FileUtils.listFiles(map, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE));
/*      */       }
/*      */     } 
/*      */     
/*  404 */     for (SubModpackDTO en : modpackVersion.getMods()) {
/*  405 */       if (en.isUserInstall()) {
/*  406 */         addToList(version, list, en);
/*      */       }
/*      */     } 
/*  409 */     for (SubModpackDTO en : modpackVersion.getResourcePacks()) {
/*  410 */       if (en.isUserInstall()) {
/*  411 */         addToList(version, list, en);
/*      */       }
/*      */     } 
/*      */     
/*  415 */     if (TLauncher.DEBUG) {
/*  416 */       U.log(new Object[] { "backed files" });
/*  417 */       for (File f : list) {
/*  418 */         U.log(new Object[] { f });
/*      */       } 
/*  420 */     }  return list;
/*      */   }
/*      */   
/*      */   private void addToList(File version, List<File> list, SubModpackDTO en) {
/*  424 */     if (en.getStateGameElement() == StateGameElement.NO_ACTIVE) {
/*  425 */       list.add(new File(version, en.getVersion().getMetadata().getPath() + ".deactivation"));
/*      */     } else {
/*  427 */       list.add(new File(version, en.getVersion().getMetadata().getPath()));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void installModPack(File file, ModpackBackupFrame.HandleListener handleListener) {
/*  432 */     AsyncThread.execute(() -> {
/*      */           log(new Object[] { "installModPack" });
/*      */           
/*      */           File versionFolder = FileUtil.getRelative("versions").toFile();
/*      */           try {
/*      */             List<String> modpackNames = analizeArchiver(file);
/*      */             FileUtil.unzipUniversal(file, versionFolder);
/*      */             for (String name : modpackNames) {
/*      */               CompleteVersion version;
/*      */               File modPackFolder = new File(versionFolder, name);
/*      */               File fileVersion = new File(modPackFolder, name + ".json");
/*      */               if (Files.notExists(fileVersion.toPath(), new java.nio.file.LinkOption[0])) {
/*      */                 version = parseCurse(versionFolder, name, modPackFolder, fileVersion);
/*      */               } else {
/*      */                 version = (CompleteVersion)this.gson.fromJson(FileUtil.readFile(fileVersion), CompleteVersion.class);
/*      */               } 
/*      */               for (GameEntityListener listener : this.gameListeners.get(GameType.MODPACK)) {
/*      */                 listener.installEntity(version);
/*      */               }
/*      */             } 
/*      */             handleListener.installedSuccess(modpackNames);
/*  453 */           } catch (Exception e) {
/*      */             U.log(new Object[] { e });
/*      */             handleListener.processError(e);
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CompleteVersion parseCurse(File versionFolder, String modpackName, File modPackFolder, File version) throws Exception {
/*  464 */     File instance = new File(modPackFolder, "minecraftinstance.json");
/*  465 */     if (Files.exists(instance.toPath(), new java.nio.file.LinkOption[0])) {
/*  466 */       File mods = new File(modPackFolder, "mods");
/*  467 */       File resourcepacks = new File(modPackFolder, "resourcepacks");
/*  468 */       File maps = new File(modPackFolder, "saves");
/*  469 */       if (!Files.exists(mods.toPath(), new java.nio.file.LinkOption[0]) && !Files.exists(resourcepacks.toPath(), new java.nio.file.LinkOption[0]) && !Files.exists(maps.toPath(), new java.nio.file.LinkOption[0])) {
/*  470 */         log(new Object[] { "modpack doesn't exist" });
/*      */       }
/*  472 */       MinecraftInstance minecraftInstance = (MinecraftInstance)this.gson.fromJson(FileUtil.readFile(instance), MinecraftInstance.class);
/*  473 */       if (minecraftInstance == null) {
/*  474 */         throw new Exception("broken config");
/*      */       }
/*  476 */       ModpackDTO modPack = new ModpackDTO();
/*  477 */       modPack.setId(Long.valueOf(-U.n()));
/*  478 */       ModpackVersionDTO modpackVersion = new ModpackVersionDTO();
/*  479 */       String[] formats = { "jar", "zip" };
/*  480 */       CompleteVersion completeVersion = (CompleteVersion)this.gson.fromJson(minecraftInstance.baseModLoader.VersionJson, CompleteVersion.class);
/*  481 */       modpackVersion.setForgeVersion(completeVersion.getID());
/*  482 */       modpackVersion.setGameVersion(minecraftInstance.baseModLoader.MinecraftVersion);
/*  483 */       completeVersion.setID(modpackName);
/*  484 */       modPack.setName(completeVersion.getID());
/*  485 */       modpackVersion.setId(Long.valueOf(-U.n() - 1L));
/*  486 */       modpackVersion.setName("1.0");
/*  487 */       modpackVersion.setMods(createHandleGameEntities(mods, formats, (Class)ModDTO.class));
/*  488 */       modpackVersion.setResourcePacks(createHandleGameEntities(resourcepacks, formats, (Class)ResourcePackDTO.class));
/*      */       
/*  490 */       modpackVersion.setMaps(createMapsByFolder(maps));
/*  491 */       modPack.setVersion((VersionDTO)modpackVersion);
/*  492 */       completeVersion.setModpackDTO(modPack);
/*  493 */       FileUtil.writeFile(version, this.gson.toJson(completeVersion));
/*  494 */       return completeVersion;
/*      */     } 
/*      */     
/*  497 */     throw new Exception("dont' find config file");
/*      */   }
/*      */   
/*      */   private List<MapDTO> createMapsByFolder(File maps) {
/*  501 */     List<MapDTO> list = new ArrayList<>();
/*  502 */     FilenameFilter filter = (dir, name) -> dir.isDirectory();
/*  503 */     for (File file : (File[])Objects.<File[]>requireNonNull(maps.listFiles(filter))) {
/*  504 */       MapDTO map = new MapDTO();
/*  505 */       VersionDTO v = new VersionDTO();
/*  506 */       v.setName("1.0");
/*  507 */       MetadataDTO meta = new MetadataDTO();
/*  508 */       meta.setPath(FileUtil.GameEntityFolder.getPath(GameType.MAP) + "/" + file.getName() + ".zip");
/*  509 */       v.setMetadata(meta);
/*  510 */       map.setUserInstall(true);
/*  511 */       map.setName(file.getName());
/*  512 */       map.setVersion(v);
/*  513 */       list.add(map);
/*      */     } 
/*  515 */     return list;
/*      */   }
/*      */   
/*      */   public List<String> analizeArchiver(File file) throws ParseModPackException {
/*      */     try {
/*  520 */       String ext = FilenameUtils.getExtension(file.getCanonicalPath());
/*  521 */       List<String> list = new ArrayList<>();
/*  522 */       switch (ext) {
/*      */         case "rar":
/*  524 */           list = FileUtil.topFolders(new Archive(file));
/*      */           break;
/*      */         case "zip":
/*  527 */           list = FileUtil.topFolders(new ZipFile(file));
/*      */           break;
/*      */       } 
/*  530 */       if (list.isEmpty()) {
/*  531 */         throw new ParseModPackException("The archive doesn't contain any folders");
/*      */       }
/*  533 */       if (!checkNameVersion(list)) {
/*  534 */         throw new ParseModPackException("there is a version with same name");
/*      */       }
/*  536 */       return list;
/*  537 */     } catch (Exception e) {
/*  538 */       log(new Object[] { "error during analize archiver " + file });
/*  539 */       throw new ParseModPackException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean checkNameVersion(List<String> list) {
/*  544 */     for (String s : list) {
/*  545 */       if (Objects.nonNull(this.tLauncher.getVersionManager().getVersionSyncInfo(s))) {
/*  546 */         return false;
/*      */       }
/*      */     } 
/*  549 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<? extends GameEntityDTO> createHandleGameEntities(File folder, String[] exts, Class<? extends GameEntityDTO> t) {
/*  554 */     List<GameEntityDTO> list = new ArrayList<>();
/*  555 */     List<File> files = (List<File>)FileUtils.listFiles(folder, exts, true);
/*  556 */     for (File f : files) {
/*      */       try {
/*  558 */         GameEntityDTO c = createHandleGameEntity(folder, t, f);
/*  559 */         list.add(c);
/*  560 */       } catch (InstantiationException|IllegalAccessException e) {
/*  561 */         log(new Object[] { e });
/*      */       } 
/*      */     } 
/*  564 */     return list;
/*      */   }
/*      */ 
/*      */   
/*      */   private GameEntityDTO createHandleGameEntity(File folder, Class<? extends GameEntityDTO> t, File f) throws InstantiationException, IllegalAccessException {
/*  569 */     GameEntityDTO c = t.newInstance();
/*  570 */     c.setId(Long.valueOf(-U.n()));
/*  571 */     c.setName(FilenameUtils.getBaseName(f.getName()));
/*  572 */     c.setUserInstall(true);
/*  573 */     MetadataDTO meta = FileUtil.createMetadata(f, folder, t);
/*  574 */     meta.setPath(FileUtil.GameEntityFolder.getPath(t, true).concat(meta.getPath()));
/*  575 */     meta.setUrl(FileUtil.GameEntityFolder.getPath(t, true).concat(meta.getUrl()));
/*  576 */     VersionDTO standardVersion = new VersionDTO();
/*  577 */     standardVersion.setId(Long.valueOf(-U.n()));
/*  578 */     standardVersion.setName("1.0");
/*  579 */     standardVersion.setMetadata(meta);
/*  580 */     c.setVersion(standardVersion);
/*  581 */     return c;
/*      */   }
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void createModpack(String name, ModpackDTO modpackDTO, boolean usedSkin) {
/*      */     try {
/*  600 */       ModpackVersionDTO mvd = (ModpackVersionDTO)modpackDTO.getVersion();
/*  601 */       CompleteVersion completeVersion = getCompleteVersionByMinecraftVersionTypeAndId(mvd
/*  602 */           .findFirstMinecraftVersionType(), mvd.getMinecraftVersionName());
/*      */       
/*  604 */       completeVersion.setID(name);
/*  605 */       completeVersion.setModpackDTO(modpackDTO);
/*  606 */       this.tLauncher.getVersionManager().getLocalList().saveVersion(completeVersion);
/*  607 */       for (GameEntityListener l : this.gameListeners.get(GameType.MODPACK)) {
/*  608 */         l.installEntity(completeVersion);
/*  609 */         l.installEntity((GameEntityDTO)completeVersion.getModpack(), GameType.MODPACK);
/*      */       } 
/*      */       
/*  612 */       if (usedSkin) {
/*  613 */         installTLSkinCapeMod(mvd);
/*      */       }
/*  615 */       if (mvd.getMinecraftVersionTypes().stream().filter(f -> f.getId().equals(Long.valueOf(2L))).findAny().isPresent()) {
/*  616 */         ModDTO m = new ModDTO();
/*  617 */         m.setId(ModDTO.FABRIC_API_ID);
/*  618 */         installEntity((GameEntityDTO)m, null, GameType.MOD, null, true);
/*  619 */       } else if (mvd.getMinecraftVersionTypes().stream().filter(f -> f.getId().equals(Long.valueOf(4L))).findAny()
/*  620 */         .isPresent()) {
/*  621 */         ModDTO m = new ModDTO();
/*  622 */         m.setId(ModDTO.QUILTED_FABRIC_API_ID);
/*  623 */         installEntity((GameEntityDTO)m, null, GameType.MOD, null, true);
/*      */       } 
/*  625 */     } catch (IOException e) {
/*  626 */       U.log(new Object[] { e });
/*      */     } 
/*      */   }
/*      */   
/*      */   public void installTLSkinCapeMod(ModpackVersionDTO mvd) {
/*  631 */     ModDTO m = new ModDTO();
/*  632 */     m.setId(ModDTO.TL_SKIN_CAPE_ID);
/*  633 */     installEntity((GameEntityDTO)m, null, GameType.MOD, null, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public CompleteVersion getCompleteVersionByMinecraftVersionTypeAndId(NameIdDTO type, NameIdDTO version) throws IOException, RequiredRemoteVersionError {
/*  638 */     MinecraftVersionDTO minecraftVersion = getCompleteVersion(type, version);
/*      */     try {
/*  640 */       CompleteVersion completeVersion = (CompleteVersion)this.gson.fromJson(minecraftVersion.getValue(), CompleteVersion.class);
/*  641 */       completeVersion = completeVersion.resolve(this.tLauncher.getVersionManager(), true);
/*  642 */       if (Objects.isNull(completeVersion)) {
/*  643 */         this.tLauncher.getVersionManager().asyncRefresh();
/*  644 */         throw new RequiredRemoteVersionError();
/*      */       } 
/*  646 */       TlauncherUtil.processRemoteVersionToSave(completeVersion, minecraftVersion.getValue(), this.gson);
/*  647 */       return completeVersion;
/*      */     }
/*  649 */     catch (NullPointerException e) {
/*  650 */       U.log(new Object[] { " type " + Objects.isNull(type) });
/*  651 */       U.log(new Object[] { " version " + Objects.isNull(version) });
/*  652 */       U.log(new Object[] { " minecraftVersion " + minecraftVersion });
/*  653 */       U.log(new Object[] { "error request " + minecraftVersion.getValue() + " " + type.toString() + " " + version.toString() });
/*  654 */       throw e;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addEntityToModpack(GameEntityDTO gameEntity, CompleteVersion completeVersion, GameType type) throws IOException {
/*  664 */     ModpackDTO modPack = completeVersion.getModpack();
/*  665 */     ModpackVersionDTO modpackVersion = (ModpackVersionDTO)modPack.getVersion();
/*  666 */     if (modpackVersion == null) {
/*  667 */       modpackVersion = new ModpackVersionDTO();
/*      */       
/*  669 */       modpackVersion.setMaps(new ArrayList());
/*  670 */       modpackVersion.setMods(new ArrayList());
/*  671 */       modpackVersion.setResourcePacks(new ArrayList());
/*  672 */       modPack.setVersion((VersionDTO)modpackVersion);
/*      */     } 
/*      */     
/*      */     try {
/*  676 */       GameEntityDTO removedEntity = findAndRemoveGameEntity(completeVersion, gameEntity, type);
/*  677 */       for (GameEntityListener l : this.gameListeners.get(type)) {
/*  678 */         l.removeEntity(removedEntity);
/*      */       }
/*  680 */     } catch (GameEntityNotFound gameEntityNotFound) {}
/*      */     
/*  682 */     modpackVersion.add(type, gameEntity);
/*      */   }
/*      */   
/*      */   private boolean checkAddedElement(VersionDTO version, GameType type, GameEntityDTO entity) {
/*  686 */     ModpackVersionDTO v = (ModpackVersionDTO)version;
/*  687 */     if (v != null && type != GameType.MODPACK) {
/*  688 */       for (GameEntityDTO en : v.getByType(type)) {
/*  689 */         if (en.getId().equals(entity.getId())) {
/*  690 */           return false;
/*      */         }
/*      */       } 
/*      */     }
/*  694 */     return true;
/*      */   }
/*      */   
/*      */   public void showFullGameEntity(GameEntityDTO entity, GameType type) {
/*  698 */     CompletableFuture.runAsync(() -> {
/*      */           synchronized (this) {
/*      */             try {
/*      */               MainPane mp = (this.tLauncher.getFrame()).mp;
/*      */ 
/*      */               
/*      */               GameEntityDTO ge = getGameEntity(type, entity.getId());
/*      */ 
/*      */               
/*      */               mp.setScene((PseudoScene)mp.modpackEnitityScene);
/*      */               
/*      */               SwingUtilities.invokeLater(());
/*  710 */             } catch (SocketTimeoutException e) {
/*      */               Alert.showLocError("modpack.internet.update");
/*  712 */             } catch (IOException e) {
/*      */               
/*      */               SwingUtilities.invokeLater(());
/*      */             }
/*      */           
/*      */           }
/*      */ 
/*      */ 
/*      */         
/*  721 */         }this.modpackExecutorService).exceptionally(e -> {
/*      */           U.log(new Object[] { e });
/*      */           return null;
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   public void showSubModpackElement(GameEntityDTO entity, GameEntityDTO parent, GameType type) throws IOException {
/*  729 */     GameEntityDTO entity1 = getGameEntity(type, entity.getId());
/*  730 */     if (entity1 == null) {
/*      */       return;
/*      */     }
/*  733 */     SwingUtilities.invokeLater(() -> (this.tLauncher.getFrame()).mp.completeSubEntityScene.showModpackElement(entity1, type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T readFromServer(Class<T> t, GameEntityDTO e, VersionDTO version) throws IOException {
/*  740 */     HashMap<String, Object> map = new HashMap<>();
/*  741 */     map.put("id", e.getId());
/*  742 */     map.put("versionId", version.getId());
/*  743 */     String res = Http.performGet(this.innerConfiguration
/*  744 */         .get("modpack.operation.url") + "read/" + t.getSimpleName().toLowerCase(), map, 
/*  745 */         U.getConnectionTimeout(), this.innerConfiguration.getInteger("modpack.update.time.connect"));
/*  746 */     return (T)this.gson.fromJson(res, t);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onVersionsRefreshing(VersionManager manager) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onVersionsRefreshingFailed(VersionManager manager) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onVersionsRefreshed(VersionManager manager) {
/*  761 */     if (!manager.isLocalRefresh()) {
/*  762 */       loadInfo();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void processGameElementByStatus() {
/*  768 */     readStatusGameElement();
/*      */   }
/*      */ 
/*      */   
/*      */   private void readStatusGameElement() {
/*      */     try {
/*  774 */       if (this.STATUS_MODPACK_FILE.exists()) {
/*  775 */         HashSet<Long> set = (HashSet<Long>)this.gson.fromJson(FileUtil.readFile(this.STATUS_MODPACK_FILE), (new TypeToken<HashSet<Long>>() {
/*      */             
/*  777 */             }).getType());
/*      */         
/*  779 */         if (Objects.nonNull(set))
/*  780 */           this.statusModpackElement.addAll(set); 
/*      */       } else {
/*  782 */         writeStatusGameElement();
/*      */       } 
/*  784 */     } catch (IOException|com.google.gson.JsonSyntaxException e) {
/*  785 */       U.log(new Object[] { e });
/*  786 */       writeStatusGameElement();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void writeStatusGameElement() {
/*      */     try {
/*  792 */       FileUtil.writeFile(this.STATUS_MODPACK_FILE, this.gson.toJson(this.statusModpackElement));
/*  793 */     } catch (IOException e1) {
/*  794 */       U.log(new Object[] { e1 });
/*      */     } 
/*      */   }
/*      */   
/*      */   public synchronized InfoMod getInfoMod() {
/*  799 */     return this.infoMod;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<CompleteVersion> getModpackVersions() {
/*  804 */     return (List<CompleteVersion>)Lists.newArrayList(this.tLauncher.getVersionManager().getLocalList().getVersions()).stream().filter(e -> {
/*      */           CompleteVersion cv = (CompleteVersion)e;
/*      */           return cv.isModpack();
/*  807 */         }).map(e -> (CompleteVersion)e).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */   
/*      */   public void changeModpackElementState(GameEntityDTO entity, GameType type) {
/*  812 */     CompleteVersion completeVersion = (this.tLauncher.getFrame()).mp.modpackScene.getSelectedCompleteVersion();
/*  813 */     ModpackVersionDTO versionDTO = (ModpackVersionDTO)completeVersion.getModpack().getVersion();
/*      */     
/*  815 */     Optional<? extends GameEntityDTO> op = versionDTO.getByType(type).stream().filter(e -> e.getId().equals(entity.getId())).findFirst();
/*  816 */     if (!op.isPresent())
/*      */       return; 
/*  818 */     SubModpackDTO en = (SubModpackDTO)op.get();
/*  819 */     AsyncThread.execute(() -> {
/*      */           for (GameEntityListener l : this.gameListeners.get(type)) {
/*      */             l.activationStarted((GameEntityDTO)en);
/*      */           }
/*      */           try {
/*      */             String command;
/*      */             switch (type) {
/*      */               case SHADERPACK:
/*      */                 for (ShaderpackDTO d : versionDTO.getShaderpacks()) {
/*      */                   if (!d.getId().equals(en.getId()) && d.getStateGameElement() == StateGameElement.ACTIVE) {
/*      */                     for (GameEntityListener l : this.gameListeners.get(type)) {
/*      */                       d.setStateGameElement(StateGameElement.NO_ACTIVE);
/*      */                       l.activation((GameEntityDTO)d);
/*      */                     } 
/*      */                   }
/*      */                 } 
/*      */                 command = "";
/*      */                 if (en.getStateGameElement() == StateGameElement.ACTIVE) {
/*      */                   en.setStateGameElement(StateGameElement.NO_ACTIVE);
/*      */                 } else {
/*      */                   en.setStateGameElement(StateGameElement.ACTIVE);
/*      */                   command = FileUtil.getFilename(en.getVersion().getMetadata().getPath());
/*      */                 } 
/*      */                 ModpackUtil.addOrReplaceShaderConfig(completeVersion, "shaderPack", command);
/*      */                 for (GameEntityListener l : this.gameListeners.get(type)) {
/*      */                   l.activation((GameEntityDTO)en);
/*      */                 }
/*      */                 break;
/*      */               
/*      */               case MOD:
/*      */               case RESOURCEPACK:
/*      */                 changeActivation(en, type, completeVersion);
/*      */                 if (en.getDependencies() != null && en.getStateGameElement() == StateGameElement.ACTIVE) {
/*      */                   for (GameEntityDependencyDTO d : en.getDependencies()) {
/*      */                     if (d.getDependencyType() == DependencyType.REQUIRED) {
/*      */                       Optional<? extends GameEntityDTO> optional = versionDTO.getByType(type).stream().filter(()).filter(()).findFirst();
/*      */                       if (optional.isPresent()) {
/*      */                         changeActivation((SubModpackDTO)optional.get(), d.getGameType(), completeVersion);
/*      */                       }
/*      */                     } 
/*      */                   } 
/*      */                 }
/*      */                 break;
/*      */               default:
/*      */                 log(new Object[] { "strange type of ", type, "for entity ", entity });
/*      */                 break;
/*      */             } 
/*      */             for (GameEntityListener l : this.gameListeners.get(GameType.MODPACK)) {
/*      */               l.updateVersion(completeVersion, completeVersion);
/*      */             }
/*      */             this.tLauncher.getVersionManager().getLocalList().refreshLocalVersion(completeVersion);
/*  870 */           } catch (Exception e) {
/*      */             log(new Object[] { e });
/*      */             for (GameEntityListener l : this.gameListeners.get(type)) {
/*      */               l.activationError(entity, e);
/*      */             }
/*      */           } 
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   private void changeActivation(SubModpackDTO entity, GameType type, CompleteVersion completeVersion) throws IOException {
/*  881 */     File modpackFolder = FileUtil.getRelative("versions/" + completeVersion.getID()).toFile();
/*      */     try {
/*  883 */       File target = new File(modpackFolder, entity.getVersion().getMetadata().getPath());
/*  884 */       if (entity.getStateGameElement() == StateGameElement.ACTIVE) {
/*  885 */         Files.move(target.toPath(), Paths.get(target.toString() + ".deactivation", new String[0]), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*      */       } else {
/*      */         
/*  888 */         Files.move(Paths.get(target.toString() + ".deactivation", new String[0]), target.toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*      */       }
/*      */     
/*  891 */     } catch (NoSuchFileException e) {
/*  892 */       log(new Object[] { entity.getStateGameElement() + " ", e.getMessage() });
/*      */     } 
/*  894 */     if (entity.getStateGameElement() == StateGameElement.NO_ACTIVE) {
/*  895 */       entity.setStateGameElement(StateGameElement.ACTIVE);
/*      */     } else {
/*  897 */       entity.setStateGameElement(StateGameElement.NO_ACTIVE);
/*      */     } 
/*  899 */     for (GameEntityListener l : this.gameListeners.get(type)) {
/*  900 */       l.activation((GameEntityDTO)entity);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void installEntity(GameEntityDTO e, VersionDTO version, GameType type, GameVersionDTO gameVersion, boolean throwError) {
/*  907 */     if (type != GameType.MODPACK && !(this.tLauncher.getFrame()).mp.modpackScene.isSelectedCompleteVersion()) {
/*  908 */       Alert.showLocMessage("modpack.select.modpack");
/*      */       return;
/*      */     } 
/*  911 */     for (GameEntityListener l : this.gameListeners.get(type)) {
/*  912 */       l.processingStarted(e, version);
/*      */     }
/*      */     try {
/*  915 */       GameEntityDTO gameEntity = null;
/*  916 */       if (GameType.MODPACK.equals(type)) {
/*  917 */         ModpackDTO installed = new ModpackDTO();
/*  918 */         installed.setId(e.getId());
/*  919 */         installed.setName(e.getName());
/*  920 */         ModpackVersionDTO mv = getInstallingModpackVersionDTO(e, version);
/*  921 */         installed.setVersion((VersionDTO)mv);
/*      */         
/*  923 */         CompleteVersion cv = getCompleteVersionByMinecraftVersionTypeAndId(mv.findFirstMinecraftVersionType(), mv.getMinecraftVersionName()).resolve(this.tLauncher.getVersionManager(), true);
/*      */         
/*  925 */         cv.setID(installed.getName() + " " + installed.getVersion().getName());
/*  926 */         cv.setModpackDTO(installed);
/*  927 */         ModpackDTO modpackDTO1 = installed;
/*  928 */         this.tLauncher.getVersionManager().getLocalList().saveVersion(cv);
/*  929 */         SwingUtilities.invokeLater(() -> {
/*      */               for (GameEntityListener l : this.gameListeners.get(type)) {
/*      */                 l.installEntity(cv);
/*      */               }
/*      */             });
/*      */       } else {
/*  935 */         CompleteVersion completeVersion = (this.tLauncher.getFrame()).mp.modpackScene.getSelectedCompleteVersion();
/*  936 */         ModpackVersionDTO mv = (ModpackVersionDTO)completeVersion.getModpack().getVersion();
/*  937 */         if (Objects.isNull(version)) {
/*  938 */           gameEntity = getInstallingGameEntity(type, e, null, getGameVersion(mv), mv
/*  939 */               .findFirstMinecraftVersionType());
/*      */         } else {
/*  941 */           gameEntity = getInstallingGameEntity(type, e, version, null, null);
/*      */         } 
/*  943 */         if (ModDTO.TL_SKIN_CAPE_ID.equals(gameEntity.getId())) {
/*  944 */           completeVersion.setSkinVersion(true);
/*      */         }
/*  946 */         checkMapFolders(completeVersion, gameEntity, type);
/*  947 */         addDependencies(gameEntity, completeVersion);
/*  948 */         if (type == GameType.SHADERPACK) {
/*  949 */           preInstallingShader(type, gameEntity, completeVersion);
/*      */         }
/*  951 */         addEntityToModpack(gameEntity, completeVersion, type);
/*  952 */         resaveVersion(completeVersion);
/*      */       } 
/*  954 */       GameEntityDTO gamEntityDTO2 = gameEntity;
/*  955 */       SwingUtilities.invokeLater(() -> {
/*      */             
/*      */             for (GameEntityListener l : this.gameListeners.get(type)) {
/*      */               l.installEntity(gamEntityDTO2, type);
/*      */             }
/*      */           });
/*  961 */     } catch (IOException e1) {
/*  962 */       U.log(new Object[] { e1 });
/*  963 */       for (GameEntityListener l : this.gameListeners.get(type)) {
/*  964 */         l.installError(e, version, e1);
/*      */       }
/*  966 */       if (e1 instanceof RequiredRemoteVersionError)
/*  967 */       { Alert.showLocWarning("Не смогло получить базовую версию для текущей версии, начато обновление версий, как оно закончиться попробуйте снова!"); }
/*      */       
/*  969 */       else if (throwError)
/*  970 */       { Alert.showLocMessage("", "modpack.try.later", null); } 
/*  971 */     } catch (SameMapFoldersException e1) {
/*  972 */       Alert.showLocWarning("modpack.map.same.folder");
/*  973 */       for (GameEntityListener l : this.gameListeners.get(type)) {
/*  974 */         l.installError(e, version, (Throwable)e1);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void preInstallingShader(GameType type, GameEntityDTO installEntity, CompleteVersion completeVersion) throws IOException {
/*  982 */     List<ShaderpackDTO> shaderpacks = ((ModpackVersionDTO)completeVersion.getModpack().getVersion()).getShaderpacks();
/*  983 */     for (ShaderpackDTO d : shaderpacks) {
/*  984 */       d.setStateGameElement(StateGameElement.NO_ACTIVE);
/*  985 */       for (GameEntityListener l : this.gameListeners.get(type)) {
/*  986 */         l.activation((GameEntityDTO)d);
/*      */       }
/*      */     } 
/*  989 */     ModpackUtil.addOrReplaceShaderConfig(completeVersion, "shaderPack", 
/*  990 */         FileUtil.getFilename(installEntity.getVersion().getMetadata().getPath()));
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkMapFolders(CompleteVersion completeVersion, GameEntityDTO installEntity, GameType type) throws SameMapFoldersException {
/*  995 */     if (type != GameType.MAP)
/*      */       return; 
/*  997 */     List<String> remoteFolders = ((MapMetadataDTO)installEntity.getVersion().getMetadata()).getFolders();
/*  998 */     for (MapDTO mapDTO : ((ModpackVersionDTO)completeVersion.getModpack().getVersion()).getMaps()) {
/*  999 */       List<String> folders = ((MapMetadataDTO)mapDTO.getVersion().getMetadata()).getFolders();
/*      */       
/* 1001 */       if (folders == null || remoteFolders == null) {
/*      */         continue;
/*      */       }
/* 1004 */       if (!Collections.disjoint(folders, remoteFolders)) {
/* 1005 */         throw new SameMapFoldersException(String.format("maps have same folders local: %s remote: %s", new Object[] { folders
/* 1006 */                 .toString(), remoteFolders.toString() }));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void installEntity(GameEntityDTO e, VersionDTO version, GameType type, boolean async) {
/* 1018 */     if (async) {
/* 1019 */       CompletableFuture.runAsync(() -> installEntity(e, version, type, null, true), this.modpackExecutorService)
/* 1020 */         .exceptionally(t -> {
/*      */             U.log(new Object[] { "error", t });
/*      */             return null;
/*      */           });
/*      */     } else {
/* 1025 */       installEntity(e, version, type, null, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addDependencies(GameEntityDTO e, CompleteVersion version) throws IOException {
/* 1034 */     List<GameEntityDependencyDTO> list = e.getDependencies();
/* 1035 */     if (list == null || list.size() == 0)
/*      */       return; 
/* 1037 */     ModpackVersionDTO mv = (ModpackVersionDTO)version.getModpack().getVersion();
/* 1038 */     for (GameEntityDependencyDTO d : list) {
/* 1039 */       if (d.getDependencyType() == DependencyType.INCOMPATIBLE || d
/* 1040 */         .getDependencyType() == DependencyType.OPTIONAL)
/*      */         continue; 
/* 1042 */       if (e.getId().equals(d.getGameEntityId()))
/*      */         continue; 
/* 1044 */       GameEntityDTO dto = new GameEntityDTO();
/* 1045 */       dto.setId(d.getGameEntityId());
/* 1046 */       if (Objects.nonNull(findGameFromCollection(dto, d.getGameType(), mv))) {
/* 1047 */         log(new Object[] { "it has already added  " + dto.getId() });
/*      */         continue;
/*      */       } 
/*      */       try {
/* 1051 */         dto = getInstallingGameEntity(d.getGameType(), dto, null, getGameVersion(mv), mv
/* 1052 */             .findFirstMinecraftVersionType());
/* 1053 */       } catch (IOException ex) {
/* 1054 */         if (StringUtils.contains(ex.getMessage(), "404")) {
/* 1055 */           log(new Object[] { String.format("can't resolve dependency for game entity %s, dependency id %s name %s", new Object[] { e
/* 1056 */                     .getName(), d.getGameEntityId(), d.getName() }) });
/*      */           continue;
/*      */         } 
/* 1059 */         throw ex;
/*      */       } 
/* 1061 */       addEntityToModpack(dto, version, d.getGameType());
/* 1062 */       for (GameEntityListener l : this.gameListeners.get(d.getGameType())) {
/* 1063 */         l.installEntity(dto, d.getGameType());
/*      */       }
/* 1065 */       addDependencies(dto, version);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void installHandleEntity(File[] files, CompleteVersion completeVersion, GameType type, HandleInstallModpackElementFrame.HandleListener handleListener) {
/* 1072 */     Path folder = FileUtil.getRelative("versions/" + completeVersion.getID() + "/" + FileUtil.GameEntityFolder.getPath(type));
/* 1073 */     folder.toFile().mkdir();
/*      */     try {
/* 1075 */       List<GameEntityDTO> list = new ArrayList<>();
/* 1076 */       for (File f : files) {
/* 1077 */         GameEntityDTO entity = initHanldeEntity(type, f, folder);
/* 1078 */         Path target = Paths.get(folder.toString(), new String[] { f.getName() });
/*      */         
/* 1080 */         if (!checkAddedElement(completeVersion.getModpack().getVersion(), type, entity)) {
/* 1081 */           throw new ParseModPackException("entity exists" + entity);
/*      */         }
/* 1083 */         Files.copy(f.toPath(), target, new CopyOption[0]);
/* 1084 */         if (type == GameType.MAP) {
/* 1085 */           FileUtil.unzipUniversal(target.toFile(), target.toFile().getParentFile());
/* 1086 */           FileUtil.deleteFile(target.toFile());
/* 1087 */         } else if (type == GameType.SHADERPACK) {
/* 1088 */           preInstallingShader(GameType.SHADERPACK, entity, completeVersion);
/*      */         } 
/* 1090 */         addEntityToModpack(entity, completeVersion, type);
/* 1091 */         list.add(entity);
/*      */       } 
/* 1093 */       for (GameEntityDTO entity : list) {
/* 1094 */         for (GameEntityListener l : this.gameListeners.get(type)) {
/* 1095 */           l.installEntity(entity, type);
/*      */         }
/*      */       } 
/* 1098 */       resaveVersion(completeVersion);
/*      */       
/* 1100 */       handleListener.installedSuccess();
/*      */     }
/* 1102 */     catch (Exception e) {
/* 1103 */       U.log(new Object[] { e });
/* 1104 */       handleListener.processError(e);
/*      */     }  } private GameEntityDTO initHanldeEntity(GameType type, File f, Path folder) throws ParseModPackException { ModDTO modDTO1; MapDTO mapDTO; ResourcePackDTO resourcePackDTO; ShaderpackDTO shaderpackDTO;
/*      */     ModVersionDTO modVersionDTO1;
/*      */     ModDTO modDTO;
/*      */     ModVersionDTO modVersionDTO;
/*      */     MapMetadataDTO mapMetadataDTO;
/* 1110 */     VersionDTO versionDTO = new VersionDTO();
/* 1111 */     switch (type) {
/*      */       case MOD:
/* 1113 */         modDTO = new ModDTO();
/* 1114 */         modVersionDTO = new ModVersionDTO();
/* 1115 */         modVersionDTO.setIncompatibleMods(new ArrayList());
/* 1116 */         modVersionDTO.setIncompatibleMods(new ArrayList());
/* 1117 */         modVersionDTO1 = modVersionDTO;
/* 1118 */         modDTO1 = modDTO;
/*      */         break;
/*      */       case MAP:
/* 1121 */         mapDTO = new MapDTO();
/*      */         break;
/*      */       case RESOURCEPACK:
/* 1124 */         resourcePackDTO = new ResourcePackDTO();
/*      */         break;
/*      */       case SHADERPACK:
/* 1127 */         shaderpackDTO = new ShaderpackDTO();
/*      */         break;
/*      */       default:
/* 1130 */         throw new ParseModPackException("not proper type");
/*      */     } 
/* 1132 */     shaderpackDTO.setId(Long.valueOf(-U.n()));
/* 1133 */     modVersionDTO1.setId(Long.valueOf(-U.n() - 1L));
/* 1134 */     shaderpackDTO.setVersion((VersionDTO)modVersionDTO1);
/* 1135 */     shaderpackDTO.setStateGameElement(StateGameElement.ACTIVE);
/* 1136 */     shaderpackDTO.setName(FilenameUtils.getBaseName(f.getName()));
/* 1137 */     shaderpackDTO.setUserInstall(true);
/* 1138 */     VersionDTO v = new VersionDTO();
/*      */     
/* 1140 */     v.setName("1.0");
/* 1141 */     MetadataDTO meta = new MetadataDTO();
/* 1142 */     if (type == GameType.MAP) {
/* 1143 */       MapMetadataDTO mapMetadata = new MapMetadataDTO();
/* 1144 */       mapMetadata.setFolders(analizeArchiver(f));
/* 1145 */       mapMetadataDTO = mapMetadata;
/*      */     } 
/*      */     
/* 1148 */     if (type == GameType.MAP) {
/* 1149 */       mapMetadataDTO.setPath("saves/" + f.getName());
/*      */     } else {
/* 1151 */       mapMetadataDTO.setPath(type.toString() + "s/" + f.getName());
/*      */     } 
/* 1153 */     v.setMetadata((MetadataDTO)mapMetadataDTO);
/* 1154 */     shaderpackDTO.setVersion(v);
/* 1155 */     return (GameEntityDTO)shaderpackDTO; }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void removeEntity(GameEntityDTO entity, VersionDTO versionDTO, GameType type) {
/* 1161 */     for (GameEntityListener l : this.gameListeners.get(type))
/* 1162 */       l.processingStarted(entity, versionDTO);  try {
/*      */       ModpackDTO modpackDTO; GameEntityDTO gameEntityDTO1;
/*      */       CompleteVersion version;
/* 1165 */       GameEntityDTO removedEntity = entity;
/* 1166 */       switch (type) {
/*      */         case MODPACK:
/* 1168 */           version = (this.tLauncher.getFrame()).mp.modpackScene.getCompleteVersion((ModpackDTO)entity, versionDTO);
/*      */           
/* 1170 */           if (Objects.isNull(version))
/*      */             return; 
/* 1172 */           this.tLauncher.getVersionManager().getLocalList().deleteVersion(version.getID(), false);
/* 1173 */           for (GameEntityListener l : this.gameListeners.get(type)) {
/* 1174 */             l.removeCompleteVersion(version);
/*      */           }
/* 1176 */           this.tLauncher.getVersionManager().getLocalList().refreshVersions();
/* 1177 */           modpackDTO = version.getModpack();
/*      */           break;
/*      */         case MOD:
/*      */         case MAP:
/*      */         case RESOURCEPACK:
/*      */         case SHADERPACK:
/* 1183 */           gameEntityDTO1 = findAndRemoveGameEntity(
/* 1184 */               (this.tLauncher.getFrame()).mp.modpackScene.getSelectedCompleteVersion(), entity, type); break;
/*      */       } 
/* 1186 */       for (GameEntityListener l : this.gameListeners.get(type)) {
/* 1187 */         l.removeEntity(gameEntityDTO1);
/*      */       }
/* 1189 */     } catch (Throwable e) {
/* 1190 */       U.log(new Object[] { e });
/* 1191 */       SwingUtilities.invokeLater(() -> {
/*      */             for (GameEntityListener l : this.gameListeners.get(type)) {
/*      */               l.installError(entity, versionDTO, e);
/*      */             }
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeEntity(GameEntityDTO entity, VersionDTO versionDTO, GameType type, boolean sync) {
/* 1201 */     removeEntity(entity, versionDTO, type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private GameEntityDTO findAndRemoveGameEntity(CompleteVersion selected, GameEntityDTO entity, GameType type) throws IOException, GameEntityNotFound {
/* 1207 */     ModpackVersionDTO versionDTO = (ModpackVersionDTO)selected.getModpack().getVersion();
/* 1208 */     GameEntityDTO current = findGameFromCollection(entity, type, versionDTO);
/* 1209 */     if (current != null) {
/* 1210 */       MetadataDTO meta = current.getVersion().getMetadata();
/* 1211 */       File removedFile = null;
/* 1212 */       File removedFileDeactivated = null;
/* 1213 */       switch (type) {
/*      */         case MOD:
/*      */         case RESOURCEPACK:
/*      */         case SHADERPACK:
/* 1217 */           removedFile = FileUtil.getRelative("versions/" + selected.getID() + "/" + meta.getPath()).toFile();
/*      */           
/* 1219 */           removedFileDeactivated = FileUtil.getRelative("versions/" + selected.getID() + "/" + meta.getPath() + ".deactivation").toFile();
/*      */           break;
/*      */ 
/*      */         
/*      */         case MAP:
/* 1224 */           removedFile = FileUtil.getRelative("versions/" + selected.getID() + FilenameUtils.removeExtension(meta.getPath())).toFile();
/*      */           break;
/*      */       } 
/* 1227 */       if (removedFile != null) {
/* 1228 */         FileUtil.deleteFile(removedFile);
/*      */       }
/* 1230 */       if (removedFileDeactivated != null) {
/* 1231 */         FileUtil.deleteFile(removedFileDeactivated);
/*      */       }
/* 1233 */       versionDTO.getByType(type).remove(current);
/* 1234 */       if (ModDTO.SKIN_MODS.contains(current.getId()))
/* 1235 */         selected.setSkinVersion(false); 
/* 1236 */       this.tLauncher.getVersionManager().getLocalList().refreshLocalVersion(selected);
/* 1237 */       return current;
/*      */     } 
/* 1239 */     throw new GameEntityNotFound("can't find in complete version: " + selected
/* 1240 */         .getID() + " gameEntity: " + entity.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   private GameEntityDTO findGameFromCollection(GameEntityDTO entity, GameType type, ModpackVersionDTO versionDTO) {
/* 1245 */     for (GameEntityDTO dto : versionDTO.getByType(type)) {
/* 1246 */       if (entity.getId().equals(dto.getId()))
/* 1247 */         return dto; 
/*      */     } 
/* 1249 */     return null;
/*      */   }
/*      */   
/*      */   public void addGameListener(GameType type, GameEntityListener listener) {
/* 1253 */     ((List<GameEntityListener>)this.gameListeners.get(type)).add(listener);
/*      */   }
/*      */   
/*      */   public void removeGameListener(GameType type, GameEntityListener listener) {
/* 1257 */     ((List)this.gameListeners.get(type)).remove(listener);
/*      */   }
/*      */ 
/*      */   
/*      */   public void renameModpack(CompleteVersion version, String newName) {
/*      */     try {
/* 1263 */       CompleteVersion newVersion = this.tLauncher.getVersionManager().getLocalList().renameVersion(version, newName);
/* 1264 */       this.tLauncher.getVersionManager().getLocalList().refreshVersions();
/* 1265 */       for (GameEntityListener l : this.gameListeners.get(GameType.MODPACK)) {
/* 1266 */         l.updateVersion(version, newVersion);
/*      */       }
/*      */       
/* 1269 */       version.setID(newName);
/* 1270 */     } catch (IOException e) {
/* 1271 */       U.log(new Object[] { e });
/* 1272 */       Alert.showError(Localizable.get("modpack.rename.exception.title"), 
/* 1273 */           Localizable.get("modpack.rename.exception"));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void resaveVersion(CompleteVersion completeVersion) {
/*      */     try {
/* 1279 */       TLauncher.getInstance().getVersionManager().getLocalList().refreshLocalVersion(completeVersion);
/* 1280 */       for (GameEntityListener l : this.gameListeners.get(GameType.MODPACK)) {
/* 1281 */         l.updateVersion(completeVersion, completeVersion);
/*      */       }
/* 1283 */     } catch (IOException e) {
/* 1284 */       U.log(new Object[] { e });
/* 1285 */       Alert.showError(Localizable.get("modpack.resave.exception.title"), 
/* 1286 */           Localizable.get("modpack.resave.exception"));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void resaveVersionWithNewForge(CompleteVersion completeVersion) {
/* 1291 */     resaveVersion(completeVersion);
/* 1292 */     for (GameEntityListener l : this.gameListeners.get(GameType.MODPACK)) {
/* 1293 */       l.updateVersionStorageAndScene(completeVersion, completeVersion);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void checkFolderSubGameEntity(CompleteVersion selectedValue, GameType current) {
/*      */     boolean find;
/* 1301 */     ModpackVersionDTO versionDTO = (ModpackVersionDTO)selectedValue.getModpack().getVersion();
/* 1302 */     Path subFolder = ModpackUtil.getPathByVersion((Version)selectedValue, new String[] { FileUtil.GameEntityFolder.getPath(current) });
/* 1303 */     if (Files.notExists(subFolder, new java.nio.file.LinkOption[0])) {
/*      */       return;
/*      */     }
/* 1306 */     switch (current) {
/*      */       case MOD:
/* 1308 */         find = isFind(FileUtils.listFiles(subFolder.toFile(), new String[] { "jar", "zip" }, true), current, versionDTO, subFolder);
/*      */         break;
/*      */       
/*      */       case RESOURCEPACK:
/*      */       case SHADERPACK:
/* 1313 */         find = isFind(FileUtils.listFiles(subFolder.toFile(), new String[] { "zip" }, true), current, versionDTO, subFolder);
/*      */         break;
/*      */       
/*      */       case MAP:
/* 1317 */         find = isFindMap(current, versionDTO, subFolder);
/*      */         break;
/*      */       default:
/*      */         return;
/*      */     } 
/* 1322 */     if (find) {
/* 1323 */       resaveVersion(selectedValue);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isFindMap(GameType current, ModpackVersionDTO versionDTO, Path subFolder) {
/* 1329 */     boolean find = false;
/* 1330 */     Set<String> set = new HashSet<>();
/* 1331 */     for (GameEntityDTO d : versionDTO.getByType(current)) {
/* 1332 */       if (((MapMetadataDTO)d.getVersion().getMetadata()).getFolders() != null)
/* 1333 */         set.addAll(((MapMetadataDTO)d.getVersion().getMetadata()).getFolders()); 
/*      */     } 
/* 1335 */     String[] array = subFolder.toFile().list((FilenameFilter)DirectoryFileFilter.DIRECTORY);
/* 1336 */     for (String m : (String[])Objects.<String[]>requireNonNull(array)) {
/* 1337 */       if (!set.contains(m)) {
/*      */         
/*      */         try {
/* 1340 */           GameEntityDTO dto = createHandleGameEntity(subFolder.toFile(), (Class)MapDTO.class, new File(subFolder
/* 1341 */                 .toFile(), m));
/* 1342 */           versionDTO.getByType(current).add(dto);
/* 1343 */         } catch (InstantiationException|IllegalAccessException e) {
/* 1344 */           U.log(new Object[] { e });
/*      */         } 
/* 1346 */         find = true;
/*      */       } 
/*      */     } 
/*      */     
/* 1350 */     return find;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isFind(Collection<File> list, GameType current, ModpackVersionDTO versionDTO, Path subFolder) {
/* 1355 */     boolean find = false;
/* 1356 */     for (File f : list) {
/* 1357 */       boolean foundFile = false;
/* 1358 */       String name = f.getName();
/* 1359 */       GameEntityDTO gd = null;
/*      */       
/*      */       try {
/* 1362 */         for (GameEntityDTO g : versionDTO.getByType(current)) {
/* 1363 */           gd = g;
/* 1364 */           if (g.getVersion().getMetadata().getPath().endsWith(name)) {
/* 1365 */             foundFile = true;
/*      */             break;
/*      */           } 
/*      */         } 
/* 1369 */       } catch (NullPointerException n) {
/* 1370 */         U.log(new Object[] { "meta is null " + gd.toString() });
/* 1371 */         throw n;
/*      */       } 
/* 1373 */       if (!foundFile) {
/*      */         try {
/* 1375 */           GameEntityDTO dto = createHandleGameEntity(subFolder.toFile(), GameType.createDTO(current), f);
/* 1376 */           versionDTO.getByType(current).add(dto);
/* 1377 */           find = true;
/* 1378 */         } catch (Exception e) {
/* 1379 */           log(new Object[] { e });
/*      */         } 
/*      */       }
/*      */     } 
/* 1383 */     return find;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkShaderStatus(CompleteVersion completeVersion) throws IOException {
/* 1388 */     List<ShaderpackDTO> shaderpacks = ((ModpackVersionDTO)completeVersion.getModpack().getVersion()).getShaderpacks();
/* 1389 */     if (shaderpacks.isEmpty())
/*      */       return; 
/* 1391 */     String name = ModpackUtil.readShaderpackConfigField(completeVersion, "shaderPack");
/*      */     
/* 1393 */     Optional<ShaderpackDTO> op = shaderpacks.stream().filter(d -> (d.getStateGameElement() == StateGameElement.ACTIVE)).findFirst();
/* 1394 */     if (StringUtils.isEmpty(name) && op.isPresent()) {
/* 1395 */       ((ShaderpackDTO)op.get()).setStateGameElement(StateGameElement.NO_ACTIVE);
/* 1396 */     } else if (StringUtils.isNotEmpty(name)) {
/* 1397 */       if (op.isPresent()) {
/* 1398 */         String fileName = FileUtil.getFilename(((ShaderpackDTO)op.get()).getVersion().getMetadata().getPath());
/* 1399 */         if (fileName.equalsIgnoreCase(name))
/*      */           return; 
/*      */       } 
/* 1402 */       shaderpacks.stream().filter(d -> (d.getStateGameElement() == StateGameElement.ACTIVE)).forEach(d -> {
/*      */             d.setStateGameElement(StateGameElement.NO_ACTIVE);
/*      */ 
/*      */ 
/*      */             
/*      */             SwingUtilities.invokeLater(());
/*      */           });
/*      */ 
/*      */       
/* 1411 */       shaderpacks.stream()
/* 1412 */         .filter(d -> FileUtil.getFilename(d.getVersion().getMetadata().getPath()).equalsIgnoreCase(name))
/* 1413 */         .forEach(d -> {
/*      */             d.setStateGameElement(StateGameElement.ACTIVE);
/*      */             SwingUtilities.invokeLater(());
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum ModpackServerCommand
/*      */   {
/* 1425 */     UPDATE, DOWNLOAD, ADD_NEW_GAME_ENTITY;
/*      */   }
/*      */   
/*      */   public void openModpackFolder(CompleteVersion version) {
/* 1429 */     OS.openFolder(FileUtil.getRelative("versions/" + version.getID()).toFile());
/*      */   }
/*      */ 
/*      */   
/*      */   public CompleteVersion getCompleteVersion(String name) throws IOException {
/*      */     try {
/* 1435 */       String value = Http.performGet(this.innerConfiguration.get("modpack.operation.url") + "read/forgeversion?name=" + name);
/*      */       
/* 1437 */       CompleteVersion completeVersion = (CompleteVersion)this.gson.fromJson(value, CompleteVersion.class);
/* 1438 */       completeVersion = completeVersion.resolve(this.tLauncher.getVersionManager(), true);
/* 1439 */       TlauncherUtil.processRemoteVersionToSave(completeVersion, value, this.gson);
/* 1440 */       return completeVersion;
/* 1441 */     } catch (NullPointerException e) {
/* 1442 */       log(new Object[] { "forge version " + name });
/* 1443 */       throw e;
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean fillFromCache(GameType t, GameEntityDTO e, ModpackVersionDTO versionDTO, Path versionFolder) {
/* 1448 */     for (CompleteVersion v : getModpackVersions()) {
/* 1449 */       if (v.getModpack().getVersion() != versionDTO) {
/* 1450 */         for (GameEntityDTO g : ((ModpackVersionDTO)v.getModpack().getVersion()).getByType(t)) {
/* 1451 */           if (((SubModpackDTO)g).getStateGameElement() != StateGameElement.NO_ACTIVE && !g.isUserInstall() && 
/* 1452 */             !e.isUserInstall() && e.getId().equals(g.getId()) && e
/* 1453 */             .getVersion().getId().equals(g.getVersion().getId())) {
/* 1454 */             Path cachedFolder = ModpackUtil.getPathByVersion(v);
/* 1455 */             if (!notExistOrCorrect(cachedFolder, e, true)) {
/* 1456 */               File dest = new File(versionFolder.toFile(), e.getVersion().getMetadata().getPath());
/*      */               
/*      */               try {
/* 1459 */                 FileUtil.copyFile(new File(cachedFolder
/* 1460 */                       .toFile(), e.getVersion().getMetadata().getPath()), dest, true);
/*      */                 
/* 1462 */                 return true;
/* 1463 */               } catch (IOException e1) {
/* 1464 */                 log(new Object[] { e1 });
/* 1465 */                 if (dest.exists())
/* 1466 */                   FileUtil.deleteFile(dest); 
/* 1467 */                 return false;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/* 1474 */     return false;
/*      */   }
/*      */   
/*      */   public List<GameEntityDTO> findDependenciesFromGameEntityDTO(GameEntityDTO entityDTO) {
/* 1478 */     List<GameEntityDTO> list = new ArrayList<>();
/* 1479 */     CompleteVersion completeVersion = (this.tLauncher.getFrame()).mp.modpackScene.getSelectedCompleteVersion();
/* 1480 */     if (completeVersion != null && !entityDTO.isUserInstall() && ((SubModpackDTO)entityDTO)
/* 1481 */       .getStateGameElement() != StateGameElement.NO_ACTIVE)
/* 1482 */       for (GameType t : GameType.values()) {
/* 1483 */         for (GameEntityDTO g : ((ModpackVersionDTO)completeVersion.getModpack().getVersion()).getByType(t)) {
/* 1484 */           if (g.getDependencies() != null && ((SubModpackDTO)g)
/* 1485 */             .getStateGameElement() != StateGameElement.NO_ACTIVE) {
/* 1486 */             for (GameEntityDependencyDTO d : g.getDependencies()) {
/* 1487 */               if (d.getDependencyType() == DependencyType.REQUIRED && entityDTO
/* 1488 */                 .getId().equals(d.getGameEntityId())) {
/* 1489 */                 list.add(g);
/*      */               }
/*      */             } 
/*      */           }
/*      */         } 
/*      */       }  
/* 1495 */     return list;
/*      */   }
/*      */   
/*      */   public void resetInfoMod() {
/* 1499 */     this.infoMod = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftPrepare() {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftAbort() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftLaunch() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftClose() {
/*      */     try {
/* 1520 */       ModpackScene scene = (this.tLauncher.getFrame()).mp.modpackScene;
/* 1521 */       if (scene.isSelectedCompleteVersion()) {
/* 1522 */         checkShaderStatus(scene.getSelectedCompleteVersion());
/*      */       }
/* 1524 */     } catch (IOException io) {
/* 1525 */       U.log(new Object[] { io });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftError(Throwable e) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftKnownError(MinecraftException e) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onMinecraftCrash(Crash crash) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void openModpackElement(Long id, GameType type) {
/* 1545 */     loadInfo();
/* 1546 */     SwingUtilities.invokeLater(() -> {
/*      */           GameEntityDTO dto = new GameEntityDTO();
/*      */           dto.setId(id);
/*      */           showFullGameEntity(dto, type);
/*      */           U.log(new Object[] { String.format("can't find game entity %s %s", new Object[] { type, id }) });
/*      */           this.tLauncher.getFrame().setAlwaysOnTop(true);
/*      */           this.tLauncher.getFrame().setAlwaysOnTop(false);
/*      */         });
/*      */   }
/*      */   
/*      */   public List<GameVersionDTO> getGameVersionsRemote(NameIdDTO nameIdDTO) throws IOException {
/* 1557 */     Map<String, Object> map = new HashMap<>();
/* 1558 */     map.put("minecraftVersionType", nameIdDTO.getId());
/* 1559 */     return (List<GameVersionDTO>)this.gsonService.getObjectWithoutSaving(Http.get(this.modpackApiURL + "gameversions", map), (new TypeToken<List<GameVersionDTO>>() {
/*      */         
/* 1561 */         }).getType());
/*      */   }
/*      */   
/*      */   public List<CategoryDTO> getCategories(GameType type) throws IOException {
/* 1565 */     return (List<CategoryDTO>)this.gsonService.getObjectWithoutSaving(this.modpackApiURL + "categories/" + type.toString(), (new TypeToken<List<CategoryDTO>>() {
/*      */         
/* 1567 */         }).getType());
/*      */   }
/*      */   
/*      */   public CategoryDTO[] getLocalCategories(GameType type) {
/* 1571 */     if (Objects.isNull(this.map.get(type))) {
/* 1572 */       return new CategoryDTO[0];
/*      */     }
/* 1574 */     return (CategoryDTO[])((List)this.map.get(type)).toArray((Object[])new CategoryDTO[0]);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<MinecraftVersionDTO> getVersionsByGameVersionAndMinecraftVersionType(Long id, NameIdDTO minecraftVersionType) throws IOException {
/* 1579 */     return (List<MinecraftVersionDTO>)this.gsonService.getObjectWithoutSaving(String.format("%s%s%s%s%s", new Object[] { this.modpackApiURL, "gameversions/", id, "/minecraftversiontypes/", minecraftVersionType
/* 1580 */             .getId() }), (new TypeToken<List<MinecraftVersionDTO>>() {  }
/* 1581 */         ).getType());
/*      */   }
/*      */   
/*      */   public GameVersionDTO getGameVersionByName(String name) throws IOException {
/* 1585 */     Map<String, Object> map = new HashMap<>();
/* 1586 */     map.put("name", name);
/* 1587 */     return (GameVersionDTO)this.gsonService.getObjectWithoutSaving(Http.get(this.modpackApiURL + "gameversions/search/findbyname", map), GameVersionDTO.class);
/*      */   }
/*      */ 
/*      */   
/*      */   public MinecraftVersionDTO getCompleteVersion(NameIdDTO type, NameIdDTO version) throws IOException {
/* 1592 */     return (MinecraftVersionDTO)this.gsonService.getObjectWithoutSaving(String.format("%s%s%s%s%s", new Object[] { this.modpackApiURL, "minecraftversiontypes/", type
/* 1593 */             .getId(), "/baseversion/", version.getId() }), MinecraftVersionDTO.class);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CommonPage<GameEntityDTO> getGameEntities(GameType type, NameIdDTO minecraftVesionType, GameVersionDTO gameVersion, String search, Set<CategoryDTO> categories, Integer page, GameEntitySort sort, boolean favorite) throws IOException {
/* 1599 */     Map<String, Object> map = new HashMap<>();
/* 1600 */     map.put("type", type);
/* 1601 */     if (Objects.nonNull(minecraftVesionType))
/* 1602 */       map.put("minecraftVesionType", minecraftVesionType.getId()); 
/* 1603 */     if (Objects.nonNull(gameVersion))
/* 1604 */       map.put("gameversion", gameVersion.getId()); 
/* 1605 */     if (Objects.nonNull(search))
/* 1606 */       map.put("search", search); 
/* 1607 */     if (Objects.nonNull(categories))
/* 1608 */       map.put("category", categories.stream().map(e -> e.getId().toString()).collect(Collectors.joining(","))); 
/* 1609 */     if (Objects.nonNull(page))
/* 1610 */       map.put("page", page); 
/* 1611 */     map.put("lang", this.tLauncher.getConfiguration().getLocale().toString().toUpperCase());
/* 1612 */     map.put("sort", sort);
/* 1613 */     if (favorite) {
/*      */       try {
/* 1615 */         map.put("uuid", this.tLauncher.getProfileManager().findUniqueTlauncherAccount().getUUID().toString());
/* 1616 */         map.put("favorite", "true");
/* 1617 */       } catch (SelectedAnyOneTLAccountException|RequiredTLAccountException e1) {
/* 1618 */         log(new Object[] { "some problem with favorites", e1 });
/*      */       } 
/*      */     }
/*      */     
/* 1622 */     return (CommonPage<GameEntityDTO>)this.gsonService.getObjectWithoutSaving(Http.get(this.modpackApiURL + "client/gameentities", map), (new TypeToken<CommonPage<GameEntityDTO>>() {
/*      */         
/* 1624 */         }).getType());
/*      */   }
/*      */   
/*      */   public GameEntityDTO getGameEntity(GameType type, Long id) throws IOException {
/* 1628 */     Map<String, Object> map = new HashMap<>();
/* 1629 */     map.put("type", type);
/* 1630 */     map.put("lang", this.tLauncher.getConfiguration().getLocale().toString().toUpperCase());
/* 1631 */     return (GameEntityDTO)this.gsonService.getObjectWithoutSaving(Http.get(this.modpackApiURL + "client/gameentities/" + id, map), 
/* 1632 */         GameType.createDTO(type));
/*      */   }
/*      */ 
/*      */   
/*      */   public GameEntityDTO getInstallingGameEntity(GameType type, GameEntityDTO gameEntity, VersionDTO version, GameVersionDTO gameVersion, NameIdDTO minecraftVersionType) throws IOException {
/* 1637 */     Map<String, Object> map = new HashMap<>();
/* 1638 */     map.put("type", type);
/* 1639 */     if (Objects.nonNull(gameVersion))
/* 1640 */       map.put("gameversion", gameVersion.getId()); 
/* 1641 */     if (Objects.nonNull(minecraftVersionType))
/* 1642 */       map.put("minecraftVersionType", minecraftVersionType.getId()); 
/* 1643 */     if (Objects.nonNull(version)) {
/* 1644 */       map.put("version", version.getId());
/*      */     }
/* 1646 */     String uri = Http.get(String.format("%sclient/gameentities/%s/installing", new Object[] { this.modpackApiURL, gameEntity.getId() }), map);
/*      */     
/* 1648 */     return (GameEntityDTO)this.gsonService.getObjectWithoutSaving(uri, GameType.createDTO(type));
/*      */   }
/*      */ 
/*      */   
/*      */   public ModpackVersionDTO getInstallingModpackVersionDTO(GameEntityDTO gameEntity, VersionDTO version) throws IOException {
/* 1653 */     Map<String, Object> map = new HashMap<>();
/* 1654 */     if (Objects.nonNull(version))
/* 1655 */       map.put("version", version.getId()); 
/* 1656 */     String uri = Http.get(String.format("%sclient/modpacks/%s/installing", new Object[] { this.modpackApiURL, gameEntity.getId() }), map);
/* 1657 */     return (ModpackVersionDTO)this.gsonService.getObjectWithoutSaving(uri, ModpackVersionDTO.class);
/*      */   }
/*      */   
/*      */   public Repo getGameEntitiesPictures(GameType type, Long gameEntity, PictureType pictureType) throws IOException {
/* 1661 */     Map<String, Object> map = new HashMap<>();
/* 1662 */     map.put("type", type);
/* 1663 */     map.put("picturetype", pictureType);
/* 1664 */     String uri = Http.get(String.format("%sclient/gameentities/%s/pictures", new Object[] { this.modpackApiURL, gameEntity }), map);
/* 1665 */     return (Repo)this.gsonService.getObjectWithoutSaving(uri, Repo.class);
/*      */   }
/*      */ 
/*      */   
/*      */   public CommonPage<VersionDTO> getGameEntityVersions(GameType type, Long gameEntity, Integer page) throws IOException {
/* 1670 */     Map<String, Object> map = new HashMap<>();
/* 1671 */     map.put("type", type);
/* 1672 */     map.put("page", page);
/* 1673 */     String uri = Http.get(String.format("%sclient/gameentities/%s/versions", new Object[] { this.modpackApiURL, gameEntity }), map);
/* 1674 */     return (CommonPage<VersionDTO>)this.gsonService.getObjectWithoutSaving(uri, (new TypeToken<CommonPage<VersionDTO>>() {  }
/* 1675 */         ).getType());
/*      */   }
/*      */   
/*      */   public List<GameEntityDTO> getModpackVersionSubElements(GameType type, Long gameEntity) throws IOException {
/* 1679 */     Map<String, Object> map = new HashMap<>();
/* 1680 */     map.put("type", type);
/* 1681 */     String uri = Http.get(String.format("%sclient/modpacks/%s/versions/subelements", new Object[] { this.modpackApiURL, gameEntity }), map);
/*      */     
/* 1683 */     return (List<GameEntityDTO>)this.gsonService.getObjectWithoutSaving(uri, (new TypeToken<List<GameEntityDTO>>() {  }
/* 1684 */         ).getType());
/*      */   }
/*      */   
/*      */   public void updateOldGameEntity(GameType type, Long gameEntity, String sourceURI) throws IOException {
/* 1688 */     Map<String, Object> map = new HashMap<>();
/* 1689 */     map.put("type", type);
/* 1690 */     map.put("url", sourceURI);
/*      */     
/* 1692 */     HttpPut put = new HttpPut(Http.get(String.format("%sclient/gameentities/old/%s", new Object[] { this.modpackApiURL, gameEntity }), map));
/* 1693 */     put.setConfig(this.requestConfig);
/* 1694 */     CloseableHttpResponse closeableHttpResponse = this.client.execute((HttpUriRequest)put);
/* 1695 */     HttpEntity entity = closeableHttpResponse.getEntity();
/* 1696 */     if (closeableHttpResponse.getStatusLine().getStatusCode() > 400) {
/* 1697 */       EntityUtils.consume(entity);
/* 1698 */       throw new IOException(String.valueOf(closeableHttpResponse.getStatusLine()));
/*      */     } 
/*      */   }
/*      */   
/*      */   public GameVersionDTO getGameVersion(ModpackVersionDTO mv) throws IOException {
/* 1703 */     if (Objects.isNull(mv.getGameVersionDTO())) {
/* 1704 */       mv.setGameVersionDTO(getGameVersionByName(mv.getGameVersion()));
/*      */     }
/* 1706 */     if (Objects.isNull(mv.getMinecraftVersionTypes())) {
/* 1707 */       NameIdDTO mvt = new NameIdDTO();
/* 1708 */       mvt.setId(Long.valueOf(1L));
/* 1709 */       mvt.setName("forge");
/* 1710 */       mv.setMinecraftVersionTypes(Lists.newArrayList((Object[])new NameIdDTO[] { mvt }));
/*      */     } 
/* 1712 */     if (Objects.isNull(mv.getMinecraftVersionName()) && Objects.isNull(mv.getForgeVersionDTO())) {
/* 1713 */       NameIdDTO mvt = new NameIdDTO();
/* 1714 */       mvt.setId(Long.valueOf(1L));
/* 1715 */       List<MinecraftVersionDTO> fv = getVersionsByGameVersionAndMinecraftVersionType(mv
/* 1716 */           .getGameVersionDTO().getId(), mv.findFirstMinecraftVersionType());
/*      */       
/* 1718 */       Optional<MinecraftVersionDTO> op = fv.stream().filter(e -> e.getName().equals(mv.getForgeVersion())).findFirst();
/* 1719 */       if (op.isPresent()) {
/* 1720 */         MinecraftVersionDTO nid = op.get();
/* 1721 */         mv.setMinecraftVersionName(new NameIdDTO(nid.getId(), nid.getName()));
/*      */       } 
/* 1723 */     } else if (Objects.isNull(mv.getMinecraftVersionName()) && Objects.nonNull(mv.getForgeVersionDTO())) {
/* 1724 */       ForgeVersionDTO fvdto = mv.getForgeVersionDTO();
/* 1725 */       NameIdDTO ver = new NameIdDTO();
/* 1726 */       ver.setId(fvdto.getId());
/* 1727 */       ver.setName(fvdto.getName());
/* 1728 */       mv.setMinecraftVersionName(ver);
/*      */     } 
/* 1730 */     return mv.getGameVersionDTO();
/*      */   }
/*      */   
/*      */   public GameEntityDTO getGameEntityDescriptions(GameType type, Long id) throws IOException {
/* 1734 */     Map<String, Object> map = new HashMap<>();
/* 1735 */     map.put("type", type);
/* 1736 */     map.put("lang", "en");
/* 1737 */     return (GameEntityDTO)this.gsonService.getObjectWithoutSaving(
/* 1738 */         Http.get(String.format("%sclient/gameentities/%s/descriptions", new Object[] { this.modpackApiURL, id }), map), 
/* 1739 */         GameType.createDTO(type));
/*      */   }
/*      */   
/*      */   public GameEntityDTO getGameEntityWithParserField(Long id, GameType type) throws IOException {
/* 1743 */     Map<String, Object> map = new HashMap<>();
/* 1744 */     map.put("type", type);
/* 1745 */     return (GameEntityDTO)this.gsonService.getObjectWithoutSaving(
/* 1746 */         Http.get(String.format("%sclient/gameentities/%s/parser/status", new Object[] { this.modpackApiURL, id }), map), 
/* 1747 */         GameType.createDTO(type));
/*      */   }
/*      */   public ParsedElementDTO labelForParsingGameEntity(Long id, GameType type) throws IOException {
/*      */     CloseableHttpResponse closeableHttpResponse;
/* 1751 */     Map<String, Object> map = new HashMap<>();
/* 1752 */     map.put("type", type);
/*      */     
/* 1754 */     HttpPatch patch = new HttpPatch(Http.get(String.format("%sclient/gameentities/%s/parser", new Object[] { this.modpackApiURL, id }), map));
/* 1755 */     HttpResponse hr = null;
/*      */     try {
/* 1757 */       closeableHttpResponse = this.closeableHttpClient.execute((HttpUriRequest)patch);
/* 1758 */       if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
/* 1759 */         U.log(new Object[] { "not proper response " + closeableHttpResponse.getStatusLine().toString() });
/* 1760 */         throw new IOException("not proper response " + closeableHttpResponse.getStatusLine());
/*      */       } 
/* 1762 */       return (ParsedElementDTO)this.gson.fromJson(IOUtils.toString(closeableHttpResponse.getEntity().getContent(), StandardCharsets.UTF_8), ParsedElementDTO.class);
/*      */     }
/*      */     finally {
/*      */       
/* 1766 */       if (Objects.nonNull(closeableHttpResponse)) {
/* 1767 */         EntityUtils.consumeQuietly(closeableHttpResponse.getEntity());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public List<NameIdDTO> getMinecraftVersionTypesRemote() throws IOException {
/* 1773 */     return (List<NameIdDTO>)this.gsonService.getObjectWithoutSaving(this.modpackApiURL + "minecraftversiontypes?lv=" + TLauncher.getVersion(), (new TypeToken<List<NameIdDTO>>() {
/*      */         
/* 1775 */         }).getType());
/*      */   }
/*      */   
/*      */   public String sendRequest(HttpRequestBase http, Object ob, String urn, Map<String, Object> map) throws IOException, ClientProtocolException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
/*      */     CloseableHttpResponse closeableHttpResponse;
/* 1780 */     if (Objects.nonNull(map))
/* 1781 */       urn = Http.get(urn, map); 
/* 1782 */     http.setURI(U.makeURI(urn));
/* 1783 */     if (Objects.nonNull(ob))
/* 1784 */       ((HttpEntityEnclosingRequestBase)http)
/* 1785 */         .setEntity((HttpEntity)new StringEntity(this.gson.toJson(ob), ContentType.APPLICATION_JSON)); 
/* 1786 */     http.setConfig(this.requestConfig);
/* 1787 */     TlauncherUtil.addAuthHeaders(http);
/* 1788 */     HttpResponse hr = null;
/*      */     try {
/* 1790 */       closeableHttpResponse = this.closeableHttpClient.execute((HttpUriRequest)http);
/* 1791 */       if (closeableHttpResponse.getStatusLine().getStatusCode() >= 300) {
/* 1792 */         throw new IOException("not proper code " + closeableHttpResponse.getStatusLine().toString());
/*      */       }
/* 1794 */       return IOUtils.toString(closeableHttpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
/*      */     } finally {
/*      */       
/* 1797 */       if (Objects.nonNull(closeableHttpResponse)) {
/* 1798 */         http.abort();
/* 1799 */         EntityUtils.consumeQuietly(closeableHttpResponse.getEntity());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFavoriteGameEntities(GameEntityDTO dto, GameType type) throws ClientProtocolException, IOException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
/* 1807 */     Account ac = this.tLauncher.getProfileManager().findUniqueTlauncherAccount();
/* 1808 */     Map<String, Object> map = new HashMap<>();
/* 1809 */     map.put("type", type);
/* 1810 */     sendRequest((HttpRequestBase)new HttpPost(), null, 
/* 1811 */         String.format("%suser/client/gameentities/%s/favorite/", new Object[] { this.modpackApiURL, dto.getId() }), map);
/* 1812 */     Set<Long> set = this.favoriteGameEntityIds.get(ac.getUUID());
/* 1813 */     if (Objects.isNull(set)) {
/* 1814 */       set = Collections.synchronizedSet(new HashSet<>());
/* 1815 */       this.favoriteGameEntityIds.put(ac.getUUID(), set);
/*      */     } 
/* 1817 */     set.add(dto.getId());
/*      */   }
/*      */ 
/*      */   
/*      */   public void deleteFavoriteGameEntities(GameEntityDTO dto, GameType type) throws ClientProtocolException, IOException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
/* 1822 */     Account ac = this.tLauncher.getProfileManager().findUniqueTlauncherAccount();
/* 1823 */     Map<String, Object> map = new HashMap<>();
/* 1824 */     map.put("type", type);
/* 1825 */     sendRequest((HttpRequestBase)new HttpDelete(), null, 
/* 1826 */         String.format("%suser/client/gameentities/%s/favorite/", new Object[] { this.modpackApiURL, dto.getId() }), map);
/* 1827 */     Set<Long> set = this.favoriteGameEntityIds.get(ac.getUUID());
/* 1828 */     if (Objects.nonNull(set))
/* 1829 */       set.remove(dto.getId()); 
/*      */   }
/*      */   public void getFavoriteGameEntities() {
/*      */     CloseableHttpResponse closeableHttpResponse;
/* 1833 */     log(new Object[] { "try to update favorite" });
/* 1834 */     HttpGet request = new HttpGet(String.format("%suser/client/gameentities/favorite", new Object[] { this.modpackApiURL }));
/* 1835 */     HttpResponse hr = null;
/*      */     try {
/* 1837 */       Account ac = this.tLauncher.getProfileManager().findUniqueTlauncherAccount();
/* 1838 */       if (this.favoriteGameEntityIds.containsKey(ac.getUUID()))
/*      */         return; 
/* 1840 */       log(new Object[] { "updated favorites for account " + ac.getDisplayName() });
/* 1841 */       request.setConfig(this.requestConfig);
/* 1842 */       TlauncherUtil.addAuthHeaders((HttpRequestBase)request);
/* 1843 */       closeableHttpResponse = this.closeableHttpClient.execute((HttpUriRequest)request);
/* 1844 */       if (closeableHttpResponse.getStatusLine().getStatusCode() != 200) {
/* 1845 */         U.log(new Object[] { "not proper response " + closeableHttpResponse.getStatusLine().toString() });
/*      */       } else {
/* 1847 */         List<NameIdDTO> list = (List<NameIdDTO>)this.gson.fromJson(new InputStreamReader(closeableHttpResponse.getEntity().getContent()), (new TypeToken<List<NameIdDTO>>() {
/*      */             
/* 1849 */             }).getType());
/* 1850 */         this.favoriteGameEntityIds.put(ac.getUUID(), 
/* 1851 */             Collections.synchronizedSet((Set<Long>)list.stream().map(e -> e.getId()).collect(Collectors.toSet())));
/*      */       } 
/* 1853 */     } catch (RequiredTLAccountException|SelectedAnyOneTLAccountException e) {
/* 1854 */       log(new Object[] { "couldn't get favorites for current accounts" });
/* 1855 */     } catch (Exception e) {
/* 1856 */       log(new Object[] { e });
/*      */     } finally {
/* 1858 */       if (Objects.nonNull(closeableHttpResponse)) {
/* 1859 */         request.abort();
/* 1860 */         EntityUtils.consumeQuietly(closeableHttpResponse.getEntity());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void importUserGameEntities(List<GameEntityDTO> list) throws ClientProtocolException, IOException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
/* 1868 */     readStatusGameElement();
/* 1869 */     if (!this.statusModpackElement.isEmpty()) {
/* 1870 */       sendRequest((HttpRequestBase)new HttpPost(), list, 
/* 1871 */           String.format("%suser/client/export/gameentities/favorite/", new Object[] { this.modpackApiURL }), null);
/* 1872 */       int size = this.statusModpackElement.size();
/* 1873 */       this.statusModpackElement.clear();
/* 1874 */       writeStatusGameElement();
/* 1875 */       log(new Object[] { "exported favorite game entities successfully " + size });
/* 1876 */       this.favoriteGameEntityIds.clear();
/*      */     } 
/*      */   }
/*      */   
/*      */   public Set<Long> getFavoriteGameEntitiesByAccount() {
/*      */     try {
/* 1882 */       Account ac = this.tLauncher.getProfileManager().findUniqueTlauncherAccount();
/* 1883 */       Set<Long> set = this.favoriteGameEntityIds.get(ac.getUUID());
/* 1884 */       if (Objects.nonNull(set))
/* 1885 */         return set; 
/* 1886 */     } catch (Throwable throwable) {}
/*      */     
/* 1888 */     return new HashSet<>();
/*      */   }
/*      */ 
/*      */   
/*      */   public void itemStateChanged(ItemEvent e) {
/* 1893 */     if (1 == e.getStateChange())
/* 1894 */       CompletableFuture.runAsync(() -> {
/*      */             PseudoScene s = (this.tLauncher.getFrame()).mp.getScene();
/*      */             if (s instanceof UpdateFavoriteValueListener) {
/*      */               getFavoriteGameEntities();
/*      */               SwingUtilities.invokeLater(());
/*      */             } 
/*      */           }); 
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\ModpackManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */