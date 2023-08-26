/*     */ package by.gdev.http.download.impl;
/*     */ 
/*     */ import by.gdev.http.download.model.Headers;
/*     */ import by.gdev.http.download.model.RequestMetadata;
/*     */ import by.gdev.http.download.service.FileCacheService;
/*     */ import by.gdev.http.download.service.HttpService;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.model.download.Metadata;
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import com.google.gson.Gson;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class FileCacheServiceImpl
/*     */   implements FileCacheService {
/*  25 */   private static final Logger log = LoggerFactory.getLogger(FileCacheServiceImpl.class); public FileCacheServiceImpl(HttpService httpService, Gson gson, Charset charset, Path directory, int timeToLife) {
/*  26 */     this.httpService = httpService; this.gson = gson; this.charset = charset; this.directory = directory; this.timeToLife = timeToLife;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpService httpService;
/*     */ 
/*     */   
/*     */   private Gson gson;
/*     */ 
/*     */   
/*     */   private Charset charset;
/*     */ 
/*     */   
/*     */   private Path directory;
/*     */   
/*     */   private int timeToLife;
/*     */ 
/*     */   
/*     */   public Path getRawObject(String url, boolean cache) throws IOException, NoSuchAlgorithmException {
/*  46 */     Path urlPath = Paths.get(this.directory.toString(), new String[] { url.replaceAll("://", "_").replaceAll("[:?=]", "_") });
/*  47 */     Path metaFile = Paths.get(String.valueOf(urlPath).concat(".metadata"), new String[0]);
/*  48 */     if (cache == true) {
/*  49 */       return getResourceWithoutHttpHead(url, metaFile, urlPath);
/*     */     }
/*  51 */     return getResourceWithHttpHead(url, urlPath, metaFile);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getRawObject(List<String> urls, Metadata metadata, boolean cache) throws IOException, NoSuchAlgorithmException {
/*  57 */     Iterator<String> iterator = urls.iterator(); if (iterator.hasNext()) { String url = iterator.next();
/*  58 */       return getRawObject(url + metadata.getRelativeUrl(), cache); }
/*     */     
/*  60 */     throw new NullPointerException("metadata is empty");
/*     */   }
/*     */ 
/*     */   
/*     */   private Path getResourceWithoutHttpHead(String url, Path metaFile, Path urlPath) throws IOException, NoSuchAlgorithmException {
/*  65 */     long purgeTime = System.currentTimeMillis() - (this.timeToLife * 1000);
/*  66 */     if (urlPath.toFile().lastModified() < purgeTime)
/*  67 */       Files.deleteIfExists(urlPath); 
/*  68 */     if (urlPath.toFile().exists() && Files.exists(metaFile, new java.nio.file.LinkOption[0])) {
/*  69 */       RequestMetadata localMetadata = (RequestMetadata)(new FileMapperService(this.gson, this.charset, "")).read(metaFile.toString(), RequestMetadata.class);
/*     */       
/*  71 */       String sha = DesktopUtil.getChecksum(urlPath.toFile(), Headers.SHA1.getValue());
/*  72 */       if (sha.equals(localMetadata.getSha1())) {
/*  73 */         log.trace("HTTP HEAD -> " + url);
/*  74 */         return urlPath;
/*     */       } 
/*  76 */       log.trace("not proper hashsum HTTP GET -> " + url);
/*  77 */       RequestMetadata serverMetadata = this.httpService.getRequestByUrlAndSave(url, urlPath);
/*  78 */       createSha1(serverMetadata, urlPath, metaFile);
/*  79 */       return urlPath;
/*     */     } 
/*     */     
/*  82 */     log.trace("HTTP GET -> " + url);
/*  83 */     this.httpService.getRequestByUrlAndSave(url, urlPath);
/*  84 */     checkMetadataFile(metaFile, url);
/*  85 */     return urlPath;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Path getResourceWithHttpHead(String url, Path urlPath, Path metaFile) throws IOException, NoSuchAlgorithmException {
/*  91 */     boolean fileExists = urlPath.toFile().exists();
/*  92 */     checkMetadataFile(metaFile, url);
/*  93 */     if (fileExists) {
/*  94 */       RequestMetadata requestMetadata1 = this.httpService.getMetaByUrl(url);
/*  95 */       RequestMetadata localMetadata = (RequestMetadata)(new FileMapperService(this.gson, this.charset, "")).read(metaFile.toString(), RequestMetadata.class);
/*     */       
/*  97 */       if ((StringUtils.equals(requestMetadata1.getETag(), localMetadata.getETag()) & 
/*  98 */         StringUtils.equals(requestMetadata1.getContentLength(), localMetadata.getContentLength()) & 
/*  99 */         StringUtils.equals(requestMetadata1.getLastModified(), localMetadata.getLastModified())) != 0) {
/* 100 */         return urlPath;
/*     */       }
/* 102 */       this.httpService.getRequestByUrlAndSave(url, urlPath);
/* 103 */       (new FileMapperService(this.gson, this.charset, "")).write(requestMetadata1, metaFile.toString());
/* 104 */       return urlPath;
/*     */     } 
/*     */     
/* 107 */     RequestMetadata serverMetadata = this.httpService.getRequestByUrlAndSave(url, urlPath);
/* 108 */     createSha1(serverMetadata, urlPath, metaFile);
/* 109 */     return urlPath;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void createSha1(RequestMetadata metadata, Path urlPath, Path metaFile) throws IOException, NoSuchAlgorithmException {
/* 115 */     metadata.setSha1(DesktopUtil.getChecksum(urlPath.toFile(), "SHA-1"));
/* 116 */     (new FileMapperService(this.gson, this.charset, "")).write(metadata, metaFile.toString());
/*     */   }
/*     */   
/*     */   private void checkMetadataFile(Path metaFile, String url) throws IOException {
/* 120 */     if (!metaFile.toFile().exists()) {
/* 121 */       RequestMetadata metadata = this.httpService.getMetaByUrl(url);
/* 122 */       (new FileMapperService(this.gson, this.charset, "")).write(metadata, metaFile.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\impl\FileCacheServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */