/*     */ package org.tlauncher.tlauncher.managers;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.tlauncher.tlauncher.component.ComponentDependence;
/*     */ import org.tlauncher.tlauncher.component.InterruptibleComponent;
/*     */ import org.tlauncher.tlauncher.component.LauncherComponent;
/*     */ import org.tlauncher.tlauncher.component.RefreshableComponent;
/*     */ import org.tlauncher.tlauncher.rmo.TLauncher;
/*     */ import org.tlauncher.util.async.LoopedThread;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComponentManager
/*     */ {
/*     */   private TLauncher tlauncher;
/*     */   private final List<LauncherComponent> components;
/*     */   private final ComponentManagerRefresher refresher;
/*     */   
/*     */   public ComponentManager(TLauncher tLauncher) {
/*  24 */     this.tlauncher = tLauncher;
/*  25 */     this
/*  26 */       .components = Collections.synchronizedList(new ArrayList<>());
/*     */     
/*  28 */     this.refresher = new ComponentManagerRefresher(this);
/*  29 */     this.refresher.start();
/*     */   }
/*     */   
/*     */   public TLauncher getLauncher() {
/*  33 */     return this.tlauncher;
/*     */   }
/*     */   
/*     */   public <T extends LauncherComponent> T loadComponent(Class<T> classOfT) {
/*  37 */     if (classOfT == null) {
/*  38 */       throw new NullPointerException();
/*     */     }
/*  40 */     if (hasComponent(classOfT)) {
/*  41 */       return getComponent(classOfT);
/*     */     }
/*     */     
/*  44 */     ComponentDependence dependence = classOfT.<ComponentDependence>getAnnotation(ComponentDependence.class);
/*  45 */     if (dependence != null)
/*  46 */       for (Class<?> requiredClass : dependence.value()) {
/*  47 */         rawLoadComponent(requiredClass);
/*     */       } 
/*  49 */     return (T)rawLoadComponent(classOfT);
/*     */   } private <T> T rawLoadComponent(Class<T> classOfT) {
/*     */     Constructor<T> constructor;
/*     */     T instance;
/*  53 */     if (classOfT == null) {
/*  54 */       throw new NullPointerException();
/*     */     }
/*  56 */     if (!LauncherComponent.class.isAssignableFrom(classOfT)) {
/*  57 */       throw new IllegalArgumentException("Given class is not a LauncherComponent: " + classOfT
/*     */           
/*  59 */           .getSimpleName());
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  64 */       constructor = classOfT.getConstructor(new Class[] { ComponentManager.class });
/*  65 */     } catch (Exception e) {
/*  66 */       throw new IllegalStateException("Cannot get constructor for component: " + classOfT
/*     */           
/*  68 */           .getSimpleName(), e);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  74 */       instance = constructor.newInstance(new Object[] { this });
/*  75 */     } catch (Exception e) {
/*  76 */       throw new IllegalStateException("Cannot createScrollWrapper a new instance for component: " + classOfT
/*     */           
/*  78 */           .getSimpleName(), e);
/*     */     } 
/*     */     
/*  81 */     LauncherComponent component = (LauncherComponent)instance;
/*  82 */     this.components.add(component);
/*     */     
/*  84 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends LauncherComponent> T getComponent(Class<T> classOfT) {
/*  89 */     if (classOfT == null) {
/*  90 */       throw new NullPointerException();
/*     */     }
/*  92 */     for (LauncherComponent component : this.components) {
/*  93 */       if (classOfT.isInstance(component))
/*  94 */         return (T)component; 
/*     */     } 
/*  96 */     throw new IllegalArgumentException("Cannot find the component!");
/*     */   }
/*     */   
/*     */   <T extends LauncherComponent> boolean hasComponent(Class<T> classOfT) {
/* 100 */     if (classOfT == null) {
/* 101 */       return false;
/*     */     }
/* 103 */     for (LauncherComponent component : this.components) {
/* 104 */       if (classOfT.isInstance(component))
/* 105 */         return true; 
/*     */     } 
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> List<T> getComponentsOf(Class<T> classOfE) {
/* 112 */     List<T> list = new ArrayList<>();
/*     */     
/* 114 */     if (classOfE == null) {
/* 115 */       return list;
/*     */     }
/* 117 */     for (LauncherComponent component : this.components) {
/* 118 */       if (classOfE.isInstance(component))
/* 119 */         list.add((T)component); 
/*     */     } 
/* 121 */     return list;
/*     */   }
/*     */   
/*     */   public void startAsyncRefresh() {
/* 125 */     this.refresher.iterate();
/*     */   }
/*     */   
/*     */   void startRefresh() {
/* 129 */     for (LauncherComponent component : this.components) {
/* 130 */       if (component instanceof RefreshableComponent) {
/* 131 */         RefreshableComponent interruptible = (RefreshableComponent)component;
/* 132 */         interruptible.refreshComponent();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void stopRefresh() {
/* 137 */     for (LauncherComponent component : this.components) {
/* 138 */       if (component instanceof InterruptibleComponent) {
/* 139 */         InterruptibleComponent interruptible = (InterruptibleComponent)component;
/* 140 */         interruptible.stopRefresh();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   class ComponentManagerRefresher extends LoopedThread { private final ComponentManager parent;
/*     */     
/*     */     ComponentManagerRefresher(ComponentManager manager) {
/* 148 */       super("ComponentManagerRefresher");
/* 149 */       this.parent = manager;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void iterateOnce() {
/* 154 */       this.parent.startRefresh();
/*     */     } }
/*     */ 
/*     */   
/*     */   public void setTlauncher(TLauncher tlauncher) {
/* 159 */     this.tlauncher = tlauncher;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\managers\ComponentManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */