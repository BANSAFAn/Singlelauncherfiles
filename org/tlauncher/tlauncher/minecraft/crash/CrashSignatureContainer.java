/*     */ package org.tlauncher.tlauncher.minecraft.crash;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CrashSignatureContainer
/*     */ {
/*     */   private static final int universalExitCode = 0;
/*  25 */   private Map<String, String> variables = new LinkedHashMap<>();
/*  26 */   private List<CrashSignature> signatures = new ArrayList<>();
/*     */   
/*     */   public Map<String, String> getVariables() {
/*  29 */     return this.variables;
/*     */   }
/*     */   
/*     */   public List<CrashSignature> getSignatures() {
/*  33 */     return this.signatures;
/*     */   }
/*     */   
/*     */   public String getVariable(String key) {
/*  37 */     return this.variables.get(key);
/*     */   }
/*     */   
/*     */   public Pattern getPattern(String key) {
/*  41 */     return Pattern.compile(this.variables.get(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  46 */     return getClass().getSimpleName() + "{\nvariables='" + this.variables + "',\nsignatures='" + this.signatures + "'}";
/*     */   }
/*     */ 
/*     */   
/*     */   public class CrashSignature
/*     */   {
/*     */     private String name;
/*     */     
/*     */     private String version;
/*     */     private String path;
/*     */     private String pattern;
/*     */     
/*     */     public String getName() {
/*  59 */       return this.name;
/*     */     }
/*     */     private int exit; private boolean fake; private boolean forge; private Pattern versionPattern; private Pattern linePattern;
/*     */     public Pattern getVersion() {
/*  63 */       return this.versionPattern;
/*     */     }
/*     */     
/*     */     public boolean hasVersion() {
/*  67 */       return (this.version != null);
/*     */     }
/*     */     
/*     */     public boolean isFake() {
/*  71 */       return this.fake;
/*     */     }
/*     */     
/*     */     public Pattern getPattern() {
/*  75 */       return this.linePattern;
/*     */     }
/*     */     
/*     */     public boolean hasPattern() {
/*  79 */       return (this.pattern != null);
/*     */     }
/*     */     
/*     */     public String getPath() {
/*  83 */       return this.path;
/*     */     }
/*     */     
/*     */     public int getExitCode() {
/*  87 */       return this.exit;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  92 */       return getClass().getSimpleName() + "{name='" + this.name + "', version='" + this.version + "', path='" + this.path + "', pattern='" + this.pattern + "', exitCode=" + this.exit + ", forge=" + this.forge + ", versionPattern='" + this.versionPattern + "', linePattern='" + this.linePattern + "'}";
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
/*     */   private static class CrashSignatureListSimpleDeserializer
/*     */   {
/* 107 */     private final Gson defaultContext = TLauncher.getGson(); private Map<String, String> variables;
/*     */     private String forgePrefix;
/*     */     
/*     */     public void setVariables(Map<String, String> vars) {
/* 111 */       this.variables = (vars == null) ? new HashMap<>() : vars;
/*     */       
/* 113 */       this
/* 114 */         .forgePrefix = this.variables.containsKey("forge") ? this.variables.get("forge") : "";
/*     */     }
/*     */ 
/*     */     
/*     */     public List<CrashSignatureContainer.CrashSignature> deserialize(JsonElement elem) throws JsonParseException {
/* 119 */       List<CrashSignatureContainer.CrashSignature> signatureList = (List<CrashSignatureContainer.CrashSignature>)this.defaultContext.fromJson(elem, (new TypeToken<List<CrashSignatureContainer.CrashSignature>>() {
/*     */           
/* 121 */           }).getType());
/*     */       
/* 123 */       for (CrashSignatureContainer.CrashSignature signature : signatureList) {
/* 124 */         analyzeSignature(signature);
/*     */       }
/* 126 */       return signatureList;
/*     */     }
/*     */     
/*     */     private CrashSignatureContainer.CrashSignature analyzeSignature(CrashSignatureContainer.CrashSignature signature) {
/* 130 */       if (signature.name == null || signature.name.isEmpty()) {
/* 131 */         throw new JsonParseException("Invalid name: \"" + signature
/* 132 */             .name + "\"");
/*     */       }
/* 134 */       if (signature.version != null) {
/* 135 */         String pattern = signature.version;
/*     */         
/* 137 */         for (Map.Entry<String, String> en : this.variables.entrySet()) {
/* 138 */           String varName = en.getKey(), varVal = en.getValue();
/* 139 */           pattern = pattern.replace("${" + varName + "}", varVal);
/*     */         } 
/*     */         
/* 142 */         signature.versionPattern = Pattern.compile(pattern);
/*     */       } 
/*     */       
/* 145 */       if (signature.pattern != null) {
/* 146 */         String pattern = signature.pattern;
/*     */         
/* 148 */         for (Map.Entry<String, String> en : this.variables.entrySet()) {
/* 149 */           String varName = en.getKey(), varVal = en.getValue();
/* 150 */           pattern = pattern.replace("${" + varName + "}", varVal);
/*     */         } 
/*     */         
/* 153 */         if (signature.forge) {
/* 154 */           pattern = this.forgePrefix + pattern;
/*     */         }
/* 156 */         signature.linePattern = Pattern.compile(pattern);
/*     */       } 
/*     */       
/* 159 */       if (signature.versionPattern == null && signature
/* 160 */         .linePattern == null && signature
/* 161 */         .exit == 0) {
/* 162 */         throw new JsonParseException("Useless signature found: " + signature
/* 163 */             .name);
/*     */       }
/* 165 */       return signature;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class CrashSignatureContainerDeserializer
/*     */     implements JsonDeserializer<CrashSignatureContainer>
/*     */   {
/* 175 */     private final Gson defaultContext = TLauncher.getGson();
/* 176 */     private final CrashSignatureContainer.CrashSignatureListSimpleDeserializer listDeserializer = new CrashSignatureContainer.CrashSignatureListSimpleDeserializer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CrashSignatureContainer deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
/* 183 */       JsonObject object = element.getAsJsonObject();
/*     */       
/* 185 */       Map<String, String> rawVariables = (Map<String, String>)this.defaultContext.fromJson(object
/* 186 */           .get("variables"), (new TypeToken<Map<String, String>>() {
/*     */           
/* 188 */           }).getType());
/* 189 */       Map<String, String> variables = new LinkedHashMap<>();
/*     */       
/* 191 */       for (Map.Entry<String, String> rawEn : rawVariables.entrySet()) {
/* 192 */         String varName = rawEn.getKey(), varVal = rawEn.getValue();
/*     */         
/* 194 */         for (Map.Entry<String, String> en : variables.entrySet()) {
/* 195 */           String replaceName = en.getKey();
/* 196 */           String replaceVal = en.getValue();
/* 197 */           varVal = varVal.replace("${" + replaceName + "}", replaceVal);
/*     */         } 
/*     */ 
/*     */         
/* 201 */         variables.put(varName, varVal);
/*     */       } 
/*     */       
/* 204 */       this.listDeserializer.setVariables(variables);
/*     */ 
/*     */       
/* 207 */       List<CrashSignatureContainer.CrashSignature> signatures = this.listDeserializer.deserialize(object.get("signatures"));
/*     */       
/* 209 */       CrashSignatureContainer list = new CrashSignatureContainer();
/* 210 */       list.variables = variables;
/* 211 */       list.signatures = signatures;
/*     */       
/* 213 */       return list;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\crash\CrashSignatureContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */