/*    */ package com.google.gson.internal;
/*    */ 
/*    */ import java.lang.reflect.Type;
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
/*    */ public final class Primitives
/*    */ {
/*    */   public static boolean isPrimitive(Type type) {
/* 34 */     return (type instanceof Class && ((Class)type).isPrimitive());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isWrapperType(Type type) {
/* 44 */     return (type == Integer.class || type == Float.class || type == Byte.class || type == Double.class || type == Long.class || type == Character.class || type == Boolean.class || type == Short.class || type == Void.class);
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
/*    */   public static <T> Class<T> wrap(Class<T> type) {
/* 66 */     if (type == int.class) return (Class)Integer.class; 
/* 67 */     if (type == float.class) return (Class)Float.class; 
/* 68 */     if (type == byte.class) return (Class)Byte.class; 
/* 69 */     if (type == double.class) return (Class)Double.class; 
/* 70 */     if (type == long.class) return (Class)Long.class; 
/* 71 */     if (type == char.class) return (Class)Character.class; 
/* 72 */     if (type == boolean.class) return (Class)Boolean.class; 
/* 73 */     if (type == short.class) return (Class)Short.class; 
/* 74 */     if (type == void.class) return (Class)Void.class; 
/* 75 */     return type;
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
/*    */   public static <T> Class<T> unwrap(Class<T> type) {
/* 89 */     if (type == Integer.class) return (Class)int.class; 
/* 90 */     if (type == Float.class) return (Class)float.class; 
/* 91 */     if (type == Byte.class) return (Class)byte.class; 
/* 92 */     if (type == Double.class) return (Class)double.class; 
/* 93 */     if (type == Long.class) return (Class)long.class; 
/* 94 */     if (type == Character.class) return (Class)char.class; 
/* 95 */     if (type == Boolean.class) return (Class)boolean.class; 
/* 96 */     if (type == Short.class) return (Class)short.class; 
/* 97 */     if (type == Void.class) return (Class)void.class; 
/* 98 */     return type;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\internal\Primitives.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */