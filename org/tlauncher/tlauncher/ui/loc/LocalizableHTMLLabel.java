/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ public class LocalizableHTMLLabel extends LocalizableLabel {
/*    */   public LocalizableHTMLLabel(String path, Object... vars) {
/*  5 */     super(path, vars);
/*    */   }
/*    */   
/*    */   public LocalizableHTMLLabel(String path) {
/*  9 */     this(path, Localizable.EMPTY_VARS);
/*    */   }
/*    */   
/*    */   public LocalizableHTMLLabel() {
/* 13 */     this((String)null);
/*    */   }
/*    */   
/*    */   public LocalizableHTMLLabel(int horizontalAlignment) {
/* 17 */     this((String)null);
/* 18 */     setHorizontalAlignment(horizontalAlignment);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setText(String path, Object... vars) {
/* 23 */     this.path = path;
/* 24 */     this.variables = Localizable.checkVariables(vars);
/*    */     
/* 26 */     String value = Localizable.get(path);
/*    */     
/* 28 */     if (value != null) {
/* 29 */       value = "<html>" + value.replace("\n", "<br/>") + "</html>";
/*    */       
/* 31 */       for (int i = 0; i < this.variables.length; i++) {
/* 32 */         value = value.replace("%" + i, this.variables[i]);
/*    */       }
/*    */     } 
/* 35 */     setRawText(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableHTMLLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */