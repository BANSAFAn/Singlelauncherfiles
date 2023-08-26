/*    */ package org.apache.commons.io.function;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ @FunctionalInterface
/*    */ public interface IOConsumer<T>
/*    */ {
/*    */   public static final IOConsumer<?> NOOP_IO_CONSUMER = t -> {
/*    */     
/*    */     };
/*    */   
/*    */   static <T> IOConsumer<T> noop() {
/* 47 */     return (IOConsumer)NOOP_IO_CONSUMER;
/*    */   }
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
/*    */   default IOConsumer<T> andThen(IOConsumer<? super T> after) {
/* 69 */     Objects.requireNonNull(after, "after");
/* 70 */     return t -> {
/*    */         accept((T)t);
/*    */         after.accept(t);
/*    */       };
/*    */   }
/*    */   
/*    */   void accept(T paramT) throws IOException;
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\function\IOConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */