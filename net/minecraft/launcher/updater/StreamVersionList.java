/*    */ package net.minecraft.launcher.updater;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import org.apache.commons.io.Charsets;
/*    */ 
/*    */ public abstract class StreamVersionList
/*    */   extends VersionList
/*    */ {
/*    */   protected String getUrl(String uri) throws IOException {
/* 13 */     InputStream inputStream = getInputStream(uri);
/*    */     
/* 15 */     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
/*    */     
/* 17 */     StringBuilder result = new StringBuilder();
/*    */     
/*    */     String line;
/* 20 */     while ((line = reader.readLine()) != null) {
/*    */       
/* 22 */       if (result.length() > 0) {
/* 23 */         result.append('\n');
/*    */       }
/* 25 */       result.append(line);
/*    */     } 
/*    */     
/* 28 */     reader.close();
/*    */     
/* 30 */     return result.toString();
/*    */   }
/*    */   
/*    */   protected abstract InputStream getInputStream(String paramString) throws IOException;
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\launche\\updater\StreamVersionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */