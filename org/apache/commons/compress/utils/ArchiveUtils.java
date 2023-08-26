/*     */ package org.apache.commons.compress.utils;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
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
/*     */ public class ArchiveUtils
/*     */ {
/*     */   private static final int MAX_SANITIZED_NAME_LENGTH = 255;
/*     */   
/*     */   public static String toString(ArchiveEntry entry) {
/*  51 */     StringBuilder sb = new StringBuilder();
/*  52 */     sb.append(entry.isDirectory() ? 100 : 45);
/*  53 */     String size = Long.toString(entry.getSize());
/*  54 */     sb.append(' ');
/*     */     
/*  56 */     for (int i = 7; i > size.length(); i--) {
/*  57 */       sb.append(' ');
/*     */     }
/*  59 */     sb.append(size);
/*  60 */     sb.append(' ').append(entry.getName());
/*  61 */     return sb.toString();
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
/*     */   public static boolean matchAsciiBuffer(String expected, byte[] buffer, int offset, int length) {
/*  76 */     byte[] buffer1 = expected.getBytes(StandardCharsets.US_ASCII);
/*  77 */     return isEqual(buffer1, 0, buffer1.length, buffer, offset, length, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matchAsciiBuffer(String expected, byte[] buffer) {
/*  88 */     return matchAsciiBuffer(expected, buffer, 0, buffer.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toAsciiBytes(String inputString) {
/*  99 */     return inputString.getBytes(StandardCharsets.US_ASCII);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toAsciiString(byte[] inputBytes) {
/* 109 */     return new String(inputBytes, StandardCharsets.US_ASCII);
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
/*     */   public static String toAsciiString(byte[] inputBytes, int offset, int length) {
/* 121 */     return new String(inputBytes, offset, length, StandardCharsets.US_ASCII);
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
/*     */   public static boolean isEqual(byte[] buffer1, int offset1, int length1, byte[] buffer2, int offset2, int length2, boolean ignoreTrailingNulls) {
/* 140 */     int minLen = Math.min(length1, length2); int i;
/* 141 */     for (i = 0; i < minLen; i++) {
/* 142 */       if (buffer1[offset1 + i] != buffer2[offset2 + i]) {
/* 143 */         return false;
/*     */       }
/*     */     } 
/* 146 */     if (length1 == length2) {
/* 147 */       return true;
/*     */     }
/* 149 */     if (ignoreTrailingNulls) {
/* 150 */       if (length1 > length2) {
/* 151 */         for (i = length2; i < length1; i++) {
/* 152 */           if (buffer1[offset1 + i] != 0) {
/* 153 */             return false;
/*     */           }
/*     */         } 
/*     */       } else {
/* 157 */         for (i = length1; i < length2; i++) {
/* 158 */           if (buffer2[offset2 + i] != 0) {
/* 159 */             return false;
/*     */           }
/*     */         } 
/*     */       } 
/* 163 */       return true;
/*     */     } 
/* 165 */     return false;
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
/*     */   public static boolean isEqual(byte[] buffer1, int offset1, int length1, byte[] buffer2, int offset2, int length2) {
/* 182 */     return isEqual(buffer1, offset1, length1, buffer2, offset2, length2, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isEqual(byte[] buffer1, byte[] buffer2) {
/* 193 */     return isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
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
/*     */   public static boolean isEqual(byte[] buffer1, byte[] buffer2, boolean ignoreTrailingNulls) {
/* 205 */     return isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, ignoreTrailingNulls);
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
/*     */   public static boolean isEqualWithNull(byte[] buffer1, int offset1, int length1, byte[] buffer2, int offset2, int length2) {
/* 222 */     return isEqual(buffer1, offset1, length1, buffer2, offset2, length2, true);
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
/*     */   public static boolean isArrayZero(byte[] a, int size) {
/* 235 */     for (int i = 0; i < size; i++) {
/* 236 */       if (a[i] != 0) {
/* 237 */         return false;
/*     */       }
/*     */     } 
/* 240 */     return true;
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
/*     */   public static String sanitize(String s) {
/* 259 */     char[] cs = s.toCharArray();
/* 260 */     char[] chars = (cs.length <= 255) ? cs : Arrays.copyOf(cs, 255);
/* 261 */     if (cs.length > 255) {
/* 262 */       Arrays.fill(chars, 252, 255, '.');
/*     */     }
/* 264 */     StringBuilder sb = new StringBuilder();
/* 265 */     for (char c : chars) {
/* 266 */       if (!Character.isISOControl(c)) {
/* 267 */         Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
/* 268 */         if (block != null && block != Character.UnicodeBlock.SPECIALS) {
/* 269 */           sb.append(c);
/*     */           continue;
/*     */         } 
/*     */       } 
/* 273 */       sb.append('?'); continue;
/*     */     } 
/* 275 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compres\\utils\ArchiveUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */