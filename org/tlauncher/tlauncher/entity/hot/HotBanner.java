/*   */ package org.tlauncher.tlauncher.entity.hot;
/*   */ 
/*   */ public class HotBanner extends Banner {
/*   */   private String mouseOnImage;
/*   */   
/* 6 */   public String toString() { return "HotBanner(mouseOnImage=" + getMouseOnImage() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object $mouseOnImage = getMouseOnImage(); return result * 59 + (($mouseOnImage == null) ? 43 : $mouseOnImage.hashCode()); } protected boolean canEqual(Object other) { return other instanceof HotBanner; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof HotBanner)) return false;  HotBanner other = (HotBanner)o; if (!other.canEqual(this)) return false;  Object this$mouseOnImage = getMouseOnImage(), other$mouseOnImage = other.getMouseOnImage(); return !((this$mouseOnImage == null) ? (other$mouseOnImage != null) : !this$mouseOnImage.equals(other$mouseOnImage)); } public void setMouseOnImage(String mouseOnImage) { this.mouseOnImage = mouseOnImage; }
/*   */    public String getMouseOnImage() {
/* 8 */     return this.mouseOnImage;
/*   */   }
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\hot\HotBanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */