/*     */ package org.apache.http.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.SocketException;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.entity.HttpEntityWrapper;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicManagedEntity
/*     */   extends HttpEntityWrapper
/*     */   implements ConnectionReleaseTrigger, EofSensorWatcher
/*     */ {
/*     */   protected ManagedClientConnection managedConn;
/*     */   protected final boolean attemptReuse;
/*     */   
/*     */   public BasicManagedEntity(HttpEntity entity, ManagedClientConnection conn, boolean reuse) {
/*  74 */     super(entity);
/*  75 */     Args.notNull(conn, "Connection");
/*  76 */     this.managedConn = conn;
/*  77 */     this.attemptReuse = reuse;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  87 */     return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
/*     */   }
/*     */   
/*     */   private void ensureConsumed() throws IOException {
/*  91 */     if (this.managedConn == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/*  96 */       if (this.attemptReuse) {
/*     */         
/*  98 */         EntityUtils.consume(this.wrappedEntity);
/*  99 */         this.managedConn.markReusable();
/*     */       } else {
/* 101 */         this.managedConn.unmarkReusable();
/*     */       } 
/*     */     } finally {
/* 104 */       releaseManagedConnection();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void consumeContent() throws IOException {
/* 114 */     ensureConsumed();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/* 119 */     super.writeTo(outstream);
/* 120 */     ensureConsumed();
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseConnection() throws IOException {
/* 125 */     ensureConsumed();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void abortConnection() throws IOException {
/* 131 */     if (this.managedConn != null) {
/*     */       try {
/* 133 */         this.managedConn.abortConnection();
/*     */       } finally {
/* 135 */         this.managedConn = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean eofDetected(InputStream wrapped) throws IOException {
/*     */     try {
/* 143 */       if (this.managedConn != null) {
/* 144 */         if (this.attemptReuse) {
/*     */ 
/*     */           
/* 147 */           wrapped.close();
/* 148 */           this.managedConn.markReusable();
/*     */         } else {
/* 150 */           this.managedConn.unmarkReusable();
/*     */         } 
/*     */       }
/*     */     } finally {
/* 154 */       releaseManagedConnection();
/*     */     } 
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean streamClosed(InputStream wrapped) throws IOException {
/*     */     try {
/* 162 */       if (this.managedConn != null) {
/* 163 */         if (this.attemptReuse) {
/* 164 */           boolean valid = this.managedConn.isOpen();
/*     */ 
/*     */           
/*     */           try {
/* 168 */             wrapped.close();
/* 169 */             this.managedConn.markReusable();
/* 170 */           } catch (SocketException ex) {
/* 171 */             if (valid) {
/* 172 */               throw ex;
/*     */             }
/*     */           } 
/*     */         } else {
/* 176 */           this.managedConn.unmarkReusable();
/*     */         } 
/*     */       }
/*     */     } finally {
/* 180 */       releaseManagedConnection();
/*     */     } 
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean streamAbort(InputStream wrapped) throws IOException {
/* 187 */     if (this.managedConn != null) {
/* 188 */       this.managedConn.abortConnection();
/*     */     }
/* 190 */     return false;
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
/*     */   protected void releaseManagedConnection() throws IOException {
/* 204 */     if (this.managedConn != null)
/*     */       try {
/* 206 */         this.managedConn.releaseConnection();
/*     */       } finally {
/* 208 */         this.managedConn = null;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\BasicManagedEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */