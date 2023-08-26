/*    */ package org.tlauncher.tlauncher.minecraft.auth;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.IOException;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class UUIDTypeAdapter
/*    */   extends TypeAdapter<UUID>
/*    */ {
/*    */   public void write(JsonWriter out, UUID value) throws IOException {
/* 13 */     out.value(fromUUID(value));
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID read(JsonReader in) throws IOException {
/* 18 */     return fromString(in.nextString());
/*    */   }
/*    */   
/*    */   public static String toUUID(String value) {
/* 22 */     if (value == null) return null; 
/* 23 */     return value.replace("-", "");
/*    */   }
/*    */   
/*    */   public static String fromUUID(UUID value) {
/* 27 */     return toUUID(value.toString());
/*    */   }
/*    */   
/*    */   public static UUID fromString(String input) {
/* 31 */     return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\UUIDTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */