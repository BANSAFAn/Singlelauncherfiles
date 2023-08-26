/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.InvalidCredentialsException;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.auth.NTCredentials;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharArrayBuffer;
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
/*     */ public class NTLMScheme
/*     */   extends AuthSchemeBase
/*     */ {
/*     */   private final NTLMEngine engine;
/*     */   private State state;
/*     */   private String challenge;
/*     */   
/*     */   enum State
/*     */   {
/*  52 */     UNINITIATED,
/*  53 */     CHALLENGE_RECEIVED,
/*  54 */     MSG_TYPE1_GENERATED,
/*  55 */     MSG_TYPE2_RECEVIED,
/*  56 */     MSG_TYPE3_GENERATED,
/*  57 */     FAILED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NTLMScheme(NTLMEngine engine) {
/*  67 */     Args.notNull(engine, "NTLM engine");
/*  68 */     this.engine = engine;
/*  69 */     this.state = State.UNINITIATED;
/*  70 */     this.challenge = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NTLMScheme() {
/*  77 */     this(new NTLMEngineImpl());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSchemeName() {
/*  82 */     return "ntlm";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getParameter(String name) {
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealm() {
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseChallenge(CharArrayBuffer buffer, int beginIndex, int endIndex) throws MalformedChallengeException {
/* 106 */     this.challenge = buffer.substringTrimmed(beginIndex, endIndex);
/* 107 */     if (this.challenge.isEmpty()) {
/* 108 */       if (this.state == State.UNINITIATED) {
/* 109 */         this.state = State.CHALLENGE_RECEIVED;
/*     */       } else {
/* 111 */         this.state = State.FAILED;
/*     */       } 
/*     */     } else {
/* 114 */       if (this.state.compareTo(State.MSG_TYPE1_GENERATED) < 0) {
/* 115 */         this.state = State.FAILED;
/* 116 */         throw new MalformedChallengeException("Out of sequence NTLM response message");
/* 117 */       }  if (this.state == State.MSG_TYPE1_GENERATED) {
/* 118 */         this.state = State.MSG_TYPE2_RECEVIED;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
/* 127 */     NTCredentials ntcredentials = null;
/*     */     try {
/* 129 */       ntcredentials = (NTCredentials)credentials;
/* 130 */     } catch (ClassCastException e) {
/* 131 */       throw new InvalidCredentialsException("Credentials cannot be used for NTLM authentication: " + credentials.getClass().getName());
/*     */     } 
/*     */ 
/*     */     
/* 135 */     String response = null;
/* 136 */     if (this.state == State.FAILED)
/* 137 */       throw new AuthenticationException("NTLM authentication failed"); 
/* 138 */     if (this.state == State.CHALLENGE_RECEIVED) {
/* 139 */       response = this.engine.generateType1Msg(ntcredentials.getDomain(), ntcredentials.getWorkstation());
/*     */ 
/*     */       
/* 142 */       this.state = State.MSG_TYPE1_GENERATED;
/* 143 */     } else if (this.state == State.MSG_TYPE2_RECEVIED) {
/* 144 */       response = this.engine.generateType3Msg(ntcredentials.getUserName(), ntcredentials.getPassword(), ntcredentials.getDomain(), ntcredentials.getWorkstation(), this.challenge);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 150 */       this.state = State.MSG_TYPE3_GENERATED;
/*     */     } else {
/* 152 */       throw new AuthenticationException("Unexpected state: " + this.state);
/*     */     } 
/* 154 */     CharArrayBuffer buffer = new CharArrayBuffer(32);
/* 155 */     if (isProxy()) {
/* 156 */       buffer.append("Proxy-Authorization");
/*     */     } else {
/* 158 */       buffer.append("Authorization");
/*     */     } 
/* 160 */     buffer.append(": NTLM ");
/* 161 */     buffer.append(response);
/* 162 */     return (Header)new BufferedHeader(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 167 */     return (this.state == State.MSG_TYPE3_GENERATED || this.state == State.FAILED);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\auth\NTLMScheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */