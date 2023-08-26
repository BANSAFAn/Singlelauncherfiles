/*     */ package by.gdev.util;
/*     */ 
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OSInfo
/*     */ {
/*     */   private static final String OS_NAME = "os.name";
/*     */   private static final String OS_VERSION = "os.version";
/*     */   private static final PrivilegedAction<OSType> osTypeAction;
/*  15 */   private static final Map<String, WindowsVersion> windowsVersionMap = new HashMap<>();
/*  16 */   public static final WindowsVersion WINDOWS_UNKNOWN = new WindowsVersion(-1, -1);
/*  17 */   public static final WindowsVersion WINDOWS_95 = new WindowsVersion(4, 0);
/*  18 */   public static final WindowsVersion WINDOWS_98 = new WindowsVersion(4, 10);
/*  19 */   public static final WindowsVersion WINDOWS_ME = new WindowsVersion(4, 90);
/*  20 */   public static final WindowsVersion WINDOWS_2000 = new WindowsVersion(5, 0);
/*  21 */   public static final WindowsVersion WINDOWS_XP = new WindowsVersion(5, 1);
/*  22 */   public static final WindowsVersion WINDOWS_2003 = new WindowsVersion(5, 2);
/*  23 */   public static final WindowsVersion WINDOWS_VISTA = new WindowsVersion(6, 0);
/*  24 */   public static final WindowsVersion WINDOWS_7 = new WindowsVersion(6, 1);
/*  25 */   public static final WindowsVersion WINDOWS_8 = new WindowsVersion(6, 2);
/*  26 */   public static final WindowsVersion WINDOWS_8_1 = new WindowsVersion(6, 3);
/*  27 */   public static final WindowsVersion WINDOWS_10 = new WindowsVersion(10, 0);
/*     */   
/*     */   public enum OSType {
/*  30 */     WINDOWS,
/*  31 */     LINUX,
/*  32 */     SOLARIS,
/*  33 */     MACOSX,
/*  34 */     UNKNOWN;
/*     */   }
/*     */ 
/*     */   
/*     */   public static OSType getOSType() throws SecurityException {
/*  39 */     String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
/*  40 */     if (osName != null) {
/*  41 */       if (osName.contains("windows")) {
/*  42 */         return OSType.WINDOWS;
/*     */       }
/*     */       
/*  45 */       if (osName.contains("os x") || osName.contains("mac")) {
/*  46 */         return OSType.MACOSX;
/*     */       }
/*     */       
/*  49 */       if (osName.contains("linux") || osName.contains("unix")) {
/*  50 */         return OSType.LINUX;
/*     */       }
/*     */       
/*  53 */       if (osName.contains("solaris") || osName.contains("sunos")) {
/*  54 */         return OSType.SOLARIS;
/*     */       }
/*     */     } 
/*     */     
/*  58 */     return OSType.UNKNOWN;
/*     */   }
/*     */   public static Arch getJavaBit() {
/*  61 */     String res = System.getProperty("sun.arch.data.model");
/*  62 */     if (res != null && res.equalsIgnoreCase("64"))
/*  63 */       return Arch.x64; 
/*  64 */     return Arch.x32;
/*     */   }
/*     */   
/*     */   public static PrivilegedAction<OSType> getOSTypeAction() {
/*  68 */     return osTypeAction;
/*     */   }
/*     */   
/*     */   public static WindowsVersion getWindowsVersion() throws SecurityException {
/*  72 */     String windowsVersion = System.getProperty("os.version");
/*  73 */     if (windowsVersion == null) {
/*  74 */       return WINDOWS_UNKNOWN;
/*     */     }
/*  76 */     synchronized (windowsVersionMap) {
/*  77 */       WindowsVersion currentVersion = windowsVersionMap.get(windowsVersion);
/*  78 */       if (currentVersion == null) {
/*  79 */         String[] data = windowsVersion.split("\\.");
/*  80 */         if (data.length != 2) {
/*  81 */           return WINDOWS_UNKNOWN;
/*     */         }
/*     */         
/*     */         try {
/*  85 */           currentVersion = new WindowsVersion(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
/*  86 */         } catch (NumberFormatException var6) {
/*  87 */           return WINDOWS_UNKNOWN;
/*     */         } 
/*     */         
/*  90 */         windowsVersionMap.put(windowsVersion, currentVersion);
/*     */       } 
/*     */       
/*  93 */       return currentVersion;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/*  99 */     windowsVersionMap.put(WINDOWS_95.toString(), WINDOWS_95);
/* 100 */     windowsVersionMap.put(WINDOWS_98.toString(), WINDOWS_98);
/* 101 */     windowsVersionMap.put(WINDOWS_ME.toString(), WINDOWS_ME);
/* 102 */     windowsVersionMap.put(WINDOWS_2000.toString(), WINDOWS_2000);
/* 103 */     windowsVersionMap.put(WINDOWS_XP.toString(), WINDOWS_XP);
/* 104 */     windowsVersionMap.put(WINDOWS_2003.toString(), WINDOWS_2003);
/* 105 */     windowsVersionMap.put(WINDOWS_VISTA.toString(), WINDOWS_VISTA);
/* 106 */     windowsVersionMap.put(WINDOWS_7.toString(), WINDOWS_7);
/* 107 */     windowsVersionMap.put(WINDOWS_8.toString(), WINDOWS_8);
/* 108 */     windowsVersionMap.put(WINDOWS_8_1.toString(), WINDOWS_8_1);
/* 109 */     windowsVersionMap.put(WINDOWS_10.toString(), WINDOWS_10);
/* 110 */     osTypeAction = OSInfo::getOSType;
/*     */   }
/*     */   
/*     */   public static class WindowsVersion implements Comparable<WindowsVersion> {
/*     */     private final int major;
/*     */     private final int minor;
/*     */     
/*     */     private WindowsVersion(int var1, int var2) {
/* 118 */       this.major = var1;
/* 119 */       this.minor = var2;
/*     */     }
/*     */     
/*     */     public int getMajor() {
/* 123 */       return this.major;
/*     */     }
/*     */     
/*     */     public int getMinor() {
/* 127 */       return this.minor;
/*     */     }
/*     */     
/*     */     public int compareTo(WindowsVersion version) {
/* 131 */       int major = this.major - version.getMajor();
/* 132 */       if (major == 0) {
/* 133 */         major = this.minor - version.getMinor();
/*     */       }
/*     */       
/* 136 */       return major;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj) {
/* 140 */       return (obj instanceof WindowsVersion && compareTo((WindowsVersion)obj) == 0);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 144 */       return 31 * this.major + this.minor;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 148 */       return this.major + "." + this.minor;
/*     */     } }
/*     */   
/*     */   public enum Arch {
/* 152 */     x32, x64;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\OSInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */