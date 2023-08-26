/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import org.apache.http.HttpInetConnection;
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
/*     */ @Deprecated
/*     */ public class SocketHttpServerConnection
/*     */   extends AbstractHttpServerConnection
/*     */   implements HttpInetConnection
/*     */ {
/*     */   private volatile boolean open;
/*  52 */   private volatile Socket socket = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void assertNotOpen() {
/*  59 */     Asserts.check(!this.open, "Connection is already open");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void assertOpen() {
/*  64 */     Asserts.check(this.open, "Connection is not open");
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
/*  86 */     return (SessionInputBuffer)new SocketInputBuffer(socket, buffersize, params);
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
/* 108 */     return (SessionOutputBuffer)new SocketOutputBuffer(socket, buffersize, params);
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
/*     */   protected void bind(Socket socket, HttpParams params) throws IOException {
/* 130 */     Args.notNull(socket, "Socket");
/* 131 */     Args.notNull(params, "HTTP parameters");
/* 132 */     this.socket = socket;
/*     */     
/* 134 */     int buffersize = params.getIntParameter("http.socket.buffer-size", -1);
/* 135 */     init(createSessionInputBuffer(socket, buffersize, params), createSessionOutputBuffer(socket, buffersize, params), params);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     this.open = true;
/*     */   }
/*     */   
/*     */   protected Socket getSocket() {
/* 144 */     return this.socket;
/*     */   }
/*     */   
/*     */   public boolean isOpen() {
/* 148 */     return this.open;
/*     */   }
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 152 */     if (this.socket != null) {
/* 153 */       return this.socket.getLocalAddress();
/*     */     }
/* 155 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLocalPort() {
/* 160 */     if (this.socket != null) {
/* 161 */       return this.socket.getLocalPort();
/*     */     }
/* 163 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public InetAddress getRemoteAddress() {
/* 168 */     if (this.socket != null) {
/* 169 */       return this.socket.getInetAddress();
/*     */     }
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemotePort() {
/* 176 */     if (this.socket != null) {
/* 177 */       return this.socket.getPort();
/*     */     }
/* 179 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSocketTimeout(int timeout) {
/* 184 */     assertOpen();
/* 185 */     if (this.socket != null) {
/*     */       try {
/* 187 */         this.socket.setSoTimeout(timeout);
/* 188 */       } catch (SocketException ignore) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSocketTimeout() {
/* 197 */     if (this.socket != null) {
/*     */       try {
/* 199 */         return this.socket.getSoTimeout();
/* 200 */       } catch (SocketException ignore) {
/* 201 */         return -1;
/*     */       } 
/*     */     }
/* 204 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() throws IOException {
/* 209 */     this.open = false;
/* 210 */     Socket tmpsocket = this.socket;
/* 211 */     if (tmpsocket != null) {
/* 212 */       tmpsocket.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 217 */     if (!this.open) {
/*     */       return;
/*     */     }
/* 220 */     this.open = false;
/* 221 */     this.open = false;
/* 222 */     Socket sock = this.socket;
/*     */     try {
/* 224 */       doFlush();
/*     */       try {
/*     */         try {
/* 227 */           sock.shutdownOutput();
/* 228 */         } catch (IOException ignore) {}
/*     */         
/*     */         try {
/* 231 */           sock.shutdownInput();
/* 232 */         } catch (IOException ignore) {}
/*     */       }
/* 234 */       catch (UnsupportedOperationException ignore) {}
/*     */     }
/*     */     finally {
/*     */       
/* 238 */       sock.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void formatAddress(StringBuilder buffer, SocketAddress socketAddress) {
/* 243 */     if (socketAddress instanceof InetSocketAddress) {
/* 244 */       InetSocketAddress addr = (InetSocketAddress)socketAddress;
/* 245 */       buffer.append((addr.getAddress() != null) ? addr.getAddress().getHostAddress() : addr.getAddress()).append(':').append(addr.getPort());
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 250 */       buffer.append(socketAddress);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 256 */     if (this.socket != null) {
/* 257 */       StringBuilder buffer = new StringBuilder();
/* 258 */       SocketAddress remoteAddress = this.socket.getRemoteSocketAddress();
/* 259 */       SocketAddress localAddress = this.socket.getLocalSocketAddress();
/* 260 */       if (remoteAddress != null && localAddress != null) {
/* 261 */         formatAddress(buffer, localAddress);
/* 262 */         buffer.append("<->");
/* 263 */         formatAddress(buffer, remoteAddress);
/*     */       } 
/* 265 */       return buffer.toString();
/*     */     } 
/* 267 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\SocketHttpServerConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */