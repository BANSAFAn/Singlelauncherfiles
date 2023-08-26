/*    */ package org.tlauncher.tlauncher.ui.swing.notification.skin;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Component;
/*    */ import java.awt.FlowLayout;
/*    */ import java.awt.Font;
/*    */ import javax.swing.BoxLayout;
/*    */ import javax.swing.JCheckBox;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel;
/*    */ import org.tlauncher.tlauncher.ui.swing.editor.EditorPane;
/*    */ 
/*    */ public class SkinNotification extends JPanel {
/*    */   public SkinNotification() {
/* 18 */     setLayout(new BorderLayout(0, 0));
/*    */     
/* 20 */     JPanel panel = new JPanel();
/* 21 */     add(panel, "West");
/* 22 */     panel.setLayout(new GridLayout(1, 0, 0, 0));
/*    */     
/* 24 */     JLabel lblNewLabel = new JLabel((Icon)ImageCache.getIcon("notification-picture.png"));
/* 25 */     lblNewLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
/* 26 */     panel.add(lblNewLabel);
/*    */     
/* 28 */     JPanel panel_1 = new JPanel();
/* 29 */     add(panel_1, "Center");
/* 30 */     panel_1.setLayout(new BoxLayout(panel_1, 1));
/*    */     
/* 32 */     JPanel titlePanel = new JPanel();
/* 33 */     panel_1.add(titlePanel);
/*    */     
/* 35 */     JLabel title = new JLabel(Localizable.get("skin.notification.up.title"));
/* 36 */     Font font = title.getFont();
/* 37 */     title.setFont(new Font(font.getName(), font.getStyle(), 16));
/* 38 */     titlePanel.add(title);
/*    */     
/* 40 */     JPanel commonInformation = new JPanel();
/* 41 */     panel_1.add(commonInformation);
/*    */ 
/*    */     
/* 44 */     FlexibleEditorPanel commonInformationLabel = new FlexibleEditorPanel("text/html", Localizable.get("skin.notification.common.message"), 400);
/* 45 */     commonInformation.add((Component)commonInformationLabel);
/*    */     
/* 47 */     JPanel TlIconPanel = new JPanel();
/* 48 */     panel_1.add(TlIconPanel);
/* 49 */     String textImage = String.format(Localizable.get("skin.notification.image.explanation"), new Object[] { ImageCache.getRes("tlauncher-user.png").toExternalForm() });
/* 50 */     FlexibleEditorPanel flexibleEditorPanel = new FlexibleEditorPanel("text/html", textImage, 400);
/* 51 */     TlIconPanel.add((Component)flexibleEditorPanel);
/*    */     
/* 53 */     JPanel detailInformation = new JPanel();
/* 54 */     panel_1.add(detailInformation);
/*    */     
/* 56 */     EditorPane detailSkinLink = new EditorPane("text/html", Localizable.get("skin.notification.link.message"));
/* 57 */     detailInformation.add((Component)detailSkinLink);
/*    */     
/* 59 */     JPanel boxPanel = new JPanel();
/* 60 */     panel_1.add(boxPanel);
/* 61 */     boxPanel.setLayout(new FlowLayout(1, 5, 5));
/*    */     
/* 63 */     JCheckBox notificationState = new JCheckBox(Localizable.get("skin.notification.state"));
/* 64 */     boxPanel.add(notificationState);
/* 65 */     notificationState.addActionListener(e -> {
/*    */           if (notificationState.isSelected())
/*    */             TLauncher.getInstance().getConfiguration().set("skin.notification.off", "true"); 
/*    */         });
/*    */   }
/*    */   
/*    */   private static final int WIDTH = 400;
/*    */   
/*    */   public static void showMessage() {
/* 74 */     SkinNotification notification = new SkinNotification();
/* 75 */     Alert.showCustomMonolog(Localizable.get("skin.notification.title"), notification);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\notification\skin\SkinNotification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */