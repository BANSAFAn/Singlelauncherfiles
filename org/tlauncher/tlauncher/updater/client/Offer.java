/*    */ package org.tlauncher.tlauncher.updater.client;
/*    */ 
/*    */ public class Offer {
/*    */   private String client;
/*    */   private String offer;
/*    */   private String installer;
/*    */   private int startCheckboxSouth;
/*    */   
/*  9 */   public void setClient(String client) { this.client = client; } private List<PointOffer> checkBoxes; private Map<String, String> topText; private Map<String, String> downText; private Map<String, String> args; public void setOffer(String offer) { this.offer = offer; } public void setInstaller(String installer) { this.installer = installer; } public void setStartCheckboxSouth(int startCheckboxSouth) { this.startCheckboxSouth = startCheckboxSouth; } public void setCheckBoxes(List<PointOffer> checkBoxes) { this.checkBoxes = checkBoxes; } public void setTopText(Map<String, String> topText) { this.topText = topText; } public void setDownText(Map<String, String> downText) { this.downText = downText; } public void setArgs(Map<String, String> args) { this.args = args; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Offer)) return false;  Offer other = (Offer)o; if (!other.canEqual(this)) return false;  Object this$client = getClient(), other$client = other.getClient(); if ((this$client == null) ? (other$client != null) : !this$client.equals(other$client)) return false;  Object this$offer = getOffer(), other$offer = other.getOffer(); if ((this$offer == null) ? (other$offer != null) : !this$offer.equals(other$offer)) return false;  Object this$installer = getInstaller(), other$installer = other.getInstaller(); if ((this$installer == null) ? (other$installer != null) : !this$installer.equals(other$installer)) return false;  if (getStartCheckboxSouth() != other.getStartCheckboxSouth()) return false;  Object<PointOffer> this$checkBoxes = (Object<PointOffer>)getCheckBoxes(), other$checkBoxes = (Object<PointOffer>)other.getCheckBoxes(); if ((this$checkBoxes == null) ? (other$checkBoxes != null) : !this$checkBoxes.equals(other$checkBoxes)) return false;  Object<String, String> this$topText = (Object<String, String>)getTopText(), other$topText = (Object<String, String>)other.getTopText(); if ((this$topText == null) ? (other$topText != null) : !this$topText.equals(other$topText)) return false;  Object<String, String> this$downText = (Object<String, String>)getDownText(), other$downText = (Object<String, String>)other.getDownText(); if ((this$downText == null) ? (other$downText != null) : !this$downText.equals(other$downText)) return false;  Object<String, String> this$args = (Object<String, String>)getArgs(), other$args = (Object<String, String>)other.getArgs(); return !((this$args == null) ? (other$args != null) : !this$args.equals(other$args)); } protected boolean canEqual(Object other) { return other instanceof Offer; } public int hashCode() { int PRIME = 59; result = 1; Object $client = getClient(); result = result * 59 + (($client == null) ? 43 : $client.hashCode()); Object $offer = getOffer(); result = result * 59 + (($offer == null) ? 43 : $offer.hashCode()); Object $installer = getInstaller(); result = result * 59 + (($installer == null) ? 43 : $installer.hashCode()); result = result * 59 + getStartCheckboxSouth(); Object<PointOffer> $checkBoxes = (Object<PointOffer>)getCheckBoxes(); result = result * 59 + (($checkBoxes == null) ? 43 : $checkBoxes.hashCode()); Object<String, String> $topText = (Object<String, String>)getTopText(); result = result * 59 + (($topText == null) ? 43 : $topText.hashCode()); Object<String, String> $downText = (Object<String, String>)getDownText(); result = result * 59 + (($downText == null) ? 43 : $downText.hashCode()); Object<String, String> $args = (Object<String, String>)getArgs(); return result * 59 + (($args == null) ? 43 : $args.hashCode()); } public String toString() {
/* 10 */     return "Offer(client=" + getClient() + ", offer=" + getOffer() + ", installer=" + getInstaller() + ", startCheckboxSouth=" + getStartCheckboxSouth() + ", checkBoxes=" + getCheckBoxes() + ", topText=" + getTopText() + ", downText=" + getDownText() + ", args=" + getArgs() + ")";
/*    */   }
/* 12 */   public String getClient() { return this.client; }
/* 13 */   public String getOffer() { return this.offer; }
/* 14 */   public String getInstaller() { return this.installer; }
/* 15 */   public int getStartCheckboxSouth() { return this.startCheckboxSouth; }
/* 16 */   public List<PointOffer> getCheckBoxes() { return this.checkBoxes; }
/* 17 */   public Map<String, String> getTopText() { return this.topText; }
/* 18 */   public Map<String, String> getDownText() { return this.downText; } public Map<String, String> getArgs() {
/* 19 */     return this.args;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\client\Offer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */