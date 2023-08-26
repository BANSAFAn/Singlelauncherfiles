/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.EofSensor;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class SocketInputBuffer
/*     */   extends AbstractSessionInputBuffer
/*     */   implements EofSensor
/*     */ {
/*     */   private final Socket socket;
/*     */   private boolean eof;
/*     */   
/*     */   public SocketInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
/*  69 */     Args.notNull(socket, "Socket");
/*  70 */     this.socket = socket;
/*  71 */     this.eof = false;
/*  72 */     int n = buffersize;
/*  73 */     if (n < 0) {
/*  74 */       n = socket.getReceiveBufferSize();
/*     */     }
/*  76 */     if (n < 1024) {
/*  77 */       n = 1024;
/*     */     }
/*  79 */     init(socket.getInputStream(), n, params);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int fillBuffer() throws IOException {
/*  84 */     int i = super.fillBuffer();
/*  85 */     this.eof = (i == -1);
/*  86 */     return i;
/*     */   }
/*     */   
/*     */   public boolean isDataAvailable(int timeout) throws IOException {
/*  90 */     boolean result = hasBufferedData();
/*  91 */     if (!result) {
/*  92 */       int oldtimeout = this.socket.getSoTimeout();
/*     */       try {
/*  94 */         this.socket.setSoTimeout(timeout);
/*  95 */         fillBuffer();
/*  96 */         result = hasBufferedData();
/*     */       } finally {
/*  98 */         this.socket.setSoTimeout(oldtimeout);
/*     */       } 
/*     */     } 
/* 101 */     return result;
/*     */   }
/*     */   
/*     */   public boolean isEof() {
/* 105 */     return this.eof;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\SocketInputBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */