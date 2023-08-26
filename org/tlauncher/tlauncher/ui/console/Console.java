/*     */ package org.tlauncher.tlauncher.ui.console;
/*     */ 
/*     */ import com.google.inject.Singleton;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.swing.SwingUtilities;
/*     */ import org.apache.log4j.Appender;
/*     */ import org.apache.log4j.Layout;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.WriterAppender;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedComponentAdapter;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.async.AsyncThread;
/*     */ import org.tlauncher.util.pastebin.Paste;
/*     */ import org.tlauncher.util.pastebin.PasteResult;
/*     */ 
/*     */ @Singleton
/*     */ public class Console
/*     */   extends WriterAppender {
/*     */   private ConsoleFrame frame;
/*     */   private Configuration global;
/*     */   private boolean killed;
/*     */   private volatile MinecraftLauncher launcher;
/*     */   
/*     */   public void init(final Configuration global, boolean show) {
/*  42 */     this.global = global;
/*     */     
/*  44 */     this.frame = new ConsoleFrame(this);
/*  45 */     update();
/*  46 */     updateLocale();
/*     */     
/*  48 */     this.frame.addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowClosing(WindowEvent e) {
/*  51 */             Console.this.setShown(false);
/*  52 */             global.set("gui.console", ConsoleType.NONE);
/*     */           }
/*     */ 
/*     */           
/*     */           public void windowClosed(WindowEvent e) {
/*  57 */             U.log(new Object[] { "Console", Console.access$000(this.this$0), "has been disposed." });
/*     */           }
/*     */         });
/*     */     
/*  61 */     this.frame.addComponentListener((ComponentListener)new ExtendedComponentAdapter(this.frame)
/*     */         {
/*     */           public void componentShown(ComponentEvent e) {
/*  64 */             Console.this.save(true);
/*     */           }
/*     */ 
/*     */           
/*     */           public void componentHidden(ComponentEvent e) {
/*  69 */             Console.this.save(true);
/*     */           }
/*     */ 
/*     */           
/*     */           public void onComponentResized(ComponentEvent e) {
/*  74 */             Console.this.save(true);
/*     */           }
/*     */ 
/*     */           
/*     */           public void onComponentMoved(ComponentEvent e) {
/*  79 */             Console.this.save(true);
/*     */           }
/*     */         });
/*     */     
/*  83 */     this.frame.addComponentListener(new ComponentListener()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/*  86 */             Console.this.save(false);
/*     */           }
/*     */ 
/*     */           
/*     */           public void componentMoved(ComponentEvent e) {
/*  91 */             Console.this.save(false);
/*     */           }
/*     */ 
/*     */           
/*     */           public void componentShown(ComponentEvent e) {
/*  96 */             Console.this.save(true);
/*     */           }
/*     */ 
/*     */           
/*     */           public void componentHidden(ComponentEvent e) {
/* 101 */             Console.this.save(true);
/*     */           }
/*     */         });
/*     */     
/* 105 */     activateOptions();
/* 106 */     if (show)
/* 107 */       show(); 
/*     */   }
/*     */   
/*     */   void update() {
/* 111 */     check();
/* 112 */     if (this.global == null) {
/*     */       return;
/*     */     }
/* 115 */     String prefix = "gui.console.";
/* 116 */     int width = this.global.getInteger(prefix + "width", 670);
/* 117 */     int height = this.global.getInteger(prefix + "height", 500);
/* 118 */     int x = this.global.getInteger(prefix + "x", 0);
/* 119 */     int y = this.global.getInteger(prefix + "y", 0);
/*     */     
/* 121 */     this.frame.setSize(width, height);
/* 122 */     this.frame.setLocation(x, y);
/*     */   }
/*     */   
/*     */   void save() {
/* 126 */     save(false);
/*     */   }
/*     */   
/*     */   void save(boolean flush) {
/* 130 */     check();
/* 131 */     if (this.global == null) {
/*     */       return;
/*     */     }
/* 134 */     String prefix = "gui.console.";
/* 135 */     int[] size = getSize(), position = getPosition();
/*     */     
/* 137 */     this.global.set(prefix + "width", Integer.valueOf(size[0]), false);
/* 138 */     this.global.set(prefix + "height", Integer.valueOf(size[1]), false);
/* 139 */     this.global.set(prefix + "x", Integer.valueOf(position[0]), false);
/* 140 */     this.global.set(prefix + "y", Integer.valueOf(position[1]), false);
/*     */   }
/*     */   
/*     */   private void check() {
/* 144 */     if (this.killed)
/* 145 */       throw new IllegalStateException("Console is already killed!"); 
/*     */   }
/*     */   
/*     */   public void setShown(boolean shown) {
/* 149 */     if (shown) {
/* 150 */       show();
/*     */     } else {
/* 152 */       hide();
/*     */     } 
/*     */   }
/*     */   public void show() {
/* 156 */     show(true);
/*     */   }
/*     */   
/*     */   public void show(boolean toFront) {
/* 160 */     SwingUtilities.invokeLater(() -> {
/*     */           if (!this.frame.isVisible()) {
/*     */             check();
/*     */             Logger.getRootLogger().addAppender((Appender)this);
/*     */             this.frame.setVisible(true);
/*     */             this.frame.print(U.readFileLog());
/*     */             this.frame.scrollDown();
/*     */           } 
/*     */           if (toFront) {
/*     */             this.frame.toFront();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void hide() {
/* 175 */     check();
/* 176 */     this.frame.setVisible(false);
/* 177 */     Logger.getRootLogger().removeAppender((Appender)this);
/* 178 */     this.frame.clear();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 182 */     check();
/* 183 */     this.frame.clear();
/*     */   }
/*     */   
/*     */   public void sendPaste() {
/* 187 */     AsyncThread.execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 190 */             Paste paste = new Paste();
/* 191 */             paste.addListener(Console.this.frame);
/*     */             
/* 193 */             paste.setTitle(Console.this.frame.getTitle());
/* 194 */             paste.setContent(U.readFileLog());
/* 195 */             PasteResult result = paste.paste();
/*     */             
/* 197 */             if (result instanceof PasteResult.PasteUploaded) {
/* 198 */               PasteResult.PasteUploaded uploaded = (PasteResult.PasteUploaded)result;
/*     */               
/* 200 */               if (Alert.showLocQuestion("console.pastebin.sent", uploaded.getURL())) {
/* 201 */                 OS.openLink(uploaded.getURL());
/*     */               }
/* 203 */             } else if (result instanceof PasteResult.PasteFailed) {
/* 204 */               Throwable error = ((PasteResult.PasteFailed)result).getError();
/*     */               
/* 206 */               if (error instanceof RuntimeException) {
/* 207 */                 Alert.showLocError("console.pastebin.invalid", error);
/* 208 */               } else if (error instanceof IOException) {
/* 209 */                 Alert.showLocError("console.pastebin.failed", error);
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   Point getPositionPoint() {
/* 218 */     check();
/* 219 */     return this.frame.getLocation();
/*     */   }
/*     */   
/*     */   int[] getPosition() {
/* 223 */     check();
/* 224 */     Point p = getPositionPoint();
/* 225 */     return new int[] { p.x, p.y };
/*     */   }
/*     */   
/*     */   Dimension getDimension() {
/* 229 */     check();
/* 230 */     return this.frame.getSize();
/*     */   }
/*     */   
/*     */   int[] getSize() {
/* 234 */     check();
/* 235 */     Dimension d = getDimension();
/* 236 */     return new int[] { d.width, d.height };
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateLocale() {
/* 241 */     this.frame.updateLocale();
/*     */   }
/*     */   
/*     */   public void setLauncherToKillProcess(MinecraftLauncher launcher) {
/* 245 */     this.launcher = launcher;
/* 246 */     this.frame.bottom.kill.setEnabled((launcher != null));
/*     */   }
/*     */   
/*     */   public void killProcess() {
/* 250 */     this.launcher.killProcess();
/* 251 */     this.frame.show(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/* 256 */     setName("tl console");
/* 257 */     setLayout((Layout)U.LOG_LAYOUT);
/* 258 */     setThreshold((Priority)Level.INFO);
/* 259 */     setEncoding(TlauncherUtil.LOG_CHARSET);
/*     */     
/* 261 */     setWriter(createWriter(new OutputStream() {
/* 262 */             ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */ 
/*     */             
/*     */             public void write(int b) throws IOException {
/* 266 */               char c = (char)b;
/* 267 */               if (c == '\n')
/* 268 */               { Console.this.frame.println(this.out.toString(TlauncherUtil.LOG_CHARSET));
/* 269 */                 this.out.reset(); }
/* 270 */               else { this.out.write(b); }
/*     */             
/*     */             }
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\console\Console.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */