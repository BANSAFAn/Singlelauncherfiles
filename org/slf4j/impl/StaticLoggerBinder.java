/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import org.apache.log4j.Level;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.helpers.Util;
/*    */ import org.slf4j.spi.LoggerFactoryBinder;
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
/*    */ public class StaticLoggerBinder
/*    */   implements LoggerFactoryBinder
/*    */ {
/* 45 */   private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final StaticLoggerBinder getSingleton() {
/* 53 */     return SINGLETON;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 61 */   public static String REQUESTED_API_VERSION = "1.6.99";
/*    */   
/* 63 */   private static final String loggerFactoryClassStr = Log4jLoggerFactory.class.getName();
/*    */ 
/*    */ 
/*    */   
/*    */   private final ILoggerFactory loggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private StaticLoggerBinder() {
/* 73 */     this.loggerFactory = new Log4jLoggerFactory();
/*    */     try {
/* 75 */       Level level = Level.TRACE;
/* 76 */     } catch (NoSuchFieldError nsfe) {
/* 77 */       Util.report("This version of SLF4J requires log4j version 1.2.12 or later. See also http://www.slf4j.org/codes.html#log4j_version");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ILoggerFactory getLoggerFactory() {
/* 83 */     return this.loggerFactory;
/*    */   }
/*    */   
/*    */   public String getLoggerFactoryClassStr() {
/* 87 */     return loggerFactoryClassStr;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\impl\StaticLoggerBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */