/*     */ package org.apache.http.conn.scheme;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.ConnectTimeoutException;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class PlainSocketFactory
/*     */   implements SocketFactory, SchemeSocketFactory
/*     */ {
/*     */   private final HostNameResolver nameResolver;
/*     */   
/*     */   public static PlainSocketFactory getSocketFactory() {
/*  62 */     return new PlainSocketFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public PlainSocketFactory(HostNameResolver nameResolver) {
/*  71 */     this.nameResolver = nameResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public PlainSocketFactory() {
/*  76 */     this.nameResolver = null;
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
/*     */   public Socket createSocket(HttpParams params) {
/*  88 */     return new Socket();
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket createSocket() {
/*  93 */     return new Socket();
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
/*     */   public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, ConnectTimeoutException {
/* 105 */     Args.notNull(remoteAddress, "Remote address");
/* 106 */     Args.notNull(params, "HTTP parameters");
/* 107 */     Socket sock = socket;
/* 108 */     if (sock == null) {
/* 109 */       sock = createSocket();
/*     */     }
/* 111 */     if (localAddress != null) {
/* 112 */       sock.setReuseAddress(HttpConnectionParams.getSoReuseaddr(params));
/* 113 */       sock.bind(localAddress);
/*     */     } 
/* 115 */     int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
/* 116 */     int soTimeout = HttpConnectionParams.getSoTimeout(params);
/*     */     
/*     */     try {
/* 119 */       sock.setSoTimeout(soTimeout);
/* 120 */       sock.connect(remoteAddress, connTimeout);
/* 121 */     } catch (SocketTimeoutException ex) {
/* 122 */       throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
/*     */     } 
/* 124 */     return sock;
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
/*     */   public final boolean isSecure(Socket sock) {
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
/*     */     InetAddress remoteAddress;
/* 151 */     InetSocketAddress local = null;
/* 152 */     if (localAddress != null || localPort > 0) {
/* 153 */       local = new InetSocketAddress(localAddress, (localPort > 0) ? localPort : 0);
/*     */     }
/*     */     
/* 156 */     if (this.nameResolver != null) {
/* 157 */       remoteAddress = this.nameResolver.resolve(host);
/*     */     } else {
/* 159 */       remoteAddress = InetAddress.getByName(host);
/*     */     } 
/* 161 */     InetSocketAddress remote = new InetSocketAddress(remoteAddress, port);
/* 162 */     return connectSocket(socket, remote, local, params);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\scheme\PlainSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */