/*     */ package org.apache.http.impl.bootstrap;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import javax.net.ServerSocketFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.http.ConnectionReuseStrategy;
/*     */ import org.apache.http.ExceptionLogger;
/*     */ import org.apache.http.HttpConnectionFactory;
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.config.ConnectionConfig;
/*     */ import org.apache.http.config.SocketConfig;
/*     */ import org.apache.http.impl.DefaultBHttpServerConnection;
/*     */ import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
/*     */ import org.apache.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.http.impl.DefaultHttpResponseFactory;
/*     */ import org.apache.http.protocol.HttpExpectationVerifier;
/*     */ import org.apache.http.protocol.HttpProcessor;
/*     */ import org.apache.http.protocol.HttpProcessorBuilder;
/*     */ import org.apache.http.protocol.HttpRequestHandler;
/*     */ import org.apache.http.protocol.HttpRequestHandlerMapper;
/*     */ import org.apache.http.protocol.HttpService;
/*     */ import org.apache.http.protocol.ResponseConnControl;
/*     */ import org.apache.http.protocol.ResponseContent;
/*     */ import org.apache.http.protocol.ResponseDate;
/*     */ import org.apache.http.protocol.ResponseServer;
/*     */ import org.apache.http.protocol.UriHttpRequestHandlerMapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerBootstrap
/*     */ {
/*     */   private int listenerPort;
/*     */   private InetAddress localAddress;
/*     */   private SocketConfig socketConfig;
/*     */   private ConnectionConfig connectionConfig;
/*     */   private LinkedList<HttpRequestInterceptor> requestFirst;
/*     */   private LinkedList<HttpRequestInterceptor> requestLast;
/*     */   private LinkedList<HttpResponseInterceptor> responseFirst;
/*     */   private LinkedList<HttpResponseInterceptor> responseLast;
/*     */   private String serverInfo;
/*     */   private HttpProcessor httpProcessor;
/*     */   private ConnectionReuseStrategy connStrategy;
/*     */   private HttpResponseFactory responseFactory;
/*     */   private HttpRequestHandlerMapper handlerMapper;
/*     */   private Map<String, HttpRequestHandler> handlerMap;
/*     */   private HttpExpectationVerifier expectationVerifier;
/*     */   private ServerSocketFactory serverSocketFactory;
/*     */   private SSLContext sslContext;
/*     */   private SSLServerSetupHandler sslSetupHandler;
/*     */   private HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory;
/*     */   private ExceptionLogger exceptionLogger;
/*     */   
/*     */   public static ServerBootstrap bootstrap() {
/*  91 */     return new ServerBootstrap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setListenerPort(int listenerPort) {
/*  98 */     this.listenerPort = listenerPort;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setLocalAddress(InetAddress localAddress) {
/* 106 */     this.localAddress = localAddress;
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setSocketConfig(SocketConfig socketConfig) {
/* 114 */     this.socketConfig = socketConfig;
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setConnectionConfig(ConnectionConfig connectionConfig) {
/* 125 */     this.connectionConfig = connectionConfig;
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setHttpProcessor(HttpProcessor httpProcessor) {
/* 133 */     this.httpProcessor = httpProcessor;
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap addInterceptorFirst(HttpResponseInterceptor itcp) {
/* 144 */     if (itcp == null) {
/* 145 */       return this;
/*     */     }
/* 147 */     if (this.responseFirst == null) {
/* 148 */       this.responseFirst = new LinkedList<HttpResponseInterceptor>();
/*     */     }
/* 150 */     this.responseFirst.addFirst(itcp);
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap addInterceptorLast(HttpResponseInterceptor itcp) {
/* 161 */     if (itcp == null) {
/* 162 */       return this;
/*     */     }
/* 164 */     if (this.responseLast == null) {
/* 165 */       this.responseLast = new LinkedList<HttpResponseInterceptor>();
/*     */     }
/* 167 */     this.responseLast.addLast(itcp);
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap addInterceptorFirst(HttpRequestInterceptor itcp) {
/* 178 */     if (itcp == null) {
/* 179 */       return this;
/*     */     }
/* 181 */     if (this.requestFirst == null) {
/* 182 */       this.requestFirst = new LinkedList<HttpRequestInterceptor>();
/*     */     }
/* 184 */     this.requestFirst.addFirst(itcp);
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap addInterceptorLast(HttpRequestInterceptor itcp) {
/* 195 */     if (itcp == null) {
/* 196 */       return this;
/*     */     }
/* 198 */     if (this.requestLast == null) {
/* 199 */       this.requestLast = new LinkedList<HttpRequestInterceptor>();
/*     */     }
/* 201 */     this.requestLast.addLast(itcp);
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setServerInfo(String serverInfo) {
/* 212 */     this.serverInfo = serverInfo;
/* 213 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setConnectionReuseStrategy(ConnectionReuseStrategy connStrategy) {
/* 220 */     this.connStrategy = connStrategy;
/* 221 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setResponseFactory(HttpResponseFactory responseFactory) {
/* 228 */     this.responseFactory = responseFactory;
/* 229 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setHandlerMapper(HttpRequestHandlerMapper handlerMapper) {
/* 236 */     this.handlerMapper = handlerMapper;
/* 237 */     return this;
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
/*     */   public final ServerBootstrap registerHandler(String pattern, HttpRequestHandler handler) {
/* 251 */     if (pattern == null || handler == null) {
/* 252 */       return this;
/*     */     }
/* 254 */     if (this.handlerMap == null) {
/* 255 */       this.handlerMap = new HashMap<String, HttpRequestHandler>();
/*     */     }
/* 257 */     this.handlerMap.put(pattern, handler);
/* 258 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setExpectationVerifier(HttpExpectationVerifier expectationVerifier) {
/* 265 */     this.expectationVerifier = expectationVerifier;
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setConnectionFactory(HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory) {
/* 274 */     this.connectionFactory = connectionFactory;
/* 275 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setSslSetupHandler(SSLServerSetupHandler sslSetupHandler) {
/* 282 */     this.sslSetupHandler = sslSetupHandler;
/* 283 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
/* 290 */     this.serverSocketFactory = serverSocketFactory;
/* 291 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setSslContext(SSLContext sslContext) {
/* 301 */     this.sslContext = sslContext;
/* 302 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setExceptionLogger(ExceptionLogger exceptionLogger) {
/* 309 */     this.exceptionLogger = exceptionLogger;
/* 310 */     return this; } public HttpServer create() {
/*     */     UriHttpRequestHandlerMapper uriHttpRequestHandlerMapper;
/*     */     DefaultConnectionReuseStrategy defaultConnectionReuseStrategy;
/*     */     DefaultHttpResponseFactory defaultHttpResponseFactory;
/*     */     DefaultBHttpServerConnectionFactory defaultBHttpServerConnectionFactory;
/* 315 */     HttpProcessor httpProcessorCopy = this.httpProcessor;
/* 316 */     if (httpProcessorCopy == null) {
/*     */       
/* 318 */       HttpProcessorBuilder b = HttpProcessorBuilder.create();
/* 319 */       if (this.requestFirst != null) {
/* 320 */         for (HttpRequestInterceptor i : this.requestFirst) {
/* 321 */           b.addFirst(i);
/*     */         }
/*     */       }
/* 324 */       if (this.responseFirst != null) {
/* 325 */         for (HttpResponseInterceptor i : this.responseFirst) {
/* 326 */           b.addFirst(i);
/*     */         }
/*     */       }
/*     */       
/* 330 */       String serverInfoCopy = this.serverInfo;
/* 331 */       if (serverInfoCopy == null) {
/* 332 */         serverInfoCopy = "Apache-HttpCore/1.1";
/*     */       }
/*     */       
/* 335 */       b.addAll(new HttpResponseInterceptor[] { (HttpResponseInterceptor)new ResponseDate(), (HttpResponseInterceptor)new ResponseServer(serverInfoCopy), (HttpResponseInterceptor)new ResponseContent(), (HttpResponseInterceptor)new ResponseConnControl() });
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 340 */       if (this.requestLast != null) {
/* 341 */         for (HttpRequestInterceptor i : this.requestLast) {
/* 342 */           b.addLast(i);
/*     */         }
/*     */       }
/* 345 */       if (this.responseLast != null) {
/* 346 */         for (HttpResponseInterceptor i : this.responseLast) {
/* 347 */           b.addLast(i);
/*     */         }
/*     */       }
/* 350 */       httpProcessorCopy = b.build();
/*     */     } 
/*     */     
/* 353 */     HttpRequestHandlerMapper handlerMapperCopy = this.handlerMapper;
/* 354 */     if (handlerMapperCopy == null) {
/* 355 */       UriHttpRequestHandlerMapper reqistry = new UriHttpRequestHandlerMapper();
/* 356 */       if (this.handlerMap != null) {
/* 357 */         for (Map.Entry<String, HttpRequestHandler> entry : this.handlerMap.entrySet()) {
/* 358 */           reqistry.register(entry.getKey(), entry.getValue());
/*     */         }
/*     */       }
/* 361 */       uriHttpRequestHandlerMapper = reqistry;
/*     */     } 
/*     */     
/* 364 */     ConnectionReuseStrategy connStrategyCopy = this.connStrategy;
/* 365 */     if (connStrategyCopy == null) {
/* 366 */       defaultConnectionReuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;
/*     */     }
/*     */     
/* 369 */     HttpResponseFactory responseFactoryCopy = this.responseFactory;
/* 370 */     if (responseFactoryCopy == null) {
/* 371 */       defaultHttpResponseFactory = DefaultHttpResponseFactory.INSTANCE;
/*     */     }
/*     */     
/* 374 */     HttpService httpService = new HttpService(httpProcessorCopy, (ConnectionReuseStrategy)defaultConnectionReuseStrategy, (HttpResponseFactory)defaultHttpResponseFactory, (HttpRequestHandlerMapper)uriHttpRequestHandlerMapper, this.expectationVerifier);
/*     */ 
/*     */ 
/*     */     
/* 378 */     ServerSocketFactory serverSocketFactoryCopy = this.serverSocketFactory;
/* 379 */     if (serverSocketFactoryCopy == null) {
/* 380 */       if (this.sslContext != null) {
/* 381 */         serverSocketFactoryCopy = this.sslContext.getServerSocketFactory();
/*     */       } else {
/* 383 */         serverSocketFactoryCopy = ServerSocketFactory.getDefault();
/*     */       } 
/*     */     }
/*     */     
/* 387 */     HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactoryCopy = this.connectionFactory;
/* 388 */     if (connectionFactoryCopy == null) {
/* 389 */       if (this.connectionConfig != null) {
/* 390 */         defaultBHttpServerConnectionFactory = new DefaultBHttpServerConnectionFactory(this.connectionConfig);
/*     */       } else {
/* 392 */         defaultBHttpServerConnectionFactory = DefaultBHttpServerConnectionFactory.INSTANCE;
/*     */       } 
/*     */     }
/*     */     
/* 396 */     ExceptionLogger exceptionLoggerCopy = this.exceptionLogger;
/* 397 */     if (exceptionLoggerCopy == null) {
/* 398 */       exceptionLoggerCopy = ExceptionLogger.NO_OP;
/*     */     }
/*     */     
/* 401 */     return new HttpServer((this.listenerPort > 0) ? this.listenerPort : 0, this.localAddress, (this.socketConfig != null) ? this.socketConfig : SocketConfig.DEFAULT, serverSocketFactoryCopy, httpService, (HttpConnectionFactory<? extends DefaultBHttpServerConnection>)defaultBHttpServerConnectionFactory, this.sslSetupHandler, exceptionLoggerCopy);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\bootstrap\ServerBootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */