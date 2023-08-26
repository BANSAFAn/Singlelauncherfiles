/*    */ package org.tlauncher.tlauncher.ui.swing.renderer;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ 
/*    */ public class CreationMinecraftTypeComboboxRenderer
/*    */   extends ModpackComboBoxRendererBasic
/*    */ {
/* 10 */   public static final Color LINE = new Color(149, 149, 149);
/*    */   
/*    */   static final int GUP_LEFT = 13;
/* 13 */   public static final Color TEXT_COLOR = new Color(25, 25, 25);
/*    */ 
/*    */   
/*    */   public String getRenderText(Object value) {
/* 17 */     return Localizable.get("modpack.version." + ((NameIdDTO)value).getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\renderer\CreationMinecraftTypeComboboxRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */