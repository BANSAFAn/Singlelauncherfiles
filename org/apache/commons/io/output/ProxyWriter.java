/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.FilterWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
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
/*     */ public class ProxyWriter
/*     */   extends FilterWriter
/*     */ {
/*     */   public ProxyWriter(Writer proxy) {
/*  39 */     super(proxy);
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
/*     */   public Writer append(char c) throws IOException {
/*     */     try {
/*  53 */       beforeWrite(1);
/*  54 */       this.out.append(c);
/*  55 */       afterWrite(1);
/*  56 */     } catch (IOException e) {
/*  57 */       handleIOException(e);
/*     */     } 
/*  59 */     return this;
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
/*  74 */       beforeWrite(end - start);
/*  75 */       this.out.append(csq, start, end);
/*  76 */       afterWrite(end - start);
/*  77 */     } catch (IOException e) {
/*  78 */       handleIOException(e);
/*     */     } 
/*  80 */     return this;
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
/*  93 */       int len = IOUtils.length(csq);
/*  94 */       beforeWrite(len);
/*  95 */       this.out.append(csq);
/*  96 */       afterWrite(len);
/*  97 */     } catch (IOException e) {
/*  98 */       handleIOException(e);
/*     */     } 
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int c) throws IOException {
/*     */     try {
/* 111 */       beforeWrite(1);
/* 112 */       this.out.write(c);
/* 113 */       afterWrite(1);
/* 114 */     } catch (IOException e) {
/* 115 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] cbuf) throws IOException {
/*     */     try {
/* 127 */       int len = IOUtils.length(cbuf);
/* 128 */       beforeWrite(len);
/* 129 */       this.out.write(cbuf);
/* 130 */       afterWrite(len);
/* 131 */     } catch (IOException e) {
/* 132 */       handleIOException(e);
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
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/*     */     try {
/* 146 */       beforeWrite(len);
/* 147 */       this.out.write(cbuf, off, len);
/* 148 */       afterWrite(len);
/* 149 */     } catch (IOException e) {
/* 150 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/*     */     try {
/* 162 */       int len = IOUtils.length(str);
/* 163 */       beforeWrite(len);
/* 164 */       this.out.write(str);
/* 165 */       afterWrite(len);
/* 166 */     } catch (IOException e) {
/* 167 */       handleIOException(e);
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
/*     */   public void write(String str, int off, int len) throws IOException {
/*     */     try {
/* 181 */       beforeWrite(len);
/* 182 */       this.out.write(str, off, len);
/* 183 */       afterWrite(len);
/* 184 */     } catch (IOException e) {
/* 185 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*     */     try {
/* 196 */       this.out.flush();
/* 197 */     } catch (IOException e) {
/* 198 */       handleIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 208 */     IOUtils.close(this.out, this::handleIOException);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleIOException(IOException e) throws IOException {
/* 262 */     throw e;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\ProxyWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */