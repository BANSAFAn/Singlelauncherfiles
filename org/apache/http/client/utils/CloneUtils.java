/*    */ package org.apache.http.client.utils;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
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
/*    */ @Immutable
/*    */ public class CloneUtils
/*    */ {
/*    */   public static <T> T cloneObject(T obj) throws CloneNotSupportedException {
/* 46 */     if (obj == null) {
/* 47 */       return null;
/*    */     }
/* 49 */     if (obj instanceof Cloneable) {
/* 50 */       Method m; Class<?> clazz = obj.getClass();
/*    */       
/*    */       try {
/* 53 */         m = clazz.getMethod("clone", (Class[])null);
/* 54 */       } catch (NoSuchMethodException ex) {
/* 55 */         throw new NoSuchMethodError(ex.getMessage());
/*    */       } 
/*    */       
/*    */       try {
/* 59 */         T result = (T)m.invoke(obj, (Object[])null);
/* 60 */         return result;
/* 61 */       } catch (InvocationTargetException ex) {
/* 62 */         Throwable cause = ex.getCause();
/* 63 */         if (cause instanceof CloneNotSupportedException) {
/* 64 */           throw (CloneNotSupportedException)cause;
/*    */         }
/* 66 */         throw new Error("Unexpected exception", cause);
/*    */       }
/* 68 */       catch (IllegalAccessException ex) {
/* 69 */         throw new IllegalAccessError(ex.getMessage());
/*    */       } 
/*    */     } 
/* 72 */     throw new CloneNotSupportedException();
/*    */   }
/*    */ 
/*    */   
/*    */   public static Object clone(Object obj) throws CloneNotSupportedException {
/* 77 */     return cloneObject(obj);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\clien\\utils\CloneUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */