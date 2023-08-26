/*    */ package org.apache.commons.compress.utils;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import java.util.ServiceConfigurationError;
/*    */ import java.util.ServiceLoader;
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
/*    */ @Deprecated
/*    */ public class ServiceLoaderIterator<E>
/*    */   implements Iterator<E>
/*    */ {
/*    */   private E nextServiceLoader;
/*    */   private final Class<E> service;
/*    */   private final Iterator<E> serviceLoaderIterator;
/*    */   
/*    */   public ServiceLoaderIterator(Class<E> service) {
/* 44 */     this(service, ClassLoader.getSystemClassLoader());
/*    */   }
/*    */   
/*    */   public ServiceLoaderIterator(Class<E> service, ClassLoader classLoader) {
/* 48 */     this.service = service;
/* 49 */     this.serviceLoaderIterator = ServiceLoader.<E>load(service, classLoader).iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 54 */     while (this.nextServiceLoader == null) {
/*    */       try {
/* 56 */         if (!this.serviceLoaderIterator.hasNext()) {
/* 57 */           return false;
/*    */         }
/* 59 */         this.nextServiceLoader = this.serviceLoaderIterator.next();
/* 60 */       } catch (ServiceConfigurationError e) {
/* 61 */         if (e.getCause() instanceof SecurityException) {
/*    */           continue;
/*    */         }
/*    */ 
/*    */         
/* 66 */         throw e;
/*    */       } 
/*    */     } 
/* 69 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public E next() {
/* 74 */     if (!hasNext()) {
/* 75 */       throw new NoSuchElementException("No more elements for service " + this.service.getName());
/*    */     }
/* 77 */     E tempNext = this.nextServiceLoader;
/* 78 */     this.nextServiceLoader = null;
/* 79 */     return tempNext;
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove() {
/* 84 */     throw new UnsupportedOperationException("service=" + this.service.getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compres\\utils\ServiceLoaderIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */