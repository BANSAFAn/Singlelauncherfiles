/*    */ package org.apache.http.params;
/*    */ 
/*    */ import org.apache.http.annotation.ThreadSafe;
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
/*    */ @Deprecated
/*    */ @ThreadSafe
/*    */ public class SyncBasicHttpParams
/*    */   extends BasicHttpParams
/*    */ {
/*    */   private static final long serialVersionUID = 5387834869062660642L;
/*    */   
/*    */   public synchronized boolean removeParameter(String name) {
/* 51 */     return super.removeParameter(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized HttpParams setParameter(String name, Object value) {
/* 56 */     return super.setParameter(name, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized Object getParameter(String name) {
/* 61 */     return super.getParameter(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized boolean isParameterSet(String name) {
/* 66 */     return super.isParameterSet(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized boolean isParameterSetLocally(String name) {
/* 71 */     return super.isParameterSetLocally(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void setParameters(String[] names, Object value) {
/* 76 */     super.setParameters(names, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void clear() {
/* 81 */     super.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized Object clone() throws CloneNotSupportedException {
/* 86 */     return super.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\params\SyncBasicHttpParams.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */