/*    */ package org.tlauncher.tlauncher.ui.button;
/*    */ 
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import java.util.List;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.SubModpackDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.GameType;
/*    */ import org.tlauncher.modpack.domain.client.share.StateGameElement;
/*    */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*    */ import org.tlauncher.tlauncher.modpack.ModpackUtil;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*    */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ 
/*    */ public class StateModpackElementButton
/*    */   extends ImageUdaterButton {
/*    */   private StateGameElement state;
/*    */   private ModpackManager manager;
/*    */   
/*    */   public StateModpackElementButton(final SubModpackDTO entity, final GameType type) {
/* 25 */     super(buildImage(entity.getStateGameElement()));
/* 26 */     this.manager = (ModpackManager)TLauncher.getInjector().getInstance(ModpackManager.class);
/*    */     
/* 28 */     if (this.state != StateGameElement.BLOCK) {
/* 29 */       addMouseListener(new MouseAdapter() {
/*    */             public void mouseClicked(MouseEvent e) {
/* 31 */               if (SwingUtilities.isLeftMouseButton(e)) {
/* 32 */                 List<GameEntityDTO> list = StateModpackElementButton.this.manager.findDependenciesFromGameEntityDTO((GameEntityDTO)entity);
/* 33 */                 StringBuilder b = ModpackUtil.buildMessage(list);
/* 34 */                 if (list.isEmpty()) {
/* 35 */                   StateModpackElementButton.this.manager.changeModpackElementState((GameEntityDTO)entity, type);
/* 36 */                 } else if (Alert.showQuestion("", Localizable.get("modpack.left.element.remove.question", new Object[] { this.val$entity
/* 37 */                         .getName(), b.toString() }))) {
/* 38 */                   StateModpackElementButton.this.manager.changeModpackElementState((GameEntityDTO)entity, type);
/*    */                 } 
/*    */               } 
/*    */             }
/*    */           });
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void setState(StateGameElement state) {
/* 48 */     setImage(ImageCache.getImage(buildImage(state)));
/* 49 */     this.state = state;
/*    */   }
/*    */   
/*    */   private static String buildImage(StateGameElement state) {
/* 53 */     return (state == null) ? (StateGameElement.ACTIVE + "-element-left.png") : (state + "-element-left.png");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\button\StateModpackElementButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */