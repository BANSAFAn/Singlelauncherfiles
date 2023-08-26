/*     */ package ch.qos.logback.classic.spi;
/*     */ 
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class ThrowableProxy
/*     */   implements IThrowableProxy
/*     */ {
/*     */   private Throwable throwable;
/*     */   private String className;
/*     */   private String message;
/*     */   StackTraceElementProxy[] stackTraceElementProxyArray;
/*     */   int commonFrames;
/*     */   private ThrowableProxy cause;
/*  31 */   private ThrowableProxy[] suppressed = NO_SUPPRESSED;
/*     */   
/*     */   private transient PackagingDataCalculator packagingDataCalculator;
/*     */   
/*     */   private boolean calculatedPackageData = false;
/*     */   private static final Method GET_SUPPRESSED_METHOD;
/*     */   
/*     */   static {
/*  39 */     Method method = null;
/*     */     try {
/*  41 */       method = Throwable.class.getMethod("getSuppressed", new Class[0]);
/*  42 */     } catch (NoSuchMethodException noSuchMethodException) {}
/*     */ 
/*     */     
/*  45 */     GET_SUPPRESSED_METHOD = method;
/*     */   }
/*     */   
/*  48 */   private static final ThrowableProxy[] NO_SUPPRESSED = new ThrowableProxy[0];
/*     */ 
/*     */   
/*     */   public ThrowableProxy(Throwable throwable) {
/*  52 */     this.throwable = throwable;
/*  53 */     this.className = throwable.getClass().getName();
/*  54 */     this.message = throwable.getMessage();
/*  55 */     this.stackTraceElementProxyArray = ThrowableProxyUtil.steArrayToStepArray(throwable.getStackTrace());
/*     */     
/*  57 */     Throwable nested = throwable.getCause();
/*     */     
/*  59 */     if (nested != null) {
/*  60 */       this.cause = new ThrowableProxy(nested);
/*  61 */       this.cause.commonFrames = ThrowableProxyUtil.findNumberOfCommonFrames(nested.getStackTrace(), this.stackTraceElementProxyArray);
/*     */     } 
/*  63 */     if (GET_SUPPRESSED_METHOD != null) {
/*     */       
/*     */       try {
/*  66 */         Object obj = GET_SUPPRESSED_METHOD.invoke(throwable, new Object[0]);
/*  67 */         if (obj instanceof Throwable[]) {
/*  68 */           Throwable[] throwableSuppressed = (Throwable[])obj;
/*  69 */           if (throwableSuppressed.length > 0) {
/*  70 */             this.suppressed = new ThrowableProxy[throwableSuppressed.length];
/*  71 */             for (int i = 0; i < throwableSuppressed.length; i++) {
/*  72 */               this.suppressed[i] = new ThrowableProxy(throwableSuppressed[i]);
/*  73 */               (this.suppressed[i]).commonFrames = ThrowableProxyUtil.findNumberOfCommonFrames(throwableSuppressed[i].getStackTrace(), this.stackTraceElementProxyArray);
/*     */             }
/*     */           
/*     */           } 
/*     */         } 
/*  78 */       } catch (IllegalAccessException illegalAccessException) {
/*     */       
/*  80 */       } catch (InvocationTargetException invocationTargetException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getThrowable() {
/*  88 */     return this.throwable;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*  92 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 101 */     return this.className;
/*     */   }
/*     */   
/*     */   public StackTraceElementProxy[] getStackTraceElementProxyArray() {
/* 105 */     return this.stackTraceElementProxyArray;
/*     */   }
/*     */   
/*     */   public int getCommonFrames() {
/* 109 */     return this.commonFrames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IThrowableProxy getCause() {
/* 118 */     return this.cause;
/*     */   }
/*     */   
/*     */   public IThrowableProxy[] getSuppressed() {
/* 122 */     return (IThrowableProxy[])this.suppressed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PackagingDataCalculator getPackagingDataCalculator() {
/* 129 */     if (this.throwable != null && this.packagingDataCalculator == null) {
/* 130 */       this.packagingDataCalculator = new PackagingDataCalculator();
/*     */     }
/* 132 */     return this.packagingDataCalculator;
/*     */   }
/*     */   
/*     */   public void calculatePackagingData() {
/* 136 */     if (this.calculatedPackageData) {
/*     */       return;
/*     */     }
/* 139 */     PackagingDataCalculator pdc = getPackagingDataCalculator();
/* 140 */     if (pdc != null) {
/* 141 */       this.calculatedPackageData = true;
/* 142 */       pdc.calculate(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fullDump() {
/* 147 */     StringBuilder builder = new StringBuilder();
/* 148 */     for (StackTraceElementProxy step : this.stackTraceElementProxyArray) {
/* 149 */       String string = step.toString();
/* 150 */       builder.append('\t').append(string);
/* 151 */       ThrowableProxyUtil.subjoinPackagingData(builder, step);
/* 152 */       builder.append(CoreConstants.LINE_SEPARATOR);
/*     */     } 
/* 154 */     System.out.println(builder.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\classic\spi\ThrowableProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */