/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.SocketException;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.conn.EofSensorInputStream;
/*     */ import org.apache.http.conn.EofSensorWatcher;
/*     */ import org.apache.http.entity.HttpEntityWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class ResponseEntityProxy
/*     */   extends HttpEntityWrapper
/*     */   implements EofSensorWatcher
/*     */ {
/*     */   private final ConnectionHolder connHolder;
/*     */   
/*     */   public static void enchance(HttpResponse response, ConnectionHolder connHolder) {
/*  53 */     HttpEntity entity = response.getEntity();
/*  54 */     if (entity != null && entity.isStreaming() && connHolder != null) {
/*  55 */       response.setEntity((HttpEntity)new ResponseEntityProxy(entity, connHolder));
/*     */     }
/*     */   }
/*     */   
/*     */   ResponseEntityProxy(HttpEntity entity, ConnectionHolder connHolder) {
/*  60 */     super(entity);
/*  61 */     this.connHolder = connHolder;
/*     */   }
/*     */   
/*     */   private void cleanup() throws IOException {
/*  65 */     if (this.connHolder != null) {
/*  66 */       this.connHolder.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private void abortConnection() throws IOException {
/*  71 */     if (this.connHolder != null) {
/*  72 */       this.connHolder.abortConnection();
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseConnection() throws IOException {
/*  77 */     if (this.connHolder != null) {
/*  78 */       this.connHolder.releaseConnection();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  89 */     return (InputStream)new EofSensorInputStream(this.wrappedEntity.getContent(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void consumeContent() throws IOException {
/*  95 */     releaseConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outstream) throws IOException {
/*     */     try {
/* 101 */       this.wrappedEntity.writeTo(outstream);
/* 102 */       releaseConnection();
/* 103 */     } catch (IOException ex) {
/* 104 */       abortConnection();
/* 105 */       throw ex;
/* 106 */     } catch (RuntimeException ex) {
/* 107 */       abortConnection();
/* 108 */       throw ex;
/*     */     } finally {
/* 110 */       cleanup();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eofDetected(InputStream wrapped) throws IOException {
/*     */     try {
/* 119 */       wrapped.close();
/* 120 */       releaseConnection();
/* 121 */     } catch (IOException ex) {
/* 122 */       abortConnection();
/* 123 */       throw ex;
/* 124 */     } catch (RuntimeException ex) {
/* 125 */       abortConnection();
/* 126 */       throw ex;
/*     */     } finally {
/* 128 */       cleanup();
/*     */     } 
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean streamClosed(InputStream wrapped) throws IOException {
/*     */     try {
/* 136 */       boolean open = (this.connHolder != null && !this.connHolder.isReleased());
/*     */ 
/*     */       
/*     */       try {
/* 140 */         wrapped.close();
/* 141 */         releaseConnection();
/* 142 */       } catch (SocketException ex) {
/* 143 */         if (open) {
/* 144 */           throw ex;
/*     */         }
/*     */       } 
/* 147 */     } catch (IOException ex) {
/* 148 */       abortConnection();
/* 149 */       throw ex;
/* 150 */     } catch (RuntimeException ex) {
/* 151 */       abortConnection();
/* 152 */       throw ex;
/*     */     } finally {
/* 154 */       cleanup();
/*     */     } 
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean streamAbort(InputStream wrapped) throws IOException {
/* 161 */     cleanup();
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     StringBuilder sb = new StringBuilder("ResponseEntityProxy{");
/* 168 */     sb.append(this.wrappedEntity);
/* 169 */     sb.append('}');
/* 170 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\ResponseEntityProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */