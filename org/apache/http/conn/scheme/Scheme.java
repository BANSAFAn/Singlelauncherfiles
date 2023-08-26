/*     */ package org.apache.http.conn.scheme;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.LangUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Immutable
/*     */ public final class Scheme
/*     */ {
/*     */   private final String name;
/*     */   private final SchemeSocketFactory socketFactory;
/*     */   private final int defaultPort;
/*     */   private final boolean layered;
/*     */   private String stringRep;
/*     */   
/*     */   public Scheme(String name, int port, SchemeSocketFactory factory) {
/*  91 */     Args.notNull(name, "Scheme name");
/*  92 */     Args.check((port > 0 && port <= 65535), "Port is invalid");
/*  93 */     Args.notNull(factory, "Socket factory");
/*  94 */     this.name = name.toLowerCase(Locale.ENGLISH);
/*  95 */     this.defaultPort = port;
/*  96 */     if (factory instanceof SchemeLayeredSocketFactory) {
/*  97 */       this.layered = true;
/*  98 */       this.socketFactory = factory;
/*  99 */     } else if (factory instanceof LayeredSchemeSocketFactory) {
/* 100 */       this.layered = true;
/* 101 */       this.socketFactory = new SchemeLayeredSocketFactoryAdaptor2((LayeredSchemeSocketFactory)factory);
/*     */     } else {
/* 103 */       this.layered = false;
/* 104 */       this.socketFactory = factory;
/*     */     } 
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
/*     */   @Deprecated
/*     */   public Scheme(String name, SocketFactory factory, int port) {
/* 126 */     Args.notNull(name, "Scheme name");
/* 127 */     Args.notNull(factory, "Socket factory");
/* 128 */     Args.check((port > 0 && port <= 65535), "Port is invalid");
/*     */     
/* 130 */     this.name = name.toLowerCase(Locale.ENGLISH);
/* 131 */     if (factory instanceof LayeredSocketFactory) {
/* 132 */       this.socketFactory = new SchemeLayeredSocketFactoryAdaptor((LayeredSocketFactory)factory);
/*     */       
/* 134 */       this.layered = true;
/*     */     } else {
/* 136 */       this.socketFactory = new SchemeSocketFactoryAdaptor(factory);
/* 137 */       this.layered = false;
/*     */     } 
/* 139 */     this.defaultPort = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getDefaultPort() {
/* 148 */     return this.defaultPort;
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
/*     */   @Deprecated
/*     */   public final SocketFactory getSocketFactory() {
/* 163 */     if (this.socketFactory instanceof SchemeSocketFactoryAdaptor) {
/* 164 */       return ((SchemeSocketFactoryAdaptor)this.socketFactory).getFactory();
/*     */     }
/* 166 */     if (this.layered) {
/* 167 */       return new LayeredSocketFactoryAdaptor((LayeredSchemeSocketFactory)this.socketFactory);
/*     */     }
/*     */     
/* 170 */     return new SocketFactoryAdaptor(this.socketFactory);
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
/*     */   public final SchemeSocketFactory getSchemeSocketFactory() {
/* 185 */     return this.socketFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 194 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isLayered() {
/* 204 */     return this.layered;
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
/*     */   public final int resolvePort(int port) {
/* 217 */     return (port <= 0) ? this.defaultPort : port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 227 */     if (this.stringRep == null) {
/* 228 */       StringBuilder buffer = new StringBuilder();
/* 229 */       buffer.append(this.name);
/* 230 */       buffer.append(':');
/* 231 */       buffer.append(Integer.toString(this.defaultPort));
/* 232 */       this.stringRep = buffer.toString();
/*     */     } 
/* 234 */     return this.stringRep;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 239 */     if (this == obj) {
/* 240 */       return true;
/*     */     }
/* 242 */     if (obj instanceof Scheme) {
/* 243 */       Scheme that = (Scheme)obj;
/* 244 */       return (this.name.equals(that.name) && this.defaultPort == that.defaultPort && this.layered == that.layered);
/*     */     } 
/*     */ 
/*     */     
/* 248 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 254 */     int hash = 17;
/* 255 */     hash = LangUtils.hashCode(hash, this.defaultPort);
/* 256 */     hash = LangUtils.hashCode(hash, this.name);
/* 257 */     hash = LangUtils.hashCode(hash, this.layered);
/* 258 */     return hash;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\conn\scheme\Scheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */