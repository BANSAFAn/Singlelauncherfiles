/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.http.Consts;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.ChallengeState;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.message.BasicHeaderValueParser;
/*     */ import org.apache.http.message.ParserCursor;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ import org.apache.http.util.CharsetUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public abstract class RFC2617Scheme
/*     */   extends AuthSchemeBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2845454858205884623L;
/*     */   private final Map<String, String> params;
/*     */   private transient Charset credentialsCharset;
/*     */   
/*     */   @Deprecated
/*     */   public RFC2617Scheme(ChallengeState challengeState) {
/*  78 */     super(challengeState);
/*  79 */     this.params = new HashMap<String, String>();
/*  80 */     this.credentialsCharset = Consts.ASCII;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RFC2617Scheme(Charset credentialsCharset) {
/*  88 */     this.params = new HashMap<String, String>();
/*  89 */     this.credentialsCharset = (credentialsCharset != null) ? credentialsCharset : Consts.ASCII;
/*     */   }
/*     */   
/*     */   public RFC2617Scheme() {
/*  93 */     this(Consts.ASCII);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCredentialsCharset() {
/* 101 */     return (this.credentialsCharset != null) ? this.credentialsCharset : Consts.ASCII;
/*     */   }
/*     */   
/*     */   String getCredentialsCharset(HttpRequest request) {
/* 105 */     String charset = (String)request.getParams().getParameter("http.auth.credential-charset");
/* 106 */     if (charset == null) {
/* 107 */       charset = getCredentialsCharset().name();
/*     */     }
/* 109 */     return charset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseChallenge(CharArrayBuffer buffer, int pos, int len) throws MalformedChallengeException {
/* 115 */     BasicHeaderValueParser basicHeaderValueParser = BasicHeaderValueParser.INSTANCE;
/* 116 */     ParserCursor cursor = new ParserCursor(pos, buffer.length());
/* 117 */     HeaderElement[] elements = basicHeaderValueParser.parseElements(buffer, cursor);
/* 118 */     this.params.clear();
/* 119 */     for (HeaderElement element : elements) {
/* 120 */       this.params.put(element.getName().toLowerCase(Locale.ROOT), element.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<String, String> getParameters() {
/* 130 */     return this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/* 142 */     if (name == null) {
/* 143 */       return null;
/*     */     }
/* 145 */     return this.params.get(name.toLowerCase(Locale.ROOT));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 155 */     return getParameter("realm");
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 159 */     out.defaultWriteObject();
/* 160 */     out.writeUTF(this.credentialsCharset.name());
/* 161 */     out.writeObject(this.challengeState);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 166 */     in.defaultReadObject();
/* 167 */     this.credentialsCharset = CharsetUtils.get(in.readUTF());
/* 168 */     if (this.credentialsCharset == null) {
/* 169 */       this.credentialsCharset = Consts.ASCII;
/*     */     }
/* 171 */     this.challengeState = (ChallengeState)in.readObject();
/*     */   }
/*     */   
/*     */   private void readObjectNoData() throws ObjectStreamException {}
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\auth\RFC2617Scheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */