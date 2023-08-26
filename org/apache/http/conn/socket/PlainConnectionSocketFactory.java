/*    */ package org.apache.http.conn.socket;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ import org.apache.http.HttpHost;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.protocol.HttpContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class PlainConnectionSocketFactory
/*    */   implements ConnectionSocketFactory
/*    */ {
/* 46 */   public static final PlainConnectionSocketFactory INSTANCE = new PlainConnectionSocketFactory();
/*    */   
/*    */   public static PlainConnectionSocketFactory getSocketFactory() {
/* 49 */     return INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket createSocket(HttpContext context) throws IOException {
/* 58 */     return new Socket();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context) throws IOException {
/* 69 */     Socket sock = (socket != null) ? socket : createSocket(context);
/* 70 */     if (localAddress != null) {
/* 71 */       sock.bind(localAddress);
/*    */     }
/*    */     try {
/* 74 */       sock.connect(remoteAddress, connectTimeout);
/* 75 */     } catch (IOException ex) {
/*    */       try {
/* 77 */         sock.close();
/* 78 */       } catch (IOException ignore) {}
/*    */       
/* 80 */       throw ex;
/*    */     } 
/* 82 */     return sock;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\socket\PlainConnectionSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */