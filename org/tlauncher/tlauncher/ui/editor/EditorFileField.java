/*     */ package org.tlauncher.tlauncher.ui.editor;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.File;
/*     */ import java.util.regex.Pattern;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileChooser;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class EditorFileField
/*     */   extends BorderPanel
/*     */   implements EditorField {
/*     */   private static final long serialVersionUID = 5136327098130653756L;
/*     */   public static final char DEFAULT_DELIMITER = ';';
/*     */   private final EditorTextField textField;
/*     */   private final LocalizableButton explorerButton;
/*     */   private final FileChooser explorer;
/*     */   private final char delimiterChar;
/*     */   private final Pattern delimiterSplitter;
/*     */   
/*     */   public EditorFileField(String prompt, boolean canBeEmpty, String button, FileChooser chooser, char delimiter) {
/*  28 */     super(10, 0);
/*  29 */     if (chooser == null) {
/*  30 */       throw new NullPointerException("FileExplorer should be defined!");
/*     */     }
/*  32 */     this.textField = new EditorTextField(prompt, canBeEmpty);
/*  33 */     this.explorerButton = (LocalizableButton)new UpdaterButton(UpdaterButton.GRAY_COLOR, button);
/*  34 */     this.explorer = chooser;
/*     */     
/*  36 */     this.delimiterChar = delimiter;
/*  37 */     this.delimiterSplitter = Pattern.compile(String.valueOf(this.delimiterChar), 16);
/*     */     
/*  39 */     this.explorerButton.addActionListener(e -> {
/*     */           this.explorerButton.setEnabled(false);
/*     */           
/*     */           this.explorer.setCurrentDirectory(getFirstFile());
/*     */           
/*     */           int result = this.explorer.showDialog((Component)this);
/*     */           if (result == 0) {
/*     */             setRawValue(this.explorer.getSelectedFiles());
/*     */           }
/*     */           this.explorerButton.setEnabled(true);
/*     */         });
/*  50 */     add((Component)this.textField, "Center");
/*  51 */     add((Component)this.explorerButton, "East");
/*     */   }
/*     */   
/*     */   public EditorFileField(String prompt, boolean canBeEmpty, FileChooser chooser) {
/*  55 */     this(prompt, canBeEmpty, "explorer.browse", chooser, ';');
/*     */   }
/*     */   
/*     */   public EditorFileField(String prompt, FileChooser chooser) {
/*  59 */     this(prompt, false, chooser);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSettingsValue() {
/*  64 */     return getValueFromRaw(getRawValues());
/*     */   }
/*     */   
/*     */   private File[] getRawValues() {
/*  68 */     String[] paths = getRawSplitValue();
/*  69 */     if (paths == null) {
/*  70 */       return null;
/*     */     }
/*  72 */     int len = paths.length;
/*  73 */     File[] files = new File[len];
/*     */     
/*  75 */     for (int i = 0; i < paths.length; i++) {
/*  76 */       files[i] = new File(paths[i]);
/*     */     }
/*  78 */     return files;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSettingsValue(String value) {
/*  83 */     this.textField.setSettingsValue(value);
/*     */   }
/*     */   
/*     */   private void setRawValue(File[] fileList) {
/*  87 */     setSettingsValue(getValueFromRaw(fileList));
/*     */   }
/*     */   
/*     */   private String[] getRawSplitValue() {
/*  91 */     return splitString(this.textField.getValue());
/*     */   }
/*     */   
/*     */   private String getValueFromRaw(File[] files) {
/*  95 */     if (files == null) {
/*  96 */       return null;
/*     */     }
/*  98 */     StringBuilder builder = new StringBuilder();
/*     */     
/* 100 */     for (File file : files) {
/* 101 */       String path = file.getAbsolutePath();
/* 102 */       builder.append(this.delimiterChar).append(path);
/*     */     } 
/*     */     
/* 105 */     return builder.substring(1);
/*     */   }
/*     */   
/*     */   private String[] splitString(String s) {
/* 109 */     if (s == null) {
/* 110 */       return null;
/*     */     }
/* 112 */     String[] split = this.delimiterSplitter.split(s);
/* 113 */     if (split.length == 0) {
/* 114 */       return null;
/*     */     }
/* 116 */     return split;
/*     */   }
/*     */   
/*     */   private File getFirstFile() {
/* 120 */     File[] files = getRawValues();
/*     */     
/* 122 */     if (files == null || files.length == 0) {
/* 123 */       return TLauncher.getDirectory();
/*     */     }
/* 125 */     return files[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValueValid() {
/* 130 */     return this.textField.isValueValid();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBackground(Color bg) {
/* 135 */     if (this.textField != null) {
/* 136 */       this.textField.setBackground(bg);
/*     */     }
/*     */   }
/*     */   
/*     */   public void block(Object reason) {
/* 141 */     Blocker.blockComponents(reason, new Component[] { (Component)this.textField, (Component)this.explorerButton });
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 146 */     Blocker.unblockComponents(Blocker.UNIVERSAL_UNBLOCK, new Component[] { (Component)this.textField, (Component)this.explorerButton });
/*     */   }
/*     */   
/*     */   protected void log(Object... w) {
/* 150 */     U.log(new Object[] { "[" + getClass().getSimpleName() + "]", w });
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\editor\EditorFileField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */