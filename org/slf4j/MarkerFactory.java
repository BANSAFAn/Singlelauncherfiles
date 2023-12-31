/*    */ package org.slf4j;
/*    */ 
/*    */ import org.slf4j.helpers.BasicMarkerFactory;
/*    */ import org.slf4j.helpers.Util;
/*    */ import org.slf4j.impl.StaticMarkerBinder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MarkerFactory
/*    */ {
/*    */   static IMarkerFactory markerFactory;
/*    */   
/*    */   static {
/*    */     try {
/* 52 */       markerFactory = StaticMarkerBinder.SINGLETON.getMarkerFactory();
/* 53 */     } catch (NoClassDefFoundError e) {
/* 54 */       markerFactory = (IMarkerFactory)new BasicMarkerFactory();
/*    */     }
/* 56 */     catch (Exception e) {
/*    */       
/* 58 */       Util.report("Unexpected failure while binding MarkerFactory", e);
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
/*    */   public static Marker getMarker(String name) {
/* 71 */     return markerFactory.getMarker(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Marker getDetachedMarker(String name) {
/* 82 */     return markerFactory.getDetachedMarker(name);
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
/*    */   public static IMarkerFactory getIMarkerFactory() {
/* 94 */     return markerFactory;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\MarkerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */