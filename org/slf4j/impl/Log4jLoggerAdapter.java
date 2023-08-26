/*     */ package org.slf4j.impl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.helpers.FormattingTuple;
/*     */ import org.slf4j.helpers.MarkerIgnoringBase;
/*     */ import org.slf4j.helpers.MessageFormatter;
/*     */ import org.slf4j.spi.LocationAwareLogger;
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
/*     */ public final class Log4jLoggerAdapter
/*     */   extends MarkerIgnoringBase
/*     */   implements LocationAwareLogger, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6182834493563598289L;
/*     */   final transient Logger logger;
/*  66 */   static final String FQCN = Log4jLoggerAdapter.class.getName();
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean traceCapable;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Log4jLoggerAdapter(Logger logger) {
/*  76 */     this.logger = logger;
/*  77 */     this.name = logger.getName();
/*  78 */     this.traceCapable = isTraceCapable();
/*     */   }
/*     */   
/*     */   private boolean isTraceCapable() {
/*     */     try {
/*  83 */       this.logger.isTraceEnabled();
/*  84 */       return true;
/*  85 */     } catch (NoSuchMethodError e) {
/*  86 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled() {
/*  96 */     if (this.traceCapable) {
/*  97 */       return this.logger.isTraceEnabled();
/*     */     }
/*  99 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String msg) {
/* 110 */     this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, msg, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String format, Object arg) {
/* 128 */     if (isTraceEnabled()) {
/* 129 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 130 */       this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String format, Object arg1, Object arg2) {
/* 152 */     if (isTraceEnabled()) {
/* 153 */       FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
/* 154 */       this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String format, Object... arguments) {
/* 174 */     if (isTraceEnabled()) {
/* 175 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 176 */       this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */   
/*     */   public void trace(String msg, Throwable t) {
/* 190 */     this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 199 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String msg) {
/* 209 */     this.logger.log(FQCN, (Priority)Level.DEBUG, msg, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String format, Object arg) {
/* 227 */     if (this.logger.isDebugEnabled()) {
/* 228 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 229 */       this.logger.log(FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String format, Object arg1, Object arg2) {
/* 250 */     if (this.logger.isDebugEnabled()) {
/* 251 */       FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
/* 252 */       this.logger.log(FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String format, Object... arguments) {
/* 270 */     if (this.logger.isDebugEnabled()) {
/* 271 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 272 */       this.logger.log(FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */   public void debug(String msg, Throwable t) {
/* 285 */     this.logger.log(FQCN, (Priority)Level.DEBUG, msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 294 */     return this.logger.isInfoEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String msg) {
/* 304 */     this.logger.log(FQCN, (Priority)Level.INFO, msg, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String format, Object arg) {
/* 321 */     if (this.logger.isInfoEnabled()) {
/* 322 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 323 */       this.logger.log(FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String format, Object arg1, Object arg2) {
/* 344 */     if (this.logger.isInfoEnabled()) {
/* 345 */       FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
/* 346 */       this.logger.log(FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String format, Object... argArray) {
/* 365 */     if (this.logger.isInfoEnabled()) {
/* 366 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 367 */       this.logger.log(FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */   
/*     */   public void info(String msg, Throwable t) {
/* 381 */     this.logger.log(FQCN, (Priority)Level.INFO, msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 390 */     return this.logger.isEnabledFor((Priority)Level.WARN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String msg) {
/* 400 */     this.logger.log(FQCN, (Priority)Level.WARN, msg, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String format, Object arg) {
/* 418 */     if (this.logger.isEnabledFor((Priority)Level.WARN)) {
/* 419 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 420 */       this.logger.log(FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String format, Object arg1, Object arg2) {
/* 441 */     if (this.logger.isEnabledFor((Priority)Level.WARN)) {
/* 442 */       FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
/* 443 */       this.logger.log(FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String format, Object... argArray) {
/* 462 */     if (this.logger.isEnabledFor((Priority)Level.WARN)) {
/* 463 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 464 */       this.logger.log(FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */   
/*     */   public void warn(String msg, Throwable t) {
/* 478 */     this.logger.log(FQCN, (Priority)Level.WARN, msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 487 */     return this.logger.isEnabledFor((Priority)Level.ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg) {
/* 497 */     this.logger.log(FQCN, (Priority)Level.ERROR, msg, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String format, Object arg) {
/* 515 */     if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
/* 516 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 517 */       this.logger.log(FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String format, Object arg1, Object arg2) {
/* 538 */     if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
/* 539 */       FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
/* 540 */       this.logger.log(FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String format, Object... argArray) {
/* 559 */     if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
/* 560 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 561 */       this.logger.log(FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
/*     */     } 
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
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 575 */     this.logger.log(FQCN, (Priority)Level.ERROR, msg, t);
/*     */   }
/*     */ 
/*     */   
/*     */   public void log(Marker marker, String callerFQCN, int level, String msg, Object[] argArray, Throwable t) {
/*     */     Level log4jLevel;
/* 581 */     switch (level) {
/*     */       case 0:
/* 583 */         log4jLevel = this.traceCapable ? Level.TRACE : Level.DEBUG;
/*     */         break;
/*     */       case 10:
/* 586 */         log4jLevel = Level.DEBUG;
/*     */         break;
/*     */       case 20:
/* 589 */         log4jLevel = Level.INFO;
/*     */         break;
/*     */       case 30:
/* 592 */         log4jLevel = Level.WARN;
/*     */         break;
/*     */       case 40:
/* 595 */         log4jLevel = Level.ERROR;
/*     */         break;
/*     */       default:
/* 598 */         throw new IllegalStateException("Level number " + level + " is not recognized.");
/*     */     } 
/*     */     
/* 601 */     this.logger.log(callerFQCN, (Priority)log4jLevel, msg, t);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\slf4j\impl\Log4jLoggerAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */