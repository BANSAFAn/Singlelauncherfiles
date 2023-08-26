/*     */ package org.tlauncher.tlauncher.ui.text;
/*     */ 
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
/*     */ 
/*     */ 
/*     */ public class ExtendedPasswordField
/*     */   extends JPasswordField
/*     */ {
/*     */   private static final long serialVersionUID = 3175896797135831502L;
/*     */   private static final String DEFAULT_PLACEHOLDER = "password";
/*     */   private CenterPanelTheme theme;
/*     */   private String placeholder;
/*     */   
/*     */   private ExtendedPasswordField(CenterPanel panel, String placeholder) {
/*  21 */     this
/*  22 */       .theme = (panel == null) ? CenterPanel.defaultTheme : panel.getTheme();
/*  23 */     this.placeholder = (placeholder == null) ? "password" : placeholder;
/*     */ 
/*     */     
/*  26 */     addFocusListener(new FocusListener()
/*     */         {
/*     */           public void focusGained(FocusEvent e) {
/*  29 */             ExtendedPasswordField.this.onFocusGained();
/*     */           }
/*     */ 
/*     */           
/*     */           public void focusLost(FocusEvent e) {
/*  34 */             ExtendedPasswordField.this.onFocusLost();
/*     */           }
/*     */         });
/*     */     
/*  38 */     getDocument().addDocumentListener(new DocumentListener()
/*     */         {
/*     */           public void insertUpdate(DocumentEvent e) {
/*  41 */             ExtendedPasswordField.this.onChange();
/*     */           }
/*     */ 
/*     */           
/*     */           public void removeUpdate(DocumentEvent e) {
/*  46 */             ExtendedPasswordField.this.onChange();
/*     */           }
/*     */ 
/*     */           
/*     */           public void changedUpdate(DocumentEvent e) {
/*  51 */             ExtendedPasswordField.this.onChange();
/*     */           }
/*     */         });
/*     */     
/*  55 */     setText((String)null);
/*     */   }
/*     */   
/*     */   public ExtendedPasswordField() {
/*  59 */     this((CenterPanel)null, (String)null);
/*     */   }
/*     */   
/*     */   private String getValueOf(String value) {
/*  63 */     if (value == null || value.isEmpty() || value.equals(this.placeholder)) {
/*  64 */       return null;
/*     */     }
/*  66 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String getText() {
/*  72 */     return super.getText();
/*     */   }
/*     */ 
/*     */   
/*     */   public char[] getPassword() {
/*  77 */     String value = getValue();
/*     */     
/*  79 */     if (value == null) {
/*  80 */       return new char[0];
/*     */     }
/*  82 */     return value.toCharArray();
/*     */   }
/*     */   
/*     */   public boolean hasPassword() {
/*  86 */     return (getValue() != null);
/*     */   }
/*     */   
/*     */   private String getValue() {
/*  90 */     return getValueOf(getText());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setText(String text) {
/*  95 */     String value = getValueOf(text);
/*     */     
/*  97 */     if (value == null) {
/*  98 */       setPlaceholder();
/*     */     } else {
/* 100 */       setForeground(this.theme.getFocus());
/* 101 */       super.setText(value);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setPlaceholder() {
/* 106 */     setForeground(this.theme.getFocusLost());
/* 107 */     super.setText(this.placeholder);
/*     */   }
/*     */   
/*     */   private void setEmpty() {
/* 111 */     setForeground(this.theme.getFocus());
/* 112 */     super.setText("");
/*     */   }
/*     */   
/*     */   void updateStyle() {
/* 116 */     setForeground((getValue() == null) ? this.theme.getFocusLost() : this.theme
/* 117 */         .getFocus());
/*     */   }
/*     */   
/*     */   public String getPlaceholder() {
/* 121 */     return this.placeholder;
/*     */   }
/*     */   
/*     */   public void setPlaceholder(String placeholder) {
/* 125 */     this.placeholder = (placeholder == null) ? "password" : placeholder;
/*     */     
/* 127 */     if (getValue() == null)
/* 128 */       setPlaceholder(); 
/*     */   }
/*     */   
/*     */   public CenterPanelTheme getTheme() {
/* 132 */     return this.theme;
/*     */   }
/*     */   
/*     */   public void setTheme(CenterPanelTheme theme) {
/* 136 */     if (theme == null) {
/* 137 */       theme = CenterPanel.defaultTheme;
/*     */     }
/* 139 */     this.theme = theme;
/* 140 */     updateStyle();
/*     */   }
/*     */   
/*     */   protected void onFocusGained() {
/* 144 */     if (getValue() == null)
/* 145 */       setEmpty(); 
/*     */   }
/*     */   
/*     */   protected void onFocusLost() {
/* 149 */     if (getValue() == null)
/* 150 */       setPlaceholder(); 
/*     */   }
/*     */   
/*     */   protected void onChange() {}
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\text\ExtendedPasswordField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */