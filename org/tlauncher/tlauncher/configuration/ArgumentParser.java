/*    */ package org.tlauncher.tlauncher.configuration;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import joptsimple.OptionException;
/*    */ import joptsimple.OptionParser;
/*    */ import joptsimple.OptionSet;
/*    */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*    */ 
/*    */ 
/*    */ public class ArgumentParser
/*    */ {
/* 13 */   private static final Map<String, String> m = createLinkMap();
/* 14 */   private static final OptionParser parser = createParser();
/*    */   
/*    */   public static OptionParser getParser() {
/* 17 */     return parser;
/*    */   }
/*    */   
/*    */   public static OptionSet parseArgs(String[] args) {
/* 21 */     OptionSet set = null;
/*    */     try {
/* 23 */       set = parser.parse(args);
/* 24 */     } catch (OptionException e) {
/* 25 */       e.printStackTrace();
/* 26 */       Alert.showError(e, false);
/*    */     } 
/* 28 */     return set;
/*    */   }
/*    */   
/*    */   public static Map<String, Object> parse(OptionSet set) {
/* 32 */     Map<String, Object> r = new HashMap<>();
/* 33 */     if (set == null) {
/* 34 */       return r;
/*    */     }
/* 36 */     for (Map.Entry<String, String> a : m.entrySet()) {
/* 37 */       String key = a.getKey();
/* 38 */       Object value = null;
/* 39 */       if (key.startsWith("-")) {
/* 40 */         key = key.substring(1);
/* 41 */         value = Boolean.valueOf(true);
/*    */       } 
/* 43 */       if (!set.has(key)) {
/*    */         continue;
/*    */       }
/* 46 */       if (value == null)
/* 47 */         value = set.valueOf(key); 
/* 48 */       r.put(a.getValue(), value);
/*    */     } 
/*    */     
/* 51 */     return r;
/*    */   }
/*    */   
/*    */   private static Map<String, String> createLinkMap() {
/* 55 */     Map<String, String> r = new HashMap<>();
/*    */     
/* 57 */     r.put("directory", "minecraft.gamedir");
/* 58 */     r.put("java-directory", "minecraft.javadir");
/* 59 */     r.put("version", "login.version.game");
/* 60 */     r.put("usertype", "login.account.type");
/* 61 */     r.put("javaargs", "minecraft.javaargs");
/* 62 */     r.put("margs", "minecraft.args");
/* 63 */     r.put("window", "minecraft.size");
/* 64 */     r.put("background", "gui.background");
/* 65 */     r.put("fullscreen", "minecraft.fullscreen");
/* 66 */     r.put("RunAllTLauncherVersions", "run.all.tlauncher.versions");
/* 67 */     r.put("RunAllOfficialVersions", "run.all.official.versions");
/*    */     
/* 69 */     return r;
/*    */   }
/*    */   
/*    */   private static OptionParser createParser() {
/* 73 */     OptionParser parser = new OptionParser();
/*    */     
/* 75 */     parser.accepts("help", "Shows this help");
/* 76 */     parser.accepts("nogui", "Starts minimal version");
/* 77 */     parser.accepts("directory", "Specifies Minecraft directory")
/* 78 */       .withRequiredArg();
/* 79 */     parser.accepts("java-directory", "Specifies Java directory")
/* 80 */       .withRequiredArg();
/* 81 */     parser.accepts("version", "Specifies version to run").withRequiredArg();
/* 82 */     parser.accepts("javaargs", "Specifies JVM arguments").withRequiredArg();
/* 83 */     parser.accepts("margs", "Specifies Minecraft arguments")
/* 84 */       .withRequiredArg();
/* 85 */     parser.accepts("window", "Specifies window size in format: width;height")
/*    */       
/* 87 */       .withRequiredArg();
/* 88 */     parser.accepts("settings", "Specifies path to settings file")
/* 89 */       .withRequiredArg();
/* 90 */     parser.accepts("background", "Specifies background image. URL links, JPEG and PNG formats are supported.")
/*    */       
/* 92 */       .withRequiredArg();
/* 93 */     parser.accepts("fullscreen", "Specifies whether fullscreen mode enabled or not")
/*    */       
/* 95 */       .withRequiredArg();
/* 96 */     parser.accepts("RunAllTLauncherVersions", "Run all tlauncher versions").withRequiredArg();
/* 97 */     parser.accepts("RunAllOfficialVersions", "Run all official versions").withRequiredArg();
/*    */     
/* 99 */     return parser;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\ArgumentParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */