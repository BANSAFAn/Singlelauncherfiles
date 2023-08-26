/*     */ package org.apache.http.conn.util;
/*     */ 
/*     */ import java.net.IDN;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.util.Args;
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
/*     */ @ThreadSafe
/*     */ public final class PublicSuffixMatcher
/*     */ {
/*     */   private final Map<String, DomainType> rules;
/*     */   private final Map<String, DomainType> exceptions;
/*     */   
/*     */   public PublicSuffixMatcher(Collection<String> rules, Collection<String> exceptions) {
/*  56 */     this(DomainType.UNKNOWN, rules, exceptions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicSuffixMatcher(DomainType domainType, Collection<String> rules, Collection<String> exceptions) {
/*  64 */     Args.notNull(domainType, "Domain type");
/*  65 */     Args.notNull(rules, "Domain suffix rules");
/*  66 */     this.rules = new ConcurrentHashMap<String, DomainType>(rules.size());
/*  67 */     for (String rule : rules) {
/*  68 */       this.rules.put(rule, domainType);
/*     */     }
/*  70 */     this.exceptions = new ConcurrentHashMap<String, DomainType>();
/*  71 */     if (exceptions != null) {
/*  72 */       for (String exception : exceptions) {
/*  73 */         this.exceptions.put(exception, domainType);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicSuffixMatcher(Collection<PublicSuffixList> lists) {
/*  82 */     Args.notNull(lists, "Domain suffix lists");
/*  83 */     this.rules = new ConcurrentHashMap<String, DomainType>();
/*  84 */     this.exceptions = new ConcurrentHashMap<String, DomainType>();
/*  85 */     for (PublicSuffixList list : lists) {
/*  86 */       DomainType domainType = list.getType();
/*  87 */       List<String> rules = list.getRules();
/*  88 */       for (String rule : rules) {
/*  89 */         this.rules.put(rule, domainType);
/*     */       }
/*  91 */       List<String> exceptions = list.getExceptions();
/*  92 */       if (exceptions != null) {
/*  93 */         for (String exception : exceptions) {
/*  94 */           this.exceptions.put(exception, domainType);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean hasEntry(Map<String, DomainType> map, String rule, DomainType expectedType) {
/* 101 */     if (map == null) {
/* 102 */       return false;
/*     */     }
/* 104 */     DomainType domainType = map.get(rule);
/* 105 */     if (domainType == null) {
/* 106 */       return false;
/*     */     }
/* 108 */     return (expectedType == null || domainType.equals(expectedType));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasRule(String rule, DomainType expectedType) {
/* 113 */     return hasEntry(this.rules, rule, expectedType);
/*     */   }
/*     */   
/*     */   private boolean hasException(String exception, DomainType expectedType) {
/* 117 */     return hasEntry(this.exceptions, exception, expectedType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDomainRoot(String domain) {
/* 128 */     return getDomainRoot(domain, null);
/*     */   }
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
/*     */   public String getDomainRoot(String domain, DomainType expectedType) {
/* 142 */     if (domain == null) {
/* 143 */       return null;
/*     */     }
/* 145 */     if (domain.startsWith(".")) {
/* 146 */       return null;
/*     */     }
/* 148 */     String domainName = null;
/* 149 */     String segment = domain.toLowerCase(Locale.ROOT);
/* 150 */     while (segment != null) {
/*     */ 
/*     */       
/* 153 */       if (hasException(IDN.toUnicode(segment), expectedType)) {
/* 154 */         return segment;
/*     */       }
/*     */       
/* 157 */       if (hasRule(IDN.toUnicode(segment), expectedType)) {
/*     */         break;
/*     */       }
/*     */       
/* 161 */       int nextdot = segment.indexOf('.');
/* 162 */       String nextSegment = (nextdot != -1) ? segment.substring(nextdot + 1) : null;
/*     */       
/* 164 */       if (nextSegment != null && 
/* 165 */         hasRule("*." + IDN.toUnicode(nextSegment), expectedType)) {
/*     */         break;
/*     */       }
/*     */       
/* 169 */       if (nextdot != -1) {
/* 170 */         domainName = segment;
/*     */       }
/* 172 */       segment = nextSegment;
/*     */     } 
/* 174 */     return domainName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(String domain) {
/* 181 */     return matches(domain, null);
/*     */   }
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
/*     */   public boolean matches(String domain, DomainType expectedType) {
/* 194 */     if (domain == null) {
/* 195 */       return false;
/*     */     }
/* 197 */     String domainRoot = getDomainRoot(domain.startsWith(".") ? domain.substring(1) : domain, expectedType);
/*     */     
/* 199 */     return (domainRoot == null);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\con\\util\PublicSuffixMatcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */