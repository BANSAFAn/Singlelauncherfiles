/*     */ package org.tlauncher.tlauncher.ui.modpack;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.eventbus.Subscribe;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.awt.CardLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.File;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.swing.AbstractCellEditor;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.CellEditor;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ import javax.swing.table.JTableHeader;
/*     */ import javax.swing.table.TableCellEditor;
/*     */ import javax.swing.table.TableCellRenderer;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.tlauncher.tlauncher.controller.JavaMinecraftController;
/*     */ import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileChooser;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableTextField;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*     */ import org.tlauncher.tlauncher.ui.settings.SettingElement;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.tlauncher.ui.text.LocalizableTextArea;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ public class ConfigurationJavaFrame
/*     */   extends TemlateModpackFrame
/*     */ {
/*     */   private LocalizableTextField path;
/*     */   private LocalizableTextArea args;
/*     */   private LocalizableTextField name;
/*     */   private JTable addedJava;
/*     */   private JScrollPane scroll;
/*     */   private CardLayout cradLayout;
/*     */   JPanel topPanel;
/*     */   JPanel bottomPanel;
/*     */   @Inject
/*     */   private JavaMinecraftController controller;
/*  85 */   private volatile MinecraftJava.CompleteMinecraftJava j = new MinecraftJava.CompleteMinecraftJava();
/*     */   
/*     */   public ConfigurationJavaFrame() {
/*  88 */     super((JFrame)TLauncher.getInstance().getFrame(), "settings.select.java", new Dimension(1000, 480));
/*  89 */     Font font = (new JButton()).getFont().deriveFont(1, 13.0F);
/*  90 */     this.addedJava = new JTable();
/*  91 */     this.scroll = new JScrollPane(this.addedJava, 20, 31);
/*     */     
/*  93 */     JavaTableModel d = new JavaTableModel();
/*     */     
/*  95 */     ExtendedPanel common = new ExtendedPanel();
/*  96 */     ExtendedPanel buttonsBottom = new ExtendedPanel();
/*  97 */     this.cradLayout = new CardLayout();
/*  98 */     this.topPanel = new JPanel(this.cradLayout);
/*  99 */     GridBagConstraints c = new GridBagConstraints();
/* 100 */     UpdaterButton updaterButton1 = new UpdaterButton(UpdaterButton.GRAY_COLOR, "explorer.browse");
/* 101 */     UpdaterButton updaterButton2 = new UpdaterButton(new Color(222, 64, 43), new Color(222, 31, 8), Color.WHITE, "settings.reset.java");
/*     */     
/* 103 */     final UpdaterButton save = new UpdaterButton(UpdaterButton.ORANGE_COLOR, "settings.save");
/* 104 */     this.path = new LocalizableTextField();
/* 105 */     this.name = new LocalizableTextField("settings.java.name");
/* 106 */     this.args = new LocalizableTextArea("settings.java.args", 2, 0);
/* 107 */     this.path.setPlaceholder("settings.java.choose", new Object[] { Localizable.get("explorer.browse") });
/*     */     
/* 109 */     LocalizableLabel localizableLabel1 = new LocalizableLabel("settings.java.path");
/* 110 */     LocalizableLabel localizableLabel2 = new LocalizableLabel("settings.java.args.jvm");
/* 111 */     LocalizableLabel localizableLabel3 = new LocalizableLabel("modpack.table.pack.element.name");
/* 112 */     LocalizableLabel localizableLabel4 = new LocalizableLabel("settings.java.not.added");
/*     */     
/* 114 */     this.addedJava.setRowHeight(50);
/* 115 */     this.addedJava.setShowVerticalLines(false);
/* 116 */     this.addedJava.setTableHeader((JTableHeader)null);
/* 117 */     this.addedJava.setModel(d);
/* 118 */     this.addedJava.getColumnModel().getColumn(0).setPreferredWidth(880);
/* 119 */     this.addedJava.getColumnModel().getColumn(1).setPreferredWidth(60);
/* 120 */     this.addedJava.getColumnModel().getColumn(2).setPreferredWidth(60);
/* 121 */     this.addedJava.setDefaultEditor(MinecraftJava.CompleteMinecraftJava.class, new JavaCellRenderAndEditor());
/* 122 */     this.addedJava.setDefaultRenderer(MinecraftJava.CompleteMinecraftJava.class, new JavaCellRenderAndEditor());
/*     */     
/* 124 */     this.addedJava.getSelectionModel().addListSelectionListener(new ListSelectionListener()
/*     */         {
/*     */           public void valueChanged(ListSelectionEvent e)
/*     */           {
/* 128 */             int row = ConfigurationJavaFrame.this.addedJava.getSelectedRow();
/* 129 */             int column = ConfigurationJavaFrame.this.addedJava.getSelectedColumn();
/* 130 */             CellEditor c = ConfigurationJavaFrame.this.addedJava.getCellEditor();
/* 131 */             if (row == -1)
/*     */               return; 
/* 133 */             if (Objects.nonNull(c))
/* 134 */               c.cancelCellEditing(); 
/* 135 */             ConfigurationJavaFrame.this.addedJava.removeRowSelectionInterval(row, row);
/* 136 */             if (column == 2) {
/* 137 */               ConfigurationJavaFrame.this.controller.remove((MinecraftJava.CompleteMinecraftJava)ConfigurationJavaFrame.this.addedJava.getModel().getValueAt(row, 0));
/* 138 */             } else if (column == 1) {
/* 139 */               ConfigurationJavaFrame.this.j = (MinecraftJava.CompleteMinecraftJava)ConfigurationJavaFrame.this.addedJava.getModel().getValueAt(row, 0);
/* 140 */               ConfigurationJavaFrame.this.path.setValue(ConfigurationJavaFrame.this.j.getPath());
/* 141 */               ConfigurationJavaFrame.this.name.setValue(ConfigurationJavaFrame.this.j.getName());
/* 142 */               ConfigurationJavaFrame.this.args.setText(ConfigurationJavaFrame.this.j.getArgs().stream().collect(Collectors.joining(" ")));
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 148 */     localizableLabel1.setFont(SettingElement.LABEL_FONT);
/* 149 */     localizableLabel2.setFont(SettingElement.LABEL_FONT);
/* 150 */     localizableLabel3.setFont(SettingElement.LABEL_FONT);
/* 151 */     localizableLabel4.setHorizontalAlignment(0);
/* 152 */     localizableLabel4.setAlignmentY(0.0F);
/* 153 */     SwingUtil.setFontSize((JComponent)localizableLabel4, 18.0F, 1);
/*     */     
/* 155 */     save.setForeground(Color.WHITE);
/* 156 */     save.setFont(font);
/* 157 */     updaterButton2.setForeground(Color.WHITE);
/* 158 */     updaterButton2.setFont(font);
/*     */     
/* 160 */     save.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/* 163 */             save.setBackground(ColorUtil.COLOR_204);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/* 168 */             save.setBackground(save.getBackgroundColor());
/*     */           }
/*     */         });
/* 171 */     common.setLayout(new BoxLayout((Container)common, 1));
/* 172 */     buttonsBottom.add((Component)updaterButton2, (Component)save);
/*     */     
/* 174 */     this.topPanel.add((Component)localizableLabel4, ModpackScene.EMPTY);
/* 175 */     this.topPanel.add(this.scroll, ModpackScene.NOT_EMPTY);
/* 176 */     common.add(this.topPanel);
/*     */     
/* 178 */     this.bottomPanel = (JPanel)new ExtendedPanel();
/* 179 */     this.bottomPanel.setLayout(new GridBagLayout());
/*     */     
/* 181 */     c.weightx = 0.1D;
/* 182 */     c.gridx = 0;
/* 183 */     c.gridy = 1;
/* 184 */     c.anchor = 17;
/* 185 */     c.insets = new Insets(0, 10, 0, 10);
/*     */     
/* 187 */     localizableLabel1.setHorizontalTextPosition(2);
/* 188 */     this.bottomPanel.add((Component)localizableLabel1, c);
/* 189 */     c.gridy = 2;
/* 190 */     localizableLabel2.setHorizontalTextPosition(2);
/* 191 */     this.bottomPanel.add((Component)localizableLabel3, c);
/* 192 */     c.gridy = 3;
/* 193 */     localizableLabel3.setHorizontalTextPosition(2);
/* 194 */     this.bottomPanel.add((Component)localizableLabel2, c);
/* 195 */     c.insets = new Insets(5, 0, 5, 10);
/* 196 */     c.fill = 2;
/* 197 */     c.weighty = 0.0D;
/* 198 */     c.weightx = 0.8D;
/* 199 */     c.gridx = 1;
/* 200 */     c.gridy = 1;
/* 201 */     this.bottomPanel.add((Component)this.path, c);
/* 202 */     c.gridy = 2;
/* 203 */     this.bottomPanel.add((Component)this.name, c);
/* 204 */     c.gridy = 3;
/* 205 */     this.bottomPanel.add((Component)this.args, c);
/* 206 */     c.insets = new Insets(0, 0, 0, 0);
/* 207 */     c.fill = 0;
/* 208 */     c.gridy = 4;
/* 209 */     c.anchor = 10;
/* 210 */     c.gridwidth = 3;
/* 211 */     c.gridx = 0;
/* 212 */     this.bottomPanel.add((Component)buttonsBottom, c);
/* 213 */     c.gridwidth = 1;
/* 214 */     c.weightx = 0.0D;
/* 215 */     c.weighty = 0.0D;
/* 216 */     c.gridx = 2;
/* 217 */     c.gridy = 1;
/* 218 */     c.insets = new Insets(0, 0, 0, 10);
/* 219 */     this.bottomPanel.add((Component)updaterButton1, c);
/* 220 */     common.add(this.bottomPanel);
/* 221 */     addCenter((JComponent)common);
/* 222 */     updaterButton1.addActionListener(e -> {
/*     */           FileChooser fileChooser = (FileChooser)TLauncher.getInjector().getInstance(FileChooser.class);
/*     */           fileChooser.setFileSelectionMode(1);
/*     */           if (Objects.nonNull(this.path.getValue())) {
/*     */             fileChooser.setCurrentDirectory(new File(this.path.getValue()));
/*     */           } else {
/*     */             fileChooser.setCurrentDirectory(OS.buildJAVAFolder().toFile());
/*     */           } 
/*     */           int result = fileChooser.showDialog(this);
/*     */           if (result == 0) {
/*     */             File f = fileChooser.getSelectedFile();
/*     */             this.path.setValue(f.getAbsolutePath());
/*     */             List<String> list1 = new ArrayList<>();
/*     */             MinecraftUtil.configureG1GC(list1);
/*     */             this.args.setText(list1.stream().collect(Collectors.joining(" ")));
/*     */             this.name.setValue(f.getAbsoluteFile().getName());
/*     */           } 
/*     */         });
/* 240 */     save.addActionListener(e -> {
/*     */           String java = OS.appendBootstrapperJvm2(this.path.getValue());
/*     */           
/*     */           if (Objects.isNull(this.path.getValue())) {
/*     */             Alert.showError("", Localizable.get("review.message.fill") + " " + Localizable.get("settings.java.path"));
/*     */             return;
/*     */           } 
/*     */           if (Files.notExists(Paths.get(java, new String[0]), new java.nio.file.LinkOption[0])) {
/*     */             Alert.showError("", Localizable.get("settings.java.not.proper.path", new Object[] { java }));
/*     */             return;
/*     */           } 
/*     */           if (StringUtils.isBlank(this.name.getValue())) {
/*     */             Alert.showError("", Localizable.get("review.message.fill") + " " + Localizable.get("modpack.table.pack.element.name"));
/*     */             return;
/*     */           } 
/*     */           this.j.setPath(this.path.getValue());
/*     */           this.j.setName(this.name.getValue());
/*     */           this.j.setArgs(Arrays.asList(this.args.getText().split(" ")));
/*     */           this.controller.add(this.j);
/*     */           resetForm();
/*     */         });
/* 261 */     updaterButton2.addActionListener(e -> resetForm());
/*     */ 
/*     */     
/* 264 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentShown(ComponentEvent e) {
/* 267 */             ConfigurationJavaFrame.this.controller.notifyListeners();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void applicationEvent(MinecraftJava event) {
/* 275 */     SwingUtilities.invokeLater(() -> {
/*     */           if (event.getJvm().isEmpty()) {
/*     */             this.cradLayout.show(this.topPanel, ModpackScene.EMPTY);
/*     */           } else {
/*     */             this.cradLayout.show(this.topPanel, ModpackScene.NOT_EMPTY);
/*     */           } 
/*     */           ((JavaTableModel)this.addedJava.getModel()).setList(Lists.newArrayList(event.getJvm().values()));
/*     */           this.addedJava.revalidate();
/*     */           this.addedJava.repaint();
/*     */         });
/*     */   }
/*     */   
/*     */   private void resetForm() {
/* 288 */     this.path.setValue("");
/* 289 */     this.name.setValue("");
/* 290 */     this.args.setText("");
/* 291 */     this.j = new MinecraftJava.CompleteMinecraftJava();
/*     */   }
/*     */   private class JavaTableModel extends AbstractTableModel {
/* 294 */     public void setList(List<MinecraftJava.CompleteMinecraftJava> list) { this.list = list; } public String toString() { return "ConfigurationJavaFrame.JavaTableModel(list=" + getList() + ")"; }
/* 295 */     public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof JavaTableModel)) return false;  JavaTableModel other = (JavaTableModel)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  Object<MinecraftJava.CompleteMinecraftJava> this$list = (Object<MinecraftJava.CompleteMinecraftJava>)getList(), other$list = (Object<MinecraftJava.CompleteMinecraftJava>)other.getList(); return !((this$list == null) ? (other$list != null) : !this$list.equals(other$list)); } protected boolean canEqual(Object other) { return other instanceof JavaTableModel; } public int hashCode() { int PRIME = 59; result = super.hashCode(); Object<MinecraftJava.CompleteMinecraftJava> $list = (Object<MinecraftJava.CompleteMinecraftJava>)getList(); return result * 59 + (($list == null) ? 43 : $list.hashCode()); }
/*     */ 
/*     */     
/* 298 */     private List<MinecraftJava.CompleteMinecraftJava> list = new ArrayList<>(); public List<MinecraftJava.CompleteMinecraftJava> getList() { return this.list; }
/*     */ 
/*     */     
/*     */     public Object getValueAt(int row, int column) {
/* 302 */       return this.list.get(row);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowCount() {
/* 307 */       return this.list.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCellEditable(int rowIndex, int columnIndex) {
/* 312 */       if (rowIndex == 0) {
/* 313 */         return super.isCellEditable(rowIndex, columnIndex);
/*     */       }
/* 315 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getColumnCount() {
/* 320 */       return 3;
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getColumnClass(int columnIndex) {
/* 325 */       return MinecraftJava.CompleteMinecraftJava.class;
/*     */     }
/*     */   }
/*     */   
/*     */   public class JavaCellRenderAndEditor
/*     */     extends AbstractCellEditor
/*     */     implements TableCellRenderer, TableCellEditor {
/*     */     public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
/* 333 */       return buildComponent(row, column, (MinecraftJava.CompleteMinecraftJava)value);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
/* 339 */       return buildComponent(row, column, (MinecraftJava.CompleteMinecraftJava)value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object getCellEditorValue() {
/* 344 */       return null;
/*     */     }
/*     */     private Component buildComponent(int row, int column, MinecraftJava.CompleteMinecraftJava v) {
/*     */       JLabel l;
/* 348 */       if (Objects.isNull(v))
/* 349 */         return null; 
/* 350 */       switch (column) {
/*     */         
/*     */         case 0:
/* 353 */           l = new JLabel((row + 1) + ") " + Localizable.get("modpack.table.pack.element.name") + ": " + v.getName() + ". " + Localizable.get("settings.java.path") + ": " + v.getPath());
/* 354 */           l.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
/* 355 */           SwingUtil.changeFontFamily(l, FontTL.ROBOTO_MEDIUM, 13);
/* 356 */           return l;
/*     */         case 1:
/* 358 */           return (Component)new ImageUdaterButton(Color.WHITE, "gear.png");
/*     */         case 2:
/* 360 */           return (Component)new ImageUdaterButton(Color.WHITE, "remove.png");
/*     */       } 
/* 362 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\ConfigurationJavaFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */