/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.ChallengeState;
/*     */ import org.apache.http.auth.ContextAwareAuthScheme;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.protocol.HTTP;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @NotThreadSafe
/*     */ public abstract class AuthSchemeBase
/*     */   implements ContextAwareAuthScheme
/*     */ {
/*     */   protected ChallengeState challengeState;
/*     */   
/*     */   @Deprecated
/*     */   public AuthSchemeBase(ChallengeState challengeState) {
/*  72 */     this.challengeState = challengeState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthSchemeBase() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processChallenge(Header header) throws MalformedChallengeException {
/*     */     CharArrayBuffer buffer;
/*     */     int pos;
/*  91 */     Args.notNull(header, "Header");
/*  92 */     String authheader = header.getName();
/*  93 */     if (authheader.equalsIgnoreCase("WWW-Authenticate")) {
/*  94 */       this.challengeState = ChallengeState.TARGET;
/*  95 */     } else if (authheader.equalsIgnoreCase("Proxy-Authenticate")) {
/*  96 */       this.challengeState = ChallengeState.PROXY;
/*     */     } else {
/*  98 */       throw new MalformedChallengeException("Unexpected header name: " + authheader);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 103 */     if (header instanceof FormattedHeader) {
/* 104 */       buffer = ((FormattedHeader)header).getBuffer();
/* 105 */       pos = ((FormattedHeader)header).getValuePos();
/*     */     } else {
/* 107 */       String str = header.getValue();
/* 108 */       if (str == null) {
/* 109 */         throw new MalformedChallengeException("Header value is null");
/*     */       }
/* 111 */       buffer = new CharArrayBuffer(str.length());
/* 112 */       buffer.append(str);
/* 113 */       pos = 0;
/*     */     } 
/* 115 */     while (pos < buffer.length() && HTTP.isWhitespace(buffer.charAt(pos))) {
/* 116 */       pos++;
/*     */     }
/* 118 */     int beginIndex = pos;
/* 119 */     while (pos < buffer.length() && !HTTP.isWhitespace(buffer.charAt(pos))) {
/* 120 */       pos++;
/*     */     }
/* 122 */     int endIndex = pos;
/* 123 */     String s = buffer.substring(beginIndex, endIndex);
/* 124 */     if (!s.equalsIgnoreCase(getSchemeName())) {
/* 125 */       throw new MalformedChallengeException("Invalid scheme identifier: " + s);
/*     */     }
/*     */     
/* 128 */     parseChallenge(buffer, pos, buffer.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 138 */     return authenticate(credentials, request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void parseChallenge(CharArrayBuffer paramCharArrayBuffer, int paramInt1, int paramInt2) throws MalformedChallengeException;
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProxy() {
/* 149 */     return (this.challengeState != null && this.challengeState == ChallengeState.PROXY);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChallengeState getChallengeState() {
/* 158 */     return this.challengeState;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 163 */     String name = getSchemeName();
/* 164 */     if (name != null) {
/* 165 */       return name.toUpperCase(Locale.ROOT);
/*     */     }
/* 167 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\auth\AuthSchemeBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */