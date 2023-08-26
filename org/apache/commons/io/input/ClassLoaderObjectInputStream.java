/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectStreamClass;
/*    */ import java.io.StreamCorruptedException;
/*    */ import java.lang.reflect.Proxy;
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
/*    */ public class ClassLoaderObjectInputStream
/*    */   extends ObjectInputStream
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public ClassLoaderObjectInputStream(ClassLoader classLoader, InputStream inputStream) throws IOException, StreamCorruptedException {
/* 51 */     super(inputStream);
/* 52 */     this.classLoader = classLoader;
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
/*    */   protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
/*    */     try {
/* 69 */       return Class.forName(objectStreamClass.getName(), false, this.classLoader);
/* 70 */     } catch (ClassNotFoundException cnfe) {
/*    */       
/* 72 */       return super.resolveClass(objectStreamClass);
/*    */     } 
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
/*    */   protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
/* 90 */     Class<?>[] interfaceClasses = new Class[interfaces.length];
/* 91 */     for (int i = 0; i < interfaces.length; i++) {
/* 92 */       interfaceClasses[i] = Class.forName(interfaces[i], false, this.classLoader);
/*    */     }
/*    */     try {
/* 95 */       return Proxy.getProxyClass(this.classLoader, interfaceClasses);
/* 96 */     } catch (IllegalArgumentException e) {
/* 97 */       return super.resolveProxyClass(interfaces);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\ClassLoaderObjectInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */