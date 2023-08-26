/*    */ package org.tlauncher.tlauncher.managers;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import com.google.inject.Key;
/*    */ import com.google.inject.name.Names;
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.tlauncher.tlauncher.component.InterruptibleComponent;
/*    */ import org.tlauncher.tlauncher.entity.AdditionalAsset;
/*    */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ public class AdditionalAssetsComponent
/*    */   extends InterruptibleComponent
/*    */ {
/* 20 */   private List<AdditionalAsset> additionalAssets = new ArrayList<>(); public List<AdditionalAsset> getAdditionalAssets() { return this.additionalAssets; }
/*    */ 
/*    */   
/*    */   public AdditionalAssetsComponent(ComponentManager manager) throws Exception {
/* 24 */     super(manager);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean refresh(int refreshID) {
/* 29 */     String result = "";
/*    */     try {
/* 31 */       result = ClientInstanceRepo.EXTRA_VERSION_REPO.getUrl("additional_assets-1.0.json");
/* 32 */       Gson gson = (Gson)TLauncher.getInjector().getInstance(Key.get(Gson.class, (Annotation)Names.named("GsonAdditionalFile")));
/* 33 */       this.additionalAssets = (List<AdditionalAsset>)gson.fromJson(result, (new TypeToken<List<AdditionalAsset>>() {  }
/* 34 */           ).getType());
/* 35 */       return true;
/* 36 */     } catch (Exception e) {
/* 37 */       U.log(new Object[] { e });
/* 38 */       U.log(new Object[] { result });
/*    */       
/* 40 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\AdditionalAssetsComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */