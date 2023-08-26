/*    */ package by.gdev.util;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StringVersionComparator
/*    */   implements Comparator<String>
/*    */ {
/*    */   public int compare(String o1, String o2) {
/* 13 */     String[] versions1 = o1.split("\\.");
/* 14 */     String[] versions2 = o2.split("\\.");
/*    */     
/* 16 */     int length = Math.min(versions1.length, versions2.length);
/* 17 */     for (int i = 0; i < length; i++) {
/* 18 */       int res = Integer.valueOf(versions1[i]).compareTo(Integer.valueOf(versions2[i]));
/* 19 */       if (res != 0)
/* 20 */         return res; 
/*    */     } 
/* 22 */     if (versions1.length != versions2.length) {
/* 23 */       if (length == versions1.length)
/* 24 */         return -1; 
/* 25 */       return 1;
/*    */     } 
/* 27 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\StringVersionComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */