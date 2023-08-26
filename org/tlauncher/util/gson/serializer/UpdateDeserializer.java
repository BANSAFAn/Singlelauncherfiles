/*    */ package org.tlauncher.util.gson.serializer;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import org.tlauncher.tlauncher.updater.client.Banner;
/*    */ import org.tlauncher.tlauncher.updater.client.Offer;
/*    */ import org.tlauncher.tlauncher.updater.client.Update;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class UpdateDeserializer implements JsonDeserializer<Update> {
/*    */   public Update deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
/*    */     try {
/* 20 */       return deserialize0(json, context);
/* 21 */     } catch (Exception e) {
/* 22 */       U.log(new Object[] { "Cannot parse update:", e });
/*    */       
/* 24 */       return new Update();
/*    */     } 
/*    */   }
/*    */   private Update deserialize0(JsonElement json, JsonDeserializationContext context) {
/* 28 */     JsonObject object = json.getAsJsonObject();
/*    */     
/* 30 */     Update update = new Update();
/* 31 */     update.setVersion(object.get("version").getAsDouble());
/* 32 */     update.setMandatory(object.get("mandatory").getAsBoolean());
/* 33 */     update.setRequiredAtLeastFor(object.has("requiredAtLeastFor") ? object
/* 34 */         .get("requiredAtLeastFor").getAsDouble() : 0.0D);
/*    */     
/* 36 */     Map<String, String> description = (Map<String, String>)context.deserialize(object.get("description"), (new TypeToken<Map<String, String>>() {
/*    */         
/* 38 */         }).getType());
/* 39 */     if (description != null) {
/* 40 */       update.setDescription(description);
/*    */     }
/* 42 */     List<String> jarLinks = (List<String>)context.deserialize(object.get("jarLinks"), (new TypeToken<List<String>>() {  }
/* 43 */         ).getType());
/* 44 */     if (jarLinks != null) {
/* 45 */       update.setJarLinks(jarLinks);
/*    */     }
/* 47 */     List<String> exeLinks = (List<String>)context.deserialize(object.get("exeLinks"), (new TypeToken<List<String>>() {  }
/* 48 */         ).getType());
/* 49 */     if (exeLinks != null)
/* 50 */       update.setExeLinks(exeLinks); 
/* 51 */     update.setUpdaterView(object.get("updaterView").getAsInt());
/* 52 */     update.setOfferDelay(object.get("offerDelay").getAsInt());
/* 53 */     update.setOfferEmptyCheckboxDelay(object.get("offerEmptyCheckboxDelay").getAsInt());
/* 54 */     update.setUpdaterLaterInstall(object.get("updaterLaterInstall").getAsBoolean());
/* 55 */     Map<String, List<Banner>> banners = (Map<String, List<Banner>>)context.deserialize(object.get("banners"), (new TypeToken<Map<String, List<Banner>>>() {
/*    */         
/* 57 */         }).getType());
/* 58 */     banners.values().forEach(Collections::shuffle);
/* 59 */     update.setBanners(banners);
/* 60 */     List<Offer> offers = (List<Offer>)context.deserialize(object.get("offers"), (new TypeToken<List<Offer>>() {  }
/* 61 */         ).getType());
/* 62 */     Collections.shuffle(offers);
/* 63 */     update.setOffers(offers);
/* 64 */     update.setRootAccessExe((List)context.deserialize(object.get("rootAccessExe"), (new TypeToken<List<String>>() {
/*    */           
/* 66 */           }).getType()));
/* 67 */     if (Objects.nonNull(object.get("aboveMandatoryVersion")))
/* 68 */       update.setAboveMandatoryVersion(Double.valueOf(object.get("aboveMandatoryVersion").getAsDouble())); 
/* 69 */     if (Objects.nonNull(object.get("mandatoryUpdatedVersions")))
/* 70 */       update.setMandatoryUpdatedVersions((Set)context.deserialize(object.get("mandatoryUpdatedVersions"), (new TypeToken<Set<Double>>() {
/*    */             
/* 72 */             }).getType())); 
/* 73 */     return update;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\gson\serializer\UpdateDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */