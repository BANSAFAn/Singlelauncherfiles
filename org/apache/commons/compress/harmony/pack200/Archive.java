/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
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
/*     */ public class Archive
/*     */ {
/*     */   private final JarInputStream jarInputStream;
/*     */   private final OutputStream outputStream;
/*     */   private JarFile jarFile;
/*     */   private long currentSegmentSize;
/*     */   private final PackingOptions options;
/*     */   
/*     */   public Archive(JarInputStream inputStream, OutputStream outputStream, PackingOptions options) throws IOException {
/*  53 */     this.jarInputStream = inputStream;
/*  54 */     if (options == null)
/*     */     {
/*  56 */       options = new PackingOptions();
/*     */     }
/*  58 */     this.options = options;
/*  59 */     if (options.isGzip()) {
/*  60 */       outputStream = new GZIPOutputStream(outputStream);
/*     */     }
/*  62 */     this.outputStream = new BufferedOutputStream(outputStream);
/*  63 */     PackingUtils.config(options);
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
/*     */   public Archive(JarFile jarFile, OutputStream outputStream, PackingOptions options) throws IOException {
/*  75 */     if (options == null) {
/*  76 */       options = new PackingOptions();
/*     */     }
/*  78 */     this.options = options;
/*  79 */     if (options.isGzip()) {
/*  80 */       outputStream = new GZIPOutputStream(outputStream);
/*     */     }
/*  82 */     this.outputStream = new BufferedOutputStream(outputStream);
/*  83 */     this.jarFile = jarFile;
/*  84 */     this.jarInputStream = null;
/*  85 */     PackingUtils.config(options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pack() throws Pack200Exception, IOException {
/*  95 */     if (0 == this.options.getEffort()) {
/*  96 */       doZeroEffortPack();
/*     */     } else {
/*  98 */       doNormalPack();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doZeroEffortPack() throws IOException {
/* 103 */     PackingUtils.log("Start to perform a zero-effort packing");
/* 104 */     if (this.jarInputStream != null) {
/* 105 */       PackingUtils.copyThroughJar(this.jarInputStream, this.outputStream);
/*     */     } else {
/* 107 */       PackingUtils.copyThroughJar(this.jarFile, this.outputStream);
/*     */     } 
/*     */   }
/*     */   private void doNormalPack() throws IOException, Pack200Exception {
/*     */     List<PackingFile> packingFileList;
/* 112 */     PackingUtils.log("Start to perform a normal packing");
/*     */     
/* 114 */     if (this.jarInputStream != null) {
/* 115 */       packingFileList = PackingUtils.getPackingFileListFromJar(this.jarInputStream, this.options.isKeepFileOrder());
/*     */     } else {
/* 117 */       packingFileList = PackingUtils.getPackingFileListFromJar(this.jarFile, this.options.isKeepFileOrder());
/*     */     } 
/*     */     
/* 120 */     List<SegmentUnit> segmentUnitList = splitIntoSegments(packingFileList);
/* 121 */     int previousByteAmount = 0;
/* 122 */     int packedByteAmount = 0;
/*     */     
/* 124 */     int segmentSize = segmentUnitList.size();
/*     */     
/* 126 */     for (int index = 0; index < segmentSize; index++) {
/* 127 */       SegmentUnit segmentUnit = segmentUnitList.get(index);
/* 128 */       (new Segment()).pack(segmentUnit, this.outputStream, this.options);
/* 129 */       previousByteAmount += segmentUnit.getByteAmount();
/* 130 */       packedByteAmount += segmentUnit.getPackedByteAmount();
/*     */     } 
/*     */     
/* 133 */     PackingUtils.log("Total: Packed " + previousByteAmount + " input bytes of " + packingFileList.size() + " files into " + packedByteAmount + " bytes in " + segmentSize + " segments");
/*     */ 
/*     */     
/* 136 */     this.outputStream.close();
/*     */   }
/*     */   
/*     */   private List<SegmentUnit> splitIntoSegments(List<PackingFile> packingFileList) {
/* 140 */     List<SegmentUnit> segmentUnitList = new ArrayList<>();
/* 141 */     List<Pack200ClassReader> classes = new ArrayList<>();
/* 142 */     List<PackingFile> files = new ArrayList<>();
/* 143 */     long segmentLimit = this.options.getSegmentLimit();
/*     */     
/* 145 */     int size = packingFileList.size();
/*     */     
/* 147 */     for (int index = 0; index < size; index++) {
/* 148 */       PackingFile packingFile = packingFileList.get(index);
/* 149 */       if (!addJarEntry(packingFile, classes, files)) {
/*     */         
/* 151 */         segmentUnitList.add(new SegmentUnit(classes, files));
/* 152 */         classes = new ArrayList<>();
/* 153 */         files = new ArrayList<>();
/* 154 */         this.currentSegmentSize = 0L;
/*     */         
/* 156 */         addJarEntry(packingFile, classes, files);
/*     */         
/* 158 */         this.currentSegmentSize = 0L;
/* 159 */       } else if (segmentLimit == 0L && estimateSize(packingFile) > 0L) {
/*     */         
/* 161 */         segmentUnitList.add(new SegmentUnit(classes, files));
/* 162 */         classes = new ArrayList<>();
/* 163 */         files = new ArrayList<>();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 168 */     if (classes.size() > 0 || files.size() > 0) {
/* 169 */       segmentUnitList.add(new SegmentUnit(classes, files));
/*     */     }
/* 171 */     return segmentUnitList;
/*     */   }
/*     */   
/*     */   private boolean addJarEntry(PackingFile packingFile, List<Pack200ClassReader> javaClasses, List<PackingFile> files) {
/* 175 */     long segmentLimit = this.options.getSegmentLimit();
/* 176 */     if (segmentLimit != -1L && segmentLimit != 0L) {
/*     */ 
/*     */ 
/*     */       
/* 180 */       long packedSize = estimateSize(packingFile);
/* 181 */       if (packedSize + this.currentSegmentSize > segmentLimit && this.currentSegmentSize > 0L)
/*     */       {
/* 183 */         return false;
/*     */       }
/*     */       
/* 186 */       this.currentSegmentSize += packedSize;
/*     */     } 
/*     */     
/* 189 */     String name = packingFile.getName();
/* 190 */     if (name.endsWith(".class") && !this.options.isPassFile(name)) {
/* 191 */       Pack200ClassReader classParser = new Pack200ClassReader(packingFile.contents);
/* 192 */       classParser.setFileName(name);
/* 193 */       javaClasses.add(classParser);
/* 194 */       packingFile.contents = new byte[0];
/*     */     } 
/* 196 */     files.add(packingFile);
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long estimateSize(PackingFile packingFile) {
/* 203 */     String name = packingFile.getName();
/* 204 */     if (name.startsWith("META-INF") || name.startsWith("/META-INF")) {
/* 205 */       return 0L;
/*     */     }
/* 207 */     long fileSize = packingFile.contents.length;
/* 208 */     if (fileSize < 0L) {
/* 209 */       fileSize = 0L;
/*     */     }
/* 211 */     return name.length() + fileSize + 5L;
/*     */   }
/*     */ 
/*     */   
/*     */   static class SegmentUnit
/*     */   {
/*     */     private final List<Pack200ClassReader> classList;
/*     */     
/*     */     private final List<Archive.PackingFile> fileList;
/*     */     
/*     */     private int byteAmount;
/*     */     private int packedByteAmount;
/*     */     
/*     */     public SegmentUnit(List<Pack200ClassReader> classes, List<Archive.PackingFile> files) {
/* 225 */       this.classList = classes;
/* 226 */       this.fileList = files;
/* 227 */       this.byteAmount = 0;
/*     */       
/* 229 */       this.byteAmount += this.classList.stream().mapToInt(element -> element.b.length).sum();
/* 230 */       this.byteAmount += this.fileList.stream().mapToInt(element -> element.contents.length).sum();
/*     */     }
/*     */     
/*     */     public List<Pack200ClassReader> getClassList() {
/* 234 */       return this.classList;
/*     */     }
/*     */     
/*     */     public int classListSize() {
/* 238 */       return this.classList.size();
/*     */     }
/*     */     
/*     */     public int fileListSize() {
/* 242 */       return this.fileList.size();
/*     */     }
/*     */     
/*     */     public List<Archive.PackingFile> getFileList() {
/* 246 */       return this.fileList;
/*     */     }
/*     */     
/*     */     public int getByteAmount() {
/* 250 */       return this.byteAmount;
/*     */     }
/*     */     
/*     */     public int getPackedByteAmount() {
/* 254 */       return this.packedByteAmount;
/*     */     }
/*     */     
/*     */     public void addPackedByteAmount(int amount) {
/* 258 */       this.packedByteAmount += amount;
/*     */     }
/*     */   }
/*     */   
/*     */   static class PackingFile
/*     */   {
/*     */     private final String name;
/*     */     private byte[] contents;
/*     */     private final long modtime;
/*     */     private final boolean deflateHint;
/*     */     private final boolean isDirectory;
/*     */     
/*     */     public PackingFile(String name, byte[] contents, long modtime) {
/* 271 */       this.name = name;
/* 272 */       this.contents = contents;
/* 273 */       this.modtime = modtime;
/* 274 */       this.deflateHint = false;
/* 275 */       this.isDirectory = false;
/*     */     }
/*     */     
/*     */     public PackingFile(byte[] bytes, JarEntry jarEntry) {
/* 279 */       this.name = jarEntry.getName();
/* 280 */       this.contents = bytes;
/* 281 */       this.modtime = jarEntry.getTime();
/* 282 */       this.deflateHint = (jarEntry.getMethod() == 8);
/* 283 */       this.isDirectory = jarEntry.isDirectory();
/*     */     }
/*     */     
/*     */     public byte[] getContents() {
/* 287 */       return this.contents;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 291 */       return this.name;
/*     */     }
/*     */     
/*     */     public long getModtime() {
/* 295 */       return this.modtime;
/*     */     }
/*     */     
/*     */     public void setContents(byte[] contents) {
/* 299 */       this.contents = contents;
/*     */     }
/*     */     
/*     */     public boolean isDefalteHint() {
/* 303 */       return this.deflateHint;
/*     */     }
/*     */     
/*     */     public boolean isDirectory() {
/* 307 */       return this.isDirectory;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 312 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\Archive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */