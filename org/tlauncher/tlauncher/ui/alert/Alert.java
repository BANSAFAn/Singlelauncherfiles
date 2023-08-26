/*     */ package org.tlauncher.tlauncher.ui.alert;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableCheckbox;
/*     */ import org.tlauncher.tlauncher.ui.loc.UpdaterButton;
/*     */ import org.tlauncher.tlauncher.ui.swing.HtmlTextPane;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ 
/*     */ public class Alert {
/*  23 */   private static final Color YES = new Color(88, 159, 42);
/*  24 */   private static final Color NO = new Color(204, 118, 47);
/*  25 */   private static final JFrame frame = new JFrame(); private static final String PREFIX = "TLauncher : "; private static final String MISSING_TITLE = "MISSING TITLE";
/*     */   private static final String MISSING_MESSAGE = "MISSING MESSAGE";
/*     */   private static final String MISSING_QUESTION = "MISSING QUESTION";
/*  28 */   private static String DEFAULT_TITLE = "An error occurred", DEFAULT_MESSAGE = "An unexpected error occurred";
/*     */   
/*     */   static {
/*  31 */     frame.setAlwaysOnTop(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void showError(String title, String message, Object textarea) {
/*  36 */     if (textarea instanceof Throwable) {
/*  37 */       U.log(new Object[] { "Showing error:", textarea });
/*  38 */       Throwable throwable = (Throwable)textarea;
/*  39 */       String messageThrowable = "";
/*  40 */       if (Objects.nonNull(throwable.getCause()))
/*  41 */         messageThrowable = throwable.getCause().getMessage(); 
/*  42 */       showMonolog(0, title, message, throwable
/*  43 */           .getMessage() + " : " + messageThrowable);
/*     */       return;
/*     */     } 
/*  46 */     showMonolog(0, title, message, textarea);
/*     */   }
/*     */   
/*     */   public static void showError(String title, String message) {
/*  50 */     showError(title, message, null);
/*     */   }
/*     */   
/*     */   public static void showError(String message, Object textarea) {
/*  54 */     showError(DEFAULT_TITLE, message, textarea);
/*     */   }
/*     */   
/*     */   public static void showError(Object textarea, boolean exit) {
/*  58 */     showError(DEFAULT_TITLE, DEFAULT_MESSAGE, textarea);
/*  59 */     if (exit)
/*  60 */       System.exit(-1); 
/*     */   }
/*     */   
/*     */   public static void showError(Object textarea) {
/*  64 */     showError(textarea, false);
/*     */   }
/*     */   
/*     */   public static void showLocError(String titlePath, String messagePath, Object textarea) {
/*  68 */     showError(getLoc(titlePath, "MISSING TITLE"), getLoc(messagePath, "MISSING MESSAGE"), textarea);
/*     */   }
/*     */   
/*     */   public static void showLocError(String path, Object textarea) {
/*  72 */     showError(getLoc(path + ".title", "MISSING TITLE"), getLoc(path, "MISSING MESSAGE"), textarea);
/*     */   }
/*     */   
/*     */   public static void showLocError(String path) {
/*  76 */     showLocError(path, null);
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
/*     */   public static void showMessage(String title, String message, Object textarea) {
/*  91 */     showMonolog(1, title, message, textarea);
/*     */   }
/*     */   
/*     */   public static void showMessage(String title, String message) {
/*  95 */     showMessage(title, message, (Object)null);
/*     */   }
/*     */   
/*     */   public static void showLocMessage(String titlePath, String messagePath, Object textarea) {
/*  99 */     showMessage(getLoc(titlePath, "MISSING TITLE"), getLoc(messagePath, "MISSING MESSAGE"), textarea);
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
/*     */   public static void showLocMessage(String path, Object textarea) {
/* 112 */     showMessage(getLoc(path + ".title", "MISSING TITLE"), getLoc(path, "MISSING MESSAGE"), textarea);
/*     */   }
/*     */   
/*     */   public static void showLocMessageWithoutTitle(String path) {
/* 116 */     showMessage(getLoc("", "MISSING TITLE"), getLoc(path, "MISSING MESSAGE"));
/*     */   }
/*     */   
/*     */   public static void showLocMessage(String path) {
/* 120 */     showLocMessage(path, null);
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
/*     */   public static void showWarning(String title, String message, Object textarea) {
/* 135 */     showMonolog(2, title, message, textarea);
/*     */   }
/*     */   
/*     */   public static void showWarning(String title, String message) {
/* 139 */     showWarning(title, message, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showLocWarning(String titlePath, String messagePath, Object textarea) {
/* 150 */     showWarning(getLoc(titlePath, "MISSING TITLE"), getLoc(messagePath, "MISSING MESSAGE"), textarea);
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
/*     */   private static void showLocWarning(String titlePath, String messagePath) {
/* 164 */     showLocWarning(titlePath, messagePath, null);
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
/*     */   public static void showLocWarning(String path, Object textarea) {
/* 176 */     showWarning(getLoc(path + ".title", "MISSING TITLE"), getLoc(path, "MISSING MESSAGE"), textarea);
/*     */   }
/*     */   
/*     */   public static void showLocWarning(String path) {
/* 180 */     showLocWarning(path, (Object)null);
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
/*     */   public static boolean showQuestion(String title, String question, Object textarea) {
/* 195 */     return (showConfirmDialog(0, 3, title, question, textarea) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean showQuestion(String title, String question) {
/* 200 */     return showQuestion(title, question, null);
/*     */   }
/*     */   
/*     */   public static boolean showLocQuestion(String titlePath, String questionPath, Object textarea) {
/* 204 */     return showQuestion(getLoc(titlePath, "MISSING TITLE"), getLoc(questionPath, "MISSING QUESTION"), textarea);
/*     */   }
/*     */   
/*     */   public static boolean showLocQuestion(String titlePath, String questionPath) {
/* 208 */     return showQuestion(getLoc(titlePath, titlePath), getLoc(questionPath, questionPath), null);
/*     */   }
/*     */   
/*     */   public static boolean showLocQuestion(String path, Object textarea) {
/* 212 */     return showQuestion(getLoc(path + ".title", "MISSING TITLE"), getLoc(path, "MISSING QUESTION"), textarea);
/*     */   }
/*     */   
/*     */   public static boolean showLocQuestion(String path) {
/* 216 */     return showLocQuestion(path, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void showMonolog(int messageType, String title, String message, Object textarea) {
/* 222 */     String button = "ui.ok";
/* 223 */     if (!Localizable.exists())
/* 224 */       button = "OK"; 
/* 225 */     UpdaterButton ok = new UpdaterButton(UpdaterButton.GREEN_COLOR, button);
/* 226 */     addListener(ok, YES);
/* 227 */     ok.setForeground(Color.WHITE);
/* 228 */     JOptionPane.showOptionDialog(frame, new AlertPanel(message, textarea), getTitle(title), 1, messageType, null, new Object[] { ok
/* 229 */         }, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int showErrorMessage(String title, String message, String button1Text, String button2Text) {
/* 234 */     UpdaterButton button1 = new UpdaterButton(UpdaterButton.GREEN_COLOR, button1Text);
/* 235 */     UpdaterButton button2 = new UpdaterButton(UpdaterButton.ORANGE_COLOR, button2Text);
/* 236 */     addListener(button1, YES);
/* 237 */     addListener(button2, NO);
/* 238 */     button1.setForeground(Color.WHITE);
/* 239 */     SwingUtil.setFontSize((JComponent)button1, 13.0F);
/* 240 */     SwingUtil.setFontSize((JComponent)button2, 13.0F);
/* 241 */     button2.setForeground(Color.WHITE);
/* 242 */     return JOptionPane.showOptionDialog(frame, Localizable.get(message), Localizable.get(title), 0, 0, null, new Object[] { button1, button2
/* 243 */         }, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public static void showCustomMonolog(String title, Object textarea) {
/* 247 */     UpdaterButton ok = new UpdaterButton(UpdaterButton.GREEN_COLOR, "ui.ok");
/* 248 */     addListener(ok, YES);
/* 249 */     ok.setForeground(Color.WHITE);
/* 250 */     JOptionPane.showOptionDialog(frame, textarea, getTitle(title), 0, -1, null, new Object[] { ok
/* 251 */         }, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int showConfirmDialog(int optionType, int messageType, String title, String message, Object textarea) {
/* 256 */     return showConfirmDialog(optionType, messageType, title, message, textarea, "ui.yes", "ui.no");
/*     */   }
/*     */ 
/*     */   
/*     */   public static int showConfirmDialog(int optionType, int messageType, String title, String message, Object textarea, String yesText, String noText) {
/* 261 */     UpdaterButton yes = new UpdaterButton(UpdaterButton.GREEN_COLOR, yesText);
/* 262 */     yes.setForeground(Color.WHITE);
/* 263 */     UpdaterButton no = new UpdaterButton(UpdaterButton.ORANGE_COLOR, noText);
/* 264 */     no.setForeground(Color.WHITE);
/* 265 */     addListener(yes, YES);
/* 266 */     addListener(no, NO);
/* 267 */     return JOptionPane.showOptionDialog(frame, new AlertPanel(message, textarea), getTitle(title), 0, messageType, null, new Object[] { yes, no
/* 268 */         }, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public static void prepareLocal() {
/* 272 */     DEFAULT_TITLE = getLoc("alert.error.title", DEFAULT_TITLE);
/* 273 */     DEFAULT_MESSAGE = getLoc("alert.error.message", DEFAULT_MESSAGE);
/*     */   }
/*     */   
/*     */   private static String getTitle(String title) {
/* 277 */     return "TLauncher : " + ((title == null) ? "MISSING TITLE" : title);
/*     */   }
/*     */   
/*     */   private static String getLoc(String path, String fallbackMessage) {
/* 281 */     String result = Localizable.get(path);
/* 282 */     return (result == null) ? fallbackMessage : result;
/*     */   }
/*     */   
/*     */   public static void showMonologError(String message, int number) {
/* 286 */     JOptionPane.showMessageDialog(null, new AlertPanel(message, null), "", number, null);
/*     */   }
/*     */   
/*     */   protected static JOptionPane getOptionPane(JComponent parent) {
/*     */     JOptionPane pane;
/* 291 */     if (!(parent instanceof JOptionPane)) {
/* 292 */       pane = getOptionPane((JComponent)parent.getParent());
/*     */     } else {
/* 294 */       pane = (JOptionPane)parent;
/*     */     } 
/* 296 */     return pane;
/*     */   }
/*     */   
/*     */   public static void addListener(final UpdaterButton changes, final Color color) {
/* 300 */     changes.addActionListener(e -> {
/*     */           JOptionPane pane = getOptionPane((JComponent)e.getSource());
/*     */           pane.setValue(changes);
/*     */         });
/* 304 */     changes.addMouseListener(new MouseAdapter()
/*     */         {
/*     */           public void mouseEntered(MouseEvent e) {
/* 307 */             changes.setBackground(color);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseExited(MouseEvent e) {
/* 312 */             changes.setBackground(changes.getBackgroundColor());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean showWarningMessageWithCheckBox(String title, String message, int width) {
/* 319 */     return showWarningMessageWithCheckBox(title, message, width, "skin.notification.state");
/*     */   }
/*     */   
/*     */   public static boolean showWarningMessageWithCheckBox(String title, String message, int width, String buttonText) {
/* 323 */     UpdaterButton ok = new UpdaterButton(UpdaterButton.GREEN_COLOR, "ui.ok");
/* 324 */     addListener(ok, YES);
/* 325 */     LocalizableCheckbox b = new LocalizableCheckbox(buttonText, LocalizableCheckbox.PANEL_TYPE.SETTINGS);
/* 326 */     b.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
/* 327 */     b.setHorizontalAlignment(0);
/* 328 */     b.setIconTextGap(10);
/* 329 */     b.setState(false);
/* 330 */     JPanel panel = new JPanel(new BorderLayout(0, 10));
/* 331 */     HtmlTextPane pane = HtmlTextPane.get(Localizable.get(message), width);
/* 332 */     panel.add((Component)pane, "Center");
/* 333 */     panel.add((Component)b, "South");
/* 334 */     ok.setForeground(Color.WHITE);
/* 335 */     JOptionPane.showOptionDialog(frame, panel, " " + Localizable.get(title), 0, 1, 
/* 336 */         (Icon)ImageCache.getIcon("warning.png"), new Object[] { ok }, null);
/*     */     
/* 338 */     return b.getState();
/*     */   }
/*     */   
/*     */   public static void showHtmlMessage(String title, String message, int type, int width) {
/*     */     String textValue;
/* 343 */     if (Localizable.exists())
/* 344 */     { textValue = Localizable.get("ui.ok"); }
/* 345 */     else { textValue = "OK"; }
/* 346 */      UpdaterButton ok = new UpdaterButton(UpdaterButton.GREEN_COLOR, textValue);
/* 347 */     addListener(ok, YES);
/* 348 */     ok.setForeground(Color.WHITE);
/* 349 */     JOptionPane.showOptionDialog(frame, HtmlTextPane.get(Localizable.get(message), width), 
/* 350 */         Localizable.get(title), 0, type, null, new Object[] { ok }, null);
/*     */   }
/*     */   
/*     */   public static void showErrorHtml(String message, int width) {
/* 354 */     showHtmlMessage("", message, 0, width);
/*     */   }
/*     */   
/*     */   public static void showErrorHtml(String title, String message) {
/* 358 */     showHtmlMessage(title, message, 0, 500);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void showMessage(String title, JPanel content, JButton[] buttons) {
/* 363 */     JDialog jDialog = new JDialog(frame, title);
/* 364 */     Arrays.<JButton>stream(buttons).forEach(e -> e.addActionListener(()));
/* 365 */     jDialog.setAlwaysOnTop(true);
/* 366 */     jDialog.setResizable(false);
/* 367 */     jDialog.setContentPane(content);
/* 368 */     jDialog.setModal(true);
/* 369 */     jDialog.pack();
/* 370 */     jDialog.setLocationRelativeTo(null);
/* 371 */     jDialog.setVisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean showWarningMessageWithCheckBox1(String title, String message, int width, String buttonText) {
/* 376 */     JButton ok = new JButton("OK");
/* 377 */     ok.addActionListener(e -> {
/*     */           JOptionPane pane = getOptionPane((JComponent)e.getSource());
/*     */           pane.setValue(ok);
/*     */         });
/* 381 */     JCheckBox b = new JCheckBox(buttonText);
/* 382 */     b.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
/* 383 */     b.setHorizontalAlignment(0);
/* 384 */     b.setIconTextGap(10);
/* 385 */     b.getModel().setSelected(false);
/* 386 */     JPanel panel = new JPanel(new BorderLayout(0, 10));
/* 387 */     HtmlTextPane pane = HtmlTextPane.get(Localizable.get(message), width);
/* 388 */     panel.add((Component)pane, "Center");
/* 389 */     panel.add(b, "South");
/* 390 */     ok.setForeground(Color.WHITE);
/* 391 */     JOptionPane.showOptionDialog(null, panel, " " + 
/* 392 */         Localizable.get(title), 0, 1, null, new Object[] { ok }, null);
/*     */     
/* 394 */     return b.getModel().isSelected();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\alert\Alert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */