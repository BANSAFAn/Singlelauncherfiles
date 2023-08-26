/*     */ package org.apache.http.impl.client;
/*     */ 
/*     */ import org.apache.http.HttpRequestInterceptor;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.HttpVersion;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.client.protocol.RequestAddCookies;
/*     */ import org.apache.http.client.protocol.RequestAuthCache;
/*     */ import org.apache.http.client.protocol.RequestClientConnControl;
/*     */ import org.apache.http.client.protocol.RequestDefaultHeaders;
/*     */ import org.apache.http.client.protocol.RequestProxyAuthentication;
/*     */ import org.apache.http.client.protocol.RequestTargetAuthentication;
/*     */ import org.apache.http.client.protocol.ResponseProcessCookies;
/*     */ import org.apache.http.conn.ClientConnectionManager;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
/*     */ import org.apache.http.params.SyncBasicHttpParams;
/*     */ import org.apache.http.protocol.BasicHttpProcessor;
/*     */ import org.apache.http.protocol.HTTP;
/*     */ import org.apache.http.protocol.RequestContent;
/*     */ import org.apache.http.protocol.RequestExpectContinue;
/*     */ import org.apache.http.protocol.RequestTargetHost;
/*     */ import org.apache.http.protocol.RequestUserAgent;
/*     */ import org.apache.http.util.VersionInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @ThreadSafe
/*     */ public class DefaultHttpClient
/*     */   extends AbstractHttpClient
/*     */ {
/*     */   public DefaultHttpClient(ClientConnectionManager conman, HttpParams params) {
/* 128 */     super(conman, params);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpClient(ClientConnectionManager conman) {
/* 137 */     super(conman, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttpClient(HttpParams params) {
/* 142 */     super(null, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultHttpClient() {
/* 147 */     super(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpParams createHttpParams() {
/* 158 */     SyncBasicHttpParams syncBasicHttpParams = new SyncBasicHttpParams();
/* 159 */     setDefaultHttpParams((HttpParams)syncBasicHttpParams);
/* 160 */     return (HttpParams)syncBasicHttpParams;
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
/*     */   public static void setDefaultHttpParams(HttpParams params) {
/* 180 */     HttpProtocolParams.setVersion(params, (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 181 */     HttpProtocolParams.setContentCharset(params, HTTP.DEF_CONTENT_CHARSET.name());
/* 182 */     HttpConnectionParams.setTcpNoDelay(params, true);
/* 183 */     HttpConnectionParams.setSocketBufferSize(params, 8192);
/* 184 */     HttpProtocolParams.setUserAgent(params, VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", DefaultHttpClient.class));
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
/*     */   protected BasicHttpProcessor createHttpProcessor() {
/* 208 */     BasicHttpProcessor httpproc = new BasicHttpProcessor();
/* 209 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestDefaultHeaders());
/*     */     
/* 211 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestContent());
/* 212 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestTargetHost());
/*     */     
/* 214 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestClientConnControl());
/* 215 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestUserAgent());
/* 216 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestExpectContinue());
/*     */     
/* 218 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestAddCookies());
/* 219 */     httpproc.addInterceptor((HttpResponseInterceptor)new ResponseProcessCookies());
/*     */     
/* 221 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestAuthCache());
/* 222 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestTargetAuthentication());
/* 223 */     httpproc.addInterceptor((HttpRequestInterceptor)new RequestProxyAuthentication());
/* 224 */     return httpproc;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\client\DefaultHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */