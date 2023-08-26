/*     */ package org.tlauncher.tlauncher.ui.settings;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SpringLayout;
/*     */ import net.minecraft.launcher.process.JavaProcessLauncher;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.tlauncher.tlauncher.rmo.Bootstrapper;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.AsyncThread;
/*     */ 
/*     */ public class ResetView extends JPanel {
/*     */   public ResetView() {
/*  32 */     SpringLayout spring = new SpringLayout();
/*  33 */     setLayout(spring);
/*  34 */     setPreferredSize(new Dimension(420, 280));
/*  35 */     LocalizableLabel localizableLabel1 = new LocalizableLabel("settings.reset.message");
/*  36 */     LocalizableLabel localizableLabel2 = new LocalizableLabel("settings.reset.not.remove.maps");
/*  37 */     localizableLabel2.setHorizontalAlignment(0);
/*  38 */     localizableLabel2.setForeground(new Color(255, 58, 66));
/*  39 */     this.resetAgain = (JButton)new UpdaterButton(new Color(222, 64, 43), new Color(222, 31, 8), Color.WHITE, "settings.reset.button.start");
/*  40 */     SwingUtil.setFontSize(this.resetAgain, 13.0F, 1);
/*  41 */     EditorCheckBox editorCheckBox1 = new EditorCheckBox("settings.reset.remove.tlauncher.settings");
/*  42 */     editorCheckBox1.setIconTextGap(15);
/*  43 */     editorCheckBox1.setSelected(true);
/*  44 */     EditorCheckBox editorCheckBox2 = new EditorCheckBox("settings.reset.remove.minecraft.folder");
/*  45 */     editorCheckBox2.setIconTextGap(15);
/*  46 */     editorCheckBox2.setSelected(true);
/*  47 */     EditorCheckBox editorCheckBox3 = new EditorCheckBox("settings.reset.remove.versions");
/*  48 */     editorCheckBox3.setIconTextGap(15);
/*  49 */     editorCheckBox3.setSelected(true);
/*     */     
/*  51 */     spring.putConstraint("North", (Component)localizableLabel1, 10, "North", this);
/*  52 */     spring.putConstraint("West", (Component)localizableLabel1, 10, "West", this);
/*  53 */     spring.putConstraint("South", (Component)localizableLabel1, 40, "North", this);
/*  54 */     spring.putConstraint("East", (Component)localizableLabel1, -10, "East", this);
/*  55 */     add((Component)localizableLabel1);
/*  56 */     spring.putConstraint("North", (Component)editorCheckBox1, 10, "South", (Component)localizableLabel1);
/*  57 */     spring.putConstraint("West", (Component)editorCheckBox1, 10, "West", this);
/*  58 */     spring.putConstraint("South", (Component)editorCheckBox1, 40, "South", (Component)localizableLabel1);
/*  59 */     spring.putConstraint("East", (Component)editorCheckBox1, -10, "East", this);
/*  60 */     add((Component)editorCheckBox1);
/*  61 */     spring.putConstraint("North", (Component)editorCheckBox3, 10, "South", (Component)editorCheckBox1);
/*  62 */     spring.putConstraint("West", (Component)editorCheckBox3, 10, "West", this);
/*  63 */     spring.putConstraint("South", (Component)editorCheckBox3, 40, "South", (Component)editorCheckBox1);
/*  64 */     spring.putConstraint("East", (Component)editorCheckBox3, -10, "East", this);
/*  65 */     add((Component)editorCheckBox3);
/*     */     
/*  67 */     spring.putConstraint("North", (Component)editorCheckBox2, 10, "South", (Component)editorCheckBox3);
/*  68 */     spring.putConstraint("West", (Component)editorCheckBox2, 10, "West", this);
/*  69 */     spring.putConstraint("South", (Component)editorCheckBox2, 40, "South", (Component)editorCheckBox3);
/*  70 */     spring.putConstraint("East", (Component)editorCheckBox2, -10, "East", this);
/*  71 */     add((Component)editorCheckBox2);
/*     */     
/*  73 */     spring.putConstraint("North", (Component)localizableLabel2, 30, "South", (Component)editorCheckBox2);
/*  74 */     spring.putConstraint("West", (Component)localizableLabel2, 0, "West", this);
/*  75 */     spring.putConstraint("South", (Component)localizableLabel2, 55, "South", (Component)editorCheckBox2);
/*  76 */     spring.putConstraint("East", (Component)localizableLabel2, 0, "East", this);
/*  77 */     add((Component)localizableLabel2);
/*     */     
/*  79 */     spring.putConstraint("North", this.resetAgain, -50, "South", this);
/*  80 */     spring.putConstraint("West", this.resetAgain, 50, "West", this);
/*  81 */     spring.putConstraint("South", this.resetAgain, -10, "South", this);
/*  82 */     spring.putConstraint("East", this.resetAgain, -50, "East", this);
/*  83 */     add(this.resetAgain);
/*     */     
/*  85 */     this.resetAgain.addActionListener(e1 -> {
/*     */           if (Alert.showLocQuestion("settings.reset.question.confirm"))
/*     */             AsyncThread.execute(()); 
/*     */         });
/*     */   }
/*     */   
/*     */   private JButton resetAgain;
/*     */   
/*     */   public JButton getResetAgain() {
/*  94 */     return this.resetAgain;
/*     */   }
/*     */   
/*     */   private void reset(LocalizableCheckbox tlSettings, LocalizableCheckbox minecraft, LocalizableCheckbox versions) {
/*  98 */     List<File> files = new ArrayList<>();
/*  99 */     JavaProcessLauncher p = Bootstrapper.restartLauncher();
/*     */     
/* 101 */     if (versions.isSelected()) {
/* 102 */       File dir2 = new File(MinecraftUtil.getWorkingDirectory(), "versions");
/* 103 */       if (dir2.exists()) {
/* 104 */         files.add(dir2);
/*     */       }
/*     */     } 
/* 107 */     if (minecraft.isSelected()) {
/*     */       try {
/* 109 */         File dir3 = MinecraftUtil.getWorkingDirectory();
/* 110 */         if (dir3.exists()) {
/* 111 */           files.addAll((Collection<? extends File>)IOUtils.readLines(getClass().getResourceAsStream("/removedFolders.txt"))
/* 112 */               .stream().map(e -> new File(dir3, e)).filter(File::exists).collect(Collectors.toList()));
/*     */         }
/* 114 */       } catch (IOException e) {
/* 115 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 119 */     if (tlSettings.isSelected()) {
/* 120 */       File tlFolder = MinecraftUtil.getTLauncherFile("");
/* 121 */       File[] listFiles = tlFolder.listFiles(pathname -> !pathname.toString().endsWith("jvms"));
/* 122 */       files.addAll(Lists.newArrayList((Object[])listFiles));
/* 123 */       TLauncher.getInstance().getConfiguration().clear();
/* 124 */       TLauncher.getInstance().getConfiguration().store();
/*     */     } 
/*     */     
/* 127 */     files.forEach(f -> { if (f.isDirectory()) {
/*     */             FileUtil.deleteDirectory(f);
/*     */           } else {
/*     */             FileUtil.deleteFile(f);
/*     */           } 
/*     */         }); try {
/* 133 */       p.start();
/* 134 */       TLauncher.kill();
/* 135 */     } catch (IOException e) {
/* 136 */       U.log(new Object[] { e });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\settings\ResetView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */