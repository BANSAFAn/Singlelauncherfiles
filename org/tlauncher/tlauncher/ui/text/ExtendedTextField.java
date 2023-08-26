/*     */ package org.tlauncher.tlauncher.ui.text;
/*     */ 
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedTextField
/*     */   extends JTextField
/*     */ {
/*     */   private static final long serialVersionUID = -1963422246993419362L;
/*     */   private CenterPanelTheme theme;
/*     */   private String placeholder;
/*     */   private String oldPlaceholder;
/*     */   
/*     */   protected ExtendedTextField(CenterPanel panel, String placeholder, String value) {
/*  27 */     this
/*  28 */       .theme = (panel == null) ? CenterPanel.defaultTheme : panel.getTheme();
/*  29 */     this.placeholder = placeholder;
/*     */     
/*  31 */     addFocusListener(new FocusListener()
/*     */         {
/*     */           public void focusGained(FocusEvent e) {
/*  34 */             ExtendedTextField.this.onFocusGained();
/*     */           }
/*     */ 
/*     */           
/*     */           public void focusLost(FocusEvent e) {
/*  39 */             ExtendedTextField.this.onFocusLost();
/*     */           }
/*     */         });
/*     */     
/*  43 */     getDocument().addDocumentListener(new DocumentListener()
/*     */         {
/*     */           public void insertUpdate(DocumentEvent e) {
/*  46 */             ExtendedTextField.this.onChange();
/*     */           }
/*     */ 
/*     */           
/*     */           public void removeUpdate(DocumentEvent e) {
/*  51 */             ExtendedTextField.this.onChange();
/*     */           }
/*     */ 
/*     */           
/*     */           public void changedUpdate(DocumentEvent e) {
/*  56 */             ExtendedTextField.this.onChange();
/*     */           }
/*     */         });
/*     */     
/*  60 */     setValue(value);
/*     */   }
/*     */   
/*     */   public ExtendedTextField(String placeholder, String value) {
/*  64 */     this((CenterPanel)null, placeholder, value);
/*     */   }
/*     */   
/*     */   public ExtendedTextField(String placeholder) {
/*  68 */     this((CenterPanel)null, placeholder, (String)null);
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
/*     */   @Deprecated
/*     */   public String getText() {
/*  82 */     return super.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getValueOf(String value) {
/*  93 */     if (value == null || value.isEmpty() || value.equals(this.placeholder) || value
/*  94 */       .equals(this.oldPlaceholder)) {
/*  95 */       return null;
/*     */     }
/*  97 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 107 */     return getValueOf(getText());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setText(String text) {
/* 112 */     String value = getValueOf(text);
/*     */     
/* 114 */     if (value == null) {
/* 115 */       setPlaceholder();
/*     */     } else {
/* 117 */       setForeground(this.theme.getFocus());
/* 118 */       setRawText(value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setPlaceholder() {
/* 123 */     setForeground(this.theme.getFocusLost());
/* 124 */     setRawText(this.placeholder);
/*     */   }
/*     */   
/*     */   private void setEmpty() {
/* 128 */     setForeground(this.theme.getFocus());
/* 129 */     setRawText("");
/*     */   }
/*     */   
/*     */   protected void updateStyle() {
/* 133 */     setForeground((getValue() == null) ? this.theme.getFocusLost() : this.theme
/* 134 */         .getFocus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(Object obj) {
/* 143 */     setText((obj == null) ? null : obj.toString());
/*     */   }
/*     */   
/*     */   protected void setValue(String s) {
/* 147 */     setText(s);
/*     */   }
/*     */   
/*     */   protected void setRawText(String s) {
/* 151 */     super.setText(s);
/* 152 */     setCaretPosition(0);
/*     */   }
/*     */   
/*     */   public String getPlaceholder() {
/* 156 */     return this.placeholder;
/*     */   }
/*     */   
/*     */   protected void setPlaceholder(String placeholder) {
/* 160 */     this.oldPlaceholder = this.placeholder;
/* 161 */     this.placeholder = placeholder;
/* 162 */     if (getValue() == null)
/* 163 */       setPlaceholder(); 
/*     */   }
/*     */   
/*     */   public CenterPanelTheme getTheme() {
/* 167 */     return this.theme;
/*     */   }
/*     */   
/*     */   protected void setTheme(CenterPanelTheme theme) {
/* 171 */     if (theme == null) {
/* 172 */       theme = CenterPanel.defaultTheme;
/*     */     }
/* 174 */     this.theme = theme;
/* 175 */     updateStyle();
/*     */   }
/*     */   
/*     */   protected void onFocusGained() {
/* 179 */     if (getValue() == null)
/* 180 */       setEmpty(); 
/*     */   }
/*     */   
/*     */   protected void onFocusLost() {
/* 184 */     if (getValue() == null)
/* 185 */       setPlaceholder(); 
/*     */   }
/*     */   
/*     */   protected void onChange() {}
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\text\ExtendedTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */