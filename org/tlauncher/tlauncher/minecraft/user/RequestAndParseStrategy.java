/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.tlauncher.tlauncher.minecraft.exceptions.ParseException;
/*    */ import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RequestAndParseStrategy<A, V extends Validatable>
/*    */ {
/*    */   private final Logger logger;
/*    */   private final Requester<A> requester;
/*    */   private final Parser<V> parser;
/*    */   
/*    */   protected RequestAndParseStrategy(Logger logger, Requester<A> requester, Parser<V> parser) {
/* 17 */     this.logger = logger;
/* 18 */     this.requester = requester;
/* 19 */     this.parser = parser;
/*    */   }
/*    */   
/*    */   protected V requestAndParse(A argument) throws IOException, InvalidResponseException {
/* 23 */     String response = this.requester.makeRequest(this.logger, argument);
/*    */     try {
/* 25 */       return this.parser.parseResponse(this.logger, response);
/* 26 */     } catch (ParseException e) {
/* 27 */       e.printStackTrace();
/* 28 */       throw new InvalidResponseException(response, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\RequestAndParseStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */