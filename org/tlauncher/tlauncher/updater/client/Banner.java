/*   */ package org.tlauncher.tlauncher.updater.client;
/*   */ public class Banner { private String image;
/*   */   private String clickLink;
/*   */   
/* 5 */   public void setImage(String image) { this.image = image; } public void setClickLink(String clickLink) { this.clickLink = clickLink; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Banner)) return false;  Banner other = (Banner)o; if (!other.canEqual(this)) return false;  Object this$image = getImage(), other$image = other.getImage(); if ((this$image == null) ? (other$image != null) : !this$image.equals(other$image)) return false;  Object this$clickLink = getClickLink(), other$clickLink = other.getClickLink(); return !((this$clickLink == null) ? (other$clickLink != null) : !this$clickLink.equals(other$clickLink)); } protected boolean canEqual(Object other) { return other instanceof Banner; } public int hashCode() { int PRIME = 59; result = 1; Object $image = getImage(); result = result * 59 + (($image == null) ? 43 : $image.hashCode()); Object $clickLink = getClickLink(); return result * 59 + (($clickLink == null) ? 43 : $clickLink.hashCode()); } public String toString() { return "Banner(image=" + getImage() + ", clickLink=" + getClickLink() + ")"; }
/*   */   
/* 7 */   public String getImage() { return this.image; } public String getClickLink() {
/* 8 */     return this.clickLink;
/*   */   } }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\client\Banner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */