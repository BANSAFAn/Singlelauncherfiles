/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.BufferInfo;
/*     */ import org.apache.http.io.SessionInputBuffer;
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
/*     */ @NotThreadSafe
/*     */ public class ContentLengthInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int BUFFER_SIZE = 2048;
/*     */   private final long contentLength;
/*  66 */   private long pos = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean closed = false;
/*     */ 
/*     */ 
/*     */   
/*  74 */   private SessionInputBuffer in = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentLengthInputStream(SessionInputBuffer in, long contentLength) {
/*  86 */     this.in = (SessionInputBuffer)Args.notNull(in, "Session input buffer");
/*  87 */     this.contentLength = Args.notNegative(contentLength, "Content length");
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
/*     */   public void close() throws IOException {
/*  99 */     if (!this.closed) {
/*     */       try {
/* 101 */         if (this.pos < this.contentLength) {
/* 102 */           byte[] buffer = new byte[2048];
/* 103 */           while (read(buffer) >= 0);
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 109 */         this.closed = true;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 116 */     if (this.in instanceof BufferInfo) {
/* 117 */       int len = ((BufferInfo)this.in).length();
/* 118 */       return Math.min(len, (int)(this.contentLength - this.pos));
/*     */     } 
/* 120 */     return 0;
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
/*     */   public int read() throws IOException {
/* 132 */     if (this.closed) {
/* 133 */       throw new IOException("Attempted read from closed stream.");
/*     */     }
/*     */     
/* 136 */     if (this.pos >= this.contentLength) {
/* 137 */       return -1;
/*     */     }
/* 139 */     int b = this.in.read();
/* 140 */     if (b == -1) {
/* 141 */       if (this.pos < this.contentLength) {
/* 142 */         throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.pos);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 147 */       this.pos++;
/*     */     } 
/* 149 */     return b;
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
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 166 */     if (this.closed) {
/* 167 */       throw new IOException("Attempted read from closed stream.");
/*     */     }
/*     */     
/* 170 */     if (this.pos >= this.contentLength) {
/* 171 */       return -1;
/*     */     }
/*     */     
/* 174 */     int chunk = len;
/* 175 */     if (this.pos + len > this.contentLength) {
/* 176 */       chunk = (int)(this.contentLength - this.pos);
/*     */     }
/* 178 */     int count = this.in.read(b, off, chunk);
/* 179 */     if (count == -1 && this.pos < this.contentLength) {
/* 180 */       throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: " + this.contentLength + "; received: " + this.pos);
/*     */     }
/*     */ 
/*     */     
/* 184 */     if (count > 0) {
/* 185 */       this.pos += count;
/*     */     }
/* 187 */     return count;
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
/*     */   public int read(byte[] b) throws IOException {
/* 200 */     return read(b, 0, b.length);
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
/*     */   public long skip(long n) throws IOException {
/* 213 */     if (n <= 0L) {
/* 214 */       return 0L;
/*     */     }
/* 216 */     byte[] buffer = new byte[2048];
/*     */ 
/*     */     
/* 219 */     long remaining = Math.min(n, this.contentLength - this.pos);
/*     */     
/* 221 */     long count = 0L;
/* 222 */     while (remaining > 0L) {
/* 223 */       int l = read(buffer, 0, (int)Math.min(2048L, remaining));
/* 224 */       if (l == -1) {
/*     */         break;
/*     */       }
/* 227 */       count += l;
/* 228 */       remaining -= l;
/*     */     } 
/* 230 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\ContentLengthInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */