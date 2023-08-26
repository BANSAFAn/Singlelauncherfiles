/*    */ package org.apache.http.config;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
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
/*    */ @NotThreadSafe
/*    */ public final class RegistryBuilder<I>
/*    */ {
/*    */   private final Map<String, I> items;
/*    */   
/*    */   public static <I> RegistryBuilder<I> create() {
/* 48 */     return new RegistryBuilder<I>();
/*    */   }
/*    */ 
/*    */   
/*    */   RegistryBuilder() {
/* 53 */     this.items = new HashMap<String, I>();
/*    */   }
/*    */   
/*    */   public RegistryBuilder<I> register(String id, I item) {
/* 57 */     Args.notEmpty(id, "ID");
/* 58 */     Args.notNull(item, "Item");
/* 59 */     this.items.put(id.toLowerCase(Locale.ROOT), item);
/* 60 */     return this;
/*    */   }
/*    */   
/*    */   public Registry<I> build() {
/* 64 */     return new Registry<I>(this.items);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 69 */     return this.items.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\config\RegistryBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */