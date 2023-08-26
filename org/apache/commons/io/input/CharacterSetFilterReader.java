/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.Reader;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public class CharacterSetFilterReader
/*    */   extends AbstractCharacterFilterReader
/*    */ {
/*    */   private static IntPredicate toIntPredicate(Set<Integer> skip) {
/* 37 */     if (skip == null) {
/* 38 */       return SKIP_NONE;
/*    */     }
/* 40 */     Set<Integer> unmodifiableSet = Collections.unmodifiableSet(skip);
/* 41 */     return c -> unmodifiableSet.contains(Integer.valueOf(c));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CharacterSetFilterReader(Reader reader, Integer... skip) {
/* 52 */     this(reader, new HashSet<>(Arrays.asList(skip)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CharacterSetFilterReader(Reader reader, Set<Integer> skip) {
/* 62 */     super(reader, toIntPredicate(skip));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\CharacterSetFilterReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */