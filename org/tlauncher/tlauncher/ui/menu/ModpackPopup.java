/*     */ package org.tlauncher.tlauncher.ui.menu;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.plaf.basic.BasicMenuItemUI;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
/*     */ import org.tlauncher.tlauncher.ui.modpack.AddedModpackStuffFrame;
/*     */ import org.tlauncher.tlauncher.ui.modpack.HandleInstallModpackElementFrame;
/*     */ import org.tlauncher.tlauncher.ui.modpack.ModpackBackupFrame;
/*     */ import org.tlauncher.tlauncher.ui.swing.box.ModpackComboBox;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ public class ModpackPopup
/*     */   extends JPopupMenu
/*     */ {
/*     */   private static final long serialVersionUID = 1985825573230509140L;
/*  37 */   public static Color BACKGROUND_ITEM = new Color(240, 240, 240);
/*  38 */   public static Color LINE = new Color(204, 204, 204);
/*     */   
/*     */   public ModpackPopup() {
/*  41 */     setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, LINE));
/*     */   }
/*     */   
/*     */   public ModpackPopup(final ModpackComboBox localmodpacks) {
/*  45 */     this();
/*  46 */     ModpackMenuItem notFound = new ModpackMenuItem("modpack.backup.not.found");
/*  47 */     ModpackMenuItem hand = new ModpackMenuItem("modpack.backup.install.hand");
/*  48 */     ModpackMenuItem backup = new ModpackMenuItem("modpack.backup.settings");
/*  49 */     add((JMenuItem)backup);
/*  50 */     add((JMenuItem)notFound);
/*  51 */     add((JMenuItem)hand);
/*     */     
/*  53 */     setPreferredSize(new Dimension((getPreferredSize()).width, 97));
/*     */     
/*  55 */     notFound.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/*  59 */             AddedModpackStuffFrame addedModpackStuffView = new AddedModpackStuffFrame((JFrame)TLauncher.getInstance().getFrame(), "modpack.added.request", "modpack.added.request.message");
/*  60 */             addedModpackStuffView.setVisible(true);
/*     */           }
/*     */         });
/*  63 */     hand.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/*  67 */             if (localmodpacks.getSelectedIndex() == 0) {
/*  68 */               Alert.showLocMessage("modpack.select.modpack");
/*     */               
/*     */               return;
/*     */             } 
/*  72 */             HandleInstallModpackElementFrame frame = new HandleInstallModpackElementFrame((JFrame)TLauncher.getInstance().getFrame(), (CompleteVersion)localmodpacks.getSelectedValue());
/*  73 */             frame.setVisible(true);
/*     */           }
/*     */         });
/*     */     
/*  77 */     backup.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/*  81 */             ModpackBackupFrame frame = new ModpackBackupFrame((JFrame)TLauncher.getInstance().getFrame(), localmodpacks);
/*  82 */             frame.setVisible(true);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/*  90 */     Rectangle rec = getVisibleRect();
/*  91 */     g.setColor(LINE);
/*  92 */     g.fillRect(rec.x, rec.y, rec.width, rec.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ModpackMenuItem
/*     */     extends LocalizableMenuItem
/*     */   {
/*     */     private static final long serialVersionUID = 371263248187501160L;
/*     */     
/* 101 */     int item_gup = 20;
/*     */     
/*     */     public ModpackMenuItem(String name) {
/* 104 */       super(name);
/* 105 */       setBackground(ModpackPopup.BACKGROUND_ITEM);
/* 106 */       SwingUtil.changeFontFamily((JComponent)this, FontTL.ROBOTO_REGULAR, 12, ColorUtil.COLOR_16);
/* 107 */       setUI(new BasicMenuItemUI()
/*     */           {
/*     */             protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int gup)
/*     */             {
/* 111 */               Rectangle rec = c.getVisibleRect();
/* 112 */               ButtonModel model = ModpackPopup.ModpackMenuItem.this.getModel();
/* 113 */               if (model.isPressed()) {
/* 114 */                 g.setColor(ColorUtil.COLOR_195);
/* 115 */               } else if (model.isArmed()) {
/* 116 */                 g.setColor(ColorUtil.COLOR_215);
/*     */               } else {
/* 118 */                 g.setColor(ModpackPopup.ModpackMenuItem.this.getBackground());
/*     */               } 
/*     */               
/* 121 */               g.fillRect(rec.x, rec.y, rec.width, rec.height);
/*     */               
/* 123 */               g.setColor(ModpackPopup.LINE);
/* 124 */               ModpackPopup.ModpackMenuItem.this.paintBorder(g);
/* 125 */               paintText(g, c, rec, ModpackPopup.ModpackMenuItem.this.getText());
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
/* 131 */               Graphics2D g2d = (Graphics2D)g;
/* 132 */               g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 133 */               FontMetrics fm = g2d.getFontMetrics();
/* 134 */               Rectangle2D r = fm.getStringBounds(text, g2d);
/* 135 */               g.setFont(ModpackPopup.ModpackMenuItem.this.getFont());
/* 136 */               g.setColor(Color.BLACK);
/* 137 */               int x = ModpackPopup.ModpackMenuItem.this.item_gup;
/* 138 */               int y = (textRect.height - (int)r.getHeight()) / 2 + fm.getAscent();
/* 139 */               g2d.drawString(text, x, y);
/* 140 */               g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void paint(Graphics g) {
/* 148 */       this.ui.paint(g, (JComponent)this);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void paintBorder(Graphics g) {
/* 153 */       Rectangle rec = getVisibleRect();
/* 154 */       g.setColor(ModpackPopup.LINE);
/* 155 */       g.drawLine(rec.x, rec.y + rec.height - 1, rec.x + rec.width, rec.y + rec.height - 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\menu\ModpackPopup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */