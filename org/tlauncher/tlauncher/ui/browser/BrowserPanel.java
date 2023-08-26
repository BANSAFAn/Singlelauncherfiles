/*     */ package org.tlauncher.tlauncher.ui.browser;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.Random;
/*     */ import javafx.application.Platform;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.concurrent.Worker;
/*     */ import javafx.embed.swing.JFXPanel;
/*     */ import javafx.scene.Group;
/*     */ import javafx.scene.Scene;
/*     */ import javafx.scene.web.WebEngine;
/*     */ import javafx.scene.web.WebEvent;
/*     */ import javafx.scene.web.WebHistory;
/*     */ import javafx.scene.web.WebView;
/*     */ import netscape.javascript.JSObject;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.launcher.resource.TlauncherResource;
/*     */ import org.tlauncher.skin.domain.AdvertisingDTO;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.handlers.ExceptionHandler;
/*     */ import org.tlauncher.tlauncher.managers.popup.menu.HotServerManager;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.MainPane;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.block.Blockable;
/*     */ import org.tlauncher.tlauncher.ui.block.Blocker;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.loc.LocalizableComponent;
/*     */ import org.tlauncher.tlauncher.ui.scenes.PseudoScene;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.advertising.AdvertisingStatusObserver;
/*     */ import org.tlauncher.util.lang.PageUtil;
/*     */ import org.tlauncher.util.statistics.StatisticsUtil;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.events.Event;
/*     */ import org.w3c.dom.events.EventTarget;
/*     */ import org.w3c.dom.html.HTMLAnchorElement;
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
/*     */ public class BrowserPanel
/*     */   extends JFXPanel
/*     */   implements LocalizableComponent, Blockable, AdvertisingStatusObserver
/*     */ {
/*     */   private static final long serialVersionUID = 5857121254870623943L;
/*     */   private static final boolean freeSurf = false;
/*     */   final BrowserHolder holder;
/*     */   final BrowserBridge bridge;
/*     */   private String failPage;
/*     */   private String style;
/*     */   private String scripts;
/*     */   private String background;
/*     */   private String currentDefaultPage;
/*     */   private int width;
/*     */   private int height;
/*     */   private boolean success;
/*     */   private int counter;
/*     */   private long lastTime;
/*     */   private boolean block;
/*     */   private volatile boolean specialFlag = false;
/*     */   private Group group;
/*     */   private WebView view;
/*     */   private WebEngine engine;
/*     */   private long start;
/*     */   private AdvertisingDTO advertisingDTO;
/*  92 */   private final Object advertisingSynchronizedObject = new Object();
/*     */ 
/*     */   
/*     */   BrowserPanel(BrowserHolder h) throws IOException {
/*  96 */     this.start = System.currentTimeMillis();
/*  97 */     this.holder = h;
/*  98 */     this.bridge = new BrowserBridge(this);
/*     */     
/* 100 */     loadResources();
/* 101 */     loadPageSync();
/* 102 */     Platform.runLater(() -> {
/*     */           Thread.currentThread().setPriority(10);
/*     */           
/*     */           log(new Object[] { "Running in JavaFX Thread" });
/*     */           
/*     */           prepareFX();
/*     */           initBrowser();
/*     */         });
/* 110 */     setOpaque(false);
/* 111 */     addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentResized(ComponentEvent e) {
/* 114 */             BrowserPanel.this.width = BrowserPanel.this.holder.getWidth();
/* 115 */             BrowserPanel.this.height = BrowserPanel.this.holder.getHeight();
/*     */             
/* 117 */             Platform.runLater(() -> {
/*     */                   if (BrowserPanel.this.view != null)
/*     */                     BrowserPanel.this.view.setPrefSize(BrowserPanel.this.width, BrowserPanel.this.height); 
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private static void log(Object... o) {
/* 126 */     U.log(new Object[] { "[Browser]", o });
/*     */   }
/*     */   
/*     */   private static void engine(WebEngine engine, Object... o) {
/* 130 */     U.log(new Object[] { "[Browser@" + engine.hashCode() + "]", o });
/*     */   }
/*     */   
/*     */   private static void page(WebEngine engine, String text, String page) {
/* 134 */     engine(engine, new Object[] { text + ':', '"' + page + '"' });
/*     */   }
/*     */   
/*     */   private synchronized void loadResources() throws IOException {
/* 138 */     log(new Object[] { "Loading resources..." });
/*     */ 
/*     */     
/* 141 */     this.failPage = FileUtil.readStream(TlauncherResource.getResource("fail.html").openStream());
/*     */ 
/*     */     
/* 144 */     this.style = FileUtil.readStream(TlauncherResource.getResource("style.css").openStream());
/*     */ 
/*     */ 
/*     */     
/* 148 */     URL jqueryResource = TlauncherResource.getResource("jquery.js");
/* 149 */     URL pageScriptResource = TlauncherResource.getResource("scripts.js");
/*     */     
/* 151 */     this
/* 152 */       .scripts = FileUtil.readStream(jqueryResource.openStream()) + '\n' + FileUtil.readStream(pageScriptResource.openStream());
/*     */ 
/*     */     
/* 155 */     URL backgroundResource = ImageCache.getRes("plains.png");
/*     */     
/* 157 */     log(new Object[] { "Loading background..." });
/*     */ 
/*     */     
/* 160 */     Object timeLock = new Object();
/* 161 */     URL url = TlauncherResource.getResource("background.image.base64.txt");
/*     */ 
/*     */     
/* 164 */     this.background = FileUtil.readStream(url.openStream(), Charsets.UTF_8.toString());
/* 165 */     log(new Object[] { "Cleaning up after loading:" });
/* 166 */     U.gc();
/*     */   }
/*     */   
/*     */   synchronized void cleanupResources() {
/* 170 */     this.style = null;
/* 171 */     this.scripts = null;
/* 172 */     this.background = null;
/*     */   }
/*     */   
/*     */   private void prepareFX() {
/* 176 */     if (!Platform.isFxApplicationThread()) {
/* 177 */       throw new IllegalStateException("Call this method from Platform.runLater()");
/*     */     }
/* 179 */     log(new Object[] { "Preparing JavaFX..." });
/*     */     
/* 181 */     Thread.currentThread().setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)ExceptionHandler.getInstance());
/*     */     
/* 183 */     this.group = new Group();
/* 184 */     Scene scene = new Scene(this.group);
/*     */     
/* 186 */     setScene(scene);
/*     */   }
/*     */   
/*     */   private synchronized void initBrowser() {
/* 190 */     log(new Object[] { "Initializing..." });
/*     */     
/* 192 */     this.group.getChildren().removeAll(new javafx.scene.Node[0]);
/*     */     
/* 194 */     this.view = JFXStartPageLoader.getInstance().getWebView();
/* 195 */     this.view.setContextMenuEnabled(false);
/* 196 */     this.view.setPrefSize(this.width, this.height);
/*     */     
/* 198 */     this.engine = this.view.getEngine();
/* 199 */     WebEngine currentEngine = this.engine;
/*     */     
/* 201 */     this.engine.setOnAlert(event -> Alert.showMessage(currentEngine.getTitle(), event.getData()));
/*     */     
/* 203 */     this.success = true;
/* 204 */     this.engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/*     */             
/*     */             onBrowserStateChanged(currentEngine, newValue);
/* 211 */           } catch (Throwable e) {
/*     */             engine(currentEngine, new Object[] { "State change handle error:", e });
/*     */           } 
/*     */         });
/* 215 */     if (this.engine.getLoadWorker().getState() == Worker.State.FAILED && !this.specialFlag) {
/* 216 */       String location = this.engine.getLocation();
/* 217 */       failLoad(location);
/*     */     } 
/* 219 */     if (this.engine.getLoadWorker().getState() == Worker.State.SUCCEEDED && !this.specialFlag) {
/* 220 */       String location = this.engine.getLocation();
/* 221 */       loadSucceeded(location);
/*     */     } 
/*     */     
/* 224 */     this.group.getChildren().add(this.view);
/*     */   }
/*     */ 
/*     */   
/*     */   private void failLoad(String location) {
/* 229 */     this.specialFlag = true;
/* 230 */     page(this.engine, "Failed to load", location);
/*     */ 
/*     */     
/* 233 */     if (!this.success) {
/*     */       
/* 235 */       this.holder.setBrowserShown("error", false);
/*     */       
/*     */       return;
/*     */     } 
/* 239 */     this.success = false;
/* 240 */     loadContent(this.failPage);
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
/*     */   private void onBrowserStateChanged(WebEngine engine, Worker.State val) {
/*     */     URI uri;
/* 259 */     if (this.engine != engine) {
/*     */       return;
/*     */     }
/* 262 */     if (val == null) {
/* 263 */       throw new NullPointerException("State is NULL!");
/*     */     }
/* 265 */     String location = engine.getLocation();
/* 266 */     this.view.setMouseTransparent(true);
/*     */     
/* 268 */     switch (val) {
/*     */ 
/*     */ 
/*     */       
/*     */       case SCHEDULED:
/* 273 */         page(engine, "Loading", location);
/*     */         
/* 275 */         if (location.isEmpty() || location.startsWith(TLauncher.getInstance().getPagePrefix()))
/*     */           break; 
/* 277 */         U.debug(new Object[] { "[state] scheduled in" });
/* 278 */         uri = U.makeURI(location);
/* 279 */         if (uri == null)
/* 280 */           uri = U.fixInvallidLink(location); 
/* 281 */         OS.openLink(uri);
/*     */ 
/*     */         
/* 284 */         Platform.runLater(() -> {
/*     */               if (this.block) {
/*     */                 return;
/*     */               }
/*     */               deniedRedirectedRecursion();
/*     */               String last = getLastEntry().getUrl();
/*     */               page(this.engine, "Last entry", last);
/*     */               loadPage(last);
/*     */             });
/*     */         break;
/*     */ 
/*     */       
/*     */       case FAILED:
/* 297 */         failLoad(location);
/*     */       
/*     */       case SUCCEEDED:
/* 300 */         loadSucceeded(location);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void deniedRedirectedRecursion() {
/* 308 */     if (this.counter > 3) {
/* 309 */       if ((new Date()).before(new Date(this.lastTime + 2000L))) {
/* 310 */         this.block = true;
/* 311 */       } else if (this.counter > 3) {
/* 312 */         this.counter = 0;
/*     */       } 
/*     */     }
/* 315 */     this.lastTime = System.currentTimeMillis();
/* 316 */     this.counter++;
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadSucceeded(String location) {
/* 321 */     this.specialFlag = true;
/* 322 */     page(this.engine, "Loaded successfully", location);
/* 323 */     this.holder.setBrowserShown("error", true);
/* 324 */     this.success = true;
/*     */     
/* 326 */     Document document = this.engine.getDocument();
/*     */     
/* 328 */     if (document == null)
/*     */       return; 
/* 330 */     NodeList bodies = document.getElementsByTagName("body");
/* 331 */     Node body = bodies.item(0);
/*     */     
/* 333 */     if (body == null) {
/* 334 */       engine(this.engine, new Object[] { "What the...? Couldn't find <body> element!" });
/*     */       
/*     */       return;
/*     */     } 
/* 338 */     String locale = TLauncher.getInstance().getConfiguration().getLocale().getLanguage();
/* 339 */     if (!location.isEmpty() && !locale.equals((new Locale("zh")).getLanguage())) {
/*     */       
/* 341 */       Element styleElement = document.createElement("style");
/* 342 */       styleElement.setAttribute("type", "text/css");
/* 343 */       styleElement.setTextContent(this.style);
/* 344 */       body.appendChild(styleElement);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 349 */     Element scriptElement = document.createElement("script");
/* 350 */     scriptElement.setAttribute("type", "text/javascript");
/* 351 */     scriptElement.setTextContent(this.scripts);
/* 352 */     body.appendChild(scriptElement);
/*     */ 
/*     */     
/* 355 */     this.engine.executeScript(this.background);
/*     */     
/* 357 */     JSObject jsobj = (JSObject)this.engine.executeScript("window");
/* 358 */     jsobj.setMember("bridge", this.bridge);
/*     */     
/* 360 */     cleanAdvertising(document);
/*     */ 
/*     */ 
/*     */     
/* 364 */     NodeList linkList = document.getElementsByTagName("a");
/* 365 */     for (int i = 0; i < linkList.getLength(); i++) {
/* 366 */       Node linkNode = linkList.item(i);
/* 367 */       EventTarget eventTarget = (EventTarget)linkNode;
/*     */       
/* 369 */       eventTarget.addEventListener("click", evt -> { EventTarget target = evt.getCurrentTarget(); HTMLAnchorElement anchorElement = (HTMLAnchorElement)target; String href = anchorElement.getHref(); if (href == null || href.isEmpty() || href.startsWith("javascript:")) return;  URI uri = U.makeURI(href); if (uri == null) uri = U.fixInvallidLink(href);  OS.openLink(uri); evt.preventDefault(); }false);
/*     */     } 
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
/* 386 */     Node adyoutube = document.getElementById("adyoutube");
/* 387 */     if (adyoutube != null) {
/* 388 */       ((EventTarget)adyoutube).addEventListener("click", evt -> StatisticsUtil.startSending("save/adyoutube", null, Maps.newHashMap()), false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 394 */     mixServers(document);
/* 395 */     addListensOnMenuServer(document);
/*     */ 
/*     */     
/* 398 */     Element serverInfoPage = document.getElementById("server_info_page");
/* 399 */     if (serverInfoPage != null) {
/* 400 */       ((EventTarget)serverInfoPage).addEventListener("click", evt -> { if (((HotServerManager)TLauncher.getInjector().getInstance(HotServerManager.class)).isReady()) { MainPane mp = (TLauncher.getInstance().getFrame()).mp; mp.setScene((PseudoScene)mp.additionalHostServerScene); }  }false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 408 */     processSidebarMessage(document);
/* 409 */     if (Blocker.isBlocked(this))
/*     */     {
/* 411 */       block((Object)null);
/*     */     }
/* 413 */     this.view.setMouseTransparent(false);
/*     */   }
/*     */   
/*     */   private void processSidebarMessage(Document document) {
/* 417 */     Element sidebarButton = document.getElementById("sidebar_1_button");
/* 418 */     Configuration configuration = TLauncher.getInstance().getConfiguration();
/* 419 */     if (sidebarButton != null) {
/*     */       
/* 421 */       Element el = document.getElementById("sidebar_1");
/* 422 */       if (configuration.getBoolean("main.cross.close")) {
/* 423 */         if (el != null) {
/* 424 */           removeChilds(el);
/*     */         }
/*     */       } else {
/* 427 */         ((EventTarget)sidebarButton).addEventListener("click", evt -> { if (el != null) { removeChilds(el); configuration.set("main.cross.close", Boolean.valueOf(true)); }  }false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void cleanAdvertising(Document document) {
/* 440 */     synchronized (this.advertisingSynchronizedObject) {
/* 441 */       if (this.advertisingDTO == null) {
/*     */         try {
/* 443 */           log(new Object[] { "fx thread has waited status of ad" });
/* 444 */           this.advertisingSynchronizedObject.wait();
/* 445 */           log(new Object[] { "fx thread continuing work" });
/* 446 */         } catch (InterruptedException e) {
/* 447 */           log(new Object[] { String.format("got status of advertising: %s", new Object[] { this.advertisingDTO }) });
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 452 */     if (this.advertisingDTO.isInfoServerAdvertising()) {
/* 453 */       removeNode("advertising_block", document);
/*     */     }
/* 455 */     if (this.advertisingDTO.isCenterAdvertising()) {
/* 456 */       removeNode("adnew", document);
/*     */     }
/* 458 */     if (this.advertisingDTO.isVideoAdvertising()) {
/* 459 */       removeNode("adyoutube", document);
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeNode(String name, Document document) {
/* 464 */     Node node = document.getElementById(name);
/* 465 */     if (node != null) {
/* 466 */       node.getParentNode().removeChild(node);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeChilds(Node node) {
/* 473 */     while (node.hasChildNodes()) {
/* 474 */       node.removeChild(node.getFirstChild());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void mixServers(Document document) {
/* 481 */     Node node = document.getElementById("advertising_servers");
/* 482 */     if (node == null)
/*     */       return; 
/* 484 */     NodeList list = node.getChildNodes();
/* 485 */     if (list == null || list.getLength() < 2)
/*     */       return; 
/* 487 */     Node element = list.item(1);
/*     */     
/* 489 */     NodeList listRowServer = element.getChildNodes();
/* 490 */     ArrayList<Node> listOld = new ArrayList<>();
/* 491 */     for (int i = 0; i < listRowServer.getLength(); i++) {
/* 492 */       Node el = listRowServer.item(i);
/* 493 */       listOld.add(el);
/*     */     } 
/*     */     
/* 496 */     removeChilds(element);
/* 497 */     Random random = new Random();
/* 498 */     while (!listOld.isEmpty()) {
/* 499 */       int index = random.nextInt(listOld.size());
/* 500 */       element.appendChild(listOld.remove(index));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addListensOnMenuServer(Document doc) {
/* 510 */     log(new Object[] { "add listens into server_choose" });
/*     */     
/* 512 */     NodeList list = doc.getElementsByTagName("server");
/* 513 */     for (int i = 0; i < list.getLength(); i++) {
/* 514 */       ((EventTarget)list.item(i)).addEventListener("mouseover", evt -> ((HotServerManager)TLauncher.getInjector().getInstance(HotServerManager.class)).processingEvent(extractServer(evt)), false);
/*     */     }
/*     */     
/* 517 */     log(new Object[] { "finished listens into server_choose" });
/*     */   }
/*     */   
/*     */   private WebHistory.Entry getLastEntry() {
/* 521 */     WebHistory hist = this.engine.getHistory();
/* 522 */     return hist.getEntries().get(hist.getCurrentIndex());
/*     */   }
/*     */   
/*     */   private String extractServer(Event evt) {
/* 526 */     Node node = (Node)evt.getCurrentTarget();
/* 527 */     NamedNodeMap map = node.getAttributes();
/* 528 */     if (map == null || map.getLength() < 2) {
/* 529 */       log(new Object[] { "map=" + map + "or map.getLength() <3" });
/* 530 */       return "";
/*     */     } 
/* 532 */     Node idServer = map.item(1);
/* 533 */     if (idServer != null) {
/* 534 */       if (idServer.getNodeValue() != null && !idServer.getNodeValue().isEmpty()) {
/* 535 */         if (evt instanceof org.w3c.dom.events.MouseEvent) {
/* 536 */           return idServer.getNodeValue();
/*     */         }
/*     */         
/* 539 */         log(new Object[] { "problems with your browser" });
/*     */       }
/*     */       else {
/*     */         
/* 543 */         log(new Object[] { "idServer is null or empty" });
/*     */       } 
/*     */     } else {
/* 546 */       log(new Object[] { "error the node doesn't have childNodes " + node.toString() });
/*     */     } 
/* 548 */     return null;
/*     */   }
/*     */   
/*     */   private void loadPage(String url) {
/* 552 */     U.debug(new Object[] { "load started after init object = " + ((System.currentTimeMillis() - this.start) / 1000L) });
/* 553 */     engine(this.engine, new Object[] { "Trying to load URL: \"" + url + "\"" });
/* 554 */     this.engine.load(url);
/*     */   }
/*     */   
/*     */   private void loadContent(String content) {
/* 558 */     this.engine.loadContent(content);
/*     */   }
/*     */   
/*     */   void execute(String script) {
/*     */     try {
/* 563 */       this.engine.executeScript(script);
/* 564 */     } catch (Exception e) {
/* 565 */       U.log(new Object[] { "Hidden JS exception:", e });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void block(Object reason) {
/* 571 */     Platform.runLater(() -> execute("page.visibility.hide();"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void unblock(Object reason) {
/* 576 */     Platform.runLater(() -> execute("page.visibility.show();"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateLocale() {
/* 583 */     String oldDefaultPage = this.currentDefaultPage;
/* 584 */     this.currentDefaultPage = PageUtil.buildPagePath();
/*     */     
/* 586 */     if (oldDefaultPage == null || oldDefaultPage.equals(this.currentDefaultPage)) {
/*     */       return;
/*     */     }
/* 589 */     Platform.runLater(() -> loadPage(this.currentDefaultPage));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadPageSync() {
/* 596 */     String oldDefaultPage = this.currentDefaultPage;
/* 597 */     this.currentDefaultPage = PageUtil.buildPagePath();
/*     */     
/* 599 */     if (oldDefaultPage == null || oldDefaultPage.equals(this.currentDefaultPage))
/*     */       return; 
/* 601 */     loadPage(this.currentDefaultPage);
/*     */   }
/*     */   
/*     */   public void setAdvertising(AdvertisingDTO dto) {
/* 605 */     this.advertisingDTO = dto;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void advertisingReceived(AdvertisingDTO advertisingDTO) {
/* 611 */     synchronized (this.advertisingSynchronizedObject) {
/* 612 */       this.advertisingDTO = advertisingDTO;
/* 613 */       this.advertisingSynchronizedObject.notify();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\browser\BrowserPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */