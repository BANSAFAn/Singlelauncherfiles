/*     */ package org.apache.commons.io.filefilter;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FilenameFilter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOCase;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileFilterUtils
/*     */ {
/*  48 */   private static final IOFileFilter cvsFilter = notFileFilter(
/*  49 */       and(new IOFileFilter[] { directoryFileFilter(), nameFileFilter("CVS") }));
/*     */ 
/*     */ 
/*     */   
/*  53 */   private static final IOFileFilter svnFilter = notFileFilter(
/*  54 */       and(new IOFileFilter[] { directoryFileFilter(), nameFileFilter(".svn") }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter ageFileFilter(Date cutoffDate) {
/*  66 */     return new AgeFileFilter(cutoffDate);
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
/*     */   public static IOFileFilter ageFileFilter(Date cutoffDate, boolean acceptOlder) {
/*  79 */     return new AgeFileFilter(cutoffDate, acceptOlder);
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
/*     */   public static IOFileFilter ageFileFilter(File cutoffReference) {
/*  93 */     return new AgeFileFilter(cutoffReference);
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
/*     */   public static IOFileFilter ageFileFilter(File cutoffReference, boolean acceptOlder) {
/* 107 */     return new AgeFileFilter(cutoffReference, acceptOlder);
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
/*     */   public static IOFileFilter ageFileFilter(long cutoff) {
/* 120 */     return new AgeFileFilter(cutoff);
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
/*     */   public static IOFileFilter ageFileFilter(long cutoff, boolean acceptOlder) {
/* 133 */     return new AgeFileFilter(cutoff, acceptOlder);
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
/*     */   public static IOFileFilter and(IOFileFilter... filters) {
/* 148 */     return new AndFileFilter(toList(filters));
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
/*     */   public static IOFileFilter andFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
/* 163 */     return new AndFileFilter(filter1, filter2);
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
/*     */   public static IOFileFilter asFileFilter(FileFilter filter) {
/* 175 */     return new DelegateFileFilter(filter);
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
/*     */   public static IOFileFilter asFileFilter(FilenameFilter filter) {
/* 187 */     return new DelegateFileFilter(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter directoryFileFilter() {
/* 197 */     return DirectoryFileFilter.DIRECTORY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter falseFileFilter() {
/* 207 */     return FalseFileFilter.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter fileFileFilter() {
/* 217 */     return FileFileFilter.INSTANCE;
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
/*     */   public static File[] filter(IOFileFilter filter, File... files) {
/* 243 */     if (filter == null) {
/* 244 */       throw new IllegalArgumentException("file filter is null");
/*     */     }
/* 246 */     if (files == null) {
/* 247 */       return FileUtils.EMPTY_FILE_ARRAY;
/*     */     }
/* 249 */     return (File[])((List)filterFiles(filter, Stream.of(files), (Collector)Collectors.toList())).toArray((Object[])FileUtils.EMPTY_FILE_ARRAY);
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
/*     */   private static <R, A> R filterFiles(IOFileFilter filter, Stream<File> stream, Collector<? super File, A, R> collector) {
/* 269 */     Objects.requireNonNull(collector, "collector");
/* 270 */     if (filter == null) {
/* 271 */       throw new IllegalArgumentException("file filter is null");
/*     */     }
/* 273 */     if (stream == null) {
/* 274 */       return Stream.<File>empty().collect(collector);
/*     */     }
/* 276 */     return stream.filter(filter::accept).collect(collector);
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
/*     */   public static File[] filter(IOFileFilter filter, Iterable<File> files) {
/* 306 */     return filterList(filter, files).<File>toArray(FileUtils.EMPTY_FILE_ARRAY);
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
/*     */   public static List<File> filterList(IOFileFilter filter, File... files) {
/* 335 */     return Arrays.asList(filter(filter, files));
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
/*     */   public static List<File> filterList(IOFileFilter filter, Iterable<File> files) {
/* 363 */     if (files == null) {
/* 364 */       return Collections.emptyList();
/*     */     }
/* 366 */     return filterFiles(filter, StreamSupport.stream(files.spliterator(), false), (Collector)Collectors.toList());
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
/*     */   public static Set<File> filterSet(IOFileFilter filter, File... files) {
/* 396 */     return new HashSet<>(Arrays.asList(filter(filter, files)));
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
/*     */   public static Set<File> filterSet(IOFileFilter filter, Iterable<File> files) {
/* 425 */     if (files == null) {
/* 426 */       return Collections.emptySet();
/*     */     }
/* 428 */     return filterFiles(filter, StreamSupport.stream(files.spliterator(), false), (Collector)Collectors.toSet());
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
/*     */   public static IOFileFilter magicNumberFileFilter(byte[] magicNumber) {
/* 447 */     return new MagicNumberFileFilter(magicNumber);
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
/*     */   public static IOFileFilter magicNumberFileFilter(byte[] magicNumber, long offset) {
/* 468 */     return new MagicNumberFileFilter(magicNumber, offset);
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
/*     */   public static IOFileFilter magicNumberFileFilter(String magicNumber) {
/* 487 */     return new MagicNumberFileFilter(magicNumber);
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
/*     */   public static IOFileFilter magicNumberFileFilter(String magicNumber, long offset) {
/* 508 */     return new MagicNumberFileFilter(magicNumber, offset);
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
/*     */   public static IOFileFilter makeCVSAware(IOFileFilter filter) {
/* 521 */     return (filter == null) ? cvsFilter : and(new IOFileFilter[] { filter, cvsFilter });
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
/*     */   public static IOFileFilter makeDirectoryOnly(IOFileFilter filter) {
/* 533 */     if (filter == null) {
/* 534 */       return DirectoryFileFilter.DIRECTORY;
/*     */     }
/* 536 */     return DirectoryFileFilter.DIRECTORY.and(filter);
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
/*     */   public static IOFileFilter makeFileOnly(IOFileFilter filter) {
/* 548 */     if (filter == null) {
/* 549 */       return FileFileFilter.INSTANCE;
/*     */     }
/* 551 */     return FileFileFilter.INSTANCE.and(filter);
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
/*     */   public static IOFileFilter makeSVNAware(IOFileFilter filter) {
/* 564 */     return (filter == null) ? svnFilter : and(new IOFileFilter[] { filter, svnFilter });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter nameFileFilter(String name) {
/* 575 */     return new NameFileFilter(name);
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
/*     */   public static IOFileFilter nameFileFilter(String name, IOCase caseSensitivity) {
/* 588 */     return new NameFileFilter(name, caseSensitivity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter notFileFilter(IOFileFilter filter) {
/* 599 */     return filter.negate();
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
/*     */   public static IOFileFilter or(IOFileFilter... filters) {
/* 614 */     return new OrFileFilter(toList(filters));
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
/*     */   public static IOFileFilter orFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
/* 629 */     return new OrFileFilter(filter1, filter2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter prefixFileFilter(String prefix) {
/* 640 */     return new PrefixFileFilter(prefix);
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
/*     */   public static IOFileFilter prefixFileFilter(String prefix, IOCase caseSensitivity) {
/* 653 */     return new PrefixFileFilter(prefix, caseSensitivity);
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
/*     */   public static IOFileFilter sizeFileFilter(long threshold) {
/* 665 */     return new SizeFileFilter(threshold);
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
/*     */   public static IOFileFilter sizeFileFilter(long threshold, boolean acceptLarger) {
/* 678 */     return new SizeFileFilter(threshold, acceptLarger);
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
/*     */   public static IOFileFilter sizeRangeFileFilter(long minSizeInclusive, long maxSizeInclusive) {
/* 692 */     IOFileFilter minimumFilter = new SizeFileFilter(minSizeInclusive, true);
/* 693 */     IOFileFilter maximumFilter = new SizeFileFilter(maxSizeInclusive + 1L, false);
/* 694 */     return minimumFilter.and(maximumFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter suffixFileFilter(String suffix) {
/* 705 */     return new SuffixFileFilter(suffix);
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
/*     */   public static IOFileFilter suffixFileFilter(String suffix, IOCase caseSensitivity) {
/* 718 */     return new SuffixFileFilter(suffix, caseSensitivity);
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
/*     */   public static List<IOFileFilter> toList(IOFileFilter... filters) {
/* 731 */     if (filters == null) {
/* 732 */       throw new IllegalArgumentException("The filters must not be null");
/*     */     }
/* 734 */     List<IOFileFilter> list = new ArrayList<>(filters.length);
/* 735 */     for (int i = 0; i < filters.length; i++) {
/* 736 */       if (filters[i] == null) {
/* 737 */         throw new IllegalArgumentException("The filter[" + i + "] is null");
/*     */       }
/* 739 */       list.add(filters[i]);
/*     */     } 
/* 741 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IOFileFilter trueFileFilter() {
/* 751 */     return TrueFileFilter.TRUE;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\filefilter\FileFilterUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */