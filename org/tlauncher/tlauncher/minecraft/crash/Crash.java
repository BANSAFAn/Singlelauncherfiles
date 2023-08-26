/*    */ package org.tlauncher.tlauncher.minecraft.crash;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class Crash
/*    */ {
/*    */   private String file;
/* 11 */   private List<CrashSignatureContainer.CrashSignature> signatures = new ArrayList<>();
/*    */   
/*    */   void addSignature(CrashSignatureContainer.CrashSignature sign) {
/* 14 */     this.signatures.add(sign);
/*    */   }
/*    */   
/*    */   void removeSignature(CrashSignatureContainer.CrashSignature sign) {
/* 18 */     this.signatures.remove(sign);
/*    */   }
/*    */   
/*    */   void setFile(String path) {
/* 22 */     this.file = path;
/*    */   }
/*    */   
/*    */   public String getFile() {
/* 26 */     return this.file;
/*    */   }
/*    */   
/*    */   public List<CrashSignatureContainer.CrashSignature> getSignatures() {
/* 30 */     return Collections.unmodifiableList(this.signatures);
/*    */   }
/*    */   
/*    */   public boolean hasSignature(CrashSignatureContainer.CrashSignature s) {
/* 34 */     return this.signatures.contains(s);
/*    */   }
/*    */   
/*    */   public boolean isRecognized() {
/* 38 */     return !this.signatures.isEmpty();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\crash\Crash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */