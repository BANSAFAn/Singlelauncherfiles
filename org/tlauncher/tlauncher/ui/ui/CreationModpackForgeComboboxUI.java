/*   */ package org.tlauncher.tlauncher.ui.ui;
/*   */ 
/*   */ import org.tlauncher.modpack.domain.client.share.MinecraftVersionDTO;
/*   */ 
/*   */ public class CreationModpackForgeComboboxUI
/*   */   extends CreationModpackComboBoxUI
/*   */ {
/*   */   public String getText(Object ob) {
/* 9 */     return ((MinecraftVersionDTO)this.comboBox.getSelectedItem()).getName();
/*   */   }
/*   */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\CreationModpackForgeComboboxUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */