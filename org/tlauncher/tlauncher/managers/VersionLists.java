/*    */ package org.tlauncher.tlauncher.managers;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.launcher.updater.ExtraVersionList;
/*    */ import net.minecraft.launcher.updater.LocalVersionList;
/*    */ import net.minecraft.launcher.updater.OfficialVersionList;
/*    */ import net.minecraft.launcher.updater.RemoteVersionList;
/*    */ import net.minecraft.launcher.updater.SkinVersionList;
/*    */ import org.tlauncher.tlauncher.component.LauncherComponent;
/*    */ import org.tlauncher.util.MinecraftUtil;
/*    */ 
/*    */ 
/*    */ public class VersionLists
/*    */   extends LauncherComponent
/*    */ {
/*    */   private final LocalVersionList localList;
/*    */   private final RemoteVersionList[] remoteLists;
/*    */   
/*    */   public VersionLists(ComponentManager manager) throws Exception {
/* 20 */     super(manager);
/*    */     
/* 22 */     this.localList = new LocalVersionList(MinecraftUtil.getWorkingDirectory());
/*    */     
/* 24 */     OfficialVersionList officialList = new OfficialVersionList();
/* 25 */     ExtraVersionList extraList = new ExtraVersionList();
/* 26 */     SkinVersionList skinVersionList = new SkinVersionList();
/*    */     
/* 28 */     this.remoteLists = new RemoteVersionList[] { (RemoteVersionList)officialList, (RemoteVersionList)extraList, (RemoteVersionList)skinVersionList };
/*    */   }
/*    */   
/*    */   public LocalVersionList getLocal() {
/* 32 */     return this.localList;
/*    */   }
/*    */   
/*    */   public void updateLocal() throws IOException {
/* 36 */     this.localList.setBaseDirectory(MinecraftUtil.getWorkingDirectory());
/*    */   }
/*    */   
/*    */   public RemoteVersionList[] getRemoteLists() {
/* 40 */     return this.remoteLists;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\VersionLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */