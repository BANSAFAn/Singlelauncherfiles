/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.Font;
/*    */ import java.awt.Insets;
/*    */ import java.net.URL;
/*    */ import javax.swing.JEditorPane;
/*    */ import javax.swing.event.HyperlinkEvent;
/*    */ import javax.swing.event.HyperlinkListener;
/*    */ import javax.swing.text.EditorKit;
/*    */ import javax.swing.text.html.StyleSheet;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*    */ import org.tlauncher.tlauncher.ui.swing.editor.ExtendedHTMLEditorKit;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ public class FlexibleEditorPanel
/*    */   extends JEditorPane
/*    */   implements LocalizableComponent
/*    */ {
/*    */   public FlexibleEditorPanel(Font font, int width) {
/* 22 */     if (font != null) {
/* 23 */       setFont(font);
/*    */     } else {
/* 25 */       font = getFont();
/*    */     } 
/* 27 */     StyleSheet css = new StyleSheet();
/* 28 */     css.importStyleSheet(getClass().getResource("styles.css"));
/* 29 */     css.addRule("body { font-family: " + font.getFamily() + ";width:" + width + 
/* 30 */         "; font-size: " + font.getSize() + "pt; } " + "a { text-decoration: underline; }");
/*    */ 
/*    */     
/* 33 */     ExtendedHTMLEditorKit html = new ExtendedHTMLEditorKit();
/* 34 */     html.setStyleSheet(css);
/*    */     
/* 36 */     getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
/* 37 */     setMargin(new Insets(0, 0, 0, 0));
/* 38 */     setEditorKit((EditorKit)html);
/* 39 */     setEditable(false);
/* 40 */     setOpaque(false);
/*    */     
/* 42 */     addHyperlinkListener(new HyperlinkListener()
/*    */         {
/*    */           public void hyperlinkUpdate(HyperlinkEvent e) {
/* 45 */             if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
/*    */               return;
/*    */             }
/* 48 */             URL url = e.getURL();
/*    */             
/* 50 */             if (url == null) {
/*    */               return;
/*    */             }
/* 53 */             OS.openLink(url);
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public FlexibleEditorPanel(int width) {
/* 59 */     this((new LocalizableLabel()).getFont(), width);
/*    */   }
/*    */   
/*    */   public FlexibleEditorPanel(String type, String text, int width) {
/* 63 */     this(width);
/* 64 */     setContentType(type);
/* 65 */     setText(Localizable.get(text));
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 70 */     setText(Localizable.get("auth.tip.tlauncher"));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\FlexibleEditorPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */