/*     */ package org.apache.commons.compress.archivers.sevenz;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.commons.compress.MemoryLimitException;
/*     */ import org.apache.commons.compress.utils.ByteUtils;
/*     */ import org.apache.commons.compress.utils.FlushShieldFilterOutputStream;
/*     */ import org.tukaani.xz.LZMA2Options;
/*     */ import org.tukaani.xz.LZMAInputStream;
/*     */ import org.tukaani.xz.LZMAOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LZMADecoder
/*     */   extends CoderBase
/*     */ {
/*     */   LZMADecoder() {
/*  33 */     super(new Class[] { LZMA2Options.class, Number.class });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
/*  39 */     if (coder.properties == null) {
/*  40 */       throw new IOException("Missing LZMA properties");
/*     */     }
/*  42 */     if (coder.properties.length < 1) {
/*  43 */       throw new IOException("LZMA properties too short");
/*     */     }
/*  45 */     byte propsByte = coder.properties[0];
/*  46 */     int dictSize = getDictionarySize(coder);
/*  47 */     if (dictSize > 2147483632) {
/*  48 */       throw new IOException("Dictionary larger than 4GiB maximum size used in " + archiveName);
/*     */     }
/*  50 */     int memoryUsageInKb = LZMAInputStream.getMemoryUsage(dictSize, propsByte);
/*  51 */     if (memoryUsageInKb > maxMemoryLimitInKb) {
/*  52 */       throw new MemoryLimitException(memoryUsageInKb, maxMemoryLimitInKb);
/*     */     }
/*  54 */     LZMAInputStream lzmaIn = new LZMAInputStream(in, uncompressedLength, propsByte, dictSize);
/*  55 */     lzmaIn.enableRelaxedEndCondition();
/*  56 */     return (InputStream)lzmaIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   OutputStream encode(OutputStream out, Object opts) throws IOException {
/*  64 */     return (OutputStream)new FlushShieldFilterOutputStream((OutputStream)new LZMAOutputStream(out, getOptions(opts), false));
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] getOptionsAsProperties(Object opts) throws IOException {
/*  69 */     LZMA2Options options = getOptions(opts);
/*  70 */     byte props = (byte)((options.getPb() * 5 + options.getLp()) * 9 + options.getLc());
/*  71 */     int dictSize = options.getDictSize();
/*  72 */     byte[] o = new byte[5];
/*  73 */     o[0] = props;
/*  74 */     ByteUtils.toLittleEndian(o, dictSize, 1, 4);
/*  75 */     return o;
/*     */   }
/*     */ 
/*     */   
/*     */   Object getOptionsFromCoder(Coder coder, InputStream in) throws IOException {
/*  80 */     if (coder.properties == null) {
/*  81 */       throw new IOException("Missing LZMA properties");
/*     */     }
/*  83 */     if (coder.properties.length < 1) {
/*  84 */       throw new IOException("LZMA properties too short");
/*     */     }
/*  86 */     byte propsByte = coder.properties[0];
/*  87 */     int props = propsByte & 0xFF;
/*  88 */     int pb = props / 45;
/*  89 */     props -= pb * 9 * 5;
/*  90 */     int lp = props / 9;
/*  91 */     int lc = props - lp * 9;
/*  92 */     LZMA2Options opts = new LZMA2Options();
/*  93 */     opts.setPb(pb);
/*  94 */     opts.setLcLp(lc, lp);
/*  95 */     opts.setDictSize(getDictionarySize(coder));
/*  96 */     return opts;
/*     */   }
/*     */   
/*     */   private int getDictionarySize(Coder coder) throws IllegalArgumentException {
/* 100 */     return (int)ByteUtils.fromLittleEndian(coder.properties, 1, 4);
/*     */   }
/*     */   
/*     */   private LZMA2Options getOptions(Object opts) throws IOException {
/* 104 */     if (opts instanceof LZMA2Options) {
/* 105 */       return (LZMA2Options)opts;
/*     */     }
/* 107 */     LZMA2Options options = new LZMA2Options();
/* 108 */     options.setDictSize(numberOptionOrDefault(opts));
/* 109 */     return options;
/*     */   }
/*     */   
/*     */   private int numberOptionOrDefault(Object opts) {
/* 113 */     return numberOptionOrDefault(opts, 8388608);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\sevenz\LZMADecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */