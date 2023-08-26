/*    */ package by.gdev.util.model;
/*    */ 
/*    */ public enum GPUDriverVersion {
/*  4 */   CUDA_V10_2("10.2"), CUDA_V10_1("10.1"), CUDA_V_10("10"),
/*  5 */   ANY_AMD("ANY_AMD");
/*    */   
/*    */   String value;
/*    */   
/*    */   GPUDriverVersion(String s) {
/* 10 */     this.value = s;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 14 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\model\GPUDriverVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */