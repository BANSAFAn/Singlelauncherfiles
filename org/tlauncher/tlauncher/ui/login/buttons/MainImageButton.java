/*    */ package org.tlauncher.tlauncher.ui.login.buttons;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import javax.swing.BorderFactory;
/*    */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*    */ 
/*    */ public class MainImageButton
/*    */   extends ImageUdaterButton {
/* 10 */   private static final Color mouseUnderColor = new Color(82, 127, 53);
/*    */   protected MouseAdapter adapter;
/*    */   
/*    */   MainImageButton(Color color, String image, String mouseUnderImage) {
/* 14 */     super(color, mouseUnderColor, image, mouseUnderImage);
/* 15 */     setBorder(BorderFactory.createEmptyBorder());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\buttons\MainImageButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */