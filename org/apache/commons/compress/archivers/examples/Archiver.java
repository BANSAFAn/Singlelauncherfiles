/*     */ package org.apache.commons.compress.archivers.examples;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.FileVisitOption;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.ArchiveException;
/*     */ import org.apache.commons.compress.archivers.ArchiveOutputStream;
/*     */ import org.apache.commons.compress.archivers.ArchiveStreamFactory;
/*     */ import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
/*     */ import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
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
/*     */ public class Archiver
/*     */ {
/*     */   private static class ArchiverFileVisitor
/*     */     extends SimpleFileVisitor<Path>
/*     */   {
/*     */     private final ArchiveOutputStream target;
/*     */     private final Path directory;
/*     */     private final LinkOption[] linkOptions;
/*     */     
/*     */     private ArchiverFileVisitor(ArchiveOutputStream target, Path directory, LinkOption... linkOptions) {
/*  62 */       this.target = target;
/*  63 */       this.directory = directory;
/*  64 */       this.linkOptions = (linkOptions == null) ? IOUtils.EMPTY_LINK_OPTIONS : (LinkOption[])linkOptions.clone();
/*     */     }
/*     */ 
/*     */     
/*     */     public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
/*  69 */       return visit(dir, attrs, false);
/*     */     }
/*     */ 
/*     */     
/*     */     protected FileVisitResult visit(Path path, BasicFileAttributes attrs, boolean isFile) throws IOException {
/*  74 */       Objects.requireNonNull(path);
/*  75 */       Objects.requireNonNull(attrs);
/*  76 */       String name = this.directory.relativize(path).toString().replace('\\', '/');
/*  77 */       if (!name.isEmpty()) {
/*  78 */         ArchiveEntry archiveEntry = this.target.createArchiveEntry(path, (isFile || name
/*  79 */             .endsWith("/")) ? name : (name + "/"), this.linkOptions);
/*  80 */         this.target.putArchiveEntry(archiveEntry);
/*  81 */         if (isFile)
/*     */         {
/*  83 */           Files.copy(path, (OutputStream)this.target);
/*     */         }
/*  85 */         this.target.closeArchiveEntry();
/*     */       } 
/*  87 */       return FileVisitResult.CONTINUE;
/*     */     }
/*     */ 
/*     */     
/*     */     public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/*  92 */       return visit(file, attrs, true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   public static final EnumSet<FileVisitOption> EMPTY_FileVisitOption = EnumSet.noneOf(FileVisitOption.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void create(ArchiveOutputStream target, File directory) throws IOException {
/* 109 */     create(target, directory.toPath(), EMPTY_FileVisitOption, new LinkOption[0]);
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
/*     */   public void create(ArchiveOutputStream target, Path directory, EnumSet<FileVisitOption> fileVisitOptions, LinkOption... linkOptions) throws IOException {
/* 124 */     Files.walkFileTree(directory, fileVisitOptions, 2147483647, new ArchiverFileVisitor(target, directory, linkOptions));
/*     */     
/* 126 */     target.finish();
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
/*     */   public void create(ArchiveOutputStream target, Path directory) throws IOException {
/* 138 */     create(target, directory, EMPTY_FileVisitOption, new LinkOption[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void create(SevenZOutputFile target, File directory) throws IOException {
/* 149 */     create(target, directory.toPath());
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
/*     */   public void create(final SevenZOutputFile target, final Path directory) throws IOException {
/* 162 */     Files.walkFileTree(directory, new ArchiverFileVisitor(null, directory, new LinkOption[0])
/*     */         {
/*     */           
/*     */           protected FileVisitResult visit(Path path, BasicFileAttributes attrs, boolean isFile) throws IOException
/*     */           {
/* 167 */             Objects.requireNonNull(path);
/* 168 */             Objects.requireNonNull(attrs);
/* 169 */             String name = directory.relativize(path).toString().replace('\\', '/');
/* 170 */             if (!name.isEmpty()) {
/* 171 */               SevenZArchiveEntry sevenZArchiveEntry = target.createArchiveEntry(path, (isFile || name
/* 172 */                   .endsWith("/")) ? name : (name + "/"), new LinkOption[0]);
/* 173 */               target.putArchiveEntry((ArchiveEntry)sevenZArchiveEntry);
/* 174 */               if (isFile)
/*     */               {
/* 176 */                 target.write(path, new OpenOption[0]);
/*     */               }
/* 178 */               target.closeArchiveEntry();
/*     */             } 
/* 180 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */         });
/*     */     
/* 184 */     target.finish();
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
/*     */   public void create(String format, File target, File directory) throws IOException, ArchiveException {
/* 199 */     create(format, target.toPath(), directory.toPath());
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
/*     */   public void create(String format, OutputStream target, File directory) throws IOException, ArchiveException {
/* 221 */     create(format, target, directory, CloseableConsumer.NULL_CONSUMER);
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
/*     */   public void create(String format, OutputStream target, File directory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
/* 245 */     try (CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer)) {
/* 246 */       create(c.<ArchiveOutputStream>track(ArchiveStreamFactory.DEFAULT.createArchiveOutputStream(format, target)), directory);
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
/*     */   public void create(String format, Path target, Path directory) throws IOException, ArchiveException {
/* 263 */     if (prefersSeekableByteChannel(format)) {
/* 264 */       try (SeekableByteChannel channel = FileChannel.open(target, new OpenOption[] { StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING })) {
/*     */         
/* 266 */         create(format, channel, directory);
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/* 271 */     try (ArchiveOutputStream outputStream = ArchiveStreamFactory.DEFAULT.createArchiveOutputStream(format, 
/* 272 */           Files.newOutputStream(target, new OpenOption[0]))) {
/* 273 */       create(outputStream, directory, EMPTY_FileVisitOption, new LinkOption[0]);
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
/*     */   public void create(String format, SeekableByteChannel target, File directory) throws IOException, ArchiveException {
/* 296 */     create(format, target, directory, CloseableConsumer.NULL_CONSUMER);
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
/*     */   public void create(String format, SeekableByteChannel target, File directory, CloseableConsumer closeableConsumer) throws IOException, ArchiveException {
/* 320 */     try (CloseableConsumerAdapter c = new CloseableConsumerAdapter(closeableConsumer)) {
/* 321 */       if (!prefersSeekableByteChannel(format)) {
/* 322 */         create(format, c.<OutputStream>track(Channels.newOutputStream(target)), directory);
/* 323 */       } else if ("zip".equalsIgnoreCase(format)) {
/* 324 */         create((ArchiveOutputStream)c.track(new ZipArchiveOutputStream(target)), directory);
/* 325 */       } else if ("7z".equalsIgnoreCase(format)) {
/* 326 */         create(c.<SevenZOutputFile>track(new SevenZOutputFile(target)), directory);
/*     */       } else {
/*     */         
/* 329 */         throw new ArchiveException("Don't know how to handle format " + format);
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
/*     */ 
/*     */   
/*     */   public void create(String format, SeekableByteChannel target, Path directory) throws IOException {
/* 345 */     if ("7z".equalsIgnoreCase(format)) {
/* 346 */       try (SevenZOutputFile sevenZFile = new SevenZOutputFile(target)) {
/* 347 */         create(sevenZFile, directory);
/*     */       } 
/* 349 */     } else if ("zip".equalsIgnoreCase(format)) {
/* 350 */       try (ZipArchiveOutputStream null = new ZipArchiveOutputStream(target)) {
/* 351 */         create((ArchiveOutputStream)zipArchiveOutputStream, directory, EMPTY_FileVisitOption, new LinkOption[0]);
/*     */       } 
/*     */     } else {
/* 354 */       throw new IllegalStateException(format);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean prefersSeekableByteChannel(String format) {
/* 359 */     return ("zip".equalsIgnoreCase(format) || "7z"
/* 360 */       .equalsIgnoreCase(format));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\examples\Archiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */