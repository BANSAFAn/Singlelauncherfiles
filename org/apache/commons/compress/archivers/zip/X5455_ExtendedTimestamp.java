/*     */ package org.apache.commons.compress.archivers.zip;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class X5455_ExtendedTimestamp
/*     */   implements ZipExtraField, Cloneable, Serializable
/*     */ {
/*  86 */   private static final ZipShort HEADER_ID = new ZipShort(21589);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte MODIFY_TIME_BIT = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte ACCESS_TIME_BIT = 2;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte CREATE_TIME_BIT = 4;
/*     */ 
/*     */ 
/*     */   
/*     */   private byte flags;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean bit0_modifyTimePresent;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean bit1_accessTimePresent;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean bit2_createTimePresent;
/*     */ 
/*     */ 
/*     */   
/*     */   private ZipLong modifyTime;
/*     */ 
/*     */   
/*     */   private ZipLong accessTime;
/*     */ 
/*     */   
/*     */   private ZipLong createTime;
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getHeaderId() {
/* 133 */     return HEADER_ID;
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
/* 144 */     return new ZipShort(1 + (this.bit0_modifyTimePresent ? 4 : 0) + ((this.bit1_accessTimePresent && this.accessTime != null) ? 4 : 0) + ((this.bit2_createTimePresent && this.createTime != null) ? 4 : 0));
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
/*     */   public ZipShort getCentralDirectoryLength() {
/* 163 */     return new ZipShort(1 + (this.bit0_modifyTimePresent ? 4 : 0));
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
/*     */   public byte[] getLocalFileDataData() {
/* 176 */     byte[] data = new byte[getLocalFileDataLength().getValue()];
/* 177 */     int pos = 0;
/* 178 */     data[pos++] = 0;
/* 179 */     if (this.bit0_modifyTimePresent) {
/* 180 */       data[0] = (byte)(data[0] | 0x1);
/* 181 */       System.arraycopy(this.modifyTime.getBytes(), 0, data, pos, 4);
/* 182 */       pos += 4;
/*     */     } 
/* 184 */     if (this.bit1_accessTimePresent && this.accessTime != null) {
/* 185 */       data[0] = (byte)(data[0] | 0x2);
/* 186 */       System.arraycopy(this.accessTime.getBytes(), 0, data, pos, 4);
/* 187 */       pos += 4;
/*     */     } 
/* 189 */     if (this.bit2_createTimePresent && this.createTime != null) {
/* 190 */       data[0] = (byte)(data[0] | 0x4);
/* 191 */       System.arraycopy(this.createTime.getBytes(), 0, data, pos, 4);
/* 192 */       pos += 4;
/*     */     } 
/* 194 */     return data;
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
/*     */   public byte[] getCentralDirectoryData() {
/* 207 */     return Arrays.copyOf(getLocalFileDataData(), getCentralDirectoryLength().getValue());
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
/* 222 */     reset();
/* 223 */     if (length < 1) {
/* 224 */       throw new ZipException("X5455_ExtendedTimestamp too short, only " + length + " bytes");
/*     */     }
/* 226 */     int len = offset + length;
/* 227 */     setFlags(data[offset++]);
/* 228 */     if (this.bit0_modifyTimePresent && offset + 4 <= len) {
/* 229 */       this.modifyTime = new ZipLong(data, offset);
/* 230 */       offset += 4;
/*     */     } else {
/* 232 */       this.bit0_modifyTimePresent = false;
/*     */     } 
/* 234 */     if (this.bit1_accessTimePresent && offset + 4 <= len) {
/* 235 */       this.accessTime = new ZipLong(data, offset);
/* 236 */       offset += 4;
/*     */     } else {
/* 238 */       this.bit1_accessTimePresent = false;
/*     */     } 
/* 240 */     if (this.bit2_createTimePresent && offset + 4 <= len) {
/* 241 */       this.createTime = new ZipLong(data, offset);
/* 242 */       offset += 4;
/*     */     } else {
/* 244 */       this.bit2_createTimePresent = false;
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
/* 256 */     reset();
/* 257 */     parseFromLocalFileData(buffer, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void reset() {
/* 265 */     setFlags((byte)0);
/* 266 */     this.modifyTime = null;
/* 267 */     this.accessTime = null;
/* 268 */     this.createTime = null;
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
/*     */   public void setFlags(byte flags) {
/* 286 */     this.flags = flags;
/* 287 */     this.bit0_modifyTimePresent = ((flags & 0x1) == 1);
/* 288 */     this.bit1_accessTimePresent = ((flags & 0x2) == 2);
/* 289 */     this.bit2_createTimePresent = ((flags & 0x4) == 4);
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
/*     */   public byte getFlags() {
/* 306 */     return this.flags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBit0_modifyTimePresent() {
/* 315 */     return this.bit0_modifyTimePresent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBit1_accessTimePresent() {
/* 324 */     return this.bit1_accessTimePresent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBit2_createTimePresent() {
/* 333 */     return this.bit2_createTimePresent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipLong getModifyTime() {
/* 342 */     return this.modifyTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipLong getAccessTime() {
/* 351 */     return this.accessTime;
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
/*     */   public ZipLong getCreateTime() {
/* 366 */     return this.createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getModifyJavaTime() {
/* 377 */     return zipLongToDate(this.modifyTime);
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
/*     */   public Date getAccessJavaTime() {
/* 389 */     return zipLongToDate(this.accessTime);
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
/*     */   public Date getCreateJavaTime() {
/* 407 */     return zipLongToDate(this.createTime);
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
/*     */   public void setModifyTime(ZipLong l) {
/* 423 */     this.bit0_modifyTimePresent = (l != null);
/* 424 */     this.flags = (byte)((l != null) ? (this.flags | 0x1) : (this.flags & 0xFFFFFFFE));
/*     */     
/* 426 */     this.modifyTime = l;
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
/*     */   public void setAccessTime(ZipLong l) {
/* 442 */     this.bit1_accessTimePresent = (l != null);
/* 443 */     this.flags = (byte)((l != null) ? (this.flags | 0x2) : (this.flags & 0xFFFFFFFD));
/*     */     
/* 445 */     this.accessTime = l;
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
/*     */   public void setCreateTime(ZipLong l) {
/* 461 */     this.bit2_createTimePresent = (l != null);
/* 462 */     this.flags = (byte)((l != null) ? (this.flags | 0x4) : (this.flags & 0xFFFFFFFB));
/*     */     
/* 464 */     this.createTime = l;
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
/*     */   public void setModifyJavaTime(Date d) {
/* 480 */     setModifyTime(dateToZipLong(d));
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
/*     */   public void setAccessJavaTime(Date d) {
/* 495 */     setAccessTime(dateToZipLong(d));
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
/* 510 */     setCreateTime(dateToZipLong(d));
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
/*     */   private static ZipLong dateToZipLong(Date d) {
/* 523 */     if (d == null) return null;
/*     */     
/* 525 */     return unixTimeToZipLong(d.getTime() / 1000L);
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
/*     */   public String toString() {
/* 537 */     StringBuilder buf = new StringBuilder();
/* 538 */     buf.append("0x5455 Zip Extra Field: Flags=");
/* 539 */     buf.append(Integer.toBinaryString(ZipUtil.unsignedIntToSignedByte(this.flags))).append(" ");
/* 540 */     if (this.bit0_modifyTimePresent && this.modifyTime != null) {
/* 541 */       Date m = getModifyJavaTime();
/* 542 */       buf.append(" Modify:[").append(m).append("] ");
/*     */     } 
/* 544 */     if (this.bit1_accessTimePresent && this.accessTime != null) {
/* 545 */       Date a = getAccessJavaTime();
/* 546 */       buf.append(" Access:[").append(a).append("] ");
/*     */     } 
/* 548 */     if (this.bit2_createTimePresent && this.createTime != null) {
/* 549 */       Date c = getCreateJavaTime();
/* 550 */       buf.append(" Create:[").append(c).append("] ");
/*     */     } 
/* 552 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 557 */     return super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 562 */     if (o instanceof X5455_ExtendedTimestamp) {
/* 563 */       X5455_ExtendedTimestamp xf = (X5455_ExtendedTimestamp)o;
/*     */ 
/*     */ 
/*     */       
/* 567 */       return ((this.flags & 0x7) == (xf.flags & 0x7) && 
/* 568 */         Objects.equals(this.modifyTime, xf.modifyTime) && 
/* 569 */         Objects.equals(this.accessTime, xf.accessTime) && 
/* 570 */         Objects.equals(this.createTime, xf.createTime));
/*     */     } 
/* 572 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 577 */     int hc = -123 * (this.flags & 0x7);
/* 578 */     if (this.modifyTime != null) {
/* 579 */       hc ^= this.modifyTime.hashCode();
/*     */     }
/* 581 */     if (this.accessTime != null)
/*     */     {
/*     */       
/* 584 */       hc ^= Integer.rotateLeft(this.accessTime.hashCode(), 11);
/*     */     }
/* 586 */     if (this.createTime != null) {
/* 587 */       hc ^= Integer.rotateLeft(this.createTime.hashCode(), 22);
/*     */     }
/* 589 */     return hc;
/*     */   }
/*     */   
/*     */   private static Date zipLongToDate(ZipLong unixTime) {
/* 593 */     return (unixTime != null) ? new Date(unixTime.getIntValue() * 1000L) : null;
/*     */   }
/*     */   
/*     */   private static ZipLong unixTimeToZipLong(long l) {
/* 597 */     if (l < -2147483648L || l > 2147483647L) {
/* 598 */       throw new IllegalArgumentException("X5455 timestamps must fit in a signed 32 bit integer: " + l);
/*     */     }
/* 600 */     return new ZipLong(l);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\X5455_ExtendedTimestamp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */