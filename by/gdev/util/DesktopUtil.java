/*     */ package by.gdev.util;
/*     */ 
/*     */ import by.gdev.util.model.download.Metadata;
/*     */ import by.gdev.util.model.download.Repo;
/*     */ import java.awt.Desktop;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.function.Function;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.client.methods.HttpHead;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class DesktopUtil
/*     */ {
/*  44 */   private static final Logger log = LoggerFactory.getLogger(DesktopUtil.class);
/*     */   
/*     */   private static final String PROTECTION = "protection.txt";
/*     */   
/*     */   private static FileLock lock;
/*  49 */   public static Set<PosixFilePermission> PERMISSIONS = new HashSet<PosixFilePermission>()
/*     */     {
/*     */     
/*     */     };
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
/*     */   public static File getSystemPath(OSInfo.OSType type, String path) {
/*  74 */     String applicationData, folder, userHome = System.getProperty("user.home", ".");
/*     */     
/*  76 */     switch (type)
/*     */     { case LINUX:
/*     */       case SOLARIS:
/*  79 */         file = new File(userHome, path);
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
/*  92 */         return file;case WINDOWS: applicationData = System.getenv("APPDATA"); folder = (applicationData != null) ? applicationData : userHome; file = new File(folder, path); return file;case MACOSX: file = new File(userHome, "Library/Application Support/" + path); return file; }  File file = new File(userHome, path); return file;
/*     */   }
/*     */   
/*     */   public static String getChecksum(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
/*  96 */     byte[] b = createChecksum(file, algorithm);
/*  97 */     StringBuilder result = new StringBuilder();
/*  98 */     for (byte cb : b)
/*  99 */       result.append(Integer.toString((cb & 0xFF) + 256, 16).substring(1)); 
/* 100 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] createChecksum(File file, String algorithm) throws IOException, NoSuchAlgorithmException {
/*     */     // Byte code:
/*     */     //   0: new java/io/BufferedInputStream
/*     */     //   3: dup
/*     */     //   4: new java/io/FileInputStream
/*     */     //   7: dup
/*     */     //   8: aload_0
/*     */     //   9: invokespecial <init> : (Ljava/io/File;)V
/*     */     //   12: invokespecial <init> : (Ljava/io/InputStream;)V
/*     */     //   15: astore_2
/*     */     //   16: aconst_null
/*     */     //   17: astore_3
/*     */     //   18: sipush #8192
/*     */     //   21: newarray byte
/*     */     //   23: astore #4
/*     */     //   25: aload_1
/*     */     //   26: invokestatic getInstance : (Ljava/lang/String;)Ljava/security/MessageDigest;
/*     */     //   29: astore #5
/*     */     //   31: aload_2
/*     */     //   32: aload #4
/*     */     //   34: invokevirtual read : ([B)I
/*     */     //   37: istore #6
/*     */     //   39: iload #6
/*     */     //   41: ifle -> 54
/*     */     //   44: aload #5
/*     */     //   46: aload #4
/*     */     //   48: iconst_0
/*     */     //   49: iload #6
/*     */     //   51: invokevirtual update : ([BII)V
/*     */     //   54: iload #6
/*     */     //   56: iconst_m1
/*     */     //   57: if_icmpne -> 31
/*     */     //   60: aload #5
/*     */     //   62: invokevirtual digest : ()[B
/*     */     //   65: astore #7
/*     */     //   67: aload_2
/*     */     //   68: ifnull -> 97
/*     */     //   71: aload_3
/*     */     //   72: ifnull -> 93
/*     */     //   75: aload_2
/*     */     //   76: invokevirtual close : ()V
/*     */     //   79: goto -> 97
/*     */     //   82: astore #8
/*     */     //   84: aload_3
/*     */     //   85: aload #8
/*     */     //   87: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
/*     */     //   90: goto -> 97
/*     */     //   93: aload_2
/*     */     //   94: invokevirtual close : ()V
/*     */     //   97: aload #7
/*     */     //   99: areturn
/*     */     //   100: astore #4
/*     */     //   102: aload #4
/*     */     //   104: astore_3
/*     */     //   105: aload #4
/*     */     //   107: athrow
/*     */     //   108: astore #9
/*     */     //   110: aload_2
/*     */     //   111: ifnull -> 140
/*     */     //   114: aload_3
/*     */     //   115: ifnull -> 136
/*     */     //   118: aload_2
/*     */     //   119: invokevirtual close : ()V
/*     */     //   122: goto -> 140
/*     */     //   125: astore #10
/*     */     //   127: aload_3
/*     */     //   128: aload #10
/*     */     //   130: invokevirtual addSuppressed : (Ljava/lang/Throwable;)V
/*     */     //   133: goto -> 140
/*     */     //   136: aload_2
/*     */     //   137: invokevirtual close : ()V
/*     */     //   140: aload #9
/*     */     //   142: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #104	-> 0
/*     */     //   #105	-> 18
/*     */     //   #106	-> 25
/*     */     //   #110	-> 31
/*     */     //   #111	-> 39
/*     */     //   #112	-> 44
/*     */     //   #114	-> 54
/*     */     //   #115	-> 60
/*     */     //   #116	-> 67
/*     */     //   #115	-> 97
/*     */     //   #104	-> 100
/*     */     //   #116	-> 108
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   25	75	4	buffer	[B
/*     */     //   31	69	5	complete	Ljava/security/MessageDigest;
/*     */     //   39	61	6	numRead	I
/*     */     //   16	127	2	fis	Ljava/io/BufferedInputStream;
/*     */     //   0	143	0	file	Ljava/io/File;
/*     */     //   0	143	1	algorithm	Ljava/lang/String;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   18	67	100	java/lang/Throwable
/*     */     //   18	67	108	finally
/*     */     //   75	79	82	java/lang/Throwable
/*     */     //   100	110	108	finally
/*     */     //   118	122	125	java/lang/Throwable
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getJavaPathByHome(boolean appendBinFolder) {
/* 120 */     String path = System.getProperty("java.home");
/* 121 */     if (appendBinFolder) {
/* 122 */       path = appendToJVM(path);
/*     */     }
/* 124 */     return path;
/*     */   }
/*     */   
/*     */   public static String appendToJVM(String path) {
/* 128 */     char separator = File.separatorChar;
/* 129 */     StringBuilder b = new StringBuilder(path);
/* 130 */     b.append(separator);
/* 131 */     b.append("bin").append(separator).append("java");
/* 132 */     if (OSInfo.getOSType().equals(OSInfo.OSType.WINDOWS))
/* 133 */       b.append("w.exe"); 
/* 134 */     return b.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T uncheckCall(Callable<T> callable) {
/*     */     try {
/* 146 */       return callable.call();
/* 147 */     } catch (Exception e) {
/* 148 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, R> Function<T, R> wrap(CheckedFunction<T, R> checkedFunction) {
/* 156 */     return t -> {
/*     */         try {
/*     */           return checkedFunction.apply(t);
/* 159 */         } catch (Exception e) {
/*     */           throw new RuntimeException(e);
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   public static void sleep(int milliSeconds) {
/*     */     try {
/* 167 */       Thread.sleep(milliSeconds);
/* 168 */     } catch (InterruptedException e) {
/* 169 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static int numberOfAttempts(List<String> urls, int maxAttepmts, RequestConfig requestConfig, CloseableHttpClient httpclient) {
/* 175 */     int attempt = 1;
/*     */     
/* 177 */     for (String url : urls) {
/*     */       try {
/* 179 */         HttpHead http = new HttpHead(url);
/* 180 */         http.setConfig(requestConfig);
/* 181 */         httpclient.execute((HttpUriRequest)http);
/* 182 */         return maxAttepmts;
/*     */       }
/* 184 */       catch (IOException iOException) {}
/*     */     } 
/*     */     
/* 187 */     return attempt;
/*     */   }
/*     */   
/*     */   private static void createDirectory(File file) throws IOException {
/* 191 */     if (file.isFile())
/*     */       return; 
/* 193 */     if (file.getParentFile() != null)
/* 194 */       file.getParentFile().mkdirs(); 
/*     */   }
/*     */   
/*     */   public static void diactivateDoubleDownloadingResourcesLock() throws IOException {
/* 198 */     if (Objects.nonNull(lock)) {
/* 199 */       lock.release();
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
/*     */   
/*     */   public static String convertListToString(String del, List<Path> list) {
/* 212 */     StringBuilder b = new StringBuilder();
/* 213 */     for (Path string : list) {
/* 214 */       b.append(string).append(del);
/*     */     }
/* 216 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static void activeDoubleDownloadingResourcesLock(String container) throws IOException {
/* 220 */     File f = new File(container, "protection.txt");
/* 221 */     createDirectory(f);
/* 222 */     if (f.exists()) {
/* 223 */       FileChannel ch = FileChannel.open(f.toPath(), new OpenOption[] { StandardOpenOption.WRITE, StandardOpenOption.CREATE });
/* 224 */       lock = ch.tryLock();
/* 225 */       if (Objects.isNull(lock)) {
/* 226 */         log.warn("Lock could not be acquired ");
/* 227 */         System.exit(4);
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
/*     */   public static String getJavaRun(Repo java) {
/* 239 */     String javaRun = null;
/* 240 */     for (Metadata s : java.getResources()) {
/*     */       
/* 242 */       if (s.isExecutable()) {
/* 243 */         javaRun = s.getPath();
/*     */       }
/*     */     } 
/* 246 */     return javaRun;
/*     */   }
/*     */   
/*     */   public static String appendBootstrapperJvm2(String path) {
/* 250 */     StringBuilder b = new StringBuilder();
/* 251 */     if (OSInfo.getOSType() == OSInfo.OSType.MACOSX) {
/* 252 */       b.append("Contents").append(File.separatorChar).append("Home").append(File.separatorChar);
/*     */     }
/* 254 */     return appendToJVM((new File(b.toString())).getPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void openLink(OSInfo.OSType type, String uri) {
/*     */     try {
/* 266 */       Desktop.getDesktop().browse(new URI(uri));
/* 267 */     } catch (IOException|java.net.URISyntaxException e) {
/* 268 */       log.warn("can't open link", e);
/* 269 */       if (type.equals(OSInfo.OSType.LINUX)) {
/*     */         try {
/* 271 */           Runtime.getRuntime().exec("gnome-open " + uri);
/* 272 */         } catch (IOException e1) {
/* 273 */           log.warn("can't open link for linix", e);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void initLookAndFeel() {
/* 282 */     LookAndFeel defaultLookAndFeel = null;
/*     */     try {
/* 284 */       defaultLookAndFeel = UIManager.getLookAndFeel();
/* 285 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/* 286 */       new JFileChooser();
/* 287 */     } catch (Throwable t) {
/* 288 */       log.warn("problem with ", t);
/* 289 */       if (Objects.nonNull(defaultLookAndFeel))
/*     */         try {
/* 291 */           UIManager.setLookAndFeel(defaultLookAndFeel);
/* 292 */         } catch (Throwable e) {
/* 293 */           log.warn("coudn't set defualt look and feel", e);
/*     */         }  
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\DesktopUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */