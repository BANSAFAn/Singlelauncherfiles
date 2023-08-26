/*    */ package org.tlauncher.tlauncher.minecraft.launcher.assitent;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.file.Files;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.List;
/*    */ import net.minecraft.launcher.versions.CompleteVersion;
/*    */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*    */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*    */ import org.tlauncher.util.FileUtil;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ 
/*    */ public class LanguageAssistance
/*    */   extends MinecraftLauncherAssistantWrapper
/*    */ {
/* 19 */   public static String OPTIONS = "options.txt";
/*    */ 
/*    */   
/*    */   public void constructProgramArguments(MinecraftLauncher launcher) {
/* 23 */     CompleteVersion v = launcher.getVersion();
/*    */     
/* 25 */     Date date = v.getReleaseTime();
/*    */     try {
/* 27 */       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
/* 28 */       Date checkDate = format.parse("2016-11-14");
/* 29 */       File gameFolder = launcher.getRunningMinecraftDir();
/* 30 */       File options = new File(gameFolder, OPTIONS);
/* 31 */       if (options.exists()) {
/* 32 */         List<String> lines = Files.readAllLines(options.toPath(), StandardCharsets.UTF_8);
/* 33 */         int index = -1;
/* 34 */         for (int i = 0; i < lines.size(); i++) {
/* 35 */           if (((String)lines.get(i)).contains("lang:")) {
/* 36 */             index = i;
/*    */             break;
/*    */           } 
/*    */         } 
/* 40 */         if (index != -1 && date.before(checkDate)) {
/* 41 */           String line = lines.get(index);
/* 42 */           String[] array = line.split(":");
/* 43 */           String[] arraysLang = array[1].split("_");
/* 44 */           String end = arraysLang[1];
/* 45 */           if (!end.toUpperCase().equals(end)) {
/* 46 */             line = "lang:" + arraysLang[0] + "_" + end.toUpperCase();
/* 47 */             lines.remove(index);
/* 48 */             lines.add(index, line);
/* 49 */             FileUtil.writeFile(options, String.join(System.lineSeparator(), (Iterable)lines));
/*    */           } 
/* 51 */         } else if (index == -1) {
/* 52 */           String lang = getLang(date, checkDate);
/* 53 */           lines.add(lang);
/* 54 */           FileUtil.writeFile(options, String.join(System.lineSeparator(), (Iterable)lines));
/*    */         } 
/*    */       } else {
/* 57 */         String lang = getLang(date, checkDate);
/* 58 */         FileUtil.writeFile(options, lang);
/*    */       } 
/* 60 */     } catch (Throwable e) {
/* 61 */       U.log(new Object[] { e });
/*    */     } 
/*    */   }
/*    */   
/*    */   private String getLang(Date date, Date checkDate) {
/* 66 */     String lang = Localizable.get().getSelected().toString();
/* 67 */     if (date.after(checkDate)) {
/* 68 */       lang = lang.toLowerCase();
/*    */     }
/* 70 */     return "lang:" + lang;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraft\launcher\assitent\LanguageAssistance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */