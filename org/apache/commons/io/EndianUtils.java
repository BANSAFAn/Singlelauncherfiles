/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EndianUtils
/*     */ {
/*     */   public static short swapShort(short value) {
/*  56 */     return (short)(((value >> 0 & 0xFF) << 8) + ((value >> 8 & 0xFF) << 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int swapInteger(int value) {
/*  66 */     return ((value >> 0 & 0xFF) << 24) + ((value >> 8 & 0xFF) << 16) + ((value >> 16 & 0xFF) << 8) + ((value >> 24 & 0xFF) << 0);
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
/*     */   public static long swapLong(long value) {
/*  79 */     return ((value >> 0L & 0xFFL) << 56L) + ((value >> 8L & 0xFFL) << 48L) + ((value >> 16L & 0xFFL) << 40L) + ((value >> 24L & 0xFFL) << 32L) + ((value >> 32L & 0xFFL) << 24L) + ((value >> 40L & 0xFFL) << 16L) + ((value >> 48L & 0xFFL) << 8L) + ((value >> 56L & 0xFFL) << 0L);
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
/*     */   public static float swapFloat(float value) {
/*  96 */     return Float.intBitsToFloat(swapInteger(Float.floatToIntBits(value)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double swapDouble(double value) {
/* 105 */     return Double.longBitsToDouble(swapLong(Double.doubleToLongBits(value)));
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
/*     */   public static void writeSwappedShort(byte[] data, int offset, short value) {
/* 118 */     data[offset + 0] = (byte)(value >> 0 & 0xFF);
/* 119 */     data[offset + 1] = (byte)(value >> 8 & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short readSwappedShort(byte[] data, int offset) {
/* 130 */     return (short)(((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8));
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
/*     */   public static int readSwappedUnsignedShort(byte[] data, int offset) {
/* 143 */     return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8);
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
/*     */   public static void writeSwappedInteger(byte[] data, int offset, int value) {
/* 155 */     data[offset + 0] = (byte)(value >> 0 & 0xFF);
/* 156 */     data[offset + 1] = (byte)(value >> 8 & 0xFF);
/* 157 */     data[offset + 2] = (byte)(value >> 16 & 0xFF);
/* 158 */     data[offset + 3] = (byte)(value >> 24 & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int readSwappedInteger(byte[] data, int offset) {
/* 169 */     return ((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16) + ((data[offset + 3] & 0xFF) << 24);
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
/*     */   public static long readSwappedUnsignedInteger(byte[] data, int offset) {
/* 184 */     long low = (((data[offset + 0] & 0xFF) << 0) + ((data[offset + 1] & 0xFF) << 8) + ((data[offset + 2] & 0xFF) << 16));
/*     */ 
/*     */ 
/*     */     
/* 188 */     long high = (data[offset + 3] & 0xFF);
/*     */     
/* 190 */     return (high << 24L) + (0xFFFFFFFFL & low);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedLong(byte[] data, int offset, long value) {
/* 201 */     data[offset + 0] = (byte)(int)(value >> 0L & 0xFFL);
/* 202 */     data[offset + 1] = (byte)(int)(value >> 8L & 0xFFL);
/* 203 */     data[offset + 2] = (byte)(int)(value >> 16L & 0xFFL);
/* 204 */     data[offset + 3] = (byte)(int)(value >> 24L & 0xFFL);
/* 205 */     data[offset + 4] = (byte)(int)(value >> 32L & 0xFFL);
/* 206 */     data[offset + 5] = (byte)(int)(value >> 40L & 0xFFL);
/* 207 */     data[offset + 6] = (byte)(int)(value >> 48L & 0xFFL);
/* 208 */     data[offset + 7] = (byte)(int)(value >> 56L & 0xFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long readSwappedLong(byte[] data, int offset) {
/* 219 */     long low = readSwappedInteger(data, offset);
/* 220 */     long high = readSwappedInteger(data, offset + 4);
/* 221 */     return (high << 32L) + (0xFFFFFFFFL & low);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedFloat(byte[] data, int offset, float value) {
/* 232 */     writeSwappedInteger(data, offset, Float.floatToIntBits(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float readSwappedFloat(byte[] data, int offset) {
/* 243 */     return Float.intBitsToFloat(readSwappedInteger(data, offset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedDouble(byte[] data, int offset, double value) {
/* 254 */     writeSwappedLong(data, offset, Double.doubleToLongBits(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double readSwappedDouble(byte[] data, int offset) {
/* 265 */     return Double.longBitsToDouble(readSwappedLong(data, offset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedShort(OutputStream output, short value) throws IOException {
/* 276 */     output.write((byte)(value >> 0 & 0xFF));
/* 277 */     output.write((byte)(value >> 8 & 0xFF));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short readSwappedShort(InputStream input) throws IOException {
/* 288 */     return (short)(((read(input) & 0xFF) << 0) + ((read(input) & 0xFF) << 8));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int readSwappedUnsignedShort(InputStream input) throws IOException {
/* 299 */     int value1 = read(input);
/* 300 */     int value2 = read(input);
/*     */     
/* 302 */     return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedInteger(OutputStream output, int value) throws IOException {
/* 313 */     output.write((byte)(value >> 0 & 0xFF));
/* 314 */     output.write((byte)(value >> 8 & 0xFF));
/* 315 */     output.write((byte)(value >> 16 & 0xFF));
/* 316 */     output.write((byte)(value >> 24 & 0xFF));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int readSwappedInteger(InputStream input) throws IOException {
/* 327 */     int value1 = read(input);
/* 328 */     int value2 = read(input);
/* 329 */     int value3 = read(input);
/* 330 */     int value4 = read(input);
/*     */     
/* 332 */     return ((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16) + ((value4 & 0xFF) << 24);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long readSwappedUnsignedInteger(InputStream input) throws IOException {
/* 343 */     int value1 = read(input);
/* 344 */     int value2 = read(input);
/* 345 */     int value3 = read(input);
/* 346 */     int value4 = read(input);
/*     */     
/* 348 */     long low = (((value1 & 0xFF) << 0) + ((value2 & 0xFF) << 8) + ((value3 & 0xFF) << 16));
/*     */     
/* 350 */     long high = (value4 & 0xFF);
/*     */     
/* 352 */     return (high << 24L) + (0xFFFFFFFFL & low);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedLong(OutputStream output, long value) throws IOException {
/* 363 */     output.write((byte)(int)(value >> 0L & 0xFFL));
/* 364 */     output.write((byte)(int)(value >> 8L & 0xFFL));
/* 365 */     output.write((byte)(int)(value >> 16L & 0xFFL));
/* 366 */     output.write((byte)(int)(value >> 24L & 0xFFL));
/* 367 */     output.write((byte)(int)(value >> 32L & 0xFFL));
/* 368 */     output.write((byte)(int)(value >> 40L & 0xFFL));
/* 369 */     output.write((byte)(int)(value >> 48L & 0xFFL));
/* 370 */     output.write((byte)(int)(value >> 56L & 0xFFL));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long readSwappedLong(InputStream input) throws IOException {
/* 381 */     byte[] bytes = new byte[8];
/* 382 */     for (int i = 0; i < 8; i++) {
/* 383 */       bytes[i] = (byte)read(input);
/*     */     }
/* 385 */     return readSwappedLong(bytes, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedFloat(OutputStream output, float value) throws IOException {
/* 396 */     writeSwappedInteger(output, Float.floatToIntBits(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float readSwappedFloat(InputStream input) throws IOException {
/* 407 */     return Float.intBitsToFloat(readSwappedInteger(input));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeSwappedDouble(OutputStream output, double value) throws IOException {
/* 418 */     writeSwappedLong(output, Double.doubleToLongBits(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double readSwappedDouble(InputStream input) throws IOException {
/* 429 */     return Double.longBitsToDouble(readSwappedLong(input));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int read(InputStream input) throws IOException {
/* 439 */     int value = input.read();
/*     */     
/* 441 */     if (-1 == value) {
/* 442 */       throw new EOFException("Unexpected EOF reached");
/*     */     }
/*     */     
/* 445 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\EndianUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */