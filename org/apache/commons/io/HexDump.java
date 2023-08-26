/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HexDump
/*     */ {
/*     */   public static void dump(byte[] data, long offset, OutputStream stream, int index) throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException {
/*  75 */     if (index < 0 || index >= data.length) {
/*  76 */       throw new ArrayIndexOutOfBoundsException("illegal index: " + index + " into array of length " + data.length);
/*     */     }
/*     */ 
/*     */     
/*  80 */     if (stream == null) {
/*  81 */       throw new IllegalArgumentException("cannot write to nullstream");
/*     */     }
/*  83 */     long display_offset = offset + index;
/*  84 */     StringBuilder buffer = new StringBuilder(74);
/*     */     
/*  86 */     for (int j = index; j < data.length; j += 16) {
/*  87 */       int chars_read = data.length - j;
/*     */       
/*  89 */       if (chars_read > 16) {
/*  90 */         chars_read = 16;
/*     */       }
/*  92 */       dump(buffer, display_offset).append(' '); int k;
/*  93 */       for (k = 0; k < 16; k++) {
/*  94 */         if (k < chars_read) {
/*  95 */           dump(buffer, data[k + j]);
/*     */         } else {
/*  97 */           buffer.append("  ");
/*     */         } 
/*  99 */         buffer.append(' ');
/*     */       } 
/* 101 */       for (k = 0; k < chars_read; k++) {
/* 102 */         if (data[k + j] >= 32 && data[k + j] < Byte.MAX_VALUE) {
/* 103 */           buffer.append((char)data[k + j]);
/*     */         } else {
/* 105 */           buffer.append('.');
/*     */         } 
/*     */       } 
/* 108 */       buffer.append(EOL);
/*     */       
/* 110 */       stream.write(buffer.toString().getBytes(Charset.defaultCharset()));
/* 111 */       stream.flush();
/* 112 */       buffer.setLength(0);
/* 113 */       display_offset += chars_read;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   public static final String EOL = System.getProperty("line.separator");
/* 122 */   private static final char[] _hexcodes = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   private static final int[] _shifts = new int[] { 28, 24, 20, 16, 12, 8, 4, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder dump(StringBuilder _lbuffer, long value) {
/* 140 */     for (int j = 0; j < 8; j++) {
/* 141 */       _lbuffer
/* 142 */         .append(_hexcodes[(int)(value >> _shifts[j]) & 0xF]);
/*     */     }
/* 144 */     return _lbuffer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static StringBuilder dump(StringBuilder _cbuffer, byte value) {
/* 155 */     for (int j = 0; j < 2; j++) {
/* 156 */       _cbuffer.append(_hexcodes[value >> _shifts[j + 6] & 0xF]);
/*     */     }
/* 158 */     return _cbuffer;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\HexDump.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */