/*    */ package com.sothawo.mapjfx.cache;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ class CachedDataInfo
/*    */   implements Serializable
/*    */ {
/*    */   private String contentType;
/*    */   private String contentEncoding;
/* 38 */   private Map<String, List<String>> headerFields = Collections.emptyMap();
/*    */   
/*    */   public Map<String, List<String>> getHeaderFields() {
/* 41 */     return this.headerFields;
/*    */   }
/*    */   
/*    */   public void setHeaderFields(Map<String, List<String>> headerFields) {
/* 45 */     this.headerFields = headerFields;
/*    */   }
/*    */   
/*    */   public String getContentEncoding() {
/* 49 */     return this.contentEncoding;
/*    */   }
/*    */   
/*    */   public void setContentEncoding(String contentEncoding) {
/* 53 */     this.contentEncoding = contentEncoding;
/*    */   }
/*    */   
/*    */   public String getContentType() {
/* 57 */     return this.contentType;
/*    */   }
/*    */   
/*    */   public void setContentType(String contentType) {
/* 61 */     this.contentType = contentType;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 66 */     return "CachedDataInfo{contentType='" + this.contentType + '\'' + ", contentEncoding='" + this.contentEncoding + '\'' + ", headerFields=" + this.headerFields + '}';
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setFromHttpUrlConnection(HttpURLConnection httpUrlConnection) {
/* 74 */     this.contentType = httpUrlConnection.getContentType();
/* 75 */     this.contentEncoding = httpUrlConnection.getContentEncoding();
/* 76 */     this.headerFields = new HashMap<>();
/* 77 */     this.headerFields.putAll(httpUrlConnection.getHeaderFields());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\sothawo\mapjfx\cache\CachedDataInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */