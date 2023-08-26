/*    */ package org.tlauncher.tlauncher.entity;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class VersionPeriod
/*    */ {
/*    */   private String minVersion;
/*    */   private List<String> ignoreVersions;
/*    */   private List<String> includeVersions;
/*    */   
/*    */   public String getMinVersion() {
/* 12 */     return this.minVersion;
/*    */   }
/*    */   
/*    */   public void setMinVersion(String minVersion) {
/* 16 */     this.minVersion = minVersion;
/*    */   }
/*    */   
/*    */   public List<String> getIgnoreVersions() {
/* 20 */     return this.ignoreVersions;
/*    */   }
/*    */   
/*    */   public void setIgnoreVersions(List<String> ignoreVersions) {
/* 24 */     this.ignoreVersions = ignoreVersions;
/*    */   }
/*    */   
/*    */   public List<String> getIncludeVersions() {
/* 28 */     return this.includeVersions;
/*    */   }
/*    */   
/*    */   public void setIncludeVersions(List<String> includeVersions) {
/* 32 */     this.includeVersions = includeVersions;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\VersionPeriod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */