/*    */ package org.tlauncher.tlauncher.ui.login.buttons;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import java.net.URI;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.block.Unblockable;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
/*    */ import org.tlauncher.util.OS;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class VisitButton
/*    */   extends LocalizableButton
/*    */   implements Unblockable
/*    */ {
/*    */   private static final long serialVersionUID = 1301825302386488945L;
/* 17 */   private static URI ru = U.makeURI("http://masken.ru/rum.html");
/* 18 */   private static URI en = U.makeURI("http://masken.ru/enmine.html");
/*    */   
/*    */   private URI link;
/*    */   
/*    */   VisitButton() {
/* 23 */     super("loginform.button.visit");
/*    */     
/* 25 */     addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 28 */             OS.openLink(VisitButton.this.link);
/*    */           }
/*    */         });
/*    */     
/* 32 */     updateLocale();
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateLocale() {
/* 37 */     super.updateLocale();
/* 38 */     this.link = TLauncher.getInstance().getConfiguration().isUSSRLocale() ? ru : en;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\buttons\VisitButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */