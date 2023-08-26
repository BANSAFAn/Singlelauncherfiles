/*     */ package org.apache.commons.compress.harmony.archive.internal.nls;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Objects;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Messages
/*     */ {
/*  50 */   private static ResourceBundle bundle = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(String msg) {
/*  59 */     if (bundle == null) {
/*  60 */       return msg;
/*     */     }
/*     */     try {
/*  63 */       return bundle.getString(msg);
/*  64 */     } catch (MissingResourceException e) {
/*  65 */       return "Missing message: " + msg;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(String msg, Object arg) {
/*  77 */     return getString(msg, new Object[] { arg });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(String msg, int arg) {
/*  88 */     return getString(msg, new Object[] { Integer.toString(arg) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(String msg, char arg) {
/*  99 */     return getString(msg, new Object[] { String.valueOf(arg) });
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
/*     */   public static String getString(String msg, Object arg1, Object arg2) {
/* 111 */     return getString(msg, new Object[] { arg1, arg2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getString(String msg, Object[] args) {
/* 122 */     String format = msg;
/*     */     
/* 124 */     if (bundle != null) {
/*     */       try {
/* 126 */         format = bundle.getString(msg);
/* 127 */       } catch (MissingResourceException missingResourceException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 132 */     return format(format, args);
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
/*     */   public static String format(String format, Object[] args) {
/* 148 */     StringBuilder answer = new StringBuilder(format.length() + args.length * 20);
/* 149 */     String[] argStrings = new String[args.length];
/* 150 */     Arrays.setAll(argStrings, i -> Objects.toString(args[i], "<null>"));
/* 151 */     int lastI = 0; int i;
/* 152 */     for (i = format.indexOf('{', 0); i >= 0; i = format.indexOf('{', lastI)) {
/* 153 */       if (i != 0 && format.charAt(i - 1) == '\\') {
/*     */         
/* 155 */         if (i != 1) {
/* 156 */           answer.append(format.substring(lastI, i - 1));
/*     */         }
/* 158 */         answer.append('{');
/* 159 */         lastI = i + 1;
/*     */       }
/* 161 */       else if (i > format.length() - 3) {
/*     */         
/* 163 */         answer.append(format.substring(lastI));
/* 164 */         lastI = format.length();
/*     */       } else {
/* 166 */         int argnum = (byte)Character.digit(format.charAt(i + 1), 10);
/* 167 */         if (argnum < 0 || format.charAt(i + 2) != '}') {
/*     */           
/* 169 */           answer.append(format.substring(lastI, i + 1));
/* 170 */           lastI = i + 1;
/*     */         } else {
/*     */           
/* 173 */           answer.append(format.substring(lastI, i));
/* 174 */           if (argnum >= argStrings.length) {
/* 175 */             answer.append("<missing argument>");
/*     */           } else {
/* 177 */             answer.append(argStrings[argnum]);
/*     */           } 
/* 179 */           lastI = i + 3;
/*     */         } 
/*     */       } 
/*     */     } 
/* 183 */     if (lastI < format.length()) {
/* 184 */       answer.append(format.substring(lastI));
/*     */     }
/* 186 */     return answer.toString();
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
/*     */   public static ResourceBundle setLocale(Locale locale, String resource) {
/*     */     try {
/* 199 */       ClassLoader loader = null;
/* 200 */       return AccessController.<ResourceBundle>doPrivileged(() -> ResourceBundle.getBundle(resource, locale, (loader != null) ? loader : ClassLoader.getSystemClassLoader()));
/*     */     }
/* 202 */     catch (MissingResourceException missingResourceException) {
/*     */ 
/*     */       
/* 205 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   static {
/*     */     try {
/* 211 */       bundle = setLocale(Locale.getDefault(), "org.apache.commons.compress.harmony.archive.internal.nls.messages");
/*     */     }
/* 213 */     catch (Throwable e) {
/* 214 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\archive\internal\nls\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */