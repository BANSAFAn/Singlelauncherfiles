/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.http.HttpConnectionMetrics;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.HttpTransportMetrics;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class HttpConnectionMetricsImpl
/*     */   implements HttpConnectionMetrics
/*     */ {
/*     */   public static final String REQUEST_COUNT = "http.request-count";
/*     */   public static final String RESPONSE_COUNT = "http.response-count";
/*     */   public static final String SENT_BYTES_COUNT = "http.sent-bytes-count";
/*     */   public static final String RECEIVED_BYTES_COUNT = "http.received-bytes-count";
/*     */   private final HttpTransportMetrics inTransportMetric;
/*     */   private final HttpTransportMetrics outTransportMetric;
/*  52 */   private long requestCount = 0L;
/*  53 */   private long responseCount = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Object> metricsCache;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpConnectionMetricsImpl(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric) {
/*  64 */     this.inTransportMetric = inTransportMetric;
/*  65 */     this.outTransportMetric = outTransportMetric;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getReceivedBytesCount() {
/*  72 */     if (this.inTransportMetric != null) {
/*  73 */       return this.inTransportMetric.getBytesTransferred();
/*     */     }
/*  75 */     return -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSentBytesCount() {
/*  81 */     if (this.outTransportMetric != null) {
/*  82 */       return this.outTransportMetric.getBytesTransferred();
/*     */     }
/*  84 */     return -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getRequestCount() {
/*  90 */     return this.requestCount;
/*     */   }
/*     */   
/*     */   public void incrementRequestCount() {
/*  94 */     this.requestCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getResponseCount() {
/*  99 */     return this.responseCount;
/*     */   }
/*     */   
/*     */   public void incrementResponseCount() {
/* 103 */     this.responseCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getMetric(String metricName) {
/* 108 */     Object value = null;
/* 109 */     if (this.metricsCache != null) {
/* 110 */       value = this.metricsCache.get(metricName);
/*     */     }
/* 112 */     if (value == null) {
/* 113 */       if ("http.request-count".equals(metricName))
/* 114 */       { value = Long.valueOf(this.requestCount); }
/* 115 */       else if ("http.response-count".equals(metricName))
/* 116 */       { value = Long.valueOf(this.responseCount); }
/* 117 */       else { if ("http.received-bytes-count".equals(metricName)) {
/* 118 */           if (this.inTransportMetric != null) {
/* 119 */             return Long.valueOf(this.inTransportMetric.getBytesTransferred());
/*     */           }
/* 121 */           return null;
/*     */         } 
/* 123 */         if ("http.sent-bytes-count".equals(metricName)) {
/* 124 */           if (this.outTransportMetric != null) {
/* 125 */             return Long.valueOf(this.outTransportMetric.getBytesTransferred());
/*     */           }
/* 127 */           return null;
/*     */         }  }
/*     */     
/*     */     }
/* 131 */     return value;
/*     */   }
/*     */   
/*     */   public void setMetric(String metricName, Object obj) {
/* 135 */     if (this.metricsCache == null) {
/* 136 */       this.metricsCache = new HashMap<String, Object>();
/*     */     }
/* 138 */     this.metricsCache.put(metricName, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 143 */     if (this.outTransportMetric != null) {
/* 144 */       this.outTransportMetric.reset();
/*     */     }
/* 146 */     if (this.inTransportMetric != null) {
/* 147 */       this.inTransportMetric.reset();
/*     */     }
/* 149 */     this.requestCount = 0L;
/* 150 */     this.responseCount = 0L;
/* 151 */     this.metricsCache = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\HttpConnectionMetricsImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */