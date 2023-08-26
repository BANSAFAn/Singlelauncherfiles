/*     */ package net.minecraft.launcher.versions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ public enum ReleaseType {
/*   9 */   RELEASE("release", false, true),
/*  10 */   SNAPSHOT("snapshot"),
/*     */   
/*  12 */   MODIFIED("modified", true, true),
/*     */   
/*  14 */   OLD_BETA("old-beta"),
/*  15 */   OLD_ALPHA("old-alpha"),
/*  16 */   PENDING("pending", false, false),
/*  17 */   UNKNOWN("unknown", false, false);
/*     */   private static final Map<String, ReleaseType> lookup;
/*     */   private static final List<ReleaseType> defaultTypes;
/*     */   private static final List<ReleaseType> definableTypes;
/*     */   private final String name;
/*     */   private final boolean isDefinable;
/*     */   private final boolean isDefault;
/*     */   
/*     */   static {
/*  26 */     HashMap<String, ReleaseType> types = new HashMap<>((values()).length);
/*  27 */     ArrayList<ReleaseType> deflTypes = new ArrayList<>(), defnTypes = new ArrayList<>();
/*     */     
/*  29 */     for (ReleaseType type : values()) {
/*  30 */       types.put(type.getName(), type);
/*     */       
/*  32 */       if (type.isDefault()) {
/*  33 */         deflTypes.add(type);
/*     */       }
/*  35 */       if (type.isDefinable()) {
/*  36 */         defnTypes.add(type);
/*     */       }
/*     */     } 
/*  39 */     lookup = Collections.unmodifiableMap(types);
/*  40 */     defaultTypes = Collections.unmodifiableList(deflTypes);
/*  41 */     definableTypes = Collections.unmodifiableList(defnTypes);
/*     */   }
/*     */   
/*     */   ReleaseType(String name, boolean isDefinable, boolean isDefault) {
/*  45 */     this.name = name;
/*  46 */     this.isDefinable = isDefinable;
/*  47 */     this.isDefault = isDefault;
/*     */   }
/*     */   
/*     */   String getName() {
/*  51 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isDefault() {
/*  55 */     return this.isDefault;
/*     */   }
/*     */   
/*     */   public boolean isDefinable() {
/*  59 */     return this.isDefinable;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  64 */     return super.toString().toLowerCase();
/*     */   }
/*     */   
/*     */   public static ReleaseType getByName(String name) {
/*  68 */     return lookup.get(name);
/*     */   }
/*     */   
/*     */   public static Collection<ReleaseType> valuesCollection() {
/*  72 */     return lookup.values();
/*     */   }
/*     */   
/*     */   public static List<ReleaseType> getDefault() {
/*  76 */     return defaultTypes;
/*     */   }
/*     */   
/*     */   public static List<ReleaseType> getDefinable() {
/*  80 */     return definableTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum SubType
/*     */   {
/*  88 */     OLD_RELEASE("old_release") { private final Date marker;
/*     */       
/*     */       SubType(String name) {
/*  91 */         GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
/*  92 */         calendar.set(2013, 3, 20, 15, 0);
/*  93 */         this.marker = calendar.getTime();
/*     */       }
/*     */       
/*     */       public boolean isSubType(Version version) {
/*     */         try {
/*  98 */           return (!version.getReleaseType().toString().startsWith("old") && version
/*  99 */             .getReleaseTime().getTime() >= 0L && version.getReleaseTime().before(this.marker));
/* 100 */         } catch (NullPointerException e) {
/* 101 */           U.log(new Object[] { "nullpointer is sub type in version" + version.getID() });
/*     */           
/* 103 */           return false;
/*     */         } 
/*     */       } },
/* 106 */     REMOTE("remote")
/*     */     {
/*     */       public boolean isSubType(Version version) {
/* 109 */         return (version.getSource() != ClientInstanceRepo.LOCAL_VERSION_REPO);
/*     */       } };
/*     */     private static final Map<String, SubType> lookup;
/*     */     private static final List<SubType> defaultSubTypes;
/*     */     private final String name;
/*     */     private final boolean isDefault;
/*     */     
/*     */     static {
/* 117 */       HashMap<String, SubType> subTypes = new HashMap<>((values()).length);
/* 118 */       ArrayList<SubType> defSubTypes = new ArrayList<>();
/*     */       
/* 120 */       for (SubType subType : values()) {
/* 121 */         subTypes.put(subType.getName(), subType);
/*     */         
/* 123 */         if (subType.isDefault()) {
/* 124 */           defSubTypes.add(subType);
/*     */         }
/*     */       } 
/* 127 */       lookup = Collections.unmodifiableMap(subTypes);
/* 128 */       defaultSubTypes = Collections.unmodifiableList(defSubTypes);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     SubType(String name, boolean isDefault) {
/* 135 */       this.name = name;
/* 136 */       this.isDefault = isDefault;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 144 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean isDefault() {
/* 148 */       return this.isDefault;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 153 */       return super.toString().toLowerCase();
/*     */     }
/*     */     
/*     */     public static SubType getByName(String name) {
/* 157 */       return lookup.get(name);
/*     */     }
/*     */     
/*     */     public static Collection<SubType> valuesCollection() {
/* 161 */       return lookup.values();
/*     */     }
/*     */     
/*     */     public static List<SubType> getDefault() {
/* 165 */       return defaultSubTypes;
/*     */     }
/*     */     
/*     */     public static SubType get(Version version) {
/* 169 */       for (SubType subType : values()) {
/* 170 */         if (subType.isSubType(version))
/* 171 */           return subType; 
/* 172 */       }  return null;
/*     */     }
/*     */     
/*     */     public abstract boolean isSubType(Version param1Version);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\ReleaseType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */