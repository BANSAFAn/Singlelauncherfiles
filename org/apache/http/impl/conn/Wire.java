/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class Wire
/*     */ {
/*     */   private final Log log;
/*     */   private final String id;
/*     */   
/*     */   public Wire(Log log, String id) {
/*  53 */     this.log = log;
/*  54 */     this.id = id;
/*     */   }
/*     */   
/*     */   public Wire(Log log) {
/*  58 */     this(log, "");
/*     */   }
/*     */ 
/*     */   
/*     */   private void wire(String header, InputStream instream) throws IOException {
/*  63 */     StringBuilder buffer = new StringBuilder();
/*     */     int ch;
/*  65 */     while ((ch = instream.read()) != -1) {
/*  66 */       if (ch == 13) {
/*  67 */         buffer.append("[\\r]"); continue;
/*  68 */       }  if (ch == 10) {
/*  69 */         buffer.append("[\\n]\"");
/*  70 */         buffer.insert(0, "\"");
/*  71 */         buffer.insert(0, header);
/*  72 */         this.log.debug(this.id + " " + buffer.toString());
/*  73 */         buffer.setLength(0); continue;
/*  74 */       }  if (ch < 32 || ch > 127) {
/*  75 */         buffer.append("[0x");
/*  76 */         buffer.append(Integer.toHexString(ch));
/*  77 */         buffer.append("]"); continue;
/*     */       } 
/*  79 */       buffer.append((char)ch);
/*     */     } 
/*     */     
/*  82 */     if (buffer.length() > 0) {
/*  83 */       buffer.append('"');
/*  84 */       buffer.insert(0, '"');
/*  85 */       buffer.insert(0, header);
/*  86 */       this.log.debug(this.id + " " + buffer.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean enabled() {
/*  92 */     return this.log.isDebugEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(InputStream outstream) throws IOException {
/*  97 */     Args.notNull(outstream, "Output");
/*  98 */     wire(">> ", outstream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void input(InputStream instream) throws IOException {
/* 103 */     Args.notNull(instream, "Input");
/* 104 */     wire("<< ", instream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(byte[] b, int off, int len) throws IOException {
/* 109 */     Args.notNull(b, "Output");
/* 110 */     wire(">> ", new ByteArrayInputStream(b, off, len));
/*     */   }
/*     */ 
/*     */   
/*     */   public void input(byte[] b, int off, int len) throws IOException {
/* 115 */     Args.notNull(b, "Input");
/* 116 */     wire("<< ", new ByteArrayInputStream(b, off, len));
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(byte[] b) throws IOException {
/* 121 */     Args.notNull(b, "Output");
/* 122 */     wire(">> ", new ByteArrayInputStream(b));
/*     */   }
/*     */ 
/*     */   
/*     */   public void input(byte[] b) throws IOException {
/* 127 */     Args.notNull(b, "Input");
/* 128 */     wire("<< ", new ByteArrayInputStream(b));
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(int b) throws IOException {
/* 133 */     output(new byte[] { (byte)b });
/*     */   }
/*     */ 
/*     */   
/*     */   public void input(int b) throws IOException {
/* 138 */     input(new byte[] { (byte)b });
/*     */   }
/*     */ 
/*     */   
/*     */   public void output(String s) throws IOException {
/* 143 */     Args.notNull(s, "Output");
/* 144 */     output(s.getBytes());
/*     */   }
/*     */ 
/*     */   
/*     */   public void input(String s) throws IOException {
/* 149 */     Args.notNull(s, "Input");
/* 150 */     input(s.getBytes());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\conn\Wire.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */