/*    */ package org.tlauncher.tlauncher.ui.converter;
/*    */ 
/*    */ import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
/*    */ import org.tlauncher.tlauncher.ui.loc.LocalizableStringConverter;
/*    */ 
/*    */ public class ActionOnLaunchConverter
/*    */   extends LocalizableStringConverter<ActionOnLaunch>
/*    */ {
/*    */   public ActionOnLaunchConverter() {
/* 10 */     super("settings.launch-action");
/*    */   }
/*    */ 
/*    */   
/*    */   public ActionOnLaunch fromString(String from) {
/* 15 */     return ActionOnLaunch.get(from);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toValue(ActionOnLaunch from) {
/* 20 */     return from.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toPath(ActionOnLaunch from) {
/* 25 */     return from.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<ActionOnLaunch> getObjectClass() {
/* 30 */     return ActionOnLaunch.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\converter\ActionOnLaunchConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */