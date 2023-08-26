/*     */ package net.minecraft.launcher.versions;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.jar.Pack200;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.launcher.versions.json.DateTypeAdapter;
/*     */ import net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory;
/*     */ import org.apache.commons.lang3.text.StrSubstitutor;
/*     */ import org.tlauncher.modpack.domain.client.version.MetadataDTO;
/*     */ import org.tlauncher.tlauncher.downloader.Downloadable;
/*     */ import org.tlauncher.tlauncher.downloader.RetryDownloadException;
/*     */ import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
/*     */ import org.tlauncher.tlauncher.repository.Repo;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tukaani.xz.XZInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Library
/*     */ {
/*     */   private static final String FORGE_LIB_SUFFIX = ".pack.xz";
/*     */   private static final StrSubstitutor SUBSTITUTOR;
/*     */   private String name;
/*     */   private List<Rule> rules;
/*     */   private Map<OS, String> natives;
/*     */   private ExtractRules extract;
/*     */   
/*     */   static {
/*  61 */     HashMap<String, String> map = new HashMap<>();
/*     */     
/*  63 */     map.put("platform", OS.CURRENT.getName());
/*  64 */     map.put("arch", OS.Arch.CURRENT.asString());
/*     */     
/*  66 */     SUBSTITUTOR = new StrSubstitutor(map);
/*     */   }
/*     */   private List<String> deleteEntries; private MetadataDTO artifact; private Map<String, MetadataDTO> classifies; private String url; private String exact_url; private String packed;
/*     */   private String checksum;
/*     */   
/*     */   private String getArtifactBaseDir() {
/*  72 */     if (this.name == null) {
/*  73 */       throw new IllegalStateException("Cannot get artifact dir of empty/blank artifact");
/*     */     }
/*     */     
/*  76 */     String[] parts = this.name.split(":");
/*  77 */     return String.format("%s/%s/%s", new Object[] { parts[0].replaceAll("\\.", "/"), parts[1], parts[2] });
/*     */   }
/*     */   
/*     */   public String getName() {
/*  81 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getPlainName() {
/*  85 */     String[] split = this.name.split(":");
/*  86 */     return split[0] + "." + split[1];
/*     */   }
/*     */   
/*     */   public List<Rule> getRules() {
/*  90 */     return (this.rules == null) ? null : Collections.<Rule>unmodifiableList(this.rules);
/*     */   }
/*     */   
/*     */   boolean appliesToCurrentEnvironment() {
/*  94 */     if (this.rules == null) {
/*  95 */       return true;
/*     */     }
/*  97 */     Rule.Action lastAction = Rule.Action.DISALLOW;
/*     */     
/*  99 */     for (Rule rule : this.rules) {
/* 100 */       Rule.Action action = rule.getAppliedAction();
/*     */       
/* 102 */       if (action != null) {
/* 103 */         lastAction = action;
/*     */       }
/*     */     } 
/* 106 */     return (lastAction == Rule.Action.ALLOW);
/*     */   }
/*     */   
/*     */   public Map<OS, String> getNatives() {
/* 110 */     return this.natives;
/*     */   }
/*     */   
/*     */   public ExtractRules getExtractRules() {
/* 114 */     return this.extract;
/*     */   }
/*     */   
/*     */   public String getChecksum() {
/* 118 */     MetadataDTO meta = defineMetadataLibrary(OS.CURRENT);
/* 119 */     if (meta != null) {
/* 120 */       return meta.getSha1();
/*     */     }
/*     */     
/* 123 */     if (this.artifact != null) {
/* 124 */       return this.artifact.getSha1();
/*     */     }
/* 126 */     return this.checksum;
/*     */   }
/*     */   
/*     */   public List<String> getDeleteEntriesList() {
/* 130 */     return this.deleteEntries;
/*     */   }
/*     */   
/*     */   public String getArtifactPath(String classifier) {
/* 134 */     if (this.name == null) {
/* 135 */       throw new IllegalStateException("Cannot get artifact path of empty/blank artifact");
/*     */     }
/*     */     
/* 138 */     return String.format("%s/%s", new Object[] { getArtifactBaseDir(), 
/* 139 */           getArtifactFilename(classifier) });
/*     */   }
/*     */   
/*     */   public String getArtifactPath() {
/* 143 */     return getArtifactPath(null);
/*     */   }
/*     */   private String getArtifactFilename(String classifier) {
/*     */     String result;
/* 147 */     if (this.name == null) {
/* 148 */       throw new IllegalStateException("Cannot get artifact filename of empty/blank artifact");
/*     */     }
/*     */     
/* 151 */     String[] parts = this.name.split(":");
/* 152 */     if (classifier == null) {
/* 153 */       result = (String)IntStream.range(1, parts.length).<CharSequence>mapToObj(i -> parts[i]).collect(Collectors.joining("-")) + ".jar";
/*     */     } else {
/*     */       
/* 156 */       result = String.format("%s-%s%s.jar", new Object[] { parts[1], parts[2], "-" + classifier });
/*     */     } 
/*     */     
/* 159 */     return SUBSTITUTOR.replace(result);
/*     */   }
/*     */   
/*     */   public enum TYPE {
/* 163 */     CLASSIFIES, ARTIFACT;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 168 */     return "Library{name='" + this.name + '\'' + ", rules=" + this.rules + ", natives=" + this.natives + ", extract=" + this.extract + ", packed='" + this.packed + "'}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Downloadable getDownloadable(Repo versionSource, File file, OS os) {
/*     */     String path;
/* 176 */     Repo repo = null;
/* 177 */     boolean isForge = "forge".equals(this.packed);
/*     */     
/* 179 */     Downloadable lib = determineNewSource(file, os);
/* 180 */     if (lib != null) {
/* 181 */       return lib;
/*     */     }
/*     */     
/* 184 */     if (this.exact_url == null) {
/* 185 */       String nativePath = (this.natives != null && appliesToCurrentEnvironment()) ? this.natives.get(os) : null;
/*     */       
/* 187 */       path = getArtifactPath(nativePath) + (isForge ? ".pack.xz" : "");
/*     */       
/* 189 */       if (this.url == null) {
/* 190 */         repo = ClientInstanceRepo.LIBRARY_REPO;
/*     */       }
/* 192 */       else if (this.url.startsWith("/")) {
/* 193 */         repo = versionSource;
/* 194 */         path = this.url.substring(1) + path;
/*     */       } else {
/*     */         
/* 197 */         path = this.url + path;
/*     */       } 
/*     */     } else {
/*     */       
/* 201 */       path = this.exact_url;
/*     */     } 
/*     */     
/* 204 */     if (isForge) {
/* 205 */       File tempFile = new File(file.getAbsolutePath() + ".pack.xz");
/* 206 */       MetadataDTO metadataDTO1 = new MetadataDTO();
/* 207 */       metadataDTO1.setUrl(path);
/* 208 */       metadataDTO1.setLocalDestination(tempFile);
/* 209 */       return new ForgeLibDownloadable(ClientInstanceRepo.EMPTY_REPO, metadataDTO1, file);
/*     */     } 
/*     */     
/* 212 */     MetadataDTO metadataDTO = new MetadataDTO();
/* 213 */     metadataDTO.setUrl(path);
/* 214 */     metadataDTO.setLocalDestination(file);
/* 215 */     return (repo == null) ? new Downloadable(ClientInstanceRepo.EMPTY_REPO, metadataDTO) : new Downloadable(repo, metadataDTO);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrl(String url) {
/* 221 */     this.url = url;
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
/*     */   private Downloadable determineNewSource(File file, OS os) {
/* 233 */     MetadataDTO meta = defineMetadataLibrary(os);
/* 234 */     if (meta != null) {
/* 235 */       meta.setLocalDestination(file);
/* 236 */       return new Downloadable(ClientInstanceRepo.EMPTY_REPO, meta);
/* 237 */     }  if (this.artifact != null) {
/* 238 */       this.artifact.setLocalDestination(file);
/* 239 */       return new Downloadable(ClientInstanceRepo.EMPTY_REPO, this.artifact);
/*     */     } 
/* 241 */     return null;
/*     */   }
/*     */   
/*     */   public static class LibrarySerializer
/*     */     implements JsonSerializer<Library>, JsonDeserializer<Library> {
/*     */     private final Gson gson;
/*     */     
/*     */     public LibrarySerializer() {
/* 249 */       GsonBuilder builder = new GsonBuilder();
/* 250 */       builder.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/* 251 */       builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/* 252 */       builder.enableComplexMapKeySerialization();
/* 253 */       this.gson = builder.create();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Library deserialize(JsonElement elem, Type type, JsonDeserializationContext context) throws JsonParseException {
/* 259 */       JsonObject object = elem.getAsJsonObject();
/* 260 */       JsonObject downloads = object.getAsJsonObject("downloads");
/* 261 */       if (downloads != null) {
/* 262 */         JsonElement artifact = downloads.get("artifact");
/* 263 */         if (artifact != null) {
/* 264 */           object.add("artifact", artifact);
/*     */         }
/* 266 */         JsonObject classifies = downloads.getAsJsonObject("classifiers");
/* 267 */         if (classifies != null) {
/* 268 */           Set<Map.Entry<String, JsonElement>> set = classifies.entrySet();
/* 269 */           JsonObject ob = new JsonObject();
/* 270 */           for (Map.Entry<String, JsonElement> el : set) {
/* 271 */             switch ((String)el.getKey()) {
/*     */               case "natives-linux":
/* 273 */                 ob.add(OS.LINUX.name().toLowerCase(), el.getValue());
/*     */                 continue;
/*     */               case "natives-windows":
/* 276 */                 ob.add(OS.WINDOWS.name().toLowerCase(), el.getValue());
/*     */                 continue;
/*     */               case "natives-macos":
/* 279 */                 ob.add(OS.OSX.name().toLowerCase(), el.getValue());
/*     */                 continue;
/*     */               case "natives-windows-32":
/* 282 */                 ob.add(OS.WINDOWS.name().toLowerCase() + "-32", el.getValue());
/*     */                 continue;
/*     */               case "natives-windows-64":
/* 285 */                 ob.add(OS.WINDOWS.name().toLowerCase() + "-64", el.getValue());
/*     */                 continue;
/*     */               case "natives-osx":
/* 288 */                 ob.add(OS.OSX.name().toLowerCase(), el.getValue());
/*     */                 continue;
/*     */               case "tests":
/* 291 */                 ob.add("tests", el.getValue());
/*     */                 continue;
/*     */               case "sources":
/* 294 */                 ob.add("sources", el.getValue());
/*     */                 continue;
/*     */               case "javadoc":
/* 297 */                 ob.add("javadoc", el.getValue());
/*     */                 continue;
/*     */               case "linux-aarch_64":
/* 300 */                 ob.add("linux-aarch_64", el.getValue());
/*     */                 continue;
/*     */               case "linux-x86_64":
/* 303 */                 ob.add("linux-x86_64", el.getValue());
/*     */                 continue;
/*     */               case "natives-macos-arm64":
/* 306 */                 ob.add("natives-macos-arm64", el.getValue());
/*     */                 continue;
/*     */             } 
/* 309 */             U.log(new Object[] { "can't find proper config for ", el });
/*     */           } 
/*     */           
/* 312 */           object.add("classifies", (JsonElement)ob);
/*     */         } 
/* 314 */         object.remove("downloads");
/*     */       } 
/* 316 */       return (Library)this.gson.fromJson(elem, Library.class);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonElement serialize(Library library, Type type, JsonSerializationContext context) {
/* 322 */       return this.gson.toJsonTree(library, type);
/*     */     }
/*     */   }
/*     */   
/*     */   private static synchronized void unpackLibrary(File library, File output, boolean retryOnOutOfMemory) throws IOException {
/*     */     XZInputStream xZInputStream;
/* 328 */     forgeLibLog(new Object[] { "Synchronized unpacking:", library });
/* 329 */     output.delete();
/* 330 */     InputStream in = null;
/* 331 */     JarOutputStream jos = null;
/*     */     try {
/* 333 */       in = new FileInputStream(library);
/* 334 */       xZInputStream = new XZInputStream(in);
/*     */       
/* 336 */       forgeLibLog(new Object[] { "Decompressing..." });
/* 337 */       byte[] decompressed = readFully((InputStream)xZInputStream);
/* 338 */       forgeLibLog(new Object[] { "Decompressed successfully" });
/*     */       
/* 340 */       String end = new String(decompressed, decompressed.length - 4, 4);
/*     */       
/* 342 */       if (!end.equals("SIGN")) {
/* 343 */         throw new RetryDownloadException("signature missing");
/*     */       }
/* 345 */       forgeLibLog(new Object[] { "Signature matches!" });
/*     */       
/* 347 */       int x = decompressed.length;
/* 348 */       int len = decompressed[x - 8] & 0xFF | (decompressed[x - 7] & 0xFF) << 8 | (decompressed[x - 6] & 0xFF) << 16 | (decompressed[x - 5] & 0xFF) << 24;
/*     */       
/* 350 */       forgeLibLog(new Object[] { "Now getting checksums..." });
/* 351 */       byte[] checksums = Arrays.copyOfRange(decompressed, decompressed.length - len - 8, decompressed.length - 8);
/*     */       
/* 353 */       FileUtil.createFile(output);
/* 354 */       FileOutputStream jarBytes = new FileOutputStream(output);
/* 355 */       jos = new JarOutputStream(jarBytes);
/*     */       
/* 357 */       forgeLibLog(new Object[] { "Now unpacking..." });
/* 358 */       Pack200.newUnpacker().unpack(new ByteArrayInputStream(decompressed), jos);
/* 359 */       forgeLibLog(new Object[] { "Unpacked successfully" });
/*     */       
/* 361 */       forgeLibLog(new Object[] { "Now trying to write checksums..." });
/* 362 */       jos.putNextEntry(new JarEntry("checksums.sha1"));
/* 363 */       jos.write(checksums);
/* 364 */       jos.closeEntry();
/*     */       
/* 366 */       forgeLibLog(new Object[] { "Now finishing..." });
/* 367 */     } catch (OutOfMemoryError oomE) {
/* 368 */       forgeLibLog(new Object[] { "Out of memory, oops", oomE });
/*     */       
/* 370 */       U.gc();
/*     */       
/* 372 */       if (retryOnOutOfMemory) {
/* 373 */         forgeLibLog(new Object[] { "Retrying..." });
/*     */         
/* 375 */         close(new Closeable[] { (Closeable)xZInputStream, jos });
/* 376 */         FileUtil.deleteFile(library);
/* 377 */         unpackLibrary(library, output, false);
/*     */         
/*     */         return;
/*     */       } 
/* 381 */       throw oomE;
/* 382 */     } catch (IOException e) {
/* 383 */       output.delete();
/* 384 */       throw e;
/*     */     } finally {
/* 386 */       close(new Closeable[] { (Closeable)xZInputStream, jos });
/* 387 */       FileUtil.deleteFile(library);
/*     */     } 
/* 389 */     forgeLibLog(new Object[] { "Done:", output });
/*     */   }
/*     */   
/*     */   private static synchronized void unpackLibrary(File library, File output) throws IOException {
/* 393 */     unpackLibrary(library, output, true);
/*     */   }
/*     */   
/*     */   private static void close(Closeable... closeables) {
/* 397 */     for (Closeable c : closeables) {
/*     */       try {
/* 399 */         c.close();
/* 400 */       } catch (Exception exception) {}
/*     */     } 
/*     */   }
/*     */   
/*     */   private static byte[] readFully(InputStream stream) throws IOException {
/*     */     int len;
/* 406 */     byte[] data = new byte[4096];
/* 407 */     ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
/*     */ 
/*     */     
/*     */     do {
/* 411 */       len = stream.read(data);
/*     */       
/* 413 */       if (len <= 0)
/* 414 */         continue;  entryBuffer.write(data, 0, len);
/*     */     }
/* 416 */     while (len != -1);
/*     */     
/* 418 */     return entryBuffer.toByteArray();
/*     */   }
/*     */   
/*     */   private static void forgeLibLog(Object... o) {
/* 422 */     U.log(new Object[] { "[ForgeLibDownloadable]", o });
/*     */   }
/*     */   
/*     */   private MetadataDTO defineMetadataLibrary(OS os) {
/* 426 */     if (this.classifies != null && this.natives != null) {
/* 427 */       if (this.classifies.get(os.name().toLowerCase()) != null)
/* 428 */         return this.classifies.get(os.name().toLowerCase()); 
/* 429 */       if (this.classifies.get(os.name().toLowerCase() + "-" + OS.Arch.CURRENT.name().replace("x", "")) != null) {
/* 430 */         return this.classifies.get(os.name().toLowerCase() + "-" + OS.Arch.CURRENT.name().replace("x", ""));
/*     */       }
/*     */     } 
/* 433 */     return null;
/*     */   }
/*     */   
/*     */   public static class ForgeLibDownloadable
/*     */     extends Downloadable {
/*     */     private final File unpacked;
/*     */     
/*     */     private ForgeLibDownloadable(Repo rep, MetadataDTO metadataDTO, File unpackedLib) {
/* 441 */       super(rep, metadataDTO);
/* 442 */       this.unpacked = unpackedLib;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onComplete() throws RetryDownloadException {
/* 447 */       super.onComplete();
/*     */       
/*     */       try {
/* 450 */         Library.unpackLibrary(getMetadataDTO().getLocalDestination(), this.unpacked);
/* 451 */       } catch (Throwable t) {
/* 452 */         throw new RetryDownloadException("cannot unpack forge library", t);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\Library.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */