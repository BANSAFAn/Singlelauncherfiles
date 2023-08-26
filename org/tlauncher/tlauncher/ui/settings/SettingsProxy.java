/*     */ package org.tlauncher.tlauncher.ui.settings;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.net.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JRadioButton;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorField;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorIntegerRangeField;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorTextField;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.VPanel;
/*     */ import org.tlauncher.util.Range;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class SettingsProxy extends VPanel implements EditorField {
/*     */   private static final String path = "settings.connection.proxy";
/*     */   private static final String block = "proxyselect";
/*     */   private final ExtendedPanel proxyTypePanel;
/*  32 */   private final LinkedHashMap<Proxy.Type, ProxyLocRadio> typeMap = new LinkedHashMap<>();
/*  33 */   private final ButtonGroup group = new ButtonGroup();
/*     */   private final ProxySettingsPanel proxySettingsPanel;
/*     */   private final EditorTextField addressField;
/*     */   private final EditorTextField portField;
/*     */   
/*     */   SettingsProxy() {
/*  39 */     setAlignmentX(0.0F);
/*     */     
/*  41 */     List<Proxy.Type> typeList = Arrays.asList(Proxy.Type.values());
/*     */     
/*  43 */     for (Proxy.Type type : typeList) {
/*  44 */       ProxyLocRadio radio = new ProxyLocRadio();
/*  45 */       radio.setText(type.name().toLowerCase());
/*  46 */       radio.setAlignmentX(0.0F);
/*  47 */       radio.setOpaque(false);
/*     */       
/*  49 */       this.group.add(radio);
/*  50 */       this.typeMap.put(type, radio);
/*     */     } 
/*     */     
/*  53 */     ((ProxyLocRadio)this.typeMap.get(Proxy.Type.DIRECT)).addItemListener(new ItemListener()
/*     */         {
/*     */           public void itemStateChanged(ItemEvent e) {
/*  56 */             boolean selected = (e.getStateChange() == 1);
/*     */             
/*  58 */             if (selected) {
/*  59 */               Blocker.block(SettingsProxy.this.proxySettingsPanel, "proxyselect");
/*     */             }
/*     */           }
/*     */         });
/*     */     
/*  64 */     this.proxyTypePanel = new ExtendedPanel();
/*  65 */     this.proxyTypePanel.setAlignmentX(0.0F);
/*  66 */     add((Component)this.proxyTypePanel);
/*     */     
/*  68 */     ItemListener listener = new ItemListener()
/*     */       {
/*     */         public void itemStateChanged(ItemEvent e) {
/*  71 */           boolean selected = (e.getStateChange() == 1);
/*     */           
/*  73 */           if (selected) {
/*  74 */             Blocker.unblock(SettingsProxy.this.proxySettingsPanel, "proxyselect");
/*     */           }
/*     */         }
/*     */       };
/*     */     
/*  79 */     for (Map.Entry<Proxy.Type, ProxyLocRadio> en : this.typeMap.entrySet()) {
/*  80 */       this.proxyTypePanel.add(en.getValue());
/*     */       
/*  82 */       if (en.getKey() == Proxy.Type.DIRECT) {
/*     */         continue;
/*     */       }
/*  85 */       ((ProxyLocRadio)en.getValue()).addItemListener(listener);
/*     */     } 
/*     */     
/*  88 */     this.proxySettingsPanel = new ProxySettingsPanel();
/*  89 */     this.proxySettingsPanel.setAlignmentX(0.0F);
/*  90 */     add((Component)this.proxySettingsPanel);
/*     */     
/*  92 */     this.addressField = new EditorTextField("settings.connection.proxy.address", false);
/*  93 */     this.proxySettingsPanel.setCenter((Component)this.addressField);
/*     */     
/*  95 */     this.portField = (EditorTextField)new EditorIntegerRangeField("settings.connection.proxy.port", new Range(Integer.valueOf(0), Integer.valueOf(65535)));
/*  96 */     this.portField.setColumns(5);
/*  97 */     this.proxySettingsPanel.setEast((Component)this.portField);
/*     */   }
/*     */   
/*     */   private Map.Entry<Proxy.Type, ProxyLocRadio> getSelectedType() {
/* 101 */     for (Map.Entry<Proxy.Type, ProxyLocRadio> en : this.typeMap.entrySet()) {
/* 102 */       if (((ProxyLocRadio)en.getValue()).isSelected())
/* 103 */         return en; 
/*     */     } 
/* 105 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setSelectedType(Proxy.Type type) {
/* 110 */     for (Map.Entry<Proxy.Type, ProxyLocRadio> en : this.typeMap.entrySet()) {
/* 111 */       if (en.getKey() == type) {
/* 112 */         ((ProxyLocRadio)en.getValue()).setSelected(true);
/*     */         return;
/*     */       } 
/*     */     } 
/* 116 */     ((ProxyLocRadio)this.typeMap.get(Proxy.Type.DIRECT)).setSelected(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSettingsValue() {
/* 121 */     Map.Entry<Proxy.Type, ProxyLocRadio> selected = getSelectedType();
/*     */     
/* 123 */     if (selected == null || selected.getKey() == Proxy.Type.DIRECT) {
/* 124 */       U.log(new Object[] { "selected is", selected, "so null" });
/* 125 */       return null;
/*     */     } 
/*     */     
/* 128 */     U.log(new Object[] { ((Proxy.Type)selected.getKey()).name().toLowerCase() + ';' + this.addressField.getValue() + ';' + this.portField.getValue() });
/*     */     
/* 130 */     return ((Proxy.Type)selected.getKey()).name().toLowerCase() + ';' + this.addressField.getValue() + ';' + this.portField.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSettingsValue(String value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValueValid() {
/* 150 */     Map.Entry<Proxy.Type, ProxyLocRadio> selected = getSelectedType();
/*     */     
/* 152 */     if (selected == null || selected.getKey() == Proxy.Type.DIRECT) {
/* 153 */       return true;
/*     */     }
/* 155 */     return (this.addressField.isValueValid() && this.portField.isValueValid());
/*     */   }
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 160 */     Blocker.blockComponents((Container)this, reason);
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 165 */     Blocker.unblockComponents((Container)this, reason);
/*     */   }
/*     */   
/*     */   private class ProxyLocRadio
/*     */     extends JRadioButton implements LocalizableComponent {
/*     */     private String currentType;
/*     */     
/*     */     public void setText(String proxyType) {
/* 173 */       this.currentType = proxyType;
/*     */       
/* 175 */       String text = Localizable.get("settings.connection.proxy.type." + proxyType);
/*     */       
/* 177 */       if (StringUtils.isBlank(text)) {
/* 178 */         text = proxyType;
/*     */       }
/* 180 */       super.setText(text);
/*     */     }
/*     */     private ProxyLocRadio() {}
/*     */     
/*     */     public void updateLocale() {
/* 185 */       setText(this.currentType);
/*     */     } }
/*     */   
/*     */   private class ProxySettingsPanel extends BorderPanel implements Blockable {
/*     */     private ProxySettingsPanel() {}
/*     */     
/*     */     public void block(Object reason) {
/* 192 */       Blocker.blockComponents((Container)this, reason);
/*     */     }
/*     */ 
/*     */     
/*     */     public void unblock(Object reason) {
/* 197 */       Blocker.unblockComponents((Container)this, reason);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\settings\SettingsProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */