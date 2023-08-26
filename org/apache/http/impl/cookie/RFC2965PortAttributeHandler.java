/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.cookie.ClientCookie;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookieRestrictionViolationException;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.cookie.SetCookie;
/*     */ import org.apache.http.cookie.SetCookie2;
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
/*     */ @Immutable
/*     */ public class RFC2965PortAttributeHandler
/*     */   implements CommonCookieAttributeHandler
/*     */ {
/*     */   private static int[] parsePortAttribute(String portValue) throws MalformedCookieException {
/*  66 */     StringTokenizer st = new StringTokenizer(portValue, ",");
/*  67 */     int[] ports = new int[st.countTokens()];
/*     */     try {
/*  69 */       int i = 0;
/*  70 */       while (st.hasMoreTokens()) {
/*  71 */         ports[i] = Integer.parseInt(st.nextToken().trim());
/*  72 */         if (ports[i] < 0) {
/*  73 */           throw new MalformedCookieException("Invalid Port attribute.");
/*     */         }
/*  75 */         i++;
/*     */       } 
/*  77 */     } catch (NumberFormatException e) {
/*  78 */       throw new MalformedCookieException("Invalid Port attribute: " + e.getMessage());
/*     */     } 
/*     */     
/*  81 */     return ports;
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
/*     */   private static boolean portMatch(int port, int[] ports) {
/*  94 */     boolean portInList = false;
/*  95 */     for (int port2 : ports) {
/*  96 */       if (port == port2) {
/*  97 */         portInList = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 101 */     return portInList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(SetCookie cookie, String portValue) throws MalformedCookieException {
/* 110 */     Args.notNull(cookie, "Cookie");
/* 111 */     if (cookie instanceof SetCookie2) {
/* 112 */       SetCookie2 cookie2 = (SetCookie2)cookie;
/* 113 */       if (portValue != null && !portValue.trim().isEmpty()) {
/* 114 */         int[] ports = parsePortAttribute(portValue);
/* 115 */         cookie2.setPorts(ports);
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
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 127 */     Args.notNull(cookie, "Cookie");
/* 128 */     Args.notNull(origin, "Cookie origin");
/* 129 */     int port = origin.getPort();
/* 130 */     if (cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("port"))
/*     */     {
/* 132 */       if (!portMatch(port, cookie.getPorts())) {
/* 133 */         throw new CookieRestrictionViolationException("Port attribute violates RFC 2965: Request port not found in cookie's port list.");
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
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 147 */     Args.notNull(cookie, "Cookie");
/* 148 */     Args.notNull(origin, "Cookie origin");
/* 149 */     int port = origin.getPort();
/* 150 */     if (cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("port")) {
/*     */       
/* 152 */       if (cookie.getPorts() == null)
/*     */       {
/* 154 */         return false;
/*     */       }
/* 156 */       if (!portMatch(port, cookie.getPorts())) {
/* 157 */         return false;
/*     */       }
/*     */     } 
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeName() {
/* 165 */     return "port";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\RFC2965PortAttributeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */