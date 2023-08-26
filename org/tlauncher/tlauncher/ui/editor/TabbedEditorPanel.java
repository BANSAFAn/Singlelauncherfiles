/*     */ package org.tlauncher.tlauncher.ui.editor;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanelTheme;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.swing.Del;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.TabbedPane;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.VPanel;
/*     */ 
/*     */ public class TabbedEditorPanel extends AbstractEditorPanel {
/*     */   protected final BorderPanel container;
/*     */   
/*     */   public TabbedEditorPanel(CenterPanelTheme theme, Insets insets) {
/*  25 */     super(theme, insets);
/*     */     
/*  27 */     this.tabs = new ArrayList<>();
/*     */     
/*  29 */     this.tabPane = new TabbedPane();
/*  30 */     if (this.tabPane.getExtendedUI() != null) {
/*  31 */       this.tabPane.getExtendedUI().setTheme(getTheme());
/*     */     }
/*  33 */     this.container = new BorderPanel();
/*  34 */     this.container.setNorth((Component)this.messagePanel);
/*  35 */     this.container.setCenter((Component)this.tabPane);
/*     */     
/*  37 */     setLayout(new BorderLayout());
/*  38 */     add((Component)this.container, "Center");
/*     */   }
/*     */   protected final TabbedPane tabPane; protected final List<EditorPanelTab> tabs;
/*     */   public TabbedEditorPanel(CenterPanelTheme theme) {
/*  42 */     this(theme, (Insets)null);
/*     */   }
/*     */   
/*     */   public TabbedEditorPanel(Insets insets) {
/*  46 */     this((CenterPanelTheme)null, insets);
/*     */   }
/*     */   
/*     */   public TabbedEditorPanel() {
/*  50 */     this(smallSquareNoTopInsets);
/*     */   }
/*     */   
/*     */   protected void add(EditorPanelTab tab) {
/*  54 */     if (tab == null) {
/*  55 */       throw new NullPointerException("tab is null");
/*     */     }
/*  57 */     this.tabPane.addTab(tab.getTabName(), tab.getTabIcon(), (Component)tab.getScroll(), tab.getTabTip());
/*  58 */     this.tabs.add(tab);
/*     */   }
/*     */   
/*     */   protected void remove(EditorPanelTab tab) {
/*  62 */     if (tab == null) {
/*  63 */       throw new NullPointerException("tab is null");
/*     */     }
/*  65 */     int index = this.tabs.indexOf(tab);
/*     */     
/*  67 */     if (index != -1) {
/*  68 */       this.tabPane.removeTabAt(index);
/*  69 */       this.tabs.remove(index);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int getTabOf(EditorPair pair) {
/*  74 */     return this.tabPane.indexOfComponent((Component)pair.getPanel());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Del del(int aligment) {
/*     */     Color border;
/*     */     try {
/*  82 */       border = this.tabPane.getExtendedUI().getTheme().getBorder();
/*  83 */     } catch (Exception e) {
/*  84 */       border = getTheme().getBorder();
/*     */     } 
/*     */     
/*  87 */     return new Del(1, aligment, border);
/*     */   }
/*     */   
/*     */   public class EditorScrollPane extends ScrollPane {
/*     */     private final TabbedEditorPanel.EditorPanelTab tab;
/*     */     
/*     */     EditorScrollPane(TabbedEditorPanel.EditorPanelTab tab) {
/*  94 */       super((Component)tab);
/*  95 */       this.tab = tab;
/*     */     }
/*     */     
/*     */     public TabbedEditorPanel.EditorPanelTab getTab() {
/*  99 */       return this.tab;
/*     */     }
/*     */   }
/*     */   
/*     */   public class EditorPanelTab
/*     */     extends ExtendedPanel
/*     */     implements LocalizableComponent {
/*     */     private final String name;
/*     */     private final String tip;
/*     */     private final Icon icon;
/*     */     private final List<ExtendedPanel> panels;
/*     */     private final TabbedEditorPanel.EditorScrollPane scroll;
/*     */     private boolean savingEnabled = true;
/*     */     private final GridBagConstraints c;
/*     */     
/*     */     public EditorPanelTab(String name, String tip, Icon icon) {
/* 115 */       this.c = new GridBagConstraints();
/* 116 */       this.c.fill = 2;
/* 117 */       this.c.gridy = 0;
/* 118 */       if (name == null) {
/* 119 */         throw new NullPointerException();
/*     */       }
/* 121 */       if (name.isEmpty()) {
/* 122 */         throw new IllegalArgumentException("name is empty");
/*     */       }
/* 124 */       this.name = name;
/* 125 */       this.tip = tip;
/* 126 */       this.icon = icon;
/*     */       
/* 128 */       this.panels = new ArrayList<>();
/*     */       
/* 130 */       setLayout(new GridBagLayout());
/*     */       
/* 132 */       this.scroll = new TabbedEditorPanel.EditorScrollPane(this);
/*     */     }
/*     */     
/*     */     public EditorPanelTab(String name) {
/* 136 */       this(name, null, null);
/*     */     }
/*     */     
/*     */     public String getTabName() {
/* 140 */       return Localizable.get(this.name);
/*     */     }
/*     */     
/*     */     public Icon getTabIcon() {
/* 144 */       return this.icon;
/*     */     }
/*     */     
/*     */     public String getTabTip() {
/* 148 */       return Localizable.get(this.tip);
/*     */     }
/*     */     
/*     */     public TabbedEditorPanel.EditorScrollPane getScroll() {
/* 152 */       return this.scroll;
/*     */     }
/*     */     
/*     */     public boolean getSavingEnabled() {
/* 156 */       return this.savingEnabled;
/*     */     }
/*     */     
/*     */     public void setSavingEnabled(boolean b) {
/* 160 */       this.savingEnabled = b;
/*     */     }
/*     */     public void add(EditorPair pair) {
/* 163 */       LocalizableLabel label = pair.getLabel();
/* 164 */       VPanel vPanel = pair.getPanel();
/*     */       
/* 166 */       this.c.gridx = 0;
/* 167 */       this.c.weightx = 0.0D;
/* 168 */       add((Component)label, this.c);
/* 169 */       this.c.gridx++;
/*     */       
/* 171 */       this.c.gridx++;
/* 172 */       add(Box.createHorizontalStrut(50), this.c);
/* 173 */       this.c.gridx++;
/* 174 */       this.c.weightx = 1.0D;
/* 175 */       add((Component)vPanel, this.c);
/* 176 */       this.c.gridy++;
/* 177 */       add(Box.createVerticalStrut(20), this.c);
/* 178 */       this.c.gridy++;
/*     */       
/* 180 */       this.panels.add(vPanel);
/*     */       
/* 182 */       TabbedEditorPanel.this.handlers.addAll(pair.getHandlers());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void nextPane() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateLocale() {
/* 192 */       int index = TabbedEditorPanel.this.tabPane.indexOfComponent((Component)this.scroll);
/*     */       
/* 194 */       if (index == -1) {
/* 195 */         throw new RuntimeException("Cannot find scroll component in tabPane for tab: " + this.name);
/*     */       }
/* 197 */       TabbedEditorPanel.this.tabPane.setTitleAt(index, getTabName());
/* 198 */       TabbedEditorPanel.this.tabPane.setToolTipTextAt(index, getTabTip());
/*     */     }
/*     */     
/*     */     public void addButtons(ExtendedPanel buttonPanel) {
/* 202 */       this.c.gridwidth = 4;
/* 203 */       this.c.gridx = 0;
/* 204 */       add((Component)buttonPanel, this.c);
/*     */     }
/*     */     
/*     */     public void addVerticalGap(int i) {
/* 208 */       add(Box.createVerticalStrut(i), this.c);
/* 209 */       this.c.gridy++;
/*     */     } }
/*     */   
/*     */   public static void main(String[] args) {
/* 213 */     JFrame f = new JFrame();
/* 214 */     f.setSize(300, 500);
/* 215 */     JPanel p = new JPanel();
/* 216 */     p.setLayout(new GridBagLayout());
/* 217 */     GridBagConstraints c = new GridBagConstraints();
/* 218 */     c.fill = 2;
/* 219 */     String test = "";
/* 220 */     for (int i = 0; i < 15; i++) {
/* 221 */       test = test + i;
/* 222 */       c.gridx = 0;
/* 223 */       c.gridy = i;
/*     */       
/* 225 */       p.add(new JButton(test), c);
/* 226 */       c.gridx = 1;
/* 227 */       p.add(Box.createHorizontalStrut(0), c);
/*     */       
/* 229 */       c.gridx = 2;
/*     */       
/* 231 */       p.add(new JButton(test), c);
/*     */     } 
/* 233 */     f.add(p);
/* 234 */     f.setDefaultCloseOperation(3);
/* 235 */     f.setVisible(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\TabbedEditorPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */