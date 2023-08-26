/*    */ package by.gdev.util.model;
/*    */ 
/*    */ public class GPUsDescriptionDTO {
/*    */   List<GPUDescription> gpus;
/*    */   String rawDescription;
/*    */   
/*  7 */   public void setGpus(List<GPUDescription> gpus) { this.gpus = gpus; } public void setRawDescription(String rawDescription) { this.rawDescription = rawDescription; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof GPUsDescriptionDTO)) return false;  GPUsDescriptionDTO other = (GPUsDescriptionDTO)o; if (!other.canEqual(this)) return false;  Object<GPUDescription> this$gpus = (Object<GPUDescription>)getGpus(), other$gpus = (Object<GPUDescription>)other.getGpus(); if ((this$gpus == null) ? (other$gpus != null) : !this$gpus.equals(other$gpus)) return false;  Object this$rawDescription = getRawDescription(), other$rawDescription = other.getRawDescription(); return !((this$rawDescription == null) ? (other$rawDescription != null) : !this$rawDescription.equals(other$rawDescription)); } protected boolean canEqual(Object other) { return other instanceof GPUsDescriptionDTO; } public int hashCode() { int PRIME = 59; result = 1; Object<GPUDescription> $gpus = (Object<GPUDescription>)getGpus(); result = result * 59 + (($gpus == null) ? 43 : $gpus.hashCode()); Object $rawDescription = getRawDescription(); return result * 59 + (($rawDescription == null) ? 43 : $rawDescription.hashCode()); } public String toString() { return "GPUsDescriptionDTO(gpus=" + getGpus() + ", rawDescription=" + getRawDescription() + ")"; }
/*    */   
/*  9 */   public List<GPUDescription> getGpus() { return this.gpus; } public String getRawDescription() {
/* 10 */     return this.rawDescription;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\model\GPUsDescriptionDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */