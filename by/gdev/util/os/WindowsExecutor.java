/*     */ package by.gdev.util.os;
/*     */ 
/*     */ import by.gdev.util.model.GPUDescription;
/*     */ import by.gdev.util.model.GPUDriverVersion;
/*     */ import by.gdev.util.model.GPUsDescriptionDTO;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ public class WindowsExecutor
/*     */   implements OSExecutor
/*     */ {
/*  28 */   private static final Logger log = Logger.getLogger(WindowsExecutor.class.getName());
/*     */ 
/*     */   
/*     */   private static final String UNKNOWN = "unknown";
/*     */ 
/*     */   
/*     */   public String execute(String command, int seconds) throws IOException, InterruptedException {
/*  35 */     Process p = Runtime.getRuntime().exec("cmd.exe /C chcp 437 & " + command);
/*  36 */     p.waitFor(seconds, TimeUnit.SECONDS);
/*  37 */     String res = IOUtils.toString(p.getInputStream(), "IBM437");
/*  38 */     p.getInputStream().close();
/*  39 */     return res;
/*     */   }
/*     */ 
/*     */   
/*     */   public GPUsDescriptionDTO getGPUInfo() throws IOException, InterruptedException {
/*  44 */     Path path = null;
/*     */     try {
/*  46 */       path = Files.createTempFile("dxdiag", ".txt", (FileAttribute<?>[])new FileAttribute[0]);
/*  47 */       String command = String.format("dxdiag /whql:off /t %s", new Object[] { path.toAbsolutePath() });
/*  48 */       execute(command, 60);
/*     */       
/*  50 */       long size = -1L;
/*  51 */       for (int i = 0; i < 60; i++) {
/*  52 */         Thread.sleep(500L);
/*  53 */         if (Files.exists(path, new java.nio.file.LinkOption[0])) {
/*  54 */           if (size == path.toFile().length()) {
/*     */             break;
/*     */           }
/*  57 */           size = path.toFile().length();
/*     */         } 
/*  59 */       }  List<String> list = Files.readAllLines(path, Charset.forName("437"));
/*  60 */       return processSystemInfoLines(list);
/*     */     } finally {
/*  62 */       if (Objects.nonNull(path)) {
/*  63 */         Files.deleteIfExists(path);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public GPUsDescriptionDTO processSystemInfoLines(List<String> list) {
/*  69 */     Set<String> set = new HashSet<>();
/*  70 */     set.add("unknown");
/*  71 */     List<GPUDescription> gpus = new ArrayList<>();
/*  72 */     GPUsDescriptionDTO desc = new GPUsDescriptionDTO();
/*  73 */     desc.setRawDescription(list.stream().collect(Collectors.joining(System.lineSeparator())));
/*  74 */     for (String s : list.stream().map(String::toLowerCase).collect(Collectors.toList())) {
/*  75 */       if (StringUtils.contains(s, "card name:")) {
/*  76 */         GPUDescription g = new GPUDescription();
/*  77 */         g.setName(s.split(":")[1]);
/*  78 */         gpus.add(g);
/*     */       } 
/*  80 */       if (gpus.size() > 0) {
/*  81 */         if (StringUtils.contains(s, "chip type:")) {
/*  82 */           ((GPUDescription)gpus.get(gpus.size() - 1)).setChipType(s.split(":")[1]); continue;
/*  83 */         }  if (StringUtils.contains(s, "display memory:")) {
/*  84 */           ((GPUDescription)gpus.get(gpus.size() - 1)).setMemory(s.split(":")[1]); continue;
/*  85 */         }  if (StringUtils.contains(s, "current mode:")) {
/*  86 */           String cm = s.split(":")[1].trim();
/*  87 */           if (!"unknown".equalsIgnoreCase(cm) && set.size() > 1) {
/*  88 */             gpus.remove(gpus.size() - 1); continue;
/*     */           } 
/*  90 */           set.add(cm);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     desc.setGpus(gpus);
/*  96 */     return desc;
/*     */   }
/*     */ 
/*     */   
/*     */   public GPUDriverVersion getGPUDriverVersion() throws IOException, InterruptedException {
/* 101 */     String res = execute("nvcc --version", 60);
/* 102 */     log.config("nvcc --version -> " + res);
/* 103 */     String[] array = res.split(System.lineSeparator());
/* 104 */     if (array.length == 5) {
/* 105 */       String[] array1 = array[4].trim().split(",");
/* 106 */       if (array1.length == 3) {
/* 107 */         String[] array2 = array1[1].trim().split(" ");
/* 108 */         if (array2.length == 2) {
/* 109 */           String rawCudaVersion = array2[1];
/* 110 */           log.config("raw cuda version " + rawCudaVersion);
/*     */           
/* 112 */           Optional<GPUDriverVersion> c = Arrays.<GPUDriverVersion>stream(GPUDriverVersion.values()).filter(e -> e.getValue().equalsIgnoreCase("10.2")).findAny();
/* 113 */           if (c.isPresent())
/* 114 */             return c.get(); 
/*     */         } 
/*     */       } 
/*     */     } 
/* 118 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\os\WindowsExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */