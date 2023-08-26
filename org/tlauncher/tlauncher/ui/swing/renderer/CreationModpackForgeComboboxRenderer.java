/*    */ package org.tlauncher.tlauncher.ui.swing.renderer;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import org.tlauncher.modpack.domain.client.share.MinecraftVersionDTO;
/*    */ 
/*    */ public class CreationModpackForgeComboboxRenderer
/*    */   extends ModpackComboBoxRendererBasic
/*    */ {
/*  9 */   public static final Color LINE = new Color(149, 149, 149);
/*    */   
/*    */   static final int GUP_LEFT = 13;
/* 12 */   public static final Color TEXT_COLOR = new Color(25, 25, 25);
/*    */ 
/*    */   
/*    */   public String getRenderText(Object value) {
/* 16 */     return ((MinecraftVersionDTO)value).getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\renderer\CreationModpackForgeComboboxRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */