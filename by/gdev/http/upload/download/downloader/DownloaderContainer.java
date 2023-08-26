/*    */ package by.gdev.http.upload.download.downloader;
/*    */ 
/*    */ import by.gdev.http.download.handler.PostHandler;
/*    */ import by.gdev.http.download.model.Headers;
/*    */ import by.gdev.util.DesktopUtil;
/*    */ import by.gdev.util.model.download.Metadata;
/*    */ import by.gdev.util.model.download.Repo;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.LinkOption;
/*    */ import java.nio.file.Paths;
/*    */ import java.nio.file.attribute.BasicFileAttributes;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DownloaderContainer
/*    */ {
/*    */   public void setDestinationRepositories(String destinationRepositories) {
/* 30 */     this.destinationRepositories = destinationRepositories; } public void setContainerSize(long containerSize) { this.containerSize = containerSize; } public void setRepo(Repo repo) { this.repo = repo; } public void setHandlers(List<PostHandler> handlers) { this.handlers = handlers; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloaderContainer)) return false;  DownloaderContainer other = (DownloaderContainer)o; if (!other.canEqual(this)) return false;  Object this$destinationRepositories = getDestinationRepositories(), other$destinationRepositories = other.getDestinationRepositories(); if ((this$destinationRepositories == null) ? (other$destinationRepositories != null) : !this$destinationRepositories.equals(other$destinationRepositories)) return false;  if (getContainerSize() != other.getContainerSize()) return false;  Object this$repo = getRepo(), other$repo = other.getRepo(); if ((this$repo == null) ? (other$repo != null) : !this$repo.equals(other$repo)) return false;  Object<PostHandler> this$handlers = (Object<PostHandler>)getHandlers(), other$handlers = (Object<PostHandler>)other.getHandlers(); return !((this$handlers == null) ? (other$handlers != null) : !this$handlers.equals(other$handlers)); } protected boolean canEqual(Object other) { return other instanceof DownloaderContainer; } public int hashCode() { int PRIME = 59; result = 1; Object $destinationRepositories = getDestinationRepositories(); result = result * 59 + (($destinationRepositories == null) ? 43 : $destinationRepositories.hashCode()); long $containerSize = getContainerSize(); result = result * 59 + (int)($containerSize >>> 32L ^ $containerSize); Object $repo = getRepo(); result = result * 59 + (($repo == null) ? 43 : $repo.hashCode()); Object<PostHandler> $handlers = (Object<PostHandler>)getHandlers(); return result * 59 + (($handlers == null) ? 43 : $handlers.hashCode()); } public String toString() { return "DownloaderContainer(destinationRepositories=" + getDestinationRepositories() + ", containerSize=" + getContainerSize() + ", repo=" + getRepo() + ", handlers=" + getHandlers() + ")"; }
/* 31 */    private String destinationRepositories; private long containerSize; private static final Logger log = LoggerFactory.getLogger(DownloaderContainer.class); private Repo repo; private List<PostHandler> handlers;
/*    */   public String getDestinationRepositories() {
/* 33 */     return this.destinationRepositories; }
/* 34 */   public long getContainerSize() { return this.containerSize; }
/* 35 */   public Repo getRepo() { return this.repo; } public List<PostHandler> getHandlers() {
/* 36 */     return this.handlers;
/*    */   }
/*    */   
/*    */   public void filterNotExistResoursesAndSetRepo(Repo repo, String workDirectory) throws NoSuchAlgorithmException, IOException {
/* 40 */     this.repo = new Repo();
/* 41 */     List<Metadata> listRes = new ArrayList<>();
/* 42 */     for (Metadata meta : repo.getResources()) {
/* 43 */       File localFile = Paths.get(workDirectory, new String[] { meta.getPath() }).toAbsolutePath().toFile();
/* 44 */       if (localFile.exists()) {
/* 45 */         String shaLocalFile = DesktopUtil.getChecksum(localFile, Headers.SHA1.getValue());
/* 46 */         BasicFileAttributes attr = Files.readAttributes(localFile.toPath(), BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/*    */         
/* 48 */         if (((!attr.isSymbolicLink() ? 1 : 0) & (!shaLocalFile.equals(meta.getSha1()) ? 1 : 0)) != 0) {
/* 49 */           listRes.add(meta);
/* 50 */           log.warn("The hash sum of the file is not equal. File " + localFile + " will be deleted. Size = " + (localFile
/* 51 */               .length() / 1024L / 1024L));
/* 52 */           Files.delete(localFile.toPath());
/*    */         }  continue;
/*    */       } 
/* 55 */       listRes.add(meta);
/*    */     } 
/*    */     
/* 58 */     this.repo.setResources(listRes);
/* 59 */     this.repo.setRepositories(repo.getRepositories());
/*    */   }
/*    */   
/*    */   public void conteinerAllSize(Repo repo) {
/* 63 */     List<Long> sizeList = new ArrayList<>();
/* 64 */     repo.getResources().forEach(size -> sizeList.add(Long.valueOf(size.getSize())));
/*    */ 
/*    */     
/* 67 */     long sum = 0L;
/* 68 */     for (Iterator<Long> iterator = sizeList.iterator(); iterator.hasNext(); ) { long l = ((Long)iterator.next()).longValue();
/* 69 */       sum += l; }
/*    */     
/* 71 */     this.containerSize = sum;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\htt\\upload\download\downloader\DownloaderContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */