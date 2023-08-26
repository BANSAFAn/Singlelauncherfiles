/*    */ package net.minecraft.launcher.versions.json;
/*    */ 
/*    */ import com.google.gson.TypeAdapter;
/*    */ import com.google.gson.stream.JsonReader;
/*    */ import com.google.gson.stream.JsonWriter;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class FileTypeAdapter
/*    */   extends TypeAdapter<File>
/*    */ {
/*    */   public void write(JsonWriter out, File value) throws IOException {
/* 13 */     if (value == null) {
/* 14 */       out.nullValue();
/*    */     } else {
/* 16 */       out.value(value.getAbsolutePath());
/*    */     } 
/*    */   }
/*    */   
/*    */   public File read(JsonReader in) throws IOException {
/* 21 */     if (in.hasNext()) {
/* 22 */       String name = in.nextString();
/* 23 */       return (name != null) ? new File(name) : null;
/*    */     } 
/* 25 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\json\FileTypeAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */