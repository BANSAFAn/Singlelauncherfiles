/*     */ package org.tlauncher.tlauncher.ui.settings;
/*     */ 
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ComboBoxUI;
/*     */ import javax.swing.plaf.ScrollBarUI;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.configuration.LangConfiguration;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
/*     */ import org.tlauncher.tlauncher.configuration.enums.BackupFrequency;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
/*     */ import org.tlauncher.tlauncher.controller.StatisticsController;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.converter.ActionOnLaunchConverter;
/*     */ import org.tlauncher.tlauncher.ui.converter.BackupFrequencyConverter;
/*     */ import org.tlauncher.tlauncher.ui.converter.ConnectionQualityConverter;
/*     */ import org.tlauncher.tlauncher.ui.converter.ConsoleTypeConverter;
/*     */ import org.tlauncher.tlauncher.ui.converter.LocaleConverter;
/*     */ import org.tlauncher.tlauncher.ui.converter.StringConverter;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorComboBox;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorField;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.HTMLLabel;
/*     */ import org.tlauncher.tlauncher.ui.swing.scroll.VersionScrollBarUI;
/*     */ import org.tlauncher.tlauncher.ui.ui.TlauncherBasicComboBoxUI;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfidentialitySetting
/*     */   extends PageSettings
/*     */ {
/*     */   private static final long serialVersionUID = -2705671690811623008L;
/*  68 */   public final TLauncher tlauncher = TLauncher.getInstance();
/*  69 */   public final Configuration global = this.tlauncher.getConfiguration();
/*  70 */   public final LangConfiguration lang = this.tlauncher.getLang(); private final EditorComboBox<Locale> local; public ConfidentialitySetting() {
/*  71 */     SpringLayout springLayout = new SpringLayout();
/*  72 */     setLayout(springLayout);
/*     */     
/*  74 */     final Color backgroundOldButtonColor = new Color(219, 64, 44);
/*  75 */     final UpdaterButton errorButton = new UpdaterButton(backgroundOldButtonColor, "settings.delete");
/*  76 */     final StatisticsController statisticsController = (StatisticsController)TLauncher.getInjector().getInstance(StatisticsController.class);
/*     */ 
/*     */     
/*  79 */     EditorComboBox<ConsoleType> consoleConverter = new EditorComboBox((StringConverter)new ConsoleTypeConverter(), (Object[])ConsoleType.values());
/*  80 */     EditorCheckBox statistics = new EditorCheckBox("statistics.settings.checkbox.name");
/*  81 */     EditorCheckBox guard = new EditorCheckBox("settings.guard");
/*  82 */     EditorCheckBox recommendedServers = new EditorCheckBox("settings.servers.recommendation");
/*  83 */     this.documentText = new HTMLLabel();
/*  84 */     this.documentText.setHorizontalAlignment(0);
/*  85 */     JLabel questionLabel = new JLabel(ImageCache.getNativeIcon("qestion-option-panel.png"));
/*     */     
/*  87 */     EditorComboBox<ConnectionQuality> connQuality = new EditorComboBox((StringConverter)new ConnectionQualityConverter(), (Object[])ConnectionQuality.values());
/*     */     
/*  89 */     EditorComboBox<ActionOnLaunch> launchAction = new EditorComboBox((StringConverter)new ActionOnLaunchConverter(), (Object[])ActionOnLaunch.values());
/*     */     
/*  91 */     EditorComboBox<BackupFrequency> backupFrequency = new EditorComboBox((StringConverter)new BackupFrequencyConverter(), (Object[])BackupFrequency.values());
/*  92 */     this.local = new EditorComboBox((StringConverter)new LocaleConverter(), (Object[])this.global.getLocales());
/*  93 */     setTLauncherBasicComboBoxUI((JComboBox<ConsoleType>)consoleConverter);
/*  94 */     setTLauncherBasicComboBoxUI((JComboBox<ConnectionQuality>)connQuality);
/*  95 */     setTLauncherBasicComboBoxUI((JComboBox<ActionOnLaunch>)launchAction);
/*  96 */     setTLauncherBasicComboBoxUI((JComboBox<Locale>)this.local);
/*  97 */     setTLauncherBasicComboBoxUI((JComboBox<BackupFrequency>)backupFrequency);
/*     */     
/*  99 */     SettingElement settingElement_2 = new SettingElement("statistics.settings.title", (JComponent)statistics, 17, -1);
/* 100 */     springLayout.putConstraint("North", (Component)settingElement_2, 0, "North", (Component)this);
/* 101 */     springLayout.putConstraint("West", (Component)settingElement_2, 0, "West", (Component)this);
/* 102 */     springLayout.putConstraint("South", (Component)settingElement_2, 21, "North", (Component)this);
/* 103 */     springLayout.putConstraint("East", (Component)settingElement_2, 0, "East", (Component)this);
/* 104 */     add((Component)settingElement_2);
/*     */     
/* 106 */     SettingElement settingElement_guard = new SettingElement("settings.guard.title", (JComponent)guard, 19, -1, questionLabel);
/*     */     
/* 108 */     questionLabel.setBounds(0, 0, 20, 19);
/* 109 */     springLayout.putConstraint("North", (Component)settingElement_guard, 15, "South", (Component)settingElement_2);
/* 110 */     springLayout.putConstraint("West", (Component)settingElement_guard, 0, "West", (Component)this);
/* 111 */     springLayout.putConstraint("East", (Component)settingElement_guard, 0, "East", (Component)this);
/* 112 */     springLayout.putConstraint("South", (Component)settingElement_guard, 38, "South", (Component)settingElement_2);
/* 113 */     add((Component)settingElement_guard);
/*     */     
/* 115 */     this.html = HtmlTextPane.createNew(Localizable.get("settings.servers.analytics"), 400);
/*     */     
/* 117 */     SettingElement settingElement_servers = new SettingElement("settings.servers.recommendation.title", (JComponent)recommendedServers, 19, -1);
/*     */     
/* 119 */     springLayout.putConstraint("North", (Component)settingElement_servers, 15, "South", (Component)settingElement_guard);
/*     */     
/* 121 */     springLayout.putConstraint("West", (Component)settingElement_servers, 0, "West", (Component)this);
/* 122 */     springLayout.putConstraint("East", (Component)settingElement_servers, 0, "East", (Component)this);
/* 123 */     springLayout.putConstraint("South", (Component)settingElement_servers, 50, "South", (Component)settingElement_guard);
/*     */     
/* 125 */     add((Component)settingElement_servers);
/*     */     
/* 127 */     SettingElement settingElement_analytics = new SettingElement("settings.servers.analytics.title", (JComponent)this.html, 19, -1, 1);
/*     */ 
/*     */     
/* 130 */     springLayout.putConstraint("North", (Component)settingElement_analytics, 15, "South", (Component)settingElement_servers);
/*     */     
/* 132 */     springLayout.putConstraint("West", (Component)settingElement_analytics, 0, "West", (Component)this);
/* 133 */     springLayout.putConstraint("East", (Component)settingElement_analytics, 0, "East", (Component)this);
/* 134 */     springLayout.putConstraint("South", (Component)settingElement_analytics, 180, "South", (Component)settingElement_servers);
/*     */     
/* 136 */     add((Component)settingElement_analytics);
/*     */     
/* 138 */     SettingElement settingElement_delete_analytics = new SettingElement("", (JComponent)errorButton, 19, -1);
/* 139 */     springLayout.putConstraint("North", (Component)settingElement_delete_analytics, 15, "South", (Component)settingElement_analytics);
/*     */     
/* 141 */     springLayout.putConstraint("West", (Component)settingElement_delete_analytics, 245, "West", (Component)this);
/* 142 */     springLayout.putConstraint("East", (Component)settingElement_delete_analytics, -100, "East", (Component)this);
/* 143 */     springLayout.putConstraint("South", (Component)settingElement_delete_analytics, 40, "South", (Component)settingElement_analytics);
/*     */     
/* 145 */     add((Component)settingElement_delete_analytics);
/*     */     
/* 147 */     SettingElement settingElement_document = new SettingElement("settings.servers.document.title", (JComponent)this.documentText, 19, -1);
/*     */     
/* 149 */     springLayout.putConstraint("North", (Component)settingElement_document, 21, "South", (Component)settingElement_delete_analytics);
/*     */     
/* 151 */     springLayout.putConstraint("West", (Component)settingElement_document, 10, "West", (Component)this);
/* 152 */     springLayout.putConstraint("East", (Component)settingElement_document, -10, "East", (Component)this);
/* 153 */     springLayout.putConstraint("South", (Component)settingElement_document, 35, "South", (Component)settingElement_delete_analytics);
/*     */     
/* 155 */     add((Component)settingElement_document);
/*     */ 
/*     */     
/* 158 */     errorButton.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/* 161 */             errorButton.setBackground(new Color(255, 0, 0));
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/* 166 */             errorButton.setBackground(backgroundOldButtonColor);
/*     */           }
/*     */         });
/* 169 */     errorButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 172 */             CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 179 */               .exceptionally(t -> {
/*     */                   SwingUtilities.invokeLater(());
/*     */                   
/*     */                   U.log(new Object[] { t });
/*     */                   
/*     */                   return null;
/*     */                 });
/*     */           }
/*     */         });
/*     */     
/* 189 */     addHandler(new HandlerSettings("gui.statistics.checkbox", (EditorField)statistics));
/* 190 */     addHandler(new HandlerSettings("gui.settings.guard.checkbox", (EditorField)guard));
/* 191 */     addHandler(new HandlerSettings("gui.settings.servers.recommendation", (EditorField)recommendedServers));
/* 192 */     SwingUtil.changeFontFamily((JComponent)this.documentText, FontTL.ROBOTO_REGULAR, 14);
/* 193 */     SwingUtil.changeFontFamily((JComponent)errorButton, FontTL.ROBOTO_REGULAR, 14, Color.white);
/* 194 */     SwingUtil.changeFontFamily((JComponent)this.html, FontTL.ROBOTO_REGULAR, 14);
/*     */     
/* 196 */     this.documentText.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/* 199 */             if (SwingUtilities.isLeftMouseButton(e)) {
/* 200 */               String url = String.format("https://ad.tlauncher.org/link/privacy-policy-%s", new Object[] {
/* 201 */                     TLauncher.getInstance().getConfiguration().isUSSRLocale() ? "ru" : "en" });
/* 202 */               OS.openLink(url);
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 207 */     questionLabel.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/* 210 */             if (SwingUtilities.isLeftMouseButton(e)) {
/* 211 */               String url = String.format("https://tlauncher.org/%s/guard.html", new Object[] {
/* 212 */                     TLauncher.getInstance().getConfiguration().isUSSRLocale() ? "ru" : "en" });
/* 213 */               OS.openLink(url);
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 218 */     ActionListener l = e -> {
/*     */         if (this.tlauncher.getProfileManager().isNotPremium()) {
/*     */           ((JCheckBox)e.getSource()).setSelected(true);
/*     */           
/*     */           Alert.showHtmlMessage("", Localizable.get("account.premium.not.available"), 1, 400);
/*     */         } 
/*     */       };
/* 225 */     recommendedServers.addActionListener(l);
/* 226 */     guard.addActionListener(l);
/*     */   }
/*     */   private HTMLLabel documentText; HtmlTextPane html;
/*     */   public boolean chooseChinaLocal() {
/* 230 */     if (Objects.isNull(this.global.getLocale()))
/* 231 */       return false; 
/* 232 */     return (((Locale)this.local.getSelectedValue()).getLanguage().equals((new Locale("zh")).getLanguage()) || this.global
/* 233 */       .getLocale().getLanguage().equals((new Locale("zh")).getLanguage()));
/*     */   }
/*     */   
/*     */   private static <T> void setTLauncherBasicComboBoxUI(JComboBox<T> comboBox) {
/* 237 */     comboBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(149, 149, 149)));
/* 238 */     comboBox.setUI((ComboBoxUI)new TlauncherBasicComboBoxUI()
/*     */         {
/*     */           
/*     */           protected ComboPopup createPopup()
/*     */           {
/* 243 */             BasicComboPopup basic = new BasicComboPopup(this.comboBox)
/*     */               {
/*     */                 protected JScrollPane createScroller() {
/* 246 */                   VersionScrollBarUI barUI = new VersionScrollBarUI()
/*     */                     {
/*     */                       protected Dimension getMinimumThumbSize() {
/* 249 */                         return new Dimension(10, 40);
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public Dimension getMaximumSize(JComponent c) {
/* 254 */                         Dimension dim = super.getMaximumSize(c);
/* 255 */                         dim.setSize(10.0D, dim.getHeight());
/* 256 */                         return dim;
/*     */                       }
/*     */ 
/*     */                       
/*     */                       public Dimension getPreferredSize(JComponent c) {
/* 261 */                         Dimension dim = super.getPreferredSize(c);
/* 262 */                         dim.setSize(13.0D, dim.getHeight());
/* 263 */                         return dim;
/*     */                       }
/*     */                     };
/* 266 */                   barUI.setGapThubm(5);
/*     */                   
/* 268 */                   JScrollPane scroller = new JScrollPane(this.list, 20, 31);
/*     */                   
/* 270 */                   scroller.getVerticalScrollBar().setUI((ScrollBarUI)barUI);
/* 271 */                   return scroller;
/*     */                 }
/*     */               };
/* 274 */             basic.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.GRAY));
/* 275 */             return basic;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/* 282 */     super.init();
/* 283 */     this.documentText.setText(Localizable.get("settings.servers.document"));
/* 284 */     this.html.setText(Localizable.get("settings.servers.analytics"));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\settings\ConfidentialitySetting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */