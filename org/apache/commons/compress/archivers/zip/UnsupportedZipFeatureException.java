/*     */ package org.apache.commons.compress.archivers.zip;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.zip.ZipException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnsupportedZipFeatureException
/*     */   extends ZipException
/*     */ {
/*     */   private final Feature reason;
/*     */   private final transient ZipArchiveEntry entry;
/*     */   private static final long serialVersionUID = 20161219L;
/*     */   
/*     */   public UnsupportedZipFeatureException(Feature reason, ZipArchiveEntry entry) {
/*  42 */     super("Unsupported feature " + reason + " used in entry " + entry
/*  43 */         .getName());
/*  44 */     this.reason = reason;
/*  45 */     this.entry = entry;
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
/*     */   public UnsupportedZipFeatureException(ZipMethod method, ZipArchiveEntry entry) {
/*  57 */     super("Unsupported compression method " + entry.getMethod() + " (" + method
/*  58 */         .name() + ") used in entry " + entry.getName());
/*  59 */     this.reason = Feature.METHOD;
/*  60 */     this.entry = entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnsupportedZipFeatureException(Feature reason) {
/*  71 */     super("Unsupported feature " + reason + " used in archive.");
/*  72 */     this.reason = reason;
/*  73 */     this.entry = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Feature getFeature() {
/*  81 */     return this.reason;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipArchiveEntry getEntry() {
/*  89 */     return this.entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Feature
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 4112582948775420359L;
/*     */ 
/*     */ 
/*     */     
/* 102 */     public static final Feature ENCRYPTION = new Feature("encryption");
/*     */ 
/*     */ 
/*     */     
/* 106 */     public static final Feature METHOD = new Feature("compression method");
/*     */ 
/*     */ 
/*     */     
/* 110 */     public static final Feature DATA_DESCRIPTOR = new Feature("data descriptor");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     public static final Feature SPLITTING = new Feature("splitting");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     public static final Feature UNKNOWN_COMPRESSED_SIZE = new Feature("unknown compressed size");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private Feature(String name) {
/* 127 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 132 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\UnsupportedZipFeatureException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */