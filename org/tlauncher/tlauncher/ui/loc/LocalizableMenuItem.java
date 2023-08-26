/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.swing.JMenuItem;
/*    */ 
/*    */ public class LocalizableMenuItem
/*    */   extends JMenuItem
/*    */   implements LocalizableComponent
/*    */ {
/*    */   private static final long serialVersionUID = 1364363532569997394L;
/* 13 */   private static List<LocalizableMenuItem> items = Collections.synchronizedList(new ArrayList<>());
/*    */   
/*    */   private String path;
/*    */   
/*    */   private String[] variables;
/*    */ 
/*    */   
/*    */   public LocalizableMenuItem(String path, Object... vars) {
/* 21 */     items.add(this);
/* 22 */     setText(path, vars);
/*    */   }
/*    */   
/*    */   public LocalizableMenuItem(String path) {
/* 26 */     this(path, Localizable.EMPTY_VARS);
/*    */   }
/*    */   
/*    */   public void setText(String path, Object... vars) {
/* 30 */     this.path = path;
/* 31 */     this.variables = Localizable.checkVariables(vars);
/*    */     
/* 33 */     String value = Localizable.get(path);
/* 34 */     for (int i = 0; i < this.variables.length; i++) {
/* 35 */       value = value.replace("%" + i, this.variables[i]);
/*    */     }
/* 37 */     super.setText(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setText(String path) {
/* 42 */     setText(path, Localizable.EMPTY_VARS);
/*    */   }
/*    */   
/*    */   public void setVariables(Object... vars) {
/* 46 */     setText(this.path, vars);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 51 */     setText(this.path, (Object[])this.variables);
/*    */   }
/*    */   
/*    */   public static void updateLocales() {
/* 55 */     for (LocalizableMenuItem item : items) {
/* 56 */       if (item == null)
/*    */         continue; 
/* 58 */       item.updateLocale();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableMenuItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */