/*     */ package org.apache.commons.compress.archivers;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.Closeable;
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
/*     */ import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
/*     */ import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
/*     */ import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
/*     */ import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
/*     */ import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
/*     */ import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
/*     */ import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
/*     */ import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
/*     */ import org.apache.commons.compress.archivers.sevenz.SevenZFile;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
/*     */ import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
/*     */ import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArchiveStreamFactory
/*     */   implements ArchiveStreamProvider
/*     */ {
/*     */   private static final int TAR_HEADER_SIZE = 512;
/*     */   private static final int DUMP_SIGNATURE_SIZE = 32;
/*     */   private static final int SIGNATURE_SIZE = 12;
/*  98 */   public static final ArchiveStreamFactory DEFAULT = new ArchiveStreamFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String APK = "apk";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String XAPK = "xapk";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String APKS = "apks";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String APKM = "apkm";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String AR = "ar";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ARJ = "arj";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String CPIO = "cpio";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DUMP = "dump";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String JAR = "jar";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String TAR = "tar";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String ZIP = "zip";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String SEVEN_Z = "7z";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile String entryEncoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SortedMap<String, ArchiveStreamProvider> archiveInputStreamProviders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SortedMap<String, ArchiveStreamProvider> archiveOutputStreamProviders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void putAll(Set<String> names, ArchiveStreamProvider provider, TreeMap<String, ArchiveStreamProvider> map) {
/* 205 */     names.forEach(name -> (ArchiveStreamProvider)map.put(toKey(name), provider));
/*     */   }
/*     */   
/*     */   private static Iterable<ArchiveStreamProvider> archiveStreamProviderIterable() {
/* 209 */     return ServiceLoader.load(ArchiveStreamProvider.class, ClassLoader.getSystemClassLoader());
/*     */   }
/*     */   
/*     */   private static String toKey(String name) {
/* 213 */     return name.toUpperCase(Locale.ROOT);
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
/*     */   public static SortedMap<String, ArchiveStreamProvider> findAvailableArchiveInputStreamProviders() {
/* 244 */     return AccessController.<SortedMap<String, ArchiveStreamProvider>>doPrivileged(() -> {
/*     */           TreeMap<String, ArchiveStreamProvider> map = new TreeMap<>();
/*     */           putAll(DEFAULT.getInputStreamArchiveNames(), DEFAULT, map);
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
/*     */   public static SortedMap<String, ArchiveStreamProvider> findAvailableArchiveOutputStreamProviders() {
/* 280 */     return AccessController.<SortedMap<String, ArchiveStreamProvider>>doPrivileged(() -> {
/*     */           TreeMap<String, ArchiveStreamProvider> map = new TreeMap<>();
/*     */           putAll(DEFAULT.getOutputStreamArchiveNames(), DEFAULT, map);
/*     */           archiveStreamProviderIterable().forEach(());
/*     */           return map;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArchiveStreamFactory() {
/* 292 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArchiveStreamFactory(String encoding) {
/* 303 */     this.encoding = encoding;
/*     */     
/* 305 */     this.entryEncoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEntryEncoding() {
/* 316 */     return this.entryEncoding;
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
/*     */   @Deprecated
/*     */   public void setEntryEncoding(String entryEncoding) {
/* 331 */     if (this.encoding != null) {
/* 332 */       throw new IllegalStateException("Cannot overide encoding set by the constructor");
/*     */     }
/* 334 */     this.entryEncoding = entryEncoding;
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
/*     */   public ArchiveInputStream createArchiveInputStream(String archiverName, InputStream in) throws ArchiveException {
/* 350 */     return createArchiveInputStream(archiverName, in, this.entryEncoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArchiveInputStream createArchiveInputStream(String archiverName, InputStream in, String actualEncoding) throws ArchiveException {
/* 357 */     if (archiverName == null) {
/* 358 */       throw new IllegalArgumentException("Archivername must not be null.");
/*     */     }
/*     */     
/* 361 */     if (in == null) {
/* 362 */       throw new IllegalArgumentException("InputStream must not be null.");
/*     */     }
/*     */     
/* 365 */     if ("ar".equalsIgnoreCase(archiverName)) {
/* 366 */       return (ArchiveInputStream)new ArArchiveInputStream(in);
/*     */     }
/* 368 */     if ("arj".equalsIgnoreCase(archiverName)) {
/* 369 */       if (actualEncoding != null) {
/* 370 */         return (ArchiveInputStream)new ArjArchiveInputStream(in, actualEncoding);
/*     */       }
/* 372 */       return (ArchiveInputStream)new ArjArchiveInputStream(in);
/*     */     } 
/* 374 */     if ("zip".equalsIgnoreCase(archiverName)) {
/* 375 */       if (actualEncoding != null) {
/* 376 */         return (ArchiveInputStream)new ZipArchiveInputStream(in, actualEncoding);
/*     */       }
/* 378 */       return (ArchiveInputStream)new ZipArchiveInputStream(in);
/*     */     } 
/* 380 */     if ("tar".equalsIgnoreCase(archiverName)) {
/* 381 */       if (actualEncoding != null) {
/* 382 */         return (ArchiveInputStream)new TarArchiveInputStream(in, actualEncoding);
/*     */       }
/* 384 */       return (ArchiveInputStream)new TarArchiveInputStream(in);
/*     */     } 
/* 386 */     if ("jar".equalsIgnoreCase(archiverName) || "apk".equalsIgnoreCase(archiverName)) {
/* 387 */       if (actualEncoding != null) {
/* 388 */         return (ArchiveInputStream)new JarArchiveInputStream(in, actualEncoding);
/*     */       }
/* 390 */       return (ArchiveInputStream)new JarArchiveInputStream(in);
/*     */     } 
/* 392 */     if ("cpio".equalsIgnoreCase(archiverName)) {
/* 393 */       if (actualEncoding != null) {
/* 394 */         return (ArchiveInputStream)new CpioArchiveInputStream(in, actualEncoding);
/*     */       }
/* 396 */       return (ArchiveInputStream)new CpioArchiveInputStream(in);
/*     */     } 
/* 398 */     if ("dump".equalsIgnoreCase(archiverName)) {
/* 399 */       if (actualEncoding != null) {
/* 400 */         return (ArchiveInputStream)new DumpArchiveInputStream(in, actualEncoding);
/*     */       }
/* 402 */       return (ArchiveInputStream)new DumpArchiveInputStream(in);
/*     */     } 
/* 404 */     if ("7z".equalsIgnoreCase(archiverName)) {
/* 405 */       throw new StreamingNotSupportedException("7z");
/*     */     }
/*     */     
/* 408 */     ArchiveStreamProvider archiveStreamProvider = getArchiveInputStreamProviders().get(toKey(archiverName));
/* 409 */     if (archiveStreamProvider != null) {
/* 410 */       return archiveStreamProvider.createArchiveInputStream(archiverName, in, actualEncoding);
/*     */     }
/*     */     
/* 413 */     throw new ArchiveException("Archiver: " + archiverName + " not found.");
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
/*     */   public ArchiveOutputStream createArchiveOutputStream(String archiverName, OutputStream out) throws ArchiveException {
/* 430 */     return createArchiveOutputStream(archiverName, out, this.entryEncoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArchiveOutputStream createArchiveOutputStream(String archiverName, OutputStream out, String actualEncoding) throws ArchiveException {
/* 437 */     if (archiverName == null) {
/* 438 */       throw new IllegalArgumentException("Archivername must not be null.");
/*     */     }
/* 440 */     if (out == null) {
/* 441 */       throw new IllegalArgumentException("OutputStream must not be null.");
/*     */     }
/*     */     
/* 444 */     if ("ar".equalsIgnoreCase(archiverName)) {
/* 445 */       return (ArchiveOutputStream)new ArArchiveOutputStream(out);
/*     */     }
/* 447 */     if ("zip".equalsIgnoreCase(archiverName)) {
/* 448 */       ZipArchiveOutputStream zip = new ZipArchiveOutputStream(out);
/* 449 */       if (actualEncoding != null) {
/* 450 */         zip.setEncoding(actualEncoding);
/*     */       }
/* 452 */       return (ArchiveOutputStream)zip;
/*     */     } 
/* 454 */     if ("tar".equalsIgnoreCase(archiverName)) {
/* 455 */       if (actualEncoding != null) {
/* 456 */         return (ArchiveOutputStream)new TarArchiveOutputStream(out, actualEncoding);
/*     */       }
/* 458 */       return (ArchiveOutputStream)new TarArchiveOutputStream(out);
/*     */     } 
/* 460 */     if ("jar".equalsIgnoreCase(archiverName)) {
/* 461 */       if (actualEncoding != null) {
/* 462 */         return (ArchiveOutputStream)new JarArchiveOutputStream(out, actualEncoding);
/*     */       }
/* 464 */       return (ArchiveOutputStream)new JarArchiveOutputStream(out);
/*     */     } 
/* 466 */     if ("cpio".equalsIgnoreCase(archiverName)) {
/* 467 */       if (actualEncoding != null) {
/* 468 */         return (ArchiveOutputStream)new CpioArchiveOutputStream(out, actualEncoding);
/*     */       }
/* 470 */       return (ArchiveOutputStream)new CpioArchiveOutputStream(out);
/*     */     } 
/* 472 */     if ("7z".equalsIgnoreCase(archiverName)) {
/* 473 */       throw new StreamingNotSupportedException("7z");
/*     */     }
/*     */     
/* 476 */     ArchiveStreamProvider archiveStreamProvider = getArchiveOutputStreamProviders().get(toKey(archiverName));
/* 477 */     if (archiveStreamProvider != null) {
/* 478 */       return archiveStreamProvider.createArchiveOutputStream(archiverName, out, actualEncoding);
/*     */     }
/*     */     
/* 481 */     throw new ArchiveException("Archiver: " + archiverName + " not found.");
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
/*     */   public ArchiveInputStream createArchiveInputStream(InputStream in) throws ArchiveException {
/* 498 */     return createArchiveInputStream(detect(in), in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String detect(InputStream in) throws ArchiveException {
/* 509 */     if (in == null) {
/* 510 */       throw new IllegalArgumentException("Stream must not be null.");
/*     */     }
/*     */     
/* 513 */     if (!in.markSupported()) {
/* 514 */       throw new IllegalArgumentException("Mark is not supported.");
/*     */     }
/*     */     
/* 517 */     byte[] signature = new byte[12];
/* 518 */     in.mark(signature.length);
/* 519 */     int signatureLength = -1;
/*     */     try {
/* 521 */       signatureLength = IOUtils.readFully(in, signature);
/* 522 */       in.reset();
/* 523 */     } catch (IOException e) {
/* 524 */       throw new ArchiveException("IOException while reading signature.", e);
/*     */     } 
/*     */     
/* 527 */     if (ZipArchiveInputStream.matches(signature, signatureLength)) {
/* 528 */       return "zip";
/*     */     }
/* 530 */     if (JarArchiveInputStream.matches(signature, signatureLength)) {
/* 531 */       return "jar";
/*     */     }
/* 533 */     if (ArArchiveInputStream.matches(signature, signatureLength)) {
/* 534 */       return "ar";
/*     */     }
/* 536 */     if (CpioArchiveInputStream.matches(signature, signatureLength)) {
/* 537 */       return "cpio";
/*     */     }
/* 539 */     if (ArjArchiveInputStream.matches(signature, signatureLength)) {
/* 540 */       return "arj";
/*     */     }
/* 542 */     if (SevenZFile.matches(signature, signatureLength)) {
/* 543 */       return "7z";
/*     */     }
/*     */ 
/*     */     
/* 547 */     byte[] dumpsig = new byte[32];
/* 548 */     in.mark(dumpsig.length);
/*     */     try {
/* 550 */       signatureLength = IOUtils.readFully(in, dumpsig);
/* 551 */       in.reset();
/* 552 */     } catch (IOException e) {
/* 553 */       throw new ArchiveException("IOException while reading dump signature", e);
/*     */     } 
/* 555 */     if (DumpArchiveInputStream.matches(dumpsig, signatureLength)) {
/* 556 */       return "dump";
/*     */     }
/*     */ 
/*     */     
/* 560 */     byte[] tarHeader = new byte[512];
/* 561 */     in.mark(tarHeader.length);
/*     */     try {
/* 563 */       signatureLength = IOUtils.readFully(in, tarHeader);
/* 564 */       in.reset();
/* 565 */     } catch (IOException e) {
/* 566 */       throw new ArchiveException("IOException while reading tar signature", e);
/*     */     } 
/* 568 */     if (TarArchiveInputStream.matches(tarHeader, signatureLength)) {
/* 569 */       return "tar";
/*     */     }
/*     */ 
/*     */     
/* 573 */     if (signatureLength >= 512) {
/* 574 */       TarArchiveInputStream tais = null;
/*     */       try {
/* 576 */         tais = new TarArchiveInputStream(new ByteArrayInputStream(tarHeader));
/*     */         
/* 578 */         if (tais.getNextTarEntry().isCheckSumOK()) {
/* 579 */           return "tar";
/*     */         }
/* 581 */       } catch (Exception exception) {
/*     */ 
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 587 */         IOUtils.closeQuietly((Closeable)tais);
/*     */       } 
/*     */     } 
/* 590 */     throw new ArchiveException("No Archiver found for the stream signature");
/*     */   }
/*     */   
/*     */   public SortedMap<String, ArchiveStreamProvider> getArchiveInputStreamProviders() {
/* 594 */     if (this.archiveInputStreamProviders == null) {
/* 595 */       this
/* 596 */         .archiveInputStreamProviders = Collections.unmodifiableSortedMap(findAvailableArchiveInputStreamProviders());
/*     */     }
/* 598 */     return this.archiveInputStreamProviders;
/*     */   }
/*     */   
/*     */   public SortedMap<String, ArchiveStreamProvider> getArchiveOutputStreamProviders() {
/* 602 */     if (this.archiveOutputStreamProviders == null) {
/* 603 */       this
/* 604 */         .archiveOutputStreamProviders = Collections.unmodifiableSortedMap(findAvailableArchiveOutputStreamProviders());
/*     */     }
/* 606 */     return this.archiveOutputStreamProviders;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getInputStreamArchiveNames() {
/* 611 */     return Sets.newHashSet((Object[])new String[] { "ar", "arj", "zip", "tar", "jar", "cpio", "dump", "7z" });
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getOutputStreamArchiveNames() {
/* 616 */     return Sets.newHashSet((Object[])new String[] { "ar", "zip", "tar", "jar", "cpio", "7z" });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\ArchiveStreamFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */