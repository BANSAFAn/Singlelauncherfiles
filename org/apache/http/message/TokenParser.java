/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ @Immutable
/*     */ public class TokenParser
/*     */ {
/*     */   public static final char CR = '\r';
/*     */   public static final char LF = '\n';
/*     */   public static final char SP = ' ';
/*     */   public static final char HT = '\t';
/*     */   public static final char DQUOTE = '"';
/*     */   public static final char ESCAPE = '\\';
/*     */   
/*     */   public static BitSet INIT_BITSET(int... b) {
/*  47 */     BitSet bitset = new BitSet();
/*  48 */     for (int aB : b) {
/*  49 */       bitset.set(aB);
/*     */     }
/*  51 */     return bitset;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWhitespace(char ch) {
/*  73 */     return (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n');
/*     */   }
/*     */   
/*  76 */   public static final TokenParser INSTANCE = new TokenParser();
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
/*     */   public String parseToken(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters) {
/*  88 */     StringBuilder dst = new StringBuilder();
/*  89 */     boolean whitespace = false;
/*  90 */     while (!cursor.atEnd()) {
/*  91 */       char current = buf.charAt(cursor.getPos());
/*  92 */       if (delimiters != null && delimiters.get(current))
/*     */         break; 
/*  94 */       if (isWhitespace(current)) {
/*  95 */         skipWhiteSpace(buf, cursor);
/*  96 */         whitespace = true; continue;
/*     */       } 
/*  98 */       if (whitespace && dst.length() > 0) {
/*  99 */         dst.append(' ');
/*     */       }
/* 101 */       copyContent(buf, cursor, delimiters, dst);
/* 102 */       whitespace = false;
/*     */     } 
/*     */     
/* 105 */     return dst.toString();
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
/*     */   public String parseValue(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters) {
/* 119 */     StringBuilder dst = new StringBuilder();
/* 120 */     boolean whitespace = false;
/* 121 */     while (!cursor.atEnd()) {
/* 122 */       char current = buf.charAt(cursor.getPos());
/* 123 */       if (delimiters != null && delimiters.get(current))
/*     */         break; 
/* 125 */       if (isWhitespace(current)) {
/* 126 */         skipWhiteSpace(buf, cursor);
/* 127 */         whitespace = true; continue;
/* 128 */       }  if (current == '"') {
/* 129 */         if (whitespace && dst.length() > 0) {
/* 130 */           dst.append(' ');
/*     */         }
/* 132 */         copyQuotedContent(buf, cursor, dst);
/* 133 */         whitespace = false; continue;
/*     */       } 
/* 135 */       if (whitespace && dst.length() > 0) {
/* 136 */         dst.append(' ');
/*     */       }
/* 138 */       copyUnquotedContent(buf, cursor, delimiters, dst);
/* 139 */       whitespace = false;
/*     */     } 
/*     */     
/* 142 */     return dst.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void skipWhiteSpace(CharArrayBuffer buf, ParserCursor cursor) {
/* 153 */     int pos = cursor.getPos();
/* 154 */     int indexFrom = cursor.getPos();
/* 155 */     int indexTo = cursor.getUpperBound();
/* 156 */     for (int i = indexFrom; i < indexTo; i++) {
/* 157 */       char current = buf.charAt(i);
/* 158 */       if (!isWhitespace(current)) {
/*     */         break;
/*     */       }
/* 161 */       pos++;
/*     */     } 
/*     */     
/* 164 */     cursor.updatePos(pos);
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
/*     */   public void copyContent(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
/* 179 */     int pos = cursor.getPos();
/* 180 */     int indexFrom = cursor.getPos();
/* 181 */     int indexTo = cursor.getUpperBound();
/* 182 */     for (int i = indexFrom; i < indexTo; i++) {
/* 183 */       char current = buf.charAt(i);
/* 184 */       if ((delimiters != null && delimiters.get(current)) || isWhitespace(current)) {
/*     */         break;
/*     */       }
/* 187 */       pos++;
/* 188 */       dst.append(current);
/*     */     } 
/*     */     
/* 191 */     cursor.updatePos(pos);
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
/*     */   public void copyUnquotedContent(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
/* 206 */     int pos = cursor.getPos();
/* 207 */     int indexFrom = cursor.getPos();
/* 208 */     int indexTo = cursor.getUpperBound();
/* 209 */     for (int i = indexFrom; i < indexTo; i++) {
/* 210 */       char current = buf.charAt(i);
/* 211 */       if ((delimiters != null && delimiters.get(current)) || isWhitespace(current) || current == '"') {
/*     */         break;
/*     */       }
/*     */       
/* 215 */       pos++;
/* 216 */       dst.append(current);
/*     */     } 
/*     */     
/* 219 */     cursor.updatePos(pos);
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
/*     */   public void copyQuotedContent(CharArrayBuffer buf, ParserCursor cursor, StringBuilder dst) {
/* 231 */     if (cursor.atEnd()) {
/*     */       return;
/*     */     }
/* 234 */     int pos = cursor.getPos();
/* 235 */     int indexFrom = cursor.getPos();
/* 236 */     int indexTo = cursor.getUpperBound();
/* 237 */     char current = buf.charAt(pos);
/* 238 */     if (current != '"') {
/*     */       return;
/*     */     }
/* 241 */     pos++;
/* 242 */     indexFrom++;
/* 243 */     boolean escaped = false;
/* 244 */     for (int i = indexFrom; i < indexTo; i++, pos++) {
/* 245 */       current = buf.charAt(i);
/* 246 */       if (escaped) {
/* 247 */         if (current != '"' && current != '\\') {
/* 248 */           dst.append('\\');
/*     */         }
/* 250 */         dst.append(current);
/* 251 */         escaped = false;
/*     */       } else {
/* 253 */         if (current == '"') {
/* 254 */           pos++;
/*     */           break;
/*     */         } 
/* 257 */         if (current == '\\') {
/* 258 */           escaped = true;
/* 259 */         } else if (current != '\r' && current != '\n') {
/* 260 */           dst.append(current);
/*     */         } 
/*     */       } 
/*     */     } 
/* 264 */     cursor.updatePos(pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\message\TokenParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */