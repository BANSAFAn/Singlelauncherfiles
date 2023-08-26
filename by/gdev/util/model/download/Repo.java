/*    */ package by.gdev.util.model.download;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class Repo {
/*    */   private List<String> repositories;
/*    */   private List<Metadata> resources;
/*    */   private boolean remoteServerSHA1;
/*    */   
/*    */   public void setRepositories(List<String> repositories) {
/* 11 */     this.repositories = repositories; } public void setResources(List<Metadata> resources) { this.resources = resources; } public void setRemoteServerSHA1(boolean remoteServerSHA1) { this.remoteServerSHA1 = remoteServerSHA1; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Repo)) return false;  Repo other = (Repo)o; if (!other.canEqual(this)) return false;  Object<String> this$repositories = (Object<String>)getRepositories(), other$repositories = (Object<String>)other.getRepositories(); if ((this$repositories == null) ? (other$repositories != null) : !this$repositories.equals(other$repositories)) return false;  Object<Metadata> this$resources = (Object<Metadata>)getResources(), other$resources = (Object<Metadata>)other.getResources(); return ((this$resources == null) ? (other$resources != null) : !this$resources.equals(other$resources)) ? false : (!(isRemoteServerSHA1() != other.isRemoteServerSHA1())); } protected boolean canEqual(Object other) { return other instanceof Repo; } public int hashCode() { int PRIME = 59; result = 1; Object<String> $repositories = (Object<String>)getRepositories(); result = result * 59 + (($repositories == null) ? 43 : $repositories.hashCode()); Object<Metadata> $resources = (Object<Metadata>)getResources(); result = result * 59 + (($resources == null) ? 43 : $resources.hashCode()); return result * 59 + (isRemoteServerSHA1() ? 79 : 97); } public String toString() { return "Repo(repositories=" + getRepositories() + ", resources=" + getResources() + ", remoteServerSHA1=" + isRemoteServerSHA1() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getRepositories() {
/* 16 */     return this.repositories; } public List<Metadata> getResources() {
/* 17 */     return this.resources;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isRemoteServerSHA1() {
/* 23 */     return this.remoteServerSHA1;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\model\download\Repo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */