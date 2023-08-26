/*    */ package org.tlauncher.tlauncher.configuration.test.environment;
/*    */ 
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.tlauncher.configuration.Configuration;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.util.OS;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaVersionBitTestEnvironment
/*    */   implements TestEnvironment
/*    */ {
/*    */   private Configuration c;
/*    */   
/*    */   public JavaVersionBitTestEnvironment(Configuration c) {
/* 17 */     this.c = c;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean testEnvironment() {
/* 23 */     String systemType = OS.getSystemInfo("System Type");
/* 24 */     U.debug(new Object[] { "system type" + systemType });
/* 25 */     if (OS.CURRENT == OS.WINDOWS && OS.Arch.CURRENT == OS.Arch.x32 && systemType != null && systemType
/* 26 */       .toLowerCase().contains("64")) {
/* 27 */       this.c.set("memory.problem.message", "java.version.32x.not.proper", false);
/* 28 */       U.log(new Object[] { "not proper java bit for system type " + systemType });
/* 29 */       return false;
/*    */     } 
/* 31 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void fix() {
/* 36 */     String value = "java.version.message.not.show";
/* 37 */     if (this.c.getBoolean(value)) {
/*    */       return;
/*    */     }
/* 40 */     SwingUtilities.invokeLater(() -> {
/*    */           if (Alert.showWarningMessageWithCheckBox("", "java.version.32x.not.proper", 400))
/*    */             this.c.set(value, Boolean.valueOf(true), false); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\test\environment\JavaVersionBitTestEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */