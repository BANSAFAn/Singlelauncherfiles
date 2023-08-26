/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.io.FileUtils;
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
/*     */ public class FileWriterWithEncoding
/*     */   extends Writer
/*     */ {
/*     */   private final Writer out;
/*     */   
/*     */   public FileWriterWithEncoding(String fileName, String charsetName) throws IOException {
/*  68 */     this(new File(fileName), charsetName, false);
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
/*     */   public FileWriterWithEncoding(String fileName, String charsetName, boolean append) throws IOException {
/*  82 */     this(new File(fileName), charsetName, append);
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
/*     */   public FileWriterWithEncoding(String fileName, Charset charset) throws IOException {
/*  94 */     this(new File(fileName), charset, false);
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
/*     */   public FileWriterWithEncoding(String fileName, Charset charset, boolean append) throws IOException {
/* 108 */     this(new File(fileName), charset, append);
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
/*     */   public FileWriterWithEncoding(String fileName, CharsetEncoder encoding) throws IOException {
/* 120 */     this(new File(fileName), encoding, false);
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
/*     */   public FileWriterWithEncoding(String fileName, CharsetEncoder charsetEncoder, boolean append) throws IOException {
/* 134 */     this(new File(fileName), charsetEncoder, append);
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
/*     */   public FileWriterWithEncoding(File file, String charsetName) throws IOException {
/* 146 */     this(file, charsetName, false);
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
/*     */   public FileWriterWithEncoding(File file, String charsetName, boolean append) throws IOException {
/* 159 */     this.out = initWriter(file, charsetName, append);
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
/*     */   public FileWriterWithEncoding(File file, Charset charset) throws IOException {
/* 171 */     this(file, charset, false);
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
/*     */   public FileWriterWithEncoding(File file, Charset encoding, boolean append) throws IOException {
/* 184 */     this.out = initWriter(file, encoding, append);
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
/*     */   public FileWriterWithEncoding(File file, CharsetEncoder charsetEncoder) throws IOException {
/* 196 */     this(file, charsetEncoder, false);
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
/*     */   public FileWriterWithEncoding(File file, CharsetEncoder charsetEncoder, boolean append) throws IOException {
/* 210 */     this.out = initWriter(file, charsetEncoder, append);
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
/*     */   private static Writer initWriter(File file, Object encoding, boolean append) throws IOException {
/* 225 */     Objects.requireNonNull(file, "file");
/* 226 */     Objects.requireNonNull(encoding, "encoding");
/* 227 */     OutputStream stream = null;
/* 228 */     boolean fileExistedAlready = file.exists();
/*     */     try {
/* 230 */       stream = Files.newOutputStream(file.toPath(), new OpenOption[] { append ? StandardOpenOption.APPEND : StandardOpenOption.CREATE });
/* 231 */       if (encoding instanceof Charset) {
/* 232 */         return new OutputStreamWriter(stream, (Charset)encoding);
/*     */       }
/* 234 */       if (encoding instanceof CharsetEncoder) {
/* 235 */         return new OutputStreamWriter(stream, (CharsetEncoder)encoding);
/*     */       }
/* 237 */       return new OutputStreamWriter(stream, (String)encoding);
/* 238 */     } catch (IOException|RuntimeException ex) {
/*     */       try {
/* 240 */         IOUtils.close(stream);
/* 241 */       } catch (IOException e) {
/* 242 */         ex.addSuppressed(e);
/*     */       } 
/* 244 */       if (!fileExistedAlready) {
/* 245 */         FileUtils.deleteQuietly(file);
/*     */       }
/* 247 */       throw ex;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int idx) throws IOException {
/* 258 */     this.out.write(idx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] chr) throws IOException {
/* 268 */     this.out.write(chr);
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
/*     */   public void write(char[] chr, int st, int end) throws IOException {
/* 280 */     this.out.write(chr, st, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/* 290 */     this.out.write(str);
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
/*     */   public void write(String str, int st, int end) throws IOException {
/* 302 */     this.out.write(str, st, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 311 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 320 */     this.out.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\FileWriterWithEncoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */