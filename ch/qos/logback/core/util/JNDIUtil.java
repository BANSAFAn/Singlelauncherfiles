/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import javax.naming.Context;
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.NamingException;
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
/*    */ public class JNDIUtil
/*    */ {
/*    */   static final String RESTRICTION_MSG = "JNDI name must start with java: but was ";
/*    */   
/*    */   public static Context getInitialContext() throws NamingException {
/* 36 */     return new InitialContext();
/*    */   }
/*    */   
/*    */   public static String lookup(Context ctx, String name) throws NamingException {
/* 40 */     if (ctx == null) {
/* 41 */       return null;
/*    */     }
/*    */     
/* 44 */     if (OptionHelper.isEmpty(name)) {
/* 45 */       return null;
/*    */     }
/*    */     
/* 48 */     if (!name.startsWith("java:")) {
/* 49 */       throw new NamingException("JNDI name must start with java: but was " + name);
/*    */     }
/*    */     
/* 52 */     Object lookup = ctx.lookup(name);
/* 53 */     return (lookup == null) ? null : lookup.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\cor\\util\JNDIUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */