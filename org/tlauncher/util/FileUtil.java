/*     */ package org.tlauncher.util;
/*     */ 
/*     */ import com.github.junrar.Archive;
/*     */ import com.github.junrar.extract.ExtractArchive;
/*     */ import com.github.junrar.rarfile.FileHeader;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URL;
/*     */ import java.net.URLDecoder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.security.DigestInputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
/*     */ import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.io.filefilter.TrueFileFilter;
/*     */ import org.tlauncher.exceptions.ParseModPackException;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.MapDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModDTO;
/*     */ import org.tlauncher.modpack.domain.client.ResourcePackDTO;
/*     */ import org.tlauncher.modpack.domain.client.ShaderpackDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.version.MapMetadataDTO;
/*     */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileUtil
/*     */ {
/*     */   public static final String DEFAULT_CHARSET = "UTF-8";
/*     */   public static final String NAME_ARCHIVE = "logOfFiles.zip";
/*  71 */   public static final Long SIZE_100 = Long.valueOf(104857600L);
/*  72 */   public static final Long SIZE_200 = Long.valueOf(209715200L);
/*  73 */   public static final Long SIZE_300 = Long.valueOf(314572800L);
/*  74 */   public static Set<PosixFilePermission> PERMISSIONS = new HashSet<PosixFilePermission>()
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
/*     */   public static Charset getCharset() {
/*     */     try {
/*  91 */       return Charset.forName("UTF-8");
/*  92 */     } catch (Exception e) {
/*  93 */       e.printStackTrace();
/*  94 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void writeFile(File file, String text) throws IOException {
/*  99 */     createFile(file);
/*     */     
/* 101 */     BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
/* 102 */     OutputStreamWriter ow = new OutputStreamWriter(os, "UTF-8");
/*     */     
/* 104 */     ow.write(text);
/* 105 */     ow.close();
/* 106 */     os.close();
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
/*     */   public static void writeZipEntry(ZipOutputStream zip, File fileRead) throws IOException {
/* 118 */     try (FileInputStream in = new FileInputStream(fileRead)) {
/* 119 */       byte[] array = new byte[8192];
/* 120 */       while (in.read(array) != -1)
/* 121 */         zip.write(array); 
/* 122 */     } catch (IOException e) {
/* 123 */       throw new IOException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String readStream(InputStream is, String charset) throws IOException {
/* 128 */     try (InputStreamReader reader = new InputStreamReader(new BufferedInputStream(is), charset)) {
/* 129 */       StringBuilder b = new StringBuilder();
/*     */       
/* 131 */       while (reader.ready()) {
/* 132 */         b.append((char)reader.read());
/*     */       }
/* 134 */       return b.toString();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String readStream(InputStream is) throws IOException {
/* 139 */     return readStream(is, Charsets.UTF_8.displayName());
/*     */   }
/*     */   
/*     */   public static String getTextResource(URL url, String charset) throws IOException {
/* 143 */     return readStream(url.openStream(), charset);
/*     */   }
/*     */   
/*     */   public static String readFile(File file, String charset) throws IOException {
/* 147 */     return readStream(new FileInputStream(file), charset);
/*     */   }
/*     */   
/*     */   public static String readFile(File file) throws IOException {
/* 151 */     return readFile(file, "UTF-8");
/*     */   }
/*     */   
/*     */   public static String getFilename(String path) {
/* 155 */     String[] folders = path.split("/");
/* 156 */     int size = folders.length;
/* 157 */     if (size == 0)
/* 158 */       return ""; 
/* 159 */     return folders[size - 1];
/*     */   }
/*     */   
/*     */   public static String getDigest(File file, String algorithm, int hashLength) {
/* 163 */     DigestInputStream stream = null;
/*     */     
/*     */     try {
/*     */       int read;
/* 167 */       stream = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance(algorithm));
/* 168 */       byte[] buffer = new byte[65536];
/*     */       
/*     */       do {
/* 171 */         read = stream.read(buffer);
/* 172 */       } while (read > 0);
/* 173 */     } catch (Exception ignored) {
/* 174 */       return null;
/*     */     } finally {
/* 176 */       close(stream);
/*     */     } 
/*     */     
/* 179 */     return String.format("%1$0" + hashLength + "x", new Object[] { new BigInteger(1, stream.getMessageDigest().digest()) });
/*     */   }
/*     */   
/*     */   private static byte[] createChecksum(File file, String algorithm) {
/* 183 */     InputStream fis = null;
/*     */     try {
/* 185 */       fis = new BufferedInputStream(new FileInputStream(file));
/*     */       
/* 187 */       byte[] buffer = new byte[8192];
/* 188 */       MessageDigest complete = MessageDigest.getInstance(algorithm);
/*     */ 
/*     */       
/*     */       while (true)
/* 192 */       { int numRead = fis.read(buffer);
/* 193 */         if (numRead > 0) {
/* 194 */           complete.update(buffer, 0, numRead);
/*     */         }
/* 196 */         if (numRead == -1)
/* 197 */           return complete.digest();  } 
/* 198 */     } catch (Exception e) {
/* 199 */       return null;
/*     */     } finally {
/* 201 */       close(fis);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getChecksum(File file, String algorithm) {
/* 206 */     if (file == null)
/* 207 */       return null; 
/* 208 */     if (!file.exists()) {
/* 209 */       return null;
/*     */     }
/* 211 */     byte[] b = createChecksum(file, algorithm);
/* 212 */     if (b == null) {
/* 213 */       return null;
/*     */     }
/* 215 */     StringBuilder result = new StringBuilder();
/* 216 */     for (byte cb : b)
/* 217 */       result.append(Integer.toString((cb & 0xFF) + 256, 16).substring(1)); 
/* 218 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static String getChecksum(File file) {
/* 222 */     return getChecksum(file, "SHA-1");
/*     */   }
/*     */   
/*     */   private static void close(Closeable a) {
/*     */     try {
/* 227 */       a.close();
/* 228 */     } catch (Exception e) {
/* 229 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static File getRunningJar() {
/*     */     try {
/* 235 */       return new File(
/* 236 */           URLDecoder.decode(FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8"));
/* 237 */     } catch (UnsupportedEncodingException e) {
/* 238 */       throw new RuntimeException("Cannot get running file!", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void copyFile(File source, File dest, boolean replace) throws IOException {
/* 243 */     if (dest.isFile()) {
/*     */       
/* 245 */       if (!replace) {
/*     */         return;
/*     */       }
/*     */     } else {
/* 249 */       createFile(dest);
/*     */     } 
/*     */     
/* 252 */     try(InputStream is = new BufferedInputStream(new FileInputStream(source)); 
/* 253 */         OutputStream os = new BufferedOutputStream(new FileOutputStream(dest))) {
/* 254 */       byte[] buffer = new byte[1024];
/*     */       int length;
/* 256 */       while ((length = is.read(buffer)) > 0) {
/* 257 */         os.write(buffer, 0, length);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void deleteFile(File file) {
/* 263 */     boolean onExit = !file.delete();
/*     */     
/* 265 */     if (onExit) {
/* 266 */       file.deleteOnExit();
/*     */       
/*     */       return;
/*     */     } 
/* 270 */     File parent = file.getParentFile();
/* 271 */     if (parent == null) {
/*     */       return;
/*     */     }
/* 274 */     File[] list = parent.listFiles();
/* 275 */     if (list == null || list.length > 0) {
/*     */       return;
/*     */     }
/* 278 */     deleteFile(parent);
/*     */   }
/*     */   
/*     */   public static void deleteDirectory(File dir) {
/* 282 */     if (!dir.isDirectory())
/* 283 */       throw new IllegalArgumentException("Specified path is not a directory: " + dir.getAbsolutePath()); 
/* 284 */     File[] files = dir.listFiles();
/* 285 */     if (Objects.isNull(files))
/*     */       return; 
/* 287 */     for (File file : files) {
/* 288 */       if (file.isDirectory()) {
/* 289 */         deleteDirectory(file);
/*     */       } else {
/* 291 */         deleteFile(file);
/*     */       } 
/* 293 */     }  deleteFile(dir);
/*     */   }
/*     */   
/*     */   public static File makeTemp(File file) throws IOException {
/* 297 */     createFile(file);
/* 298 */     file.deleteOnExit();
/*     */     
/* 300 */     return file;
/*     */   }
/*     */   
/*     */   public static boolean createFolder(File dir) throws IOException {
/* 304 */     if (dir == null) {
/* 305 */       throw new NullPointerException();
/*     */     }
/* 307 */     if (dir.isDirectory()) {
/* 308 */       return false;
/*     */     }
/* 310 */     if (!dir.mkdirs()) {
/* 311 */       throw new IOException("Cannot createScrollWrapper folders: " + dir.getAbsolutePath());
/*     */     }
/* 313 */     if (!dir.canWrite()) {
/* 314 */       throw new IOException("Created directory is not accessible: " + dir.getAbsolutePath());
/*     */     }
/* 316 */     return true;
/*     */   }
/*     */   
/*     */   public static void createFile(File file) throws IOException {
/* 320 */     if (file.isFile()) {
/*     */       return;
/*     */     }
/* 323 */     if (file.getParentFile() != null)
/*     */     {
/* 325 */       file.getParentFile().mkdirs();
/*     */     }
/* 327 */     if (!file.createNewFile())
/* 328 */       throw new IOException("Cannot createScrollWrapper file, or it was created during runtime: " + file
/* 329 */           .getAbsolutePath()); 
/*     */   }
/*     */   
/*     */   public static void unTarGz(File zip, File folder, boolean replace, boolean deleteEmptyFile) throws IOException {
/* 333 */     createFolder(folder);
/*     */     
/* 335 */     try (TarArchiveInputStream zis = new TarArchiveInputStream((InputStream)new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(zip))))) {
/*     */       TarArchiveEntry ze;
/*     */ 
/*     */       
/* 339 */       while ((ze = (TarArchiveEntry)zis.getNextEntry()) != null) {
/* 340 */         String fileName = ze.getName();
/* 341 */         if (ze.isDirectory()) {
/*     */           continue;
/*     */         }
/* 344 */         unZipAndTarGz(fileName, folder, replace, (InputStream)zis, deleteEmptyFile);
/*     */       } 
/* 346 */       zis.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void unZip(File zip, File folder, boolean replace, boolean deleteEmptyFile) throws IOException {
/* 351 */     createFolder(folder);
/*     */     
/* 353 */     try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)), StandardCharsets.UTF_8)) {
/*     */       ZipEntry ze;
/*     */ 
/*     */       
/* 357 */       while ((ze = zis.getNextEntry()) != null) {
/* 358 */         String fileName = ze.getName();
/* 359 */         if (ze.isDirectory()) {
/*     */           continue;
/*     */         }
/* 362 */         unZipAndTarGz(fileName, folder, replace, zis, deleteEmptyFile);
/*     */       } 
/* 364 */       zis.closeEntry();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void unZipAndTarGz(String fileName, File folder, boolean replace, InputStream zis, boolean deleteEmptyFile) throws IOException {
/* 370 */     byte[] buffer = new byte[1024];
/* 371 */     File newFile = new File(folder, fileName);
/* 372 */     if (!replace && newFile.isFile()) {
/*     */       return;
/*     */     }
/* 375 */     createFile(newFile);
/*     */     
/* 377 */     OutputStream fos = new BufferedOutputStream(new FileOutputStream(newFile));
/*     */     
/* 379 */     int count = 0; int len;
/* 380 */     while ((len = zis.read(buffer)) > 0) {
/* 381 */       count += len;
/* 382 */       fos.write(buffer, 0, len);
/*     */     } 
/*     */     
/* 385 */     fos.close();
/*     */ 
/*     */ 
/*     */     
/* 389 */     if (deleteEmptyFile && count == 0) {
/* 390 */       Files.delete(newFile.toPath());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void unZip(File zip, File folder, boolean replace) throws IOException {
/* 395 */     unZip(zip, folder, replace, true);
/*     */   }
/*     */   
/*     */   public static String getResource(URL resource, String charset) throws IOException {
/* 399 */     InputStream is = new BufferedInputStream(resource.openStream());
/*     */     
/* 401 */     InputStreamReader reader = new InputStreamReader(is, charset);
/*     */     
/* 403 */     StringBuilder b = new StringBuilder();
/* 404 */     while (reader.ready()) {
/* 405 */       b.append((char)reader.read());
/*     */     }
/* 407 */     reader.close();
/*     */     
/* 409 */     return b.toString();
/*     */   }
/*     */   
/*     */   public static String getResource(URL resource) throws IOException {
/* 413 */     return getResource(resource, "UTF-8");
/*     */   }
/*     */   
/*     */   public static String getFolder(URL url, String separator) {
/* 417 */     String[] folders = url.toString().split(separator);
/* 418 */     StringBuilder s = new StringBuilder();
/*     */     
/* 420 */     for (int i = 0; i < folders.length - 1; i++) {
/* 421 */       s.append(folders[i]).append(separator);
/*     */     }
/* 423 */     return s.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getExtension(File f) {
/* 431 */     if (!f.isFile() && f.isDirectory()) {
/* 432 */       return null;
/*     */     }
/* 434 */     String ext = "";
/* 435 */     String s = f.getName();
/* 436 */     int i = s.lastIndexOf('.');
/*     */     
/* 438 */     if (i > 0 && i < s.length() - 1) {
/* 439 */       ext = s.substring(i + 1).toLowerCase();
/*     */     }
/*     */     
/* 442 */     return ext;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean checkFreeSpace(File file, long size) {
/*     */     try {
/* 448 */       FileStore store = Files.getFileStore(file.toPath().getRoot());
/* 449 */       long res = store.getUsableSpace();
/* 450 */       return (res > size);
/* 451 */     } catch (IOException iOException) {
/*     */       
/* 453 */       return true;
/*     */     } 
/*     */   }
/*     */   public static InputStream getResourceAppStream(String name) {
/* 457 */     return FileUtil.class.getResourceAsStream(name);
/*     */   }
/*     */   
/*     */   public static Path getRelative(String path) {
/* 461 */     return Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), new String[] { path });
/*     */   }
/*     */   
/*     */   public static Path getRelativeConfig(String path) {
/* 465 */     return getRelative(TLauncher.getInnerSettings().get(path));
/*     */   }
/*     */   
/*     */   public static File getRelativeConfigFile(String path) {
/* 469 */     return getRelative(TLauncher.getInnerSettings().get(path)).toFile();
/*     */   }
/*     */   
/*     */   public static List<String> topFolders(Archive rar) {
/* 473 */     FileHeader fh = rar.nextFileHeader();
/*     */     
/* 475 */     Set<String> list = new HashSet<>();
/* 476 */     while (fh != null) {
/* 477 */       String line = Paths.get(fh.getFileNameW(), new String[0]).toString();
/* 478 */       if (line.indexOf(File.separator) > 0) {
/* 479 */         list.add(line.substring(0, line.indexOf(File.separator)));
/*     */       }
/* 481 */       fh = rar.nextFileHeader();
/*     */     } 
/* 483 */     return new ArrayList<>(list);
/*     */   }
/*     */   
/*     */   public static List<String> topFolders(ZipFile zipFile) {
/* 487 */     Set<String> list = new HashSet<>();
/* 488 */     Enumeration<? extends ZipEntry> entries = zipFile.entries();
/* 489 */     while (entries.hasMoreElements()) {
/* 490 */       ZipEntry entry = entries.nextElement();
/* 491 */       String name = entry.getName();
/* 492 */       int index = name.indexOf("/");
/* 493 */       if (index > 0) {
/* 494 */         list.add(name.substring(0, index));
/*     */       }
/*     */     } 
/* 497 */     IOUtils.closeQuietly(zipFile);
/* 498 */     return new ArrayList<>(list);
/*     */   }
/*     */   
/*     */   public static MetadataDTO createMetadata(File file, File rootFolder, Class<? extends GameEntityDTO> c) {
/*     */     MetadataDTO metadata;
/* 503 */     if (c == MapDTO.class) {
/* 504 */       MapMetadataDTO map = new MapMetadataDTO();
/* 505 */       map.setFolders(Lists.newArrayList((Object[])new String[] { file.getName() }));
/* 506 */       MapMetadataDTO mapMetadataDTO1 = map;
/*     */     } else {
/* 508 */       metadata = new MetadataDTO();
/* 509 */       metadata.setSha1(getChecksum(file, "SHA-1"));
/* 510 */       metadata.setSize(file.length());
/*     */     } 
/* 512 */     metadata.setPath(rootFolder.toPath().relativize(file.toPath()).toString().replace("\\", "/"));
/* 513 */     metadata.setUrl(rootFolder.toPath().relativize(file.toPath()).toString().replace("\\", "/"));
/* 514 */     return metadata;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void zipFiles(List<File> files, Path root, File result) throws IOException {
/* 522 */     FileOutputStream fos = new FileOutputStream(result);
/* 523 */     ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(fos), StandardCharsets.UTF_8);
/* 524 */     for (File input : files) {
/* 525 */       FileInputStream fis = new FileInputStream(input);
/* 526 */       ZipEntry ze = new ZipEntry(root.relativize(input.toPath()).toString().replaceAll("\\\\", "/"));
/* 527 */       zipOut.putNextEntry(ze);
/* 528 */       byte[] tmp = new byte[4096];
/*     */       int size;
/* 530 */       while ((size = fis.read(tmp)) != -1) {
/* 531 */         zipOut.write(tmp, 0, size);
/*     */       }
/* 533 */       zipOut.flush();
/* 534 */       fis.close();
/*     */     } 
/* 536 */     zipOut.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void backupModpacks(Map<String, String> map, List<File> files, Path root, File result, List<String> modpacks) throws IOException {
/* 546 */     FileOutputStream fos = new FileOutputStream(result);
/* 547 */     ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(fos), StandardCharsets.UTF_8);
/* 548 */     for (File input : files) {
/* 549 */       InputStream fis; if (map.containsKey(input.getName())) {
/* 550 */         fis = new ByteArrayInputStream(((String)map.get(input.getName())).getBytes(StandardCharsets.UTF_8));
/*     */       } else {
/* 552 */         fis = new FileInputStream(input);
/*     */       } 
/* 554 */       ZipEntry ze = new ZipEntry(root.relativize(input.toPath()).toString().replaceAll("\\\\", "/"));
/* 555 */       U.log(new Object[] { ze.getName() });
/*     */       
/* 557 */       zipOut.putNextEntry(ze);
/* 558 */       byte[] tmp = new byte[4096];
/*     */       int size;
/* 560 */       while ((size = fis.read(tmp)) != -1) {
/* 561 */         zipOut.write(tmp, 0, size);
/*     */       }
/* 563 */       zipOut.flush();
/* 564 */       fis.close();
/*     */     } 
/* 566 */     zipOut.close();
/*     */   }
/*     */   
/*     */   public static void unzipUniversal(File file, File folder) throws IOException, ParseModPackException {
/*     */     ExtractArchive extractArchive;
/* 571 */     String ext = FilenameUtils.getExtension(file.getCanonicalPath());
/* 572 */     switch (ext) {
/*     */       case "rar":
/* 574 */         extractArchive = new ExtractArchive();
/* 575 */         extractArchive.extractArchive(file, folder);
/*     */         return;
/*     */       case "zip":
/* 578 */         unZip(file, folder, true);
/*     */         return;
/*     */     } 
/* 581 */     throw new ParseModPackException("problem with format of the modpack");
/*     */   }
/*     */ 
/*     */   
/*     */   public static void zipFolder(File srcFolder, File destZipFile) throws Exception {
/* 586 */     List<File> files = (List<File>)FileUtils.listFiles(srcFolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
/*     */ 
/*     */     
/* 589 */     zipFiles(files, srcFolder.toPath().getParent(), destZipFile);
/*     */   }
/*     */   
/*     */   public enum GameEntityFolder
/*     */   {
/* 594 */     MODS, RESOURCEPACKS, MAPS, SAVES, RESOURCES, SHADERPACKS;
/*     */ 
/*     */     
/*     */     public String toString() {
/* 598 */       return super.toString().toLowerCase();
/*     */     }
/*     */     
/*     */     public static String getPath(GameType type) {
/* 602 */       switch (type) {
/*     */         case MOD:
/* 604 */           return MODS.toString().toLowerCase();
/*     */         case MAP:
/* 606 */           return SAVES.toString().toLowerCase();
/*     */         case RESOURCEPACK:
/* 608 */           return RESOURCEPACKS.toString().toLowerCase();
/*     */         case SHADERPACK:
/* 610 */           return SHADERPACKS.toString().toLowerCase();
/*     */       } 
/* 612 */       return "";
/*     */     }
/*     */ 
/*     */     
/*     */     public static String getPath(Class<? extends GameEntityDTO> type, boolean folderSeparate) {
/* 617 */       String path = "";
/* 618 */       if (type == ModDTO.class) {
/* 619 */         path = getPath(GameType.MOD);
/* 620 */       } else if (type == MapDTO.class) {
/* 621 */         path = getPath(GameType.MAP);
/* 622 */       } else if (type == ResourcePackDTO.class) {
/* 623 */         path = getPath(GameType.RESOURCEPACK);
/* 624 */       } else if (type == ShaderpackDTO.class) {
/* 625 */         path = getPath(GameType.SHADERPACK);
/* 626 */       }  if (folderSeparate) {
/* 627 */         return path + "/";
/*     */       }
/* 629 */       return path;
/*     */     }
/*     */   }
/*     */   
/*     */   public static String readZippedFile(File file, String name) throws IOException {
/* 634 */     try (ZipFile f = new ZipFile(file)) {
/* 635 */       Enumeration<? extends ZipEntry> en = f.entries();
/* 636 */       while (en.hasMoreElements()) {
/* 637 */         ZipEntry entry = en.nextElement();
/* 638 */         if (entry.getName().endsWith(name)) {
/* 639 */           return readStream(f.getInputStream(entry));
/*     */         }
/*     */       } 
/*     */     } 
/* 643 */     throw new FileNotFoundException(file.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\FileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */