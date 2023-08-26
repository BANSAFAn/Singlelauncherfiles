/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.io.input.XmlStreamReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlStreamWriter
/*     */   extends Writer
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private final OutputStream out;
/*     */   private final String defaultEncoding;
/*  48 */   private StringWriter xmlPrologWriter = new StringWriter(8192);
/*     */ 
/*     */ 
/*     */   
/*     */   private Writer writer;
/*     */ 
/*     */ 
/*     */   
/*     */   private String encoding;
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamWriter(OutputStream out) {
/*  61 */     this(out, (String)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamWriter(OutputStream out, String defaultEncoding) {
/*  72 */     this.out = out;
/*  73 */     this.defaultEncoding = (defaultEncoding != null) ? defaultEncoding : "UTF-8";
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
/*     */   public XmlStreamWriter(File file) throws FileNotFoundException {
/*  85 */     this(file, (String)null);
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
/*     */   public XmlStreamWriter(File file, String defaultEncoding) throws FileNotFoundException {
/*  99 */     this(new FileOutputStream(file), defaultEncoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 108 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultEncoding() {
/* 117 */     return this.defaultEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 127 */     if (this.writer == null) {
/* 128 */       this.encoding = this.defaultEncoding;
/* 129 */       this.writer = new OutputStreamWriter(this.out, this.encoding);
/* 130 */       this.writer.write(this.xmlPrologWriter.toString());
/*     */     } 
/* 132 */     this.writer.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 142 */     if (this.writer != null) {
/* 143 */       this.writer.flush();
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
/*     */   private void detectEncoding(char[] cbuf, int off, int len) throws IOException {
/* 157 */     int size = len;
/* 158 */     StringBuffer xmlProlog = this.xmlPrologWriter.getBuffer();
/* 159 */     if (xmlProlog.length() + len > 8192) {
/* 160 */       size = 8192 - xmlProlog.length();
/*     */     }
/* 162 */     this.xmlPrologWriter.write(cbuf, off, size);
/*     */ 
/*     */     
/* 165 */     if (xmlProlog.length() >= 5) {
/* 166 */       if (xmlProlog.substring(0, 5).equals("<?xml")) {
/*     */         
/* 168 */         int xmlPrologEnd = xmlProlog.indexOf("?>");
/* 169 */         if (xmlPrologEnd > 0) {
/*     */           
/* 171 */           Matcher m = ENCODING_PATTERN.matcher(xmlProlog.substring(0, xmlPrologEnd));
/*     */           
/* 173 */           if (m.find()) {
/* 174 */             this.encoding = m.group(1).toUpperCase(Locale.ROOT);
/* 175 */             this.encoding = this.encoding.substring(1, this.encoding.length() - 1);
/*     */           }
/*     */           else {
/*     */             
/* 179 */             this.encoding = this.defaultEncoding;
/*     */           } 
/* 181 */         } else if (xmlProlog.length() >= 8192) {
/*     */ 
/*     */           
/* 184 */           this.encoding = this.defaultEncoding;
/*     */         } 
/*     */       } else {
/*     */         
/* 188 */         this.encoding = this.defaultEncoding;
/*     */       } 
/* 190 */       if (this.encoding != null) {
/*     */         
/* 192 */         this.xmlPrologWriter = null;
/* 193 */         this.writer = new OutputStreamWriter(this.out, this.encoding);
/* 194 */         this.writer.write(xmlProlog.toString());
/* 195 */         if (len > size) {
/* 196 */           this.writer.write(cbuf, off + size, len - size);
/*     */         }
/*     */       } 
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
/* 212 */     if (this.xmlPrologWriter != null) {
/* 213 */       detectEncoding(cbuf, off, len);
/*     */     } else {
/* 215 */       this.writer.write(cbuf, off, len);
/*     */     } 
/*     */   }
/*     */   
/* 219 */   static final Pattern ENCODING_PATTERN = XmlStreamReader.ENCODING_PATTERN;
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\XmlStreamWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */