/*    */ package org.tlauncher.tlauncher.ui.alert;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.event.ActionEvent;
/*    */ import javax.swing.BoxLayout;
/*    */ import javax.swing.JCheckBox;
/*    */ import javax.swing.JPanel;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel;
/*    */ 
/*    */ public class Notification
/*    */   extends JPanel {
/*    */   public Notification(String message, String saveKey) {
/* 15 */     BoxLayout box = new BoxLayout(this, 1);
/* 16 */     setLayout(box);
/*    */     
/* 18 */     FlexibleEditorPanel label = new FlexibleEditorPanel("text/html", Localizable.get(message), 500);
/*    */     
/* 20 */     JCheckBox notificationState = new JCheckBox(Localizable.get("skin.notification.state"));
/*    */     
/* 22 */     notificationState.addActionListener(e -> {
/*    */           if (notificationState.isSelected()) {
/*    */             TLauncher.getInstance().getConfiguration().set(saveKey, "true", true);
/*    */           } else {
/*    */             TLauncher.getInstance().getConfiguration().set(saveKey, "false", true);
/*    */           } 
/*    */         });
/* 29 */     add((Component)label);
/* 30 */     JPanel panel = new JPanel();
/* 31 */     panel.add(notificationState);
/* 32 */     add(panel);
/*    */   }
/*    */   
/*    */   private static final int WIDTH = 500;
/*    */   public static final String MEMORY_NOTIFICATION = "memory.notification.off";
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\alert\Notification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */