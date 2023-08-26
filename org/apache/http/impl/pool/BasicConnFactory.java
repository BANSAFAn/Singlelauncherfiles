/*     */ package org.apache.http.impl.pool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpConnectionFactory;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.config.SocketConfig;
/*     */ import org.apache.http.impl.DefaultBHttpClientConnection;
/*     */ import org.apache.http.impl.DefaultBHttpClientConnectionFactory;
/*     */ import org.apache.http.params.HttpParamConfig;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.pool.ConnFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class BasicConnFactory
/*     */   implements ConnFactory<HttpHost, HttpClientConnection>
/*     */ {
/*     */   private final SocketFactory plainfactory;
/*     */   private final SSLSocketFactory sslfactory;
/*     */   private final int connectTimeout;
/*     */   private final SocketConfig sconfig;
/*     */   private final HttpConnectionFactory<? extends HttpClientConnection> connFactory;
/*     */   
/*     */   @Deprecated
/*     */   public BasicConnFactory(SSLSocketFactory sslfactory, HttpParams params) {
/*  75 */     Args.notNull(params, "HTTP params");
/*  76 */     this.plainfactory = null;
/*  77 */     this.sslfactory = sslfactory;
/*  78 */     this.connectTimeout = params.getIntParameter("http.connection.timeout", 0);
/*  79 */     this.sconfig = HttpParamConfig.getSocketConfig(params);
/*  80 */     this.connFactory = (HttpConnectionFactory<? extends HttpClientConnection>)new DefaultBHttpClientConnectionFactory(HttpParamConfig.getConnectionConfig(params));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BasicConnFactory(HttpParams params) {
/*  90 */     this((SSLSocketFactory)null, params);
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
/*     */   public BasicConnFactory(SocketFactory plainfactory, SSLSocketFactory sslfactory, int connectTimeout, SocketConfig sconfig, ConnectionConfig cconfig) {
/* 103 */     this.plainfactory = plainfactory;
/* 104 */     this.sslfactory = sslfactory;
/* 105 */     this.connectTimeout = connectTimeout;
/* 106 */     this.sconfig = (sconfig != null) ? sconfig : SocketConfig.DEFAULT;
/* 107 */     this.connFactory = (HttpConnectionFactory<? extends HttpClientConnection>)new DefaultBHttpClientConnectionFactory((cconfig != null) ? cconfig : ConnectionConfig.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicConnFactory(int connectTimeout, SocketConfig sconfig, ConnectionConfig cconfig) {
/* 116 */     this(null, null, connectTimeout, sconfig, cconfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicConnFactory(SocketConfig sconfig, ConnectionConfig cconfig) {
/* 123 */     this(null, null, 0, sconfig, cconfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicConnFactory() {
/* 130 */     this(null, null, 0, SocketConfig.DEFAULT, ConnectionConfig.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected HttpClientConnection create(Socket socket, HttpParams params) throws IOException {
/* 138 */     int bufsize = params.getIntParameter("http.socket.buffer-size", 8192);
/* 139 */     DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(bufsize);
/* 140 */     conn.bind(socket);
/* 141 */     return (HttpClientConnection)conn;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpClientConnection create(HttpHost host) throws IOException {
/* 146 */     String scheme = host.getSchemeName();
/* 147 */     Socket socket = null;
/* 148 */     if ("http".equalsIgnoreCase(scheme)) {
/* 149 */       socket = (this.plainfactory != null) ? this.plainfactory.createSocket() : new Socket();
/*     */     }
/* 151 */     if ("https".equalsIgnoreCase(scheme)) {
/* 152 */       socket = ((this.sslfactory != null) ? this.sslfactory : SSLSocketFactory.getDefault()).createSocket();
/*     */     }
/*     */     
/* 155 */     if (socket == null) {
/* 156 */       throw new IOException(scheme + " scheme is not supported");
/*     */     }
/* 158 */     String hostname = host.getHostName();
/* 159 */     int port = host.getPort();
/* 160 */     if (port == -1) {
/* 161 */       if (host.getSchemeName().equalsIgnoreCase("http")) {
/* 162 */         port = 80;
/* 163 */       } else if (host.getSchemeName().equalsIgnoreCase("https")) {
/* 164 */         port = 443;
/*     */       } 
/*     */     }
/* 167 */     socket.setSoTimeout(this.sconfig.getSoTimeout());
/* 168 */     if (this.sconfig.getSndBufSize() > 0) {
/* 169 */       socket.setSendBufferSize(this.sconfig.getSndBufSize());
/*     */     }
/* 171 */     if (this.sconfig.getRcvBufSize() > 0) {
/* 172 */       socket.setReceiveBufferSize(this.sconfig.getRcvBufSize());
/*     */     }
/* 174 */     socket.setTcpNoDelay(this.sconfig.isTcpNoDelay());
/* 175 */     int linger = this.sconfig.getSoLinger();
/* 176 */     if (linger >= 0) {
/* 177 */       socket.setSoLinger(true, linger);
/*     */     }
/* 179 */     socket.setKeepAlive(this.sconfig.isSoKeepAlive());
/* 180 */     socket.connect(new InetSocketAddress(hostname, port), this.connectTimeout);
/* 181 */     return (HttpClientConnection)this.connFactory.createConnection(socket);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\pool\BasicConnFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */