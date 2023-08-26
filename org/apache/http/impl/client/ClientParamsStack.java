/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.params.AbstractHttpParams;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ClientParamsStack
/*     */   extends AbstractHttpParams
/*     */ {
/*     */   protected final HttpParams applicationParams;
/*     */   protected final HttpParams clientParams;
/*     */   protected final HttpParams requestParams;
/*     */   protected final HttpParams overrideParams;
/*     */   
/*     */   public ClientParamsStack(HttpParams aparams, HttpParams cparams, HttpParams rparams, HttpParams oparams) {
/* 101 */     this.applicationParams = aparams;
/* 102 */     this.clientParams = cparams;
/* 103 */     this.requestParams = rparams;
/* 104 */     this.overrideParams = oparams;
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
/*     */   public ClientParamsStack(ClientParamsStack stack) {
/* 116 */     this(stack.getApplicationParams(), stack.getClientParams(), stack.getRequestParams(), stack.getOverrideParams());
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
/*     */   public ClientParamsStack(ClientParamsStack stack, HttpParams aparams, HttpParams cparams, HttpParams rparams, HttpParams oparams) {
/* 139 */     this((aparams != null) ? aparams : stack.getApplicationParams(), (cparams != null) ? cparams : stack.getClientParams(), (rparams != null) ? rparams : stack.getRequestParams(), (oparams != null) ? oparams : stack.getOverrideParams());
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
/*     */   public final HttpParams getApplicationParams() {
/* 152 */     return this.applicationParams;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpParams getClientParams() {
/* 161 */     return this.clientParams;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpParams getRequestParams() {
/* 170 */     return this.requestParams;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpParams getOverrideParams() {
/* 179 */     return this.overrideParams;
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
/*     */   public Object getParameter(String name) {
/* 194 */     Args.notNull(name, "Parameter name");
/*     */     
/* 196 */     Object result = null;
/*     */     
/* 198 */     if (this.overrideParams != null) {
/* 199 */       result = this.overrideParams.getParameter(name);
/*     */     }
/* 201 */     if (result == null && this.requestParams != null) {
/* 202 */       result = this.requestParams.getParameter(name);
/*     */     }
/* 204 */     if (result == null && this.clientParams != null) {
/* 205 */       result = this.clientParams.getParameter(name);
/*     */     }
/* 207 */     if (result == null && this.applicationParams != null) {
/* 208 */       result = this.applicationParams.getParameter(name);
/*     */     }
/* 210 */     return result;
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
/*     */   public HttpParams setParameter(String name, Object value) throws UnsupportedOperationException {
/* 230 */     throw new UnsupportedOperationException("Setting parameters in a stack is not supported.");
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
/*     */   public boolean removeParameter(String name) {
/* 249 */     throw new UnsupportedOperationException("Removing parameters in a stack is not supported.");
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
/*     */   public HttpParams copy() {
/* 270 */     return (HttpParams)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\ClientParamsStack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */