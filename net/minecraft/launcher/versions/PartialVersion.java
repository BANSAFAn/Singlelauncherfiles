/*     */ package net.minecraft.launcher.versions;
/*     */ 
/*     */ import java.util.Date;
/*     */ import net.minecraft.launcher.updater.VersionList;
/*     */ import org.tlauncher.tlauncher.repository.Repo;
/*     */ 
/*     */ public class PartialVersion
/*     */   extends AbstractVersion
/*     */   implements Cloneable
/*     */ {
/*     */   private String id;
/*     */   private String jar;
/*     */   private Date time;
/*     */   private Date releaseTime;
/*     */   private ReleaseType type;
/*     */   private Repo source;
/*     */   private VersionList list;
/*     */   
/*     */   public String getID() {
/*  20 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getJar() {
/*  25 */     return this.jar;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setID(String id) {
/*  30 */     this.id = id;
/*     */   }
/*     */ 
/*     */   
/*     */   public ReleaseType getReleaseType() {
/*  35 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Repo getSource() {
/*  40 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSource(Repo repo) {
/*  45 */     if (repo == null) {
/*  46 */       throw new NullPointerException();
/*     */     }
/*  48 */     this.source = repo;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdatedTime() {
/*  53 */     return this.time;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getReleaseTime() {
/*  58 */     return this.releaseTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public VersionList getVersionList() {
/*  63 */     return this.list;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVersionList(VersionList list) {
/*  68 */     if (list == null) {
/*  69 */       throw new NullPointerException();
/*     */     }
/*  71 */     this.list = list;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  76 */     if (this == o)
/*  77 */       return true; 
/*  78 */     if (o == null)
/*  79 */       return false; 
/*  80 */     if (hashCode() == o.hashCode()) {
/*  81 */       return true;
/*     */     }
/*  83 */     if (!(o instanceof Version)) {
/*  84 */       return false;
/*     */     }
/*  86 */     Version compare = (Version)o;
/*  87 */     if (compare.getID() == null) {
/*  88 */       return false;
/*     */     }
/*  90 */     return compare.getID().equals(this.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     return getClass().getSimpleName() + "{id='" + this.id + "', time=" + this.time + ", release=" + this.releaseTime + ", type=" + this.type + ", source=" + this.source + ", list=" + this.list + "}";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isActivateSkinCapeForUserVersion() {
/* 101 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\PartialVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */