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
/*    */ @Deprecated
/*    */ @NotThreadSafe
/*    */ public class DefaultHttpServerConnection
/*    */   extends SocketHttpServerConnection
/*    */ {
/*    */   public void bind(Socket socket, HttpParams params) throws IOException {
/* 55 */     Args.notNull(socket, "Socket");
/* 56 */     Args.notNull(params, "HTTP parameters");
/* 57 */     assertNotOpen();
/* 58 */     socket.setTcpNoDelay(params.getBooleanParameter("http.tcp.nodelay", true));
/* 59 */     socket.setSoTimeout(params.getIntParameter("http.socket.timeout", 0));
/* 60 */     socket.setKeepAlive(params.getBooleanParameter("http.socket.keepalive", false));
/* 61 */     int linger = params.getIntParameter("http.socket.linger", -1);
/* 62 */     if (linger >= 0) {
/* 63 */       socket.setSoLinger((linger > 0), linger);
/*    */     }
/* 65 */     if (linger >= 0) {
/* 66 */       socket.setSoLinger((linger > 0), linger);
/*    */     }
/* 68 */     super.bind(socket, params);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\DefaultHttpServerConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */