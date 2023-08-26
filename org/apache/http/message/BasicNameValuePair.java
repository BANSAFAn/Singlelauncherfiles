/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.LangUtils;
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
/*     */ public class BasicNameValuePair
/*     */   implements NameValuePair, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6437800749411518984L;
/*     */   private final String name;
/*     */   private final String value;
/*     */   
/*     */   public BasicNameValuePair(String name, String value) {
/*  58 */     this.name = (String)Args.notNull(name, "Name");
/*  59 */     this.value = value;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  64 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  69 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  76 */     if (this.value == null) {
/*  77 */       return this.name;
/*     */     }
/*  79 */     int len = this.name.length() + 1 + this.value.length();
/*  80 */     StringBuilder buffer = new StringBuilder(len);
/*  81 */     buffer.append(this.name);
/*  82 */     buffer.append("=");
/*  83 */     buffer.append(this.value);
/*  84 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  89 */     if (this == object) {
/*  90 */       return true;
/*     */     }
/*  92 */     if (object instanceof NameValuePair) {
/*  93 */       BasicNameValuePair that = (BasicNameValuePair)object;
/*  94 */       return (this.name.equals(that.name) && LangUtils.equals(this.value, that.value));
/*     */     } 
/*     */     
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 102 */     int hash = 17;
/* 103 */     hash = LangUtils.hashCode(hash, this.name);
/* 104 */     hash = LangUtils.hashCode(hash, this.value);
/* 105 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 110 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicNameValuePair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */