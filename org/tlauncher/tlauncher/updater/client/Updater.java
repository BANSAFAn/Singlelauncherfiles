/*     */ package org.tlauncher.tlauncher.updater.client;
/*     */ 
/*     */ import com.google.gson.GsonBuilder;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.launcher.Http;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.AsyncThread;
/*     */ import org.tlauncher.util.gson.serializer.UpdateDeserializer;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Updater
/*     */ {
/*     */   private Update update;
/*  26 */   private final List<UpdaterListener> listeners = Collections.synchronizedList(new ArrayList<>());
/*     */   
/*     */   public Update getUpdate() {
/*  29 */     return this.update;
/*     */   }
/*     */   
/*     */   protected SearchResult findUpdate0() {
/*  33 */     SearchResult result = null;
/*     */     
/*  35 */     log(new Object[] { "Requesting an update..." });
/*     */     
/*  37 */     List<Throwable> errorList = new ArrayList<>();
/*     */ 
/*     */     
/*  40 */     String get = "?version=" + Http.encode(String.valueOf(TLauncher.getVersion())) + "&client=" + Http.encode(TLauncher.getInstance().getConfiguration().getClient().toString());
/*  41 */     String type = TLauncher.getInnerSettings().get("type").toLowerCase(Locale.ROOT);
/*  42 */     for (String updateUrl : getUpdateUrlList()) {
/*  43 */       updateUrl = String.format(updateUrl, new Object[] { type });
/*  44 */       long startTime = System.currentTimeMillis();
/*  45 */       log(new Object[] { "Requesting from:", updateUrl });
/*     */       
/*  47 */       String response = null;
/*     */       
/*     */       try {
/*  50 */         URL url = new URL(updateUrl + get);
/*     */         
/*  52 */         log(new Object[] { "Making request:", url });
/*     */         
/*  54 */         HttpURLConnection connection = Downloadable.setUp(url.openConnection(U.getProxy()), true);
/*  55 */         connection.setDoOutput(true);
/*     */ 
/*     */         
/*  58 */         response = IOUtils.toString(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
/*     */         
/*  60 */         Update update = (Update)(new GsonBuilder()).registerTypeAdapter(Update.class, new UpdateDeserializer()).create().fromJson(response, Update.class);
/*  61 */         result = new SearchSucceeded(update);
/*  62 */       } catch (Exception e) {
/*  63 */         log(new Object[] { "Failed to request from:", updateUrl, e });
/*     */         
/*  65 */         if (response != null) {
/*  66 */           log(new Object[] { "Response:", response });
/*     */         }
/*  68 */         result = null;
/*  69 */         errorList.add(e);
/*     */       } 
/*     */       
/*  72 */       log(new Object[] { "Request time:", Long.valueOf(System.currentTimeMillis() - startTime), "ms" });
/*     */       
/*  74 */       if (result == null) {
/*     */         continue;
/*     */       }
/*  77 */       log(new Object[] { "Successfully requested from:", updateUrl });
/*     */     } 
/*     */ 
/*     */     
/*  81 */     return (result == null) ? new SearchFailed(errorList) : result;
/*     */   }
/*     */   
/*     */   private SearchResult findUpdate() {
/*     */     try {
/*  86 */       SearchResult result = findUpdate0();
/*  87 */       dispatchResult(result);
/*  88 */       return result;
/*  89 */     } catch (Exception e) {
/*  90 */       log(new Object[] { e });
/*  91 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void asyncFindUpdate() {
/*  96 */     AsyncThread.execute(() -> findUpdate());
/*     */   }
/*     */   
/*     */   public void addListener(UpdaterListener l) {
/* 100 */     this.listeners.add(l);
/*     */   }
/*     */   
/*     */   public void removeListener(UpdaterListener l) {
/* 104 */     this.listeners.remove(l);
/*     */   }
/*     */   
/*     */   private void dispatchResult(SearchResult result) {
/* 108 */     requireNotNull(result, "result");
/*     */     
/* 110 */     if (result instanceof SearchSucceeded) {
/* 111 */       synchronized (this.listeners) {
/* 112 */         for (UpdaterListener l : this.listeners)
/* 113 */           l.onUpdaterSucceeded((SearchSucceeded)result); 
/*     */       } 
/* 115 */     } else if (result instanceof SearchFailed) {
/* 116 */       synchronized (this.listeners) {
/* 117 */         for (UpdaterListener l : this.listeners)
/* 118 */           l.onUpdaterErrored((SearchFailed)result); 
/*     */       } 
/*     */     } else {
/* 121 */       throw new IllegalArgumentException("unknown result of " + result.getClass());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onUpdaterRequests() {
/* 126 */     synchronized (this.listeners) {
/* 127 */       for (UpdaterListener l : this.listeners)
/* 128 */         l.onUpdaterRequesting(this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected List<String> getUpdateUrlList() {
/* 133 */     return Arrays.asList(TLauncher.getUpdateRepos());
/*     */   }
/*     */   
/*     */   public abstract class SearchResult {
/*     */     protected final Update response;
/*     */     
/*     */     public SearchResult(Update response) {
/* 140 */       this.response = response;
/*     */     }
/*     */     
/*     */     public final Update getResponse() {
/* 144 */       return this.response;
/*     */     }
/*     */     
/*     */     public final Updater getUpdater() {
/* 148 */       return Updater.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 153 */       return getClass().getSimpleName() + "{response=" + this.response + "}";
/*     */     }
/*     */   }
/*     */   
/*     */   public class SearchSucceeded extends SearchResult {
/*     */     public SearchSucceeded(Update response) {
/* 159 */       super((Update)Updater.requireNotNull((T)response, "response"));
/*     */     }
/*     */   }
/*     */   
/*     */   public class SearchFailed extends SearchResult {
/* 164 */     protected final List<Throwable> errorList = new ArrayList<>();
/*     */     
/*     */     public SearchFailed(List<Throwable> list) {
/* 167 */       super(null);
/*     */       
/* 169 */       for (Throwable t : list) {
/* 170 */         if (t == null)
/* 171 */           throw new NullPointerException(); 
/* 172 */       }  this.errorList.addAll(list);
/*     */     }
/*     */     
/*     */     public final List<Throwable> getCauseList() {
/* 176 */       return this.errorList;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 181 */       return getClass().getSimpleName() + "{errors=" + this.errorList + "}";
/*     */     }
/*     */   }
/*     */   
/*     */   protected void log(Object... o) {
/* 186 */     U.log(new Object[] { "[Updater]", o });
/*     */   }
/*     */   
/*     */   private static <T> T requireNotNull(T obj, String name) {
/* 190 */     if (obj == null)
/* 191 */       throw new NullPointerException(name); 
/* 192 */     return obj;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\client\Updater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */