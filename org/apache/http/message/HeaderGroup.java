/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ @NotThreadSafe
/*     */ public class HeaderGroup
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2608834160639271617L;
/*  54 */   private final Header[] EMPTY = new Header[0];
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<Header> headers;
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderGroup() {
/*  63 */     this.headers = new ArrayList<Header>(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  70 */     this.headers.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHeader(Header header) {
/*  80 */     if (header == null) {
/*     */       return;
/*     */     }
/*  83 */     this.headers.add(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeHeader(Header header) {
/*  92 */     if (header == null) {
/*     */       return;
/*     */     }
/*  95 */     this.headers.remove(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateHeader(Header header) {
/* 106 */     if (header == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 112 */     for (int i = 0; i < this.headers.size(); i++) {
/* 113 */       Header current = this.headers.get(i);
/* 114 */       if (current.getName().equalsIgnoreCase(header.getName())) {
/* 115 */         this.headers.set(i, header);
/*     */         return;
/*     */       } 
/*     */     } 
/* 119 */     this.headers.add(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeaders(Header[] headers) {
/* 130 */     clear();
/* 131 */     if (headers == null) {
/*     */       return;
/*     */     }
/* 134 */     Collections.addAll(this.headers, headers);
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
/*     */   public Header getCondensedHeader(String name) {
/* 149 */     Header[] hdrs = getHeaders(name);
/*     */     
/* 151 */     if (hdrs.length == 0)
/* 152 */       return null; 
/* 153 */     if (hdrs.length == 1) {
/* 154 */       return hdrs[0];
/*     */     }
/* 156 */     CharArrayBuffer valueBuffer = new CharArrayBuffer(128);
/* 157 */     valueBuffer.append(hdrs[0].getValue());
/* 158 */     for (int i = 1; i < hdrs.length; i++) {
/* 159 */       valueBuffer.append(", ");
/* 160 */       valueBuffer.append(hdrs[i].getValue());
/*     */     } 
/*     */     
/* 163 */     return new BasicHeader(name.toLowerCase(Locale.ROOT), valueBuffer.toString());
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
/*     */   public Header[] getHeaders(String name) {
/* 178 */     List<Header> headersFound = null;
/*     */ 
/*     */ 
/*     */     
/* 182 */     for (int i = 0; i < this.headers.size(); i++) {
/* 183 */       Header header = this.headers.get(i);
/* 184 */       if (header.getName().equalsIgnoreCase(name)) {
/* 185 */         if (headersFound == null) {
/* 186 */           headersFound = new ArrayList<Header>();
/*     */         }
/* 188 */         headersFound.add(header);
/*     */       } 
/*     */     } 
/* 191 */     return (headersFound != null) ? headersFound.<Header>toArray(new Header[headersFound.size()]) : this.EMPTY;
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
/*     */   public Header getFirstHeader(String name) {
/* 206 */     for (int i = 0; i < this.headers.size(); i++) {
/* 207 */       Header header = this.headers.get(i);
/* 208 */       if (header.getName().equalsIgnoreCase(name)) {
/* 209 */         return header;
/*     */       }
/*     */     } 
/* 212 */     return null;
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
/*     */   public Header getLastHeader(String name) {
/* 225 */     for (int i = this.headers.size() - 1; i >= 0; i--) {
/* 226 */       Header header = this.headers.get(i);
/* 227 */       if (header.getName().equalsIgnoreCase(name)) {
/* 228 */         return header;
/*     */       }
/*     */     } 
/*     */     
/* 232 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header[] getAllHeaders() {
/* 241 */     return this.headers.<Header>toArray(new Header[this.headers.size()]);
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
/*     */   public boolean containsHeader(String name) {
/* 257 */     for (int i = 0; i < this.headers.size(); i++) {
/* 258 */       Header header = this.headers.get(i);
/* 259 */       if (header.getName().equalsIgnoreCase(name)) {
/* 260 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 264 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderIterator iterator() {
/* 275 */     return new BasicListHeaderIterator(this.headers, null);
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
/*     */   public HeaderIterator iterator(String name) {
/* 289 */     return new BasicListHeaderIterator(this.headers, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderGroup copy() {
/* 300 */     HeaderGroup clone = new HeaderGroup();
/* 301 */     clone.headers.addAll(this.headers);
/* 302 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 307 */     return super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 312 */     return this.headers.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\HeaderGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */