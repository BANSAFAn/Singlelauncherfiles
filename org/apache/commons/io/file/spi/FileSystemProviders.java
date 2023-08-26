/*     */ package org.apache.commons.io.file.spi;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.FileSystems;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.spi.FileSystemProvider;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileSystemProviders
/*     */ {
/*  35 */   private static final FileSystemProviders INSTALLED = new FileSystemProviders(FileSystemProvider.installedProviders());
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<FileSystemProvider> providers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileSystemProvider getFileSystemProvider(Path path) {
/*  45 */     return ((Path)Objects.<Path>requireNonNull(path, "path")).getFileSystem().provider();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileSystemProviders installed() {
/*  55 */     return INSTALLED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private FileSystemProviders(List<FileSystemProvider> providers) {
/*  64 */     this.providers = providers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystemProvider getFileSystemProvider(String scheme) {
/*  75 */     Objects.requireNonNull(scheme, "scheme");
/*     */     
/*  77 */     if (scheme.equalsIgnoreCase("file")) {
/*  78 */       return FileSystems.getDefault().provider();
/*     */     }
/*     */     
/*  81 */     if (this.providers != null) {
/*  82 */       for (FileSystemProvider provider : this.providers) {
/*  83 */         if (provider.getScheme().equalsIgnoreCase(scheme)) {
/*  84 */           return provider;
/*     */         }
/*     */       } 
/*     */     }
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystemProvider getFileSystemProvider(URI uri) {
/*  98 */     return getFileSystemProvider(((URI)Objects.<URI>requireNonNull(uri, "uri")).getScheme());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSystemProvider getFileSystemProvider(URL url) {
/* 108 */     return getFileSystemProvider(((URL)Objects.<URL>requireNonNull(url, "url")).getProtocol());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\file\spi\FileSystemProviders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */