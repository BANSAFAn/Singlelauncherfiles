/*    */ package org.tlauncher.util.statistics;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Toolkit;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.Date;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.launcher.Http;
/*    */ import org.tlauncher.statistics.UniqueClientDTO;
/*    */ import org.tlauncher.statistics.UpdaterDTO;
/*    */ import org.tlauncher.tlauncher.configuration.Configuration;
/*    */ import org.tlauncher.tlauncher.controller.UpdaterFormController;
/*    */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*    */ import org.tlauncher.tlauncher.updater.client.Update;
/*    */ import org.tlauncher.util.OS;
/*    */ import org.tlauncher.util.U;
/*    */ import org.tlauncher.util.async.AsyncThread;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StatisticsUtil
/*    */ {
/*    */   public static void startSending(String path, Object ob, Map<String, Object> queries) {
/* 28 */     if (!TLauncher.getInstance().getConfiguration().getBoolean("gui.statistics.checkbox")) {
/* 29 */       AsyncThread.execute(() -> {
/*    */             try {
/*    */               send(path, ob, queries);
/* 32 */             } catch (IOException e) {
/*    */               U.log(new Object[] { e });
/*    */             } 
/*    */           });
/*    */     }
/*    */   }
/*    */   
/*    */   public static void send(String path, Object ob, Map<String, Object> queries) throws IOException {
/* 40 */     String domain = TLauncher.getInnerSettings().get("statistics.url");
/* 41 */     Http.performPost(new URL(Http.get(domain + path, queries)), TLauncher.getGson().toJson(ob), "application/json");
/*    */   }
/*    */ 
/*    */   
/*    */   public static void sendMachineInfo(Configuration conf) {
/* 46 */     UniqueClientDTO running = new UniqueClientDTO();
/* 47 */     running.setClientVersion(TLauncher.getVersion());
/* 48 */     running.setOs(OS.CURRENT.name());
/* 49 */     Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
/* 50 */     running.setResolution(size.width + "x" + size.height);
/* 51 */     running.setJavaVersion(System.getProperty("java.version"));
/* 52 */     running.setOsVersion(OS.VERSION);
/* 53 */     running.setUuid(conf.get("client").replace("-", ""));
/* 54 */     running.setGpu(conf.get("gpu.info"));
/* 55 */     running.setRam(OS.Arch.TOTAL_RAM_MB);
/* 56 */     running.setProcessorArchitecture(conf.get("processor.architecture"));
/* 57 */     running.setBits(OS.getJavaBit().name());
/* 58 */     String processor = conf.get("process.info");
/* 59 */     if (Objects.nonNull(processor))
/* 60 */       processor = processor.trim(); 
/* 61 */     running.setCpu(processor);
/* 62 */     startSending("save/run/tlauncher/unique/month", running, Maps.newHashMap());
/*    */   }
/*    */   
/*    */   public static void sendUpdatingInfo(Update update, UpdaterFormController.UserResult res) {
/*    */     try {
/* 67 */       UpdaterDTO dto = preparedUpdaterDTO(update, res);
/* 68 */       send("updater/save", dto, Maps.newHashMap());
/* 69 */     } catch (Throwable t) {
/* 70 */       U.log(new Object[] { t });
/*    */     } 
/*    */   }
/*    */   
/*    */   public static UpdaterDTO preparedUpdaterDTO(Update update, UpdaterFormController.UserResult res) {
/* 75 */     UpdaterDTO dto = new UpdaterDTO();
/* 76 */     dto.setClient(TLauncher.getInstance().getConfiguration().get("client"));
/* 77 */     dto.setOffer(update.getSelectedOffer().getOffer());
/* 78 */     dto.setArgs(res.getOfferArgs());
/* 79 */     dto.setCurrentVersion(TLauncher.getInnerSettings().getDouble("version"));
/* 80 */     dto.setNewVersion(update.getVersion());
/* 81 */     dto.setRequestTime((new Date()).toString());
/* 82 */     return dto;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\statistics\StatisticsUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */