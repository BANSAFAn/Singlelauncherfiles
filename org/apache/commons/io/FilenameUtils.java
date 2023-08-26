/*      */ package org.apache.commons.io;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Deque;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FilenameUtils
/*      */ {
/*   89 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String EMPTY_STRING = "";
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int NOT_FOUND = -1;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final char EXTENSION_SEPARATOR = '.';
/*      */ 
/*      */ 
/*      */   
/*  105 */   public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char UNIX_SEPARATOR = '/';
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char WINDOWS_SEPARATOR = '\\';
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  120 */   private static final char SYSTEM_SEPARATOR = File.separatorChar;
/*      */ 
/*      */   
/*      */   private static final char OTHER_SEPARATOR;
/*      */ 
/*      */   
/*      */   static {
/*  127 */     if (isSystemWindows()) {
/*  128 */       OTHER_SEPARATOR = '/';
/*      */     } else {
/*  130 */       OTHER_SEPARATOR = '\\';
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isSystemWindows() {
/*  146 */     return (SYSTEM_SEPARATOR == '\\');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isSeparator(char ch) {
/*  156 */     return (ch == '/' || ch == '\\');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String normalize(String fileName) {
/*  200 */     return doNormalize(fileName, SYSTEM_SEPARATOR, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String normalize(String fileName, boolean unixSeparator) {
/*  247 */     char separator = unixSeparator ? '/' : '\\';
/*  248 */     return doNormalize(fileName, separator, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String normalizeNoEndSeparator(String fileName) {
/*  293 */     return doNormalize(fileName, SYSTEM_SEPARATOR, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String normalizeNoEndSeparator(String fileName, boolean unixSeparator) {
/*  340 */     char separator = unixSeparator ? '/' : '\\';
/*  341 */     return doNormalize(fileName, separator, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String doNormalize(String fileName, char separator, boolean keepSeparator) {
/*  353 */     if (fileName == null) {
/*  354 */       return null;
/*      */     }
/*      */     
/*  357 */     requireNonNullChars(fileName);
/*      */     
/*  359 */     int size = fileName.length();
/*  360 */     if (size == 0) {
/*  361 */       return fileName;
/*      */     }
/*  363 */     int prefix = getPrefixLength(fileName);
/*  364 */     if (prefix < 0) {
/*  365 */       return null;
/*      */     }
/*      */     
/*  368 */     char[] array = new char[size + 2];
/*  369 */     fileName.getChars(0, fileName.length(), array, 0);
/*      */ 
/*      */     
/*  372 */     char otherSeparator = (separator == SYSTEM_SEPARATOR) ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;
/*  373 */     for (int i = 0; i < array.length; i++) {
/*  374 */       if (array[i] == otherSeparator) {
/*  375 */         array[i] = separator;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  380 */     boolean lastIsDirectory = true;
/*  381 */     if (array[size - 1] != separator) {
/*  382 */       array[size++] = separator;
/*  383 */       lastIsDirectory = false;
/*      */     } 
/*      */ 
/*      */     
/*      */     int j;
/*      */     
/*  389 */     for (j = (prefix != 0) ? prefix : 1; j < size; j++) {
/*  390 */       if (array[j] == separator && array[j - 1] == separator) {
/*  391 */         System.arraycopy(array, j, array, j - 1, size - j);
/*  392 */         size--;
/*  393 */         j--;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  398 */     for (j = prefix + 1; j < size; j++) {
/*  399 */       if (array[j] == separator && array[j - 1] == '.' && (j == prefix + 1 || array[j - 2] == separator)) {
/*      */         
/*  401 */         if (j == size - 1) {
/*  402 */           lastIsDirectory = true;
/*      */         }
/*  404 */         System.arraycopy(array, j + 1, array, j - 1, size - j);
/*  405 */         size -= 2;
/*  406 */         j--;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  412 */     for (j = prefix + 2; j < size; j++) {
/*  413 */       if (array[j] == separator && array[j - 1] == '.' && array[j - 2] == '.' && (j == prefix + 2 || array[j - 3] == separator)) {
/*      */         
/*  415 */         if (j == prefix + 2) {
/*  416 */           return null;
/*      */         }
/*  418 */         if (j == size - 1) {
/*  419 */           lastIsDirectory = true;
/*      */         }
/*      */         
/*  422 */         int k = j - 4; while (true) { if (k >= prefix) {
/*  423 */             if (array[k] == separator) {
/*      */               
/*  425 */               System.arraycopy(array, j + 1, array, k + 1, size - j);
/*  426 */               size -= j - k;
/*  427 */               j = k + 1; break;
/*      */             } 
/*      */             k--;
/*      */             continue;
/*      */           } 
/*  432 */           System.arraycopy(array, j + 1, array, prefix, size - j);
/*  433 */           size -= j + 1 - prefix;
/*  434 */           j = prefix + 1; break; }
/*      */       
/*      */       } 
/*      */     } 
/*  438 */     if (size <= 0) {
/*  439 */       return "";
/*      */     }
/*  441 */     if (size <= prefix) {
/*  442 */       return new String(array, 0, size);
/*      */     }
/*  444 */     if (lastIsDirectory && keepSeparator) {
/*  445 */       return new String(array, 0, size);
/*      */     }
/*  447 */     return new String(array, 0, size - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String concat(String basePath, String fullFileNameToAdd) {
/*  491 */     int prefix = getPrefixLength(fullFileNameToAdd);
/*  492 */     if (prefix < 0) {
/*  493 */       return null;
/*      */     }
/*  495 */     if (prefix > 0) {
/*  496 */       return normalize(fullFileNameToAdd);
/*      */     }
/*  498 */     if (basePath == null) {
/*  499 */       return null;
/*      */     }
/*  501 */     int len = basePath.length();
/*  502 */     if (len == 0) {
/*  503 */       return normalize(fullFileNameToAdd);
/*      */     }
/*  505 */     char ch = basePath.charAt(len - 1);
/*  506 */     if (isSeparator(ch)) {
/*  507 */       return normalize(basePath + fullFileNameToAdd);
/*      */     }
/*  509 */     return normalize(basePath + '/' + fullFileNameToAdd);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean directoryContains(String canonicalParent, String canonicalChild) {
/*  534 */     Objects.requireNonNull(canonicalParent, "canonicalParent");
/*      */     
/*  536 */     if (canonicalChild == null) {
/*  537 */       return false;
/*      */     }
/*      */     
/*  540 */     if (IOCase.SYSTEM.checkEquals(canonicalParent, canonicalChild)) {
/*  541 */       return false;
/*      */     }
/*      */     
/*  544 */     return IOCase.SYSTEM.checkStartsWith(canonicalChild, canonicalParent);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String separatorsToUnix(String path) {
/*  554 */     if (path == null || path.indexOf('\\') == -1) {
/*  555 */       return path;
/*      */     }
/*  557 */     return path.replace('\\', '/');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String separatorsToWindows(String path) {
/*  567 */     if (path == null || path.indexOf('/') == -1) {
/*  568 */       return path;
/*      */     }
/*  570 */     return path.replace('/', '\\');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String separatorsToSystem(String path) {
/*  580 */     if (path == null) {
/*  581 */       return null;
/*      */     }
/*  583 */     return isSystemWindows() ? separatorsToWindows(path) : separatorsToUnix(path);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getPrefixLength(String fileName) {
/*  626 */     if (fileName == null) {
/*  627 */       return -1;
/*      */     }
/*  629 */     int len = fileName.length();
/*  630 */     if (len == 0) {
/*  631 */       return 0;
/*      */     }
/*  633 */     char ch0 = fileName.charAt(0);
/*  634 */     if (ch0 == ':') {
/*  635 */       return -1;
/*      */     }
/*  637 */     if (len == 1) {
/*  638 */       if (ch0 == '~') {
/*  639 */         return 2;
/*      */       }
/*  641 */       return isSeparator(ch0) ? 1 : 0;
/*      */     } 
/*  643 */     if (ch0 == '~') {
/*  644 */       int i = fileName.indexOf('/', 1);
/*  645 */       int j = fileName.indexOf('\\', 1);
/*  646 */       if (i == -1 && j == -1) {
/*  647 */         return len + 1;
/*      */       }
/*  649 */       i = (i == -1) ? j : i;
/*  650 */       j = (j == -1) ? i : j;
/*  651 */       return Math.min(i, j) + 1;
/*      */     } 
/*  653 */     char ch1 = fileName.charAt(1);
/*  654 */     if (ch1 == ':') {
/*  655 */       ch0 = Character.toUpperCase(ch0);
/*  656 */       if (ch0 >= 'A' && ch0 <= 'Z') {
/*  657 */         if (len == 2 && !FileSystem.getCurrent().supportsDriveLetter()) {
/*  658 */           return 0;
/*      */         }
/*  660 */         if (len == 2 || !isSeparator(fileName.charAt(2))) {
/*  661 */           return 2;
/*      */         }
/*  663 */         return 3;
/*      */       } 
/*  665 */       if (ch0 == '/') {
/*  666 */         return 1;
/*      */       }
/*  668 */       return -1;
/*      */     } 
/*      */     
/*  671 */     if (!isSeparator(ch0) || !isSeparator(ch1)) {
/*  672 */       return isSeparator(ch0) ? 1 : 0;
/*      */     }
/*  674 */     int posUnix = fileName.indexOf('/', 2);
/*  675 */     int posWin = fileName.indexOf('\\', 2);
/*  676 */     if ((posUnix == -1 && posWin == -1) || posUnix == 2 || posWin == 2) {
/*  677 */       return -1;
/*      */     }
/*  679 */     posUnix = (posUnix == -1) ? posWin : posUnix;
/*  680 */     posWin = (posWin == -1) ? posUnix : posWin;
/*  681 */     int pos = Math.min(posUnix, posWin) + 1;
/*  682 */     String hostnamePart = fileName.substring(2, pos - 1);
/*  683 */     return isValidHostName(hostnamePart) ? pos : -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOfLastSeparator(String fileName) {
/*  699 */     if (fileName == null) {
/*  700 */       return -1;
/*      */     }
/*  702 */     int lastUnixPos = fileName.lastIndexOf('/');
/*  703 */     int lastWindowsPos = fileName.lastIndexOf('\\');
/*  704 */     return Math.max(lastUnixPos, lastWindowsPos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int indexOfExtension(String fileName) throws IllegalArgumentException {
/*  730 */     if (fileName == null) {
/*  731 */       return -1;
/*      */     }
/*  733 */     if (isSystemWindows()) {
/*      */       
/*  735 */       int offset = fileName.indexOf(':', getAdsCriticalOffset(fileName));
/*  736 */       if (offset != -1) {
/*  737 */         throw new IllegalArgumentException("NTFS ADS separator (':') in file name is forbidden.");
/*      */       }
/*      */     } 
/*  740 */     int extensionPos = fileName.lastIndexOf('.');
/*  741 */     int lastSeparator = indexOfLastSeparator(fileName);
/*  742 */     return (lastSeparator > extensionPos) ? -1 : extensionPos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPrefix(String fileName) {
/*  775 */     if (fileName == null) {
/*  776 */       return null;
/*      */     }
/*  778 */     int len = getPrefixLength(fileName);
/*  779 */     if (len < 0) {
/*  780 */       return null;
/*      */     }
/*  782 */     if (len > fileName.length()) {
/*  783 */       requireNonNullChars(fileName + '/');
/*  784 */       return fileName + '/';
/*      */     } 
/*  786 */     String path = fileName.substring(0, len);
/*  787 */     requireNonNullChars(path);
/*  788 */     return path;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPath(String fileName) {
/*  815 */     return doGetPath(fileName, 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getPathNoEndSeparator(String fileName) {
/*  843 */     return doGetPath(fileName, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String doGetPath(String fileName, int separatorAdd) {
/*  854 */     if (fileName == null) {
/*  855 */       return null;
/*      */     }
/*  857 */     int prefix = getPrefixLength(fileName);
/*  858 */     if (prefix < 0) {
/*  859 */       return null;
/*      */     }
/*  861 */     int index = indexOfLastSeparator(fileName);
/*  862 */     int endIndex = index + separatorAdd;
/*  863 */     if (prefix >= fileName.length() || index < 0 || prefix >= endIndex) {
/*  864 */       return "";
/*      */     }
/*  866 */     String path = fileName.substring(prefix, endIndex);
/*  867 */     requireNonNullChars(path);
/*  868 */     return path;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getFullPath(String fileName) {
/*  897 */     return doGetFullPath(fileName, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getFullPathNoEndSeparator(String fileName) {
/*  927 */     return doGetFullPath(fileName, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String doGetFullPath(String fileName, boolean includeSeparator) {
/*  938 */     if (fileName == null) {
/*  939 */       return null;
/*      */     }
/*  941 */     int prefix = getPrefixLength(fileName);
/*  942 */     if (prefix < 0) {
/*  943 */       return null;
/*      */     }
/*  945 */     if (prefix >= fileName.length()) {
/*  946 */       if (includeSeparator) {
/*  947 */         return getPrefix(fileName);
/*      */       }
/*  949 */       return fileName;
/*      */     } 
/*  951 */     int index = indexOfLastSeparator(fileName);
/*  952 */     if (index < 0) {
/*  953 */       return fileName.substring(0, prefix);
/*      */     }
/*  955 */     int end = index + (includeSeparator ? 1 : 0);
/*  956 */     if (end == 0) {
/*  957 */       end++;
/*      */     }
/*  959 */     return fileName.substring(0, end);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getName(String fileName) {
/*  981 */     if (fileName == null) {
/*  982 */       return null;
/*      */     }
/*  984 */     requireNonNullChars(fileName);
/*  985 */     int index = indexOfLastSeparator(fileName);
/*  986 */     return fileName.substring(index + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void requireNonNullChars(String path) {
/*  997 */     if (path.indexOf(false) >= 0) {
/*  998 */       throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getBaseName(String fileName) {
/* 1022 */     return removeExtension(getName(fileName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getExtension(String fileName) throws IllegalArgumentException {
/* 1054 */     if (fileName == null) {
/* 1055 */       return null;
/*      */     }
/* 1057 */     int index = indexOfExtension(fileName);
/* 1058 */     if (index == -1) {
/* 1059 */       return "";
/*      */     }
/* 1061 */     return fileName.substring(index + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getAdsCriticalOffset(String fileName) {
/* 1072 */     int offset1 = fileName.lastIndexOf(SYSTEM_SEPARATOR);
/* 1073 */     int offset2 = fileName.lastIndexOf(OTHER_SEPARATOR);
/* 1074 */     if (offset1 == -1) {
/* 1075 */       if (offset2 == -1) {
/* 1076 */         return 0;
/*      */       }
/* 1078 */       return offset2 + 1;
/*      */     } 
/* 1080 */     if (offset2 == -1) {
/* 1081 */       return offset1 + 1;
/*      */     }
/* 1083 */     return Math.max(offset1, offset2) + 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String removeExtension(String fileName) {
/* 1104 */     if (fileName == null) {
/* 1105 */       return null;
/*      */     }
/* 1107 */     requireNonNullChars(fileName);
/*      */     
/* 1109 */     int index = indexOfExtension(fileName);
/* 1110 */     if (index == -1) {
/* 1111 */       return fileName;
/*      */     }
/* 1113 */     return fileName.substring(0, index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean equals(String fileName1, String fileName2) {
/* 1128 */     return equals(fileName1, fileName2, false, IOCase.SENSITIVE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean equalsOnSystem(String fileName1, String fileName2) {
/* 1143 */     return equals(fileName1, fileName2, false, IOCase.SYSTEM);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean equalsNormalized(String fileName1, String fileName2) {
/* 1158 */     return equals(fileName1, fileName2, true, IOCase.SENSITIVE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean equalsNormalizedOnSystem(String fileName1, String fileName2) {
/* 1175 */     return equals(fileName1, fileName2, true, IOCase.SYSTEM);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean equals(String fileName1, String fileName2, boolean normalized, IOCase caseSensitivity) {
/* 1193 */     if (fileName1 == null || fileName2 == null) {
/* 1194 */       return (fileName1 == null && fileName2 == null);
/*      */     }
/* 1196 */     if (normalized) {
/* 1197 */       fileName1 = normalize(fileName1);
/* 1198 */       if (fileName1 == null) {
/* 1199 */         return false;
/*      */       }
/* 1201 */       fileName2 = normalize(fileName2);
/* 1202 */       if (fileName2 == null) {
/* 1203 */         return false;
/*      */       }
/*      */     } 
/* 1206 */     if (caseSensitivity == null) {
/* 1207 */       caseSensitivity = IOCase.SENSITIVE;
/*      */     }
/* 1209 */     return caseSensitivity.checkEquals(fileName1, fileName2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isExtension(String fileName, String extension) {
/* 1225 */     if (fileName == null) {
/* 1226 */       return false;
/*      */     }
/* 1228 */     requireNonNullChars(fileName);
/*      */     
/* 1230 */     if (extension == null || extension.isEmpty()) {
/* 1231 */       return (indexOfExtension(fileName) == -1);
/*      */     }
/* 1233 */     String fileExt = getExtension(fileName);
/* 1234 */     return fileExt.equals(extension);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isExtension(String fileName, String... extensions) {
/* 1250 */     if (fileName == null) {
/* 1251 */       return false;
/*      */     }
/* 1253 */     requireNonNullChars(fileName);
/*      */     
/* 1255 */     if (extensions == null || extensions.length == 0) {
/* 1256 */       return (indexOfExtension(fileName) == -1);
/*      */     }
/* 1258 */     String fileExt = getExtension(fileName);
/* 1259 */     for (String extension : extensions) {
/* 1260 */       if (fileExt.equals(extension)) {
/* 1261 */         return true;
/*      */       }
/*      */     } 
/* 1264 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isExtension(String fileName, Collection<String> extensions) {
/* 1280 */     if (fileName == null) {
/* 1281 */       return false;
/*      */     }
/* 1283 */     requireNonNullChars(fileName);
/*      */     
/* 1285 */     if (extensions == null || extensions.isEmpty()) {
/* 1286 */       return (indexOfExtension(fileName) == -1);
/*      */     }
/* 1288 */     String fileExt = getExtension(fileName);
/* 1289 */     for (String extension : extensions) {
/* 1290 */       if (fileExt.equals(extension)) {
/* 1291 */         return true;
/*      */       }
/*      */     } 
/* 1294 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean wildcardMatch(String fileName, String wildcardMatcher) {
/* 1320 */     return wildcardMatch(fileName, wildcardMatcher, IOCase.SENSITIVE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean wildcardMatchOnSystem(String fileName, String wildcardMatcher) {
/* 1346 */     return wildcardMatch(fileName, wildcardMatcher, IOCase.SYSTEM);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean wildcardMatch(String fileName, String wildcardMatcher, IOCase caseSensitivity) {
/* 1364 */     if (fileName == null && wildcardMatcher == null) {
/* 1365 */       return true;
/*      */     }
/* 1367 */     if (fileName == null || wildcardMatcher == null) {
/* 1368 */       return false;
/*      */     }
/* 1370 */     if (caseSensitivity == null) {
/* 1371 */       caseSensitivity = IOCase.SENSITIVE;
/*      */     }
/* 1373 */     String[] wcs = splitOnTokens(wildcardMatcher);
/* 1374 */     boolean anyChars = false;
/* 1375 */     int textIdx = 0;
/* 1376 */     int wcsIdx = 0;
/* 1377 */     Deque<int[]> backtrack = (Deque)new ArrayDeque<>(wcs.length);
/*      */ 
/*      */     
/*      */     do {
/* 1381 */       if (!backtrack.isEmpty()) {
/* 1382 */         int[] array = backtrack.pop();
/* 1383 */         wcsIdx = array[0];
/* 1384 */         textIdx = array[1];
/* 1385 */         anyChars = true;
/*      */       } 
/*      */ 
/*      */       
/* 1389 */       while (wcsIdx < wcs.length) {
/*      */         
/* 1391 */         if (wcs[wcsIdx].equals("?")) {
/*      */           
/* 1393 */           textIdx++;
/* 1394 */           if (textIdx > fileName.length()) {
/*      */             break;
/*      */           }
/* 1397 */           anyChars = false;
/*      */         }
/* 1399 */         else if (wcs[wcsIdx].equals("*")) {
/*      */           
/* 1401 */           anyChars = true;
/* 1402 */           if (wcsIdx == wcs.length - 1) {
/* 1403 */             textIdx = fileName.length();
/*      */           }
/*      */         }
/*      */         else {
/*      */           
/* 1408 */           if (anyChars) {
/*      */             
/* 1410 */             textIdx = caseSensitivity.checkIndexOf(fileName, textIdx, wcs[wcsIdx]);
/* 1411 */             if (textIdx == -1) {
/*      */               break;
/*      */             }
/*      */             
/* 1415 */             int repeat = caseSensitivity.checkIndexOf(fileName, textIdx + 1, wcs[wcsIdx]);
/* 1416 */             if (repeat >= 0) {
/* 1417 */               backtrack.push(new int[] { wcsIdx, repeat });
/*      */             }
/* 1419 */           } else if (!caseSensitivity.checkRegionMatches(fileName, textIdx, wcs[wcsIdx])) {
/*      */             break;
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1426 */           textIdx += wcs[wcsIdx].length();
/* 1427 */           anyChars = false;
/*      */         } 
/*      */         
/* 1430 */         wcsIdx++;
/*      */       } 
/*      */ 
/*      */       
/* 1434 */       if (wcsIdx == wcs.length && textIdx == fileName.length()) {
/* 1435 */         return true;
/*      */       }
/*      */     }
/* 1438 */     while (!backtrack.isEmpty());
/*      */     
/* 1440 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String[] splitOnTokens(String text) {
/* 1455 */     if (text.indexOf('?') == -1 && text.indexOf('*') == -1) {
/* 1456 */       return new String[] { text };
/*      */     }
/*      */     
/* 1459 */     char[] array = text.toCharArray();
/* 1460 */     ArrayList<String> list = new ArrayList<>();
/* 1461 */     StringBuilder buffer = new StringBuilder();
/* 1462 */     char prevChar = Character.MIN_VALUE;
/* 1463 */     for (char ch : array) {
/* 1464 */       if (ch == '?' || ch == '*') {
/* 1465 */         if (buffer.length() != 0) {
/* 1466 */           list.add(buffer.toString());
/* 1467 */           buffer.setLength(0);
/*      */         } 
/* 1469 */         if (ch == '?') {
/* 1470 */           list.add("?");
/* 1471 */         } else if (prevChar != '*') {
/* 1472 */           list.add("*");
/*      */         } 
/*      */       } else {
/* 1475 */         buffer.append(ch);
/*      */       } 
/* 1477 */       prevChar = ch;
/*      */     } 
/* 1479 */     if (buffer.length() != 0) {
/* 1480 */       list.add(buffer.toString());
/*      */     }
/*      */     
/* 1483 */     return list.<String>toArray(EMPTY_STRING_ARRAY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isValidHostName(String name) {
/* 1499 */     return (isIPv6Address(name) || isRFC3986HostName(name));
/*      */   }
/*      */ 
/*      */   
/* 1503 */   private static final Pattern IPV4_PATTERN = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
/*      */   
/*      */   private static final int IPV4_MAX_OCTET_VALUE = 255;
/*      */   
/*      */   private static final int IPV6_MAX_HEX_GROUPS = 8;
/*      */   
/*      */   private static final int IPV6_MAX_HEX_DIGITS_PER_GROUP = 4;
/*      */   private static final int MAX_UNSIGNED_SHORT = 65535;
/*      */   private static final int BASE_16 = 16;
/*      */   
/*      */   private static boolean isIPv4Address(String name) {
/* 1514 */     Matcher m = IPV4_PATTERN.matcher(name);
/* 1515 */     if (!m.matches() || m.groupCount() != 4) {
/* 1516 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1520 */     for (int i = 1; i <= 4; i++) {
/* 1521 */       String ipSegment = m.group(i);
/* 1522 */       int iIpSegment = Integer.parseInt(ipSegment);
/* 1523 */       if (iIpSegment > 255) {
/* 1524 */         return false;
/*      */       }
/*      */       
/* 1527 */       if (ipSegment.length() > 1 && ipSegment.startsWith("0")) {
/* 1528 */         return false;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1533 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isIPv6Address(String inet6Address) {
/* 1549 */     boolean containsCompressedZeroes = inet6Address.contains("::");
/* 1550 */     if (containsCompressedZeroes && inet6Address.indexOf("::") != inet6Address.lastIndexOf("::")) {
/* 1551 */       return false;
/*      */     }
/* 1553 */     if ((inet6Address.startsWith(":") && !inet6Address.startsWith("::")) || (inet6Address
/* 1554 */       .endsWith(":") && !inet6Address.endsWith("::"))) {
/* 1555 */       return false;
/*      */     }
/* 1557 */     String[] octets = inet6Address.split(":");
/* 1558 */     if (containsCompressedZeroes) {
/* 1559 */       List<String> octetList = new ArrayList<>(Arrays.asList(octets));
/* 1560 */       if (inet6Address.endsWith("::")) {
/*      */         
/* 1562 */         octetList.add("");
/* 1563 */       } else if (inet6Address.startsWith("::") && !octetList.isEmpty()) {
/* 1564 */         octetList.remove(0);
/*      */       } 
/* 1566 */       octets = octetList.<String>toArray(EMPTY_STRING_ARRAY);
/*      */     } 
/* 1568 */     if (octets.length > 8) {
/* 1569 */       return false;
/*      */     }
/* 1571 */     int validOctets = 0;
/* 1572 */     int emptyOctets = 0;
/* 1573 */     int index = 0; while (true) { if (index < octets.length)
/* 1574 */       { String octet = octets[index];
/* 1575 */         if (octet.isEmpty())
/* 1576 */         { emptyOctets++;
/* 1577 */           if (emptyOctets > 1) {
/* 1578 */             return false;
/*      */           } }
/*      */         else
/* 1581 */         { emptyOctets = 0;
/*      */           
/* 1583 */           if (index == octets.length - 1 && octet.contains("."))
/* 1584 */           { if (!isIPv4Address(octet)) {
/* 1585 */               return false;
/*      */             }
/* 1587 */             validOctets += 2; }
/*      */           else
/*      */           { int octetInt;
/* 1590 */             if (octet.length() > 4) {
/* 1591 */               return false;
/*      */             }
/*      */             
/*      */             try {
/* 1595 */               octetInt = Integer.parseInt(octet, 16);
/* 1596 */             } catch (NumberFormatException e) {
/* 1597 */               return false;
/*      */             } 
/* 1599 */             if (octetInt < 0 || octetInt > 65535) {
/* 1600 */               return false;
/*      */             }
/*      */             
/* 1603 */             validOctets++; }  index++; }  } else { break; }  validOctets++; }
/*      */     
/* 1605 */     return (validOctets <= 8 && (validOctets >= 8 || containsCompressedZeroes));
/*      */   }
/*      */   
/* 1608 */   private static final Pattern REG_NAME_PART_PATTERN = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-]*$");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isRFC3986HostName(String name) {
/* 1619 */     String[] parts = name.split("\\.", -1);
/* 1620 */     for (int i = 0; i < parts.length; i++) {
/* 1621 */       if (parts[i].isEmpty())
/*      */       {
/* 1623 */         return (i == parts.length - 1);
/*      */       }
/* 1625 */       if (!REG_NAME_PART_PATTERN.matcher(parts[i]).matches()) {
/* 1626 */         return false;
/*      */       }
/*      */     } 
/* 1629 */     return true;
/*      */   }
/*      */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\FilenameUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */