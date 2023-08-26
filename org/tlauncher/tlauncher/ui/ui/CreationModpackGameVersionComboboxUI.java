/*    */ package org.tlauncher.tlauncher.ui.ui;
/*    */ 
/*    */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ 
/*    */ public class CreationModpackGameVersionComboboxUI
/*    */   extends CreationModpackComboBoxUI {
/*    */   public String getText(Object ob) {
/*  9 */     GameVersionDTO gv = (GameVersionDTO)ob;
/* 10 */     if (Long.valueOf(0L).equals(gv.getId()))
/* 11 */       return Localizable.get("modpack.version.any"); 
/* 12 */     return gv.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\ui\CreationModpackGameVersionComboboxUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */