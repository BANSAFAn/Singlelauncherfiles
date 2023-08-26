/*     */ package org.apache.http.client.utils;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.text.ParsePosition;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import org.apache.http.annotation.Immutable;
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
/*     */ 
/*     */ 
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
/*     */ public final class DateUtils
/*     */ {
/*     */   public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*     */   public static final String PATTERN_RFC1036 = "EEE, dd-MMM-yy HH:mm:ss zzz";
/*     */   public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
/*  69 */   private static final String[] DEFAULT_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy" };
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Date DEFAULT_TWO_DIGIT_YEAR_START;
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*     */   
/*     */   static {
/*  80 */     Calendar calendar = Calendar.getInstance();
/*  81 */     calendar.setTimeZone(GMT);
/*  82 */     calendar.set(2000, 0, 1, 0, 0, 0);
/*  83 */     calendar.set(14, 0);
/*  84 */     DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
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
/*     */   public static Date parseDate(String dateValue) {
/*  96 */     return parseDate(dateValue, null, null);
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
/*     */   public static Date parseDate(String dateValue, String[] dateFormats) {
/* 108 */     return parseDate(dateValue, dateFormats, null);
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
/*     */   public static Date parseDate(String dateValue, String[] dateFormats, Date startDate) {
/* 127 */     Args.notNull(dateValue, "Date value");
/* 128 */     String[] localDateFormats = (dateFormats != null) ? dateFormats : DEFAULT_PATTERNS;
/* 129 */     Date localStartDate = (startDate != null) ? startDate : DEFAULT_TWO_DIGIT_YEAR_START;
/* 130 */     String v = dateValue;
/*     */ 
/*     */     
/* 133 */     if (v.length() > 1 && v.startsWith("'") && v.endsWith("'")) {
/* 134 */       v = v.substring(1, v.length() - 1);
/*     */     }
/*     */     
/* 137 */     for (String dateFormat : localDateFormats) {
/* 138 */       SimpleDateFormat dateParser = DateFormatHolder.formatFor(dateFormat);
/* 139 */       dateParser.set2DigitYearStart(localStartDate);
/* 140 */       ParsePosition pos = new ParsePosition(0);
/* 141 */       Date result = dateParser.parse(v, pos);
/* 142 */       if (pos.getIndex() != 0) {
/* 143 */         return result;
/*     */       }
/*     */     } 
/* 146 */     return null;
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
/*     */   public static String formatDate(Date date) {
/* 158 */     return formatDate(date, "EEE, dd MMM yyyy HH:mm:ss zzz");
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
/*     */   public static String formatDate(Date date, String pattern) {
/* 175 */     Args.notNull(date, "Date");
/* 176 */     Args.notNull(pattern, "Pattern");
/* 177 */     SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
/* 178 */     return formatter.format(date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearThreadLocal() {
/* 187 */     DateFormatHolder.clearThreadLocal();
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
/*     */   static final class DateFormatHolder
/*     */   {
/* 203 */     private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = new ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static SimpleDateFormat formatFor(String pattern) {
/* 218 */       SoftReference<Map<String, SimpleDateFormat>> ref = THREADLOCAL_FORMATS.get();
/* 219 */       Map<String, SimpleDateFormat> formats = (ref == null) ? null : ref.get();
/* 220 */       if (formats == null) {
/* 221 */         formats = new HashMap<String, SimpleDateFormat>();
/* 222 */         THREADLOCAL_FORMATS.set(new SoftReference<Map<String, SimpleDateFormat>>(formats));
/*     */       } 
/*     */ 
/*     */       
/* 226 */       SimpleDateFormat format = formats.get(pattern);
/* 227 */       if (format == null) {
/* 228 */         format = new SimpleDateFormat(pattern, Locale.US);
/* 229 */         format.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 230 */         formats.put(pattern, format);
/*     */       } 
/*     */       
/* 233 */       return format;
/*     */     }
/*     */     
/*     */     public static void clearThreadLocal() {
/* 237 */       THREADLOCAL_FORMATS.remove();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\clien\\utils\DateUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */