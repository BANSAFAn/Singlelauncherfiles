/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
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
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ class CloseableHttpResponseProxy
/*     */   implements InvocationHandler
/*     */ {
/*     */   private static final Constructor<?> CONSTRUCTOR;
/*     */   private final HttpResponse original;
/*     */   
/*     */   static {
/*     */     try {
/*  54 */       CONSTRUCTOR = Proxy.getProxyClass(CloseableHttpResponseProxy.class.getClassLoader(), new Class[] { CloseableHttpResponse.class }).getConstructor(new Class[] { InvocationHandler.class });
/*     */     }
/*  56 */     catch (NoSuchMethodException ex) {
/*  57 */       throw new IllegalStateException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CloseableHttpResponseProxy(HttpResponse original) {
/*  65 */     this.original = original;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  69 */     HttpEntity entity = this.original.getEntity();
/*  70 */     EntityUtils.consume(entity);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*  76 */     String mname = method.getName();
/*  77 */     if (mname.equals("close")) {
/*  78 */       close();
/*  79 */       return null;
/*     */     } 
/*     */     try {
/*  82 */       return method.invoke(this.original, args);
/*  83 */     } catch (InvocationTargetException ex) {
/*  84 */       Throwable cause = ex.getCause();
/*  85 */       if (cause != null) {
/*  86 */         throw cause;
/*     */       }
/*  88 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static CloseableHttpResponse newProxy(HttpResponse original) {
/*     */     try {
/*  96 */       return (CloseableHttpResponse)CONSTRUCTOR.newInstance(new Object[] { new CloseableHttpResponseProxy(original) });
/*  97 */     } catch (InstantiationException ex) {
/*  98 */       throw new IllegalStateException(ex);
/*  99 */     } catch (InvocationTargetException ex) {
/* 100 */       throw new IllegalStateException(ex);
/* 101 */     } catch (IllegalAccessException ex) {
/* 102 */       throw new IllegalStateException(ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\CloseableHttpResponseProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */