/*     */ package org.apache.commons.compress.archivers.zip;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.compress.utils.FileNameUtils;
/*     */ import org.apache.commons.compress.utils.MultiReadOnlySeekableByteChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ZipSplitReadOnlySeekableByteChannel
/*     */   extends MultiReadOnlySeekableByteChannel
/*     */ {
/*  52 */   private static final Path[] EMPTY_PATH_ARRAY = new Path[0];
/*     */   
/*     */   private static final int ZIP_SPLIT_SIGNATURE_LENGTH = 4;
/*  55 */   private final ByteBuffer zipSplitSignatureByteBuffer = ByteBuffer.allocate(4);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipSplitReadOnlySeekableByteChannel(List<SeekableByteChannel> channels) throws IOException {
/*  71 */     super(channels);
/*     */ 
/*     */     
/*  74 */     assertSplitSignature(channels);
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
/*     */   private void assertSplitSignature(List<SeekableByteChannel> channels) throws IOException {
/*  97 */     SeekableByteChannel channel = channels.get(0);
/*     */     
/*  99 */     channel.position(0L);
/*     */     
/* 101 */     this.zipSplitSignatureByteBuffer.rewind();
/* 102 */     channel.read(this.zipSplitSignatureByteBuffer);
/* 103 */     ZipLong signature = new ZipLong(this.zipSplitSignatureByteBuffer.array());
/* 104 */     if (!signature.equals(ZipLong.DD_SIG)) {
/* 105 */       channel.position(0L);
/* 106 */       throw new IOException("The first zip split segment does not begin with split zip file signature");
/*     */     } 
/*     */     
/* 109 */     channel.position(0L);
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
/*     */   public static SeekableByteChannel forOrderedSeekableByteChannels(SeekableByteChannel... channels) throws IOException {
/* 122 */     if (((SeekableByteChannel[])Objects.requireNonNull((T)channels, "channels must not be null")).length == 1) {
/* 123 */       return channels[0];
/*     */     }
/* 125 */     return (SeekableByteChannel)new ZipSplitReadOnlySeekableByteChannel(Arrays.asList(channels));
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
/*     */   public static SeekableByteChannel forOrderedSeekableByteChannels(SeekableByteChannel lastSegmentChannel, Iterable<SeekableByteChannel> channels) throws IOException {
/* 141 */     Objects.requireNonNull(channels, "channels");
/* 142 */     Objects.requireNonNull(lastSegmentChannel, "lastSegmentChannel");
/*     */     
/* 144 */     List<SeekableByteChannel> channelsList = new ArrayList<>();
/* 145 */     channels.forEach(channelsList::add);
/* 146 */     channelsList.add(lastSegmentChannel);
/*     */     
/* 148 */     return forOrderedSeekableByteChannels(channelsList.<SeekableByteChannel>toArray(new SeekableByteChannel[0]));
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
/*     */   public static SeekableByteChannel buildFromLastSplitSegment(File lastSegmentFile) throws IOException {
/* 161 */     return buildFromLastSplitSegment(lastSegmentFile.toPath());
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
/*     */   public static SeekableByteChannel buildFromLastSplitSegment(Path lastSegmentPath) throws IOException {
/*     */     ArrayList<Path> splitZipSegments;
/* 174 */     String extension = FileNameUtils.getExtension(lastSegmentPath);
/* 175 */     if (!extension.equalsIgnoreCase("zip")) {
/* 176 */       throw new IllegalArgumentException("The extension of last zip split segment should be .zip");
/*     */     }
/*     */ 
/*     */     
/* 180 */     Path parent = Objects.nonNull(lastSegmentPath.getParent()) ? lastSegmentPath.getParent() : lastSegmentPath.getFileSystem().getPath(".", new String[0]);
/* 181 */     String fileBaseName = FileNameUtils.getBaseName(lastSegmentPath);
/*     */ 
/*     */ 
/*     */     
/* 185 */     Pattern pattern = Pattern.compile(Pattern.quote(fileBaseName) + ".[zZ][0-9]+");
/* 186 */     try (Stream<Path> walk = Files.walk(parent, 1, new java.nio.file.FileVisitOption[0])) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 191 */       splitZipSegments = (ArrayList<Path>)walk.filter(x$0 -> Files.isRegularFile(x$0, new java.nio.file.LinkOption[0])).filter(path -> pattern.matcher(path.getFileName().toString()).matches()).sorted(new ZipSplitSegmentComparator()).collect(Collectors.toCollection(ArrayList::new));
/*     */     } 
/*     */     
/* 194 */     return forPaths(lastSegmentPath, splitZipSegments);
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
/*     */   public static SeekableByteChannel forFiles(File... files) throws IOException {
/* 209 */     List<Path> paths = new ArrayList<>();
/* 210 */     for (File f : (File[])Objects.<File[]>requireNonNull(files, "files must not be null")) {
/* 211 */       paths.add(f.toPath());
/*     */     }
/*     */     
/* 214 */     return forPaths(paths.<Path>toArray(EMPTY_PATH_ARRAY));
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
/*     */   public static SeekableByteChannel forPaths(Path... paths) throws IOException {
/* 229 */     List<SeekableByteChannel> channels = new ArrayList<>();
/* 230 */     for (Path path : (Path[])Objects.<Path[]>requireNonNull(paths, "paths must not be null")) {
/* 231 */       channels.add(Files.newByteChannel(path, new OpenOption[] { StandardOpenOption.READ }));
/*     */     } 
/* 233 */     if (channels.size() == 1) {
/* 234 */       return channels.get(0);
/*     */     }
/* 236 */     return (SeekableByteChannel)new ZipSplitReadOnlySeekableByteChannel(channels);
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
/*     */   public static SeekableByteChannel forFiles(File lastSegmentFile, Iterable<File> files) throws IOException {
/* 251 */     Objects.requireNonNull(files, "files");
/* 252 */     Objects.requireNonNull(lastSegmentFile, "lastSegmentFile");
/*     */     
/* 254 */     List<Path> filesList = new ArrayList<>();
/* 255 */     files.forEach(f -> filesList.add(f.toPath()));
/*     */     
/* 257 */     return forPaths(lastSegmentFile.toPath(), filesList);
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
/*     */   public static SeekableByteChannel forPaths(Path lastSegmentPath, Iterable<Path> paths) throws IOException {
/* 272 */     Objects.requireNonNull(paths, "paths");
/* 273 */     Objects.requireNonNull(lastSegmentPath, "lastSegmentPath");
/*     */     
/* 275 */     List<Path> filesList = new ArrayList<>();
/* 276 */     paths.forEach(filesList::add);
/* 277 */     filesList.add(lastSegmentPath);
/*     */     
/* 279 */     return forPaths(filesList.<Path>toArray(EMPTY_PATH_ARRAY));
/*     */   }
/*     */   
/*     */   private static class ZipSplitSegmentComparator
/*     */     implements Comparator<Path>, Serializable {
/*     */     private static final long serialVersionUID = 20200123L;
/*     */     
/*     */     public int compare(Path file1, Path file2) {
/* 287 */       String extension1 = FileNameUtils.getExtension(file1);
/* 288 */       String extension2 = FileNameUtils.getExtension(file2);
/*     */       
/* 290 */       if (!extension1.startsWith("z")) {
/* 291 */         return -1;
/*     */       }
/*     */       
/* 294 */       if (!extension2.startsWith("z")) {
/* 295 */         return 1;
/*     */       }
/*     */       
/* 298 */       Integer splitSegmentNumber1 = Integer.valueOf(Integer.parseInt(extension1.substring(1)));
/* 299 */       Integer splitSegmentNumber2 = Integer.valueOf(Integer.parseInt(extension2.substring(1)));
/*     */       
/* 301 */       return splitSegmentNumber1.compareTo(splitSegmentNumber2);
/*     */     }
/*     */     
/*     */     private ZipSplitSegmentComparator() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\ZipSplitReadOnlySeekableByteChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */