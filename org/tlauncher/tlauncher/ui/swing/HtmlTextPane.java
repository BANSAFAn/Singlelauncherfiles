/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.Insets;
/*    */ import java.net.URL;
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.JEditorPane;
/*    */ import javax.swing.JScrollPane;
/*    */ import javax.swing.event.HyperlinkEvent;
/*    */ import javax.swing.text.html.HTMLEditorKit;
/*    */ import javax.swing.text.html.StyleSheet;
/*    */ import org.launcher.resource.TlauncherResource;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ 
/*    */ public class HtmlTextPane
/*    */   extends JEditorPane
/*    */ {
/* 18 */   private static final HtmlTextPane HTML_TEXT_PANE = new HtmlTextPane("text/html", "");
/* 19 */   private static final HtmlTextPane HTML_TEXT_PANE_WIDTH = new HtmlTextPane("text/html", "");
/*    */   
/*    */   public HtmlTextPane(String type, String text) {
/* 22 */     super(type, text);
/* 23 */     getDocument().putProperty("IgnoreCharsetDirective", Boolean.TRUE);
/* 24 */     setMargin(new Insets(0, 0, 0, 0));
/* 25 */     setEditable(false);
/* 26 */     setOpaque(false);
/* 27 */     HTMLEditorKit kit = new HTMLEditorKit();
/* 28 */     setEditorKit(kit);
/* 29 */     addHyperlinkListener(e -> {
/*    */           if (!e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
/*    */             return;
/*    */           }
/*    */           URL url = e.getURL();
/*    */           if (url == null)
/*    */             return; 
/*    */           OS.openLink(url);
/*    */         });
/*    */   }
/*    */   
/*    */   public static HtmlTextPane get(String text) {
/* 41 */     HTML_TEXT_PANE.setText(text);
/* 42 */     return HTML_TEXT_PANE;
/*    */   }
/*    */   
/*    */   public static HtmlTextPane get(String text, int width) {
/* 46 */     HTMLEditorKit kit = (HTMLEditorKit)HTML_TEXT_PANE_WIDTH.getEditorKit();
/* 47 */     kit.getStyleSheet().addRule(String.format("body {width:%spx;}", new Object[] { Integer.valueOf(width) }));
/* 48 */     kit.getStyleSheet().addRule("a { text-decoration: underline; color: #147de0;}");
/* 49 */     HTML_TEXT_PANE_WIDTH.setText(text);
/* 50 */     return HTML_TEXT_PANE_WIDTH;
/*    */   }
/*    */   
/*    */   public static HtmlTextPane getWithWidth(String text, int width) {
/* 54 */     HtmlTextPane h = new HtmlTextPane("text/html", "");
/* 55 */     HTMLEditorKit kit = (HTMLEditorKit)h.getEditorKit();
/* 56 */     kit.getStyleSheet().addRule(String.format("body {width:%spx;}", new Object[] { Integer.valueOf(width) }));
/* 57 */     kit.getStyleSheet().addRule("a { text-decoration: underline; color: #147de0;}");
/* 58 */     h.setText(text);
/* 59 */     return h;
/*    */   }
/*    */ 
/*    */   
/*    */   public static HtmlTextPane createNew(String text, int width) {
/* 64 */     HtmlTextPane pane = new HtmlTextPane("text/html", "");
/* 65 */     pane.setText(text);
/* 66 */     HTMLEditorKit kit = (HTMLEditorKit)pane.getEditorKit();
/* 67 */     StyleSheet ss = new StyleSheet();
/* 68 */     ss.importStyleSheet(TlauncherResource.getResource("updater.css"));
/* 69 */     kit.getStyleSheet().addStyleSheet(ss);
/* 70 */     return pane;
/*    */   }
/*    */   
/*    */   public static JScrollPane createNewAndWrap(String text, int width) {
/* 74 */     return wrap(createNew(text, width));
/*    */   }
/*    */   
/*    */   private static JScrollPane wrap(HtmlTextPane pane) {
/* 78 */     JScrollPane jScrollPane = new JScrollPane(pane, 21, 31);
/*    */     
/* 80 */     jScrollPane.getViewport().setOpaque(false);
/* 81 */     jScrollPane.setOpaque(false);
/* 82 */     jScrollPane.setBorder(BorderFactory.createEmptyBorder());
/* 83 */     return jScrollPane;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\HtmlTextPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */