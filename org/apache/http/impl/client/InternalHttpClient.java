/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.auth.AuthSchemeProvider;
/*     */ import org.apache.http.auth.AuthState;
/*     */ import org.apache.http.client.ClientProtocolException;
/*     */ import org.apache.http.client.CookieStore;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.Configurable;
/*     */ import org.apache.http.client.methods.HttpExecutionAware;
/*     */ import org.apache.http.client.methods.HttpRequestWrapper;
/*     */ import org.apache.http.client.params.HttpClientParamConfig;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.HttpClientConnectionManager;
/*     */ import org.apache.http.conn.ManagedClientConnection;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.HttpRoutePlanner;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.cookie.CookieSpecProvider;
/*     */ import org.apache.http.impl.execchain.ClientExecChain;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpParamsNames;
/*     */ import org.apache.http.protocol.BasicHttpContext;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ @ThreadSafe
/*     */ class InternalHttpClient
/*     */   extends CloseableHttpClient
/*     */   implements Configurable
/*     */ {
/*  79 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final ClientExecChain execChain;
/*     */   
/*     */   private final HttpClientConnectionManager connManager;
/*     */   
/*     */   private final HttpRoutePlanner routePlanner;
/*     */   
/*     */   private final Lookup<CookieSpecProvider> cookieSpecRegistry;
/*     */   
/*     */   private final Lookup<AuthSchemeProvider> authSchemeRegistry;
/*     */   
/*     */   private final CookieStore cookieStore;
/*     */   
/*     */   private final CredentialsProvider credentialsProvider;
/*     */   
/*     */   private final RequestConfig defaultConfig;
/*     */   
/*     */   private final List<Closeable> closeables;
/*     */ 
/*     */   
/*     */   public InternalHttpClient(ClientExecChain execChain, HttpClientConnectionManager connManager, HttpRoutePlanner routePlanner, Lookup<CookieSpecProvider> cookieSpecRegistry, Lookup<AuthSchemeProvider> authSchemeRegistry, CookieStore cookieStore, CredentialsProvider credentialsProvider, RequestConfig defaultConfig, List<Closeable> closeables) {
/* 102 */     Args.notNull(execChain, "HTTP client exec chain");
/* 103 */     Args.notNull(connManager, "HTTP connection manager");
/* 104 */     Args.notNull(routePlanner, "HTTP route planner");
/* 105 */     this.execChain = execChain;
/* 106 */     this.connManager = connManager;
/* 107 */     this.routePlanner = routePlanner;
/* 108 */     this.cookieSpecRegistry = cookieSpecRegistry;
/* 109 */     this.authSchemeRegistry = authSchemeRegistry;
/* 110 */     this.cookieStore = cookieStore;
/* 111 */     this.credentialsProvider = credentialsProvider;
/* 112 */     this.defaultConfig = defaultConfig;
/* 113 */     this.closeables = closeables;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
/* 120 */     HttpHost host = target;
/* 121 */     if (host == null) {
/* 122 */       host = (HttpHost)request.getParams().getParameter("http.default-host");
/*     */     }
/* 124 */     return this.routePlanner.determineRoute(host, request, context);
/*     */   }
/*     */   
/*     */   private void setupContext(HttpClientContext context) {
/* 128 */     if (context.getAttribute("http.auth.target-scope") == null) {
/* 129 */       context.setAttribute("http.auth.target-scope", new AuthState());
/*     */     }
/* 131 */     if (context.getAttribute("http.auth.proxy-scope") == null) {
/* 132 */       context.setAttribute("http.auth.proxy-scope", new AuthState());
/*     */     }
/* 134 */     if (context.getAttribute("http.authscheme-registry") == null) {
/* 135 */       context.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
/*     */     }
/* 137 */     if (context.getAttribute("http.cookiespec-registry") == null) {
/* 138 */       context.setAttribute("http.cookiespec-registry", this.cookieSpecRegistry);
/*     */     }
/* 140 */     if (context.getAttribute("http.cookie-store") == null) {
/* 141 */       context.setAttribute("http.cookie-store", this.cookieStore);
/*     */     }
/* 143 */     if (context.getAttribute("http.auth.credentials-provider") == null) {
/* 144 */       context.setAttribute("http.auth.credentials-provider", this.credentialsProvider);
/*     */     }
/* 146 */     if (context.getAttribute("http.request-config") == null) {
/* 147 */       context.setAttribute("http.request-config", this.defaultConfig);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
/* 156 */     Args.notNull(request, "HTTP request");
/* 157 */     HttpExecutionAware execAware = null;
/* 158 */     if (request instanceof HttpExecutionAware) {
/* 159 */       execAware = (HttpExecutionAware)request;
/*     */     }
/*     */     try {
/* 162 */       HttpRequestWrapper wrapper = HttpRequestWrapper.wrap(request, target);
/* 163 */       HttpClientContext localcontext = HttpClientContext.adapt((context != null) ? context : (HttpContext)new BasicHttpContext());
/*     */       
/* 165 */       RequestConfig config = null;
/* 166 */       if (request instanceof Configurable) {
/* 167 */         config = ((Configurable)request).getConfig();
/*     */       }
/* 169 */       if (config == null) {
/* 170 */         HttpParams params = request.getParams();
/* 171 */         if (params instanceof HttpParamsNames) {
/* 172 */           if (!((HttpParamsNames)params).getNames().isEmpty()) {
/* 173 */             config = HttpClientParamConfig.getRequestConfig(params);
/*     */           }
/*     */         } else {
/* 176 */           config = HttpClientParamConfig.getRequestConfig(params);
/*     */         } 
/*     */       } 
/* 179 */       if (config != null) {
/* 180 */         localcontext.setRequestConfig(config);
/*     */       }
/* 182 */       setupContext(localcontext);
/* 183 */       HttpRoute route = determineRoute(target, (HttpRequest)wrapper, (HttpContext)localcontext);
/* 184 */       return this.execChain.execute(route, wrapper, localcontext, execAware);
/* 185 */     } catch (HttpException httpException) {
/* 186 */       throw new ClientProtocolException(httpException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestConfig getConfig() {
/* 192 */     return this.defaultConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 197 */     if (this.closeables != null) {
/* 198 */       for (Closeable closeable : this.closeables) {
/*     */         try {
/* 200 */           closeable.close();
/* 201 */         } catch (IOException ex) {
/* 202 */           this.log.error(ex.getMessage(), ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpParams getParams() {
/* 210 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientConnectionManager getConnectionManager() {
/* 216 */     return new ClientConnectionManager()
/*     */       {
/*     */         public void shutdown()
/*     */         {
/* 220 */           InternalHttpClient.this.connManager.shutdown();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public ClientConnectionRequest requestConnection(HttpRoute route, Object state) {
/* 226 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
/* 233 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */         
/*     */         public SchemeRegistry getSchemeRegistry() {
/* 238 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */         
/*     */         public void closeIdleConnections(long idletime, TimeUnit tunit) {
/* 243 */           InternalHttpClient.this.connManager.closeIdleConnections(idletime, tunit);
/*     */         }
/*     */ 
/*     */         
/*     */         public void closeExpiredConnections() {
/* 248 */           InternalHttpClient.this.connManager.closeExpiredConnections();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\InternalHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */