/*    */ package net.minecraft.launcher.process;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class JavaProcess {
/*    */   private final List<String> commands;
/*    */   
/*    */   public JavaProcess(List<String> commands, Process process, JavaProcessListener listener) {
/*  9 */     this.commands = commands;
/* 10 */     this.process = process;
/*    */     
/* 12 */     ProcessMonitorThread monitor = new ProcessMonitorThread(this, listener);
/* 13 */     monitor.start();
/*    */   }
/*    */   private final Process process;
/*    */   public Process getRawProcess() {
/* 17 */     return this.process;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isRunning() {
/*    */     try {
/* 23 */       this.process.exitValue();
/* 24 */     } catch (IllegalThreadStateException ex) {
/* 25 */       return true;
/*    */     } 
/*    */     
/* 28 */     return false;
/*    */   }
/*    */   
/*    */   public int getExitCode() {
/*    */     try {
/* 33 */       return this.process.exitValue();
/* 34 */     } catch (IllegalThreadStateException ex) {
/* 35 */       ex.fillInStackTrace();
/* 36 */       throw ex;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return "JavaProcess[commands=" + this.commands + ", isRunning=" + 
/* 43 */       isRunning() + "]";
/*    */   }
/*    */   
/*    */   public void stop() {
/* 47 */     this.process.destroy();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\process\JavaProcess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */