/*      */ package com.google.gson.stream;
/*      */ 
/*      */ import com.google.gson.internal.JsonReaderInternalAccess;
/*      */ import com.google.gson.internal.bind.JsonTreeReader;
/*      */ import java.io.Closeable;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ import java.util.Arrays;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JsonReader
/*      */   implements Closeable
/*      */ {
/*      */   private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
/*      */   private static final int PEEKED_NONE = 0;
/*      */   private static final int PEEKED_BEGIN_OBJECT = 1;
/*      */   private static final int PEEKED_END_OBJECT = 2;
/*      */   private static final int PEEKED_BEGIN_ARRAY = 3;
/*      */   private static final int PEEKED_END_ARRAY = 4;
/*      */   private static final int PEEKED_TRUE = 5;
/*      */   private static final int PEEKED_FALSE = 6;
/*      */   private static final int PEEKED_NULL = 7;
/*      */   private static final int PEEKED_SINGLE_QUOTED = 8;
/*      */   private static final int PEEKED_DOUBLE_QUOTED = 9;
/*      */   private static final int PEEKED_UNQUOTED = 10;
/*      */   private static final int PEEKED_BUFFERED = 11;
/*      */   private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
/*      */   private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
/*      */   private static final int PEEKED_UNQUOTED_NAME = 14;
/*      */   private static final int PEEKED_LONG = 15;
/*      */   private static final int PEEKED_NUMBER = 16;
/*      */   private static final int PEEKED_EOF = 17;
/*      */   private static final int NUMBER_CHAR_NONE = 0;
/*      */   private static final int NUMBER_CHAR_SIGN = 1;
/*      */   private static final int NUMBER_CHAR_DIGIT = 2;
/*      */   private static final int NUMBER_CHAR_DECIMAL = 3;
/*      */   private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
/*      */   private static final int NUMBER_CHAR_EXP_E = 5;
/*      */   private static final int NUMBER_CHAR_EXP_SIGN = 6;
/*      */   private static final int NUMBER_CHAR_EXP_DIGIT = 7;
/*      */   private final Reader in;
/*      */   private boolean lenient = false;
/*  237 */   private final char[] buffer = new char[1024];
/*  238 */   private int pos = 0;
/*  239 */   private int limit = 0;
/*      */   
/*  241 */   private int lineNumber = 0;
/*  242 */   private int lineStart = 0;
/*      */   
/*  244 */   int peeked = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private long peekedLong;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int peekedNumberLength;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String peekedString;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  268 */   private int[] stack = new int[32];
/*  269 */   private int stackSize = 0; private String[] pathNames; private int[] pathIndices;
/*      */   public JsonReader(Reader in) {
/*  271 */     this.stack[this.stackSize++] = 6;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  282 */     this.pathNames = new String[32];
/*  283 */     this.pathIndices = new int[32];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  289 */     if (in == null) {
/*  290 */       throw new NullPointerException("in == null");
/*      */     }
/*  292 */     this.in = in;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setLenient(boolean lenient) {
/*  325 */     this.lenient = lenient;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isLenient() {
/*  332 */     return this.lenient;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginArray() throws IOException {
/*  340 */     int p = this.peeked;
/*  341 */     if (p == 0) {
/*  342 */       p = doPeek();
/*      */     }
/*  344 */     if (p == 3) {
/*  345 */       push(1);
/*  346 */       this.pathIndices[this.stackSize - 1] = 0;
/*  347 */       this.peeked = 0;
/*      */     } else {
/*  349 */       throw new IllegalStateException("Expected BEGIN_ARRAY but was " + peek() + locationString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endArray() throws IOException {
/*  358 */     int p = this.peeked;
/*  359 */     if (p == 0) {
/*  360 */       p = doPeek();
/*      */     }
/*  362 */     if (p == 4) {
/*  363 */       this.stackSize--;
/*  364 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  365 */       this.peeked = 0;
/*      */     } else {
/*  367 */       throw new IllegalStateException("Expected END_ARRAY but was " + peek() + locationString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginObject() throws IOException {
/*  376 */     int p = this.peeked;
/*  377 */     if (p == 0) {
/*  378 */       p = doPeek();
/*      */     }
/*  380 */     if (p == 1) {
/*  381 */       push(3);
/*  382 */       this.peeked = 0;
/*      */     } else {
/*  384 */       throw new IllegalStateException("Expected BEGIN_OBJECT but was " + peek() + locationString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endObject() throws IOException {
/*  393 */     int p = this.peeked;
/*  394 */     if (p == 0) {
/*  395 */       p = doPeek();
/*      */     }
/*  397 */     if (p == 2) {
/*  398 */       this.stackSize--;
/*  399 */       this.pathNames[this.stackSize] = null;
/*  400 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  401 */       this.peeked = 0;
/*      */     } else {
/*  403 */       throw new IllegalStateException("Expected END_OBJECT but was " + peek() + locationString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasNext() throws IOException {
/*  411 */     int p = this.peeked;
/*  412 */     if (p == 0) {
/*  413 */       p = doPeek();
/*      */     }
/*  415 */     return (p != 2 && p != 4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonToken peek() throws IOException {
/*  422 */     int p = this.peeked;
/*  423 */     if (p == 0) {
/*  424 */       p = doPeek();
/*      */     }
/*      */     
/*  427 */     switch (p) {
/*      */       case 1:
/*  429 */         return JsonToken.BEGIN_OBJECT;
/*      */       case 2:
/*  431 */         return JsonToken.END_OBJECT;
/*      */       case 3:
/*  433 */         return JsonToken.BEGIN_ARRAY;
/*      */       case 4:
/*  435 */         return JsonToken.END_ARRAY;
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*  439 */         return JsonToken.NAME;
/*      */       case 5:
/*      */       case 6:
/*  442 */         return JsonToken.BOOLEAN;
/*      */       case 7:
/*  444 */         return JsonToken.NULL;
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*  449 */         return JsonToken.STRING;
/*      */       case 15:
/*      */       case 16:
/*  452 */         return JsonToken.NUMBER;
/*      */       case 17:
/*  454 */         return JsonToken.END_DOCUMENT;
/*      */     } 
/*  456 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   
/*      */   int doPeek() throws IOException {
/*  461 */     int peekStack = this.stack[this.stackSize - 1];
/*  462 */     if (peekStack == 1)
/*  463 */     { this.stack[this.stackSize - 1] = 2; }
/*  464 */     else if (peekStack == 2)
/*      */     
/*  466 */     { int i = nextNonWhitespace(true);
/*  467 */       switch (i) {
/*      */         case 93:
/*  469 */           return this.peeked = 4;
/*      */         case 59:
/*  471 */           checkLenient(); break;
/*      */         case 44:
/*      */           break;
/*      */         default:
/*  475 */           throw syntaxError("Unterminated array");
/*      */       }  }
/*  477 */     else { if (peekStack == 3 || peekStack == 5) {
/*  478 */         this.stack[this.stackSize - 1] = 4;
/*      */         
/*  480 */         if (peekStack == 5) {
/*  481 */           int j = nextNonWhitespace(true);
/*  482 */           switch (j) {
/*      */             case 125:
/*  484 */               return this.peeked = 2;
/*      */             case 59:
/*  486 */               checkLenient(); break;
/*      */             case 44:
/*      */               break;
/*      */             default:
/*  490 */               throw syntaxError("Unterminated object");
/*      */           } 
/*      */         } 
/*  493 */         int i = nextNonWhitespace(true);
/*  494 */         switch (i) {
/*      */           case 34:
/*  496 */             return this.peeked = 13;
/*      */           case 39:
/*  498 */             checkLenient();
/*  499 */             return this.peeked = 12;
/*      */           case 125:
/*  501 */             if (peekStack != 5) {
/*  502 */               return this.peeked = 2;
/*      */             }
/*  504 */             throw syntaxError("Expected name");
/*      */         } 
/*      */         
/*  507 */         checkLenient();
/*  508 */         this.pos--;
/*  509 */         if (isLiteral((char)i)) {
/*  510 */           return this.peeked = 14;
/*      */         }
/*  512 */         throw syntaxError("Expected name");
/*      */       } 
/*      */       
/*  515 */       if (peekStack == 4) {
/*  516 */         this.stack[this.stackSize - 1] = 5;
/*      */         
/*  518 */         int i = nextNonWhitespace(true);
/*  519 */         switch (i) {
/*      */           case 58:
/*      */             break;
/*      */           case 61:
/*  523 */             checkLenient();
/*  524 */             if ((this.pos < this.limit || fillBuffer(1)) && this.buffer[this.pos] == '>') {
/*  525 */               this.pos++;
/*      */             }
/*      */             break;
/*      */           default:
/*  529 */             throw syntaxError("Expected ':'");
/*      */         } 
/*  531 */       } else if (peekStack == 6) {
/*  532 */         if (this.lenient) {
/*  533 */           consumeNonExecutePrefix();
/*      */         }
/*  535 */         this.stack[this.stackSize - 1] = 7;
/*  536 */       } else if (peekStack == 7) {
/*  537 */         int i = nextNonWhitespace(false);
/*  538 */         if (i == -1) {
/*  539 */           return this.peeked = 17;
/*      */         }
/*  541 */         checkLenient();
/*  542 */         this.pos--;
/*      */       }
/*  544 */       else if (peekStack == 8) {
/*  545 */         throw new IllegalStateException("JsonReader is closed");
/*      */       }  }
/*      */     
/*  548 */     int c = nextNonWhitespace(true);
/*  549 */     switch (c) {
/*      */       case 93:
/*  551 */         if (peekStack == 1) {
/*  552 */           return this.peeked = 4;
/*      */         }
/*      */ 
/*      */       
/*      */       case 44:
/*      */       case 59:
/*  558 */         if (peekStack == 1 || peekStack == 2) {
/*  559 */           checkLenient();
/*  560 */           this.pos--;
/*  561 */           return this.peeked = 7;
/*      */         } 
/*  563 */         throw syntaxError("Unexpected value");
/*      */       
/*      */       case 39:
/*  566 */         checkLenient();
/*  567 */         return this.peeked = 8;
/*      */       case 34:
/*  569 */         return this.peeked = 9;
/*      */       case 91:
/*  571 */         return this.peeked = 3;
/*      */       case 123:
/*  573 */         return this.peeked = 1;
/*      */     } 
/*  575 */     this.pos--;
/*      */ 
/*      */     
/*  578 */     int result = peekKeyword();
/*  579 */     if (result != 0) {
/*  580 */       return result;
/*      */     }
/*      */     
/*  583 */     result = peekNumber();
/*  584 */     if (result != 0) {
/*  585 */       return result;
/*      */     }
/*      */     
/*  588 */     if (!isLiteral(this.buffer[this.pos])) {
/*  589 */       throw syntaxError("Expected value");
/*      */     }
/*      */     
/*  592 */     checkLenient();
/*  593 */     return this.peeked = 10;
/*      */   }
/*      */   private int peekKeyword() throws IOException {
/*      */     String keyword, keywordUpper;
/*      */     int peeking;
/*  598 */     char c = this.buffer[this.pos];
/*      */ 
/*      */ 
/*      */     
/*  602 */     if (c == 't' || c == 'T') {
/*  603 */       keyword = "true";
/*  604 */       keywordUpper = "TRUE";
/*  605 */       peeking = 5;
/*  606 */     } else if (c == 'f' || c == 'F') {
/*  607 */       keyword = "false";
/*  608 */       keywordUpper = "FALSE";
/*  609 */       peeking = 6;
/*  610 */     } else if (c == 'n' || c == 'N') {
/*  611 */       keyword = "null";
/*  612 */       keywordUpper = "NULL";
/*  613 */       peeking = 7;
/*      */     } else {
/*  615 */       return 0;
/*      */     } 
/*      */ 
/*      */     
/*  619 */     int length = keyword.length();
/*  620 */     for (int i = 1; i < length; i++) {
/*  621 */       if (this.pos + i >= this.limit && !fillBuffer(i + 1)) {
/*  622 */         return 0;
/*      */       }
/*  624 */       c = this.buffer[this.pos + i];
/*  625 */       if (c != keyword.charAt(i) && c != keywordUpper.charAt(i)) {
/*  626 */         return 0;
/*      */       }
/*      */     } 
/*      */     
/*  630 */     if ((this.pos + length < this.limit || fillBuffer(length + 1)) && 
/*  631 */       isLiteral(this.buffer[this.pos + length])) {
/*  632 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*  636 */     this.pos += length;
/*  637 */     return this.peeked = peeking;
/*      */   }
/*      */   
/*      */   private int peekNumber() throws IOException {
/*      */     int j;
/*  642 */     char[] buffer = this.buffer;
/*  643 */     int p = this.pos;
/*  644 */     int l = this.limit;
/*      */     
/*  646 */     long value = 0L;
/*  647 */     boolean negative = false;
/*  648 */     boolean fitsInLong = true;
/*  649 */     int last = 0;
/*      */     
/*  651 */     int i = 0;
/*      */ 
/*      */     
/*  654 */     for (;; i++) {
/*  655 */       if (p + i == l) {
/*  656 */         if (i == buffer.length)
/*      */         {
/*      */           
/*  659 */           return 0;
/*      */         }
/*  661 */         if (!fillBuffer(i + 1)) {
/*      */           break;
/*      */         }
/*  664 */         p = this.pos;
/*  665 */         l = this.limit;
/*      */       } 
/*      */       
/*  668 */       char c = buffer[p + i];
/*  669 */       switch (c) {
/*      */         case '-':
/*  671 */           if (last == 0) {
/*  672 */             negative = true;
/*  673 */             last = 1; break;
/*      */           } 
/*  675 */           if (last == 5) {
/*  676 */             last = 6;
/*      */             break;
/*      */           } 
/*  679 */           return 0;
/*      */         
/*      */         case '+':
/*  682 */           if (last == 5) {
/*  683 */             last = 6;
/*      */             break;
/*      */           } 
/*  686 */           return 0;
/*      */         
/*      */         case 'E':
/*      */         case 'e':
/*  690 */           if (last == 2 || last == 4) {
/*  691 */             last = 5;
/*      */             break;
/*      */           } 
/*  694 */           return 0;
/*      */         
/*      */         case '.':
/*  697 */           if (last == 2) {
/*  698 */             last = 3;
/*      */             break;
/*      */           } 
/*  701 */           return 0;
/*      */         
/*      */         default:
/*  704 */           if (c < '0' || c > '9') {
/*  705 */             if (!isLiteral(c)) {
/*      */               break;
/*      */             }
/*  708 */             return 0;
/*      */           } 
/*  710 */           if (last == 1 || last == 0) {
/*  711 */             value = -(c - 48);
/*  712 */             last = 2; break;
/*  713 */           }  if (last == 2) {
/*  714 */             if (value == 0L) {
/*  715 */               return 0;
/*      */             }
/*  717 */             long newValue = value * 10L - (c - 48);
/*  718 */             j = fitsInLong & ((value > -922337203685477580L || (value == -922337203685477580L && newValue < value)) ? 1 : 0);
/*      */             
/*  720 */             value = newValue; break;
/*  721 */           }  if (last == 3) {
/*  722 */             last = 4; break;
/*  723 */           }  if (last == 5 || last == 6) {
/*  724 */             last = 7;
/*      */           }
/*      */           break;
/*      */       } 
/*      */     
/*      */     } 
/*  730 */     if (last == 2 && j != 0 && (value != Long.MIN_VALUE || negative) && (value != 0L || false == negative)) {
/*  731 */       this.peekedLong = negative ? value : -value;
/*  732 */       this.pos += i;
/*  733 */       return this.peeked = 15;
/*  734 */     }  if (last == 2 || last == 4 || last == 7) {
/*      */       
/*  736 */       this.peekedNumberLength = i;
/*  737 */       return this.peeked = 16;
/*      */     } 
/*  739 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isLiteral(char c) throws IOException {
/*  744 */     switch (c) {
/*      */       case '#':
/*      */       case '/':
/*      */       case ';':
/*      */       case '=':
/*      */       case '\\':
/*  750 */         checkLenient();
/*      */       case '\t':
/*      */       case '\n':
/*      */       case '\f':
/*      */       case '\r':
/*      */       case ' ':
/*      */       case ',':
/*      */       case ':':
/*      */       case '[':
/*      */       case ']':
/*      */       case '{':
/*      */       case '}':
/*  762 */         return false;
/*      */     } 
/*  764 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextName() throws IOException {
/*      */     String result;
/*  776 */     int p = this.peeked;
/*  777 */     if (p == 0) {
/*  778 */       p = doPeek();
/*      */     }
/*      */     
/*  781 */     if (p == 14) {
/*  782 */       result = nextUnquotedValue();
/*  783 */     } else if (p == 12) {
/*  784 */       result = nextQuotedValue('\'');
/*  785 */     } else if (p == 13) {
/*  786 */       result = nextQuotedValue('"');
/*      */     } else {
/*  788 */       throw new IllegalStateException("Expected a name but was " + peek() + locationString());
/*      */     } 
/*  790 */     this.peeked = 0;
/*  791 */     this.pathNames[this.stackSize - 1] = result;
/*  792 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextString() throws IOException {
/*      */     String result;
/*  804 */     int p = this.peeked;
/*  805 */     if (p == 0) {
/*  806 */       p = doPeek();
/*      */     }
/*      */     
/*  809 */     if (p == 10) {
/*  810 */       result = nextUnquotedValue();
/*  811 */     } else if (p == 8) {
/*  812 */       result = nextQuotedValue('\'');
/*  813 */     } else if (p == 9) {
/*  814 */       result = nextQuotedValue('"');
/*  815 */     } else if (p == 11) {
/*  816 */       result = this.peekedString;
/*  817 */       this.peekedString = null;
/*  818 */     } else if (p == 15) {
/*  819 */       result = Long.toString(this.peekedLong);
/*  820 */     } else if (p == 16) {
/*  821 */       result = new String(this.buffer, this.pos, this.peekedNumberLength);
/*  822 */       this.pos += this.peekedNumberLength;
/*      */     } else {
/*  824 */       throw new IllegalStateException("Expected a string but was " + peek() + locationString());
/*      */     } 
/*  826 */     this.peeked = 0;
/*  827 */     this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  828 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nextBoolean() throws IOException {
/*  839 */     int p = this.peeked;
/*  840 */     if (p == 0) {
/*  841 */       p = doPeek();
/*      */     }
/*  843 */     if (p == 5) {
/*  844 */       this.peeked = 0;
/*  845 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  846 */       return true;
/*  847 */     }  if (p == 6) {
/*  848 */       this.peeked = 0;
/*  849 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  850 */       return false;
/*      */     } 
/*  852 */     throw new IllegalStateException("Expected a boolean but was " + peek() + locationString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void nextNull() throws IOException {
/*  863 */     int p = this.peeked;
/*  864 */     if (p == 0) {
/*  865 */       p = doPeek();
/*      */     }
/*  867 */     if (p == 7) {
/*  868 */       this.peeked = 0;
/*  869 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*      */     } else {
/*  871 */       throw new IllegalStateException("Expected null but was " + peek() + locationString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double nextDouble() throws IOException {
/*  885 */     int p = this.peeked;
/*  886 */     if (p == 0) {
/*  887 */       p = doPeek();
/*      */     }
/*      */     
/*  890 */     if (p == 15) {
/*  891 */       this.peeked = 0;
/*  892 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  893 */       return this.peekedLong;
/*      */     } 
/*      */     
/*  896 */     if (p == 16) {
/*  897 */       this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
/*  898 */       this.pos += this.peekedNumberLength;
/*  899 */     } else if (p == 8 || p == 9) {
/*  900 */       this.peekedString = nextQuotedValue((p == 8) ? 39 : 34);
/*  901 */     } else if (p == 10) {
/*  902 */       this.peekedString = nextUnquotedValue();
/*  903 */     } else if (p != 11) {
/*  904 */       throw new IllegalStateException("Expected a double but was " + peek() + locationString());
/*      */     } 
/*      */     
/*  907 */     this.peeked = 11;
/*  908 */     double result = Double.parseDouble(this.peekedString);
/*  909 */     if (!this.lenient && (Double.isNaN(result) || Double.isInfinite(result))) {
/*  910 */       throw new MalformedJsonException("JSON forbids NaN and infinities: " + result + 
/*  911 */           locationString());
/*      */     }
/*  913 */     this.peekedString = null;
/*  914 */     this.peeked = 0;
/*  915 */     this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  916 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long nextLong() throws IOException {
/*  930 */     int p = this.peeked;
/*  931 */     if (p == 0) {
/*  932 */       p = doPeek();
/*      */     }
/*      */     
/*  935 */     if (p == 15) {
/*  936 */       this.peeked = 0;
/*  937 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  938 */       return this.peekedLong;
/*      */     } 
/*      */     
/*  941 */     if (p == 16) {
/*  942 */       this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
/*  943 */       this.pos += this.peekedNumberLength;
/*  944 */     } else if (p == 8 || p == 9 || p == 10) {
/*  945 */       if (p == 10) {
/*  946 */         this.peekedString = nextUnquotedValue();
/*      */       } else {
/*  948 */         this.peekedString = nextQuotedValue((p == 8) ? 39 : 34);
/*      */       } 
/*      */       try {
/*  951 */         long l = Long.parseLong(this.peekedString);
/*  952 */         this.peeked = 0;
/*  953 */         this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  954 */         return l;
/*  955 */       } catch (NumberFormatException numberFormatException) {}
/*      */     }
/*      */     else {
/*      */       
/*  959 */       throw new IllegalStateException("Expected a long but was " + peek() + locationString());
/*      */     } 
/*      */     
/*  962 */     this.peeked = 11;
/*  963 */     double asDouble = Double.parseDouble(this.peekedString);
/*  964 */     long result = (long)asDouble;
/*  965 */     if (result != asDouble) {
/*  966 */       throw new NumberFormatException("Expected a long but was " + this.peekedString + locationString());
/*      */     }
/*  968 */     this.peekedString = null;
/*  969 */     this.peeked = 0;
/*  970 */     this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/*  971 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String nextQuotedValue(char quote) throws IOException {
/*  986 */     char[] buffer = this.buffer;
/*  987 */     StringBuilder builder = null;
/*      */     while (true) {
/*  989 */       int p = this.pos;
/*  990 */       int l = this.limit;
/*      */       
/*  992 */       int start = p;
/*  993 */       while (p < l) {
/*  994 */         int c = buffer[p++];
/*      */         
/*  996 */         if (c == quote) {
/*  997 */           this.pos = p;
/*  998 */           int len = p - start - 1;
/*  999 */           if (builder == null) {
/* 1000 */             return new String(buffer, start, len);
/*      */           }
/* 1002 */           builder.append(buffer, start, len);
/* 1003 */           return builder.toString();
/*      */         } 
/* 1005 */         if (c == 92) {
/* 1006 */           this.pos = p;
/* 1007 */           int len = p - start - 1;
/* 1008 */           if (builder == null) {
/* 1009 */             int estimatedLength = (len + 1) * 2;
/* 1010 */             builder = new StringBuilder(Math.max(estimatedLength, 16));
/*      */           } 
/* 1012 */           builder.append(buffer, start, len);
/* 1013 */           builder.append(readEscapeCharacter());
/* 1014 */           p = this.pos;
/* 1015 */           l = this.limit;
/* 1016 */           start = p; continue;
/* 1017 */         }  if (c == 10) {
/* 1018 */           this.lineNumber++;
/* 1019 */           this.lineStart = p;
/*      */         } 
/*      */       } 
/*      */       
/* 1023 */       if (builder == null) {
/* 1024 */         int estimatedLength = (p - start) * 2;
/* 1025 */         builder = new StringBuilder(Math.max(estimatedLength, 16));
/*      */       } 
/* 1027 */       builder.append(buffer, start, p - start);
/* 1028 */       this.pos = p;
/* 1029 */       if (!fillBuffer(1)) {
/* 1030 */         throw syntaxError("Unterminated string");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String nextUnquotedValue() throws IOException {
/* 1040 */     StringBuilder builder = null;
/* 1041 */     int i = 0;
/*      */ 
/*      */     
/*      */     label34: while (true) {
/* 1045 */       for (; this.pos + i < this.limit; i++)
/* 1046 */       { switch (this.buffer[this.pos + i])
/*      */         { case '#':
/*      */           case '/':
/*      */           case ';':
/*      */           case '=':
/*      */           case '\\':
/* 1052 */             checkLenient(); break label34;
/*      */           case '\t': break label34;
/*      */           case '\n': break label34;
/*      */           case '\f': break label34;
/*      */           case '\r': break label34;
/*      */           case ' ': break label34;
/*      */           case ',':
/*      */             break label34;
/*      */           case ':':
/*      */             break label34;
/*      */           case '[':
/*      */             break label34;
/*      */           case ']':
/*      */             break label34;
/*      */           case '{':
/*      */             break label34;
/*      */           case '}':
/* 1069 */             break label34; }  }  if (i < this.buffer.length) {
/* 1070 */         if (fillBuffer(i + 1)) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/* 1078 */       if (builder == null) {
/* 1079 */         builder = new StringBuilder(Math.max(i, 16));
/*      */       }
/* 1081 */       builder.append(this.buffer, this.pos, i);
/* 1082 */       this.pos += i;
/* 1083 */       i = 0;
/* 1084 */       if (!fillBuffer(1)) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */     
/* 1089 */     String result = (null == builder) ? new String(this.buffer, this.pos, i) : builder.append(this.buffer, this.pos, i).toString();
/* 1090 */     this.pos += i;
/* 1091 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private void skipQuotedValue(char quote) throws IOException {
/* 1096 */     char[] buffer = this.buffer;
/*      */     while (true) {
/* 1098 */       int p = this.pos;
/* 1099 */       int l = this.limit;
/*      */       
/* 1101 */       while (p < l) {
/* 1102 */         int c = buffer[p++];
/* 1103 */         if (c == quote) {
/* 1104 */           this.pos = p; return;
/*      */         } 
/* 1106 */         if (c == 92) {
/* 1107 */           this.pos = p;
/* 1108 */           readEscapeCharacter();
/* 1109 */           p = this.pos;
/* 1110 */           l = this.limit; continue;
/* 1111 */         }  if (c == 10) {
/* 1112 */           this.lineNumber++;
/* 1113 */           this.lineStart = p;
/*      */         } 
/*      */       } 
/* 1116 */       this.pos = p;
/* 1117 */       if (!fillBuffer(1))
/* 1118 */         throw syntaxError("Unterminated string"); 
/*      */     } 
/*      */   }
/*      */   private void skipUnquotedValue() throws IOException {
/*      */     do {
/* 1123 */       int i = 0;
/* 1124 */       for (; this.pos + i < this.limit; i++) {
/* 1125 */         switch (this.buffer[this.pos + i]) {
/*      */           case '#':
/*      */           case '/':
/*      */           case ';':
/*      */           case '=':
/*      */           case '\\':
/* 1131 */             checkLenient();
/*      */           case '\t':
/*      */           case '\n':
/*      */           case '\f':
/*      */           case '\r':
/*      */           case ' ':
/*      */           case ',':
/*      */           case ':':
/*      */           case '[':
/*      */           case ']':
/*      */           case '{':
/*      */           case '}':
/* 1143 */             this.pos += i;
/*      */             return;
/*      */         } 
/*      */       } 
/* 1147 */       this.pos += i;
/* 1148 */     } while (fillBuffer(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int nextInt() throws IOException {
/* 1162 */     int p = this.peeked;
/* 1163 */     if (p == 0) {
/* 1164 */       p = doPeek();
/*      */     }
/*      */ 
/*      */     
/* 1168 */     if (p == 15) {
/* 1169 */       int i = (int)this.peekedLong;
/* 1170 */       if (this.peekedLong != i) {
/* 1171 */         throw new NumberFormatException("Expected an int but was " + this.peekedLong + locationString());
/*      */       }
/* 1173 */       this.peeked = 0;
/* 1174 */       this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/* 1175 */       return i;
/*      */     } 
/*      */     
/* 1178 */     if (p == 16) {
/* 1179 */       this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
/* 1180 */       this.pos += this.peekedNumberLength;
/* 1181 */     } else if (p == 8 || p == 9 || p == 10) {
/* 1182 */       if (p == 10) {
/* 1183 */         this.peekedString = nextUnquotedValue();
/*      */       } else {
/* 1185 */         this.peekedString = nextQuotedValue((p == 8) ? 39 : 34);
/*      */       } 
/*      */       try {
/* 1188 */         int i = Integer.parseInt(this.peekedString);
/* 1189 */         this.peeked = 0;
/* 1190 */         this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/* 1191 */         return i;
/* 1192 */       } catch (NumberFormatException numberFormatException) {}
/*      */     }
/*      */     else {
/*      */       
/* 1196 */       throw new IllegalStateException("Expected an int but was " + peek() + locationString());
/*      */     } 
/*      */     
/* 1199 */     this.peeked = 11;
/* 1200 */     double asDouble = Double.parseDouble(this.peekedString);
/* 1201 */     int result = (int)asDouble;
/* 1202 */     if (result != asDouble) {
/* 1203 */       throw new NumberFormatException("Expected an int but was " + this.peekedString + locationString());
/*      */     }
/* 1205 */     this.peekedString = null;
/* 1206 */     this.peeked = 0;
/* 1207 */     this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/* 1208 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/* 1215 */     this.peeked = 0;
/* 1216 */     this.stack[0] = 8;
/* 1217 */     this.stackSize = 1;
/* 1218 */     this.in.close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void skipValue() throws IOException {
/* 1227 */     int count = 0;
/*      */     do {
/* 1229 */       int p = this.peeked;
/* 1230 */       if (p == 0) {
/* 1231 */         p = doPeek();
/*      */       }
/*      */       
/* 1234 */       if (p == 3) {
/* 1235 */         push(1);
/* 1236 */         count++;
/* 1237 */       } else if (p == 1) {
/* 1238 */         push(3);
/* 1239 */         count++;
/* 1240 */       } else if (p == 4) {
/* 1241 */         this.stackSize--;
/* 1242 */         count--;
/* 1243 */       } else if (p == 2) {
/* 1244 */         this.stackSize--;
/* 1245 */         count--;
/* 1246 */       } else if (p == 14 || p == 10) {
/* 1247 */         skipUnquotedValue();
/* 1248 */       } else if (p == 8 || p == 12) {
/* 1249 */         skipQuotedValue('\'');
/* 1250 */       } else if (p == 9 || p == 13) {
/* 1251 */         skipQuotedValue('"');
/* 1252 */       } else if (p == 16) {
/* 1253 */         this.pos += this.peekedNumberLength;
/*      */       } 
/* 1255 */       this.peeked = 0;
/* 1256 */     } while (count != 0);
/*      */     
/* 1258 */     this.pathIndices[this.stackSize - 1] = this.pathIndices[this.stackSize - 1] + 1;
/* 1259 */     this.pathNames[this.stackSize - 1] = "null";
/*      */   }
/*      */   
/*      */   private void push(int newTop) {
/* 1263 */     if (this.stackSize == this.stack.length) {
/* 1264 */       int newLength = this.stackSize * 2;
/* 1265 */       this.stack = Arrays.copyOf(this.stack, newLength);
/* 1266 */       this.pathIndices = Arrays.copyOf(this.pathIndices, newLength);
/* 1267 */       this.pathNames = Arrays.<String>copyOf(this.pathNames, newLength);
/*      */     } 
/* 1269 */     this.stack[this.stackSize++] = newTop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean fillBuffer(int minimum) throws IOException {
/* 1278 */     char[] buffer = this.buffer;
/* 1279 */     this.lineStart -= this.pos;
/* 1280 */     if (this.limit != this.pos) {
/* 1281 */       this.limit -= this.pos;
/* 1282 */       System.arraycopy(buffer, this.pos, buffer, 0, this.limit);
/*      */     } else {
/* 1284 */       this.limit = 0;
/*      */     } 
/*      */     
/* 1287 */     this.pos = 0;
/*      */     int total;
/* 1289 */     while ((total = this.in.read(buffer, this.limit, buffer.length - this.limit)) != -1) {
/* 1290 */       this.limit += total;
/*      */ 
/*      */       
/* 1293 */       if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && buffer[0] == '﻿') {
/* 1294 */         this.pos++;
/* 1295 */         this.lineStart++;
/* 1296 */         minimum++;
/*      */       } 
/*      */       
/* 1299 */       if (this.limit >= minimum) {
/* 1300 */         return true;
/*      */       }
/*      */     } 
/* 1303 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int nextNonWhitespace(boolean throwOnEof) throws IOException {
/* 1321 */     char[] buffer = this.buffer;
/* 1322 */     int p = this.pos;
/* 1323 */     int l = this.limit;
/*      */     while (true) {
/* 1325 */       if (p == l) {
/* 1326 */         this.pos = p;
/* 1327 */         if (!fillBuffer(1)) {
/*      */           break;
/*      */         }
/* 1330 */         p = this.pos;
/* 1331 */         l = this.limit;
/*      */       } 
/*      */       
/* 1334 */       int c = buffer[p++];
/* 1335 */       if (c == 10) {
/* 1336 */         this.lineNumber++;
/* 1337 */         this.lineStart = p; continue;
/*      */       } 
/* 1339 */       if (c == 32 || c == 13 || c == 9) {
/*      */         continue;
/*      */       }
/*      */       
/* 1343 */       if (c == 47) {
/* 1344 */         this.pos = p;
/* 1345 */         if (p == l) {
/* 1346 */           this.pos--;
/* 1347 */           boolean charsLoaded = fillBuffer(2);
/* 1348 */           this.pos++;
/* 1349 */           if (!charsLoaded) {
/* 1350 */             return c;
/*      */           }
/*      */         } 
/*      */         
/* 1354 */         checkLenient();
/* 1355 */         char peek = buffer[this.pos];
/* 1356 */         switch (peek) {
/*      */           
/*      */           case '*':
/* 1359 */             this.pos++;
/* 1360 */             if (!skipTo("*/")) {
/* 1361 */               throw syntaxError("Unterminated comment");
/*      */             }
/* 1363 */             p = this.pos + 2;
/* 1364 */             l = this.limit;
/*      */             continue;
/*      */ 
/*      */           
/*      */           case '/':
/* 1369 */             this.pos++;
/* 1370 */             skipToEndOfLine();
/* 1371 */             p = this.pos;
/* 1372 */             l = this.limit;
/*      */             continue;
/*      */         } 
/*      */         
/* 1376 */         return c;
/*      */       } 
/* 1378 */       if (c == 35) {
/* 1379 */         this.pos = p;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1385 */         checkLenient();
/* 1386 */         skipToEndOfLine();
/* 1387 */         p = this.pos;
/* 1388 */         l = this.limit; continue;
/*      */       } 
/* 1390 */       this.pos = p;
/* 1391 */       return c;
/*      */     } 
/*      */     
/* 1394 */     if (throwOnEof) {
/* 1395 */       throw new EOFException("End of input" + locationString());
/*      */     }
/* 1397 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkLenient() throws IOException {
/* 1402 */     if (!this.lenient) {
/* 1403 */       throw syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void skipToEndOfLine() throws IOException {
/* 1413 */     while (this.pos < this.limit || fillBuffer(1)) {
/* 1414 */       char c = this.buffer[this.pos++];
/* 1415 */       if (c == '\n') {
/* 1416 */         this.lineNumber++;
/* 1417 */         this.lineStart = this.pos; break;
/*      */       } 
/* 1419 */       if (c == '\r') {
/*      */         break;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean skipTo(String toFind) throws IOException {
/* 1429 */     int length = toFind.length();
/*      */     
/* 1431 */     for (; this.pos + length <= this.limit || fillBuffer(length); this.pos++) {
/* 1432 */       if (this.buffer[this.pos] == '\n') {
/* 1433 */         this.lineNumber++;
/* 1434 */         this.lineStart = this.pos + 1;
/*      */       } else {
/*      */         
/* 1437 */         int c = 0; while (true) { if (c < length) {
/* 1438 */             if (this.buffer[this.pos + c] != toFind.charAt(c))
/*      */               break;  c++;
/*      */             continue;
/*      */           } 
/* 1442 */           return true; } 
/*      */       } 
/* 1444 */     }  return false;
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1448 */     return getClass().getSimpleName() + locationString();
/*      */   }
/*      */   
/*      */   String locationString() {
/* 1452 */     int line = this.lineNumber + 1;
/* 1453 */     int column = this.pos - this.lineStart + 1;
/* 1454 */     return " at line " + line + " column " + column + " path " + getPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPath() {
/* 1462 */     StringBuilder result = (new StringBuilder()).append('$');
/* 1463 */     for (int i = 0, size = this.stackSize; i < size; i++) {
/* 1464 */       switch (this.stack[i]) {
/*      */         case 1:
/*      */         case 2:
/* 1467 */           result.append('[').append(this.pathIndices[i]).append(']');
/*      */           break;
/*      */         
/*      */         case 3:
/*      */         case 4:
/*      */         case 5:
/* 1473 */           result.append('.');
/* 1474 */           if (this.pathNames[i] != null) {
/* 1475 */             result.append(this.pathNames[i]);
/*      */           }
/*      */           break;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     } 
/* 1485 */     return result.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char readEscapeCharacter() throws IOException {
/*      */     char result;
/*      */     int i, end;
/* 1498 */     if (this.pos == this.limit && !fillBuffer(1)) {
/* 1499 */       throw syntaxError("Unterminated escape sequence");
/*      */     }
/*      */     
/* 1502 */     char escaped = this.buffer[this.pos++];
/* 1503 */     switch (escaped) {
/*      */       case 'u':
/* 1505 */         if (this.pos + 4 > this.limit && !fillBuffer(4)) {
/* 1506 */           throw syntaxError("Unterminated escape sequence");
/*      */         }
/*      */         
/* 1509 */         result = Character.MIN_VALUE;
/* 1510 */         for (i = this.pos, end = i + 4; i < end; i++) {
/* 1511 */           char c = this.buffer[i];
/* 1512 */           result = (char)(result << 4);
/* 1513 */           if (c >= '0' && c <= '9') {
/* 1514 */             result = (char)(result + c - 48);
/* 1515 */           } else if (c >= 'a' && c <= 'f') {
/* 1516 */             result = (char)(result + c - 97 + 10);
/* 1517 */           } else if (c >= 'A' && c <= 'F') {
/* 1518 */             result = (char)(result + c - 65 + 10);
/*      */           } else {
/* 1520 */             throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
/*      */           } 
/*      */         } 
/* 1523 */         this.pos += 4;
/* 1524 */         return result;
/*      */       
/*      */       case 't':
/* 1527 */         return '\t';
/*      */       
/*      */       case 'b':
/* 1530 */         return '\b';
/*      */       
/*      */       case 'n':
/* 1533 */         return '\n';
/*      */       
/*      */       case 'r':
/* 1536 */         return '\r';
/*      */       
/*      */       case 'f':
/* 1539 */         return '\f';
/*      */       
/*      */       case '\n':
/* 1542 */         this.lineNumber++;
/* 1543 */         this.lineStart = this.pos;
/*      */ 
/*      */       
/*      */       case '"':
/*      */       case '\'':
/*      */       case '/':
/*      */       case '\\':
/* 1550 */         return escaped;
/*      */     } 
/*      */     
/* 1553 */     throw syntaxError("Invalid escape sequence");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private IOException syntaxError(String message) throws IOException {
/* 1562 */     throw new MalformedJsonException(message + locationString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void consumeNonExecutePrefix() throws IOException {
/* 1570 */     nextNonWhitespace(true);
/*      */ 
/*      */     
/* 1573 */     int p = --this.pos;
/* 1574 */     if (p + 5 > this.limit && !fillBuffer(5)) {
/*      */       return;
/*      */     }
/*      */     
/* 1578 */     char[] buf = this.buffer;
/* 1579 */     if (buf[p] != ')' || buf[p + 1] != ']' || buf[p + 2] != '}' || buf[p + 3] != '\'' || buf[p + 4] != '\n') {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1584 */     this.pos += 5;
/*      */   }
/*      */   
/*      */   static {
/* 1588 */     JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
/*      */         public void promoteNameToValue(JsonReader reader) throws IOException {
/* 1590 */           if (reader instanceof JsonTreeReader) {
/* 1591 */             ((JsonTreeReader)reader).promoteNameToValue();
/*      */             return;
/*      */           } 
/* 1594 */           int p = reader.peeked;
/* 1595 */           if (p == 0) {
/* 1596 */             p = reader.doPeek();
/*      */           }
/* 1598 */           if (p == 13) {
/* 1599 */             reader.peeked = 9;
/* 1600 */           } else if (p == 12) {
/* 1601 */             reader.peeked = 8;
/* 1602 */           } else if (p == 14) {
/* 1603 */             reader.peeked = 10;
/*      */           } else {
/* 1605 */             throw new IllegalStateException("Expected a name but was " + reader
/* 1606 */                 .peek() + reader.locationString());
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\stream\JsonReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */