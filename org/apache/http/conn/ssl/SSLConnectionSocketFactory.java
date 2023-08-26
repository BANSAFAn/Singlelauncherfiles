/*     */ package org.apache.http.conn.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLHandshakeException;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
/*     */ import org.apache.http.conn.util.PublicSuffixMatcherLoader;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.ssl.SSLContexts;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.TextUtils;
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
/*     */ @ThreadSafe
/*     */ public class SSLConnectionSocketFactory
/*     */   implements LayeredConnectionSocketFactory
/*     */ {
/*     */   public static final String TLS = "TLS";
/*     */   public static final String SSL = "SSL";
/*     */   public static final String SSLV2 = "SSLv2";
/*     */   @Deprecated
/* 144 */   public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = AllowAllHostnameVerifier.INSTANCE;
/*     */ 
/*     */   
/*     */   @Deprecated
/* 148 */   public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = BrowserCompatHostnameVerifier.INSTANCE;
/*     */ 
/*     */   
/*     */   @Deprecated
/* 152 */   public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = StrictHostnameVerifier.INSTANCE;
/*     */ 
/*     */   
/* 155 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final SSLSocketFactory socketfactory;
/*     */   private final HostnameVerifier hostnameVerifier;
/*     */   
/*     */   public static HostnameVerifier getDefaultHostnameVerifier() {
/* 161 */     return new DefaultHostnameVerifier(PublicSuffixMatcherLoader.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final String[] supportedProtocols;
/*     */   
/*     */   private final String[] supportedCipherSuites;
/*     */ 
/*     */   
/*     */   public static SSLConnectionSocketFactory getSocketFactory() throws SSLInitializationException {
/* 172 */     return new SSLConnectionSocketFactory(SSLContexts.createDefault(), getDefaultHostnameVerifier());
/*     */   }
/*     */   
/*     */   private static String[] split(String s) {
/* 176 */     if (TextUtils.isBlank(s)) {
/* 177 */       return null;
/*     */     }
/* 179 */     return s.split(" *, *");
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
/*     */   public static SSLConnectionSocketFactory getSystemSocketFactory() throws SSLInitializationException {
/* 191 */     return new SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), getDefaultHostnameVerifier());
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
/*     */   public SSLConnectionSocketFactory(SSLContext sslContext) {
/* 204 */     this(sslContext, getDefaultHostnameVerifier());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SSLConnectionSocketFactory(SSLContext sslContext, X509HostnameVerifier hostnameVerifier) {
/* 214 */     this(((SSLContext)Args.notNull(sslContext, "SSL context")).getSocketFactory(), (String[])null, (String[])null, hostnameVerifier);
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
/*     */   @Deprecated
/*     */   public SSLConnectionSocketFactory(SSLContext sslContext, String[] supportedProtocols, String[] supportedCipherSuites, X509HostnameVerifier hostnameVerifier) {
/* 228 */     this(((SSLContext)Args.notNull(sslContext, "SSL context")).getSocketFactory(), supportedProtocols, supportedCipherSuites, hostnameVerifier);
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
/*     */   public SSLConnectionSocketFactory(SSLSocketFactory socketfactory, X509HostnameVerifier hostnameVerifier) {
/* 240 */     this(socketfactory, (String[])null, (String[])null, hostnameVerifier);
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
/*     */   @Deprecated
/*     */   public SSLConnectionSocketFactory(SSLSocketFactory socketfactory, String[] supportedProtocols, String[] supportedCipherSuites, X509HostnameVerifier hostnameVerifier) {
/* 253 */     this(socketfactory, supportedProtocols, supportedCipherSuites, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLConnectionSocketFactory(SSLContext sslContext, HostnameVerifier hostnameVerifier) {
/* 261 */     this(((SSLContext)Args.notNull(sslContext, "SSL context")).getSocketFactory(), (String[])null, (String[])null, hostnameVerifier);
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
/*     */   public SSLConnectionSocketFactory(SSLContext sslContext, String[] supportedProtocols, String[] supportedCipherSuites, HostnameVerifier hostnameVerifier) {
/* 273 */     this(((SSLContext)Args.notNull(sslContext, "SSL context")).getSocketFactory(), supportedProtocols, supportedCipherSuites, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLConnectionSocketFactory(SSLSocketFactory socketfactory, HostnameVerifier hostnameVerifier) {
/* 283 */     this(socketfactory, (String[])null, (String[])null, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLConnectionSocketFactory(SSLSocketFactory socketfactory, String[] supportedProtocols, String[] supportedCipherSuites, HostnameVerifier hostnameVerifier) {
/* 294 */     this.socketfactory = (SSLSocketFactory)Args.notNull(socketfactory, "SSL socket factory");
/* 295 */     this.supportedProtocols = supportedProtocols;
/* 296 */     this.supportedCipherSuites = supportedCipherSuites;
/* 297 */     this.hostnameVerifier = (hostnameVerifier != null) ? hostnameVerifier : getDefaultHostnameVerifier();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void prepareSocket(SSLSocket socket) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(HttpContext context) throws IOException {
/* 313 */     return SocketFactory.getDefault().createSocket();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
/* 324 */     Args.notNull(host, "HTTP host");
/* 325 */     Args.notNull(remoteAddress, "Remote address");
/* 326 */     Socket sock = (socket != null) ? socket : createSocket(context);
/* 327 */     if (localAddress != null) {
/* 328 */       sock.bind(localAddress);
/*     */     }
/*     */     try {
/* 331 */       if (connectTimeout > 0 && sock.getSoTimeout() == 0) {
/* 332 */         sock.setSoTimeout(connectTimeout);
/*     */       }
/* 334 */       if (this.log.isDebugEnabled()) {
/* 335 */         this.log.debug("Connecting socket to " + remoteAddress + " with timeout " + connectTimeout);
/*     */       }
/* 337 */       sock.connect(remoteAddress, connectTimeout);
/* 338 */     } catch (IOException ex) {
/*     */       try {
/* 340 */         sock.close();
/* 341 */       } catch (IOException ignore) {}
/*     */       
/* 343 */       throw ex;
/*     */     } 
/*     */     
/* 346 */     if (sock instanceof SSLSocket) {
/* 347 */       SSLSocket sslsock = (SSLSocket)sock;
/* 348 */       this.log.debug("Starting handshake");
/* 349 */       sslsock.startHandshake();
/* 350 */       verifyHostname(sslsock, host.getHostName());
/* 351 */       return sock;
/*     */     } 
/* 353 */     return createLayeredSocket(sock, host.getHostName(), remoteAddress.getPort(), context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createLayeredSocket(Socket socket, String target, int port, HttpContext context) throws IOException {
/* 363 */     SSLSocket sslsock = (SSLSocket)this.socketfactory.createSocket(socket, target, port, true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 368 */     if (this.supportedProtocols != null) {
/* 369 */       sslsock.setEnabledProtocols(this.supportedProtocols);
/*     */     } else {
/*     */       
/* 372 */       String[] allProtocols = sslsock.getEnabledProtocols();
/* 373 */       List<String> enabledProtocols = new ArrayList<String>(allProtocols.length);
/* 374 */       for (String protocol : allProtocols) {
/* 375 */         if (!protocol.startsWith("SSL")) {
/* 376 */           enabledProtocols.add(protocol);
/*     */         }
/*     */       } 
/* 379 */       if (!enabledProtocols.isEmpty()) {
/* 380 */         sslsock.setEnabledProtocols(enabledProtocols.<String>toArray(new String[enabledProtocols.size()]));
/*     */       }
/*     */     } 
/* 383 */     if (this.supportedCipherSuites != null) {
/* 384 */       sslsock.setEnabledCipherSuites(this.supportedCipherSuites);
/*     */     }
/*     */     
/* 387 */     if (this.log.isDebugEnabled()) {
/* 388 */       this.log.debug("Enabled protocols: " + Arrays.<String>asList(sslsock.getEnabledProtocols()));
/* 389 */       this.log.debug("Enabled cipher suites:" + Arrays.<String>asList(sslsock.getEnabledCipherSuites()));
/*     */     } 
/*     */     
/* 392 */     prepareSocket(sslsock);
/* 393 */     this.log.debug("Starting handshake");
/* 394 */     sslsock.startHandshake();
/* 395 */     verifyHostname(sslsock, target);
/* 396 */     return sslsock;
/*     */   }
/*     */   
/*     */   private void verifyHostname(SSLSocket sslsock, String hostname) throws IOException {
/*     */     try {
/* 401 */       SSLSession session = sslsock.getSession();
/* 402 */       if (session == null) {
/*     */ 
/*     */ 
/*     */         
/* 406 */         InputStream in = sslsock.getInputStream();
/* 407 */         in.available();
/*     */ 
/*     */         
/* 410 */         session = sslsock.getSession();
/* 411 */         if (session == null) {
/*     */ 
/*     */           
/* 414 */           sslsock.startHandshake();
/* 415 */           session = sslsock.getSession();
/*     */         } 
/*     */       } 
/* 418 */       if (session == null) {
/* 419 */         throw new SSLHandshakeException("SSL session not available");
/*     */       }
/*     */       
/* 422 */       if (this.log.isDebugEnabled()) {
/* 423 */         this.log.debug("Secure session established");
/* 424 */         this.log.debug(" negotiated protocol: " + session.getProtocol());
/* 425 */         this.log.debug(" negotiated cipher suite: " + session.getCipherSuite());
/*     */ 
/*     */         
/*     */         try {
/* 429 */           Certificate[] certs = session.getPeerCertificates();
/* 430 */           X509Certificate x509 = (X509Certificate)certs[0];
/* 431 */           X500Principal peer = x509.getSubjectX500Principal();
/*     */           
/* 433 */           this.log.debug(" peer principal: " + peer.toString());
/* 434 */           Collection<List<?>> altNames1 = x509.getSubjectAlternativeNames();
/* 435 */           if (altNames1 != null) {
/* 436 */             List<String> altNames = new ArrayList<String>();
/* 437 */             for (List<?> aC : altNames1) {
/* 438 */               if (!aC.isEmpty()) {
/* 439 */                 altNames.add((String)aC.get(1));
/*     */               }
/*     */             } 
/* 442 */             this.log.debug(" peer alternative names: " + altNames);
/*     */           } 
/*     */           
/* 445 */           X500Principal issuer = x509.getIssuerX500Principal();
/* 446 */           this.log.debug(" issuer principal: " + issuer.toString());
/* 447 */           Collection<List<?>> altNames2 = x509.getIssuerAlternativeNames();
/* 448 */           if (altNames2 != null) {
/* 449 */             List<String> altNames = new ArrayList<String>();
/* 450 */             for (List<?> aC : altNames2) {
/* 451 */               if (!aC.isEmpty()) {
/* 452 */                 altNames.add((String)aC.get(1));
/*     */               }
/*     */             } 
/* 455 */             this.log.debug(" issuer alternative names: " + altNames);
/*     */           } 
/* 457 */         } catch (Exception ignore) {}
/*     */       } 
/*     */ 
/*     */       
/* 461 */       if (!this.hostnameVerifier.verify(hostname, session)) {
/* 462 */         Certificate[] certs = session.getPeerCertificates();
/* 463 */         X509Certificate x509 = (X509Certificate)certs[0];
/* 464 */         X500Principal x500Principal = x509.getSubjectX500Principal();
/* 465 */         throw new SSLPeerUnverifiedException("Host name '" + hostname + "' does not match " + "the certificate subject provided by the peer (" + x500Principal.toString() + ")");
/*     */       }
/*     */     
/*     */     }
/* 469 */     catch (IOException iox) {
/*     */       
/* 471 */       try { sslsock.close(); } catch (Exception x) {}
/* 472 */       throw iox;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\ssl\SSLConnectionSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */