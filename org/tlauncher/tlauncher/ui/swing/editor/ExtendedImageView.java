/*      */ package org.tlauncher.tlauncher.ui.swing.editor;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Container;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.net.URL;
/*      */ import java.util.Dictionary;
/*      */ import javax.imageio.ImageIO;
/*      */ import javax.swing.GrayFilter;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.GlyphView;
/*      */ import javax.swing.text.Highlighter;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.LayeredHighlighter;
/*      */ import javax.swing.text.Position;
/*      */ import javax.swing.text.Segment;
/*      */ import javax.swing.text.StyledDocument;
/*      */ import javax.swing.text.View;
/*      */ import javax.swing.text.ViewFactory;
/*      */ import javax.swing.text.html.HTML;
/*      */ import javax.swing.text.html.HTMLDocument;
/*      */ import javax.swing.text.html.InlineView;
/*      */ import javax.swing.text.html.StyleSheet;
/*      */ import javax.xml.bind.DatatypeConverter;
/*      */ import org.tlauncher.util.U;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ExtendedImageView
/*      */   extends View
/*      */ {
/*   58 */   private static String base64s = "data:image/"; private static String base64e = ";base64,";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean sIsInc = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   72 */   private static int sIncRate = 100;
/*      */ 
/*      */   
/*      */   private static final String PENDING_IMAGE = "html.pendingImage";
/*      */ 
/*      */   
/*      */   private static final String MISSING_IMAGE = "html.missingImage";
/*      */ 
/*      */   
/*      */   private static final String IMAGE_CACHE_PROPERTY = "imageCache";
/*      */ 
/*      */   
/*      */   private static final int DEFAULT_WIDTH = 38;
/*      */ 
/*      */   
/*      */   private static final int DEFAULT_HEIGHT = 38;
/*      */ 
/*      */   
/*      */   private static final int LOADING_FLAG = 1;
/*      */ 
/*      */   
/*      */   private static final int LINK_FLAG = 2;
/*      */ 
/*      */   
/*      */   private static final int WIDTH_FLAG = 4;
/*      */ 
/*      */   
/*      */   private static final int HEIGHT_FLAG = 8;
/*      */ 
/*      */   
/*      */   private static final int RELOAD_FLAG = 16;
/*      */ 
/*      */   
/*      */   private static final int RELOAD_IMAGE_FLAG = 32;
/*      */ 
/*      */   
/*      */   private static final int SYNC_LOAD_FLAG = 64;
/*      */ 
/*      */   
/*      */   private AttributeSet attr;
/*      */ 
/*      */   
/*      */   private Image image;
/*      */ 
/*      */   
/*      */   private Image disabledImage;
/*      */ 
/*      */   
/*      */   private int width;
/*      */ 
/*      */   
/*      */   private int height;
/*      */   
/*      */   private int state;
/*      */   
/*      */   private Container container;
/*      */   
/*      */   private Rectangle fBounds;
/*      */   
/*      */   private Color borderColor;
/*      */   
/*      */   private short borderSize;
/*      */   
/*      */   private short leftInset;
/*      */   
/*      */   private short rightInset;
/*      */   
/*      */   private short topInset;
/*      */   
/*      */   private short bottomInset;
/*      */   
/*      */   private ImageObserver imageObserver;
/*      */   
/*      */   private View altView;
/*      */   
/*      */   private float vAlign;
/*      */ 
/*      */   
/*      */   public ExtendedImageView(Element elem) {
/*  151 */     super(elem);
/*  152 */     this.fBounds = new Rectangle();
/*  153 */     this.imageObserver = new ImageHandler();
/*  154 */     this.state = 48;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getAltText() {
/*  163 */     return (String)getElement().getAttributes()
/*  164 */       .getAttribute(HTML.Attribute.ALT);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getImageSource() {
/*  172 */     return (String)getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Icon getNoImageIcon() {
/*  179 */     return (Icon)UIManager.getLookAndFeelDefaults().get("html.missingImage");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Icon getLoadingImageIcon() {
/*  186 */     return (Icon)UIManager.getLookAndFeelDefaults().get("html.pendingImage");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Image getImage() {
/*  193 */     sync();
/*  194 */     return this.image;
/*      */   }
/*      */   
/*      */   private Image getImage(boolean enabled) {
/*  198 */     Image img = getImage();
/*  199 */     if (!enabled) {
/*  200 */       if (this.disabledImage == null) {
/*  201 */         this.disabledImage = GrayFilter.createDisabledImage(img);
/*      */       }
/*  203 */       img = this.disabledImage;
/*      */     } 
/*  205 */     return img;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLoadsSynchronously(boolean newValue) {
/*  215 */     synchronized (this) {
/*  216 */       if (newValue) {
/*  217 */         this.state |= 0x40;
/*      */       } else {
/*  219 */         this.state = (this.state | 0x40) ^ 0x40;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getLoadsSynchronously() {
/*  228 */     return ((this.state & 0x40) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected StyleSheet getStyleSheet() {
/*  235 */     HTMLDocument doc = (HTMLDocument)getDocument();
/*  236 */     return doc.getStyleSheet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AttributeSet getAttributes() {
/*  246 */     sync();
/*  247 */     return this.attr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getToolTipText(float x, float y, Shape allocation) {
/*  259 */     return getAltText();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setPropertiesFromAttributes() {
/*  266 */     StyleSheet sheet = getStyleSheet();
/*  267 */     this.attr = sheet.getViewAttributes(this);
/*      */ 
/*      */     
/*  270 */     this.borderSize = (short)getIntAttr(HTML.Attribute.BORDER, 0);
/*      */     
/*  272 */     this.leftInset = this.rightInset = (short)(getIntAttr(HTML.Attribute.HSPACE, 0) + this.borderSize);
/*      */     
/*  274 */     this.topInset = this.bottomInset = (short)(getIntAttr(HTML.Attribute.VSPACE, 0) + this.borderSize);
/*      */ 
/*      */     
/*  277 */     this
/*  278 */       .borderColor = ((StyledDocument)getDocument()).getForeground(getAttributes());
/*      */     
/*  280 */     AttributeSet attr = getElement().getAttributes();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  285 */     Object alignment = attr.getAttribute(HTML.Attribute.ALIGN);
/*      */     
/*  287 */     this.vAlign = 1.0F;
/*  288 */     if (alignment != null) {
/*  289 */       alignment = alignment.toString();
/*  290 */       if ("top".equals(alignment)) {
/*  291 */         this.vAlign = 0.0F;
/*  292 */       } else if ("middle".equals(alignment)) {
/*  293 */         this.vAlign = 0.5F;
/*      */       } 
/*      */     } 
/*      */     
/*  297 */     AttributeSet anchorAttr = (AttributeSet)attr.getAttribute(HTML.Tag.A);
/*  298 */     if (anchorAttr != null && anchorAttr
/*  299 */       .isDefined(HTML.Attribute.HREF)) {
/*  300 */       synchronized (this) {
/*  301 */         this.state |= 0x2;
/*      */       } 
/*      */     } else {
/*  304 */       synchronized (this) {
/*  305 */         this.state = (this.state | 0x2) ^ 0x2;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setParent(View parent) {
/*  316 */     View oldParent = getParent();
/*  317 */     super.setParent(parent);
/*  318 */     this.container = (parent != null) ? getContainer() : null;
/*  319 */     if (oldParent != parent) {
/*  320 */       synchronized (this) {
/*  321 */         this.state |= 0x10;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f) {
/*  331 */     super.changedUpdate(e, a, f);
/*      */     
/*  333 */     synchronized (this) {
/*  334 */       this.state |= 0x30;
/*      */     } 
/*      */ 
/*      */     
/*  338 */     preferenceChanged(null, true, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void paint(Graphics g, Shape a) {
/*  350 */     sync();
/*      */ 
/*      */     
/*  353 */     Rectangle rect = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
/*  354 */     Rectangle clip = g.getClipBounds();
/*      */     
/*  356 */     this.fBounds.setBounds(rect);
/*  357 */     paintHighlights(g, a);
/*  358 */     paintBorder(g, rect);
/*  359 */     if (clip != null) {
/*  360 */       g.clipRect(rect.x + this.leftInset, rect.y + this.topInset, rect.width - this.leftInset - this.rightInset, rect.height - this.topInset - this.bottomInset);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  365 */     Container host = getContainer();
/*  366 */     Image img = getImage((host == null || host.isEnabled()));
/*  367 */     if (img != null) {
/*  368 */       if (!hasPixels(img)) {
/*      */         
/*  370 */         Icon icon = getLoadingImageIcon();
/*  371 */         if (icon != null) {
/*  372 */           icon.paintIcon(host, g, rect.x + this.leftInset, rect.y + this.topInset);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  377 */         g.drawImage(img, rect.x + this.leftInset, rect.y + this.topInset, this.width, this.height, this.imageObserver);
/*      */       } 
/*      */     } else {
/*      */       
/*  381 */       Icon icon = getNoImageIcon();
/*  382 */       if (icon != null) {
/*  383 */         icon.paintIcon(host, g, rect.x + this.leftInset, rect.y + this.topInset);
/*      */       }
/*      */       
/*  386 */       View view = getAltView();
/*      */       
/*  388 */       if (view != null && ((this.state & 0x4) == 0 || this.width > 38)) {
/*      */ 
/*      */         
/*  391 */         Rectangle altRect = new Rectangle(rect.x + this.leftInset + 38, rect.y + this.topInset, rect.width - this.leftInset - this.rightInset - 38, rect.height - this.topInset - this.bottomInset);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  396 */         view.paint(g, altRect);
/*      */       } 
/*      */     } 
/*  399 */     if (clip != null)
/*      */     {
/*  401 */       g.setClip(clip.x, clip.y, clip.width, clip.height);
/*      */     }
/*      */   }
/*      */   
/*      */   private void paintHighlights(Graphics g, Shape shape) {
/*  406 */     if (this.container instanceof JTextComponent) {
/*  407 */       JTextComponent tc = (JTextComponent)this.container;
/*  408 */       Highlighter h = tc.getHighlighter();
/*  409 */       if (h instanceof LayeredHighlighter) {
/*  410 */         ((LayeredHighlighter)h)
/*  411 */           .paintLayeredHighlights(g, getStartOffset(), getEndOffset(), shape, tc, this);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void paintBorder(Graphics g, Rectangle rect) {
/*  417 */     Color color = this.borderColor;
/*      */     
/*  419 */     if ((this.borderSize > 0 || this.image == null) && color != null) {
/*  420 */       int xOffset = this.leftInset - this.borderSize;
/*  421 */       int yOffset = this.topInset - this.borderSize;
/*  422 */       g.setColor(color);
/*  423 */       int n = (this.image == null) ? 1 : this.borderSize;
/*  424 */       for (int counter = 0; counter < n; counter++) {
/*  425 */         g.drawRect(rect.x + xOffset + counter, rect.y + yOffset + counter, rect.width - counter - counter - xOffset - xOffset - 1, rect.height - counter - counter - yOffset - yOffset - 1);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPreferredSpan(int axis) {
/*  445 */     sync();
/*      */ 
/*      */     
/*  448 */     if (axis == 0 && (this.state & 0x4) == 4) {
/*  449 */       getPreferredSpanFromAltView(axis);
/*  450 */       return (this.width + this.leftInset + this.rightInset);
/*      */     } 
/*  452 */     if (axis == 1 && (this.state & 0x8) == 8) {
/*  453 */       getPreferredSpanFromAltView(axis);
/*  454 */       return (this.height + this.topInset + this.bottomInset);
/*      */     } 
/*      */     
/*  457 */     Image image = getImage();
/*      */     
/*  459 */     if (image != null) {
/*  460 */       switch (axis) {
/*      */         case 0:
/*  462 */           return (this.width + this.leftInset + this.rightInset);
/*      */         case 1:
/*  464 */           return (this.height + this.topInset + this.bottomInset);
/*      */       } 
/*  466 */       throw new IllegalArgumentException("Invalid axis: " + axis);
/*      */     } 
/*      */ 
/*      */     
/*  470 */     View view = getAltView();
/*  471 */     float retValue = 0.0F;
/*      */     
/*  473 */     if (view != null) {
/*  474 */       retValue = view.getPreferredSpan(axis);
/*      */     }
/*  476 */     switch (axis) {
/*      */       case 0:
/*  478 */         return retValue + (this.width + this.leftInset + this.rightInset);
/*      */       case 1:
/*  480 */         return retValue + (this.height + this.topInset + this.bottomInset);
/*      */     } 
/*  482 */     throw new IllegalArgumentException("Invalid axis: " + axis);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getAlignment(int axis) {
/*  501 */     switch (axis) {
/*      */       case 1:
/*  503 */         return this.vAlign;
/*      */     } 
/*  505 */     return super.getAlignment(axis);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
/*  522 */     int p0 = getStartOffset();
/*  523 */     int p1 = getEndOffset();
/*  524 */     if (pos >= p0 && pos <= p1) {
/*  525 */       Rectangle r = a.getBounds();
/*  526 */       if (pos == p1) {
/*  527 */         r.x += r.width;
/*      */       }
/*  529 */       r.width = 0;
/*  530 */       return r;
/*      */     } 
/*  532 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
/*  548 */     Rectangle alloc = (Rectangle)a;
/*  549 */     if (x < (alloc.x + alloc.width)) {
/*  550 */       bias[0] = Position.Bias.Forward;
/*  551 */       return getStartOffset();
/*      */     } 
/*  553 */     bias[0] = Position.Bias.Backward;
/*  554 */     return getEndOffset();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSize(float width, float height) {
/*  566 */     sync();
/*      */     
/*  568 */     if (getImage() == null) {
/*  569 */       View view = getAltView();
/*      */       
/*  571 */       if (view != null) {
/*  572 */         view.setSize(Math.max(0.0F, width - (38 + this.leftInset + this.rightInset)), 
/*  573 */             Math.max(0.0F, height - (this.topInset + this.bottomInset)));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasPixels(Image image) {
/*  588 */     return (image != null && image
/*  589 */       .getHeight(this.imageObserver) > 0 && image
/*  590 */       .getWidth(this.imageObserver) > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float getPreferredSpanFromAltView(int axis) {
/*  598 */     if (getImage() == null) {
/*  599 */       View view = getAltView();
/*      */       
/*  601 */       if (view != null) {
/*  602 */         return view.getPreferredSpan(axis);
/*      */       }
/*      */     } 
/*  605 */     return 0.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void repaint(long delay) {
/*  613 */     if (this.container != null && this.fBounds != null) {
/*  614 */       this.container.repaint(delay, this.fBounds.x, this.fBounds.y, this.fBounds.width, this.fBounds.height);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getIntAttr(HTML.Attribute name, int deflt) {
/*  624 */     AttributeSet attr = getElement().getAttributes();
/*  625 */     if (attr.isDefined(name)) {
/*      */       int i;
/*  627 */       String val = (String)attr.getAttribute(name);
/*  628 */       if (val == null) {
/*  629 */         i = deflt;
/*      */       } else {
/*      */         try {
/*  632 */           i = Math.max(0, Integer.parseInt(val));
/*  633 */         } catch (NumberFormatException x) {
/*  634 */           i = deflt;
/*      */         } 
/*      */       } 
/*  637 */       return i;
/*      */     } 
/*      */     
/*  640 */     return deflt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void sync() {
/*  647 */     int s = this.state;
/*  648 */     if ((s & 0x20) != 0) {
/*  649 */       refreshImage();
/*      */     }
/*  651 */     s = this.state;
/*  652 */     if ((s & 0x10) != 0) {
/*  653 */       synchronized (this) {
/*  654 */         this.state = (this.state | 0x10) ^ 0x10;
/*      */       } 
/*  656 */       setPropertiesFromAttributes();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void refreshImage() {
/*  666 */     synchronized (this) {
/*      */       
/*  668 */       this.state = (this.state | 0x1 | 0x20 | 0x4 | 0x8) ^ 0x2C;
/*      */ 
/*      */       
/*  671 */       this.image = null;
/*  672 */       this.width = this.height = 0;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  677 */       loadImage();
/*      */ 
/*      */       
/*  680 */       updateImageSize();
/*      */     } finally {
/*  682 */       synchronized (this) {
/*      */         
/*  684 */         this.state = (this.state | 0x1) ^ 0x1;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void loadImage() {
/*      */     try {
/*  695 */       this.image = loadNewImage();
/*  696 */     } catch (Exception e) {
/*  697 */       this.image = null;
/*  698 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */   
/*      */   private Image loadNewImage() throws Exception {
/*  703 */     String source = getImageSource();
/*      */     
/*  705 */     if (source == null) {
/*  706 */       return null;
/*      */     }
/*  708 */     if (source.startsWith(base64s)) {
/*  709 */       int startPoint = base64s.length();
/*      */       
/*  711 */       String imageType = source.substring(startPoint, startPoint + 4);
/*      */       
/*  713 */       if (imageType.startsWith("png") || imageType.startsWith("jpg")) {
/*  714 */         startPoint += 3;
/*  715 */       } else if (imageType.equals("jpeg")) {
/*  716 */         startPoint += 4;
/*      */       } else {
/*  718 */         return null;
/*      */       } 
/*  720 */       if (!source.substring(startPoint, startPoint + base64e.length()).equals(base64e)) {
/*  721 */         return null;
/*      */       }
/*  723 */       startPoint += base64e.length();
/*      */       
/*  725 */       byte[] bytes = DatatypeConverter.parseBase64Binary(source.substring(startPoint));
/*  726 */       return ImageIO.read(new ByteArrayInputStream(bytes));
/*      */     } 
/*      */     
/*  729 */     URL src = U.makeURL(source);
/*      */     
/*  731 */     if (src == null) {
/*  732 */       return null;
/*      */     }
/*  734 */     Image newImage = null;
/*      */     
/*  736 */     Dictionary<?, ?> cache = (Dictionary<?, ?>)getDocument().getProperty("imageCache");
/*  737 */     if (cache != null) {
/*  738 */       newImage = (Image)cache.get(src);
/*      */     } else {
/*  740 */       newImage = Toolkit.getDefaultToolkit().createImage(src);
/*      */       
/*  742 */       if (newImage != null && getLoadsSynchronously()) {
/*      */         
/*  744 */         ImageIcon ii = new ImageIcon();
/*  745 */         ii.setImage(newImage);
/*      */       } 
/*      */     } 
/*      */     
/*  749 */     return newImage;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateImageSize() {
/*  757 */     int newWidth = 0;
/*  758 */     int newHeight = 0;
/*  759 */     int newState = 0;
/*  760 */     Image newImage = getImage();
/*      */     
/*  762 */     if (newImage != null) {
/*      */ 
/*      */ 
/*      */       
/*  766 */       newWidth = getIntAttr(HTML.Attribute.WIDTH, -1);
/*  767 */       if (newWidth > 0) {
/*  768 */         newState |= 0x4;
/*      */       }
/*  770 */       newHeight = getIntAttr(HTML.Attribute.HEIGHT, -1);
/*  771 */       if (newHeight > 0) {
/*  772 */         newState |= 0x8;
/*      */       }
/*      */       
/*  775 */       if (newWidth <= 0) {
/*  776 */         newWidth = newImage.getWidth(this.imageObserver);
/*  777 */         if (newWidth <= 0) {
/*  778 */           newWidth = 38;
/*      */         }
/*      */       } 
/*      */       
/*  782 */       if (newHeight <= 0) {
/*  783 */         newHeight = newImage.getHeight(this.imageObserver);
/*  784 */         if (newHeight <= 0) {
/*  785 */           newHeight = 38;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  790 */       if ((newState & 0xC) != 0) {
/*  791 */         Toolkit.getDefaultToolkit().prepareImage(newImage, newWidth, newHeight, this.imageObserver);
/*      */       }
/*      */       else {
/*      */         
/*  795 */         Toolkit.getDefaultToolkit().prepareImage(newImage, -1, -1, this.imageObserver);
/*      */       } 
/*      */ 
/*      */       
/*  799 */       boolean createText = false;
/*  800 */       synchronized (this) {
/*      */ 
/*      */ 
/*      */         
/*  804 */         if (this.image != null) {
/*  805 */           if ((newState & 0x4) == 4 || this.width == 0) {
/*  806 */             this.width = newWidth;
/*      */           }
/*  808 */           if ((newState & 0x8) == 8 || this.height == 0)
/*      */           {
/*  810 */             this.height = newHeight;
/*      */           }
/*      */         } else {
/*  813 */           createText = true;
/*  814 */           if ((newState & 0x4) == 4) {
/*  815 */             this.width = newWidth;
/*      */           }
/*  817 */           if ((newState & 0x8) == 8) {
/*  818 */             this.height = newHeight;
/*      */           }
/*      */         } 
/*  821 */         this.state |= newState;
/*  822 */         this.state = (this.state | 0x1) ^ 0x1;
/*      */       } 
/*  824 */       if (createText)
/*      */       {
/*  826 */         updateAltTextView();
/*      */       }
/*      */     } else {
/*  829 */       this.width = this.height = 38;
/*  830 */       updateAltTextView();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateAltTextView() {
/*  838 */     String text = getAltText();
/*      */     
/*  840 */     if (text != null) {
/*      */ 
/*      */       
/*  843 */       ImageLabelView newView = new ImageLabelView(getElement(), text);
/*  844 */       synchronized (this) {
/*  845 */         this.altView = newView;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private View getAltView() {
/*      */     View view;
/*  856 */     synchronized (this) {
/*  857 */       view = this.altView;
/*      */     } 
/*  859 */     if (view != null && view.getParent() == null) {
/*  860 */       view.setParent(getParent());
/*      */     }
/*  862 */     return view;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void safePreferenceChanged() {
/*  870 */     if (SwingUtilities.isEventDispatchThread()) {
/*  871 */       Document doc = getDocument();
/*  872 */       if (doc instanceof AbstractDocument) {
/*  873 */         ((AbstractDocument)doc).readLock();
/*      */       }
/*  875 */       preferenceChanged(null, true, true);
/*  876 */       if (doc instanceof AbstractDocument) {
/*  877 */         ((AbstractDocument)doc).readUnlock();
/*      */       }
/*      */     } else {
/*  880 */       SwingUtilities.invokeLater(new Runnable()
/*      */           {
/*      */             public void run() {
/*  883 */               ExtendedImageView.this.safePreferenceChanged();
/*      */             }
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class ImageHandler
/*      */     implements ImageObserver
/*      */   {
/*      */     private ImageHandler() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean imageUpdate(Image img, int flags, int x, int y, int newWidth, int newHeight) {
/*  902 */       if ((img != ExtendedImageView.this.image && img != ExtendedImageView.this.disabledImage) || ExtendedImageView.this
/*  903 */         .image == null || ExtendedImageView.this.getParent() == null)
/*      */       {
/*  905 */         return false;
/*      */       }
/*      */ 
/*      */       
/*  909 */       if ((flags & 0xC0) != 0) {
/*  910 */         ExtendedImageView.this.repaint(0L);
/*  911 */         synchronized (ExtendedImageView.this) {
/*  912 */           if (ExtendedImageView.this.image == img) {
/*      */ 
/*      */             
/*  915 */             ExtendedImageView.this.image = null;
/*  916 */             if ((ExtendedImageView.this.state & 0x4) != 4) {
/*  917 */               ExtendedImageView.this.width = 38;
/*      */             }
/*  919 */             if ((ExtendedImageView.this.state & 0x8) != 8) {
/*  920 */               ExtendedImageView.this.height = 38;
/*      */             }
/*      */           } else {
/*  923 */             ExtendedImageView.this.disabledImage = null;
/*      */           } 
/*  925 */           if ((ExtendedImageView.this.state & 0x1) == 1)
/*      */           {
/*      */             
/*  928 */             return false;
/*      */           }
/*      */         } 
/*  931 */         ExtendedImageView.this.updateAltTextView();
/*  932 */         ExtendedImageView.this.safePreferenceChanged();
/*  933 */         return false;
/*      */       } 
/*      */       
/*  936 */       if (ExtendedImageView.this.image == img) {
/*      */         
/*  938 */         short changed = 0;
/*  939 */         if ((flags & 0x2) != 0 && 
/*  940 */           !ExtendedImageView.this.getElement().getAttributes().isDefined(HTML.Attribute.HEIGHT)) {
/*  941 */           changed = (short)(changed | 0x1);
/*      */         }
/*  943 */         if ((flags & 0x1) != 0 && 
/*  944 */           !ExtendedImageView.this.getElement().getAttributes().isDefined(HTML.Attribute.WIDTH)) {
/*  945 */           changed = (short)(changed | 0x2);
/*      */         }
/*      */         
/*  948 */         synchronized (ExtendedImageView.this) {
/*  949 */           if ((changed & 0x1) == 1 && (ExtendedImageView.this.state & 0x4) == 0) {
/*  950 */             ExtendedImageView.this.width = newWidth;
/*      */           }
/*  952 */           if ((changed & 0x2) == 2 && (ExtendedImageView.this.state & 0x8) == 0) {
/*  953 */             ExtendedImageView.this.height = newHeight;
/*      */           }
/*  955 */           if ((ExtendedImageView.this.state & 0x1) == 1)
/*      */           {
/*      */             
/*  958 */             return true;
/*      */           }
/*      */         } 
/*  961 */         if (changed != 0) {
/*      */           
/*  963 */           ExtendedImageView.this.safePreferenceChanged();
/*  964 */           return true;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  969 */       if ((flags & 0x30) != 0) {
/*  970 */         ExtendedImageView.this.repaint(0L);
/*  971 */       } else if ((flags & 0x8) != 0 && ExtendedImageView.sIsInc) {
/*  972 */         ExtendedImageView.this.repaint(ExtendedImageView.sIncRate);
/*      */       } 
/*  974 */       return ((flags & 0x20) == 0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class ImageLabelView
/*      */     extends InlineView
/*      */   {
/*      */     private Segment segment;
/*      */     
/*      */     private Color fg;
/*      */ 
/*      */     
/*      */     ImageLabelView(Element e, String text) {
/*  989 */       super(e);
/*  990 */       reset(text);
/*      */     }
/*      */     
/*      */     public void reset(String text) {
/*  994 */       this.segment = new Segment(text.toCharArray(), 0, text.length());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void paint(Graphics g, Shape a) {
/* 1001 */       GlyphView.GlyphPainter painter = getGlyphPainter();
/*      */       
/* 1003 */       if (painter != null) {
/* 1004 */         g.setColor(getForeground());
/* 1005 */         painter.paint(this, g, a, getStartOffset(), getEndOffset());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public Segment getText(int p0, int p1) {
/* 1011 */       if (p0 < 0 || p1 > this.segment.array.length) {
/* 1012 */         throw new RuntimeException("ImageLabelView: Stale view");
/*      */       }
/* 1014 */       this.segment.offset = p0;
/* 1015 */       this.segment.count = p1 - p0;
/* 1016 */       return this.segment;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getStartOffset() {
/* 1021 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getEndOffset() {
/* 1026 */       return this.segment.array.length;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public View breakView(int axis, int p0, float pos, float len) {
/* 1032 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Color getForeground() {
/*      */       View parent;
/* 1038 */       if (this.fg == null && (parent = getParent()) != null) {
/* 1039 */         Document doc = getDocument();
/* 1040 */         AttributeSet attr = parent.getAttributes();
/*      */         
/* 1042 */         if (attr != null && doc instanceof StyledDocument) {
/* 1043 */           this.fg = ((StyledDocument)doc).getForeground(attr);
/*      */         }
/*      */       } 
/* 1046 */       return this.fg;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\editor\ExtendedImageView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */