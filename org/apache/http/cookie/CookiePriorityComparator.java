/*    */ package org.apache.http.cookie;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.Date;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.impl.cookie.BasicClientCookie;
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
/*    */ public class CookiePriorityComparator
/*    */   implements Comparator<Cookie>
/*    */ {
/* 46 */   public static final CookiePriorityComparator INSTANCE = new CookiePriorityComparator();
/*    */   
/*    */   private int getPathLength(Cookie cookie) {
/* 49 */     String path = cookie.getPath();
/* 50 */     return (path != null) ? path.length() : 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(Cookie c1, Cookie c2) {
/* 55 */     int l1 = getPathLength(c1);
/* 56 */     int l2 = getPathLength(c2);
/*    */     
/* 58 */     int result = l2 - l1;
/* 59 */     if (result == 0 && c1 instanceof BasicClientCookie && c2 instanceof BasicClientCookie) {
/* 60 */       Date d1 = ((BasicClientCookie)c1).getCreationDate();
/* 61 */       Date d2 = ((BasicClientCookie)c2).getCreationDate();
/* 62 */       if (d1 != null && d2 != null) {
/* 63 */         return (int)(d1.getTime() - d2.getTime());
/*    */       }
/*    */     } 
/* 66 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\cookie\CookiePriorityComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */