/*    */ package org.apache.http.conn.util;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.util.Args;
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
/*    */ public final class PublicSuffixList
/*    */ {
/*    */   private final DomainType type;
/*    */   private final List<String> rules;
/*    */   private final List<String> exceptions;
/*    */   
/*    */   public PublicSuffixList(DomainType type, List<String> rules, List<String> exceptions) {
/* 55 */     this.type = (DomainType)Args.notNull(type, "Domain type");
/* 56 */     this.rules = Collections.unmodifiableList((List<? extends String>)Args.notNull(rules, "Domain suffix rules"));
/* 57 */     this.exceptions = Collections.unmodifiableList((exceptions != null) ? exceptions : Collections.<String>emptyList());
/*    */   }
/*    */   
/*    */   public PublicSuffixList(List<String> rules, List<String> exceptions) {
/* 61 */     this(DomainType.UNKNOWN, rules, exceptions);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DomainType getType() {
/* 68 */     return this.type;
/*    */   }
/*    */   
/*    */   public List<String> getRules() {
/* 72 */     return this.rules;
/*    */   }
/*    */   
/*    */   public List<String> getExceptions() {
/* 76 */     return this.exceptions;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\con\\util\PublicSuffixList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */