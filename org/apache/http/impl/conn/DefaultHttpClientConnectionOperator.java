/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NoRouteToHostException;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketTimeoutException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.config.SocketConfig;
/*     */ import org.apache.http.conn.ConnectTimeoutException;
/*     */ import org.apache.http.conn.DnsResolver;
/*     */ import org.apache.http.conn.HttpClientConnectionOperator;
/*     */ import org.apache.http.conn.HttpHostConnectException;
/*     */ import org.apache.http.conn.ManagedHttpClientConnection;
/*     */ import org.apache.http.conn.SchemePortResolver;
/*     */ import org.apache.http.conn.UnsupportedSchemeException;
/*     */ import org.apache.http.conn.socket.ConnectionSocketFactory;
/*     */ import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @Immutable
/*     */ public class DefaultHttpClientConnectionOperator
/*     */   implements HttpClientConnectionOperator
/*     */ {
/*     */   static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";
/*  68 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final Lookup<ConnectionSocketFactory> socketFactoryRegistry;
/*     */   
/*     */   private final SchemePortResolver schemePortResolver;
/*     */   
/*     */   private final DnsResolver dnsResolver;
/*     */ 
/*     */   
/*     */   public DefaultHttpClientConnectionOperator(Lookup<ConnectionSocketFactory> socketFactoryRegistry, SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
/*  79 */     Args.notNull(socketFactoryRegistry, "Socket factory registry");
/*  80 */     this.socketFactoryRegistry = socketFactoryRegistry;
/*  81 */     this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : DefaultSchemePortResolver.INSTANCE;
/*     */     
/*  83 */     this.dnsResolver = (dnsResolver != null) ? dnsResolver : SystemDefaultDnsResolver.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Lookup<ConnectionSocketFactory> getSocketFactoryRegistry(HttpContext context) {
/*  89 */     Lookup<ConnectionSocketFactory> reg = (Lookup<ConnectionSocketFactory>)context.getAttribute("http.socket-factory-registry");
/*     */     
/*  91 */     if (reg == null) {
/*  92 */       reg = this.socketFactoryRegistry;
/*     */     }
/*  94 */     return reg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(ManagedHttpClientConnection conn, HttpHost host, InetSocketAddress localAddress, int connectTimeout, SocketConfig socketConfig, HttpContext context) throws IOException {
/* 105 */     Lookup<ConnectionSocketFactory> registry = getSocketFactoryRegistry(context);
/* 106 */     ConnectionSocketFactory sf = (ConnectionSocketFactory)registry.lookup(host.getSchemeName());
/* 107 */     if (sf == null) {
/* 108 */       throw new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported");
/*     */     }
/*     */     
/* 111 */     (new InetAddress[1])[0] = host.getAddress(); InetAddress[] addresses = (host.getAddress() != null) ? new InetAddress[1] : this.dnsResolver.resolve(host.getHostName());
/*     */     
/* 113 */     int port = this.schemePortResolver.resolve(host);
/* 114 */     for (int i = 0; i < addresses.length; i++) {
/* 115 */       InetAddress address = addresses[i];
/* 116 */       boolean last = (i == addresses.length - 1);
/*     */       
/* 118 */       Socket sock = sf.createSocket(context);
/* 119 */       sock.setSoTimeout(socketConfig.getSoTimeout());
/* 120 */       sock.setReuseAddress(socketConfig.isSoReuseAddress());
/* 121 */       sock.setTcpNoDelay(socketConfig.isTcpNoDelay());
/* 122 */       sock.setKeepAlive(socketConfig.isSoKeepAlive());
/* 123 */       if (socketConfig.getRcvBufSize() > 0) {
/* 124 */         sock.setReceiveBufferSize(socketConfig.getRcvBufSize());
/*     */       }
/* 126 */       if (socketConfig.getSndBufSize() > 0) {
/* 127 */         sock.setSendBufferSize(socketConfig.getSndBufSize());
/*     */       }
/*     */       
/* 130 */       int linger = socketConfig.getSoLinger();
/* 131 */       if (linger >= 0) {
/* 132 */         sock.setSoLinger(true, linger);
/*     */       }
/* 134 */       conn.bind(sock);
/*     */       
/* 136 */       InetSocketAddress remoteAddress = new InetSocketAddress(address, port);
/* 137 */       if (this.log.isDebugEnabled()) {
/* 138 */         this.log.debug("Connecting to " + remoteAddress);
/*     */       }
/*     */       try {
/* 141 */         sock = sf.connectSocket(connectTimeout, sock, host, remoteAddress, localAddress, context);
/*     */         
/* 143 */         conn.bind(sock);
/* 144 */         if (this.log.isDebugEnabled()) {
/* 145 */           this.log.debug("Connection established " + conn);
/*     */         }
/*     */         return;
/* 148 */       } catch (SocketTimeoutException ex) {
/* 149 */         if (last) {
/* 150 */           throw new ConnectTimeoutException(ex, host, addresses);
/*     */         }
/* 152 */       } catch (ConnectException ex) {
/* 153 */         if (last) {
/* 154 */           String msg = ex.getMessage();
/* 155 */           if ("Connection timed out".equals(msg)) {
/* 156 */             throw new ConnectTimeoutException(ex, host, addresses);
/*     */           }
/* 158 */           throw new HttpHostConnectException(ex, host, addresses);
/*     */         }
/*     */       
/* 161 */       } catch (NoRouteToHostException ex) {
/* 162 */         if (last) {
/* 163 */           throw ex;
/*     */         }
/*     */       } 
/* 166 */       if (this.log.isDebugEnabled()) {
/* 167 */         this.log.debug("Connect to " + remoteAddress + " timed out. " + "Connection will be retried using another IP address");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(ManagedHttpClientConnection conn, HttpHost host, HttpContext context) throws IOException {
/* 178 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 179 */     Lookup<ConnectionSocketFactory> registry = getSocketFactoryRegistry((HttpContext)clientContext);
/* 180 */     ConnectionSocketFactory sf = (ConnectionSocketFactory)registry.lookup(host.getSchemeName());
/* 181 */     if (sf == null) {
/* 182 */       throw new UnsupportedSchemeException(host.getSchemeName() + " protocol is not supported");
/*     */     }
/*     */     
/* 185 */     if (!(sf instanceof LayeredConnectionSocketFactory)) {
/* 186 */       throw new UnsupportedSchemeException(host.getSchemeName() + " protocol does not support connection upgrade");
/*     */     }
/*     */     
/* 189 */     LayeredConnectionSocketFactory lsf = (LayeredConnectionSocketFactory)sf;
/* 190 */     Socket sock = conn.getSocket();
/* 191 */     int port = this.schemePortResolver.resolve(host);
/* 192 */     sock = lsf.createLayeredSocket(sock, host.getHostName(), port, context);
/* 193 */     conn.bind(sock);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\DefaultHttpClientConnectionOperator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */