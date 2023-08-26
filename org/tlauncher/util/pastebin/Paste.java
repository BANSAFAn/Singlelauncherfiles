/*     */ package org.tlauncher.util.pastebin;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import net.minecraft.launcher.Http;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Paste
/*     */ {
/*     */   private static final String DEV_KEY = "394691d8a27e3852a24969dbeba85e53";
/*  15 */   private static final URL POST_URL = Http.constantURL("https://pastebin.com/api/api_post.php");
/*     */   
/*     */   private String title;
/*     */   
/*     */   public final String getTitle() {
/*  20 */     return this.title;
/*     */   }
/*     */   private String content; private String format;
/*     */   public final void setTitle(String title) {
/*  24 */     this.title = title;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getContent() {
/*  30 */     return this.content;
/*     */   }
/*     */   
/*     */   public final void setContent(String content) {
/*  34 */     this.content = content;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getFormat() {
/*  40 */     return this.format;
/*     */   }
/*     */   
/*     */   public final void setFormat(String format) {
/*  44 */     this.format = format;
/*     */   }
/*     */   
/*  47 */   private ExpireDate expires = ExpireDate.ONE_WEEK;
/*     */   
/*     */   public final ExpireDate getExpireDate() {
/*  50 */     return this.expires;
/*     */   }
/*     */   
/*     */   public final void setExpireDate(ExpireDate date) {
/*  54 */     this.expires = date;
/*     */   }
/*     */   
/*  57 */   private Visibility visibility = Visibility.NOT_LISTED;
/*     */   
/*     */   public final Visibility getVisibility() {
/*  60 */     return this.visibility;
/*     */   }
/*     */   
/*     */   public final void setVisibility(Visibility vis) {
/*  64 */     this.visibility = vis;
/*     */   }
/*     */   
/*  67 */   private final ArrayList<PasteListener> listeners = new ArrayList<>(); private PasteResult result;
/*     */   
/*     */   public void addListener(PasteListener listener) {
/*  70 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeListener(PasteListener listener) {
/*  74 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PasteResult paste() {
/*  80 */     for (PasteListener listener : this.listeners) {
/*  81 */       listener.pasteUploading(this);
/*     */     }
/*     */     try {
/*  84 */       this.result = doPaste();
/*  85 */     } catch (Throwable t) {
/*  86 */       this.result = new PasteResult.PasteFailed(this, t);
/*     */     } 
/*     */     
/*  89 */     for (PasteListener listener : this.listeners) {
/*  90 */       listener.pasteDone(this);
/*     */     }
/*  92 */     return this.result;
/*     */   }
/*     */   
/*     */   private PasteResult.PasteUploaded doPaste() throws IOException {
/*  96 */     if (StringUtils.isEmpty(getContent())) {
/*  97 */       throw new IllegalArgumentException("content is empty");
/*     */     }
/*  99 */     if (getVisibility() == null) {
/* 100 */       throw new NullPointerException("visibility");
/*     */     }
/* 102 */     if (getExpireDate() == null) {
/* 103 */       throw new NullPointerException("expire date");
/*     */     }
/* 105 */     HashMap<String, Object> query = new HashMap<>();
/*     */     
/* 107 */     query.put("api_dev_key", "394691d8a27e3852a24969dbeba85e53");
/* 108 */     query.put("api_option", "paste");
/* 109 */     query.put("api_paste_name", getTitle());
/* 110 */     query.put("api_paste_code", getContent());
/* 111 */     query.put("api_paste_private", Integer.valueOf(getVisibility().getValue()));
/* 112 */     query.put("api_paste_expire_date", getExpireDate().getValue());
/*     */     
/* 114 */     String answer = Http.performPost(POST_URL, query);
/*     */     
/* 116 */     if (answer.startsWith("http")) {
/* 117 */       return new PasteResult.PasteUploaded(this, new URL(answer));
/*     */     }
/* 119 */     throw new IOException("illegal answer: \"" + answer + '"');
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\pastebin\Paste.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */