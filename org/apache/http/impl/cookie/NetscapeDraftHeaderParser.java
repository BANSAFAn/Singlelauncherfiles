/*    */ package org.apache.http.impl.cookie;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.BitSet;
/*    */ import java.util.List;
/*    */ import org.apache.http.HeaderElement;
/*    */ import org.apache.http.NameValuePair;
/*    */ import org.apache.http.ParseException;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.message.BasicHeaderElement;
/*    */ import org.apache.http.message.BasicNameValuePair;
/*    */ import org.apache.http.message.ParserCursor;
/*    */ import org.apache.http.message.TokenParser;
/*    */ import org.apache.http.util.Args;
/*    */ import org.apache.http.util.CharArrayBuffer;
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
/*    */ @Immutable
/*    */ public class NetscapeDraftHeaderParser
/*    */ {
/* 52 */   public static final NetscapeDraftHeaderParser DEFAULT = new NetscapeDraftHeaderParser();
/*    */ 
/*    */   
/*    */   private static final char PARAM_DELIMITER = ';';
/*    */ 
/*    */   
/* 58 */   private static final BitSet TOKEN_DELIMS = TokenParser.INIT_BITSET(new int[] { 61, 59 });
/* 59 */   private static final BitSet VALUE_DELIMS = TokenParser.INIT_BITSET(new int[] { 59 });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 65 */   private final TokenParser tokenParser = TokenParser.INSTANCE;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HeaderElement parseHeader(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
/* 71 */     Args.notNull(buffer, "Char array buffer");
/* 72 */     Args.notNull(cursor, "Parser cursor");
/* 73 */     NameValuePair nvp = parseNameValuePair(buffer, cursor);
/* 74 */     List<NameValuePair> params = new ArrayList<NameValuePair>();
/* 75 */     while (!cursor.atEnd()) {
/* 76 */       NameValuePair param = parseNameValuePair(buffer, cursor);
/* 77 */       params.add(param);
/*    */     } 
/* 79 */     return (HeaderElement)new BasicHeaderElement(nvp.getName(), nvp.getValue(), params.<NameValuePair>toArray(new NameValuePair[params.size()]));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor) {
/* 86 */     String name = this.tokenParser.parseToken(buffer, cursor, TOKEN_DELIMS);
/* 87 */     if (cursor.atEnd()) {
/* 88 */       return (NameValuePair)new BasicNameValuePair(name, null);
/*    */     }
/* 90 */     int delim = buffer.charAt(cursor.getPos());
/* 91 */     cursor.updatePos(cursor.getPos() + 1);
/* 92 */     if (delim != 61) {
/* 93 */       return (NameValuePair)new BasicNameValuePair(name, null);
/*    */     }
/* 95 */     String value = this.tokenParser.parseToken(buffer, cursor, VALUE_DELIMS);
/* 96 */     if (!cursor.atEnd()) {
/* 97 */       cursor.updatePos(cursor.getPos() + 1);
/*    */     }
/* 99 */     return (NameValuePair)new BasicNameValuePair(name, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\cookie\NetscapeDraftHeaderParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */