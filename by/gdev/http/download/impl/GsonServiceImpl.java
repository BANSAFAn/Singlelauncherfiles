/*    */ package by.gdev.http.download.impl;
/*    */ 
/*    */ import by.gdev.http.download.service.FileCacheService;
/*    */ import by.gdev.http.download.service.GsonService;
/*    */ import by.gdev.http.download.service.HttpService;
/*    */ import com.google.gson.Gson;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.lang.reflect.Type;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Path;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GsonServiceImpl
/*    */   implements GsonService
/*    */ {
/* 25 */   private static final Logger log = LoggerFactory.getLogger(GsonServiceImpl.class); public GsonServiceImpl(Gson gson, FileCacheService fileService, HttpService httpService) {
/* 26 */     this.gson = gson; this.fileService = fileService; this.httpService = httpService;
/*    */   }
/*    */ 
/*    */   
/*    */   private Gson gson;
/*    */   
/*    */   private FileCacheService fileService;
/*    */   
/*    */   private HttpService httpService;
/*    */ 
/*    */   
/*    */   public <T> T getObject(String url, Class<T> class1, boolean cache) throws IOException, NoSuchAlgorithmException {
/* 38 */     Path pathFile = this.fileService.getRawObject(url, cache);
/* 39 */     try (InputStreamReader read = new InputStreamReader(new FileInputStream(pathFile.toFile()), StandardCharsets.UTF_8)) {
/*    */       
/* 41 */       return (T)this.gson.fromJson(read, class1);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> T getObjectByUrls(List<String> urls, String urn, Class<T> class1, boolean cache) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
/* 51 */     T returnValue = null;
/* 52 */     for (String url : urls) {
/*    */       try {
/* 54 */         Path pathFile = this.fileService.getRawObject(url + urn, cache);
/* 55 */         try (InputStreamReader read = new InputStreamReader(new FileInputStream(pathFile.toFile()), StandardCharsets.UTF_8)) {
/*    */           
/* 57 */           returnValue = (T)this.gson.fromJson(read, class1);
/*    */         } 
/* 59 */       } catch (IOException e) {
/* 60 */         log.error("Error = " + e.getMessage());
/*    */       } 
/*    */     } 
/* 63 */     return returnValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T getObjectWithoutSaving(String url, Class<T> class1) throws IOException {
/* 68 */     return getObjectWithoutSaving(url, class1, (Map<String, String>)null);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T getObjectWithoutSaving(String url, Type type) throws IOException {
/* 73 */     return getObjectWithoutSaving(url, type, (Map<String, String>)null);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T getObjectWithoutSaving(String url, Class<T> class1, Map<String, String> headers) throws IOException {
/* 78 */     return (T)this.gson.fromJson(this.httpService.getRequestByUrl(url, headers), class1);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T getObjectWithoutSaving(String url, Type type, Map<String, String> headers) throws IOException {
/* 83 */     return (T)this.gson.fromJson(this.httpService.getRequestByUrl(url, headers), type);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\impl\GsonServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */