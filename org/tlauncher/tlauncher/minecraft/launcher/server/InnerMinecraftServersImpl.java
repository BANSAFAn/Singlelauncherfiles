/*     */ package org.tlauncher.tlauncher.minecraft.launcher.server;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.inject.Named;
/*     */ import net.minecraft.common.CompressedStreamTools;
/*     */ import net.minecraft.common.NBTBase;
/*     */ import net.minecraft.common.NBTTagCompound;
/*     */ import net.minecraft.common.NBTTagList;
/*     */ import net.minecraft.launcher.versions.Version;
/*     */ import org.apache.commons.lang3.time.DateUtils;
/*     */ import org.tlauncher.exceptions.ParseException;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.entity.server.RemoteServer;
/*     */ import org.tlauncher.tlauncher.entity.server.ReplacedAddressServer;
/*     */ import org.tlauncher.tlauncher.entity.server.Server;
/*     */ import org.tlauncher.tlauncher.managers.ServerList;
/*     */ import org.tlauncher.tlauncher.managers.ServerListManager;
/*     */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ @Singleton
/*     */ public class InnerMinecraftServersImpl implements InnerMinecraftServer {
/*  40 */   private static final File INNER_SERVERS_FILE = MinecraftUtil.getTLauncherFile("InnerStateServer-1.3.json");
/*  41 */   private static final File SERVERS_DAT_FILE = FileUtil.getRelative("servers.dat").toFile();
/*     */   
/*     */   @Inject
/*     */   private TLauncher tlauncher;
/*     */   
/*     */   private List<RemoteServer> innerServers;
/*     */   private List<Server> localServerList;
/*     */   @Inject
/*     */   @Named("GsonCompleteVersion")
/*     */   private Gson gson;
/*     */   
/*     */   public void prepareInnerServer() throws IOException {
/*  53 */     U.classNameLog(InnerMinecraftServersImpl.class, "prepare inner servers");
/*  54 */     ServerList serverList = ((ServerListManager)this.tlauncher.getManager().getComponent(ServerListManager.class)).getList();
/*  55 */     Configuration settings = this.tlauncher.getConfiguration();
/*     */     
/*  57 */     if (this.tlauncher.getProfileManager().isNotPremium() || settings.getBoolean("gui.settings.servers.recommendation")) {
/*  58 */       addNewServers(serverList.getNewServers());
/*     */       
/*  60 */       this.innerServers.stream().filter(s -> s.getState().equals(RemoteServer.ServerState.DEACTIVATED))
/*  61 */         .filter(s -> (s.getMaxRemovingCountServer().intValue() > s.getRemovedTime().size()))
/*  62 */         .filter(s -> {
/*     */             int hours = s.getRecoveryServerTime().intValue();
/*     */             
/*     */             Date addedDate = DateUtils.addHours(new Date(((Long)s.getRemovedTime().get(s.getRemovedTime().size() - 1)).longValue()), hours);
/*     */             return (new Date()).after(addedDate);
/*  67 */           }).forEach(s -> {
/*     */             s.setState(RemoteServer.ServerState.ACTIVE);
/*     */             if (!this.localServerList.contains(s))
/*     */               this.localServerList.add(0, s); 
/*     */           });
/*     */     } 
/*  73 */     if (this.tlauncher.getProfileManager().isNotPremium() || settings.getBoolean("gui.settings.guard.checkbox")) {
/*  74 */       this.innerServers.removeIf(s -> ((List)serverList.getRemovedServers().stream().map(String::toLowerCase).collect(Collectors.toList())).contains(s.getAddress().toLowerCase()));
/*     */       
/*  76 */       this.localServerList.removeIf(s -> ((List)serverList.getRemovedServers().stream().map(String::toLowerCase).collect(Collectors.toList())).contains(s.getAddress().toLowerCase()));
/*     */     } 
/*     */     
/*  79 */     for (ReplacedAddressServer s : serverList.getClientChangedAddress()) {
/*  80 */       this.localServerList.stream().filter(s1 -> s.getOldAddress().equalsIgnoreCase(s1.getAddress())).forEach(s1 -> {
/*     */             U.log(new Object[] { String.format("changed address from %s to %s ", new Object[] { s1.getAddress(), s.getNewAddress() }) });
/*     */             
/*     */             s1.setAddress(s.getNewAddress());
/*     */           });
/*     */     } 
/*     */     try {
/*  87 */       writeServerDatFile(SERVERS_DAT_FILE, this.localServerList);
/*  88 */       FileUtil.writeFile(INNER_SERVERS_FILE, this.gson.toJson(this.innerServers));
/*  89 */     } catch (RuntimeException e) {
/*  90 */       U.log(new Object[] { e });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void searchRemovedServers() throws IOException {
/*  96 */     U.classNameLog(InnerMinecraftServersImpl.class, "search changers of the servers");
/*  97 */     this.localServerList = readServerDatFile(SERVERS_DAT_FILE);
/*  98 */     this.innerServers.stream().filter(s -> s.getState().equals(RemoteServer.ServerState.ACTIVE))
/*  99 */       .filter(s -> !this.localServerList.contains(s)).forEach(s -> {
/*     */           s.setState(RemoteServer.ServerState.DEACTIVATED);
/*     */           
/*     */           s.getRemovedTime().add(Long.valueOf((new Date()).getTime()));
/*     */         });
/* 104 */     this.innerServers.removeIf(s -> (s.getState().equals(RemoteServer.ServerState.DEACTIVATED) && s.getRemovedTime().size() >= s.getMaxRemovingCountServer().intValue() && (new Date()).after(DateUtils.addDays(s.getAddedDate(), 5))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPageServer(Server server) throws IOException {
/* 111 */     this.localServerList = readServerDatFile(SERVERS_DAT_FILE);
/* 112 */     this.localServerList.removeIf(s -> s.getAddress().equals(server.getAddress()));
/* 113 */     this.localServerList.add(0, server);
/* 114 */     writeServerDatFile(SERVERS_DAT_FILE, this.localServerList);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPageServerToModpack(Server server, Version c) throws IOException {
/* 119 */     Path path = ModpackUtil.getPathByVersion(c, new String[] { "servers.dat" });
/* 120 */     List<Server> list = new ArrayList<>();
/* 121 */     if (Files.exists(path, new java.nio.file.LinkOption[0]))
/* 122 */       list = readServerDatFile(path.toFile()); 
/* 123 */     list.remove(server);
/* 124 */     list.add(0, server);
/* 125 */     writeServerDatFile(path.toFile(), list);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addNewServers(List<RemoteServer> remoteServers) {
/* 130 */     String lang = Localizable.get().getSelected().toString();
/* 131 */     remoteServers.stream().filter(s -> !this.localServerList.contains(s))
/* 132 */       .filter(s -> (s.getLocales().isEmpty() || s.getLocales().contains(lang)))
/* 133 */       .filter(s -> !this.innerServers.contains(s) ? true : this.innerServers.stream().filter(()).anyMatch(()))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 138 */       .forEach(s -> {
/*     */           s.initRemote();
/*     */           if (!this.innerServers.contains(s)) {
/*     */             this.innerServers.add(0, s);
/*     */           }
/*     */           this.localServerList.add(0, s);
/*     */         });
/*     */   }
/*     */   
/*     */   public void initInnerServers() {
/* 148 */     if (Files.exists(INNER_SERVERS_FILE.toPath(), new java.nio.file.LinkOption[0])) {
/*     */       try {
/* 150 */         this.innerServers = (List<RemoteServer>)this.gson.fromJson(FileUtil.readFile(INNER_SERVERS_FILE), (new TypeToken<List<RemoteServer>>() {
/*     */             
/* 152 */             }).getType());
/*     */         return;
/* 154 */       } catch (JsonParseException|IOException e) {
/* 155 */         U.log(new Object[] { e });
/*     */       } 
/*     */     }
/* 158 */     this.innerServers = new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Server> readServerDatFile(File file) throws IOException {
/* 163 */     List<Server> list = new ArrayList<>();
/* 164 */     NBTTagCompound compound = CompressedStreamTools.read(file);
/* 165 */     if (Objects.nonNull(compound)) {
/* 166 */       NBTTagList servers = compound.getTagList("servers");
/* 167 */       for (int i = 0; i < servers.tagCount(); i++) {
/*     */         try {
/* 169 */           list.add(Server.loadFromNBT((NBTTagCompound)servers.tagAt(i)));
/* 170 */         } catch (ParseException e) {
/* 171 */           U.log(new Object[] { "found server ", e });
/*     */         } 
/*     */       } 
/* 174 */       U.log(new Object[] { "read servers from servers.dat", list });
/*     */     } 
/* 176 */     return list;
/*     */   }
/*     */   
/*     */   private void writeServerDatFile(File file, List<Server> list) throws IOException {
/* 180 */     U.log(new Object[] { "save servers to servers.dat" });
/* 181 */     NBTTagList servers = new NBTTagList();
/* 182 */     list.forEach(s -> servers.appendTag((NBTBase)s.getNBT()));
/* 183 */     NBTTagCompound compound = new NBTTagCompound();
/* 184 */     compound.setTag("servers", (NBTBase)servers);
/* 185 */     CompressedStreamTools.safeWrite(compound, file);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\server\InnerMinecraftServersImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */