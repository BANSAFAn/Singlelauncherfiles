/*    */ package org.tlauncher.util.gson.serializer;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import org.tlauncher.modpack.domain.client.GameEntityDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.CategoryDTO;
/*    */ import org.tlauncher.modpack.domain.client.share.JavaEnum;
/*    */ import org.tlauncher.modpack.domain.client.version.VersionDTO;
/*    */ 
/*    */ public class ElementCollectionsPool {
/* 15 */   private static final Map<String, String> stringElements = new HashMap<>();
/* 16 */   private static final Map<Set<String>, Set<String>> set = new HashMap<>();
/*    */   
/* 18 */   private static final Map<List<JavaEnum>, List<JavaEnum>> javaEnums = new HashMap<>();
/* 19 */   private static final Map<List<CategoryDTO>, List<CategoryDTO>> categories = new HashMap<>();
/*    */   
/*    */   public static void fill(GameEntityDTO en) {
/* 22 */     if (en.getVersions() == null)
/*    */       return; 
/* 24 */     for (VersionDTO v : en.getVersions()) {
/* 25 */       if (v.getGameVersions() != null) {
/* 26 */         Set<String> games = new HashSet<>();
/* 27 */         for (String s : v.getGameVersions()) {
/* 28 */           if (!stringElements.containsKey(s)) {
/* 29 */             stringElements.put(s, s);
/* 30 */             s = stringElements.get(s);
/*    */           } 
/* 32 */           games.add(stringElements.get(s));
/*    */         } 
/* 34 */         if (set.containsKey(games)) {
/* 35 */           v.setGameVersions(Lists.newArrayList(set.get(games)));
/*    */         } else {
/* 37 */           v.setGameVersions(Lists.newArrayList(games));
/* 38 */           set.put(games, games);
/*    */         } 
/*    */       } 
/*    */       
/* 42 */       List<JavaEnum> java = v.getJavaVersions();
/* 43 */       if (java != null) {
/* 44 */         if (javaEnums.containsKey(java)) {
/* 45 */           v.setJavaVersions(javaEnums.get(java));
/*    */         } else {
/* 47 */           javaEnums.put(java, java);
/*    */         } 
/*    */       }
/* 50 */       List<CategoryDTO> listCategories = en.getCategories();
/* 51 */       listCategories.removeIf(e -> Objects.isNull(e));
/* 52 */       if (listCategories != null) {
/* 53 */         if (categories.containsKey(listCategories)) {
/* 54 */           en.setCategories(categories.get(listCategories)); continue;
/*    */         } 
/* 56 */         categories.put(listCategories, listCategories);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\gson\serializer\ElementCollectionsPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */