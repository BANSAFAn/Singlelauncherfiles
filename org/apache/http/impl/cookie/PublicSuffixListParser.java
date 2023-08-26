/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.conn.util.PublicSuffixList;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ @Immutable
/*    */ public class PublicSuffixListParser
/*    */ {
/*    */   private final PublicSuffixFilter filter;
/*    */   private final org.apache.http.conn.util.PublicSuffixListParser parser;
/*    */   
/*    */   PublicSuffixListParser(PublicSuffixFilter filter) {
/* 51 */     this.filter = filter;
/* 52 */     this.parser = new org.apache.http.conn.util.PublicSuffixListParser();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void parse(Reader reader) throws IOException {
/* 64 */     PublicSuffixList suffixList = this.parser.parse(reader);
/* 65 */     this.filter.setPublicSuffixes(suffixList.getRules());
/* 66 */     this.filter.setExceptions(suffixList.getExceptions());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\PublicSuffixListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */