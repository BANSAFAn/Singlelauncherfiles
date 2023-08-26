/*     */ package org.tlauncher.tlauncher.managers;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonSyntaxException;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.minecraft.launcher.updater.AssetIndex;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*     */ import org.tlauncher.tlauncher.component.ComponentDependence;
/*     */ import org.tlauncher.tlauncher.component.LauncherComponent;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*     */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ 
/*     */ @ComponentDependence({VersionManager.class, VersionLists.class})
/*     */ public class AssetsManager
/*     */   extends LauncherComponent {
/*     */   public AssetsManager(ComponentManager manager) throws Exception {
/*  28 */     super(manager);
/*     */     
/*  30 */     this.gson = TLauncher.getGson();
/*  31 */     this.assetsFlushLock = new Object();
/*     */   }
/*     */   private final Gson gson;
/*     */   private final Object assetsFlushLock;
/*     */   
/*     */   public DownloadableContainer downloadResources(CompleteVersion version, List<AssetIndex.AssetObject> list, boolean force) {
/*  37 */     File baseDirectory = this.manager.getLauncher().getVersionManager().getLocalList().getBaseDirectory();
/*  38 */     DownloadableContainer container = new DownloadableContainer();
/*  39 */     container.addAll(getResourceFiles(version, baseDirectory, list));
/*  40 */     return container;
/*     */   }
/*     */   
/*     */   private static Set<Downloadable> getResourceFiles(CompleteVersion version, File baseDirectory, List<AssetIndex.AssetObject> list) {
/*  44 */     Set<Downloadable> result = new HashSet<>();
/*     */     
/*  46 */     File objectsFolder = new File(baseDirectory, "assets/objects");
/*     */     
/*  48 */     for (AssetIndex.AssetObject object : list) {
/*  49 */       String filename = object.getFilename();
/*  50 */       MetadataDTO metadataDTO = new MetadataDTO();
/*  51 */       metadataDTO.setUrl(filename);
/*  52 */       metadataDTO.setPath(filename);
/*  53 */       metadataDTO.setSha1(object.getHash());
/*  54 */       metadataDTO.setSize(object.getSize());
/*  55 */       metadataDTO.setLocalDestination(new File(objectsFolder, filename));
/*  56 */       Downloadable d = new Downloadable(ClientInstanceRepo.ASSETS_REPO, metadataDTO, false, true);
/*  57 */       result.add(d);
/*     */     } 
/*     */     
/*  60 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<AssetIndex.AssetObject> getResourceFiles(CompleteVersion version, File baseDirectory, boolean local) {
/*  65 */     List<AssetIndex.AssetObject> list = null;
/*     */     
/*  67 */     if (!local) {
/*     */       try {
/*  69 */         list = getRemoteResourceFilesList(version, baseDirectory, true);
/*  70 */       } catch (Exception e) {
/*  71 */         log(new Object[] { "Cannot get remote assets list. Trying to use the local one.", e });
/*     */       } 
/*     */     }
/*  74 */     if (list == null) {
/*  75 */       list = getLocalResourceFilesList(version, baseDirectory);
/*     */     }
/*  77 */     if (list == null) {
/*     */       try {
/*  79 */         list = getRemoteResourceFilesList(version, baseDirectory, true);
/*  80 */       } catch (Exception e) {
/*  81 */         log(new Object[] { "Gave up trying to get assets list.", e });
/*     */       } 
/*     */     }
/*  84 */     return list;
/*     */   }
/*     */   
/*     */   private List<AssetIndex.AssetObject> getLocalResourceFilesList(CompleteVersion version, File baseDirectory) {
/*  88 */     String json, indexName = version.getAssets();
/*     */     
/*  90 */     File indexesFolder = new File(baseDirectory, "assets/indexes/");
/*  91 */     File indexFile = new File(indexesFolder, indexName + ".json");
/*  92 */     if (Objects.nonNull(version.getAssetIndex())) {
/*  93 */       long size = version.getAssetIndex().getSize();
/*  94 */       if (size != 0L && version.getID().equals("1.14") && indexFile.length() != size) {
/*  95 */         log(new Object[] { "not new assets index file" });
/*  96 */         return null;
/*     */       } 
/*     */     } 
/*  99 */     log(new Object[] { "Reading indexes from file", indexFile });
/*     */     
/*     */     try {
/* 102 */       json = FileUtil.readFile(indexFile);
/* 103 */     } catch (Exception e) {
/* 104 */       log(new Object[] { "Cannot read local resource files list for index:", indexName, e });
/*     */       
/* 106 */       return null;
/*     */     } 
/* 108 */     AssetIndex index = null;
/*     */     try {
/* 110 */       index = (AssetIndex)this.gson.fromJson(json, AssetIndex.class);
/* 111 */     } catch (JsonSyntaxException e) {
/* 112 */       log(new Object[] { "JSON file is invalid", e });
/*     */     } 
/* 114 */     if (index == null) {
/* 115 */       log(new Object[] { "Cannot read data from JSON file." });
/* 116 */       return null;
/*     */     } 
/* 118 */     return Lists.newArrayList(index.getUniqueObjects());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<AssetIndex.AssetObject> getRemoteResourceFilesList(CompleteVersion version, File baseDirectory, boolean save) throws IOException {
/* 124 */     String json, indexName = version.getAssets();
/* 125 */     if (indexName == null) {
/* 126 */       indexName = "legacy";
/*     */     }
/* 128 */     File assets = new File(baseDirectory, "assets");
/* 129 */     File indexesFolder = new File(assets, "indexes");
/*     */     
/* 131 */     File indexFile = new File(indexesFolder, indexName + ".json");
/*     */     
/* 133 */     log(new Object[] { "Reading from repository..." });
/*     */ 
/*     */ 
/*     */     
/* 137 */     if (version.getAssetIndex() != null) {
/* 138 */       json = ClientInstanceRepo.EMPTY_REPO.getUrl(version.getAssetIndex().getUrl());
/*     */     } else {
/* 140 */       json = ClientInstanceRepo.OFFICIAL_VERSION_REPO.getUrl("indexes/" + indexName + ".json");
/*     */     } 
/*     */ 
/*     */     
/* 144 */     if (save)
/* 145 */       synchronized (this.assetsFlushLock) {
/* 146 */         FileUtil.writeFile(indexFile, json);
/*     */       }  
/* 148 */     return Lists.newArrayList(((AssetIndex)this.gson.fromJson(json, AssetIndex.class)).getUniqueObjects());
/*     */   }
/*     */ 
/*     */   
/*     */   private List<AssetIndex.AssetObject> checkResources(CompleteVersion version, File baseDirectory, boolean fast) {
/* 153 */     log(new Object[] { "Checking resources..." });
/*     */     
/* 155 */     List<AssetIndex.AssetObject> r = new ArrayList<>();
/*     */     
/* 157 */     List<AssetIndex.AssetObject> list = getResourceFiles(version, baseDirectory, fast);
/*     */     
/* 159 */     if (list == null) {
/* 160 */       log(new Object[] { "Cannot get assets list. Aborting." });
/* 161 */       return r;
/*     */     } 
/*     */     
/* 164 */     log(new Object[] { "Fast comparing:", Boolean.valueOf(fast) });
/*     */     
/* 166 */     for (AssetIndex.AssetObject resource : list) {
/* 167 */       if (!checkResource(baseDirectory, resource, fast))
/* 168 */         r.add(resource); 
/*     */     } 
/* 170 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<AssetIndex.AssetObject> checkResources(CompleteVersion version, boolean fast) {
/* 175 */     return checkResources(version, ((VersionLists)this.manager.<VersionLists>getComponent(VersionLists.class))
/* 176 */         .getLocal().getBaseDirectory(), fast);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean checkResource(File baseDirectory, AssetIndex.AssetObject local, boolean fast) {
/* 181 */     String path = local.getFilename();
/*     */     
/* 183 */     File file = new File(baseDirectory, "assets/objects/" + path);
/* 184 */     long size = file.length();
/*     */     
/* 186 */     if (!file.isFile() || size == 0L) {
/* 187 */       return false;
/*     */     }
/* 189 */     if (fast) {
/* 190 */       return true;
/*     */     }
/* 192 */     if (local.getHash() == null) {
/* 193 */       return true;
/*     */     }
/* 195 */     return local.getHash().equals(FileUtil.getChecksum(file));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\AssetsManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */