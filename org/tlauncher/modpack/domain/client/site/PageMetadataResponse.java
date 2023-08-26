/*   */ package org.tlauncher.modpack.domain.client.site;
/*   */ public class PageMetadataResponse {
/*   */   private PageIndexEnum pageIndexEnum;
/*   */   
/* 5 */   public PageIndexEnum getPageIndexEnum() { return this.pageIndexEnum; } private boolean ready; private boolean engTranslation; public boolean isReady() { return this.ready; } public boolean isEngTranslation() { return this.engTranslation; } public void setPageIndexEnum(PageIndexEnum pageIndexEnum) { this.pageIndexEnum = pageIndexEnum; } public void setReady(boolean ready) { this.ready = ready; } public void setEngTranslation(boolean engTranslation) { this.engTranslation = engTranslation; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof PageMetadataResponse)) return false;  PageMetadataResponse other = (PageMetadataResponse)o; if (!other.canEqual(this)) return false;  if (isReady() != other.isReady()) return false;  if (isEngTranslation() != other.isEngTranslation()) return false;  Object this$pageIndexEnum = getPageIndexEnum(), other$pageIndexEnum = other.getPageIndexEnum(); return !((this$pageIndexEnum == null) ? (other$pageIndexEnum != null) : !this$pageIndexEnum.equals(other$pageIndexEnum)); } protected boolean canEqual(Object other) { return other instanceof PageMetadataResponse; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + (isReady() ? 79 : 97); result = result * 59 + (isEngTranslation() ? 79 : 97); Object $pageIndexEnum = getPageIndexEnum(); return result * 59 + (($pageIndexEnum == null) ? 43 : $pageIndexEnum.hashCode()); } public String toString() { return "PageMetadataResponse(pageIndexEnum=" + getPageIndexEnum() + ", ready=" + isReady() + ", engTranslation=" + isEngTranslation() + ")"; }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\site\PageMetadataResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */