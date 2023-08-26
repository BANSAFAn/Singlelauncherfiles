/*    */ package org.tlauncher.tlauncher.ui.background.slide;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.net.URL;
/*    */ import javax.imageio.ImageIO;
/*    */ import org.tlauncher.util.Reflect;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ public class Slide
/*    */ {
/*    */   private final URL url;
/*    */   private Image image;
/*    */   
/*    */   public Slide(URL url) {
/* 17 */     if (url == null) {
/* 18 */       throw new NullPointerException();
/*    */     }
/* 20 */     this.url = url;
/*    */     
/* 22 */     if (isLocal()) {
/* 23 */       load();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 28 */     if (o == null) {
/* 29 */       return false;
/*    */     }
/* 31 */     Slide slide = (Slide)Reflect.cast(o, Slide.class);
/* 32 */     if (slide == null) {
/* 33 */       return false;
/*    */     }
/* 35 */     return this.url.equals(slide.url);
/*    */   }
/*    */   
/*    */   public URL getURL() {
/* 39 */     return this.url;
/*    */   }
/*    */   
/*    */   public boolean isLocal() {
/* 43 */     return this.url.getProtocol().equals("file");
/*    */   }
/*    */   
/*    */   public Image getImage() {
/* 47 */     if (this.image == null)
/* 48 */       load(); 
/* 49 */     return this.image;
/*    */   }
/*    */   
/*    */   private void load() {
/* 53 */     log(new Object[] { "Loading from:", this.url });
/*    */     
/* 55 */     BufferedImage tempImage = null;
/*    */     
/*    */     try {
/* 58 */       tempImage = ImageIO.read(this.url);
/* 59 */     } catch (Throwable e) {
/* 60 */       log(new Object[] { "Cannot load slide!", e });
/*    */       
/*    */       return;
/*    */     } 
/* 64 */     if (tempImage == null) {
/* 65 */       log(new Object[] { "Image seems to be corrupted." });
/*    */       
/*    */       return;
/*    */     } 
/* 69 */     log(new Object[] { "Loaded successfully!" });
/* 70 */     this.image = tempImage;
/*    */   }
/*    */   
/*    */   protected void log(Object... w) {
/* 74 */     U.log(new Object[] { "[" + getClass().getSimpleName() + "]", w });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\background\slide\Slide.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */