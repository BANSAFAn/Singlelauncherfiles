/*      */ package org.apache.commons.compress.archivers.tar;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.UncheckedIOException;
/*      */ import java.math.BigDecimal;
/*      */ import java.nio.file.DirectoryStream;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.LinkOption;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.BasicFileAttributes;
/*      */ import java.nio.file.attribute.DosFileAttributes;
/*      */ import java.nio.file.attribute.FileTime;
/*      */ import java.nio.file.attribute.PosixFileAttributes;
/*      */ import java.time.Instant;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*      */ import org.apache.commons.compress.archivers.EntryStreamOffsets;
/*      */ import org.apache.commons.compress.archivers.zip.ZipEncoding;
/*      */ import org.apache.commons.compress.utils.ArchiveUtils;
/*      */ import org.apache.commons.compress.utils.IOUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class TarArchiveEntry
/*      */   implements ArchiveEntry, TarConstants, EntryStreamOffsets
/*      */ {
/*  194 */   private static final TarArchiveEntry[] EMPTY_TAR_ARCHIVE_ENTRY_ARRAY = new TarArchiveEntry[0];
/*      */ 
/*      */ 
/*      */   
/*      */   public static final long UNKNOWN = -1L;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final int MAX_NAMELEN = 31;
/*      */ 
/*      */   
/*      */   public static final int DEFAULT_DIR_MODE = 16877;
/*      */ 
/*      */   
/*      */   public static final int DEFAULT_FILE_MODE = 33188;
/*      */ 
/*      */   
/*      */   public static final int MILLIS_PER_SECOND = 1000;
/*      */ 
/*      */ 
/*      */   
/*      */   private static FileTime fileTimeFromOptionalSeconds(long seconds) {
/*  216 */     if (seconds <= 0L) {
/*  217 */       return null;
/*      */     }
/*  219 */     return FileTime.from(seconds, TimeUnit.SECONDS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String normalizeFileName(String fileName, boolean preserveAbsolutePath) {
/*  226 */     if (!preserveAbsolutePath) {
/*  227 */       String property = System.getProperty("os.name");
/*  228 */       if (property != null) {
/*  229 */         String osName = property.toLowerCase(Locale.ROOT);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  234 */         if (osName.startsWith("windows")) {
/*  235 */           if (fileName.length() > 2) {
/*  236 */             char ch1 = fileName.charAt(0);
/*  237 */             char ch2 = fileName.charAt(1);
/*      */             
/*  239 */             if (ch2 == ':' && ((ch1 >= 'a' && ch1 <= 'z') || (ch1 >= 'A' && ch1 <= 'Z'))) {
/*  240 */               fileName = fileName.substring(2);
/*      */             }
/*      */           } 
/*  243 */         } else if (osName.contains("netware")) {
/*  244 */           int colon = fileName.indexOf(':');
/*  245 */           if (colon != -1) {
/*  246 */             fileName = fileName.substring(colon + 1);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  252 */     fileName = fileName.replace(File.separatorChar, '/');
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  257 */     while (!preserveAbsolutePath && fileName.startsWith("/")) {
/*  258 */       fileName = fileName.substring(1);
/*      */     }
/*  260 */     return fileName;
/*      */   }
/*      */   
/*      */   private static Instant parseInstantFromDecimalSeconds(String value) {
/*  264 */     BigDecimal epochSeconds = new BigDecimal(value);
/*  265 */     long seconds = epochSeconds.longValue();
/*  266 */     long nanos = epochSeconds.remainder(BigDecimal.ONE).movePointRight(9).longValue();
/*  267 */     return Instant.ofEpochSecond(seconds, nanos);
/*      */   }
/*      */ 
/*      */   
/*  271 */   private String name = "";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean preserveAbsolutePath;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int mode;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long userId;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long groupId;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long size;
/*      */ 
/*      */ 
/*      */   
/*      */   private FileTime mTime;
/*      */ 
/*      */ 
/*      */   
/*      */   private FileTime cTime;
/*      */ 
/*      */ 
/*      */   
/*      */   private FileTime aTime;
/*      */ 
/*      */ 
/*      */   
/*      */   private FileTime birthTime;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkSumOK;
/*      */ 
/*      */ 
/*      */   
/*      */   private byte linkFlag;
/*      */ 
/*      */ 
/*      */   
/*  324 */   private String linkName = "";
/*      */ 
/*      */   
/*  327 */   private String magic = "ustar\000";
/*      */ 
/*      */   
/*  330 */   private String version = "00";
/*      */ 
/*      */   
/*      */   private String userName;
/*      */ 
/*      */   
/*  336 */   private String groupName = "";
/*      */ 
/*      */ 
/*      */   
/*      */   private int devMajor;
/*      */ 
/*      */   
/*      */   private int devMinor;
/*      */ 
/*      */   
/*      */   private List<TarArchiveStructSparse> sparseHeaders;
/*      */ 
/*      */   
/*      */   private boolean isExtended;
/*      */ 
/*      */   
/*      */   private long realSize;
/*      */ 
/*      */   
/*      */   private boolean paxGNUSparse;
/*      */ 
/*      */   
/*      */   private boolean paxGNU1XSparse;
/*      */ 
/*      */   
/*      */   private boolean starSparse;
/*      */ 
/*      */   
/*      */   private final Path file;
/*      */ 
/*      */   
/*      */   private final LinkOption[] linkOptions;
/*      */ 
/*      */   
/*  370 */   private final Map<String, String> extraPaxHeaders = new HashMap<>();
/*      */   
/*  372 */   private long dataOffset = -1L;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TarArchiveEntry(boolean preserveAbsolutePath) {
/*  378 */     String user = System.getProperty("user.name", "");
/*      */     
/*  380 */     if (user.length() > 31) {
/*  381 */       user = user.substring(0, 31);
/*      */     }
/*      */     
/*  384 */     this.userName = user;
/*  385 */     this.file = null;
/*  386 */     this.linkOptions = IOUtils.EMPTY_LINK_OPTIONS;
/*  387 */     this.preserveAbsolutePath = preserveAbsolutePath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(byte[] headerBuf) {
/*  398 */     this(false);
/*  399 */     parseTarHeader(headerBuf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(byte[] headerBuf, ZipEncoding encoding) throws IOException {
/*  414 */     this(headerBuf, encoding, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(byte[] headerBuf, ZipEncoding encoding, boolean lenient) throws IOException {
/*  431 */     this(Collections.emptyMap(), headerBuf, encoding, lenient);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(byte[] headerBuf, ZipEncoding encoding, boolean lenient, long dataOffset) throws IOException {
/*  447 */     this(headerBuf, encoding, lenient);
/*  448 */     setDataOffset(dataOffset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(File file) {
/*  470 */     this(file, file.getPath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(File file, String fileName) {
/*  492 */     String normalizedName = normalizeFileName(fileName, false);
/*  493 */     this.file = file.toPath();
/*  494 */     this.linkOptions = IOUtils.EMPTY_LINK_OPTIONS;
/*      */     
/*      */     try {
/*  497 */       readFileMode(this.file, normalizedName, new LinkOption[0]);
/*  498 */     } catch (IOException e) {
/*      */ 
/*      */       
/*  501 */       if (!file.isDirectory()) {
/*  502 */         this.size = file.length();
/*      */       }
/*      */     } 
/*      */     
/*  506 */     this.userName = "";
/*      */     try {
/*  508 */       readOsSpecificProperties(this.file, new LinkOption[0]);
/*  509 */     } catch (IOException e) {
/*      */ 
/*      */       
/*  512 */       this.mTime = FileTime.fromMillis(file.lastModified());
/*      */     } 
/*  514 */     this.preserveAbsolutePath = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(Map<String, String> globalPaxHeaders, byte[] headerBuf, ZipEncoding encoding, boolean lenient) throws IOException {
/*  531 */     this(false);
/*  532 */     parseTarHeader(globalPaxHeaders, headerBuf, encoding, false, lenient);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(Map<String, String> globalPaxHeaders, byte[] headerBuf, ZipEncoding encoding, boolean lenient, long dataOffset) throws IOException {
/*  549 */     this(globalPaxHeaders, headerBuf, encoding, lenient);
/*  550 */     setDataOffset(dataOffset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(Path file) throws IOException {
/*  569 */     this(file, file.toString(), new LinkOption[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(Path file, String fileName, LinkOption... linkOptions) throws IOException {
/*  589 */     String normalizedName = normalizeFileName(fileName, false);
/*  590 */     this.file = file;
/*  591 */     this.linkOptions = (linkOptions == null) ? IOUtils.EMPTY_LINK_OPTIONS : linkOptions;
/*      */     
/*  593 */     readFileMode(file, normalizedName, linkOptions);
/*      */     
/*  595 */     this.userName = "";
/*  596 */     readOsSpecificProperties(file, new LinkOption[0]);
/*  597 */     this.preserveAbsolutePath = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(String name) {
/*  611 */     this(name, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(String name, boolean preserveAbsolutePath) {
/*  630 */     this(preserveAbsolutePath);
/*      */     
/*  632 */     name = normalizeFileName(name, preserveAbsolutePath);
/*  633 */     boolean isDir = name.endsWith("/");
/*      */     
/*  635 */     this.name = name;
/*  636 */     this.mode = isDir ? 16877 : 33188;
/*  637 */     this.linkFlag = isDir ? 53 : 48;
/*  638 */     this.mTime = FileTime.from(Instant.now());
/*  639 */     this.userName = "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(String name, byte linkFlag) {
/*  654 */     this(name, linkFlag, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry(String name, byte linkFlag, boolean preserveAbsolutePath) {
/*  673 */     this(name, preserveAbsolutePath);
/*  674 */     this.linkFlag = linkFlag;
/*  675 */     if (linkFlag == 76) {
/*  676 */       this.magic = "ustar ";
/*  677 */       this.version = " \000";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addPaxHeader(String name, String value) {
/*      */     try {
/*  690 */       processPaxHeader(name, value);
/*  691 */     } catch (IOException ex) {
/*  692 */       throw new IllegalArgumentException("Invalid input", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearExtraPaxHeaders() {
/*  701 */     this.extraPaxHeaders.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object it) {
/*  713 */     if (it == null || getClass() != it.getClass()) {
/*  714 */       return false;
/*      */     }
/*  716 */     return equals((TarArchiveEntry)it);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(TarArchiveEntry it) {
/*  727 */     return (it != null && getName().equals(it.getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int evaluateType(Map<String, String> globalPaxHeaders, byte[] header) {
/*  737 */     if (ArchiveUtils.matchAsciiBuffer("ustar ", header, 257, 6)) {
/*  738 */       return 2;
/*      */     }
/*  740 */     if (ArchiveUtils.matchAsciiBuffer("ustar\000", header, 257, 6)) {
/*  741 */       if (isXstar(globalPaxHeaders, header)) {
/*  742 */         return 4;
/*      */       }
/*  744 */       return 3;
/*      */     } 
/*  746 */     return 0;
/*      */   }
/*      */   
/*      */   private int fill(byte value, int offset, byte[] outbuf, int length) {
/*  750 */     for (int i = 0; i < length; i++) {
/*  751 */       outbuf[offset + i] = value;
/*      */     }
/*  753 */     return offset + length;
/*      */   }
/*      */   
/*      */   private int fill(int value, int offset, byte[] outbuf, int length) {
/*  757 */     return fill((byte)value, offset, outbuf, length);
/*      */   }
/*      */   
/*      */   void fillGNUSparse0xData(Map<String, String> headers) {
/*  761 */     this.paxGNUSparse = true;
/*  762 */     this.realSize = Integer.parseInt(headers.get("GNU.sparse.size"));
/*  763 */     if (headers.containsKey("GNU.sparse.name"))
/*      */     {
/*  765 */       this.name = headers.get("GNU.sparse.name");
/*      */     }
/*      */   }
/*      */   
/*      */   void fillGNUSparse1xData(Map<String, String> headers) throws IOException {
/*  770 */     this.paxGNUSparse = true;
/*  771 */     this.paxGNU1XSparse = true;
/*  772 */     if (headers.containsKey("GNU.sparse.name")) {
/*  773 */       this.name = headers.get("GNU.sparse.name");
/*      */     }
/*  775 */     if (headers.containsKey("GNU.sparse.realsize")) {
/*      */       try {
/*  777 */         this.realSize = Integer.parseInt(headers.get("GNU.sparse.realsize"));
/*  778 */       } catch (NumberFormatException ex) {
/*  779 */         throw new IOException("Corrupted TAR archive. GNU.sparse.realsize header for " + this.name + " contains non-numeric value");
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void fillStarSparseData(Map<String, String> headers) throws IOException {
/*  786 */     this.starSparse = true;
/*  787 */     if (headers.containsKey("SCHILY.realsize")) {
/*      */       try {
/*  789 */         this.realSize = Long.parseLong(headers.get("SCHILY.realsize"));
/*  790 */       } catch (NumberFormatException ex) {
/*  791 */         throw new IOException("Corrupted TAR archive. SCHILY.realsize header for " + this.name + " contains non-numeric value");
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FileTime getCreationTime() {
/*  804 */     return this.birthTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getDataOffset() {
/*  813 */     return this.dataOffset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDevMajor() {
/*  823 */     return this.devMajor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getDevMinor() {
/*  833 */     return this.devMinor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TarArchiveEntry[] getDirectoryEntries() {
/*  846 */     if (this.file == null || !isDirectory()) {
/*  847 */       return EMPTY_TAR_ARCHIVE_ENTRY_ARRAY;
/*      */     }
/*      */     
/*  850 */     List<TarArchiveEntry> entries = new ArrayList<>();
/*  851 */     try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(this.file)) {
/*  852 */       for (Path p : dirStream) {
/*  853 */         entries.add(new TarArchiveEntry(p));
/*      */       }
/*  855 */     } catch (IOException e) {
/*  856 */       return EMPTY_TAR_ARCHIVE_ENTRY_ARRAY;
/*      */     } 
/*  858 */     return entries.<TarArchiveEntry>toArray(EMPTY_TAR_ARCHIVE_ENTRY_ARRAY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getExtraPaxHeader(String name) {
/*  868 */     return this.extraPaxHeaders.get(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, String> getExtraPaxHeaders() {
/*  877 */     return Collections.unmodifiableMap(this.extraPaxHeaders);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File getFile() {
/*  889 */     if (this.file == null) {
/*  890 */       return null;
/*      */     }
/*  892 */     return this.file.toFile();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getGroupId() {
/*  904 */     return (int)(this.groupId & 0xFFFFFFFFFFFFFFFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getGroupName() {
/*  913 */     return this.groupName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FileTime getLastAccessTime() {
/*  923 */     return this.aTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getLastModifiedDate() {
/*  935 */     return getModTime();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FileTime getLastModifiedTime() {
/*  945 */     return this.mTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getLinkName() {
/*  954 */     return this.linkName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLongGroupId() {
/*  964 */     return this.groupId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLongUserId() {
/*  974 */     return this.userId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMode() {
/*  983 */     return this.mode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Date getModTime() {
/*  994 */     return new Date(this.mTime.toMillis());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/* 1006 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<TarArchiveStructSparse> getOrderedSparseHeaders() throws IOException {
/* 1017 */     if (this.sparseHeaders == null || this.sparseHeaders.isEmpty()) {
/* 1018 */       return Collections.emptyList();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1023 */     List<TarArchiveStructSparse> orderedAndFiltered = (List<TarArchiveStructSparse>)this.sparseHeaders.stream().filter(s -> (s.getOffset() > 0L || s.getNumbytes() > 0L)).sorted(Comparator.comparingLong(TarArchiveStructSparse::getOffset)).collect(Collectors.toList());
/*      */     
/* 1025 */     int numberOfHeaders = orderedAndFiltered.size();
/* 1026 */     for (int i = 0; i < numberOfHeaders; i++) {
/* 1027 */       TarArchiveStructSparse str = orderedAndFiltered.get(i);
/* 1028 */       if (i + 1 < numberOfHeaders && str
/* 1029 */         .getOffset() + str.getNumbytes() > ((TarArchiveStructSparse)orderedAndFiltered.get(i + 1)).getOffset()) {
/* 1030 */         throw new IOException("Corrupted TAR archive. Sparse blocks for " + 
/* 1031 */             getName() + " overlap each other.");
/*      */       }
/* 1033 */       if (str.getOffset() + str.getNumbytes() < 0L)
/*      */       {
/* 1035 */         throw new IOException("Unreadable TAR archive. Offset and numbytes for sparse block in " + 
/* 1036 */             getName() + " too large.");
/*      */       }
/*      */     } 
/* 1039 */     if (!orderedAndFiltered.isEmpty()) {
/* 1040 */       TarArchiveStructSparse last = orderedAndFiltered.get(numberOfHeaders - 1);
/* 1041 */       if (last.getOffset() + last.getNumbytes() > getRealSize()) {
/* 1042 */         throw new IOException("Corrupted TAR archive. Sparse block extends beyond real size of the entry");
/*      */       }
/*      */     } 
/*      */     
/* 1046 */     return orderedAndFiltered;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getPath() {
/* 1059 */     return this.file;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getRealSize() {
/* 1072 */     if (!isSparse()) {
/* 1073 */       return getSize();
/*      */     }
/* 1075 */     return this.realSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getSize() {
/* 1088 */     return this.size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<TarArchiveStructSparse> getSparseHeaders() {
/* 1098 */     return this.sparseHeaders;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FileTime getStatusChangeTime() {
/* 1108 */     return this.cTime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public int getUserId() {
/* 1120 */     return (int)(this.userId & 0xFFFFFFFFFFFFFFFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getUserName() {
/* 1129 */     return this.userName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1139 */     return getName().hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBlockDevice() {
/* 1149 */     return (this.linkFlag == 52);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCharacterDevice() {
/* 1159 */     return (this.linkFlag == 51);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCheckSumOK() {
/* 1170 */     return this.checkSumOK;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDescendent(TarArchiveEntry desc) {
/* 1182 */     return desc.getName().startsWith(getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDirectory() {
/* 1192 */     if (this.file != null) {
/* 1193 */       return Files.isDirectory(this.file, this.linkOptions);
/*      */     }
/*      */     
/* 1196 */     if (this.linkFlag == 53) {
/* 1197 */       return true;
/*      */     }
/*      */     
/* 1200 */     return (!isPaxHeader() && !isGlobalPaxHeader() && getName().endsWith("/"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isExtended() {
/* 1210 */     return this.isExtended;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFIFO() {
/* 1220 */     return (this.linkFlag == 54);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFile() {
/* 1230 */     if (this.file != null) {
/* 1231 */       return Files.isRegularFile(this.file, this.linkOptions);
/*      */     }
/* 1233 */     if (this.linkFlag == 0 || this.linkFlag == 48) {
/* 1234 */       return true;
/*      */     }
/* 1236 */     return !getName().endsWith("/");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGlobalPaxHeader() {
/* 1247 */     return (this.linkFlag == 103);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGNULongLinkEntry() {
/* 1256 */     return (this.linkFlag == 75);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGNULongNameEntry() {
/* 1265 */     return (this.linkFlag == 76);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isGNUSparse() {
/* 1274 */     return (isOldGNUSparse() || isPaxGNUSparse());
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isInvalidPrefix(byte[] header) {
/* 1279 */     if (header[475] != 0)
/*      */     {
/* 1281 */       if (header[156] == 77) {
/*      */ 
/*      */ 
/*      */         
/* 1285 */         if ((header[464] & 0x80) == 0 && header[475] != 32)
/*      */         {
/* 1287 */           return true;
/*      */         }
/*      */       } else {
/* 1290 */         return true;
/*      */       } 
/*      */     }
/* 1293 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isInvalidXtarTime(byte[] buffer, int offset, int length) {
/* 1298 */     if ((buffer[offset] & 0x80) == 0) {
/* 1299 */       int lastIndex = length - 1;
/* 1300 */       for (int i = 0; i < lastIndex; i++) {
/* 1301 */         byte b1 = buffer[offset + i];
/* 1302 */         if (b1 < 48 || b1 > 55) {
/* 1303 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 1307 */       byte b = buffer[offset + lastIndex];
/* 1308 */       if (b != 32 && b != 0) {
/* 1309 */         return true;
/*      */       }
/*      */     } 
/* 1312 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLink() {
/* 1322 */     return (this.linkFlag == 49);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOldGNUSparse() {
/* 1333 */     return (this.linkFlag == 83);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPaxGNU1XSparse() {
/* 1343 */     return this.paxGNU1XSparse;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPaxGNUSparse() {
/* 1354 */     return this.paxGNUSparse;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPaxHeader() {
/* 1366 */     return (this.linkFlag == 120 || this.linkFlag == 88);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSparse() {
/* 1377 */     return (isGNUSparse() || isStarSparse());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isStarSparse() {
/* 1387 */     return this.starSparse;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isStreamContiguous() {
/* 1396 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSymbolicLink() {
/* 1406 */     return (this.linkFlag == 50);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isXstar(Map<String, String> globalPaxHeaders, byte[] header) {
/* 1416 */     if (ArchiveUtils.matchAsciiBuffer("tar\000", header, 508, 4)) {
/* 1417 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1427 */     String archType = globalPaxHeaders.get("SCHILY.archtype");
/* 1428 */     if (archType != null) {
/* 1429 */       return ("xustar".equals(archType) || "exustar".equals(archType));
/*      */     }
/*      */ 
/*      */     
/* 1433 */     if (isInvalidPrefix(header)) {
/* 1434 */       return false;
/*      */     }
/* 1436 */     if (isInvalidXtarTime(header, 476, 12)) {
/* 1437 */       return false;
/*      */     }
/* 1439 */     if (isInvalidXtarTime(header, 488, 12)) {
/* 1440 */       return false;
/*      */     }
/*      */     
/* 1443 */     return true;
/*      */   }
/*      */   
/*      */   private long parseOctalOrBinary(byte[] header, int offset, int length, boolean lenient) {
/* 1447 */     if (lenient) {
/*      */       try {
/* 1449 */         return TarUtils.parseOctalOrBinary(header, offset, length);
/* 1450 */       } catch (IllegalArgumentException ex) {
/* 1451 */         return -1L;
/*      */       } 
/*      */     }
/* 1454 */     return TarUtils.parseOctalOrBinary(header, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseTarHeader(byte[] header) {
/*      */     try {
/* 1465 */       parseTarHeader(header, TarUtils.DEFAULT_ENCODING);
/* 1466 */     } catch (IOException ex) {
/*      */       try {
/* 1468 */         parseTarHeader(header, TarUtils.DEFAULT_ENCODING, true, false);
/* 1469 */       } catch (IOException ex2) {
/*      */         
/* 1471 */         throw new UncheckedIOException(ex2);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parseTarHeader(byte[] header, ZipEncoding encoding) throws IOException {
/* 1488 */     parseTarHeader(header, encoding, false, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void parseTarHeader(byte[] header, ZipEncoding encoding, boolean oldStyle, boolean lenient) throws IOException {
/* 1494 */     parseTarHeader(Collections.emptyMap(), header, encoding, oldStyle, lenient);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void parseTarHeader(Map<String, String> globalPaxHeaders, byte[] header, ZipEncoding encoding, boolean oldStyle, boolean lenient) throws IOException {
/*      */     try {
/* 1501 */       parseTarHeaderUnwrapped(globalPaxHeaders, header, encoding, oldStyle, lenient);
/* 1502 */     } catch (IllegalArgumentException ex) {
/* 1503 */       throw new IOException("Corrupted TAR archive.", ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void parseTarHeaderUnwrapped(Map<String, String> globalPaxHeaders, byte[] header, ZipEncoding encoding, boolean oldStyle, boolean lenient) throws IOException {
/*      */     String xstarPrefix;
/* 1510 */     int offset = 0;
/*      */     
/* 1512 */     this
/* 1513 */       .name = oldStyle ? TarUtils.parseName(header, offset, 100) : TarUtils.parseName(header, offset, 100, encoding);
/* 1514 */     offset += 100;
/* 1515 */     this.mode = (int)parseOctalOrBinary(header, offset, 8, lenient);
/* 1516 */     offset += 8;
/* 1517 */     this.userId = (int)parseOctalOrBinary(header, offset, 8, lenient);
/* 1518 */     offset += 8;
/* 1519 */     this.groupId = (int)parseOctalOrBinary(header, offset, 8, lenient);
/* 1520 */     offset += 8;
/* 1521 */     this.size = TarUtils.parseOctalOrBinary(header, offset, 12);
/* 1522 */     if (this.size < 0L) {
/* 1523 */       throw new IOException("broken archive, entry with negative size");
/*      */     }
/* 1525 */     offset += 12;
/* 1526 */     this.mTime = FileTime.from(parseOctalOrBinary(header, offset, 12, lenient), TimeUnit.SECONDS);
/* 1527 */     offset += 12;
/* 1528 */     this.checkSumOK = TarUtils.verifyCheckSum(header);
/* 1529 */     offset += 8;
/* 1530 */     this.linkFlag = header[offset++];
/* 1531 */     this
/* 1532 */       .linkName = oldStyle ? TarUtils.parseName(header, offset, 100) : TarUtils.parseName(header, offset, 100, encoding);
/* 1533 */     offset += 100;
/* 1534 */     this.magic = TarUtils.parseName(header, offset, 6);
/* 1535 */     offset += 6;
/* 1536 */     this.version = TarUtils.parseName(header, offset, 2);
/* 1537 */     offset += 2;
/* 1538 */     this
/* 1539 */       .userName = oldStyle ? TarUtils.parseName(header, offset, 32) : TarUtils.parseName(header, offset, 32, encoding);
/* 1540 */     offset += 32;
/* 1541 */     this
/* 1542 */       .groupName = oldStyle ? TarUtils.parseName(header, offset, 32) : TarUtils.parseName(header, offset, 32, encoding);
/* 1543 */     offset += 32;
/* 1544 */     if (this.linkFlag == 51 || this.linkFlag == 52) {
/* 1545 */       this.devMajor = (int)parseOctalOrBinary(header, offset, 8, lenient);
/* 1546 */       offset += 8;
/* 1547 */       this.devMinor = (int)parseOctalOrBinary(header, offset, 8, lenient);
/* 1548 */       offset += 8;
/*      */     } else {
/* 1550 */       offset += 16;
/*      */     } 
/*      */     
/* 1553 */     int type = evaluateType(globalPaxHeaders, header);
/* 1554 */     switch (type) {
/*      */       case 2:
/* 1556 */         this.aTime = fileTimeFromOptionalSeconds(parseOctalOrBinary(header, offset, 12, lenient));
/* 1557 */         offset += 12;
/* 1558 */         this.cTime = fileTimeFromOptionalSeconds(parseOctalOrBinary(header, offset, 12, lenient));
/* 1559 */         offset += 12;
/* 1560 */         offset += 12;
/* 1561 */         offset += 4;
/* 1562 */         offset++;
/* 1563 */         this
/* 1564 */           .sparseHeaders = new ArrayList<>(TarUtils.readSparseStructs(header, offset, 4));
/* 1565 */         offset += 96;
/* 1566 */         this.isExtended = TarUtils.parseBoolean(header, offset);
/* 1567 */         offset++;
/* 1568 */         this.realSize = TarUtils.parseOctal(header, offset, 12);
/* 1569 */         offset += 12;
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 4:
/* 1575 */         xstarPrefix = oldStyle ? TarUtils.parseName(header, offset, 131) : TarUtils.parseName(header, offset, 131, encoding);
/* 1576 */         offset += 131;
/* 1577 */         if (!xstarPrefix.isEmpty()) {
/* 1578 */           this.name = xstarPrefix + "/" + this.name;
/*      */         }
/* 1580 */         this.aTime = fileTimeFromOptionalSeconds(parseOctalOrBinary(header, offset, 12, lenient));
/* 1581 */         offset += 12;
/* 1582 */         this.cTime = fileTimeFromOptionalSeconds(parseOctalOrBinary(header, offset, 12, lenient));
/* 1583 */         offset += 12;
/*      */         return;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1590 */     String prefix = oldStyle ? TarUtils.parseName(header, offset, 155) : TarUtils.parseName(header, offset, 155, encoding);
/* 1591 */     offset += 155;
/*      */ 
/*      */     
/* 1594 */     if (isDirectory() && !this.name.endsWith("/")) {
/* 1595 */       this.name += "/";
/*      */     }
/* 1597 */     if (!prefix.isEmpty()) {
/* 1598 */       this.name = prefix + "/" + this.name;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processPaxHeader(String key, String val) throws IOException {
/* 1612 */     processPaxHeader(key, val, this.extraPaxHeaders);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processPaxHeader(String key, String val, Map<String, String> headers) throws IOException {
/*      */     long size;
/*      */     int devMinor, devMajor;
/* 1650 */     switch (key) {
/*      */       case "path":
/* 1652 */         setName(val);
/*      */         return;
/*      */       case "linkpath":
/* 1655 */         setLinkName(val);
/*      */         return;
/*      */       case "gid":
/* 1658 */         setGroupId(Long.parseLong(val));
/*      */         return;
/*      */       case "gname":
/* 1661 */         setGroupName(val);
/*      */         return;
/*      */       case "uid":
/* 1664 */         setUserId(Long.parseLong(val));
/*      */         return;
/*      */       case "uname":
/* 1667 */         setUserName(val);
/*      */         return;
/*      */       case "size":
/* 1670 */         size = Long.parseLong(val);
/* 1671 */         if (size < 0L) {
/* 1672 */           throw new IOException("Corrupted TAR archive. Entry size is negative");
/*      */         }
/* 1674 */         setSize(size);
/*      */         return;
/*      */       case "mtime":
/* 1677 */         setLastModifiedTime(FileTime.from(parseInstantFromDecimalSeconds(val)));
/*      */         return;
/*      */       case "atime":
/* 1680 */         setLastAccessTime(FileTime.from(parseInstantFromDecimalSeconds(val)));
/*      */         return;
/*      */       case "ctime":
/* 1683 */         setStatusChangeTime(FileTime.from(parseInstantFromDecimalSeconds(val)));
/*      */         return;
/*      */       case "LIBARCHIVE.creationtime":
/* 1686 */         setCreationTime(FileTime.from(parseInstantFromDecimalSeconds(val)));
/*      */         return;
/*      */       case "SCHILY.devminor":
/* 1689 */         devMinor = Integer.parseInt(val);
/* 1690 */         if (devMinor < 0) {
/* 1691 */           throw new IOException("Corrupted TAR archive. Dev-Minor is negative");
/*      */         }
/* 1693 */         setDevMinor(devMinor);
/*      */         return;
/*      */       case "SCHILY.devmajor":
/* 1696 */         devMajor = Integer.parseInt(val);
/* 1697 */         if (devMajor < 0) {
/* 1698 */           throw new IOException("Corrupted TAR archive. Dev-Major is negative");
/*      */         }
/* 1700 */         setDevMajor(devMajor);
/*      */         return;
/*      */       case "GNU.sparse.size":
/* 1703 */         fillGNUSparse0xData(headers);
/*      */         return;
/*      */       case "GNU.sparse.realsize":
/* 1706 */         fillGNUSparse1xData(headers);
/*      */         return;
/*      */       case "SCHILY.filetype":
/* 1709 */         if ("sparse".equals(val)) {
/* 1710 */           fillStarSparseData(headers);
/*      */         }
/*      */         return;
/*      */     } 
/* 1714 */     this.extraPaxHeaders.put(key, val);
/*      */   }
/*      */ 
/*      */   
/*      */   private void readFileMode(Path file, String normalizedName, LinkOption... options) throws IOException {
/* 1719 */     if (Files.isDirectory(file, options)) {
/* 1720 */       this.mode = 16877;
/* 1721 */       this.linkFlag = 53;
/*      */       
/* 1723 */       int nameLength = normalizedName.length();
/* 1724 */       if (nameLength == 0 || normalizedName.charAt(nameLength - 1) != '/') {
/* 1725 */         this.name = normalizedName + "/";
/*      */       } else {
/* 1727 */         this.name = normalizedName;
/*      */       } 
/*      */     } else {
/* 1730 */       this.mode = 33188;
/* 1731 */       this.linkFlag = 48;
/* 1732 */       this.name = normalizedName;
/* 1733 */       this.size = Files.size(file);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readOsSpecificProperties(Path file, LinkOption... options) throws IOException {
/* 1738 */     Set<String> availableAttributeViews = file.getFileSystem().supportedFileAttributeViews();
/* 1739 */     if (availableAttributeViews.contains("posix")) {
/* 1740 */       PosixFileAttributes posixFileAttributes = Files.<PosixFileAttributes>readAttributes(file, PosixFileAttributes.class, options);
/* 1741 */       setLastModifiedTime(posixFileAttributes.lastModifiedTime());
/* 1742 */       setCreationTime(posixFileAttributes.creationTime());
/* 1743 */       setLastAccessTime(posixFileAttributes.lastAccessTime());
/* 1744 */       this.userName = posixFileAttributes.owner().getName();
/* 1745 */       this.groupName = posixFileAttributes.group().getName();
/* 1746 */       if (availableAttributeViews.contains("unix")) {
/* 1747 */         this.userId = ((Number)Files.getAttribute(file, "unix:uid", options)).longValue();
/* 1748 */         this.groupId = ((Number)Files.getAttribute(file, "unix:gid", options)).longValue();
/*      */         try {
/* 1750 */           setStatusChangeTime((FileTime)Files.getAttribute(file, "unix:ctime", options));
/* 1751 */         } catch (IllegalArgumentException illegalArgumentException) {}
/*      */       }
/*      */     
/*      */     }
/* 1755 */     else if (availableAttributeViews.contains("dos")) {
/* 1756 */       DosFileAttributes dosFileAttributes = Files.<DosFileAttributes>readAttributes(file, DosFileAttributes.class, options);
/* 1757 */       setLastModifiedTime(dosFileAttributes.lastModifiedTime());
/* 1758 */       setCreationTime(dosFileAttributes.creationTime());
/* 1759 */       setLastAccessTime(dosFileAttributes.lastAccessTime());
/* 1760 */       this.userName = Files.getOwner(file, options).getName();
/*      */     } else {
/* 1762 */       BasicFileAttributes basicFileAttributes = Files.readAttributes(file, BasicFileAttributes.class, options);
/* 1763 */       setLastModifiedTime(basicFileAttributes.lastModifiedTime());
/* 1764 */       setCreationTime(basicFileAttributes.creationTime());
/* 1765 */       setLastAccessTime(basicFileAttributes.lastAccessTime());
/* 1766 */       this.userName = Files.getOwner(file, options).getName();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCreationTime(FileTime time) {
/* 1777 */     this.birthTime = time;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDataOffset(long dataOffset) {
/* 1786 */     if (dataOffset < 0L) {
/* 1787 */       throw new IllegalArgumentException("The offset can not be smaller than 0");
/*      */     }
/* 1789 */     this.dataOffset = dataOffset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDevMajor(int devNo) {
/* 1800 */     if (devNo < 0) {
/* 1801 */       throw new IllegalArgumentException("Major device number is out of range: " + devNo);
/*      */     }
/*      */     
/* 1804 */     this.devMajor = devNo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDevMinor(int devNo) {
/* 1815 */     if (devNo < 0) {
/* 1816 */       throw new IllegalArgumentException("Minor device number is out of range: " + devNo);
/*      */     }
/*      */     
/* 1819 */     this.devMinor = devNo;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGroupId(int groupId) {
/* 1828 */     setGroupId(groupId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGroupId(long groupId) {
/* 1838 */     this.groupId = groupId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGroupName(String groupName) {
/* 1847 */     this.groupName = groupName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIds(int userId, int groupId) {
/* 1857 */     setUserId(userId);
/* 1858 */     setGroupId(groupId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastAccessTime(FileTime time) {
/* 1868 */     this.aTime = time;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastModifiedTime(FileTime time) {
/* 1878 */     this.mTime = Objects.<FileTime>requireNonNull(time, "Time must not be null");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLinkName(String link) {
/* 1889 */     this.linkName = link;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMode(int mode) {
/* 1898 */     this.mode = mode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModTime(Date time) {
/* 1908 */     setLastModifiedTime(FileTime.fromMillis(time.getTime()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModTime(FileTime time) {
/* 1919 */     setLastModifiedTime(time);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModTime(long time) {
/* 1930 */     setLastModifiedTime(FileTime.fromMillis(time));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String name) {
/* 1939 */     this.name = normalizeFileName(name, this.preserveAbsolutePath);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNames(String userName, String groupName) {
/* 1949 */     setUserName(userName);
/* 1950 */     setGroupName(groupName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSize(long size) {
/* 1960 */     if (size < 0L) {
/* 1961 */       throw new IllegalArgumentException("Size is out of range: " + size);
/*      */     }
/* 1963 */     this.size = size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSparseHeaders(List<TarArchiveStructSparse> sparseHeaders) {
/* 1972 */     this.sparseHeaders = sparseHeaders;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStatusChangeTime(FileTime time) {
/* 1982 */     this.cTime = time;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUserId(int userId) {
/* 1991 */     setUserId(userId);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUserId(long userId) {
/* 2001 */     this.userId = userId;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUserName(String userName) {
/* 2010 */     this.userName = userName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void updateEntryFromPaxHeaders(Map<String, String> headers) throws IOException {
/* 2019 */     for (Map.Entry<String, String> ent : headers.entrySet()) {
/* 2020 */       processPaxHeader(ent.getKey(), ent.getValue(), headers);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeEntryHeader(byte[] outbuf) {
/*      */     try {
/* 2033 */       writeEntryHeader(outbuf, TarUtils.DEFAULT_ENCODING, false);
/* 2034 */     } catch (IOException ex) {
/*      */       try {
/* 2036 */         writeEntryHeader(outbuf, TarUtils.FALLBACK_ENCODING, false);
/* 2037 */       } catch (IOException ex2) {
/*      */         
/* 2039 */         throw new UncheckedIOException(ex2);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeEntryHeader(byte[] outbuf, ZipEncoding encoding, boolean starMode) throws IOException {
/* 2057 */     int offset = 0;
/*      */     
/* 2059 */     offset = TarUtils.formatNameBytes(this.name, outbuf, offset, 100, encoding);
/*      */     
/* 2061 */     offset = writeEntryHeaderField(this.mode, outbuf, offset, 8, starMode);
/* 2062 */     offset = writeEntryHeaderField(this.userId, outbuf, offset, 8, starMode);
/*      */     
/* 2064 */     offset = writeEntryHeaderField(this.groupId, outbuf, offset, 8, starMode);
/*      */     
/* 2066 */     offset = writeEntryHeaderField(this.size, outbuf, offset, 12, starMode);
/* 2067 */     offset = writeEntryHeaderField(this.mTime.to(TimeUnit.SECONDS), outbuf, offset, 12, starMode);
/*      */ 
/*      */     
/* 2070 */     int csOffset = offset;
/*      */     
/* 2072 */     offset = fill((byte)32, offset, outbuf, 8);
/*      */     
/* 2074 */     outbuf[offset++] = this.linkFlag;
/* 2075 */     offset = TarUtils.formatNameBytes(this.linkName, outbuf, offset, 100, encoding);
/*      */     
/* 2077 */     offset = TarUtils.formatNameBytes(this.magic, outbuf, offset, 6);
/* 2078 */     offset = TarUtils.formatNameBytes(this.version, outbuf, offset, 2);
/* 2079 */     offset = TarUtils.formatNameBytes(this.userName, outbuf, offset, 32, encoding);
/*      */     
/* 2081 */     offset = TarUtils.formatNameBytes(this.groupName, outbuf, offset, 32, encoding);
/*      */     
/* 2083 */     offset = writeEntryHeaderField(this.devMajor, outbuf, offset, 8, starMode);
/*      */     
/* 2085 */     offset = writeEntryHeaderField(this.devMinor, outbuf, offset, 8, starMode);
/*      */ 
/*      */     
/* 2088 */     if (starMode) {
/*      */       
/* 2090 */       offset = fill(0, offset, outbuf, 131);
/* 2091 */       offset = writeEntryHeaderOptionalTimeField(this.aTime, offset, outbuf, 12);
/* 2092 */       offset = writeEntryHeaderOptionalTimeField(this.cTime, offset, outbuf, 12);
/*      */       
/* 2094 */       offset = fill(0, offset, outbuf, 8);
/*      */ 
/*      */       
/* 2097 */       offset = fill(0, offset, outbuf, 4);
/*      */     } 
/*      */     
/* 2100 */     offset = fill(0, offset, outbuf, outbuf.length - offset);
/*      */     
/* 2102 */     long chk = TarUtils.computeCheckSum(outbuf);
/*      */     
/* 2104 */     TarUtils.formatCheckSumOctalBytes(chk, outbuf, csOffset, 8);
/*      */   }
/*      */ 
/*      */   
/*      */   private int writeEntryHeaderField(long value, byte[] outbuf, int offset, int length, boolean starMode) {
/* 2109 */     if (!starMode && (value < 0L || value >= 1L << 3 * (length - 1)))
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 2114 */       return TarUtils.formatLongOctalBytes(0L, outbuf, offset, length);
/*      */     }
/* 2116 */     return TarUtils.formatLongOctalOrBinaryBytes(value, outbuf, offset, length);
/*      */   }
/*      */ 
/*      */   
/*      */   private int writeEntryHeaderOptionalTimeField(FileTime time, int offset, byte[] outbuf, int fieldLength) {
/* 2121 */     if (time != null) {
/* 2122 */       offset = writeEntryHeaderField(time.to(TimeUnit.SECONDS), outbuf, offset, fieldLength, true);
/*      */     } else {
/* 2124 */       offset = fill(0, offset, outbuf, fieldLength);
/*      */     } 
/* 2126 */     return offset;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\tar\TarArchiveEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */