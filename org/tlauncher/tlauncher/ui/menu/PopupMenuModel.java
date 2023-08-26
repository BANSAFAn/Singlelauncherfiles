/*    */ package org.tlauncher.tlauncher.ui.menu;
/*    */ 
/*    */ import net.minecraft.launcher.updater.VersionSyncInfo;
/*    */ import org.tlauncher.tlauncher.entity.ServerInfo;
/*    */ 
/*    */ public class PopupMenuModel {
/*    */   private List<VersionSyncInfo> servers;
/*    */   private ServerInfo info;
/*    */   
/* 10 */   public void setServers(List<VersionSyncInfo> servers) { this.servers = servers; } private boolean mainPage; private VersionSyncInfo selected; public void setInfo(ServerInfo info) { this.info = info; } public void setMainPage(boolean mainPage) { this.mainPage = mainPage; } public void setSelected(VersionSyncInfo selected) { this.selected = selected; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof PopupMenuModel)) return false;  PopupMenuModel other = (PopupMenuModel)o; if (!other.canEqual(this)) return false;  Object<VersionSyncInfo> this$servers = (Object<VersionSyncInfo>)getServers(), other$servers = (Object<VersionSyncInfo>)other.getServers(); if ((this$servers == null) ? (other$servers != null) : !this$servers.equals(other$servers)) return false;  Object this$info = getInfo(), other$info = other.getInfo(); if ((this$info == null) ? (other$info != null) : !this$info.equals(other$info)) return false;  if (isMainPage() != other.isMainPage()) return false;  Object this$selected = getSelected(), other$selected = other.getSelected(); return !((this$selected == null) ? (other$selected != null) : !this$selected.equals(other$selected)); } protected boolean canEqual(Object other) { return other instanceof PopupMenuModel; } public int hashCode() { int PRIME = 59; result = 1; Object<VersionSyncInfo> $servers = (Object<VersionSyncInfo>)getServers(); result = result * 59 + (($servers == null) ? 43 : $servers.hashCode()); Object $info = getInfo(); result = result * 59 + (($info == null) ? 43 : $info.hashCode()); result = result * 59 + (isMainPage() ? 79 : 97); Object $selected = getSelected(); return result * 59 + (($selected == null) ? 43 : $selected.hashCode()); } public String toString() { return "PopupMenuModel(servers=" + getServers() + ", info=" + getInfo() + ", mainPage=" + isMainPage() + ", selected=" + getSelected() + ")"; }
/*    */    public List<VersionSyncInfo> getServers() {
/* 12 */     return this.servers;
/*    */   }
/* 14 */   public boolean isMainPage() { return this.mainPage; } public VersionSyncInfo getSelected() {
/* 15 */     return this.selected;
/*    */   }
/*    */   public ServerInfo getInfo() {
/* 18 */     return this.info;
/*    */   }
/*    */   
/*    */   public PopupMenuModel(List<VersionSyncInfo> servers, ServerInfo info, boolean mainPage) {
/* 22 */     this.info = info;
/* 23 */     this.servers = servers;
/* 24 */     this.mainPage = mainPage;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 28 */     return getAddress().split(":")[0];
/*    */   }
/*    */   
/*    */   public String getAddress() {
/* 32 */     return this.info.getAddress();
/*    */   }
/*    */   
/*    */   public String getResolvedAddress() {
/* 36 */     if (Objects.nonNull(this.info.getRedirectAddress()))
/* 37 */       return this.info.getRedirectAddress(); 
/* 38 */     return getAddress();
/*    */   }
/*    */   public String getServerId() {
/* 41 */     return this.info.getServerId();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\menu\PopupMenuModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */