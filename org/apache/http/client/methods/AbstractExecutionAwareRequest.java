/*     */ package org.apache.http.client.methods;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.client.utils.CloneUtils;
/*     */ import org.apache.http.concurrent.Cancellable;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ConnectionReleaseTrigger;
/*     */ import org.apache.http.message.AbstractHttpMessage;
/*     */ import org.apache.http.message.HeaderGroup;
/*     */ import org.apache.http.params.HttpParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractExecutionAwareRequest
/*     */   extends AbstractHttpMessage
/*     */   implements HttpExecutionAware, AbortableHttpRequest, Cloneable, HttpRequest
/*     */ {
/*  49 */   private final AtomicBoolean aborted = new AtomicBoolean(false);
/*  50 */   private final AtomicReference<Cancellable> cancellableRef = new AtomicReference<Cancellable>(null);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setConnectionRequest(final ClientConnectionRequest connRequest) {
/*  56 */     setCancellable(new Cancellable()
/*     */         {
/*     */           public boolean cancel()
/*     */           {
/*  60 */             connRequest.abortRequest();
/*  61 */             return true;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setReleaseTrigger(final ConnectionReleaseTrigger releaseTrigger) {
/*  70 */     setCancellable(new Cancellable()
/*     */         {
/*     */           public boolean cancel()
/*     */           {
/*     */             try {
/*  75 */               releaseTrigger.abortConnection();
/*  76 */               return true;
/*  77 */             } catch (IOException ex) {
/*  78 */               return false;
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void abort() {
/*  87 */     if (this.aborted.compareAndSet(false, true)) {
/*  88 */       Cancellable cancellable = this.cancellableRef.getAndSet(null);
/*  89 */       if (cancellable != null) {
/*  90 */         cancellable.cancel();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAborted() {
/*  97 */     return this.aborted.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCancellable(Cancellable cancellable) {
/* 105 */     if (!this.aborted.get()) {
/* 106 */       this.cancellableRef.set(cancellable);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 112 */     AbstractExecutionAwareRequest clone = (AbstractExecutionAwareRequest)super.clone();
/* 113 */     clone.headergroup = (HeaderGroup)CloneUtils.cloneObject(this.headergroup);
/* 114 */     clone.params = (HttpParams)CloneUtils.cloneObject(this.params);
/* 115 */     return clone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void completed() {
/* 122 */     this.cancellableRef.set(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 131 */     Cancellable cancellable = this.cancellableRef.getAndSet(null);
/* 132 */     if (cancellable != null) {
/* 133 */       cancellable.cancel();
/*     */     }
/* 135 */     this.aborted.set(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\methods\AbstractExecutionAwareRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */