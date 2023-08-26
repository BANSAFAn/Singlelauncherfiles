/*    */ package org.tlauncher.tlauncher.ui.model;
/*    */ public class CurrentUserPosition { private volatile byte bad;
/*    */   private volatile byte good;
/*    */   
/*  5 */   public void setBad(byte bad) { this.bad = bad; } public void setGood(byte good) { this.good = good; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof CurrentUserPosition)) return false;  CurrentUserPosition other = (CurrentUserPosition)o; return !other.canEqual(this) ? false : ((getBad() != other.getBad()) ? false : (!(getGood() != other.getGood()))); } protected boolean canEqual(Object other) { return other instanceof CurrentUserPosition; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getBad(); return result * 59 + getGood(); } public String toString() { return "CurrentUserPosition(bad=" + getBad() + ", good=" + getGood() + ")"; }
/*    */   
/*  7 */   public byte getBad() { return this.bad; } public byte getGood() {
/*  8 */     return this.good;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void update(boolean position, boolean added) {
/* 16 */     if (position) {
/* 17 */       if (added) {
/* 18 */         this.good = 1;
/*    */       } else {
/* 20 */         this.good = -1;
/* 21 */       }  this.bad = 0;
/*    */     } else {
/* 23 */       if (added) {
/* 24 */         this.bad = 1;
/*    */       } else {
/* 26 */         this.bad = -1;
/* 27 */       }  this.good = 0;
/*    */     } 
/*    */   }
/*    */   
/*    */   public byte getByPosition(boolean pos) {
/* 32 */     return pos ? this.good : this.bad;
/*    */   } }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\model\CurrentUserPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */