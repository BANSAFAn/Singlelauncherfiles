/*     */ package org.apache.commons.compress.archivers.dump;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DumpArchiveEntry
/*     */   implements ArchiveEntry
/*     */ {
/*     */   private String name;
/* 184 */   private TYPE type = TYPE.UNKNOWN;
/*     */   private int mode;
/* 186 */   private Set<PERMISSION> permissions = Collections.emptySet();
/*     */   
/*     */   private long size;
/*     */   
/*     */   private long atime;
/*     */   
/*     */   private long mtime;
/*     */   
/*     */   private int uid;
/*     */   private int gid;
/* 196 */   private final DumpArchiveSummary summary = null;
/*     */ 
/*     */   
/* 199 */   private final TapeSegmentHeader header = new TapeSegmentHeader();
/*     */   
/*     */   private String simpleName;
/*     */   
/*     */   private String originalName;
/*     */   
/*     */   private int volume;
/*     */   
/*     */   private long offset;
/*     */   
/*     */   private int ino;
/*     */   
/*     */   private int nlink;
/*     */   
/*     */   private long ctime;
/*     */   
/*     */   private int generation;
/*     */   
/*     */   private boolean isDeleted;
/*     */ 
/*     */   
/*     */   public DumpArchiveEntry() {}
/*     */ 
/*     */   
/*     */   public DumpArchiveEntry(String name, String simpleName) {
/* 224 */     setName(name);
/* 225 */     this.simpleName = simpleName;
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
/*     */   protected DumpArchiveEntry(String name, String simpleName, int ino, TYPE type) {
/* 238 */     setType(type);
/* 239 */     setName(name);
/* 240 */     this.simpleName = simpleName;
/* 241 */     this.ino = ino;
/* 242 */     this.offset = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSimpleName() {
/* 250 */     return this.simpleName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setSimpleName(String simpleName) {
/* 258 */     this.simpleName = simpleName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIno() {
/* 266 */     return this.header.getIno();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNlink() {
/* 274 */     return this.nlink;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNlink(int nlink) {
/* 282 */     this.nlink = nlink;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreationTime() {
/* 290 */     return new Date(this.ctime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreationTime(Date ctime) {
/* 298 */     this.ctime = ctime.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGeneration() {
/* 306 */     return this.generation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGeneration(int generation) {
/* 314 */     this.generation = generation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeleted() {
/* 322 */     return this.isDeleted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeleted(boolean isDeleted) {
/* 330 */     this.isDeleted = isDeleted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getOffset() {
/* 338 */     return this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOffset(long offset) {
/* 346 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVolume() {
/* 354 */     return this.volume;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVolume(int volume) {
/* 362 */     this.volume = volume;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DumpArchiveConstants.SEGMENT_TYPE getHeaderType() {
/* 370 */     return this.header.getType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHeaderCount() {
/* 378 */     return this.header.getCount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getHeaderHoles() {
/* 386 */     return this.header.getHoles();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSparseRecord(int idx) {
/* 395 */     return ((this.header.getCdata(idx) & 0x1) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 400 */     return this.ino;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 405 */     if (o == this) {
/* 406 */       return true;
/*     */     }
/* 408 */     if (o == null || !o.getClass().equals(getClass())) {
/* 409 */       return false;
/*     */     }
/*     */     
/* 412 */     DumpArchiveEntry rhs = (DumpArchiveEntry)o;
/*     */     
/* 414 */     if (this.ino != rhs.ino) {
/* 415 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 419 */     if ((this.summary == null && rhs.summary != null) || (this.summary != null && 
/* 420 */       !this.summary.equals(rhs.summary))) {
/* 421 */       return false;
/*     */     }
/*     */     
/* 424 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 429 */     return getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static DumpArchiveEntry parse(byte[] buffer) {
/* 439 */     DumpArchiveEntry entry = new DumpArchiveEntry();
/* 440 */     TapeSegmentHeader header = entry.header;
/*     */     
/* 442 */     header.type = DumpArchiveConstants.SEGMENT_TYPE.find(DumpArchiveUtil.convert32(buffer, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 448 */     header.volume = DumpArchiveUtil.convert32(buffer, 12);
/*     */     
/* 450 */     entry.ino = header.ino = DumpArchiveUtil.convert32(buffer, 20);
/*     */ 
/*     */ 
/*     */     
/* 454 */     int m = DumpArchiveUtil.convert16(buffer, 32);
/*     */ 
/*     */     
/* 457 */     entry.setType(TYPE.find(m >> 12 & 0xF));
/*     */ 
/*     */     
/* 460 */     entry.setMode(m);
/*     */     
/* 462 */     entry.nlink = DumpArchiveUtil.convert16(buffer, 34);
/*     */     
/* 464 */     entry.setSize(DumpArchiveUtil.convert64(buffer, 40));
/*     */ 
/*     */     
/* 467 */     long t = 1000L * DumpArchiveUtil.convert32(buffer, 48) + (DumpArchiveUtil.convert32(buffer, 52) / 1000);
/* 468 */     entry.setAccessTime(new Date(t));
/*     */     
/* 470 */     t = 1000L * DumpArchiveUtil.convert32(buffer, 56) + (DumpArchiveUtil.convert32(buffer, 60) / 1000);
/* 471 */     entry.setLastModifiedDate(new Date(t));
/*     */     
/* 473 */     t = 1000L * DumpArchiveUtil.convert32(buffer, 64) + (DumpArchiveUtil.convert32(buffer, 68) / 1000);
/* 474 */     entry.ctime = t;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 480 */     entry.generation = DumpArchiveUtil.convert32(buffer, 140);
/* 481 */     entry.setUserId(DumpArchiveUtil.convert32(buffer, 144));
/* 482 */     entry.setGroupId(DumpArchiveUtil.convert32(buffer, 148));
/*     */     
/* 484 */     header.count = DumpArchiveUtil.convert32(buffer, 160);
/*     */     
/* 486 */     header.holes = 0;
/*     */     
/* 488 */     for (int i = 0; i < 512 && i < header.count; i++) {
/* 489 */       if (buffer[164 + i] == 0) {
/* 490 */         header.holes++;
/*     */       }
/*     */     } 
/*     */     
/* 494 */     System.arraycopy(buffer, 164, header.cdata, 0, 512);
/*     */     
/* 496 */     entry.volume = header.getVolume();
/*     */ 
/*     */     
/* 499 */     return entry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void update(byte[] buffer) {
/* 506 */     this.header.volume = DumpArchiveUtil.convert32(buffer, 16);
/* 507 */     this.header.count = DumpArchiveUtil.convert32(buffer, 160);
/*     */     
/* 509 */     this.header.holes = 0;
/*     */     
/* 511 */     for (int i = 0; i < 512 && i < this.header.count; i++) {
/* 512 */       if (buffer[164 + i] == 0) {
/* 513 */         this.header.holes++;
/*     */       }
/*     */     } 
/*     */     
/* 517 */     System.arraycopy(buffer, 164, this.header.cdata, 0, 512);
/*     */   }
/*     */ 
/*     */   
/*     */   static class TapeSegmentHeader
/*     */   {
/*     */     private DumpArchiveConstants.SEGMENT_TYPE type;
/*     */     
/*     */     private int volume;
/*     */     
/*     */     private int ino;
/*     */     private int count;
/*     */     private int holes;
/* 530 */     private final byte[] cdata = new byte[512];
/*     */     
/*     */     public DumpArchiveConstants.SEGMENT_TYPE getType() {
/* 533 */       return this.type;
/*     */     }
/*     */     
/*     */     public int getVolume() {
/* 537 */       return this.volume;
/*     */     }
/*     */     
/*     */     public int getIno() {
/* 541 */       return this.ino;
/*     */     }
/*     */     
/*     */     void setIno(int ino) {
/* 545 */       this.ino = ino;
/*     */     }
/*     */     
/*     */     public int getCount() {
/* 549 */       return this.count;
/*     */     }
/*     */     
/*     */     public int getHoles() {
/* 553 */       return this.holes;
/*     */     }
/*     */     
/*     */     public int getCdata(int idx) {
/* 557 */       return this.cdata[idx];
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
/*     */   public String getName() {
/* 570 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getOriginalName() {
/* 578 */     return this.originalName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setName(String name) {
/* 586 */     this.originalName = name;
/* 587 */     if (name != null) {
/* 588 */       if (isDirectory() && !name.endsWith("/")) {
/* 589 */         name = name + "/";
/*     */       }
/* 591 */       if (name.startsWith("./")) {
/* 592 */         name = name.substring(2);
/*     */       }
/*     */     } 
/* 595 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getLastModifiedDate() {
/* 604 */     return new Date(this.mtime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 613 */     return (this.type == TYPE.DIRECTORY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFile() {
/* 621 */     return (this.type == TYPE.FILE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSocket() {
/* 629 */     return (this.type == TYPE.SOCKET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChrDev() {
/* 637 */     return (this.type == TYPE.CHRDEV);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlkDev() {
/* 645 */     return (this.type == TYPE.BLKDEV);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFifo() {
/* 653 */     return (this.type == TYPE.FIFO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TYPE getType() {
/* 661 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(TYPE type) {
/* 669 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMode() {
/* 677 */     return this.mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(int mode) {
/* 685 */     this.mode = mode & 0xFFF;
/* 686 */     this.permissions = PERMISSION.find(mode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<PERMISSION> getPermissions() {
/* 694 */     return this.permissions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 703 */     return isDirectory() ? -1L : this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getEntrySize() {
/* 710 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(long size) {
/* 718 */     this.size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastModifiedDate(Date mtime) {
/* 726 */     this.mtime = mtime.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getAccessTime() {
/* 734 */     return new Date(this.atime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessTime(Date atime) {
/* 742 */     this.atime = atime.getTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUserId() {
/* 750 */     return this.uid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserId(int uid) {
/* 758 */     this.uid = uid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGroupId() {
/* 766 */     return this.gid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGroupId(int gid) {
/* 774 */     this.gid = gid;
/*     */   }
/*     */   
/*     */   public enum TYPE {
/* 778 */     WHITEOUT(14),
/* 779 */     SOCKET(12),
/* 780 */     LINK(10),
/* 781 */     FILE(8),
/* 782 */     BLKDEV(6),
/* 783 */     DIRECTORY(4),
/* 784 */     CHRDEV(2),
/* 785 */     FIFO(1),
/* 786 */     UNKNOWN(15);
/*     */     
/*     */     private final int code;
/*     */     
/*     */     TYPE(int code) {
/* 791 */       this.code = code;
/*     */     }
/*     */     
/*     */     public static TYPE find(int code) {
/* 795 */       TYPE type = UNKNOWN;
/*     */       
/* 797 */       for (TYPE t : values()) {
/* 798 */         if (code == t.code) {
/* 799 */           type = t;
/*     */         }
/*     */       } 
/*     */       
/* 803 */       return type;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum PERMISSION {
/* 808 */     SETUID(2048),
/* 809 */     SETGUI(1024),
/* 810 */     STICKY(512),
/* 811 */     USER_READ(256),
/* 812 */     USER_WRITE(128),
/* 813 */     USER_EXEC(64),
/* 814 */     GROUP_READ(32),
/* 815 */     GROUP_WRITE(16),
/* 816 */     GROUP_EXEC(8),
/* 817 */     WORLD_READ(4),
/* 818 */     WORLD_WRITE(2),
/* 819 */     WORLD_EXEC(1);
/*     */     
/*     */     private final int code;
/*     */     
/*     */     PERMISSION(int code) {
/* 824 */       this.code = code;
/*     */     }
/*     */     
/*     */     public static Set<PERMISSION> find(int code) {
/* 828 */       Set<PERMISSION> set = new HashSet<>();
/*     */       
/* 830 */       for (PERMISSION p : values()) {
/* 831 */         if ((code & p.code) == p.code) {
/* 832 */           set.add(p);
/*     */         }
/*     */       } 
/*     */       
/* 836 */       if (set.isEmpty()) {
/* 837 */         return Collections.emptySet();
/*     */       }
/*     */       
/* 840 */       return EnumSet.copyOf(set);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\dump\DumpArchiveEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */