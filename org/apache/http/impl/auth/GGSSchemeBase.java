/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.InvalidCredentialsException;
/*     */ import org.apache.http.auth.KerberosCredentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSCredential;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSManager;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class GGSSchemeBase
/*     */   extends AuthSchemeBase
/*     */ {
/*     */   enum State
/*     */   {
/*  66 */     UNINITIATED,
/*  67 */     CHALLENGE_RECEIVED,
/*  68 */     TOKEN_GENERATED,
/*  69 */     FAILED;
/*     */   }
/*     */   
/*  72 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final Base64 base64codec;
/*     */   
/*     */   private final boolean stripPort;
/*     */   
/*     */   private final boolean useCanonicalHostname;
/*     */   
/*     */   private State state;
/*     */   
/*     */   private byte[] token;
/*     */   
/*     */   private String service;
/*     */   
/*     */   GGSSchemeBase(boolean stripPort, boolean useCanonicalHostname) {
/*  87 */     this.base64codec = new Base64(0);
/*  88 */     this.stripPort = stripPort;
/*  89 */     this.useCanonicalHostname = useCanonicalHostname;
/*  90 */     this.state = State.UNINITIATED;
/*     */   }
/*     */   
/*     */   GGSSchemeBase(boolean stripPort) {
/*  94 */     this(stripPort, true);
/*     */   }
/*     */   
/*     */   GGSSchemeBase() {
/*  98 */     this(true, true);
/*     */   }
/*     */   
/*     */   protected GSSManager getManager() {
/* 102 */     return GSSManager.getInstance();
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] generateGSSToken(byte[] input, Oid oid, String authServer) throws GSSException {
/* 107 */     return generateGSSToken(input, oid, authServer, (Credentials)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] generateGSSToken(byte[] input, Oid oid, String authServer, Credentials credentials) throws GSSException {
/*     */     GSSCredential gssCredential;
/* 116 */     byte[] inputBuff = input;
/* 117 */     if (inputBuff == null) {
/* 118 */       inputBuff = new byte[0];
/*     */     }
/* 120 */     GSSManager manager = getManager();
/* 121 */     GSSName serverName = manager.createName(this.service + "@" + authServer, GSSName.NT_HOSTBASED_SERVICE);
/*     */ 
/*     */     
/* 124 */     if (credentials instanceof KerberosCredentials) {
/* 125 */       gssCredential = ((KerberosCredentials)credentials).getGSSCredential();
/*     */     } else {
/* 127 */       gssCredential = null;
/*     */     } 
/*     */     
/* 130 */     GSSContext gssContext = manager.createContext(serverName.canonicalize(oid), oid, gssCredential, 0);
/*     */     
/* 132 */     gssContext.requestMutualAuth(true);
/* 133 */     gssContext.requestCredDeleg(true);
/* 134 */     return gssContext.initSecContext(inputBuff, 0, inputBuff.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected byte[] generateToken(byte[] input, String authServer) throws GSSException {
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] generateToken(byte[] input, String authServer, Credentials credentials) throws GSSException {
/* 152 */     return generateToken(input, authServer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 157 */     return (this.state == State.TOKEN_GENERATED || this.state == State.FAILED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
/* 169 */     return authenticate(credentials, request, (HttpContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/*     */     String tokenstr;
/*     */     CharArrayBuffer buffer;
/* 177 */     Args.notNull(request, "HTTP request");
/* 178 */     switch (this.state) {
/*     */       case UNINITIATED:
/* 180 */         throw new AuthenticationException(getSchemeName() + " authentication has not been initiated");
/*     */       case FAILED:
/* 182 */         throw new AuthenticationException(getSchemeName() + " authentication has failed");
/*     */       case CHALLENGE_RECEIVED:
/*     */         try {
/* 185 */           HttpHost host; String authServer; HttpRoute route = (HttpRoute)context.getAttribute("http.route");
/* 186 */           if (route == null) {
/* 187 */             throw new AuthenticationException("Connection route is not available");
/*     */           }
/*     */           
/* 190 */           if (isProxy()) {
/* 191 */             host = route.getProxyHost();
/* 192 */             if (host == null) {
/* 193 */               host = route.getTargetHost();
/*     */             }
/*     */           } else {
/* 196 */             host = route.getTargetHost();
/*     */           } 
/*     */           
/* 199 */           String hostname = host.getHostName();
/*     */           
/* 201 */           if (this.useCanonicalHostname) {
/*     */             
/*     */             try {
/*     */ 
/*     */ 
/*     */               
/* 207 */               hostname = resolveCanonicalHostname(hostname);
/* 208 */             } catch (UnknownHostException ignore) {}
/*     */           }
/*     */           
/* 211 */           if (this.stripPort) {
/* 212 */             authServer = hostname;
/*     */           } else {
/* 214 */             authServer = hostname + ":" + host.getPort();
/*     */           } 
/*     */           
/* 217 */           this.service = host.getSchemeName().toUpperCase(Locale.ROOT);
/*     */           
/* 219 */           if (this.log.isDebugEnabled()) {
/* 220 */             this.log.debug("init " + authServer);
/*     */           }
/* 222 */           this.token = generateToken(this.token, authServer, credentials);
/* 223 */           this.state = State.TOKEN_GENERATED;
/* 224 */         } catch (GSSException gsse) {
/* 225 */           this.state = State.FAILED;
/* 226 */           if (gsse.getMajor() == 9 || gsse.getMajor() == 8)
/*     */           {
/* 228 */             throw new InvalidCredentialsException(gsse.getMessage(), gsse);
/*     */           }
/* 230 */           if (gsse.getMajor() == 13) {
/* 231 */             throw new InvalidCredentialsException(gsse.getMessage(), gsse);
/*     */           }
/* 233 */           if (gsse.getMajor() == 10 || gsse.getMajor() == 19 || gsse.getMajor() == 20)
/*     */           {
/*     */             
/* 236 */             throw new AuthenticationException(gsse.getMessage(), gsse);
/*     */           }
/*     */           
/* 239 */           throw new AuthenticationException(gsse.getMessage());
/*     */         } 
/*     */       case TOKEN_GENERATED:
/* 242 */         tokenstr = new String(this.base64codec.encode(this.token));
/* 243 */         if (this.log.isDebugEnabled()) {
/* 244 */           this.log.debug("Sending response '" + tokenstr + "' back to the auth server");
/*     */         }
/* 246 */         buffer = new CharArrayBuffer(32);
/* 247 */         if (isProxy()) {
/* 248 */           buffer.append("Proxy-Authorization");
/*     */         } else {
/* 250 */           buffer.append("Authorization");
/*     */         } 
/* 252 */         buffer.append(": Negotiate ");
/* 253 */         buffer.append(tokenstr);
/* 254 */         return (Header)new BufferedHeader(buffer);
/*     */     } 
/* 256 */     throw new IllegalStateException("Illegal state: " + this.state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseChallenge(CharArrayBuffer buffer, int beginIndex, int endIndex) throws MalformedChallengeException {
/* 264 */     String challenge = buffer.substringTrimmed(beginIndex, endIndex);
/* 265 */     if (this.log.isDebugEnabled()) {
/* 266 */       this.log.debug("Received challenge '" + challenge + "' from the auth server");
/*     */     }
/* 268 */     if (this.state == State.UNINITIATED) {
/* 269 */       this.token = Base64.decodeBase64(challenge.getBytes());
/* 270 */       this.state = State.CHALLENGE_RECEIVED;
/*     */     } else {
/* 272 */       this.log.debug("Authentication already attempted");
/* 273 */       this.state = State.FAILED;
/*     */     } 
/*     */   }
/*     */   
/*     */   private String resolveCanonicalHostname(String host) throws UnknownHostException {
/* 278 */     InetAddress in = InetAddress.getByName(host);
/* 279 */     String canonicalServer = in.getCanonicalHostName();
/* 280 */     if (in.getHostAddress().contentEquals(canonicalServer)) {
/* 281 */       return host;
/*     */     }
/* 283 */     return canonicalServer;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\auth\GGSSchemeBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */