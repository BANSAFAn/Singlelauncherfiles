/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicLineFormatter
/*     */   implements LineFormatter
/*     */ {
/*     */   @Deprecated
/*  63 */   public static final BasicLineFormatter DEFAULT = new BasicLineFormatter();
/*     */   
/*  65 */   public static final BasicLineFormatter INSTANCE = new BasicLineFormatter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CharArrayBuffer initBuffer(CharArrayBuffer charBuffer) {
/*  80 */     CharArrayBuffer buffer = charBuffer;
/*  81 */     if (buffer != null) {
/*  82 */       buffer.clear();
/*     */     } else {
/*  84 */       buffer = new CharArrayBuffer(64);
/*     */     } 
/*  86 */     return buffer;
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
/*     */   public static String formatProtocolVersion(ProtocolVersion version, LineFormatter formatter) {
/* 103 */     return ((formatter != null) ? formatter : INSTANCE).appendProtocolVersion(null, version).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer appendProtocolVersion(CharArrayBuffer buffer, ProtocolVersion version) {
/* 112 */     Args.notNull(version, "Protocol version");
/*     */     
/* 114 */     CharArrayBuffer result = buffer;
/* 115 */     int len = estimateProtocolVersionLen(version);
/* 116 */     if (result == null) {
/* 117 */       result = new CharArrayBuffer(len);
/*     */     } else {
/* 119 */       result.ensureCapacity(len);
/*     */     } 
/*     */     
/* 122 */     result.append(version.getProtocol());
/* 123 */     result.append('/');
/* 124 */     result.append(Integer.toString(version.getMajor()));
/* 125 */     result.append('.');
/* 126 */     result.append(Integer.toString(version.getMinor()));
/*     */     
/* 128 */     return result;
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
/*     */   protected int estimateProtocolVersionLen(ProtocolVersion version) {
/* 142 */     return version.getProtocol().length() + 4;
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
/*     */   public static String formatRequestLine(RequestLine reqline, LineFormatter formatter) {
/* 158 */     return ((formatter != null) ? formatter : INSTANCE).formatRequestLine(null, reqline).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer formatRequestLine(CharArrayBuffer buffer, RequestLine reqline) {
/* 167 */     Args.notNull(reqline, "Request line");
/* 168 */     CharArrayBuffer result = initBuffer(buffer);
/* 169 */     doFormatRequestLine(result, reqline);
/*     */     
/* 171 */     return result;
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
/*     */   protected void doFormatRequestLine(CharArrayBuffer buffer, RequestLine reqline) {
/* 185 */     String method = reqline.getMethod();
/* 186 */     String uri = reqline.getUri();
/*     */ 
/*     */     
/* 189 */     int len = method.length() + 1 + uri.length() + 1 + estimateProtocolVersionLen(reqline.getProtocolVersion());
/*     */     
/* 191 */     buffer.ensureCapacity(len);
/*     */     
/* 193 */     buffer.append(method);
/* 194 */     buffer.append(' ');
/* 195 */     buffer.append(uri);
/* 196 */     buffer.append(' ');
/* 197 */     appendProtocolVersion(buffer, reqline.getProtocolVersion());
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
/*     */   public static String formatStatusLine(StatusLine statline, LineFormatter formatter) {
/* 214 */     return ((formatter != null) ? formatter : INSTANCE).formatStatusLine(null, statline).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer formatStatusLine(CharArrayBuffer buffer, StatusLine statline) {
/* 223 */     Args.notNull(statline, "Status line");
/* 224 */     CharArrayBuffer result = initBuffer(buffer);
/* 225 */     doFormatStatusLine(result, statline);
/*     */     
/* 227 */     return result;
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
/*     */   protected void doFormatStatusLine(CharArrayBuffer buffer, StatusLine statline) {
/* 242 */     int len = estimateProtocolVersionLen(statline.getProtocolVersion()) + 1 + 3 + 1;
/*     */     
/* 244 */     String reason = statline.getReasonPhrase();
/* 245 */     if (reason != null) {
/* 246 */       len += reason.length();
/*     */     }
/* 248 */     buffer.ensureCapacity(len);
/*     */     
/* 250 */     appendProtocolVersion(buffer, statline.getProtocolVersion());
/* 251 */     buffer.append(' ');
/* 252 */     buffer.append(Integer.toString(statline.getStatusCode()));
/* 253 */     buffer.append(' ');
/* 254 */     if (reason != null) {
/* 255 */       buffer.append(reason);
/*     */     }
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
/*     */   public static String formatHeader(Header header, LineFormatter formatter) {
/* 272 */     return ((formatter != null) ? formatter : INSTANCE).formatHeader(null, header).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArrayBuffer formatHeader(CharArrayBuffer buffer, Header header) {
/*     */     CharArrayBuffer result;
/* 281 */     Args.notNull(header, "Header");
/*     */ 
/*     */     
/* 284 */     if (header instanceof FormattedHeader) {
/*     */       
/* 286 */       result = ((FormattedHeader)header).getBuffer();
/*     */     } else {
/* 288 */       result = initBuffer(buffer);
/* 289 */       doFormatHeader(result, header);
/*     */     } 
/* 291 */     return result;
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
/*     */   protected void doFormatHeader(CharArrayBuffer buffer, Header header) {
/* 306 */     String name = header.getName();
/* 307 */     String value = header.getValue();
/*     */     
/* 309 */     int len = name.length() + 2;
/* 310 */     if (value != null) {
/* 311 */       len += value.length();
/*     */     }
/* 313 */     buffer.ensureCapacity(len);
/*     */     
/* 315 */     buffer.append(name);
/* 316 */     buffer.append(": ");
/* 317 */     if (value != null)
/* 318 */       buffer.append(value); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicLineFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */