/*    */ package org.tlauncher.tlauncher.configuration;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class InnerConfiguration
/*    */   extends SimpleConfiguration
/*    */ {
/*    */   public InnerConfiguration(InputStream in) throws IOException {
/* 13 */     super(in);
/*    */   }
/*    */   public String getAccessRepoTlauncherOrg(String key) {
/* 16 */     return get(key);
/*    */   }
/*    */   
/*    */   public String[] getArrayAccess(String key) {
/* 20 */     return getAccessRepoTlauncherOrg(key).split(",");
/*    */   }
/*    */   
/*    */   public String[] getArrayShuffle(String key) {
/* 24 */     List<String> list = Lists.newArrayList((Object[])getArray(key));
/* 25 */     Collections.shuffle(list);
/* 26 */     return list.<String>toArray(new String[0]);
/*    */   }
/*    */   
/*    */   public String[] getArray(String key) {
/* 30 */     return get(key).split(",");
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\configuration\InnerConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */