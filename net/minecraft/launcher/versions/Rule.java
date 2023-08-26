/*    */ package net.minecraft.launcher.versions;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.tlauncher.util.OS;
/*    */ 
/*    */ public class Rule
/*    */ {
/* 10 */   private Action action = Action.ALLOW;
/*    */   
/*    */   private OSRestriction os;
/*    */   private Map<String, Object> features;
/*    */   
/*    */   public Rule() {}
/*    */   
/*    */   public Rule(Rule rule) {
/* 18 */     this.action = rule.action;
/*    */     
/* 20 */     if (rule.os != null)
/* 21 */       this.os = new OSRestriction(rule.os); 
/*    */   }
/*    */   
/*    */   public Action getAppliedAction() {
/* 25 */     if (this.features != null && this.features.get("is_demo_user") != null) {
/* 26 */       return Action.DISALLOW;
/*    */     }
/*    */     
/* 29 */     if (this.os != null && !this.os.isCurrentOperatingSystem()) {
/* 30 */       return null;
/*    */     }
/* 32 */     return this.action;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 36 */     return "Rule{action=" + this.action + ", os=" + this.os + ", features=" + this.features + '}';
/*    */   }
/*    */   
/*    */   public enum Action {
/* 40 */     ALLOW, DISALLOW;
/*    */   }
/*    */   
/*    */   public class OSRestriction {
/*    */     private OS name;
/*    */     private String version;
/*    */     
/*    */     public OSRestriction(OSRestriction osRestriction) {
/* 48 */       this.name = osRestriction.name;
/* 49 */       this.version = osRestriction.version;
/*    */     }
/*    */     
/*    */     public boolean isCurrentOperatingSystem() {
/* 53 */       if (this.name != null && this.name != OS.CURRENT) {
/* 54 */         return false;
/*    */       }
/* 56 */       if (this.version != null) {
/*    */         try {
/* 58 */           Pattern pattern = Pattern.compile(this.version);
/* 59 */           Matcher matcher = pattern.matcher(System.getProperty("os.version"));
/* 60 */           if (!matcher.matches())
/* 61 */             return false; 
/* 62 */         } catch (Throwable throwable) {}
/*    */       }
/*    */       
/* 65 */       return true;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 70 */       return "OSRestriction{name=" + this.name + ", version='" + this.version + '\'' + '}';
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launcher\versions\Rule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */