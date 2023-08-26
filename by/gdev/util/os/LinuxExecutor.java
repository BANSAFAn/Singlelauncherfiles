/*    */ package by.gdev.util.os;
/*    */ 
/*    */ import by.gdev.util.model.GPUDescription;
/*    */ import by.gdev.util.model.GPUDriverVersion;
/*    */ import by.gdev.util.model.GPUsDescriptionDTO;
/*    */ import java.io.IOException;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.stream.Collectors;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LinuxExecutor
/*    */   implements OSExecutor
/*    */ {
/* 23 */   private static final Path CUDA_VERSION_PATH = Paths.get("/usr/local/cuda/version.txt", new String[0]);
/*    */ 
/*    */   
/*    */   public String execute(String command, int seconds) throws IOException, InterruptedException {
/* 27 */     Process p = Runtime.getRuntime().exec(command);
/* 28 */     p.waitFor(seconds, TimeUnit.SECONDS);
/* 29 */     String res = IOUtils.toString(p.getInputStream(), Charset.defaultCharset());
/* 30 */     p.getInputStream().close();
/* 31 */     return res;
/*    */   }
/*    */ 
/*    */   
/*    */   public GPUsDescriptionDTO getGPUInfo() throws IOException, InterruptedException {
/* 36 */     String res = execute("lshw -C display", 60);
/* 37 */     return getGPUInfo1(res, "product:");
/*    */   }
/*    */ 
/*    */   
/*    */   public GPUDriverVersion getGPUDriverVersion() throws IOException {
/* 42 */     if (Files.notExists(CUDA_VERSION_PATH, new java.nio.file.LinkOption[0]))
/* 43 */       return null; 
/* 44 */     String s = new String(Files.readAllBytes(CUDA_VERSION_PATH));
/* 45 */     String[] res = s.split(" ");
/* 46 */     if (res.length == 3) {
/*    */       
/* 48 */       Optional<GPUDriverVersion> op = Arrays.<GPUDriverVersion>stream(GPUDriverVersion.values()).filter(f -> res[2].startsWith(f.getValue())).findFirst();
/* 49 */       if (op.isPresent()) {
/* 50 */         return op.get();
/*    */       }
/*    */     } 
/* 53 */     return null;
/*    */   }
/*    */   
/*    */   protected GPUsDescriptionDTO getGPUInfo1(String res, String stringStart) {
/* 57 */     String[] params = res.split(System.lineSeparator());
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 63 */     List<GPUDescription> gpus = (List<GPUDescription>)Arrays.<String>stream(params).map(String::toLowerCase).filter(e -> e.contains(stringStart)).map(s -> { GPUDescription g = new GPUDescription(); g.setName(s.split(":")[1]); return g; }).collect(Collectors.toList());
/* 64 */     GPUsDescriptionDTO gpusDescriptionDTO = new GPUsDescriptionDTO();
/* 65 */     gpusDescriptionDTO.setRawDescription(res);
/* 66 */     gpusDescriptionDTO.setGpus(gpus);
/* 67 */     return gpusDescriptionDTO;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\os\LinuxExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */