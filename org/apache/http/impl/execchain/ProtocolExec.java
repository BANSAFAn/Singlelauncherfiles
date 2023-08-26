/*     */ package org.apache.http.impl.execchain;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.auth.AuthScope;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.UsernamePasswordCredentials;
/*     */ import org.apache.http.client.CredentialsProvider;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpExecutionAware;
/*     */ import org.apache.http.client.methods.HttpRequestWrapper;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.client.protocol.HttpClientContext;
/*     */ import org.apache.http.client.utils.URIUtils;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.conn.routing.RouteInfo;
/*     */ import org.apache.http.impl.client.BasicCredentialsProvider;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.protocol.HttpProcessor;
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
/*     */ @Immutable
/*     */ public class ProtocolExec
/*     */   implements ClientExecChain
/*     */ {
/*  76 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final ClientExecChain requestExecutor;
/*     */   private final HttpProcessor httpProcessor;
/*     */   
/*     */   public ProtocolExec(ClientExecChain requestExecutor, HttpProcessor httpProcessor) {
/*  82 */     Args.notNull(requestExecutor, "HTTP client request executor");
/*  83 */     Args.notNull(httpProcessor, "HTTP protocol processor");
/*  84 */     this.requestExecutor = requestExecutor;
/*  85 */     this.httpProcessor = httpProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void rewriteRequestURI(HttpRequestWrapper request, HttpRoute route) throws ProtocolException {
/*  91 */     URI uri = request.getURI();
/*  92 */     if (uri != null) {
/*     */       try {
/*  94 */         request.setURI(URIUtils.rewriteURIForRoute(uri, (RouteInfo)route));
/*  95 */       } catch (URISyntaxException ex) {
/*  96 */         throw new ProtocolException("Invalid URI: " + uri, ex);
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
/*     */   public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
/* 108 */     Args.notNull(route, "HTTP route");
/* 109 */     Args.notNull(request, "HTTP request");
/* 110 */     Args.notNull(context, "HTTP context");
/*     */     
/* 112 */     HttpRequest original = request.getOriginal();
/* 113 */     URI uri = null;
/* 114 */     if (original instanceof HttpUriRequest) {
/* 115 */       uri = ((HttpUriRequest)original).getURI();
/*     */     } else {
/* 117 */       String uriString = original.getRequestLine().getUri();
/*     */       try {
/* 119 */         uri = URI.create(uriString);
/* 120 */       } catch (IllegalArgumentException ex) {
/* 121 */         if (this.log.isDebugEnabled()) {
/* 122 */           this.log.debug("Unable to parse '" + uriString + "' as a valid URI; " + "request URI and Host header may be inconsistent", ex);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 128 */     request.setURI(uri);
/*     */ 
/*     */     
/* 131 */     rewriteRequestURI(request, route);
/*     */     
/* 133 */     HttpParams params = request.getParams();
/* 134 */     HttpHost virtualHost = (HttpHost)params.getParameter("http.virtual-host");
/*     */     
/* 136 */     if (virtualHost != null && virtualHost.getPort() == -1) {
/* 137 */       int port = route.getTargetHost().getPort();
/* 138 */       if (port != -1) {
/* 139 */         virtualHost = new HttpHost(virtualHost.getHostName(), port, virtualHost.getSchemeName());
/*     */       }
/*     */       
/* 142 */       if (this.log.isDebugEnabled()) {
/* 143 */         this.log.debug("Using virtual host" + virtualHost);
/*     */       }
/*     */     } 
/*     */     
/* 147 */     HttpHost target = null;
/* 148 */     if (virtualHost != null) {
/* 149 */       target = virtualHost;
/*     */     }
/* 151 */     else if (uri != null && uri.isAbsolute() && uri.getHost() != null) {
/* 152 */       target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
/*     */     } 
/*     */     
/* 155 */     if (target == null) {
/* 156 */       target = request.getTarget();
/*     */     }
/* 158 */     if (target == null) {
/* 159 */       target = route.getTargetHost();
/*     */     }
/*     */ 
/*     */     
/* 163 */     if (uri != null) {
/* 164 */       String userinfo = uri.getUserInfo();
/* 165 */       if (userinfo != null) {
/* 166 */         BasicCredentialsProvider basicCredentialsProvider; CredentialsProvider credsProvider = context.getCredentialsProvider();
/* 167 */         if (credsProvider == null) {
/* 168 */           basicCredentialsProvider = new BasicCredentialsProvider();
/* 169 */           context.setCredentialsProvider((CredentialsProvider)basicCredentialsProvider);
/*     */         } 
/* 171 */         basicCredentialsProvider.setCredentials(new AuthScope(target), (Credentials)new UsernamePasswordCredentials(userinfo));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     context.setAttribute("http.target_host", target);
/* 179 */     context.setAttribute("http.route", route);
/* 180 */     context.setAttribute("http.request", request);
/*     */     
/* 182 */     this.httpProcessor.process((HttpRequest)request, (HttpContext)context);
/*     */     
/* 184 */     CloseableHttpResponse response = this.requestExecutor.execute(route, request, context, execAware);
/*     */ 
/*     */     
/*     */     try {
/* 188 */       context.setAttribute("http.response", response);
/* 189 */       this.httpProcessor.process((HttpResponse)response, (HttpContext)context);
/* 190 */       return response;
/* 191 */     } catch (RuntimeException ex) {
/* 192 */       response.close();
/* 193 */       throw ex;
/* 194 */     } catch (IOException ex) {
/* 195 */       response.close();
/* 196 */       throw ex;
/* 197 */     } catch (HttpException ex) {
/* 198 */       response.close();
/* 199 */       throw ex;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\execchain\ProtocolExec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */