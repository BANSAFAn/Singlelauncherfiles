/*    */ package org.tlauncher.tlauncher.ui.text;
/*    */ 
/*    */ import java.awt.event.FocusEvent;
/*    */ import java.awt.event.FocusListener;
/*    */ import javax.swing.JTextArea;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LocalizableTextArea
/*    */   extends JTextArea
/*    */   implements LocalizableComponent
/*    */ {
/*    */   private static final long serialVersionUID = -6319054735918918355L;
/* 22 */   private CenterPanelTheme theme = CenterPanel.defaultTheme;
/*    */   private String placeholder;
/*    */   
/*    */   public LocalizableTextArea(String placeholder, int rows, int columns) {
/* 26 */     super(rows, columns);
/* 27 */     setLineWrap(true);
/* 28 */     this.placeholder = placeholder;
/* 29 */     addFocusListener(new FocusListener()
/*    */         {
/*    */           public void focusLost(FocusEvent e)
/*    */           {
/* 33 */             LocalizableTextArea.this.setPlaceholder();
/*    */           }
/*    */ 
/*    */           
/*    */           public void focusGained(FocusEvent e) {
/* 38 */             LocalizableTextArea.this.setPlaceholder();
/*    */           }
/*    */         });
/* 41 */     setPlaceholder();
/*    */   }
/*    */   private void setPlaceholder() {
/* 44 */     if (StringUtils.isBlank(getText())) {
/* 45 */       setForeground(this.theme.getFocusLost());
/* 46 */       super.setText(Localizable.get(this.placeholder));
/* 47 */     } else if (Localizable.get(this.placeholder).equals(getText())) {
/* 48 */       super.setText("");
/* 49 */       setForeground(this.theme.getFocus());
/*    */     } 
/*    */   }
/*    */   
/*    */   public void updateLocale() {
/* 54 */     setPlaceholder();
/*    */   }
/*    */   
/*    */   public void setText(String t) {
/* 58 */     setForeground(this.theme.getFocus());
/* 59 */     super.setText(t);
/* 60 */     if (StringUtils.isBlank(getText()))
/* 61 */       setPlaceholder(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\text\LocalizableTextArea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */