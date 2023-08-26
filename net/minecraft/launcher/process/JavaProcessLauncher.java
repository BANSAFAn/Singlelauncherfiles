/*     */ package net.minecraft.launcher.process;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ 
/*     */ public class JavaProcessLauncher
/*     */ {
/*     */   private final String jvmPath;
/*     */   private final List<String> commands;
/*     */   private File directory;
/*     */   private ProcessBuilder process;
/*     */   private JavaProcessListener listener;
/*     */   
/*     */   public JavaProcessLauncher(String jvmPath, String[] commands) {
/*  20 */     this.jvmPath = jvmPath;
/*  21 */     this.commands = new ArrayList<>();
/*  22 */     Collections.addAll(this.commands, commands);
/*     */   }
/*     */   
/*     */   public JavaProcess start() throws IOException {
/*  26 */     List<String> full = getFullCommands();
/*  27 */     return new JavaProcess(full, createProcess().start(), this.listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public ProcessBuilder createProcess() {
/*  32 */     if (this.process == null)
/*  33 */       this
/*  34 */         .process = (new ProcessBuilder(getFullCommands())).directory(this.directory).redirectErrorStream(true); 
/*  35 */     String javaOption = TlauncherUtil.findJavaOptionAndGetName();
/*  36 */     if (Objects.nonNull(javaOption)) {
/*  37 */       this.process.environment().put(javaOption, "");
/*     */     }
/*  39 */     return this.process;
/*     */   }
/*     */   
/*     */   private List<String> getFullCommands() {
/*  43 */     List<String> result = new ArrayList<>(this.commands);
/*  44 */     result.add(0, this.jvmPath);
/*  45 */     return result;
/*     */   }
/*     */   
/*     */   public String getCommandsAsString() {
/*  49 */     List<String> parts = getFullCommands();
/*  50 */     StringBuilder full = new StringBuilder();
/*  51 */     boolean first = true;
/*     */     
/*  53 */     for (String part : parts) {
/*  54 */       if (first) {
/*  55 */         first = false;
/*     */       } else {
/*  57 */         full.append(' ');
/*  58 */       }  full.append(part);
/*     */     } 
/*     */     
/*  61 */     return full.toString();
/*     */   }
/*     */   
/*     */   public List<String> getCommands() {
/*  65 */     return this.commands;
/*     */   }
/*     */   
/*     */   public void addCommand(Object command) {
/*  69 */     this.commands.add(command.toString());
/*     */   }
/*     */   
/*     */   public void addCommand(Object key, Object value) {
/*  73 */     this.commands.add(key.toString());
/*  74 */     this.commands.add(value.toString());
/*     */   }
/*     */   
/*     */   public void addCommands(Object[] commands) {
/*  78 */     for (Object c : commands)
/*  79 */       this.commands.add(c.toString()); 
/*     */   }
/*     */   
/*     */   public void addSplitCommands(Object commands) {
/*  83 */     addCommands((Object[])commands.toString().split(" "));
/*     */   }
/*     */   
/*     */   public JavaProcessLauncher directory(File directory) {
/*  87 */     this.directory = directory;
/*     */     
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public File getDirectory() {
/*  93 */     return this.directory;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  98 */     return "JavaProcessLauncher[commands=" + this.commands + ", java=" + this.jvmPath + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public void setListener(JavaProcessListener listener) {
/* 103 */     this.listener = listener;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\process\JavaProcessLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */