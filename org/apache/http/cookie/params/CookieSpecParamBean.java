/*    */ package org.apache.http.cookie.params;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.params.HttpAbstractParamBean;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ @NotThreadSafe
/*    */ public class CookieSpecParamBean
/*    */   extends HttpAbstractParamBean
/*    */ {
/*    */   public CookieSpecParamBean(HttpParams params) {
/* 51 */     super(params);
/*    */   }
/*    */   
/*    */   public void setDatePatterns(Collection<String> patterns) {
/* 55 */     this.params.setParameter("http.protocol.cookie-datepatterns", patterns);
/*    */   }
/*    */   
/*    */   public void setSingleHeader(boolean singleHeader) {
/* 59 */     this.params.setBooleanParameter("http.protocol.single-cookie-header", singleHeader);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\cookie\params\CookieSpecParamBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */