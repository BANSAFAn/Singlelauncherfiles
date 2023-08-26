/*     */ package org.apache.commons.compress.archivers.dump;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.compress.archivers.zip.ZipEncoding;
/*     */ import org.apache.commons.compress.utils.ByteUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DumpArchiveUtil
/*     */ {
/*     */   public static int calculateChecksum(byte[] buffer) {
/*  44 */     int calc = 0;
/*     */     
/*  46 */     for (int i = 0; i < 256; i++) {
/*  47 */       calc += convert32(buffer, 4 * i);
/*     */     }
/*     */     
/*  50 */     return 84446 - calc - 
/*  51 */       convert32(buffer, 28);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean verify(byte[] buffer) {
/*  61 */     int magic = convert32(buffer, 24);
/*     */     
/*  63 */     if (magic != 60012) {
/*  64 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  68 */     int checksum = convert32(buffer, 28);
/*     */     
/*  70 */     return (checksum == calculateChecksum(buffer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int getIno(byte[] buffer) {
/*  79 */     return convert32(buffer, 20);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final long convert64(byte[] buffer, int offset) {
/*  90 */     return ByteUtils.fromLittleEndian(buffer, offset, 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int convert32(byte[] buffer, int offset) {
/* 101 */     return (int)ByteUtils.fromLittleEndian(buffer, offset, 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int convert16(byte[] buffer, int offset) {
/* 112 */     return (int)ByteUtils.fromLittleEndian(buffer, offset, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String decode(ZipEncoding encoding, byte[] b, int offset, int len) throws IOException {
/* 120 */     return encoding.decode(Arrays.copyOfRange(b, offset, offset + len));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\dump\DumpArchiveUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */