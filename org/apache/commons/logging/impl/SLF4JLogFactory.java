/*     */ package org.apache.commons.logging.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogConfigurationException;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.slf4j.spi.LocationAwareLogger;
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
/*     */ public class SLF4JLogFactory
/*     */   extends LogFactory
/*     */ {
/*  67 */   ConcurrentMap<String, Log> loggerMap = new ConcurrentHashMap<String, Log>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected Hashtable attributes = new Hashtable<Object, Object>();
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
/*     */   public Object getAttribute(String name) {
/*  96 */     return this.attributes.get(name);
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
/*     */   public String[] getAttributeNames() {
/* 108 */     List<String> names = new ArrayList<String>();
/* 109 */     Enumeration<String> keys = this.attributes.keys();
/* 110 */     while (keys.hasMoreElements()) {
/* 111 */       names.add(keys.nextElement());
/*     */     }
/* 113 */     String[] results = new String[names.size()];
/* 114 */     for (int i = 0; i < results.length; i++) {
/* 115 */       results[i] = names.get(i);
/*     */     }
/* 117 */     return results;
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
/*     */   public Log getInstance(Class clazz) throws LogConfigurationException {
/* 132 */     return getInstance(clazz.getName());
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
/*     */   public Log getInstance(String name) throws LogConfigurationException {
/* 150 */     Log newInstance, instance = this.loggerMap.get(name);
/* 151 */     if (instance != null) {
/* 152 */       return instance;
/*     */     }
/*     */     
/* 155 */     Logger slf4jLogger = LoggerFactory.getLogger(name);
/* 156 */     if (slf4jLogger instanceof LocationAwareLogger) {
/* 157 */       newInstance = new SLF4JLocationAwareLog((LocationAwareLogger)slf4jLogger);
/*     */     } else {
/* 159 */       newInstance = new SLF4JLog(slf4jLogger);
/*     */     } 
/* 161 */     Log oldInstance = this.loggerMap.putIfAbsent(name, newInstance);
/* 162 */     return (oldInstance == null) ? newInstance : oldInstance;
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
/*     */   
/*     */   public void release() {
/* 182 */     System.out.println("WARN: The method " + SLF4JLogFactory.class + "#release() was invoked.");
/* 183 */     System.out.println("WARN: Please see http://www.slf4j.org/codes.html#release for an explanation.");
/* 184 */     System.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAttribute(String name) {
/* 195 */     this.attributes.remove(name);
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
/*     */   public void setAttribute(String name, Object value) {
/* 212 */     if (value == null) {
/* 213 */       this.attributes.remove(name);
/*     */     } else {
/* 215 */       this.attributes.put(name, value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\logging\impl\SLF4JLogFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */