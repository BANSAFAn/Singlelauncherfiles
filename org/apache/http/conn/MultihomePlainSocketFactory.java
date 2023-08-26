/*     */ package org.apache.http.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.scheme.SocketFactory;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public final class MultihomePlainSocketFactory
/*     */   implements SocketFactory
/*     */ {
/*  67 */   private static final MultihomePlainSocketFactory DEFAULT_FACTORY = new MultihomePlainSocketFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MultihomePlainSocketFactory getSocketFactory() {
/*  74 */     return DEFAULT_FACTORY;
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
/*     */   public Socket createSocket() {
/*  88 */     return new Socket();
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
/*     */   public Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException {
/* 111 */     Args.notNull(host, "Target host");
/* 112 */     Args.notNull(params, "HTTP parameters");
/*     */     
/* 114 */     Socket sock = socket;
/* 115 */     if (sock == null) {
/* 116 */       sock = createSocket();
/*     */     }
/*     */     
/* 119 */     if (localAddress != null || localPort > 0) {
/* 120 */       InetSocketAddress isa = new InetSocketAddress(localAddress, (localPort > 0) ? localPort : 0);
/*     */       
/* 122 */       sock.bind(isa);
/*     */     } 
/*     */     
/* 125 */     int timeout = HttpConnectionParams.getConnectionTimeout(params);
/*     */     
/* 127 */     InetAddress[] inetadrs = InetAddress.getAllByName(host);
/* 128 */     List<InetAddress> addresses = new ArrayList<InetAddress>(inetadrs.length);
/* 129 */     addresses.addAll(Arrays.asList(inetadrs));
/* 130 */     Collections.shuffle(addresses);
/*     */     
/* 132 */     IOException lastEx = null;
/* 133 */     for (InetAddress remoteAddress : addresses) {
/*     */       try {
/* 135 */         sock.connect(new InetSocketAddress(remoteAddress, port), timeout);
/*     */         break;
/* 137 */       } catch (SocketTimeoutException ex) {
/* 138 */         throw new ConnectTimeoutException("Connect to " + remoteAddress + " timed out");
/* 139 */       } catch (IOException ex) {
/*     */         
/* 141 */         sock = new Socket();
/*     */         
/* 143 */         lastEx = ex;
/*     */       } 
/*     */     } 
/* 146 */     if (lastEx != null) {
/* 147 */       throw lastEx;
/*     */     }
/* 149 */     return sock;
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
/*     */   public final boolean isSecure(Socket sock) throws IllegalArgumentException {
/* 168 */     Args.notNull(sock, "Socket");
/*     */ 
/*     */     
/* 171 */     Asserts.check(!sock.isClosed(), "Socket is closed");
/* 172 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\MultihomePlainSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */