/*    */ package org.tlauncher.tlauncher.ui.swing.extended;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Point;
/*    */ import java.awt.event.ComponentEvent;
/*    */ import java.awt.event.ComponentListener;
/*    */ import org.tlauncher.tlauncher.ui.swing.util.IntegerArrayGetter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ExtendedComponentListener
/*    */   implements ComponentListener
/*    */ {
/*    */   private final Component comp;
/*    */   private final QuickParameterListenerThread resizeListener;
/*    */   private final QuickParameterListenerThread moveListener;
/*    */   private ComponentEvent lastResizeEvent;
/*    */   private ComponentEvent lastMoveEvent;
/*    */   
/*    */   public ExtendedComponentListener(Component component, int tick) {
/* 23 */     if (component == null) {
/* 24 */       throw new NullPointerException();
/*    */     }
/* 26 */     this.comp = component;
/*    */     
/* 28 */     this.resizeListener = new QuickParameterListenerThread(new IntegerArrayGetter()
/*    */         {
/*    */           public int[] getIntegerArray()
/*    */           {
/* 32 */             return new int[] { ExtendedComponentListener.access$000(this.this$0).getWidth(), ExtendedComponentListener.access$000(this.this$0).getHeight() }, ;
/*    */           }
/*    */         },  new Runnable()
/*    */         {
/*    */           public void run() {
/* 37 */             ExtendedComponentListener.this.onComponentResized(ExtendedComponentListener.this.lastResizeEvent);
/*    */           }
/*    */         },  tick);
/*    */ 
/*    */     
/* 42 */     this.moveListener = new QuickParameterListenerThread(new IntegerArrayGetter()
/*    */         {
/*    */           public int[] getIntegerArray()
/*    */           {
/* 46 */             Point location = ExtendedComponentListener.this.comp.getLocation();
/* 47 */             return new int[] { location.x, location.y }, ;
/*    */           }
/*    */         },  new Runnable()
/*    */         {
/*    */           public void run() {
/* 52 */             ExtendedComponentListener.this.onComponentMoved(ExtendedComponentListener.this.lastMoveEvent);
/*    */           }
/*    */         },  tick);
/*    */   }
/*    */ 
/*    */   
/*    */   public ExtendedComponentListener(Component component) {
/* 59 */     this(component, 500);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void componentResized(ComponentEvent e) {
/* 64 */     onComponentResizing(e);
/* 65 */     this.resizeListener.startListening();
/*    */   }
/*    */ 
/*    */   
/*    */   public final void componentMoved(ComponentEvent e) {
/* 70 */     onComponentMoving(e);
/* 71 */     this.moveListener.startListening();
/*    */   }
/*    */   
/*    */   public boolean isListening() {
/* 75 */     return (this.resizeListener.isIterating() || this.moveListener.isIterating());
/*    */   }
/*    */   
/*    */   public abstract void onComponentResizing(ComponentEvent paramComponentEvent);
/*    */   
/*    */   public abstract void onComponentResized(ComponentEvent paramComponentEvent);
/*    */   
/*    */   public abstract void onComponentMoving(ComponentEvent paramComponentEvent);
/*    */   
/*    */   public abstract void onComponentMoved(ComponentEvent paramComponentEvent);
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlaunche\\ui\swing\extended\ExtendedComponentListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */