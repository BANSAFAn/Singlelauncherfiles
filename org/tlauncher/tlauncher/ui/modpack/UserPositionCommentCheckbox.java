/*    */ package org.tlauncher.tlauncher.ui.modpack;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Objects;
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.ButtonGroup;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.modpack.domain.client.CommentDTO;
/*    */ import org.tlauncher.tlauncher.controller.CommentModpackController;
/*    */ import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
/*    */ import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.label.CheckBoxBlockAction;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.tlauncher.ui.model.CurrentUserPosition;
/*    */ 
/*    */ public class UserPositionCommentCheckbox
/*    */   extends CheckBoxBlockAction {
/*    */   private ButtonGroup buttonGroup;
/*    */   private boolean position;
/*    */   private CurrentUserPosition pos;
/*    */   
/*    */   public boolean isPosition() {
/* 25 */     return this.position;
/*    */   } private static final long serialVersionUID = 1L; private CommentModpackController controller; private CommentDTO comment; public void setPos(CurrentUserPosition pos) {
/* 27 */     this.pos = pos;
/*    */   }
/*    */ 
/*    */   
/*    */   public UserPositionCommentCheckbox(String selectedIcon, String diselectedIcon, ButtonGroup buttonGroup, boolean position) {
/* 32 */     super(selectedIcon, diselectedIcon);
/* 33 */     this.buttonGroup = buttonGroup;
/* 34 */     this.position = position;
/*    */   }
/*    */   
/*    */   public void setController(CommentModpackController controller) {
/* 38 */     this.controller = controller;
/*    */   } public void setComment(CommentDTO comment) {
/* 40 */     this.comment = comment;
/*    */   }
/*    */ 
/*    */   
/*    */   public void executeRequest() {
/*    */     try {
/* 46 */       if (isSelected()) {
/* 47 */         this.controller.deletePosition(this.comment.getId());
/* 48 */         this.buttonGroup.clearSelection();
/* 49 */         this.pos.update(this.position, false);
/*    */       } else {
/* 51 */         this.controller.setPosition(this.position, this.comment.getId());
/* 52 */         this.pos.update(this.position, true);
/*    */       } 
/* 54 */       SwingUtilities.invokeLater(() -> {
/*    */             Enumeration<AbstractButton> en = this.buttonGroup.getElements();
/*    */             while (en.hasMoreElements()) {
/*    */               UserPositionCommentCheckbox u = (UserPositionCommentCheckbox)en.nextElement();
/*    */               u.initCounterPosition();
/*    */             } 
/*    */           });
/* 61 */     } catch (RequiredTLAccountException e) {
/* 62 */       Alert.showLocError("modpack.right.panel.required.tl.account.title", 
/* 63 */           Localizable.get("modpack.right.panel.required.tl.account", new Object[] {
/* 64 */               Localizable.get("loginform.button.settings.account")
/*    */             }), null);
/* 66 */     } catch (SelectedAnyOneTLAccountException e) {
/* 67 */       Alert.showLocError("modpack.right.panel.required.tl.account.title", "modpack.right.panel.select.account.tl", null);
/*    */     }
/* 69 */     catch (IOException e) {
/* 70 */       Alert.showLocMessage("modpack.remote.not.found", "modpack.try.later", null);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void initCounterPosition() {
/* 76 */     Long value = this.position ? this.comment.getGoodPosition() : this.comment.getBadPosition();
/* 77 */     if (Objects.isNull(value))
/* 78 */       value = Long.valueOf(0L); 
/* 79 */     if (Objects.nonNull(this.comment.getAuthorPosition()))
/* 80 */       if (this.comment.getAuthorPosition().isPosition() && this.position) {
/* 81 */         Long long_1 = value, long_2 = value = Long.valueOf(value.longValue() - 1L);
/* 82 */       } else if (!this.comment.getAuthorPosition().isPosition() && !this.position) {
/* 83 */         Long long_1 = value, long_2 = value = Long.valueOf(value.longValue() - 1L);
/*    */       }  
/* 85 */     value = Long.valueOf(value.longValue() + this.pos.getByPosition(this.position));
/* 86 */     if (value.longValue() >= 0L) {
/* 87 */       setText("" + value);
/*    */     } else {
/* 89 */       setText("0");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\UserPositionCommentCheckbox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */