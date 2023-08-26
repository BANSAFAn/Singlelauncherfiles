/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ import javax.swing.JTextArea;
/*    */ import javax.swing.text.Highlighter;
/*    */ 
/*    */ public class TextWrapperLabel extends JTextArea {
/*    */   public TextWrapperLabel(String text) {
/*  7 */     super(text);
/*  8 */     setLineWrap(true);
/*  9 */     setWrapStyleWord(true);
/* 10 */     setEditable(false);
/* 11 */     setHighlighter((Highlighter)null);
/* 12 */     setOpaque(false);
/* 13 */     setBorder(BorderFactory.createEmptyBorder());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\TextWrapperLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */