/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ 
/*    */ public class RedirectUrl {
/*    */   private final URL url;
/*    */   
/*    */   public RedirectUrl(URL url) {
/* 10 */     this.url = url;
/*    */   }
/*    */   
/*    */   public RedirectUrl(String url) throws MalformedURLException {
/* 14 */     this(new URL(url));
/*    */   }
/*    */   
/*    */   public static RedirectUrl of(String url) {
/*    */     try {
/* 19 */       return new RedirectUrl(url);
/* 20 */     } catch (MalformedURLException e) {
/* 21 */       throw new Error("invalid url: " + url, e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 26 */     if (this == o)
/* 27 */       return true; 
/* 28 */     if (o == null || getClass() != o.getClass())
/* 29 */       return false; 
/* 30 */     RedirectUrl that = (RedirectUrl)o;
/* 31 */     return this.url.equals(that.url);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 35 */     return this.url.hashCode();
/*    */   }
/*    */   
/*    */   public URL getUrl() {
/* 39 */     return this.url;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 43 */     return "RedirectUrl{uri=" + this.url + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\RedirectUrl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */