/*    */ package org.tlauncher.tlauncher.ui.console;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
/*    */ import org.tlauncher.tlauncher.ui.center.DefaultCenterPanelTheme;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableInvalidateTextField;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ class SearchField
/*    */   extends LocalizableInvalidateTextField
/*    */ {
/*    */   private static final long serialVersionUID = -6453744340240419870L;
/* 15 */   private static final CenterPanelTheme darkTheme = (CenterPanelTheme)new DefaultCenterPanelTheme() {
/* 16 */       public final Color backgroundColor = new Color(0, 0, 0, 255);
/*    */       
/* 18 */       public final Color focusColor = new Color(255, 255, 255, 255);
/* 19 */       public final Color focusLostColor = new Color(128, 128, 128, 255);
/*    */       
/* 21 */       public final Color successColor = this.focusColor;
/*    */ 
/*    */       
/*    */       public Color getBackground() {
/* 25 */         return this.backgroundColor;
/*    */       }
/*    */ 
/*    */       
/*    */       public Color getFocus() {
/* 30 */         return this.focusColor;
/*    */       }
/*    */ 
/*    */       
/*    */       public Color getFocusLost() {
/* 35 */         return this.focusLostColor;
/*    */       }
/*    */ 
/*    */       
/*    */       public Color getSuccess() {
/* 40 */         return this.successColor;
/*    */       }
/*    */     };
/*    */   
/*    */   SearchField(final SearchPanel sp) {
/* 45 */     super("console.search.placeholder");
/*    */     
/* 47 */     if (OS.WINDOWS.isCurrent()) {
/* 48 */       setTheme(darkTheme);
/*    */     }
/* 50 */     setText(null);
/* 51 */     setCaretColor(Color.white);
/*    */     
/* 53 */     addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 56 */             sp.search();
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\console\SearchField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */