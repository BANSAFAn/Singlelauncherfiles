/*    */ package org.slf4j.impl;
/*    */ 
/*    */ import org.slf4j.IMarkerFactory;
/*    */ import org.slf4j.helpers.BasicMarkerFactory;
/*    */ import org.slf4j.spi.MarkerFactoryBinder;
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
/*    */ public class StaticMarkerBinder
/*    */   implements MarkerFactoryBinder
/*    */ {
/* 44 */   public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();
/*    */   
/* 46 */   final IMarkerFactory markerFactory = (IMarkerFactory)new BasicMarkerFactory();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IMarkerFactory getMarkerFactory() {
/* 56 */     return this.markerFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMarkerFactoryClassStr() {
/* 64 */     return BasicMarkerFactory.class.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\impl\StaticMarkerBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */