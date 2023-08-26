/*    */ package ch.qos.logback.core.joran.event.stax;
/*    */ 
/*    */ import javax.xml.stream.Location;
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
/*    */ public class StaxEvent
/*    */ {
/*    */   final String name;
/*    */   final Location location;
/*    */   
/*    */   StaxEvent(String name, Location location) {
/* 24 */     this.name = name;
/* 25 */     this.location = location;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 30 */     return this.name;
/*    */   }
/*    */   
/*    */   public Location getLocation() {
/* 34 */     return this.location;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\joran\event\stax\StaxEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */