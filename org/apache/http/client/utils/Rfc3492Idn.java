/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.util.StringTokenizer;
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
/*     */ @Deprecated
/*     */ @Immutable
/*     */ public class Rfc3492Idn
/*     */   implements Idn
/*     */ {
/*     */   private static final int base = 36;
/*     */   private static final int tmin = 1;
/*     */   private static final int tmax = 26;
/*     */   private static final int skew = 38;
/*     */   private static final int damp = 700;
/*     */   private static final int initial_bias = 72;
/*     */   private static final int initial_n = 128;
/*     */   private static final char delimiter = '-';
/*     */   private static final String ACE_PREFIX = "xn--";
/*     */   
/*     */   private int adapt(int delta, int numpoints, boolean firsttime) {
/*  54 */     int d = delta;
/*  55 */     if (firsttime) {
/*  56 */       d /= 700;
/*     */     } else {
/*  58 */       d /= 2;
/*     */     } 
/*  60 */     d += d / numpoints;
/*  61 */     int k = 0;
/*  62 */     while (d > 455) {
/*  63 */       d /= 35;
/*  64 */       k += 36;
/*     */     } 
/*  66 */     return k + 36 * d / (d + 38);
/*     */   }
/*     */   
/*     */   private int digit(char c) {
/*  70 */     if (c >= 'A' && c <= 'Z') {
/*  71 */       return c - 65;
/*     */     }
/*  73 */     if (c >= 'a' && c <= 'z') {
/*  74 */       return c - 97;
/*     */     }
/*  76 */     if (c >= '0' && c <= '9') {
/*  77 */       return c - 48 + 26;
/*     */     }
/*  79 */     throw new IllegalArgumentException("illegal digit: " + c);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toUnicode(String punycode) {
/*  84 */     StringBuilder unicode = new StringBuilder(punycode.length());
/*  85 */     StringTokenizer tok = new StringTokenizer(punycode, ".");
/*  86 */     while (tok.hasMoreTokens()) {
/*  87 */       String t = tok.nextToken();
/*  88 */       if (unicode.length() > 0) {
/*  89 */         unicode.append('.');
/*     */       }
/*  91 */       if (t.startsWith("xn--")) {
/*  92 */         t = decode(t.substring(4));
/*     */       }
/*  94 */       unicode.append(t);
/*     */     } 
/*  96 */     return unicode.toString();
/*     */   }
/*     */   
/*     */   protected String decode(String s) {
/* 100 */     String input = s;
/* 101 */     int n = 128;
/* 102 */     int i = 0;
/* 103 */     int bias = 72;
/* 104 */     StringBuilder output = new StringBuilder(input.length());
/* 105 */     int lastdelim = input.lastIndexOf('-');
/* 106 */     if (lastdelim != -1) {
/* 107 */       output.append(input.subSequence(0, lastdelim));
/* 108 */       input = input.substring(lastdelim + 1);
/*     */     } 
/*     */     
/* 111 */     while (!input.isEmpty()) {
/* 112 */       int oldi = i;
/* 113 */       int w = 1;
/* 114 */       int k = 36;
/* 115 */       for (; !input.isEmpty(); k += 36) {
/*     */         int t;
/*     */         
/* 118 */         char c = input.charAt(0);
/* 119 */         input = input.substring(1);
/* 120 */         int digit = digit(c);
/* 121 */         i += digit * w;
/*     */         
/* 123 */         if (k <= bias + 1) {
/* 124 */           t = 1;
/* 125 */         } else if (k >= bias + 26) {
/* 126 */           t = 26;
/*     */         } else {
/* 128 */           t = k - bias;
/*     */         } 
/* 130 */         if (digit < t) {
/*     */           break;
/*     */         }
/* 133 */         w *= 36 - t;
/*     */       } 
/* 135 */       bias = adapt(i - oldi, output.length() + 1, (oldi == 0));
/* 136 */       n += i / (output.length() + 1);
/* 137 */       i %= output.length() + 1;
/*     */       
/* 139 */       output.insert(i, (char)n);
/* 140 */       i++;
/*     */     } 
/* 142 */     return output.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\clien\\utils\Rfc3492Idn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */