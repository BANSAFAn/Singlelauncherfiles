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
/*    */ 
/*    */ @Deprecated
/*    */ @Immutable
/*    */ public class JdkIdn
/*    */   implements Idn
/*    */ {
/*    */   private final Method toUnicode;
/*    */   
/*    */   public JdkIdn() throws ClassNotFoundException {
/* 51 */     Class<?> clazz = Class.forName("java.net.IDN");
/*    */     try {
/* 53 */       this.toUnicode = clazz.getMethod("toUnicode", new Class[] { String.class });
/* 54 */     } catch (SecurityException e) {
/*    */       
/* 56 */       throw new IllegalStateException(e.getMessage(), e);
/* 57 */     } catch (NoSuchMethodException e) {
/*    */       
/* 59 */       throw new IllegalStateException(e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toUnicode(String punycode) {
/*    */     try {
/* 66 */       return (String)this.toUnicode.invoke(null, new Object[] { punycode });
/* 67 */     } catch (IllegalAccessException e) {
/* 68 */       throw new IllegalStateException(e.getMessage(), e);
/* 69 */     } catch (InvocationTargetException e) {
/* 70 */       Throwable t = e.getCause();
/* 71 */       throw new RuntimeException(t.getMessage(), t);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\clien\\utils\JdkIdn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */