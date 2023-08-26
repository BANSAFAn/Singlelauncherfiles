/*     */ package org.apache.commons.compress.harmony.unpack200;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.apache.commons.compress.harmony.pack200.Pack200Exception;
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
/*     */ public class Archive
/*     */ {
/*     */   private InputStream inputStream;
/*     */   private final JarOutputStream outputStream;
/*     */   private boolean removePackFile;
/*  48 */   private int logLevel = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   private FileOutputStream logFile;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean overrideDeflateHint;
/*     */ 
/*     */   
/*     */   private boolean deflateHint;
/*     */ 
/*     */   
/*     */   private String inputFileName;
/*     */ 
/*     */   
/*     */   private String outputFileName;
/*     */ 
/*     */ 
/*     */   
/*     */   public Archive(String inputFile, String outputFile) throws FileNotFoundException, IOException {
/*  70 */     this.inputFileName = inputFile;
/*  71 */     this.outputFileName = outputFile;
/*  72 */     this.inputStream = new FileInputStream(inputFile);
/*  73 */     this.outputStream = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Archive(InputStream inputStream, JarOutputStream outputStream) {
/*  84 */     this.inputStream = inputStream;
/*  85 */     this.outputStream = outputStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unpack() throws Pack200Exception, IOException {
/*  95 */     this.outputStream.setComment("PACK200");
/*     */     try {
/*  97 */       if (!this.inputStream.markSupported()) {
/*  98 */         this.inputStream = new BufferedInputStream(this.inputStream);
/*  99 */         if (!this.inputStream.markSupported()) {
/* 100 */           throw new IllegalStateException();
/*     */         }
/*     */       } 
/* 103 */       this.inputStream.mark(2);
/* 104 */       if ((this.inputStream.read() & 0xFF | (this.inputStream.read() & 0xFF) << 8) == 35615) {
/* 105 */         this.inputStream.reset();
/* 106 */         this.inputStream = new BufferedInputStream(new GZIPInputStream(this.inputStream));
/*     */       } else {
/* 108 */         this.inputStream.reset();
/*     */       } 
/* 110 */       this.inputStream.mark(4);
/* 111 */       int[] magic = { 202, 254, 208, 13 };
/*     */       
/* 113 */       int[] word = new int[4];
/* 114 */       for (int i = 0; i < word.length; i++) {
/* 115 */         word[i] = this.inputStream.read();
/*     */       }
/* 117 */       boolean compressedWithE0 = false;
/* 118 */       for (int m = 0; m < magic.length; m++) {
/* 119 */         if (word[m] != magic[m]) {
/* 120 */           compressedWithE0 = true;
/*     */         }
/*     */       } 
/* 123 */       this.inputStream.reset();
/* 124 */       if (compressedWithE0) {
/*     */         
/* 126 */         JarInputStream jarInputStream = new JarInputStream(this.inputStream);
/*     */         JarEntry jarEntry;
/* 128 */         while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
/* 129 */           this.outputStream.putNextEntry(jarEntry);
/* 130 */           byte[] bytes = new byte[16384];
/* 131 */           int bytesRead = jarInputStream.read(bytes);
/* 132 */           while (bytesRead != -1) {
/* 133 */             this.outputStream.write(bytes, 0, bytesRead);
/* 134 */             bytesRead = jarInputStream.read(bytes);
/*     */           } 
/* 136 */           this.outputStream.closeEntry();
/*     */         } 
/*     */       } else {
/* 139 */         int j = 0;
/* 140 */         while (available(this.inputStream)) {
/* 141 */           j++;
/* 142 */           Segment segment = new Segment();
/* 143 */           segment.setLogLevel(this.logLevel);
/* 144 */           segment.setLogStream((this.logFile != null) ? this.logFile : System.out);
/* 145 */           segment.setPreRead(false);
/*     */           
/* 147 */           if (j == 1) {
/* 148 */             segment.log(2, "Unpacking from " + this.inputFileName + " to " + this.outputFileName);
/*     */           }
/*     */           
/* 151 */           segment.log(2, "Reading segment " + j);
/* 152 */           if (this.overrideDeflateHint) {
/* 153 */             segment.overrideDeflateHint(this.deflateHint);
/*     */           }
/* 155 */           segment.unpack(this.inputStream, this.outputStream);
/* 156 */           this.outputStream.flush();
/*     */           
/* 158 */           if (this.inputStream instanceof FileInputStream) {
/* 159 */             this.inputFileName = ((FileInputStream)this.inputStream).getFD().toString();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       try {
/* 165 */         this.inputStream.close();
/* 166 */       } catch (Exception exception) {}
/*     */       
/*     */       try {
/* 169 */         this.outputStream.close();
/* 170 */       } catch (Exception exception) {}
/*     */       
/* 172 */       if (this.logFile != null) {
/*     */         try {
/* 174 */           this.logFile.close();
/* 175 */         } catch (Exception exception) {}
/*     */       }
/*     */     } 
/*     */     
/* 179 */     if (this.removePackFile) {
/* 180 */       boolean deleted = false;
/* 181 */       if (this.inputFileName != null) {
/* 182 */         File file = new File(this.inputFileName);
/* 183 */         deleted = file.delete();
/*     */       } 
/* 185 */       if (!deleted) {
/* 186 */         throw new Pack200Exception("Failed to delete the input file.");
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean available(InputStream inputStream) throws IOException {
/* 192 */     inputStream.mark(1);
/* 193 */     int check = inputStream.read();
/* 194 */     inputStream.reset();
/* 195 */     return (check != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemovePackFile(boolean removePackFile) {
/* 204 */     this.removePackFile = removePackFile;
/*     */   }
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 208 */     if (verbose) {
/* 209 */       this.logLevel = 2;
/* 210 */     } else if (this.logLevel == 2) {
/* 211 */       this.logLevel = 1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setQuiet(boolean quiet) {
/* 216 */     if (quiet) {
/* 217 */       this.logLevel = 0;
/* 218 */     } else if (this.logLevel == 0) {
/* 219 */       this.logLevel = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setLogFile(String logFileName) throws FileNotFoundException {
/* 224 */     this.logFile = new FileOutputStream(logFileName);
/*     */   }
/*     */   
/*     */   public void setLogFile(String logFileName, boolean append) throws FileNotFoundException {
/* 228 */     this.logFile = new FileOutputStream(logFileName, append);
/*     */   }
/*     */   
/*     */   public void setDeflateHint(boolean deflateHint) {
/* 232 */     this.overrideDeflateHint = true;
/* 233 */     this.deflateHint = deflateHint;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmon\\unpack200\Archive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */