/*    */ package org.tlauncher.tlauncher.updater.client;
/*    */ 
/*    */ public class PointOffer {
/*    */   private String name;
/*    */   private boolean active;
/*    */   private Map<String, String> texts;
/*    */   
/*  8 */   public void setName(String name) { this.name = name; } public void setActive(boolean active) { this.active = active; } public void setTexts(Map<String, String> texts) { this.texts = texts; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof PointOffer)) return false;  PointOffer other = (PointOffer)o; if (!other.canEqual(this)) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  if (isActive() != other.isActive()) return false;  Object<String, String> this$texts = (Object<String, String>)getTexts(), other$texts = (Object<String, String>)other.getTexts(); return !((this$texts == null) ? (other$texts != null) : !this$texts.equals(other$texts)); } protected boolean canEqual(Object other) { return other instanceof PointOffer; } public int hashCode() { int PRIME = 59; result = 1; Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); result = result * 59 + (isActive() ? 79 : 97); Object<String, String> $texts = (Object<String, String>)getTexts(); return result * 59 + (($texts == null) ? 43 : $texts.hashCode()); } public String toString() {
/*  9 */     return "PointOffer(name=" + getName() + ", active=" + isActive() + ", texts=" + getTexts() + ")";
/*    */   }
/* 11 */   public String getName() { return this.name; }
/* 12 */   public boolean isActive() { return this.active; } public Map<String, String> getTexts() {
/* 13 */     return this.texts;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\client\PointOffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */