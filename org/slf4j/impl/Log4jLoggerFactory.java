/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import org.apache.log4j.LogManager;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.slf4j.ILoggerFactory;
/*    */ import org.slf4j.Logger;
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
/*    */ public class Log4jLoggerFactory
/*    */   implements ILoggerFactory
/*    */ {
/* 49 */   ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Logger getLogger(String name) {
/*    */     Logger log4jLogger;
/* 58 */     Logger slf4jLogger = this.loggerMap.get(name);
/* 59 */     if (slf4jLogger != null) {
/* 60 */       return slf4jLogger;
/*    */     }
/*    */     
/* 63 */     if (name.equalsIgnoreCase("ROOT")) {
/* 64 */       log4jLogger = LogManager.getRootLogger();
/*    */     } else {
/* 66 */       log4jLogger = LogManager.getLogger(name);
/*    */     } 
/* 68 */     Log4jLoggerAdapter log4jLoggerAdapter = new Log4jLoggerAdapter(log4jLogger);
/* 69 */     Logger oldInstance = (Logger)this.loggerMap.putIfAbsent(name, log4jLoggerAdapter);
/* 70 */     return (oldInstance == null) ? (Logger)log4jLoggerAdapter : oldInstance;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\impl\Log4jLoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */