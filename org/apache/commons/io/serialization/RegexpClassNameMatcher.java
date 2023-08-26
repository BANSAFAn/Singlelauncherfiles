/*    */ package org.apache.commons.io.serialization;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class RegexpClassNameMatcher
/*    */   implements ClassNameMatcher
/*    */ {
/*    */   private final Pattern pattern;
/*    */   
/*    */   public RegexpClassNameMatcher(String regex) {
/* 39 */     this(Pattern.compile(regex));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RegexpClassNameMatcher(Pattern pattern) {
/* 49 */     if (pattern == null) {
/* 50 */       throw new IllegalArgumentException("Null pattern");
/*    */     }
/* 52 */     this.pattern = pattern;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(String className) {
/* 57 */     return this.pattern.matcher(className).matches();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\serialization\RegexpClassNameMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */