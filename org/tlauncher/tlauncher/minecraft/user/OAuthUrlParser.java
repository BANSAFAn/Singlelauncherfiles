/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import org.apache.http.NameValuePair;
/*    */ import org.apache.http.client.utils.URLEncodedUtils;
/*    */ import org.tlauncher.tlauncher.minecraft.exceptions.ParseException;
/*    */ 
/*    */ public class OAuthUrlParser
/*    */ {
/*    */   private static String findByKey(List<NameValuePair> pairs, String key) {
/* 15 */     Optional<NameValuePair> pair = pairs.stream().filter(p -> p.getName().equals(key)).findAny();
/* 16 */     return pair.<String>map(NameValuePair::getValue).orElse(null);
/*    */   }
/*    */   
/*    */   public String parseAndValidate(String url) throws MicrosoftOAuthCodeRequestException, ParseException {
/*    */     List<NameValuePair> pairs;
/*    */     try {
/* 22 */       pairs = URLEncodedUtils.parse(new URI(url), String.valueOf(StandardCharsets.UTF_8));
/* 23 */     } catch (URISyntaxException e) {
/* 24 */       throw new ParseException(e);
/*    */     } 
/* 26 */     String error = findByKey(pairs, "error");
/* 27 */     if (error != null) {
/* 28 */       if (error.equals("access_denied"))
/* 29 */         throw new CodeRequestCancelledException("redirect page received \"access_denied\""); 
/* 30 */       throw new CodeRequestErrorException(error, findByKey(pairs, "error_description"));
/*    */     } 
/* 32 */     Optional<NameValuePair> code = pairs.stream().filter(p -> p.getName().equals("code")).findAny();
/* 33 */     if (code.isPresent())
/* 34 */       return ((NameValuePair)code.get()).getValue(); 
/* 35 */     throw new ParseException("no code in query");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\OAuthUrlParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */