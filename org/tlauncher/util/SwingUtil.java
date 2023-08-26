/*     */ package org.tlauncher.util;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.UIDefaults;
/*     */ import org.launcher.resource.TlauncherResource;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.util.swing.FontTL;
/*     */ 
/*     */ public class SwingUtil {
/*  25 */   private static Map<FontTL, Font> FONTS = new HashMap<>();
/*     */   
/*  27 */   private static final List<Image> favicons = new ArrayList<>();
/*     */   
/*     */   private static final String methodName = "getApplication";
/*     */   
/*     */   private static final String className = "com.apple.eawt.Application";
/*     */   private static final String methodSetDocIconImage = "setDockIconImage";
/*     */   
/*     */   private static List<Image> getFavicons() {
/*  35 */     if (!favicons.isEmpty()) {
/*  36 */       return Collections.unmodifiableList(favicons);
/*     */     }
/*  38 */     int[] sizes = { 256, 128, 96, 64, 48, 32, 24, 16 };
/*     */     
/*  40 */     StringBuilder loadedBuilder = new StringBuilder();
/*  41 */     for (int i : sizes) {
/*  42 */       Image image = ImageCache.getImage("fav" + i + ".png", false);
/*  43 */       if (image != null) {
/*     */ 
/*     */         
/*  46 */         loadedBuilder.append(", ").append(i).append("px");
/*  47 */         favicons.add(image);
/*     */       } 
/*  49 */     }  String loaded = loadedBuilder.toString();
/*     */     
/*  51 */     if (loaded.isEmpty()) {
/*  52 */       log(new Object[] { "No favicon is loaded." });
/*     */     } else {
/*  54 */       log(new Object[] { "Favicons loaded:", loaded.substring(2) });
/*     */     } 
/*  56 */     return favicons;
/*     */   }
/*     */   
/*     */   public static void init() throws IOException, FontFormatException {
/*  60 */     FONTS.put(FontTL.ROBOTO_REGULAR, Font.createFont(0, 
/*  61 */           TlauncherResource.getResource("Roboto-Regular.ttf").openStream()));
/*  62 */     FONTS.put(FontTL.ROBOTO_BOLD, 
/*  63 */         Font.createFont(0, TlauncherResource.getResource("Roboto-Bold.ttf").openStream()));
/*  64 */     FONTS.put(FontTL.ROBOTO_MEDIUM, Font.createFont(0, 
/*  65 */           TlauncherResource.getResource("Roboto-Medium.ttf").openStream()));
/*  66 */     FONTS.put(FontTL.CALIBRI, Font.createFont(0, 
/*  67 */           TlauncherResource.getResource("Calibri.ttf").openStream()));
/*  68 */     FONTS.put(FontTL.CALIBRI_BOLD, Font.createFont(0, 
/*  69 */           TlauncherResource.getResource("Calibri-Bold.ttf").openStream()));
/*     */   }
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
/*     */   public static void setFavicons(JFrame frame) {
/* 110 */     if (OS.is(new OS[] { OS.OSX })) {
/*     */       
/*     */       try {
/* 113 */         Image image = Toolkit.getDefaultToolkit().getImage(ImageCache.getRes("fav256.png"));
/*     */ 
/*     */         
/* 116 */         Class<?> app = Class.forName("com.apple.eawt.Application");
/*     */         
/* 118 */         Method method = app.getMethod("getApplication", new Class[0]);
/* 119 */         Object instanceApplication = method.invoke(null, new Object[0]);
/* 120 */         method = instanceApplication.getClass().getMethod("setDockIconImage", new Class[] { Image.class });
/* 121 */         method.invoke(instanceApplication, new Object[] { image });
/* 122 */       } catch (Exception e) {
/* 123 */         U.log(new Object[] { "couldn't set a favicon for mac os platform", e });
/*     */       } 
/*     */     } else {
/* 126 */       frame.setIconImages(getFavicons());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void initFont(int defSize) {
/*     */     try {
/* 132 */       UIDefaults defaults = UIManager.getDefaults();
/*     */       
/* 134 */       int maxSize = defSize + 2;
/*     */       
/* 136 */       for (Enumeration<?> e = defaults.keys(); e.hasMoreElements(); ) {
/* 137 */         Object key = e.nextElement();
/* 138 */         Object value = defaults.get(key);
/*     */         
/* 140 */         if (value instanceof Font) {
/* 141 */           Font font = (Font)value;
/* 142 */           int size = font.getSize();
/*     */           
/* 144 */           if (size < defSize) {
/* 145 */             size = defSize;
/* 146 */           } else if (size > maxSize) {
/* 147 */             size = maxSize;
/*     */           } 
/* 149 */           if (value instanceof FontUIResource) {
/* 150 */             defaults.put(key, new FontUIResource(font.getFamily(), font.getStyle(), size)); continue;
/*     */           } 
/* 152 */           defaults.put(key, new Font("Roboto", font.getStyle(), size));
/*     */         }
/*     */       
/*     */       } 
/* 156 */     } catch (Exception e) {
/* 157 */       log(new Object[] { "Cannot change font sizes!", e });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Cursor getCursor(int type) {
/*     */     try {
/* 163 */       return Cursor.getPredefinedCursor(type);
/* 164 */     } catch (IllegalArgumentException iaE) {
/* 165 */       iaE.printStackTrace();
/*     */       
/* 167 */       return null;
/*     */     } 
/*     */   }
/*     */   public static void setFontSize(JComponent comp, float size) {
/* 171 */     comp.setFont(comp.getFont().deriveFont(size));
/*     */   }
/*     */   
/*     */   public static void setFontSize(JComponent comp, float size, int type) {
/* 175 */     comp.setFont(comp.getFont().deriveFont(type, size));
/*     */   }
/*     */   
/*     */   public static Point getRelativeLocation(Component parent, Component comp) {
/* 179 */     Point compLocation = comp.getLocationOnScreen(), parentLocation = parent.getLocationOnScreen();
/*     */     
/* 181 */     return new Point(compLocation.x - parentLocation.x, compLocation.y - parentLocation.y);
/*     */   }
/*     */   
/*     */   private static void log(Object... o) {
/* 185 */     U.log(new Object[] { "[Swing]", o });
/*     */   }
/*     */ 
/*     */   
/*     */   public static void changeFontFamily(JComponent component, FontTL family, int size) {
/*     */     try {
/* 191 */       if ((new Locale("zh")).getLanguage().equals(TLauncher.getInstance().getConfiguration().getLocale().getLanguage())) {
/* 192 */         component.setFont(component.getFont().deriveFont(size));
/*     */         return;
/*     */       } 
/* 195 */     } catch (NullPointerException n) {
/* 196 */       U.log(new Object[] { "lg " + (new Locale("zh")).getLanguage() });
/* 197 */       U.log(new Object[] { "lg " + TLauncher.getInstance().getConfiguration().getLocale() });
/*     */     } 
/* 199 */     component.setFont(((Font)FONTS.get(family)).deriveFont(size));
/*     */   }
/*     */   
/*     */   public static void changeFontFamily(Component component, FontTL family) {
/* 203 */     Font f = component.getFont();
/* 204 */     if (f != null) {
/* 205 */       f = ((Font)FONTS.get(family)).deriveFont(f.getStyle(), f.getSize());
/* 206 */       component.setFont(f);
/*     */     } 
/* 208 */     if (component instanceof Container) {
/* 209 */       for (Component child : ((Container)component).getComponents()) {
/* 210 */         changeFontFamily(child, family);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void changeFontFamily(JComponent component, FontTL family, int size, Color foreground) {
/* 216 */     changeFontFamily(component, family, size);
/* 217 */     component.setForeground(foreground);
/*     */   }
/*     */   
/*     */   public static void setImageJLabel(JLabel label, String url) {
/* 221 */     setImageJLabel(label, url, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void configHorizontalSpingLayout(SpringLayout spring, JComponent target, JComponent coordinated, int width) {
/* 226 */     spring.putConstraint("West", target, 0, "East", coordinated);
/* 227 */     spring.putConstraint("East", target, width, "East", coordinated);
/* 228 */     spring.putConstraint("North", target, 0, "North", coordinated);
/* 229 */     spring.putConstraint("South", target, 0, "South", coordinated);
/*     */   }
/*     */   
/*     */   public static void setImageJLabel(JLabel label, String url, Dimension dimension) {
/*     */     try {
/* 234 */       URL link = new URL(url);
/* 235 */       if (!ImageCache.setLocalIcon(label, url)) {
/* 236 */         AsyncThread.execute(() -> {
/*     */               try {
/*     */                 setIcon(label, link, dimension);
/*     */                 label.repaint();
/* 240 */               } catch (RuntimeException e) {
/*     */                 U.log(new Object[] { e });
/*     */               } 
/*     */             });
/*     */       } else {
/* 245 */         setIcon(label, link, dimension);
/*     */       }
/*     */     
/* 248 */     } catch (MalformedURLException e) {
/* 249 */       log(new Object[] { e });
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void setIcon(JLabel label, URL link, Dimension dimension) {
/* 254 */     BufferedImage bufferedImage = ImageCache.loadImage(link);
/* 255 */     if (dimension != null) {
/* 256 */       label.setIcon(new ImageIcon(bufferedImage
/* 257 */             .getScaledInstance(dimension.width, dimension.height, 4)));
/*     */     } else {
/* 259 */       label.setIcon(new ImageIcon(bufferedImage));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static int getWidthText(JComponent c, String text) {
/* 264 */     AffineTransform affinetransform = new AffineTransform();
/* 265 */     FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
/* 266 */     return (int)c.getFont().getStringBounds(text, frc).getWidth();
/*     */   }
/*     */   
/*     */   public static void paintShadowLine(Rectangle rec, Graphics g, int i, int max) {
/* 270 */     if (i < 0)
/* 271 */       i = 0; 
/* 272 */     int y = rec.y, current = 0;
/* 273 */     Graphics2D g2 = (Graphics2D)g;
/* 274 */     for (; y < rec.height + rec.y; y++) {
/* 275 */       g2.setColor(new Color(i, i, i));
/* 276 */       if (current != max && i != 255) {
/* 277 */         current++;
/* 278 */         i++;
/*     */       } 
/* 280 */       g2.drawLine(rec.x, y, rec.x + rec.width, y);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void paintText(Graphics2D g2d, JComponent comp, String text) {
/* 285 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/* 286 */     FontMetrics fm = g2d.getFontMetrics();
/* 287 */     Rectangle2D r = fm.getStringBounds(text, g2d);
/* 288 */     g2d.setFont(comp.getFont());
/* 289 */     g2d.setColor(comp.getForeground());
/* 290 */     int x = (comp.getWidth() - (int)r.getWidth()) / 2;
/* 291 */     int y = (comp.getHeight() - (int)r.getHeight()) / 2 + fm.getAscent() - 1;
/* 292 */     g2d.drawString(text, x, y);
/* 293 */     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\SwingUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */