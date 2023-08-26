/*     */ package org.apache.commons.compress.compressors.lz4;
/*     */ 
/*     */ import java.util.zip.Checksum;
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
/*     */ public class XXHash32
/*     */   implements Checksum
/*     */ {
/*     */   private static final int BUF_SIZE = 16;
/*     */   private static final int ROTATE_BITS = 13;
/*     */   private static final int PRIME1 = -1640531535;
/*     */   private static final int PRIME2 = -2048144777;
/*     */   private static final int PRIME3 = -1028477379;
/*     */   private static final int PRIME4 = 668265263;
/*     */   private static final int PRIME5 = 374761393;
/*  44 */   private final byte[] oneByte = new byte[1];
/*  45 */   private final int[] state = new int[4];
/*     */ 
/*     */   
/*  48 */   private final byte[] buffer = new byte[16];
/*     */   
/*     */   private final int seed;
/*     */   
/*     */   private int totalLen;
/*     */   
/*     */   private int pos;
/*     */ 
/*     */   
/*     */   public XXHash32() {
/*  58 */     this(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XXHash32(int seed) {
/*  66 */     this.seed = seed;
/*  67 */     initializeState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/*  72 */     initializeState();
/*  73 */     this.totalLen = 0;
/*  74 */     this.pos = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(int b) {
/*  79 */     this.oneByte[0] = (byte)(b & 0xFF);
/*  80 */     update(this.oneByte, 0, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(byte[] b, int off, int len) {
/*  85 */     if (len <= 0) {
/*     */       return;
/*     */     }
/*  88 */     this.totalLen += len;
/*     */     
/*  90 */     int end = off + len;
/*     */     
/*  92 */     if (this.pos + len < 16) {
/*  93 */       System.arraycopy(b, off, this.buffer, this.pos, len);
/*  94 */       this.pos += len;
/*     */       
/*     */       return;
/*     */     } 
/*  98 */     if (this.pos > 0) {
/*  99 */       int size = 16 - this.pos;
/* 100 */       System.arraycopy(b, off, this.buffer, this.pos, size);
/* 101 */       process(this.buffer, 0);
/* 102 */       off += size;
/*     */     } 
/*     */     
/* 105 */     int limit = end - 16;
/* 106 */     while (off <= limit) {
/* 107 */       process(b, off);
/* 108 */       off += 16;
/*     */     } 
/*     */     
/* 111 */     if (off < end) {
/* 112 */       this.pos = end - off;
/* 113 */       System.arraycopy(b, off, this.buffer, 0, this.pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getValue() {
/*     */     int hash;
/* 120 */     if (this.totalLen > 16) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 125 */       hash = Integer.rotateLeft(this.state[0], 1) + Integer.rotateLeft(this.state[1], 7) + Integer.rotateLeft(this.state[2], 12) + Integer.rotateLeft(this.state[3], 18);
/*     */     } else {
/* 127 */       hash = this.state[2] + 374761393;
/*     */     } 
/* 129 */     hash += this.totalLen;
/*     */     
/* 131 */     int idx = 0;
/* 132 */     int limit = this.pos - 4;
/* 133 */     for (; idx <= limit; idx += 4) {
/* 134 */       hash = Integer.rotateLeft(hash + getInt(this.buffer, idx) * -1028477379, 17) * 668265263;
/*     */     }
/* 136 */     while (idx < this.pos) {
/* 137 */       hash = Integer.rotateLeft(hash + (this.buffer[idx++] & 0xFF) * 374761393, 11) * -1640531535;
/*     */     }
/*     */     
/* 140 */     hash ^= hash >>> 15;
/* 141 */     hash *= -2048144777;
/* 142 */     hash ^= hash >>> 13;
/* 143 */     hash *= -1028477379;
/* 144 */     hash ^= hash >>> 16;
/* 145 */     return hash & 0xFFFFFFFFL;
/*     */   }
/*     */   
/*     */   private static int getInt(byte[] buffer, int idx) {
/* 149 */     return (int)(ByteUtils.fromLittleEndian(buffer, idx, 4) & 0xFFFFFFFFL);
/*     */   }
/*     */   
/*     */   private void initializeState() {
/* 153 */     this.state[0] = this.seed + -1640531535 + -2048144777;
/* 154 */     this.state[1] = this.seed + -2048144777;
/* 155 */     this.state[2] = this.seed;
/* 156 */     this.state[3] = this.seed - -1640531535;
/*     */   }
/*     */ 
/*     */   
/*     */   private void process(byte[] b, int offset) {
/* 161 */     int s0 = this.state[0];
/* 162 */     int s1 = this.state[1];
/* 163 */     int s2 = this.state[2];
/* 164 */     int s3 = this.state[3];
/*     */     
/* 166 */     s0 = Integer.rotateLeft(s0 + getInt(b, offset) * -2048144777, 13) * -1640531535;
/* 167 */     s1 = Integer.rotateLeft(s1 + getInt(b, offset + 4) * -2048144777, 13) * -1640531535;
/* 168 */     s2 = Integer.rotateLeft(s2 + getInt(b, offset + 8) * -2048144777, 13) * -1640531535;
/* 169 */     s3 = Integer.rotateLeft(s3 + getInt(b, offset + 12) * -2048144777, 13) * -1640531535;
/*     */     
/* 171 */     this.state[0] = s0;
/* 172 */     this.state[1] = s1;
/* 173 */     this.state[2] = s2;
/* 174 */     this.state[3] = s3;
/*     */     
/* 176 */     this.pos = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\lz4\XXHash32.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */