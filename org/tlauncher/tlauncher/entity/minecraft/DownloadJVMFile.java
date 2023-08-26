/*    */ package org.tlauncher.tlauncher.entity.minecraft;
/*    */ 
/*    */ 
/*    */ public class DownloadJVMFile {
/*    */   private MetadataDTO lzma;
/*    */   private MetadataDTO raw;
/*    */   
/*  8 */   public void setLzma(MetadataDTO lzma) { this.lzma = lzma; } public void setRaw(MetadataDTO raw) { this.raw = raw; } public String toString() { return "DownloadJVMFile(lzma=" + getLzma() + ", raw=" + getRaw() + ")"; }
/*  9 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof DownloadJVMFile)) return false;  DownloadJVMFile other = (DownloadJVMFile)o; if (!other.canEqual(this)) return false;  Object this$lzma = getLzma(), other$lzma = other.getLzma(); if ((this$lzma == null) ? (other$lzma != null) : !this$lzma.equals(other$lzma)) return false;  Object this$raw = getRaw(), other$raw = other.getRaw(); return !((this$raw == null) ? (other$raw != null) : !this$raw.equals(other$raw)); } protected boolean canEqual(Object other) { return other instanceof DownloadJVMFile; } public int hashCode() { int PRIME = 59; result = 1; Object $lzma = getLzma(); result = result * 59 + (($lzma == null) ? 43 : $lzma.hashCode()); Object $raw = getRaw(); return result * 59 + (($raw == null) ? 43 : $raw.hashCode()); }
/*    */   
/* 11 */   public MetadataDTO getLzma() { return this.lzma; } public MetadataDTO getRaw() {
/* 12 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\minecraft\DownloadJVMFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */