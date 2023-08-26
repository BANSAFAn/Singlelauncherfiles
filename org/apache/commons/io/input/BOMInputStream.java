/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.ByteOrderMark;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BOMInputStream
/*     */   extends ProxyInputStream
/*     */ {
/*     */   private final boolean include;
/*     */   private final List<ByteOrderMark> boms;
/*     */   private ByteOrderMark byteOrderMark;
/*     */   private int[] firstBytes;
/*     */   private int fbLength;
/*     */   private int fbIndex;
/*     */   private int markFbIndex;
/*     */   private boolean markedAtStart;
/*     */   private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator;
/*     */   
/*     */   public BOMInputStream(InputStream delegate) {
/* 109 */     this(delegate, false, new ByteOrderMark[] { ByteOrderMark.UTF_8 });
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
/*     */   public BOMInputStream(InputStream delegate, boolean include) {
/* 121 */     this(delegate, include, new ByteOrderMark[] { ByteOrderMark.UTF_8 });
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
/*     */   public BOMInputStream(InputStream delegate, ByteOrderMark... boms) {
/* 133 */     this(delegate, false, boms);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 139 */     ByteOrderMarkLengthComparator = ((bom1, bom2) -> {
/*     */         int len1 = bom1.length();
/*     */         int len2 = bom2.length();
/*     */         return Integer.compare(len2, len1);
/*     */       });
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
/*     */   public BOMInputStream(InputStream delegate, boolean include, ByteOrderMark... boms) {
/* 156 */     super(delegate);
/* 157 */     if (IOUtils.length((Object[])boms) == 0) {
/* 158 */       throw new IllegalArgumentException("No BOMs specified");
/*     */     }
/* 160 */     this.include = include;
/* 161 */     List<ByteOrderMark> list = Arrays.asList(boms);
/*     */     
/* 163 */     list.sort(ByteOrderMarkLengthComparator);
/* 164 */     this.boms = list;
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
/*     */   public boolean hasBOM() throws IOException {
/* 176 */     return (getBOM() != null);
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
/*     */   public boolean hasBOM(ByteOrderMark bom) throws IOException {
/* 191 */     if (!this.boms.contains(bom)) {
/* 192 */       throw new IllegalArgumentException("Stream not configure to detect " + bom);
/*     */     }
/* 194 */     getBOM();
/* 195 */     return (this.byteOrderMark != null && this.byteOrderMark.equals(bom));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteOrderMark getBOM() throws IOException {
/* 206 */     if (this.firstBytes == null) {
/* 207 */       this.fbLength = 0;
/*     */       
/* 209 */       int maxBomSize = ((ByteOrderMark)this.boms.get(0)).length();
/* 210 */       this.firstBytes = new int[maxBomSize];
/*     */       
/* 212 */       for (int i = 0; i < this.firstBytes.length; i++) {
/* 213 */         this.firstBytes[i] = this.in.read();
/* 214 */         this.fbLength++;
/* 215 */         if (this.firstBytes[i] < 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 220 */       this.byteOrderMark = find();
/* 221 */       if (this.byteOrderMark != null && !this.include) {
/* 222 */         if (this.byteOrderMark.length() < this.firstBytes.length) {
/* 223 */           this.fbIndex = this.byteOrderMark.length();
/*     */         } else {
/* 225 */           this.fbLength = 0;
/*     */         } 
/*     */       }
/*     */     } 
/* 229 */     return this.byteOrderMark;
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
/*     */   public String getBOMCharsetName() throws IOException {
/* 241 */     getBOM();
/* 242 */     return (this.byteOrderMark == null) ? null : this.byteOrderMark.getCharsetName();
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
/*     */   private int readFirstBytes() throws IOException {
/* 255 */     getBOM();
/* 256 */     return (this.fbIndex < this.fbLength) ? this.firstBytes[this.fbIndex++] : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteOrderMark find() {
/* 265 */     for (ByteOrderMark bom : this.boms) {
/* 266 */       if (matches(bom)) {
/* 267 */         return bom;
/*     */       }
/*     */     } 
/* 270 */     return null;
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
/*     */   private boolean matches(ByteOrderMark bom) {
/* 285 */     for (int i = 0; i < bom.length(); i++) {
/* 286 */       if (bom.get(i) != this.firstBytes[i]) {
/* 287 */         return false;
/*     */       }
/*     */     } 
/* 290 */     return true;
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
/*     */   public int read() throws IOException {
/* 306 */     int b = readFirstBytes();
/* 307 */     return (b >= 0) ? b : this.in.read();
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
/*     */   public int read(byte[] buf, int off, int len) throws IOException {
/* 325 */     int firstCount = 0;
/* 326 */     int b = 0;
/* 327 */     while (len > 0 && b >= 0) {
/* 328 */       b = readFirstBytes();
/* 329 */       if (b >= 0) {
/* 330 */         buf[off++] = (byte)(b & 0xFF);
/* 331 */         len--;
/* 332 */         firstCount++;
/*     */       } 
/*     */     } 
/* 335 */     int secondCount = this.in.read(buf, off, len);
/* 336 */     return (secondCount < 0) ? ((firstCount > 0) ? firstCount : -1) : (firstCount + secondCount);
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
/*     */   public int read(byte[] buf) throws IOException {
/* 350 */     return read(buf, 0, buf.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readlimit) {
/* 361 */     this.markFbIndex = this.fbIndex;
/* 362 */     this.markedAtStart = (this.firstBytes == null);
/* 363 */     this.in.mark(readlimit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/* 374 */     this.fbIndex = this.markFbIndex;
/* 375 */     if (this.markedAtStart) {
/* 376 */       this.firstBytes = null;
/*     */     }
/*     */     
/* 379 */     this.in.reset();
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
/*     */   public long skip(long n) throws IOException {
/* 393 */     int skipped = 0;
/* 394 */     while (n > skipped && readFirstBytes() >= 0) {
/* 395 */       skipped++;
/*     */     }
/* 397 */     return this.in.skip(n - skipped) + skipped;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\BOMInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */