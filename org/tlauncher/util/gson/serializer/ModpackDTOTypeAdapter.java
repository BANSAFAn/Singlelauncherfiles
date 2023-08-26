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
/*    */ import org.tlauncher.modpack.domain.client.MapDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModDTO;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.ResourcePackDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ public class ModpackDTOTypeAdapter implements JsonSerializer<ModpackDTO>, JsonDeserializer<ModpackDTO> {
/*    */   public ModpackDTOTypeAdapter() {
/* 23 */     GsonBuilder builder = new GsonBuilder();
/* 24 */     builder.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/* 25 */     builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/* 26 */     builder.registerTypeAdapter(ModDTO.class, new ModDTOTypeAdapter());
/* 27 */     builder.registerTypeAdapter(MapDTO.class, new MapDTOTypeAdapter());
/* 28 */     builder.registerTypeAdapter(ResourcePackDTO.class, new ResourcePackDTOTypeAdapter());
/* 29 */     builder.enableComplexMapKeySerialization();
/* 30 */     this.gson = builder.create();
/*    */   }
/*    */   private Gson gson;
/*    */   
/*    */   public ModpackDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
/* 35 */     ModpackDTO dto = (ModpackDTO)this.gson.fromJson(jsonElement, ModpackDTO.class);
/* 36 */     JsonObject object = jsonElement.getAsJsonObject();
/* 37 */     dto.setVersion((VersionDTO)this.gson.fromJson(object.get("version"), ModpackVersionDTO.class));
/* 38 */     dto.setVersions((List)this.gson.fromJson(object.get("versions"), (new TypeToken<List<ModpackVersionDTO>>() {  }
/* 39 */           ).getType()));
/* 40 */     ElementCollectionsPool.fill((GameEntityDTO)dto);
/* 41 */     return dto;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement serialize(ModpackDTO modpackDTO, Type type, JsonSerializationContext jsonSerializationContext) {
/* 46 */     return this.gson.toJsonTree(modpackDTO);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\gson\serializer\ModpackDTOTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */