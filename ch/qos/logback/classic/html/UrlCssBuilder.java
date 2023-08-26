/*    */ package ch.qos.logback.classic.html;
/*    */ 
/*    */ import ch.qos.logback.core.html.CssBuilder;
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
/*    */ public class UrlCssBuilder
/*    */   implements CssBuilder
/*    */ {
/* 27 */   String url = "http://logback.qos.ch/css/classic.css";
/*    */   
/*    */   public String getUrl() {
/* 30 */     return this.url;
/*    */   }
/*    */   
/*    */   public void setUrl(String url) {
/* 34 */     this.url = url;
/*    */   }
/*    */   
/*    */   public void addCss(StringBuilder sbuf) {
/* 38 */     sbuf.append("<link REL=StyleSheet HREF=\"");
/* 39 */     sbuf.append(this.url);
/* 40 */     sbuf.append("\" TITLE=\"Basic\" />");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\classic\html\UrlCssBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */