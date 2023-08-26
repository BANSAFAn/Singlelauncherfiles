/*    */ package org.tlauncher.tlauncher.controller;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import javax.inject.Inject;
/*    */ import javax.inject.Named;
/*    */ import net.minecraft.launcher.Http;
/*    */ import org.tlauncher.modpack.domain.client.AddedGameEntityDTO;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class AddedModpackStuffController {
/*    */   @Inject
/*    */   @Named("GsonCompleteVersion")
/*    */   private Gson gson;
/*    */   
/*    */   public void send(String link) {
/* 22 */     AddedGameEntityDTO en = new AddedGameEntityDTO();
/* 23 */     en.setUrl(link);
/*    */ 
/*    */     
/*    */     try {
/* 27 */       URL url = new URL(TLauncher.getInnerSettings().get("modpack.operation.url") + ModpackManager.ModpackServerCommand.ADD_NEW_GAME_ENTITY.toString().toLowerCase());
/* 28 */       Http.performPost(url, this.gson.toJson(en), "application/json");
/* 29 */       Alert.showLocMessage("modpack.send.success");
/* 30 */     } catch (IOException e) {
/* 31 */       Alert.showMonologError(Localizable.get().get("modpack.error.send.unsuccess"), 0);
/* 32 */       U.log(new Object[] { e });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\controller\AddedModpackStuffController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */