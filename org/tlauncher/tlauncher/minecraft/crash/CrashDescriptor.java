/*     */ package org.tlauncher.tlauncher.minecraft.crash;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import java.util.Scanner;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CrashDescriptor
/*     */ {
/*     */   private static CrashSignatureContainer container;
/*     */   public static final int goodExitCode = 0;
/*     */   private static final String loggerPrefix = "[Crash]";
/*     */   private final MinecraftLauncher launcher;
/*     */   
/*     */   public CrashDescriptor(MinecraftLauncher launcher) {
/*  25 */     if (launcher == null) {
/*  26 */       throw new NullPointerException();
/*     */     }
/*  28 */     this.launcher = launcher;
/*     */   }
/*     */   
/*     */   public Crash scan() {
/*  32 */     int exitCode = this.launcher.getExitCode();
/*  33 */     if (exitCode == 0 && System.currentTimeMillis() > this.launcher.getStartupTime() + 60000L) {
/*  34 */       return null;
/*     */     }
/*  36 */     Crash crash = new Crash();
/*  37 */     if (container.getVariables().isEmpty())
/*  38 */       return crash; 
/*  39 */     Pattern filePattern = container.getPattern("crash");
/*     */     
/*  41 */     String version = this.launcher.getVersionName();
/*     */     
/*  43 */     Scanner scanner = new Scanner(U.readFileLog());
/*     */     
/*  45 */     while (scanner.hasNextLine()) {
/*  46 */       String line = scanner.nextLine();
/*  47 */       if (filePattern.matcher(line).matches()) {
/*  48 */         Matcher fileMatcher = filePattern.matcher(line);
/*     */         
/*  50 */         if (fileMatcher.matches() && fileMatcher.groupCount() == 1) {
/*  51 */           crash.setFile(fileMatcher.group(1));
/*  52 */           log(new Object[] { "Found crash report file:", crash.getFile() });
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*  58 */       for (CrashSignatureContainer.CrashSignature signature : container.getSignatures()) {
/*  59 */         if (signature.hasVersion() && !signature.getVersion().matcher(version).matches()) {
/*     */           continue;
/*     */         }
/*  62 */         if (signature.getExitCode() != 0 && signature.getExitCode() != exitCode) {
/*     */           continue;
/*     */         }
/*  65 */         if (signature.hasPattern() && !signature.getPattern().matcher(line).matches()) {
/*     */           continue;
/*     */         }
/*  68 */         if (signature.isFake()) {
/*  69 */           log(new Object[] { "Minecraft closed with an illegal exit code not due to error. Scanning has been cancelled" });
/*  70 */           log(new Object[] { "Fake signature:", signature.getName() });
/*     */           
/*  72 */           scanner.close();
/*  73 */           return null;
/*     */         } 
/*     */         
/*  76 */         if (crash.hasSignature(signature)) {
/*     */           continue;
/*     */         }
/*  79 */         log(new Object[] { "Signature \"" + signature.getName() + "\" matches!" });
/*  80 */         crash.addSignature(signature);
/*     */       } 
/*     */     } 
/*  83 */     scanner.close();
/*  84 */     if (crash.isRecognized() && exitCode == 0 && 
/*  85 */       !crash.getSignatures().stream().filter(e -> e.getName().equals("Bad video drivers")).findAny().isPresent())
/*  86 */       return null; 
/*  87 */     if (crash.isRecognized()) {
/*  88 */       log(new Object[] { "Crash has been recognized!" });
/*  89 */     } else if (exitCode == 0) {
/*  90 */       return null;
/*     */     } 
/*  92 */     return crash;
/*     */   }
/*     */   
/*     */   void log(Object... w) {
/*  96 */     this.launcher.log(new Object[] { "[Crash]", w });
/*  97 */     U.log(new Object[] { "[Crash]", w });
/*     */   }
/*     */   
/*     */   static {
/* 101 */     GsonBuilder builder = new GsonBuilder();
/* 102 */     builder.registerTypeAdapter(CrashSignatureContainer.class, new CrashSignatureContainer.CrashSignatureContainerDeserializer());
/*     */     
/* 104 */     Gson gson = builder.create();
/*     */     
/*     */     try {
/* 107 */       container = (CrashSignatureContainer)gson.fromJson(FileUtil.getResource(TLauncher.class.getResource("/signatures.json")), CrashSignatureContainer.class);
/* 108 */     } catch (Exception e) {
/* 109 */       U.log(new Object[] { "Cannot parse crash signatures!", e });
/* 110 */       container = new CrashSignatureContainer();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\crash\CrashDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */