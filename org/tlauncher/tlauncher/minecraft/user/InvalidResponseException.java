/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParser;
/*    */ 
/*    */ public class InvalidResponseException extends Exception {
/*    */   private final String response;
/*    */   
/*    */   public InvalidResponseException(String response) {
/* 10 */     super(response);
/* 11 */     this.response = response;
/*    */   }
/*    */   
/*    */   public InvalidResponseException(String response, String message) {
/* 15 */     super(message);
/* 16 */     this.response = response;
/*    */   }
/*    */   
/*    */   public InvalidResponseException(String response, Throwable cause) {
/* 20 */     super(response, cause);
/* 21 */     this.response = response;
/*    */   }
/*    */   
/*    */   public String getResponse() {
/* 25 */     return this.response;
/*    */   }
/*    */   
/*    */   public JsonObject getResponseAsJson() {
/*    */     try {
/* 30 */       return (JsonObject)(new JsonParser()).parse(this.response);
/* 31 */     } catch (RuntimeException rE) {
/* 32 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\InvalidResponseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */