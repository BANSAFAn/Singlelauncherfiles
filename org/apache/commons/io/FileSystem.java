/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
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
/*     */ public enum FileSystem
/*     */ {
/*  39 */   GENERIC(false, false, 2147483647, 2147483647, new char[] { Character.MIN_VALUE }, new String[0], false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   LINUX(true, true, 255, 4096, new char[] { Character.MIN_VALUE, '/' }, new String[0], false),
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
/*  56 */   MAC_OSX(true, true, 255, 1024, new char[] { Character.MIN_VALUE, '/', ':' }, new String[0], false),
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
/*  77 */   WINDOWS(false, true, 255, 32000, new char[] { Character.MIN_VALUE, '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', '"', '*', '/', ':', '<', '>', '?', '\\', '|' }, new String[] { "AUX", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "CON", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9", "NUL", "PRN" }, true);
/*     */   
/*     */   private static final boolean IS_OS_LINUX;
/*     */   
/*     */   private static final boolean IS_OS_MAC;
/*     */   
/*     */   private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
/*     */   
/*     */   private static final boolean IS_OS_WINDOWS;
/*     */   
/*     */   private final boolean casePreserving;
/*     */   
/*     */   private final boolean caseSensitive;
/*     */   
/*     */   private final char[] illegalFileNameChars;
/*     */   
/*     */   private final int maxFileNameLength;
/*     */   
/*     */   private final int maxPathLength;
/*     */   private final String[] reservedFileNames;
/*     */   private final boolean supportsDriveLetter;
/*     */   
/*     */   static {
/* 100 */     IS_OS_LINUX = getOsMatchesName("Linux");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     IS_OS_MAC = getOsMatchesName("Mac");
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
/* 125 */     IS_OS_WINDOWS = getOsMatchesName("Windows");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FileSystem getCurrent() {
/* 133 */     if (IS_OS_LINUX) {
/* 134 */       return LINUX;
/*     */     }
/* 136 */     if (IS_OS_MAC) {
/* 137 */       return MAC_OSX;
/*     */     }
/* 139 */     if (IS_OS_WINDOWS) {
/* 140 */       return WINDOWS;
/*     */     }
/* 142 */     return GENERIC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean getOsMatchesName(String osNamePrefix) {
/* 153 */     return isOsNameMatch(getSystemProperty("os.name"), osNamePrefix);
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
/*     */   private static String getSystemProperty(String property) {
/*     */     try {
/* 171 */       return System.getProperty(property);
/* 172 */     } catch (SecurityException ex) {
/*     */       
/* 174 */       System.err.println("Caught a SecurityException reading the system property '" + property + "'; the SystemUtils property value will default to null.");
/*     */       
/* 176 */       return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isOsNameMatch(String osName, String osNamePrefix) {
/* 193 */     if (osName == null) {
/* 194 */       return false;
/*     */     }
/* 196 */     return osName.toUpperCase(Locale.ROOT).startsWith(osNamePrefix.toUpperCase(Locale.ROOT));
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
/*     */   FileSystem(boolean caseSensitive, boolean casePreserving, int maxFileLength, int maxPathLength, char[] illegalFileNameChars, String[] reservedFileNames, boolean supportsDriveLetter) {
/* 221 */     this.maxFileNameLength = maxFileLength;
/* 222 */     this.maxPathLength = maxPathLength;
/* 223 */     this.illegalFileNameChars = Objects.<char[]>requireNonNull(illegalFileNameChars, "illegalFileNameChars");
/* 224 */     this.reservedFileNames = Objects.<String[]>requireNonNull(reservedFileNames, "reservedFileNames");
/* 225 */     this.caseSensitive = caseSensitive;
/* 226 */     this.casePreserving = casePreserving;
/* 227 */     this.supportsDriveLetter = supportsDriveLetter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] getIllegalFileNameChars() {
/* 236 */     return (char[])this.illegalFileNameChars.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxFileNameLength() {
/* 245 */     return this.maxFileNameLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxPathLength() {
/* 254 */     return this.maxPathLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getReservedFileNames() {
/* 263 */     return (String[])this.reservedFileNames.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCasePreserving() {
/* 272 */     return this.casePreserving;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCaseSensitive() {
/* 281 */     return this.caseSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isIllegalFileNameChar(char c) {
/* 292 */     return (Arrays.binarySearch(this.illegalFileNameChars, c) >= 0);
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
/*     */   public boolean isLegalFileName(CharSequence candidate) {
/* 305 */     if (candidate == null || candidate.length() == 0 || candidate.length() > this.maxFileNameLength) {
/* 306 */       return false;
/*     */     }
/* 308 */     if (isReservedFileName(candidate)) {
/* 309 */       return false;
/*     */     }
/* 311 */     for (int i = 0; i < candidate.length(); i++) {
/* 312 */       if (isIllegalFileNameChar(candidate.charAt(i))) {
/* 313 */         return false;
/*     */       }
/*     */     } 
/* 316 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReservedFileName(CharSequence candidate) {
/* 327 */     return (Arrays.binarySearch((Object[])this.reservedFileNames, candidate) >= 0);
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
/*     */   public boolean supportsDriveLetter() {
/* 343 */     return this.supportsDriveLetter;
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
/*     */   public String toLegalFileName(String candidate, char replacement) {
/* 359 */     if (isIllegalFileNameChar(replacement))
/* 360 */       throw new IllegalArgumentException(
/* 361 */           String.format("The replacement character '%s' cannot be one of the %s illegal characters: %s", new Object[] {
/*     */               
/* 363 */               (replacement == '\000') ? "\\0" : Character.valueOf(replacement), name(), Arrays.toString(this.illegalFileNameChars)
/*     */             })); 
/* 365 */     String truncated = (candidate.length() > this.maxFileNameLength) ? candidate.substring(0, this.maxFileNameLength) : candidate;
/*     */     
/* 367 */     boolean changed = false;
/* 368 */     char[] charArray = truncated.toCharArray();
/* 369 */     for (int i = 0; i < charArray.length; i++) {
/* 370 */       if (isIllegalFileNameChar(charArray[i])) {
/* 371 */         charArray[i] = replacement;
/* 372 */         changed = true;
/*     */       } 
/*     */     } 
/* 375 */     return changed ? String.valueOf(charArray) : truncated;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\FileSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */