/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.client.CredentialsProvider;
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
/*     */ @ThreadSafe
/*     */ public class BasicCredentialsProvider
/*     */   implements CredentialsProvider
/*     */ {
/*  53 */   private final ConcurrentHashMap<AuthScope, Credentials> credMap = new ConcurrentHashMap<AuthScope, Credentials>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCredentials(AuthScope authscope, Credentials credentials) {
/*  60 */     Args.notNull(authscope, "Authentication scope");
/*  61 */     this.credMap.put(authscope, credentials);
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
/*     */   private static Credentials matchCredentials(Map<AuthScope, Credentials> map, AuthScope authscope) {
/*  76 */     Credentials creds = map.get(authscope);
/*  77 */     if (creds == null) {
/*     */ 
/*     */       
/*  80 */       int bestMatchFactor = -1;
/*  81 */       AuthScope bestMatch = null;
/*  82 */       for (AuthScope current : map.keySet()) {
/*  83 */         int factor = authscope.match(current);
/*  84 */         if (factor > bestMatchFactor) {
/*  85 */           bestMatchFactor = factor;
/*  86 */           bestMatch = current;
/*     */         } 
/*     */       } 
/*  89 */       if (bestMatch != null) {
/*  90 */         creds = map.get(bestMatch);
/*     */       }
/*     */     } 
/*  93 */     return creds;
/*     */   }
/*     */ 
/*     */   
/*     */   public Credentials getCredentials(AuthScope authscope) {
/*  98 */     Args.notNull(authscope, "Authentication scope");
/*  99 */     return matchCredentials(this.credMap, authscope);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 104 */     this.credMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 109 */     return this.credMap.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\BasicCredentialsProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */