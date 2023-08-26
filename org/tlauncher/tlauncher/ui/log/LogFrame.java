/*     */ package org.tlauncher.tlauncher.ui.log;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import net.minecraft.launcher.Http;
/*     */ import org.tlauncher.tlauncher.configuration.InnerConfiguration;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.util.ValidateUtil;
/*     */ import org.tlauncher.util.SwingUtil;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class LogFrame extends JFrame {
/*     */   private final JFrame parent;
/*     */   
/*     */   public LogFrame(JFrame frame, Throwable errorMessage) {
/*  33 */     if (frame == null) {
/*  34 */       this.parent = new JFrame();
/*     */     } else {
/*  36 */       this.parent = frame;
/*  37 */     }  setBounds(100, 100, 400, 366);
/*  38 */     this.contentPane = new JPanel();
/*  39 */     this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
/*  40 */     setContentPane(this.contentPane);
/*  41 */     this.contentPane.setLayout((LayoutManager)null);
/*  42 */     setLocationRelativeTo((Component)null);
/*  43 */     setAlwaysOnTop(true);
/*  44 */     setResizable(false);
/*  45 */     setTitle(Localizable.get("log.form.title"));
/*  46 */     SwingUtil.setFavicons(this);
/*     */     
/*  48 */     JPanel panel = new JPanel();
/*  49 */     panel.setBounds(0, 11, 400, 316);
/*  50 */     this.contentPane.add(panel);
/*  51 */     panel.setLayout((LayoutManager)null);
/*     */     
/*  53 */     JLabel lblEmail = new JLabel(Localizable.get("check.email.name"));
/*  54 */     lblEmail.setBounds(10, 10, 334, 15);
/*  55 */     panel.add(lblEmail);
/*     */     
/*  57 */     this.emailField = new JTextField();
/*  58 */     this.emailField.setBounds(10, 25, 207, 20);
/*  59 */     panel.add(this.emailField);
/*  60 */     this.emailField.setColumns(10);
/*     */     
/*  62 */     JLabel descriptionErrorLabel = new JLabel(Localizable.get("log.email.error.description"));
/*  63 */     descriptionErrorLabel.setBounds(10, 55, 334, 15);
/*  64 */     panel.add(descriptionErrorLabel);
/*     */     
/*  66 */     JScrollPane scrollPane = new JScrollPane();
/*  67 */     scrollPane.setHorizontalScrollBarPolicy(31);
/*  68 */     scrollPane.setBounds(10, 70, 380, 80);
/*  69 */     panel.add(scrollPane);
/*     */     
/*  71 */     JTextArea descriptionErrorArea = new JTextArea();
/*  72 */     descriptionErrorArea.setLineWrap(true);
/*  73 */     descriptionErrorArea.setRows(5);
/*  74 */     descriptionErrorArea.setColumns(10);
/*  75 */     scrollPane.setViewportView(descriptionErrorArea);
/*     */     
/*  77 */     JScrollPane scrollPane_1 = new JScrollPane();
/*  78 */     scrollPane_1.setHorizontalScrollBarPolicy(31);
/*  79 */     scrollPane_1.setViewportBorder(UIManager.getBorder("TextPane.border"));
/*  80 */     scrollPane_1.setBounds(10, 175, 380, 96);
/*  81 */     panel.add(scrollPane_1);
/*     */     
/*  83 */     JTextArea outputErrorArea = new JTextArea();
/*     */     
/*  85 */     outputErrorArea.setColumns(10);
/*  86 */     outputErrorArea.setRows(5);
/*  87 */     outputErrorArea.setLineWrap(true);
/*  88 */     scrollPane_1.setViewportView(outputErrorArea);
/*     */     
/*  90 */     outputErrorArea.setText(errorMessage.getClass().getName() + ": " + errorMessage.getMessage());
/*  91 */     outputErrorArea.setEditable(false);
/*  92 */     outputErrorArea.setEnabled(true);
/*     */     
/*  94 */     JButton btnNewButton = new JButton(Localizable.get("log.form.send"));
/*  95 */     btnNewButton.setBounds(61, 282, 134, 23);
/*  96 */     panel.add(btnNewButton);
/*     */     
/*  98 */     JLabel outputLabelError = new JLabel(Localizable.get("log.email.error.issue"));
/*  99 */     outputLabelError.setBounds(10, 161, 334, 14);
/* 100 */     panel.add(outputLabelError);
/*     */     
/* 102 */     JButton noSendButton = new JButton(Localizable.get("log.form.send.no"));
/* 103 */     noSendButton.setBounds(205, 282, 128, 23);
/* 104 */     panel.add(noSendButton);
/* 105 */     this.parent.setEnabled(false);
/*     */     
/* 107 */     addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent e)
/*     */           {
/* 111 */             LogFrame.this.releaseLog(LogFrame.this.parent);
/*     */           }
/*     */         });
/* 114 */     btnNewButton.addActionListener(e -> {
/*     */           if (!this.emailField.getText().isEmpty() && !ValidateUtil.validateEmail(this.emailField.getText())) {
/*     */             JOptionPane.showMessageDialog(this, "check.email.input");
/*     */             
/*     */             return;
/*     */           } 
/*     */           releaseLog(this.parent);
/*     */           setVisible(false);
/*     */           this.parent.setEnabled(true);
/*     */           InnerConfiguration s = TLauncher.getInnerSettings();
/*     */           try {
/*     */             Map<String, Object> query = Maps.newHashMap();
/*     */             query.put("version", Double.valueOf(TLauncher.getVersion()));
/*     */             query.put("clientType", s.get("type"));
/*     */             URL url = Http.constantURL(Http.get(TLauncher.getInnerSettings().get("log.system"), query));
/*     */             Http.performPost(url, U.readFileLog().getBytes(TlauncherUtil.LOG_CHARSET), "text/plain", true);
/*     */             Alert.showMonologError(Localizable.get().get("alert.error.send.log.success"), 1);
/* 131 */           } catch (Throwable ex) {
/*     */             StringWriter stringWriter = new StringWriter();
/*     */             
/*     */             ex.printStackTrace(new PrintWriter(stringWriter));
/*     */             U.log(new Object[] { stringWriter.toString() });
/*     */             Alert.showMonologError(Localizable.get().get("alert.error.send.log.unsuccess"), 0);
/*     */           } 
/*     */           dispose();
/*     */         });
/* 140 */     noSendButton.addActionListener(e -> releaseLog(this.parent));
/*     */   }
/*     */   private JPanel contentPane; private JTextField emailField;
/*     */   private void releaseLog(JFrame parent) {
/* 144 */     parent.setEnabled(true);
/* 145 */     dispose();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\log\LogFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */