/*     */ package org.apache.http.config;
/*     */ 
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ @Immutable
/*     */ public class SocketConfig
/*     */   implements Cloneable
/*     */ {
/*  41 */   public static final SocketConfig DEFAULT = (new Builder()).build();
/*     */ 
/*     */   
/*     */   private final int soTimeout;
/*     */   
/*     */   private final boolean soReuseAddress;
/*     */   
/*     */   private final int soLinger;
/*     */   
/*     */   private final boolean soKeepAlive;
/*     */   
/*     */   private final boolean tcpNoDelay;
/*     */   
/*     */   private final int sndBufSize;
/*     */   
/*     */   private final int rcvBufSize;
/*     */   
/*     */   private int backlogSize;
/*     */ 
/*     */   
/*     */   SocketConfig(int soTimeout, boolean soReuseAddress, int soLinger, boolean soKeepAlive, boolean tcpNoDelay, int sndBufSize, int rcvBufSize, int backlogSize) {
/*  62 */     this.soTimeout = soTimeout;
/*  63 */     this.soReuseAddress = soReuseAddress;
/*  64 */     this.soLinger = soLinger;
/*  65 */     this.soKeepAlive = soKeepAlive;
/*  66 */     this.tcpNoDelay = tcpNoDelay;
/*  67 */     this.sndBufSize = sndBufSize;
/*  68 */     this.rcvBufSize = rcvBufSize;
/*  69 */     this.backlogSize = backlogSize;
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
/*     */   public int getSoTimeout() {
/*  82 */     return this.soTimeout;
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
/*     */   public boolean isSoReuseAddress() {
/*  96 */     return this.soReuseAddress;
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
/*     */   public int getSoLinger() {
/* 110 */     return this.soLinger;
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
/*     */   public boolean isSoKeepAlive() {
/* 124 */     return this.soKeepAlive;
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
/*     */   public boolean isTcpNoDelay() {
/* 138 */     return this.tcpNoDelay;
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
/*     */   public int getSndBufSize() {
/* 153 */     return this.sndBufSize;
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
/*     */   public int getRcvBufSize() {
/* 168 */     return this.rcvBufSize;
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
/*     */   public int getBacklogSize() {
/* 181 */     return this.backlogSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SocketConfig clone() throws CloneNotSupportedException {
/* 186 */     return (SocketConfig)super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 191 */     StringBuilder builder = new StringBuilder();
/* 192 */     builder.append("[soTimeout=").append(this.soTimeout).append(", soReuseAddress=").append(this.soReuseAddress).append(", soLinger=").append(this.soLinger).append(", soKeepAlive=").append(this.soKeepAlive).append(", tcpNoDelay=").append(this.tcpNoDelay).append(", sndBufSize=").append(this.sndBufSize).append(", rcvBufSize=").append(this.rcvBufSize).append(", backlogSize=").append(this.backlogSize).append("]");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 205 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder copy(SocketConfig config) {
/* 209 */     Args.notNull(config, "Socket config");
/* 210 */     return (new Builder()).setSoTimeout(config.getSoTimeout()).setSoReuseAddress(config.isSoReuseAddress()).setSoLinger(config.getSoLinger()).setSoKeepAlive(config.isSoKeepAlive()).setTcpNoDelay(config.isTcpNoDelay()).setSndBufSize(config.getSndBufSize()).setRcvBufSize(config.getRcvBufSize()).setBacklogSize(config.getBacklogSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private int soTimeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean soReuseAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 233 */     private int soLinger = -1;
/*     */     private boolean soKeepAlive;
/*     */     private boolean tcpNoDelay = true;
/*     */     
/*     */     public Builder setSoTimeout(int soTimeout) {
/* 238 */       this.soTimeout = soTimeout;
/* 239 */       return this;
/*     */     }
/*     */     private int sndBufSize; private int rcvBufSize; private int backlogSize;
/*     */     public Builder setSoReuseAddress(boolean soReuseAddress) {
/* 243 */       this.soReuseAddress = soReuseAddress;
/* 244 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSoLinger(int soLinger) {
/* 248 */       this.soLinger = soLinger;
/* 249 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setSoKeepAlive(boolean soKeepAlive) {
/* 253 */       this.soKeepAlive = soKeepAlive;
/* 254 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTcpNoDelay(boolean tcpNoDelay) {
/* 258 */       this.tcpNoDelay = tcpNoDelay;
/* 259 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSndBufSize(int sndBufSize) {
/* 266 */       this.sndBufSize = sndBufSize;
/* 267 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setRcvBufSize(int rcvBufSize) {
/* 274 */       this.rcvBufSize = rcvBufSize;
/* 275 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setBacklogSize(int backlogSize) {
/* 282 */       this.backlogSize = backlogSize;
/* 283 */       return this;
/*     */     }
/*     */     
/*     */     public SocketConfig build() {
/* 287 */       return new SocketConfig(this.soTimeout, this.soReuseAddress, this.soLinger, this.soKeepAlive, this.tcpNoDelay, this.sndBufSize, this.rcvBufSize, this.backlogSize);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\config\SocketConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */