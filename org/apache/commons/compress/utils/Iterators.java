/*    */ package org.apache.commons.compress.utils;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Objects;
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
/*    */ public class Iterators
/*    */ {
/*    */   public static <T> boolean addAll(Collection<T> collection, Iterator<? extends T> iterator) {
/* 49 */     Objects.requireNonNull(collection);
/* 50 */     Objects.requireNonNull(iterator);
/* 51 */     boolean wasModified = false;
/* 52 */     while (iterator.hasNext()) {
/* 53 */       wasModified |= collection.add(iterator.next());
/*    */     }
/* 55 */     return wasModified;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compres\\utils\Iterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */