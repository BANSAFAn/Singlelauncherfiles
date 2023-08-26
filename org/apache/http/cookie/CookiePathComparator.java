/*    */ package org.apache.http.cookie;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ @Immutable
/*    */ public class CookiePathComparator
/*    */   implements Serializable, Comparator<Cookie>
/*    */ {
/* 53 */   public static final CookiePathComparator INSTANCE = new CookiePathComparator();
/*    */   
/*    */   private static final long serialVersionUID = 7523645369616405818L;
/*    */   
/*    */   private String normalizePath(Cookie cookie) {
/* 58 */     String path = cookie.getPath();
/* 59 */     if (path == null) {
/* 60 */       path = "/";
/*    */     }
/* 62 */     if (!path.endsWith("/")) {
/* 63 */       path = path + '/';
/*    */     }
/* 65 */     return path;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(Cookie c1, Cookie c2) {
/* 70 */     String path1 = normalizePath(c1);
/* 71 */     String path2 = normalizePath(c2);
/* 72 */     if (path1.equals(path2))
/* 73 */       return 0; 
/* 74 */     if (path1.startsWith(path2))
/* 75 */       return -1; 
/* 76 */     if (path2.startsWith(path1)) {
/* 77 */       return 1;
/*    */     }
/*    */     
/* 80 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\cookie\CookiePathComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */