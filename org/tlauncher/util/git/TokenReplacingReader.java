/*     */ package org.tlauncher.util.git;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PushbackReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenReplacingReader
/*     */   extends Reader
/*     */ {
/*  14 */   protected PushbackReader pushbackReader = null;
/*  15 */   protected ITokenResolver tokenResolver = null;
/*  16 */   protected StringBuilder tokenNameBuffer = new StringBuilder();
/*  17 */   protected String tokenValue = null;
/*  18 */   protected int tokenValueIndex = 0;
/*     */   
/*     */   public TokenReplacingReader(Reader source, ITokenResolver resolver) {
/*  21 */     this.pushbackReader = new PushbackReader(source, 2);
/*  22 */     this.tokenResolver = resolver;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(CharBuffer target) throws IOException {
/*  27 */     throw new RuntimeException("Operation Not Supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  32 */     if (this.tokenValue != null) {
/*  33 */       if (this.tokenValueIndex < this.tokenValue.length()) {
/*  34 */         return this.tokenValue.charAt(this.tokenValueIndex++);
/*     */       }
/*  36 */       if (this.tokenValueIndex == this.tokenValue.length()) {
/*  37 */         this.tokenValue = null;
/*  38 */         this.tokenValueIndex = 0;
/*     */       } 
/*     */     } 
/*     */     
/*  42 */     int data = this.pushbackReader.read();
/*  43 */     if (data != 36) return data;
/*     */     
/*  45 */     data = this.pushbackReader.read();
/*  46 */     if (data != 123) {
/*  47 */       this.pushbackReader.unread(data);
/*  48 */       return 36;
/*     */     } 
/*  50 */     this.tokenNameBuffer.delete(0, this.tokenNameBuffer.length());
/*     */     
/*  52 */     data = this.pushbackReader.read();
/*  53 */     while (data != 125) {
/*  54 */       this.tokenNameBuffer.append((char)data);
/*  55 */       data = this.pushbackReader.read();
/*     */     } 
/*     */     
/*  58 */     this
/*  59 */       .tokenValue = this.tokenResolver.resolveToken(this.tokenNameBuffer.toString());
/*     */     
/*  61 */     if (this.tokenValue == null) {
/*  62 */       this.tokenValue = "${" + this.tokenNameBuffer.toString() + "}";
/*     */     }
/*  64 */     if (this.tokenValue.length() == 0) {
/*  65 */       return read();
/*     */     }
/*  67 */     return this.tokenValue.charAt(this.tokenValueIndex++);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] cbuf) throws IOException {
/*  74 */     return read(cbuf, 0, cbuf.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/*  79 */     int charsRead = 0;
/*  80 */     for (int i = 0; i < len; i++) {
/*  81 */       int nextChar = read();
/*  82 */       if (nextChar == -1) {
/*  83 */         if (charsRead == 0) {
/*  84 */           charsRead = -1;
/*     */         }
/*     */         break;
/*     */       } 
/*  88 */       charsRead = i + 1;
/*  89 */       cbuf[off + i] = (char)nextChar;
/*     */     } 
/*  91 */     return charsRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  96 */     this.pushbackReader.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 101 */     throw new RuntimeException("Operation Not Supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ready() throws IOException {
/* 106 */     return this.pushbackReader.ready();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void mark(int readAheadLimit) throws IOException {
/* 116 */     throw new RuntimeException("Operation Not Supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/* 121 */     throw new RuntimeException("Operation Not Supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\git\TokenReplacingReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */