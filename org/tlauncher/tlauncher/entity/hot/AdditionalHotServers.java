/*    */ package org.tlauncher.tlauncher.entity.hot;
/*    */ 
/*    */ public class AdditionalHotServers {
/*    */   private List<AdditionalHotServer> list;
/*    */   private HotBanner upBanner;
/*    */   
/*  7 */   public void setList(List<AdditionalHotServer> list) { this.list = list; } private HotBanner downBanner; private boolean shuffle; public void setUpBanner(HotBanner upBanner) { this.upBanner = upBanner; } public void setDownBanner(HotBanner downBanner) { this.downBanner = downBanner; } public void setShuffle(boolean shuffle) { this.shuffle = shuffle; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof AdditionalHotServers)) return false;  AdditionalHotServers other = (AdditionalHotServers)o; if (!other.canEqual(this)) return false;  Object<AdditionalHotServer> this$list = (Object<AdditionalHotServer>)getList(), other$list = (Object<AdditionalHotServer>)other.getList(); if ((this$list == null) ? (other$list != null) : !this$list.equals(other$list)) return false;  Object this$upBanner = getUpBanner(), other$upBanner = other.getUpBanner(); if ((this$upBanner == null) ? (other$upBanner != null) : !this$upBanner.equals(other$upBanner)) return false;  Object this$downBanner = getDownBanner(), other$downBanner = other.getDownBanner(); return ((this$downBanner == null) ? (other$downBanner != null) : !this$downBanner.equals(other$downBanner)) ? false : (!(isShuffle() != other.isShuffle())); } protected boolean canEqual(Object other) { return other instanceof AdditionalHotServers; } public int hashCode() { int PRIME = 59; result = 1; Object<AdditionalHotServer> $list = (Object<AdditionalHotServer>)getList(); result = result * 59 + (($list == null) ? 43 : $list.hashCode()); Object $upBanner = getUpBanner(); result = result * 59 + (($upBanner == null) ? 43 : $upBanner.hashCode()); Object $downBanner = getDownBanner(); result = result * 59 + (($downBanner == null) ? 43 : $downBanner.hashCode()); return result * 59 + (isShuffle() ? 79 : 97); } public String toString() { return "AdditionalHotServers(list=" + getList() + ", upBanner=" + getUpBanner() + ", downBanner=" + getDownBanner() + ", shuffle=" + isShuffle() + ")"; }
/*    */   
/*  9 */   public List<AdditionalHotServer> getList() { return this.list; }
/* 10 */   public HotBanner getUpBanner() { return this.upBanner; }
/* 11 */   public HotBanner getDownBanner() { return this.downBanner; } public boolean isShuffle() {
/* 12 */     return this.shuffle;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\hot\AdditionalHotServers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */