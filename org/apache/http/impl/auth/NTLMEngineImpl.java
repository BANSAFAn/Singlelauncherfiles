/*      */ package org.apache.http.impl.auth;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.charset.Charset;
/*      */ import java.security.Key;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.SecureRandom;
/*      */ import java.util.Arrays;
/*      */ import java.util.Locale;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.crypto.spec.SecretKeySpec;
/*      */ import org.apache.commons.codec.binary.Base64;
/*      */ import org.apache.http.Consts;
/*      */ import org.apache.http.annotation.NotThreadSafe;
/*      */ import org.apache.http.util.CharsetUtils;
/*      */ import org.apache.http.util.EncodingUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @NotThreadSafe
/*      */ final class NTLMEngineImpl
/*      */   implements NTLMEngine
/*      */ {
/*   55 */   private static final Charset UNICODE_LITTLE_UNMARKED = CharsetUtils.lookup("UnicodeLittleUnmarked");
/*      */   
/*   57 */   private static final Charset DEFAULT_CHARSET = Consts.ASCII;
/*      */   
/*      */   protected static final int FLAG_REQUEST_UNICODE_ENCODING = 1;
/*      */   
/*      */   protected static final int FLAG_REQUEST_TARGET = 4;
/*      */   
/*      */   protected static final int FLAG_REQUEST_SIGN = 16;
/*      */   
/*      */   protected static final int FLAG_REQUEST_SEAL = 32;
/*      */   
/*      */   protected static final int FLAG_REQUEST_LAN_MANAGER_KEY = 128;
/*      */   
/*      */   protected static final int FLAG_REQUEST_NTLMv1 = 512;
/*      */   protected static final int FLAG_DOMAIN_PRESENT = 4096;
/*      */   protected static final int FLAG_WORKSTATION_PRESENT = 8192;
/*      */   protected static final int FLAG_REQUEST_ALWAYS_SIGN = 32768;
/*      */   protected static final int FLAG_REQUEST_NTLM2_SESSION = 524288;
/*      */   protected static final int FLAG_REQUEST_VERSION = 33554432;
/*      */   protected static final int FLAG_TARGETINFO_PRESENT = 8388608;
/*      */   protected static final int FLAG_REQUEST_128BIT_KEY_EXCH = 536870912;
/*      */   protected static final int FLAG_REQUEST_EXPLICIT_KEY_EXCH = 1073741824;
/*      */   protected static final int FLAG_REQUEST_56BIT_ENCRYPTION = -2147483648;
/*      */   private static final SecureRandom RND_GEN;
/*      */   private static final byte[] SIGNATURE;
/*      */   
/*      */   static {
/*   83 */     SecureRandom rnd = null;
/*      */     try {
/*   85 */       rnd = SecureRandom.getInstance("SHA1PRNG");
/*   86 */     } catch (Exception ignore) {}
/*      */     
/*   88 */     RND_GEN = rnd;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   95 */     byte[] bytesWithoutNull = "NTLMSSP".getBytes(Consts.ASCII);
/*   96 */     SIGNATURE = new byte[bytesWithoutNull.length + 1];
/*   97 */     System.arraycopy(bytesWithoutNull, 0, SIGNATURE, 0, bytesWithoutNull.length);
/*   98 */     SIGNATURE[bytesWithoutNull.length] = 0;
/*      */   }
/*      */   
/*  101 */   private static final String TYPE_1_MESSAGE = (new Type1Message()).getResponse();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getResponseFor(String message, String username, String password, String host, String domain) throws NTLMEngineException {
/*      */     String response;
/*  124 */     if (message == null || message.trim().equals("")) {
/*  125 */       response = getType1Message(host, domain);
/*      */     } else {
/*  127 */       Type2Message t2m = new Type2Message(message);
/*  128 */       response = getType3Message(username, password, host, domain, t2m.getChallenge(), t2m.getFlags(), t2m.getTarget(), t2m.getTargetInfo());
/*      */     } 
/*      */     
/*  131 */     return response;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getType1Message(String host, String domain) throws NTLMEngineException {
/*  148 */     return TYPE_1_MESSAGE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getType3Message(String user, String password, String host, String domain, byte[] nonce, int type2Flags, String target, byte[] targetInformation) throws NTLMEngineException {
/*  174 */     return (new Type3Message(domain, host, user, password, nonce, type2Flags, target, targetInformation)).getResponse();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static String stripDotSuffix(String value) {
/*  180 */     if (value == null) {
/*  181 */       return null;
/*      */     }
/*  183 */     int index = value.indexOf(".");
/*  184 */     if (index != -1) {
/*  185 */       return value.substring(0, index);
/*      */     }
/*  187 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private static String convertHost(String host) {
/*  192 */     return stripDotSuffix(host);
/*      */   }
/*      */ 
/*      */   
/*      */   private static String convertDomain(String domain) {
/*  197 */     return stripDotSuffix(domain);
/*      */   }
/*      */   
/*      */   private static int readULong(byte[] src, int index) throws NTLMEngineException {
/*  201 */     if (src.length < index + 4) {
/*  202 */       throw new NTLMEngineException("NTLM authentication - buffer too small for DWORD");
/*      */     }
/*  204 */     return src[index] & 0xFF | (src[index + 1] & 0xFF) << 8 | (src[index + 2] & 0xFF) << 16 | (src[index + 3] & 0xFF) << 24;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int readUShort(byte[] src, int index) throws NTLMEngineException {
/*  209 */     if (src.length < index + 2) {
/*  210 */       throw new NTLMEngineException("NTLM authentication - buffer too small for WORD");
/*      */     }
/*  212 */     return src[index] & 0xFF | (src[index + 1] & 0xFF) << 8;
/*      */   }
/*      */   
/*      */   private static byte[] readSecurityBuffer(byte[] src, int index) throws NTLMEngineException {
/*  216 */     int length = readUShort(src, index);
/*  217 */     int offset = readULong(src, index + 4);
/*  218 */     if (src.length < offset + length) {
/*  219 */       throw new NTLMEngineException("NTLM authentication - buffer too small for data item");
/*      */     }
/*      */     
/*  222 */     byte[] buffer = new byte[length];
/*  223 */     System.arraycopy(src, offset, buffer, 0, length);
/*  224 */     return buffer;
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte[] makeRandomChallenge() throws NTLMEngineException {
/*  229 */     if (RND_GEN == null) {
/*  230 */       throw new NTLMEngineException("Random generator not available");
/*      */     }
/*  232 */     byte[] rval = new byte[8];
/*  233 */     synchronized (RND_GEN) {
/*  234 */       RND_GEN.nextBytes(rval);
/*      */     } 
/*  236 */     return rval;
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte[] makeSecondaryKey() throws NTLMEngineException {
/*  241 */     if (RND_GEN == null) {
/*  242 */       throw new NTLMEngineException("Random generator not available");
/*      */     }
/*  244 */     byte[] rval = new byte[16];
/*  245 */     synchronized (RND_GEN) {
/*  246 */       RND_GEN.nextBytes(rval);
/*      */     } 
/*  248 */     return rval;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static class CipherGen
/*      */   {
/*      */     protected final String domain;
/*      */     
/*      */     protected final String user;
/*      */     
/*      */     protected final String password;
/*      */     
/*      */     protected final byte[] challenge;
/*      */     protected final String target;
/*      */     protected final byte[] targetInformation;
/*      */     protected byte[] clientChallenge;
/*      */     protected byte[] clientChallenge2;
/*      */     protected byte[] secondaryKey;
/*      */     protected byte[] timestamp;
/*  267 */     protected byte[] lmHash = null;
/*  268 */     protected byte[] lmResponse = null;
/*  269 */     protected byte[] ntlmHash = null;
/*  270 */     protected byte[] ntlmResponse = null;
/*  271 */     protected byte[] ntlmv2Hash = null;
/*  272 */     protected byte[] lmv2Hash = null;
/*  273 */     protected byte[] lmv2Response = null;
/*  274 */     protected byte[] ntlmv2Blob = null;
/*  275 */     protected byte[] ntlmv2Response = null;
/*  276 */     protected byte[] ntlm2SessionResponse = null;
/*  277 */     protected byte[] lm2SessionResponse = null;
/*  278 */     protected byte[] lmUserSessionKey = null;
/*  279 */     protected byte[] ntlmUserSessionKey = null;
/*  280 */     protected byte[] ntlmv2UserSessionKey = null;
/*  281 */     protected byte[] ntlm2SessionResponseUserSessionKey = null;
/*  282 */     protected byte[] lanManagerSessionKey = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public CipherGen(String domain, String user, String password, byte[] challenge, String target, byte[] targetInformation, byte[] clientChallenge, byte[] clientChallenge2, byte[] secondaryKey, byte[] timestamp) {
/*  288 */       this.domain = domain;
/*  289 */       this.target = target;
/*  290 */       this.user = user;
/*  291 */       this.password = password;
/*  292 */       this.challenge = challenge;
/*  293 */       this.targetInformation = targetInformation;
/*  294 */       this.clientChallenge = clientChallenge;
/*  295 */       this.clientChallenge2 = clientChallenge2;
/*  296 */       this.secondaryKey = secondaryKey;
/*  297 */       this.timestamp = timestamp;
/*      */     }
/*      */ 
/*      */     
/*      */     public CipherGen(String domain, String user, String password, byte[] challenge, String target, byte[] targetInformation) {
/*  302 */       this(domain, user, password, challenge, target, targetInformation, null, null, null, null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getClientChallenge() throws NTLMEngineException {
/*  308 */       if (this.clientChallenge == null) {
/*  309 */         this.clientChallenge = NTLMEngineImpl.makeRandomChallenge();
/*      */       }
/*  311 */       return this.clientChallenge;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getClientChallenge2() throws NTLMEngineException {
/*  317 */       if (this.clientChallenge2 == null) {
/*  318 */         this.clientChallenge2 = NTLMEngineImpl.makeRandomChallenge();
/*      */       }
/*  320 */       return this.clientChallenge2;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getSecondaryKey() throws NTLMEngineException {
/*  326 */       if (this.secondaryKey == null) {
/*  327 */         this.secondaryKey = NTLMEngineImpl.makeSecondaryKey();
/*      */       }
/*  329 */       return this.secondaryKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMHash() throws NTLMEngineException {
/*  335 */       if (this.lmHash == null) {
/*  336 */         this.lmHash = NTLMEngineImpl.lmHash(this.password);
/*      */       }
/*  338 */       return this.lmHash;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMResponse() throws NTLMEngineException {
/*  344 */       if (this.lmResponse == null) {
/*  345 */         this.lmResponse = NTLMEngineImpl.lmResponse(getLMHash(), this.challenge);
/*      */       }
/*  347 */       return this.lmResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMHash() throws NTLMEngineException {
/*  353 */       if (this.ntlmHash == null) {
/*  354 */         this.ntlmHash = NTLMEngineImpl.ntlmHash(this.password);
/*      */       }
/*  356 */       return this.ntlmHash;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMResponse() throws NTLMEngineException {
/*  362 */       if (this.ntlmResponse == null) {
/*  363 */         this.ntlmResponse = NTLMEngineImpl.lmResponse(getNTLMHash(), this.challenge);
/*      */       }
/*  365 */       return this.ntlmResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMv2Hash() throws NTLMEngineException {
/*  371 */       if (this.lmv2Hash == null) {
/*  372 */         this.lmv2Hash = NTLMEngineImpl.lmv2Hash(this.domain, this.user, getNTLMHash());
/*      */       }
/*  374 */       return this.lmv2Hash;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2Hash() throws NTLMEngineException {
/*  380 */       if (this.ntlmv2Hash == null) {
/*  381 */         this.ntlmv2Hash = NTLMEngineImpl.ntlmv2Hash(this.domain, this.user, getNTLMHash());
/*      */       }
/*  383 */       return this.ntlmv2Hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] getTimestamp() {
/*  388 */       if (this.timestamp == null) {
/*  389 */         long time = System.currentTimeMillis();
/*  390 */         time += 11644473600000L;
/*  391 */         time *= 10000L;
/*      */         
/*  393 */         this.timestamp = new byte[8];
/*  394 */         for (int i = 0; i < 8; i++) {
/*  395 */           this.timestamp[i] = (byte)(int)time;
/*  396 */           time >>>= 8L;
/*      */         } 
/*      */       } 
/*  399 */       return this.timestamp;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2Blob() throws NTLMEngineException {
/*  405 */       if (this.ntlmv2Blob == null) {
/*  406 */         this.ntlmv2Blob = NTLMEngineImpl.createBlob(getClientChallenge2(), this.targetInformation, getTimestamp());
/*      */       }
/*  408 */       return this.ntlmv2Blob;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2Response() throws NTLMEngineException {
/*  414 */       if (this.ntlmv2Response == null) {
/*  415 */         this.ntlmv2Response = NTLMEngineImpl.lmv2Response(getNTLMv2Hash(), this.challenge, getNTLMv2Blob());
/*      */       }
/*  417 */       return this.ntlmv2Response;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMv2Response() throws NTLMEngineException {
/*  423 */       if (this.lmv2Response == null) {
/*  424 */         this.lmv2Response = NTLMEngineImpl.lmv2Response(getLMv2Hash(), this.challenge, getClientChallenge());
/*      */       }
/*  426 */       return this.lmv2Response;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLM2SessionResponse() throws NTLMEngineException {
/*  432 */       if (this.ntlm2SessionResponse == null) {
/*  433 */         this.ntlm2SessionResponse = NTLMEngineImpl.ntlm2SessionResponse(getNTLMHash(), this.challenge, getClientChallenge());
/*      */       }
/*  435 */       return this.ntlm2SessionResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLM2SessionResponse() throws NTLMEngineException {
/*  441 */       if (this.lm2SessionResponse == null) {
/*  442 */         byte[] clntChallenge = getClientChallenge();
/*  443 */         this.lm2SessionResponse = new byte[24];
/*  444 */         System.arraycopy(clntChallenge, 0, this.lm2SessionResponse, 0, clntChallenge.length);
/*  445 */         Arrays.fill(this.lm2SessionResponse, clntChallenge.length, this.lm2SessionResponse.length, (byte)0);
/*      */       } 
/*  447 */       return this.lm2SessionResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMUserSessionKey() throws NTLMEngineException {
/*  453 */       if (this.lmUserSessionKey == null) {
/*  454 */         this.lmUserSessionKey = new byte[16];
/*  455 */         System.arraycopy(getLMHash(), 0, this.lmUserSessionKey, 0, 8);
/*  456 */         Arrays.fill(this.lmUserSessionKey, 8, 16, (byte)0);
/*      */       } 
/*  458 */       return this.lmUserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMUserSessionKey() throws NTLMEngineException {
/*  464 */       if (this.ntlmUserSessionKey == null) {
/*  465 */         NTLMEngineImpl.MD4 md4 = new NTLMEngineImpl.MD4();
/*  466 */         md4.update(getNTLMHash());
/*  467 */         this.ntlmUserSessionKey = md4.getOutput();
/*      */       } 
/*  469 */       return this.ntlmUserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2UserSessionKey() throws NTLMEngineException {
/*  475 */       if (this.ntlmv2UserSessionKey == null) {
/*  476 */         byte[] ntlmv2hash = getNTLMv2Hash();
/*  477 */         byte[] truncatedResponse = new byte[16];
/*  478 */         System.arraycopy(getNTLMv2Response(), 0, truncatedResponse, 0, 16);
/*  479 */         this.ntlmv2UserSessionKey = NTLMEngineImpl.hmacMD5(truncatedResponse, ntlmv2hash);
/*      */       } 
/*  481 */       return this.ntlmv2UserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLM2SessionResponseUserSessionKey() throws NTLMEngineException {
/*  487 */       if (this.ntlm2SessionResponseUserSessionKey == null) {
/*  488 */         byte[] ntlm2SessionResponseNonce = getLM2SessionResponse();
/*  489 */         byte[] sessionNonce = new byte[this.challenge.length + ntlm2SessionResponseNonce.length];
/*  490 */         System.arraycopy(this.challenge, 0, sessionNonce, 0, this.challenge.length);
/*  491 */         System.arraycopy(ntlm2SessionResponseNonce, 0, sessionNonce, this.challenge.length, ntlm2SessionResponseNonce.length);
/*  492 */         this.ntlm2SessionResponseUserSessionKey = NTLMEngineImpl.hmacMD5(sessionNonce, getNTLMUserSessionKey());
/*      */       } 
/*  494 */       return this.ntlm2SessionResponseUserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLanManagerSessionKey() throws NTLMEngineException {
/*  500 */       if (this.lanManagerSessionKey == null) {
/*      */         try {
/*  502 */           byte[] keyBytes = new byte[14];
/*  503 */           System.arraycopy(getLMHash(), 0, keyBytes, 0, 8);
/*  504 */           Arrays.fill(keyBytes, 8, keyBytes.length, (byte)-67);
/*  505 */           Key lowKey = NTLMEngineImpl.createDESKey(keyBytes, 0);
/*  506 */           Key highKey = NTLMEngineImpl.createDESKey(keyBytes, 7);
/*  507 */           byte[] truncatedResponse = new byte[8];
/*  508 */           System.arraycopy(getLMResponse(), 0, truncatedResponse, 0, truncatedResponse.length);
/*  509 */           Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/*  510 */           des.init(1, lowKey);
/*  511 */           byte[] lowPart = des.doFinal(truncatedResponse);
/*  512 */           des = Cipher.getInstance("DES/ECB/NoPadding");
/*  513 */           des.init(1, highKey);
/*  514 */           byte[] highPart = des.doFinal(truncatedResponse);
/*  515 */           this.lanManagerSessionKey = new byte[16];
/*  516 */           System.arraycopy(lowPart, 0, this.lanManagerSessionKey, 0, lowPart.length);
/*  517 */           System.arraycopy(highPart, 0, this.lanManagerSessionKey, lowPart.length, highPart.length);
/*  518 */         } catch (Exception e) {
/*  519 */           throw new NTLMEngineException(e.getMessage(), e);
/*      */         } 
/*      */       }
/*  522 */       return this.lanManagerSessionKey;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] hmacMD5(byte[] value, byte[] key) throws NTLMEngineException {
/*  529 */     HMACMD5 hmacMD5 = new HMACMD5(key);
/*  530 */     hmacMD5.update(value);
/*  531 */     return hmacMD5.getOutput();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] RC4(byte[] value, byte[] key) throws NTLMEngineException {
/*      */     try {
/*  538 */       Cipher rc4 = Cipher.getInstance("RC4");
/*  539 */       rc4.init(1, new SecretKeySpec(key, "RC4"));
/*  540 */       return rc4.doFinal(value);
/*  541 */     } catch (Exception e) {
/*  542 */       throw new NTLMEngineException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] ntlm2SessionResponse(byte[] ntlmHash, byte[] challenge, byte[] clientChallenge) throws NTLMEngineException {
/*      */     try {
/*  557 */       MessageDigest md5 = MessageDigest.getInstance("MD5");
/*  558 */       md5.update(challenge);
/*  559 */       md5.update(clientChallenge);
/*  560 */       byte[] digest = md5.digest();
/*      */       
/*  562 */       byte[] sessionHash = new byte[8];
/*  563 */       System.arraycopy(digest, 0, sessionHash, 0, 8);
/*  564 */       return lmResponse(ntlmHash, sessionHash);
/*  565 */     } catch (Exception e) {
/*  566 */       if (e instanceof NTLMEngineException) {
/*  567 */         throw (NTLMEngineException)e;
/*      */       }
/*  569 */       throw new NTLMEngineException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] lmHash(String password) throws NTLMEngineException {
/*      */     try {
/*  584 */       byte[] oemPassword = password.toUpperCase(Locale.ROOT).getBytes(Consts.ASCII);
/*  585 */       int length = Math.min(oemPassword.length, 14);
/*  586 */       byte[] keyBytes = new byte[14];
/*  587 */       System.arraycopy(oemPassword, 0, keyBytes, 0, length);
/*  588 */       Key lowKey = createDESKey(keyBytes, 0);
/*  589 */       Key highKey = createDESKey(keyBytes, 7);
/*  590 */       byte[] magicConstant = "KGS!@#$%".getBytes(Consts.ASCII);
/*  591 */       Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/*  592 */       des.init(1, lowKey);
/*  593 */       byte[] lowHash = des.doFinal(magicConstant);
/*  594 */       des.init(1, highKey);
/*  595 */       byte[] highHash = des.doFinal(magicConstant);
/*  596 */       byte[] lmHash = new byte[16];
/*  597 */       System.arraycopy(lowHash, 0, lmHash, 0, 8);
/*  598 */       System.arraycopy(highHash, 0, lmHash, 8, 8);
/*  599 */       return lmHash;
/*  600 */     } catch (Exception e) {
/*  601 */       throw new NTLMEngineException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] ntlmHash(String password) throws NTLMEngineException {
/*  615 */     if (UNICODE_LITTLE_UNMARKED == null) {
/*  616 */       throw new NTLMEngineException("Unicode not supported");
/*      */     }
/*  618 */     byte[] unicodePassword = password.getBytes(UNICODE_LITTLE_UNMARKED);
/*  619 */     MD4 md4 = new MD4();
/*  620 */     md4.update(unicodePassword);
/*  621 */     return md4.getOutput();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] lmv2Hash(String domain, String user, byte[] ntlmHash) throws NTLMEngineException {
/*  632 */     if (UNICODE_LITTLE_UNMARKED == null) {
/*  633 */       throw new NTLMEngineException("Unicode not supported");
/*      */     }
/*  635 */     HMACMD5 hmacMD5 = new HMACMD5(ntlmHash);
/*      */     
/*  637 */     hmacMD5.update(user.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
/*  638 */     if (domain != null) {
/*  639 */       hmacMD5.update(domain.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
/*      */     }
/*  641 */     return hmacMD5.getOutput();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] ntlmv2Hash(String domain, String user, byte[] ntlmHash) throws NTLMEngineException {
/*  652 */     if (UNICODE_LITTLE_UNMARKED == null) {
/*  653 */       throw new NTLMEngineException("Unicode not supported");
/*      */     }
/*  655 */     HMACMD5 hmacMD5 = new HMACMD5(ntlmHash);
/*      */     
/*  657 */     hmacMD5.update(user.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
/*  658 */     if (domain != null) {
/*  659 */       hmacMD5.update(domain.getBytes(UNICODE_LITTLE_UNMARKED));
/*      */     }
/*  661 */     return hmacMD5.getOutput();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] lmResponse(byte[] hash, byte[] challenge) throws NTLMEngineException {
/*      */     try {
/*  676 */       byte[] keyBytes = new byte[21];
/*  677 */       System.arraycopy(hash, 0, keyBytes, 0, 16);
/*  678 */       Key lowKey = createDESKey(keyBytes, 0);
/*  679 */       Key middleKey = createDESKey(keyBytes, 7);
/*  680 */       Key highKey = createDESKey(keyBytes, 14);
/*  681 */       Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/*  682 */       des.init(1, lowKey);
/*  683 */       byte[] lowResponse = des.doFinal(challenge);
/*  684 */       des.init(1, middleKey);
/*  685 */       byte[] middleResponse = des.doFinal(challenge);
/*  686 */       des.init(1, highKey);
/*  687 */       byte[] highResponse = des.doFinal(challenge);
/*  688 */       byte[] lmResponse = new byte[24];
/*  689 */       System.arraycopy(lowResponse, 0, lmResponse, 0, 8);
/*  690 */       System.arraycopy(middleResponse, 0, lmResponse, 8, 8);
/*  691 */       System.arraycopy(highResponse, 0, lmResponse, 16, 8);
/*  692 */       return lmResponse;
/*  693 */     } catch (Exception e) {
/*  694 */       throw new NTLMEngineException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] lmv2Response(byte[] hash, byte[] challenge, byte[] clientData) throws NTLMEngineException {
/*  714 */     HMACMD5 hmacMD5 = new HMACMD5(hash);
/*  715 */     hmacMD5.update(challenge);
/*  716 */     hmacMD5.update(clientData);
/*  717 */     byte[] mac = hmacMD5.getOutput();
/*  718 */     byte[] lmv2Response = new byte[mac.length + clientData.length];
/*  719 */     System.arraycopy(mac, 0, lmv2Response, 0, mac.length);
/*  720 */     System.arraycopy(clientData, 0, lmv2Response, mac.length, clientData.length);
/*  721 */     return lmv2Response;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] createBlob(byte[] clientChallenge, byte[] targetInformation, byte[] timestamp) {
/*  736 */     byte[] blobSignature = { 1, 1, 0, 0 };
/*  737 */     byte[] reserved = { 0, 0, 0, 0 };
/*  738 */     byte[] unknown1 = { 0, 0, 0, 0 };
/*  739 */     byte[] unknown2 = { 0, 0, 0, 0 };
/*  740 */     byte[] blob = new byte[blobSignature.length + reserved.length + timestamp.length + 8 + unknown1.length + targetInformation.length + unknown2.length];
/*      */     
/*  742 */     int offset = 0;
/*  743 */     System.arraycopy(blobSignature, 0, blob, offset, blobSignature.length);
/*  744 */     offset += blobSignature.length;
/*  745 */     System.arraycopy(reserved, 0, blob, offset, reserved.length);
/*  746 */     offset += reserved.length;
/*  747 */     System.arraycopy(timestamp, 0, blob, offset, timestamp.length);
/*  748 */     offset += timestamp.length;
/*  749 */     System.arraycopy(clientChallenge, 0, blob, offset, 8);
/*  750 */     offset += 8;
/*  751 */     System.arraycopy(unknown1, 0, blob, offset, unknown1.length);
/*  752 */     offset += unknown1.length;
/*  753 */     System.arraycopy(targetInformation, 0, blob, offset, targetInformation.length);
/*  754 */     offset += targetInformation.length;
/*  755 */     System.arraycopy(unknown2, 0, blob, offset, unknown2.length);
/*  756 */     offset += unknown2.length;
/*  757 */     return blob;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Key createDESKey(byte[] bytes, int offset) {
/*  773 */     byte[] keyBytes = new byte[7];
/*  774 */     System.arraycopy(bytes, offset, keyBytes, 0, 7);
/*  775 */     byte[] material = new byte[8];
/*  776 */     material[0] = keyBytes[0];
/*  777 */     material[1] = (byte)(keyBytes[0] << 7 | (keyBytes[1] & 0xFF) >>> 1);
/*  778 */     material[2] = (byte)(keyBytes[1] << 6 | (keyBytes[2] & 0xFF) >>> 2);
/*  779 */     material[3] = (byte)(keyBytes[2] << 5 | (keyBytes[3] & 0xFF) >>> 3);
/*  780 */     material[4] = (byte)(keyBytes[3] << 4 | (keyBytes[4] & 0xFF) >>> 4);
/*  781 */     material[5] = (byte)(keyBytes[4] << 3 | (keyBytes[5] & 0xFF) >>> 5);
/*  782 */     material[6] = (byte)(keyBytes[5] << 2 | (keyBytes[6] & 0xFF) >>> 6);
/*  783 */     material[7] = (byte)(keyBytes[6] << 1);
/*  784 */     oddParity(material);
/*  785 */     return new SecretKeySpec(material, "DES");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void oddParity(byte[] bytes) {
/*  795 */     for (int i = 0; i < bytes.length; i++) {
/*  796 */       byte b = bytes[i];
/*  797 */       boolean needsParity = (((b >>> 7 ^ b >>> 6 ^ b >>> 5 ^ b >>> 4 ^ b >>> 3 ^ b >>> 2 ^ b >>> 1) & 0x1) == 0);
/*      */       
/*  799 */       if (needsParity) {
/*  800 */         bytes[i] = (byte)(bytes[i] | 0x1);
/*      */       } else {
/*  802 */         bytes[i] = (byte)(bytes[i] & 0xFFFFFFFE);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static class NTLMMessage
/*      */   {
/*  810 */     private byte[] messageContents = null;
/*      */ 
/*      */     
/*  813 */     private int currentOutputPosition = 0;
/*      */ 
/*      */ 
/*      */     
/*      */     NTLMMessage() {}
/*      */ 
/*      */     
/*      */     NTLMMessage(String messageBody, int expectedType) throws NTLMEngineException {
/*  821 */       this.messageContents = Base64.decodeBase64(messageBody.getBytes(NTLMEngineImpl.DEFAULT_CHARSET));
/*      */       
/*  823 */       if (this.messageContents.length < NTLMEngineImpl.SIGNATURE.length) {
/*  824 */         throw new NTLMEngineException("NTLM message decoding error - packet too short");
/*      */       }
/*  826 */       int i = 0;
/*  827 */       while (i < NTLMEngineImpl.SIGNATURE.length) {
/*  828 */         if (this.messageContents[i] != NTLMEngineImpl.SIGNATURE[i]) {
/*  829 */           throw new NTLMEngineException("NTLM message expected - instead got unrecognized bytes");
/*      */         }
/*      */         
/*  832 */         i++;
/*      */       } 
/*      */ 
/*      */       
/*  836 */       int type = readULong(NTLMEngineImpl.SIGNATURE.length);
/*  837 */       if (type != expectedType) {
/*  838 */         throw new NTLMEngineException("NTLM type " + Integer.toString(expectedType) + " message expected - instead got type " + Integer.toString(type));
/*      */       }
/*      */ 
/*      */       
/*  842 */       this.currentOutputPosition = this.messageContents.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int getPreambleLength() {
/*  850 */       return NTLMEngineImpl.SIGNATURE.length + 4;
/*      */     }
/*      */ 
/*      */     
/*      */     protected int getMessageLength() {
/*  855 */       return this.currentOutputPosition;
/*      */     }
/*      */ 
/*      */     
/*      */     protected byte readByte(int position) throws NTLMEngineException {
/*  860 */       if (this.messageContents.length < position + 1) {
/*  861 */         throw new NTLMEngineException("NTLM: Message too short");
/*      */       }
/*  863 */       return this.messageContents[position];
/*      */     }
/*      */ 
/*      */     
/*      */     protected void readBytes(byte[] buffer, int position) throws NTLMEngineException {
/*  868 */       if (this.messageContents.length < position + buffer.length) {
/*  869 */         throw new NTLMEngineException("NTLM: Message too short");
/*      */       }
/*  871 */       System.arraycopy(this.messageContents, position, buffer, 0, buffer.length);
/*      */     }
/*      */ 
/*      */     
/*      */     protected int readUShort(int position) throws NTLMEngineException {
/*  876 */       return NTLMEngineImpl.readUShort(this.messageContents, position);
/*      */     }
/*      */ 
/*      */     
/*      */     protected int readULong(int position) throws NTLMEngineException {
/*  881 */       return NTLMEngineImpl.readULong(this.messageContents, position);
/*      */     }
/*      */ 
/*      */     
/*      */     protected byte[] readSecurityBuffer(int position) throws NTLMEngineException {
/*  886 */       return NTLMEngineImpl.readSecurityBuffer(this.messageContents, position);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void prepareResponse(int maxlength, int messageType) {
/*  898 */       this.messageContents = new byte[maxlength];
/*  899 */       this.currentOutputPosition = 0;
/*  900 */       addBytes(NTLMEngineImpl.SIGNATURE);
/*  901 */       addULong(messageType);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void addByte(byte b) {
/*  911 */       this.messageContents[this.currentOutputPosition] = b;
/*  912 */       this.currentOutputPosition++;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void addBytes(byte[] bytes) {
/*  922 */       if (bytes == null) {
/*      */         return;
/*      */       }
/*  925 */       for (byte b : bytes) {
/*  926 */         this.messageContents[this.currentOutputPosition] = b;
/*  927 */         this.currentOutputPosition++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addUShort(int value) {
/*  933 */       addByte((byte)(value & 0xFF));
/*  934 */       addByte((byte)(value >> 8 & 0xFF));
/*      */     }
/*      */ 
/*      */     
/*      */     protected void addULong(int value) {
/*  939 */       addByte((byte)(value & 0xFF));
/*  940 */       addByte((byte)(value >> 8 & 0xFF));
/*  941 */       addByte((byte)(value >> 16 & 0xFF));
/*  942 */       addByte((byte)(value >> 24 & 0xFF));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getResponse() {
/*      */       byte[] resp;
/*  953 */       if (this.messageContents.length > this.currentOutputPosition) {
/*  954 */         byte[] tmp = new byte[this.currentOutputPosition];
/*  955 */         System.arraycopy(this.messageContents, 0, tmp, 0, this.currentOutputPosition);
/*  956 */         resp = tmp;
/*      */       } else {
/*  958 */         resp = this.messageContents;
/*      */       } 
/*  960 */       return EncodingUtils.getAsciiString(Base64.encodeBase64(resp));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class Type1Message
/*      */     extends NTLMMessage
/*      */   {
/*      */     private final byte[] hostBytes;
/*      */     
/*      */     private final byte[] domainBytes;
/*      */     
/*      */     Type1Message(String domain, String host) throws NTLMEngineException {
/*  973 */       if (NTLMEngineImpl.UNICODE_LITTLE_UNMARKED == null) {
/*  974 */         throw new NTLMEngineException("Unicode not supported");
/*      */       }
/*      */       
/*  977 */       String unqualifiedHost = NTLMEngineImpl.convertHost(host);
/*      */       
/*  979 */       String unqualifiedDomain = NTLMEngineImpl.convertDomain(domain);
/*      */       
/*  981 */       this.hostBytes = (unqualifiedHost != null) ? unqualifiedHost.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
/*      */       
/*  983 */       this.domainBytes = (unqualifiedDomain != null) ? unqualifiedDomain.toUpperCase(Locale.ROOT).getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Type1Message() {
/*  989 */       this.hostBytes = null;
/*  990 */       this.domainBytes = null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getResponse() {
/* 1000 */       int finalLength = 40;
/*      */ 
/*      */ 
/*      */       
/* 1004 */       prepareResponse(40, 1);
/*      */ 
/*      */       
/* 1007 */       addULong(-1576500735);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1032 */       addUShort(0);
/* 1033 */       addUShort(0);
/*      */ 
/*      */       
/* 1036 */       addULong(40);
/*      */ 
/*      */       
/* 1039 */       addUShort(0);
/* 1040 */       addUShort(0);
/*      */ 
/*      */       
/* 1043 */       addULong(40);
/*      */ 
/*      */       
/* 1046 */       addUShort(261);
/*      */       
/* 1048 */       addULong(2600);
/*      */       
/* 1050 */       addUShort(3840);
/*      */ 
/*      */       
/* 1053 */       if (this.hostBytes != null) {
/* 1054 */         addBytes(this.hostBytes);
/*      */       }
/*      */       
/* 1057 */       if (this.domainBytes != null) {
/* 1058 */         addBytes(this.domainBytes);
/*      */       }
/*      */       
/* 1061 */       return super.getResponse();
/*      */     }
/*      */   }
/*      */   
/*      */   static class Type2Message
/*      */     extends NTLMMessage
/*      */   {
/*      */     protected byte[] challenge;
/*      */     protected String target;
/*      */     protected byte[] targetInfo;
/*      */     protected int flags;
/*      */     
/*      */     Type2Message(String message) throws NTLMEngineException {
/* 1074 */       super(message, 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1091 */       this.challenge = new byte[8];
/* 1092 */       readBytes(this.challenge, 24);
/*      */       
/* 1094 */       this.flags = readULong(20);
/*      */       
/* 1096 */       if ((this.flags & 0x1) == 0) {
/* 1097 */         throw new NTLMEngineException("NTLM type 2 message indicates no support for Unicode. Flags are: " + Integer.toString(this.flags));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1103 */       this.target = null;
/*      */ 
/*      */ 
/*      */       
/* 1107 */       if (getMessageLength() >= 20) {
/* 1108 */         byte[] bytes = readSecurityBuffer(12);
/* 1109 */         if (bytes.length != 0) {
/*      */           try {
/* 1111 */             this.target = new String(bytes, "UnicodeLittleUnmarked");
/* 1112 */           } catch (UnsupportedEncodingException e) {
/* 1113 */             throw new NTLMEngineException(e.getMessage(), e);
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1119 */       this.targetInfo = null;
/*      */       
/* 1121 */       if (getMessageLength() >= 48) {
/* 1122 */         byte[] bytes = readSecurityBuffer(40);
/* 1123 */         if (bytes.length != 0) {
/* 1124 */           this.targetInfo = bytes;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     byte[] getChallenge() {
/* 1131 */       return this.challenge;
/*      */     }
/*      */ 
/*      */     
/*      */     String getTarget() {
/* 1136 */       return this.target;
/*      */     }
/*      */ 
/*      */     
/*      */     byte[] getTargetInfo() {
/* 1141 */       return this.targetInfo;
/*      */     }
/*      */ 
/*      */     
/*      */     int getFlags() {
/* 1146 */       return this.flags;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class Type3Message
/*      */     extends NTLMMessage
/*      */   {
/*      */     protected int type2Flags;
/*      */     
/*      */     protected byte[] domainBytes;
/*      */     
/*      */     protected byte[] hostBytes;
/*      */     
/*      */     protected byte[] userBytes;
/*      */     
/*      */     protected byte[] lmResp;
/*      */     
/*      */     protected byte[] ntResp;
/*      */     
/*      */     protected byte[] sessionKey;
/*      */     
/*      */     Type3Message(String domain, String host, String user, String password, byte[] nonce, int type2Flags, String target, byte[] targetInformation) throws NTLMEngineException {
/*      */       byte[] userSessionKey;
/* 1170 */       this.type2Flags = type2Flags;
/*      */ 
/*      */       
/* 1173 */       String unqualifiedHost = NTLMEngineImpl.convertHost(host);
/*      */       
/* 1175 */       String unqualifiedDomain = NTLMEngineImpl.convertDomain(domain);
/*      */ 
/*      */       
/* 1178 */       NTLMEngineImpl.CipherGen gen = new NTLMEngineImpl.CipherGen(unqualifiedDomain, user, password, nonce, target, targetInformation);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1186 */         if ((type2Flags & 0x800000) != 0 && targetInformation != null && target != null) {
/*      */ 
/*      */           
/* 1189 */           this.ntResp = gen.getNTLMv2Response();
/* 1190 */           this.lmResp = gen.getLMv2Response();
/* 1191 */           if ((type2Flags & 0x80) != 0) {
/* 1192 */             userSessionKey = gen.getLanManagerSessionKey();
/*      */           } else {
/* 1194 */             userSessionKey = gen.getNTLMv2UserSessionKey();
/*      */           }
/*      */         
/*      */         }
/* 1198 */         else if ((type2Flags & 0x80000) != 0) {
/*      */           
/* 1200 */           this.ntResp = gen.getNTLM2SessionResponse();
/* 1201 */           this.lmResp = gen.getLM2SessionResponse();
/* 1202 */           if ((type2Flags & 0x80) != 0) {
/* 1203 */             userSessionKey = gen.getLanManagerSessionKey();
/*      */           } else {
/* 1205 */             userSessionKey = gen.getNTLM2SessionResponseUserSessionKey();
/*      */           } 
/*      */         } else {
/* 1208 */           this.ntResp = gen.getNTLMResponse();
/* 1209 */           this.lmResp = gen.getLMResponse();
/* 1210 */           if ((type2Flags & 0x80) != 0) {
/* 1211 */             userSessionKey = gen.getLanManagerSessionKey();
/*      */           } else {
/* 1213 */             userSessionKey = gen.getNTLMUserSessionKey();
/*      */           }
/*      */         
/*      */         } 
/* 1217 */       } catch (NTLMEngineException e) {
/*      */ 
/*      */         
/* 1220 */         this.ntResp = new byte[0];
/* 1221 */         this.lmResp = gen.getLMResponse();
/* 1222 */         if ((type2Flags & 0x80) != 0) {
/* 1223 */           userSessionKey = gen.getLanManagerSessionKey();
/*      */         } else {
/* 1225 */           userSessionKey = gen.getLMUserSessionKey();
/*      */         } 
/*      */       } 
/*      */       
/* 1229 */       if ((type2Flags & 0x10) != 0) {
/* 1230 */         if ((type2Flags & 0x40000000) != 0) {
/* 1231 */           this.sessionKey = NTLMEngineImpl.RC4(gen.getSecondaryKey(), userSessionKey);
/*      */         } else {
/* 1233 */           this.sessionKey = userSessionKey;
/*      */         } 
/*      */       } else {
/* 1236 */         this.sessionKey = null;
/*      */       } 
/* 1238 */       if (NTLMEngineImpl.UNICODE_LITTLE_UNMARKED == null) {
/* 1239 */         throw new NTLMEngineException("Unicode not supported");
/*      */       }
/* 1241 */       this.hostBytes = (unqualifiedHost != null) ? unqualifiedHost.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
/* 1242 */       this.domainBytes = (unqualifiedDomain != null) ? unqualifiedDomain.toUpperCase(Locale.ROOT).getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
/*      */       
/* 1244 */       this.userBytes = user.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     String getResponse() {
/* 1250 */       int sessionKeyLen, ntRespLen = this.ntResp.length;
/* 1251 */       int lmRespLen = this.lmResp.length;
/*      */       
/* 1253 */       int domainLen = (this.domainBytes != null) ? this.domainBytes.length : 0;
/* 1254 */       int hostLen = (this.hostBytes != null) ? this.hostBytes.length : 0;
/* 1255 */       int userLen = this.userBytes.length;
/*      */       
/* 1257 */       if (this.sessionKey != null) {
/* 1258 */         sessionKeyLen = this.sessionKey.length;
/*      */       } else {
/* 1260 */         sessionKeyLen = 0;
/*      */       } 
/*      */ 
/*      */       
/* 1264 */       int lmRespOffset = 72;
/* 1265 */       int ntRespOffset = 72 + lmRespLen;
/* 1266 */       int domainOffset = ntRespOffset + ntRespLen;
/* 1267 */       int userOffset = domainOffset + domainLen;
/* 1268 */       int hostOffset = userOffset + userLen;
/* 1269 */       int sessionKeyOffset = hostOffset + hostLen;
/* 1270 */       int finalLength = sessionKeyOffset + sessionKeyLen;
/*      */ 
/*      */       
/* 1273 */       prepareResponse(finalLength, 3);
/*      */ 
/*      */       
/* 1276 */       addUShort(lmRespLen);
/* 1277 */       addUShort(lmRespLen);
/*      */ 
/*      */       
/* 1280 */       addULong(72);
/*      */ 
/*      */       
/* 1283 */       addUShort(ntRespLen);
/* 1284 */       addUShort(ntRespLen);
/*      */ 
/*      */       
/* 1287 */       addULong(ntRespOffset);
/*      */ 
/*      */       
/* 1290 */       addUShort(domainLen);
/* 1291 */       addUShort(domainLen);
/*      */ 
/*      */       
/* 1294 */       addULong(domainOffset);
/*      */ 
/*      */       
/* 1297 */       addUShort(userLen);
/* 1298 */       addUShort(userLen);
/*      */ 
/*      */       
/* 1301 */       addULong(userOffset);
/*      */ 
/*      */       
/* 1304 */       addUShort(hostLen);
/* 1305 */       addUShort(hostLen);
/*      */ 
/*      */       
/* 1308 */       addULong(hostOffset);
/*      */ 
/*      */       
/* 1311 */       addUShort(sessionKeyLen);
/* 1312 */       addUShort(sessionKeyLen);
/*      */ 
/*      */       
/* 1315 */       addULong(sessionKeyOffset);
/*      */ 
/*      */       
/* 1318 */       addULong(this.type2Flags & 0x80 | this.type2Flags & 0x200 | this.type2Flags & 0x80000 | 0x2000000 | this.type2Flags & 0x8000 | this.type2Flags & 0x20 | this.type2Flags & 0x10 | this.type2Flags & 0x20000000 | this.type2Flags & Integer.MIN_VALUE | this.type2Flags & 0x40000000 | this.type2Flags & 0x800000 | this.type2Flags & 0x1 | this.type2Flags & 0x4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1346 */       addUShort(261);
/*      */       
/* 1348 */       addULong(2600);
/*      */       
/* 1350 */       addUShort(3840);
/*      */ 
/*      */       
/* 1353 */       addBytes(this.lmResp);
/* 1354 */       addBytes(this.ntResp);
/* 1355 */       addBytes(this.domainBytes);
/* 1356 */       addBytes(this.userBytes);
/* 1357 */       addBytes(this.hostBytes);
/* 1358 */       if (this.sessionKey != null) {
/* 1359 */         addBytes(this.sessionKey);
/*      */       }
/*      */       
/* 1362 */       return super.getResponse();
/*      */     }
/*      */   }
/*      */   
/*      */   static void writeULong(byte[] buffer, int value, int offset) {
/* 1367 */     buffer[offset] = (byte)(value & 0xFF);
/* 1368 */     buffer[offset + 1] = (byte)(value >> 8 & 0xFF);
/* 1369 */     buffer[offset + 2] = (byte)(value >> 16 & 0xFF);
/* 1370 */     buffer[offset + 3] = (byte)(value >> 24 & 0xFF);
/*      */   }
/*      */   
/*      */   static int F(int x, int y, int z) {
/* 1374 */     return x & y | (x ^ 0xFFFFFFFF) & z;
/*      */   }
/*      */   
/*      */   static int G(int x, int y, int z) {
/* 1378 */     return x & y | x & z | y & z;
/*      */   }
/*      */   
/*      */   static int H(int x, int y, int z) {
/* 1382 */     return x ^ y ^ z;
/*      */   }
/*      */   
/*      */   static int rotintlft(int val, int numbits) {
/* 1386 */     return val << numbits | val >>> 32 - numbits;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class MD4
/*      */   {
/* 1397 */     protected int A = 1732584193;
/* 1398 */     protected int B = -271733879;
/* 1399 */     protected int C = -1732584194;
/* 1400 */     protected int D = 271733878;
/* 1401 */     protected long count = 0L;
/* 1402 */     protected byte[] dataBuffer = new byte[64];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void update(byte[] input) {
/* 1411 */       int curBufferPos = (int)(this.count & 0x3FL);
/* 1412 */       int inputIndex = 0;
/* 1413 */       while (input.length - inputIndex + curBufferPos >= this.dataBuffer.length) {
/*      */ 
/*      */ 
/*      */         
/* 1417 */         int transferAmt = this.dataBuffer.length - curBufferPos;
/* 1418 */         System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
/* 1419 */         this.count += transferAmt;
/* 1420 */         curBufferPos = 0;
/* 1421 */         inputIndex += transferAmt;
/* 1422 */         processBuffer();
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1427 */       if (inputIndex < input.length) {
/* 1428 */         int transferAmt = input.length - inputIndex;
/* 1429 */         System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
/* 1430 */         this.count += transferAmt;
/* 1431 */         curBufferPos += transferAmt;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     byte[] getOutput() {
/* 1438 */       int bufferIndex = (int)(this.count & 0x3FL);
/* 1439 */       int padLen = (bufferIndex < 56) ? (56 - bufferIndex) : (120 - bufferIndex);
/* 1440 */       byte[] postBytes = new byte[padLen + 8];
/*      */ 
/*      */       
/* 1443 */       postBytes[0] = Byte.MIN_VALUE;
/*      */       
/* 1445 */       for (int i = 0; i < 8; i++) {
/* 1446 */         postBytes[padLen + i] = (byte)(int)(this.count * 8L >>> 8 * i);
/*      */       }
/*      */ 
/*      */       
/* 1450 */       update(postBytes);
/*      */ 
/*      */       
/* 1453 */       byte[] result = new byte[16];
/* 1454 */       NTLMEngineImpl.writeULong(result, this.A, 0);
/* 1455 */       NTLMEngineImpl.writeULong(result, this.B, 4);
/* 1456 */       NTLMEngineImpl.writeULong(result, this.C, 8);
/* 1457 */       NTLMEngineImpl.writeULong(result, this.D, 12);
/* 1458 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void processBuffer() {
/* 1463 */       int[] d = new int[16];
/*      */       
/* 1465 */       for (int i = 0; i < 16; i++) {
/* 1466 */         d[i] = (this.dataBuffer[i * 4] & 0xFF) + ((this.dataBuffer[i * 4 + 1] & 0xFF) << 8) + ((this.dataBuffer[i * 4 + 2] & 0xFF) << 16) + ((this.dataBuffer[i * 4 + 3] & 0xFF) << 24);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1472 */       int AA = this.A;
/* 1473 */       int BB = this.B;
/* 1474 */       int CC = this.C;
/* 1475 */       int DD = this.D;
/* 1476 */       round1(d);
/* 1477 */       round2(d);
/* 1478 */       round3(d);
/* 1479 */       this.A += AA;
/* 1480 */       this.B += BB;
/* 1481 */       this.C += CC;
/* 1482 */       this.D += DD;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void round1(int[] d) {
/* 1487 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[0], 3);
/* 1488 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[1], 7);
/* 1489 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[2], 11);
/* 1490 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[3], 19);
/*      */       
/* 1492 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[4], 3);
/* 1493 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[5], 7);
/* 1494 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[6], 11);
/* 1495 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[7], 19);
/*      */       
/* 1497 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[8], 3);
/* 1498 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[9], 7);
/* 1499 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[10], 11);
/* 1500 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[11], 19);
/*      */       
/* 1502 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[12], 3);
/* 1503 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[13], 7);
/* 1504 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[14], 11);
/* 1505 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[15], 19);
/*      */     }
/*      */     
/*      */     protected void round2(int[] d) {
/* 1509 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[0] + 1518500249, 3);
/* 1510 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[4] + 1518500249, 5);
/* 1511 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[8] + 1518500249, 9);
/* 1512 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[12] + 1518500249, 13);
/*      */       
/* 1514 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[1] + 1518500249, 3);
/* 1515 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[5] + 1518500249, 5);
/* 1516 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[9] + 1518500249, 9);
/* 1517 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[13] + 1518500249, 13);
/*      */       
/* 1519 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[2] + 1518500249, 3);
/* 1520 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[6] + 1518500249, 5);
/* 1521 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[10] + 1518500249, 9);
/* 1522 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[14] + 1518500249, 13);
/*      */       
/* 1524 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[3] + 1518500249, 3);
/* 1525 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[7] + 1518500249, 5);
/* 1526 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[11] + 1518500249, 9);
/* 1527 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[15] + 1518500249, 13);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void round3(int[] d) {
/* 1532 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[0] + 1859775393, 3);
/* 1533 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[8] + 1859775393, 9);
/* 1534 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[4] + 1859775393, 11);
/* 1535 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[12] + 1859775393, 15);
/*      */       
/* 1537 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[2] + 1859775393, 3);
/* 1538 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[10] + 1859775393, 9);
/* 1539 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[6] + 1859775393, 11);
/* 1540 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[14] + 1859775393, 15);
/*      */       
/* 1542 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[1] + 1859775393, 3);
/* 1543 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[9] + 1859775393, 9);
/* 1544 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[5] + 1859775393, 11);
/* 1545 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[13] + 1859775393, 15);
/*      */       
/* 1547 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[3] + 1859775393, 3);
/* 1548 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[11] + 1859775393, 9);
/* 1549 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[7] + 1859775393, 11);
/* 1550 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[15] + 1859775393, 15);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class HMACMD5
/*      */   {
/*      */     protected byte[] ipad;
/*      */     
/*      */     protected byte[] opad;
/*      */     
/*      */     protected MessageDigest md5;
/*      */ 
/*      */     
/*      */     HMACMD5(byte[] input) throws NTLMEngineException {
/* 1566 */       byte[] key = input;
/*      */       try {
/* 1568 */         this.md5 = MessageDigest.getInstance("MD5");
/* 1569 */       } catch (Exception ex) {
/*      */ 
/*      */         
/* 1572 */         throw new NTLMEngineException("Error getting md5 message digest implementation: " + ex.getMessage(), ex);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1577 */       this.ipad = new byte[64];
/* 1578 */       this.opad = new byte[64];
/*      */       
/* 1580 */       int keyLength = key.length;
/* 1581 */       if (keyLength > 64) {
/*      */         
/* 1583 */         this.md5.update(key);
/* 1584 */         key = this.md5.digest();
/* 1585 */         keyLength = key.length;
/*      */       } 
/* 1587 */       int i = 0;
/* 1588 */       while (i < keyLength) {
/* 1589 */         this.ipad[i] = (byte)(key[i] ^ 0x36);
/* 1590 */         this.opad[i] = (byte)(key[i] ^ 0x5C);
/* 1591 */         i++;
/*      */       } 
/* 1593 */       while (i < 64) {
/* 1594 */         this.ipad[i] = 54;
/* 1595 */         this.opad[i] = 92;
/* 1596 */         i++;
/*      */       } 
/*      */ 
/*      */       
/* 1600 */       this.md5.reset();
/* 1601 */       this.md5.update(this.ipad);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     byte[] getOutput() {
/* 1607 */       byte[] digest = this.md5.digest();
/* 1608 */       this.md5.update(this.opad);
/* 1609 */       return this.md5.digest(digest);
/*      */     }
/*      */ 
/*      */     
/*      */     void update(byte[] input) {
/* 1614 */       this.md5.update(input);
/*      */     }
/*      */ 
/*      */     
/*      */     void update(byte[] input, int offset, int length) {
/* 1619 */       this.md5.update(input, offset, length);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String generateType1Msg(String domain, String workstation) throws NTLMEngineException {
/* 1628 */     return getType1Message(workstation, domain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String generateType3Msg(String username, String password, String domain, String workstation, String challenge) throws NTLMEngineException {
/* 1638 */     Type2Message t2m = new Type2Message(challenge);
/* 1639 */     return getType3Message(username, password, workstation, domain, t2m.getChallenge(), t2m.getFlags(), t2m.getTarget(), t2m.getTargetInfo());
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\impl\auth\NTLMEngineImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */