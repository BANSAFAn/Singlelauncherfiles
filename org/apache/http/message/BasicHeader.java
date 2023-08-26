/*    */ package org.apache.http.message;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HeaderElement;
/*    */ import org.apache.http.ParseException;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.util.Args;
/*    */ import org.apache.http.util.CharArrayBuffer;
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
/*    */ @Immutable
/*    */ public class BasicHeader
/*    */   implements Header, Cloneable, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -5427236326487562174L;
/*    */   private final String name;
/*    */   private final String value;
/*    */   
/*    */   public BasicHeader(String name, String value) {
/* 59 */     this.name = (String)Args.notNull(name, "Name");
/* 60 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 65 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 70 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 76 */     return BasicLineFormatter.INSTANCE.formatHeader((CharArrayBuffer)null, this).toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public HeaderElement[] getElements() throws ParseException {
/* 81 */     if (this.value != null)
/*    */     {
/* 83 */       return BasicHeaderValueParser.parseElements(this.value, (HeaderValueParser)null);
/*    */     }
/* 85 */     return new HeaderElement[0];
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object clone() throws CloneNotSupportedException {
/* 91 */     return super.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */