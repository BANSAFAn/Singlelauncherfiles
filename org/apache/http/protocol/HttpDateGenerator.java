/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.TimeZone;
/*    */ import org.apache.http.annotation.GuardedBy;
/*    */ import org.apache.http.annotation.ThreadSafe;
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
/*    */ @ThreadSafe
/*    */ public class HttpDateGenerator
/*    */ {
/*    */   public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
/* 52 */   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*    */   @GuardedBy("this")
/*    */   private final DateFormat dateformat;
/*    */   @GuardedBy("this")
/* 56 */   private long dateAsLong = 0L;
/*    */   @GuardedBy("this")
/* 58 */   private String dateAsText = null;
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpDateGenerator() {
/* 63 */     this.dateformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
/* 64 */     this.dateformat.setTimeZone(GMT);
/*    */   }
/*    */   
/*    */   public synchronized String getCurrentDate() {
/* 68 */     long now = System.currentTimeMillis();
/* 69 */     if (now - this.dateAsLong > 1000L) {
/*    */       
/* 71 */       this.dateAsText = this.dateformat.format(new Date(now));
/* 72 */       this.dateAsLong = now;
/*    */     } 
/* 74 */     return this.dateAsText;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\protocol\HttpDateGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */