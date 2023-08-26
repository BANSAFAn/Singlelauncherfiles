/*     */ package org.tlauncher.tlauncher.ui.settings;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorIntegerField;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.OS;
/*     */ 
/*     */ public class SettingsMemorySlider extends BorderPanel implements EditorField {
/*  30 */   public static final Color HINT_BACKGROUND_COLOR = new Color(113, 113, 113);
/*     */   
/*     */   private final JSlider slider;
/*     */   private final EditorIntegerField inputField;
/*     */   private final LocalizableLabel mb;
/*     */   private Configuration c;
/*     */   private JLabel question;
/*     */   
/*     */   SettingsMemorySlider() {
/*  39 */     SpringLayout springLayout = new SpringLayout();
/*  40 */     setLayout(springLayout);
/*  41 */     ExtendedPanel extendedPanel = new ExtendedPanel();
/*  42 */     springLayout.putConstraint("North", (Component)extendedPanel, 50, "North", (Component)this);
/*  43 */     springLayout.putConstraint("West", (Component)extendedPanel, 0, "East", (Component)this);
/*  44 */     springLayout.putConstraint("South", (Component)extendedPanel, -236, "South", (Component)this);
/*  45 */     springLayout.putConstraint("East", (Component)extendedPanel, 0, "East", (Component)this);
/*  46 */     add((Component)extendedPanel);
/*     */     
/*  48 */     this.slider = new JSlider();
/*  49 */     this.slider.setOpaque(false);
/*  50 */     this.slider.setMinimum(512);
/*  51 */     this.slider.setMaximum(OS.Arch.MAX_MEMORY);
/*  52 */     int tick = (OS.Arch.MAX_MEMORY - 512) / 5;
/*  53 */     if (tick == 0) {
/*  54 */       this.slider.setMajorTickSpacing(1);
/*     */     } else {
/*  56 */       this.slider.setMajorTickSpacing(tick);
/*  57 */     }  this.slider.setSnapToTicks(true);
/*  58 */     this.slider.setPaintLabels(true);
/*  59 */     this.slider.setUI((SliderUI)new SettingsSliderUI(this.slider));
/*  60 */     this.slider.setPreferredSize(new Dimension(336, 35));
/*     */     
/*  62 */     this.inputField = new EditorIntegerField();
/*  63 */     this.inputField.setColumns(5);
/*     */     
/*  65 */     this.mb = new LocalizableLabel("settings.java.memory.mb");
/*     */ 
/*     */     
/*  68 */     SpringLayout spring = new SpringLayout();
/*  69 */     extendedPanel.setLayout(spring);
/*  70 */     JLayeredPane sliderPanel = new JLayeredPane();
/*  71 */     spring.putConstraint("North", sliderPanel, 0, "North", (Component)extendedPanel);
/*  72 */     spring.putConstraint("West", sliderPanel, 0, "West", (Component)extendedPanel);
/*  73 */     spring.putConstraint("East", sliderPanel, -70, "East", (Component)extendedPanel);
/*  74 */     spring.putConstraint("South", sliderPanel, 0, "South", (Component)extendedPanel);
/*  75 */     extendedPanel.add(sliderPanel);
/*     */     
/*  77 */     spring.putConstraint("North", (Component)this.inputField, 0, "North", (Component)extendedPanel);
/*  78 */     spring.putConstraint("West", (Component)this.inputField, 2, "East", sliderPanel);
/*  79 */     spring.putConstraint("East", (Component)this.inputField, 45, "East", sliderPanel);
/*  80 */     extendedPanel.add((Component)this.inputField);
/*     */     
/*  82 */     spring.putConstraint("North", (Component)this.mb, 3, "North", (Component)extendedPanel);
/*  83 */     spring.putConstraint("West", (Component)this.mb, 1, "East", (Component)this.inputField);
/*  84 */     spring.putConstraint("East", (Component)this.mb, 0, "East", (Component)extendedPanel);
/*  85 */     extendedPanel.add((Component)this.mb);
/*     */     
/*  87 */     sliderPanel.add(this.slider, 0);
/*  88 */     this.slider.setBounds(0, 0, 336, 35);
/*  89 */     this.c = TLauncher.getInstance().getConfiguration();
/*     */ 
/*     */     
/*  92 */     this.question = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
/*  93 */     this.question.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mousePressed(MouseEvent e) {
/*  96 */             Alert.showLocMessage(SettingsMemorySlider.this.c.get("memory.problem.message"));
/*     */           }
/*     */         });
/*  99 */     sliderPanel.add(this.question, 1);
/* 100 */     this.question.setBounds(330, 0, 20, 20);
/*     */     
/* 102 */     this.inputField.getDocument().addDocumentListener(new DocumentListener()
/*     */         {
/*     */           public void insertUpdate(DocumentEvent e)
/*     */           {
/* 106 */             SettingsMemorySlider.this.updateInfo();
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void removeUpdate(DocumentEvent e) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void changedUpdate(DocumentEvent e) {}
/*     */         });
/* 118 */     if (!TLauncher.getInstance().getConfiguration().getBoolean("settings.tip.close")) {
/* 119 */       addHint((JPanel)extendedPanel, spring);
/*     */     }
/*     */     
/* 122 */     this.slider.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseReleased(MouseEvent e) {
/* 125 */             SettingsMemorySlider.this.requestFocusInWindow();
/*     */           }
/*     */         });
/*     */     
/* 129 */     this.slider.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseReleased(MouseEvent e) {
/* 132 */             SettingsMemorySlider.this.onSliderUpdate();
/*     */           }
/*     */         });
/*     */     
/* 136 */     this.slider.addKeyListener(new KeyAdapter()
/*     */         {
/*     */           public void keyReleased(KeyEvent e) {
/* 139 */             SettingsMemorySlider.this.onSliderUpdate();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisible(boolean aFlag) {
/* 147 */     super.setVisible(aFlag);
/*     */   }
/*     */   
/*     */   private void addHint(JPanel panel_1, SpringLayout sl_panel_1) {
/* 151 */     int ARC_SIZE = 10;
/* 152 */     final JLabel crossTipButton = new JLabel() {
/* 153 */         BufferedImage image = ImageCache.getImage("close-cross.png");
/*     */ 
/*     */         
/*     */         protected void paintComponent(Graphics g) {
/* 157 */           paint(g);
/*     */         }
/*     */ 
/*     */         
/*     */         public void paint(Graphics g0) {
/* 162 */           ((Graphics2D)g0).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/* 163 */           Rectangle rec = getVisibleRect();
/* 164 */           g0.setColor(SettingsScene.BACKGROUND);
/* 165 */           g0.fillRect(rec.x, rec.y, rec.width, rec.height);
/* 166 */           g0.setColor(getBackground());
/* 167 */           g0.fillRect(rec.x, rec.y, rec.width - 10, rec.height);
/*     */           
/* 169 */           g0.fillRoundRect(rec.x, rec.y, rec.width, rec.height, 10, 10);
/*     */           
/* 171 */           JComponent component = this;
/* 172 */           paintPicture(g0, component, rec);
/* 173 */           ((Graphics2D)g0).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*     */         }
/*     */ 
/*     */         
/*     */         protected void paintPicture(Graphics g, JComponent c, Rectangle rect) {
/* 178 */           Graphics2D g2d = (Graphics2D)g;
/*     */           
/* 180 */           int x = (getWidth() - this.image.getWidth(null)) / 2;
/* 181 */           int y = (getHeight() - this.image.getHeight(null)) / 2;
/*     */           
/* 183 */           g2d.drawImage(this.image, x, y, (ImageObserver)null);
/*     */         }
/*     */       };
/*     */     
/* 187 */     crossTipButton.setOpaque(true);
/* 188 */     crossTipButton.setBackground(HINT_BACKGROUND_COLOR);
/* 189 */     crossTipButton.setPreferredSize(new Dimension(32, 30));
/* 190 */     crossTipButton.setSize(new Dimension(32, 30));
/* 191 */     crossTipButton.setPreferredSize(new Dimension(32, 30));
/* 192 */     LocalizableLabel hint = new LocalizableLabel("settings.warning");
/* 193 */     final ExtendedPanel extendedPanel = new ExtendedPanel(new BorderLayout(10, 0))
/*     */       {
/*     */         public void paintComponent(Graphics g0)
/*     */         {
/* 197 */           Graphics2D g = (Graphics2D)g0;
/*     */           
/* 199 */           int x = 0;
/*     */           
/* 201 */           g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */           
/* 203 */           g.setColor(SettingsMemorySlider.HINT_BACKGROUND_COLOR);
/* 204 */           g.fillRoundRect(x, x, getWidth(), getHeight(), 10, 10);
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
/* 217 */           g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
/*     */         }
/*     */       };
/* 220 */     extendedPanel.setInsets(0, 5, 0, 0);
/* 221 */     sl_panel_1.putConstraint("East", (Component)extendedPanel, -20, "East", this.slider);
/* 222 */     sl_panel_1.putConstraint("North", (Component)extendedPanel, 9, "South", this.slider);
/* 223 */     sl_panel_1.putConstraint("South", (Component)extendedPanel, 40, "South", this.slider);
/*     */     
/* 225 */     hint.setVerticalTextPosition(0);
/* 226 */     hint.setHorizontalAlignment(0);
/* 227 */     hint.setVerticalAlignment(0);
/* 228 */     hint.setForeground(new Color(212, 212, 212));
/* 229 */     hint.setFont(hint.getFont().deriveFont(12.0F));
/* 230 */     extendedPanel.setOpaque(true);
/* 231 */     extendedPanel.add((Component)hint, "Center");
/* 232 */     extendedPanel.add(crossTipButton, "East");
/* 233 */     panel_1.add((Component)extendedPanel);
/* 234 */     crossTipButton.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 236 */             if (!SwingUtilities.isLeftMouseButton(e)) {
/*     */               return;
/*     */             }
/* 239 */             SwingUtilities.invokeLater(() -> extendedPanel.setVisible(false));
/* 240 */             TLauncher.getInstance().getConfiguration().set("settings.tip.close", Boolean.valueOf(true));
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void mouseEntered(MouseEvent e) {
/* 246 */             crossTipButton.setBackground(new Color(124, 124, 124));
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/* 251 */             crossTipButton.setBackground(SettingsMemorySlider.HINT_BACKGROUND_COLOR);
/*     */           }
/*     */         });
/*     */     
/* 255 */     sl_panel_1.putConstraint("West", (Component)extendedPanel, 0, "West", panel_1);
/* 256 */     extendedPanel.setBackground(HINT_BACKGROUND_COLOR);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBackground(Color color) {
/* 261 */     if (this.inputField != null) {
/* 262 */       this.inputField.setBackground(color);
/*     */     }
/*     */   }
/*     */   
/*     */   public void block(Object reason) {
/* 267 */     Blocker.blockComponents(reason, new Component[] { this.slider, (Component)this.inputField });
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 272 */     Blocker.unblockComponents(reason, new Component[] { this.slider, (Component)this.inputField });
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSettingsValue() {
/* 277 */     return this.inputField.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSettingsValue(String value) {
/* 282 */     this.inputField.setValue(value);
/* 283 */     updateInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValueValid() {
/* 288 */     return (this.inputField.getIntegerValue() >= 512 && this.inputField.getIntegerValue() <= OS.Arch.MAX_MEMORY);
/*     */   }
/*     */ 
/*     */   
/*     */   private void onSliderUpdate() {
/* 293 */     this.inputField.setValue(Integer.valueOf(this.slider.getValue()));
/* 294 */     updateTip();
/*     */   }
/*     */   
/*     */   private void updateSlider() {
/* 298 */     int intVal = this.inputField.getIntegerValue();
/*     */     
/* 300 */     if (intVal > 1) {
/* 301 */       this.slider.setValue(intVal);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateTip() {}
/*     */   
/*     */   private void updateInfo() {
/* 309 */     updateSlider();
/* 310 */     updateTip();
/*     */   }
/*     */   
/*     */   public void initMemoryQuestion() {
/* 314 */     if (!this.c.isExist("memory.problem.message"))
/* 315 */       this.question.setVisible(false); 
/* 316 */     this.question.repaint();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\settings\SettingsMemorySlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */