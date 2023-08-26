/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.BitSet;
/*     */ import java.util.Calendar;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.cookie.SetCookie;
/*     */ import org.apache.http.message.ParserCursor;
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
/*     */ @Immutable
/*     */ public class LaxExpiresHandler
/*     */   extends AbstractCookieAttributeHandler
/*     */   implements CommonCookieAttributeHandler
/*     */ {
/*  53 */   static final TimeZone UTC = TimeZone.getTimeZone("UTC");
/*     */   private static final BitSet DELIMS;
/*     */   
/*     */   static {
/*  57 */     BitSet bitSet = new BitSet();
/*  58 */     bitSet.set(9); int b;
/*  59 */     for (b = 32; b <= 47; b++) {
/*  60 */       bitSet.set(b);
/*     */     }
/*  62 */     for (b = 59; b <= 64; b++) {
/*  63 */       bitSet.set(b);
/*     */     }
/*  65 */     for (b = 91; b <= 96; b++) {
/*  66 */       bitSet.set(b);
/*     */     }
/*  68 */     for (b = 123; b <= 126; b++) {
/*  69 */       bitSet.set(b);
/*     */     }
/*  71 */     DELIMS = bitSet;
/*     */ 
/*     */ 
/*     */     
/*  75 */     ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>(12);
/*  76 */     map.put("jan", Integer.valueOf(0));
/*  77 */     map.put("feb", Integer.valueOf(1));
/*  78 */     map.put("mar", Integer.valueOf(2));
/*  79 */     map.put("apr", Integer.valueOf(3));
/*  80 */     map.put("may", Integer.valueOf(4));
/*  81 */     map.put("jun", Integer.valueOf(5));
/*  82 */     map.put("jul", Integer.valueOf(6));
/*  83 */     map.put("aug", Integer.valueOf(7));
/*  84 */     map.put("sep", Integer.valueOf(8));
/*  85 */     map.put("oct", Integer.valueOf(9));
/*  86 */     map.put("nov", Integer.valueOf(10));
/*  87 */     map.put("dec", Integer.valueOf(11));
/*  88 */     MONTHS = map;
/*     */   }
/*     */   private static final Map<String, Integer> MONTHS;
/*  91 */   private static final Pattern TIME_PATTERN = Pattern.compile("^([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})([^0-9].*)?$");
/*     */   
/*  93 */   private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("^([0-9]{1,2})([^0-9].*)?$");
/*     */   
/*  95 */   private static final Pattern MONTH_PATTERN = Pattern.compile("^(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)(.*)?$", 2);
/*     */   
/*  97 */   private static final Pattern YEAR_PATTERN = Pattern.compile("^([0-9]{2,4})([^0-9].*)?$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/* 106 */     Args.notNull(cookie, "Cookie");
/* 107 */     ParserCursor cursor = new ParserCursor(0, value.length());
/* 108 */     StringBuilder content = new StringBuilder();
/*     */     
/* 110 */     int second = 0, minute = 0, hour = 0, day = 0, month = 0, year = 0;
/* 111 */     boolean foundTime = false, foundDayOfMonth = false, foundMonth = false, foundYear = false;
/*     */     try {
/* 113 */       while (!cursor.atEnd()) {
/* 114 */         skipDelims(value, cursor);
/* 115 */         content.setLength(0);
/* 116 */         copyContent(value, cursor, content);
/*     */         
/* 118 */         if (content.length() == 0) {
/*     */           break;
/*     */         }
/* 121 */         if (!foundTime) {
/* 122 */           Matcher matcher = TIME_PATTERN.matcher(content);
/* 123 */           if (matcher.matches()) {
/* 124 */             foundTime = true;
/* 125 */             hour = Integer.parseInt(matcher.group(1));
/* 126 */             minute = Integer.parseInt(matcher.group(2));
/* 127 */             second = Integer.parseInt(matcher.group(3));
/*     */             continue;
/*     */           } 
/*     */         } 
/* 131 */         if (!foundDayOfMonth) {
/* 132 */           Matcher matcher = DAY_OF_MONTH_PATTERN.matcher(content);
/* 133 */           if (matcher.matches()) {
/* 134 */             foundDayOfMonth = true;
/* 135 */             day = Integer.parseInt(matcher.group(1));
/*     */             continue;
/*     */           } 
/*     */         } 
/* 139 */         if (!foundMonth) {
/* 140 */           Matcher matcher = MONTH_PATTERN.matcher(content);
/* 141 */           if (matcher.matches()) {
/* 142 */             foundMonth = true;
/* 143 */             month = ((Integer)MONTHS.get(matcher.group(1).toLowerCase(Locale.ROOT))).intValue();
/*     */             continue;
/*     */           } 
/*     */         } 
/* 147 */         if (!foundYear) {
/* 148 */           Matcher matcher = YEAR_PATTERN.matcher(content);
/* 149 */           if (matcher.matches()) {
/* 150 */             foundYear = true;
/* 151 */             year = Integer.parseInt(matcher.group(1));
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 156 */     } catch (NumberFormatException ignore) {
/* 157 */       throw new MalformedCookieException("Invalid 'expires' attribute: " + value);
/*     */     } 
/* 159 */     if (!foundTime || !foundDayOfMonth || !foundMonth || !foundYear) {
/* 160 */       throw new MalformedCookieException("Invalid 'expires' attribute: " + value);
/*     */     }
/* 162 */     if (year >= 70 && year <= 99) {
/* 163 */       year = 1900 + year;
/*     */     }
/* 165 */     if (year >= 0 && year <= 69) {
/* 166 */       year = 2000 + year;
/*     */     }
/* 168 */     if (day < 1 || day > 31 || year < 1601 || hour > 23 || minute > 59 || second > 59) {
/* 169 */       throw new MalformedCookieException("Invalid 'expires' attribute: " + value);
/*     */     }
/*     */     
/* 172 */     Calendar c = Calendar.getInstance();
/* 173 */     c.setTimeZone(UTC);
/* 174 */     c.setTimeInMillis(0L);
/* 175 */     c.set(13, second);
/* 176 */     c.set(12, minute);
/* 177 */     c.set(11, hour);
/* 178 */     c.set(5, day);
/* 179 */     c.set(2, month);
/* 180 */     c.set(1, year);
/* 181 */     cookie.setExpiryDate(c.getTime());
/*     */   }
/*     */   
/*     */   private void skipDelims(CharSequence buf, ParserCursor cursor) {
/* 185 */     int pos = cursor.getPos();
/* 186 */     int indexFrom = cursor.getPos();
/* 187 */     int indexTo = cursor.getUpperBound();
/* 188 */     for (int i = indexFrom; i < indexTo; ) {
/* 189 */       char current = buf.charAt(i);
/* 190 */       if (DELIMS.get(current)) {
/* 191 */         pos++;
/*     */         
/*     */         i++;
/*     */       } 
/*     */     } 
/* 196 */     cursor.updatePos(pos);
/*     */   }
/*     */   
/*     */   private void copyContent(CharSequence buf, ParserCursor cursor, StringBuilder dst) {
/* 200 */     int pos = cursor.getPos();
/* 201 */     int indexFrom = cursor.getPos();
/* 202 */     int indexTo = cursor.getUpperBound();
/* 203 */     for (int i = indexFrom; i < indexTo; i++) {
/* 204 */       char current = buf.charAt(i);
/* 205 */       if (DELIMS.get(current)) {
/*     */         break;
/*     */       }
/* 208 */       pos++;
/* 209 */       dst.append(current);
/*     */     } 
/*     */     
/* 212 */     cursor.updatePos(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeName() {
/* 217 */     return "expires";
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\LaxExpiresHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */