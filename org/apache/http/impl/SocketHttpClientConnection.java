/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import org.apache.http.HttpInetConnection;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.impl.io.SocketInputBuffer;
/*     */ import org.apache.http.impl.io.SocketOutputBuffer;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.io.SessionOutputBuffer;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public class SocketHttpClientConnection
/*     */   extends AbstractHttpClientConnection
/*     */   implements HttpInetConnection
/*     */ {
/*     */   private volatile boolean open;
/*  63 */   private volatile Socket socket = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertNotOpen() {
/*  70 */     Asserts.check(!this.open, "Connection is already open");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void assertOpen() {
/*  75 */     Asserts.check(this.open, "Connection is not open");
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
/*     */   protected SessionInputBuffer createSessionInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/*  97 */     return (SessionInputBuffer)new SocketInputBuffer(socket, buffersize, params);
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
/*     */   protected SessionOutputBuffer createSessionOutputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/* 119 */     return (SessionOutputBuffer)new SocketOutputBuffer(socket, buffersize, params);
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
/*     */   
/*     */   protected void bind(Socket socket, HttpParams params) throws IOException {
/* 143 */     Args.notNull(socket, "Socket");
/* 144 */     Args.notNull(params, "HTTP parameters");
/* 145 */     this.socket = socket;
/*     */     
/* 147 */     int buffersize = params.getIntParameter("http.socket.buffer-size", -1);
/* 148 */     init(createSessionInputBuffer(socket, buffersize, params), createSessionOutputBuffer(socket, buffersize, params), params);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     this.open = true;
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 157 */     return this.open;
/*     */   }
/*     */   
/*     */   protected Socket getSocket() {
/* 161 */     return this.socket;
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 165 */     if (this.socket != null) {
/* 166 */       return this.socket.getLocalAddress();
/*     */     }
/* 168 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 173 */     if (this.socket != null) {
/* 174 */       return this.socket.getLocalPort();
/*     */     }
/* 176 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 181 */     if (this.socket != null) {
/* 182 */       return this.socket.getInetAddress();
/*     */     }
/* 184 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 189 */     if (this.socket != null) {
/* 190 */       return this.socket.getPort();
/*     */     }
/* 192 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 197 */     assertOpen();
/* 198 */     if (this.socket != null) {
/*     */       try {
/* 200 */         this.socket.setSoTimeout(timeout);
/* 201 */       } catch (SocketException ignore) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 210 */     if (this.socket != null) {
/*     */       try {
/* 212 */         return this.socket.getSoTimeout();
/* 213 */       } catch (SocketException ignore) {
/* 214 */         return -1;
/*     */       } 
/*     */     }
/* 217 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 222 */     this.open = false;
/* 223 */     Socket tmpsocket = this.socket;
/* 224 */     if (tmpsocket != null) {
/* 225 */       tmpsocket.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 230 */     if (!this.open) {
/*     */       return;
/*     */     }
/* 233 */     this.open = false;
/* 234 */     Socket sock = this.socket;
/*     */     try {
/* 236 */       doFlush();
/*     */       try {
/*     */         try {
/* 239 */           sock.shutdownOutput();
/* 240 */         } catch (IOException ignore) {}
/*     */         
/*     */         try {
/* 243 */           sock.shutdownInput();
/* 244 */         } catch (IOException ignore) {}
/*     */       }
/* 246 */       catch (UnsupportedOperationException ignore) {}
/*     */     }
/*     */     finally {
/*     */       
/* 250 */       sock.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void formatAddress(StringBuilder buffer, SocketAddress socketAddress) {
/* 255 */     if (socketAddress instanceof InetSocketAddress) {
/* 256 */       InetSocketAddress addr = (InetSocketAddress)socketAddress;
/* 257 */       buffer.append((addr.getAddress() != null) ? addr.getAddress().getHostAddress() : addr.getAddress()).append(':').append(addr.getPort());
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 262 */       buffer.append(socketAddress);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 268 */     if (this.socket != null) {
/* 269 */       StringBuilder buffer = new StringBuilder();
/* 270 */       SocketAddress remoteAddress = this.socket.getRemoteSocketAddress();
/* 271 */       SocketAddress localAddress = this.socket.getLocalSocketAddress();
/* 272 */       if (remoteAddress != null && localAddress != null) {
/* 273 */         formatAddress(buffer, localAddress);
/* 274 */         buffer.append("<->");
/* 275 */         formatAddress(buffer, remoteAddress);
/*     */       } 
/* 277 */       return buffer.toString();
/*     */     } 
/* 279 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\SocketHttpClientConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */