/*    */ package net.minecraft.launcher.process;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.Objects;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ class ProcessMonitorThread extends Thread {
/*    */   private final JavaProcess process;
/*    */   private JavaProcessListener listener;
/*    */   
/*    */   public ProcessMonitorThread(JavaProcess process, JavaProcessListener listener) {
/* 15 */     this.process = process;
/* 16 */     this.listener = listener;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 21 */     Process raw = this.process.getRawProcess();
/* 22 */     InputStreamReader reader = new InputStreamReader(raw.getInputStream());
/* 23 */     BufferedReader buf = new BufferedReader(reader);
/*    */ 
/*    */     
/* 26 */     while (this.process.isRunning()) {
/*    */       
/* 28 */       try { String line; while ((line = buf.readLine()) != null) {
/* 29 */           if (this.listener != null)
/* 30 */             this.listener.onJavaProcessLog(this.process, line); 
/*    */         }  }
/* 32 */       catch (Throwable throwable)
/*    */       
/*    */       { 
/* 35 */         try { buf.close(); }
/* 36 */         catch (IOException ex)
/* 37 */         { Logger.getLogger(ProcessMonitorThread.class.getName()).log(Level.SEVERE, (String)null, ex); }  } finally { try { buf.close(); } catch (IOException ex) { Logger.getLogger(ProcessMonitorThread.class.getName()).log(Level.SEVERE, (String)null, ex); }
/*    */          }
/*    */     
/*    */     } 
/*    */     
/* 42 */     if (Objects.nonNull(this.listener))
/* 43 */       this.listener.onJavaProcessEnded(this.process); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\process\ProcessMonitorThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */