/*    */ package org.tlauncher.tlauncher.ui.swing.box;
/*    */ 
/*    */ import javax.swing.DefaultComboBoxModel;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ 
/*    */ class LocalModapackBoxModel
/*    */   extends DefaultComboBoxModel<CompleteVersion> {
/* 10 */   private final CompleteVersion DEFAULT = new CompleteVersion();
/*    */   
/*    */   public LocalModapackBoxModel() {
/* 13 */     this.DEFAULT.setID(Localizable.get("modpack.local.box.default"));
/* 14 */     ModpackDTO d = new ModpackDTO();
/* 15 */     d.setId(Long.valueOf(0L));
/* 16 */     d.setName("");
/* 17 */     this.DEFAULT.setModpackDTO(d);
/*    */ 
/*    */ 
/*    */     
/* 21 */     addElement(this.DEFAULT);
/* 22 */     setSelectedItem(this.DEFAULT);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeAllElements() {
/* 27 */     super.removeAllElements();
/* 28 */     addElement(this.DEFAULT);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeElement(Object anObject) {
/* 33 */     if (anObject == this.DEFAULT)
/*    */       return; 
/* 35 */     super.removeElement(anObject);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeElementAt(int index) {
/* 40 */     if (index == 0)
/*    */       return; 
/* 42 */     super.removeElementAt(index);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\box\LocalModapackBoxModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */