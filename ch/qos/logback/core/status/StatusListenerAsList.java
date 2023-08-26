/*    */ package ch.qos.logback.core.status;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StatusListenerAsList
/*    */   implements StatusListener
/*    */ {
/* 27 */   List<Status> statusList = new ArrayList<Status>();
/*    */   
/*    */   public void addStatusEvent(Status status) {
/* 30 */     this.statusList.add(status);
/*    */   }
/*    */   
/*    */   public List<Status> getStatusList() {
/* 34 */     return this.statusList;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\status\StatusListenerAsList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */