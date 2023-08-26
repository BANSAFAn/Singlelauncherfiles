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
/*    */ @Immutable
/*    */ public class CookieIdentityComparator
/*    */   implements Serializable, Comparator<Cookie>
/*    */ {
/*    */   private static final long serialVersionUID = 4466565437490631532L;
/*    */   
/*    */   public int compare(Cookie c1, Cookie c2) {
/* 50 */     int res = c1.getName().compareTo(c2.getName());
/* 51 */     if (res == 0) {
/*    */       
/* 53 */       String d1 = c1.getDomain();
/* 54 */       if (d1 == null) {
/* 55 */         d1 = "";
/* 56 */       } else if (d1.indexOf('.') == -1) {
/* 57 */         d1 = d1 + ".local";
/*    */       } 
/* 59 */       String d2 = c2.getDomain();
/* 60 */       if (d2 == null) {
/* 61 */         d2 = "";
/* 62 */       } else if (d2.indexOf('.') == -1) {
/* 63 */         d2 = d2 + ".local";
/*    */       } 
/* 65 */       res = d1.compareToIgnoreCase(d2);
/*    */     } 
/* 67 */     if (res == 0) {
/* 68 */       String p1 = c1.getPath();
/* 69 */       if (p1 == null) {
/* 70 */         p1 = "/";
/*    */       }
/* 72 */       String p2 = c2.getPath();
/* 73 */       if (p2 == null) {
/* 74 */         p2 = "/";
/*    */       }
/* 76 */       res = p1.compareTo(p2);
/*    */     } 
/* 78 */     return res;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\cookie\CookieIdentityComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */