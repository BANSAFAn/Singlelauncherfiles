/*     */ package org.tlauncher.tlauncher.ui.loc;
/*     */ 
/*     */ import java.awt.event.ItemListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JCheckBox;
/*     */ import org.tlauncher.tlauncher.ui.TLauncherFrame;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ 
/*     */ 
/*     */ public class LocalizableCheckbox
/*     */   extends JCheckBox
/*     */   implements LocalizableComponent
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String path;
/*     */   
/*     */   public LocalizableCheckbox(String path) {
/*  18 */     init(PANEL_TYPE.LOGIN);
/*  19 */     setLabel(path);
/*     */   }
/*     */   
/*     */   public LocalizableCheckbox(String path, boolean state) {
/*  23 */     super("", state);
/*  24 */     init(PANEL_TYPE.LOGIN);
/*     */     
/*  26 */     setText(path);
/*     */   }
/*     */   
/*     */   public LocalizableCheckbox(String path2, PANEL_TYPE settings) {
/*  30 */     setText(path2);
/*  31 */     init(settings);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setLabel(String path) {
/*  37 */     setText(path);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setText(String path) {
/*  42 */     this.path = path;
/*  43 */     super.setText((Localizable.get() == null) ? path : Localizable.get()
/*  44 */         .get(path));
/*     */   }
/*     */   
/*     */   public String getLangPath() {
/*  48 */     return this.path;
/*     */   }
/*     */   
/*     */   public boolean getState() {
/*  52 */     return getModel().isSelected();
/*     */   }
/*     */   
/*     */   public void setState(boolean state) {
/*  56 */     getModel().setSelected(state);
/*     */   }
/*     */   
/*     */   public void addListener(ItemListener l) {
/*  60 */     getModel().addItemListener(l);
/*     */   }
/*     */   
/*     */   public void removeListener(ItemListener l) {
/*  64 */     getModel().removeItemListener(l);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLocale() {
/*  69 */     setLabel(this.path);
/*     */   }
/*     */   
/*     */   protected void init(PANEL_TYPE panel) {
/*  73 */     Icon off = null;
/*  74 */     Icon on = null;
/*     */     
/*  76 */     switch (panel) {
/*     */       case SETTINGS:
/*  78 */         on = ImageCache.getNativeIcon("settings-check-box-on.png");
/*  79 */         off = ImageCache.getNativeIcon("settings-check-box-off.png");
/*     */         break;
/*     */       case LOGIN:
/*  82 */         on = ImageCache.getNativeIcon("checkbox-on.png");
/*  83 */         off = ImageCache.getNativeIcon("checkbox-off.png");
/*     */         break;
/*     */     } 
/*  86 */     setFont(getFont().deriveFont(TLauncherFrame.fontSize));
/*  87 */     setOpaque(false);
/*     */     
/*  89 */     setIcon(off);
/*     */     
/*  91 */     setSelectedIcon(on);
/*     */     
/*  93 */     setDisabledIcon(off);
/*     */     
/*  95 */     setDisabledSelectedIcon(on);
/*     */     
/*  97 */     setPressedIcon(on);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum PANEL_TYPE
/*     */   {
/* 105 */     SETTINGS, LOGIN;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\LocalizableCheckbox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */