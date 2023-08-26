/*    */ package org.tlauncher.util.pastebin;
/*    */ 
/*    */ import java.net.URL;
/*    */ 
/*    */ public abstract class PasteResult {
/*    */   private final Paste paste;
/*    */   
/*    */   PasteResult(Paste paste) {
/*  9 */     this.paste = paste;
/*    */   }
/*    */   
/*    */   public final Paste getPaste() {
/* 13 */     return this.paste;
/*    */   }
/*    */   
/*    */   public static class PasteUploaded extends PasteResult {
/*    */     private final URL url;
/*    */     
/*    */     PasteUploaded(Paste paste, URL url) {
/* 20 */       super(paste);
/*    */       
/* 22 */       this.url = url;
/*    */     }
/*    */     
/*    */     public final URL getURL() {
/* 26 */       return this.url;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 31 */       return "PasteUploaded{url='" + this.url + "'}";
/*    */     }
/*    */   }
/*    */   
/*    */   public static class PasteFailed extends PasteResult {
/*    */     private final Throwable error;
/*    */     
/*    */     PasteFailed(Paste paste, Throwable error) {
/* 39 */       super(paste);
/*    */       
/* 41 */       this.error = error;
/*    */     }
/*    */     
/*    */     public final Throwable getError() {
/* 45 */       return this.error;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 50 */       return "PasteFailed{error='" + this.error + "'}";
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\pastebin\PasteResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */