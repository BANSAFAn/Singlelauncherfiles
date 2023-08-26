/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.ConnectTimeoutException;
/*     */ import org.apache.http.conn.DnsResolver;
/*     */ import org.apache.http.conn.HttpInetSocketAddress;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.conn.scheme.SchemeSocketFactory;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @ThreadSafe
/*     */ public class DefaultClientConnectionOperator
/*     */   implements ClientConnectionOperator
/*     */ {
/*  92 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final DnsResolver dnsResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultClientConnectionOperator(SchemeRegistry schemes) {
/* 108 */     Args.notNull(schemes, "Scheme registry");
/* 109 */     this.schemeRegistry = schemes;
/* 110 */     this.dnsResolver = new SystemDefaultDnsResolver();
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
/*     */   public DefaultClientConnectionOperator(SchemeRegistry schemes, DnsResolver dnsResolver) {
/* 123 */     Args.notNull(schemes, "Scheme registry");
/*     */     
/* 125 */     Args.notNull(dnsResolver, "DNS resolver");
/*     */     
/* 127 */     this.schemeRegistry = schemes;
/* 128 */     this.dnsResolver = dnsResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public OperatedClientConnection createConnection() {
/* 133 */     return new DefaultClientConnection();
/*     */   }
/*     */   
/*     */   private SchemeRegistry getSchemeRegistry(HttpContext context) {
/* 137 */     SchemeRegistry reg = (SchemeRegistry)context.getAttribute("http.scheme-registry");
/*     */     
/* 139 */     if (reg == null) {
/* 140 */       reg = this.schemeRegistry;
/*     */     }
/* 142 */     return reg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void openConnection(OperatedClientConnection conn, HttpHost target, InetAddress local, HttpContext context, HttpParams params) throws IOException {
/* 152 */     Args.notNull(conn, "Connection");
/* 153 */     Args.notNull(target, "Target host");
/* 154 */     Args.notNull(params, "HTTP parameters");
/* 155 */     Asserts.check(!conn.isOpen(), "Connection must not be open");
/*     */     
/* 157 */     SchemeRegistry registry = getSchemeRegistry(context);
/* 158 */     Scheme schm = registry.getScheme(target.getSchemeName());
/* 159 */     SchemeSocketFactory sf = schm.getSchemeSocketFactory();
/*     */     
/* 161 */     InetAddress[] addresses = resolveHostname(target.getHostName());
/* 162 */     int port = schm.resolvePort(target.getPort());
/* 163 */     for (int i = 0; i < addresses.length; i++) {
/* 164 */       InetAddress address = addresses[i];
/* 165 */       boolean last = (i == addresses.length - 1);
/*     */       
/* 167 */       Socket sock = sf.createSocket(params);
/* 168 */       conn.opening(sock, target);
/*     */       
/* 170 */       HttpInetSocketAddress httpInetSocketAddress = new HttpInetSocketAddress(target, address, port);
/* 171 */       InetSocketAddress localAddress = null;
/* 172 */       if (local != null) {
/* 173 */         localAddress = new InetSocketAddress(local, 0);
/*     */       }
/* 175 */       if (this.log.isDebugEnabled()) {
/* 176 */         this.log.debug("Connecting to " + httpInetSocketAddress);
/*     */       }
/*     */       try {
/* 179 */         Socket connsock = sf.connectSocket(sock, (InetSocketAddress)httpInetSocketAddress, localAddress, params);
/* 180 */         if (sock != connsock) {
/* 181 */           sock = connsock;
/* 182 */           conn.opening(sock, target);
/*     */         } 
/* 184 */         prepareSocket(sock, context, params);
/* 185 */         conn.openCompleted(sf.isSecure(sock), params);
/*     */         return;
/* 187 */       } catch (ConnectException ex) {
/* 188 */         if (last) {
/* 189 */           throw ex;
/*     */         }
/* 191 */       } catch (ConnectTimeoutException ex) {
/* 192 */         if (last) {
/* 193 */           throw ex;
/*     */         }
/*     */       } 
/* 196 */       if (this.log.isDebugEnabled()) {
/* 197 */         this.log.debug("Connect to " + httpInetSocketAddress + " timed out. " + "Connection will be retried using another IP address");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateSecureConnection(OperatedClientConnection conn, HttpHost target, HttpContext context, HttpParams params) throws IOException {
/* 209 */     Args.notNull(conn, "Connection");
/* 210 */     Args.notNull(target, "Target host");
/* 211 */     Args.notNull(params, "Parameters");
/* 212 */     Asserts.check(conn.isOpen(), "Connection must be open");
/*     */     
/* 214 */     SchemeRegistry registry = getSchemeRegistry(context);
/* 215 */     Scheme schm = registry.getScheme(target.getSchemeName());
/* 216 */     Asserts.check(schm.getSchemeSocketFactory() instanceof SchemeLayeredSocketFactory, "Socket factory must implement SchemeLayeredSocketFactory");
/*     */     
/* 218 */     SchemeLayeredSocketFactory lsf = (SchemeLayeredSocketFactory)schm.getSchemeSocketFactory();
/* 219 */     Socket sock = lsf.createLayeredSocket(conn.getSocket(), target.getHostName(), schm.resolvePort(target.getPort()), params);
/*     */     
/* 221 */     prepareSocket(sock, context, params);
/* 222 */     conn.update(sock, target, lsf.isSecure(sock), params);
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
/*     */   protected void prepareSocket(Socket sock, HttpContext context, HttpParams params) throws IOException {
/* 238 */     sock.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(params));
/* 239 */     sock.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
/*     */     
/* 241 */     int linger = HttpConnectionParams.getLinger(params);
/* 242 */     if (linger >= 0) {
/* 243 */       sock.setSoLinger((linger > 0), linger);
/*     */     }
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
/*     */   protected InetAddress[] resolveHostname(String host) throws UnknownHostException {
/* 262 */     return this.dnsResolver.resolve(host);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\DefaultClientConnectionOperator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */