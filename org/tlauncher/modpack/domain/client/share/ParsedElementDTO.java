/*   */ package org.tlauncher.modpack.domain.client.share;
/*   */ public class ParsedElementDTO {
/*   */   private boolean parse;
/*   */   
/* 5 */   public boolean isParse() { return this.parse; } private long updated; private long nextUpdated; public long getUpdated() { return this.updated; } public long getNextUpdated() { return this.nextUpdated; } public void setParse(boolean parse) { this.parse = parse; } public void setUpdated(long updated) { this.updated = updated; } public void setNextUpdated(long nextUpdated) { this.nextUpdated = nextUpdated; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof ParsedElementDTO)) return false;  ParsedElementDTO other = (ParsedElementDTO)o; return !other.canEqual(this) ? false : ((isParse() != other.isParse()) ? false : ((getUpdated() != other.getUpdated()) ? false : (!(getNextUpdated() != other.getNextUpdated())))); } protected boolean canEqual(Object other) { return other instanceof ParsedElementDTO; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + (isParse() ? 79 : 97); long $updated = getUpdated(); result = result * 59 + (int)($updated ^ $updated >>> 32L); long $nextUpdated = getNextUpdated(); return result * 59 + (int)($nextUpdated ^ $nextUpdated >>> 32L); } public String toString() { return "ParsedElementDTO(parse=" + isParse() + ", updated=" + getUpdated() + ", nextUpdated=" + getNextUpdated() + ")"; }
/*   */ 
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\ParsedElementDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */