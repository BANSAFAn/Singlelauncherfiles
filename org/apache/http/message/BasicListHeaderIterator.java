/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicListHeaderIterator
/*     */   implements HeaderIterator
/*     */ {
/*     */   protected final List<Header> allHeaders;
/*     */   protected int currentIndex;
/*     */   protected int lastIndex;
/*     */   protected String headerName;
/*     */   
/*     */   public BasicListHeaderIterator(List<Header> headers, String name) {
/*  86 */     this.allHeaders = (List<Header>)Args.notNull(headers, "Header list");
/*  87 */     this.headerName = name;
/*  88 */     this.currentIndex = findNext(-1);
/*  89 */     this.lastIndex = -1;
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
/*     */   protected int findNext(int pos) {
/* 103 */     int from = pos;
/* 104 */     if (from < -1) {
/* 105 */       return -1;
/*     */     }
/*     */     
/* 108 */     int to = this.allHeaders.size() - 1;
/* 109 */     boolean found = false;
/* 110 */     while (!found && from < to) {
/* 111 */       from++;
/* 112 */       found = filterHeader(from);
/*     */     } 
/* 114 */     return found ? from : -1;
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
/*     */   protected boolean filterHeader(int index) {
/* 127 */     if (this.headerName == null) {
/* 128 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 132 */     String name = ((Header)this.allHeaders.get(index)).getName();
/*     */     
/* 134 */     return this.headerName.equalsIgnoreCase(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/* 141 */     return (this.currentIndex >= 0);
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
/*     */   public Header nextHeader() throws NoSuchElementException {
/* 156 */     int current = this.currentIndex;
/* 157 */     if (current < 0) {
/* 158 */       throw new NoSuchElementException("Iteration already finished.");
/*     */     }
/*     */     
/* 161 */     this.lastIndex = current;
/* 162 */     this.currentIndex = findNext(current);
/*     */     
/* 164 */     return this.allHeaders.get(current);
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
/*     */   public final Object next() throws NoSuchElementException {
/* 179 */     return nextHeader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() throws UnsupportedOperationException {
/* 189 */     Asserts.check((this.lastIndex >= 0), "No header to remove");
/* 190 */     this.allHeaders.remove(this.lastIndex);
/* 191 */     this.lastIndex = -1;
/* 192 */     this.currentIndex--;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicListHeaderIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */