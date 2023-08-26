/*    */ package org.apache.commons.io;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.util.Iterator;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
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
/*    */ class StreamIterator<E>
/*    */   implements Iterator<E>, Closeable
/*    */ {
/*    */   private final Iterator<E> iterator;
/*    */   private final Stream<E> stream;
/*    */   
/*    */   public static <T> Iterator<T> iterator(Stream<T> stream) {
/* 49 */     return (Iterator)(new StreamIterator(stream)).iterator;
/*    */   }
/*    */   
/*    */   private StreamIterator(Stream<E> stream) {
/* 53 */     this.stream = Objects.<Stream<E>>requireNonNull(stream, "stream");
/* 54 */     this.iterator = stream.iterator();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 62 */     boolean hasNext = this.iterator.hasNext();
/* 63 */     if (!hasNext) {
/* 64 */       close();
/*    */     }
/* 66 */     return hasNext;
/*    */   }
/*    */ 
/*    */   
/*    */   public E next() {
/* 71 */     E next = this.iterator.next();
/* 72 */     if (next == null) {
/* 73 */       close();
/*    */     }
/* 75 */     return next;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {
/* 83 */     this.stream.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\StreamIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */