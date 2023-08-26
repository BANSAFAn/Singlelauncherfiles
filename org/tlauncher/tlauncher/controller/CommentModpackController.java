/*     */ package org.tlauncher.tlauncher.controller;
/*     */ 
/*     */ import by.gdev.http.download.service.GsonService;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import com.google.inject.Inject;
/*     */ import com.google.inject.Singleton;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import javax.swing.ImageIcon;
/*     */ import net.minecraft.launcher.Http;
/*     */ import org.apache.http.client.ClientProtocolException;
/*     */ import org.apache.http.client.methods.HttpDelete;
/*     */ import org.apache.http.client.methods.HttpPatch;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpPut;
/*     */ import org.apache.http.client.methods.HttpRequestBase;
/*     */ import org.tlauncher.modpack.domain.client.CommentDTO;
/*     */ import org.tlauncher.modpack.domain.client.share.GameEntitySort;
/*     */ import org.tlauncher.modpack.domain.client.share.GameType;
/*     */ import org.tlauncher.modpack.domain.client.share.TopicType;
/*     */ import org.tlauncher.modpack.domain.client.site.CommonPage;
/*     */ import org.tlauncher.tlauncher.exceptions.RequiredTLAccountException;
/*     */ import org.tlauncher.tlauncher.exceptions.SelectedAnyOneTLAccountException;
/*     */ import org.tlauncher.tlauncher.managers.ModpackManager;
/*     */ import org.tlauncher.tlauncher.minecraft.auth.Account;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.tlauncher.ui.images.ImageCache;
/*     */ import org.tlauncher.tlauncher.ui.model.GameEntityComment;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Singleton
/*     */ public class CommentModpackController
/*     */ {
/*     */   @Inject
/*     */   private GsonService gsonService;
/*  43 */   private String modpackApiURL = TLauncher.getInnerSettings().get("modpack.operation.url");
/*     */   
/*     */   @Inject
/*     */   private Gson gson;
/*     */   @Inject
/*     */   private ModpackManager modpackManager;
/*     */   
/*     */   public CommonPage<CommentDTO> getComments(Long id, Integer page, GameEntitySort sort, TopicType topicType) throws IOException {
/*  51 */     Map<String, Object> map = new HashMap<>();
/*  52 */     map.put("page", page);
/*  53 */     map.put("sort", sort.name().toString());
/*  54 */     map.put("type", topicType.name().toString());
/*  55 */     TLauncher tl = TLauncher.getInstance();
/*     */     
/*  57 */     Map<String, String> map1 = new HashMap<>();
/*     */     try {
/*  59 */       Account ac = TLauncher.getInstance().getProfileManager().findUniqueTlauncherAccount();
/*  60 */       map1.put("uuid", tl.getProfileManager().getClientToken().toString());
/*  61 */       map1.put("accessToken", ac.getAccessToken());
/*  62 */     } catch (SelectedAnyOneTLAccountException|RequiredTLAccountException selectedAnyOneTLAccountException) {}
/*     */     
/*  64 */     return (CommonPage<CommentDTO>)this.gsonService.getObjectWithoutSaving(Http.get(this.modpackApiURL + "comments/page/" + id, map), (new TypeToken<CommonPage<CommentDTO>>() {
/*     */         
/*  66 */         },  ).getType(), map1);
/*     */   }
/*     */   
/*     */   public ImageIcon loadIcon(String author) {
/*  70 */     BufferedImage b = ImageCache.loadImage(Http.constantURL(this.modpackApiURL + "user/picture?author=" + author), false);
/*     */     
/*  72 */     return Objects.isNull(b) ? null : new ImageIcon(b.getScaledInstance(35, 35, 1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void deletePosition(Long id) throws ClientProtocolException, IOException, SelectedAnyOneTLAccountException, RequiredTLAccountException {
/*  77 */     this.modpackManager.sendRequest((HttpRequestBase)new HttpDelete(), null, String.format("%scomments/%s/position", new Object[] { this.modpackApiURL, id }), null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPosition(boolean position, Long commentId) throws SelectedAnyOneTLAccountException, RequiredTLAccountException, ClientProtocolException, IOException {
/*  83 */     this.modpackManager.sendRequest((HttpRequestBase)new HttpPut(), null, 
/*  84 */         String.format("%scomments/%s/position?position=%s", new Object[] { this.modpackApiURL, commentId, Boolean.valueOf(position) }), null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String saveComment(String message, TopicType topicType, String lang, GameType gameType, Long topicPage, Long topicId) throws RequiredTLAccountException, SelectedAnyOneTLAccountException, ClientProtocolException, IOException {
/*  90 */     Map<String, Object> map = new HashMap<>();
/*  91 */     map.put("topic_type", topicType.toString().toUpperCase(Locale.ROOT));
/*  92 */     map.put("lang", lang);
/*  93 */     map.put("game_type", gameType);
/*  94 */     map.put("topic_page", topicPage);
/*  95 */     GameEntityComment g = new GameEntityComment();
/*  96 */     g.setDescription(message);
/*  97 */     g.setTopicId(topicId);
/*  98 */     return this.modpackManager.sendRequest((HttpRequestBase)new HttpPost(), g, String.format("%scomments", new Object[] { this.modpackApiURL }), map);
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(Long id, Long topicPage) throws ClientProtocolException, IOException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
/* 103 */     Map<String, Object> map = new HashMap<>();
/* 104 */     map.put("topic_page", topicPage);
/* 105 */     this.modpackManager.sendRequest((HttpRequestBase)new HttpDelete(), null, String.format("%scomments/%s", new Object[] { this.modpackApiURL, id }), map);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CommentDTO update(Long id, GameEntityComment g) throws ClientProtocolException, IOException, RequiredTLAccountException, SelectedAnyOneTLAccountException {
/* 111 */     return (CommentDTO)this.gson.fromJson(this.modpackManager
/* 112 */         .sendRequest((HttpRequestBase)new HttpPatch(), g, String.format("%scomments/%s", new Object[] { this.modpackApiURL, id }), null), CommentDTO.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\controller\CommentModpackController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */