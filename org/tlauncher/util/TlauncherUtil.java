/*     */ package org.tlauncher.util;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.gson.Gson;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.time.LocalDate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import javax.swing.JFrame;
/*     */ import net.minecraft.launcher.Http;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.ModifiedVersion;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.time.DateUtils;
/*     */ import org.apache.http.client.methods.HttpRequestBase;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.configuration.InnerConfiguration;
/*     */ import org.tlauncher.tlauncher.configuration.LangConfiguration;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
/*     */ import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.log.LogFrame;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.model.JavaConfig;
/*     */ import org.tlauncher.tlauncher.updater.bootstrapper.model.JavaDownloadedElement;
/*     */ import org.tlauncher.util.async.AsyncThread;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TlauncherUtil
/*     */ {
/*     */   public static final String PROTECTED_DOUBLE_RUNNING_FILE = "doubleRunningProtection.txt";
/*     */   private static final int CAN_RUNNING_AFTER = 1;
/*  64 */   public static final String LOG_CHARSET = defineCharsetString("cp1251");
/*     */   public static final String TLAUNCHER_ADDITIONAL_CONFIG = "TLauncherAdditional";
/*  66 */   private static volatile long time = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sendLog(Throwable e) {
/*  74 */     if (TLauncher.DEBUG) {
/*     */       return;
/*     */     }
/*  77 */     if (Localizable.get() == null) {
/*     */       try {
/*  79 */         Configuration settings = Configuration.createConfiguration();
/*  80 */         Locale locale = settings.getLocale();
/*     */         
/*  82 */         InnerConfiguration innerConfig = new InnerConfiguration(FileUtil.getResourceAppStream("/inner.tlauncher.properties"));
/*  83 */         Localizable.setLang(new LangConfiguration(settings.getLocales(), locale, innerConfig
/*  84 */               .get("tlauncher.language.folder")));
/*  85 */       } catch (IOException e1) {
/*  86 */         e1.addSuppressed(e);
/*     */ 
/*     */         
/*  89 */         e = e1;
/*  90 */         e1.printStackTrace();
/*     */       } 
/*     */     }
/*  93 */     (new LogFrame((JFrame)TLauncher.getInstance().getFrame(), e)).setVisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void sendLog(String type) {
/*  98 */     if (TLauncher.DEBUG) {
/*     */       return;
/*     */     }
/* 101 */     if (time + 120000L > System.currentTimeMillis()) {
/*     */       return;
/*     */     }
/*     */     
/* 105 */     time = System.currentTimeMillis();
/*     */     try {
/* 107 */       Map<String, Object> query = Maps.newHashMap();
/* 108 */       query.put("version", Double.valueOf(TLauncher.getVersion()));
/* 109 */       query.put("clientType", type);
/* 110 */       URL url = Http.constantURL(Http.get(TLauncher.getInnerSettings().get("log.system"), query));
/* 111 */       Http.performPost(url, U.readFileLog().getBytes(LOG_CHARSET), "text/plain", true);
/* 112 */     } catch (Throwable ex) {
/* 113 */       StringWriter stringWriter = new StringWriter();
/* 114 */       ex.printStackTrace(new PrintWriter(stringWriter));
/* 115 */       U.log(new Object[] { stringWriter.toString() });
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void checkRedirect() {}
/*     */ 
/*     */   
/*     */   public static int hostAvailabilityCheck(String host) {
/* 124 */     if (host.endsWith("/"))
/* 125 */       host = host.substring(0, host.length() - 1); 
/*     */     try {
/* 127 */       URL url = new URL(host);
/* 128 */       HttpURLConnection httpConn = Downloadable.setUp(url.openConnection(), true);
/* 129 */       httpConn.setRequestMethod("HEAD");
/* 130 */       httpConn.setInstanceFollowRedirects(true);
/* 131 */       httpConn.connect();
/* 132 */       U.debug(new Object[] { host + " : " + httpConn.getResponseCode() });
/* 133 */       return httpConn.getResponseCode();
/* 134 */     } catch (Throwable e) {
/* 135 */       U.debug(new Object[] { host + " is down " });
/* 136 */       return 500;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void testNet() {
/*     */     try {
/* 142 */       Http.performGet("https://tlauncher.org/repo/update/lch/additional_hot_servers.json");
/* 143 */       Http.performGet("https://dl2.fastrepo.org/not_remove_test_file.txt");
/* 144 */       testNet1();
/* 145 */     } catch (Throwable e) {
/* 146 */       if (e instanceof javax.net.ssl.SSLHandshakeException) {
/* 147 */         Alert.showLocWarning("", "block.doctor.web", null);
/*     */       } else {
/* 149 */         testNet1();
/* 150 */       }  U.log(new Object[] { "error", e });
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void testNet1() {
/* 155 */     int code = hostAvailabilityCheck("http://page.tlauncher.org");
/* 156 */     if (code == 503 || code == 403) {
/* 157 */       Alert.showErrorHtml("", "alert.block.ip");
/*     */     }
/*     */   }
/*     */   
/*     */   public static void deactivateSSL() {
/* 162 */     Configuration c = TLauncher.getInstance().getConfiguration();
/*     */     
/* 164 */     if (c.get("ssl.deactivate.date") == null || 
/* 165 */       LocalDate.parse(c.get("ssl.deactivate.date")).isBefore(LocalDate.now())) {
/* 166 */       TLauncher.getInstance().getConfiguration().set("ssl.deactivate.date", LocalDate.now().plusMonths(1L));
/*     */       try {
/* 168 */         TLauncher.getInstance().getConfiguration().save();
/* 169 */       } catch (IOException e1) {
/* 170 */         U.log(new Object[] { e1 });
/*     */       } 
/*     */     } 
/*     */     try {
/* 174 */       SSLContext context = SSLContext.getInstance("SSL");
/* 175 */       context.init(null, (TrustManager[])new X509TrustManager[] { new X509TrustManager()
/*     */             {
/*     */               public void checkClientTrusted(X509Certificate[] chain, String authType) {}
/*     */ 
/*     */               
/*     */               public void checkServerTrusted(X509Certificate[] chain, String authType) {}
/*     */               
/*     */               public X509Certificate[] getAcceptedIssuers() {
/* 183 */                 return new X509Certificate[0];
/*     */               }
/*     */             },   }, null);
/* 186 */       HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
/* 187 */     } catch (Throwable var1) {
/* 188 */       U.log(new Object[] { var1 });
/*     */     } 
/*     */     
/* 191 */     HttpsURLConnection.setDefaultHostnameVerifier((s, sslSession) -> true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkServersAvalability(String[] array) {
/* 202 */     List<CompletableFuture<Integer>> servers = (List<CompletableFuture<Integer>>)Arrays.<String>stream(array).map(link -> CompletableFuture.supplyAsync((), AsyncThread.getService())).collect(Collectors.toList());
/*     */     try {
/* 204 */       CompletableFuture.allOf((CompletableFuture<?>[])servers.<CompletableFuture>toArray(new CompletableFuture[0])).get();
/* 205 */       if (servers.stream().allMatch(e -> {
/*     */             try {
/*     */               return ((Integer)e.get()).equals(Integer.valueOf(200));
/* 208 */             } catch (Exception exception) {
/*     */               return false;
/*     */             } 
/*     */           })) {
/* 212 */         U.log(new Object[] { "#####    all servers are available   ######" });
/*     */       }
/* 214 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public static int checkDoubleRunning() {
/* 219 */     File f = MinecraftUtil.getTLauncherFile("doubleRunningProtection.txt");
/* 220 */     if (Files.exists(f.toPath(), new java.nio.file.LinkOption[0])) {
/*     */       try {
/* 222 */         Date start = new Date(Long.parseLong(FileUtil.readFile(f)));
/* 223 */         Date end = new Date(start.getTime() + TimeUnit.MINUTES.toMillis(1L));
/* 224 */         Date current = new Date();
/* 225 */         if (current.after(start) && current.before(end))
/* 226 */           return 
/* 227 */             (int)(TimeUnit.MINUTES.toSeconds(1L) - (current.getTime() - start.getTime()) / 1000L); 
/* 228 */       } catch (Throwable e) {
/* 229 */         U.log(new Object[] { e });
/*     */       } 
/*     */     }
/* 232 */     return 0;
/*     */   }
/*     */   
/*     */   public static String resolveHostName(String path) throws MalformedURLException, UnknownHostException {
/* 236 */     URL url = new URL(path);
/* 237 */     return url.getProtocol() + "://" + InetAddress.getByName(url.getHost()).getHostAddress() + ":" + url.getPort() + url
/* 238 */       .getFile();
/*     */   }
/*     */   
/*     */   public static boolean isAdmin() {
/* 242 */     if (!OS.is(new OS[] { OS.WINDOWS }))
/* 243 */       return true; 
/*     */     try {
/* 245 */       Class<?> cl = Class.forName("com.sun.security.auth.module.NTSystem");
/* 246 */       if (Objects.isNull(cl)) {
/* 247 */         return true;
/*     */       }
/* 249 */       Method method = cl.getMethod("getGroupIDs", new Class[0]);
/* 250 */       String[] groups = (String[])method.invoke(cl.newInstance(), new Object[0]);
/* 251 */       for (String group : groups) {
/* 252 */         if (group.equals("S-1-5-32-544"))
/* 253 */           return true; 
/*     */       } 
/* 255 */       return false;
/* 256 */     } catch (Exception e) {
/* 257 */       U.log(new Object[] { e });
/*     */       
/* 259 */       return true;
/*     */     } 
/*     */   }
/*     */   public static String getPageLanguage() {
/* 263 */     if (Objects.nonNull(TLauncher.getInstance()) && Objects.nonNull(TLauncher.getInstance().getConfiguration()) && 
/* 264 */       TLauncher.getInstance().getConfiguration().isUSSRLocale()) {
/* 265 */       return "ru";
/*     */     }
/* 267 */     return "en";
/*     */   }
/*     */   
/*     */   public static String findJavaOptionAndGetName() {
/* 271 */     for (Map.Entry<String, String> e : System.getenv().entrySet()) {
/* 272 */       if (((String)e.getKey()).equalsIgnoreCase("_java_options"))
/* 273 */         return e.getKey(); 
/* 274 */     }  return null;
/*     */   }
/*     */   
/*     */   public static String getStringError(Throwable e) {
/* 278 */     StringWriter stringWriter = new StringWriter();
/* 279 */     e.printStackTrace(new PrintWriter(stringWriter));
/* 280 */     return stringWriter.toString().replaceAll(System.lineSeparator(), "<br>");
/*     */   }
/*     */   
/*     */   public static File getJVMFolder(JavaConfig config, File tlauncherFolder) {
/* 284 */     JavaDownloadedElement java = getProperJavaElement(config);
/* 285 */     return new File(new File(tlauncherFolder, "jvms"), java.getJavaFolder());
/*     */   }
/*     */   
/*     */   public static JavaDownloadedElement getProperJavaElement(JavaConfig config) {
/* 289 */     if (useX64JavaInsteadX32Java()) {
/* 290 */       return (JavaDownloadedElement)((Map)config.getConfig().get(OS.CURRENT)).get(OS.Arch.x64);
/*     */     }
/* 292 */     return (JavaDownloadedElement)((Map)config.getConfig().get(OS.CURRENT)).get(OS.Arch.CURRENT);
/*     */   }
/*     */   
/*     */   public static boolean useX64JavaInsteadX32Java() {
/* 296 */     if (OS.is(new OS[] { OS.WINDOWS }) && OS.Arch.CURRENT.equals(OS.Arch.x32)) {
/* 297 */       String s = OS.executeByTerminal("wmic os get osarchitecture");
/* 298 */       return s.contains("64");
/*     */     } 
/* 300 */     return false;
/*     */   }
/*     */   
/*     */   public static void showCriticalProblem(String message) {
/* 304 */     Alert.showErrorHtml("A critical error has occurred, ask for help <br> <a href='https://vk.me/tlauncher'> https://vk.me/tlauncher </a> or by mail <b> support@tlauncher.org </b><br><br>" + message, 500);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void showCriticalProblem(Throwable e) {
/* 311 */     showCriticalProblem(getStringError(e));
/* 312 */     TLauncher.kill();
/*     */   }
/*     */   
/*     */   public static boolean hasCorrectJavaFX() {
/*     */     try {
/* 317 */       Class.forName("javafx.embed.swing.JFXPanel");
/* 318 */     } catch (Throwable e) {
/* 319 */       return false;
/*     */     } 
/* 321 */     return true;
/*     */   }
/*     */   
/*     */   public static void fillGPUInfo(Configuration con, boolean wait) {
/*     */     try {
/* 326 */       if (OS.is(new OS[] { OS.WINDOWS })) {
/* 327 */         Path dxdiag = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), new String[] { "logs", "tlauncher", "dxdiag.txt" });
/*     */         
/* 329 */         boolean dxdiagExist = Files.exists(dxdiag, new java.nio.file.LinkOption[0]);
/* 330 */         if (!dxdiagExist || FileUtils.isFileOlder(dxdiag.toFile(), DateUtils.addDays(new Date(), -10))) {
/* 331 */           String command = String.format("dxdiag /whql:off /t %s", new Object[] { dxdiag.toString() });
/* 332 */           OS.executeByTerminal(command);
/*     */         } 
/* 334 */         AsyncThread.execute(() -> {
/*     */               if (wait) {
/*     */                 U.sleepFor(15000L);
/*     */               }
/*     */               
/*     */               if (Files.exists(dxdiag, new java.nio.file.LinkOption[0])) {
/*     */                 try {
/*     */                   String file = FileUtil.readFile(dxdiag.toFile(), Charset.defaultCharset().name());
/*     */                   
/*     */                   String[] params = file.split(System.lineSeparator());
/*     */                   
/*     */                   String name = Arrays.<String>stream(params).filter(()).map(()).collect(Collectors.joining(","));
/*     */                   
/*     */                   if (StringUtils.isNotBlank(name)) {
/*     */                     con.set("gpu.info.full", name);
/*     */                   }
/*     */                   
/*     */                   List<String> names = (List<String>)Arrays.<String>stream(params).filter(()).map(()).collect(Collectors.toList());
/*     */                   
/*     */                   if (!names.isEmpty()) {
/*     */                     String gpu = ((String)names.get(names.size() - 1)).trim();
/*     */                     if (!gpu.equalsIgnoreCase("Intel(R) HD Graphics")) {
/*     */                       con.set("gpu.info", gpu);
/*     */                     }
/*     */                   } 
/* 359 */                 } catch (IOException e) {
/*     */                   
/*     */                   e.printStackTrace();
/*     */                 } 
/*     */               }
/*     */             });
/* 365 */       } else if (OS.is(new OS[] { OS.LINUX })) {
/* 366 */         String res = OS.executeByTerminal("lshw -C display");
/* 367 */         String[] params = res.split(System.lineSeparator());
/*     */         
/* 369 */         List<String> names = (List<String>)Arrays.<String>stream(params).filter(e -> e.contains("product:")).map(s -> s.split(":")[1]).collect(Collectors.toList());
/* 370 */         if (!names.isEmpty()) {
/* 371 */           con.set("gpu.info", ((String)names.get(names.size() - 1)).trim());
/* 372 */           con.set("gpu.info.full", ((String)names.get(names.size() - 1)).trim());
/*     */         }
/*     */       
/* 375 */       } else if (OS.is(new OS[] { OS.OSX })) {
/* 376 */         String res = OS.executeByTerminal("system_profiler SPDisplaysDataType");
/*     */ 
/*     */ 
/*     */         
/* 380 */         List<String> names = (List<String>)Arrays.<String>stream(res.split(System.lineSeparator())).filter(e -> e.toLowerCase().contains("chipset model:")).map(s -> s.split(":")[1]).collect(Collectors.toList());
/* 381 */         if (!names.isEmpty()) {
/* 382 */           con.set("gpu.info", ((String)names.get(names.size() - 1)).trim());
/* 383 */           con.set("gpu.info.full", ((String)names.get(names.size() - 1)).trim());
/*     */         } 
/*     */       } 
/* 386 */     } catch (Throwable e) {
/* 387 */       U.log(new Object[] { e });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String defineCharsetString(String charset) {
/* 392 */     if (Charset.isSupported(charset)) {
/* 393 */       return charset;
/*     */     }
/* 395 */     return Charset.defaultCharset().name();
/*     */   }
/*     */   
/*     */   public static void createTimeStart() {
/*     */     try {
/* 400 */       File file = MinecraftUtil.getTLauncherFile("doubleRunningProtection.txt");
/* 401 */       FileUtil.writeFile(file, "" + (new Date()).getTime());
/* 402 */     } catch (Throwable e) {
/* 403 */       U.log(new Object[] { "can't delete file", e });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void clearTimeLabel() {
/* 408 */     U.log(new Object[] { "[Double running]", "clear time label" });
/*     */     try {
/* 410 */       FileUtil.deleteFile(MinecraftUtil.getTLauncherFile("doubleRunningProtection.txt"));
/* 411 */     } catch (Throwable e) {
/* 412 */       U.log(new Object[] { "can't delete file", e });
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void processRemoteVersionToSave(CompleteVersion complete, String remoteVersion, Gson gson) {
/* 417 */     ModifiedVersion modifiedVersion = complete.getModifiedVersion();
/* 418 */     if (Objects.isNull(complete.getInheritsFrom()) && notHasAnyAdditionalTLauncherField(modifiedVersion)) {
/* 419 */       modifiedVersion.setRemoteVersion(remoteVersion);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean notHasAnyAdditionalTLauncherField(ModifiedVersion modifiedVersion) {
/* 424 */     return (modifiedVersion.getJar() == null && modifiedVersion.getModpack() == null && modifiedVersion
/* 425 */       .getModsLibraries() == null && modifiedVersion.getAdditionalFiles() == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addAuthHeaders(HttpRequestBase http) throws RequiredTLAccountException, SelectedAnyOneTLAccountException {
/* 430 */     TLauncher tl = TLauncher.getInstance();
/* 431 */     Account ac = tl.getProfileManager().findUniqueTlauncherAccount();
/* 432 */     http.addHeader("uuid", tl.getProfileManager().getClientToken().toString());
/* 433 */     http.addHeader("accessToken", ac.getAccessToken());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\TlauncherUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */