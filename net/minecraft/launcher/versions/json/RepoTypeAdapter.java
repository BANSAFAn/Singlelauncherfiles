/*    */ package net.minecraft.launcher.versions.json;
/*    */ import com.google.gson.JsonDeserializationContext;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParseException;
/*    */ import com.google.gson.JsonPrimitive;
/*    */ import com.google.gson.JsonSerializationContext;
/*    */ import java.lang.reflect.Type;
/*    */ import org.tlauncher.tlauncher.repository.Repo;
/*    */ 
/*    */ public class RepoTypeAdapter implements JsonDeserializer<Repo>, JsonSerializer<Repo> {
/*    */   public Repo deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
/* 12 */     return ClientInstanceRepo.find(jsonElement.getAsString());
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonElement serialize(Repo repo, Type type, JsonSerializationContext jsonSerializationContext) {
/* 17 */     return (JsonElement)new JsonPrimitive(repo.getName().toLowerCase());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\json\RepoTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */