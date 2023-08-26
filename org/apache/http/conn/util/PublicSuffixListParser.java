/*     */ package org.apache.http.conn.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ public final class PublicSuffixListParser
/*     */ {
/*     */   public PublicSuffixList parse(Reader reader) throws IOException {
/*  59 */     List<String> rules = new ArrayList<String>();
/*  60 */     List<String> exceptions = new ArrayList<String>();
/*  61 */     BufferedReader r = new BufferedReader(reader);
/*     */     
/*     */     String line;
/*  64 */     while ((line = r.readLine()) != null) {
/*  65 */       if (line.isEmpty()) {
/*     */         continue;
/*     */       }
/*  68 */       if (line.startsWith("//")) {
/*     */         continue;
/*     */       }
/*  71 */       if (line.startsWith(".")) {
/*  72 */         line = line.substring(1);
/*     */       }
/*     */       
/*  75 */       boolean isException = line.startsWith("!");
/*  76 */       if (isException) {
/*  77 */         line = line.substring(1);
/*     */       }
/*     */       
/*  80 */       if (isException) {
/*  81 */         exceptions.add(line); continue;
/*     */       } 
/*  83 */       rules.add(line);
/*     */     } 
/*     */     
/*  86 */     return new PublicSuffixList(DomainType.UNKNOWN, rules, exceptions);
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
/*     */   
/*     */   public List<PublicSuffixList> parseByType(Reader reader) throws IOException {
/* 101 */     List<PublicSuffixList> result = new ArrayList<PublicSuffixList>(2);
/*     */     
/* 103 */     BufferedReader r = new BufferedReader(reader);
/* 104 */     StringBuilder sb = new StringBuilder(256);
/*     */     
/* 106 */     DomainType domainType = null;
/* 107 */     List<String> rules = null;
/* 108 */     List<String> exceptions = null;
/*     */     String line;
/* 110 */     while ((line = r.readLine()) != null) {
/* 111 */       if (line.isEmpty()) {
/*     */         continue;
/*     */       }
/* 114 */       if (line.startsWith("//")) {
/*     */         
/* 116 */         if (domainType == null) {
/* 117 */           if (line.contains("===BEGIN ICANN DOMAINS===")) {
/* 118 */             domainType = DomainType.ICANN; continue;
/* 119 */           }  if (line.contains("===BEGIN PRIVATE DOMAINS==="))
/* 120 */             domainType = DomainType.PRIVATE; 
/*     */           continue;
/*     */         } 
/* 123 */         if (line.contains("===END ICANN DOMAINS===") || line.contains("===END PRIVATE DOMAINS===")) {
/* 124 */           if (rules != null) {
/* 125 */             result.add(new PublicSuffixList(domainType, rules, exceptions));
/*     */           }
/* 127 */           domainType = null;
/* 128 */           rules = null;
/* 129 */           exceptions = null;
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 135 */       if (domainType == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 139 */       if (line.startsWith(".")) {
/* 140 */         line = line.substring(1);
/*     */       }
/*     */       
/* 143 */       boolean isException = line.startsWith("!");
/* 144 */       if (isException) {
/* 145 */         line = line.substring(1);
/*     */       }
/*     */       
/* 148 */       if (isException) {
/* 149 */         if (exceptions == null) {
/* 150 */           exceptions = new ArrayList<String>();
/*     */         }
/* 152 */         exceptions.add(line); continue;
/*     */       } 
/* 154 */       if (rules == null) {
/* 155 */         rules = new ArrayList<String>();
/*     */       }
/* 157 */       rules.add(line);
/*     */     } 
/*     */     
/* 160 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\con\\util\PublicSuffixListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */