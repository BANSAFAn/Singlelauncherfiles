/*     */ package org.apache.commons.codec.language;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ColognePhonetic
/*     */   implements StringEncoder
/*     */ {
/* 183 */   private static final char[] AEIJOUY = new char[] { 'A', 'E', 'I', 'J', 'O', 'U', 'Y' };
/* 184 */   private static final char[] SCZ = new char[] { 'S', 'C', 'Z' };
/* 185 */   private static final char[] WFPV = new char[] { 'W', 'F', 'P', 'V' };
/* 186 */   private static final char[] GKQ = new char[] { 'G', 'K', 'Q' };
/* 187 */   private static final char[] CKQ = new char[] { 'C', 'K', 'Q' };
/* 188 */   private static final char[] AHKLOQRUX = new char[] { 'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X' };
/* 189 */   private static final char[] SZ = new char[] { 'S', 'Z' };
/* 190 */   private static final char[] AHOUKQX = new char[] { 'A', 'H', 'O', 'U', 'K', 'Q', 'X' };
/* 191 */   private static final char[] TDX = new char[] { 'T', 'D', 'X' };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class CologneBuffer
/*     */   {
/*     */     protected final char[] data;
/*     */ 
/*     */ 
/*     */     
/* 202 */     protected int length = 0;
/*     */     
/*     */     public CologneBuffer(char[] data) {
/* 205 */       this.data = data;
/* 206 */       this.length = data.length;
/*     */     }
/*     */     
/*     */     public CologneBuffer(int buffSize) {
/* 210 */       this.data = new char[buffSize];
/* 211 */       this.length = 0;
/*     */     }
/*     */     
/*     */     protected abstract char[] copyData(int param1Int1, int param1Int2);
/*     */     
/*     */     public int length() {
/* 217 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 222 */       return new String(copyData(0, this.length));
/*     */     }
/*     */   }
/*     */   
/*     */   private class CologneOutputBuffer
/*     */     extends CologneBuffer {
/*     */     public CologneOutputBuffer(int buffSize) {
/* 229 */       super(buffSize);
/*     */     }
/*     */     
/*     */     public void addRight(char chr) {
/* 233 */       this.data[this.length] = chr;
/* 234 */       this.length++;
/*     */     }
/*     */ 
/*     */     
/*     */     protected char[] copyData(int start, int length) {
/* 239 */       char[] newData = new char[length];
/* 240 */       System.arraycopy(this.data, start, newData, 0, length);
/* 241 */       return newData;
/*     */     }
/*     */   }
/*     */   
/*     */   private class CologneInputBuffer
/*     */     extends CologneBuffer {
/*     */     public CologneInputBuffer(char[] data) {
/* 248 */       super(data);
/*     */     }
/*     */     
/*     */     public void addLeft(char ch) {
/* 252 */       this.length++;
/* 253 */       this.data[getNextPos()] = ch;
/*     */     }
/*     */ 
/*     */     
/*     */     protected char[] copyData(int start, int length) {
/* 258 */       char[] newData = new char[length];
/* 259 */       System.arraycopy(this.data, this.data.length - this.length + start, newData, 0, length);
/* 260 */       return newData;
/*     */     }
/*     */     
/*     */     public char getNextChar() {
/* 264 */       return this.data[getNextPos()];
/*     */     }
/*     */     
/*     */     protected int getNextPos() {
/* 268 */       return this.data.length - this.length;
/*     */     }
/*     */     
/*     */     public char removeNext() {
/* 272 */       char ch = getNextChar();
/* 273 */       this.length--;
/* 274 */       return ch;
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
/*     */ 
/*     */   
/* 287 */   private static final char[][] PREPROCESS_MAP = new char[][] { { 'Ä', 'A' }, { 'Ü', 'U' }, { 'Ö', 'O' }, { 'ß', 'S' } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean arrayContains(char[] arr, char key) {
/* 298 */     for (char element : arr) {
/* 299 */       if (element == key) {
/* 300 */         return true;
/*     */       }
/*     */     } 
/* 303 */     return false;
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
/*     */   public String colognePhonetic(String text) {
/* 318 */     if (text == null) {
/* 319 */       return null;
/*     */     }
/*     */     
/* 322 */     text = preprocess(text);
/*     */     
/* 324 */     CologneOutputBuffer output = new CologneOutputBuffer(text.length() * 2);
/* 325 */     CologneInputBuffer input = new CologneInputBuffer(text.toCharArray());
/*     */ 
/*     */ 
/*     */     
/* 329 */     char lastChar = '-';
/* 330 */     char lastCode = '/';
/*     */ 
/*     */ 
/*     */     
/* 334 */     int rightLength = input.length();
/*     */     
/* 336 */     while (rightLength > 0) {
/* 337 */       char nextChar, code, chr = input.removeNext();
/*     */       
/* 339 */       if ((rightLength = input.length()) > 0) {
/* 340 */         nextChar = input.getNextChar();
/*     */       } else {
/* 342 */         nextChar = '-';
/*     */       } 
/*     */       
/* 345 */       if (arrayContains(AEIJOUY, chr)) {
/* 346 */         code = '0';
/* 347 */       } else if (chr == 'H' || chr < 'A' || chr > 'Z') {
/* 348 */         if (lastCode == '/') {
/*     */           continue;
/*     */         }
/* 351 */         code = '-';
/* 352 */       } else if (chr == 'B' || (chr == 'P' && nextChar != 'H')) {
/* 353 */         code = '1';
/* 354 */       } else if ((chr == 'D' || chr == 'T') && !arrayContains(SCZ, nextChar)) {
/* 355 */         code = '2';
/* 356 */       } else if (arrayContains(WFPV, chr)) {
/* 357 */         code = '3';
/* 358 */       } else if (arrayContains(GKQ, chr)) {
/* 359 */         code = '4';
/* 360 */       } else if (chr == 'X' && !arrayContains(CKQ, lastChar)) {
/* 361 */         code = '4';
/* 362 */         input.addLeft('S');
/* 363 */         rightLength++;
/* 364 */       } else if (chr == 'S' || chr == 'Z') {
/* 365 */         code = '8';
/* 366 */       } else if (chr == 'C') {
/* 367 */         if (lastCode == '/') {
/* 368 */           if (arrayContains(AHKLOQRUX, nextChar)) {
/* 369 */             code = '4';
/*     */           } else {
/* 371 */             code = '8';
/*     */           }
/*     */         
/* 374 */         } else if (arrayContains(SZ, lastChar) || !arrayContains(AHOUKQX, nextChar)) {
/* 375 */           code = '8';
/*     */         } else {
/* 377 */           code = '4';
/*     */         }
/*     */       
/* 380 */       } else if (arrayContains(TDX, chr)) {
/* 381 */         code = '8';
/* 382 */       } else if (chr == 'R') {
/* 383 */         code = '7';
/* 384 */       } else if (chr == 'L') {
/* 385 */         code = '5';
/* 386 */       } else if (chr == 'M' || chr == 'N') {
/* 387 */         code = '6';
/*     */       } else {
/* 389 */         code = chr;
/*     */       } 
/*     */       
/* 392 */       if (code != '-' && ((lastCode != code && (code != '0' || lastCode == '/')) || code < '0' || code > '8')) {
/* 393 */         output.addRight(code);
/*     */       }
/*     */       
/* 396 */       lastChar = chr;
/* 397 */       lastCode = code;
/*     */     } 
/* 399 */     return output.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object encode(Object object) throws EncoderException {
/* 404 */     if (!(object instanceof String)) {
/* 405 */       throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 411 */     return encode((String)object);
/*     */   }
/*     */ 
/*     */   
/*     */   public String encode(String text) {
/* 416 */     return colognePhonetic(text);
/*     */   }
/*     */   
/*     */   public boolean isEncodeEqual(String text1, String text2) {
/* 420 */     return colognePhonetic(text1).equals(colognePhonetic(text2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String preprocess(String text) {
/* 427 */     text = text.toUpperCase(Locale.GERMAN);
/*     */     
/* 429 */     char[] chrs = text.toCharArray();
/*     */     
/* 431 */     for (int index = 0; index < chrs.length; index++) {
/* 432 */       if (chrs[index] > 'Z') {
/* 433 */         for (char[] element : PREPROCESS_MAP) {
/* 434 */           if (chrs[index] == element[0]) {
/* 435 */             chrs[index] = element[1];
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 441 */     return new String(chrs);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\codec\language\ColognePhonetic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */