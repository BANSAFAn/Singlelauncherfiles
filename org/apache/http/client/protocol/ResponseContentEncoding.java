/*     */ package org.apache.http.client.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Locale;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseInterceptor;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.entity.DecompressingEntity;
/*     */ import org.apache.http.client.entity.DeflateInputStream;
/*     */ import org.apache.http.client.entity.InputStreamFactory;
/*     */ import org.apache.http.config.Lookup;
/*     */ import org.apache.http.config.RegistryBuilder;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class ResponseContentEncoding
/*     */   implements HttpResponseInterceptor
/*     */ {
/*     */   public static final String UNCOMPRESSED = "http.client.response.uncompressed";
/*     */   
/*  63 */   private static final InputStreamFactory GZIP = new InputStreamFactory()
/*     */     {
/*     */       public InputStream create(InputStream instream) throws IOException
/*     */       {
/*  67 */         return new GZIPInputStream(instream);
/*     */       }
/*     */     };
/*     */   
/*  71 */   private static final InputStreamFactory DEFLATE = new InputStreamFactory()
/*     */     {
/*     */       public InputStream create(InputStream instream) throws IOException
/*     */       {
/*  75 */         return (InputStream)new DeflateInputStream(instream);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   private final Lookup<InputStreamFactory> decoderRegistry;
/*     */   
/*     */   private final boolean ignoreUnknown;
/*     */ 
/*     */   
/*     */   public ResponseContentEncoding(Lookup<InputStreamFactory> decoderRegistry, boolean ignoreUnknown) {
/*  87 */     this.decoderRegistry = (decoderRegistry != null) ? decoderRegistry : (Lookup<InputStreamFactory>)RegistryBuilder.create().register("gzip", GZIP).register("x-gzip", GZIP).register("deflate", DEFLATE).build();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     this.ignoreUnknown = ignoreUnknown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseContentEncoding(boolean ignoreUnknown) {
/* 100 */     this(null, ignoreUnknown);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResponseContentEncoding(Lookup<InputStreamFactory> decoderRegistry) {
/* 107 */     this(decoderRegistry, true);
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
/*     */   public ResponseContentEncoding() {
/* 119 */     this((Lookup<InputStreamFactory>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 126 */     HttpEntity entity = response.getEntity();
/*     */     
/* 128 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 129 */     RequestConfig requestConfig = clientContext.getRequestConfig();
/*     */ 
/*     */     
/* 132 */     if (requestConfig.isContentCompressionEnabled() && entity != null && entity.getContentLength() != 0L) {
/* 133 */       Header ceheader = entity.getContentEncoding();
/* 134 */       if (ceheader != null) {
/* 135 */         HeaderElement[] codecs = ceheader.getElements();
/* 136 */         for (HeaderElement codec : codecs) {
/* 137 */           String codecname = codec.getName().toLowerCase(Locale.ROOT);
/* 138 */           InputStreamFactory decoderFactory = (InputStreamFactory)this.decoderRegistry.lookup(codecname);
/* 139 */           if (decoderFactory != null) {
/* 140 */             response.setEntity((HttpEntity)new DecompressingEntity(response.getEntity(), decoderFactory));
/* 141 */             response.removeHeaders("Content-Length");
/* 142 */             response.removeHeaders("Content-Encoding");
/* 143 */             response.removeHeaders("Content-MD5");
/*     */           }
/* 145 */           else if (!"identity".equals(codecname) && !this.ignoreUnknown) {
/* 146 */             throw new HttpException("Unsupported Content-Encoding: " + codec.getName());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\protocol\ResponseContentEncoding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */