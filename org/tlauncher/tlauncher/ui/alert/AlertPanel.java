/*    */ package org.tlauncher.tlauncher.ui.alert;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.BoxLayout;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JTextArea;
/*    */ import org.tlauncher.tlauncher.ui.swing.ScrollPane;
/*    */ import org.tlauncher.tlauncher.ui.swing.TextPopup;
/*    */ import org.tlauncher.tlauncher.ui.swing.editor.EditorPane;
/*    */ import org.tlauncher.util.StringUtil;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ class AlertPanel extends JPanel {
/*    */   private static final int MAX_CHARS = 80;
/* 15 */   private static final Dimension MAX_SIZE = new Dimension(500, 300); private static final int MAX_WIDTH = 500; private static final int MAX_HEIGHT = 300;
/*    */   AlertPanel(String rawMessage, Object rawTextarea) {
/*    */     String message;
/* 18 */     setLayout(new BoxLayout(this, 1));
/*    */ 
/*    */ 
/*    */     
/* 22 */     if (rawMessage == null) {
/* 23 */       message = null;
/*    */     } else {
/* 25 */       message = StringUtil.wrap("<html>" + rawMessage + "</html>", 80);
/*    */     } 
/*    */ 
/*    */     
/* 29 */     EditorPane label = new EditorPane("text/html", message);
/* 30 */     label.setAlignmentX(0.0F);
/* 31 */     label.setFocusable(false);
/* 32 */     add((Component)label);
/*    */     
/* 34 */     if (rawTextarea == null) {
/*    */       return;
/*    */     }
/* 37 */     String textarea = U.toLog(new Object[] { rawTextarea });
/*    */     
/* 39 */     JTextArea area = new JTextArea(textarea);
/* 40 */     area.addMouseListener((MouseListener)new TextPopup());
/* 41 */     area.setFont(getFont());
/* 42 */     area.setEditable(false);
/*    */     
/* 44 */     ScrollPane scroll = new ScrollPane(area, true);
/* 45 */     scroll.setAlignmentX(0.0F);
/* 46 */     scroll.setVBPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
/*    */     
/* 48 */     int textAreaHeight = StringUtil.countLines(textarea) * getFontMetrics(getFont()).getHeight();
/*    */     
/* 50 */     if (textAreaHeight > 300) {
/* 51 */       scroll.setPreferredSize(MAX_SIZE);
/*    */     }
/* 53 */     add((Component)scroll);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\alert\AlertPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */