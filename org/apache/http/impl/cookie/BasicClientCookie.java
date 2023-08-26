/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.cookie.ClientCookie;
/*     */ import org.apache.http.cookie.SetCookie;
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
/*     */ @NotThreadSafe
/*     */ public class BasicClientCookie
/*     */   implements SetCookie, ClientCookie, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3869795591041535538L;
/*     */   private final String name;
/*     */   private Map<String, String> attribs;
/*     */   private String value;
/*     */   private String cookieComment;
/*     */   private String cookieDomain;
/*     */   private Date cookieExpiryDate;
/*     */   private String cookiePath;
/*     */   private boolean isSecure;
/*     */   private int cookieVersion;
/*     */   private Date creationDate;
/*     */   
/*     */   public BasicClientCookie(String name, String value) {
/*  59 */     Args.notNull(name, "Name");
/*  60 */     this.name = name;
/*  61 */     this.attribs = new HashMap<String, String>();
/*  62 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  72 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  82 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/*  92 */     this.value = value;
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
/*     */   public String getComment() {
/* 105 */     return this.cookieComment;
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
/*     */   public void setComment(String comment) {
/* 118 */     this.cookieComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentURL() {
/* 127 */     return null;
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
/*     */   public Date getExpiryDate() {
/* 144 */     return this.cookieExpiryDate;
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
/*     */   public void setExpiryDate(Date expiryDate) {
/* 160 */     this.cookieExpiryDate = expiryDate;
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
/*     */   public boolean isPersistent() {
/* 173 */     return (null != this.cookieExpiryDate);
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
/*     */   public String getDomain() {
/* 186 */     return this.cookieDomain;
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
/*     */   public void setDomain(String domain) {
/* 198 */     if (domain != null) {
/* 199 */       this.cookieDomain = domain.toLowerCase(Locale.ROOT);
/*     */     } else {
/* 201 */       this.cookieDomain = null;
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
/*     */   public String getPath() {
/* 215 */     return this.cookiePath;
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
/*     */   public void setPath(String path) {
/* 228 */     this.cookiePath = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 237 */     return this.isSecure;
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
/*     */   public void setSecure(boolean secure) {
/* 254 */     this.isSecure = secure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getPorts() {
/* 263 */     return null;
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
/*     */   public int getVersion() {
/* 278 */     return this.cookieVersion;
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
/*     */   public void setVersion(int version) {
/* 291 */     this.cookieVersion = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpired(Date date) {
/* 302 */     Args.notNull(date, "Date");
/* 303 */     return (this.cookieExpiryDate != null && this.cookieExpiryDate.getTime() <= date.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getCreationDate() {
/* 311 */     return this.creationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreationDate(Date creationDate) {
/* 318 */     this.creationDate = creationDate;
/*     */   }
/*     */   
/*     */   public void setAttribute(String name, String value) {
/* 322 */     this.attribs.put(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttribute(String name) {
/* 327 */     return this.attribs.get(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAttribute(String name) {
/* 332 */     return this.attribs.containsKey(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeAttribute(String name) {
/* 339 */     return (this.attribs.remove(name) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 344 */     BasicClientCookie clone = (BasicClientCookie)super.clone();
/* 345 */     clone.attribs = new HashMap<String, String>(this.attribs);
/* 346 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 351 */     StringBuilder buffer = new StringBuilder();
/* 352 */     buffer.append("[version: ");
/* 353 */     buffer.append(Integer.toString(this.cookieVersion));
/* 354 */     buffer.append("]");
/* 355 */     buffer.append("[name: ");
/* 356 */     buffer.append(this.name);
/* 357 */     buffer.append("]");
/* 358 */     buffer.append("[value: ");
/* 359 */     buffer.append(this.value);
/* 360 */     buffer.append("]");
/* 361 */     buffer.append("[domain: ");
/* 362 */     buffer.append(this.cookieDomain);
/* 363 */     buffer.append("]");
/* 364 */     buffer.append("[path: ");
/* 365 */     buffer.append(this.cookiePath);
/* 366 */     buffer.append("]");
/* 367 */     buffer.append("[expiry: ");
/* 368 */     buffer.append(this.cookieExpiryDate);
/* 369 */     buffer.append("]");
/* 370 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\BasicClientCookie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */