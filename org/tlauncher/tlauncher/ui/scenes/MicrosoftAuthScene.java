/*    */ package org.tlauncher.tlauncher.ui.scenes;
/*    */ import com.sun.webkit.network.CookieManager;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.lang.reflect.Field;
/*    */ import java.net.CookieHandler;
/*    */ import java.util.Map;
/*    */ import javafx.application.Platform;
/*    */ import javafx.beans.value.ObservableValue;
/*    */ import javafx.embed.swing.JFXPanel;
/*    */ import javafx.scene.Group;
/*    */ import javafx.scene.Scene;
/*    */ import javafx.scene.web.WebEngine;
/*    */ import javafx.scene.web.WebEvent;
/*    */ import javafx.scene.web.WebView;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.tlauncher.tlauncher.handlers.ExceptionHandler;
/*    */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*    */ import org.tlauncher.tlauncher.minecraft.user.oauth.OAuthApplication;
/*    */ import org.tlauncher.tlauncher.ui.MainPane;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ import org.tlauncher.tlauncher.ui.listener.auth.AuthenticatorListener;
/*    */ import org.tlauncher.tlauncher.ui.swing.extended.ExtendedPanel;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class MicrosoftAuthScene extends PseudoScene {
/* 27 */   public static final Dimension SIZE = new Dimension(MainPane.SIZE.width, MainPane.SIZE.height);
/* 28 */   private final ExtendedPanel base = new ExtendedPanel();
/*    */   private WebEngine engine;
/*    */   private final MicrosoftBrowerPanel panel;
/*    */   
/*    */   public MicrosoftAuthScene(MainPane main) {
/* 33 */     super(main);
/* 34 */     this.panel = new MicrosoftBrowerPanel();
/* 35 */     this.panel.setPreferredSize(new Dimension((SIZE.getSize()).width - 250, (SIZE.getSize()).height - 80));
/* 36 */     this.base.add(this.panel);
/* 37 */     this.base.setSize(SIZE);
/* 38 */     add((Component)this.base);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setShown(boolean shown) {
/* 43 */     if (shown) {
/* 44 */       Platform.runLater(() -> {
/*    */             try {
/*    */               cleanCookie();
/*    */               
/*    */               OAuthApplication m = OAuthApplication.TLAUNCHER_PARAMETERS;
/*    */               String url = String.format("%s?client_id=%s&response_type=code&redirect_uri=%s&scope=%s", new Object[] { m.getBasicURL(), m.getClientId(), m.getRedirectURL(), m.getScope() });
/*    */               this.engine.load(url);
/* 51 */             } catch (Throwable e) {
/*    */               U.log(new Object[] { e });
/*    */             } 
/*    */           });
/*    */     }
/* 56 */     super.setShown(shown);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void cleanCookie() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
/* 62 */     CookieManager cookieManager = (CookieManager)CookieHandler.getDefault();
/* 63 */     Field f = cookieManager.getClass().getDeclaredField("store");
/* 64 */     f.setAccessible(true);
/* 65 */     Object cookieStore = f.get(cookieManager);
/*    */     
/* 67 */     Field bucketsField = Class.forName("com.sun.webkit.network.CookieStore").getDeclaredField("buckets");
/* 68 */     bucketsField.setAccessible(true);
/* 69 */     Map buckets = (Map)bucketsField.get(cookieStore);
/* 70 */     f.setAccessible(true);
/* 71 */     buckets.clear();
/*    */   }
/*    */   
/*    */   private class MicrosoftBrowerPanel
/*    */     extends JFXPanel {
/*    */     public MicrosoftBrowerPanel() {
/* 77 */       Platform.runLater(() -> {
/*    */             Thread.currentThread().setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)ExceptionHandler.getInstance());
/*    */             Group group = new Group();
/*    */             Scene scene = new Scene(group);
/*    */             setScene(scene);
/*    */             WebView view = new WebView();
/*    */             view.setContextMenuEnabled(false);
/*    */             MicrosoftAuthScene.this.engine = view.getEngine();
/*    */             MicrosoftAuthScene.this.engine.setOnAlert(());
/*    */             group.getChildren().add(view);
/*    */             view.getEngine().locationProperty().addListener(());
/*    */           });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\scenes\MicrosoftAuthScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */