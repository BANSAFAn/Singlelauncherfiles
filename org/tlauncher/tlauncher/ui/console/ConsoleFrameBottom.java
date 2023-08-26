/*    */ package org.tlauncher.tlauncher.ui.console;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*    */ import org.tlauncher.tlauncher.ui.swing.ImageButton;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*    */ 
/*    */ public class ConsoleFrameBottom extends BorderPanel implements LocalizableComponent {
/*    */   private static final long serialVersionUID = 7438348937589503917L;
/*    */   private final ConsoleFrame frame;
/*    */   public final LocalizableButton closeCancelButton;
/*    */   public final ImageButton pastebin;
/*    */   public final ImageButton kill;
/*    */   public final ImageButton scrollingDown;
/*    */   
/*    */   ConsoleFrameBottom(ConsoleFrame fr) {
/* 24 */     this.frame = fr;
/*    */     
/* 26 */     setOpaque(true);
/* 27 */     setBackground(Color.darkGray);
/*    */     
/* 29 */     this.closeCancelButton = new LocalizableButton("console.close.cancel");
/* 30 */     this.closeCancelButton.setVisible(false);
/* 31 */     this.closeCancelButton.addActionListener(e -> {
/*    */           if (!this.closeCancelButton.isVisible()) {
/*    */             return;
/*    */           }
/*    */           this.frame.hiding = false;
/*    */           this.closeCancelButton.setVisible(false);
/*    */         });
/* 38 */     setCenter((Component)this.closeCancelButton);
/* 39 */     this.pastebin = newButton("mail-attachment.png", e -> this.frame.console.sendPaste());
/* 40 */     this.scrollingDown = newButton("down.png", e -> this.frame.scrollDown());
/*    */     
/* 42 */     this.kill = newButton("process-stop.png", new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 45 */             ConsoleFrameBottom.this.frame.console.killProcess();
/* 46 */             ConsoleFrameBottom.this.kill.setEnabled(false);
/*    */           }
/*    */         });
/* 49 */     this.kill.setEnabled(false);
/* 50 */     updateLocale();
/*    */     
/* 52 */     ExtendedPanel buttonPanel = new ExtendedPanel();
/* 53 */     buttonPanel.add(new Component[] { (Component)this.scrollingDown, (Component)this.pastebin, (Component)this.kill });
/* 54 */     setEast((Component)buttonPanel);
/*    */   }
/*    */   
/*    */   private ImageButton newButton(String path, ActionListener action) {
/* 58 */     ImageButton button = new ImageButton(path);
/* 59 */     button.addActionListener(action);
/* 60 */     button.setPreferredSize(new Dimension(32, 32));
/* 61 */     return button;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 66 */     this.pastebin.setToolTipText(Localizable.get("console.pastebin"));
/* 67 */     this.kill.setToolTipText(Localizable.get("console.kill"));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\console\ConsoleFrameBottom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */