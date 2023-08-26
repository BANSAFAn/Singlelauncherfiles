/*    */ package org.tlauncher.tlauncher.service;
/*    */ 
/*    */ import com.google.inject.Inject;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.time.Instant;
/*    */ import java.time.ZoneId;
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import javax.xml.parsers.DocumentBuilder;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import javax.xml.parsers.ParserConfigurationException;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.tlauncher.util.U;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.NamedNodeMap;
/*    */ import org.w3c.dom.Node;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlLogDeserialization
/*    */ {
/* 23 */   private static final Logger log = Logger.getLogger(XmlLogDeserialization.class);
/*    */   
/*    */   private DocumentBuilder builder;
/*    */   
/* 27 */   private StringBuilder log4jMessage = new StringBuilder();
/* 28 */   private final String logFormat = "[%s] [%s/%s]: %s";
/* 29 */   private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:SS");
/* 30 */   private final String LOG_PATTERN_REPLACE = "\033\\[[;\\d]*[ -/]*[@-~]";
/*    */   private boolean xmlLog;
/*    */   
/*    */   @Inject
/*    */   public void init() throws ParserConfigurationException {
/* 35 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/* 36 */     this.builder = factory.newDocumentBuilder();
/*    */   }
/*    */   
/*    */   public void addToLog(String message) {
/* 40 */     String trimmedMessage = message.trim();
/* 41 */     if (trimmedMessage.startsWith("<log4j:Event")) {
/* 42 */       this.xmlLog = true;
/*    */     }
/* 44 */     if (this.xmlLog) {
/* 45 */       this.log4jMessage.append(message).append(System.lineSeparator());
/* 46 */       if (trimmedMessage.startsWith("<") && trimmedMessage.startsWith("</log4j:Event")) {
/*    */         try {
/* 48 */           Document doc = this.builder.parse(new ByteArrayInputStream(this.log4jMessage.toString().getBytes()));
/* 49 */           Node node1 = doc.getFirstChild();
/* 50 */           NamedNodeMap attributes = node1.getAttributes();
/* 51 */           message = String.format("[%s] [%s/%s]: %s", new Object[] {
/* 52 */                 Instant.ofEpochMilli(Long.valueOf(node1.getAttributes().item(3).getFirstChild().getTextContent()).longValue())
/* 53 */                 .atZone(ZoneId.systemDefault()).toLocalTime().format(this.formatter), attributes
/* 54 */                 .item(2).getFirstChild().getTextContent(), attributes
/* 55 */                 .item(0).getFirstChild().getTextContent(), node1
/* 56 */                 .getChildNodes().item(1).getFirstChild().getTextContent() });
/* 57 */           if (node1.getChildNodes().getLength() == 5) {
/* 58 */             message = message + System.lineSeparator() + node1.getChildNodes().item(3).getTextContent();
/*    */           }
/*    */         }
/* 61 */         catch (SAXException|java.io.IOException e) {
/* 62 */           log.warn("error with parsing log ", e);
/*    */         } 
/* 64 */         U.plog(new Object[] { message.replaceAll("\033\\[[;\\d]*[ -/]*[@-~]", "") });
/* 65 */         this.log4jMessage.setLength(0);
/* 66 */         this.xmlLog = false;
/*    */       } 
/*    */     } else {
/* 69 */       U.plog(new Object[] { message.replaceAll("\033\\[[;\\d]*[ -/]*[@-~]", "") });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\service\XmlLogDeserialization.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */