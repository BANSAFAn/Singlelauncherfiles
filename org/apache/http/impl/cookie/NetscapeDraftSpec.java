/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.annotation.Obsolete;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.message.ParserCursor;
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
/*     */ @Obsolete
/*     */ @ThreadSafe
/*     */ public class NetscapeDraftSpec
/*     */   extends CookieSpecBase
/*     */ {
/*     */   protected static final String EXPIRES_PATTERN = "EEE, dd-MMM-yy HH:mm:ss z";
/*     */   
/*     */   public NetscapeDraftSpec(String[] datepatterns) {
/*  67 */     super(new CommonCookieAttributeHandler[] { null, null, null, null, new BasicExpiresHandler((datepatterns != null) ? (String[])datepatterns.clone() : new String[1]) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   NetscapeDraftSpec(CommonCookieAttributeHandler... handlers) {
/*  76 */     super(handlers);
/*     */   }
/*     */   
/*     */   public NetscapeDraftSpec() {
/*  80 */     this((String[])null);
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
/*     */ 
/*     */   
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/*     */     CharArrayBuffer buffer;
/*     */     ParserCursor cursor;
/* 110 */     Args.notNull(header, "Header");
/* 111 */     Args.notNull(origin, "Cookie origin");
/* 112 */     if (!header.getName().equalsIgnoreCase("Set-Cookie")) {
/* 113 */       throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
/*     */     }
/*     */     
/* 116 */     NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
/*     */ 
/*     */     
/* 119 */     if (header instanceof FormattedHeader) {
/* 120 */       buffer = ((FormattedHeader)header).getBuffer();
/* 121 */       cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
/*     */     }
/*     */     else {
/*     */       
/* 125 */       String s = header.getValue();
/* 126 */       if (s == null) {
/* 127 */         throw new MalformedCookieException("Header value is null");
/*     */       }
/* 129 */       buffer = new CharArrayBuffer(s.length());
/* 130 */       buffer.append(s);
/* 131 */       cursor = new ParserCursor(0, buffer.length());
/*     */     } 
/* 133 */     return parse(new HeaderElement[] { parser.parseHeader(buffer, cursor) }origin);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/* 138 */     Args.notEmpty(cookies, "List of cookies");
/* 139 */     CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
/* 140 */     buffer.append("Cookie");
/* 141 */     buffer.append(": ");
/* 142 */     for (int i = 0; i < cookies.size(); i++) {
/* 143 */       Cookie cookie = cookies.get(i);
/* 144 */       if (i > 0) {
/* 145 */         buffer.append("; ");
/*     */       }
/* 147 */       buffer.append(cookie.getName());
/* 148 */       String s = cookie.getValue();
/* 149 */       if (s != null) {
/* 150 */         buffer.append("=");
/* 151 */         buffer.append(s);
/*     */       } 
/*     */     } 
/* 154 */     List<Header> headers = new ArrayList<Header>(1);
/* 155 */     headers.add(new BufferedHeader(buffer));
/* 156 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 161 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getVersionHeader() {
/* 166 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 171 */     return "netscape";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\NetscapeDraftSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */