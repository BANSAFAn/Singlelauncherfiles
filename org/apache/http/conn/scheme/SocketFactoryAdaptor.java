/*    */ package org.apache.http.conn.scheme;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ import java.net.UnknownHostException;
/*    */ import org.apache.http.conn.ConnectTimeoutException;
/*    */ import org.apache.http.params.BasicHttpParams;
/*    */ import org.apache.http.params.HttpParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ class SocketFactoryAdaptor
/*    */   implements SocketFactory
/*    */ {
/*    */   private final SchemeSocketFactory factory;
/*    */   
/*    */   SocketFactoryAdaptor(SchemeSocketFactory factory) {
/* 47 */     this.factory = factory;
/*    */   }
/*    */ 
/*    */   
/*    */   public Socket createSocket() throws IOException {
/* 52 */     BasicHttpParams basicHttpParams = new BasicHttpParams();
/* 53 */     return this.factory.createSocket((HttpParams)basicHttpParams);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket connectSocket(Socket socket, String host, int port, InetAddress localAddress, int localPort, HttpParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
/* 62 */     InetSocketAddress local = null;
/* 63 */     if (localAddress != null || localPort > 0) {
/* 64 */       local = new InetSocketAddress(localAddress, (localPort > 0) ? localPort : 0);
/*    */     }
/* 66 */     InetAddress remoteAddress = InetAddress.getByName(host);
/* 67 */     InetSocketAddress remote = new InetSocketAddress(remoteAddress, port);
/* 68 */     return this.factory.connectSocket(socket, remote, local, params);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSecure(Socket socket) throws IllegalArgumentException {
/* 73 */     return this.factory.isSecure(socket);
/*    */   }
/*    */   
/*    */   public SchemeSocketFactory getFactory() {
/* 77 */     return this.factory;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 82 */     if (obj == null) {
/* 83 */       return false;
/*    */     }
/* 85 */     if (this == obj) {
/* 86 */       return true;
/*    */     }
/* 88 */     if (obj instanceof SocketFactoryAdaptor) {
/* 89 */       return this.factory.equals(((SocketFactoryAdaptor)obj).factory);
/*    */     }
/* 91 */     return this.factory.equals(obj);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 97 */     return this.factory.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\scheme\SocketFactoryAdaptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */