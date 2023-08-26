/*     */ package org.apache.commons.compress.compressors.gzip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.Deflater;
/*     */ import org.apache.commons.compress.compressors.CompressorOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GzipCompressorOutputStream
/*     */   extends CompressorOutputStream
/*     */ {
/*     */   private static final int FNAME = 8;
/*     */   private static final int FCOMMENT = 16;
/*     */   private final OutputStream out;
/*     */   private final Deflater deflater;
/*     */   private final byte[] deflateBuffer;
/*     */   private boolean closed;
/*  63 */   private final CRC32 crc = new CRC32();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GzipCompressorOutputStream(OutputStream out) throws IOException {
/*  71 */     this(out, new GzipParameters());
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
/*     */   public GzipCompressorOutputStream(OutputStream out, GzipParameters parameters) throws IOException {
/*  83 */     this.out = out;
/*  84 */     this.deflater = new Deflater(parameters.getCompressionLevel(), true);
/*  85 */     this.deflateBuffer = new byte[parameters.getBufferSize()];
/*  86 */     writeHeader(parameters);
/*     */   }
/*     */   
/*     */   private void writeHeader(GzipParameters parameters) throws IOException {
/*  90 */     String filename = parameters.getFilename();
/*  91 */     String comment = parameters.getComment();
/*     */     
/*  93 */     ByteBuffer buffer = ByteBuffer.allocate(10);
/*  94 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/*  95 */     buffer.putShort((short)-29921);
/*  96 */     buffer.put((byte)8);
/*  97 */     buffer.put((byte)(((filename != null) ? 8 : 0) | ((comment != null) ? 16 : 0)));
/*  98 */     buffer.putInt((int)(parameters.getModificationTime() / 1000L));
/*     */ 
/*     */     
/* 101 */     int compressionLevel = parameters.getCompressionLevel();
/* 102 */     if (compressionLevel == 9) {
/* 103 */       buffer.put((byte)2);
/* 104 */     } else if (compressionLevel == 1) {
/* 105 */       buffer.put((byte)4);
/*     */     } else {
/* 107 */       buffer.put((byte)0);
/*     */     } 
/*     */     
/* 110 */     buffer.put((byte)parameters.getOperatingSystem());
/*     */     
/* 112 */     this.out.write(buffer.array());
/*     */     
/* 114 */     if (filename != null) {
/* 115 */       this.out.write(filename.getBytes(StandardCharsets.ISO_8859_1));
/* 116 */       this.out.write(0);
/*     */     } 
/*     */     
/* 119 */     if (comment != null) {
/* 120 */       this.out.write(comment.getBytes(StandardCharsets.ISO_8859_1));
/* 121 */       this.out.write(0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void writeTrailer() throws IOException {
/* 126 */     ByteBuffer buffer = ByteBuffer.allocate(8);
/* 127 */     buffer.order(ByteOrder.LITTLE_ENDIAN);
/* 128 */     buffer.putInt((int)this.crc.getValue());
/* 129 */     buffer.putInt(this.deflater.getTotalIn());
/*     */     
/* 131 */     this.out.write(buffer.array());
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 136 */     write(new byte[] { (byte)(b & 0xFF) }, 0, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] buffer) throws IOException {
/* 146 */     write(buffer, 0, buffer.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] buffer, int offset, int length) throws IOException {
/* 156 */     if (this.deflater.finished()) {
/* 157 */       throw new IOException("Cannot write more data, the end of the compressed data stream has been reached");
/*     */     }
/*     */     
/* 160 */     if (length > 0) {
/* 161 */       this.deflater.setInput(buffer, offset, length);
/*     */       
/* 163 */       while (!this.deflater.needsInput()) {
/* 164 */         deflate();
/*     */       }
/*     */       
/* 167 */       this.crc.update(buffer, offset, length);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void deflate() throws IOException {
/* 172 */     int length = this.deflater.deflate(this.deflateBuffer, 0, this.deflateBuffer.length);
/* 173 */     if (length > 0) {
/* 174 */       this.out.write(this.deflateBuffer, 0, length);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void finish() throws IOException {
/* 185 */     if (!this.deflater.finished()) {
/* 186 */       this.deflater.finish();
/*     */       
/* 188 */       while (!this.deflater.finished()) {
/* 189 */         deflate();
/*     */       }
/*     */       
/* 192 */       writeTrailer();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 203 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 208 */     if (!this.closed)
/*     */       try {
/* 210 */         finish();
/*     */       } finally {
/* 212 */         this.deflater.end();
/* 213 */         this.out.close();
/* 214 */         this.closed = true;
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\gzip\GzipCompressorOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */