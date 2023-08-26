/*    */ package org.tlauncher.tlauncher.ui.settings;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.event.FocusEvent;
/*    */ import java.awt.event.FocusListener;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.JComponent;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.tlauncher.tlauncher.configuration.Configuration;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.editor.EditorField;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PageSettings
/*    */   extends ExtendedPanel
/*    */   implements SettingsHandlerInterface
/*    */ {
/* 34 */   private final List<HandlerSettings> settingsList = new ArrayList<>();
/* 35 */   protected TLauncher tlauncher = TLauncher.getInstance();
/* 36 */   protected final Configuration global = this.tlauncher.getConfiguration();
/*    */   
/*    */   private static final long serialVersionUID = 971905170736637142L;
/*    */   
/*    */   public boolean validateSettings() {
/* 41 */     for (HandlerSettings handler : this.settingsList) {
/* 42 */       if (!handler.getEditorField().isValueValid()) {
/* 43 */         EditorField editorField = handler.getEditorField();
/* 44 */         ((JComponent)editorField).setBackground(Color.PINK);
/* 45 */         return false;
/*    */       } 
/*    */     } 
/* 48 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValues() {
/* 53 */     for (HandlerSettings handler : this.settingsList) {
/* 54 */       String key = handler.getKey();
/* 55 */       String oldValue = this.global.get(key);
/* 56 */       String newValue = handler.getEditorField().getSettingsValue();
/* 57 */       if (!StringUtils.equals(oldValue, newValue)) {
/* 58 */         this.global.set(key, newValue);
/* 59 */         handler.onChange(oldValue, newValue);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefaultSettings() {
/* 66 */     for (HandlerSettings handler : this.settingsList) {
/* 67 */       handler.getEditorField().setSettingsValue(this.global.getDefault(handler.getKey()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void addHandler(HandlerSettings handler) {
/* 73 */     addFocus((Component)handler.getEditorField(), new FocusListener()
/*    */         {
/*    */           public void focusLost(FocusEvent e) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/*    */           public void focusGained(FocusEvent e) {
/* 82 */             e.getComponent().setBackground(Color.white);
/*    */           }
/*    */         });
/* 85 */     this.settingsList.add(handler);
/*    */   }
/*    */   
/*    */   private void addFocus(Component comp, FocusListener focus) {
/* 89 */     comp.addFocusListener(focus);
/*    */     
/* 91 */     if (comp instanceof Container)
/* 92 */       for (Component curComp : ((Container)comp).getComponents()) {
/* 93 */         addFocus(curComp, focus);
/*    */       } 
/*    */   }
/*    */   
/*    */   public void init() {
/* 98 */     for (HandlerSettings handler : this.settingsList)
/* 99 */       handler.getEditorField().setSettingsValue(this.global.get(handler.getKey())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\settings\PageSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */