/*     */ package org.apache.commons.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IOExceptionList
/*     */   extends IOException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final List<? extends Throwable> causeList;
/*     */   
/*     */   public IOExceptionList(List<? extends Throwable> causeList) {
/*  44 */     this(String.format("%,d exceptions: %s", new Object[] { Integer.valueOf((causeList == null) ? 0 : causeList.size()), causeList }), causeList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IOExceptionList(String message, List<? extends Throwable> causeList) {
/*  55 */     super(message, (causeList == null || causeList.isEmpty()) ? null : causeList.get(0));
/*  56 */     this.causeList = (causeList == null) ? Collections.<Throwable>emptyList() : causeList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Throwable> T getCause(int index) {
/*  67 */     return (T)this.causeList.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Throwable> T getCause(int index, Class<T> clazz) {
/*  79 */     return clazz.cast(this.causeList.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Throwable> List<T> getCauseList() {
/*  89 */     return (List)this.causeList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Throwable> List<T> getCauseList(Class<T> clazz) {
/* 100 */     return (List)this.causeList;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\IOExceptionList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */