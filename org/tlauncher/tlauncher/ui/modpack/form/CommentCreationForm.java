/*     */ package org.tlauncher.tlauncher.ui.modpack.form;
/*     */ 
/*     */ import com.google.common.eventbus.EventBus;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.inject.Inject;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseMotionAdapter;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.tlauncher.modpack.domain.client.CommentDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.share.TopicType;
/*     */ import org.tlauncher.tlauncher.controller.CommentModpackController;
/*     */ import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
/*     */ import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.loc.ImageUdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterFullButton;
/*     */ import org.tlauncher.tlauncher.ui.model.GameEntityComment;
/*     */ import org.tlauncher.tlauncher.ui.model.InsertCommentDTO;
/*     */ import org.tlauncher.tlauncher.ui.modpack.DiscussionPanel;
/*     */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ public class CommentCreationForm
/*     */   extends ExtendedPanel {
/*  55 */   private static final Logger log = LoggerFactory.getLogger(CommentCreationForm.class); @Inject
/*     */   private CommentModpackController controller;
/*     */   @Inject
/*     */   private EventBus eventBus;
/*     */   @Inject
/*     */   private Gson gson;
/*     */   private volatile CommentDTO changedComment;
/*     */   
/*  63 */   public void setChangedComment(CommentDTO changedComment) { this.changedComment = changedComment; } public CommentDTO getChangedComment() {
/*  64 */     return this.changedComment;
/*     */   }
/*  66 */   private JTextArea textArea = new JTextArea();
/*  67 */   private JButton boldButton = (JButton)new UpdaterButton(Color.gray, "B");
/*  68 */   private JButton inclineButton = (JButton)new UpdaterButton(Color.gray, "I");
/*  69 */   private JButton underscoreButton = (JButton)new UpdaterButton(Color.gray, "U");
/*  70 */   private JButton leaveComment = (JButton)new UpdaterFullButton(new Color(54, 153, 208), ColorUtil.BACKGROUND_COMBO_BOX_POPUP_SELECTED, "modpack.comment.leave", "create-modpack.png");
/*     */   
/*  72 */   private JButton close = (JButton)new ImageUdaterButton(ColorUtil.BLUE_COLOR, "close-modpack.png"); private GameType type; private Long topicPage; private Long topicId;
/*  73 */   private static final Dimension SIZE = new Dimension(500, 300); private DiscussionPanel.Comment parent; private TopicType topicType; public void setType(GameType type) {
/*  74 */     this.type = type;
/*     */   } public void setTopicPage(Long topicPage) {
/*  76 */     this.topicPage = topicPage;
/*     */   } public void setTopicId(Long topicId) {
/*  78 */     this.topicId = topicId;
/*     */   } public void setParent(DiscussionPanel.Comment parent) {
/*  80 */     this.parent = parent;
/*     */   } public void setTopicType(TopicType topicType) {
/*  82 */     this.topicType = topicType;
/*     */   }
/*     */   
/*     */   public CommentCreationForm() {
/*  86 */     setLayout(new FlowLayout(1, 0, 0));
/*  87 */     setVisible(false);
/*  88 */     JPanel pp = new JPanel(new FlowLayout(1, 0, 140));
/*  89 */     LocalizableLabel title = new LocalizableLabel("modpack.comment.creation");
/*  90 */     pp.setBackground(new Color(1, 1, 1, 100));
/*  91 */     pp.setOpaque(true);
/*  92 */     pp.setPreferredSize(ModpackScene.SIZE);
/*  93 */     setSize(ModpackScene.SIZE);
/*  94 */     add(pp);
/*  95 */     JPanel panel = new JPanel();
/*  96 */     JPanel upPanel = new JPanel(new FlowLayout(1, 0, 0));
/*  97 */     ExtendedPanel extendedPanel = new ExtendedPanel(new BorderLayout(0, 0));
/*  98 */     upPanel.setBackground(ColorUtil.BLUE_COLOR);
/*  99 */     extendedPanel.setPreferredSize(new Dimension(500, 45));
/* 100 */     upPanel.setPreferredSize(new Dimension(500, 45));
/* 101 */     this.close.setPreferredSize(new Dimension(41, 45));
/* 102 */     title.setPreferredSize(new Dimension(200, 45));
/* 103 */     title.setHorizontalAlignment(0);
/* 104 */     panel.setBackground(ColorUtil.BLUE_COLOR);
/* 105 */     BoxLayout l = new BoxLayout(panel, 1);
/* 106 */     panel.setOpaque(true);
/* 107 */     panel.setBackground(new Color(213, 213, 213));
/* 108 */     panel.setLayout(l);
/* 109 */     JPanel buttons = new JPanel(new FlowLayout(0));
/* 110 */     buttons.setOpaque(false);
/* 111 */     buttons.add(this.boldButton);
/* 112 */     buttons.add(this.inclineButton);
/* 113 */     buttons.add(this.underscoreButton);
/*     */     
/* 115 */     extendedPanel.add((Component)title, "Center");
/* 116 */     extendedPanel.add(this.close, "East");
/* 117 */     upPanel.add((Component)extendedPanel);
/* 118 */     panel.add(upPanel);
/* 119 */     panel.add(buttons);
/* 120 */     panel.add((Component)ModpackScene.createScrollWrapper(this.textArea));
/*     */     
/* 122 */     this.close.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/* 125 */             CommentCreationForm.this.close.setBackground(new Color(60, 145, 193));
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/* 130 */             CommentCreationForm.this.close.setBackground(ColorUtil.BLUE_COLOR_UNDER);
/*     */           }
/*     */         });
/* 133 */     this.close.addActionListener(e -> setVisible(false));
/*     */ 
/*     */ 
/*     */     
/* 137 */     JPanel p1 = new JPanel();
/* 138 */     p1.setOpaque(false);
/* 139 */     p1.add(this.leaveComment);
/* 140 */     panel.add(p1);
/* 141 */     this.leaveComment.setPreferredSize(new Dimension(200, 50));
/*     */     
/* 143 */     panel.setPreferredSize(SIZE);
/* 144 */     pp.add(panel);
/*     */     
/* 146 */     this.boldButton.addActionListener(e -> wrapText("b"));
/* 147 */     this.inclineButton.addActionListener(e -> wrapText("em"));
/* 148 */     this.underscoreButton.addActionListener(e -> wrapText("u"));
/* 149 */     this.textArea.setWrapStyleWord(true);
/* 150 */     this.textArea.setLineWrap(true);
/*     */     
/* 152 */     SwingUtil.changeFontFamily(this.leaveComment, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/* 153 */     SwingUtil.changeFontFamily(this.boldButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/* 154 */     SwingUtil.changeFontFamily(this.inclineButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/* 155 */     SwingUtil.changeFontFamily(this.underscoreButton, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/* 156 */     SwingUtil.changeFontFamily((JComponent)title, FontTL.ROBOTO_REGULAR, 14, Color.WHITE);
/* 157 */     this.leaveComment.addMouseListener(new MouseAdapter() {
/*     */           public void mousePressed(MouseEvent e) {
/* 159 */             if (!Pattern.matches("^(?!<em></em>|<i></i>|<u></u>|<b></b>)([\\w\\sа-яА-ЯёЁ\\.\\-–:; «»·,*!+…'?%@)(/]|<b>|</b>|<i>|</i>|<u>|</u>|<em>|</em>)+(?<!<em></em>|<i></i>|<u></u>|<b></b>){1,2000}$", CommentCreationForm.this.textArea.getText())) {
/* 160 */               CommentCreationForm.this.textArea.setBorder(BorderFactory.createLineBorder(Color.red, 1));
/*     */               return;
/*     */             } 
/* 163 */             CommentCreationForm.this.leaveComment.setEnabled(false);
/* 164 */             CompletableFuture.runAsync(() -> {
/*     */                   try {
/*     */                     if (Objects.isNull(CommentCreationForm.this.changedComment)) {
/*     */                       String res = CommentCreationForm.this.controller.saveComment(CommentCreationForm.this.textArea.getText(), CommentCreationForm.this.topicType, TLauncher.getInstance().getLang().isUSSRLocale() ? "ru" : "en", CommentCreationForm.this.type, CommentCreationForm.this.topicPage, CommentCreationForm.this.topicId);
/*     */ 
/*     */                       
/*     */                       SwingUtilities.invokeLater(());
/*     */                     } else {
/*     */                       GameEntityComment gec = new GameEntityComment();
/*     */ 
/*     */                       
/*     */                       gec.setDescription(CommentCreationForm.this.textArea.getText());
/*     */ 
/*     */                       
/*     */                       CommentDTO uc = CommentCreationForm.this.controller.update(CommentCreationForm.this.changedComment.getId(), gec);
/*     */                       
/*     */                       CommentCreationForm.this.eventBus.post(uc);
/*     */                     } 
/* 182 */                   } catch (RequiredTLAccountException ex) {
/*     */ 
/*     */                     
/*     */                     Alert.showLocError("modpack.right.panel.required.tl.account.title", Localizable.get("modpack.right.panel.required.tl.account", new Object[] { Localizable.get("loginform.button.settings.account") }), null);
/*     */                   }
/* 187 */                   catch (SelectedAnyOneTLAccountException ex) {
/*     */                     
/*     */                     Alert.showLocError("modpack.right.panel.required.tl.account.title", "modpack.right.panel.select.account.tl", null);
/* 190 */                   } catch (Throwable ex) {
/*     */                     CommentCreationForm.log.warn("error", ex);
/*     */ 
/*     */                     
/*     */                     Alert.showLocMessage("modpack.remote.not.found", "modpack.try.later", null);
/*     */                   } 
/*     */                   
/*     */                   SwingUtilities.invokeLater(());
/*     */                 });
/*     */           }
/*     */         });
/*     */     
/* 202 */     pp.addMouseMotionListener(new MouseMotionAdapter() {  }
/*     */       );
/* 204 */     this.textArea.addFocusListener(new FocusAdapter()
/*     */         {
/*     */           public void focusGained(FocusEvent e) {
/* 207 */             CommentCreationForm.this.textArea.setBorder(BorderFactory.createEmptyBorder());
/*     */           }
/*     */         });
/* 210 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentShown(ComponentEvent e) {
/* 213 */             CommentCreationForm.this.textArea.setBorder(BorderFactory.createEmptyBorder());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void wrapText(String tag) {
/* 219 */     String value = "<%s>%s</%s>";
/* 220 */     if (this.textArea.getSelectionStart() == this.textArea.getSelectionEnd()) {
/* 221 */       int index = this.textArea.getCaretPosition();
/* 222 */       this.textArea.insert(String.format(value, new Object[] { tag, "", tag }), index);
/*     */     } else {
/* 224 */       this.textArea.insert(String.format("<%s>", new Object[] { tag }), this.textArea.getSelectionStart());
/* 225 */       this.textArea.insert(String.format("</%s>", new Object[] { tag }), this.textArea.getSelectionEnd());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void preparedForNewComment() {
/* 230 */     this.parent = null;
/* 231 */     this.textArea.setText("");
/* 232 */     if (Objects.nonNull(this.changedComment))
/* 233 */       this.textArea.setText(this.changedComment.getDescription()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\form\CommentCreationForm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */