/*    */ package org.tlauncher.tlauncher.ui.swing.box;
/*    */ 
/*    */ import javax.swing.BorderFactory;
/*    */ import javax.swing.ComboBoxModel;
/*    */ import javax.swing.DefaultComboBoxModel;
/*    */ import javax.swing.JComboBox;
/*    */ import javax.swing.plaf.ComboBoxUI;
/*    */ import org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI;
/*    */ 
/*    */ public class TlauncherCustomBox<T>
/*    */   extends JComboBox<T> {
/*    */   public TlauncherCustomBox() {
/* 13 */     init();
/*    */   }
/*    */   
/*    */   public TlauncherCustomBox(ComboBoxModel<T> aModel) {
/* 17 */     super(aModel);
/* 18 */     init();
/*    */   }
/*    */   
/*    */   public TlauncherCustomBox(T[] items) {
/* 22 */     super(items);
/* 23 */     setModel(new DefaultComboBoxModel<>(items));
/* 24 */     init();
/*    */   }
/*    */   
/*    */   protected void init() {
/* 28 */     setUI((ComboBoxUI)new TlauncherBasicComboBoxUI());
/* 29 */     setBorder(BorderFactory.createEmptyBorder());
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\box\TlauncherCustomBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */