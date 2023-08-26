/*    */ package org.tlauncher.tlauncher.updater.bootstrapper.model;
/*    */ 
/*    */ public class DownloadedBootInfo {
/*    */   List<DownloadedElement> libraries;
/*    */   JavaDownloadedElement element;
/*    */   
/*  7 */   public void setLibraries(List<DownloadedElement> libraries) { this.libraries = libraries; } public void setElement(JavaDownloadedElement element) { this.element = element; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloadedBootInfo)) return false;  DownloadedBootInfo other = (DownloadedBootInfo)o; if (!other.canEqual(this)) return false;  Object<DownloadedElement> this$libraries = (Object<DownloadedElement>)getLibraries(), other$libraries = (Object<DownloadedElement>)other.getLibraries(); if ((this$libraries == null) ? (other$libraries != null) : !this$libraries.equals(other$libraries)) return false;  Object this$element = getElement(), other$element = other.getElement(); return !((this$element == null) ? (other$element != null) : !this$element.equals(other$element)); } protected boolean canEqual(Object other) { return other instanceof DownloadedBootInfo; } public int hashCode() { int PRIME = 59; result = 1; Object<DownloadedElement> $libraries = (Object<DownloadedElement>)getLibraries(); result = result * 59 + (($libraries == null) ? 43 : $libraries.hashCode()); Object $element = getElement(); return result * 59 + (($element == null) ? 43 : $element.hashCode()); } public String toString() { return "DownloadedBootInfo(libraries=" + getLibraries() + ", element=" + getElement() + ")"; }
/*    */   
/*  9 */   public List<DownloadedElement> getLibraries() { return this.libraries; } public JavaDownloadedElement getElement() {
/* 10 */     return this.element;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\bootstrapper\model\DownloadedBootInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */