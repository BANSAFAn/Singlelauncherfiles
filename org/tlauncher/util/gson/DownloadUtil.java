/*    */ package org.tlauncher.util.gson;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.GsonBuilder;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.net.URL;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.launcher.Http;
/*    */ import net.minecraft.launcher.versions.json.PatternTypeAdapter;
/*    */ import org.tlauncher.tlauncher.repository.Repo;
/*    */ import org.tlauncher.tlauncher.rmo.Bootstrapper;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ public class DownloadUtil
/*    */ {
/*    */   private static final Gson gson;
/*    */   private static final int CONNECT_TIMEOUT = 5000;
/*    */   private static final int READ_TIMEOUT = 15000;
/*    */   
/*    */   static {
/* 26 */     GsonBuilder builder = new GsonBuilder();
/* 27 */     builder.registerTypeAdapter(Pattern.class, new PatternTypeAdapter());
/* 28 */     builder.setPrettyPrinting();
/* 29 */     gson = builder.create();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> T loadObjectByKey(String key, Class<T> cl) throws IOException {
/* 38 */     String result = readTextByKey(key, "", true);
/* 39 */     return (T)gson.fromJson(result, cl);
/*    */   }
/*    */   
/*    */   public static <T> T loadByRepository(Repo repo, Class<T> cl) throws IOException {
/* 43 */     return (T)gson.fromJson(repo.getUrl(), cl);
/*    */   }
/*    */   
/*    */   public static <T> T loadByRepository(Repo repository, Type type) throws IOException {
/* 47 */     return (T)gson.fromJson(repository.getUrl(), type);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> T loadObjectByKey(String key, Type type, boolean flag) throws IOException {
/* 58 */     return (T)gson.fromJson(readTextByKey(key, "", flag), type);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String readTextByKey(String key, String postfix, boolean innerConfig) throws IOException {
/*    */     List<String> urls;
/* 68 */     if (innerConfig) {
/* 69 */       urls = Arrays.asList(TLauncher.getInnerSettings().get(key).split(","));
/*    */     } else {
/* 71 */       urls = Arrays.asList(Bootstrapper.innerConfig.get(key).split(","));
/*    */     } 
/* 73 */     IOException error = null;
/* 74 */     for (String url : urls) {
/* 75 */       log("request to " + url);
/*    */       try {
/* 77 */         return Http.performGet(new URL(url + postfix), 5000, 15000);
/* 78 */       } catch (IOException ex) {
/* 79 */         error = ex;
/*    */       } 
/*    */     } 
/* 82 */     throw new IOException("couldn't download ", error);
/*    */   }
/*    */   
/*    */   private static void log(String line) {
/* 86 */     U.log(new Object[] { "[GsonUtil] ", line });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\gson\DownloadUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */