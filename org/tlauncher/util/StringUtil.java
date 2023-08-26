/*     */ package org.tlauncher.util;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.tlauncher.exceptions.ParseException;
/*     */ 
/*     */ 
/*     */ public class StringUtil
/*     */ {
/*     */   public static final String LINK_PATTERN = "<a style='color:#585858;' href='link'>link</a>";
/*     */   
/*     */   public static String addSlashes(String str, EscapeGroup group) {
/*  19 */     if (str == null) {
/*  20 */       return "";
/*     */     }
/*  22 */     StringBuilder s = new StringBuilder(str);
/*  23 */     for (int i = 0; i < s.length(); i++) {
/*  24 */       char curChar = s.charAt(i);
/*     */       
/*  26 */       for (char c : group.getChars()) {
/*  27 */         if (curChar == c)
/*  28 */           s.insert(i++, '\\'); 
/*     */       } 
/*  30 */     }  return s.toString();
/*     */   }
/*     */   
/*     */   public static String[] addSlashes(String[] str, EscapeGroup group) {
/*  34 */     if (str == null) {
/*  35 */       return null;
/*     */     }
/*  37 */     int len = str.length;
/*  38 */     String[] ret = new String[len];
/*     */     
/*  40 */     for (int i = 0; i < len; i++) {
/*  41 */       ret[i] = addSlashes(str[i], group);
/*     */     }
/*  43 */     return ret;
/*     */   }
/*     */   
/*     */   public static String iconv(String inChar, String outChar, String str) {
/*  47 */     Charset in = Charset.forName(inChar), out = Charset.forName(outChar);
/*  48 */     CharsetDecoder decoder = in.newDecoder();
/*  49 */     CharsetEncoder encoder = out.newEncoder();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  54 */       ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(str));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  59 */       CharBuffer cbuf = decoder.decode(bbuf);
/*     */       
/*  61 */       return cbuf.toString();
/*  62 */     } catch (Exception e) {
/*  63 */       e.printStackTrace();
/*  64 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean parseBoolean(String b) throws ParseException {
/*  69 */     if (b == null) {
/*  70 */       throw new ParseException("String cannot be NULL!");
/*     */     }
/*  72 */     if (b.equalsIgnoreCase("true"))
/*  73 */       return true; 
/*  74 */     if (b.equalsIgnoreCase("false")) {
/*  75 */       return false;
/*     */     }
/*  77 */     throw new ParseException("Cannot parse value (" + b + ")!");
/*     */   }
/*     */   
/*     */   public static int countLines(String str) {
/*  81 */     if (str == null || str.length() == 0) {
/*  82 */       return 0;
/*     */     }
/*  84 */     int lines = 1;
/*  85 */     int len = str.length();
/*  86 */     for (int pos = 0; pos < len; pos++) {
/*  87 */       char c = str.charAt(pos);
/*  88 */       if (c == '\r') {
/*  89 */         lines++;
/*  90 */         if (pos + 1 < len && str.charAt(pos + 1) == '\n')
/*  91 */           pos++; 
/*  92 */       } else if (c == '\n') {
/*  93 */         lines++;
/*     */       } 
/*     */     } 
/*     */     
/*  97 */     return lines;
/*     */   }
/*     */   
/*     */   public static char lastChar(String str) {
/* 101 */     if (str == null) {
/* 102 */       throw new NullPointerException();
/*     */     }
/* 104 */     int len = str.length();
/*     */     
/* 106 */     if (len == 0) {
/* 107 */       return Character.MIN_VALUE;
/*     */     }
/* 109 */     if (len == 1) {
/* 110 */       return str.charAt(0);
/*     */     }
/* 112 */     return str.charAt(len - 1);
/*     */   }
/*     */   
/*     */   public static String randomizeWord(String str, boolean softly) {
/* 116 */     if (str == null) {
/* 117 */       return null;
/*     */     }
/* 119 */     int len = str.length();
/*     */     
/* 121 */     if (len < 4) {
/* 122 */       return str;
/*     */     }
/* 124 */     boolean[] reversedFlag = new boolean[len];
/*     */     
/* 126 */     if (softly)
/*     */     {
/* 128 */       reversedFlag[0] = true;
/*     */     }
/*     */     
/* 131 */     boolean chosenLastLetter = !softly;
/*     */     
/* 133 */     char[] chars = str.toCharArray();
/*     */     int i;
/* 135 */     for (i = len - 1; i > -1; i--) {
/* 136 */       char curChar = chars[i];
/* 137 */       int charType = Character.getType(curChar);
/* 138 */       boolean canBeReversed = (charType == 1 || charType == 2);
/*     */ 
/*     */       
/* 141 */       reversedFlag[i] = reversedFlag[i] | (!canBeReversed);
/*     */       
/* 143 */       if (canBeReversed && !chosenLastLetter) {
/* 144 */         reversedFlag[i] = true;
/* 145 */         chosenLastLetter = true;
/*     */       } 
/*     */     } 
/*     */     
/* 149 */     for (i = 0; i < len; i++) {
/* 150 */       if (!reversedFlag[i]) {
/*     */ 
/*     */         
/* 153 */         int newPos = i, tries = 0;
/*     */         
/* 155 */         while (tries < 3) {
/* 156 */           tries++;
/*     */           
/* 158 */           newPos = (new Random()).nextInt(len);
/*     */           
/* 160 */           if (reversedFlag[newPos]) {
/*     */             continue;
/*     */           }
/* 163 */           tries = 10;
/*     */         } 
/*     */ 
/*     */         
/* 167 */         if (tries == 10) {
/*     */ 
/*     */           
/* 170 */           char curChar = chars[i], replaceChar = chars[newPos];
/*     */           
/* 172 */           chars[i] = replaceChar;
/* 173 */           chars[newPos] = curChar;
/*     */           
/* 175 */           reversedFlag[i] = true;
/* 176 */           reversedFlag[newPos] = true;
/*     */         } 
/*     */       } 
/* 179 */     }  return new String(chars);
/*     */   }
/*     */   
/*     */   public static String randomizeWord(String str) {
/* 183 */     return randomizeWord(str, true);
/*     */   }
/*     */   
/*     */   public static String randomize(String str, boolean softly) {
/* 187 */     if (str == null) {
/* 188 */       return null;
/*     */     }
/* 190 */     if (str.isEmpty()) {
/* 191 */       return str;
/*     */     }
/* 193 */     String[] lines = str.split("\n");
/* 194 */     StringBuilder lineBuilder = new StringBuilder();
/*     */     
/* 196 */     boolean isFirstLine = true;
/*     */     
/* 198 */     for (String line : lines) {
/* 199 */       String[] words = line.split(" ");
/* 200 */       StringBuilder wordBuilder = new StringBuilder();
/*     */       
/* 202 */       boolean isFirstWord = true;
/*     */       
/* 204 */       for (String word : words) {
/*     */         
/* 206 */         if (isFirstWord) {
/* 207 */           isFirstWord = false;
/*     */         } else {
/* 209 */           wordBuilder.append(' ');
/*     */         } 
/* 211 */         wordBuilder.append(randomizeWord(word));
/*     */       } 
/*     */       
/* 214 */       if (isFirstLine) {
/* 215 */         isFirstLine = false;
/*     */       } else {
/* 217 */         lineBuilder.append('\n');
/*     */       } 
/* 219 */       lineBuilder.append(wordBuilder);
/*     */     } 
/*     */     
/* 222 */     return lineBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String randomize(String str) {
/* 226 */     return randomize(str, true);
/*     */   }
/*     */   
/*     */   public static boolean isHTML(char[] s) {
/* 230 */     if (s != null && 
/* 231 */       s.length >= 6 && s[0] == '<' && s[5] == '>') {
/* 232 */       String tag = new String(s, 1, 4);
/* 233 */       return tag.equalsIgnoreCase("html");
/*     */     } 
/*     */     
/* 236 */     return false;
/*     */   }
/*     */   
/*     */   public static String wrap(char[] s, int maxChars, boolean rudeBreaking, boolean detectHTML) {
/* 240 */     if (s == null)
/* 241 */       throw new NullPointerException("sequence"); 
/* 242 */     if (maxChars < 1) {
/* 243 */       throw new IllegalArgumentException("maxChars < 1");
/*     */     }
/* 245 */     detectHTML = (detectHTML && isHTML(s));
/*     */     
/* 247 */     String lineBreak = detectHTML ? "<br />" : "\n";
/*     */     
/* 249 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 251 */     int len = s.length, remaining = maxChars;
/* 252 */     boolean tagDetecting = false, ignoreCurrent = false;
/*     */ 
/*     */     
/* 255 */     for (int x = 0; x < len; x++) {
/* 256 */       char current = s[x];
/*     */       
/* 258 */       if (current == '<' && detectHTML) {
/*     */         
/* 260 */         tagDetecting = true;
/* 261 */         ignoreCurrent = true;
/* 262 */       } else if (tagDetecting) {
/*     */         
/* 264 */         if (current == '>') {
/* 265 */           tagDetecting = false;
/*     */         }
/* 267 */         ignoreCurrent = true;
/*     */       } 
/*     */       
/* 270 */       if (ignoreCurrent) {
/* 271 */         ignoreCurrent = false;
/*     */         
/* 273 */         builder.append(current);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 278 */         remaining--;
/*     */         
/* 280 */         if (s[x] == '\n' || (remaining < 1 && current == ' ')) {
/* 281 */           remaining = maxChars;
/* 282 */           builder.append(lineBreak);
/*     */         } else {
/* 284 */           if (lookForward(s, x, lineBreak)) {
/* 285 */             remaining = maxChars;
/*     */           }
/*     */           
/* 288 */           builder.append(current);
/*     */           
/* 290 */           if (remaining <= 0)
/*     */           {
/* 292 */             if (rudeBreaking)
/*     */             
/*     */             { 
/* 295 */               remaining = maxChars;
/* 296 */               builder.append(lineBreak); }  } 
/*     */         } 
/*     */       } 
/* 299 */     }  return builder.toString();
/*     */   }
/*     */   
/*     */   private static boolean lookForward(char[] c, int caret, CharSequence search) {
/* 303 */     if (c == null) {
/* 304 */       throw new NullPointerException("char array");
/*     */     }
/* 306 */     if (caret < 0) {
/* 307 */       throw new IllegalArgumentException("caret < 0");
/*     */     }
/* 309 */     if (caret >= c.length) {
/* 310 */       return false;
/*     */     }
/* 312 */     int length = search.length(), available = c.length - caret;
/*     */     
/* 314 */     if (length < available) {
/* 315 */       return false;
/*     */     }
/* 317 */     for (int i = 0; i < length; i++) {
/* 318 */       if (c[caret + i] != search.charAt(i))
/* 319 */         return false; 
/*     */     } 
/* 321 */     return true;
/*     */   }
/*     */   
/*     */   public static String wrap(String s, int maxChars, boolean rudeBreaking, boolean detectHTML) {
/* 325 */     return wrap(s.toCharArray(), maxChars, rudeBreaking, detectHTML);
/*     */   }
/*     */   
/*     */   public static String wrap(char[] s, int maxChars, boolean rudeBreaking) {
/* 329 */     return wrap(s, maxChars, rudeBreaking, true);
/*     */   }
/*     */   
/*     */   public static String wrap(char[] s, int maxChars) {
/* 333 */     return wrap(s, maxChars, false);
/*     */   }
/*     */   
/*     */   public static String wrap(String s, int maxChars) {
/* 337 */     return wrap(s.toCharArray(), maxChars);
/*     */   }
/*     */   
/*     */   public static String cut(String string, int max) {
/* 341 */     if (string == null) {
/* 342 */       return null;
/*     */     }
/* 344 */     int len = string.length();
/* 345 */     if (len <= max) {
/* 346 */       return string;
/*     */     }
/* 348 */     String[] words = string.split(" ");
/* 349 */     String ret = "";
/* 350 */     int remaining = max + 1;
/*     */     
/* 352 */     for (int x = 0; x < words.length; x++) {
/* 353 */       String curword = words[x];
/* 354 */       int curlen = curword.length();
/*     */       
/* 356 */       if (curlen < remaining) {
/* 357 */         ret = ret + " " + curword;
/* 358 */         remaining -= curlen + 1;
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 363 */         if (x == 0)
/* 364 */           ret = ret + " " + curword.substring(0, remaining - 1); 
/*     */         break;
/*     */       } 
/*     */     } 
/* 368 */     if (ret.length() == 0)
/* 369 */       return ""; 
/* 370 */     return ret.substring(1) + "...";
/*     */   }
/*     */   
/*     */   public static String requireNotBlank(String s, String name) {
/* 374 */     if (s == null) {
/* 375 */       throw new NullPointerException(name);
/*     */     }
/* 377 */     if (StringUtils.isBlank(s)) {
/* 378 */       throw new IllegalArgumentException(name);
/*     */     }
/* 380 */     return s;
/*     */   }
/*     */   
/*     */   public static String getLink(String link) {
/* 384 */     return "<a style='color:#585858;' href='link'>link</a>".replaceAll("link", link);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String convertListToString(String c, List<String> l) {
/* 391 */     StringBuilder b = new StringBuilder();
/*     */     
/* 393 */     for (String string : l) {
/* 394 */       b.append(string).append(c);
/*     */     }
/* 396 */     return b.toString();
/*     */   }
/*     */   
/* 399 */   public enum EscapeGroup { DOUBLE_QUOTE((String)new char[] { '"' }), COMMAND((String)DOUBLE_QUOTE, new char[] { '\'', ' ' }),
/* 400 */     REGEXP((String)COMMAND, new char[] { '/', '\\', '?', '*', '+', '[', ']', ':', '{', '}', '(', ')' });
/*     */     
/*     */     private final char[] chars;
/*     */     
/*     */     EscapeGroup(char... symbols) {
/* 405 */       this.chars = symbols;
/*     */     }
/*     */     
/*     */     EscapeGroup(EscapeGroup extend, char... symbols) {
/* 409 */       int len = extend.chars.length + symbols.length;
/* 410 */       this.chars = new char[len];
/*     */       
/* 412 */       int x = 0;
/* 413 */       for (; x < extend.chars.length; x++) {
/* 414 */         this.chars[x] = extend.chars[x];
/*     */       }
/* 416 */       System.arraycopy(symbols, 0, this.chars, x, symbols.length);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public char[] getChars() {
/* 423 */       return this.chars;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\StringUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */