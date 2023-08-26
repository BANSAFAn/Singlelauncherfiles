/*    */ package org.tlauncher.util.gson.serializer;
/*    */ import com.google.gson.Gson;
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
/*    */ import org.tlauncher.modpack.domain.client.MapDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ public class MapDTOTypeAdapter implements JsonSerializer<MapDTO>, JsonDeserializer<MapDTO> {
/*    */   public MapDTOTypeAdapter() {
/* 21 */     GsonBuilder builder = new GsonBuilder();
/* 22 */     builder.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/* 23 */     builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/* 24 */     builder.registerTypeAdapter(MetadataDTO.class, new MetadataDTOAdapter());
/* 25 */     builder.enableComplexMapKeySerialization();
/* 26 */     this.gson = builder.create();
/*    */   }
/*    */   private Gson gson;
/*    */   
/*    */   public MapDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
/* 31 */     MapDTO dto = (MapDTO)this.gson.fromJson(jsonElement, MapDTO.class);
/* 32 */     JsonObject object = jsonElement.getAsJsonObject();
/* 33 */     VersionDTO version = (VersionDTO)this.gson.fromJson(object.get("version"), VersionDTO.class);
/* 34 */     dto.setVersions((List)this.gson.fromJson(object.get("versions"), (new TypeToken<List<VersionDTO>>() {
/*    */           
/* 36 */           }).getType()));
/* 37 */     dto.setVersion(version);
/* 38 */     ElementCollectionsPool.fill((GameEntityDTO)dto);
/* 39 */     return dto;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement serialize(MapDTO modpackDTO, Type type, JsonSerializationContext jsonSerializationContext) {
/* 44 */     return this.gson.toJsonTree(modpackDTO);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\gson\serializer\MapDTOTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */