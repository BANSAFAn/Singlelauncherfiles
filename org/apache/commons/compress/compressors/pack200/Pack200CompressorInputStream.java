/*     */ package org.apache.commons.compress.compressors.pack200;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.util.Map;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import org.apache.commons.compress.compressors.CompressorInputStream;
/*     */ import org.apache.commons.compress.java.util.jar.Pack200;
/*     */ import org.apache.commons.compress.utils.CloseShieldFilterInputStream;
/*     */ import org.apache.commons.compress.utils.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Pack200CompressorInputStream
/*     */   extends CompressorInputStream
/*     */ {
/*     */   private final InputStream originalInput;
/*     */   private final StreamBridge streamBridge;
/*     */   
/*     */   public Pack200CompressorInputStream(InputStream in) throws IOException {
/*  61 */     this(in, Pack200Strategy.IN_MEMORY);
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
/*     */   public Pack200CompressorInputStream(InputStream in, Pack200Strategy mode) throws IOException {
/*  78 */     this(in, null, mode, null);
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
/*     */   public Pack200CompressorInputStream(InputStream in, Map<String, String> props) throws IOException {
/*  95 */     this(in, Pack200Strategy.IN_MEMORY, props);
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
/*     */   public Pack200CompressorInputStream(InputStream in, Pack200Strategy mode, Map<String, String> props) throws IOException {
/* 114 */     this(in, null, mode, props);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Pack200CompressorInputStream(File f) throws IOException {
/* 125 */     this(f, Pack200Strategy.IN_MEMORY);
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
/*     */   public Pack200CompressorInputStream(File f, Pack200Strategy mode) throws IOException {
/* 138 */     this(null, f, mode, null);
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
/*     */   public Pack200CompressorInputStream(File f, Map<String, String> props) throws IOException {
/* 152 */     this(f, Pack200Strategy.IN_MEMORY, props);
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
/*     */   public Pack200CompressorInputStream(File f, Pack200Strategy mode, Map<String, String> props) throws IOException {
/* 167 */     this(null, f, mode, props);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Pack200CompressorInputStream(InputStream in, File f, Pack200Strategy mode, Map<String, String> props) throws IOException {
/* 174 */     this.originalInput = in;
/* 175 */     this.streamBridge = mode.newStreamBridge();
/* 176 */     try (JarOutputStream jarOut = new JarOutputStream(this.streamBridge)) {
/* 177 */       Pack200.Unpacker u = Pack200.newUnpacker();
/* 178 */       if (props != null) {
/* 179 */         u.properties().putAll(props);
/*     */       }
/* 181 */       if (f == null) {
/*     */ 
/*     */         
/* 184 */         try (CloseShieldFilterInputStream closeShield = new CloseShieldFilterInputStream(in)) {
/* 185 */           u.unpack((InputStream)closeShield, jarOut);
/*     */         } 
/*     */       } else {
/* 188 */         u.unpack(f, jarOut);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 195 */     return this.streamBridge.getInput().read();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 200 */     return this.streamBridge.getInput().read(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int count) throws IOException {
/* 205 */     return this.streamBridge.getInput().read(b, off, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/* 210 */     return this.streamBridge.getInput().available();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*     */     try {
/* 216 */       return this.streamBridge.getInput().markSupported();
/* 217 */     } catch (IOException ex) {
/* 218 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void mark(int limit) {
/*     */     try {
/* 225 */       this.streamBridge.getInput().mark(limit);
/* 226 */     } catch (IOException ex) {
/* 227 */       throw new UncheckedIOException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/* 233 */     this.streamBridge.getInput().reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long count) throws IOException {
/* 238 */     return IOUtils.skip(this.streamBridge.getInput(), count);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 244 */       this.streamBridge.stop();
/*     */     } finally {
/* 246 */       if (this.originalInput != null) {
/* 247 */         this.originalInput.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/* 252 */   private static final byte[] CAFE_DOOD = new byte[] { -54, -2, -48, 13 };
/*     */ 
/*     */   
/* 255 */   private static final int SIG_LENGTH = CAFE_DOOD.length;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean matches(byte[] signature, int length) {
/* 269 */     if (length < SIG_LENGTH) {
/* 270 */       return false;
/*     */     }
/*     */     
/* 273 */     for (int i = 0; i < SIG_LENGTH; i++) {
/* 274 */       if (signature[i] != CAFE_DOOD[i]) {
/* 275 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 279 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\pack200\Pack200CompressorInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */