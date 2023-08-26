/*   */ package org.tlauncher.tlauncher.entity.minecraft;
/*   */ 
/*   */ 
/*   */ public class JVMManifest {
/*   */   Map<String, JVMFile> files;
/*   */   
/* 7 */   public String toString() { return "JVMManifest(files=" + getFiles() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object<String, JVMFile> $files = (Object<String, JVMFile>)getFiles(); return result * 59 + (($files == null) ? 43 : $files.hashCode()); } protected boolean canEqual(Object other) { return other instanceof JVMManifest; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JVMManifest)) return false;  JVMManifest other = (JVMManifest)o; if (!other.canEqual(this)) return false;  Object<String, JVMFile> this$files = (Object<String, JVMFile>)getFiles(), other$files = (Object<String, JVMFile>)other.getFiles(); return !((this$files == null) ? (other$files != null) : !this$files.equals(other$files)); } public void setFiles(Map<String, JVMFile> files) { this.files = files; }
/*   */    public Map<String, JVMFile> getFiles() {
/* 9 */     return this.files;
/*   */   }
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\minecraft\JVMManifest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */