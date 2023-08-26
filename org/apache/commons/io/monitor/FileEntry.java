/*     */ package org.apache.commons.io.monitor;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.nio.file.Files;
/*     */ import org.apache.commons.io.FileUtils;
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
/*     */ public class FileEntry
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2505664948818681153L;
/*  51 */   static final FileEntry[] EMPTY_FILE_ENTRY_ARRAY = new FileEntry[0];
/*     */   
/*     */   private final FileEntry parent;
/*     */   
/*     */   private FileEntry[] children;
/*     */   
/*     */   private final File file;
/*     */   
/*     */   private String name;
/*     */   
/*     */   private boolean exists;
/*     */   
/*     */   private boolean directory;
/*     */   private long lastModified;
/*     */   private long length;
/*     */   
/*     */   public FileEntry(File file) {
/*  68 */     this(null, file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileEntry(FileEntry parent, File file) {
/*  78 */     if (file == null) {
/*  79 */       throw new IllegalArgumentException("File is missing");
/*     */     }
/*  81 */     this.file = file;
/*  82 */     this.parent = parent;
/*  83 */     this.name = file.getName();
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
/*     */   public boolean refresh(File file) {
/* 102 */     boolean origExists = this.exists;
/* 103 */     long origLastModified = this.lastModified;
/* 104 */     boolean origDirectory = this.directory;
/* 105 */     long origLength = this.length;
/*     */ 
/*     */     
/* 108 */     this.name = file.getName();
/* 109 */     this.exists = Files.exists(file.toPath(), new java.nio.file.LinkOption[0]);
/* 110 */     this.directory = (this.exists && file.isDirectory());
/*     */     try {
/* 112 */       this.lastModified = this.exists ? FileUtils.lastModified(file) : 0L;
/* 113 */     } catch (IOException e) {
/* 114 */       this.lastModified = 0L;
/*     */     } 
/* 116 */     this.length = (this.exists && !this.directory) ? file.length() : 0L;
/*     */ 
/*     */     
/* 119 */     return (this.exists != origExists || this.lastModified != origLastModified || this.directory != origDirectory || this.length != origLength);
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
/*     */   public FileEntry newChildInstance(File file) {
/* 133 */     return new FileEntry(this, file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileEntry getParent() {
/* 142 */     return this.parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLevel() {
/* 151 */     return (this.parent == null) ? 0 : (this.parent.getLevel() + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileEntry[] getChildren() {
/* 162 */     return (this.children != null) ? this.children : EMPTY_FILE_ENTRY_ARRAY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChildren(FileEntry... children) {
/* 171 */     this.children = children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 180 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 189 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 198 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastModified() {
/* 208 */     return this.lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastModified(long lastModified) {
/* 218 */     this.lastModified = lastModified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLength() {
/* 227 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLength(long length) {
/* 236 */     this.length = length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExists() {
/* 246 */     return this.exists;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExists(boolean exists) {
/* 256 */     this.exists = exists;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 265 */     return this.directory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirectory(boolean directory) {
/* 274 */     this.directory = directory;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\monitor\FileEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */