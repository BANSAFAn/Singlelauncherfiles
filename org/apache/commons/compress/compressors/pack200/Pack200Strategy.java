/*    */ package org.apache.commons.compress.compressors.pack200;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public enum Pack200Strategy
/*    */ {
/* 31 */   IN_MEMORY
/*    */   {
/*    */     StreamBridge newStreamBridge() {
/* 34 */       return new InMemoryCachingStreamBridge();
/*    */     }
/*    */   },
/*    */   
/* 38 */   TEMP_FILE
/*    */   {
/*    */     StreamBridge newStreamBridge() throws IOException {
/* 41 */       return new TempFileCachingStreamBridge();
/*    */     }
/*    */   };
/*    */   
/*    */   abstract StreamBridge newStreamBridge() throws IOException;
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\pack200\Pack200Strategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */