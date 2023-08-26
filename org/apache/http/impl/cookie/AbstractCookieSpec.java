/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieSpec;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public abstract class AbstractCookieSpec
/*     */   implements CookieSpec
/*     */ {
/*     */   private final Map<String, CookieAttributeHandler> attribHandlerMap;
/*     */   
/*     */   public AbstractCookieSpec() {
/*  63 */     this.attribHandlerMap = new ConcurrentHashMap<String, CookieAttributeHandler>(10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractCookieSpec(HashMap<String, CookieAttributeHandler> map) {
/*  71 */     Asserts.notNull(map, "Attribute handler map");
/*  72 */     this.attribHandlerMap = new ConcurrentHashMap<String, CookieAttributeHandler>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractCookieSpec(CommonCookieAttributeHandler... handlers) {
/*  80 */     this.attribHandlerMap = new ConcurrentHashMap<String, CookieAttributeHandler>(handlers.length);
/*  81 */     for (CommonCookieAttributeHandler handler : handlers) {
/*  82 */       this.attribHandlerMap.put(handler.getAttributeName(), handler);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void registerAttribHandler(String name, CookieAttributeHandler handler) {
/*  94 */     Args.notNull(name, "Attribute name");
/*  95 */     Args.notNull(handler, "Attribute handler");
/*  96 */     this.attribHandlerMap.put(name, handler);
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
/*     */   protected CookieAttributeHandler findAttribHandler(String name) {
/* 108 */     return this.attribHandlerMap.get(name);
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
/*     */   protected CookieAttributeHandler getAttribHandler(String name) {
/* 120 */     CookieAttributeHandler handler = findAttribHandler(name);
/* 121 */     Asserts.check((handler != null), "Handler not registered for " + name + " attribute");
/*     */     
/* 123 */     return handler;
/*     */   }
/*     */   
/*     */   protected Collection<CookieAttributeHandler> getAttribHandlers() {
/* 127 */     return this.attribHandlerMap.values();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\AbstractCookieSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */