/*    */ package net.minecraft.launcher.versions;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ExtractRules {
/*  8 */   private List<String> exclude = new ArrayList<>();
/*    */ 
/*    */   
/*    */   public ExtractRules() {}
/*    */   
/*    */   public ExtractRules(String[] exclude) {
/* 14 */     if (exclude != null)
/* 15 */       Collections.addAll(this.exclude, exclude); 
/*    */   }
/*    */   
/*    */   public ExtractRules(ExtractRules rules) {
/* 19 */     for (String exclude : rules.exclude)
/* 20 */       this.exclude.add(exclude); 
/*    */   }
/*    */   
/*    */   public List<String> getExcludes() {
/* 24 */     return this.exclude;
/*    */   }
/*    */   
/*    */   public boolean shouldExtract(String path) {
/* 28 */     if (this.exclude == null) {
/* 29 */       return true;
/*    */     }
/* 31 */     for (String rule : this.exclude) {
/* 32 */       if (path.startsWith(rule))
/* 33 */         return false; 
/*    */     } 
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\ExtractRules.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */