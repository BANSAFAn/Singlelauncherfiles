/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Formatter;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.ChallengeState;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.message.BasicHeaderValueFormatter;
/*     */ import org.apache.http.message.BasicNameValuePair;
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
/*     */ 
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
/*     */ public class DigestScheme
/*     */   extends RFC2617Scheme
/*     */ {
/*     */   private static final long serialVersionUID = 3883908186234566916L;
/*  86 */   private static final char[] HEXADECIMAL = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */   
/*     */   private boolean complete;
/*     */   
/*     */   private static final int QOP_UNKNOWN = -1;
/*     */   
/*     */   private static final int QOP_MISSING = 0;
/*     */   
/*     */   private static final int QOP_AUTH_INT = 1;
/*     */   
/*     */   private static final int QOP_AUTH = 2;
/*     */   
/*     */   private String lastNonce;
/*     */   
/*     */   private long nounceCount;
/*     */   
/*     */   private String cnonce;
/*     */   
/*     */   private String a1;
/*     */   
/*     */   private String a2;
/*     */   
/*     */   public DigestScheme(Charset credentialsCharset) {
/* 109 */     super(credentialsCharset);
/* 110 */     this.complete = false;
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
/*     */   public DigestScheme(ChallengeState challengeState) {
/* 123 */     super(challengeState);
/*     */   }
/*     */   
/*     */   public DigestScheme() {
/* 127 */     this(Consts.ASCII);
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
/* 141 */     super.processChallenge(header);
/* 142 */     this.complete = true;
/* 143 */     if (getParameters().isEmpty()) {
/* 144 */       throw new MalformedChallengeException("Authentication challenge is empty");
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
/*     */   public boolean isComplete() {
/* 156 */     String s = getParameter("stale");
/* 157 */     if ("true".equalsIgnoreCase(s)) {
/* 158 */       return false;
/*     */     }
/* 160 */     return this.complete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeName() {
/* 171 */     return "digest";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/* 181 */     return false;
/*     */   }
/*     */   
/*     */   public void overrideParamter(String name, String value) {
/* 185 */     getParameters().put(name, value);
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
/* 196 */     return authenticate(credentials, request, (HttpContext)new BasicHttpContext());
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
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 219 */     Args.notNull(credentials, "Credentials");
/* 220 */     Args.notNull(request, "HTTP request");
/* 221 */     if (getParameter("realm") == null) {
/* 222 */       throw new AuthenticationException("missing realm in challenge");
/*     */     }
/* 224 */     if (getParameter("nonce") == null) {
/* 225 */       throw new AuthenticationException("missing nonce in challenge");
/*     */     }
/*     */     
/* 228 */     getParameters().put("methodname", request.getRequestLine().getMethod());
/* 229 */     getParameters().put("uri", request.getRequestLine().getUri());
/* 230 */     String charset = getParameter("charset");
/* 231 */     if (charset == null) {
/* 232 */       getParameters().put("charset", getCredentialsCharset(request));
/*     */     }
/* 234 */     return createDigestHeader(credentials, request);
/*     */   }
/*     */ 
/*     */   
/*     */   private static MessageDigest createMessageDigest(String digAlg) throws UnsupportedDigestAlgorithmException {
/*     */     try {
/* 240 */       return MessageDigest.getInstance(digAlg);
/* 241 */     } catch (Exception e) {
/* 242 */       throw new UnsupportedDigestAlgorithmException("Unsupported algorithm in HTTP Digest authentication: " + digAlg);
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
/*     */   private Header createDigestHeader(Credentials credentials, HttpRequest request) throws AuthenticationException {
/*     */     MessageDigest digester;
/* 258 */     String digestValue, uri = getParameter("uri");
/* 259 */     String realm = getParameter("realm");
/* 260 */     String nonce = getParameter("nonce");
/* 261 */     String opaque = getParameter("opaque");
/* 262 */     String method = getParameter("methodname");
/* 263 */     String algorithm = getParameter("algorithm");
/*     */     
/* 265 */     if (algorithm == null) {
/* 266 */       algorithm = "MD5";
/*     */     }
/*     */     
/* 269 */     Set<String> qopset = new HashSet<String>(8);
/* 270 */     int qop = -1;
/* 271 */     String qoplist = getParameter("qop");
/* 272 */     if (qoplist != null) {
/* 273 */       StringTokenizer tok = new StringTokenizer(qoplist, ",");
/* 274 */       while (tok.hasMoreTokens()) {
/* 275 */         String variant = tok.nextToken().trim();
/* 276 */         qopset.add(variant.toLowerCase(Locale.ROOT));
/*     */       } 
/* 278 */       if (request instanceof HttpEntityEnclosingRequest && qopset.contains("auth-int")) {
/* 279 */         qop = 1;
/* 280 */       } else if (qopset.contains("auth")) {
/* 281 */         qop = 2;
/*     */       } 
/*     */     } else {
/* 284 */       qop = 0;
/*     */     } 
/*     */     
/* 287 */     if (qop == -1) {
/* 288 */       throw new AuthenticationException("None of the qop methods is supported: " + qoplist);
/*     */     }
/*     */     
/* 291 */     String charset = getParameter("charset");
/* 292 */     if (charset == null) {
/* 293 */       charset = "ISO-8859-1";
/*     */     }
/*     */     
/* 296 */     String digAlg = algorithm;
/* 297 */     if (digAlg.equalsIgnoreCase("MD5-sess")) {
/* 298 */       digAlg = "MD5";
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 303 */       digester = createMessageDigest(digAlg);
/* 304 */     } catch (UnsupportedDigestAlgorithmException ex) {
/* 305 */       throw new AuthenticationException("Unsuppported digest algorithm: " + digAlg);
/*     */     } 
/*     */     
/* 308 */     String uname = credentials.getUserPrincipal().getName();
/* 309 */     String pwd = credentials.getPassword();
/*     */     
/* 311 */     if (nonce.equals(this.lastNonce)) {
/* 312 */       this.nounceCount++;
/*     */     } else {
/* 314 */       this.nounceCount = 1L;
/* 315 */       this.cnonce = null;
/* 316 */       this.lastNonce = nonce;
/*     */     } 
/* 318 */     StringBuilder sb = new StringBuilder(256);
/* 319 */     Formatter formatter = new Formatter(sb, Locale.US);
/* 320 */     formatter.format("%08x", new Object[] { Long.valueOf(this.nounceCount) });
/* 321 */     formatter.close();
/* 322 */     String nc = sb.toString();
/*     */     
/* 324 */     if (this.cnonce == null) {
/* 325 */       this.cnonce = createCnonce();
/*     */     }
/*     */     
/* 328 */     this.a1 = null;
/* 329 */     this.a2 = null;
/*     */     
/* 331 */     if (algorithm.equalsIgnoreCase("MD5-sess")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 337 */       sb.setLength(0);
/* 338 */       sb.append(uname).append(':').append(realm).append(':').append(pwd);
/* 339 */       String checksum = encode(digester.digest(EncodingUtils.getBytes(sb.toString(), charset)));
/* 340 */       sb.setLength(0);
/* 341 */       sb.append(checksum).append(':').append(nonce).append(':').append(this.cnonce);
/* 342 */       this.a1 = sb.toString();
/*     */     } else {
/*     */       
/* 345 */       sb.setLength(0);
/* 346 */       sb.append(uname).append(':').append(realm).append(':').append(pwd);
/* 347 */       this.a1 = sb.toString();
/*     */     } 
/*     */     
/* 350 */     String hasha1 = encode(digester.digest(EncodingUtils.getBytes(this.a1, charset)));
/*     */     
/* 352 */     if (qop == 2) {
/*     */       
/* 354 */       this.a2 = method + ':' + uri;
/* 355 */     } else if (qop == 1) {
/*     */       
/* 357 */       HttpEntity entity = null;
/* 358 */       if (request instanceof HttpEntityEnclosingRequest) {
/* 359 */         entity = ((HttpEntityEnclosingRequest)request).getEntity();
/*     */       }
/* 361 */       if (entity != null && !entity.isRepeatable()) {
/*     */         
/* 363 */         if (qopset.contains("auth")) {
/* 364 */           qop = 2;
/* 365 */           this.a2 = method + ':' + uri;
/*     */         } else {
/* 367 */           throw new AuthenticationException("Qop auth-int cannot be used with a non-repeatable entity");
/*     */         } 
/*     */       } else {
/*     */         
/* 371 */         HttpEntityDigester entityDigester = new HttpEntityDigester(digester);
/*     */         try {
/* 373 */           if (entity != null) {
/* 374 */             entity.writeTo(entityDigester);
/*     */           }
/* 376 */           entityDigester.close();
/* 377 */         } catch (IOException ex) {
/* 378 */           throw new AuthenticationException("I/O error reading entity content", ex);
/*     */         } 
/* 380 */         this.a2 = method + ':' + uri + ':' + encode(entityDigester.getDigest());
/*     */       } 
/*     */     } else {
/* 383 */       this.a2 = method + ':' + uri;
/*     */     } 
/*     */     
/* 386 */     String hasha2 = encode(digester.digest(EncodingUtils.getBytes(this.a2, charset)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 391 */     if (qop == 0) {
/* 392 */       sb.setLength(0);
/* 393 */       sb.append(hasha1).append(':').append(nonce).append(':').append(hasha2);
/* 394 */       digestValue = sb.toString();
/*     */     } else {
/* 396 */       sb.setLength(0);
/* 397 */       sb.append(hasha1).append(':').append(nonce).append(':').append(nc).append(':').append(this.cnonce).append(':').append((qop == 1) ? "auth-int" : "auth").append(':').append(hasha2);
/*     */ 
/*     */       
/* 400 */       digestValue = sb.toString();
/*     */     } 
/*     */     
/* 403 */     String digest = encode(digester.digest(EncodingUtils.getAsciiBytes(digestValue)));
/*     */     
/* 405 */     CharArrayBuffer buffer = new CharArrayBuffer(128);
/* 406 */     if (isProxy()) {
/* 407 */       buffer.append("Proxy-Authorization");
/*     */     } else {
/* 409 */       buffer.append("Authorization");
/*     */     } 
/* 411 */     buffer.append(": Digest ");
/*     */     
/* 413 */     List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(20);
/* 414 */     params.add(new BasicNameValuePair("username", uname));
/* 415 */     params.add(new BasicNameValuePair("realm", realm));
/* 416 */     params.add(new BasicNameValuePair("nonce", nonce));
/* 417 */     params.add(new BasicNameValuePair("uri", uri));
/* 418 */     params.add(new BasicNameValuePair("response", digest));
/*     */     
/* 420 */     if (qop != 0) {
/* 421 */       params.add(new BasicNameValuePair("qop", (qop == 1) ? "auth-int" : "auth"));
/* 422 */       params.add(new BasicNameValuePair("nc", nc));
/* 423 */       params.add(new BasicNameValuePair("cnonce", this.cnonce));
/*     */     } 
/*     */     
/* 426 */     params.add(new BasicNameValuePair("algorithm", algorithm));
/* 427 */     if (opaque != null) {
/* 428 */       params.add(new BasicNameValuePair("opaque", opaque));
/*     */     }
/*     */     
/* 431 */     for (int i = 0; i < params.size(); i++) {
/* 432 */       BasicNameValuePair param = params.get(i);
/* 433 */       if (i > 0) {
/* 434 */         buffer.append(", ");
/*     */       }
/* 436 */       String name = param.getName();
/* 437 */       boolean noQuotes = ("nc".equals(name) || "qop".equals(name) || "algorithm".equals(name));
/*     */       
/* 439 */       BasicHeaderValueFormatter.INSTANCE.formatNameValuePair(buffer, (NameValuePair)param, !noQuotes);
/*     */     } 
/* 441 */     return (Header)new BufferedHeader(buffer);
/*     */   }
/*     */   
/*     */   String getCnonce() {
/* 445 */     return this.cnonce;
/*     */   }
/*     */   
/*     */   String getA1() {
/* 449 */     return this.a1;
/*     */   }
/*     */   
/*     */   String getA2() {
/* 453 */     return this.a2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String encode(byte[] binaryData) {
/* 464 */     int n = binaryData.length;
/* 465 */     char[] buffer = new char[n * 2];
/* 466 */     for (int i = 0; i < n; i++) {
/* 467 */       int low = binaryData[i] & 0xF;
/* 468 */       int high = (binaryData[i] & 0xF0) >> 4;
/* 469 */       buffer[i * 2] = HEXADECIMAL[high];
/* 470 */       buffer[i * 2 + 1] = HEXADECIMAL[low];
/*     */     } 
/*     */     
/* 473 */     return new String(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createCnonce() {
/* 483 */     SecureRandom rnd = new SecureRandom();
/* 484 */     byte[] tmp = new byte[8];
/* 485 */     rnd.nextBytes(tmp);
/* 486 */     return encode(tmp);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 491 */     StringBuilder builder = new StringBuilder();
/* 492 */     builder.append("DIGEST [complete=").append(this.complete).append(", nonce=").append(this.lastNonce).append(", nc=").append(this.nounceCount).append("]");
/*     */ 
/*     */ 
/*     */     
/* 496 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\auth\DigestScheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */