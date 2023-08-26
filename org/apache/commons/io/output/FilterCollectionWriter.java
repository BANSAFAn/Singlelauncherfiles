/*     */ package org.apache.commons.io.output;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.apache.commons.io.IOExceptionList;
/*     */ import org.apache.commons.io.IOIndexedException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FilterCollectionWriter
/*     */   extends Writer
/*     */ {
/*  51 */   protected final Collection<Writer> EMPTY_WRITERS = Collections.emptyList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Collection<Writer> writers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FilterCollectionWriter(Collection<Writer> writers) {
/*  64 */     this.writers = (writers == null) ? this.EMPTY_WRITERS : writers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FilterCollectionWriter(Writer... writers) {
/*  73 */     this.writers = (writers == null) ? this.EMPTY_WRITERS : Arrays.<Writer>asList(writers);
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
/*     */   private List<Exception> add(List<Exception> causeList, int i, IOException e) {
/*  85 */     if (causeList == null) {
/*  86 */       causeList = new ArrayList<>();
/*     */     }
/*  88 */     causeList.add(new IOIndexedException(i, e));
/*  89 */     return causeList;
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(char c) throws IOException {
/*  94 */     List<Exception> causeList = null;
/*  95 */     int i = 0;
/*  96 */     for (Writer w : this.writers) {
/*  97 */       if (w != null) {
/*     */         try {
/*  99 */           w.append(c);
/* 100 */         } catch (IOException e) {
/* 101 */           causeList = add(causeList, i, e);
/*     */         } 
/*     */       }
/* 104 */       i++;
/*     */     } 
/* 106 */     if (notEmpty(causeList)) {
/* 107 */       throw new IOExceptionList("append", causeList);
/*     */     }
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(CharSequence csq) throws IOException {
/* 114 */     List<Exception> causeList = null;
/* 115 */     int i = 0;
/* 116 */     for (Writer w : this.writers) {
/* 117 */       if (w != null) {
/*     */         try {
/* 119 */           w.append(csq);
/* 120 */         } catch (IOException e) {
/* 121 */           causeList = add(causeList, i, e);
/*     */         } 
/*     */       }
/* 124 */       i++;
/*     */     } 
/* 126 */     if (notEmpty(causeList)) {
/* 127 */       throw new IOExceptionList("append", causeList);
/*     */     }
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Writer append(CharSequence csq, int start, int end) throws IOException {
/* 135 */     List<Exception> causeList = null;
/* 136 */     int i = 0;
/* 137 */     for (Writer w : this.writers) {
/* 138 */       if (w != null) {
/*     */         try {
/* 140 */           w.append(csq, start, end);
/* 141 */         } catch (IOException e) {
/* 142 */           causeList = add(causeList, i, e);
/*     */         } 
/*     */       }
/* 145 */       i++;
/*     */     } 
/* 147 */     if (notEmpty(causeList)) {
/* 148 */       throw new IOExceptionList("append", causeList);
/*     */     }
/* 150 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 155 */     List<Exception> causeList = null;
/* 156 */     int i = 0;
/* 157 */     for (Writer w : this.writers) {
/* 158 */       if (w != null) {
/*     */         try {
/* 160 */           w.close();
/* 161 */         } catch (IOException e) {
/* 162 */           causeList = add(causeList, i, e);
/*     */         } 
/*     */       }
/* 165 */       i++;
/*     */     } 
/* 167 */     if (notEmpty(causeList)) {
/* 168 */       throw new IOExceptionList("close", causeList);
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
/*     */   public void flush() throws IOException {
/* 180 */     List<Exception> causeList = null;
/* 181 */     int i = 0;
/* 182 */     for (Writer w : this.writers) {
/* 183 */       if (w != null) {
/*     */         try {
/* 185 */           w.flush();
/* 186 */         } catch (IOException e) {
/* 187 */           causeList = add(causeList, i, e);
/*     */         } 
/*     */       }
/* 190 */       i++;
/*     */     } 
/* 192 */     if (notEmpty(causeList)) {
/* 193 */       throw new IOExceptionList("flush", causeList);
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
/*     */   private boolean notEmpty(List<Exception> causeList) {
/* 205 */     return (causeList != null && !causeList.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(char[] cbuf) throws IOException {
/* 210 */     List<Exception> causeList = null;
/* 211 */     int i = 0;
/* 212 */     for (Writer w : this.writers) {
/* 213 */       if (w != null) {
/*     */         try {
/* 215 */           w.write(cbuf);
/* 216 */         } catch (IOException e) {
/* 217 */           causeList = add(causeList, i, e);
/*     */         } 
/*     */       }
/* 220 */       i++;
/*     */     } 
/* 222 */     if (notEmpty(causeList)) {
/* 223 */       throw new IOExceptionList("write", causeList);
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
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/* 238 */     List<Exception> causeList = null;
/* 239 */     int i = 0;
/* 240 */     for (Writer w : this.writers) {
/* 241 */       if (w != null) {
/*     */         try {
/* 243 */           w.write(cbuf, off, len);
/* 244 */         } catch (IOException e) {
/* 245 */           causeList = add(causeList, i, e);
/*     */         } 
/*     */       }
/* 248 */       i++;
/*     */     } 
/* 250 */     if (notEmpty(causeList)) {
/* 251 */       throw new IOExceptionList("write", causeList);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int c) throws IOException {
/* 262 */     List<Exception> causeList = null;
/* 263 */     int i = 0;
/* 264 */     for (Writer w : this.writers) {
/* 265 */       if (w != null) {
/*     */         try {
/* 267 */           w.write(c);
/* 268 */         } catch (IOException e) {
/* 269 */           causeList = add(causeList, i, e);
/*     */         } 
/*     */       }
/* 272 */       i++;
/*     */     } 
/* 274 */     if (notEmpty(causeList)) {
/* 275 */       throw new IOExceptionList("write", causeList);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/* 281 */     List<Exception> causeList = null;
/* 282 */     int i = 0;
/* 283 */     for (Writer w : this.writers) {
/* 284 */       if (w != null) {
/*     */         try {
/* 286 */           w.write(str);
/* 287 */         } catch (IOException e) {
/* 288 */           causeList = add(causeList, i, e);
/*     */         } 
/*     */       }
/* 291 */       i++;
/*     */     } 
/* 293 */     if (notEmpty(causeList)) {
/* 294 */       throw new IOExceptionList("write", causeList);
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
/*     */   
/*     */   public void write(String str, int off, int len) throws IOException {
/* 310 */     List<Exception> causeList = null;
/* 311 */     int i = 0;
/* 312 */     for (Writer w : this.writers) {
/* 313 */       if (w != null) {
/*     */         try {
/* 315 */           w.write(str, off, len);
/* 316 */         } catch (IOException e) {
/* 317 */           causeList = add(causeList, i, e);
/*     */         } 
/*     */       }
/* 320 */       i++;
/*     */     } 
/* 322 */     if (notEmpty(causeList))
/* 323 */       throw new IOExceptionList("write", causeList); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\FilterCollectionWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */