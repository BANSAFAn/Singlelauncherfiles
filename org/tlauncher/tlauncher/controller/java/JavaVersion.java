/*    */ package org.tlauncher.tlauncher.controller.java;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaVersion
/*    */ {
/*    */   private Map<OS.Arch, Map<OS, List<String>>> map;
/*    */   
/*    */   public Map<OS.Arch, Map<OS, List<String>>> getMap() {
/* 17 */     return this.map;
/*    */   }
/*    */   
/*    */   public void setMap(Map<OS.Arch, Map<OS, List<String>>> map) {
/* 21 */     this.map = map;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getProperUrl() throws NotIdentifiedSystem {
/* 29 */     Map<OS, List<String>> res = this.map.get(OS.Arch.CURRENT);
/* 30 */     if (res == null)
/* 31 */       throw new NotIdentifiedSystem("coudn't find" + OS.Arch.CURRENT); 
/* 32 */     return res.get(OS.CURRENT);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\controller\java\JavaVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */