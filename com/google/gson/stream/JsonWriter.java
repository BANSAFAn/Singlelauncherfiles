/*     */ package com.google.gson.stream;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonWriter
/*     */   implements Closeable, Flushable
/*     */ {
/* 146 */   private static final String[] REPLACEMENT_CHARS = new String[128]; static {
/* 147 */     for (int i = 0; i <= 31; i++) {
/* 148 */       REPLACEMENT_CHARS[i] = String.format("\\u%04x", new Object[] { Integer.valueOf(i) });
/*     */     } 
/* 150 */     REPLACEMENT_CHARS[34] = "\\\"";
/* 151 */     REPLACEMENT_CHARS[92] = "\\\\";
/* 152 */     REPLACEMENT_CHARS[9] = "\\t";
/* 153 */     REPLACEMENT_CHARS[8] = "\\b";
/* 154 */     REPLACEMENT_CHARS[10] = "\\n";
/* 155 */     REPLACEMENT_CHARS[13] = "\\r";
/* 156 */     REPLACEMENT_CHARS[12] = "\\f";
/* 157 */   } private static final String[] HTML_SAFE_REPLACEMENT_CHARS = (String[])REPLACEMENT_CHARS.clone(); static {
/* 158 */     HTML_SAFE_REPLACEMENT_CHARS[60] = "\\u003c";
/* 159 */     HTML_SAFE_REPLACEMENT_CHARS[62] = "\\u003e";
/* 160 */     HTML_SAFE_REPLACEMENT_CHARS[38] = "\\u0026";
/* 161 */     HTML_SAFE_REPLACEMENT_CHARS[61] = "\\u003d";
/* 162 */     HTML_SAFE_REPLACEMENT_CHARS[39] = "\\u0027";
/*     */   }
/*     */ 
/*     */   
/*     */   private final Writer out;
/*     */   
/* 168 */   private int[] stack = new int[32];
/* 169 */   private int stackSize = 0; private String indent;
/*     */   public JsonWriter(Writer out) {
/* 171 */     push(6);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     this.separator = ":";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     this.serializeNulls = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     if (out == null) {
/* 200 */       throw new NullPointerException("out == null");
/*     */     }
/* 202 */     this.out = out;
/*     */   }
/*     */ 
/*     */   
/*     */   private String separator;
/*     */   
/*     */   private boolean lenient;
/*     */   private boolean htmlSafe;
/*     */   private String deferredName;
/*     */   private boolean serializeNulls;
/*     */   
/*     */   public final void setIndent(String indent) {
/* 214 */     if (indent.length() == 0) {
/* 215 */       this.indent = null;
/* 216 */       this.separator = ":";
/*     */     } else {
/* 218 */       this.indent = indent;
/* 219 */       this.separator = ": ";
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
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setLenient(boolean lenient) {
/* 236 */     this.lenient = lenient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLenient() {
/* 243 */     return this.lenient;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setHtmlSafe(boolean htmlSafe) {
/* 254 */     this.htmlSafe = htmlSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isHtmlSafe() {
/* 262 */     return this.htmlSafe;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSerializeNulls(boolean serializeNulls) {
/* 270 */     this.serializeNulls = serializeNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean getSerializeNulls() {
/* 278 */     return this.serializeNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter beginArray() throws IOException {
/* 288 */     writeDeferredName();
/* 289 */     return open(1, '[');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter endArray() throws IOException {
/* 298 */     return close(1, 2, ']');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter beginObject() throws IOException {
/* 308 */     writeDeferredName();
/* 309 */     return open(3, '{');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter endObject() throws IOException {
/* 318 */     return close(3, 5, '}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JsonWriter open(int empty, char openBracket) throws IOException {
/* 326 */     beforeValue();
/* 327 */     push(empty);
/* 328 */     this.out.write(openBracket);
/* 329 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JsonWriter close(int empty, int nonempty, char closeBracket) throws IOException {
/* 338 */     int context = peek();
/* 339 */     if (context != nonempty && context != empty) {
/* 340 */       throw new IllegalStateException("Nesting problem.");
/*     */     }
/* 342 */     if (this.deferredName != null) {
/* 343 */       throw new IllegalStateException("Dangling name: " + this.deferredName);
/*     */     }
/*     */     
/* 346 */     this.stackSize--;
/* 347 */     if (context == nonempty) {
/* 348 */       newline();
/*     */     }
/* 350 */     this.out.write(closeBracket);
/* 351 */     return this;
/*     */   }
/*     */   
/*     */   private void push(int newTop) {
/* 355 */     if (this.stackSize == this.stack.length) {
/* 356 */       this.stack = Arrays.copyOf(this.stack, this.stackSize * 2);
/*     */     }
/* 358 */     this.stack[this.stackSize++] = newTop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int peek() {
/* 365 */     if (this.stackSize == 0) {
/* 366 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 368 */     return this.stack[this.stackSize - 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void replaceTop(int topOfStack) {
/* 375 */     this.stack[this.stackSize - 1] = topOfStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter name(String name) throws IOException {
/* 385 */     if (name == null) {
/* 386 */       throw new NullPointerException("name == null");
/*     */     }
/* 388 */     if (this.deferredName != null) {
/* 389 */       throw new IllegalStateException();
/*     */     }
/* 391 */     if (this.stackSize == 0) {
/* 392 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 394 */     this.deferredName = name;
/* 395 */     return this;
/*     */   }
/*     */   
/*     */   private void writeDeferredName() throws IOException {
/* 399 */     if (this.deferredName != null) {
/* 400 */       beforeName();
/* 401 */       string(this.deferredName);
/* 402 */       this.deferredName = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(String value) throws IOException {
/* 413 */     if (value == null) {
/* 414 */       return nullValue();
/*     */     }
/* 416 */     writeDeferredName();
/* 417 */     beforeValue();
/* 418 */     string(value);
/* 419 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter jsonValue(String value) throws IOException {
/* 430 */     if (value == null) {
/* 431 */       return nullValue();
/*     */     }
/* 433 */     writeDeferredName();
/* 434 */     beforeValue();
/* 435 */     this.out.append(value);
/* 436 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter nullValue() throws IOException {
/* 445 */     if (this.deferredName != null) {
/* 446 */       if (this.serializeNulls) {
/* 447 */         writeDeferredName();
/*     */       } else {
/* 449 */         this.deferredName = null;
/* 450 */         return this;
/*     */       } 
/*     */     }
/* 453 */     beforeValue();
/* 454 */     this.out.write("null");
/* 455 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(boolean value) throws IOException {
/* 464 */     writeDeferredName();
/* 465 */     beforeValue();
/* 466 */     this.out.write(value ? "true" : "false");
/* 467 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(Boolean value) throws IOException {
/* 476 */     if (value == null) {
/* 477 */       return nullValue();
/*     */     }
/* 479 */     writeDeferredName();
/* 480 */     beforeValue();
/* 481 */     this.out.write(value.booleanValue() ? "true" : "false");
/* 482 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(double value) throws IOException {
/* 493 */     writeDeferredName();
/* 494 */     if (!this.lenient && (Double.isNaN(value) || Double.isInfinite(value))) {
/* 495 */       throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
/*     */     }
/* 497 */     beforeValue();
/* 498 */     this.out.append(Double.toString(value));
/* 499 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(long value) throws IOException {
/* 508 */     writeDeferredName();
/* 509 */     beforeValue();
/* 510 */     this.out.write(Long.toString(value));
/* 511 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonWriter value(Number value) throws IOException {
/* 522 */     if (value == null) {
/* 523 */       return nullValue();
/*     */     }
/*     */     
/* 526 */     writeDeferredName();
/* 527 */     String string = value.toString();
/* 528 */     if (!this.lenient && (string
/* 529 */       .equals("-Infinity") || string.equals("Infinity") || string.equals("NaN"))) {
/* 530 */       throw new IllegalArgumentException("Numeric values must be finite, but was " + value);
/*     */     }
/* 532 */     beforeValue();
/* 533 */     this.out.append(string);
/* 534 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 542 */     if (this.stackSize == 0) {
/* 543 */       throw new IllegalStateException("JsonWriter is closed.");
/*     */     }
/* 545 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 554 */     this.out.close();
/*     */     
/* 556 */     int size = this.stackSize;
/* 557 */     if (size > 1 || (size == 1 && this.stack[size - 1] != 7)) {
/* 558 */       throw new IOException("Incomplete document");
/*     */     }
/* 560 */     this.stackSize = 0;
/*     */   }
/*     */   
/*     */   private void string(String value) throws IOException {
/* 564 */     String[] replacements = this.htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
/* 565 */     this.out.write(34);
/* 566 */     int last = 0;
/* 567 */     int length = value.length();
/* 568 */     for (int i = 0; i < length; i++) {
/* 569 */       String replacement; char c = value.charAt(i);
/*     */       
/* 571 */       if (c < '') {
/* 572 */         replacement = replacements[c];
/* 573 */         if (replacement == null) {
/*     */           continue;
/*     */         }
/* 576 */       } else if (c == ' ') {
/* 577 */         replacement = "\\u2028";
/* 578 */       } else if (c == ' ') {
/* 579 */         replacement = "\\u2029";
/*     */       } else {
/*     */         continue;
/*     */       } 
/* 583 */       if (last < i) {
/* 584 */         this.out.write(value, last, i - last);
/*     */       }
/* 586 */       this.out.write(replacement);
/* 587 */       last = i + 1; continue;
/*     */     } 
/* 589 */     if (last < length) {
/* 590 */       this.out.write(value, last, length - last);
/*     */     }
/* 592 */     this.out.write(34);
/*     */   }
/*     */   
/*     */   private void newline() throws IOException {
/* 596 */     if (this.indent == null) {
/*     */       return;
/*     */     }
/*     */     
/* 600 */     this.out.write(10);
/* 601 */     for (int i = 1, size = this.stackSize; i < size; i++) {
/* 602 */       this.out.write(this.indent);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void beforeName() throws IOException {
/* 611 */     int context = peek();
/* 612 */     if (context == 5) {
/* 613 */       this.out.write(44);
/* 614 */     } else if (context != 3) {
/* 615 */       throw new IllegalStateException("Nesting problem.");
/*     */     } 
/* 617 */     newline();
/* 618 */     replaceTop(4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void beforeValue() throws IOException {
/* 628 */     switch (peek()) {
/*     */       case 7:
/* 630 */         if (!this.lenient) {
/* 631 */           throw new IllegalStateException("JSON must have only one top-level value.");
/*     */         }
/*     */ 
/*     */       
/*     */       case 6:
/* 636 */         replaceTop(7);
/*     */         return;
/*     */       
/*     */       case 1:
/* 640 */         replaceTop(2);
/* 641 */         newline();
/*     */         return;
/*     */       
/*     */       case 2:
/* 645 */         this.out.append(',');
/* 646 */         newline();
/*     */         return;
/*     */       
/*     */       case 4:
/* 650 */         this.out.append(this.separator);
/* 651 */         replaceTop(5);
/*     */         return;
/*     */     } 
/*     */     
/* 655 */     throw new IllegalStateException("Nesting problem.");
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\com\google\gson\stream\JsonWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */