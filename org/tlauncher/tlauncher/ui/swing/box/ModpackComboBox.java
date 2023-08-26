/*     */ package org.tlauncher.tlauncher.ui.swing.box;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ComboBoxModel;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.plaf.ComboBoxUI;
/*     */ import javax.swing.plaf.ScrollBarUI;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.listener.mods.GameEntityListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComboBox;
/*     */ import org.tlauncher.tlauncher.ui.swing.renderer.ModpackComboxRenderer;
/*     */ import org.tlauncher.tlauncher.ui.ui.ModpackComboBoxUI;
/*     */ import org.tlauncher.tlauncher.ui.ui.ModpackScrollBarUI;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class ModpackComboBox
/*     */   extends ExtendedComboBox<CompleteVersion>
/*     */   implements GameEntityListener, LocalizableComponent {
/*     */   private static final long serialVersionUID = 7773875370848584863L;
/*     */   
/*     */   public ModpackComboBox() {
/*  39 */     setModel(new LocalModapackBoxModel());
/*  40 */     setRenderer((ListCellRenderer)new ModpackComboxRenderer());
/*  41 */     setUI((ComboBoxUI)new ModpackComboBoxUI()
/*     */         {
/*     */           protected ComboPopup createPopup()
/*     */           {
/*  45 */             BasicComboPopup basic = new BasicComboPopup(this.comboBox)
/*     */               {
/*     */                 private static final long serialVersionUID = -4987177491183525990L;
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*     */                 protected JScrollPane createScroller() {
/*  53 */                   ModpackScrollBarUI barUI = new ModpackScrollBarUI()
/*     */                     {
/*     */                       protected Dimension getMinimumThumbSize() {
/*  56 */                         return new Dimension(10, 40);
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public Dimension getPreferredSize(JComponent c) {
/*  61 */                         Dimension dim = super.getPreferredSize(c);
/*  62 */                         dim.setSize(8.0D, dim.getHeight());
/*  63 */                         return dim;
/*     */                       }
/*     */                     };
/*     */ 
/*     */                   
/*  68 */                   barUI.setGapThubm(5);
/*     */                   
/*  70 */                   JScrollPane scroller = new JScrollPane(this.list, 20, 31);
/*     */                   
/*  72 */                   scroller.getVerticalScrollBar().setUI((ScrollBarUI)barUI);
/*  73 */                   scroller.setBackground(ColorUtil.BACKGROUND_COMBO_BOX_POPUP);
/*  74 */                   return scroller;
/*     */                 }
/*     */               };
/*  77 */             basic.setMaximumSize(new Dimension(172, 149));
/*  78 */             basic.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, ModpackComboxRenderer.LINE));
/*  79 */             return basic;
/*     */           }
/*     */         });
/*     */     
/*  83 */     setBorder(BorderFactory.createLineBorder(ModpackComboxRenderer.LINE, 1));
/*  84 */     addItemListener(e -> TLauncher.getInstance().getConfiguration().set("modpack.combobox.index", Integer.valueOf(getSelectedIndex())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activationStarted(GameEntityDTO e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activation(GameEntityDTO e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activationError(GameEntityDTO e, Throwable t) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processingStarted(GameEntityDTO e, VersionDTO version) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void installEntity(GameEntityDTO e, GameType type) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void installEntity(CompleteVersion e) {
/* 114 */     addItem(e);
/* 115 */     setSelectedItem(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeEntity(GameEntityDTO e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void installError(GameEntityDTO e, VersionDTO v, Throwable t) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void populateStatus(GameEntityDTO status, GameType type, boolean state) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateVersion(CompleteVersion v, CompleteVersion newVersion) {
/* 134 */     repaint();
/*     */   }
/*     */   
/*     */   public void updateVersionStorageAndScene(CompleteVersion v, CompleteVersion newVersion) {
/* 138 */     removeItem(v);
/* 139 */     installEntity(newVersion);
/* 140 */     repaint();
/*     */   }
/*     */   
/*     */   public List<ModpackDTO> getModpacks() {
/* 144 */     int size = getModel().getSize();
/* 145 */     List<ModpackDTO> list = new ArrayList<>();
/* 146 */     for (int i = 1; i < size; i++) {
/* 147 */       list.add(((CompleteVersion)getModel().getElementAt(i)).getModpack());
/*     */     }
/* 149 */     return list;
/*     */   }
/*     */   
/*     */   public CompleteVersion findByModpack(ModpackDTO modpackDTO, VersionDTO versionDTO) {
/* 153 */     int size = getModel().getSize(); int i;
/* 154 */     for (i = 1; i < size; i++) {
/* 155 */       ModpackDTO m = ((CompleteVersion)getModel().getElementAt(i)).getModpack();
/* 156 */       if (m.getId().equals(modpackDTO.getId()) && m.getVersion().getId().equals(versionDTO.getId()))
/* 157 */         return getModel().getElementAt(i); 
/*     */     } 
/* 159 */     U.log(new Object[] { "" + modpackDTO.getId() + " " + versionDTO.getId() });
/* 160 */     for (i = 1; i < size; i++) {
/* 161 */       ModpackDTO m = ((CompleteVersion)getModel().getElementAt(i)).getModpack();
/* 162 */       U.log(new Object[] { "m id =" + m.getId() + " v id =" + m.getVersion().getId() });
/*     */     } 
/* 164 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeCompleteVersion(CompleteVersion e) {
/* 169 */     ComboBoxModel<CompleteVersion> model = getModel();
/* 170 */     for (int i = 1; i < model.getSize(); i++) {
/* 171 */       if (((CompleteVersion)model.getElementAt(i)).getID().equals(e.getID())) {
/*     */         
/* 173 */         removeItemAt(i);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLocale() {
/* 181 */     ((CompleteVersion)getModel().getElementAt(0)).setID(Localizable.get("modpack.local.box.default"));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\box\ModpackComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */