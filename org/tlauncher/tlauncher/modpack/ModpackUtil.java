/*     */ package org.tlauncher.tlauncher.modpack;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.launcher.versions.CompleteVersion;
/*     */ import net.minecraft.launcher.versions.Version;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDependencyDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.DependencyType;
/*     */ import org.tlauncher.modpack.domain.client.share.ForgeStringComparator;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.loc.Localizable;
/*     */ import org.tlauncher.util.FileUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModpackUtil
/*     */ {
/*     */   public static List<? extends VersionDTO> sortByDate(List<VersionDTO> versions) {
/*  44 */     List<VersionDTO> sortList = new ArrayList<>(versions);
/*  45 */     sortList.sort((o1, o2) -> o2.getUpdateDate().compareTo(o1.getUpdateDate()));
/*  46 */     return sortList;
/*     */   }
/*     */   
/*     */   public static Path getPathByVersion(Version version, String... paths) {
/*  50 */     StringBuilder builder = new StringBuilder();
/*  51 */     builder.append("versions").append("/")
/*  52 */       .append(version.getID());
/*  53 */     for (String line : paths) {
/*  54 */       builder.append("/").append(line);
/*     */     }
/*  56 */     return FileUtil.getRelative(builder.toString());
/*     */   }
/*     */   
/*     */   public static Path getPathByVersion(CompleteVersion version) {
/*  60 */     return getPathByVersion((Version)version, new String[] { "" });
/*     */   }
/*     */   
/*     */   public static String getLatestGameVersion(Set<String> c) {
/*  64 */     if (c.isEmpty())
/*  65 */       return null; 
/*  66 */     ArrayList<String> list = Lists.newArrayList(c);
/*  67 */     list.sort((Comparator<? super String>)new ForgeStringComparator());
/*  68 */     return list.get(0);
/*     */   }
/*     */   
/*     */   public static void extractIncompatible(GameEntityDTO en, Set<Long> set) {
/*  72 */     if (en.getDependencies() == null)
/*     */       return; 
/*  74 */     for (GameEntityDependencyDTO d : en.getDependencies()) {
/*  75 */       if (d.getDependencyType() == DependencyType.INCOMPATIBLE) {
/*  76 */         set.add(d.getGameEntityId());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Set<Long> getAllModpackIds(ModpackDTO m) {
/*  82 */     Set<Long> set = new HashSet<>();
/*  83 */     if (m.getId() != null)
/*  84 */       set.add(m.getId()); 
/*  85 */     for (GameType t : GameType.getSubEntities()) {
/*  86 */       List<? extends GameEntityDTO> list = ((ModpackVersionDTO)m.getVersion()).getByType(t);
/*  87 */       for (int i = 0; i < list.size(); i++) {
/*  88 */         if (Objects.nonNull(((GameEntityDTO)list.get(i)).getId())) {
/*  89 */           set.add(((GameEntityDTO)list.get(i)).getId());
/*     */         }
/*     */       } 
/*     */     } 
/*  93 */     return set;
/*     */   }
/*     */   
/*     */   public static List<String> getPictureURL(Long id, String type) {
/*  97 */     return (List<String>)Arrays.<String>stream(TLauncher.getInnerSettings().getArray("file.server"))
/*  98 */       .map(s -> s + "/pictures/" + type + "/" + id + ".png").collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public static StringBuilder buildMessage(List<GameEntityDTO> list) {
/* 102 */     StringBuilder b = new StringBuilder();
/* 103 */     for (GameEntityDTO dependency : list)
/* 104 */       b.append(dependency.getName())
/* 105 */         .append("(")
/* 106 */         .append(Localizable.get("modpack.button." + GameType.create(dependency.getClass())))
/* 107 */         .append(")").append(" "); 
/* 108 */     return b;
/*     */   }
/*     */   
/*     */   public static Path getPath(CompleteVersion v, GameType type) {
/* 112 */     switch (type) {
/*     */       case RESOURCEPACK:
/* 114 */         return getPathByVersion((Version)v, new String[] { "resourcepacks" });
/*     */       case MOD:
/* 116 */         return getPathByVersion((Version)v, new String[] { "mods" });
/*     */       case MAP:
/* 118 */         return getPathByVersion((Version)v, new String[] { "saves" });
/*     */       case SHADERPACK:
/* 120 */         return getPathByVersion((Version)v, new String[] { "shaderpacks" });
/*     */     } 
/* 122 */     throw new RuntimeException("not proper type");
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean useSkinMod(CompleteVersion version) {
/* 127 */     for (ModDTO m : ((ModpackVersionDTO)version.getModpack().getVersion()).getMods()) {
/* 128 */       if (ModDTO.TL_SKIN_CAPE_ID.equals(m.getId()))
/* 129 */         return true; 
/* 130 */     }  return false;
/*     */   }
/*     */   
/*     */   public static void addOrReplaceShaderConfig(CompleteVersion v, String field, String filename) throws IOException {
/* 134 */     Path p = getPathByVersion(v).resolve("optionsshaders.txt");
/* 135 */     if (Files.notExists(p, new java.nio.file.LinkOption[0]))
/* 136 */       Files.createFile(p, (FileAttribute<?>[])new FileAttribute[0]); 
/* 137 */     Properties properties = new Properties();
/* 138 */     FileInputStream inputStream = new FileInputStream(p.toFile());
/* 139 */     properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
/* 140 */     IOUtils.closeQuietly(inputStream);
/* 141 */     properties.put(field, filename);
/* 142 */     FileWriter fileWriter = new FileWriter(p.toFile());
/* 143 */     properties.store(fileWriter, "");
/* 144 */     IOUtils.closeQuietly(fileWriter);
/*     */   }
/*     */   
/*     */   public static String readShaderpackConfigField(CompleteVersion v, String name) throws IOException {
/* 148 */     Path p = getPathByVersion(v).resolve("optionsshaders.txt");
/* 149 */     Properties properties = new Properties();
/* 150 */     FileInputStream inputStream = new FileInputStream(p.toFile());
/* 151 */     properties.load(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
/* 152 */     IOUtils.closeQuietly(inputStream);
/* 153 */     return properties.getProperty(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\modpack\ModpackUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */