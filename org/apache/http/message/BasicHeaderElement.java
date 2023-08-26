/*     */ package org.apache.http.message;
/*     */ 
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.NotThreadSafe;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BasicHeaderElement
/*     */   implements HeaderElement, Cloneable
/*     */ {
/*     */   private final String name;
/*     */   private final String value;
/*     */   private final NameValuePair[] parameters;
/*     */   
/*     */   public BasicHeaderElement(String name, String value, NameValuePair[] parameters) {
/*  61 */     this.name = (String)Args.notNull(name, "Name");
/*  62 */     this.value = value;
/*  63 */     if (parameters != null) {
/*  64 */       this.parameters = parameters;
/*     */     } else {
/*  66 */       this.parameters = new NameValuePair[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHeaderElement(String name, String value) {
/*  77 */     this(name, value, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  82 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  87 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public NameValuePair[] getParameters() {
/*  92 */     return (NameValuePair[])this.parameters.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParameterCount() {
/*  97 */     return this.parameters.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NameValuePair getParameter(int index) {
/* 103 */     return this.parameters[index];
/*     */   }
/*     */ 
/*     */   
/*     */   public NameValuePair getParameterByName(String name) {
/* 108 */     Args.notNull(name, "Name");
/* 109 */     NameValuePair found = null;
/* 110 */     for (NameValuePair current : this.parameters) {
/* 111 */       if (current.getName().equalsIgnoreCase(name)) {
/* 112 */         found = current;
/*     */         break;
/*     */       } 
/*     */     } 
/* 116 */     return found;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 121 */     if (this == object) {
/* 122 */       return true;
/*     */     }
/* 124 */     if (object instanceof HeaderElement) {
/* 125 */       BasicHeaderElement that = (BasicHeaderElement)object;
/* 126 */       return (this.name.equals(that.name) && LangUtils.equals(this.value, that.value) && LangUtils.equals((Object[])this.parameters, (Object[])that.parameters));
/*     */     } 
/*     */ 
/*     */     
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 136 */     int hash = 17;
/* 137 */     hash = LangUtils.hashCode(hash, this.name);
/* 138 */     hash = LangUtils.hashCode(hash, this.value);
/* 139 */     for (NameValuePair parameter : this.parameters) {
/* 140 */       hash = LangUtils.hashCode(hash, parameter);
/*     */     }
/* 142 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     StringBuilder buffer = new StringBuilder();
/* 148 */     buffer.append(this.name);
/* 149 */     if (this.value != null) {
/* 150 */       buffer.append("=");
/* 151 */       buffer.append(this.value);
/*     */     } 
/* 153 */     for (NameValuePair parameter : this.parameters) {
/* 154 */       buffer.append("; ");
/* 155 */       buffer.append(parameter);
/*     */     } 
/* 157 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 164 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\BasicHeaderElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */