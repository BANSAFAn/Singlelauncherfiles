/*    */ package org.tlauncher.tlauncher.managers;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import org.tlauncher.tlauncher.component.RefreshableComponent;
/*    */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*    */ import org.tlauncher.tlauncher.repository.Repo;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ 
/*    */ public class ServerListManager extends RefreshableComponent {
/*    */   private final Gson gson;
/*    */   private final Repo repo;
/* 12 */   private ServerList serverList = new ServerList();
/*    */   
/*    */   public ServerListManager(ComponentManager manager) throws Exception {
/* 15 */     super(manager);
/* 16 */     this.repo = ClientInstanceRepo.SERVER_LIST_REPO;
/* 17 */     this.gson = TLauncher.getGson();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean refresh() {
/*    */     try {
/* 23 */       this.serverList = (ServerList)this.gson.fromJson(this.repo.getUrl(), ServerList.class);
/* 24 */       return true;
/* 25 */     } catch (Throwable throwable) {
/*    */       
/* 27 */       return false;
/*    */     } 
/*    */   }
/*    */   public ServerList getList() {
/* 31 */     return this.serverList;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\ServerListManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */