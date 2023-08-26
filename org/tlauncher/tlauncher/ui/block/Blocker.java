/*     */ package org.tlauncher.tlauncher.ui.block;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Blocker {
/*  12 */   private static final Map<Blockable, List<Object>> blockMap = new Hashtable<>();
/*  13 */   public static final Object UNIVERSAL_UNBLOCK = "universal block";
/*  14 */   public static final Object WEAK_BLOCK = "weak";
/*     */   
/*     */   private static void add(Blockable blockable) {
/*  17 */     if (blockable == null) {
/*  18 */       throw new NullPointerException();
/*     */     }
/*  20 */     blockMap.put(blockable, 
/*  21 */         Collections.synchronizedList(new ArrayList()));
/*     */   }
/*     */   
/*     */   public static void cleanUp(Blockable blockable) {
/*  25 */     if (blockable == null) {
/*  26 */       throw new NullPointerException();
/*     */     }
/*  28 */     blockMap.remove(blockable);
/*     */   }
/*     */   
/*     */   public static boolean contains(Blockable blockable) {
/*  32 */     if (blockable == null) {
/*  33 */       throw new NullPointerException();
/*     */     }
/*  35 */     return blockMap.containsKey(blockable);
/*     */   }
/*     */   
/*     */   public static void block(Blockable blockable, Object reason) {
/*  39 */     if (blockable == null) {
/*     */       return;
/*     */     }
/*  42 */     if (reason == null) {
/*  43 */       throw new NullPointerException("Reason is NULL!");
/*     */     }
/*  45 */     if (!blockMap.containsKey(blockable)) {
/*  46 */       add(blockable);
/*     */     }
/*  48 */     List<Object> reasons = blockMap.get(blockable);
/*     */     
/*  50 */     if (reasons.contains(reason)) {
/*     */       return;
/*     */     }
/*  53 */     boolean blocked = !reasons.isEmpty();
/*     */     
/*  55 */     reasons.add(reason);
/*     */     
/*  57 */     if (blocked) {
/*     */       return;
/*     */     }
/*  60 */     blockable.block(reason);
/*     */   }
/*     */   
/*     */   public static void block(Object reason, Blockable... blockables) {
/*  64 */     if (blockables == null || reason == null) {
/*  65 */       throw new NullPointerException("Blockables are NULL: " + ((blockables == null) ? 1 : 0) + ", reason is NULL: " + ((reason == null) ? 1 : 0));
/*     */     }
/*     */ 
/*     */     
/*  69 */     for (Blockable blockable : blockables)
/*  70 */       block(blockable, reason); 
/*     */   }
/*     */   
/*     */   public static boolean unblock(Blockable blockable, Object reason) {
/*  74 */     if (blockable == null) {
/*  75 */       return false;
/*     */     }
/*  77 */     if (reason == null) {
/*  78 */       throw new NullPointerException("Reason is NULL!");
/*     */     }
/*  80 */     if (!blockMap.containsKey(blockable)) {
/*  81 */       return true;
/*     */     }
/*  83 */     List<Object> reasons = blockMap.get(blockable);
/*     */     
/*  85 */     reasons.remove(reason);
/*     */     
/*  87 */     if (reason.equals(UNIVERSAL_UNBLOCK))
/*  88 */       reasons.clear(); 
/*  89 */     if (reasons.contains(WEAK_BLOCK))
/*  90 */       reasons.remove(WEAK_BLOCK); 
/*  91 */     if (!reasons.isEmpty()) {
/*  92 */       return false;
/*     */     }
/*  94 */     blockable.unblock(reason);
/*  95 */     return true;
/*     */   }
/*     */   
/*     */   public static void unblock(Object reason, Blockable... blockables) {
/*  99 */     if (blockables == null || reason == null) {
/* 100 */       throw new NullPointerException("Blockables are NULL: " + ((blockables == null) ? 1 : 0) + ", reason is NULL: " + ((reason == null) ? 1 : 0));
/*     */     }
/*     */ 
/*     */     
/* 104 */     for (Blockable blockable : blockables) {
/* 105 */       unblock(blockable, reason);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setBlocked(Blockable blockable, Object reason, boolean blocked) {
/* 110 */     if (blocked) {
/* 111 */       block(blockable, reason);
/*     */     } else {
/* 113 */       unblock(blockable, reason);
/*     */     } 
/*     */   }
/*     */   public static boolean isBlocked(Blockable blockable) {
/* 117 */     if (blockable == null) {
/* 118 */       throw new NullPointerException();
/*     */     }
/* 120 */     if (!blockMap.containsKey(blockable)) {
/* 121 */       return false;
/*     */     }
/* 123 */     return !((List)blockMap.get(blockable)).isEmpty();
/*     */   }
/*     */   
/*     */   public static List<Object> getBlockList(Blockable blockable) {
/* 127 */     if (blockable == null) {
/* 128 */       throw new NullPointerException();
/*     */     }
/* 130 */     if (!blockMap.containsKey(blockable)) {
/* 131 */       add(blockable);
/*     */     }
/* 133 */     return Collections.unmodifiableList(blockMap.get(blockable));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void blockComponents(Object reason, Component... components) {
/* 140 */     if (components == null) {
/* 141 */       throw new NullPointerException("Components is NULL!");
/*     */     }
/* 143 */     if (reason == null) {
/* 144 */       throw new NullPointerException("Reason is NULL!");
/*     */     }
/* 146 */     for (Component component : components) {
/* 147 */       if (component instanceof Blockable) {
/* 148 */         block((Blockable)component, reason);
/* 149 */       } else if (!(component instanceof Unblockable)) {
/* 150 */         component.setEnabled(false);
/* 151 */         if (component instanceof Container)
/* 152 */           blockComponents((Container)component, reason); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public static void blockComponents(Container container, Object reason) {
/* 157 */     blockComponents(reason, container.getComponents());
/*     */   }
/*     */   
/*     */   public static void unblockComponents(Object reason, Component... components) {
/* 161 */     if (components == null) {
/* 162 */       throw new NullPointerException("Components is NULL!");
/*     */     }
/* 164 */     if (reason == null) {
/* 165 */       throw new NullPointerException("Reason is NULL!");
/*     */     }
/* 167 */     for (Component component : components) {
/* 168 */       if (component instanceof Blockable) {
/* 169 */         unblock((Blockable)component, reason);
/* 170 */       } else if (!(component instanceof Unblockable)) {
/* 171 */         component.setEnabled(true);
/* 172 */         if (component instanceof Container)
/* 173 */           unblockComponents((Container)component, reason); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public static void unblockComponents(Container container, Object reason) {
/* 178 */     unblockComponents(reason, container.getComponents());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\block\Blocker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */