/*    */ package org.tlauncher.tlauncher.minecraft.launcher;
/*    */ 
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ 
/*    */ public class MinecraftException
/*    */   extends Exception {
/*    */   private static final long serialVersionUID = -2415374288600214879L;
/*    */   private final String langPath;
/*    */   private final String[] langVars;
/*    */   
/*    */   MinecraftException(String message, String langPath, Throwable cause, Object... langVars) {
/* 12 */     super(message, cause);
/*    */     
/* 14 */     if (langPath == null) {
/* 15 */       throw new NullPointerException("Lang path required!");
/*    */     }
/* 17 */     if (langVars == null) {
/* 18 */       langVars = new Object[0];
/*    */     }
/* 20 */     this.langPath = langPath;
/* 21 */     this.langVars = Localizable.checkVariables(langVars);
/*    */   }
/*    */   
/*    */   public MinecraftException(String message, String langPath, Throwable cause) {
/* 25 */     this(message, langPath, cause, new Object[0]);
/*    */   }
/*    */   
/*    */   public MinecraftException(String message, String langPath, Object... vars) {
/* 29 */     this(message, langPath, null, vars);
/*    */   }
/*    */   
/*    */   public String getLangPath() {
/* 33 */     return this.langPath;
/*    */   }
/*    */   
/*    */   public String[] getLangVars() {
/* 37 */     return this.langVars;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\MinecraftException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */