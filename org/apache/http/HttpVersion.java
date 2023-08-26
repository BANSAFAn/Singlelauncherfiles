/*     */ package org.apache.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public final class HttpVersion
/*     */   extends ProtocolVersion
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5856653513894415344L;
/*     */   public static final String HTTP = "HTTP";
/*  57 */   public static final HttpVersion HTTP_0_9 = new HttpVersion(0, 9);
/*     */ 
/*     */   
/*  60 */   public static final HttpVersion HTTP_1_0 = new HttpVersion(1, 0);
/*     */ 
/*     */   
/*  63 */   public static final HttpVersion HTTP_1_1 = new HttpVersion(1, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpVersion(int major, int minor) {
/*  75 */     super("HTTP", major, minor);
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
/*     */   public ProtocolVersion forVersion(int major, int minor) {
/*  90 */     if (major == this.major && minor == this.minor) {
/*  91 */       return this;
/*     */     }
/*     */     
/*  94 */     if (major == 1) {
/*  95 */       if (minor == 0) {
/*  96 */         return HTTP_1_0;
/*     */       }
/*  98 */       if (minor == 1) {
/*  99 */         return HTTP_1_1;
/*     */       }
/*     */     } 
/* 102 */     if (major == 0 && minor == 9) {
/* 103 */       return HTTP_0_9;
/*     */     }
/*     */ 
/*     */     
/* 107 */     return new HttpVersion(major, minor);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\HttpVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */