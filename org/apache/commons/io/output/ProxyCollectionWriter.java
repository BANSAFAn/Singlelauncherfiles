/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Collection;
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
/*     */ public class ProxyCollectionWriter
/*     */   extends FilterCollectionWriter
/*     */ {
/*     */   public ProxyCollectionWriter(Collection<Writer> writers) {
/*  44 */     super(writers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProxyCollectionWriter(Writer... writers) {
/*  53 */     super(writers);
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
/*     */   protected void afterWrite(int n) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Writer append(char c) throws IOException {
/*     */     try {
/*  83 */       beforeWrite(1);
/*  84 */       super.append(c);
/*  85 */       afterWrite(1);
/*  86 */     } catch (IOException e) {
/*  87 */       handleIOException(e);
/*     */     } 
/*  89 */     return this;
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
/*     */   public Writer append(CharSequence csq) throws IOException {
/*     */     try {
/* 102 */       int len = IOUtils.length(csq);
/* 103 */       beforeWrite(len);
/* 104 */       super.append(csq);
/* 105 */       afterWrite(len);
/* 106 */     } catch (IOException e) {
/* 107 */       handleIOException(e);
/*     */     } 
/* 109 */     return this;
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
/*     */   public Writer append(CharSequence csq, int start, int end) throws IOException {
/*     */     try {
/* 124 */       beforeWrite(end - start);
/* 125 */       super.append(csq, start, end);
/* 126 */       afterWrite(end - start);
/* 127 */     } catch (IOException e) {
/* 128 */       handleIOException(e);
/*     */     } 
/* 130 */     return this;
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
/*     */   protected void beforeWrite(int n) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 157 */       super.close();
/* 158 */     } catch (IOException e) {
/* 159 */       handleIOException(e);
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
/*     */     try {
/* 171 */       super.flush();
/* 172 */     } catch (IOException e) {
/* 173 */       handleIOException(e);
/*     */     } 
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
/*     */   protected void handleIOException(IOException e) throws IOException {
/* 188 */     throw e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] cbuf) throws IOException {
/*     */     try {
/* 200 */       int len = IOUtils.length(cbuf);
/* 201 */       beforeWrite(len);
/* 202 */       super.write(cbuf);
/* 203 */       afterWrite(len);
/* 204 */     } catch (IOException e) {
/* 205 */       handleIOException(e);
/*     */     } 
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
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/*     */     try {
/* 220 */       beforeWrite(len);
/* 221 */       super.write(cbuf, off, len);
/* 222 */       afterWrite(len);
/* 223 */     } catch (IOException e) {
/* 224 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int c) throws IOException {
/*     */     try {
/* 237 */       beforeWrite(1);
/* 238 */       super.write(c);
/* 239 */       afterWrite(1);
/* 240 */     } catch (IOException e) {
/* 241 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/*     */     try {
/* 254 */       int len = IOUtils.length(str);
/* 255 */       beforeWrite(len);
/* 256 */       super.write(str);
/* 257 */       afterWrite(len);
/* 258 */     } catch (IOException e) {
/* 259 */       handleIOException(e);
/*     */     } 
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
/*     */   public void write(String str, int off, int len) throws IOException {
/*     */     try {
/* 274 */       beforeWrite(len);
/* 275 */       super.write(str, off, len);
/* 276 */       afterWrite(len);
/* 277 */     } catch (IOException e) {
/* 278 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\ProxyCollectionWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */