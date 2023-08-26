/*     */ package org.apache.commons.compress.compressors;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.util.Collections;
/*     */ import java.util.Locale;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.commons.compress.compressors.brotli.BrotliCompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.brotli.BrotliUtils;
/*     */ import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.deflate64.Deflate64CompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.lz4.BlockLZ4CompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.lz4.FramedLZ4CompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.lz4.FramedLZ4CompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.lzma.LZMAUtils;
/*     */ import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.xz.XZUtils;
/*     */ import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.zstandard.ZstdCompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.zstandard.ZstdUtils;
/*     */ import org.apache.commons.compress.utils.IOUtils;
/*     */ import org.apache.commons.compress.utils.Sets;
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
/*     */ public class CompressorStreamFactory
/*     */   implements CompressorStreamProvider
/*     */ {
/*  97 */   private static final CompressorStreamFactory SINGLETON = new CompressorStreamFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String BROTLI = "br";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String BZIP2 = "bzip2";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String GZIP = "gz";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String PACK200 = "pack200";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String XZ = "xz";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LZMA = "lzma";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String SNAPPY_FRAMED = "snappy-framed";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String SNAPPY_RAW = "snappy-raw";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String Z = "z";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFLATE = "deflate";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFLATE64 = "deflate64";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LZ4_BLOCK = "lz4-block";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LZ4_FRAMED = "lz4-framed";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ZSTANDARD = "zstd";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 209 */   private static final String YOU_NEED_BROTLI_DEC = youNeed("Google Brotli Dec", "https://github.com/google/brotli/");
/* 210 */   private static final String YOU_NEED_XZ_JAVA = youNeed("XZ for Java", "https://tukaani.org/xz/java.html");
/* 211 */   private static final String YOU_NEED_ZSTD_JNI = youNeed("Zstd JNI", "https://github.com/luben/zstd-jni");
/*     */   
/*     */   private static String youNeed(String name, String url) {
/* 214 */     return " In addition to Apache Commons Compress you need the " + name + " library - see " + url;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Boolean decompressUntilEOF;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SortedMap<String, CompressorStreamProvider> compressorInputStreamProviders;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SortedMap<String, CompressorStreamProvider> compressorOutputStreamProviders;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean decompressConcatenated;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int memoryLimitInKb;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SortedMap<String, CompressorStreamProvider> findAvailableCompressorInputStreamProviders() {
/* 245 */     return AccessController.<SortedMap<String, CompressorStreamProvider>>doPrivileged(() -> {
/*     */           TreeMap<String, CompressorStreamProvider> map = new TreeMap<>();
/*     */           putAll(SINGLETON.getInputStreamCompressorNames(), SINGLETON, map);
/*     */           archiveStreamProviderIterable().forEach(());
/*     */           return map;
/*     */         });
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
/*     */   public static SortedMap<String, CompressorStreamProvider> findAvailableCompressorOutputStreamProviders() {
/* 281 */     return AccessController.<SortedMap<String, CompressorStreamProvider>>doPrivileged(() -> {
/*     */           TreeMap<String, CompressorStreamProvider> map = new TreeMap<>();
/*     */           putAll(SINGLETON.getOutputStreamCompressorNames(), SINGLETON, map);
/*     */           archiveStreamProviderIterable().forEach(());
/*     */           return map;
/*     */         });
/*     */   }
/*     */   
/*     */   public static String getBrotli() {
/* 290 */     return "br";
/*     */   }
/*     */   
/*     */   public static String getBzip2() {
/* 294 */     return "bzip2";
/*     */   }
/*     */   
/*     */   public static String getDeflate() {
/* 298 */     return "deflate";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getDeflate64() {
/* 306 */     return "deflate64";
/*     */   }
/*     */   
/*     */   public static String getGzip() {
/* 310 */     return "gz";
/*     */   }
/*     */   
/*     */   public static String getLzma() {
/* 314 */     return "lzma";
/*     */   }
/*     */   
/*     */   public static String getPack200() {
/* 318 */     return "pack200";
/*     */   }
/*     */   
/*     */   public static CompressorStreamFactory getSingleton() {
/* 322 */     return SINGLETON;
/*     */   }
/*     */   
/*     */   public static String getSnappyFramed() {
/* 326 */     return "snappy-framed";
/*     */   }
/*     */   
/*     */   public static String getSnappyRaw() {
/* 330 */     return "snappy-raw";
/*     */   }
/*     */   
/*     */   public static String getXz() {
/* 334 */     return "xz";
/*     */   }
/*     */   
/*     */   public static String getZ() {
/* 338 */     return "z";
/*     */   }
/*     */   
/*     */   public static String getLZ4Framed() {
/* 342 */     return "lz4-framed";
/*     */   }
/*     */   
/*     */   public static String getLZ4Block() {
/* 346 */     return "lz4-block";
/*     */   }
/*     */   
/*     */   public static String getZstandard() {
/* 350 */     return "zstd";
/*     */   }
/*     */   
/*     */   static void putAll(Set<String> names, CompressorStreamProvider provider, TreeMap<String, CompressorStreamProvider> map) {
/* 354 */     names.forEach(name -> (CompressorStreamProvider)map.put(toKey(name), provider));
/*     */   }
/*     */   
/*     */   private static Iterable<CompressorStreamProvider> archiveStreamProviderIterable() {
/* 358 */     return ServiceLoader.load(CompressorStreamProvider.class, ClassLoader.getSystemClassLoader());
/*     */   }
/*     */   
/*     */   private static String toKey(String name) {
/* 362 */     return name.toUpperCase(Locale.ROOT);
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
/*     */   public CompressorStreamFactory() {
/* 393 */     this.decompressUntilEOF = null;
/* 394 */     this.memoryLimitInKb = -1;
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
/*     */   
/*     */   public CompressorStreamFactory(boolean decompressUntilEOF, int memoryLimitInKb) {
/* 414 */     this.decompressUntilEOF = Boolean.valueOf(decompressUntilEOF);
/*     */ 
/*     */     
/* 417 */     this.decompressConcatenated = decompressUntilEOF;
/* 418 */     this.memoryLimitInKb = memoryLimitInKb;
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
/*     */   public CompressorStreamFactory(boolean decompressUntilEOF) {
/* 432 */     this(decompressUntilEOF, -1);
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
/*     */   public static String detect(InputStream inputStream) throws CompressorException {
/* 447 */     if (inputStream == null) {
/* 448 */       throw new IllegalArgumentException("Stream must not be null.");
/*     */     }
/*     */     
/* 451 */     if (!inputStream.markSupported()) {
/* 452 */       throw new IllegalArgumentException("Mark is not supported.");
/*     */     }
/*     */     
/* 455 */     byte[] signature = new byte[12];
/* 456 */     inputStream.mark(signature.length);
/* 457 */     int signatureLength = -1;
/*     */     try {
/* 459 */       signatureLength = IOUtils.readFully(inputStream, signature);
/* 460 */       inputStream.reset();
/* 461 */     } catch (IOException e) {
/* 462 */       throw new CompressorException("IOException while reading signature.", e);
/*     */     } 
/*     */     
/* 465 */     if (BZip2CompressorInputStream.matches(signature, signatureLength)) {
/* 466 */       return "bzip2";
/*     */     }
/*     */     
/* 469 */     if (GzipCompressorInputStream.matches(signature, signatureLength)) {
/* 470 */       return "gz";
/*     */     }
/*     */     
/* 473 */     if (Pack200CompressorInputStream.matches(signature, signatureLength)) {
/* 474 */       return "pack200";
/*     */     }
/*     */     
/* 477 */     if (FramedSnappyCompressorInputStream.matches(signature, signatureLength)) {
/* 478 */       return "snappy-framed";
/*     */     }
/*     */     
/* 481 */     if (ZCompressorInputStream.matches(signature, signatureLength)) {
/* 482 */       return "z";
/*     */     }
/*     */     
/* 485 */     if (DeflateCompressorInputStream.matches(signature, signatureLength)) {
/* 486 */       return "deflate";
/*     */     }
/*     */     
/* 489 */     if (XZUtils.matches(signature, signatureLength)) {
/* 490 */       return "xz";
/*     */     }
/*     */     
/* 493 */     if (LZMAUtils.matches(signature, signatureLength)) {
/* 494 */       return "lzma";
/*     */     }
/*     */     
/* 497 */     if (FramedLZ4CompressorInputStream.matches(signature, signatureLength)) {
/* 498 */       return "lz4-framed";
/*     */     }
/*     */     
/* 501 */     if (ZstdUtils.matches(signature, signatureLength)) {
/* 502 */       return "zstd";
/*     */     }
/*     */     
/* 505 */     throw new CompressorException("No Compressor found for the stream signature.");
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
/*     */   public CompressorInputStream createCompressorInputStream(InputStream in) throws CompressorException {
/* 522 */     return createCompressorInputStream(detect(in), in);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompressorInputStream createCompressorInputStream(String name, InputStream in) throws CompressorException {
/* 548 */     return createCompressorInputStream(name, in, this.decompressConcatenated);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CompressorInputStream createCompressorInputStream(String name, InputStream in, boolean actualDecompressConcatenated) throws CompressorException {
/* 554 */     if (name == null || in == null) {
/* 555 */       throw new IllegalArgumentException("Compressor name and stream must not be null.");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 560 */       if ("gz".equalsIgnoreCase(name)) {
/* 561 */         return (CompressorInputStream)new GzipCompressorInputStream(in, actualDecompressConcatenated);
/*     */       }
/*     */       
/* 564 */       if ("bzip2".equalsIgnoreCase(name)) {
/* 565 */         return (CompressorInputStream)new BZip2CompressorInputStream(in, actualDecompressConcatenated);
/*     */       }
/*     */       
/* 568 */       if ("br".equalsIgnoreCase(name)) {
/* 569 */         if (!BrotliUtils.isBrotliCompressionAvailable()) {
/* 570 */           throw new CompressorException("Brotli compression is not available." + YOU_NEED_BROTLI_DEC);
/*     */         }
/* 572 */         return (CompressorInputStream)new BrotliCompressorInputStream(in);
/*     */       } 
/*     */       
/* 575 */       if ("xz".equalsIgnoreCase(name)) {
/* 576 */         if (!XZUtils.isXZCompressionAvailable()) {
/* 577 */           throw new CompressorException("XZ compression is not available." + YOU_NEED_XZ_JAVA);
/*     */         }
/* 579 */         return (CompressorInputStream)new XZCompressorInputStream(in, actualDecompressConcatenated, this.memoryLimitInKb);
/*     */       } 
/*     */       
/* 582 */       if ("zstd".equalsIgnoreCase(name)) {
/* 583 */         if (!ZstdUtils.isZstdCompressionAvailable()) {
/* 584 */           throw new CompressorException("Zstandard compression is not available." + YOU_NEED_ZSTD_JNI);
/*     */         }
/* 586 */         return (CompressorInputStream)new ZstdCompressorInputStream(in);
/*     */       } 
/*     */       
/* 589 */       if ("lzma".equalsIgnoreCase(name)) {
/* 590 */         if (!LZMAUtils.isLZMACompressionAvailable()) {
/* 591 */           throw new CompressorException("LZMA compression is not available" + YOU_NEED_XZ_JAVA);
/*     */         }
/* 593 */         return (CompressorInputStream)new LZMACompressorInputStream(in, this.memoryLimitInKb);
/*     */       } 
/*     */       
/* 596 */       if ("pack200".equalsIgnoreCase(name)) {
/* 597 */         return (CompressorInputStream)new Pack200CompressorInputStream(in);
/*     */       }
/*     */       
/* 600 */       if ("snappy-raw".equalsIgnoreCase(name)) {
/* 601 */         return (CompressorInputStream)new SnappyCompressorInputStream(in);
/*     */       }
/*     */       
/* 604 */       if ("snappy-framed".equalsIgnoreCase(name)) {
/* 605 */         return (CompressorInputStream)new FramedSnappyCompressorInputStream(in);
/*     */       }
/*     */       
/* 608 */       if ("z".equalsIgnoreCase(name)) {
/* 609 */         return (CompressorInputStream)new ZCompressorInputStream(in, this.memoryLimitInKb);
/*     */       }
/*     */       
/* 612 */       if ("deflate".equalsIgnoreCase(name)) {
/* 613 */         return (CompressorInputStream)new DeflateCompressorInputStream(in);
/*     */       }
/*     */       
/* 616 */       if ("deflate64".equalsIgnoreCase(name)) {
/* 617 */         return (CompressorInputStream)new Deflate64CompressorInputStream(in);
/*     */       }
/*     */       
/* 620 */       if ("lz4-block".equalsIgnoreCase(name)) {
/* 621 */         return (CompressorInputStream)new BlockLZ4CompressorInputStream(in);
/*     */       }
/*     */       
/* 624 */       if ("lz4-framed".equalsIgnoreCase(name)) {
/* 625 */         return (CompressorInputStream)new FramedLZ4CompressorInputStream(in, actualDecompressConcatenated);
/*     */       }
/*     */     }
/* 628 */     catch (IOException e) {
/* 629 */       throw new CompressorException("Could not create CompressorInputStream.", e);
/*     */     } 
/* 631 */     CompressorStreamProvider compressorStreamProvider = getCompressorInputStreamProviders().get(toKey(name));
/* 632 */     if (compressorStreamProvider != null) {
/* 633 */       return compressorStreamProvider.createCompressorInputStream(name, in, actualDecompressConcatenated);
/*     */     }
/*     */     
/* 636 */     throw new CompressorException("Compressor: " + name + " not found.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompressorOutputStream createCompressorOutputStream(String name, OutputStream out) throws CompressorException {
/* 659 */     if (name == null || out == null) {
/* 660 */       throw new IllegalArgumentException("Compressor name and stream must not be null.");
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 665 */       if ("gz".equalsIgnoreCase(name)) {
/* 666 */         return (CompressorOutputStream)new GzipCompressorOutputStream(out);
/*     */       }
/*     */       
/* 669 */       if ("bzip2".equalsIgnoreCase(name)) {
/* 670 */         return (CompressorOutputStream)new BZip2CompressorOutputStream(out);
/*     */       }
/*     */       
/* 673 */       if ("xz".equalsIgnoreCase(name)) {
/* 674 */         return (CompressorOutputStream)new XZCompressorOutputStream(out);
/*     */       }
/*     */       
/* 677 */       if ("pack200".equalsIgnoreCase(name)) {
/* 678 */         return (CompressorOutputStream)new Pack200CompressorOutputStream(out);
/*     */       }
/*     */       
/* 681 */       if ("lzma".equalsIgnoreCase(name)) {
/* 682 */         return (CompressorOutputStream)new LZMACompressorOutputStream(out);
/*     */       }
/*     */       
/* 685 */       if ("deflate".equalsIgnoreCase(name)) {
/* 686 */         return (CompressorOutputStream)new DeflateCompressorOutputStream(out);
/*     */       }
/*     */       
/* 689 */       if ("snappy-framed".equalsIgnoreCase(name)) {
/* 690 */         return (CompressorOutputStream)new FramedSnappyCompressorOutputStream(out);
/*     */       }
/*     */       
/* 693 */       if ("lz4-block".equalsIgnoreCase(name)) {
/* 694 */         return (CompressorOutputStream)new BlockLZ4CompressorOutputStream(out);
/*     */       }
/*     */       
/* 697 */       if ("lz4-framed".equalsIgnoreCase(name)) {
/* 698 */         return (CompressorOutputStream)new FramedLZ4CompressorOutputStream(out);
/*     */       }
/*     */       
/* 701 */       if ("zstd".equalsIgnoreCase(name)) {
/* 702 */         return (CompressorOutputStream)new ZstdCompressorOutputStream(out);
/*     */       }
/* 704 */     } catch (IOException e) {
/* 705 */       throw new CompressorException("Could not create CompressorOutputStream", e);
/*     */     } 
/* 707 */     CompressorStreamProvider compressorStreamProvider = getCompressorOutputStreamProviders().get(toKey(name));
/* 708 */     if (compressorStreamProvider != null) {
/* 709 */       return compressorStreamProvider.createCompressorOutputStream(name, out);
/*     */     }
/* 711 */     throw new CompressorException("Compressor: " + name + " not found.");
/*     */   }
/*     */   
/*     */   public SortedMap<String, CompressorStreamProvider> getCompressorInputStreamProviders() {
/* 715 */     if (this.compressorInputStreamProviders == null) {
/* 716 */       this
/* 717 */         .compressorInputStreamProviders = Collections.unmodifiableSortedMap(findAvailableCompressorInputStreamProviders());
/*     */     }
/* 719 */     return this.compressorInputStreamProviders;
/*     */   }
/*     */   
/*     */   public SortedMap<String, CompressorStreamProvider> getCompressorOutputStreamProviders() {
/* 723 */     if (this.compressorOutputStreamProviders == null) {
/* 724 */       this
/* 725 */         .compressorOutputStreamProviders = Collections.unmodifiableSortedMap(findAvailableCompressorOutputStreamProviders());
/*     */     }
/* 727 */     return this.compressorOutputStreamProviders;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean getDecompressConcatenated() {
/* 732 */     return this.decompressConcatenated;
/*     */   }
/*     */   
/*     */   public Boolean getDecompressUntilEOF() {
/* 736 */     return this.decompressUntilEOF;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getInputStreamCompressorNames() {
/* 741 */     return Sets.newHashSet((Object[])new String[] { "gz", "br", "bzip2", "xz", "lzma", "pack200", "deflate", "snappy-raw", "snappy-framed", "z", "lz4-block", "lz4-framed", "zstd", "deflate64" });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getOutputStreamCompressorNames() {
/* 747 */     return Sets.newHashSet((Object[])new String[] { "gz", "bzip2", "xz", "lzma", "pack200", "deflate", "snappy-framed", "lz4-block", "lz4-framed", "zstd" });
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setDecompressConcatenated(boolean decompressConcatenated) {
/* 771 */     if (this.decompressUntilEOF != null) {
/* 772 */       throw new IllegalStateException("Cannot override the setting defined by the constructor");
/*     */     }
/* 774 */     this.decompressConcatenated = decompressConcatenated;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\CompressorStreamFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */