/*    */ package org.tlauncher.util.git;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class MapTokenResolver
/*    */   implements ITokenResolver {
/*  8 */   protected Map<String, String> tokenMap = new HashMap<>();
/*    */   
/*    */   public MapTokenResolver(Map<String, String> tokenMap) {
/* 11 */     this.tokenMap = tokenMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public String resolveToken(String tokenName) {
/* 16 */     return this.tokenMap.get(tokenName);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\git\MapTokenResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */