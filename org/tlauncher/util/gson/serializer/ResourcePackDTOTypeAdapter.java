/*    */ package org.tlauncher.util.gson.serializer;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.TypeAdapterFactory;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import net.minecraft.launcher.versions.json.DateTypeAdapter;
/*    */ import net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory;
/*    */ import org.tlauncher.modpack.domain.client.ResourcePackDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ public class ResourcePackDTOTypeAdapter implements JsonSerializer<ResourcePackDTO>, JsonDeserializer<ResourcePackDTO> {
/*    */   public ResourcePackDTOTypeAdapter() {
/* 20 */     GsonBuilder builder = new GsonBuilder();
/* 21 */     builder.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/* 22 */     builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/* 23 */     builder.enableComplexMapKeySerialization();
/* 24 */     this.gson = builder.create();
/*    */   }
/*    */   private Gson gson;
/*    */   
/*    */   public ResourcePackDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
/* 29 */     ResourcePackDTO dto = (ResourcePackDTO)this.gson.fromJson(jsonElement, ResourcePackDTO.class);
/* 30 */     JsonObject object = jsonElement.getAsJsonObject();
/* 31 */     dto.setVersion((VersionDTO)this.gson.fromJson(object.get("version"), VersionDTO.class));
/* 32 */     dto.setVersions((List)this.gson.fromJson(object.get("versions"), (new TypeToken<List<VersionDTO>>() {
/*    */           
/* 34 */           }).getType()));
/* 35 */     ElementCollectionsPool.fill((GameEntityDTO)dto);
/* 36 */     return dto;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement serialize(ResourcePackDTO dto, Type type, JsonSerializationContext jsonSerializationContext) {
/* 41 */     return this.gson.toJsonTree(dto);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\gson\serializer\ResourcePackDTOTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */