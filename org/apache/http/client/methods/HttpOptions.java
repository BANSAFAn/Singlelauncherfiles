/*    */ package org.apache.http.client.methods;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HeaderElement;
/*    */ import org.apache.http.HeaderIterator;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.util.Args;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @NotThreadSafe
/*    */ public class HttpOptions
/*    */   extends HttpRequestBase
/*    */ {
/*    */   public static final String METHOD_NAME = "OPTIONS";
/*    */   
/*    */   public HttpOptions() {}
/*    */   
/*    */   public HttpOptions(URI uri) {
/* 69 */     setURI(uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpOptions(String uri) {
/* 77 */     setURI(URI.create(uri));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMethod() {
/* 82 */     return "OPTIONS";
/*    */   }
/*    */   
/*    */   public Set<String> getAllowedMethods(HttpResponse response) {
/* 86 */     Args.notNull(response, "HTTP response");
/*    */     
/* 88 */     HeaderIterator it = response.headerIterator("Allow");
/* 89 */     Set<String> methods = new HashSet<String>();
/* 90 */     while (it.hasNext()) {
/* 91 */       Header header = it.nextHeader();
/* 92 */       HeaderElement[] elements = header.getElements();
/* 93 */       for (HeaderElement element : elements) {
/* 94 */         methods.add(element.getName());
/*    */       }
/*    */     } 
/* 97 */     return methods;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\client\methods\HttpOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */