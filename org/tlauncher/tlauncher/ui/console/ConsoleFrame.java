/*     */ package org.tlauncher.tlauncher.ui.console;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.AdjustmentEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringReader;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.BoundedRangeModel;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.text.DefaultCaret;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileChooser;
/*     */ import org.tlauncher.tlauncher.ui.explorer.filters.CommonFilter;
/*     */ import org.tlauncher.tlauncher.ui.explorer.filters.FilesAndExtentionFilter;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.swing.EmptyAction;
/*     */ import org.tlauncher.tlauncher.ui.swing.ScrollPane;
/*     */ import org.tlauncher.tlauncher.ui.swing.TextPopup;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.pastebin.Paste;
/*     */ import org.tlauncher.util.pastebin.PasteListener;
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
/*     */ public class ConsoleFrame
/*     */   extends JFrame
/*     */   implements PasteListener, LocalizableComponent
/*     */ {
/*     */   public static final int MIN_WIDTH = 670;
/*     */   public static final int MIN_HEIGHT = 500;
/*     */   public final Console console;
/*     */   public final JTextArea textarea;
/*     */   public final JScrollBar vScrollbar;
/*     */   public final ConsoleFrameBottom bottom;
/*     */   public final ConsoleTextPopup popup;
/*     */   private int lastWindowWidth;
/*     */   private int scrollBarValue;
/*     */   private boolean scrollDown;
/*     */   private final Object busy;
/*     */   volatile boolean hiding;
/*     */   
/*     */   ConsoleFrame(Console console) {
/* 113 */     this.busy = new Object(); this.console = console; this.textarea = new JTextArea(); this.textarea.setLineWrap(true); this.textarea.setEditable(false); this.textarea.setAutoscrolls(true); this.textarea.setMargin(new Insets(0, 0, 0, 0)); this.textarea.setFont(new Font("DialogInput", 0, (int)((new LocalizableLabel()).getFont().getSize() * 1.2D))); this.textarea.setForeground(Color.white); this.textarea.setCaretColor(Color.white); this.textarea.setBackground(Color.black); this.textarea.setSelectionColor(Color.gray); ((DefaultCaret)this.textarea.getCaret()).setUpdatePolicy(2); this.popup = new ConsoleTextPopup(); this.textarea.addMouseListener((MouseListener)this.popup); ScrollPane scrollPane = new ScrollPane(this.textarea); scrollPane.setBorder(null); scrollPane.setVBPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); this.vScrollbar = scrollPane.getVerticalScrollBar(); BoundedRangeModel vsbModel = this.vScrollbar.getModel(); this.vScrollbar.addAdjustmentListener(e -> { if (getWidth() != this.lastWindowWidth)
/*     */             return;  int nv = e.getValue(); if (nv < this.scrollBarValue) { this.scrollDown = false; } else if (nv == vsbModel.getMaximum() - vsbModel.getExtent()) { this.scrollDown = true; }  this.scrollBarValue = nv;
/*     */         }); addComponentListener(new ComponentAdapter() { public void componentResized(ComponentEvent e) { ConsoleFrame.this.lastWindowWidth = ConsoleFrame.this.getWidth(); } }); getContentPane().setLayout(new BorderLayout()); getContentPane().add((Component)scrollPane, "Center"); getContentPane().add((Component)(this.bottom = new ConsoleFrameBottom(this)), "South"); SwingUtil.setFavicons(this);
/* 116 */   } public void print(String string) { synchronized (this.busy) {
/* 117 */       Document document = this.textarea.getDocument();
/*     */       
/*     */       try {
/* 120 */         document.insertString(document.getLength(), string, null);
/* 121 */       } catch (Throwable throwable) {}
/*     */ 
/*     */       
/* 124 */       if (this.scrollDown)
/* 125 */         scrollDown(); 
/*     */     }  }
/*     */    public void println(String string) {
/*     */     print(string + '\n');
/*     */   } public void clear() {
/* 130 */     this.textarea.setText("");
/*     */   }
/*     */   
/*     */   public void scrollDown() {
/* 134 */     SwingUtilities.invokeLater(() -> this.vScrollbar.setValue(this.vScrollbar.getMaximum()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLocale() {
/* 139 */     setTitle(Localizable.get("console"));
/* 140 */     Localizable.updateContainer(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void pasteUploading(Paste paste) {
/* 145 */     this.bottom.pastebin.setEnabled(false);
/* 146 */     this.popup.pastebinAction.setEnabled(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void pasteDone(Paste paste) {
/* 151 */     this.bottom.pastebin.setEnabled(true);
/* 152 */     this.popup.pastebinAction.setEnabled(true);
/*     */   }
/*     */   
/*     */   public class ConsoleTextPopup extends TextPopup {
/*     */     private final FileChooser explorer;
/*     */     private final Action saveAllAction;
/*     */     private final Action pastebinAction;
/*     */     private final Action clearAllAction;
/*     */     
/*     */     ConsoleTextPopup() {
/* 162 */       this.explorer = (FileChooser)TLauncher.getInjector().getInstance(FileChooser.class);
/* 163 */       this.explorer.setFileFilter((CommonFilter)new FilesAndExtentionFilter("log", new String[] { "log" }));
/*     */       
/* 165 */       this.saveAllAction = (Action)new EmptyAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 168 */             ConsoleFrame.ConsoleTextPopup.this.onSavingCalled();
/*     */           }
/*     */         };
/*     */       
/* 172 */       this.pastebinAction = (Action)new EmptyAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 175 */             ConsoleFrame.this.console.sendPaste();
/*     */           }
/*     */         };
/*     */       
/* 179 */       this.clearAllAction = (Action)new EmptyAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 182 */             ConsoleFrame.ConsoleTextPopup.this.onClearCalled();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     protected JPopupMenu getPopup(MouseEvent e, JTextComponent comp) {
/* 189 */       JPopupMenu menu = super.getPopup(e, comp);
/*     */       
/* 191 */       if (menu == null) {
/* 192 */         return null;
/*     */       }
/* 194 */       menu.addSeparator();
/* 195 */       menu.add(this.saveAllAction).setText(Localizable.get("console.save.popup"));
/* 196 */       menu.add(this.pastebinAction).setText(Localizable.get("console.pastebin"));
/* 197 */       menu.addSeparator();
/* 198 */       menu.add(this.clearAllAction).setText(Localizable.get("console.clear.popup"));
/*     */       
/* 200 */       return menu;
/*     */     }
/*     */     
/*     */     protected void onSavingCalled() {
/* 204 */       this.explorer.setSelectedFile(new File(ConsoleFrame.this.console.getName() + ".log"));
/*     */       
/* 206 */       int result = this.explorer.showSaveDialog(ConsoleFrame.this);
/*     */       
/* 208 */       if (result != 0) {
/*     */         return;
/*     */       }
/* 211 */       File file = this.explorer.getSelectedFile();
/*     */       
/* 213 */       if (file == null) {
/* 214 */         U.log(new Object[] { "Returned NULL. Damn it!" });
/*     */         
/*     */         return;
/*     */       } 
/* 218 */       String path = file.getAbsolutePath();
/*     */       
/* 220 */       if (!path.endsWith(".log")) {
/* 221 */         path = path + ".log";
/*     */       }
/* 223 */       file = new File(path);
/*     */       
/* 225 */       OutputStream output = null;
/*     */       try {
/* 227 */         FileUtil.createFile(file);
/* 228 */         StringReader input = new StringReader(U.readFileLog());
/* 229 */         output = new BufferedOutputStream(new FileOutputStream(file));
/*     */         
/*     */         int current;
/* 232 */         while ((current = input.read()) != -1) {
/* 233 */           if (current == 10 && OS.WINDOWS.isCurrent())
/* 234 */             output.write(13); 
/* 235 */           output.write(current);
/*     */         } 
/* 237 */         output.close();
/* 238 */       } catch (Throwable throwable) {
/* 239 */         Alert.showLocError("console.save.error", throwable);
/*     */       } finally {
/* 241 */         if (output != null)
/*     */           try {
/* 243 */             output.close();
/* 244 */           } catch (IOException ignored) {
/* 245 */             ignored.printStackTrace();
/*     */           }  
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void onClearCalled() {
/* 251 */       ConsoleFrame.this.console.clear();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\console\ConsoleFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */