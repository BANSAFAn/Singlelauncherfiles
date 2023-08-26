/*    */ package org.apache.commons.io.file;
/*    */ 
/*    */ import org.apache.commons.io.IOUtils;
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
/*    */ public enum StandardDeleteOption
/*    */   implements DeleteOption
/*    */ {
/* 32 */   OVERRIDE_READ_ONLY;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean overrideReadOnly(DeleteOption[] options) {
/* 43 */     if (IOUtils.length((Object[])options) == 0) {
/* 44 */       return false;
/*    */     }
/* 46 */     for (DeleteOption deleteOption : options) {
/* 47 */       if (deleteOption == OVERRIDE_READ_ONLY) {
/* 48 */         return true;
/*    */       }
/*    */     } 
/* 51 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\file\StandardDeleteOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */