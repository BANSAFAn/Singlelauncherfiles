/*     */ package org.tlauncher.modpack.domain.client.share;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonCreator;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonValue;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.stream.Collectors;
/*     */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*     */ import org.tlauncher.modpack.domain.client.MapDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModDTO;
/*     */ import org.tlauncher.modpack.domain.client.ModpackDTO;
/*     */ import org.tlauncher.modpack.domain.client.ResourcePackDTO;
/*     */ import org.tlauncher.modpack.domain.client.ShaderpackDTO;
/*     */ import org.tlauncher.modpack.domain.client.version.ModVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
/*     */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*     */ 
/*     */ 
/*     */ @JsonFormat(shape = JsonFormat.Shape.OBJECT)
/*     */ public enum GameType
/*     */ {
/*  24 */   MAP, MOD, MODPACK, RESOURCEPACK, NOT_MODPACK, SHADERPACK;
/*     */   @JsonValue
/*     */   public String toLowerCase() {
/*  27 */     return name().toLowerCase(Locale.ROOT);
/*     */   }
/*     */   
/*     */   public String toWebParam() {
/*  31 */     return name().toUpperCase(Locale.ROOT);
/*     */   }
/*     */   
/*     */   @JsonCreator
/*     */   public static GameType create(String value) {
/*  36 */     return valueOf(value.toUpperCase());
/*     */   }
/*     */   
/*     */   public static Class<? extends GameEntityDTO> createDTO(GameType type) {
/*  40 */     switch (type) {
/*     */       case null:
/*  42 */         return (Class)MapDTO.class;
/*     */       case MOD:
/*  44 */         return (Class)ModDTO.class;
/*     */       case RESOURCEPACK:
/*  46 */         return (Class)ResourcePackDTO.class;
/*     */       case MODPACK:
/*  48 */         return (Class)ModpackDTO.class;
/*     */       case SHADERPACK:
/*  50 */         return (Class)ShaderpackDTO.class;
/*     */     } 
/*  52 */     throw new NullPointerException();
/*     */   }
/*     */   
/*     */   public static Class<? extends VersionDTO> createVersionDTO(GameType type) {
/*  56 */     switch (type) {
/*     */       case null:
/*  58 */         return VersionDTO.class;
/*     */       case MOD:
/*  60 */         return (Class)ModVersionDTO.class;
/*     */       case RESOURCEPACK:
/*  62 */         return VersionDTO.class;
/*     */       case MODPACK:
/*  64 */         return (Class)ModpackVersionDTO.class;
/*     */       case SHADERPACK:
/*  66 */         return VersionDTO.class;
/*     */     } 
/*  68 */     throw new NullPointerException();
/*     */   }
/*     */ 
/*     */   
/*     */   public static GameType create(Class<? extends GameEntityDTO> c) {
/*  73 */     return create(c.getSimpleName().replaceAll("DTO", ""));
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<GameType> getSubEntities() {
/*  78 */     return new ArrayList<GameType>()
/*     */       {
/*     */       
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> getPluralWords() {
/*  87 */     return (List<String>)getExistedGameTypes().stream().map(GameType::getPluralStringWord)
/*  88 */       .collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public static String getPluralStringWord(GameType type) {
/*  92 */     return String.valueOf(type.name().toLowerCase(Locale.ROOT)) + "s";
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<GameType> getExistedGameTypes() {
/*  97 */     return new ArrayList<GameType>() {
/*     */       
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\GameType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */