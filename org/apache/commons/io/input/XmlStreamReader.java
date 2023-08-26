/*     */ package org.apache.commons.io.input;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.commons.io.ByteOrderMark;
/*     */ import org.apache.commons.io.IOUtils;
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
/*     */ public class XmlStreamReader
/*     */   extends Reader
/*     */ {
/*     */   private static final String UTF_8 = "UTF-8";
/*     */   private static final String US_ASCII = "US-ASCII";
/*     */   private static final String UTF_16BE = "UTF-16BE";
/*     */   private static final String UTF_16LE = "UTF-16LE";
/*     */   private static final String UTF_32BE = "UTF-32BE";
/*     */   private static final String UTF_32LE = "UTF-32LE";
/*     */   private static final String UTF_16 = "UTF-16";
/*     */   private static final String UTF_32 = "UTF-32";
/*     */   private static final String EBCDIC = "CP1047";
/*  92 */   private static final ByteOrderMark[] BOMS = new ByteOrderMark[] { ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   private static final ByteOrderMark[] XML_GUESS_BYTES = new ByteOrderMark[] { new ByteOrderMark("UTF-8", new int[] { 60, 63, 120, 109 }), new ByteOrderMark("UTF-16BE", new int[] { 0, 60, 0, 63 }), new ByteOrderMark("UTF-16LE", new int[] { 60, 0, 63, 0 }), new ByteOrderMark("UTF-32BE", new int[] { 0, 0, 0, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109 }), new ByteOrderMark("UTF-32LE", new int[] { 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109, 0, 0, 0 }), new ByteOrderMark("CP1047", new int[] { 76, 111, 167, 148 }) };
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
/* 113 */   private static final Pattern CHARSET_PATTERN = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   public static final Pattern ENCODING_PATTERN = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:'.[^']*'))", 8);
/*     */ 
/*     */   
/*     */   private static final String RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch";
/*     */ 
/*     */   
/*     */   private static final String RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM";
/*     */ 
/*     */   
/*     */   private static final String HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL";
/*     */ 
/*     */   
/*     */   private static final String HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch";
/*     */ 
/*     */   
/*     */   private static final String HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME";
/*     */ 
/*     */   
/*     */   private final Reader reader;
/*     */ 
/*     */   
/*     */   private final String encoding;
/*     */   
/*     */   private final String defaultEncoding;
/*     */ 
/*     */   
/*     */   static String getContentTypeEncoding(String httpContentType) {
/* 145 */     String encoding = null;
/* 146 */     if (httpContentType != null) {
/* 147 */       int i = httpContentType.indexOf(";");
/* 148 */       if (i > -1) {
/* 149 */         String postMime = httpContentType.substring(i + 1);
/* 150 */         Matcher m = CHARSET_PATTERN.matcher(postMime);
/* 151 */         encoding = m.find() ? m.group(1) : null;
/* 152 */         encoding = (encoding != null) ? encoding.toUpperCase(Locale.ROOT) : null;
/*     */       } 
/*     */     } 
/* 155 */     return encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getContentTypeMime(String httpContentType) {
/* 165 */     String mime = null;
/* 166 */     if (httpContentType != null) {
/* 167 */       int i = httpContentType.indexOf(";");
/* 168 */       if (i >= 0) {
/* 169 */         mime = httpContentType.substring(0, i);
/*     */       } else {
/* 171 */         mime = httpContentType;
/*     */       } 
/* 173 */       mime = mime.trim();
/*     */     } 
/* 175 */     return mime;
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
/*     */   private static String getXmlProlog(InputStream inputStream, String guessedEnc) throws IOException {
/* 188 */     String encoding = null;
/* 189 */     if (guessedEnc != null) {
/* 190 */       byte[] bytes = IOUtils.byteArray();
/* 191 */       inputStream.mark(8192);
/* 192 */       int offset = 0;
/* 193 */       int max = 8192;
/* 194 */       int c = inputStream.read(bytes, offset, max);
/* 195 */       int firstGT = -1;
/* 196 */       String xmlProlog = "";
/* 197 */       while (c != -1 && firstGT == -1 && offset < 8192) {
/* 198 */         offset += c;
/* 199 */         max -= c;
/* 200 */         c = inputStream.read(bytes, offset, max);
/* 201 */         xmlProlog = new String(bytes, 0, offset, guessedEnc);
/* 202 */         firstGT = xmlProlog.indexOf('>');
/*     */       } 
/* 204 */       if (firstGT == -1) {
/* 205 */         if (c == -1) {
/* 206 */           throw new IOException("Unexpected end of XML stream");
/*     */         }
/* 208 */         throw new IOException("XML prolog or ROOT element not found on first " + offset + " bytes");
/*     */       } 
/*     */ 
/*     */       
/* 212 */       int bytesRead = offset;
/* 213 */       if (bytesRead > 0) {
/* 214 */         inputStream.reset();
/*     */         
/* 216 */         BufferedReader bReader = new BufferedReader(new StringReader(xmlProlog.substring(0, firstGT + 1)));
/* 217 */         StringBuffer prolog = new StringBuffer();
/*     */         String line;
/* 219 */         while ((line = bReader.readLine()) != null) {
/* 220 */           prolog.append(line);
/*     */         }
/* 222 */         Matcher m = ENCODING_PATTERN.matcher(prolog);
/* 223 */         if (m.find()) {
/* 224 */           encoding = m.group(1).toUpperCase(Locale.ROOT);
/* 225 */           encoding = encoding.substring(1, encoding.length() - 1);
/*     */         } 
/*     */       } 
/*     */     } 
/* 229 */     return encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isAppXml(String mime) {
/* 240 */     return (mime != null && (mime
/* 241 */       .equals("application/xml") || mime
/* 242 */       .equals("application/xml-dtd") || mime
/* 243 */       .equals("application/xml-external-parsed-entity") || (mime
/* 244 */       .startsWith("application/") && mime.endsWith("+xml"))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean isTextXml(String mime) {
/* 255 */     return (mime != null && (mime
/* 256 */       .equals("text/xml") || mime
/* 257 */       .equals("text/xml-external-parsed-entity") || (mime
/* 258 */       .startsWith("text/") && mime.endsWith("+xml"))));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamReader(File file) throws IOException {
/* 280 */     this(((File)Objects.<File>requireNonNull(file, "file")).toPath());
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
/*     */   public XmlStreamReader(InputStream inputStream) throws IOException {
/* 295 */     this(inputStream, true);
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
/*     */   public XmlStreamReader(InputStream inputStream, boolean lenient) throws IOException {
/* 326 */     this(inputStream, lenient, (String)null);
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
/*     */   public XmlStreamReader(InputStream inputStream, boolean lenient, String defaultEncoding) throws IOException {
/* 360 */     Objects.requireNonNull(inputStream, "inputStream");
/* 361 */     this.defaultEncoding = defaultEncoding;
/* 362 */     BOMInputStream bom = new BOMInputStream(new BufferedInputStream(inputStream, 8192), false, BOMS);
/* 363 */     BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
/* 364 */     this.encoding = doRawStream(bom, pis, lenient);
/* 365 */     this.reader = new InputStreamReader(pis, this.encoding);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamReader(InputStream inputStream, String httpContentType) throws IOException {
/* 387 */     this(inputStream, httpContentType, true);
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
/*     */   public XmlStreamReader(InputStream inputStream, String httpContentType, boolean lenient) throws IOException {
/* 425 */     this(inputStream, httpContentType, lenient, null);
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
/*     */   public XmlStreamReader(InputStream inputStream, String httpContentType, boolean lenient, String defaultEncoding) throws IOException {
/* 466 */     Objects.requireNonNull(inputStream, "inputStream");
/* 467 */     this.defaultEncoding = defaultEncoding;
/* 468 */     BOMInputStream bom = new BOMInputStream(new BufferedInputStream(inputStream, 8192), false, BOMS);
/* 469 */     BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
/* 470 */     this.encoding = processHttpStream(bom, pis, httpContentType, lenient);
/* 471 */     this.reader = new InputStreamReader(pis, this.encoding);
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
/*     */   public XmlStreamReader(Path file) throws IOException {
/* 489 */     this(Files.newInputStream(Objects.<Path>requireNonNull(file, "file"), new java.nio.file.OpenOption[0]));
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
/*     */ 
/*     */   
/*     */   public XmlStreamReader(URL url) throws IOException {
/* 510 */     this(((URL)Objects.<URL>requireNonNull(url, "url")).openConnection(), (String)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XmlStreamReader(URLConnection conn, String defaultEncoding) throws IOException {
/* 533 */     Objects.requireNonNull(conn, "conn");
/* 534 */     this.defaultEncoding = defaultEncoding;
/* 535 */     boolean lenient = true;
/* 536 */     String contentType = conn.getContentType();
/* 537 */     InputStream inputStream = conn.getInputStream();
/*     */     
/* 539 */     BOMInputStream bom = new BOMInputStream(new BufferedInputStream(inputStream, 8192), false, BOMS);
/* 540 */     BOMInputStream pis = new BOMInputStream(bom, true, XML_GUESS_BYTES);
/* 541 */     if (conn instanceof java.net.HttpURLConnection || contentType != null) {
/* 542 */       this.encoding = processHttpStream(bom, pis, contentType, true);
/*     */     } else {
/* 544 */       this.encoding = doRawStream(bom, pis, true);
/*     */     } 
/* 546 */     this.reader = new InputStreamReader(pis, this.encoding);
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
/*     */   
/*     */   String calculateHttpEncoding(String httpContentType, String bomEnc, String xmlGuessEnc, String xmlEnc, boolean lenient) throws IOException {
/* 566 */     if (lenient && xmlEnc != null) {
/* 567 */       return xmlEnc;
/*     */     }
/*     */ 
/*     */     
/* 571 */     String cTMime = getContentTypeMime(httpContentType);
/* 572 */     String cTEnc = getContentTypeEncoding(httpContentType);
/* 573 */     boolean appXml = isAppXml(cTMime);
/* 574 */     boolean textXml = isTextXml(cTMime);
/*     */ 
/*     */     
/* 577 */     if (!appXml && !textXml) {
/* 578 */       String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 579 */       throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */     } 
/*     */ 
/*     */     
/* 583 */     if (cTEnc == null) {
/* 584 */       if (appXml) {
/* 585 */         return calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc);
/*     */       }
/* 587 */       return (this.defaultEncoding == null) ? "US-ASCII" : this.defaultEncoding;
/*     */     } 
/*     */ 
/*     */     
/* 591 */     if (cTEnc.equals("UTF-16BE") || cTEnc.equals("UTF-16LE")) {
/* 592 */       if (bomEnc != null) {
/* 593 */         String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 594 */         throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 596 */       return cTEnc;
/*     */     } 
/*     */ 
/*     */     
/* 600 */     if (cTEnc.equals("UTF-16")) {
/* 601 */       if (bomEnc != null && bomEnc.startsWith("UTF-16")) {
/* 602 */         return bomEnc;
/*     */       }
/* 604 */       String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 605 */       throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */     } 
/*     */ 
/*     */     
/* 609 */     if (cTEnc.equals("UTF-32BE") || cTEnc.equals("UTF-32LE")) {
/* 610 */       if (bomEnc != null) {
/* 611 */         String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 612 */         throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 614 */       return cTEnc;
/*     */     } 
/*     */ 
/*     */     
/* 618 */     if (cTEnc.equals("UTF-32")) {
/* 619 */       if (bomEnc != null && bomEnc.startsWith("UTF-32")) {
/* 620 */         return bomEnc;
/*     */       }
/* 622 */       String msg = MessageFormat.format("Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch", new Object[] { cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc });
/* 623 */       throw new XmlStreamReaderException(msg, cTMime, cTEnc, bomEnc, xmlGuessEnc, xmlEnc);
/*     */     } 
/*     */     
/* 626 */     return cTEnc;
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
/*     */   String calculateRawEncoding(String bomEnc, String xmlGuessEnc, String xmlEnc) throws IOException {
/* 642 */     if (bomEnc == null) {
/* 643 */       if (xmlGuessEnc == null || xmlEnc == null) {
/* 644 */         return (this.defaultEncoding == null) ? "UTF-8" : this.defaultEncoding;
/*     */       }
/* 646 */       if (xmlEnc.equals("UTF-16") && (xmlGuessEnc
/* 647 */         .equals("UTF-16BE") || xmlGuessEnc.equals("UTF-16LE"))) {
/* 648 */         return xmlGuessEnc;
/*     */       }
/* 650 */       return xmlEnc;
/*     */     } 
/*     */ 
/*     */     
/* 654 */     if (bomEnc.equals("UTF-8")) {
/* 655 */       if (xmlGuessEnc != null && !xmlGuessEnc.equals("UTF-8")) {
/* 656 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 657 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 659 */       if (xmlEnc != null && !xmlEnc.equals("UTF-8")) {
/* 660 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 661 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 663 */       return bomEnc;
/*     */     } 
/*     */ 
/*     */     
/* 667 */     if (bomEnc.equals("UTF-16BE") || bomEnc.equals("UTF-16LE")) {
/* 668 */       if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
/* 669 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 670 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 672 */       if (xmlEnc != null && !xmlEnc.equals("UTF-16") && !xmlEnc.equals(bomEnc)) {
/* 673 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 674 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 676 */       return bomEnc;
/*     */     } 
/*     */ 
/*     */     
/* 680 */     if (bomEnc.equals("UTF-32BE") || bomEnc.equals("UTF-32LE")) {
/* 681 */       if (xmlGuessEnc != null && !xmlGuessEnc.equals(bomEnc)) {
/* 682 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 683 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 685 */       if (xmlEnc != null && !xmlEnc.equals("UTF-32") && !xmlEnc.equals(bomEnc)) {
/* 686 */         String str = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 687 */         throw new XmlStreamReaderException(str, bomEnc, xmlGuessEnc, xmlEnc);
/*     */       } 
/* 689 */       return bomEnc;
/*     */     } 
/*     */ 
/*     */     
/* 693 */     String msg = MessageFormat.format("Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM", new Object[] { bomEnc, xmlGuessEnc, xmlEnc });
/* 694 */     throw new XmlStreamReaderException(msg, bomEnc, xmlGuessEnc, xmlEnc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 704 */     this.reader.close();
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
/*     */   private String doLenientDetection(String httpContentType, XmlStreamReaderException ex) throws IOException {
/* 718 */     if (httpContentType != null && httpContentType.startsWith("text/html")) {
/* 719 */       httpContentType = httpContentType.substring("text/html".length());
/* 720 */       httpContentType = "text/xml" + httpContentType;
/*     */       try {
/* 722 */         return calculateHttpEncoding(httpContentType, ex.getBomEncoding(), ex
/* 723 */             .getXmlGuessEncoding(), ex.getXmlEncoding(), true);
/* 724 */       } catch (XmlStreamReaderException ex2) {
/* 725 */         ex = ex2;
/*     */       } 
/*     */     } 
/* 728 */     String encoding = ex.getXmlEncoding();
/* 729 */     if (encoding == null) {
/* 730 */       encoding = ex.getContentTypeEncoding();
/*     */     }
/* 732 */     if (encoding == null) {
/* 733 */       encoding = (this.defaultEncoding == null) ? "UTF-8" : this.defaultEncoding;
/*     */     }
/* 735 */     return encoding;
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
/*     */   private String doRawStream(BOMInputStream bom, BOMInputStream pis, boolean lenient) throws IOException {
/* 750 */     String bomEnc = bom.getBOMCharsetName();
/* 751 */     String xmlGuessEnc = pis.getBOMCharsetName();
/* 752 */     String xmlEnc = getXmlProlog(pis, xmlGuessEnc);
/*     */     try {
/* 754 */       return calculateRawEncoding(bomEnc, xmlGuessEnc, xmlEnc);
/* 755 */     } catch (XmlStreamReaderException ex) {
/* 756 */       if (lenient) {
/* 757 */         return doLenientDetection(null, ex);
/*     */       }
/* 759 */       throw ex;
/*     */     } 
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
/*     */   public String getDefaultEncoding() {
/* 772 */     return this.defaultEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 781 */     return this.encoding;
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
/*     */   private String processHttpStream(BOMInputStream bom, BOMInputStream pis, String httpContentType, boolean lenient) throws IOException {
/* 797 */     String bomEnc = bom.getBOMCharsetName();
/* 798 */     String xmlGuessEnc = pis.getBOMCharsetName();
/* 799 */     String xmlEnc = getXmlProlog(pis, xmlGuessEnc);
/*     */     try {
/* 801 */       return calculateHttpEncoding(httpContentType, bomEnc, xmlGuessEnc, xmlEnc, lenient);
/* 802 */     } catch (XmlStreamReaderException ex) {
/* 803 */       if (lenient) {
/* 804 */         return doLenientDetection(httpContentType, ex);
/*     */       }
/* 806 */       throw ex;
/*     */     } 
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
/*     */   public int read(char[] buf, int offset, int len) throws IOException {
/* 820 */     return this.reader.read(buf, offset, len);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\input\XmlStreamReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */