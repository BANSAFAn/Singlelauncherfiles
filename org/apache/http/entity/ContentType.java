/*     */ package org.apache.http.entity;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.message.BasicHeaderValueFormatter;
/*     */ import org.apache.http.message.BasicHeaderValueParser;
/*     */ import org.apache.http.message.BasicNameValuePair;
/*     */ import org.apache.http.message.ParserCursor;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ import org.apache.http.util.TextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ContentType
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7768694718232371896L;
/*  69 */   public static final ContentType APPLICATION_ATOM_XML = create("application/atom+xml", Consts.ISO_8859_1);
/*     */   
/*  71 */   public static final ContentType APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", Consts.ISO_8859_1);
/*     */   
/*  73 */   public static final ContentType APPLICATION_JSON = create("application/json", Consts.UTF_8);
/*     */   
/*  75 */   public static final ContentType APPLICATION_OCTET_STREAM = create("application/octet-stream", (Charset)null);
/*     */   
/*  77 */   public static final ContentType APPLICATION_SVG_XML = create("application/svg+xml", Consts.ISO_8859_1);
/*     */   
/*  79 */   public static final ContentType APPLICATION_XHTML_XML = create("application/xhtml+xml", Consts.ISO_8859_1);
/*     */   
/*  81 */   public static final ContentType APPLICATION_XML = create("application/xml", Consts.ISO_8859_1);
/*     */   
/*  83 */   public static final ContentType MULTIPART_FORM_DATA = create("multipart/form-data", Consts.ISO_8859_1);
/*     */   
/*  85 */   public static final ContentType TEXT_HTML = create("text/html", Consts.ISO_8859_1);
/*     */   
/*  87 */   public static final ContentType TEXT_PLAIN = create("text/plain", Consts.ISO_8859_1);
/*     */   
/*  89 */   public static final ContentType TEXT_XML = create("text/xml", Consts.ISO_8859_1);
/*     */   
/*  91 */   public static final ContentType WILDCARD = create("*/*", (Charset)null);
/*     */ 
/*     */ 
/*     */   
/*  95 */   public static final ContentType DEFAULT_TEXT = TEXT_PLAIN;
/*  96 */   public static final ContentType DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
/*     */   
/*     */   private final String mimeType;
/*     */   
/*     */   private final Charset charset;
/*     */   
/*     */   private final NameValuePair[] params;
/*     */   
/*     */   ContentType(String mimeType, Charset charset) {
/* 105 */     this.mimeType = mimeType;
/* 106 */     this.charset = charset;
/* 107 */     this.params = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ContentType(String mimeType, Charset charset, NameValuePair[] params) {
/* 114 */     this.mimeType = mimeType;
/* 115 */     this.charset = charset;
/* 116 */     this.params = params;
/*     */   }
/*     */   
/*     */   public String getMimeType() {
/* 120 */     return this.mimeType;
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/* 124 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/* 131 */     Args.notEmpty(name, "Parameter name");
/* 132 */     if (this.params == null) {
/* 133 */       return null;
/*     */     }
/* 135 */     for (NameValuePair param : this.params) {
/* 136 */       if (param.getName().equalsIgnoreCase(name)) {
/* 137 */         return param.getValue();
/*     */       }
/*     */     } 
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 149 */     CharArrayBuffer buf = new CharArrayBuffer(64);
/* 150 */     buf.append(this.mimeType);
/* 151 */     if (this.params != null) {
/* 152 */       buf.append("; ");
/* 153 */       BasicHeaderValueFormatter.INSTANCE.formatParameters(buf, this.params, false);
/* 154 */     } else if (this.charset != null) {
/* 155 */       buf.append("; charset=");
/* 156 */       buf.append(this.charset.name());
/*     */     } 
/* 158 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static boolean valid(String s) {
/* 162 */     for (int i = 0; i < s.length(); i++) {
/* 163 */       char ch = s.charAt(i);
/* 164 */       if (ch == '"' || ch == ',' || ch == ';') {
/* 165 */         return false;
/*     */       }
/*     */     } 
/* 168 */     return true;
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
/*     */   public static ContentType create(String mimeType, Charset charset) {
/* 180 */     String type = ((String)Args.notBlank(mimeType, "MIME type")).toLowerCase(Locale.ROOT);
/* 181 */     Args.check(valid(type), "MIME type may not contain reserved characters");
/* 182 */     return new ContentType(type, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentType create(String mimeType) {
/* 193 */     return new ContentType(mimeType, (Charset)null);
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
/*     */   public static ContentType create(String mimeType, String charset) throws UnsupportedCharsetException {
/* 209 */     return create(mimeType, !TextUtils.isBlank(charset) ? Charset.forName(charset) : null);
/*     */   }
/*     */   
/*     */   private static ContentType create(HeaderElement helem, boolean strict) {
/* 213 */     return create(helem.getName(), helem.getParameters(), strict);
/*     */   }
/*     */   
/*     */   private static ContentType create(String mimeType, NameValuePair[] params, boolean strict) {
/* 217 */     Charset charset = null;
/* 218 */     for (NameValuePair param : params) {
/* 219 */       if (param.getName().equalsIgnoreCase("charset")) {
/* 220 */         String s = param.getValue();
/* 221 */         if (!TextUtils.isBlank(s)) {
/*     */           try {
/* 223 */             charset = Charset.forName(s);
/* 224 */           } catch (UnsupportedCharsetException ex) {
/* 225 */             if (strict) {
/* 226 */               throw ex;
/*     */             }
/*     */           } 
/*     */         }
/*     */         break;
/*     */       } 
/*     */     } 
/* 233 */     return new ContentType(mimeType, charset, (params != null && params.length > 0) ? params : null);
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
/*     */   public static ContentType create(String mimeType, NameValuePair... params) throws UnsupportedCharsetException {
/* 248 */     String type = ((String)Args.notBlank(mimeType, "MIME type")).toLowerCase(Locale.ROOT);
/* 249 */     Args.check(valid(type), "MIME type may not contain reserved characters");
/* 250 */     return create(mimeType, params, true);
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
/*     */   public static ContentType parse(String s) throws ParseException, UnsupportedCharsetException {
/* 265 */     Args.notNull(s, "Content type");
/* 266 */     CharArrayBuffer buf = new CharArrayBuffer(s.length());
/* 267 */     buf.append(s);
/* 268 */     ParserCursor cursor = new ParserCursor(0, s.length());
/* 269 */     HeaderElement[] elements = BasicHeaderValueParser.INSTANCE.parseElements(buf, cursor);
/* 270 */     if (elements.length > 0) {
/* 271 */       return create(elements[0], true);
/*     */     }
/* 273 */     throw new ParseException("Invalid content type: " + s);
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
/*     */   public static ContentType get(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
/* 291 */     if (entity == null) {
/* 292 */       return null;
/*     */     }
/* 294 */     Header header = entity.getContentType();
/* 295 */     if (header != null) {
/* 296 */       HeaderElement[] elements = header.getElements();
/* 297 */       if (elements.length > 0) {
/* 298 */         return create(elements[0], true);
/*     */       }
/*     */     } 
/* 301 */     return null;
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
/*     */   public static ContentType getLenient(HttpEntity entity) {
/* 315 */     if (entity == null) {
/* 316 */       return null;
/*     */     }
/* 318 */     Header header = entity.getContentType();
/* 319 */     if (header != null) {
/*     */       try {
/* 321 */         HeaderElement[] elements = header.getElements();
/* 322 */         if (elements.length > 0) {
/* 323 */           return create(elements[0], false);
/*     */         }
/* 325 */       } catch (ParseException ex) {
/* 326 */         return null;
/*     */       } 
/*     */     }
/* 329 */     return null;
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
/*     */   public static ContentType getOrDefault(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
/* 345 */     ContentType contentType = get(entity);
/* 346 */     return (contentType != null) ? contentType : DEFAULT_TEXT;
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
/*     */   public static ContentType getLenientOrDefault(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
/* 360 */     ContentType contentType = get(entity);
/* 361 */     return (contentType != null) ? contentType : DEFAULT_TEXT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentType withCharset(Charset charset) {
/* 372 */     return create(getMimeType(), charset);
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
/*     */   public ContentType withCharset(String charset) {
/* 385 */     return create(getMimeType(), charset);
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
/*     */   public ContentType withParameters(NameValuePair... params) throws UnsupportedCharsetException {
/* 397 */     if (params.length == 0) {
/* 398 */       return this;
/*     */     }
/* 400 */     Map<String, String> paramMap = new LinkedHashMap<String, String>();
/* 401 */     if (this.params != null) {
/* 402 */       for (NameValuePair param : this.params) {
/* 403 */         paramMap.put(param.getName(), param.getValue());
/*     */       }
/*     */     }
/* 406 */     for (NameValuePair param : params) {
/* 407 */       paramMap.put(param.getName(), param.getValue());
/*     */     }
/* 409 */     List<NameValuePair> newParams = new ArrayList<NameValuePair>(paramMap.size() + 1);
/* 410 */     if (this.charset != null && !paramMap.containsKey("charset")) {
/* 411 */       newParams.add(new BasicNameValuePair("charset", this.charset.name()));
/*     */     }
/* 413 */     for (Map.Entry<String, String> entry : paramMap.entrySet()) {
/* 414 */       newParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
/*     */     }
/* 416 */     return create(getMimeType(), newParams.<NameValuePair>toArray(new NameValuePair[newParams.size()]), true);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\entity\ContentType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */