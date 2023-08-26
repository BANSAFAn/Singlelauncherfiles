/*     */ package org.tlauncher.tlauncher.downloader;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*     */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*     */ import org.tlauncher.tlauncher.repository.Repo;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.U;
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
/*     */ public class Downloadable
/*     */ {
/*     */   private static final boolean DEFAULT_FORCE = false;
/*     */   private static final boolean DEFAULT_FAST = false;
/*     */   
/*     */   public Downloadable(Repo repo, MetadataDTO metadataDTO, boolean forceDownload, boolean fastDownload) {
/*  38 */     this();
/*  39 */     String unb = TLauncher.getInnerSettings().getArray("file.server")[1];
/*  40 */     if (metadataDTO.getUrl().startsWith(unb)) {
/*  41 */       this.metadataDTO = new MetadataDTO();
/*  42 */       this.metadataDTO.setSha1(metadataDTO.getSha1());
/*  43 */       this.metadataDTO.setSize(metadataDTO.getSize());
/*  44 */       this.metadataDTO.setLocalDestination(metadataDTO.getLocalDestination());
/*  45 */       this.metadataDTO.setPath(metadataDTO.getPath());
/*  46 */       this.metadataDTO.setUrl(metadataDTO.getUrl().replace(unb, ""));
/*  47 */       setRepo(ClientInstanceRepo.createModpackRepo());
/*     */     } else {
/*  49 */       this.metadataDTO = metadataDTO;
/*  50 */       setRepo(repo);
/*     */     } 
/*  52 */     this.forceDownload = forceDownload;
/*  53 */     this.fastDownload = fastDownload;
/*     */   }
/*     */ 
/*     */   
/*     */   public Downloadable(Repo repo, MetadataDTO metadataDTO, boolean forceDownload) {
/*  58 */     this(repo, metadataDTO, forceDownload, false);
/*     */   }
/*     */   
/*     */   public Downloadable(Repo repo, MetadataDTO metadataDTO) {
/*  62 */     this(repo, metadataDTO, false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  67 */   private final List<File> additionalDestinations = Collections.synchronizedList(new ArrayList<>());
/*     */   
/*  69 */   private final List<DownloadableHandler> handlers = Collections.synchronizedList(new ArrayList<>()); private Repo repo; private MetadataDTO metadataDTO; private boolean forceDownload;
/*     */   private boolean fastDownload;
/*     */   
/*     */   public void setRepo(Repo repo) {
/*  73 */     if (repo == null)
/*  74 */       throw new NullPointerException("Repository is NULL!"); 
/*  75 */     checkLocked();
/*  76 */     this.repo = repo;
/*     */   } private boolean locked; private DownloadableContainer container; private long alreadyDownloaded; private Throwable error;
/*     */   private Downloadable() {}
/*     */   long getAlreadyDownloaded() {
/*  80 */     return this.alreadyDownloaded;
/*     */   }
/*     */   
/*     */   void setAlreadyDownloaded(long alreadyDownloaded) {
/*  84 */     this.alreadyDownloaded = alreadyDownloaded;
/*     */   }
/*     */   
/*     */   public boolean isForce() {
/*  88 */     return this.forceDownload;
/*     */   }
/*     */   
/*     */   public void setForce(boolean force) {
/*  92 */     checkLocked();
/*  93 */     this.forceDownload = force;
/*     */   }
/*     */   
/*     */   public boolean isFast() {
/*  97 */     return this.fastDownload;
/*     */   }
/*     */   
/*     */   public void setFast(boolean fast) {
/* 101 */     checkLocked();
/* 102 */     this.fastDownload = fast;
/*     */   }
/*     */   
/*     */   public Repo getRepository() {
/* 106 */     return this.repo;
/*     */   }
/*     */   
/*     */   boolean hasRepository() {
/* 110 */     return (this.repo != null);
/*     */   }
/*     */   
/*     */   public String getFilename() {
/* 114 */     return FileUtil.getFilename(this.metadataDTO.getPath());
/*     */   }
/*     */   
/*     */   List<File> getAdditionalDestinations() {
/* 118 */     return Collections.unmodifiableList(this.additionalDestinations);
/*     */   }
/*     */   
/*     */   public void addAdditionalDestination(File file) {
/* 122 */     if (file == null) {
/* 123 */       throw new NullPointerException();
/*     */     }
/* 125 */     checkLocked();
/* 126 */     this.additionalDestinations.add(file);
/*     */   }
/*     */   
/*     */   public void addHandler(DownloadableHandler handler) {
/* 130 */     if (handler == null) {
/* 131 */       throw new NullPointerException();
/*     */     }
/* 133 */     checkLocked();
/* 134 */     this.handlers.add(handler);
/*     */   }
/*     */   
/*     */   protected void setContainer(DownloadableContainer container) {
/* 138 */     checkLocked();
/* 139 */     this.container = container;
/*     */   }
/*     */   
/*     */   public Throwable getError() {
/* 143 */     return this.error;
/*     */   }
/*     */   
/*     */   private void setLocked(boolean locked) {
/* 147 */     this.locked = locked;
/*     */   }
/*     */   
/*     */   private void checkLocked() {
/* 151 */     if (this.locked)
/* 152 */       throw new IllegalStateException("Downloadable is locked!"); 
/*     */   }
/*     */   
/*     */   void onStart() {
/* 156 */     setLocked(true);
/*     */     
/* 158 */     for (DownloadableHandler handler : this.handlers)
/* 159 */       handler.onStart(this); 
/*     */   }
/*     */   
/*     */   void onAbort(AbortedDownloadException ae) {
/* 163 */     setLocked(false);
/*     */     
/* 165 */     this.error = ae;
/*     */     
/* 167 */     for (DownloadableHandler handler : this.handlers) {
/* 168 */       handler.onAbort(this);
/*     */     }
/* 170 */     if (this.container != null)
/* 171 */       this.container.onAbort(this); 
/*     */   }
/*     */   
/*     */   protected void onComplete() throws RetryDownloadException {
/* 175 */     setLocked(false);
/*     */     
/* 177 */     for (DownloadableHandler handler : this.handlers) {
/* 178 */       handler.onComplete(this);
/*     */     }
/* 180 */     if (this.container != null)
/* 181 */       this.container.onComplete(this); 
/*     */   }
/*     */   
/*     */   void onError(Throwable e) {
/* 185 */     this.error = e;
/*     */     
/* 187 */     if (e == null) {
/*     */       return;
/*     */     }
/* 190 */     setLocked(false);
/*     */     
/* 192 */     for (DownloadableHandler handler : this.handlers) {
/* 193 */       handler.onError(this, e);
/*     */     }
/* 195 */     if (this.container != null) {
/* 196 */       this.container.onError(this, e);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 201 */     return getClass().getSimpleName() + "{path='" + this.metadataDTO.getPath() + "'; repo=" + this.repo + "; destinations=" + this.metadataDTO
/* 202 */       .getLocalDestination() + "," + this.additionalDestinations + "; force=" + this.forceDownload + "; fast=" + this.fastDownload + "; locked=" + this.locked + "; container=" + this.container + "; handlers=" + this.handlers + "; error=" + this.error + ";}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpURLConnection setUp(URLConnection connection0, int timeout, boolean fake) {
/* 211 */     if (connection0 == null) {
/* 212 */       throw new NullPointerException();
/*     */     }
/* 214 */     if (!(connection0 instanceof HttpURLConnection)) {
/* 215 */       throw new IllegalArgumentException("Unknown connection protocol: " + connection0);
/*     */     }
/*     */     
/* 218 */     HttpURLConnection connection = (HttpURLConnection)connection0;
/*     */     
/* 220 */     connection.setConnectTimeout(timeout);
/* 221 */     connection.setReadTimeout(timeout);
/* 222 */     connection.setInstanceFollowRedirects(true);
/*     */     
/* 224 */     connection.setUseCaches(false);
/* 225 */     connection.setDefaultUseCaches(false);
/* 226 */     connection.setRequestProperty("Cache-Control", "no-store,max-age=0,no-cache");
/*     */     
/* 228 */     connection.setRequestProperty("Expires", "0");
/* 229 */     connection.setRequestProperty("Pragma", "no-cache");
/* 230 */     if (!fake) {
/* 231 */       return connection;
/*     */     }
/*     */ 
/*     */     
/* 235 */     switch (OS.CURRENT)
/*     */     { case OSX:
/* 237 */         userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8) AppleWebKit/535.18.5 (KHTML, like Gecko) Version/5.2 Safari/535.18.5";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 247 */         connection.setRequestProperty("User-Agent", userAgent);
/*     */         
/* 249 */         return connection;case WINDOWS: userAgent = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0; .NET4.0C)"; connection.setRequestProperty("User-Agent", userAgent); return connection; }  String userAgent = "Mozilla/5.0 (Linux; Linux x86_64; rv:29.0) Gecko/20100101 Firefox/29.0"; connection.setRequestProperty("User-Agent", userAgent); return connection;
/*     */   }
/*     */ 
/*     */   
/*     */   public static HttpURLConnection setUp(URLConnection connection, boolean fake) {
/* 254 */     return setUp(connection, U.getConnectionTimeout(), fake);
/*     */   }
/*     */   
/*     */   public static String getEtag(String etag) {
/* 258 */     if (etag == null) {
/* 259 */       return "-";
/*     */     }
/* 261 */     if (etag.startsWith("\"") && etag.endsWith("\"")) {
/* 262 */       return etag.substring(1, etag.length() - 1);
/*     */     }
/* 264 */     return etag;
/*     */   }
/*     */   
/*     */   public MetadataDTO getMetadataDTO() {
/* 268 */     return this.metadataDTO;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 273 */     if (this == o) return true; 
/* 274 */     if (o == null || getClass() != o.getClass()) return false; 
/* 275 */     if (o.hashCode() != hashCode()) return false; 
/* 276 */     Downloadable that = (Downloadable)o;
/* 277 */     return (Objects.equals(this.additionalDestinations, that.additionalDestinations) && 
/* 278 */       Objects.equals(this.repo, that.repo) && 
/* 279 */       Objects.equals(this.metadataDTO, that.metadataDTO));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 285 */     return Objects.hash(new Object[] { this.additionalDestinations, this.repo, this.metadataDTO });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\downloader\Downloadable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */