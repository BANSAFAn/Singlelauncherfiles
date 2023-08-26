/*    */ package org.tlauncher.tlauncher.ui.login.buttons;
/*    */ 
/*    */ import java.awt.event.ActionEvent;
/*    */ import org.tlauncher.tlauncher.ui.block.Unblockable;
/*    */ import org.tlauncher.tlauncher.ui.login.LoginForm;
/*    */ import org.tlauncher.util.MinecraftUtil;
/*    */ import org.tlauncher.util.OS;
/*    */ import org.tlauncher.util.async.AsyncThread;
/*    */ 
/*    */ public class FolderButton
/*    */   extends MainImageButton
/*    */   implements Unblockable
/*    */ {
/*    */   private static final long serialVersionUID = 2488886626114461187L;
/*    */   
/*    */   FolderButton(LoginForm loginform) {
/* 17 */     super(DARK_GREEN_COLOR, "folder-mouse-under.png", "folder.png");
/*    */     
/* 19 */     addActionListener(e -> AsyncThread.execute(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\login\buttons\FolderButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */