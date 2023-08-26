/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.ChallengeState;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.protocol.BasicHttpContext;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ import org.apache.http.util.EncodingUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicScheme
/*     */   extends RFC2617Scheme
/*     */ {
/*     */   private static final long serialVersionUID = -1931571557597830536L;
/*     */   private boolean complete;
/*     */   
/*     */   public BasicScheme(Charset credentialsCharset) {
/*  65 */     super(credentialsCharset);
/*  66 */     this.complete = false;
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
/*     */   @Deprecated
/*     */   public BasicScheme(ChallengeState challengeState) {
/*  79 */     super(challengeState);
/*     */   }
/*     */   
/*     */   public BasicScheme() {
/*  83 */     this(Consts.ASCII);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeName() {
/*  93 */     return "basic";
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
/*     */   public void processChallenge(Header header) throws MalformedChallengeException {
/* 107 */     super.processChallenge(header);
/* 108 */     this.complete = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 119 */     return this.complete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
/* 140 */     return authenticate(credentials, request, (HttpContext)new BasicHttpContext());
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
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 161 */     Args.notNull(credentials, "Credentials");
/* 162 */     Args.notNull(request, "HTTP request");
/* 163 */     StringBuilder tmp = new StringBuilder();
/* 164 */     tmp.append(credentials.getUserPrincipal().getName());
/* 165 */     tmp.append(":");
/* 166 */     tmp.append((credentials.getPassword() == null) ? "null" : credentials.getPassword());
/*     */     
/* 168 */     Base64 base64codec = new Base64(0);
/* 169 */     byte[] base64password = base64codec.encode(EncodingUtils.getBytes(tmp.toString(), getCredentialsCharset(request)));
/*     */ 
/*     */     
/* 172 */     CharArrayBuffer buffer = new CharArrayBuffer(32);
/* 173 */     if (isProxy()) {
/* 174 */       buffer.append("Proxy-Authorization");
/*     */     } else {
/* 176 */       buffer.append("Authorization");
/*     */     } 
/* 178 */     buffer.append(": Basic ");
/* 179 */     buffer.append(base64password, 0, base64password.length);
/*     */     
/* 181 */     return (Header)new BufferedHeader(buffer);
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
/*     */   @Deprecated
/*     */   public static Header authenticate(Credentials credentials, String charset, boolean proxy) {
/* 200 */     Args.notNull(credentials, "Credentials");
/* 201 */     Args.notNull(charset, "charset");
/*     */     
/* 203 */     StringBuilder tmp = new StringBuilder();
/* 204 */     tmp.append(credentials.getUserPrincipal().getName());
/* 205 */     tmp.append(":");
/* 206 */     tmp.append((credentials.getPassword() == null) ? "null" : credentials.getPassword());
/*     */     
/* 208 */     byte[] base64password = Base64.encodeBase64(EncodingUtils.getBytes(tmp.toString(), charset), false);
/*     */ 
/*     */     
/* 211 */     CharArrayBuffer buffer = new CharArrayBuffer(32);
/* 212 */     if (proxy) {
/* 213 */       buffer.append("Proxy-Authorization");
/*     */     } else {
/* 215 */       buffer.append("Authorization");
/*     */     } 
/* 217 */     buffer.append(": Basic ");
/* 218 */     buffer.append(base64password, 0, base64password.length);
/*     */     
/* 220 */     return (Header)new BufferedHeader(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 225 */     StringBuilder builder = new StringBuilder();
/* 226 */     builder.append("BASIC [complete=").append(this.complete).append("]");
/*     */     
/* 228 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\auth\BasicScheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */