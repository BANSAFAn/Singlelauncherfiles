/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HeaderElementIterator;
/*     */ import org.apache.http.HeaderIterator;
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
/*     */ @NotThreadSafe
/*     */ public class BasicHeaderElementIterator
/*     */   implements HeaderElementIterator
/*     */ {
/*     */   private final HeaderIterator headerIt;
/*     */   private final HeaderValueParser parser;
/*  52 */   private HeaderElement currentElement = null;
/*  53 */   private CharArrayBuffer buffer = null;
/*  54 */   private ParserCursor cursor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHeaderElementIterator(HeaderIterator headerIterator, HeaderValueParser parser) {
/*  62 */     this.headerIt = (HeaderIterator)Args.notNull(headerIterator, "Header iterator");
/*  63 */     this.parser = (HeaderValueParser)Args.notNull(parser, "Parser");
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicHeaderElementIterator(HeaderIterator headerIterator) {
/*  68 */     this(headerIterator, BasicHeaderValueParser.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   private void bufferHeaderValue() {
/*  73 */     this.cursor = null;
/*  74 */     this.buffer = null;
/*  75 */     while (this.headerIt.hasNext()) {
/*  76 */       Header h = this.headerIt.nextHeader();
/*  77 */       if (h instanceof FormattedHeader) {
/*  78 */         this.buffer = ((FormattedHeader)h).getBuffer();
/*  79 */         this.cursor = new ParserCursor(0, this.buffer.length());
/*  80 */         this.cursor.updatePos(((FormattedHeader)h).getValuePos());
/*     */         break;
/*     */       } 
/*  83 */       String value = h.getValue();
/*  84 */       if (value != null) {
/*  85 */         this.buffer = new CharArrayBuffer(value.length());
/*  86 */         this.buffer.append(value);
/*  87 */         this.cursor = new ParserCursor(0, this.buffer.length());
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseNextElement() {
/*  96 */     while (this.headerIt.hasNext() || this.cursor != null) {
/*  97 */       if (this.cursor == null || this.cursor.atEnd())
/*     */       {
/*  99 */         bufferHeaderValue();
/*     */       }
/*     */       
/* 102 */       if (this.cursor != null) {
/*     */         
/* 104 */         while (!this.cursor.atEnd()) {
/* 105 */           HeaderElement e = this.parser.parseHeaderElement(this.buffer, this.cursor);
/* 106 */           if (e.getName().length() != 0 || e.getValue() != null) {
/*     */             
/* 108 */             this.currentElement = e;
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 113 */         if (this.cursor.atEnd()) {
/*     */           
/* 115 */           this.cursor = null;
/* 116 */           this.buffer = null;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 124 */     if (this.currentElement == null) {
/* 125 */       parseNextElement();
/*     */     }
/* 127 */     return (this.currentElement != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public HeaderElement nextElement() throws NoSuchElementException {
/* 132 */     if (this.currentElement == null) {
/* 133 */       parseNextElement();
/*     */     }
/*     */     
/* 136 */     if (this.currentElement == null) {
/* 137 */       throw new NoSuchElementException("No more header elements available");
/*     */     }
/*     */     
/* 140 */     HeaderElement element = this.currentElement;
/* 141 */     this.currentElement = null;
/* 142 */     return element;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object next() throws NoSuchElementException {
/* 147 */     return nextElement();
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() throws UnsupportedOperationException {
/* 152 */     throw new UnsupportedOperationException("Remove not supported");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicHeaderElementIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */