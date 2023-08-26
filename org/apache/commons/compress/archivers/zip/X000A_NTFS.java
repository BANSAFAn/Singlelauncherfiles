/*     */ package org.apache.commons.compress.archivers.zip;
/*     */ 
/*     */ import java.util.Date;
/*     */ import java.util.Objects;
/*     */ import java.util.zip.ZipException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class X000A_NTFS
/*     */   implements ZipExtraField
/*     */ {
/*  68 */   private static final ZipShort HEADER_ID = new ZipShort(10);
/*  69 */   private static final ZipShort TIME_ATTR_TAG = new ZipShort(1);
/*  70 */   private static final ZipShort TIME_ATTR_SIZE = new ZipShort(24);
/*     */   
/*  72 */   private ZipEightByteInteger modifyTime = ZipEightByteInteger.ZERO;
/*  73 */   private ZipEightByteInteger accessTime = ZipEightByteInteger.ZERO;
/*  74 */   private ZipEightByteInteger createTime = ZipEightByteInteger.ZERO;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long EPOCH_OFFSET = -116444736000000000L;
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getHeaderId() {
/*  83 */     return HEADER_ID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getLocalFileDataLength() {
/*  94 */     return new ZipShort(32);
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
/*     */   public ZipShort getCentralDirectoryLength() {
/* 112 */     return getLocalFileDataLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getLocalFileDataData() {
/* 123 */     byte[] data = new byte[getLocalFileDataLength().getValue()];
/* 124 */     int pos = 4;
/* 125 */     System.arraycopy(TIME_ATTR_TAG.getBytes(), 0, data, pos, 2);
/* 126 */     pos += 2;
/* 127 */     System.arraycopy(TIME_ATTR_SIZE.getBytes(), 0, data, pos, 2);
/* 128 */     pos += 2;
/* 129 */     System.arraycopy(this.modifyTime.getBytes(), 0, data, pos, 8);
/* 130 */     pos += 8;
/* 131 */     System.arraycopy(this.accessTime.getBytes(), 0, data, pos, 8);
/* 132 */     pos += 8;
/* 133 */     System.arraycopy(this.createTime.getBytes(), 0, data, pos, 8);
/* 134 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getCentralDirectoryData() {
/* 145 */     return getLocalFileDataData();
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
/*     */   public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
/* 160 */     int len = offset + length;
/*     */ 
/*     */     
/* 163 */     offset += 4;
/*     */     
/* 165 */     while (offset + 4 <= len) {
/* 166 */       ZipShort tag = new ZipShort(data, offset);
/* 167 */       offset += 2;
/* 168 */       if (tag.equals(TIME_ATTR_TAG)) {
/* 169 */         readTimeAttr(data, offset, len - offset);
/*     */         break;
/*     */       } 
/* 172 */       ZipShort size = new ZipShort(data, offset);
/* 173 */       offset += 2 + size.getValue();
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
/*     */   public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) throws ZipException {
/* 185 */     reset();
/* 186 */     parseFromLocalFileData(buffer, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipEightByteInteger getModifyTime() {
/* 197 */     return this.modifyTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipEightByteInteger getAccessTime() {
/* 206 */     return this.accessTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipEightByteInteger getCreateTime() {
/* 215 */     return this.createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getModifyJavaTime() {
/* 224 */     return zipToDate(this.modifyTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getAccessJavaTime() {
/* 234 */     return zipToDate(this.accessTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreateJavaTime() {
/* 244 */     return zipToDate(this.createTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModifyTime(ZipEightByteInteger t) {
/* 254 */     this.modifyTime = (t == null) ? ZipEightByteInteger.ZERO : t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessTime(ZipEightByteInteger t) {
/* 264 */     this.accessTime = (t == null) ? ZipEightByteInteger.ZERO : t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreateTime(ZipEightByteInteger t) {
/* 274 */     this.createTime = (t == null) ? ZipEightByteInteger.ZERO : t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModifyJavaTime(Date d) {
/* 282 */     setModifyTime(dateToZip(d));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccessJavaTime(Date d) {
/* 290 */     setAccessTime(dateToZip(d));
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
/*     */   public void setCreateJavaTime(Date d) {
/* 305 */     setCreateTime(dateToZip(d));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 316 */     StringBuilder buf = new StringBuilder();
/* 317 */     buf.append("0x000A Zip Extra Field:")
/* 318 */       .append(" Modify:[").append(getModifyJavaTime()).append("] ")
/* 319 */       .append(" Access:[").append(getAccessJavaTime()).append("] ")
/* 320 */       .append(" Create:[").append(getCreateJavaTime()).append("] ");
/* 321 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 326 */     if (o instanceof X000A_NTFS) {
/* 327 */       X000A_NTFS xf = (X000A_NTFS)o;
/*     */       
/* 329 */       return (Objects.equals(this.modifyTime, xf.modifyTime) && 
/* 330 */         Objects.equals(this.accessTime, xf.accessTime) && 
/* 331 */         Objects.equals(this.createTime, xf.createTime));
/*     */     } 
/* 333 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 338 */     int hc = -123;
/* 339 */     if (this.modifyTime != null) {
/* 340 */       hc ^= this.modifyTime.hashCode();
/*     */     }
/* 342 */     if (this.accessTime != null)
/*     */     {
/*     */       
/* 345 */       hc ^= Integer.rotateLeft(this.accessTime.hashCode(), 11);
/*     */     }
/* 347 */     if (this.createTime != null) {
/* 348 */       hc ^= Integer.rotateLeft(this.createTime.hashCode(), 22);
/*     */     }
/* 350 */     return hc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reset() {
/* 358 */     this.modifyTime = ZipEightByteInteger.ZERO;
/* 359 */     this.accessTime = ZipEightByteInteger.ZERO;
/* 360 */     this.createTime = ZipEightByteInteger.ZERO;
/*     */   }
/*     */   
/*     */   private void readTimeAttr(byte[] data, int offset, int length) {
/* 364 */     if (length >= 26) {
/* 365 */       ZipShort tagValueLength = new ZipShort(data, offset);
/* 366 */       if (TIME_ATTR_SIZE.equals(tagValueLength)) {
/* 367 */         offset += 2;
/* 368 */         this.modifyTime = new ZipEightByteInteger(data, offset);
/* 369 */         offset += 8;
/* 370 */         this.accessTime = new ZipEightByteInteger(data, offset);
/* 371 */         offset += 8;
/* 372 */         this.createTime = new ZipEightByteInteger(data, offset);
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
/*     */   private static ZipEightByteInteger dateToZip(Date d) {
/* 385 */     if (d == null) return null; 
/* 386 */     return new ZipEightByteInteger(d.getTime() * 10000L - -116444736000000000L);
/*     */   }
/*     */   
/*     */   private static Date zipToDate(ZipEightByteInteger z) {
/* 390 */     if (z == null || ZipEightByteInteger.ZERO.equals(z)) return null; 
/* 391 */     long l = (z.getLongValue() + -116444736000000000L) / 10000L;
/* 392 */     return new Date(l);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\X000A_NTFS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */