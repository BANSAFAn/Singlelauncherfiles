/*     */ package org.tlauncher.util;
/*     */ 
/*     */ import org.tlauncher.exceptions.ParseException;
/*     */ 
/*     */ public class IntegerArray
/*     */ {
/*     */   public static final char defaultDelimiter = ';';
/*     */   private final int[] integers;
/*     */   private final char delimiter;
/*     */   private final int length;
/*     */   
/*     */   private IntegerArray(char del, int... values) {
/*  13 */     this.delimiter = del;
/*  14 */     this.length = values.length;
/*  15 */     this.integers = new int[this.length];
/*  16 */     System.arraycopy(values, 0, this.integers, 0, this.length);
/*     */   }
/*     */   
/*     */   public IntegerArray(int... values) {
/*  20 */     this(';', values);
/*     */   }
/*     */   
/*     */   public int get(int pos) {
/*  24 */     if (pos < 0 || pos >= this.length) {
/*  25 */       throw new ArrayIndexOutOfBoundsException("Invalid position (" + pos + " / " + this.length + ")!");
/*     */     }
/*     */     
/*  28 */     return this.integers[pos];
/*     */   }
/*     */   
/*     */   public void set(int pos, int val) {
/*  32 */     if (pos < 0 || pos >= this.length) {
/*  33 */       throw new ArrayIndexOutOfBoundsException("Invalid position (" + pos + " / " + this.length + ")!");
/*     */     }
/*     */     
/*  36 */     this.integers[pos] = val;
/*     */   }
/*     */   
/*     */   public int size() {
/*  40 */     return this.length;
/*     */   }
/*     */   
/*     */   public int[] toArray() {
/*  44 */     int[] r = new int[this.length];
/*     */     
/*  46 */     System.arraycopy(this.integers, 0, r, 0, this.length);
/*  47 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  52 */     StringBuilder sb = new StringBuilder();
/*  53 */     boolean first = true;
/*     */     
/*  55 */     for (int i : this.integers) {
/*  56 */       if (!first) {
/*  57 */         sb.append(this.delimiter);
/*     */       } else {
/*  59 */         first = false;
/*  60 */       }  sb.append(i);
/*     */     } 
/*     */     
/*  63 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static IntegerArray parseIntegerArray(String val, char del) throws ParseException {
/*  67 */     if (val == null) {
/*  68 */       throw new ParseException("String cannot be NULL!");
/*     */     }
/*  70 */     if (val.length() <= 1) {
/*  71 */       throw new ParseException("String mustn't equal or be less than delimiter!");
/*     */     }
/*     */     
/*  74 */     String regexp = "(?<!\\\\)";
/*  75 */     if (del != 'x')
/*  76 */       regexp = regexp + "\\"; 
/*  77 */     regexp = regexp + del;
/*     */     
/*  79 */     String[] ints = val.split(regexp);
/*  80 */     int l = ints.length;
/*  81 */     int[] arr = new int[l];
/*  82 */     for (int i = 0; i < l; i++) {
/*     */       int cur; try {
/*  84 */         cur = Integer.parseInt(ints[i]);
/*  85 */       } catch (NumberFormatException e) {
/*  86 */         U.log(new Object[] { "Cannot parse integer (iteration: " + i + ")", e });
/*  87 */         throw new ParseException("Cannot parse integer (iteration: " + i + ")", e);
/*     */       } 
/*     */ 
/*     */       
/*  91 */       arr[i] = cur;
/*     */     } 
/*     */     
/*  94 */     return new IntegerArray(del, arr);
/*     */   }
/*     */ 
/*     */   
/*     */   public static IntegerArray parseIntegerArray(String val) throws ParseException {
/*  99 */     return parseIntegerArray(val, ';');
/*     */   }
/*     */   
/*     */   private static int[] toArray(String val, char del) throws ParseException {
/* 103 */     IntegerArray arr = parseIntegerArray(val, del);
/* 104 */     return arr.toArray();
/*     */   }
/*     */   
/*     */   public static int[] toArray(String val) throws ParseException {
/* 108 */     return toArray(val, ';');
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\IntegerArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */