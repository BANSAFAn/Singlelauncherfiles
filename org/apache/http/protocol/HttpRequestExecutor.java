/*     */ package org.apache.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpClientConnection;
/*     */ import org.apache.http.HttpEntityEnclosingRequest;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ public class HttpRequestExecutor
/*     */ {
/*     */   public static final int DEFAULT_WAIT_FOR_CONTINUE = 3000;
/*     */   private final int waitForContinue;
/*     */   
/*     */   public HttpRequestExecutor(int waitForContinue) {
/*  71 */     this.waitForContinue = Args.positive(waitForContinue, "Wait for continue time");
/*     */   }
/*     */   
/*     */   public HttpRequestExecutor() {
/*  75 */     this(3000);
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
/*     */   protected boolean canResponseHaveBody(HttpRequest request, HttpResponse response) {
/*  92 */     if ("HEAD".equalsIgnoreCase(request.getRequestLine().getMethod())) {
/*  93 */       return false;
/*     */     }
/*  95 */     int status = response.getStatusLine().getStatusCode();
/*  96 */     return (status >= 200 && status != 204 && status != 304 && status != 205);
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
/*     */   public HttpResponse execute(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
/* 118 */     Args.notNull(request, "HTTP request");
/* 119 */     Args.notNull(conn, "Client connection");
/* 120 */     Args.notNull(context, "HTTP context");
/*     */     try {
/* 122 */       HttpResponse response = doSendRequest(request, conn, context);
/* 123 */       if (response == null) {
/* 124 */         response = doReceiveResponse(request, conn, context);
/*     */       }
/* 126 */       return response;
/* 127 */     } catch (IOException ex) {
/* 128 */       closeConnection(conn);
/* 129 */       throw ex;
/* 130 */     } catch (HttpException ex) {
/* 131 */       closeConnection(conn);
/* 132 */       throw ex;
/* 133 */     } catch (RuntimeException ex) {
/* 134 */       closeConnection(conn);
/* 135 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void closeConnection(HttpClientConnection conn) {
/*     */     try {
/* 141 */       conn.close();
/* 142 */     } catch (IOException ignore) {}
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
/*     */   public void preProcess(HttpRequest request, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
/* 162 */     Args.notNull(request, "HTTP request");
/* 163 */     Args.notNull(processor, "HTTP processor");
/* 164 */     Args.notNull(context, "HTTP context");
/* 165 */     context.setAttribute("http.request", request);
/* 166 */     processor.process(request, context);
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
/*     */   
/*     */   protected HttpResponse doSendRequest(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
/* 195 */     Args.notNull(request, "HTTP request");
/* 196 */     Args.notNull(conn, "Client connection");
/* 197 */     Args.notNull(context, "HTTP context");
/*     */     
/* 199 */     HttpResponse response = null;
/*     */     
/* 201 */     context.setAttribute("http.connection", conn);
/* 202 */     context.setAttribute("http.request_sent", Boolean.FALSE);
/*     */     
/* 204 */     conn.sendRequestHeader(request);
/* 205 */     if (request instanceof HttpEntityEnclosingRequest) {
/*     */ 
/*     */ 
/*     */       
/* 209 */       boolean sendentity = true;
/* 210 */       ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
/*     */       
/* 212 */       if (((HttpEntityEnclosingRequest)request).expectContinue() && !ver.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/*     */ 
/*     */         
/* 215 */         conn.flush();
/*     */ 
/*     */         
/* 218 */         if (conn.isResponseAvailable(this.waitForContinue)) {
/* 219 */           response = conn.receiveResponseHeader();
/* 220 */           if (canResponseHaveBody(request, response)) {
/* 221 */             conn.receiveResponseEntity(response);
/*     */           }
/* 223 */           int status = response.getStatusLine().getStatusCode();
/* 224 */           if (status < 200) {
/* 225 */             if (status != 100) {
/* 226 */               throw new ProtocolException("Unexpected response: " + response.getStatusLine());
/*     */             }
/*     */ 
/*     */             
/* 230 */             response = null;
/*     */           } else {
/* 232 */             sendentity = false;
/*     */           } 
/*     */         } 
/*     */       } 
/* 236 */       if (sendentity) {
/* 237 */         conn.sendRequestEntity((HttpEntityEnclosingRequest)request);
/*     */       }
/*     */     } 
/* 240 */     conn.flush();
/* 241 */     context.setAttribute("http.request_sent", Boolean.TRUE);
/* 242 */     return response;
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
/*     */   protected HttpResponse doReceiveResponse(HttpRequest request, HttpClientConnection conn, HttpContext context) throws HttpException, IOException {
/* 264 */     Args.notNull(request, "HTTP request");
/* 265 */     Args.notNull(conn, "Client connection");
/* 266 */     Args.notNull(context, "HTTP context");
/* 267 */     HttpResponse response = null;
/* 268 */     int statusCode = 0;
/*     */     
/* 270 */     while (response == null || statusCode < 200) {
/*     */       
/* 272 */       response = conn.receiveResponseHeader();
/* 273 */       if (canResponseHaveBody(request, response)) {
/* 274 */         conn.receiveResponseEntity(response);
/*     */       }
/* 276 */       statusCode = response.getStatusLine().getStatusCode();
/*     */     } 
/*     */ 
/*     */     
/* 280 */     return response;
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
/*     */   public void postProcess(HttpResponse response, HttpProcessor processor, HttpContext context) throws HttpException, IOException {
/* 305 */     Args.notNull(response, "HTTP response");
/* 306 */     Args.notNull(processor, "HTTP processor");
/* 307 */     Args.notNull(context, "HTTP context");
/* 308 */     context.setAttribute("http.response", response);
/* 309 */     processor.process(response, context);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\HttpRequestExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */