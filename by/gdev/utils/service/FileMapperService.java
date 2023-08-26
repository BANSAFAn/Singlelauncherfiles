/*    */ package by.gdev.utils.service;
/*    */ 
/*    */ import by.gdev.util.excepiton.NotAllowWriteFileOperation;
/*    */ import com.google.gson.Gson;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Path;
/*    */ import java.nio.file.Paths;
/*    */ import java.nio.file.attribute.FileAttribute;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class FileMapperService
/*    */ {
/* 21 */   private static final Logger log = LoggerFactory.getLogger(FileMapperService.class);
/*    */   
/*    */   private Gson gson;
/*    */   private Charset charset;
/*    */   private String workingDirectory;
/*    */   
/*    */   public FileMapperService(Gson gson, Charset charset, String workingDirectory) {
/* 28 */     this.gson = gson;
/* 29 */     this.charset = charset;
/* 30 */     this.workingDirectory = workingDirectory;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(Object create, String config) throws IOException, UnsupportedOperationException {
/* 41 */     Path path = Paths.get(this.workingDirectory, new String[] { config });
/* 42 */     if (Files.notExists(path.getParent(), new java.nio.file.LinkOption[0]))
/* 43 */       Files.createDirectories(path.getParent(), (FileAttribute<?>[])new FileAttribute[0]); 
/* 44 */     if (Files.exists(path, new java.nio.file.LinkOption[0]) && !path.toFile().canWrite())
/* 45 */       throw new NotAllowWriteFileOperation(path.toString()); 
/* 46 */     try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path.toFile()), this.charset)) {
/* 47 */       this.gson.toJson(create, out);
/*    */     } 
/*    */   }
/*    */   
/*    */   public <T> T read(String file, Class<T> cl) {
/* 52 */     try (InputStreamReader read = new InputStreamReader(new FileInputStream(Paths.get(this.workingDirectory, new String[] { file }).toFile()), this.charset)) {
/* 53 */       return (T)this.gson.fromJson(read, cl);
/* 54 */     } catch (FileNotFoundException e) {
/* 55 */       log.info("file not exist " + file);
/* 56 */     } catch (Throwable t) {
/* 57 */       log.warn("error read json " + file, t);
/*    */     } 
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\utils\service\FileMapperService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */