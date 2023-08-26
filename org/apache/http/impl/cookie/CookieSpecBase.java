/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.MalformedCookieException;
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
/*     */ @ThreadSafe
/*     */ public abstract class CookieSpecBase
/*     */   extends AbstractCookieSpec
/*     */ {
/*     */   public CookieSpecBase() {}
/*     */   
/*     */   protected CookieSpecBase(HashMap<String, CookieAttributeHandler> map) {
/*  61 */     super(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CookieSpecBase(CommonCookieAttributeHandler... handlers) {
/*  68 */     super(handlers);
/*     */   }
/*     */   
/*     */   protected static String getDefaultPath(CookieOrigin origin) {
/*  72 */     String defaultPath = origin.getPath();
/*  73 */     int lastSlashIndex = defaultPath.lastIndexOf('/');
/*  74 */     if (lastSlashIndex >= 0) {
/*  75 */       if (lastSlashIndex == 0)
/*     */       {
/*  77 */         lastSlashIndex = 1;
/*     */       }
/*  79 */       defaultPath = defaultPath.substring(0, lastSlashIndex);
/*     */     } 
/*  81 */     return defaultPath;
/*     */   }
/*     */   
/*     */   protected static String getDefaultDomain(CookieOrigin origin) {
/*  85 */     return origin.getHost();
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Cookie> parse(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
/*  90 */     List<Cookie> cookies = new ArrayList<Cookie>(elems.length);
/*  91 */     for (HeaderElement headerelement : elems) {
/*  92 */       String name = headerelement.getName();
/*  93 */       String value = headerelement.getValue();
/*  94 */       if (name != null && !name.isEmpty()) {
/*     */ 
/*     */ 
/*     */         
/*  98 */         BasicClientCookie cookie = new BasicClientCookie(name, value);
/*  99 */         cookie.setPath(getDefaultPath(origin));
/* 100 */         cookie.setDomain(getDefaultDomain(origin));
/*     */ 
/*     */         
/* 103 */         NameValuePair[] attribs = headerelement.getParameters();
/* 104 */         for (int j = attribs.length - 1; j >= 0; j--) {
/* 105 */           NameValuePair attrib = attribs[j];
/* 106 */           String s = attrib.getName().toLowerCase(Locale.ROOT);
/*     */           
/* 108 */           cookie.setAttribute(s, attrib.getValue());
/*     */           
/* 110 */           CookieAttributeHandler handler = findAttribHandler(s);
/* 111 */           if (handler != null) {
/* 112 */             handler.parse(cookie, attrib.getValue());
/*     */           }
/*     */         } 
/* 115 */         cookies.add(cookie);
/*     */       } 
/* 117 */     }  return cookies;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 123 */     Args.notNull(cookie, "Cookie");
/* 124 */     Args.notNull(origin, "Cookie origin");
/* 125 */     for (CookieAttributeHandler handler : getAttribHandlers()) {
/* 126 */       handler.validate(cookie, origin);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 132 */     Args.notNull(cookie, "Cookie");
/* 133 */     Args.notNull(origin, "Cookie origin");
/* 134 */     for (CookieAttributeHandler handler : getAttribHandlers()) {
/* 135 */       if (!handler.match(cookie, origin)) {
/* 136 */         return false;
/*     */       }
/*     */     } 
/* 139 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\CookieSpecBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */