/*    */ package org.tlauncher.tlauncher.ui.swing.notification;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.GridLayout;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import javax.swing.BoxLayout;
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JPanel;
/*    */ import org.tlauncher.tlauncher.configuration.LangConfiguration;
/*    */ import org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ public class UpdaterJavaNotification
/*    */   extends JPanel {
/* 20 */   private final Dimension DIMENSION = new Dimension(500, 100);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public UpdaterJavaNotification(LangConfiguration configuration) {
/* 26 */     setLayout(new BoxLayout(this, 1));
/*    */ 
/*    */     
/* 29 */     JPanel messagePanel = new JPanel();
/* 30 */     add(messagePanel);
/* 31 */     messagePanel.setLayout(new GridLayout(0, 1, 0, 0));
/*    */     
/* 33 */     String userMessage = OS.is(new OS[] { OS.WINDOWS }) ? configuration.get("updater.java.notification.message") : configuration.get("updater.java.notification.message.special");
/* 34 */     FlexibleEditorPanel message = new FlexibleEditorPanel("text/html", userMessage, this.DIMENSION.width);
/*    */     
/* 36 */     messagePanel.add((Component)message);
/*    */   }
/*    */   
/*    */   public static int showUpdaterJavaNotfication(LangConfiguration configuration) {
/* 40 */     List<Object> buttons = new ArrayList(3);
/* 41 */     if (OS.is(new OS[] { OS.WINDOWS })) {
/* 42 */       buttons.add(configuration.get("updater.java.install.button"));
/*    */     }
/* 44 */     buttons.add(configuration.get("updater.java.myself.button"));
/* 45 */     buttons.add(configuration.get("updater.java.reminder.button"));
/* 46 */     JFrame f = new JFrame();
/* 47 */     f.setAlwaysOnTop(true);
/* 48 */     int res = JOptionPane.showOptionDialog(f, new UpdaterJavaNotification(configuration), configuration
/* 49 */         .get("updater.java.notification.title"), -1, 2, null, buttons
/* 50 */         .toArray(), null);
/*    */     
/* 52 */     if (!OS.is(new OS[] { OS.WINDOWS }) && res > -1)
/* 53 */       res++; 
/* 54 */     return res;
/*    */   }
/*    */   public static void main(String[] args) throws IOException {
/* 57 */     LangConfiguration langConfiguration = new LangConfiguration(new Locale[] { new Locale("ru", "Ru") }new Locale("ru", "Ru"), "/lang/tlauncher/");
/*    */ 
/*    */     
/* 60 */     showUpdaterJavaNotfication(langConfiguration);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\notification\UpdaterJavaNotification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */