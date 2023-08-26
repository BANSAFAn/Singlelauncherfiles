/*    */ package org.tlauncher.tlauncher.ui.versions;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import java.beans.PropertyChangeListener;
/*    */ import javax.swing.plaf.basic.BasicHTML;
/*    */ import javax.swing.text.View;
/*    */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*    */ import org.tlauncher.tlauncher.ui.swing.ResizeableComponent;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.HTMLLabel;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ public class VersionTipPanel
/*    */   extends CenterPanel implements LocalizableComponent, ResizeableComponent {
/*    */   private final HTMLLabel tip;
/*    */   
/*    */   VersionTipPanel(VersionHandler handler) {
/* 20 */     super(CenterPanel.tipTheme, CenterPanel.squareInsets);
/*    */     
/* 22 */     this.tip = new HTMLLabel();
/* 23 */     add((Component)this.tip);
/*    */     
/* 25 */     this.tip.addPropertyChangeListener("html", new PropertyChangeListener()
/*    */         {
/*    */           public void propertyChange(PropertyChangeEvent evt)
/*    */           {
/* 29 */             Object o = evt.getNewValue();
/* 30 */             if (o == null || !(o instanceof View))
/*    */               return; 
/* 32 */             View view = (View)o;
/*    */ 
/*    */             
/* 35 */             BasicHTML.getHTMLBaseline(view, 500 - VersionTipPanel.this.getHorizontalInsets(), 0);
/*    */           }
/*    */         });
/*    */ 
/*    */     
/* 40 */     updateLocale();
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 45 */     this.tip.setText("");
/*    */     
/* 47 */     String text = Localizable.get("version.list.tip");
/* 48 */     if (text == null)
/*    */       return; 
/* 50 */     text = text.replace("{Ctrl}", OS.OSX.isCurrent() ? "Command" : "Ctrl");
/* 51 */     this.tip.setText(text);
/*    */     
/* 53 */     onResize();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onResize() {
/* 58 */     setSize(500, this.tip.getHeight() + getVerticalInsets());
/*    */   }
/*    */   
/*    */   private int getVerticalInsets() {
/* 62 */     return (getInsets()).top + (getInsets()).bottom;
/*    */   }
/*    */   
/*    */   private int getHorizontalInsets() {
/* 66 */     return (getInsets()).left + (getInsets()).right;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\versions\VersionTipPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */