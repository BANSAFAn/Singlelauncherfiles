/*    */ package org.tlauncher.modpack.domain.client.share;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ForgeStringComparator
/*    */   implements Comparator<String>
/*    */ {
/*    */   public int compare(String o2, String o1) {
/* 15 */     String[] versions1 = o1.split("\\.");
/* 16 */     String[] versions2 = o2.split("\\.");
/*    */     
/* 18 */     int length = Math.min(versions1.length, versions2.length);
/* 19 */     for (int i = 0; i < length; i++) {
/*    */       int res;
/*    */       try {
/* 22 */         res = Integer.valueOf(versions1[i]).compareTo(Integer.valueOf(versions2[i]));
/* 23 */       } catch (NumberFormatException e) {
/* 24 */         res = versions1[i].compareTo(versions2[i]);
/*    */       } 
/* 26 */       if (res != 0)
/* 27 */         return res; 
/*    */     } 
/* 29 */     if (versions1.length != versions2.length) {
/* 30 */       if (length == versions1.length)
/* 31 */         return -1; 
/* 32 */       return 1;
/*    */     } 
/* 34 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\ForgeStringComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */