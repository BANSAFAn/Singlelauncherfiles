/*     */ package by.gdev.http.download.impl;
/*     */ 
/*     */ import by.gdev.http.download.model.Headers;
/*     */ import by.gdev.http.download.model.RequestMetadata;
/*     */ import by.gdev.http.download.service.HttpService;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.SocketTimeoutException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.CloseableHttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpHead;
/*     */ import org.apache.http.client.methods.HttpRequestBase;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ 
/*     */ public class HttpServiceImpl implements HttpService {
/*     */   private String proxy;
/*     */   private CloseableHttpClient httpclient;
/*     */   
/*     */   public HttpServiceImpl(String proxy, CloseableHttpClient httpclient, RequestConfig requestConfig, int maxAttepmts) {
/*  36 */     this.proxy = proxy; this.httpclient = httpclient; this.requestConfig = requestConfig; this.maxAttepmts = maxAttepmts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RequestConfig requestConfig;
/*     */ 
/*     */ 
/*     */   
/*     */   private int maxAttepmts;
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestMetadata getRequestByUrlAndSave(String url, Path path) throws IOException {
/*  51 */     RequestMetadata request = null;
/*  52 */     for (int attepmts = 0; attepmts < this.maxAttepmts; attepmts++) {
/*     */       try {
/*  54 */         request = getResourseByUrl(url, path);
/*     */         break;
/*  56 */       } catch (SocketTimeoutException e1) {
/*  57 */         attepmts++;
/*  58 */         if (attepmts == this.maxAttepmts)
/*  59 */           throw new SocketTimeoutException(); 
/*  60 */       } catch (IOException e) {
/*  61 */         if (Objects.nonNull(this.proxy)) {
/*  62 */           request = getResourseByUrl(this.proxy + url, path);
/*     */         } else {
/*  64 */           throw e;
/*     */         } 
/*     */       } 
/*  67 */     }  return request;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestMetadata getMetaByUrl(String url) throws IOException {
/*  77 */     RequestMetadata request = null;
/*  78 */     for (int attepmts = 0; attepmts < this.maxAttepmts; attepmts++) {
/*     */       try {
/*  80 */         request = getMetadata(url);
/*     */         break;
/*  82 */       } catch (SocketTimeoutException e) {
/*  83 */         attepmts++;
/*  84 */         if (attepmts == this.maxAttepmts)
/*  85 */           throw new SocketTimeoutException(); 
/*     */       } 
/*     */     } 
/*  88 */     return request;
/*     */   }
/*     */   
/*     */   public String getRequestByUrl(String url) throws IOException {
/*  92 */     return getRequestByUrl(url, null);
/*     */   }
/*     */   
/*     */   private String getStringByUrl(String url, Map<String, String> headers) throws IOException {
/*  96 */     InputStream in = null;
/*  97 */     HttpGet httpGet = null;
/*     */     try {
/*  99 */       httpGet = new HttpGet(url);
/* 100 */       if (Objects.nonNull(headers))
/* 101 */         for (Map.Entry<String, String> e : headers.entrySet()) {
/* 102 */           httpGet.addHeader(e.getKey(), e.getValue());
/*     */         } 
/* 104 */       CloseableHttpResponse response = getResponse((HttpRequestBase)httpGet);
/* 105 */       StatusLine st = response.getStatusLine();
/* 106 */       if (200 == response.getStatusLine().getStatusCode()) {
/* 107 */         in = response.getEntity().getContent();
/* 108 */         return IOUtils.toString(in, StandardCharsets.UTF_8);
/*     */       } 
/* 110 */       throw new IOException(
/* 111 */           String.format("code %s phrase %s %s", new Object[] { Integer.valueOf(st.getStatusCode()), st.getReasonPhrase(), url }));
/*     */     } finally {
/*     */       
/* 114 */       httpGet.abort();
/* 115 */       IOUtils.closeQuietly(in);
/*     */     } 
/*     */   }
/*     */   
/*     */   private RequestMetadata getMetadata(String url) throws IOException {
/* 120 */     RequestMetadata request = new RequestMetadata();
/* 121 */     HttpHead httpUrl = new HttpHead(url);
/* 122 */     CloseableHttpResponse response = getResponse((HttpRequestBase)httpUrl);
/* 123 */     if (response.containsHeader(Headers.ETAG.getValue())) {
/* 124 */       request.setETag(response.getFirstHeader(Headers.ETAG.getValue()).getValue().replaceAll("\"", ""));
/*     */     } else {
/* 126 */       request.setETag(null);
/* 127 */     }  if (response.containsHeader(Headers.LASTMODIFIED.getValue())) {
/* 128 */       request.setLastModified(response
/* 129 */           .getFirstHeader(Headers.LASTMODIFIED.getValue()).getValue().replaceAll("\"", ""));
/*     */     } else {
/* 131 */       request.setLastModified(null);
/* 132 */     }  if (response.containsHeader(Headers.CONTENTLENGTH.getValue())) {
/* 133 */       request.setContentLength(response.getFirstHeader(Headers.CONTENTLENGTH.getValue()).getValue());
/*     */     } else {
/* 135 */       request.setContentLength(null);
/* 136 */     }  return request;
/*     */   }
/*     */   
/*     */   private RequestMetadata getResourseByUrl(String url, Path path) throws IOException, SocketTimeoutException {
/* 140 */     HttpGet httpGet = new HttpGet(url);
/* 141 */     BufferedInputStream in = null;
/* 142 */     BufferedOutputStream out = null;
/* 143 */     if (!path.toFile().getParentFile().exists())
/* 144 */       path.toFile().getParentFile().mkdirs(); 
/* 145 */     Path temp = Paths.get(path.toAbsolutePath().toString() + ".temp", new String[0]);
/*     */     try {
/* 147 */       CloseableHttpResponse response = getResponse((HttpRequestBase)httpGet);
/* 148 */       StatusLine st = response.getStatusLine();
/* 149 */       HttpEntity entity = response.getEntity();
/* 150 */       if (200 != response.getStatusLine().getStatusCode()) {
/* 151 */         throw new IOException(String.format("code %s phrase %s", new Object[] { Integer.valueOf(st.getStatusCode()), st.getReasonPhrase() }));
/*     */       }
/* 153 */       in = new BufferedInputStream(entity.getContent());
/* 154 */       out = new BufferedOutputStream(new FileOutputStream(temp.toFile()));
/* 155 */       byte[] buffer = new byte[65536];
/* 156 */       int curread = in.read(buffer);
/* 157 */       while (curread != -1) {
/* 158 */         out.write(buffer, 0, curread);
/* 159 */         curread = in.read(buffer);
/*     */       } 
/*     */     } finally {
/* 162 */       httpGet.abort();
/* 163 */       IOUtils.closeQuietly(in);
/* 164 */       IOUtils.closeQuietly(out);
/*     */     } 
/* 166 */     Files.move(Paths.get(temp.toString(), new String[0]), path.toAbsolutePath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/* 167 */     return getMetadata(url);
/*     */   }
/*     */   
/*     */   private CloseableHttpResponse getResponse(HttpRequestBase http) throws IOException {
/* 171 */     http.setConfig(this.requestConfig);
/* 172 */     return this.httpclient.execute((HttpUriRequest)http);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRequestByUrl(String url, Map<String, String> map) throws IOException {
/* 177 */     SocketTimeoutException ste = null;
/* 178 */     for (int attepmts = 0; attepmts < this.maxAttepmts; attepmts++) {
/*     */       try {
/* 180 */         return getStringByUrl(url, map);
/* 181 */       } catch (SocketTimeoutException e) {
/* 182 */         ste = e;
/*     */       } 
/*     */     } 
/* 185 */     throw ste;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\impl\HttpServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */