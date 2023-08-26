/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.TreeSet;
/*     */ import org.apache.http.annotation.GuardedBy;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieIdentityComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class BasicCookieStore
/*     */   implements CookieStore, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7581093305228232025L;
/*     */   @GuardedBy("this")
/*  58 */   private final TreeSet<Cookie> cookies = new TreeSet<Cookie>((Comparator<? super Cookie>)new CookieIdentityComparator());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addCookie(Cookie cookie) {
/*  73 */     if (cookie != null) {
/*     */       
/*  75 */       this.cookies.remove(cookie);
/*  76 */       if (!cookie.isExpired(new Date())) {
/*  77 */         this.cookies.add(cookie);
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
/*     */   
/*     */   public synchronized void addCookies(Cookie[] cookies) {
/*  93 */     if (cookies != null) {
/*  94 */       for (Cookie cooky : cookies) {
/*  95 */         addCookie(cooky);
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
/*     */   public synchronized List<Cookie> getCookies() {
/* 109 */     return new ArrayList<Cookie>(this.cookies);
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
/*     */   public synchronized boolean clearExpired(Date date) {
/* 122 */     if (date == null) {
/* 123 */       return false;
/*     */     }
/* 125 */     boolean removed = false;
/* 126 */     for (Iterator<Cookie> it = this.cookies.iterator(); it.hasNext();) {
/* 127 */       if (((Cookie)it.next()).isExpired(date)) {
/* 128 */         it.remove();
/* 129 */         removed = true;
/*     */       } 
/*     */     } 
/* 132 */     return removed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/* 140 */     this.cookies.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String toString() {
/* 145 */     return this.cookies.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\BasicCookieStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */