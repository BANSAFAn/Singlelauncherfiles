/*   */ package org.tlauncher.modpack.domain.client;
/*   */ public class AddedGameEntityDTO {
/*   */   private String url;
/*   */   
/* 5 */   public String getUrl() { return this.url; } public void setUrl(String url) { this.url = url; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof AddedGameEntityDTO)) return false;  AddedGameEntityDTO other = (AddedGameEntityDTO)o; if (!other.canEqual(this)) return false;  Object this$url = getUrl(), other$url = other.getUrl(); return !((this$url == null) ? (other$url != null) : !this$url.equals(other$url)); } protected boolean canEqual(Object other) { return other instanceof AddedGameEntityDTO; } public int hashCode() { int PRIME = 59; result = 1; Object $url = getUrl(); return result * 59 + (($url == null) ? 43 : $url.hashCode()); } public String toString() { return "AddedGameEntityDTO(url=" + getUrl() + ")"; }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\AddedGameEntityDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */