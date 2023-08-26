/*    */ package org.tlauncher.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Range<T extends Number>
/*    */ {
/*    */   private final T minValue;
/*    */   private final T maxValue;
/*    */   private final boolean including;
/*    */   private final double doubleMin;
/*    */   private final double doubleMax;
/*    */   
/*    */   public Range(T minValue, T maxValue, boolean including) {
/* 14 */     if (minValue == null) {
/* 15 */       throw new NullPointerException("min");
/*    */     }
/* 17 */     if (maxValue == null) {
/* 18 */       throw new NullPointerException("max");
/*    */     }
/* 20 */     this.minValue = minValue;
/* 21 */     this.maxValue = maxValue;
/*    */     
/* 23 */     this.doubleMin = minValue.doubleValue();
/* 24 */     this.doubleMax = maxValue.doubleValue();
/*    */     
/* 26 */     if (this.doubleMin >= this.doubleMax) {
/* 27 */       throw new IllegalArgumentException("min >= max");
/*    */     }
/* 29 */     this.including = including;
/*    */   }
/*    */   
/*    */   public Range(T minValue, T maxValue) {
/* 33 */     this(minValue, maxValue, true);
/*    */   }
/*    */   
/*    */   public T getMinValue() {
/* 37 */     return this.minValue;
/*    */   }
/*    */   
/*    */   public T getMaxValue() {
/* 41 */     return this.maxValue;
/*    */   }
/*    */   
/*    */   public boolean getIncluding() {
/* 45 */     return this.including;
/*    */   }
/*    */   
/*    */   public RangeDifference getDifference(T value) {
/* 49 */     if (value == null) {
/* 50 */       throw new NullPointerException("value");
/*    */     }
/* 52 */     double doubleValue = value.doubleValue();
/* 53 */     double min = doubleValue - this.doubleMin;
/*    */     
/* 55 */     if (min == 0.0D) {
/* 56 */       return this.including ? RangeDifference.FITS : RangeDifference.LESS;
/*    */     }
/* 58 */     if (min < 0.0D) {
/* 59 */       return RangeDifference.LESS;
/*    */     }
/* 61 */     double max = doubleValue - this.doubleMax;
/*    */     
/* 63 */     if (max == 0.0D) {
/* 64 */       return this.including ? RangeDifference.FITS : RangeDifference.GREATER;
/*    */     }
/* 66 */     if (max > 0.0D) {
/* 67 */       return RangeDifference.GREATER;
/*    */     }
/* 69 */     return RangeDifference.FITS;
/*    */   }
/*    */   
/*    */   public boolean fits(T value) {
/* 73 */     return (getDifference(value) == RangeDifference.FITS);
/*    */   }
/*    */   
/*    */   public enum RangeDifference {
/* 77 */     LESS, FITS, GREATER;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\Range.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */