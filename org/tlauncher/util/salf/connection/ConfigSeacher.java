/*    */ package org.tlauncher.util.salf.connection;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonSyntaxException;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ import org.tlauncher.util.MinecraftUtil;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConfigSeacher
/*    */ {
/*    */   private String[] urlServer;
/*    */   private String nameFile;
/*    */   
/*    */   public ConfigSeacher(String[] urlServer, String nameFile) {
/* 31 */     this.urlServer = urlServer;
/* 32 */     this.nameFile = nameFile;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private ServerEntity readConfigFromFile() throws IOException {
/* 44 */     Path p = Paths.get(MinecraftUtil.getWorkingDirectory().getCanonicalPath(), new String[] { this.nameFile });
/*    */     
/* 46 */     if (!Files.isReadable(p)) {
/* 47 */       log("file doesn't readable or exist is " + p.toString());
/* 48 */       return null;
/*    */     } 
/* 50 */     String dataJson = FileUtil.readFile(new File(MinecraftUtil.getWorkingDirectory(), this.nameFile));
/* 51 */     return (ServerEntity)(new Gson()).fromJson(dataJson, ServerEntity.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public ServerEntity saveConfigFromServer() throws IOException {
/* 56 */     IOException io = null;
/* 57 */     for (String s : this.urlServer) {
/*    */       try {
/* 59 */         String serverText = IOUtils.toString(new URL(s), "utf-8");
/* 60 */         IOUtils.write(serverText, new FileOutputStream(new File(
/* 61 */                 MinecraftUtil.getWorkingDirectory().getCanonicalPath(), this.nameFile)), "utf-8");
/*    */         
/* 63 */         return (ServerEntity)(new Gson()).fromJson(serverText, ServerEntity.class);
/* 64 */       } catch (IOException e) {
/* 65 */         io = e;
/*    */       } 
/*    */     } 
/* 68 */     throw io;
/*    */   }
/*    */   
/*    */   private ServerEntity readOldServer() throws IOException {
/* 72 */     ServerEntity server = null;
/*    */     try {
/* 74 */       server = readConfigFromFile();
/* 75 */     } catch (JsonSyntaxException ex) {
/* 76 */       log(ex.getMessage());
/*    */     } 
/* 78 */     if (server == null) {
/* 79 */       server = saveConfigFromServer();
/*    */     }
/* 81 */     if (server == null) {
/* 82 */       throw new NullPointerException("didn't receive data from filenameFile=" + this.nameFile + " urlServer=" + this.urlServer);
/*    */     }
/* 84 */     return server;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ServerEntity readServer() throws IOException {
/* 93 */     return readOldServer();
/*    */   }
/*    */   
/*    */   private void log(String line) {
/* 97 */     U.log(new Object[] { "[ConfigSeacher] ", line });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\salf\connection\ConfigSeacher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */