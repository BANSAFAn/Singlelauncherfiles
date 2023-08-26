/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.commons.compress.utils.ExactMath;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BHSDCodec
/*     */   extends Codec
/*     */ {
/*     */   private final int b;
/*     */   private final int d;
/*     */   private final int h;
/*     */   private final int l;
/*     */   private final int s;
/*     */   private long cardinality;
/*     */   private final long smallest;
/*     */   private final long largest;
/*     */   private final long[] powers;
/*     */   
/*     */   public BHSDCodec(int b, int h) {
/* 114 */     this(b, h, 0, 0);
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
/*     */   public BHSDCodec(int b, int h, int s) {
/* 126 */     this(b, h, s, 0);
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
/*     */   public BHSDCodec(int b, int h, int s, int d) {
/* 139 */     if (b < 1 || b > 5) {
/* 140 */       throw new IllegalArgumentException("1<=b<=5");
/*     */     }
/* 142 */     if (h < 1 || h > 256) {
/* 143 */       throw new IllegalArgumentException("1<=h<=256");
/*     */     }
/* 145 */     if (s < 0 || s > 2) {
/* 146 */       throw new IllegalArgumentException("0<=s<=2");
/*     */     }
/* 148 */     if (d < 0 || d > 1) {
/* 149 */       throw new IllegalArgumentException("0<=d<=1");
/*     */     }
/* 151 */     if (b == 1 && h != 256) {
/* 152 */       throw new IllegalArgumentException("b=1 -> h=256");
/*     */     }
/* 154 */     if (h == 256 && b == 5) {
/* 155 */       throw new IllegalArgumentException("h=256 -> b!=5");
/*     */     }
/* 157 */     this.b = b;
/* 158 */     this.h = h;
/* 159 */     this.s = s;
/* 160 */     this.d = d;
/* 161 */     this.l = 256 - h;
/* 162 */     if (h == 1) {
/* 163 */       this.cardinality = (b * 255 + 1);
/*     */     } else {
/* 165 */       this.cardinality = (long)((long)(this.l * (1.0D - Math.pow(h, b)) / (1 - h)) + Math.pow(h, b));
/*     */     } 
/* 167 */     this.smallest = calculateSmallest();
/* 168 */     this.largest = calculateLargest();
/*     */     
/* 170 */     this.powers = new long[b];
/* 171 */     Arrays.setAll(this.powers, c -> (long)Math.pow(h, c));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long cardinality() {
/* 180 */     return this.cardinality;
/*     */   }
/*     */ 
/*     */   
/*     */   public int decode(InputStream in) throws IOException, Pack200Exception {
/* 185 */     if (this.d != 0) {
/* 186 */       throw new Pack200Exception("Delta encoding used without passing in last value; this is a coding error");
/*     */     }
/* 188 */     return decode(in, 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public int decode(InputStream in, long last) throws IOException, Pack200Exception {
/* 193 */     int n = 0;
/* 194 */     long z = 0L;
/* 195 */     long x = 0L;
/*     */     
/*     */     do {
/* 198 */       x = in.read();
/* 199 */       this.lastBandLength++;
/* 200 */       z += x * this.powers[n];
/* 201 */       n++;
/* 202 */     } while (x >= this.l && n < this.b);
/*     */     
/* 204 */     if (x == -1L) {
/* 205 */       throw new EOFException("End of stream reached whilst decoding");
/*     */     }
/*     */     
/* 208 */     if (isSigned()) {
/* 209 */       int u = (1 << this.s) - 1;
/* 210 */       if ((z & u) == u) {
/* 211 */         z = z >>> this.s ^ 0xFFFFFFFFFFFFFFFFL;
/*     */       } else {
/* 213 */         z -= z >>> this.s;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     if (isDelta()) {
/* 233 */       z += last;
/*     */     }
/* 235 */     return (int)z;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] decodeInts(int n, InputStream in) throws IOException, Pack200Exception {
/* 240 */     int[] band = super.decodeInts(n, in);
/* 241 */     if (isDelta()) {
/* 242 */       for (int i = 0; i < band.length; i++) {
/* 243 */         while (band[i] > this.largest) {
/* 244 */           band[i] = (int)(band[i] - this.cardinality);
/*     */         }
/* 246 */         while (band[i] < this.smallest) {
/* 247 */           band[i] = ExactMath.add(band[i], this.cardinality);
/*     */         }
/*     */       } 
/*     */     }
/* 251 */     return band;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] decodeInts(int n, InputStream in, int firstValue) throws IOException, Pack200Exception {
/* 257 */     int[] band = super.decodeInts(n, in, firstValue);
/* 258 */     if (isDelta()) {
/* 259 */       for (int i = 0; i < band.length; i++) {
/* 260 */         while (band[i] > this.largest) {
/* 261 */           band[i] = (int)(band[i] - this.cardinality);
/*     */         }
/* 263 */         while (band[i] < this.smallest) {
/* 264 */           band[i] = ExactMath.add(band[i], this.cardinality);
/*     */         }
/*     */       } 
/*     */     }
/* 268 */     return band;
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
/*     */   public boolean encodes(long value) {
/* 284 */     return (value >= this.smallest && value <= this.largest);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] encode(int value, int last) throws Pack200Exception {
/* 289 */     if (!encodes(value)) {
/* 290 */       throw new Pack200Exception("The codec " + this + " does not encode the value " + value);
/*     */     }
/*     */     
/* 293 */     long z = value;
/* 294 */     if (isDelta()) {
/* 295 */       z -= last;
/*     */     }
/* 297 */     if (isSigned()) {
/* 298 */       if (z < -2147483648L) {
/* 299 */         z += 4294967296L;
/* 300 */       } else if (z > 2147483647L) {
/* 301 */         z -= 4294967296L;
/*     */       } 
/* 303 */       if (z < 0L) {
/* 304 */         z = (-z << this.s) - 1L;
/* 305 */       } else if (this.s == 1) {
/* 306 */         z <<= this.s;
/*     */       } else {
/* 308 */         z += (z - z % 3L) / 3L;
/*     */       } 
/* 310 */     } else if (z < 0L) {
/*     */ 
/*     */       
/* 313 */       z += Math.min(this.cardinality, 4294967296L);
/*     */     } 
/* 315 */     if (z < 0L) {
/* 316 */       throw new Pack200Exception("unable to encode");
/*     */     }
/*     */     
/* 319 */     List<Byte> byteList = new ArrayList<>();
/* 320 */     for (int n = 0; n < this.b; n++) {
/*     */       long byteN;
/* 322 */       if (z < this.l) {
/* 323 */         byteN = z;
/*     */       } else {
/* 325 */         byteN = z % this.h;
/* 326 */         while (byteN < this.l) {
/* 327 */           byteN += this.h;
/*     */         }
/*     */       } 
/* 330 */       byteList.add(Byte.valueOf((byte)(int)byteN));
/* 331 */       if (byteN < this.l) {
/*     */         break;
/*     */       }
/* 334 */       z -= byteN;
/* 335 */       z /= this.h;
/*     */     } 
/* 337 */     byte[] bytes = new byte[byteList.size()];
/* 338 */     for (int i = 0; i < bytes.length; i++) {
/* 339 */       bytes[i] = ((Byte)byteList.get(i)).byteValue();
/*     */     }
/* 341 */     return bytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] encode(int value) throws Pack200Exception {
/* 346 */     return encode(value, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDelta() {
/* 355 */     return (this.d != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSigned() {
/* 364 */     return (this.s != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long largest() {
/* 373 */     return this.largest;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private long calculateLargest() {
/*     */     long result;
/* 380 */     if (this.d == 1) {
/* 381 */       BHSDCodec bh0 = new BHSDCodec(this.b, this.h);
/* 382 */       return bh0.largest();
/*     */     } 
/* 384 */     if (this.s == 0) {
/* 385 */       result = cardinality() - 1L;
/* 386 */     } else if (this.s == 1) {
/* 387 */       result = cardinality() / 2L - 1L;
/* 388 */     } else if (this.s == 2) {
/* 389 */       result = 3L * cardinality() / 4L - 1L;
/*     */     } else {
/* 391 */       throw new Error("Unknown s value");
/*     */     } 
/* 393 */     return Math.min(((this.s == 0) ? 4294967294L : 2147483647L) - 1L, result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long smallest() {
/* 402 */     return this.smallest;
/*     */   }
/*     */   
/*     */   private long calculateSmallest() {
/*     */     long result;
/* 407 */     if (this.d == 1 || !isSigned()) {
/* 408 */       if (this.cardinality >= 4294967296L) {
/* 409 */         result = -2147483648L;
/*     */       } else {
/* 411 */         result = 0L;
/*     */       } 
/*     */     } else {
/* 414 */       result = Math.max(-2147483648L, -cardinality() / (1 << this.s));
/*     */     } 
/* 416 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 424 */     StringBuilder buffer = new StringBuilder(11);
/* 425 */     buffer.append('(');
/* 426 */     buffer.append(this.b);
/* 427 */     buffer.append(',');
/* 428 */     buffer.append(this.h);
/* 429 */     if (this.s != 0 || this.d != 0) {
/* 430 */       buffer.append(',');
/* 431 */       buffer.append(this.s);
/*     */     } 
/* 433 */     if (this.d != 0) {
/* 434 */       buffer.append(',');
/* 435 */       buffer.append(this.d);
/*     */     } 
/* 437 */     buffer.append(')');
/* 438 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getB() {
/* 445 */     return this.b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getH() {
/* 452 */     return this.h;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getS() {
/* 459 */     return this.s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getL() {
/* 466 */     return this.l;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 471 */     if (o instanceof BHSDCodec) {
/* 472 */       BHSDCodec codec = (BHSDCodec)o;
/* 473 */       return (codec.b == this.b && codec.h == this.h && codec.s == this.s && codec.d == this.d);
/*     */     } 
/* 475 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 480 */     return ((this.b * 37 + this.h) * 37 + this.s) * 37 + this.d;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\BHSDCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */