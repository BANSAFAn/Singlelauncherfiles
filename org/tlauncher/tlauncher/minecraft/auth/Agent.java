/*    */ package org.tlauncher.tlauncher.minecraft.auth;
/*    */ 
/*    */ public class Agent {
/*  4 */   public static final Agent MINECRAFT = new Agent("Minecraft", 1);
/*    */   
/*    */   private final String name;
/*    */   private final int version;
/*    */   
/*    */   private Agent(String name, int version) {
/* 10 */     this.name = name;
/* 11 */     this.version = version;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 15 */     return this.name;
/*    */   }
/*    */   
/*    */   public int getVersion() {
/* 19 */     return this.version;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 24 */     return "Agent{name='" + this.name + '\'' + ", version=" + this.version + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\auth\Agent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */