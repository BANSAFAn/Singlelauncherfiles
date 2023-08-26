/*     */ package org.apache.commons.compress.archivers.sevenz;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Objects;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SevenZArchiveEntry
/*     */   implements ArchiveEntry
/*     */ {
/*     */   private String name;
/*     */   private boolean hasStream;
/*     */   private boolean isDirectory;
/*     */   private boolean isAntiItem;
/*     */   private boolean hasCreationDate;
/*     */   private boolean hasLastModifiedDate;
/*     */   private boolean hasAccessDate;
/*     */   private long creationDate;
/*     */   private long lastModifiedDate;
/*     */   private long accessDate;
/*     */   private boolean hasWindowsAttributes;
/*     */   private int windowsAttributes;
/*     */   private boolean hasCrc;
/*     */   private long crc;
/*     */   private long compressedCrc;
/*     */   private long size;
/*     */   private long compressedSize;
/*     */   private Iterable<? extends SevenZMethodConfiguration> contentMethods;
/*  54 */   static final SevenZArchiveEntry[] EMPTY_SEVEN_Z_ARCHIVE_ENTRY_ARRAY = new SevenZArchiveEntry[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  68 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  77 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasStream() {
/*  85 */     return this.hasStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasStream(boolean hasStream) {
/*  93 */     this.hasStream = hasStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 103 */     return this.isDirectory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirectory(boolean isDirectory) {
/* 112 */     this.isDirectory = isDirectory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAntiItem() {
/* 121 */     return this.isAntiItem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAntiItem(boolean isAntiItem) {
/* 130 */     this.isAntiItem = isAntiItem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasCreationDate() {
/* 138 */     return this.hasCreationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasCreationDate(boolean hasCreationDate) {
/* 146 */     this.hasCreationDate = hasCreationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreationDate() {
/* 156 */     if (this.hasCreationDate) {
/* 157 */       return ntfsTimeToJavaTime(this.creationDate);
/*     */     }
/* 159 */     throw new UnsupportedOperationException("The entry doesn't have this timestamp");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreationDate(long ntfsCreationDate) {
/* 169 */     this.creationDate = ntfsCreationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreationDate(Date creationDate) {
/* 177 */     this.hasCreationDate = (creationDate != null);
/* 178 */     if (this.hasCreationDate) {
/* 179 */       this.creationDate = javaTimeToNtfsTime(creationDate);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasLastModifiedDate() {
/* 188 */     return this.hasLastModifiedDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasLastModifiedDate(boolean hasLastModifiedDate) {
/* 197 */     this.hasLastModifiedDate = hasLastModifiedDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getLastModifiedDate() {
/* 208 */     if (this.hasLastModifiedDate) {
/* 209 */       return ntfsTimeToJavaTime(this.lastModifiedDate);
/*     */     }
/* 211 */     throw new UnsupportedOperationException("The entry doesn't have this timestamp");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastModifiedDate(long ntfsLastModifiedDate) {
/* 221 */     this.lastModifiedDate = ntfsLastModifiedDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastModifiedDate(Date lastModifiedDate) {
/* 229 */     this.hasLastModifiedDate = (lastModifiedDate != null);
/* 230 */     if (this.hasLastModifiedDate) {
/* 231 */       this.lastModifiedDate = javaTimeToNtfsTime(lastModifiedDate);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasAccessDate() {
/* 240 */     return this.hasAccessDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasAccessDate(boolean hasAcessDate) {
/* 248 */     this.hasAccessDate = hasAcessDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getAccessDate() {
/* 258 */     if (this.hasAccessDate) {
/* 259 */       return ntfsTimeToJavaTime(this.accessDate);
/*     */     }
/* 261 */     throw new UnsupportedOperationException("The entry doesn't have this timestamp");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessDate(long ntfsAccessDate) {
/* 271 */     this.accessDate = ntfsAccessDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessDate(Date accessDate) {
/* 279 */     this.hasAccessDate = (accessDate != null);
/* 280 */     if (this.hasAccessDate) {
/* 281 */       this.accessDate = javaTimeToNtfsTime(accessDate);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasWindowsAttributes() {
/* 290 */     return this.hasWindowsAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasWindowsAttributes(boolean hasWindowsAttributes) {
/* 298 */     this.hasWindowsAttributes = hasWindowsAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getWindowsAttributes() {
/* 306 */     return this.windowsAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWindowsAttributes(int windowsAttributes) {
/* 314 */     this.windowsAttributes = windowsAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getHasCrc() {
/* 324 */     return this.hasCrc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHasCrc(boolean hasCrc) {
/* 332 */     this.hasCrc = hasCrc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int getCrc() {
/* 342 */     return (int)this.crc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setCrc(int crc) {
/* 352 */     this.crc = crc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCrcValue() {
/* 361 */     return this.crc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCrcValue(long crc) {
/* 370 */     this.crc = crc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   int getCompressedCrc() {
/* 380 */     return (int)this.compressedCrc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   void setCompressedCrc(int crc) {
/* 390 */     this.compressedCrc = crc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getCompressedCrcValue() {
/* 399 */     return this.compressedCrc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setCompressedCrcValue(long crc) {
/* 408 */     this.compressedCrc = crc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 418 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(long size) {
/* 427 */     this.size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getCompressedSize() {
/* 436 */     return this.compressedSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setCompressedSize(long size) {
/* 445 */     this.compressedSize = size;
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
/*     */   public void setContentMethods(Iterable<? extends SevenZMethodConfiguration> methods) {
/* 463 */     if (methods != null) {
/* 464 */       LinkedList<SevenZMethodConfiguration> l = new LinkedList<>();
/* 465 */       methods.forEach(l::addLast);
/* 466 */       this.contentMethods = Collections.unmodifiableList(l);
/*     */     } else {
/* 468 */       this.contentMethods = null;
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
/*     */   public void setContentMethods(SevenZMethodConfiguration... methods) {
/* 487 */     setContentMethods(Arrays.asList(methods));
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
/*     */   public Iterable<? extends SevenZMethodConfiguration> getContentMethods() {
/* 505 */     return this.contentMethods;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 510 */     String n = getName();
/* 511 */     return (n == null) ? 0 : n.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 516 */     if (this == obj) {
/* 517 */       return true;
/*     */     }
/* 519 */     if (obj == null || getClass() != obj.getClass()) {
/* 520 */       return false;
/*     */     }
/* 522 */     SevenZArchiveEntry other = (SevenZArchiveEntry)obj;
/* 523 */     return (
/* 524 */       Objects.equals(this.name, other.name) && this.hasStream == other.hasStream && this.isDirectory == other.isDirectory && this.isAntiItem == other.isAntiItem && this.hasCreationDate == other.hasCreationDate && this.hasLastModifiedDate == other.hasLastModifiedDate && this.hasAccessDate == other.hasAccessDate && this.creationDate == other.creationDate && this.lastModifiedDate == other.lastModifiedDate && this.accessDate == other.accessDate && this.hasWindowsAttributes == other.hasWindowsAttributes && this.windowsAttributes == other.windowsAttributes && this.hasCrc == other.hasCrc && this.crc == other.crc && this.compressedCrc == other.compressedCrc && this.size == other.size && this.compressedSize == other.compressedSize && 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 541 */       equalSevenZMethods(this.contentMethods, other.contentMethods));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date ntfsTimeToJavaTime(long ntfsTime) {
/* 551 */     Calendar ntfsEpoch = Calendar.getInstance();
/* 552 */     ntfsEpoch.setTimeZone(TimeZone.getTimeZone("GMT+0"));
/* 553 */     ntfsEpoch.set(1601, 0, 1, 0, 0, 0);
/* 554 */     ntfsEpoch.set(14, 0);
/* 555 */     long realTime = ntfsEpoch.getTimeInMillis() + ntfsTime / 10000L;
/* 556 */     return new Date(realTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long javaTimeToNtfsTime(Date date) {
/* 565 */     Calendar ntfsEpoch = Calendar.getInstance();
/* 566 */     ntfsEpoch.setTimeZone(TimeZone.getTimeZone("GMT+0"));
/* 567 */     ntfsEpoch.set(1601, 0, 1, 0, 0, 0);
/* 568 */     ntfsEpoch.set(14, 0);
/* 569 */     return (date.getTime() - ntfsEpoch.getTimeInMillis()) * 1000L * 10L;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean equalSevenZMethods(Iterable<? extends SevenZMethodConfiguration> c1, Iterable<? extends SevenZMethodConfiguration> c2) {
/* 574 */     if (c1 == null) {
/* 575 */       return (c2 == null);
/*     */     }
/* 577 */     if (c2 == null) {
/* 578 */       return false;
/*     */     }
/* 580 */     Iterator<? extends SevenZMethodConfiguration> i2 = c2.iterator();
/* 581 */     for (SevenZMethodConfiguration element : c1) {
/* 582 */       if (!i2.hasNext()) {
/* 583 */         return false;
/*     */       }
/* 585 */       if (!element.equals(i2.next())) {
/* 586 */         return false;
/*     */       }
/*     */     } 
/* 589 */     return !i2.hasNext();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\sevenz\SevenZArchiveEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */