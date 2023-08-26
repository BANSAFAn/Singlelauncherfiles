/*     */ package org.tlauncher.util.guice;
/*     */ 
/*     */ import by.gdev.http.download.config.HttpClientConfig;
/*     */ import by.gdev.http.download.impl.FileCacheServiceImpl;
/*     */ import by.gdev.http.download.impl.GsonServiceImpl;
/*     */ import by.gdev.http.download.impl.HttpServiceImpl;
/*     */ import by.gdev.http.download.service.FileCacheService;
/*     */ import by.gdev.http.download.service.GsonService;
/*     */ import by.gdev.http.download.service.HttpService;
/*     */ import by.gdev.util.DesktopUtil;
/*     */ import by.gdev.utils.service.FileMapperService;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.eventbus.EventBus;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.TypeAdapterFactory;
/*     */ import com.google.inject.AbstractModule;
/*     */ import com.google.inject.Injector;
/*     */ import com.google.inject.Provides;
/*     */ import com.google.inject.Singleton;
/*     */ import com.google.inject.TypeLiteral;
/*     */ import com.google.inject.assistedinject.FactoryModuleBuilder;
/*     */ import com.google.inject.matcher.Matchers;
/*     */ import com.google.inject.name.Named;
/*     */ import com.google.inject.name.Names;
/*     */ import com.google.inject.spi.InjectionListener;
/*     */ import com.google.inject.spi.TypeEncounter;
/*     */ import com.google.inject.spi.TypeListener;
/*     */ import java.io.File;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Date;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.json.DateTypeAdapter;
/*     */ import net.minecraft.launcher.versions.json.FileTypeAdapter;
/*     */ import net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory;
/*     */ import net.minecraft.launcher.versions.json.PatternTypeAdapter;
/*     */ import org.apache.http.client.config.RequestConfig;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.tlauncher.modpack.domain.client.GameVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.MapDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*     */ import org.tlauncher.modpack.domain.client.ResourcePackDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.NameIdDTO;
/*     */ import org.tlauncher.tlauncher.configuration.Configuration;
/*     */ import org.tlauncher.tlauncher.configuration.InnerConfiguration;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.AuthenticatorDatabase;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.UUIDTypeAdapter;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.assitent.AdditionalFileAssistance;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.assitent.AdditionalFileAssistanceFactory;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.assitent.LanguageAssistance;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.assitent.SoundAssist;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServer;
/*     */ import org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServersImpl;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.alert.Alert;
/*     */ import org.tlauncher.tlauncher.ui.console.Console;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileChooser;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileExplorer;
/*     */ import org.tlauncher.tlauncher.ui.explorer.FileWrapper;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.tlauncher.ui.progress.ProgressFrame;
/*     */ import org.tlauncher.util.MinecraftUtil;
/*     */ import org.tlauncher.util.TlauncherUtil;
/*     */ import org.tlauncher.util.U;
/*     */ import org.tlauncher.util.gson.serializer.MapDTOTypeAdapter;
/*     */ import org.tlauncher.util.gson.serializer.ModDTOTypeAdapter;
/*     */ import org.tlauncher.util.gson.serializer.ModpackDTOTypeAdapter;
/*     */ import org.tlauncher.util.gson.serializer.ResourcePackDTOTypeAdapter;
/*     */ 
/*     */ public class GuiceModule
/*     */   extends AbstractModule
/*     */ {
/*     */   private final Configuration configuration;
/*     */   private final InnerConfiguration inner;
/*     */   private Injector injector;
/*     */   private boolean usedDefaultChooser = true;
/*  84 */   private EventBus eventBus = new EventBus();
/*     */   
/*     */   public Injector getInjector() {
/*  87 */     return this.injector;
/*     */   }
/*     */   
/*     */   public void setInjector(Injector injector) {
/*  91 */     this.injector = injector;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuiceModule(Configuration configuration, InnerConfiguration inner) {
/*  96 */     this.configuration = configuration;
/*  97 */     this.inner = inner;
/*     */     
/*     */     try {
/* 100 */       new FileExplorer();
/* 101 */     } catch (Throwable t) {
/* 102 */       U.log(new Object[] { "problem with standard FileExplorer" });
/* 103 */       this.usedDefaultChooser = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void configure() {
/*     */     Class<FileWrapper> clazz;
/* 110 */     bind(Gson.class).annotatedWith((Annotation)Names.named("GsonCompleteVersion")).toInstance(createGsonCompleteVersion());
/* 111 */     bind(Gson.class).annotatedWith((Annotation)Names.named("GsonAdditionalFile")).toInstance(createAddittionalFileGson());
/* 112 */     bind(InnerMinecraftServer.class).to(InnerMinecraftServersImpl.class);
/* 113 */     bind(Console.class).annotatedWith((Annotation)Names.named("console")).toInstance(new Console());
/*     */     
/* 115 */     bind(ExecutorService.class).annotatedWith((Annotation)Names.named("modpackExecutorService"))
/* 116 */       .toInstance(Executors.newSingleThreadExecutor());
/* 117 */     Class<FileExplorer> clazz1 = FileExplorer.class;
/* 118 */     if (!this.usedDefaultChooser)
/* 119 */       clazz = FileWrapper.class; 
/* 120 */     bind(FileChooser.class).to(clazz);
/*     */     
/* 122 */     install((new FactoryModuleBuilder()).implement(MinecraftLauncher.class, MinecraftLauncher.class)
/* 123 */         .build(MinecraftLauncherFactory.class));
/* 124 */     install((new FactoryModuleBuilder()).implement(TLauncher.class, TLauncher.class).build(TlauncherFactory.class));
/* 125 */     install((new FactoryModuleBuilder()).implement(ProgressFrame.class, ProgressFrame.class)
/* 126 */         .build(CustomBarFactory.class));
/* 127 */     install((new FactoryModuleBuilder()).implement(SoundAssist.class, SoundAssist.class)
/* 128 */         .build(SoundAssistFactory.class));
/* 129 */     install((new FactoryModuleBuilder()).implement(AdditionalFileAssistance.class, AdditionalFileAssistance.class)
/* 130 */         .build(AdditionalFileAssistanceFactory.class));
/* 131 */     install((new FactoryModuleBuilder()).implement(LanguageAssistance.class, LanguageAssistance.class)
/* 132 */         .build(LanguageAssistFactory.class));
/*     */     
/* 134 */     bind(EventBus.class).toInstance(this.eventBus);
/*     */     
/* 136 */     bindListener(Matchers.any(), new TypeListener() {
/*     */           public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
/* 138 */             typeEncounter.register(new InjectionListener<I>() {
/*     */                   public void afterInjection(I i) {
/* 140 */                     GuiceModule.this.eventBus.register(i);
/*     */                   }
/*     */                 });
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   public TLauncher getTlauncher() {
/*     */     try {
/* 151 */       return ((TlauncherFactory)TLauncher.getInjector().getInstance(TlauncherFactory.class)).create(this.configuration);
/* 152 */     } catch (Throwable e) {
/* 153 */       if (TlauncherUtil.getStringError(e).contains("Problem reading font data.")) {
/* 154 */         String text = Localizable.get("alert.error.font.problem").replaceAll("pageLang", 
/* 155 */             TlauncherUtil.getPageLanguage());
/* 156 */         Alert.showErrorHtml(text, 400);
/* 157 */         TLauncher.kill();
/*     */       } else {
/* 159 */         U.log(new Object[] { "can't create TLauncher instance", e });
/*     */       } 
/* 161 */       throw new NullPointerException("can't create TLauncher instance");
/*     */     } 
/*     */   }
/*     */   @Provides
/*     */   @Singleton
/*     */   public FileCacheService getFileCacheService() {
/* 167 */     return (FileCacheService)new FileCacheServiceImpl(getHttpService(), getGson(), StandardCharsets.UTF_8, 
/* 168 */         MinecraftUtil.getTLauncherFile("cache").toPath(), this.inner.getInteger("file.cache.service.time.to.life"));
/*     */   }
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   public Gson getGson() {
/* 174 */     GsonBuilder builder = (new GsonBuilder()).setPrettyPrinting();
/* 175 */     return builder.create();
/*     */   }
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   public FileMapperService createFileMapperService() {
/* 181 */     return new FileMapperService(getGson(), StandardCharsets.UTF_8, 
/* 182 */         MinecraftUtil.getWorkingDirectory().getAbsolutePath());
/*     */   }
/*     */   
/*     */   private Gson createGsonCompleteVersion() {
/* 186 */     GsonBuilder builder = new GsonBuilder();
/* 187 */     builder.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/* 188 */     builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/* 189 */     builder.registerTypeAdapter(CompleteVersion.class, new CompleteVersion.CompleteVersionSerializer());
/* 190 */     builder.registerTypeAdapter(ModpackDTO.class, new ModpackDTOTypeAdapter());
/* 191 */     builder.registerTypeAdapter(ModDTO.class, new ModDTOTypeAdapter());
/* 192 */     builder.registerTypeAdapter(MapDTO.class, new MapDTOTypeAdapter());
/* 193 */     builder.registerTypeAdapter(ResourcePackDTO.class, new ResourcePackDTOTypeAdapter());
/* 194 */     builder.enableComplexMapKeySerialization();
/* 195 */     builder.setPrettyPrinting();
/* 196 */     builder.disableHtmlEscaping();
/* 197 */     return builder.create();
/*     */   }
/*     */   
/*     */   private Gson createAddittionalFileGson() {
/* 201 */     GsonBuilder builder = new GsonBuilder();
/* 202 */     builder.registerTypeAdapter(Pattern.class, new PatternTypeAdapter());
/* 203 */     builder.setPrettyPrinting();
/* 204 */     return builder.create();
/*     */   }
/*     */   
/*     */   private Gson createProfileGson() {
/* 208 */     GsonBuilder builder = new GsonBuilder();
/* 209 */     builder.registerTypeAdapterFactory((TypeAdapterFactory)new LowerCaseEnumTypeAdapterFactory());
/* 210 */     builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
/* 211 */     builder.registerTypeAdapter(File.class, new FileTypeAdapter());
/* 212 */     builder.registerTypeAdapter(AuthenticatorDatabase.class, new AuthenticatorDatabase.Serializer());
/* 213 */     builder.registerTypeAdapter(UUIDTypeAdapter.class, new UUIDTypeAdapter());
/* 214 */     builder.setPrettyPrinting();
/* 215 */     return builder.create();
/*     */   }
/*     */   
/*     */   private int getMaxAttemts() {
/* 219 */     return DesktopUtil.numberOfAttempts(Lists.newArrayList((Object[])this.inner.getArray("net.check.urls")), this.inner
/* 220 */         .getInteger("max.attempts.per.request"), getRequestConfig(), getHttpClient());
/*     */   }
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   private RequestConfig getRequestConfig() {
/* 226 */     return RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(60000).build();
/*     */   }
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   CloseableHttpClient getHttpClient() {
/* 232 */     return HttpClientConfig.getInstanceHttpClient();
/*     */   }
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   public HttpService getHttpService() {
/* 238 */     return (HttpService)new HttpServiceImpl(null, getHttpClient(), getRequestConfig(), getMaxAttemts());
/*     */   }
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   public GsonService getGsonService() {
/* 244 */     return (GsonService)new GsonServiceImpl(createGsonCompleteVersion(), getFileCacheService(), getHttpService());
/*     */   }
/*     */   
/*     */   @Provides
/*     */   @Singleton
/*     */   public ExecutorService createExecutorService() {
/* 250 */     return Executors.newCachedThreadPool();
/*     */   }
/*     */   
/*     */   @Named("profileFileMapperService")
/*     */   @Provides
/*     */   @Singleton
/*     */   public FileMapperService createFileMapperService1() {
/* 257 */     return new FileMapperService(createProfileGson(), StandardCharsets.UTF_8, 
/* 258 */         MinecraftUtil.getWorkingDirectory().getAbsolutePath());
/*     */   }
/*     */   
/*     */   @Named("anyVersionType")
/*     */   @Provides
/*     */   @Singleton
/*     */   public NameIdDTO createAnyVersionType() {
/* 265 */     return new NameIdDTO(Long.valueOf(0L), "any");
/*     */   }
/*     */   
/*     */   @Named("anyGameVersion")
/*     */   @Provides
/*     */   @Singleton
/*     */   public GameVersionDTO createAnyGameVersion() {
/* 272 */     GameVersionDTO g = new GameVersionDTO();
/* 273 */     g.setId(Long.valueOf(0L));
/* 274 */     g.setName("modpack.version.any");
/* 275 */     return g;
/*     */   }
/*     */   
/*     */   @Named("singleDownloadExecutor")
/*     */   @Provides
/*     */   @Singleton
/*     */   public Executor createImagesThread() {
/* 282 */     return Executors.newSingleThreadExecutor();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\guice\GuiceModule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */