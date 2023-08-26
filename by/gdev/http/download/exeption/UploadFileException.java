/*    */ package by.gdev.http.download.exeption;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class UploadFileException
/*    */   extends IOException {
/*    */   private static final long serialVersionUID = -2684927056566219164L;
/*    */   private String uri;
/*    */   private String localPath;
/*    */   
/*    */   public void setUri(String uri) {
/* 12 */     this.uri = uri; } public void setLocalPath(String localPath) { this.localPath = localPath; } public String toString() { return "UploadFileException(uri=" + getUri() + ", localPath=" + getLocalPath() + ")"; }
/* 13 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof UploadFileException)) return false;  UploadFileException other = (UploadFileException)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object this$uri = getUri(), other$uri = other.getUri(); if ((this$uri == null) ? (other$uri != null) : !this$uri.equals(other$uri)) return false;  Object this$localPath = getLocalPath(), other$localPath = other.getLocalPath(); return !((this$localPath == null) ? (other$localPath != null) : !this$localPath.equals(other$localPath)); } protected boolean canEqual(Object other) { return other instanceof UploadFileException; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object $uri = getUri(); result = result * 59 + (($uri == null) ? 43 : $uri.hashCode()); Object $localPath = getLocalPath(); return result * 59 + (($localPath == null) ? 43 : $localPath.hashCode()); }
/*    */ 
/*    */   
/* 16 */   public String getUri() { return this.uri; } public String getLocalPath() {
/* 17 */     return this.localPath;
/*    */   } public UploadFileException(String uri, String localPath, String message) {
/* 19 */     super(message);
/* 20 */     this.uri = uri;
/* 21 */     this.localPath = localPath;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gdev\http\download\exeption\UploadFileException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */