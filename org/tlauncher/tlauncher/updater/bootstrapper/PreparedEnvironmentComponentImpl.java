/*     */ package org.tlauncher.tlauncher.updater.bootstrapper;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.downloader.DownloadableContainer;
/*     */ import org.tlauncher.tlauncher.downloader.DownloadableContainerHandler;
/*     */ import org.tlauncher.tlauncher.downloader.DownloadableContainerHandlerAdapter;
/*     */ import org.tlauncher.tlauncher.downloader.Downloader;
/*     */ import org.tlauncher.tlauncher.downloader.RetryDownloadException;
/*     */ import org.tlauncher.tlauncher.repository.Repo;
/*     */ import org.tlauncher.tlauncher.rmo.Bootstrapper;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedBootInfo;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.model.DownloadedElement;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.model.JavaConfig;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.model.JavaDownloadedElement;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.model.LibraryConfig;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class PreparedEnvironmentComponentImpl implements PreparedEnvironmentComponent {
/*     */   private final LibraryConfig config;
/*     */   private final JavaConfig javaConfig;
/*     */   
/*     */   public PreparedEnvironmentComponentImpl(LibraryConfig config, JavaConfig javaConfig, File workFolder, File jvmsFolder, Downloader downloader) {
/*  35 */     this.config = config; this.javaConfig = javaConfig; this.workFolder = workFolder; this.jvmsFolder = jvmsFolder; this.downloader = downloader;
/*     */   }
/*     */ 
/*     */   
/*     */   private final File workFolder;
/*     */   
/*     */   private final File jvmsFolder;
/*     */   
/*     */   private Downloader downloader;
/*     */   
/*     */   public List<String> getLibrariesForRunning() {
/*  46 */     List<DownloadedElement> list = this.config.getLibraries();
/*  47 */     List<String> result = new ArrayList<>();
/*  48 */     for (DownloadedElement downloadedElement : list) {
/*  49 */       File f = new File(this.workFolder, downloadedElement.getStoragePath());
/*  50 */       result.add(f.getPath());
/*     */     } 
/*  52 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public DownloadedBootInfo validateLibraryAndJava() {
/*  57 */     List<DownloadedElement> result = new ArrayList<>();
/*  58 */     List<DownloadedElement> list = this.config.getLibraries();
/*  59 */     for (DownloadedElement downloadedElement : list) {
/*  60 */       if (downloadedElement.notExistOrValid(this.workFolder))
/*  61 */         result.add(downloadedElement); 
/*     */     } 
/*  63 */     DownloadedBootInfo el = new DownloadedBootInfo();
/*  64 */     el.setLibraries(result);
/*  65 */     JavaDownloadedElement javaDownloadedElement = TlauncherUtil.getProperJavaElement(this.javaConfig);
/*     */     
/*  67 */     if (!javaDownloadedElement.existFolder(this.jvmsFolder)) {
/*  68 */       el.setElement(javaDownloadedElement);
/*  69 */       return el;
/*     */     } 
/*  71 */     return el;
/*     */   }
/*     */ 
/*     */   
/*     */   public void preparedLibrariesAndJava(DownloadedBootInfo info) {
/*  76 */     if (info.getLibraries().isEmpty() && Objects.isNull(info.getElement()))
/*     */       return; 
/*  78 */     if (Objects.nonNull(info.getElement())) {
/*  79 */       MetadataDTO metadataDTO = new MetadataDTO();
/*  80 */       metadataDTO.setUrl("");
/*  81 */       metadataDTO.setPath(info.getElement().getStoragePath());
/*  82 */       metadataDTO.setSha1(info.getElement().getShaCode());
/*  83 */       metadataDTO.setSize(info.getElement().getSize());
/*  84 */       metadataDTO.setLocalDestination(new File(this.jvmsFolder, info.getElement().getStoragePath()));
/*  85 */       DownloadableContainer javaDownloadableContainer = new DownloadableContainer();
/*  86 */       DownloadableDownloadElement downloadedJava = new DownloadableDownloadElement((DownloadedElement)info.getElement(), metadataDTO);
/*     */       
/*  88 */       javaDownloadableContainer.add(downloadedJava);
/*  89 */       javaDownloadableContainer.addHandler((DownloadableContainerHandler)new DownloadableHandler(this.jvmsFolder)
/*     */           {
/*     */             public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
/*  92 */               PreparedEnvironmentComponentImpl.DownloadableDownloadElement el = (PreparedEnvironmentComponentImpl.DownloadableDownloadElement)d;
/*  93 */               super.onComplete(c, d);
/*  94 */               JavaDownloadedElement original = (JavaDownloadedElement)el.getElement();
/*  95 */               File javaFolder = new File(PreparedEnvironmentComponentImpl.this.jvmsFolder, original.getJavaFolder());
/*     */               
/*  97 */               boolean originalJVM = original.isOriginalJVM();
/*  98 */               File tempJVM = new File(javaFolder + "_temp"); try {
/*     */                 File javaTempFolder;
/* 100 */                 if (javaFolder.exists())
/* 101 */                   FileUtil.deleteDirectory(javaFolder); 
/* 102 */                 if (tempJVM.exists())
/* 103 */                   FileUtil.deleteDirectory(tempJVM); 
/* 104 */                 PreparedEnvironmentComponentImpl.this.unpack(d, Boolean.valueOf(originalJVM), tempJVM);
/*     */                 
/* 106 */                 if (originalJVM) {
/* 107 */                   javaTempFolder = new File(tempJVM, original.getJavaFolder());
/*     */                 } else {
/* 109 */                   javaTempFolder = tempJVM;
/* 110 */                 }  File java = new File(OS.appendBootstrapperJvm(javaTempFolder.getPath()));
/* 111 */                 if (OS.is(new OS[] { OS.LINUX }))
/* 112 */                   Files.setPosixFilePermissions(java.toPath(), FileUtil.PERMISSIONS); 
/* 113 */                 if (!javaTempFolder.renameTo(javaFolder))
/* 114 */                   FileUtils.copyDirectory(javaTempFolder, javaFolder); 
/* 115 */                 if (tempJVM.exists())
/* 116 */                   FileUtil.deleteFile(tempJVM); 
/* 117 */                 FileUtil.deleteFile(d.getMetadataDTO().getLocalDestination());
/* 118 */               } catch (IOException t) {
/* 119 */                 U.log(new Object[] { t });
/* 120 */                 if (javaFolder.exists())
/* 121 */                   FileUtil.deleteDirectory(javaFolder); 
/* 122 */                 throw new RetryDownloadException("cannot unpack archive", t);
/*     */               } 
/*     */             }
/*     */           });
/* 126 */       this.downloader.add(javaDownloadableContainer);
/*     */     } 
/* 128 */     if (!info.getLibraries().isEmpty()) {
/* 129 */       DownloadableContainer container = new DownloadableContainer();
/* 130 */       for (DownloadedElement e : info.getLibraries()) {
/* 131 */         MetadataDTO metadataDTO = new MetadataDTO();
/* 132 */         metadataDTO.setUrl("");
/* 133 */         metadataDTO.setPath(e.getStoragePath());
/* 134 */         metadataDTO.setSha1(e.getShaCode());
/* 135 */         metadataDTO.setSize(e.getSize());
/* 136 */         metadataDTO.setLocalDestination(new File(this.workFolder, e.getStoragePath()));
/* 137 */         container.add(new DownloadableDownloadElement(e, metadataDTO));
/*     */       } 
/* 139 */       container.addHandler((DownloadableContainerHandler)new DownloadableHandler(this.workFolder));
/* 140 */       this.downloader.add(container);
/*     */     } 
/* 142 */     this.downloader.startDownloadAndWait();
/*     */   }
/*     */   
/*     */   private void unpack(Downloadable d, Boolean originalJVM, File tempJVM) throws IOException {
/* 146 */     if (OS.is(new OS[] { OS.OSX })) {
/*     */       try {
/* 148 */         String[] commands = { "tar", "-xf", d.getMetadataDTO().getLocalDestination().getPath() };
/* 149 */         ProcessBuilder p = new ProcessBuilder(commands);
/* 150 */         p.directory(this.jvmsFolder);
/* 151 */         Process process = p.start();
/*     */         try {
/* 153 */           process.waitFor(90L, TimeUnit.SECONDS);
/* 154 */         } catch (InterruptedException e) {
/* 155 */           e.printStackTrace();
/*     */         } 
/* 157 */       } catch (Throwable e) {
/* 158 */         TlauncherUtil.showCriticalProblem(e);
/*     */       }
/*     */     
/* 161 */     } else if (originalJVM.booleanValue()) {
/* 162 */       FileUtil.unTarGz(d.getMetadataDTO().getLocalDestination(), tempJVM, false, false);
/*     */     } else {
/* 164 */       FileUtil.unZip(d.getMetadataDTO().getLocalDestination(), this.jvmsFolder, false, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setDownloader(Downloader downloader) {
/* 169 */     this.downloader = downloader;
/*     */   } public static class DownloadableDownloadElement extends Downloadable {
/*     */     private DownloadedElement element;
/* 172 */     public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloadableDownloadElement)) return false;  DownloadableDownloadElement other = (DownloadableDownloadElement)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$element = getElement(), other$element = other.getElement(); return !((this$element == null) ? (other$element != null) : !this$element.equals(other$element)); } protected boolean canEqual(Object other) { return other instanceof DownloadableDownloadElement; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $element = getElement(); return result * 59 + (($element == null) ? 43 : $element.hashCode()); }
/* 173 */     public void setElement(DownloadedElement element) { this.element = element; } public String toString() { return "PreparedEnvironmentComponentImpl.DownloadableDownloadElement(element=" + getElement() + ")"; }
/*     */      public DownloadedElement getElement() {
/* 175 */       return this.element;
/*     */     }
/*     */     
/*     */     DownloadableDownloadElement(DownloadedElement element, MetadataDTO metadataDTO) {
/* 179 */       super(new Repo((String[])element.getUrl().toArray((Object[])new String[0]), "BOOTSTRAP"), metadataDTO);
/* 180 */       this.element = element;
/*     */     } }
/*     */   private static class DownloadableHandler extends DownloadableContainerHandlerAdapter { private File downloadFolder;
/*     */     
/* 184 */     public void setDownloadFolder(File downloadFolder) { this.downloadFolder = downloadFolder; } public String toString() { return "PreparedEnvironmentComponentImpl.DownloadableHandler(downloadFolder=" + getDownloadFolder() + ")"; }
/* 185 */     public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloadableHandler)) return false;  DownloadableHandler other = (DownloadableHandler)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$downloadFolder = getDownloadFolder(), other$downloadFolder = other.getDownloadFolder(); return !((this$downloadFolder == null) ? (other$downloadFolder != null) : !this$downloadFolder.equals(other$downloadFolder)); } protected boolean canEqual(Object other) { return other instanceof DownloadableHandler; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $downloadFolder = getDownloadFolder(); return result * 59 + (($downloadFolder == null) ? 43 : $downloadFolder.hashCode()); } public DownloadableHandler(File downloadFolder) {
/* 186 */       this.downloadFolder = downloadFolder;
/*     */     } public File getDownloadFolder() {
/* 188 */       return this.downloadFolder;
/*     */     }
/*     */     
/*     */     public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
/* 192 */       PreparedEnvironmentComponentImpl.DownloadableDownloadElement el = (PreparedEnvironmentComponentImpl.DownloadableDownloadElement)d;
/* 193 */       if (el.getElement().notExistOrValid(this.downloadFolder)) {
/* 194 */         throw new RetryDownloadException("illegal library hash. " + el.getElement());
/*     */       }
/*     */     }
/*     */     
/*     */     public void onError(DownloadableContainer c, Downloadable d, Throwable e) {
/* 199 */       U.log(new Object[] { e });
/* 200 */       TlauncherUtil.showCriticalProblem(Bootstrapper.langConfiguration.get("updater.download.fail", new Object[] { "" }));
/* 201 */       TLauncher.kill();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\bootstrapper\PreparedEnvironmentComponentImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */