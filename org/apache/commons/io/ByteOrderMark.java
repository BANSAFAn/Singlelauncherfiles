/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteOrderMark
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  36 */   public static final ByteOrderMark UTF_8 = new ByteOrderMark("UTF-8", new int[] { 239, 187, 191 });
/*     */ 
/*     */   
/*  39 */   public static final ByteOrderMark UTF_16BE = new ByteOrderMark("UTF-16BE", new int[] { 254, 255 });
/*     */ 
/*     */   
/*  42 */   public static final ByteOrderMark UTF_16LE = new ByteOrderMark("UTF-16LE", new int[] { 255, 254 });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static final ByteOrderMark UTF_32BE = new ByteOrderMark("UTF-32BE", new int[] { 0, 0, 254, 255 });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   public static final ByteOrderMark UTF_32LE = new ByteOrderMark("UTF-32LE", new int[] { 255, 254, 0, 0 });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final char UTF_BOM = '﻿';
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String charsetName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int[] bytes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteOrderMark(String charsetName, int... bytes) {
/*  80 */     if (charsetName == null || charsetName.isEmpty()) {
/*  81 */       throw new IllegalArgumentException("No charsetName specified");
/*     */     }
/*  83 */     if (bytes == null || bytes.length == 0) {
/*  84 */       throw new IllegalArgumentException("No bytes specified");
/*     */     }
/*  86 */     this.charsetName = charsetName;
/*  87 */     this.bytes = new int[bytes.length];
/*  88 */     System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCharsetName() {
/*  97 */     return this.charsetName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 106 */     return this.bytes.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(int pos) {
/* 116 */     return this.bytes[pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/* 125 */     byte[] copy = IOUtils.byteArray(this.bytes.length);
/* 126 */     for (int i = 0; i < this.bytes.length; i++) {
/* 127 */       copy[i] = (byte)this.bytes[i];
/*     */     }
/* 129 */     return copy;
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
/*     */   public boolean equals(Object obj) {
/* 141 */     if (!(obj instanceof ByteOrderMark)) {
/* 142 */       return false;
/*     */     }
/* 144 */     ByteOrderMark bom = (ByteOrderMark)obj;
/* 145 */     if (this.bytes.length != bom.length()) {
/* 146 */       return false;
/*     */     }
/* 148 */     for (int i = 0; i < this.bytes.length; i++) {
/* 149 */       if (this.bytes[i] != bom.get(i)) {
/* 150 */         return false;
/*     */       }
/*     */     } 
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 164 */     int hashCode = getClass().hashCode();
/* 165 */     for (int b : this.bytes) {
/* 166 */       hashCode += b;
/*     */     }
/* 168 */     return hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 178 */     StringBuilder builder = new StringBuilder();
/* 179 */     builder.append(getClass().getSimpleName());
/* 180 */     builder.append('[');
/* 181 */     builder.append(this.charsetName);
/* 182 */     builder.append(": ");
/* 183 */     for (int i = 0; i < this.bytes.length; i++) {
/* 184 */       if (i > 0) {
/* 185 */         builder.append(",");
/*     */       }
/* 187 */       builder.append("0x");
/* 188 */       builder.append(Integer.toHexString(0xFF & this.bytes[i]).toUpperCase(Locale.ROOT));
/*     */     } 
/* 190 */     builder.append(']');
/* 191 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\ByteOrderMark.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */