/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ public class XmlStreamReaderException
/*     */   extends IOException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final String bomEncoding;
/*     */   private final String xmlGuessEncoding;
/*     */   private final String xmlEncoding;
/*     */   private final String contentTypeMime;
/*     */   private final String contentTypeEncoding;
/*     */   
/*     */   public XmlStreamReaderException(String msg, String bomEnc, String xmlGuessEnc, String xmlEnc) {
/*  62 */     this(msg, null, null, bomEnc, xmlGuessEnc, xmlEnc);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamReaderException(String msg, String ctMime, String ctEnc, String bomEnc, String xmlGuessEnc, String xmlEnc) {
/*  81 */     super(msg);
/*  82 */     this.contentTypeMime = ctMime;
/*  83 */     this.contentTypeEncoding = ctEnc;
/*  84 */     this.bomEncoding = bomEnc;
/*  85 */     this.xmlGuessEncoding = xmlGuessEnc;
/*  86 */     this.xmlEncoding = xmlEnc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBomEncoding() {
/*  95 */     return this.bomEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getXmlGuessEncoding() {
/* 104 */     return this.xmlGuessEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getXmlEncoding() {
/* 113 */     return this.xmlEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentTypeMime() {
/* 124 */     return this.contentTypeMime;
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
/*     */   public String getContentTypeEncoding() {
/* 136 */     return this.contentTypeEncoding;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\XmlStreamReaderException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */