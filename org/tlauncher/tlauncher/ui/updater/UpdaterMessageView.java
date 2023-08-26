/*     */ package org.tlauncher.tlauncher.ui.updater;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.SpringLayout;
/*     */ import org.tlauncher.tlauncher.controller.UpdaterFormController;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageIcon;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.RoundUpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
/*     */ import org.tlauncher.tlauncher.ui.swing.ImagePanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.OwnImageCheckBox;
/*     */ import org.tlauncher.tlauncher.updater.client.Banner;
/*     */ import org.tlauncher.tlauncher.updater.client.Offer;
/*     */ import org.tlauncher.tlauncher.updater.client.PointOffer;
/*     */ import org.tlauncher.tlauncher.updater.client.Update;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ 
/*     */ public class UpdaterMessageView
/*     */   extends JPanel
/*     */ {
/*     */   private static final long serialVersionUID = -9173760470917959755L;
/*  44 */   private static final Dimension SIZE = new Dimension(900, 600); private final UpdaterButton ok; private UpdaterButton updater; private UpdaterButton laterUpdater; public UpdaterMessageView(Update update, int messageType, String lang, boolean isAdmin) { String image;
/*  45 */     this.ok = (UpdaterButton)new RoundUpdaterButton(Color.WHITE, new Color(107, 202, 45), new Color(91, 174, 37), "launcher.update.updater.button");
/*  46 */     this.updater = (UpdaterButton)new RoundUpdaterButton(Color.WHITE, new Color(107, 202, 45), new Color(91, 174, 37), "launcher.update.updater.button");
/*  47 */     this.laterUpdater = (UpdaterButton)new RoundUpdaterButton(Color.WHITE, new Color(235, 132, 46), new Color(200, 112, 38), "launcher.update.later.button");
/*  48 */     this.down = new JPanel();
/*     */ 
/*     */     
/*  51 */     this.checkBoxList = new ArrayList<>();
/*     */ 
/*     */ 
/*     */     
/*  55 */     setPreferredSize(SIZE);
/*     */     
/*  57 */     if (messageType == 2)
/*  58 */     { image = "offer.png"; }
/*  59 */     else if (messageType == 1)
/*  60 */     { image = "banner.png"; }
/*  61 */     else { image = "without-banner-offer.png"; }
/*     */     
/*  63 */     this.imageTop = new ImagePanel(image, 1.0F, 1.0F, true);
/*  64 */     JLabel tlauncher = new JLabel("TLAUNCHER " + update.getVersion());
/*  65 */     JScrollPane message = HtmlTextPane.createNewAndWrap(Localizable.get(update.isMandatory() ? "launcher.update.message.mandatory" : "launcher.update.message.optional"), 246);
/*     */     
/*  67 */     JScrollPane changes = HtmlTextPane.createNewAndWrap(update.getDescription(), 246);
/*  68 */     JScrollPane notice = HtmlTextPane.createNewAndWrap(Localizable.get("updater.notice"), 700);
/*     */     
/*  70 */     SpringLayout spring = new SpringLayout();
/*  71 */     SpringLayout topSpring = new SpringLayout();
/*  72 */     SpringLayout downSpring = new SpringLayout();
/*  73 */     setLayout(spring);
/*  74 */     this.imageTop.setLayout(topSpring);
/*  75 */     this.down.setLayout(downSpring);
/*     */     
/*  77 */     spring.putConstraint("West", (Component)this.imageTop, 0, "West", this);
/*  78 */     spring.putConstraint("East", (Component)this.imageTop, 0, "East", this);
/*  79 */     spring.putConstraint("North", (Component)this.imageTop, 0, "North", this);
/*  80 */     spring.putConstraint("South", (Component)this.imageTop, 453, "North", this);
/*  81 */     add((Component)this.imageTop);
/*     */     
/*  83 */     spring.putConstraint("West", this.down, 0, "West", this);
/*  84 */     spring.putConstraint("East", this.down, 0, "East", this);
/*  85 */     spring.putConstraint("North", this.down, 453, "North", this);
/*  86 */     spring.putConstraint("South", this.down, 600, "North", this);
/*  87 */     add(this.down);
/*     */ 
/*     */     
/*  90 */     topSpring.putConstraint("West", tlauncher, 40, "West", (Component)this.imageTop);
/*  91 */     topSpring.putConstraint("East", tlauncher, 306, "West", (Component)this.imageTop);
/*  92 */     topSpring.putConstraint("North", tlauncher, 37, "North", (Component)this.imageTop);
/*  93 */     topSpring.putConstraint("South", tlauncher, 67, "North", (Component)this.imageTop);
/*  94 */     this.imageTop.add(tlauncher);
/*     */     
/*  96 */     topSpring.putConstraint("West", message, 40, "West", (Component)this.imageTop);
/*  97 */     topSpring.putConstraint("East", message, 286, "West", (Component)this.imageTop);
/*  98 */     topSpring.putConstraint("North", message, 60, "North", (Component)this.imageTop);
/*  99 */     topSpring.putConstraint("South", message, 160, "North", (Component)this.imageTop);
/* 100 */     this.imageTop.add(message);
/*     */     
/* 102 */     topSpring.putConstraint("West", changes, 40, "West", (Component)this.imageTop);
/* 103 */     topSpring.putConstraint("East", changes, 286, "West", (Component)this.imageTop);
/* 104 */     topSpring.putConstraint("North", changes, 144, "North", (Component)this.imageTop);
/* 105 */     topSpring.putConstraint("South", changes, -40, "South", (Component)this.imageTop);
/* 106 */     this.imageTop.add(changes);
/*     */     
/* 108 */     downSpring.putConstraint("West", notice, 40, "West", this.down);
/* 109 */     downSpring.putConstraint("East", notice, 0, "East", this.down);
/* 110 */     downSpring.putConstraint("North", notice, 5, "North", this.down);
/* 111 */     downSpring.putConstraint("South", notice, 65, "North", this.down);
/* 112 */     this.down.add(notice);
/* 113 */     if (update.isMandatory()) {
/* 114 */       downSpring.putConstraint("West", (Component)this.ok, 365, "West", this.down);
/* 115 */       downSpring.putConstraint("East", (Component)this.ok, 535, "West", this.down);
/* 116 */       downSpring.putConstraint("North", (Component)this.ok, 86, "North", this.down);
/* 117 */       downSpring.putConstraint("South", (Component)this.ok, 123, "North", this.down);
/* 118 */       this.down.add((Component)this.ok);
/*     */     } else {
/* 120 */       downSpring.putConstraint("West", (Component)this.updater, 273, "West", this.down);
/* 121 */       downSpring.putConstraint("East", (Component)this.updater, 443, "West", this.down);
/* 122 */       downSpring.putConstraint("North", (Component)this.updater, 86, "North", this.down);
/* 123 */       downSpring.putConstraint("South", (Component)this.updater, 123, "North", this.down);
/* 124 */       this.down.add((Component)this.updater);
/*     */       
/* 126 */       downSpring.putConstraint("West", (Component)this.laterUpdater, 454, "West", this.down);
/* 127 */       downSpring.putConstraint("East", (Component)this.laterUpdater, 624, "West", this.down);
/* 128 */       downSpring.putConstraint("North", (Component)this.laterUpdater, 86, "North", this.down);
/* 129 */       downSpring.putConstraint("South", (Component)this.laterUpdater, 123, "North", this.down);
/* 130 */       this.down.add((Component)this.laterUpdater);
/*     */     } 
/*     */ 
/*     */     
/* 134 */     if (messageType == 1) {
/* 135 */       final Banner banner = ((List<Banner>)update.getBanners().get(lang)).get(0);
/* 136 */       JLabel imagePanel = null;
/*     */       try {
/* 138 */         imagePanel = new JLabel((Icon)new ImageIcon(ImageCache.loadImage(new URL(banner.getImage()))));
/* 139 */         imagePanel.addMouseListener(new MouseAdapter()
/*     */             {
/*     */               public void mouseClicked(MouseEvent e) {
/* 142 */                 OS.openLink(banner.getClickLink());
/*     */               }
/*     */             });
/* 145 */       } catch (MalformedURLException malformedURLException) {}
/*     */       
/* 147 */       topSpring.putConstraint("West", imagePanel, 326, "West", (Component)this.imageTop);
/* 148 */       topSpring.putConstraint("East", imagePanel, 0, "East", (Component)this.imageTop);
/* 149 */       topSpring.putConstraint("North", imagePanel, 0, "North", (Component)this.imageTop);
/* 150 */       topSpring.putConstraint("South", imagePanel, 453, "North", (Component)this.imageTop);
/* 151 */       this.imageTop.add(imagePanel);
/*     */     }
/* 153 */     else if (messageType == 2) {
/* 154 */       Offer offer = update.getSelectedOffer();
/* 155 */       JScrollPane pane = HtmlTextPane.createNewAndWrap((String)offer.getTopText().get(lang), 574);
/* 156 */       topSpring.putConstraint("West", pane, 326, "West", (Component)this.imageTop);
/* 157 */       topSpring.putConstraint("East", pane, 0, "East", (Component)this.imageTop);
/* 158 */       topSpring.putConstraint("North", pane, 0, "North", (Component)this.imageTop);
/* 159 */       topSpring.putConstraint("South", pane, offer.getStartCheckboxSouth(), "North", (Component)this.imageTop);
/* 160 */       this.imageTop.add(pane);
/* 161 */       int start = offer.getStartCheckboxSouth();
/* 162 */       for (PointOffer p : offer.getCheckBoxes()) {
/* 163 */         OwnImageCheckBox ownImageCheckBox = new OwnImageCheckBox((String)p.getTexts().get(lang), "updater-checkbox-on.png", "updater-checkbox-off.png");
/* 164 */         if (isAdmin)
/* 165 */         { ownImageCheckBox.setSelected(p.isActive()); }
/* 166 */         else { ownImageCheckBox.setSelected(false); }
/* 167 */          ownImageCheckBox.setIconTextGap(18);
/* 168 */         ownImageCheckBox.setActionCommand(p.getName());
/* 169 */         ownImageCheckBox.setVerticalAlignment(0);
/* 170 */         SwingUtil.changeFontFamily((JComponent)ownImageCheckBox, FontTL.CALIBRI, 16);
/* 171 */         topSpring.putConstraint("West", (Component)ownImageCheckBox, 378, "West", (Component)this.imageTop);
/* 172 */         topSpring.putConstraint("East", (Component)ownImageCheckBox, 0, "East", (Component)this.imageTop);
/* 173 */         topSpring.putConstraint("North", (Component)ownImageCheckBox, start, "North", (Component)this.imageTop);
/* 174 */         topSpring.putConstraint("South", (Component)ownImageCheckBox, start + 39, "North", (Component)this.imageTop);
/* 175 */         start += 39;
/* 176 */         this.checkBoxList.add(ownImageCheckBox);
/* 177 */         this.imageTop.add((Component)ownImageCheckBox);
/*     */       } 
/* 179 */       JScrollPane downDescription = HtmlTextPane.createNewAndWrap((String)offer.getDownText().get(lang), 574);
/* 180 */       topSpring.putConstraint("West", downDescription, 328, "West", (Component)this.imageTop);
/* 181 */       topSpring.putConstraint("East", downDescription, 0, "East", (Component)this.imageTop);
/* 182 */       topSpring.putConstraint("North", downDescription, 320, "North", (Component)this.imageTop);
/* 183 */       topSpring.putConstraint("South", downDescription, 0, "South", (Component)this.imageTop);
/* 184 */       this.imageTop.add(downDescription);
/*     */     } 
/*     */     
/* 187 */     this.down.setBackground(Color.WHITE);
/*     */     
/* 189 */     SwingUtil.changeFontFamily(tlauncher, FontTL.CALIBRI_BOLD, 30);
/* 190 */     SwingUtil.changeFontFamily((JComponent)this.updater, FontTL.ROBOTO_REGULAR, 13);
/* 191 */     SwingUtil.changeFontFamily((JComponent)this.ok, FontTL.ROBOTO_REGULAR, 13);
/* 192 */     SwingUtil.changeFontFamily((JComponent)this.laterUpdater, FontTL.ROBOTO_REGULAR, 13);
/*     */     
/* 194 */     tlauncher.setHorizontalTextPosition(2);
/*     */     
/* 196 */     this.updater.addActionListener(e -> this.result = 1);
/* 197 */     this.laterUpdater.addActionListener(e -> this.result = 0);
/* 198 */     this.ok.addActionListener(e -> this.result = 1); }
/*     */   
/*     */   private JPanel down; private ImagePanel imageTop; private int result; private List<JCheckBox> checkBoxList;
/*     */   public UpdaterFormController.UserResult showMessage() {
/* 202 */     this.result = -1;
/* 203 */     Alert.showMessage("  " + Localizable.get("launcher.update.title"), this, new JButton[] { (JButton)this.updater, (JButton)this.laterUpdater, (JButton)this.ok });
/* 204 */     UpdaterFormController.UserResult res = new UpdaterFormController.UserResult();
/* 205 */     res.setUserChooser(this.result);
/* 206 */     StringBuilder builder = new StringBuilder();
/* 207 */     for (JCheckBox box : this.checkBoxList) {
/* 208 */       if (box.isSelected()) {
/* 209 */         if (builder.length() > 0)
/* 210 */           builder.append("+"); 
/* 211 */         builder.append(box.getActionCommand());
/* 212 */         res.setSelectedAnyCheckBox(true);
/*     */       } 
/*     */     } 
/* 215 */     res.setOfferArgs(builder.toString());
/* 216 */     return res;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\u\\updater\UpdaterMessageView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */