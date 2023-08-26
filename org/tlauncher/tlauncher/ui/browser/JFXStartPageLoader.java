/*    */ package org.tlauncher.tlauncher.ui.browser;
/*    */ 
/*    */ import javafx.application.Platform;
/*    */ import javafx.beans.value.ChangeListener;
/*    */ import javafx.beans.value.ObservableValue;
/*    */ import javafx.concurrent.Worker;
/*    */ import javafx.embed.swing.JFXPanel;
/*    */ import javafx.scene.web.WebView;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.util.OS;
/*    */ import org.tlauncher.util.U;
/*    */ import org.tlauncher.util.lang.PageUtil;
/*    */ 
/*    */ public class JFXStartPageLoader
/*    */   extends JFXPanel {
/*    */   private String loadPage;
/*    */   private static JFXStartPageLoader jfxStartPageLoader;
/*    */   private WebView view;
/*    */   private long start;
/*    */   
/*    */   private JFXStartPageLoader() {
/* 24 */     if (OS.is(new OS[] { OS.LINUX })) {
/*    */       try {
/* 26 */         new JFXPanel();
/* 27 */       } catch (NoClassDefFoundError e) {
/* 28 */         Alert.showErrorHtml("alert.error.linux.javafx", 400);
/* 29 */         System.exit(1);
/*    */       } 
/*    */     }
/*    */     
/* 33 */     this.loadPage = PageUtil.buildPagePath();
/*    */     
/* 35 */     Platform.runLater(new Runnable()
/*    */         {
/*    */           public void run()
/*    */           {
/* 39 */             JFXStartPageLoader.this.view = new WebView();
/* 40 */             JFXStartPageLoader.this.view.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
/*    */                 {
/*    */                   public void changed(ObservableValue<? extends Worker.State> arg0, Worker.State arg1, Worker.State arg2)
/*    */                   {
/* 44 */                     switch (arg2) {
/*    */                       case SUCCEEDED:
/* 46 */                         JFXStartPageLoader.this.log(new Object[] { "succeeded load page: " + JFXStartPageLoader.access$100(this.this$1.this$0) + " during " + ((System.currentTimeMillis() - JFXStartPageLoader.access$200(this.this$1.this$0)) / 1000L) });
/*    */                         break;
/*    */                       case FAILED:
/* 49 */                         JFXStartPageLoader.this.log(new Object[] { "fail load page: " + JFXStartPageLoader.access$100(this.this$1.this$0) });
/*    */                         break;
/*    */                       case SCHEDULED:
/* 52 */                         JFXStartPageLoader.this.log(new Object[] { "start load page: " + JFXStartPageLoader.access$100(this.this$1.this$0) });
/* 53 */                         JFXStartPageLoader.this.start = System.currentTimeMillis();
/*    */                         break;
/*    */                     } 
/*    */ 
/*    */ 
/*    */                   
/*    */                   }
/*    */                 });
/* 61 */             JFXStartPageLoader.this.view.getEngine().load(JFXStartPageLoader.this.loadPage);
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public static synchronized JFXStartPageLoader getInstance() {
/*    */     try {
/* 68 */       if (jfxStartPageLoader == null)
/* 69 */         jfxStartPageLoader = new JFXStartPageLoader(); 
/* 70 */     } catch (RuntimeException e) {
/* 71 */       if (e.getMessage().contains("glass.dll")) {
/* 72 */         String url = " https://tlauncher.org/ru/unsatisfiedlinkerror-java-bin-glass.html";
/* 73 */         if (!TLauncher.getInnerSettings().isUSSRLocale())
/* 74 */           url = " https://tlauncher.org/en/unsatisfiedlinkerror-java-bin-glass.html"; 
/* 75 */         Alert.showErrorHtml("", Localizable.get("alert.start.message", new Object[] { url }));
/* 76 */         TLauncher.kill();
/*    */       } 
/*    */     } 
/* 79 */     return jfxStartPageLoader;
/*    */   }
/*    */ 
/*    */   
/*    */   public WebView getWebView() {
/* 84 */     return this.view;
/*    */   }
/*    */   private void log(Object... objects) {
/* 87 */     U.log(new Object[] { "[JFXStartPageLoader] ", objects });
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\browser\JFXStartPageLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */