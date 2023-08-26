/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Date;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.cookie.SetCookie2;
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
/*     */ @NotThreadSafe
/*     */ public class BasicClientCookie2
/*     */   extends BasicClientCookie
/*     */   implements SetCookie2
/*     */ {
/*     */   private static final long serialVersionUID = -7744598295706617057L;
/*     */   private String commentURL;
/*     */   private int[] ports;
/*     */   private boolean discard;
/*     */   
/*     */   public BasicClientCookie2(String name, String value) {
/*  56 */     super(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getPorts() {
/*  61 */     return this.ports;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPorts(int[] ports) {
/*  66 */     this.ports = ports;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommentURL() {
/*  71 */     return this.commentURL;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCommentURL(String commentURL) {
/*  76 */     this.commentURL = commentURL;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDiscard(boolean discard) {
/*  81 */     this.discard = discard;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPersistent() {
/*  86 */     return (!this.discard && super.isPersistent());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExpired(Date date) {
/*  91 */     return (this.discard || super.isExpired(date));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/*  96 */     BasicClientCookie2 clone = (BasicClientCookie2)super.clone();
/*  97 */     if (this.ports != null) {
/*  98 */       clone.ports = (int[])this.ports.clone();
/*     */     }
/* 100 */     return clone;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\BasicClientCookie2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */