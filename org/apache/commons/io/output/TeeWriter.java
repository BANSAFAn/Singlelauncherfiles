/*    */ package org.apache.commons.io.output;
/*    */ 
/*    */ import java.io.Writer;
/*    */ import java.util.Collection;
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
/*    */ public class TeeWriter
/*    */   extends ProxyCollectionWriter
/*    */ {
/*    */   public TeeWriter(Collection<Writer> writers) {
/* 40 */     super(writers);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TeeWriter(Writer... writers) {
/* 49 */     super(writers);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\output\TeeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */