/*    */ package org.tlauncher.tlauncher.updater.bootstrapper.model;
/*    */ 
/*    */ 
/*    */ public class JavaDownloadedElement extends DownloadedElement {
/*    */   private String javaFolder;
/*    */   private boolean originalJVM;
/*    */   
/*  8 */   public void setJavaFolder(String javaFolder) { this.javaFolder = javaFolder; } public void setOriginalJVM(boolean originalJVM) { this.originalJVM = originalJVM; } public String toString() { return "JavaDownloadedElement(javaFolder=" + getJavaFolder() + ", originalJVM=" + isOriginalJVM() + ")"; }
/*  9 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JavaDownloadedElement)) return false;  JavaDownloadedElement other = (JavaDownloadedElement)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$javaFolder = getJavaFolder(), other$javaFolder = other.getJavaFolder(); return ((this$javaFolder == null) ? (other$javaFolder != null) : !this$javaFolder.equals(other$javaFolder)) ? false : (!(isOriginalJVM() != other.isOriginalJVM())); } protected boolean canEqual(Object other) { return other instanceof JavaDownloadedElement; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $javaFolder = getJavaFolder(); result = result * 59 + (($javaFolder == null) ? 43 : $javaFolder.hashCode()); return result * 59 + (isOriginalJVM() ? 79 : 97); }
/*    */ 
/*    */   
/* 12 */   public String getJavaFolder() { return this.javaFolder; } public boolean isOriginalJVM() {
/* 13 */     return this.originalJVM;
/*    */   }
/*    */   public boolean existFolder(File folder) {
/* 16 */     return (new File(folder, this.javaFolder)).exists();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\updater\bootstrapper\model\JavaDownloadedElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */