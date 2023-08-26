/*    */ package org.apache.http.message;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.http.ProtocolVersion;
/*    */ import org.apache.http.RequestLine;
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
/*    */ @Immutable
/*    */ public class BasicRequestLine
/*    */   implements RequestLine, Cloneable, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 2810581718468737193L;
/*    */   private final ProtocolVersion protoversion;
/*    */   private final String method;
/*    */   private final String uri;
/*    */   
/*    */   public BasicRequestLine(String method, String uri, ProtocolVersion version) {
/* 55 */     this.method = (String)Args.notNull(method, "Method");
/* 56 */     this.uri = (String)Args.notNull(uri, "URI");
/* 57 */     this.protoversion = (ProtocolVersion)Args.notNull(version, "Version");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMethod() {
/* 62 */     return this.method;
/*    */   }
/*    */ 
/*    */   
/*    */   public ProtocolVersion getProtocolVersion() {
/* 67 */     return this.protoversion;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUri() {
/* 72 */     return this.uri;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 78 */     return BasicLineFormatter.INSTANCE.formatRequestLine((CharArrayBuffer)null, this).toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object clone() throws CloneNotSupportedException {
/* 83 */     return super.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicRequestLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */