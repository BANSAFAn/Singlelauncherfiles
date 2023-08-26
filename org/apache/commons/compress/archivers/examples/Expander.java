/*     */ package org.apache.commons.compress.archivers.examples;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.ArchiveException;
/*     */ import org.apache.commons.compress.archivers.ArchiveInputStream;
/*     */ import org.apache.commons.compress.archivers.ArchiveStreamFactory;
/*     */ import org.apache.commons.compress.archivers.sevenz.SevenZFile;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.tar.TarFile;
/*     */ import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.zip.ZipFile;
/*     */ import org.apache.commons.compress.utils.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Expander
/*     */ {
/*     */   private void expand(ArchiveEntrySupplier supplier, EntryWriter writer, Path targetDirectory) throws IOException {
/*  65 */     boolean nullTarget = (targetDirectory == null);
/*  66 */     Path targetDirPath = nullTarget ? null : targetDirectory.normalize();
/*  67 */     ArchiveEntry nextEntry = supplier.getNextReadableEntry();
/*  68 */     while (nextEntry != null) {
/*  69 */       Path targetPath = nullTarget ? null : targetDirectory.resolve(nextEntry.getName());
/*     */ 
/*     */       
/*  72 */       if (!nullTarget && !targetPath.normalize().startsWith(targetDirPath) && !Files.isSameFile(targetDirectory, targetPath)) {
/*  73 */         throw new IOException("Expanding " + nextEntry.getName() + " would create file outside of " + targetDirectory);
/*     */       }
/*  75 */       if (nextEntry.isDirectory()) {
/*  76 */         if (!nullTarget && !Files.isDirectory(targetPath, new java.nio.file.LinkOption[0]) && Files.createDirectories(targetPath, (FileAttribute<?>[])new FileAttribute[0]) == null) {
/*  77 */           throw new IOException("Failed to create directory " + targetPath);
/*     */         }
/*     */       } else {
/*  80 */         Path parent = nullTarget ? null : targetPath.getParent();
/*  81 */         if (!nullTarget && !Files.isDirectory(parent, new java.nio.file.LinkOption[0]) && Files.createDirectories(parent, (FileAttribute<?>[])new FileAttribute[0]) == null) {
/*  82 */           throw new IOException("Failed to create directory " + parent);
/*     */         }
/*  84 */         if (nullTarget) {
/*  85 */           writer.writeEntryDataTo(nextEntry, null);
/*     */         } else {
/*  87 */           try (OutputStream outputStream = Files.newOutputStream(targetPath, new OpenOption[0])) {
/*  88 */             writer.writeEntryDataTo(nextEntry, outputStream);
/*     */           } 
/*     */         } 
/*     */       } 
/*  92 */       nextEntry = supplier.getNextReadableEntry();
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
/*     */   public void expand(ArchiveInputStream archive, File targetDirectory) throws IOException {
/* 104 */     expand(archive, toPath(targetDirectory));
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
/*     */   public void expand(ArchiveInputStream archive, Path targetDirectory) throws IOException {
/* 116 */     expand(() -> { ArchiveEntry next = archive.getNextEntry(); while (next != null && !archive.canReadEntryData(next)) next = archive.getNextEntry();  return next; }(entry, out) -> IOUtils.copy((InputStream)archive, out), targetDirectory);
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
/*     */   public void expand(File archive, File targetDirectory) throws IOException, ArchiveException {
/* 136 */     expand(archive.toPath(), toPath(targetDirectory));
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
/*     */   @Deprecated
/*     */   public void expand(InputStream archive, File targetDirectory) throws IOException, ArchiveException {
/* 157 */     expand(archive, targetDirectory, CloseableConsumer.NULL_CONSUMER);
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
/*     */   public void expand(InputStream archive, File targetDirectory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
/* 181 */     try (CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer)) {
/* 182 */       expand(c.<ArchiveInputStream>track(ArchiveStreamFactory.DEFAULT.createArchiveInputStream(archive)), targetDirectory);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void expand(Path archive, Path targetDirectory) throws IOException, ArchiveException {
/* 199 */     String format = null;
/* 200 */     try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(archive, new OpenOption[0]))) {
/* 201 */       format = ArchiveStreamFactory.detect(inputStream);
/*     */     } 
/* 203 */     expand(format, archive, targetDirectory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void expand(SevenZFile archive, File targetDirectory) throws IOException {
/* 214 */     expand(archive, toPath(targetDirectory));
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
/*     */   public void expand(SevenZFile archive, Path targetDirectory) throws IOException {
/* 227 */     expand(archive::getNextEntry, (entry, out) -> { byte[] buffer = new byte[8192]; int n; while (-1 != (n = archive.read(buffer))) { if (out != null) out.write(buffer, 0, n);  }  }targetDirectory);
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
/*     */   public void expand(String format, File archive, File targetDirectory) throws IOException, ArchiveException {
/* 249 */     expand(format, archive.toPath(), toPath(targetDirectory));
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
/*     */   @Deprecated
/*     */   public void expand(String format, InputStream archive, File targetDirectory) throws IOException, ArchiveException {
/* 271 */     expand(format, archive, targetDirectory, CloseableConsumer.NULL_CONSUMER);
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
/*     */   public void expand(String format, InputStream archive, File targetDirectory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
/* 295 */     expand(format, archive, toPath(targetDirectory), closeableConsumer);
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
/*     */   public void expand(String format, InputStream archive, Path targetDirectory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
/* 319 */     try (CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer)) {
/* 320 */       expand(c.<ArchiveInputStream>track(ArchiveStreamFactory.DEFAULT.createArchiveInputStream(format, archive)), targetDirectory);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void expand(String format, Path archive, Path targetDirectory) throws IOException, ArchiveException {
/* 337 */     if (prefersSeekableByteChannel(format)) {
/* 338 */       try (SeekableByteChannel channel = FileChannel.open(archive, new OpenOption[] { StandardOpenOption.READ })) {
/* 339 */         expand(format, channel, targetDirectory, CloseableConsumer.CLOSING_CONSUMER);
/*     */       } 
/*     */       return;
/*     */     } 
/* 343 */     try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(archive, new OpenOption[0]))) {
/* 344 */       expand(format, inputStream, targetDirectory, CloseableConsumer.CLOSING_CONSUMER);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void expand(String format, SeekableByteChannel archive, File targetDirectory) throws IOException, ArchiveException {
/* 367 */     expand(format, archive, targetDirectory, CloseableConsumer.NULL_CONSUMER);
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
/*     */   public void expand(String format, SeekableByteChannel archive, File targetDirectory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
/* 391 */     expand(format, archive, toPath(targetDirectory), closeableConsumer);
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
/*     */   public void expand(String format, SeekableByteChannel archive, Path targetDirectory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
/* 416 */     try (CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer)) {
/* 417 */       if (!prefersSeekableByteChannel(format)) {
/* 418 */         expand(format, c.<InputStream>track(Channels.newInputStream(archive)), targetDirectory, CloseableConsumer.NULL_CONSUMER);
/* 419 */       } else if ("tar".equalsIgnoreCase(format)) {
/* 420 */         expand(c.<TarFile>track(new TarFile(archive)), targetDirectory);
/* 421 */       } else if ("zip".equalsIgnoreCase(format)) {
/* 422 */         expand(c.<ZipFile>track(new ZipFile(archive)), targetDirectory);
/* 423 */       } else if ("7z".equalsIgnoreCase(format)) {
/* 424 */         expand(c.<SevenZFile>track(new SevenZFile(archive)), targetDirectory);
/*     */       } else {
/*     */         
/* 427 */         throw new ArchiveException("Don't know how to handle format " + format);
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
/*     */ 
/*     */   
/*     */   public void expand(TarFile archive, File targetDirectory) throws IOException {
/* 441 */     expand(archive, toPath(targetDirectory));
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
/*     */   public void expand(TarFile archive, Path targetDirectory) throws IOException {
/* 454 */     Iterator<TarArchiveEntry> entryIterator = archive.getEntries().iterator();
/* 455 */     expand(() -> entryIterator.hasNext() ? entryIterator.next() : null, (entry, out) -> { try (InputStream in = archive.getInputStream((TarArchiveEntry)entry)) { IOUtils.copy(in, out); }  }targetDirectory);
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
/*     */   public void expand(ZipFile archive, File targetDirectory) throws IOException {
/* 471 */     expand(archive, toPath(targetDirectory));
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
/*     */   public void expand(ZipFile archive, Path targetDirectory) throws IOException {
/* 484 */     Enumeration<ZipArchiveEntry> entries = archive.getEntries();
/* 485 */     expand(() -> { ZipArchiveEntry next = entries.hasMoreElements() ? entries.nextElement() : null; while (next != null && !archive.canReadEntryData(next)) next = entries.hasMoreElements() ? entries.nextElement() : null;  return (ArchiveEntry)next; }(entry, out) -> { try (InputStream in = archive.getInputStream((ZipArchiveEntry)entry)) { IOUtils.copy(in, out); }  }targetDirectory);
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
/*     */   private boolean prefersSeekableByteChannel(String format) {
/* 499 */     return ("tar".equalsIgnoreCase(format) || "zip"
/* 500 */       .equalsIgnoreCase(format) || "7z"
/* 501 */       .equalsIgnoreCase(format));
/*     */   }
/*     */   
/*     */   private Path toPath(File targetDirectory) {
/* 505 */     return (targetDirectory != null) ? targetDirectory.toPath() : null;
/*     */   }
/*     */   
/*     */   private static interface EntryWriter {
/*     */     void writeEntryDataTo(ArchiveEntry param1ArchiveEntry, OutputStream param1OutputStream) throws IOException;
/*     */   }
/*     */   
/*     */   private static interface ArchiveEntrySupplier {
/*     */     ArchiveEntry getNextReadableEntry() throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\examples\Expander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */