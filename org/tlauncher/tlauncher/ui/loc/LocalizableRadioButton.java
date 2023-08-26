/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import java.awt.event.ItemListener;
/*    */ import javax.swing.JRadioButton;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ 
/*    */ 
/*    */ public class LocalizableRadioButton
/*    */   extends JRadioButton
/*    */   implements LocalizableComponent
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String path;
/*    */   
/*    */   public LocalizableRadioButton() {
/* 16 */     init();
/*    */   }
/*    */   
/*    */   public LocalizableRadioButton(String path) {
/* 20 */     init();
/* 21 */     setLabel(path);
/*    */   }
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public void setLabel(String path) {
/* 27 */     setText(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setText(String path) {
/* 32 */     this.path = path;
/* 33 */     super.setText((Localizable.get() == null) ? path : Localizable.get()
/* 34 */         .get(path));
/*    */   }
/*    */   
/*    */   public String getLangPath() {
/* 38 */     return this.path;
/*    */   }
/*    */   
/*    */   public void addListener(ItemListener l) {
/* 42 */     getModel().addItemListener(l);
/*    */   }
/*    */   
/*    */   public void removeListener(ItemListener l) {
/* 46 */     getModel().removeItemListener(l);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 51 */     setLabel(this.path);
/*    */   }
/*    */   
/*    */   private void init() {
/* 55 */     setOpaque(false);
/* 56 */     setIcon(ImageCache.getNativeIcon("radio-button-off.png"));
/*    */     
/* 58 */     setSelectedIcon(ImageCache.getNativeIcon("radio-button-on.png"));
/*    */     
/* 60 */     setDisabledIcon(ImageCache.getNativeIcon("radio-button-off.png"));
/*    */     
/* 62 */     setDisabledSelectedIcon(ImageCache.getNativeIcon("radio-button-on.png"));
/*    */     
/* 64 */     setPressedIcon(ImageCache.getNativeIcon("radio-button-on.png"));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableRadioButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */