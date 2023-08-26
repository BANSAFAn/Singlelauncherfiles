/*     */ package org.tlauncher.tlauncher.site.play;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.io.IOException;
/*     */ import java.security.KeyStore;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLServerSocket;
/*     */ import javax.net.ssl.SSLServerSocketFactory;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import net.minecraft.launcher.Http;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.tlauncher.entity.ServerCommandEntity;
/*     */ import org.tlauncher.tlauncher.entity.server.SiteServer;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.managers.VersionManager;
/*     */ import org.tlauncher.tlauncher.managers.VersionManagerListener;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ public class SitePlay
/*     */   implements VersionManagerListener
/*     */ {
/*     */   private boolean status = false;
/*     */   @Inject
/*     */   private TLauncher tLauncher;
/*     */   @Inject
/*     */   private ModpackManager manager;
/*     */   
/*     */   public void onVersionsRefreshing(VersionManager manager) {}
/*     */   
/*     */   public void onVersionsRefreshingFailed(VersionManager manager) {
/*  44 */     init();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onVersionsRefreshed(VersionManager manager) {
/*  49 */     init();
/*     */   }
/*     */   
/*     */   private synchronized void init() {
/*  53 */     if (this.status) {
/*     */       return;
/*     */     }
/*  56 */     this.status = true;
/*  57 */     Thread th = new Thread(new Runnable()
/*     */         {
/*     */           public void run() {
/*  60 */             String[] ports = TLauncher.getInnerSettings().getArray("local.ports.client.play");
/*  61 */             for (String port : ports) {
/*     */               try {
/*  63 */                 SSLContext sc = SitePlay.this.createSSLContext();
/*  64 */                 SSLServerSocketFactory ssf = sc.getServerSocketFactory();
/*     */                 
/*  66 */                 SSLServerSocket s = (SSLServerSocket)ssf.createServerSocket(Integer.valueOf(port).intValue());
/*  67 */                 s.setEnabledCipherSuites(sc.getServerSocketFactory().getSupportedCipherSuites());
/*     */                 
/*  69 */                 U.log(new Object[] { "run server on ", port });
/*     */                 while (true) {
/*     */                   try {
/*  72 */                     SSLSocket socket = (SSLSocket)s.accept();
/*  73 */                     ServerCommandEntity res = Http.readRequestInfo(socket);
/*  74 */                     if (!res.getRequestType().equals("OPTIONS")) {
/*  75 */                       if (res.getUrn().equals("/")) {
/*  76 */                         SitePlay.this.runGameWithServer(res); continue;
/*  77 */                       }  if (res.getUrn().startsWith("/open/modpack/element")) {
/*  78 */                         CompletableFuture.runAsync(() -> SitePlay.this.manager.openModpackElement(Long.valueOf((String)res.getQueries().get("id")), GameType.create((String)res.getQueries().get("type"))));
/*     */                       }
/*     */                     }
/*     */                   
/*     */                   }
/*  83 */                   catch (IOException e) {
/*  84 */                     U.log(new Object[] { e });
/*     */                   } 
/*     */                 } 
/*  87 */               } catch (Exception e) {
/*  88 */                 U.log(new Object[] { e });
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/*  93 */     th.setDaemon(true);
/*  94 */     th.start();
/*     */   }
/*     */   
/*     */   private SSLContext createSSLContext() throws Exception {
/*  98 */     char[] ksPass = "test123123".toCharArray();
/*  99 */     char[] ctPass = "test123123".toCharArray();
/* 100 */     KeyStore ks = KeyStore.getInstance("JKS");
/*     */     
/* 102 */     ks.load(TLauncher.class.getResource("/play_game_server").openStream(), ksPass);
/* 103 */     KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
/* 104 */     kmf.init(ks, ctPass);
/* 105 */     SSLContext sc = SSLContext.getInstance("TLS");
/* 106 */     sc.init(kmf.getKeyManagers(), null, null);
/* 107 */     return sc;
/*     */   }
/*     */   
/*     */   private void runGameWithServer(ServerCommandEntity res) {
/* 111 */     SiteServer siteServer = (SiteServer)(new Gson()).fromJson(res.getBody(), SiteServer.class);
/* 112 */     this.tLauncher.getFrame().setAlwaysOnTop(true);
/* 113 */     this.tLauncher.getFrame().setAlwaysOnTop(false);
/* 114 */     (this.tLauncher.getFrame()).mp.defaultScene.loginForm.startLauncher(siteServer);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\site\play\SitePlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */