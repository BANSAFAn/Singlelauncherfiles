/*    */ package org.tlauncher.tlauncher.configuration.test.environment;
/*    */ 
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.tlauncher.configuration.Configuration;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.util.OS;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaBitTestEnvironment
/*    */   implements TestEnvironment
/*    */ {
/*    */   private Configuration c;
/*    */   
/*    */   public JavaBitTestEnvironment(Configuration c) {
/* 18 */     this.c = c;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean testEnvironment() {
/* 23 */     String systemType = OS.getSystemInfo("System Type");
/* 24 */     String processorDesc = OS.getSystemInfo("Processor(s)");
/* 25 */     U.debug(new Object[] { "system type:" + systemType });
/* 26 */     U.debug(new Object[] { "Processor:" + processorDesc });
/* 27 */     if (OS.CURRENT == OS.WINDOWS && systemType != null && processorDesc != null && systemType
/* 28 */       .toLowerCase().contains("86")) {
/* 29 */       U.log(new Object[] { "system type" + systemType });
/* 30 */       U.log(new Object[] { "Processor(s)" + processorDesc });
/* 31 */       if (processorDesc.toLowerCase().contains("64")) {
/* 32 */         this.c.set("memory.problem.message", "system.32x.not.proper", true);
/* 33 */         U.log(new Object[] { "not proper bit system for this processor " + OS.getSystemInfo("Processor(s)") });
/* 34 */         return false;
/*    */       } 
/*    */     } 
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void fix() {
/* 42 */     String value = "system.bit.message.not.show";
/* 43 */     if (this.c.getBoolean(value)) {
/*    */       return;
/*    */     }
/* 46 */     SwingUtilities.invokeLater(() -> {
/*    */           if (Alert.showWarningMessageWithCheckBox("", Localizable.get("system.32x.not.proper"), 400))
/*    */             this.c.set(value, Boolean.valueOf(true), true); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\test\environment\JavaBitTestEnvironment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */