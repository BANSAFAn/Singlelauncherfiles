/*     */ package org.tlauncher.tlauncher.ui.modpack;
/*     */ 
/*     */ import by.gdev.http.download.service.FileCacheService;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.util.model.download.Repo;
/*     */ import com.google.inject.Injector;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.DefaultListModel;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.PictureType;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.icon.ImageIconPicturePosition;
/*     */ import org.tlauncher.tlauncher.ui.swing.renderer.PictureListRenderer;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class PicturePanel
/*     */   extends ExtendedPanel {
/*     */   private JButton previousPicture;
/*     */   private JButton nextPicture;
/*  50 */   private JList<ImageIcon> list = new JList<>();
/*  51 */   private final int WIDHT_BUTTON = 64;
/*     */   private int current;
/*  53 */   private Dimension buttonSize = new Dimension(64, 155);
/*  54 */   private List<ImageIconPicturePosition> cache = Collections.synchronizedList(new ArrayList<>());
/*     */   private LocalizableLabel downloadingLabel;
/*     */   private ExecutorService executorService;
/*     */   
/*     */   public PicturePanel(final GameEntityDTO g, final GameType type) {
/*  59 */     Injector inj = TLauncher.getInjector();
/*  60 */     final ModpackManager m = (ModpackManager)inj.getInstance(ModpackManager.class);
/*  61 */     this.executorService = (ExecutorService)inj.getInstance(ExecutorService.class);
/*  62 */     final FileCacheService fileCacheService = (FileCacheService)inj.getInstance(FileCacheService.class);
/*  63 */     setPreferredSize(new Dimension(1050, 318));
/*  64 */     setLayout(new BorderLayout(0, 0));
/*  65 */     setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/*  66 */     setOpaque(false);
/*  67 */     this.previousPicture = (JButton)new ImageUdaterButton(Color.WHITE, Color.WHITE, "previous-arrow.png", "previous-arrow-under.png");
/*     */     
/*  69 */     this.nextPicture = (JButton)new ImageUdaterButton(Color.WHITE, Color.WHITE, "next-arrow.png", "next-arrow-under.png");
/*  70 */     this.downloadingLabel = new LocalizableLabel("loginform.loading");
/*  71 */     this.downloadingLabel.setHorizontalAlignment(0);
/*  72 */     this.downloadingLabel.setVerticalAlignment(0);
/*  73 */     SwingUtil.setFontSize((JComponent)this.downloadingLabel, 16.0F);
/*     */     
/*  75 */     this.list.setPreferredSize(new Dimension(ModpackScene.SIZE.width - 128, 190));
/*  76 */     this.list.setOpaque(false);
/*  77 */     this.list.setLayoutOrientation(2);
/*  78 */     this.list.setVisibleRowCount(1);
/*  79 */     this.nextPicture.setPreferredSize(this.buttonSize);
/*  80 */     this.previousPicture.setPreferredSize(this.buttonSize);
/*  81 */     this.list.setCellRenderer((ListCellRenderer<? super ImageIcon>)new PictureListRenderer());
/*     */     
/*  83 */     this.list.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mousePressed(MouseEvent e)
/*     */           {
/*  87 */             if (PicturePanel.this.list.getModel().getSize() > 0 && PicturePanel.this.current != -1) {
/*     */               
/*  89 */               BigPictureObserver pictureObserver = new BigPictureObserver((JFrame)TLauncher.getInstance().getFrame(), "", ((ImageIconPicturePosition)PicturePanel.this.cache.get(PicturePanel.this.current + PicturePanel.this.list.locationToIndex(e.getPoint()))).getPosition(), type, g);
/*  90 */               pictureObserver.setVisible(true);
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  96 */     this.previousPicture.setOpaque(false);
/*  97 */     this.nextPicture.setOpaque(false);
/*     */     
/*  99 */     this.nextPicture.addActionListener(e -> updateNext());
/* 100 */     this.previousPicture.addActionListener(e -> updatePrevious());
/*     */     
/* 102 */     add((Component)this.downloadingLabel, "Center");
/*     */     
/* 104 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentShown(ComponentEvent e)
/*     */           {
/* 108 */             if (!PicturePanel.this.contains((Component)PicturePanel.this.downloadingLabel))
/*     */               return; 
/* 110 */             CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 134 */               .exceptionally(t -> {
/*     */                   U.log(new Object[] { t });
/*     */                   return null;
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void afterDownload() {
/* 143 */     SwingUtilities.invokeLater(() -> {
/*     */           if (this.cache.size() == 1) {
/*     */             this.list.setBorder(BorderFactory.createEmptyBorder(64, 304, 0, 0));
/*     */           } else {
/*     */             this.list.setBorder(BorderFactory.createEmptyBorder(64, 0, 0, 0));
/*     */           } 
/*     */           if (this.cache.size() > 3) {
/*     */             add(this.previousPicture, "West");
/*     */             add(this.nextPicture, "East");
/*     */           } else {
/*     */             setBorder(BorderFactory.createEmptyBorder(0, 64, 0, 64));
/*     */           } 
/*     */           remove((Component)this.downloadingLabel);
/*     */           if (this.cache.isEmpty()) {
/*     */             LocalizableLabel localizableLabel = new LocalizableLabel("modpack.complete.picture.empty");
/*     */             localizableLabel.setHorizontalAlignment(0);
/*     */             localizableLabel.setVerticalAlignment(0);
/*     */             SwingUtil.setFontSize((JComponent)localizableLabel, 16.0F);
/*     */             add((Component)localizableLabel, "Center");
/*     */           } else {
/*     */             add(this.list, "Center");
/*     */             updateData();
/*     */           } 
/*     */           revalidate();
/*     */           repaint();
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateNext() {
/* 173 */     int enable = this.cache.size() - 3 - this.current;
/* 174 */     if (enable > 0) {
/* 175 */       this.current++;
/* 176 */       updateData();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updatePrevious() {
/* 183 */     if (this.current > 0) {
/* 184 */       this.current--;
/* 185 */       updateData();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateData() {
/* 190 */     DefaultListModel<ImageIcon> page = new DefaultListModel<>();
/* 191 */     for (int i = this.current, j = 0; i < this.cache.size() && j < 3; i++, j++) {
/* 192 */       page.addElement((ImageIcon)this.cache.get(i));
/*     */     }
/* 194 */     this.list.setModel(page);
/* 195 */     this.nextPicture.setPreferredSize(this.buttonSize);
/* 196 */     this.previousPicture.setPreferredSize(this.buttonSize);
/* 197 */     repaint();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\PicturePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */