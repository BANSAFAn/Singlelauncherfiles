/*    */ package org.tlauncher.util.pastebin;
/*    */ 
/*    */ public enum ExpireDate {
/*  4 */   NEVER("N"),
/*  5 */   TEN_MINUTES("10M"),
/*  6 */   ONE_HOUR("1H"),
/*  7 */   ONE_DAY("1D"),
/*  8 */   ONE_WEEK("1W"),
/*  9 */   TWO_WEEKS("2W"),
/* 10 */   ONE_MONTH("1M");
/*    */   
/*    */   private String value;
/*    */   
/*    */   ExpireDate(String value) {
/* 15 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 19 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\pastebin\ExpireDate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */