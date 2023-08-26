/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.Reader;
/*    */ import java.util.function.IntPredicate;
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
/*    */ public class CharacterFilterReader
/*    */   extends AbstractCharacterFilterReader
/*    */ {
/*    */   public CharacterFilterReader(Reader reader, int skip) {
/* 38 */     super(reader, c -> (c == skip));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CharacterFilterReader(Reader reader, IntPredicate skip) {
/* 49 */     super(reader, skip);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\CharacterFilterReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */