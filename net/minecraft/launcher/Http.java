/*     */ package net.minecraft.launcher;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Socket;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import javax.net.ssl.SSLException;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.tlauncher.tlauncher.entity.ServerCommandEntity;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ public class Http
/*     */ {
/*     */   public static final String JSON_CONTENT_TYPE = "application/json";
/*     */   public static final String TEXT_PLAIN = "text/plain";
/*     */   public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
/*     */   
/*     */   public static String get(String url, Map<String, Object> query) {
/*  38 */     String line = buildQuery(query);
/*  39 */     if (Strings.isNullOrEmpty(line)) {
/*  40 */       return url;
/*     */     }
/*  42 */     return url + "?" + line;
/*     */   }
/*     */   
/*     */   public static String get(String url, String key, String value) {
/*  46 */     Map<String, Object> map = new HashMap<>();
/*  47 */     map.put(key, value);
/*  48 */     String line = buildQuery(map);
/*  49 */     if (Strings.isNullOrEmpty(line)) {
/*  50 */       return url;
/*     */     }
/*  52 */     return url + "?" + line;
/*     */   }
/*     */   
/*     */   public static String performPost(URL url, Map<String, Object> query) throws IOException {
/*  56 */     return performPost(url, buildQuery(query), "application/x-www-form-urlencoded");
/*     */   }
/*     */ 
/*     */   
/*     */   public static String performGet(URL url, int connTimeout, int readTimeout) throws IOException {
/*     */     try {
/*  62 */       HttpURLConnection connection = (HttpURLConnection)url.openConnection(U.getProxy());
/*     */       
/*  64 */       connection.setConnectTimeout(connTimeout);
/*  65 */       connection.setReadTimeout(readTimeout);
/*  66 */       connection.setRequestMethod("GET");
/*  67 */       connection.setInstanceFollowRedirects(true);
/*  68 */       String res = "";
/*     */       try {
/*  70 */         if (connection.getContentType() != null && connection
/*  71 */           .getContentType().equalsIgnoreCase("application/zip")) {
/*  72 */           res = readZip(connection);
/*     */         }
/*     */         else {
/*     */           
/*  76 */           BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
/*  77 */           StringBuilder response = new StringBuilder();
/*     */           String line;
/*  79 */           while ((line = reader.readLine()) != null) {
/*  80 */             response.append(line);
/*     */           }
/*  82 */           reader.close();
/*  83 */           res = response.toString();
/*     */         } 
/*     */       } finally {
/*  86 */         if (TLauncher.DEBUG) {
/*  87 */           String shortRes = res.contains("\r") ? res.replaceAll("\r", "") : res;
/*  88 */           if (shortRes.length() > 400) {
/*  89 */             shortRes = shortRes.substring(0, 400);
/*     */           }
/*  91 */           U.debug(new Object[] { "request: " + url + ", responce: " + shortRes });
/*     */         } 
/*     */       } 
/*  94 */       return res;
/*  95 */     } catch (SSLException e) {
/*  96 */       TlauncherUtil.deactivateSSL();
/*  97 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String performGet(String url) throws IOException {
/* 102 */     return performGet(constantURL(url), U.getConnectionTimeout(), U.getReadTimeout());
/*     */   }
/*     */   
/*     */   public static String performGet(URL url) throws IOException {
/* 106 */     return performGet(url, U.getConnectionTimeout(), U.getReadTimeout());
/*     */   }
/*     */ 
/*     */   
/*     */   public static String performGet(String url, Map<String, Object> map, int connTimeout, int readTimeout) throws IOException {
/* 111 */     StringBuilder stringBuilder = new StringBuilder(url);
/* 112 */     stringBuilder.append("?");
/*     */     
/* 114 */     for (Map.Entry<String, Object> e : map.entrySet()) {
/* 115 */       stringBuilder.append(e.getKey()).append("=").append(encode(e.getValue().toString())).append("&");
/*     */     }
/* 117 */     stringBuilder.setLength(stringBuilder.length() - 1);
/* 118 */     return performGet(new URL(stringBuilder.toString()), connTimeout, readTimeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String performPost(URL url, byte[] body, String contentType, boolean gzip) throws IOException {
/* 123 */     HttpURLConnection connection = (HttpURLConnection)url.openConnection(U.getProxy());
/* 124 */     connection.setConnectTimeout(U.getConnectionTimeout());
/* 125 */     connection.setReadTimeout(U.getReadTimeout());
/* 126 */     connection.setRequestMethod("POST");
/* 127 */     connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
/* 128 */     connection.setRequestProperty("Content-Language", "en-US");
/* 129 */     connection.setUseCaches(false);
/* 130 */     connection.setDoInput(true);
/* 131 */     connection.setInstanceFollowRedirects(true);
/* 132 */     connection.setDoOutput(true);
/*     */     
/* 134 */     if (gzip) {
/* 135 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 136 */       GZIPOutputStream g = new GZIPOutputStream(out);
/* 137 */       g.write(body);
/* 138 */       g.close();
/* 139 */       body = out.toByteArray();
/*     */     } 
/* 141 */     connection.setRequestProperty("Content-Length", "" + body.length);
/* 142 */     OutputStream writer = new DataOutputStream(connection.getOutputStream());
/* 143 */     writer.write(body);
/* 144 */     writer.close();
/*     */     
/* 146 */     BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
/*     */     
/* 148 */     StringBuilder response = new StringBuilder();
/*     */     String line;
/* 150 */     while ((line = reader.readLine()) != null) {
/* 151 */       response.append(line);
/* 152 */       response.append('\r');
/*     */     } 
/*     */     
/* 155 */     reader.close();
/* 156 */     return response.toString();
/*     */   }
/*     */   
/*     */   public static String performPost(URL url, String body, String contentType) throws IOException {
/* 160 */     return performPost(url, body.getBytes(StandardCharsets.UTF_8), contentType, false);
/*     */   }
/*     */   
/*     */   public static URL constantURL(String input) {
/*     */     try {
/* 165 */       return new URL(input);
/* 166 */     } catch (MalformedURLException e) {
/* 167 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String encode(String s) {
/*     */     try {
/* 173 */       return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/")
/* 174 */         .replaceAll("%21", "!").replaceAll("%27", "'").replaceAll("%28", "(").replaceAll("%29", ")")
/* 175 */         .replaceAll("%7E", "~");
/* 176 */     } catch (UnsupportedEncodingException e) {
/* 177 */       throw new RuntimeException("UTF-8 is not supported.", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static ServerCommandEntity readRequestInfo(Socket socket) throws IOException {
/* 182 */     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
/* 183 */     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
/*     */     
/* 185 */     ServerCommandEntity res = new ServerCommandEntity();
/* 186 */     String line = in.readLine();
/* 187 */     StringBuilder raw = new StringBuilder();
/* 188 */     raw.append(line);
/* 189 */     String[] array = line.split(" ");
/* 190 */     res.setRequestType(array[0]);
/* 191 */     res.setUrn(array[1]);
/* 192 */     res.setQueries(getQueryMap(res.getUrn()));
/* 193 */     boolean isPost = raw.toString().startsWith("POST");
/* 194 */     int contentLength = 0;
/* 195 */     while (!(line = in.readLine()).equals("")) {
/* 196 */       raw.append('\n').append(line);
/* 197 */       if (isPost) {
/* 198 */         String contentHeader = "Content-Length: ";
/* 199 */         if (line.startsWith("Content-Length: ")) {
/* 200 */           contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
/*     */         }
/*     */       } 
/*     */     } 
/* 204 */     StringBuilder body = new StringBuilder();
/* 205 */     if (isPost)
/*     */     {
/* 207 */       for (int i = 0; i < contentLength; i++) {
/* 208 */         int c = in.read();
/* 209 */         body.append((char)c);
/*     */       } 
/*     */     }
/* 212 */     raw.append(body.toString());
/*     */     
/* 214 */     out.write("HTTP/1.1 200 OK\r\n");
/* 215 */     out.write("Content-Type: text/html\r\n");
/* 216 */     out.write("Access-Control-Allow-Origin: *\r\n");
/* 217 */     out.write("\r\n");
/*     */     
/* 219 */     out.close();
/* 220 */     in.close();
/* 221 */     socket.close();
/* 222 */     res.setBody(body.toString());
/* 223 */     return res;
/*     */   }
/*     */   
/*     */   private static Map<String, String> getQueryMap(String query) {
/* 227 */     String[] params = query.split("\\?");
/* 228 */     if (params.length != 2)
/* 229 */       return new HashMap<>(); 
/* 230 */     params = params[1].split("&");
/* 231 */     Map<String, String> map = new HashMap<>();
/* 232 */     for (String param : params) {
/* 233 */       String name = param.split("=")[0];
/* 234 */       String value = param.split("=")[1];
/* 235 */       map.put(name, value);
/*     */     } 
/* 237 */     return map;
/*     */   }
/*     */   
/*     */   private static String buildQuery(Map<String, Object> query) {
/* 241 */     StringBuilder builder = new StringBuilder();
/* 242 */     for (Map.Entry<String, Object> entry : query.entrySet()) {
/* 243 */       if (builder.length() > 0) {
/* 244 */         builder.append('&');
/*     */       }
/*     */       try {
/* 247 */         builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
/* 248 */       } catch (UnsupportedEncodingException e) {
/* 249 */         U.log(new Object[] { "Unexpected exception building query", e });
/*     */       } 
/*     */       
/* 252 */       if (entry.getValue() != null) {
/* 253 */         builder.append('=');
/*     */         
/*     */         try {
/* 256 */           builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
/* 257 */         } catch (UnsupportedEncodingException e) {
/* 258 */           U.log(new Object[] { e });
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 263 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private static String readZip(HttpURLConnection connection) throws IOException {
/* 267 */     ZipInputStream zip = new ZipInputStream(connection.getInputStream(), StandardCharsets.UTF_8);
/* 268 */     zip.getNextEntry();
/*     */     
/* 270 */     String res = IOUtils.toString(zip);
/* 271 */     zip.closeEntry();
/* 272 */     zip.close();
/* 273 */     return res;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\Http.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */