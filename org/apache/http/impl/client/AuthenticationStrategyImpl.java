/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthOption;
/*     */ import org.apache.http.auth.AuthScheme;
/*     */ import org.apache.http.auth.AuthSchemeProvider;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.client.AuthCache;
/*     */ import org.apache.http.client.AuthenticationStrategy;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.config.Lookup;
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
/*     */ @Immutable
/*     */ abstract class AuthenticationStrategyImpl
/*     */   implements AuthenticationStrategy
/*     */ {
/*  68 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*  70 */   private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList(new String[] { "Negotiate", "Kerberos", "NTLM", "Digest", "Basic" }));
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int challengeCode;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String headerName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AuthenticationStrategyImpl(int challengeCode, String headerName) {
/*  87 */     this.challengeCode = challengeCode;
/*  88 */     this.headerName = headerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAuthenticationRequested(HttpHost authhost, HttpResponse response, HttpContext context) {
/*  96 */     Args.notNull(response, "HTTP response");
/*  97 */     int status = response.getStatusLine().getStatusCode();
/*  98 */     return (status == this.challengeCode);
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
/*     */   public Map<String, Header> getChallenges(HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
/* 111 */     Args.notNull(response, "HTTP response");
/* 112 */     Header[] headers = response.getHeaders(this.headerName);
/* 113 */     Map<String, Header> map = new HashMap<String, Header>(headers.length);
/* 114 */     for (Header header : headers) {
/*     */       CharArrayBuffer buffer;
/*     */       int pos;
/* 117 */       if (header instanceof FormattedHeader) {
/* 118 */         buffer = ((FormattedHeader)header).getBuffer();
/* 119 */         pos = ((FormattedHeader)header).getValuePos();
/*     */       } else {
/* 121 */         String str = header.getValue();
/* 122 */         if (str == null) {
/* 123 */           throw new MalformedChallengeException("Header value is null");
/*     */         }
/* 125 */         buffer = new CharArrayBuffer(str.length());
/* 126 */         buffer.append(str);
/* 127 */         pos = 0;
/*     */       } 
/* 129 */       while (pos < buffer.length() && HTTP.isWhitespace(buffer.charAt(pos))) {
/* 130 */         pos++;
/*     */       }
/* 132 */       int beginIndex = pos;
/* 133 */       while (pos < buffer.length() && !HTTP.isWhitespace(buffer.charAt(pos))) {
/* 134 */         pos++;
/*     */       }
/* 136 */       int endIndex = pos;
/* 137 */       String s = buffer.substring(beginIndex, endIndex);
/* 138 */       map.put(s.toLowerCase(Locale.ROOT), header);
/*     */     } 
/* 140 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Collection<String> getPreferredAuthSchemes(RequestConfig paramRequestConfig);
/*     */ 
/*     */ 
/*     */   
/*     */   public Queue<AuthOption> select(Map<String, Header> challenges, HttpHost authhost, HttpResponse response, HttpContext context) throws MalformedChallengeException {
/* 151 */     Args.notNull(challenges, "Map of auth challenges");
/* 152 */     Args.notNull(authhost, "Host");
/* 153 */     Args.notNull(response, "HTTP response");
/* 154 */     Args.notNull(context, "HTTP context");
/* 155 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*     */     
/* 157 */     Queue<AuthOption> options = new LinkedList<AuthOption>();
/* 158 */     Lookup<AuthSchemeProvider> registry = clientContext.getAuthSchemeRegistry();
/* 159 */     if (registry == null) {
/* 160 */       this.log.debug("Auth scheme registry not set in the context");
/* 161 */       return options;
/*     */     } 
/* 163 */     CredentialsProvider credsProvider = clientContext.getCredentialsProvider();
/* 164 */     if (credsProvider == null) {
/* 165 */       this.log.debug("Credentials provider not set in the context");
/* 166 */       return options;
/*     */     } 
/* 168 */     RequestConfig config = clientContext.getRequestConfig();
/* 169 */     Collection<String> authPrefs = getPreferredAuthSchemes(config);
/* 170 */     if (authPrefs == null) {
/* 171 */       authPrefs = DEFAULT_SCHEME_PRIORITY;
/*     */     }
/* 173 */     if (this.log.isDebugEnabled()) {
/* 174 */       this.log.debug("Authentication schemes in the order of preference: " + authPrefs);
/*     */     }
/*     */     
/* 177 */     for (String id : authPrefs) {
/* 178 */       Header challenge = challenges.get(id.toLowerCase(Locale.ROOT));
/* 179 */       if (challenge != null) {
/* 180 */         AuthSchemeProvider authSchemeProvider = (AuthSchemeProvider)registry.lookup(id);
/* 181 */         if (authSchemeProvider == null) {
/* 182 */           if (this.log.isWarnEnabled()) {
/* 183 */             this.log.warn("Authentication scheme " + id + " not supported");
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 188 */         AuthScheme authScheme = authSchemeProvider.create(context);
/* 189 */         authScheme.processChallenge(challenge);
/*     */         
/* 191 */         AuthScope authScope = new AuthScope(authhost.getHostName(), authhost.getPort(), authScheme.getRealm(), authScheme.getSchemeName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 197 */         Credentials credentials = credsProvider.getCredentials(authScope);
/* 198 */         if (credentials != null)
/* 199 */           options.add(new AuthOption(authScheme, credentials)); 
/*     */         continue;
/*     */       } 
/* 202 */       if (this.log.isDebugEnabled()) {
/* 203 */         this.log.debug("Challenge for " + id + " authentication scheme not available");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 208 */     return options;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void authSucceeded(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
/* 214 */     Args.notNull(authhost, "Host");
/* 215 */     Args.notNull(authScheme, "Auth scheme");
/* 216 */     Args.notNull(context, "HTTP context");
/*     */     
/* 218 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*     */     
/* 220 */     if (isCachable(authScheme)) {
/* 221 */       AuthCache authCache = clientContext.getAuthCache();
/* 222 */       if (authCache == null) {
/* 223 */         authCache = new BasicAuthCache();
/* 224 */         clientContext.setAuthCache(authCache);
/*     */       } 
/* 226 */       if (this.log.isDebugEnabled()) {
/* 227 */         this.log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
/*     */       }
/*     */       
/* 230 */       authCache.put(authhost, authScheme);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isCachable(AuthScheme authScheme) {
/* 235 */     if (authScheme == null || !authScheme.isComplete()) {
/* 236 */       return false;
/*     */     }
/* 238 */     String schemeName = authScheme.getSchemeName();
/* 239 */     return (schemeName.equalsIgnoreCase("Basic") || schemeName.equalsIgnoreCase("Digest"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void authFailed(HttpHost authhost, AuthScheme authScheme, HttpContext context) {
/* 246 */     Args.notNull(authhost, "Host");
/* 247 */     Args.notNull(context, "HTTP context");
/*     */     
/* 249 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*     */     
/* 251 */     AuthCache authCache = clientContext.getAuthCache();
/* 252 */     if (authCache != null) {
/* 253 */       if (this.log.isDebugEnabled()) {
/* 254 */         this.log.debug("Clearing cached auth scheme for " + authhost);
/*     */       }
/* 256 */       authCache.remove(authhost);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\AuthenticationStrategyImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */