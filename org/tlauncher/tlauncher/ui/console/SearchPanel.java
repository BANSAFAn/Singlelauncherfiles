/*     */ package org.tlauncher.tlauncher.ui.console;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.GroupLayout;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableInvalidateTextField;
/*     */ import org.tlauncher.tlauncher.ui.swing.ImageButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.BorderPanel;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ 
/*     */ public class SearchPanel extends ExtendedPanel {
/*     */   final ConsoleFrame cf;
/*     */   public final SearchField field;
/*     */   public final SearchPrefs prefs;
/*     */   public final FindButton find;
/*     */   public final KillButton kill;
/*     */   private int startIndex;
/*     */   private int endIndex;
/*     */   private String lastText;
/*     */   private boolean lastRegexp;
/*     */   
/*     */   SearchPanel(ConsoleFrame cf) {
/*  26 */     this.cf = cf;
/*     */     
/*  28 */     this.field = new SearchField();
/*  29 */     this.prefs = new SearchPrefs();
/*     */     
/*  31 */     this.find = new FindButton();
/*  32 */     this.kill = new KillButton();
/*     */     
/*  34 */     GroupLayout layout = new GroupLayout((Container)this);
/*  35 */     setLayout(layout);
/*     */     
/*  37 */     layout.setAutoCreateContainerGaps(true);
/*     */     
/*  39 */     layout.setHorizontalGroup(layout
/*  40 */         .createSequentialGroup()
/*  41 */         .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent((Component)this.field).addComponent((Component)this.prefs))
/*  42 */         .addGap(4)
/*  43 */         .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent((Component)this.find, 48, 48, 2147483647).addComponent((Component)this.kill)));
/*     */ 
/*     */     
/*  46 */     layout.linkSize(0, new Component[] { (Component)this.find, (Component)this.kill });
/*     */     
/*  48 */     layout.setVerticalGroup(layout.createSequentialGroup()
/*  49 */         .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent((Component)this.field).addComponent((Component)this.find, 24, 24, 2147483647))
/*  50 */         .addGap(2)
/*  51 */         .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent((Component)this.prefs).addComponent((Component)this.kill)));
/*     */ 
/*     */     
/*  54 */     layout.linkSize(1, new Component[] { (Component)this.field, (Component)this.prefs, (Component)this.find, (Component)this.kill });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void search() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class SearchField
/*     */     extends LocalizableInvalidateTextField
/*     */   {
/*     */     private SearchField() {
/*  70 */       super("console.search.placeholder");
/*  71 */       addActionListener(new ActionListener()
/*     */           {
/*     */             public void actionPerformed(ActionEvent e) {
/*  74 */               SearchPanel.this.search();
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public class SearchPrefs extends BorderPanel {
/*     */     public final LocalizableCheckbox regexp;
/*     */     
/*     */     private SearchPrefs() {
/*  84 */       this.regexp = new LocalizableCheckbox("console.search.prefs.regexp");
/*  85 */       this.regexp.setFocusable(false);
/*  86 */       SearchPanel.this.field.setFont(this.regexp.getFont());
/*  87 */       setWest((Component)this.regexp);
/*     */     }
/*     */     
/*     */     public boolean getUseRegExp() {
/*  91 */       return this.regexp.isSelected();
/*     */     }
/*     */     
/*     */     public void setUseRegExp(boolean use) {
/*  95 */       this.regexp.setSelected(use);
/*     */     }
/*     */   }
/*     */   
/*     */   public class FindButton extends ImageButton {
/*     */     private FindButton() {
/* 101 */       addActionListener(new ActionListener()
/*     */           {
/*     */             public void actionPerformed(ActionEvent e) {
/* 104 */               SearchPanel.this.search();
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class KillButton
/*     */     extends ImageButton
/*     */   {
/*     */     private KillButton() {}
/*     */   }
/*     */   
/*     */   private void focus() {
/* 118 */     this.field.requestFocusInWindow();
/*     */   }
/*     */   
/*     */   private class Range
/*     */   {
/*     */     private int start;
/*     */     private int end;
/*     */     
/*     */     Range(int start, int end) {
/* 127 */       this.start = start;
/* 128 */       this.end = end;
/*     */     }
/*     */     
/*     */     boolean isCorrect() {
/* 132 */       return (this.start > 0 && this.end > this.start);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\console\SearchPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */