/*    */ package org.tlauncher.tlauncher.repository;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ 
/*    */ 
/*    */ public class ClientInstanceRepo
/*    */ {
/* 11 */   public static final Repo LOCAL_VERSION_REPO = new Repo(new String[0], "LOCAL_VERSION_REPO");
/* 12 */   public static final Repo OFFICIAL_VERSION_REPO = new Repo(TLauncher.getInnerSettings().getArray("official.repo"), "OFFICIAL_VERSION_REPO");
/* 13 */   public static final Repo EMPTY_REPO = new Repo(TLauncher.getInnerSettings().getArray("empty.repositories"), "EMPTY");
/* 14 */   public static final Repo EXTRA_VERSION_REPO = new Repo(TLauncher.getInnerSettings().getArray("tlauncher.versions.repo"), "EXTRA_VERSION_REPO");
/* 15 */   public static final Repo ASSETS_REPO = new Repo(TLauncher.getInnerSettings().getArray("assets.repo"), "ASSETS_REPO");
/* 16 */   public static final Repo LIBRARY_REPO = new Repo(TLauncher.getInnerSettings().getArray("library.repo"), "LIBRARY_REPO");
/* 17 */   public static final Repo SKIN_VERSION_REPO = new Repo(TLauncher.getInnerSettings().getArray("skin.extra.repo"), "SKIN_VERSION_REPO");
/* 18 */   public static final Repo HOT_SERVERS_REPO = new Repo(TLauncher.getInnerSettings().getArray("hot.servers"), "HOT_SERVERS_REPO");
/* 19 */   public static final Repo ADD_HOT_SERVERS_REPO = new Repo(TLauncher.getInnerSettings().getArray("add.hot.servers"), "ADD_HOT_SERVERS_REPO");
/*    */   
/* 21 */   public static final Repo SERVER_LIST_REPO = new Repo(new String[] { "http://repo.tlauncher.org/update/downloads/configs/inner_servers.json", "https://tlauncher.org/repo/update/downloads/configs/inner_servers.json", "http://advancedrepository.com/update/downloads/configs/inner_servers.json" }, "SERVER_LIST_REPO");
/*    */ 
/*    */   
/* 24 */   private static final List<Repo> LIST = new ArrayList<Repo>()
/*    */     {
/*    */     
/*    */     };
/*    */ 
/*    */ 
/*    */   
/*    */   public static Repo find(String name) {
/* 32 */     if (name.isEmpty())
/* 33 */       return EMPTY_REPO; 
/* 34 */     Optional<Repo> repo = LIST.stream().filter(r -> r.getName().equalsIgnoreCase(name)).findFirst();
/* 35 */     if (repo.isPresent())
/* 36 */       return repo.get(); 
/* 37 */     throw new RuntimeException("can't find proper repo " + name);
/*    */   }
/*    */   
/*    */   public static Repo createModpackRepo() {
/* 41 */     return new Repo(TLauncher.getInnerSettings().getArray("file.server"), "MODPACK_REPO");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\repository\ClientInstanceRepo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */