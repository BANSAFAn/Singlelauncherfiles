/*     */ package org.tlauncher.tlauncher.ui.modpack;
/*     */ 
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import com.google.common.eventbus.Subscribe;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.name.Named;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.IOException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.tlauncher.modpack.domain.client.CommentDTO;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameEntitySort;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.share.TopicType;
/*     */ import org.tlauncher.modpack.domain.client.site.CommonPage;
/*     */ import org.tlauncher.tlauncher.controller.CommentModpackController;
/*     */ import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableLabel;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.model.CurrentUserPosition;
/*     */ import org.tlauncher.tlauncher.ui.model.InsertCommentDTO;
/*     */ import org.tlauncher.tlauncher.ui.modpack.form.CommentCreationForm;
/*     */ import org.tlauncher.tlauncher.ui.scenes.ModpackScene;
/*     */ import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*     */ import org.tlauncher.util.ColorUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DiscussionPanel
/*     */   extends ExtendedPanel
/*     */ {
/*  70 */   private static final Logger log = LoggerFactory.getLogger(DiscussionPanel.class); private static final long serialVersionUID = -6938564413758462564L; private GameEntityDTO dto; private GameType type; private boolean first;
/*     */   @Inject
/*     */   private CommentModpackController controller;
/*     */   @Inject
/*     */   @Named("singleDownloadExecutor")
/*     */   private Executor singleDownloadExecutor;
/*  76 */   private static final Object ob = new Object(); private CommentCreationForm commentCreationForm; private JScrollPane scrollPane;
/*     */   public void setDto(GameEntityDTO dto) {
/*  78 */     this.dto = dto;
/*     */   } public void setType(GameType type) {
/*  80 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentCreationForm(CommentCreationForm commentCreationForm) {
/*  88 */     this.commentCreationForm = commentCreationForm;
/*     */   }
/*     */   public JScrollPane getScrollPane() {
/*  91 */     return this.scrollPane;
/*     */   }
/*  93 */   public final SimpleDateFormat format = new SimpleDateFormat(" dd.MM.YYYY HH:MM", Localizable.get().getSelected());
/*  94 */   private int oneGapeCommentWidth = 25;
/*  95 */   private int commentDescriptionWidth = 695;
/*  96 */   int v = 210;
/*  97 */   private JButton addedComment = (JButton)new UpdaterButton(new Color(this.v, this.v, this.v), new Color(this.v - 30, this.v - 30, this.v - 30), "modpack.comment.leave");
/*     */   
/*     */   private Component gapeSpace;
/*     */   
/*     */   public DiscussionPanel() {
/* 102 */     setLayout(new BoxLayout((Container)this, 3));
/* 103 */     this.scrollPane = (JScrollPane)ModpackScene.createScrollWrapper((JComponent)this);
/* 104 */     this.scrollPane.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentShown(ComponentEvent e) {
/* 107 */             if (DiscussionPanel.this.first)
/*     */               return; 
/* 109 */             DiscussionPanel.this.preparingContent(DiscussionPanel.this.dto.getId(), 0, 1, TopicType.GAME_ENTITY, 0);
/*     */           }
/*     */         });
/* 112 */     this.scrollPane.setPreferredSize(new Dimension(ModpackScene.SIZE.width, 500));
/* 113 */     ExtendedPanel extendedPanel = new ExtendedPanel(new FlowLayout(2, 0, 0));
/* 114 */     extendedPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 15, 92));
/* 115 */     extendedPanel.add(this.addedComment);
/* 116 */     extendedPanel.setBackground(Color.GREEN);
/* 117 */     SwingUtil.changeFontFamily(this.addedComment, FontTL.ROBOTO_REGULAR, 15, Color.WHITE);
/* 118 */     add((Component)extendedPanel);
/* 119 */     this.addedComment.addActionListener(c -> SwingUtilities.invokeLater(()));
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
/* 130 */     this.gapeSpace = Box.createVerticalStrut(1);
/*     */   } public class Comment extends ExtendedPanel {
/*     */     private static final long serialVersionUID = 6313239430482145868L; private int gape;
/* 133 */     public void setGape(int gape) { this.gape = gape; } private volatile CommentDTO comment; private JPanel centerComment; private HtmlTextPane descriptionFull; public void setComment(CommentDTO comment) { this.comment = comment; } public void setCenterComment(JPanel centerComment) { this.centerComment = centerComment; } public void setDescriptionFull(HtmlTextPane descriptionFull) { this.descriptionFull = descriptionFull; } public String toString() { return "DiscussionPanel.Comment(gape=" + getGape() + ", comment=" + getComment() + ", centerComment=" + getCenterComment() + ", descriptionFull=" + getDescriptionFull() + ")"; }
/* 134 */     public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Comment)) return false;  Comment other = (Comment)o; if (!other.canEqual(this)) return false;  if (!super.equals(o)) return false;  if (getGape() != other.getGape()) return false;  Object this$comment = getComment(), other$comment = other.getComment(); if ((this$comment == null) ? (other$comment != null) : !this$comment.equals(other$comment)) return false;  Object this$centerComment = getCenterComment(), other$centerComment = other.getCenterComment(); if ((this$centerComment == null) ? (other$centerComment != null) : !this$centerComment.equals(other$centerComment)) return false;  Object this$descriptionFull = getDescriptionFull(), other$descriptionFull = other.getDescriptionFull(); return !((this$descriptionFull == null) ? (other$descriptionFull != null) : !this$descriptionFull.equals(other$descriptionFull)); } protected boolean canEqual(Object other) { return other instanceof Comment; } public int hashCode() { int PRIME = 59; result = super.hashCode(); result = result * 59 + getGape(); Object $comment = getComment(); result = result * 59 + (($comment == null) ? 43 : $comment.hashCode()); Object $centerComment = getCenterComment(); result = result * 59 + (($centerComment == null) ? 43 : $centerComment.hashCode()); Object $descriptionFull = getDescriptionFull(); return result * 59 + (($descriptionFull == null) ? 43 : $descriptionFull.hashCode()); }
/*     */ 
/*     */     
/*     */     public int getGape() {
/* 138 */       return this.gape;
/* 139 */     } public CommentDTO getComment() { return this.comment; }
/* 140 */     public JPanel getCenterComment() { return this.centerComment; } public HtmlTextPane getDescriptionFull() {
/* 141 */       return this.descriptionFull;
/*     */     }
/*     */     public Comment(final CommentDTO c, int gape, CommentDTO parent) {
/* 144 */       setBorder(BorderFactory.createEmptyBorder(0, 25 + gape * DiscussionPanel.this.oneGapeCommentWidth, 5, 0));
/* 145 */       this.gape = gape;
/* 146 */       this.comment = c;
/* 147 */       setLayout(new BorderLayout());
/* 148 */       ButtonGroup bg = new ButtonGroup();
/* 149 */       JLabel authorImage = new JLabel();
/* 150 */       final LocalizableLabel response = new LocalizableLabel("modpack.response");
/*     */       
/* 152 */       JLabel author = new JLabel(c.getAuthor());
/* 153 */       JLabel commentDate = new JLabel(DiscussionPanel.this.format.format(new Date(c.getUpdated().longValue())));
/*     */       
/* 155 */       ExtendedPanel extendedPanel1 = new ExtendedPanel(new FlowLayout(0, 0, 0));
/* 156 */       this.centerComment = (JPanel)new ExtendedPanel(new BorderLayout());
/* 157 */       ExtendedPanel extendedPanel2 = new ExtendedPanel(new FlowLayout(0, 0, 0));
/* 158 */       ExtendedPanel extendedPanel3 = new ExtendedPanel();
/*     */       
/* 160 */       BoxLayout bl = new BoxLayout((Container)extendedPanel3, 1);
/* 161 */       extendedPanel3.setLayout(bl);
/* 162 */       UserPositionCommentCheckbox dislike = new UserPositionCommentCheckbox("modpack/icon-dislike-whole.png", "modpack/icon-dislike.png", bg, false);
/*     */       
/* 164 */       UserPositionCommentCheckbox like = new UserPositionCommentCheckbox("modpack/icon-like-whole.png", "modpack/icon-like.png", bg, true);
/*     */       
/* 166 */       CurrentUserPosition userPos = new CurrentUserPosition();
/*     */       
/* 168 */       if (Objects.nonNull(c.getAuthorPosition())) {
/* 169 */         if (c.getAuthorPosition().isPosition()) {
/* 170 */           like.setSelected(true);
/* 171 */           userPos.setGood((byte)1);
/*     */         } else {
/* 173 */           dislike.setSelected(true);
/* 174 */           userPos.setBad((byte)1);
/*     */         } 
/*     */       }
/* 177 */       updateText();
/* 178 */       createUserPosition(c, dislike, userPos);
/* 179 */       createUserPosition(c, like, userPos);
/*     */       
/* 181 */       bg.add((AbstractButton)dislike);
/* 182 */       bg.add((AbstractButton)like);
/* 183 */       extendedPanel3.add(Box.createVerticalStrut(10));
/* 184 */       extendedPanel3.add((Component)dislike);
/* 185 */       extendedPanel3.add((Component)like);
/*     */       
/* 187 */       extendedPanel3.setPreferredSize(new Dimension(100, 50));
/*     */       
/* 189 */       ExtendedPanel extendedPanel4 = new ExtendedPanel(new BorderLayout());
/* 190 */       extendedPanel4.setPreferredSize(new Dimension(45, 45));
/*     */       
/* 192 */       authorImage.setPreferredSize(new Dimension(45, 30));
/* 193 */       extendedPanel4.add(authorImage, "North");
/*     */       
/* 195 */       extendedPanel2.add((Component)localizableLabel);
/* 196 */       if (c.isEdited()) {
/* 197 */         LocalizableLabel localizableLabel1 = new LocalizableLabel("modpack.popup.delete");
/* 198 */         extendedPanel2.add((Component)localizableLabel1);
/* 199 */         localizableLabel1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
/* 200 */         SwingUtil.changeFontFamily((JComponent)localizableLabel1, FontTL.ROBOTO_REGULAR, 15, Color.RED);
/* 201 */         localizableLabel1.addMouseListener(new MouseAdapter()
/*     */             {
/*     */               public void mousePressed(MouseEvent e)
/*     */               {
/* 205 */                 if (SwingUtilities.isLeftMouseButton(e)) {
/* 206 */                   int res = Alert.showConfirmDialog(0, 2, "", 
/* 207 */                       Localizable.get("modpack.comment.delete"), null, Localizable.get("ui.yes"), 
/* 208 */                       Localizable.get("ui.no"));
/* 209 */                   if (res != 0)
/*     */                     return; 
/* 211 */                   CompletableFuture.runAsync(() -> {
/*     */                         try {
/*     */                           DiscussionPanel.this.controller.delete(c.getId(), DiscussionPanel.this.dto.getId());
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
/*     */                           SwingUtilities.invokeLater(());
/* 225 */                         } catch (SelectedAnyOneTLAccountException|org.tlauncher.tlauncher.exceptions.RequiredTLAccountException e1) {
/*     */                           
/*     */                           Alert.showLocError("modpack.right.panel.required.tl.account.title", "modpack.right.panel.select.account.tl", null);
/* 228 */                         } catch (IOException ex) {
/*     */                           DiscussionPanel.log.warn("error", ex);
/*     */                           
/*     */                           Alert.showLocMessage("", "modpack.try.later", null);
/*     */                         } 
/*     */                       });
/*     */                 } 
/*     */               }
/*     */             });
/*     */       } 
/* 238 */       extendedPanel1.add(author);
/* 239 */       extendedPanel1.add(commentDate);
/* 240 */       if (c.isEdited()) {
/* 241 */         LocalizableLabel localizableLabel1 = new LocalizableLabel("settings.change");
/* 242 */         extendedPanel1.add((Component)localizableLabel1);
/* 243 */         SwingUtil.changeFontFamily((JComponent)localizableLabel1, FontTL.ROBOTO_REGULAR, 15, Color.GRAY);
/* 244 */         localizableLabel1.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
/* 245 */         localizableLabel1.addMouseListener(new MouseAdapter()
/*     */             {
/*     */               public void mousePressed(MouseEvent e) {
/* 248 */                 if (SwingUtilities.isLeftMouseButton(e)) {
/* 249 */                   SwingUtilities.invokeLater(() -> {
/*     */                         DiscussionPanel.this.commentCreationForm.setChangedComment(DiscussionPanel.Comment.this.comment);
/*     */                         
/*     */                         DiscussionPanel.this.commentCreationForm.preparedForNewComment();
/*     */                         DiscussionPanel.this.commentCreationForm.setTopicPage(DiscussionPanel.this.dto.getId());
/*     */                         DiscussionPanel.this.commentCreationForm.setTopicId(DiscussionPanel.Comment.this.comment.getId());
/*     */                         DiscussionPanel.this.commentCreationForm.setType(null);
/*     */                         DiscussionPanel.this.commentCreationForm.setTopicType(null);
/*     */                         DiscussionPanel.this.commentCreationForm.setVisible(true);
/*     */                       });
/*     */                 }
/*     */               }
/*     */             });
/*     */       } 
/* 263 */       this.centerComment.add((Component)extendedPanel1, "North");
/* 264 */       this.centerComment.add((Component)this.descriptionFull, "Center");
/* 265 */       this.centerComment.add((Component)extendedPanel2, "South");
/* 266 */       add((Component)extendedPanel4, "West");
/* 267 */       add(this.centerComment, "Center");
/* 268 */       add((Component)extendedPanel3, "East");
/*     */       
/* 270 */       authorImage.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
/* 271 */       like.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
/*     */       
/* 273 */       SwingUtil.changeFontFamily(author, FontTL.ROBOTO_REGULAR, 15, ColorUtil.COLOR_25);
/* 274 */       SwingUtil.changeFontFamily(commentDate, FontTL.ROBOTO_REGULAR, 15, ColorUtil.COLOR_25);
/* 275 */       SwingUtil.changeFontFamily((JComponent)localizableLabel, FontTL.ROBOTO_REGULAR, 15, Color.GRAY);
/* 276 */       SwingUtil.changeFontFamily((JComponent)dislike, FontTL.ROBOTO_REGULAR, 15, Color.BLACK);
/* 277 */       SwingUtil.changeFontFamily((JComponent)like, FontTL.ROBOTO_REGULAR, 15, Color.BLACK);
/*     */       
/* 279 */       localizableLabel.addMouseListener(new MouseInputAdapter()
/*     */           {
/*     */             public void mouseEntered(MouseEvent e)
/*     */             {
/* 283 */               response.setForeground(ColorUtil.COLOR_25);
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseExited(MouseEvent e) {
/* 288 */               response.setForeground(Color.GRAY);
/*     */             }
/*     */           });
/*     */       
/* 292 */       localizableLabel.addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mousePressed(MouseEvent e) {
/* 295 */               if (SwingUtilities.isLeftMouseButton(e) && !c.isRemoved()) {
/* 296 */                 if (DiscussionPanel.Comment.this.getGape() >= TLauncher.getInnerSettings().getInteger("modpack.comment.max.branch")) {
/* 297 */                   Alert.showLocMessage("skin.notification.title", "modpack.comment.max.branch", null);
/*     */                 } else {
/* 299 */                   DiscussionPanel.this.commentCreationForm.setChangedComment(null);
/* 300 */                   DiscussionPanel.this.commentCreationForm.preparedForNewComment();
/* 301 */                   DiscussionPanel.this.commentCreationForm.setTopicPage(DiscussionPanel.this.dto.getId());
/* 302 */                   DiscussionPanel.this.commentCreationForm.setTopicId(c.getId());
/* 303 */                   DiscussionPanel.this.commentCreationForm.setType(DiscussionPanel.this.type);
/* 304 */                   DiscussionPanel.this.commentCreationForm.setTopicType(TopicType.SUB_COMMENT);
/* 305 */                   DiscussionPanel.this.commentCreationForm.setParent(DiscussionPanel.Comment.this);
/* 306 */                   DiscussionPanel.this.commentCreationForm.setVisible(true);
/*     */                 } 
/*     */               }
/*     */             }
/*     */           });
/*     */       
/* 312 */       CompletableFuture.runAsync(() -> { ImageIcon image = DiscussionPanel.this.controller.loadIcon(c.getAuthor()); if (Objects.nonNull(image)) SwingUtilities.invokeLater(());  }DiscussionPanel.this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 320 */           .singleDownloadExecutor).exceptionally(e -> {
/*     */             DiscussionPanel.log.warn("error", e);
/*     */             return null;
/*     */           });
/* 324 */       if (c.isRemoved()) {
/* 325 */         localizableLabel.setVisible(false);
/* 326 */         extendedPanel3.setVisible(false);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void updateText() {
/*     */       String text;
/* 332 */       if (Objects.nonNull(this.descriptionFull)) {
/* 333 */         this.centerComment.remove((Component)this.descriptionFull);
/*     */       }
/* 335 */       if (this.comment.isRemoved()) {
/* 336 */         text = Localizable.get("modpack.comment.deleted");
/*     */       } else {
/* 338 */         text = this.comment.getDescription();
/* 339 */       }  this.descriptionFull = HtmlTextPane.getWithWidth(text, DiscussionPanel.this.commentDescriptionWidth - this.gape * DiscussionPanel.this.oneGapeCommentWidth);
/* 340 */       this.descriptionFull.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
/* 341 */       this.centerComment.add((Component)this.descriptionFull, "Center");
/* 342 */       this.centerComment.revalidate();
/* 343 */       this.centerComment.repaint();
/*     */     }
/*     */     
/*     */     private void createUserPosition(CommentDTO c, UserPositionCommentCheckbox dislike, CurrentUserPosition pos) {
/* 347 */       dislike.setObject(DiscussionPanel.ob);
/* 348 */       dislike.setController(DiscussionPanel.this.controller);
/* 349 */       dislike.setExecutor(DiscussionPanel.this.singleDownloadExecutor);
/* 350 */       dislike.setComment(c);
/* 351 */       dislike.setHorizontalTextPosition(4);
/* 352 */       dislike.setPos(pos);
/* 353 */       dislike.initCounterPosition();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void preparingContent(Long id, int page, int insertElement, TopicType type, int gape) {
/* 359 */     CompletableFuture.runAsync(() -> DesktopUtil.uncheckCall(()))
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
/* 375 */       .exceptionally(t -> {
/*     */           U.log(new Object[] { t });
/*     */           return null;
/*     */         });
/*     */   }
/*     */   
/*     */   private void calculateGape() {
/* 382 */     int size1 = 4;
/* 383 */     remove(this.gapeSpace);
/* 384 */     int size = (getComponents()).length;
/* 385 */     if (size < size1 && size != 1) {
/* 386 */       this.gapeSpace.setPreferredSize(new Dimension(0, (size1 - size) * 85));
/* 387 */       add(this.gapeSpace);
/* 388 */     } else if (findIndexElement(this.gapeSpace) != -1) {
/* 389 */       remove(this.gapeSpace);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addedComment(List<? super Component> result, final CommonPage<CommentDTO> page, final int gape, final Long id, CommentDTO parent) {
/* 394 */     for (CommentDTO c : page.getContent()) {
/* 395 */       result.add(new Comment(c, gape, parent));
/* 396 */       if (Objects.nonNull(c.getSubComments()))
/* 397 */         addedComment(result, c.getSubComments(), gape + 1, c.getId(), c); 
/*     */     } 
/* 399 */     if (page.isNext()) {
/* 400 */       LocalizableLabel localizableLabel = new LocalizableLabel("modpack.comment.show.more");
/* 401 */       final ExtendedPanel p = new ExtendedPanel(new BorderLayout(0, 0));
/* 402 */       localizableLabel.setHorizontalAlignment(2);
/* 403 */       extendedPanel.setBorder(BorderFactory.createEmptyBorder(0, 25 + gape * this.oneGapeCommentWidth, 10, 0));
/* 404 */       SwingUtil.changeFontFamily((JComponent)localizableLabel, FontTL.ROBOTO_REGULAR, 16, ColorUtil.BLUE_COLOR);
/* 405 */       extendedPanel.add((Component)localizableLabel, "West");
/*     */       
/* 407 */       result.add(extendedPanel);
/* 408 */       localizableLabel.addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mousePressed(MouseEvent e)
/*     */             {
/* 412 */               if (SwingUtilities.isLeftMouseButton(e)) {
/* 413 */                 int j = DiscussionPanel.this.findIndexElement(p);
/* 414 */                 SwingUtilities.invokeLater(() -> DiscussionPanel.this.remove(p));
/* 415 */                 DiscussionPanel.this.preparingContent(id, page.getCurrent().intValue() + 1, j, (gape == 0) ? TopicType.GAME_ENTITY : TopicType.SUB_COMMENT, gape);
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void insertComment(InsertCommentDTO comment) {
/* 427 */     SwingUtilities.invokeLater(() -> {
/*     */           if (Objects.isNull(comment.getParent())) {
/*     */             add((Component)new Comment((CommentDTO)comment, 0, null), 1);
/*     */           } else {
/*     */             int j = findIndexElement((Component)comment.getParent());
/*     */             add((Component)new Comment((CommentDTO)comment, comment.getParent().getGape() + 1, comment.getParent().getComment()), j + 1);
/*     */           } 
/*     */           calculateGape();
/*     */           revalidate();
/*     */           repaint();
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void insertComment(CommentDTO comment) {
/* 443 */     SwingUtilities.invokeLater(() -> {
/*     */           Comment com = findIndexElement(comment);
/*     */           if (Objects.nonNull(com)) {
/*     */             com.setComment(comment);
/*     */             com.updateText();
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private int findIndexElement(Component p) {
/* 453 */     int j = -1;
/* 454 */     for (int i = 0; i < (getComponents()).length; i++) {
/* 455 */       if (getComponents()[i].equals(p)) {
/* 456 */         j = i;
/*     */         break;
/*     */       } 
/*     */     } 
/* 460 */     return j;
/*     */   }
/*     */   
/*     */   private Comment findIndexElement(CommentDTO c) {
/* 464 */     for (int i = 0; i < (getComponents()).length; i++) {
/* 465 */       Component cc = getComponents()[i];
/* 466 */       if (cc instanceof Comment && ((Comment)cc).getComment().getId().equals(c.getId())) {
/* 467 */         return (Comment)cc;
/*     */       }
/*     */     } 
/* 470 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\modpack\DiscussionPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */