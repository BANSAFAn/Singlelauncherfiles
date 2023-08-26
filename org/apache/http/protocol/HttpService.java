/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.HttpServerConnection;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.entity.ByteArrayEntity;
/*     */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.http.impl.DefaultHttpResponseFactory;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.EncodingUtils;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class HttpService
/*     */ {
/*  80 */   private volatile HttpParams params = null;
/*  81 */   private volatile HttpProcessor processor = null;
/*  82 */   private volatile HttpRequestHandlerMapper handlerMapper = null;
/*  83 */   private volatile ConnectionReuseStrategy connStrategy = null;
/*  84 */   private volatile HttpResponseFactory responseFactory = null;
/*  85 */   private volatile HttpExpectationVerifier expectationVerifier = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public HttpService(HttpProcessor processor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpRequestHandlerResolver handlerResolver, HttpExpectationVerifier expectationVerifier, HttpParams params) {
/* 109 */     this(processor, connStrategy, responseFactory, new HttpRequestHandlerResolverAdapter(handlerResolver), expectationVerifier);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     this.params = params;
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
/*     */   @Deprecated
/*     */   public HttpService(HttpProcessor processor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpRequestHandlerResolver handlerResolver, HttpParams params) {
/* 137 */     this(processor, connStrategy, responseFactory, new HttpRequestHandlerResolverAdapter(handlerResolver), (HttpExpectationVerifier)null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     this.params = params;
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
/*     */   public HttpService(HttpProcessor proc, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory) {
/* 161 */     setHttpProcessor(proc);
/* 162 */     setConnReuseStrategy(connStrategy);
/* 163 */     setResponseFactory(responseFactory);
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
/*     */   public HttpService(HttpProcessor processor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpRequestHandlerMapper handlerMapper, HttpExpectationVerifier expectationVerifier) {
/* 186 */     this.processor = (HttpProcessor)Args.notNull(processor, "HTTP processor");
/* 187 */     this.connStrategy = (connStrategy != null) ? connStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;
/*     */     
/* 189 */     this.responseFactory = (responseFactory != null) ? responseFactory : (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE;
/*     */     
/* 191 */     this.handlerMapper = handlerMapper;
/* 192 */     this.expectationVerifier = expectationVerifier;
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
/*     */   public HttpService(HttpProcessor processor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpRequestHandlerMapper handlerMapper) {
/* 212 */     this(processor, connStrategy, responseFactory, handlerMapper, (HttpExpectationVerifier)null);
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
/*     */   public HttpService(HttpProcessor processor, HttpRequestHandlerMapper handlerMapper) {
/* 225 */     this(processor, (ConnectionReuseStrategy)null, (HttpResponseFactory)null, handlerMapper, (HttpExpectationVerifier)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setHttpProcessor(HttpProcessor processor) {
/* 233 */     Args.notNull(processor, "HTTP processor");
/* 234 */     this.processor = processor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setConnReuseStrategy(ConnectionReuseStrategy connStrategy) {
/* 242 */     Args.notNull(connStrategy, "Connection reuse strategy");
/* 243 */     this.connStrategy = connStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setResponseFactory(HttpResponseFactory responseFactory) {
/* 251 */     Args.notNull(responseFactory, "Response factory");
/* 252 */     this.responseFactory = responseFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setParams(HttpParams params) {
/* 260 */     this.params = params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setHandlerResolver(HttpRequestHandlerResolver handlerResolver) {
/* 268 */     this.handlerMapper = new HttpRequestHandlerResolverAdapter(handlerResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setExpectationVerifier(HttpExpectationVerifier expectationVerifier) {
/* 276 */     this.expectationVerifier = expectationVerifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public HttpParams getParams() {
/* 284 */     return this.params;
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
/*     */   public void handleRequest(HttpServerConnection conn, HttpContext context) throws IOException, HttpException {
/* 301 */     context.setAttribute("http.connection", conn);
/*     */     
/* 303 */     HttpRequest request = null;
/* 304 */     HttpResponse response = null;
/*     */     
/*     */     try {
/* 307 */       request = conn.receiveRequestHeader();
/* 308 */       if (request instanceof HttpEntityEnclosingRequest)
/*     */       {
/* 310 */         if (((HttpEntityEnclosingRequest)request).expectContinue()) {
/* 311 */           response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, 100, context);
/*     */           
/* 313 */           if (this.expectationVerifier != null) {
/*     */             try {
/* 315 */               this.expectationVerifier.verify(request, response, context);
/* 316 */             } catch (HttpException ex) {
/* 317 */               response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
/*     */               
/* 319 */               handleException(ex, response);
/*     */             } 
/*     */           }
/* 322 */           if (response.getStatusLine().getStatusCode() < 200) {
/*     */ 
/*     */             
/* 325 */             conn.sendResponseHeader(response);
/* 326 */             conn.flush();
/* 327 */             response = null;
/* 328 */             conn.receiveRequestEntity((HttpEntityEnclosingRequest)request);
/*     */           } 
/*     */         } else {
/* 331 */           conn.receiveRequestEntity((HttpEntityEnclosingRequest)request);
/*     */         } 
/*     */       }
/*     */       
/* 335 */       context.setAttribute("http.request", request);
/*     */       
/* 337 */       if (response == null) {
/* 338 */         response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, 200, context);
/*     */         
/* 340 */         this.processor.process(request, context);
/* 341 */         doService(request, response, context);
/*     */       } 
/*     */ 
/*     */       
/* 345 */       if (request instanceof HttpEntityEnclosingRequest) {
/* 346 */         HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
/* 347 */         EntityUtils.consume(entity);
/*     */       }
/*     */     
/* 350 */     } catch (HttpException ex) {
/* 351 */       response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
/*     */ 
/*     */       
/* 354 */       handleException(ex, response);
/*     */     } 
/*     */     
/* 357 */     context.setAttribute("http.response", response);
/*     */     
/* 359 */     this.processor.process(response, context);
/* 360 */     conn.sendResponseHeader(response);
/* 361 */     if (canResponseHaveBody(request, response)) {
/* 362 */       conn.sendResponseEntity(response);
/*     */     }
/* 364 */     conn.flush();
/*     */     
/* 366 */     if (!this.connStrategy.keepAlive(response, context)) {
/* 367 */       conn.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
/* 372 */     if (request != null && "HEAD".equalsIgnoreCase(request.getRequestLine().getMethod())) {
/* 373 */       return false;
/*     */     }
/* 375 */     int status = response.getStatusLine().getStatusCode();
/* 376 */     return (status >= 200 && status != 204 && status != 304 && status != 205);
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
/*     */   protected void handleException(HttpException ex, HttpResponse response) {
/* 391 */     if (ex instanceof org.apache.http.MethodNotSupportedException) {
/* 392 */       response.setStatusCode(501);
/* 393 */     } else if (ex instanceof org.apache.http.UnsupportedHttpVersionException) {
/* 394 */       response.setStatusCode(505);
/* 395 */     } else if (ex instanceof org.apache.http.ProtocolException) {
/* 396 */       response.setStatusCode(400);
/*     */     } else {
/* 398 */       response.setStatusCode(500);
/*     */     } 
/* 400 */     String message = ex.getMessage();
/* 401 */     if (message == null) {
/* 402 */       message = ex.toString();
/*     */     }
/* 404 */     byte[] msg = EncodingUtils.getAsciiBytes(message);
/* 405 */     ByteArrayEntity entity = new ByteArrayEntity(msg);
/* 406 */     entity.setContentType("text/plain; charset=US-ASCII");
/* 407 */     response.setEntity((HttpEntity)entity);
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
/*     */   protected void doService(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 431 */     HttpRequestHandler handler = null;
/* 432 */     if (this.handlerMapper != null) {
/* 433 */       handler = this.handlerMapper.lookup(request);
/*     */     }
/* 435 */     if (handler != null) {
/* 436 */       handler.handle(request, response, context);
/*     */     } else {
/* 438 */       response.setStatusCode(501);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   private static class HttpRequestHandlerResolverAdapter
/*     */     implements HttpRequestHandlerMapper
/*     */   {
/*     */     private final HttpRequestHandlerResolver resolver;
/*     */ 
/*     */     
/*     */     public HttpRequestHandlerResolverAdapter(HttpRequestHandlerResolver resolver) {
/* 451 */       this.resolver = resolver;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpRequestHandler lookup(HttpRequest request) {
/* 456 */       return this.resolver.lookup(request.getRequestLine().getUri());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\HttpService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */