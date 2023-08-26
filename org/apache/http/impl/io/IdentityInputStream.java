/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.io.BufferInfo;
/*    */ import org.apache.http.io.SessionInputBuffer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @NotThreadSafe
/*    */ public class IdentityInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private final SessionInputBuffer in;
/*    */   private boolean closed = false;
/*    */   
/*    */   public IdentityInputStream(SessionInputBuffer in) {
/* 64 */     this.in = (SessionInputBuffer)Args.notNull(in, "Session input buffer");
/*    */   }
/*    */ 
/*    */   
/*    */   public int available() throws IOException {
/* 69 */     if (this.in instanceof BufferInfo) {
/* 70 */       return ((BufferInfo)this.in).length();
/*    */     }
/* 72 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 78 */     this.closed = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 83 */     if (this.closed) {
/* 84 */       return -1;
/*    */     }
/* 86 */     return this.in.read();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 92 */     if (this.closed) {
/* 93 */       return -1;
/*    */     }
/* 95 */     return this.in.read(b, off, len);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\io\IdentityInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */