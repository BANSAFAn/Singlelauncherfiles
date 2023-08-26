/*    */ package ch.jamiete.mcping;
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
/*    */ public class MinecraftPingOptions
/*    */ {
/*    */   private String hostname;
/* 37 */   private int port = 25565;
/* 38 */   private int timeout = 2000;
/* 39 */   private String charset = "UTF-8";
/*    */   
/*    */   public String getHostname() {
/* 42 */     return this.hostname;
/*    */   }
/*    */   
/*    */   public MinecraftPingOptions setHostname(String hostname) {
/* 46 */     this.hostname = hostname;
/* 47 */     return this;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 51 */     return this.port;
/*    */   }
/*    */   
/*    */   public MinecraftPingOptions setPort(int port) {
/* 55 */     this.port = port;
/* 56 */     return this;
/*    */   }
/*    */   
/*    */   public int getTimeout() {
/* 60 */     return this.timeout;
/*    */   }
/*    */   
/*    */   public MinecraftPingOptions setTimeout(int timeout) {
/* 64 */     this.timeout = timeout;
/* 65 */     return this;
/*    */   }
/*    */   
/*    */   public String getCharset() {
/* 69 */     return this.charset;
/*    */   }
/*    */   
/*    */   public MinecraftPingOptions setCharset(String charset) {
/* 73 */     this.charset = charset;
/* 74 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\jamiete\mcping\MinecraftPingOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */