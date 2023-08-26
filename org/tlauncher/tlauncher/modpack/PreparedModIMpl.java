/*     */ package org.tlauncher.tlauncher.modpack;
/*     */ 
/*     */ import com.github.junrar.Archive;
/*     */ import com.github.junrar.exception.RarException;
/*     */ import com.github.junrar.extract.ExtractArchive;
/*     */ import com.github.junrar.rarfile.FileHeader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.tlauncher.util.U;
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
/*     */ public class PreparedModIMpl
/*     */   implements PreparedMod
/*     */ {
/*     */   public List<Path> prepare(CompleteVersion completeVersion) {
/*  40 */     return new ArrayList<>();
/*     */   }
/*     */   
/*     */   private void prepareModeFolders(Path source, Path dest, List<Path> paths) {
/*  44 */     File[] files = source.toFile().listFiles();
/*  45 */     for (int i = 0; i < files.length; i++) {
/*  46 */       paths.add(files[i].toPath());
/*     */     }
/*     */     try {
/*  49 */       Files.copy(source, dest, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*  50 */     } catch (IOException e) {
/*  51 */       U.log(new Object[] { e });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void prepareGameFolders(Path source, Path dest, List<Path> paths) {
/*  56 */     File[] files = source.toFile().listFiles();
/*  57 */     ExtractArchive extractArchive = new ExtractArchive();
/*  58 */     for (int i = 0; i < files.length; i++) {
/*     */       
/*  60 */       String ext = FilenameUtils.getExtension(files[i].toString());
/*  61 */       switch (ext) {
/*     */         case "rar":
/*     */           try {
/*  64 */             paths.addAll(topFolders(new Archive(files[i])));
/*  65 */           } catch (IOException|RarException e) {
/*  66 */             U.log(new Object[] { e });
/*     */           } 
/*  68 */           extractArchive.extractArchive(files[i], dest.toFile());
/*     */           return;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case "zip":
/*     */           return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  81 */       U.log(new Object[] { "don't find extension" });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Path> topFolders(Archive rar) throws IOException, RarException {
/*  87 */     FileHeader fh = rar.nextFileHeader();
/*  88 */     List<Path> list = new ArrayList<>();
/*  89 */     while (fh != null) {
/*  90 */       if (fh.isDirectory()) {
/*  91 */         list.add(Paths.get(fh.getFileNameString(), new String[0]));
/*     */       }
/*  93 */       fh = rar.nextFileHeader();
/*     */     } 
/*  95 */     return list;
/*     */   }
/*     */   
/*     */   private List<Path> topFolders(ZipFile zipFile) {
/*  99 */     List<Path> list = new ArrayList<>();
/* 100 */     Enumeration<? extends ZipEntry> entries = zipFile.entries();
/* 101 */     while (entries.hasMoreElements()) {
/* 102 */       ZipEntry entry = entries.nextElement();
/* 103 */       if (entry.isDirectory()) {
/* 104 */         File file = new File(entry.getName());
/* 105 */         if (file.getParent() == null) {
/* 106 */           list.add(Paths.get(file.getName(), new String[0]));
/*     */         }
/*     */       } 
/*     */     } 
/* 110 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\modpack\PreparedModIMpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */