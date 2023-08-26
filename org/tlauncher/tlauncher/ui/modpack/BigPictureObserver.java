/*     */ package org.tlauncher.tlauncher.ui.modpack;
/*     */ 
/*     */ import by.gdev.http.download.service.FileCacheService;
/*     */ import by.gdev.util.model.download.Metadata;
/*     */ import by.gdev.util.model.download.Repo;
/*     */ import com.google.inject.Injector;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.PictureType;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ public class BigPictureObserver
/*     */   extends TemlateModpackFrame
/*     */ {
/*  40 */   private Color backgroundPanel = new Color(188, 188, 188);
/*     */   
/*     */   private static final int heightPanel = 586;
/*  43 */   final JLabel picture = new JLabel(); private static final int width = 1050; int currentPicture;
/*  44 */   final JLabel close = new JLabel(ImageCache.getNativeIcon("picture-exit.png"));
/*     */   private GameType type;
/*     */   private GameEntityDTO gameEntity;
/*     */   private Repo pictures;
/*     */   private ModpackManager manager;
/*     */   private FileCacheService fileCacheService;
/*     */   
/*     */   public BigPictureObserver(JFrame parent, String title, int i, GameType type, GameEntityDTO gameEntity) {
/*  52 */     super(parent, title, new Dimension(1050, 586), OS.is(new OS[] { OS.LINUX }));
/*  53 */     Injector inj = TLauncher.getInjector();
/*  54 */     this.fileCacheService = (FileCacheService)inj.getInstance(FileCacheService.class);
/*  55 */     this.manager = (ModpackManager)inj.getInstance(ModpackManager.class);
/*  56 */     this.type = type;
/*  57 */     this.gameEntity = gameEntity;
/*  58 */     this.currentPicture = i;
/*  59 */     JLayeredPane layeredPane = new JLayeredPane();
/*  60 */     Button previousPicture = new Button("big-picture-previous-arrow.png", "previous-arrow-under.png");
/*  61 */     Button nextPicture = new Button("big-picture-next-arrow.png", "next-arrow-under.png");
/*     */     
/*  63 */     previousPicture.setPreferredSize(new Dimension(46, 155));
/*  64 */     nextPicture.setPreferredSize(new Dimension(46, 155));
/*     */     
/*  66 */     layeredPane.setBackground(this.backgroundPanel);
/*     */     
/*  68 */     this.picture.setBorder(BorderFactory.createLineBorder(Color.white, 5));
/*     */     
/*  70 */     this.close.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/*  73 */             BigPictureObserver.this.close.setIcon(ImageCache.getNativeIcon("picture-exit-on.png"));
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/*  78 */             BigPictureObserver.this.close.setIcon(ImageCache.getNativeIcon("picture-exit.png"));
/*     */           }
/*     */ 
/*     */           
/*     */           public void mousePressed(MouseEvent e) {
/*  83 */             if (SwingUtilities.isLeftMouseButton(e)) {
/*  84 */               BigPictureObserver.this.setVisible(false);
/*     */             }
/*     */           }
/*     */         });
/*  88 */     layeredPane.add((Component)previousPicture, 1);
/*  89 */     layeredPane.add((Component)nextPicture, 1);
/*  90 */     layeredPane.add(this.picture, 1);
/*  91 */     layeredPane.add(this.close, 0);
/*  92 */     layeredPane.setSize(new Dimension(1050, 586));
/*  93 */     previousPicture.setBounds(10, 266, 19, 33);
/*  94 */     nextPicture.setBounds(1021, 266, 19, 33);
/*     */     
/*  96 */     putOnPanel();
/*     */     
/*  98 */     add(layeredPane);
/*  99 */     nextPicture.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 103 */             if (Objects.nonNull(BigPictureObserver.this.pictures) && BigPictureObserver.this.currentPicture < BigPictureObserver.this.pictures.getResources().size() - 1) {
/* 104 */               BigPictureObserver.this.currentPicture++;
/* 105 */               BigPictureObserver.this.putOnPanel();
/*     */             } 
/*     */           }
/*     */         });
/* 109 */     previousPicture.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 113 */             if (BigPictureObserver.this.currentPicture > 0) {
/* 114 */               BigPictureObserver.this.currentPicture--;
/* 115 */               BigPictureObserver.this.putOnPanel();
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void putOnPanel() {
/* 123 */     CompletableFuture.runAsync(() -> {
/*     */           try {
/*     */             if (Objects.isNull(this.pictures)) {
/*     */               this.pictures = this.manager.getGameEntitiesPictures(this.type, this.gameEntity.getId(), PictureType.MAX);
/*     */             }
/*     */             
/*     */             if (!this.pictures.getResources().isEmpty()) {
/*     */               Metadata meta = this.pictures.getResources().get(this.currentPicture);
/*     */               this.picture.setIcon(new ImageIcon(Files.readAllBytes(this.fileCacheService.getRawObject(this.pictures.getRepositories(), meta, true))));
/*     */             } 
/* 133 */           } catch (Exception e) {
/*     */             U.log(new Object[] { e });
/*     */           } 
/* 136 */         }).thenAccept(e -> {
/*     */           Dimension pictureSize = this.picture.getPreferredSize();
/*     */           this.picture.setBounds((1050 - pictureSize.width) / 2, (586 - pictureSize.height) / 2, pictureSize.width, pictureSize.height);
/*     */           this.close.setBounds((1050 - pictureSize.width) / 2 + pictureSize.width - 21, (586 - pictureSize.height) / 2 - 21, 42, 42);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private class Button
/*     */     extends ImageUdaterButton
/*     */   {
/*     */     public Button(final String s, final String s1) {
/* 148 */       super(s);
/* 149 */       setOpaque(false);
/* 150 */       addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mouseEntered(MouseEvent e) {
/* 153 */               BigPictureObserver.Button.this.setImage(BigPictureObserver.Button.loadImage(s1));
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseExited(MouseEvent e) {
/* 158 */               BigPictureObserver.Button.this.setImage(BigPictureObserver.Button.loadImage(s));
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void paintComponent(Graphics g) {
/* 166 */       paintPicture(g, (JComponent)this, getBounds());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\BigPictureObserver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */