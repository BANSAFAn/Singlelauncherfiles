/*     */ package org.apache.http.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class EofSensorInputStream
/*     */   extends InputStream
/*     */   implements ConnectionReleaseTrigger
/*     */ {
/*     */   protected InputStream wrappedStream;
/*     */   private boolean selfClosed;
/*     */   private final EofSensorWatcher eofWatcher;
/*     */   
/*     */   public EofSensorInputStream(InputStream in, EofSensorWatcher watcher) {
/*  84 */     Args.notNull(in, "Wrapped stream");
/*  85 */     this.wrappedStream = in;
/*  86 */     this.selfClosed = false;
/*  87 */     this.eofWatcher = watcher;
/*     */   }
/*     */   
/*     */   boolean isSelfClosed() {
/*  91 */     return this.selfClosed;
/*     */   }
/*     */   
/*     */   InputStream getWrappedStream() {
/*  95 */     return this.wrappedStream;
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
/*     */   protected boolean isReadAllowed() throws IOException {
/* 108 */     if (this.selfClosed) {
/* 109 */       throw new IOException("Attempted read on closed stream.");
/*     */     }
/* 111 */     return (this.wrappedStream != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 116 */     int l = -1;
/*     */     
/* 118 */     if (isReadAllowed()) {
/*     */       try {
/* 120 */         l = this.wrappedStream.read();
/* 121 */         checkEOF(l);
/* 122 */       } catch (IOException ex) {
/* 123 */         checkAbort();
/* 124 */         throw ex;
/*     */       } 
/*     */     }
/*     */     
/* 128 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 133 */     int l = -1;
/*     */     
/* 135 */     if (isReadAllowed()) {
/*     */       try {
/* 137 */         l = this.wrappedStream.read(b, off, len);
/* 138 */         checkEOF(l);
/* 139 */       } catch (IOException ex) {
/* 140 */         checkAbort();
/* 141 */         throw ex;
/*     */       } 
/*     */     }
/*     */     
/* 145 */     return l;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 150 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 155 */     int a = 0;
/*     */     
/* 157 */     if (isReadAllowed()) {
/*     */       try {
/* 159 */         a = this.wrappedStream.available();
/*     */       }
/* 161 */       catch (IOException ex) {
/* 162 */         checkAbort();
/* 163 */         throw ex;
/*     */       } 
/*     */     }
/*     */     
/* 167 */     return a;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 173 */     this.selfClosed = true;
/* 174 */     checkClose();
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
/*     */   protected void checkEOF(int eof) throws IOException {
/* 196 */     if (this.wrappedStream != null && eof < 0) {
/*     */       try {
/* 198 */         boolean scws = true;
/* 199 */         if (this.eofWatcher != null) {
/* 200 */           scws = this.eofWatcher.eofDetected(this.wrappedStream);
/*     */         }
/* 202 */         if (scws) {
/* 203 */           this.wrappedStream.close();
/*     */         }
/*     */       } finally {
/* 206 */         this.wrappedStream = null;
/*     */       } 
/*     */     }
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
/*     */   protected void checkClose() throws IOException {
/* 224 */     if (this.wrappedStream != null) {
/*     */       try {
/* 226 */         boolean scws = true;
/* 227 */         if (this.eofWatcher != null) {
/* 228 */           scws = this.eofWatcher.streamClosed(this.wrappedStream);
/*     */         }
/* 230 */         if (scws) {
/* 231 */           this.wrappedStream.close();
/*     */         }
/*     */       } finally {
/* 234 */         this.wrappedStream = null;
/*     */       } 
/*     */     }
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
/*     */   protected void checkAbort() throws IOException {
/* 254 */     if (this.wrappedStream != null) {
/*     */       try {
/* 256 */         boolean scws = true;
/* 257 */         if (this.eofWatcher != null) {
/* 258 */           scws = this.eofWatcher.streamAbort(this.wrappedStream);
/*     */         }
/* 260 */         if (scws) {
/* 261 */           this.wrappedStream.close();
/*     */         }
/*     */       } finally {
/* 264 */         this.wrappedStream = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void releaseConnection() throws IOException {
/* 274 */     close();
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
/*     */   public void abortConnection() throws IOException {
/* 287 */     this.selfClosed = true;
/* 288 */     checkAbort();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\EofSensorInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */