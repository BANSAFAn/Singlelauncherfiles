/*     */ package org.tlauncher.tlauncher.ui.background.slide;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.regex.Pattern;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.explorer.filters.ImageFileFilter;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.LoopedThread;
/*     */ 
/*     */ public class SlideBackgroundThread
/*     */   extends LoopedThread {
/*  17 */   private static final Pattern extensionPattern = ImageFileFilter.extensionPattern;
/*     */   
/*     */   private static final String defaultImageName = "plains.jpg";
/*     */   
/*     */   private final SlideBackground background;
/*     */   final Slide defaultSlide;
/*     */   private Slide currentSlide;
/*     */   
/*     */   SlideBackgroundThread(SlideBackground background) {
/*  26 */     super("SlideBackgroundThread");
/*     */     
/*  28 */     this.background = background;
/*  29 */     this.defaultSlide = new Slide(ImageCache.getRes("plains.jpg"));
/*     */     
/*  31 */     startAndWait();
/*     */   }
/*     */   
/*     */   public SlideBackground getBackground() {
/*  35 */     return this.background;
/*     */   }
/*     */   
/*     */   public Slide getSlide() {
/*  39 */     return this.currentSlide;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void refreshSlide(boolean animate) {
/*  44 */     String path = TLauncher.getInstance().getConfiguration().get("gui.background");
/*  45 */     URL url = getImageURL(path);
/*  46 */     Slide slide = (url == null) ? this.defaultSlide : new Slide(url);
/*     */     
/*  48 */     setSlide(slide, animate);
/*     */   }
/*     */   
/*     */   public void asyncRefreshSlide() {
/*  52 */     iterate();
/*     */   }
/*     */   
/*     */   public synchronized void setSlide(Slide slide, boolean animate) {
/*  56 */     if (slide == null) {
/*  57 */       throw new NullPointerException();
/*     */     }
/*  59 */     if (slide.equals(this.currentSlide)) {
/*     */       return;
/*     */     }
/*  62 */     Image image = slide.getImage();
/*     */     
/*  64 */     if (image == null) {
/*  65 */       slide = this.defaultSlide;
/*  66 */       image = slide.getImage();
/*     */     } 
/*     */     
/*  69 */     this.currentSlide = slide;
/*     */     
/*  71 */     if (image == null) {
/*  72 */       log(new Object[] { "Default image is NULL. Check accessibility to the JAR file of TLauncher." });
/*     */       
/*     */       return;
/*     */     } 
/*  76 */     this.background.holder.cover.makeCover(animate);
/*  77 */     this.background.setImage(image);
/*     */     
/*  79 */     U.sleepFor(500L);
/*     */     
/*  81 */     this.background.holder.cover.removeCover(animate);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void iterateOnce() {
/*  86 */     refreshSlide(true);
/*     */   }
/*     */   
/*     */   private URL getImageURL(String path) {
/*  90 */     log(new Object[] { "Trying to resolve path:", path });
/*     */     
/*  92 */     if (path == null) {
/*  93 */       log(new Object[] { "Na NULL i suda NULL." });
/*  94 */       return null;
/*     */     } 
/*     */     
/*  97 */     URL asURL = U.makeURL(path);
/*  98 */     if (asURL != null) {
/*  99 */       log(new Object[] { "Path resolved as an URL:", asURL });
/* 100 */       return asURL;
/*     */     } 
/*     */     
/* 103 */     File asFile = new File(path);
/* 104 */     if (asFile.isFile()) {
/* 105 */       String absPath = asFile.getAbsolutePath();
/* 106 */       log(new Object[] { "Path resolved as a file:", absPath });
/*     */       
/* 108 */       String ext = FileUtil.getExtension(asFile);
/* 109 */       if (ext == null || !extensionPattern.matcher(ext).matches()) {
/* 110 */         log(new Object[] { "This file doesn't seem to be an image. It should have JPG or PNG format." });
/* 111 */         return null;
/*     */       } 
/*     */       
/*     */       try {
/* 115 */         return asFile.toURI().toURL();
/* 116 */       } catch (IOException e) {
/* 117 */         log(new Object[] { "Cannot covert this file into URL.", e });
/* 118 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     log(new Object[] { "Cannot resolve this path." });
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   protected void log(Object... w) {
/* 127 */     U.log(new Object[] { "[" + getClass().getSimpleName() + "]", w });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\background\slide\SlideBackgroundThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */