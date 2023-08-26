/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JCheckBox;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageIcon;
/*    */ 
/*    */ public class OwnImageCheckBox extends JCheckBox {
/*    */   public OwnImageCheckBox(String text, String onText, String offText) {
/* 10 */     super(text);
/* 11 */     ImageIcon on = ImageCache.getIcon(onText);
/* 12 */     ImageIcon off = ImageCache.getIcon(offText);
/* 13 */     setSelectedIcon((Icon)on);
/* 14 */     setDisabledSelectedIcon((Icon)on);
/* 15 */     setPressedIcon((Icon)on);
/* 16 */     setIcon((Icon)off);
/* 17 */     setDisabledIcon((Icon)off);
/* 18 */     setOpaque(false);
/* 19 */     setFocusable(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\OwnImageCheckBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */