/*    */ package org.tlauncher.tlauncher.ui.swing.renderer;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CreationModpackGameVersionComboboxRenderer
/*    */   extends ModpackComboBoxRendererBasic
/*    */ {
/*    */   private static final long serialVersionUID = 4805241656738015345L;
/* 13 */   public static final Color LINE = new Color(149, 149, 149);
/*    */   
/*    */   static final int GUP_LEFT = 13;
/* 16 */   public static final Color TEXT_COLOR = new Color(25, 25, 25);
/*    */ 
/*    */   
/*    */   public String getRenderText(Object value) {
/* 20 */     GameVersionDTO gv = (GameVersionDTO)value;
/* 21 */     if (Long.valueOf(0L).equals(gv.getId()))
/* 22 */       return Localizable.get("modpack.version.any"); 
/* 23 */     return gv.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\renderer\CreationModpackGameVersionComboboxRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */