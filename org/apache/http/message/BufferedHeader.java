/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BufferedHeader
/*     */   implements FormattedHeader, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2768352615787625448L;
/*     */   private final String name;
/*     */   private final CharArrayBuffer buffer;
/*     */   private final int valuePos;
/*     */   
/*     */   public BufferedHeader(CharArrayBuffer buffer) throws ParseException {
/*  79 */     Args.notNull(buffer, "Char array buffer");
/*  80 */     int colon = buffer.indexOf(58);
/*  81 */     if (colon == -1) {
/*  82 */       throw new ParseException("Invalid header: " + buffer.toString());
/*     */     }
/*     */     
/*  85 */     String s = buffer.substringTrimmed(0, colon);
/*  86 */     if (s.length() == 0) {
/*  87 */       throw new ParseException("Invalid header: " + buffer.toString());
/*     */     }
/*     */     
/*  90 */     this.buffer = buffer;
/*  91 */     this.name = s;
/*  92 */     this.valuePos = colon + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  98 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 103 */     return this.buffer.substringTrimmed(this.valuePos, this.buffer.length());
/*     */   }
/*     */ 
/*     */   
/*     */   public HeaderElement[] getElements() throws ParseException {
/* 108 */     ParserCursor cursor = new ParserCursor(0, this.buffer.length());
/* 109 */     cursor.updatePos(this.valuePos);
/* 110 */     return BasicHeaderValueParser.INSTANCE.parseElements(this.buffer, cursor);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getValuePos() {
/* 115 */     return this.valuePos;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArrayBuffer getBuffer() {
/* 120 */     return this.buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 125 */     return this.buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 132 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BufferedHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */