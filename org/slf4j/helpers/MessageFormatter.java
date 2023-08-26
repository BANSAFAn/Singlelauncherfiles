/*     */ package org.slf4j.helpers;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MessageFormatter
/*     */ {
/*     */   static final char DELIM_START = '{';
/*     */   static final char DELIM_STOP = '}';
/*     */   static final String DELIM_STR = "{}";
/*     */   private static final char ESCAPE_CHAR = '\\';
/*     */   
/*     */   public static final FormattingTuple format(String messagePattern, Object arg) {
/* 124 */     return arrayFormat(messagePattern, new Object[] { arg });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final FormattingTuple format(String messagePattern, Object arg1, Object arg2) {
/* 152 */     return arrayFormat(messagePattern, new Object[] { arg1, arg2 });
/*     */   }
/*     */   
/*     */   static final Throwable getThrowableCandidate(Object[] argArray) {
/* 156 */     if (argArray == null || argArray.length == 0) {
/* 157 */       return null;
/*     */     }
/*     */     
/* 160 */     Object lastEntry = argArray[argArray.length - 1];
/* 161 */     if (lastEntry instanceof Throwable) {
/* 162 */       return (Throwable)lastEntry;
/*     */     }
/* 164 */     return null;
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
/*     */   public static final FormattingTuple arrayFormat(String messagePattern, Object[] argArray) {
/* 182 */     Throwable throwableCandidate = getThrowableCandidate(argArray);
/*     */     
/* 184 */     if (messagePattern == null) {
/* 185 */       return new FormattingTuple(null, argArray, throwableCandidate);
/*     */     }
/*     */     
/* 188 */     if (argArray == null) {
/* 189 */       return new FormattingTuple(messagePattern);
/*     */     }
/*     */     
/* 192 */     int i = 0;
/*     */     
/* 194 */     StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);
/*     */     
/*     */     int L;
/* 197 */     for (L = 0; L < argArray.length; L++) {
/*     */       
/* 199 */       int j = messagePattern.indexOf("{}", i);
/*     */       
/* 201 */       if (j == -1) {
/*     */         
/* 203 */         if (i == 0) {
/* 204 */           return new FormattingTuple(messagePattern, argArray, throwableCandidate);
/*     */         }
/*     */ 
/*     */         
/* 208 */         sbuf.append(messagePattern.substring(i, messagePattern.length()));
/* 209 */         return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
/*     */       } 
/*     */ 
/*     */       
/* 213 */       if (isEscapedDelimeter(messagePattern, j)) {
/* 214 */         if (!isDoubleEscaped(messagePattern, j)) {
/* 215 */           L--;
/* 216 */           sbuf.append(messagePattern.substring(i, j - 1));
/* 217 */           sbuf.append('{');
/* 218 */           i = j + 1;
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 223 */           sbuf.append(messagePattern.substring(i, j - 1));
/* 224 */           deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object, Object>());
/* 225 */           i = j + 2;
/*     */         } 
/*     */       } else {
/*     */         
/* 229 */         sbuf.append(messagePattern.substring(i, j));
/* 230 */         deeplyAppendParameter(sbuf, argArray[L], new HashMap<Object, Object>());
/* 231 */         i = j + 2;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 236 */     sbuf.append(messagePattern.substring(i, messagePattern.length()));
/* 237 */     if (L < argArray.length - 1) {
/* 238 */       return new FormattingTuple(sbuf.toString(), argArray, throwableCandidate);
/*     */     }
/* 240 */     return new FormattingTuple(sbuf.toString(), argArray, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final boolean isEscapedDelimeter(String messagePattern, int delimeterStartIndex) {
/* 247 */     if (delimeterStartIndex == 0) {
/* 248 */       return false;
/*     */     }
/* 250 */     char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
/* 251 */     if (potentialEscape == '\\') {
/* 252 */       return true;
/*     */     }
/* 254 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final boolean isDoubleEscaped(String messagePattern, int delimeterStartIndex) {
/* 260 */     if (delimeterStartIndex >= 2 && messagePattern.charAt(delimeterStartIndex - 2) == '\\')
/*     */     {
/* 262 */       return true;
/*     */     }
/* 264 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void deeplyAppendParameter(StringBuffer sbuf, Object o, Map seenMap) {
/* 271 */     if (o == null) {
/* 272 */       sbuf.append("null");
/*     */       return;
/*     */     } 
/* 275 */     if (!o.getClass().isArray()) {
/* 276 */       safeObjectAppend(sbuf, o);
/*     */ 
/*     */     
/*     */     }
/* 280 */     else if (o instanceof boolean[]) {
/* 281 */       booleanArrayAppend(sbuf, (boolean[])o);
/* 282 */     } else if (o instanceof byte[]) {
/* 283 */       byteArrayAppend(sbuf, (byte[])o);
/* 284 */     } else if (o instanceof char[]) {
/* 285 */       charArrayAppend(sbuf, (char[])o);
/* 286 */     } else if (o instanceof short[]) {
/* 287 */       shortArrayAppend(sbuf, (short[])o);
/* 288 */     } else if (o instanceof int[]) {
/* 289 */       intArrayAppend(sbuf, (int[])o);
/* 290 */     } else if (o instanceof long[]) {
/* 291 */       longArrayAppend(sbuf, (long[])o);
/* 292 */     } else if (o instanceof float[]) {
/* 293 */       floatArrayAppend(sbuf, (float[])o);
/* 294 */     } else if (o instanceof double[]) {
/* 295 */       doubleArrayAppend(sbuf, (double[])o);
/*     */     } else {
/* 297 */       objectArrayAppend(sbuf, (Object[])o, seenMap);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void safeObjectAppend(StringBuffer sbuf, Object o) {
/*     */     try {
/* 304 */       String oAsString = o.toString();
/* 305 */       sbuf.append(oAsString);
/* 306 */     } catch (Throwable t) {
/* 307 */       System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + "]");
/*     */ 
/*     */       
/* 310 */       t.printStackTrace();
/* 311 */       sbuf.append("[FAILED toString()]");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void objectArrayAppend(StringBuffer sbuf, Object[] a, Map seenMap) {
/* 318 */     sbuf.append('[');
/* 319 */     if (!seenMap.containsKey(a)) {
/* 320 */       seenMap.put(a, null);
/* 321 */       int len = a.length;
/* 322 */       for (int i = 0; i < len; i++) {
/* 323 */         deeplyAppendParameter(sbuf, a[i], seenMap);
/* 324 */         if (i != len - 1) {
/* 325 */           sbuf.append(", ");
/*     */         }
/*     */       } 
/* 328 */       seenMap.remove(a);
/*     */     } else {
/* 330 */       sbuf.append("...");
/*     */     } 
/* 332 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void booleanArrayAppend(StringBuffer sbuf, boolean[] a) {
/* 336 */     sbuf.append('[');
/* 337 */     int len = a.length;
/* 338 */     for (int i = 0; i < len; i++) {
/* 339 */       sbuf.append(a[i]);
/* 340 */       if (i != len - 1)
/* 341 */         sbuf.append(", "); 
/*     */     } 
/* 343 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void byteArrayAppend(StringBuffer sbuf, byte[] a) {
/* 347 */     sbuf.append('[');
/* 348 */     int len = a.length;
/* 349 */     for (int i = 0; i < len; i++) {
/* 350 */       sbuf.append(a[i]);
/* 351 */       if (i != len - 1)
/* 352 */         sbuf.append(", "); 
/*     */     } 
/* 354 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void charArrayAppend(StringBuffer sbuf, char[] a) {
/* 358 */     sbuf.append('[');
/* 359 */     int len = a.length;
/* 360 */     for (int i = 0; i < len; i++) {
/* 361 */       sbuf.append(a[i]);
/* 362 */       if (i != len - 1)
/* 363 */         sbuf.append(", "); 
/*     */     } 
/* 365 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void shortArrayAppend(StringBuffer sbuf, short[] a) {
/* 369 */     sbuf.append('[');
/* 370 */     int len = a.length;
/* 371 */     for (int i = 0; i < len; i++) {
/* 372 */       sbuf.append(a[i]);
/* 373 */       if (i != len - 1)
/* 374 */         sbuf.append(", "); 
/*     */     } 
/* 376 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void intArrayAppend(StringBuffer sbuf, int[] a) {
/* 380 */     sbuf.append('[');
/* 381 */     int len = a.length;
/* 382 */     for (int i = 0; i < len; i++) {
/* 383 */       sbuf.append(a[i]);
/* 384 */       if (i != len - 1)
/* 385 */         sbuf.append(", "); 
/*     */     } 
/* 387 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void longArrayAppend(StringBuffer sbuf, long[] a) {
/* 391 */     sbuf.append('[');
/* 392 */     int len = a.length;
/* 393 */     for (int i = 0; i < len; i++) {
/* 394 */       sbuf.append(a[i]);
/* 395 */       if (i != len - 1)
/* 396 */         sbuf.append(", "); 
/*     */     } 
/* 398 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void floatArrayAppend(StringBuffer sbuf, float[] a) {
/* 402 */     sbuf.append('[');
/* 403 */     int len = a.length;
/* 404 */     for (int i = 0; i < len; i++) {
/* 405 */       sbuf.append(a[i]);
/* 406 */       if (i != len - 1)
/* 407 */         sbuf.append(", "); 
/*     */     } 
/* 409 */     sbuf.append(']');
/*     */   }
/*     */   
/*     */   private static void doubleArrayAppend(StringBuffer sbuf, double[] a) {
/* 413 */     sbuf.append('[');
/* 414 */     int len = a.length;
/* 415 */     for (int i = 0; i < len; i++) {
/* 416 */       sbuf.append(a[i]);
/* 417 */       if (i != len - 1)
/* 418 */         sbuf.append(", "); 
/*     */     } 
/* 420 */     sbuf.append(']');
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\helpers\MessageFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */