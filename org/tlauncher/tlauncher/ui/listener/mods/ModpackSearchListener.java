/*    */ package org.tlauncher.tlauncher.ui.listener.mods;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
/*    */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*    */ 
/*    */ public class ModpackSearchListener
/*    */   implements ActionListener {
/*    */   public ModpackSearchListener(ModpackScene scene, String previousValue, LocalizableTextField field) {
/* 12 */     this.scene = scene; this.previousValue = previousValue; this.field = field;
/*    */   }
/*    */   private ModpackScene scene;
/*    */   private String previousValue;
/*    */   private LocalizableTextField field;
/*    */   
/*    */   public void actionPerformed(ActionEvent e) {
/* 19 */     if (this.scene.isVisible() && !StringUtils.equals(this.previousValue, this.field.getValue())) {
/* 20 */       this.previousValue = this.field.getValue();
/* 21 */       this.scene.fillGameEntitiesPanel(true);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\mods\ModpackSearchListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */