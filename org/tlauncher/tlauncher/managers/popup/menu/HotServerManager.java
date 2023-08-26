/*     */ package org.tlauncher.tlauncher.managers.popup.menu;
/*     */ import ch.jamiete.mcping.MinecraftPing;
/*     */ import ch.jamiete.mcping.MinecraftPingOptions;
/*     */ import ch.jamiete.mcping.MinecraftPingReply;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.launcher.updater.VersionFilter;
/*     */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.ReleaseType;
/*     */ import org.tlauncher.tlauncher.entity.ServerInfo;
/*     */ import org.tlauncher.tlauncher.entity.hot.AdditionalHotServer;
/*     */ import org.tlauncher.tlauncher.entity.hot.AdditionalHotServers;
/*     */ import org.tlauncher.tlauncher.entity.server.RemoteServer;
/*     */ import org.tlauncher.tlauncher.entity.server.Server;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.managers.VersionManagerAdapter;
/*     */ import org.tlauncher.tlauncher.minecraft.crash.Crash;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServer;
/*     */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.menu.PopupMenuModel;
/*     */ import org.tlauncher.tlauncher.ui.menu.PopupMenuView;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.AsyncThread;
/*     */ import org.tlauncher.util.gson.DownloadUtil;
/*     */ import org.tlauncher.util.statistics.StatisticsUtil;
/*     */ 
/*     */ @Singleton
/*     */ public class HotServerManager extends VersionManagerAdapter implements MinecraftListener {
/*  47 */   private final Map<String, PopupMenuModel> hashMap = Collections.synchronizedMap(new HashMap<>());
/*     */   private boolean serviceAvailable = true;
/*     */   private PopupMenuModel current;
/*  50 */   private final VersionFilter filter = (new VersionFilter()).exclude(new ReleaseType[] { ReleaseType.SNAPSHOT });
/*     */   
/*     */   private AdditionalHotServers additionalHotServers;
/*     */   
/*     */   private List<ServerInfo> hotServers;
/*     */   
/*     */   @Inject
/*     */   private InnerMinecraftServer innerMinecraftServer;
/*     */   @Inject
/*     */   private TLauncher tLauncher;
/*     */   
/*     */   public void processingEvent(String serverId) {
/*  62 */     if (this.serviceAvailable) {
/*  63 */       this.current = this.hashMap.get(serverId);
/*  64 */       if (this.current != null) {
/*     */         
/*  66 */         PopupMenuView view = this.current.isMainPage() ? (TLauncher.getInstance().getFrame()).mp.defaultScene.getPopupMenuView() : (TLauncher.getInstance().getFrame()).mp.additionalHostServerScene.getPopupMenuView();
/*  67 */         view.showSelectedModel(this.current);
/*     */       } else {
/*  69 */         U.log(new Object[] { "server id hasn't found = " + serverId });
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void launchGame(VersionSyncInfo name) {
/*  75 */     changeVersion(name);
/*  76 */     block();
/*  77 */     (TLauncher.getInstance().getFrame()).mp.openDefaultScene();
/*  78 */     RemoteServer s = new RemoteServer();
/*  79 */     s.setOfficialAccount(this.current.getInfo().isMojangAccount());
/*  80 */     if (!(this.current.getInfo() instanceof AdditionalHotServer)) {
/*     */       try {
/*  82 */         getMinecraftPingReplyAndResolvedAddress(this.current.getInfo());
/*  83 */       } catch (Throwable e) {
/*  84 */         U.log(new Object[] { e });
/*     */       } 
/*     */     }
/*  87 */     s.setAddress(this.current.getResolvedAddress());
/*  88 */     s.setName(this.current.getName());
/*  89 */     sendStat();
/*  90 */     addServerToList(false, name);
/*  91 */     (TLauncher.getInstance().getFrame()).mp.defaultScene.loginForm.startLauncher(s);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendStat() {
/*     */     String type;
/*  98 */     if (this.current.isMainPage()) {
/*  99 */       type = "main/page";
/*     */     } else {
/* 101 */       type = "additional";
/* 102 */     }  HashMap<String, Object> map = new HashMap<>();
/* 103 */     map.put("version", this.current.getServerId());
/* 104 */     StatisticsUtil.startSending("save/hot/server/" + type, null, map);
/*     */   }
/*     */   
/*     */   public void copyAddress() {
/* 108 */     StringSelection selection = new StringSelection(this.current.getAddress());
/* 109 */     Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 110 */     clipboard.setContents(selection, selection);
/* 111 */     Alert.showMessage("", Localizable.get().get("menu.copy.done"));
/*     */   }
/*     */   
/*     */   private void changeVersion(VersionSyncInfo version) {
/* 115 */     this.current.setSelected(version);
/* 116 */     (this.tLauncher.getFrame()).mp.defaultScene.loginForm.versions.setSelectedValue(version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addServerToList(boolean showMessage, VersionSyncInfo v) {
/* 125 */     this.innerMinecraftServer.initInnerServers();
/* 126 */     Server server = new Server();
/* 127 */     server.setAddress(this.current.getAddress());
/* 128 */     server.setName(server.getIp());
/*     */     try {
/* 130 */       if (v.isInstalled() && ((CompleteVersion)v.getLocal()).isModpack())
/* 131 */       { this.innerMinecraftServer.addPageServerToModpack(server, v.getLocal()); }
/* 132 */       else { this.innerMinecraftServer.addPageServer(server); } 
/* 133 */     } catch (Throwable e) {
/* 134 */       U.log(new Object[] { e });
/*     */     } 
/* 136 */     if (showMessage) {
/* 137 */       Alert.showMessage("", Localizable.get().get("menu.favorite.done"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftPrepare() {
/* 143 */     block();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftAbort() {
/* 149 */     enablePopup();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftLaunch() {
/* 155 */     block();
/*     */     try {
/* 157 */       if (!this.tLauncher.getLauncher().getVersion().isModpack()) {
/* 158 */         this.innerMinecraftServer.initInnerServers();
/* 159 */         this.innerMinecraftServer.searchRemovedServers();
/* 160 */         this.innerMinecraftServer.prepareInnerServer();
/*     */       } 
/* 162 */     } catch (Throwable e) {
/* 163 */       U.log(new Object[] { e });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftClose() {
/* 169 */     enablePopup();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftError(Throwable e) {
/* 175 */     enablePopup();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftKnownError(MinecraftException e) {
/* 181 */     enablePopup();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftCrash(Crash crash) {
/* 187 */     enablePopup();
/*     */   }
/*     */ 
/*     */   
/*     */   public void enablePopup() {
/* 192 */     this.serviceAvailable = true;
/*     */   }
/*     */   
/*     */   private void block() {
/* 196 */     this.serviceAvailable = false;
/*     */   }
/*     */   
/*     */   private void addServers(List<? extends ServerInfo> list, boolean mainPage, List<VersionSyncInfo> versions) {
/* 200 */     for (ServerInfo info : list) {
/* 201 */       if (this.hashMap.get(info.getServerId()) != null) {
/* 202 */         U.log(new Object[] { "!!!the same id was found: " + info.getServerId() });
/*     */         continue;
/*     */       } 
/* 205 */       PopupMenuModel p = new PopupMenuModel(findAvailableVersions(info, versions), info, mainPage);
/* 206 */       this.hashMap.put(info.getServerId(), p);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<VersionSyncInfo> findAvailableVersions(ServerInfo serverInfo, List<VersionSyncInfo> versionList) {
/* 213 */     List<VersionSyncInfo> list = new ArrayList<>();
/* 214 */     for (VersionSyncInfo v : versionList) {
/* 215 */       if (!this.filter.satisfies(v.getAvailableVersion()) || serverInfo
/* 216 */         .getIgnoreVersions().contains(v.getID())) {
/*     */         continue;
/*     */       }
/* 219 */       list.add(v);
/* 220 */       if (Objects.equals(serverInfo.getMinVersion(), v.getID())) {
/*     */         break;
/*     */       }
/*     */     } 
/* 224 */     versionList.stream().filter(v -> this.filter.satisfies(v.getAvailableVersion()))
/* 225 */       .filter(v -> serverInfo.getIncludeVersions().contains(v.getID())).forEach(list::add);
/* 226 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionsRefreshed(VersionManager manager) {
/* 231 */     List<VersionSyncInfo> list = manager.getVersions();
/* 232 */     this.hashMap.clear();
/* 233 */     AsyncThread.execute(() -> {
/*     */           try {
/*     */             if (Objects.isNull(this.additionalHotServers)) {
/*     */               AdditionalHotServers servers = (AdditionalHotServers)DownloadUtil.loadByRepository(ClientInstanceRepo.ADD_HOT_SERVERS_REPO, AdditionalHotServers.class);
/*     */               
/*     */               servers.setList((List)servers.getList().stream().filter(AdditionalHotServer::isActive).collect(Collectors.toList()));
/*     */               if (servers.isShuffle()) {
/*     */                 Collections.shuffle(servers.getList());
/*     */               }
/*     */               this.additionalHotServers = servers;
/*     */             } 
/*     */             addServers(this.additionalHotServers.getList(), false, list);
/* 245 */           } catch (Throwable e) {
/*     */             U.log(new Object[] { "can't load additional servers", e });
/*     */           } 
/*     */         });
/* 249 */     AsyncThread.execute(() -> {
/*     */           try {
/*     */             if (Objects.isNull(this.hotServers))
/*     */               this.hotServers = (List<ServerInfo>)DownloadUtil.loadByRepository(ClientInstanceRepo.HOT_SERVERS_REPO, (new TypeToken<List<ServerInfo>>()
/*     */                   {
/*     */                   
/*     */                   }).getType()); 
/*     */             addServers(this.hotServers, true, list);
/* 257 */           } catch (Throwable e) {
/*     */             U.log(new Object[] { "can't load hot servers", e });
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public AdditionalHotServers getAdditionalHotServers() {
/* 264 */     return this.additionalHotServers;
/*     */   }
/*     */   
/*     */   public boolean isReady() {
/* 268 */     return Objects.nonNull(this.additionalHotServers);
/*     */   }
/*     */   
/*     */   public void fillServer(AdditionalHotServer s) {
/*     */     try {
/* 273 */       MinecraftPingReply data = getMinecraftPingReplyAndResolvedAddress((ServerInfo)s);
/* 274 */       s.setOnline(Integer.valueOf(data.getPlayers().getOnline()));
/* 275 */       s.setMax(Integer.valueOf(data.getPlayers().getMax()));
/* 276 */       s.setUpdated(DateUtils.addMinutes(new Date(), 15));
/* 277 */       s.setImage(data.getFavicon());
/* 278 */     } catch (Throwable t) {
/* 279 */       U.log(new Object[] { t });
/*     */     } 
/*     */   }
/*     */   private MinecraftPingReply getMinecraftPingReplyAndResolvedAddress(ServerInfo s) throws IOException {
/*     */     MinecraftPingReply data;
/* 284 */     String[] serverConfig = s.getAddress().split(":");
/*     */     
/* 286 */     MinecraftPing p = new MinecraftPing();
/*     */     
/* 288 */     MinecraftPingOptions options = (new MinecraftPingOptions()).setHostname(serverConfig[0]).setPort(Integer.parseInt(serverConfig[1]));
/*     */     try {
/* 290 */       data = p.getPing(options);
/* 291 */     } catch (Throwable t) {
/* 292 */       p.resolveDNS(options);
/* 293 */       s.setRedirectAddress(String.format("%s:%s", new Object[] { options.getHostname(), Integer.valueOf(options.getPort()) }));
/* 294 */       data = p.getPing(options);
/*     */     } 
/* 296 */     return data;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\popup\menu\HotServerManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */