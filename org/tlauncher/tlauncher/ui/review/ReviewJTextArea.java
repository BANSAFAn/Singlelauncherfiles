/*    */ package org.tlauncher.tlauncher.ui.review;
/*    */ 
/*    */ import javax.swing.JTextArea;
/*    */ import javax.swing.text.AttributeSet;
/*    */ import javax.swing.text.BadLocationException;
/*    */ import javax.swing.text.Document;
/*    */ import javax.swing.text.PlainDocument;
/*    */ 
/*    */ public class ReviewJTextArea
/*    */   extends JTextArea
/*    */ {
/*    */   public ReviewJTextArea() {}
/*    */   
/*    */   public ReviewJTextArea(int rows, int column) {
/* 15 */     super(rows, column);
/*    */   }
/*    */   
/*    */   protected final Document createDefaultModel() {
/* 19 */     return new PlainDocument()
/*    */       {
/*    */ 
/*    */         
/*    */         public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
/*    */         {
/* 25 */           if (ReviewJTextArea.this.getText().length() < 1500)
/* 26 */             super.insertString(offs, str, a); 
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\review\ReviewJTextArea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */