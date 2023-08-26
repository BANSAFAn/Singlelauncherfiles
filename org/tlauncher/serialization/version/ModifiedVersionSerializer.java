/*    */ package org.tlauncher.serialization.version;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonDeserializer;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Date;
/*    */ import net.minecraft.launcher.versions.ModifiedVersion;
/*    */ import net.minecraft.launcher.versions.json.DateTypeAdapter;
/*    */ import net.minecraft.launcher.versions.json.RepoTypeAdapter;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.tlauncher.repository.Repo;
/*    */ import org.tlauncher.util.gson.serializer.ModpackDTOTypeAdapter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModifiedVersionSerializer
/*    */   implements JsonSerializer<ModifiedVersion>, JsonDeserializer<ModifiedVersion>
/*    */ {
/*    */   private final Gson defaultContext;
/*    */   
/*    */   public ModifiedVersionSerializer() {
/* 30 */     GsonBuilder remoteBuilder = new GsonBuilder();
/*    */     
/* 32 */     remoteBuilder.registerTypeAdapter(Repo.class, new RepoTypeAdapter());
/* 33 */     remoteBuilder.registerTypeAdapter(ModpackDTO.class, new ModpackDTOTypeAdapter());
/* 34 */     remoteBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/* 35 */     remoteBuilder.enableComplexMapKeySerialization();
/* 36 */     remoteBuilder.disableHtmlEscaping();
/* 37 */     remoteBuilder.setPrettyPrinting();
/*    */     
/* 39 */     this.defaultContext = remoteBuilder.create();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ModifiedVersion deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
/* 45 */     JsonObject object = jsonElement.getAsJsonObject();
/*    */     
/* 47 */     return (ModifiedVersion)this.defaultContext.fromJson((JsonElement)object, ModifiedVersion.class);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonElement serialize(ModifiedVersion modifiedVersion, Type type, JsonSerializationContext jsonSerializationContext) {
/* 53 */     JsonObject jsonElement = (JsonObject)this.defaultContext.toJsonTree(modifiedVersion, type);
/* 54 */     JsonElement jar = jsonElement.get("jar");
/* 55 */     if (jar == null)
/* 56 */       jsonElement.remove("downloadJarLibraries"); 
/* 57 */     if (jsonElement.has("userConfigSkinVersion"))
/* 58 */       jsonElement.remove("userConfigSkinVersion"); 
/* 59 */     return (JsonElement)jsonElement;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\serialization\version\ModifiedVersionSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */