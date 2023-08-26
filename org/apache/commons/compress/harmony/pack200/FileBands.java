/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.commons.compress.utils.ExactMath;
/*     */ import org.objectweb.asm.ClassReader;
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
/*     */ public class FileBands
/*     */   extends BandSet
/*     */ {
/*     */   private final CPUTF8[] fileName;
/*     */   private int[] file_name;
/*     */   private final int[] file_modtime;
/*     */   private final long[] file_size;
/*     */   private final int[] file_options;
/*     */   private final byte[][] file_bits;
/*     */   private final List<Archive.PackingFile> fileList;
/*     */   private final PackingOptions options;
/*     */   private final CpBands cpBands;
/*     */   
/*     */   public FileBands(CpBands cpBands, SegmentHeader segmentHeader, PackingOptions options, Archive.SegmentUnit segmentUnit, int effort) {
/*  50 */     super(effort, segmentHeader);
/*  51 */     this.fileList = segmentUnit.getFileList();
/*  52 */     this.options = options;
/*  53 */     this.cpBands = cpBands;
/*  54 */     int size = this.fileList.size();
/*  55 */     this.fileName = new CPUTF8[size];
/*  56 */     this.file_modtime = new int[size];
/*  57 */     this.file_size = new long[size];
/*  58 */     this.file_options = new int[size];
/*  59 */     int totalSize = 0;
/*  60 */     this.file_bits = new byte[size][];
/*  61 */     int archiveModtime = segmentHeader.getArchive_modtime();
/*     */     
/*  63 */     Set<String> classNames = new HashSet<>();
/*  64 */     for (ClassReader reader : segmentUnit.getClassList()) {
/*  65 */       classNames.add(reader.getClassName());
/*     */     }
/*  67 */     CPUTF8 emptyString = cpBands.getCPUtf8("");
/*     */     
/*  69 */     int latestModtime = Integer.MIN_VALUE;
/*  70 */     boolean isLatest = !"keep".equals(options.getModificationTime());
/*  71 */     for (int i = 0; i < size; i++) {
/*  72 */       Archive.PackingFile packingFile = this.fileList.get(i);
/*  73 */       String name = packingFile.getName();
/*  74 */       if (name.endsWith(".class") && !options.isPassFile(name)) {
/*  75 */         this.file_options[i] = this.file_options[i] | 0x2;
/*  76 */         if (classNames.contains(name.substring(0, name.length() - 6))) {
/*  77 */           this.fileName[i] = emptyString;
/*     */         } else {
/*  79 */           this.fileName[i] = cpBands.getCPUtf8(name);
/*     */         } 
/*     */       } else {
/*  82 */         this.fileName[i] = cpBands.getCPUtf8(name);
/*     */       } 
/*     */       
/*  85 */       if (options.isKeepDeflateHint() && packingFile.isDefalteHint()) {
/*  86 */         this.file_options[i] = this.file_options[i] | 0x1;
/*     */       }
/*  88 */       byte[] bytes = packingFile.getContents();
/*  89 */       this.file_size[i] = bytes.length;
/*  90 */       totalSize = ExactMath.add(totalSize, this.file_size[i]);
/*     */ 
/*     */       
/*  93 */       long modtime = (packingFile.getModtime() + TimeZone.getDefault().getRawOffset()) / 1000L;
/*  94 */       this.file_modtime[i] = (int)(modtime - archiveModtime);
/*  95 */       if (isLatest && latestModtime < this.file_modtime[i]) {
/*  96 */         latestModtime = this.file_modtime[i];
/*     */       }
/*     */       
/*  99 */       this.file_bits[i] = packingFile.getContents();
/*     */     } 
/*     */     
/* 102 */     if (isLatest) {
/* 103 */       Arrays.fill(this.file_modtime, latestModtime);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finaliseBands() {
/* 112 */     this.file_name = new int[this.fileName.length];
/* 113 */     for (int i = 0; i < this.file_name.length; i++) {
/* 114 */       if (this.fileName[i].equals(this.cpBands.getCPUtf8(""))) {
/* 115 */         Archive.PackingFile packingFile = this.fileList.get(i);
/* 116 */         String name = packingFile.getName();
/* 117 */         if (this.options.isPassFile(name)) {
/* 118 */           this.fileName[i] = this.cpBands.getCPUtf8(name);
/* 119 */           this.file_options[i] = this.file_options[i] & 0xFFFFFFFD;
/*     */         } 
/*     */       } 
/* 122 */       this.file_name[i] = this.fileName[i].getIndex();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void pack(OutputStream out) throws IOException, Pack200Exception {
/* 128 */     PackingUtils.log("Writing file bands...");
/* 129 */     byte[] encodedBand = encodeBandInt("file_name", this.file_name, Codec.UNSIGNED5);
/* 130 */     out.write(encodedBand);
/* 131 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from file_name[" + this.file_name.length + "]");
/*     */     
/* 133 */     encodedBand = encodeFlags("file_size", this.file_size, Codec.UNSIGNED5, Codec.UNSIGNED5, this.segmentHeader
/* 134 */         .have_file_size_hi());
/* 135 */     out.write(encodedBand);
/* 136 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from file_size[" + this.file_size.length + "]");
/*     */     
/* 138 */     if (this.segmentHeader.have_file_modtime()) {
/* 139 */       encodedBand = encodeBandInt("file_modtime", this.file_modtime, Codec.DELTA5);
/* 140 */       out.write(encodedBand);
/* 141 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from file_modtime[" + this.file_modtime.length + "]");
/*     */     } 
/* 143 */     if (this.segmentHeader.have_file_options()) {
/* 144 */       encodedBand = encodeBandInt("file_options", this.file_options, Codec.UNSIGNED5);
/* 145 */       out.write(encodedBand);
/* 146 */       PackingUtils.log("Wrote " + encodedBand.length + " bytes from file_options[" + this.file_options.length + "]");
/*     */     } 
/*     */     
/* 149 */     encodedBand = encodeBandInt("file_bits", flatten(this.file_bits), Codec.BYTE1);
/* 150 */     out.write(encodedBand);
/* 151 */     PackingUtils.log("Wrote " + encodedBand.length + " bytes from file_bits[" + this.file_bits.length + "]");
/*     */   }
/*     */   
/*     */   private int[] flatten(byte[][] bytes) {
/* 155 */     int total = 0;
/* 156 */     for (byte[] element : bytes) {
/* 157 */       total += element.length;
/*     */     }
/* 159 */     int[] band = new int[total];
/* 160 */     int index = 0;
/* 161 */     for (byte[] element : bytes) {
/* 162 */       for (byte element2 : element) {
/* 163 */         band[index++] = element2 & 0xFF;
/*     */       }
/*     */     } 
/* 166 */     return band;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\FileBands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */