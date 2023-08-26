/*     */ package org.apache.commons.io.function;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface IOFunction<T, R>
/*     */ {
/*     */   default <V> IOFunction<V, R> compose(IOFunction<? super V, ? extends T> before) {
/*  62 */     Objects.requireNonNull(before, "before");
/*  63 */     return v -> apply(before.apply(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <V> IOFunction<V, R> compose(Function<? super V, ? extends T> before) {
/*  82 */     Objects.requireNonNull(before, "before");
/*  83 */     return v -> apply(before.apply(v));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default IOSupplier<R> compose(IOSupplier<? extends T> before) {
/* 100 */     Objects.requireNonNull(before, "before");
/* 101 */     return () -> apply(before.get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default IOSupplier<R> compose(Supplier<? extends T> before) {
/* 118 */     Objects.requireNonNull(before, "before");
/* 119 */     return () -> apply(before.get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <V> IOFunction<T, V> andThen(IOFunction<? super R, ? extends V> after) {
/* 138 */     Objects.requireNonNull(after, "after");
/* 139 */     return t -> after.apply(apply((T)t));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <V> IOFunction<T, V> andThen(Function<? super R, ? extends V> after) {
/* 158 */     Objects.requireNonNull(after, "after");
/* 159 */     return t -> after.apply(apply((T)t));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default IOConsumer<T> andThen(IOConsumer<? super R> after) {
/* 176 */     Objects.requireNonNull(after, "after");
/* 177 */     return t -> after.accept(apply((T)t));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default IOConsumer<T> andThen(Consumer<? super R> after) {
/* 194 */     Objects.requireNonNull(after, "after");
/* 195 */     return t -> after.accept(apply((T)t));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> IOFunction<T, T> identity() {
/* 205 */     return t -> t;
/*     */   }
/*     */   
/*     */   R apply(T paramT) throws IOException;
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\function\IOFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */