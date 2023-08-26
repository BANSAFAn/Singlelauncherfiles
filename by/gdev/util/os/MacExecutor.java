/*    */ package by.gdev.util.os;
/*    */ 
/*    */ import by.gdev.util.model.GPUsDescriptionDTO;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class MacExecutor
/*    */   extends LinuxExecutor
/*    */ {
/*    */   public GPUsDescriptionDTO getGPUInfo() throws IOException, InterruptedException {
/* 11 */     String res = execute("system_profiler SPDisplaysDataType", 60);
/* 12 */     return getGPUInfo1(res, "chipset model:");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\os\MacExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */