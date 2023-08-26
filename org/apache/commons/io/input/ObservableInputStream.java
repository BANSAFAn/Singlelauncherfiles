/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObservableInputStream
/*     */   extends ProxyInputStream
/*     */ {
/*     */   private final List<Observer> observers;
/*     */   
/*     */   public static abstract class Observer
/*     */   {
/*     */     public void closed() throws IOException {}
/*     */     
/*     */     public void data(byte[] buffer, int offset, int length) throws IOException {}
/*     */     
/*     */     public void data(int value) throws IOException {}
/*     */     
/*     */     public void error(IOException exception) throws IOException {
/*  94 */       throw exception;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void finished() throws IOException {}
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
/*     */   public ObservableInputStream(InputStream inputStream) {
/* 117 */     this(inputStream, new ArrayList<>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ObservableInputStream(InputStream inputStream, List<Observer> observers) {
/* 127 */     super(inputStream);
/* 128 */     this.observers = observers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObservableInputStream(InputStream inputStream, Observer... observers) {
/* 139 */     this(inputStream, Arrays.asList(observers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Observer observer) {
/* 148 */     this.observers.add(observer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 153 */     IOException ioe = null;
/*     */     try {
/* 155 */       super.close();
/* 156 */     } catch (IOException e) {
/* 157 */       ioe = e;
/*     */     } 
/* 159 */     if (ioe == null) {
/* 160 */       noteClosed();
/*     */     } else {
/* 162 */       noteError(ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consume() throws IOException {
/* 172 */     byte[] buffer = IOUtils.byteArray();
/* 173 */     while (read(buffer) != -1);
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
/*     */   public List<Observer> getObservers() {
/* 185 */     return this.observers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void noteClosed() throws IOException {
/* 194 */     for (Observer observer : getObservers()) {
/* 195 */       observer.closed();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void noteDataByte(int value) throws IOException {
/* 206 */     for (Observer observer : getObservers()) {
/* 207 */       observer.data(value);
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
/*     */   protected void noteDataBytes(byte[] buffer, int offset, int length) throws IOException {
/* 220 */     for (Observer observer : getObservers()) {
/* 221 */       observer.data(buffer, offset, length);
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
/*     */   protected void noteError(IOException exception) throws IOException {
/* 233 */     for (Observer observer : getObservers()) {
/* 234 */       observer.error(exception);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void noteFinished() throws IOException {
/* 244 */     for (Observer observer : getObservers()) {
/* 245 */       observer.finished();
/*     */     }
/*     */   }
/*     */   
/*     */   private void notify(byte[] buffer, int offset, int result, IOException ioe) throws IOException {
/* 250 */     if (ioe != null) {
/* 251 */       noteError(ioe);
/* 252 */       throw ioe;
/*     */     } 
/* 254 */     if (result == -1) {
/* 255 */       noteFinished();
/* 256 */     } else if (result > 0) {
/* 257 */       noteDataBytes(buffer, offset, result);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 263 */     int result = 0;
/* 264 */     IOException ioe = null;
/*     */     try {
/* 266 */       result = super.read();
/* 267 */     } catch (IOException ex) {
/* 268 */       ioe = ex;
/*     */     } 
/* 270 */     if (ioe != null) {
/* 271 */       noteError(ioe);
/* 272 */       throw ioe;
/*     */     } 
/* 274 */     if (result == -1) {
/* 275 */       noteFinished();
/*     */     } else {
/* 277 */       noteDataByte(result);
/*     */     } 
/* 279 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] buffer) throws IOException {
/* 284 */     int result = 0;
/* 285 */     IOException ioe = null;
/*     */     try {
/* 287 */       result = super.read(buffer);
/* 288 */     } catch (IOException ex) {
/* 289 */       ioe = ex;
/*     */     } 
/* 291 */     notify(buffer, 0, result, ioe);
/* 292 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] buffer, int offset, int length) throws IOException {
/* 297 */     int result = 0;
/* 298 */     IOException ioe = null;
/*     */     try {
/* 300 */       result = super.read(buffer, offset, length);
/* 301 */     } catch (IOException ex) {
/* 302 */       ioe = ex;
/*     */     } 
/* 304 */     notify(buffer, offset, result, ioe);
/* 305 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(Observer observer) {
/* 314 */     this.observers.remove(observer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAllObservers() {
/* 321 */     this.observers.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\ObservableInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */