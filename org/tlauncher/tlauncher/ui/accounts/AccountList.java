/*     */ package org.tlauncher.tlauncher.ui.accounts;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import javax.swing.DefaultListModel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ScrollBarUI;
/*     */ import org.tlauncher.tlauncher.entity.profile.ClientProfile;
/*     */ import org.tlauncher.tlauncher.managers.ProfileManagerListener;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.scenes.AccountEditorScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.AccountCellRenderer;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ public class AccountList extends CenterPanel implements ProfileManagerListener {
/*  29 */   public static final Dimension SIZE = new Dimension(211, 171);
/*     */   
/*     */   private static final long serialVersionUID = 3280495266368287215L;
/*     */   
/*     */   public final DefaultListModel<Account> model;
/*     */   
/*     */   public final JList<Account> list;
/*     */   
/*     */   public final UpdaterButton add;
/*     */   public final UpdaterButton remove;
/*     */   private final AccountEditorScene scene;
/*     */   private final ProfileManager profileManager;
/*  41 */   private final Account temp = new Account();
/*     */   
/*     */   public AccountList(AccountEditorScene sc) {
/*  44 */     super(noInsets);
/*  45 */     setPreferredSize(SIZE);
/*  46 */     this.scene = sc;
/*  47 */     this.profileManager = TLauncher.getInstance().getProfileManager();
/*  48 */     ExtendedPanel extendedPanel1 = new ExtendedPanel(new BorderLayout(0, 0));
/*  49 */     LocalizableLabel label = new LocalizableLabel("account.list");
/*  50 */     SwingUtil.changeFontFamily((Component)label, FontTL.ROBOTO_BOLD);
/*     */     
/*  52 */     label.setFont(label.getFont().deriveFont(14.0F));
/*  53 */     label.setBorder(BorderFactory.createEmptyBorder(0, 0, 13, 0));
/*  54 */     label.setHorizontalAlignment(2);
/*  55 */     label.setVerticalAlignment(1);
/*     */     
/*  57 */     this.model = new DefaultListModel<>();
/*  58 */     this.list = new JList<>(this.model);
/*  59 */     this.list.setCellRenderer((ListCellRenderer<? super Account>)new AccountCellRenderer(AccountCellRenderer.AccountCellType.EDITOR));
/*  60 */     this.list.setSelectionMode(0);
/*  61 */     this.list.setBackground(AccountEditorScene.BACKGROUND_ACCOUNT_COLOR);
/*  62 */     this.list.addListSelectionListener(e -> {
/*     */           if (Objects.nonNull(this.list.getSelectedValue())) {
/*     */             this.scene.editor.fillFormBySelectedAccount(this.list.getSelectedValue());
/*     */           } else {
/*     */             this.scene.editor.clear();
/*     */           }  blockOrUnblock();
/*     */         });
/*  69 */     JScrollPane scroll = new JScrollPane(this.list);
/*  70 */     scroll.getVerticalScrollBar().setUI((ScrollBarUI)new AccountScrollBarUI());
/*  71 */     scroll.setBorder((Border)null);
/*  72 */     scroll.setHorizontalScrollBarPolicy(31);
/*  73 */     scroll.setVerticalScrollBarPolicy(20);
/*     */     
/*  75 */     ExtendedPanel extendedPanel2 = new ExtendedPanel();
/*  76 */     extendedPanel2.setLayout(new FlowLayout(0, 0, 0));
/*  77 */     extendedPanel2.setPreferredSize(new Dimension(SIZE.width, 26));
/*  78 */     this.add = new UpdaterButton(ExtendedButton.ORANGE_COLOR, "account.list.add");
/*  79 */     this.add.setFont(this.add.getFont().deriveFont(1, 16.0F));
/*  80 */     this.add.setForeground(Color.WHITE);
/*  81 */     this.add.setPreferredSize(new Dimension(100, 26));
/*     */     
/*  83 */     this.add.addActionListener(e -> {
/*     */           for (int i = 0; i < this.model.getSize(); i++) {
/*     */             if (((Account)this.model.getElementAt(i)).getUsername() == null)
/*     */               return; 
/*     */           } 
/*     */           this.model.addElement(this.temp);
/*     */           this.list.setSelectedValue(this.temp, true);
/*     */           defocus();
/*     */         });
/*  92 */     this.remove = new UpdaterButton(ImageUdaterButton.ORANGE_COLOR, "account.list.remove");
/*  93 */     this.remove.setPreferredSize(new Dimension(100, 26));
/*  94 */     this.remove.setFont(this.remove.getFont().deriveFont(1, 16.0F));
/*  95 */     this.remove.setForeground(Color.WHITE);
/*  96 */     this.remove.addActionListener(e -> {
/*     */           Account ac = this.list.getSelectedValue();
/*     */           if (Objects.nonNull(ac)) {
/*     */             this.model.removeElement(ac);
/*     */             try {
/*     */               this.profileManager.remove(ac);
/* 102 */             } catch (IOException exception) {
/*     */               U.log(new Object[] { exception });
/*     */             } 
/*     */             defocus();
/*     */           } 
/*     */         });
/* 108 */     extendedPanel2.add((Component)this.add);
/* 109 */     extendedPanel2.add(Box.createHorizontalStrut(11));
/* 110 */     extendedPanel2.add((Component)this.remove);
/*     */     
/* 112 */     extendedPanel1.add("South", (Component)extendedPanel2);
/* 113 */     extendedPanel1.add("Center", scroll);
/* 114 */     extendedPanel1.add("North", (Component)label);
/*     */     
/* 116 */     add((Component)extendedPanel1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTempToList() {
/* 122 */     this.model.addElement(this.temp);
/* 123 */     this.list.setSelectedValue(this.temp, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireRefreshed(ClientProfile cl) {
/* 128 */     SwingUtilities.invokeLater(() -> {
/*     */           boolean hasTemp = this.model.contains(this.temp);
/*     */           fireClientProfileChanged(cl);
/*     */           if (hasTemp) {
/*     */             addTempToList();
/*     */             blockOrUnblock();
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void fireClientProfileChanged(ClientProfile cl) {
/* 140 */     SwingUtilities.invokeLater(() -> {
/*     */           this.model.clear();
/*     */           for (Account account : cl.getAccounts().values()) {
/*     */             if (account.getShortUUID().equalsIgnoreCase(cl.getSelectedAccountUUID())) {
/*     */               this.list.setSelectedValue(account, true);
/*     */               Blocker.unblock("auth", new Blockable[] { (Blockable)this.scene.editor });
/*     */             } 
/*     */             this.model.addElement(account);
/*     */           } 
/*     */           blockOrUnblock();
/*     */         });
/*     */   }
/*     */   
/*     */   private void blockOrUnblock() {
/* 154 */     if (Objects.isNull(this.list.getSelectedValue())) {
/* 155 */       Blocker.block("auth", new Blockable[] { (Blockable)this.scene.editor });
/* 156 */     } else if (((Account)this.list.getSelectedValue()).equals(this.temp)) {
/* 157 */       Blocker.unblock("auth", new Blockable[] { (Blockable)this.scene.editor });
/*     */     } else {
/* 159 */       Blocker.unblock("auth", new Blockable[] { (Blockable)this.scene.editor });
/* 160 */       for (AccountEditor.AuthTypeRadio o : this.scene.editor.radioMap.values())
/* 161 */         o.setEnabled(false); 
/* 162 */       if (Account.AccountType.OFFICIAL_ACCOUNTS.contains(((Account)this.list.getSelectedValue()).getType()))
/* 163 */         Blocker.block("auth", new Blockable[] { (Blockable)this.scene.editor }); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setTempAccountType(Account.AccountType accountType) {
/* 168 */     this.temp.setType(accountType);
/* 169 */     this.list.revalidate();
/* 170 */     this.list.repaint();
/*     */   }
/*     */   
/*     */   public Account getAccountFromSelected() {
/* 174 */     if (this.temp.equals(this.list.getSelectedValue())) {
/* 175 */       Account ac = new Account();
/* 176 */       ac.setType(this.temp.getType());
/* 177 */       return ac;
/* 178 */     }  return this.list.getSelectedValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\accounts\AccountList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */