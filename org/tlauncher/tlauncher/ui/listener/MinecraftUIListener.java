/*     */ package org.tlauncher.tlauncher.ui.listener;
/*     */ import com.google.common.base.Strings;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.launcher.Http;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.json.Argument;
/*     */ import net.minecraft.launcher.versions.json.ArgumentType;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.configuration.LangConfiguration;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
/*     */ import org.tlauncher.tlauncher.minecraft.crash.Crash;
/*     */ import org.tlauncher.tlauncher.minecraft.crash.CrashSignatureContainer;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.StringUtil;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ public class MinecraftUIListener implements MinecraftListener {
/*     */   public MinecraftUIListener(TLauncher tlauncher) {
/*  42 */     this.t = tlauncher;
/*     */     
/*  44 */     this.lang = this.t.getLang();
/*     */   }
/*     */ 
/*     */   
/*     */   private final TLauncher t;
/*     */   
/*     */   private final LangConfiguration lang;
/*     */   
/*     */   public void onMinecraftPrepare() {}
/*     */   
/*     */   public void onMinecraftAbort() {}
/*     */   
/*     */   public void onMinecraftLaunch() {
/*  57 */     if (!this.t.getConfiguration().getActionOnLaunch().equals(ActionOnLaunch.NOTHING)) {
/*  58 */       this.t.hide();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onMinecraftClose() {
/*  63 */     if (!this.t.getLauncher().isLaunchAssist()) {
/*     */       return;
/*     */     }
/*  66 */     this.t.show();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMinecraftCrash(Crash crash) {
/*  72 */     if (!this.t.getLauncher().isLaunchAssist())
/*  73 */       this.t.show(); 
/*  74 */     Configuration c = this.t.getConfiguration();
/*  75 */     String p = "crash.", title = Localizable.get(p + "title");
/*  76 */     String report = crash.getFile();
/*  77 */     String skipAccountProperty = "skip.account.property";
/*  78 */     if (!crash.isRecognized()) {
/*  79 */       showPossibleSolutions();
/*     */     } else {
/*  81 */       for (CrashSignatureContainer.CrashSignature sign : crash.getSignatures()) {
/*  82 */         String path = sign.getPath(), message = p + path;
/*  83 */         if ("not proper gpu1".equalsIgnoreCase(sign.getName()) && 
/*  84 */           !c.getBoolean("fixed.gpu.jre.error") && OS.is(new OS[] { OS.WINDOWS })) {
/*  85 */           c.set("fixed.gpu.jre.error", Boolean.valueOf(true));
/*  86 */           U.log(new Object[] { "set new jre" });
/*  87 */         } else if ("Bad video drivers".equalsIgnoreCase(sign.getName()) && !c.getBoolean("fixed.gpu.jre.error") && 
/*  88 */           OS.is(new OS[] { OS.WINDOWS }) && OS.VERSION.contains("10") && (
/*  89 */           StringUtils.contains(this.t.getConfiguration().get("gpu.info.full"), "Intel(R) HD Graphics Family") || 
/*  90 */           StringUtils.contains(this.t.getConfiguration().get("gpu.info.full"), "Pentium"))) {
/*  91 */           c.set("fixed.gpu.jre.error", Boolean.valueOf(true));
/*  92 */           Alert.showLocMessageWithoutTitle("crash.restart");
/*  93 */           U.log(new Object[] { "set new jre" });
/*  94 */         } else if (c.getBoolean("fixed.gpu.jre.error") && "Bad video drivers"
/*  95 */           .equalsIgnoreCase(sign.getName())) {
/*  96 */           Alert.showLocMessageWithoutTitle("crash.opengl.windows10.error");
/*     */         } 
/*  98 */         if (sign.getName().equalsIgnoreCase("thread creation problem")) {
/*  99 */           if (!replaceJVMParam()) {
/* 100 */             showPossibleSolutions();
/*     */             return;
/*     */           } 
/*     */         } else {
/* 104 */           if (sign.getName().equalsIgnoreCase("Not reserve ram") && this.t
/* 105 */             .getConfiguration().getInteger("minecraft.memory.ram2") == OS.Arch.PREFERRED_MEMORY) {
/* 106 */             showProperMemoryMessage(); continue;
/*     */           } 
/* 108 */           if (sign.getName().equalsIgnoreCase("Not reserve ram") || sign
/* 109 */             .getName().equalsIgnoreCase("jetty ram problem")) {
/* 110 */             if (Alert.showLocQuestion(title, message))
/* 111 */               this.t.getConfiguration().set("minecraft.memory.ram2", Integer.valueOf(OS.Arch.PREFERRED_MEMORY), true);  return;
/*     */           } 
/* 113 */           if (sign.getName().equalsIgnoreCase("gson lenient") && 
/* 114 */             !this.t.getConfiguration().getBoolean(skipAccountProperty))
/* 115 */           { this.t.getConfiguration().set(skipAccountProperty, Boolean.valueOf(true)); }
/* 116 */           else { if (sign.getName().equalsIgnoreCase("Bad video drivers")) {
/* 117 */               String gpuLink = "", gpuName = this.t.getConfiguration().get("gpu.info");
/* 118 */               String cpuLink = "", cpuName = this.t.getConfiguration().get("process.info");
/* 119 */               int i = 2;
/* 120 */               if (!Strings.isNullOrEmpty(gpuName)) {
/* 121 */                 gpuName = String.join(" ", new CharSequence[] { gpuName, OS.NAME, OS.VERSION, Localizable.get("crash.opengl.download.driver") });
/* 122 */                 gpuLink = Http.get("https://www.google.com/search", "q", gpuName);
/* 123 */                 gpuLink = String.format("<br>%s)%s <a href='%s'>%s</a>", new Object[] { Integer.valueOf(i++), Localizable.get("crash.opengl.install.gpu"), gpuLink, Localizable.get("click.me") });
/*     */               } 
/* 125 */               if (!Strings.isNullOrEmpty(cpuName)) {
/* 126 */                 cpuName = String.join(" ", new CharSequence[] { cpuName, OS.NAME, OS.VERSION, Localizable.get("crash.opengl.download.driver") });
/* 127 */                 cpuLink = Http.get("https://www.google.com/search", "q", cpuName);
/* 128 */                 cpuLink = String.format("<br>%s)%s <a href='%s'>%s</a>", new Object[] { Integer.valueOf(i++), Localizable.get("crash.opengl.install.cpu"), cpuLink, Localizable.get("click.me") });
/*     */               } 
/* 130 */               message = String.format(Localizable.get(message), new Object[] { gpuLink, cpuLink, Localizable.get("crash.opengl.help") });
/* 131 */               Alert.showErrorHtml("", message); continue;
/*     */             } 
/* 133 */             if ("vbo fix".equals(sign.getName()) && !fixProblem()) {
/*     */               
/* 135 */               showPossibleSolutions(); return;
/*     */             } 
/* 137 */             if (("OutOfMemory error".equals(sign.getName()) || "PermGen error"
/* 138 */               .equals(sign.getName())) && "system.32x.not.proper"
/* 139 */               .equals(c.get("memory.problem.message")))
/* 140 */             { message = Localizable.get(message) + Localizable.get("crash.outofmemory.32bit"); }
/* 141 */             else { if ("problem g1gc".equals(sign.getName())) {
/* 142 */                 Alert.showLocMessageWithoutTitle(message);
/* 143 */                 Alert.showLocMessageWithoutTitle("crash.swap.file.increase");
/* 144 */                 this.t.getConfiguration().set("minecraft.memory.ram2", Integer.valueOf(OS.Arch.PREFERRED_MEMORY), true); return;
/*     */               } 
/* 146 */               if ("failed create event loop".equals(sign.getName()))
/* 147 */               { long l = c.getLong("reset.net");
/* 148 */                 if (l == 0L || (new Date(l)).before(new Date())) {
/* 149 */                   executeFixed();
/*     */                   return;
/*     */                 }  }
/* 152 */               else if ("forge config error".equalsIgnoreCase(sign.getName()))
/* 153 */               { Path configDir = Paths.get(this.t.getLauncher().getRunningMinecraftDir().getPath(), new String[] { "config" });
/* 154 */                 if (Files.exists(configDir, new java.nio.file.LinkOption[0]) && Files.isDirectory(configDir, new java.nio.file.LinkOption[0]))
/* 155 */                   FileUtil.deleteDirectory(configDir.toFile());  }  }
/*     */              }
/*     */         
/* 158 */         }  Alert.showLocMessage(title, message, report);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void executeFixed() {
/* 164 */     Alert.showLocMessage("", "crash.switch.antivirus.system.auto", null);
/* 165 */     String path = "http://repo.tlauncher.org/update/downloads/libraries/org/tlauncher/updater/TLauncherUpdater.exe";
/*     */     try {
/* 167 */       Path runner = Files.createTempFile("TLauncherUpdater", ".exe", (FileAttribute<?>[])new FileAttribute[0]);
/* 168 */       FileUtils.copyURLToFile(new URL(path), runner.toFile(), 30000, 30000);
/* 169 */       Process p = Runtime.getRuntime().exec(new String[] { "cmd", "/c", runner.toString(), "\\\"netsh winsock reset\\\"" });
/* 170 */       int code = p.waitFor();
/* 171 */       if (code == 0)
/* 172 */         this.t.getConfiguration().set("reset.net", Long.valueOf(DateUtils.addDays(new Date(), 7).getTime()), true); 
/* 173 */       U.log(new Object[] { "finished fixed with code " + code });
/* 174 */     } catch (IOException|InterruptedException e) {
/* 175 */       U.log(new Object[] { e });
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean fixProblem() {
/* 180 */     boolean canFixed = false;
/* 181 */     File options = new File(this.t.getLauncher().getRunningMinecraftDir(), LanguageAssistance.OPTIONS);
/* 182 */     if (options.exists()) {
/*     */       try {
/* 184 */         List<String> lines = Files.readAllLines(options.toPath(), StandardCharsets.UTF_8);
/* 185 */         int indexVsync = -1, indexVbo = -1;
/* 186 */         for (int i = 0; i < lines.size(); i++) {
/* 187 */           if (((String)lines.get(i)).contains("enableVsync:")) {
/* 188 */             indexVsync = i;
/*     */           }
/* 190 */           if (((String)lines.get(i)).contains("useVbo:")) {
/* 191 */             indexVbo = i;
/*     */           }
/*     */         } 
/* 194 */         String vbo = "useVbo:false";
/* 195 */         String vsync = "enableVsync:true";
/* 196 */         if (indexVbo == -1) {
/* 197 */           lines.add(vbo);
/* 198 */           canFixed = true;
/* 199 */         } else if (((String)lines.get(indexVbo)).endsWith("true")) {
/* 200 */           lines.remove(indexVbo);
/* 201 */           lines.add(indexVbo, vbo);
/* 202 */           canFixed = true;
/*     */         } 
/* 204 */         if (indexVsync == -1) {
/* 205 */           lines.add(vsync);
/* 206 */           canFixed = true;
/* 207 */         } else if (((String)lines.get(indexVsync)).endsWith("false")) {
/* 208 */           lines.remove(indexVsync);
/* 209 */           lines.add(indexVsync, vsync);
/* 210 */           canFixed = true;
/*     */         } 
/* 212 */         if (canFixed)
/* 213 */           FileUtil.writeFile(options, String.join(System.lineSeparator(), (Iterable)lines)); 
/* 214 */       } catch (Throwable t) {
/* 215 */         U.log(new Object[] { t });
/* 216 */         return false;
/*     */       } 
/*     */     }
/* 219 */     return canFixed;
/*     */   }
/*     */   
/*     */   private void showProperMemoryMessage() {
/* 223 */     String link, recommendedMemory = "";
/*     */     
/* 225 */     if (TLauncher.getInstance().getConfiguration().isUSSRLocale()) {
/* 226 */       link = StringUtil.getLink("https://www.dmosk.ru/polezno.php?review=memory-notfull");
/*     */     }
/*     */     else {
/*     */       
/* 230 */       link = StringUtil.getLink("https://www.howtogeek.com/131632/hardware-upgrade-why-windows-cant-see-all-your-ram") + "<br>" + StringUtil.getLink("https://windowsreport.com/windows-10-isnt-using-all-ram");
/*     */     } 
/* 232 */     if (OS.Arch.TOTAL_RAM_MB < 1600) {
/* 233 */       recommendedMemory = Localizable.get("crash.java.not.enough.memory.solve.additional.low.memory");
/*     */     }
/* 235 */     Alert.showErrorHtml(String.format(Localizable.get("crash.java.not.enough.memory.solve"), new Object[] { Integer.valueOf(OS.Arch.TOTAL_RAM_MB), recommendedMemory, link }), 500);
/*     */   }
/*     */   
/*     */   private boolean replaceJVMParam() {
/*     */     try {
/* 240 */       CompleteVersion version = this.t.getVersionManager().getLocalList().getCompleteVersion(this.t.getLauncher()
/* 241 */           .getVersion().getID());
/* 242 */       Map<ArgumentType, List<Argument>> args = version.getArguments();
/*     */       
/* 244 */       if (Objects.nonNull(args) && Objects.nonNull(args.get(ArgumentType.JVM))) {
/* 245 */         List<Argument> list = args.get(ArgumentType.JVM);
/* 246 */         for (int i = 0; i < list.size(); i++) {
/* 247 */           Optional<String> xss1024 = Arrays.<String>stream(((Argument)list.get(i)).getValues()).filter(e -> e.equalsIgnoreCase("-Xss1M")).findAny();
/* 248 */           Optional<String> xss512 = Arrays.<String>stream(((Argument)list.get(i)).getValues()).filter(e -> e.equalsIgnoreCase("-Xss512K")).findAny();
/* 249 */           if (xss1024.isPresent()) {
/* 250 */             int ram = OS.Arch.PREFERRED_MEMORY - 256;
/* 251 */             if (ram > 512)
/* 252 */               this.t.getConfiguration().set("minecraft.memory.ram2", Integer.valueOf(ram), false); 
/* 253 */             list.remove(i);
/* 254 */             list.add(i, new Argument(new String[] { "-Xss512K" }, null));
/* 255 */             return true;
/*     */           } 
/* 257 */           if (xss512.isPresent()) {
/* 258 */             list.remove(i);
/* 259 */             list.add(i, new Argument(new String[] { "-Xss256K" }, null));
/* 260 */             return true;
/*     */           } 
/*     */         } 
/* 263 */         this.t.getVersionManager().getLocalList().saveVersion(version);
/*     */       } 
/* 265 */     } catch (IOException e) {
/* 266 */       U.log(new Object[0]);
/*     */     } 
/* 268 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftError(Throwable e) {
/* 273 */     showPossibleSolutions();
/*     */   }
/*     */   
/*     */   private void showPossibleSolutions() {
/* 277 */     MinecraftLauncher launcher = this.t.getLauncher();
/* 278 */     if (Objects.nonNull(launcher.getVersion()) && launcher.getVersion().isModpack()) {
/* 279 */       Alert.showErrorHtml("launcher.error.modpack.solutions", 400);
/*     */     } else {
/* 281 */       File mods = new File(MinecraftUtil.getWorkingDirectory(), "mods");
/* 282 */       File resourcepacks = new File(MinecraftUtil.getWorkingDirectory(), "resourcepacks");
/* 283 */       File shaderpacks = new File(MinecraftUtil.getWorkingDirectory(), "shaderpacks");
/* 284 */       String modpackStructureMessage = "";
/*     */       
/* 286 */       if (mods.exists() && FileUtils.listFiles(mods, new String[] { "jar", "zip" }, true).size() > 2) {
/* 287 */         modpackStructureMessage = Localizable.get("launcher.error.standard.version.point.mod");
/* 288 */       } else if (resourcepacks.exists() && !FileUtils.listFiles(resourcepacks, new String[] { "zip" }, true).isEmpty()) {
/* 289 */         modpackStructureMessage = Localizable.get("launcher.error.standard.version.point.resourcepack");
/* 290 */       } else if (shaderpacks.exists() && !FileUtils.listFiles(shaderpacks, new String[] { "zip" }, true).isEmpty()) {
/* 291 */         modpackStructureMessage = Localizable.get("launcher.error.standard.version.point.shaderpack");
/*     */       } 
/* 293 */       if (!modpackStructureMessage.isEmpty()) {
/* 294 */         Alert.showErrorHtml(Localizable.get(modpackStructureMessage, new Object[] { Localizable.get("crash.opengl.help") }), 600);
/*     */         return;
/*     */       } 
/* 297 */       Alert.showErrorHtml(Localizable.get("launcher.error.standard.version.solutions", new Object[] { modpackStructureMessage, 
/* 298 */               Localizable.get("crash.opengl.help") }), 400);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMinecraftKnownError(MinecraftException e) {
/* 304 */     Alert.showError(this.lang
/* 305 */         .get("launcher.error.title"), this.lang
/* 306 */         .get("launcher.error." + e.getLangPath(), (Object[])e
/* 307 */           .getLangVars()), e.getCause());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\listener\MinecraftUIListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */