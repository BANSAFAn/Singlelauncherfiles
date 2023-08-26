/*    */ package org.tlauncher.tlauncher.ui.swing;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import javax.swing.JScrollPane;
/*    */ 
/*    */ public class ScrollPane
/*    */   extends JScrollPane {
/*    */   private static final boolean DEFAULT_BORDER = false;
/*    */   
/*    */   public ScrollPane(Component view, ScrollBarPolicy vertical, ScrollBarPolicy horizontal, boolean border) {
/* 11 */     super(view);
/*    */     
/* 13 */     setOpaque(false);
/* 14 */     getViewport().setOpaque(false);
/*    */     
/* 16 */     if (!border) {
/* 17 */       setBorder(null);
/*    */     }
/* 19 */     setVBPolicy(vertical);
/* 20 */     setHBPolicy(horizontal);
/*    */   }
/*    */   
/*    */   public ScrollPane(Component view, ScrollBarPolicy vertical, ScrollBarPolicy horizontal) {
/* 24 */     this(view, vertical, horizontal, false);
/*    */   }
/*    */   
/*    */   public ScrollPane(Component view, ScrollBarPolicy generalPolicy, boolean border) {
/* 28 */     this(view, generalPolicy, generalPolicy, border);
/*    */   }
/*    */   
/*    */   public ScrollPane(Component view, ScrollBarPolicy generalPolicy) {
/* 32 */     this(view, generalPolicy, generalPolicy);
/*    */   }
/*    */   
/*    */   public ScrollPane(Component view, boolean border) {
/* 36 */     this(view, ScrollBarPolicy.AS_NEEDED, border);
/*    */   }
/*    */   
/*    */   public ScrollPane(Component view) {
/* 40 */     this(view, ScrollBarPolicy.AS_NEEDED);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setVerticalScrollBarPolicy(ScrollBarPolicy policy) {
/*    */     int i_policy;
/* 46 */     switch (policy) {
/*    */       case ALWAYS:
/* 48 */         i_policy = 22;
/*    */         break;
/*    */       case AS_NEEDED:
/* 51 */         i_policy = 20;
/*    */         break;
/*    */       case NEVER:
/* 54 */         i_policy = 21;
/*    */         break;
/*    */       default:
/* 57 */         throw new IllegalArgumentException();
/*    */     } 
/*    */     
/* 60 */     setVerticalScrollBarPolicy(i_policy);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setHorizontalScrollBarPolicy(ScrollBarPolicy policy) {
/*    */     int i_policy;
/* 66 */     switch (policy) {
/*    */       case ALWAYS:
/* 68 */         i_policy = 32;
/*    */         break;
/*    */       case AS_NEEDED:
/* 71 */         i_policy = 30;
/*    */         break;
/*    */       case NEVER:
/* 74 */         i_policy = 31;
/*    */         break;
/*    */       default:
/* 77 */         throw new IllegalArgumentException();
/*    */     } 
/*    */     
/* 80 */     setHorizontalScrollBarPolicy(i_policy);
/*    */   }
/*    */   
/*    */   public void setVBPolicy(ScrollBarPolicy policy) {
/* 84 */     setVerticalScrollBarPolicy(policy);
/*    */   }
/*    */   
/*    */   public void setHBPolicy(ScrollBarPolicy policy) {
/* 88 */     setHorizontalScrollBarPolicy(policy);
/*    */   }
/*    */   
/*    */   public enum ScrollBarPolicy
/*    */   {
/* 93 */     ALWAYS, AS_NEEDED, NEVER;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\ScrollPane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */