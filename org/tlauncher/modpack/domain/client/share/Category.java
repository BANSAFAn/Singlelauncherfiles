/*     */ package org.tlauncher.modpack.domain.client.share;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonCreator;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonValue;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ 
/*     */ @JsonFormat(shape = JsonFormat.Shape.OBJECT)
/*     */ public enum Category
/*     */ {
/*  13 */   ALL, MAGIC,
/*  14 */   ADVENTURE_RPG,
/*  15 */   COSMETIC, ARMOR_WEAPONS_TOOLS, TECHNOLOGY, MAP_INFORMATION, LIBRARY_API, CHITA,
/*  16 */   ADDONS_FORESTRY, TECHNOLOGY_FARMING,
/*  17 */   ADDONS_THERMALEXPANSION, TECHNOLOGY_ENERGY, WORLD_MOBS, WORLD_BIOMES,
/*  18 */   SERVER_UTILITY, BLOOD_MAGIC, WORLD_DIMENSIONS, ADDONS_BUILDCRAFT, APPLIED_ENERGISTICS_2,
/*  19 */   TECHNOLOGY_ITEM_FLUID_ENERGY_TRANSPORT, TECHNOLOGY_PLAYER_TRANSPORT, MC_MISCELLANEOUS,
/*  20 */   WORLD_ORES_RESOURCES, MC_FOOD, REDSTONE, MC_ADDONS, WORLD_STRUCTURES, ADDONS_INDUSTRIALCRAFT,
/*  21 */   WORLD_GEN, TECHNOLOGY_PROCESSING, ADDONS_THAUMCRAFT, ADDONS_TINKERS_CONSTRUCT, TECHNOLOGY_GENETICS,
/*  22 */   ADVENTURE,
/*  23 */   CREATION, GAME_MAP, PARKOUR, SURVIVAL, PUZZLE, MODDED_WORLD,
/*  24 */   SIXTEEN_X,
/*  25 */   THIRTY_TWO_X, SIXTY_FOUR_X, ONE_TWENTY_EIGHT_X, TWO_FIFTY_SIX_X, FIVE_TWELVE_X_AND_BEYOND,
/*  26 */   ANIMATED, MEDIEVAL, MOD_SUPPORT, MODERN, PHOTO_REALISTIC, STEAMPUNK, TRADITIONAL, MISCELLANEOUS,
/*  27 */   ADVENTURE_AND_RPG,
/*  28 */   COMBAT_PVP, FTB_OFFICIAL_PACK, HARDCORE, MAP_BASED, MINI_GAME, MULTIPLAYER, QUESTS, SCI_FI, TECH, EXPLORATION, EXTRA_LARGE, SMALL_LIGHT,
/*     */   
/*  30 */   LUCKY_BLOCKS,
/*  31 */   CRAFTTWEAKER, HARDCORE_QUESTING_MODE, CONFIGURATION, SCRIPTS, RECIPES, BUILDING_GADGETS, PROGRESSION, GUIDEBOOK,
/*  32 */   REALISTIC, LAG_LESS, LOW_END, PSYCHEDELIC, CUSTOMIZATION, SHADERPACKS, STORAGE, FABRIC, TWITCH_INTEGRATION, SKYBLOCK;
/*     */   public static final Map<Category, Integer> common;
/*     */   
/*     */   @JsonCreator
/*     */   public static Category createCategory(String value) {
/*  37 */     return valueOf(value.toUpperCase());
/*     */   }
/*     */   public static final Map<Category, Integer> shaderMap;
/*     */   
/*     */   @JsonValue
/*     */   public String toString() {
/*  43 */     return name().toLowerCase(Locale.ROOT);
/*     */   }
/*     */   static {
/*  46 */     common = new HashMap<Category, Integer>()
/*     */       {
/*     */       
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     shaderMap = new HashMap<Category, Integer>()
/*     */       {
/*     */       
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Category[] getCategories(GameType type) {
/*  97 */     switch (type) {
/*     */       case MODPACK:
/*  99 */         return new Category[] { ALL, ADVENTURE_AND_RPG, COMBAT_PVP, EXPLORATION, EXTRA_LARGE, FTB_OFFICIAL_PACK, HARDCORE, MAGIC, MAP_BASED, 
/* 100 */             MINI_GAME, MULTIPLAYER, QUESTS, SCI_FI, SMALL_LIGHT, TECH, SKYBLOCK };
/*     */       
/*     */       case MOD:
/* 103 */         return new Category[] { ALL, ADVENTURE_RPG, ARMOR_WEAPONS_TOOLS, COSMETIC, MC_FOOD, MAGIC, MAP_INFORMATION, 
/* 104 */             REDSTONE, SERVER_UTILITY, 
/* 105 */             TECHNOLOGY, 
/* 106 */             TECHNOLOGY_ENERGY, TECHNOLOGY_ITEM_FLUID_ENERGY_TRANSPORT, TECHNOLOGY_FARMING, TECHNOLOGY_GENETICS, 
/* 107 */             TECHNOLOGY_PLAYER_TRANSPORT, TECHNOLOGY_PROCESSING, 
/* 108 */             WORLD_GEN, 
/* 109 */             WORLD_BIOMES, WORLD_DIMENSIONS, WORLD_MOBS, WORLD_ORES_RESOURCES, WORLD_STRUCTURES, 
/* 110 */             MC_ADDONS, 
/* 111 */             APPLIED_ENERGISTICS_2, BLOOD_MAGIC, ADDONS_BUILDCRAFT, ADDONS_FORESTRY, ADDONS_INDUSTRIALCRAFT, ADDONS_THAUMCRAFT, 
/* 112 */             ADDONS_THERMALEXPANSION, ADDONS_TINKERS_CONSTRUCT, 
/* 113 */             LIBRARY_API, MC_MISCELLANEOUS, CHITA, FABRIC, TWITCH_INTEGRATION, STORAGE };
/*     */       case null:
/* 115 */         return new Category[] { ALL, ADVENTURE, CREATION, GAME_MAP, MODDED_WORLD, PARKOUR, 
/* 116 */             PUZZLE, SURVIVAL };
/*     */       case RESOURCEPACK:
/* 118 */         return new Category[] { ALL, SIXTEEN_X, THIRTY_TWO_X, SIXTY_FOUR_X, ONE_TWENTY_EIGHT_X, TWO_FIFTY_SIX_X, FIVE_TWELVE_X_AND_BEYOND, ANIMATED, MEDIEVAL, MOD_SUPPORT, MODERN, 
/* 119 */             PHOTO_REALISTIC, STEAMPUNK, TRADITIONAL, MISCELLANEOUS };
/*     */       case SHADERPACK:
/* 121 */         return new Category[] { ALL, SHADERPACKS, REALISTIC, LAG_LESS, LOW_END, PSYCHEDELIC, 
/* 122 */             CUSTOMIZATION, CONFIGURATION, LUCKY_BLOCKS, GUIDEBOOK, QUESTS, HARDCORE_QUESTING_MODE, 
/* 123 */             PROGRESSION, SCRIPTS, CRAFTTWEAKER, RECIPES, WORLD_GEN, BUILDING_GADGETS };
/*     */     } 
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<Category, Integer> getSubCategories(GameType type) {
/* 131 */     switch (type) {
/*     */       case null:
/*     */       case MOD:
/*     */       case MODPACK:
/*     */       case RESOURCEPACK:
/* 136 */         return common;
/*     */       case SHADERPACK:
/* 138 */         return shaderMap;
/*     */     } 
/*     */     
/* 141 */     return new HashMap<>();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\modpack\domain\client\share\Category.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */