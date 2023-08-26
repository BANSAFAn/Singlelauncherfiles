/*    */ package org.apache.http.impl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.params.HttpParams;
/*    */ import org.apache.http.util.Args;
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
/*    */ @NotThreadSafe
/*    */ public class DefaultHttpClientConnection
/*    */   extends SocketHttpClientConnection
/*    */ {
/*    */   public void bind(Socket socket, HttpParams params) throws IOException {
/* 57 */     Args.notNull(socket, "Socket");
/* 58 */     Args.notNull(params, "HTTP parameters");
/* 59 */     assertNotOpen();
/* 60 */     socket.setTcpNoDelay(params.getBooleanParameter("http.tcp.nodelay", true));
/* 61 */     socket.setSoTimeout(params.getIntParameter("http.socket.timeout", 0));
/* 62 */     socket.setKeepAlive(params.getBooleanParameter("http.socket.keepalive", false));
/* 63 */     int linger = params.getIntParameter("http.socket.linger", -1);
/* 64 */     if (linger >= 0) {
/* 65 */       socket.setSoLinger((linger > 0), linger);
/*    */     }
/* 67 */     super.bind(socket, params);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\DefaultHttpClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */