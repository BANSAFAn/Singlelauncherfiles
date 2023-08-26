/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.TokenIterator;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicTokenIterator
/*     */   implements TokenIterator
/*     */ {
/*     */   public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
/*     */   protected final HeaderIterator headerIt;
/*     */   protected String currentHeader;
/*     */   protected String currentToken;
/*     */   protected int searchPos;
/*     */   
/*     */   public BasicTokenIterator(HeaderIterator headerIterator) {
/*  85 */     this.headerIt = (HeaderIterator)Args.notNull(headerIterator, "Header iterator");
/*  86 */     this.searchPos = findNext(-1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  93 */     return (this.currentToken != null);
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
/*     */   public String nextToken() throws NoSuchElementException, ParseException {
/* 109 */     if (this.currentToken == null) {
/* 110 */       throw new NoSuchElementException("Iteration already finished.");
/*     */     }
/*     */     
/* 113 */     String result = this.currentToken;
/*     */     
/* 115 */     this.searchPos = findNext(this.searchPos);
/*     */     
/* 117 */     return result;
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
/*     */   public final Object next() throws NoSuchElementException, ParseException {
/* 133 */     return nextToken();
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
/*     */   public final void remove() throws UnsupportedOperationException {
/* 146 */     throw new UnsupportedOperationException("Removing tokens is not supported.");
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
/*     */ 
/*     */   
/*     */   protected int findNext(int pos) throws ParseException {
/* 168 */     int from = pos;
/* 169 */     if (from < 0) {
/*     */       
/* 171 */       if (!this.headerIt.hasNext()) {
/* 172 */         return -1;
/*     */       }
/* 174 */       this.currentHeader = this.headerIt.nextHeader().getValue();
/* 175 */       from = 0;
/*     */     } else {
/*     */       
/* 178 */       from = findTokenSeparator(from);
/*     */     } 
/*     */     
/* 181 */     int start = findTokenStart(from);
/* 182 */     if (start < 0) {
/* 183 */       this.currentToken = null;
/* 184 */       return -1;
/*     */     } 
/*     */     
/* 187 */     int end = findTokenEnd(start);
/* 188 */     this.currentToken = createToken(this.currentHeader, start, end);
/* 189 */     return end;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String createToken(String value, int start, int end) {
/* 215 */     return value.substring(start, end);
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
/*     */   protected int findTokenStart(int pos) {
/* 230 */     int from = Args.notNegative(pos, "Search position");
/* 231 */     boolean found = false;
/* 232 */     while (!found && this.currentHeader != null) {
/*     */       
/* 234 */       int to = this.currentHeader.length();
/* 235 */       while (!found && from < to) {
/*     */         
/* 237 */         char ch = this.currentHeader.charAt(from);
/* 238 */         if (isTokenSeparator(ch) || isWhitespace(ch)) {
/*     */           
/* 240 */           from++; continue;
/* 241 */         }  if (isTokenChar(this.currentHeader.charAt(from))) {
/*     */           
/* 243 */           found = true; continue;
/*     */         } 
/* 245 */         throw new ParseException("Invalid character before token (pos " + from + "): " + this.currentHeader);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 250 */       if (!found) {
/* 251 */         if (this.headerIt.hasNext()) {
/* 252 */           this.currentHeader = this.headerIt.nextHeader().getValue();
/* 253 */           from = 0; continue;
/*     */         } 
/* 255 */         this.currentHeader = null;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 260 */     return found ? from : -1;
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
/*     */ 
/*     */   
/*     */   protected int findTokenSeparator(int pos) {
/* 282 */     int from = Args.notNegative(pos, "Search position");
/* 283 */     boolean found = false;
/* 284 */     int to = this.currentHeader.length();
/* 285 */     while (!found && from < to) {
/* 286 */       char ch = this.currentHeader.charAt(from);
/* 287 */       if (isTokenSeparator(ch)) {
/* 288 */         found = true; continue;
/* 289 */       }  if (isWhitespace(ch)) {
/* 290 */         from++; continue;
/* 291 */       }  if (isTokenChar(ch)) {
/* 292 */         throw new ParseException("Tokens without separator (pos " + from + "): " + this.currentHeader);
/*     */       }
/*     */ 
/*     */       
/* 296 */       throw new ParseException("Invalid character after token (pos " + from + "): " + this.currentHeader);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 302 */     return from;
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
/*     */   protected int findTokenEnd(int from) {
/* 318 */     Args.notNegative(from, "Search position");
/* 319 */     int to = this.currentHeader.length();
/* 320 */     int end = from + 1;
/* 321 */     while (end < to && isTokenChar(this.currentHeader.charAt(end))) {
/* 322 */       end++;
/*     */     }
/*     */     
/* 325 */     return end;
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
/*     */   protected boolean isTokenSeparator(char ch) {
/* 341 */     return (ch == ',');
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
/*     */   protected boolean isWhitespace(char ch) {
/* 360 */     return (ch == '\t' || Character.isSpaceChar(ch));
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
/*     */   protected boolean isTokenChar(char ch) {
/* 379 */     if (Character.isLetterOrDigit(ch)) {
/* 380 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 384 */     if (Character.isISOControl(ch)) {
/* 385 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 389 */     if (isHttpSeparator(ch)) {
/* 390 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 399 */     return true;
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
/*     */   protected boolean isHttpSeparator(char ch) {
/* 414 */     return (" ,;=()<>@:\\\"/[]?{}\t".indexOf(ch) >= 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicTokenIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */