/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.function.Function;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.client.fluent.Request;
/*    */ import org.apache.http.util.EntityUtils;
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ public class HttpClientRequester<A>
/*    */   implements Requester<A> {
/*    */   private final Function<A, Request> requestFactory;
/*    */   
/*    */   public HttpClientRequester(Function<A, Request> requestFactory) {
/* 15 */     this.requestFactory = requestFactory;
/*    */   }
/*    */   
/*    */   public String makeRequest(Logger logger, A argument) throws InvalidResponseException, IOException {
/* 19 */     Request request = this.requestFactory.apply(argument);
/* 20 */     logger.trace("Sending request: {}" + request);
/* 21 */     HttpResponse httpResponse = request.execute().returnResponse();
/* 22 */     logger.trace("Reading response");
/* 23 */     String response = EntityUtils.toString(httpResponse.getEntity());
/* 24 */     logger.trace("Response: {}" + response);
/* 25 */     int statusCode = httpResponse.getStatusLine().getStatusCode();
/* 26 */     logger.trace("Status code: {}" + statusCode);
/* 27 */     if (statusCode >= 200 && statusCode <= 299)
/* 28 */       return response; 
/* 29 */     throw new InvalidStatusCodeException(statusCode, response);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\HttpClientRequester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */