/*     */ package org.apache.http.impl.bootstrap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import javax.net.ServerSocketFactory;
/*     */ import javax.net.ssl.SSLServerSocket;
/*     */ import org.apache.http.ExceptionLogger;
/*     */ import org.apache.http.HttpConnectionFactory;
/*     */ import org.apache.http.HttpServerConnection;
/*     */ import org.apache.http.config.SocketConfig;
/*     */ import org.apache.http.impl.DefaultBHttpServerConnection;
/*     */ import org.apache.http.protocol.HttpService;
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
/*     */ public class HttpServer
/*     */ {
/*     */   private final int port;
/*     */   private final InetAddress ifAddress;
/*     */   private final SocketConfig socketConfig;
/*     */   private final ServerSocketFactory serverSocketFactory;
/*     */   private final HttpService httpService;
/*     */   private final HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory;
/*     */   private final SSLServerSetupHandler sslSetupHandler;
/*     */   private final ExceptionLogger exceptionLogger;
/*     */   private final ExecutorService listenerExecutorService;
/*     */   private final ThreadGroup workerThreads;
/*     */   private final ExecutorService workerExecutorService;
/*     */   private final AtomicReference<Status> status;
/*     */   private volatile ServerSocket serverSocket;
/*     */   private volatile RequestListener requestListener;
/*     */   
/*     */   enum Status
/*     */   {
/*  53 */     READY, ACTIVE, STOPPING;
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   HttpServer(int port, InetAddress ifAddress, SocketConfig socketConfig, ServerSocketFactory serverSocketFactory, HttpService httpService, HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory, SSLServerSetupHandler sslSetupHandler, ExceptionLogger exceptionLogger) {
/*  80 */     this.port = port;
/*  81 */     this.ifAddress = ifAddress;
/*  82 */     this.socketConfig = socketConfig;
/*  83 */     this.serverSocketFactory = serverSocketFactory;
/*  84 */     this.httpService = httpService;
/*  85 */     this.connectionFactory = connectionFactory;
/*  86 */     this.sslSetupHandler = sslSetupHandler;
/*  87 */     this.exceptionLogger = exceptionLogger;
/*  88 */     this.listenerExecutorService = Executors.newSingleThreadExecutor(new ThreadFactoryImpl("HTTP-listener-" + this.port));
/*     */     
/*  90 */     this.workerThreads = new ThreadGroup("HTTP-workers");
/*  91 */     this.workerExecutorService = Executors.newCachedThreadPool(new ThreadFactoryImpl("HTTP-worker", this.workerThreads));
/*     */     
/*  93 */     this.status = new AtomicReference<Status>(Status.READY);
/*     */   }
/*     */   
/*     */   public InetAddress getInetAddress() {
/*  97 */     ServerSocket localSocket = this.serverSocket;
/*  98 */     if (localSocket != null) {
/*  99 */       return localSocket.getInetAddress();
/*     */     }
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 106 */     ServerSocket localSocket = this.serverSocket;
/* 107 */     if (localSocket != null) {
/* 108 */       return localSocket.getLocalPort();
/*     */     }
/* 110 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() throws IOException {
/* 115 */     if (this.status.compareAndSet(Status.READY, Status.ACTIVE)) {
/* 116 */       this.serverSocket = this.serverSocketFactory.createServerSocket(this.port, this.socketConfig.getBacklogSize(), this.ifAddress);
/*     */       
/* 118 */       this.serverSocket.setReuseAddress(this.socketConfig.isSoReuseAddress());
/* 119 */       if (this.socketConfig.getRcvBufSize() > 0) {
/* 120 */         this.serverSocket.setReceiveBufferSize(this.socketConfig.getRcvBufSize());
/*     */       }
/* 122 */       if (this.sslSetupHandler != null && this.serverSocket instanceof SSLServerSocket) {
/* 123 */         this.sslSetupHandler.initialize((SSLServerSocket)this.serverSocket);
/*     */       }
/* 125 */       this.requestListener = new RequestListener(this.socketConfig, this.serverSocket, this.httpService, (HttpConnectionFactory)this.connectionFactory, this.exceptionLogger, this.workerExecutorService);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       this.listenerExecutorService.execute(this.requestListener);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 137 */     if (this.status.compareAndSet(Status.ACTIVE, Status.STOPPING)) {
/* 138 */       RequestListener local = this.requestListener;
/* 139 */       if (local != null) {
/*     */         try {
/* 141 */           local.terminate();
/* 142 */         } catch (IOException ex) {
/* 143 */           this.exceptionLogger.log(ex);
/*     */         } 
/*     */       }
/* 146 */       this.workerThreads.interrupt();
/* 147 */       this.listenerExecutorService.shutdown();
/* 148 */       this.workerExecutorService.shutdown();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException {
/* 153 */     this.workerExecutorService.awaitTermination(timeout, timeUnit);
/*     */   }
/*     */   
/*     */   public void shutdown(long gracePeriod, TimeUnit timeUnit) {
/* 157 */     stop();
/* 158 */     if (gracePeriod > 0L) {
/*     */       try {
/* 160 */         awaitTermination(gracePeriod, timeUnit);
/* 161 */       } catch (InterruptedException ex) {
/* 162 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     }
/* 165 */     List<Runnable> runnables = this.workerExecutorService.shutdownNow();
/* 166 */     for (Runnable runnable : runnables) {
/* 167 */       if (runnable instanceof Worker) {
/* 168 */         Worker worker = (Worker)runnable;
/* 169 */         HttpServerConnection conn = worker.getConnection();
/*     */         try {
/* 171 */           conn.shutdown();
/* 172 */         } catch (IOException ex) {
/* 173 */           this.exceptionLogger.log(ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\bootstrap\HttpServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */