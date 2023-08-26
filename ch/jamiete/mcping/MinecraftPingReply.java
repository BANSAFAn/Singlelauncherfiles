/*     */ package ch.jamiete.mcping;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Base64;
/*     */ import java.util.List;
/*     */ import javax.imageio.ImageIO;
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
/*     */ public class MinecraftPingReply
/*     */ {
/*     */   private Players players;
/*     */   private Version version;
/*     */   private String favicon;
/*     */   
/*     */   public Players getPlayers() {
/*  58 */     return this.players;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Version getVersion() {
/*  65 */     return this.version;
/*     */   }
/*     */   
/*     */   public BufferedImage getFavicon() throws IOException {
/*  69 */     String part = this.favicon.split(",")[1];
/*  70 */     byte[] imageByte = Base64.getDecoder().decode(part.replaceAll("\\n", "").getBytes(StandardCharsets.UTF_8));
/*  71 */     ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
/*  72 */     return ImageIO.read(bis);
/*     */   }
/*     */   
/*     */   public String getFaviconString() {
/*  76 */     return this.favicon;
/*     */   }
/*     */ 
/*     */   
/*     */   public class Description
/*     */   {
/*     */     private String text;
/*     */ 
/*     */     
/*     */     public String getText() {
/*  86 */       return this.text;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class Players
/*     */   {
/*     */     private int max;
/*     */     
/*     */     private int online;
/*     */     private List<MinecraftPingReply.Player> sample;
/*     */     
/*     */     public int getMax() {
/*  99 */       return this.max;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getOnline() {
/* 106 */       return this.online;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<MinecraftPingReply.Player> getSample() {
/* 113 */       return this.sample;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class Player
/*     */   {
/*     */     private String name;
/*     */     
/*     */     private String id;
/*     */     
/*     */     public String getName() {
/* 125 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getId() {
/* 132 */       return this.id;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class Version
/*     */   {
/*     */     private String name;
/*     */     
/*     */     private int protocol;
/*     */ 
/*     */     
/*     */     public String getName() {
/* 145 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getProtocol() {
/* 152 */       return this.protocol;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\jamiete\mcping\MinecraftPingReply.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */