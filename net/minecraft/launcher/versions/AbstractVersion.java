/*    */ package net.minecraft.launcher.versions;
/*    */ 
/*    */ public abstract class AbstractVersion
/*    */   implements Version
/*    */ {
/*    */   private String url;
/*    */   private boolean skinVersion;
/*    */   
/*    */   public String getUrl() {
/* 10 */     return this.url;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setUrl(String url) {
/* 15 */     this.url = url;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSkinVersion() {
/* 20 */     return this.skinVersion;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSkinVersion(boolean skinVersion) {
/* 25 */     this.skinVersion = skinVersion;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\AbstractVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */