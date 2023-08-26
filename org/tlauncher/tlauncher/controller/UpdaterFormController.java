/*     */ package org.tlauncher.tlauncher.controller;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.launcher.Http;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.lang3.time.DateUtils;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.TLauncherFrame;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.updater.UpdaterMessageView;
/*     */ import org.tlauncher.tlauncher.updater.client.Offer;
/*     */ import org.tlauncher.tlauncher.updater.client.Update;
/*     */ import org.tlauncher.util.OS;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.statistics.StatisticsUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpdaterFormController
/*     */ {
/*     */   private UpdaterMessageView view;
/*     */   private Update update;
/*     */   private int messageType;
/*  39 */   private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*     */   
/*     */   private Configuration settings;
/*     */   
/*     */   public UpdaterFormController(Update update, Configuration settings) {
/*  44 */     this.update = update;
/*  45 */     this.settings = settings;
/*  46 */     int force = update.getUpdaterView();
/*  47 */     String lang = Localizable.get().getSelected().toString();
/*  48 */     boolean hashOfferDelay = false;
/*     */ 
/*     */     
/*  51 */     Date offerDelay = DateUtils.addDays(new Date(settings.getLong("updater.offer.installer.empty.checkbox.delay")), update.getOfferEmptyCheckboxDelay());
/*  52 */     if (settings.isExist("updater.offer.installer.empty.checkbox.delay") && offerDelay.after(new Date())) {
/*  53 */       hashOfferDelay = true;
/*     */     }
/*     */ 
/*     */     
/*  57 */     offerDelay = DateUtils.addDays(new Date(settings.getLong("updater.offer.installer.delay")), update.getOfferDelay());
/*  58 */     if (settings.isExist("updater.offer.installer.delay") && offerDelay.after(new Date())) {
/*  59 */       hashOfferDelay = true;
/*     */     }
/*     */     try {
/*  62 */       if (settings.isExist("updater.offer.installer.empty.checkbox.delay1")) {
/*  63 */         offerDelay = DateUtils.addDays(FORMAT.parse(settings.get("updater.offer.installer.empty.checkbox.delay1")), update
/*  64 */             .getOfferEmptyCheckboxDelay());
/*  65 */         if (offerDelay.after(new Date())) {
/*  66 */           hashOfferDelay = true;
/*     */         }
/*     */       } 
/*  69 */       offerDelay = DateUtils.addDays(FORMAT.parse(settings.get("updater.offer.installer.delay1")), update.getOfferDelay());
/*  70 */       if (settings.isExist("updater.offer.installer.delay1") && offerDelay.after(new Date())) {
/*  71 */         hashOfferDelay = true;
/*     */       }
/*  73 */     } catch (ParseException parseException) {}
/*     */ 
/*     */     
/*  76 */     if (!hashOfferDelay && OS.is(new OS[] { OS.WINDOWS }) && force == 2 && update
/*  77 */       .getOfferByLang(lang).isPresent())
/*  78 */     { update.setSelectedOffer(update.getOfferByLang(lang).get());
/*  79 */       this.messageType = 2; }
/*  80 */     else if (!update.getBanners().isEmpty() && update.getBanners().get(lang) != null && force != 0)
/*     */     
/*  82 */     { this.messageType = 1; }
/*  83 */     else { this.messageType = 0; }
/*  84 */      this.view = new UpdaterMessageView(update, this.messageType, lang, TlauncherUtil.isAdmin());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getResult() {
/*  92 */     long delay = this.settings.getLong("updater.delay");
/*  93 */     int hours = TLauncher.getInnerSettings().getInteger("updater.chooser.delay");
/*  94 */     if ((new Date()).getTime() < (new Date(delay)).getTime() + (hours * 3600 * 1000)) {
/*  95 */       return false;
/*     */     }
/*  97 */     return processUpdating();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean processUpdating() {
/* 102 */     UserResult res = this.view.showMessage();
/* 103 */     switch (res.getUserChooser()) {
/*     */       case 1:
/* 105 */         if (this.messageType == 2 && 
/* 106 */           isExecutedOffer(res)) {
/* 107 */           return processUpdating();
/*     */         }
/*     */         
/* 110 */         return true;
/*     */       case 0:
/* 112 */         if (this.messageType == 2 && 
/* 113 */           isExecutedOffer(res)) {
/* 114 */           return processUpdating();
/*     */         }
/*     */         
/* 117 */         this.settings.set("updater.delay", Long.valueOf((new Date()).getTime()), true);
/* 118 */         return false;
/*     */     } 
/* 120 */     TLauncherFrame frame = TLauncher.getInstance().getFrame();
/* 121 */     if (Objects.isNull(frame)) {
/* 122 */       System.exit(0);
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isExecutedOffer(UserResult res) {
/*     */     try {
/* 132 */       if (res.getUserChooser() == 1 || (res.getUserChooser() == 0 && this.update.isUpdaterLaterInstall())) {
/* 133 */         Offer offer = this.update.getSelectedOffer();
/* 134 */         Path tempFile = Files.createTempFile("install", ".exe", (FileAttribute<?>[])new FileAttribute[0]);
/* 135 */         FileUtils.copyURLToFile(new URL(offer.getInstaller()), tempFile.toFile(), 30000, 30000);
/* 136 */         String args = (String)offer.getArgs().get(res.getOfferArgs());
/* 137 */         String runningOffer = tempFile + " " + args;
/* 138 */         if (res.isSelectedAnyCheckBox()) {
/* 139 */           Path runner = Files.createTempFile("TLauncherUpdater", ".exe", (FileAttribute<?>[])new FileAttribute[0]);
/* 140 */           FileUtils.copyURLToFile(new URL(this.update.getRootAccessExe().get(0)), runner.toFile(), 30000, 30000);
/*     */           
/* 142 */           TLauncher.getInstance().getDownloader().setConfiguration(ConnectionQuality.BAD);
/* 143 */           String url = Http.get(TLauncher.getInnerSettings().get("statistics.url") + "updater/save", Maps.newHashMap());
/* 144 */           String data = TLauncher.getGson().toJson(StatisticsUtil.preparedUpdaterDTO(this.update, res));
/*     */ 
/*     */           
/* 147 */           String[] s = { "cmd", "/c", runner.toString(), runningOffer.replace("\"", "\\\""), url, data.replace("\"", "\\\"") };
/* 148 */           Process p = Runtime.getRuntime().exec(s);
/* 149 */           if (p.waitFor() == 1)
/* 150 */             return true; 
/*     */         } else {
/* 152 */           StatisticsUtil.sendUpdatingInfo(this.update, res);
/* 153 */           Runtime.getRuntime().exec(runningOffer);
/*     */         } 
/*     */       } 
/* 156 */     } catch (Exception e) {
/* 157 */       U.log(new Object[] { e });
/*     */     } 
/* 159 */     if (res.isSelectedAnyCheckBox()) {
/* 160 */       TLauncher.getInstance().getConfiguration()
/* 161 */         .set("updater.offer.installer.delay1", FORMAT.format(new Date()), true);
/*     */     } else {
/* 163 */       TLauncher.getInstance().getConfiguration()
/* 164 */         .set("updater.offer.installer.empty.checkbox.delay1", FORMAT.format(new Date()), true);
/*     */     } 
/* 166 */     return false;
/*     */   }
/*     */   public static class UserResult {
/*     */     private String offerArgs;
/* 170 */     public void setOfferArgs(String offerArgs) { this.offerArgs = offerArgs; } private int userChooser; private boolean selectedAnyCheckBox; public void setUserChooser(int userChooser) { this.userChooser = userChooser; } public void setSelectedAnyCheckBox(boolean selectedAnyCheckBox) { this.selectedAnyCheckBox = selectedAnyCheckBox; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof UserResult)) return false;  UserResult other = (UserResult)o; if (!other.canEqual(this)) return false;  Object this$offerArgs = getOfferArgs(), other$offerArgs = other.getOfferArgs(); return ((this$offerArgs == null) ? (other$offerArgs != null) : !this$offerArgs.equals(other$offerArgs)) ? false : ((getUserChooser() != other.getUserChooser()) ? false : (!(isSelectedAnyCheckBox() != other.isSelectedAnyCheckBox()))); } protected boolean canEqual(Object other) { return other instanceof UserResult; } public int hashCode() { int PRIME = 59; result = 1; Object $offerArgs = getOfferArgs(); result = result * 59 + (($offerArgs == null) ? 43 : $offerArgs.hashCode()); result = result * 59 + getUserChooser(); return result * 59 + (isSelectedAnyCheckBox() ? 79 : 97); } public String toString() { return "UpdaterFormController.UserResult(offerArgs=" + getOfferArgs() + ", userChooser=" + getUserChooser() + ", selectedAnyCheckBox=" + isSelectedAnyCheckBox() + ")"; }
/*     */     
/* 172 */     public String getOfferArgs() { return this.offerArgs; }
/* 173 */     public int getUserChooser() { return this.userChooser; } public boolean isSelectedAnyCheckBox() {
/* 174 */       return this.selectedAnyCheckBox;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\controller\UpdaterFormController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */