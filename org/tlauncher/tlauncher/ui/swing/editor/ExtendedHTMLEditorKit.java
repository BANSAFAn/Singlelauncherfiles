/*     */ package org.tlauncher.tlauncher.ui.swing.editor;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.html.HTML;
/*     */ import javax.swing.text.html.HTMLDocument;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableMenuItem;
/*     */ 
/*     */ public class ExtendedHTMLEditorKit extends HTMLEditorKit {
/*  19 */   protected static final ExtendedHTMLFactory extendedFactory = new ExtendedHTMLFactory();
/*     */   
/*  21 */   public static final HyperlinkProcessor defaultHyperlinkProcessor = new HyperlinkProcessor() {
/*     */       public void process(String link) {
/*     */         URI uri;
/*  24 */         if (link == null) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/*     */         try {
/*  30 */           uri = new URI(link);
/*  31 */         } catch (URISyntaxException e) {
/*  32 */           Alert.showLocError("browser.hyperlink.createScrollWrapper.error", e);
/*     */           
/*     */           return;
/*     */         } 
/*  36 */         OS.openLink(uri);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public ViewFactory getViewFactory() {
/*  42 */     return extendedFactory;
/*     */   }
/*     */   
/*     */   public static class ExtendedHTMLFactory
/*     */     extends HTMLEditorKit.HTMLFactory {
/*     */     public View create(Element elem) {
/*  48 */       HTML.Tag kind = ExtendedHTMLEditorKit.getTag(elem);
/*     */       
/*  50 */       if (kind == HTML.Tag.IMG) {
/*  51 */         return new ExtendedImageView(elem);
/*     */       }
/*     */       
/*  54 */       return super.create(elem);
/*     */     }
/*     */   }
/*     */   
/*  58 */   protected final ExtendedLinkController linkController = new ExtendedLinkController();
/*     */ 
/*     */   
/*     */   public void install(JEditorPane pane) {
/*  62 */     super.install(pane);
/*     */     
/*  64 */     for (MouseListener listener : pane.getMouseListeners()) {
/*  65 */       if (listener instanceof HTMLEditorKit.LinkController) {
/*  66 */         pane.removeMouseListener(listener);
/*  67 */         pane.removeMouseMotionListener((MouseMotionListener)listener);
/*     */         
/*  69 */         pane.addMouseListener(this.linkController);
/*  70 */         pane.addMouseMotionListener(this.linkController);
/*     */       } 
/*     */     } 
/*     */   }
/*  74 */   private HyperlinkProcessor hlProc = defaultHyperlinkProcessor;
/*     */   
/*     */   private boolean processPopup = true;
/*     */ 
/*     */   
/*     */   public final boolean getProcessPopup() {
/*  80 */     return this.processPopup;
/*     */   }
/*     */   
/*     */   public final void setProcessPopup(boolean process) {
/*  84 */     this.processPopup = process;
/*     */   }
/*     */   
/*  87 */   private static final Cursor HAND = Cursor.getPredefinedCursor(12);
/*     */   
/*     */   private String popupHref;
/*  90 */   private final JPopupMenu popup = new JPopupMenu();
/*     */   
/*     */   public ExtendedHTMLEditorKit() {
/*  93 */     LocalizableMenuItem open = new LocalizableMenuItem("browser.hyperlink.popup.open");
/*  94 */     open.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  97 */             ExtendedHTMLEditorKit.this.hlProc.process(ExtendedHTMLEditorKit.this.popupHref);
/*     */           }
/*     */         });
/* 100 */     this.popup.add((JMenuItem)open);
/*     */     
/* 102 */     LocalizableMenuItem copy = new LocalizableMenuItem("browser.hyperlink.popup.copy");
/* 103 */     copy.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 106 */             Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ExtendedHTMLEditorKit.this.popupHref), null);
/*     */           }
/*     */         });
/* 109 */     this.popup.add((JMenuItem)copy);
/*     */     
/* 111 */     LocalizableMenuItem show = new LocalizableMenuItem("browser.hyperlink.popup.show");
/* 112 */     show.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 115 */             Alert.showLocMessage("browser.hyperlink.popup.show.alert", ExtendedHTMLEditorKit.this.popupHref);
/*     */           }
/*     */         });
/* 118 */     this.popup.add((JMenuItem)show);
/*     */   }
/*     */   
/*     */   public class ExtendedLinkController
/*     */     extends MouseAdapter
/*     */   {
/*     */     public void mouseClicked(MouseEvent e) {
/* 125 */       JEditorPane editor = (JEditorPane)e.getSource();
/*     */       
/* 127 */       if (!editor.isEnabled() && !editor.isDisplayable()) {
/*     */         return;
/*     */       }
/* 130 */       String href = ExtendedHTMLEditorKit.getAnchorHref(e);
/*     */       
/* 132 */       if (href == null) {
/*     */         return;
/*     */       }
/* 135 */       switch (e.getButton()) {
/*     */         
/*     */         case 3:
/* 138 */           if (ExtendedHTMLEditorKit.this.processPopup) {
/* 139 */             ExtendedHTMLEditorKit.this.popupHref = href;
/* 140 */             ExtendedHTMLEditorKit.this.popup.show(editor, e.getX(), e.getY());
/*     */           } 
/*     */           return;
/*     */       } 
/*     */       
/* 145 */       ExtendedHTMLEditorKit.this.hlProc.process(href);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/* 151 */       JEditorPane editor = (JEditorPane)e.getSource();
/*     */       
/* 153 */       if (!editor.isEnabled() && !editor.isDisplayable()) {
/*     */         return;
/*     */       }
/* 156 */       editor.setCursor((ExtendedHTMLEditorKit.getAnchorHref(e) == null) ? Cursor.getDefaultCursor() : ExtendedHTMLEditorKit.HAND);
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseExited(MouseEvent e) {
/* 161 */       JEditorPane editor = (JEditorPane)e.getSource();
/*     */       
/* 163 */       if (!editor.isEnabled() && !editor.isDisplayable()) {
/*     */         return;
/*     */       }
/* 166 */       editor.setCursor(Cursor.getDefaultCursor());
/*     */     }
/*     */   }
/*     */   
/*     */   private static HTML.Tag getTag(Element elem) {
/* 171 */     AttributeSet attrs = elem.getAttributes();
/* 172 */     Object elementName = attrs.getAttribute("$ename");
/* 173 */     Object o = (elementName != null) ? null : attrs.getAttribute(StyleConstants.NameAttribute);
/*     */     
/* 175 */     return (o instanceof HTML.Tag) ? (HTML.Tag)o : null;
/*     */   }
/*     */   
/*     */   private static String getAnchorHref(MouseEvent e) {
/* 179 */     JEditorPane editor = (JEditorPane)e.getSource();
/*     */     
/* 181 */     if (!(editor.getDocument() instanceof HTMLDocument)) {
/* 182 */       return null;
/*     */     }
/* 184 */     HTMLDocument hdoc = (HTMLDocument)editor.getDocument();
/* 185 */     Element elem = hdoc.getCharacterElement(editor.viewToModel(e.getPoint()));
/*     */     
/* 187 */     HTML.Tag tag = getTag(elem);
/*     */     
/* 189 */     if (tag == HTML.Tag.CONTENT) {
/* 190 */       Object anchorAttr = elem.getAttributes().getAttribute(HTML.Tag.A);
/*     */       
/* 192 */       if (anchorAttr != null && anchorAttr instanceof AttributeSet) {
/* 193 */         AttributeSet anchor = (AttributeSet)anchorAttr;
/* 194 */         Object hrefObject = anchor.getAttribute(HTML.Attribute.HREF);
/*     */         
/* 196 */         if (hrefObject != null && hrefObject instanceof String) {
/* 197 */           return (String)hrefObject;
/*     */         }
/*     */       } 
/*     */     } 
/* 201 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\editor\ExtendedHTMLEditorKit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */