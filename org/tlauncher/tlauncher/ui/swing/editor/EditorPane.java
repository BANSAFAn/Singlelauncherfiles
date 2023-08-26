/*    */ package org.tlauncher.tlauncher.ui.swing.editor;
/*    */ 
/*    */ import java.awt.Font;
/*    */ import java.awt.Insets;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import javax.swing.JEditorPane;
/*    */ import javax.swing.event.HyperlinkEvent;
/*    */ import javax.swing.event.HyperlinkListener;
/*    */ import javax.swing.text.html.StyleSheet;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ 
/*    */ public class EditorPane
/*    */   extends JEditorPane
/*    */ {
/*    */   private static final long serialVersionUID = -2857352867725574106L;
/*    */   
/*    */   public EditorPane(Font font) {
/* 21 */     if (font != null) {
/* 22 */       setFont(font);
/*    */     } else {
/* 24 */       font = getFont();
/*    */     } 
/* 26 */     StyleSheet css = new StyleSheet();
/* 27 */     css.importStyleSheet(getClass().getResource("styles.css"));
/* 28 */     css.addRule("body { font-family: " + 
/*    */         
/* 30 */         font.getFamily() + "; font-size: " + 
/* 31 */         font.getSize() + "pt; } " + "a { text-decoration: underline; }");
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 36 */     ExtendedHTMLEditorKit html = new ExtendedHTMLEditorKit();
/* 37 */     html.setStyleSheet(css);
/*    */     
/* 39 */     getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
/* 40 */     setMargin(new Insets(0, 0, 0, 0));
/* 41 */     setEditorKit(html);
/* 42 */     setEditable(false);
/* 43 */     setOpaque(false);
/*    */     
/* 45 */     addHyperlinkListener(new HyperlinkListener()
/*    */         {
/*    */           public void hyperlinkUpdate(HyperlinkEvent e) {
/* 48 */             if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
/*    */               return;
/*    */             }
/* 51 */             URL url = e.getURL();
/*    */             
/* 53 */             if (url == null) {
/*    */               return;
/*    */             }
/* 56 */             OS.openLink(url);
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public EditorPane() {
/* 62 */     this((new LocalizableLabel()).getFont());
/*    */   }
/*    */   
/*    */   public EditorPane(URL initialPage) throws IOException {
/* 66 */     this();
/* 67 */     setPage(initialPage);
/*    */   }
/*    */   
/*    */   public EditorPane(String url) throws IOException {
/* 71 */     this();
/* 72 */     setPage(url);
/*    */   }
/*    */   
/*    */   public EditorPane(String type, String text) {
/* 76 */     this();
/* 77 */     setContentType(type);
/* 78 */     setText(text);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\editor\EditorPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */