/*    */ package org.tlauncher.util.gson.serializer;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import com.google.gson.JsonSerializer;
/*    */ import java.lang.reflect.Type;
/*    */ import org.tlauncher.modpack.domain.client.version.MapMetadataDTO;
/*    */ 
/*    */ public class MetadataDTOAdapter implements JsonSerializer<MapMetadataDTO>, JsonDeserializer<MapMetadataDTO> {
/*    */   public MapMetadataDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
/* 12 */     MapMetadataDTO meta = (MapMetadataDTO)context.deserialize(json, MapMetadataDTO.class);
/* 13 */     return meta;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement serialize(MapMetadataDTO src, Type typeOfSrc, JsonSerializationContext context) {
/* 18 */     return context.serialize(src);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\gson\serializer\MetadataDTOAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */