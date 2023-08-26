/*     */ package org.apache.http.conn.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ConnectTimeoutException;
/*     */ import org.apache.http.conn.HttpInetSocketAddress;
/*     */ import org.apache.http.conn.scheme.HostNameResolver;
/*     */ import org.apache.http.conn.scheme.LayeredSchemeSocketFactory;
/*     */ import org.apache.http.conn.scheme.LayeredSocketFactory;
/*     */ import org.apache.http.conn.scheme.SchemeLayeredSocketFactory;
/*     */ import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
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
/*     */ public class SSLSocketFactory
/*     */   implements LayeredConnectionSocketFactory, SchemeLayeredSocketFactory, LayeredSchemeSocketFactory, LayeredSocketFactory
/*     */ {
/*     */   public static final String TLS = "TLS";
/*     */   public static final String SSL = "SSL";
/*     */   public static final String SSLV2 = "SSLv2";
/* 151 */   public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
/*     */ 
/*     */   
/* 154 */   public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new BrowserCompatHostnameVerifier();
/*     */ 
/*     */   
/* 157 */   public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = new StrictHostnameVerifier();
/*     */   
/*     */   private final javax.net.ssl.SSLSocketFactory socketfactory;
/*     */   
/*     */   private final HostNameResolver nameResolver;
/*     */   
/*     */   private volatile X509HostnameVerifier hostnameVerifier;
/*     */   private final String[] supportedProtocols;
/*     */   private final String[] supportedCipherSuites;
/*     */   
/*     */   public static SSLSocketFactory getSocketFactory() throws SSLInitializationException {
/* 168 */     return new SSLSocketFactory(SSLContexts.createDefault(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] split(String s) {
/* 174 */     if (TextUtils.isBlank(s)) {
/* 175 */       return null;
/*     */     }
/* 177 */     return s.split(" *, *");
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
/*     */   public static SSLSocketFactory getSystemSocketFactory() throws SSLInitializationException {
/* 190 */     return new SSLSocketFactory((javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
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
/*     */   public SSLSocketFactory(String algorithm, KeyStore keystore, String keyPassword, KeyStore truststore, SecureRandom random, HostNameResolver nameResolver) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 212 */     this(SSLContexts.custom().useProtocol(algorithm).setSecureRandom(random).loadKeyMaterial(keystore, (keyPassword != null) ? keyPassword.toCharArray() : null).loadTrustMaterial(truststore).build(), nameResolver);
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
/*     */   public SSLSocketFactory(String algorithm, KeyStore keystore, String keyPassword, KeyStore truststore, SecureRandom random, TrustStrategy trustStrategy, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 233 */     this(SSLContexts.custom().useProtocol(algorithm).setSecureRandom(random).loadKeyMaterial(keystore, (keyPassword != null) ? keyPassword.toCharArray() : null).loadTrustMaterial(truststore, trustStrategy).build(), hostnameVerifier);
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
/*     */   public SSLSocketFactory(String algorithm, KeyStore keystore, String keyPassword, KeyStore truststore, SecureRandom random, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 253 */     this(SSLContexts.custom().useProtocol(algorithm).setSecureRandom(random).loadKeyMaterial(keystore, (keyPassword != null) ? keyPassword.toCharArray() : null).loadTrustMaterial(truststore).build(), hostnameVerifier);
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
/*     */   public SSLSocketFactory(KeyStore keystore, String keystorePassword, KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 267 */     this(SSLContexts.custom().loadKeyMaterial(keystore, (keystorePassword != null) ? keystorePassword.toCharArray() : null).loadTrustMaterial(truststore).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(KeyStore keystore, String keystorePassword) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 278 */     this(SSLContexts.custom().loadKeyMaterial(keystore, (keystorePassword != null) ? keystorePassword.toCharArray() : null).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 287 */     this(SSLContexts.custom().loadTrustMaterial(truststore).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
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
/*     */   public SSLSocketFactory(TrustStrategy trustStrategy, X509HostnameVerifier hostnameVerifier) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 300 */     this(SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build(), hostnameVerifier);
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
/*     */   public SSLSocketFactory(TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
/* 312 */     this(SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build(), BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(SSLContext sslContext) {
/* 319 */     this(sslContext, BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(SSLContext sslContext, HostNameResolver nameResolver) {
/* 325 */     this.socketfactory = sslContext.getSocketFactory();
/* 326 */     this.hostnameVerifier = BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
/* 327 */     this.nameResolver = nameResolver;
/* 328 */     this.supportedProtocols = null;
/* 329 */     this.supportedCipherSuites = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(SSLContext sslContext, X509HostnameVerifier hostnameVerifier) {
/* 337 */     this(((SSLContext)Args.notNull(sslContext, "SSL context")).getSocketFactory(), (String[])null, (String[])null, hostnameVerifier);
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
/*     */   public SSLSocketFactory(SSLContext sslContext, String[] supportedProtocols, String[] supportedCipherSuites, X509HostnameVerifier hostnameVerifier) {
/* 349 */     this(((SSLContext)Args.notNull(sslContext, "SSL context")).getSocketFactory(), supportedProtocols, supportedCipherSuites, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(javax.net.ssl.SSLSocketFactory socketfactory, X509HostnameVerifier hostnameVerifier) {
/* 359 */     this(socketfactory, (String[])null, (String[])null, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLSocketFactory(javax.net.ssl.SSLSocketFactory socketfactory, String[] supportedProtocols, String[] supportedCipherSuites, X509HostnameVerifier hostnameVerifier) {
/* 370 */     this.socketfactory = (javax.net.ssl.SSLSocketFactory)Args.notNull(socketfactory, "SSL socket factory");
/* 371 */     this.supportedProtocols = supportedProtocols;
/* 372 */     this.supportedCipherSuites = supportedCipherSuites;
/* 373 */     this.hostnameVerifier = (hostnameVerifier != null) ? hostnameVerifier : BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
/* 374 */     this.nameResolver = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(HttpParams params) throws IOException {
/* 384 */     return createSocket((HttpContext)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket createSocket() throws IOException {
/* 389 */     return createSocket((HttpContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket connectSocket(Socket socket, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
/*     */     HttpHost host;
/* 401 */     Args.notNull(remoteAddress, "Remote address");
/* 402 */     Args.notNull(params, "HTTP parameters");
/*     */     
/* 404 */     if (remoteAddress instanceof HttpInetSocketAddress) {
/* 405 */       host = ((HttpInetSocketAddress)remoteAddress).getHttpHost();
/*     */     } else {
/* 407 */       host = new HttpHost(remoteAddress.getHostName(), remoteAddress.getPort(), "https");
/*     */     } 
/* 409 */     int socketTimeout = HttpConnectionParams.getSoTimeout(params);
/* 410 */     int connectTimeout = HttpConnectionParams.getConnectionTimeout(params);
/* 411 */     socket.setSoTimeout(socketTimeout);
/* 412 */     return connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, (HttpContext)null);
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
/*     */   public boolean isSecure(Socket sock) throws IllegalArgumentException {
/* 432 */     Args.notNull(sock, "Socket");
/* 433 */     Asserts.check(sock instanceof SSLSocket, "Socket not created by this factory");
/* 434 */     Asserts.check(!sock.isClosed(), "Socket is closed");
/* 435 */     return true;
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
/*     */   public Socket createLayeredSocket(Socket socket, String host, int port, HttpParams params) throws IOException, UnknownHostException {
/* 447 */     return createLayeredSocket(socket, host, port, (HttpContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createLayeredSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
/* 456 */     return createLayeredSocket(socket, host, port, (HttpContext)null);
/*     */   }
/*     */   
/*     */   public void setHostnameVerifier(X509HostnameVerifier hostnameVerifier) {
/* 460 */     Args.notNull(hostnameVerifier, "Hostname verifier");
/* 461 */     this.hostnameVerifier = hostnameVerifier;
/*     */   }
/*     */   
/*     */   public X509HostnameVerifier getHostnameVerifier() {
/* 465 */     return this.hostnameVerifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket connectSocket(Socket socket, String host, int port, InetAddress local, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
/*     */     InetAddress remote;
/* 475 */     if (this.nameResolver != null) {
/* 476 */       remote = this.nameResolver.resolve(host);
/*     */     } else {
/* 478 */       remote = InetAddress.getByName(host);
/*     */     } 
/* 480 */     InetSocketAddress localAddress = null;
/* 481 */     if (local != null || localPort > 0) {
/* 482 */       localAddress = new InetSocketAddress(local, (localPort > 0) ? localPort : 0);
/*     */     }
/* 484 */     HttpInetSocketAddress httpInetSocketAddress = new HttpInetSocketAddress(new HttpHost(host, port), remote, port);
/*     */     
/* 486 */     return connectSocket(socket, (InetSocketAddress)httpInetSocketAddress, localAddress, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
/* 494 */     return createLayeredSocket(socket, host, port, autoClose);
/*     */   }
/*     */ 
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
/*     */   private void internalPrepareSocket(SSLSocket socket) throws IOException {
/* 511 */     if (this.supportedProtocols != null) {
/* 512 */       socket.setEnabledProtocols(this.supportedProtocols);
/*     */     }
/* 514 */     if (this.supportedCipherSuites != null) {
/* 515 */       socket.setEnabledCipherSuites(this.supportedCipherSuites);
/*     */     }
/* 517 */     prepareSocket(socket);
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket createSocket(HttpContext context) throws IOException {
/* 522 */     SSLSocket sock = (SSLSocket)this.socketfactory.createSocket();
/* 523 */     internalPrepareSocket(sock);
/* 524 */     return sock;
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
/* 535 */     Args.notNull(host, "HTTP host");
/* 536 */     Args.notNull(remoteAddress, "Remote address");
/* 537 */     Socket sock = (socket != null) ? socket : createSocket(context);
/* 538 */     if (localAddress != null) {
/* 539 */       sock.bind(localAddress);
/*     */     }
/*     */     try {
/* 542 */       sock.connect(remoteAddress, connectTimeout);
/* 543 */     } catch (IOException ex) {
/*     */       try {
/* 545 */         sock.close();
/* 546 */       } catch (IOException ignore) {}
/*     */       
/* 548 */       throw ex;
/*     */     } 
/*     */     
/* 551 */     if (sock instanceof SSLSocket) {
/* 552 */       SSLSocket sslsock = (SSLSocket)sock;
/* 553 */       sslsock.startHandshake();
/* 554 */       verifyHostname(sslsock, host.getHostName());
/* 555 */       return sock;
/*     */     } 
/* 557 */     return createLayeredSocket(sock, host.getHostName(), remoteAddress.getPort(), context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket createLayeredSocket(Socket socket, String target, int port, HttpContext context) throws IOException {
/* 567 */     SSLSocket sslsock = (SSLSocket)this.socketfactory.createSocket(socket, target, port, true);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 572 */     internalPrepareSocket(sslsock);
/* 573 */     sslsock.startHandshake();
/* 574 */     verifyHostname(sslsock, target);
/* 575 */     return sslsock;
/*     */   }
/*     */   
/*     */   private void verifyHostname(SSLSocket sslsock, String hostname) throws IOException {
/*     */     try {
/* 580 */       this.hostnameVerifier.verify(hostname, sslsock);
/*     */     }
/* 582 */     catch (IOException iox) {
/*     */       
/* 584 */       try { sslsock.close(); } catch (Exception x) {}
/* 585 */       throw iox;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\ssl\SSLSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */