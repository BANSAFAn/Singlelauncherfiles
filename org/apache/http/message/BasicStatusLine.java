/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharArrayBuffer;
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
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public class BasicStatusLine
/*     */   implements StatusLine, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2443303766890459269L;
/*     */   private final ProtocolVersion protoVersion;
/*     */   private final int statusCode;
/*     */   private final String reasonPhrase;
/*     */   
/*     */   public BasicStatusLine(ProtocolVersion version, int statusCode, String reasonPhrase) {
/*  70 */     this.protoVersion = (ProtocolVersion)Args.notNull(version, "Version");
/*  71 */     this.statusCode = Args.notNegative(statusCode, "Status code");
/*  72 */     this.reasonPhrase = reasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getStatusCode() {
/*  79 */     return this.statusCode;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/*  84 */     return this.protoVersion;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReasonPhrase() {
/*  89 */     return this.reasonPhrase;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     return BasicLineFormatter.INSTANCE.formatStatusLine((CharArrayBuffer)null, this).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 100 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicStatusLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */