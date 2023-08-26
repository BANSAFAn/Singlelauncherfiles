/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.objectweb.asm.Attribute;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PackingOptions
/*     */ {
/*  31 */   private static final Attribute[] EMPTY_ATTRIBUTE_ARRAY = new Attribute[0];
/*     */   
/*     */   public static final long SEGMENT_LIMIT = 1000000L;
/*     */   
/*     */   public static final String STRIP = "strip";
/*     */   public static final String ERROR = "error";
/*     */   public static final String PASS = "pass";
/*     */   public static final String KEEP = "keep";
/*     */   private boolean gzip = true;
/*     */   private boolean stripDebug;
/*     */   private boolean keepFileOrder = true;
/*  42 */   private long segmentLimit = 1000000L;
/*  43 */   private int effort = 5;
/*  44 */   private String deflateHint = "keep";
/*  45 */   private String modificationTime = "keep";
/*  46 */   private final List<String> passFiles = new ArrayList<>();
/*  47 */   private String unknownAttributeAction = "pass";
/*  48 */   private final Map<String, String> classAttributeActions = new HashMap<>();
/*  49 */   private final Map<String, String> fieldAttributeActions = new HashMap<>();
/*  50 */   private final Map<String, String> methodAttributeActions = new HashMap<>();
/*  51 */   private final Map<String, String> codeAttributeActions = new HashMap<>();
/*     */   
/*     */   private boolean verbose;
/*     */   private String logFile;
/*     */   private Attribute[] unknownAttributeTypes;
/*     */   
/*     */   public void addClassAttributeAction(String attributeName, String action) {
/*  58 */     this.classAttributeActions.put(attributeName, action);
/*     */   }
/*     */   
/*     */   public void addCodeAttributeAction(String attributeName, String action) {
/*  62 */     this.codeAttributeActions.put(attributeName, action);
/*     */   }
/*     */   
/*     */   public void addFieldAttributeAction(String attributeName, String action) {
/*  66 */     this.fieldAttributeActions.put(attributeName, action);
/*     */   }
/*     */   
/*     */   public void addMethodAttributeAction(String attributeName, String action) {
/*  70 */     this.methodAttributeActions.put(attributeName, action);
/*     */   }
/*     */   
/*     */   private void addOrUpdateAttributeActions(List<Attribute> prototypes, Map<String, String> attributeActions, int tag) {
/*  74 */     if (attributeActions != null && attributeActions.size() > 0)
/*     */     {
/*  76 */       for (String name : attributeActions.keySet()) {
/*  77 */         String action = attributeActions.get(name);
/*  78 */         boolean prototypeExists = false;
/*  79 */         for (Attribute prototype : prototypes) {
/*  80 */           NewAttribute newAttribute = (NewAttribute)prototype;
/*  81 */           if (newAttribute.type.equals(name)) {
/*     */             
/*  83 */             newAttribute.addContext(tag);
/*  84 */             prototypeExists = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*  89 */         if (!prototypeExists) {
/*  90 */           NewAttribute newAttribute; if ("error".equals(action)) {
/*  91 */             newAttribute = new NewAttribute.ErrorAttribute(name, tag);
/*  92 */           } else if ("strip".equals(action)) {
/*  93 */             newAttribute = new NewAttribute.StripAttribute(name, tag);
/*  94 */           } else if ("pass".equals(action)) {
/*  95 */             newAttribute = new NewAttribute.PassAttribute(name, tag);
/*     */           } else {
/*  97 */             newAttribute = new NewAttribute(name, action, tag);
/*     */           } 
/*  99 */           prototypes.add(newAttribute);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPassFile(String passFileName) {
/* 112 */     String fileSeparator = System.getProperty("file.separator");
/* 113 */     if (fileSeparator.equals("\\"))
/*     */     {
/* 115 */       fileSeparator = fileSeparator + "\\";
/*     */     }
/* 117 */     passFileName = passFileName.replaceAll(fileSeparator, "/");
/* 118 */     this.passFiles.add(passFileName);
/*     */   }
/*     */   
/*     */   public String getDeflateHint() {
/* 122 */     return this.deflateHint;
/*     */   }
/*     */   
/*     */   public int getEffort() {
/* 126 */     return this.effort;
/*     */   }
/*     */   
/*     */   public String getLogFile() {
/* 130 */     return this.logFile;
/*     */   }
/*     */   
/*     */   public String getModificationTime() {
/* 134 */     return this.modificationTime;
/*     */   }
/*     */   
/*     */   private String getOrDefault(Map<String, String> map, String type, String defaultValue) {
/* 138 */     return (map == null) ? defaultValue : map.getOrDefault(type, defaultValue);
/*     */   }
/*     */   
/*     */   public long getSegmentLimit() {
/* 142 */     return this.segmentLimit;
/*     */   }
/*     */   
/*     */   public String getUnknownAttributeAction() {
/* 146 */     return this.unknownAttributeAction;
/*     */   }
/*     */   
/*     */   public Attribute[] getUnknownAttributePrototypes() {
/* 150 */     if (this.unknownAttributeTypes == null) {
/* 151 */       List<Attribute> prototypes = new ArrayList<>();
/* 152 */       addOrUpdateAttributeActions(prototypes, this.classAttributeActions, 0);
/* 153 */       addOrUpdateAttributeActions(prototypes, this.methodAttributeActions, 2);
/* 154 */       addOrUpdateAttributeActions(prototypes, this.fieldAttributeActions, 1);
/* 155 */       addOrUpdateAttributeActions(prototypes, this.codeAttributeActions, 3);
/* 156 */       this.unknownAttributeTypes = prototypes.<Attribute>toArray(EMPTY_ATTRIBUTE_ARRAY);
/*     */     } 
/* 158 */     return this.unknownAttributeTypes;
/*     */   }
/*     */   
/*     */   public String getUnknownClassAttributeAction(String type) {
/* 162 */     return getOrDefault(this.classAttributeActions, type, this.unknownAttributeAction);
/*     */   }
/*     */   
/*     */   public String getUnknownCodeAttributeAction(String type) {
/* 166 */     return getOrDefault(this.codeAttributeActions, type, this.unknownAttributeAction);
/*     */   }
/*     */   
/*     */   public String getUnknownFieldAttributeAction(String type) {
/* 170 */     return getOrDefault(this.fieldAttributeActions, type, this.unknownAttributeAction);
/*     */   }
/*     */   
/*     */   public String getUnknownMethodAttributeAction(String type) {
/* 174 */     return getOrDefault(this.methodAttributeActions, type, this.unknownAttributeAction);
/*     */   }
/*     */   
/*     */   public boolean isGzip() {
/* 178 */     return this.gzip;
/*     */   }
/*     */   
/*     */   public boolean isKeepDeflateHint() {
/* 182 */     return "keep".equals(this.deflateHint);
/*     */   }
/*     */   
/*     */   public boolean isKeepFileOrder() {
/* 186 */     return this.keepFileOrder;
/*     */   }
/*     */   
/*     */   public boolean isPassFile(String passFileName) {
/* 190 */     for (String pass : this.passFiles) {
/* 191 */       if (passFileName.equals(pass)) {
/* 192 */         return true;
/*     */       }
/* 194 */       if (!pass.endsWith(".class")) {
/*     */         
/* 196 */         if (!pass.endsWith("/"))
/*     */         {
/*     */ 
/*     */           
/* 200 */           pass = pass + "/";
/*     */         }
/* 202 */         return passFileName.startsWith(pass);
/*     */       } 
/*     */     } 
/* 205 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isStripDebug() {
/* 209 */     return this.stripDebug;
/*     */   }
/*     */   
/*     */   public boolean isVerbose() {
/* 213 */     return this.verbose;
/*     */   }
/*     */   
/*     */   public void removePassFile(String passFileName) {
/* 217 */     this.passFiles.remove(passFileName);
/*     */   }
/*     */   
/*     */   public void setDeflateHint(String deflateHint) {
/* 221 */     if (!"keep".equals(deflateHint) && !"true".equals(deflateHint) && !"false".equals(deflateHint)) {
/* 222 */       throw new IllegalArgumentException("Bad argument: -H " + deflateHint + " ? deflate hint should be either true, false or keep (default)");
/*     */     }
/* 224 */     this.deflateHint = deflateHint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEffort(int effort) {
/* 233 */     this.effort = effort;
/*     */   }
/*     */   
/*     */   public void setGzip(boolean gzip) {
/* 237 */     this.gzip = gzip;
/*     */   }
/*     */   
/*     */   public void setKeepFileOrder(boolean keepFileOrder) {
/* 241 */     this.keepFileOrder = keepFileOrder;
/*     */   }
/*     */   
/*     */   public void setLogFile(String logFile) {
/* 245 */     this.logFile = logFile;
/*     */   }
/*     */   
/*     */   public void setModificationTime(String modificationTime) {
/* 249 */     if (!"keep".equals(modificationTime) && !"latest".equals(modificationTime)) {
/* 250 */       throw new IllegalArgumentException("Bad argument: -m " + modificationTime + " ? transmit modtimes should be either latest or keep (default)");
/*     */     }
/* 252 */     this.modificationTime = modificationTime;
/*     */   }
/*     */   
/*     */   public void setQuiet(boolean quiet) {
/* 256 */     this.verbose = !quiet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSegmentLimit(long segmentLimit) {
/* 265 */     this.segmentLimit = segmentLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStripDebug(boolean stripDebug) {
/* 276 */     this.stripDebug = stripDebug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnknownAttributeAction(String unknownAttributeAction) {
/* 285 */     this.unknownAttributeAction = unknownAttributeAction;
/* 286 */     if (!"pass".equals(unknownAttributeAction) && !"error".equals(unknownAttributeAction) && !"strip".equals(unknownAttributeAction)) {
/* 287 */       throw new IllegalArgumentException("Incorrect option for -U, " + unknownAttributeAction);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 292 */     this.verbose = verbose;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\PackingOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */