/*    */ package org.tlauncher.tlauncher.ui.loc;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import org.tlauncher.tlauncher.configuration.LangConfiguration;
/*    */ import org.tlauncher.util.Reflect;
/*    */ import org.tlauncher.util.U;
/*    */ 
/*    */ public class Localizable {
/* 10 */   public static final Object[] EMPTY_VARS = new Object[0];
/*    */   
/*    */   private static final LocalizableFilter defaultFilter = comp -> true;
/*    */   private static LangConfiguration lang;
/*    */   
/*    */   public static void setLang(LangConfiguration l) {
/* 16 */     lang = l;
/*    */   }
/*    */   
/*    */   public static LangConfiguration get() {
/* 20 */     return lang;
/*    */   }
/*    */   
/*    */   public static boolean exists() {
/* 24 */     return (lang != null);
/*    */   }
/*    */   
/*    */   public static String get(String path) {
/* 28 */     return (lang != null) ? lang.get(path) : path;
/*    */   }
/*    */   
/*    */   public static String get(String path, Object... vars) {
/* 32 */     return (lang != null) ? lang.get(path, vars) : (path + " {" + 
/* 33 */       U.toLog(vars) + "}");
/*    */   }
/*    */   
/*    */   public static String getByKeys(String path, Object... keys) {
/* 37 */     return (lang != null) ? lang.ngetKeys(path, keys) : (path + " {" + 
/* 38 */       U.toLog(keys) + "}");
/*    */   }
/*    */   
/*    */   public static String nget(String path) {
/* 42 */     return (lang != null) ? lang.nget(path) : null;
/*    */   }
/*    */   
/*    */   public static String[] checkVariables(Object[] check) {
/* 46 */     if (check == null) {
/* 47 */       throw new NullPointerException();
/*    */     }
/* 49 */     String[] string = new String[check.length];
/*    */     
/* 51 */     for (int i = 0; i < check.length; i++) {
/* 52 */       if (check[i] == null) {
/* 53 */         throw new NullPointerException("Variable at index " + i + " is NULL!");
/*    */       }
/* 55 */       string[i] = check[i].toString();
/*    */     } 
/*    */     
/* 58 */     return string;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void updateContainer(Container container, LocalizableFilter filter) {
/* 63 */     for (Component c : container.getComponents()) {
/* 64 */       LocalizableComponent asLocalizable = (LocalizableComponent)Reflect.cast(c, LocalizableComponent.class);
/*    */       
/* 66 */       if (asLocalizable != null && filter.localize(c)) {
/* 67 */         asLocalizable.updateLocale();
/*    */       }
/* 69 */       if (c instanceof Container)
/* 70 */         updateContainer((Container)c, filter); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void updateContainer(Container container) {
/* 75 */     updateContainer(container, defaultFilter);
/*    */   }
/*    */   
/*    */   public static interface LocalizableFilter {
/*    */     boolean localize(Component param1Component);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\loc\Localizable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */