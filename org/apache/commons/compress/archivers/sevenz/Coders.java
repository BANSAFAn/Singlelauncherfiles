/*     */ package org.apache.commons.compress.archivers.sevenz;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.SequenceInputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.zip.Deflater;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ import java.util.zip.Inflater;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
/*     */ import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
/*     */ import org.apache.commons.compress.compressors.deflate64.Deflate64CompressorInputStream;
/*     */ import org.apache.commons.compress.utils.FlushShieldFilterOutputStream;
/*     */ import org.tukaani.xz.ARMOptions;
/*     */ import org.tukaani.xz.ARMThumbOptions;
/*     */ import org.tukaani.xz.FilterOptions;
/*     */ import org.tukaani.xz.FinishableOutputStream;
/*     */ import org.tukaani.xz.FinishableWrapperOutputStream;
/*     */ import org.tukaani.xz.IA64Options;
/*     */ import org.tukaani.xz.PowerPCOptions;
/*     */ import org.tukaani.xz.SPARCOptions;
/*     */ import org.tukaani.xz.X86Options;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Coders
/*     */ {
/*  47 */   private static final Map<SevenZMethod, CoderBase> CODER_MAP = new HashMap<SevenZMethod, CoderBase>()
/*     */     {
/*     */       private static final long serialVersionUID = 1664829131806520867L;
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static CoderBase findByMethod(SevenZMethod method) {
/*  70 */     return CODER_MAP.get(method);
/*     */   }
/*     */ 
/*     */   
/*     */   static InputStream addDecoder(String archiveName, InputStream is, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
/*  75 */     CoderBase cb = findByMethod(SevenZMethod.byId(coder.decompressionMethodId));
/*  76 */     if (cb == null) {
/*  77 */       throw new IOException("Unsupported compression method " + 
/*  78 */           Arrays.toString(coder.decompressionMethodId) + " used in " + archiveName);
/*     */     }
/*     */     
/*  81 */     return cb.decode(archiveName, is, uncompressedLength, coder, password, maxMemoryLimitInKb);
/*     */   }
/*     */ 
/*     */   
/*     */   static OutputStream addEncoder(OutputStream out, SevenZMethod method, Object options) throws IOException {
/*  86 */     CoderBase cb = findByMethod(method);
/*  87 */     if (cb == null) {
/*  88 */       throw new IOException("Unsupported compression method " + method);
/*     */     }
/*  90 */     return cb.encode(out, options);
/*     */   }
/*     */   static class CopyDecoder extends CoderBase { CopyDecoder() {
/*  93 */       super(new Class[0]);
/*     */     }
/*     */     
/*     */     InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
/*  97 */       return in;
/*     */     }
/*     */     
/*     */     OutputStream encode(OutputStream out, Object options) {
/* 101 */       return out;
/*     */     } }
/*     */   
/*     */   static class BCJDecoder extends CoderBase { private final FilterOptions opts;
/*     */     
/*     */     BCJDecoder(FilterOptions opts) {
/* 107 */       super(new Class[0]);
/* 108 */       this.opts = opts;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
/*     */       try {
/* 115 */         return this.opts.getInputStream(in);
/* 116 */       } catch (AssertionError e) {
/* 117 */         throw new IOException("BCJ filter used in " + archiveName + " needs XZ for Java > 1.4 - see https://commons.apache.org/proper/commons-compress/limitations.html#7Z", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     OutputStream encode(OutputStream out, Object options) {
/* 127 */       return (OutputStream)new FlushShieldFilterOutputStream((OutputStream)this.opts.getOutputStream((FinishableOutputStream)new FinishableWrapperOutputStream(out)));
/*     */     } }
/*     */ 
/*     */   
/*     */   static class DeflateDecoder extends CoderBase {
/* 132 */     private static final byte[] ONE_ZERO_BYTE = new byte[1];
/*     */     DeflateDecoder() {
/* 134 */       super(new Class[] { Number.class });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
/* 142 */       Inflater inflater = new Inflater(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 148 */       InflaterInputStream inflaterInputStream = new InflaterInputStream(new SequenceInputStream(in, new ByteArrayInputStream(ONE_ZERO_BYTE)), inflater);
/*     */       
/* 150 */       return new DeflateDecoderInputStream(inflaterInputStream, inflater);
/*     */     }
/*     */ 
/*     */     
/*     */     OutputStream encode(OutputStream out, Object options) {
/* 155 */       int level = numberOptionOrDefault(options, 9);
/* 156 */       Deflater deflater = new Deflater(level, true);
/* 157 */       DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(out, deflater);
/* 158 */       return new DeflateDecoderOutputStream(deflaterOutputStream, deflater);
/*     */     }
/*     */     
/*     */     static class DeflateDecoderInputStream
/*     */       extends InputStream
/*     */     {
/*     */       final InflaterInputStream inflaterInputStream;
/*     */       Inflater inflater;
/*     */       
/*     */       public DeflateDecoderInputStream(InflaterInputStream inflaterInputStream, Inflater inflater) {
/* 168 */         this.inflaterInputStream = inflaterInputStream;
/* 169 */         this.inflater = inflater;
/*     */       }
/*     */ 
/*     */       
/*     */       public int read() throws IOException {
/* 174 */         return this.inflaterInputStream.read();
/*     */       }
/*     */ 
/*     */       
/*     */       public int read(byte[] b, int off, int len) throws IOException {
/* 179 */         return this.inflaterInputStream.read(b, off, len);
/*     */       }
/*     */ 
/*     */       
/*     */       public int read(byte[] b) throws IOException {
/* 184 */         return this.inflaterInputStream.read(b);
/*     */       }
/*     */ 
/*     */       
/*     */       public void close() throws IOException {
/*     */         try {
/* 190 */           this.inflaterInputStream.close();
/*     */         } finally {
/* 192 */           this.inflater.end();
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     static class DeflateDecoderOutputStream
/*     */       extends OutputStream
/*     */     {
/*     */       final DeflaterOutputStream deflaterOutputStream;
/*     */       Deflater deflater;
/*     */       
/*     */       public DeflateDecoderOutputStream(DeflaterOutputStream deflaterOutputStream, Deflater deflater) {
/* 204 */         this.deflaterOutputStream = deflaterOutputStream;
/* 205 */         this.deflater = deflater;
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(int b) throws IOException {
/* 210 */         this.deflaterOutputStream.write(b);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(byte[] b) throws IOException {
/* 215 */         this.deflaterOutputStream.write(b);
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(byte[] b, int off, int len) throws IOException {
/* 220 */         this.deflaterOutputStream.write(b, off, len);
/*     */       }
/*     */ 
/*     */       
/*     */       public void close() throws IOException {
/*     */         try {
/* 226 */           this.deflaterOutputStream.close();
/*     */         } finally {
/* 228 */           this.deflater.end();
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class Deflate64Decoder extends CoderBase {
/*     */     Deflate64Decoder() {
/* 236 */       super(new Class[] { Number.class });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
/* 244 */       return (InputStream)new Deflate64CompressorInputStream(in);
/*     */     }
/*     */   }
/*     */   
/*     */   static class BZIP2Decoder extends CoderBase {
/*     */     BZIP2Decoder() {
/* 250 */       super(new Class[] { Number.class });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     InputStream decode(String archiveName, InputStream in, long uncompressedLength, Coder coder, byte[] password, int maxMemoryLimitInKb) throws IOException {
/* 257 */       return (InputStream)new BZip2CompressorInputStream(in);
/*     */     }
/*     */ 
/*     */     
/*     */     OutputStream encode(OutputStream out, Object options) throws IOException {
/* 262 */       int blockSize = numberOptionOrDefault(options, 9);
/* 263 */       return (OutputStream)new BZip2CompressorOutputStream(out, blockSize);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\sevenz\Coders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */