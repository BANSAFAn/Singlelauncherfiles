/*     */ package org.tlauncher.tlauncher.ui.accounts;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Authenticator;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.center.CenterPanel;
/*     */ import org.tlauncher.tlauncher.ui.editor.EditorCheckBox;
/*     */ import org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableRadioButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.scenes.AccountEditorScene;
/*     */ import org.tlauncher.tlauncher.ui.scenes.PseudoScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.CheckBoxListener;
/*     */ import org.tlauncher.tlauncher.ui.swing.FlexibleEditorPanel;
/*     */ import org.tlauncher.tlauncher.ui.text.ExtendedPasswordField;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AccountEditor
/*     */   extends CenterPanel
/*     */   implements AuthenticatorListener
/*     */ {
/*     */   private static final String passlock = "passlock";
/*     */   private final AccountEditorScene scene;
/*     */   public final UsernameField username;
/*     */   public final BlockablePasswordField password;
/*     */   public final ButtonGroup authGroup;
/*  55 */   public static final Dimension SIZE = new Dimension(244, 221);
/*     */   
/*     */   public final UpdaterButton save;
/*     */   public final LinkedHashMap<Account.AccountType, AuthTypeRadio> radioMap;
/*     */   public final EditorCheckBox skinCheckBox;
/*  60 */   public static final Color FIELD_COLOR = new Color(149, 149, 149); public final AuthTypeRadio mojangAuth; public final AuthTypeRadio tlauncherAuth; public final AuthTypeRadio microsoftAuth;
/*     */   public final AuthTypeRadio freeAuth;
/*     */   private final Configuration configuration;
/*     */   private final FlexibleEditorPanel flex;
/*  64 */   private final JProgressBar bar = new JProgressBar();
/*     */   
/*     */   public AccountEditor(final AccountEditorScene sc, FlexibleEditorPanel flex) {
/*  67 */     super(noInsets);
/*  68 */     this.bar.setIndeterminate(true);
/*  69 */     this.bar.setVisible(false);
/*  70 */     this.configuration = TLauncher.getInstance().getConfiguration();
/*  71 */     this.scene = sc;
/*  72 */     this.flex = flex;
/*     */     
/*  74 */     this.username = new UsernameField(this, UsernameField.UsernameState.EMAIL);
/*  75 */     Border empty = new EmptyBorder(0, 9, 0, 0);
/*  76 */     CompoundBorder border = new CompoundBorder(BorderFactory.createLineBorder(FIELD_COLOR, 1), empty);
/*  77 */     this.username.setBorder(border);
/*  78 */     this.username.setForeground(FIELD_COLOR);
/*     */     
/*  80 */     this.password = new BlockablePasswordField();
/*  81 */     this.password.setBorder(border);
/*  82 */     this.password.setEnabled(false);
/*  83 */     this.password.setForeground(FIELD_COLOR);
/*     */     
/*  85 */     this.authGroup = new ButtonGroup();
/*  86 */     this.radioMap = new LinkedHashMap<>();
/*  87 */     this.mojangAuth = new AuthTypeRadio(Account.AccountType.MOJANG);
/*  88 */     this.mojangAuth.setFont(this.mojangAuth.getFont().deriveFont(1));
/*  89 */     this.tlauncherAuth = new AuthTypeRadio(Account.AccountType.TLAUNCHER);
/*  90 */     this.tlauncherAuth.setFont(this.tlauncherAuth.getFont().deriveFont(1));
/*  91 */     this.microsoftAuth = new AuthTypeRadio(Account.AccountType.MICROSOFT);
/*  92 */     this.microsoftAuth.setFont(this.microsoftAuth.getFont().deriveFont(1));
/*  93 */     this.freeAuth = new AuthTypeRadio(Account.AccountType.FREE);
/*  94 */     this.freeAuth.setFont(this.freeAuth.getFont().deriveFont(1));
/*  95 */     this.save = new UpdaterButton(UpdaterButton.GREEN_COLOR, "account.save");
/*  96 */     this.save.setFont(this.save.getFont().deriveFont(1, 16.0F));
/*  97 */     this.save.setForeground(Color.WHITE);
/*     */     
/*  99 */     this.skinCheckBox = new EditorCheckBox("skin.status.checkbox")
/*     */       {
/*     */         public void block(Object reason) {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void unblock(Object reason) {}
/*     */       };
/* 108 */     this.skinCheckBox.setState(this.configuration.getBoolean("skin.status.checkbox.state"));
/* 109 */     this.skinCheckBox.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseReleased(MouseEvent e) {
/* 112 */             if (SwingUtilities.isLeftMouseButton(e)) {
/* 113 */               if (!AccountEditor.this.skinCheckBox.getState()) {
/* 114 */                 if (Alert.showLocQuestion("skin.status.title", "skin.status.message")) {
/* 115 */                   AccountEditor.this.configuration.set("skin.status.checkbox.state", Boolean.valueOf(false), true);
/*     */                 } else {
/* 117 */                   AccountEditor.this.skinCheckBox.setState(true);
/*     */                 } 
/*     */               } else {
/* 120 */                 Alert.showMessage(AccountEditor.this.lang.get("skin.checkbox.switch.on.title"), AccountEditor.this.lang
/* 121 */                     .get("skin.checkbox.switch.on.message"));
/* 122 */                 AccountEditor.this.configuration.set("skin.status.checkbox.state", Boolean.valueOf(true), true);
/*     */               } 
/*     */             }
/*     */           }
/*     */         });
/*     */     
/* 128 */     setPreferredSize(SIZE);
/* 129 */     SpringLayout springLayout = new SpringLayout();
/* 130 */     setLayout(springLayout);
/*     */     
/* 132 */     UsernameField usernameField = this.username;
/* 133 */     springLayout.putConstraint("North", (Component)usernameField, 0, "North", (Component)this);
/* 134 */     springLayout.putConstraint("South", (Component)usernameField, 20, "North", (Component)usernameField);
/* 135 */     springLayout.putConstraint("West", (Component)usernameField, 0, "West", (Component)this);
/* 136 */     springLayout.putConstraint("East", (Component)usernameField, SIZE.width, "West", (Component)this);
/* 137 */     add((Component)usernameField);
/*     */     
/* 139 */     AuthTypeRadio authTypeRadio1 = this.tlauncherAuth;
/* 140 */     springLayout.putConstraint("North", (Component)authTypeRadio1, 12, "South", (Component)usernameField);
/* 141 */     springLayout.putConstraint("South", (Component)authTypeRadio1, 15, "North", (Component)authTypeRadio1);
/* 142 */     springLayout.putConstraint("West", (Component)authTypeRadio1, -4, "West", (Component)usernameField);
/*     */     
/* 144 */     springLayout.putConstraint("East", (Component)authTypeRadio1, 8, "East", (Component)usernameField);
/*     */     
/* 146 */     add((Component)authTypeRadio1);
/*     */     
/* 148 */     AuthTypeRadio authTypeRadio2 = this.microsoftAuth;
/* 149 */     springLayout.putConstraint("North", (Component)authTypeRadio2, 12, "South", (Component)authTypeRadio1);
/* 150 */     springLayout.putConstraint("South", (Component)authTypeRadio2, 15, "North", (Component)authTypeRadio2);
/* 151 */     springLayout.putConstraint("West", (Component)authTypeRadio2, -4, "West", (Component)usernameField);
/* 152 */     springLayout.putConstraint("East", (Component)authTypeRadio2, 0, "East", (Component)usernameField);
/* 153 */     add((Component)authTypeRadio2);
/*     */     
/* 155 */     AuthTypeRadio authTypeRadio3 = this.freeAuth;
/* 156 */     springLayout.putConstraint("North", (Component)authTypeRadio3, 12, "South", (Component)authTypeRadio2);
/* 157 */     springLayout.putConstraint("South", (Component)authTypeRadio3, 15, "North", (Component)authTypeRadio3);
/* 158 */     springLayout.putConstraint("West", (Component)authTypeRadio3, -4, "West", (Component)usernameField);
/* 159 */     springLayout.putConstraint("East", (Component)authTypeRadio3, 0, "East", (Component)usernameField);
/* 160 */     add((Component)authTypeRadio3);
/* 161 */     AuthTypeRadio authTypeRadio4 = this.mojangAuth;
/* 162 */     springLayout.putConstraint("North", (Component)authTypeRadio4, 12, "South", (Component)authTypeRadio3);
/* 163 */     springLayout.putConstraint("South", (Component)authTypeRadio4, 15, "North", (Component)authTypeRadio4);
/* 164 */     springLayout.putConstraint("West", (Component)authTypeRadio4, -4, "West", (Component)usernameField);
/* 165 */     springLayout.putConstraint("East", (Component)authTypeRadio4, 0, "East", (Component)usernameField);
/* 166 */     add((Component)authTypeRadio4);
/*     */     
/* 168 */     BlockablePasswordField blockablePasswordField = this.password;
/* 169 */     springLayout.putConstraint("North", (Component)blockablePasswordField, 12, "South", (Component)authTypeRadio4);
/* 170 */     springLayout.putConstraint("South", (Component)blockablePasswordField, 19, "North", (Component)blockablePasswordField);
/* 171 */     springLayout.putConstraint("West", (Component)blockablePasswordField, 0, "West", (Component)usernameField);
/* 172 */     springLayout.putConstraint("East", (Component)blockablePasswordField, 0, "East", (Component)usernameField);
/* 173 */     add((Component)blockablePasswordField);
/*     */     
/* 175 */     EditorCheckBox editorCheckBox = this.skinCheckBox;
/* 176 */     springLayout.putConstraint("North", (Component)editorCheckBox, 12, "South", (Component)blockablePasswordField);
/* 177 */     springLayout.putConstraint("South", (Component)editorCheckBox, 15, "North", (Component)editorCheckBox);
/* 178 */     springLayout.putConstraint("West", (Component)editorCheckBox, -4, "West", (Component)usernameField);
/* 179 */     springLayout.putConstraint("East", (Component)editorCheckBox, 0, "East", (Component)usernameField);
/* 180 */     add((Component)editorCheckBox);
/*     */     
/* 182 */     addPanel4((JComponent)this.save, springLayout, (JComponent)usernameField, (JComponent)editorCheckBox);
/* 183 */     addPanel4(this.bar, springLayout, (JComponent)usernameField, (JComponent)editorCheckBox);
/*     */     
/* 185 */     this.save.addActionListener(e -> {
/*     */           defocus();
/*     */           
/*     */           Account acc = get();
/*     */           if (acc.getUsername() == null && !acc.getType().equals(Account.AccountType.MICROSOFT)) {
/*     */             Alert.showLocError("auth.error.email.account");
/*     */             return;
/*     */           } 
/*     */           if (acc.getAccessToken() == null && acc.getPassword() == null && !acc.getType().equals(Account.AccountType.FREE)) {
/*     */             Alert.showLocError("auth.error.nopass");
/*     */             return;
/*     */           } 
/*     */           Authenticator.authenticate(acc, this);
/*     */         });
/* 199 */     this.microsoftAuth.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseClicked(MouseEvent e) {
/* 202 */             if (SwingUtilities.isLeftMouseButton(e) && AccountEditor.this.microsoftAuth.isEnabled()) {
/* 203 */               sc.getMainPane().setScene((PseudoScene)(sc.getMainPane()).microsoftAuthScene);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void addPanel4(JComponent component, SpringLayout springLayout, JComponent panel, JComponent panel_5) {
/* 211 */     springLayout.putConstraint("North", component, 9, "South", panel_5);
/* 212 */     springLayout.putConstraint("South", component, 26, "North", component);
/* 213 */     springLayout.putConstraint("West", component, 0, "West", panel);
/* 214 */     springLayout.putConstraint("East", component, 0, "East", panel);
/* 215 */     add(component);
/*     */   }
/*     */   
/*     */   public void setSelectedAccountType(Account.AccountType type) {
/* 219 */     AuthTypeRadio selectable = this.radioMap.get(type);
/* 220 */     if (selectable != null)
/* 221 */       selectable.setSelected(true); 
/*     */   }
/*     */   
/*     */   public void clear() {
/* 225 */     setSelectedAccountType(Account.AccountType.TLAUNCHER);
/* 226 */     this.username.setText(null);
/* 227 */     this.password.setText(null);
/* 228 */     this.scene.list.setTempAccountType(Account.AccountType.TLAUNCHER);
/*     */   }
/*     */ 
/*     */   
/*     */   public Account get() {
/* 233 */     Account account = this.scene.list.getAccountFromSelected();
/* 234 */     account.setUsername(this.username.getValue());
/*     */     
/* 236 */     switch (account.getType()) {
/*     */       case TLAUNCHER:
/*     */       case MOJANG:
/* 239 */         if (this.password.hasPassword()) {
/* 240 */           account.setPassword(new String(this.password.getPassword()));
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 247 */     return account;
/*     */   }
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 252 */     super.block(reason);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 259 */     super.unblock(reason);
/*     */   }
/*     */   
/*     */   public void fillFormBySelectedAccount(Account selectedValue) {
/* 263 */     this.username.setText(selectedValue.getDisplayName());
/* 264 */     this.authGroup.setSelected(((AuthTypeRadio)this.radioMap.get(selectedValue.getType())).getModel(), true);
/*     */   }
/*     */   
/*     */   private class BlockablePasswordField extends ExtendedPasswordField implements Blockable { private BlockablePasswordField() {}
/*     */     
/*     */     public void block(Object reason) {
/* 270 */       setEnabled(false);
/*     */     }
/*     */ 
/*     */     
/*     */     public void unblock(Object reason) {
/* 275 */       setEnabled(true);
/*     */     } }
/*     */ 
/*     */   
/*     */   public class AuthTypeRadio extends LocalizableRadioButton {
/*     */     private final Account.AccountType type;
/*     */     
/*     */     private AuthTypeRadio(final Account.AccountType type) {
/* 283 */       super("account.auth." + type.toString().toLowerCase(Locale.ROOT));
/* 284 */       AccountEditor.this.radioMap.put(type, this);
/* 285 */       AccountEditor.this.authGroup.add((AbstractButton)this);
/*     */       
/* 287 */       this.type = type;
/* 288 */       setFocusable(false);
/*     */       
/* 290 */       addItemListener((ItemListener)new CheckBoxListener()
/*     */           {
/*     */             public void itemStateChanged(boolean newstate) {
/* 293 */               if (newstate) {
/* 294 */                 AccountEditor.this.scene.list.setTempAccountType(type);
/*     */               }
/* 296 */               if (newstate && !AccountEditor.this.password.hasPassword()) {
/* 297 */                 AccountEditor.this.password.setText(null);
/*     */               }
/* 299 */               if (newstate) {
/* 300 */                 AccountEditor.this.scene.tip.setAccountType(type);
/* 301 */                 AccountEditor.this.flex.setText(Localizable.get("auth.tip." + type.name().toLowerCase(Locale.ROOT)));
/*     */               } 
/*     */               
/* 304 */               if (Account.AccountType.FREE == type && newstate) {
/* 305 */                 Blocker.setBlocked(AccountEditor.this.password, "passlock", true);
/*     */               } else {
/* 307 */                 Blocker.setBlocked(AccountEditor.this.password, "passlock", false);
/* 308 */               }  AccountEditor.this.username.setState(newstate ? UsernameField.UsernameState.USERNAME : UsernameField.UsernameState.EMAIL_LOGIN);
/* 309 */               if (AccountEditor.this.mojangAuth.isSelected()) {
/* 310 */                 AccountEditor.this.username.setState(UsernameField.UsernameState.EMAIL);
/*     */               }
/* 312 */               AccountEditor.this.defocus();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public Account.AccountType getAccountType() {
/* 318 */       return this.type;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAuthPassing(Authenticator auth) {
/* 324 */     this.save.setVisible(false);
/* 325 */     this.bar.setVisible(true);
/* 326 */     this.bar.setValue(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onAuthPassingError(Authenticator auth, Exception e) {
/* 332 */     this.bar.setVisible(false);
/* 333 */     this.save.setVisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAuthPassed(Authenticator auth) {
/* 338 */     this.bar.setVisible(false);
/* 339 */     this.save.setVisible(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\accounts\AccountEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */