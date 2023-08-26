/*    */ package org.apache.commons.io.input;
/*    */ 
/*    */ import java.io.FilterReader;
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractCharacterFilterReader
/*    */   extends FilterReader
/*    */ {
/*    */   protected static final IntPredicate SKIP_NONE = ch -> false;
/*    */   private final IntPredicate skip;
/*    */   
/*    */   protected AbstractCharacterFilterReader(Reader reader) {
/* 46 */     this(reader, SKIP_NONE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AbstractCharacterFilterReader(Reader reader, IntPredicate skip) {
/* 57 */     super(reader);
/* 58 */     this.skip = (skip == null) ? SKIP_NONE : skip;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean filter(int ch) {
/* 68 */     return this.skip.test(ch);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/*    */     int ch;
/*    */     do {
/* 75 */       ch = this.in.read();
/* 76 */     } while (ch != -1 && filter(ch));
/* 77 */     return ch;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 82 */     int read = super.read(cbuf, off, len);
/* 83 */     if (read == -1) {
/* 84 */       return -1;
/*    */     }
/* 86 */     int pos = off - 1;
/* 87 */     for (int readPos = off; readPos < off + read; readPos++) {
/* 88 */       if (!filter(cbuf[readPos])) {
/*    */ 
/*    */         
/* 91 */         pos++;
/* 92 */         if (pos < readPos)
/* 93 */           cbuf[pos] = cbuf[readPos]; 
/*    */       } 
/*    */     } 
/* 96 */     return pos - off + 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\AbstractCharacterFilterReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */