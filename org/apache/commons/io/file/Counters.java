/*     */ package org.apache.commons.io.file;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.Objects;
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
/*     */ public class Counters
/*     */ {
/*     */   private static class AbstractPathCounters
/*     */     implements PathCounters
/*     */   {
/*     */     private final Counters.Counter byteCounter;
/*     */     private final Counters.Counter directoryCounter;
/*     */     private final Counters.Counter fileCounter;
/*     */     
/*     */     protected AbstractPathCounters(Counters.Counter byteCounter, Counters.Counter directoryCounter, Counters.Counter fileCounter) {
/*  48 */       this.byteCounter = byteCounter;
/*  49 */       this.directoryCounter = directoryCounter;
/*  50 */       this.fileCounter = fileCounter;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/*  55 */       if (this == obj) {
/*  56 */         return true;
/*     */       }
/*  58 */       if (!(obj instanceof AbstractPathCounters)) {
/*  59 */         return false;
/*     */       }
/*  61 */       AbstractPathCounters other = (AbstractPathCounters)obj;
/*  62 */       return (Objects.equals(this.byteCounter, other.byteCounter) && 
/*  63 */         Objects.equals(this.directoryCounter, other.directoryCounter) && 
/*  64 */         Objects.equals(this.fileCounter, other.fileCounter));
/*     */     }
/*     */ 
/*     */     
/*     */     public Counters.Counter getByteCounter() {
/*  69 */       return this.byteCounter;
/*     */     }
/*     */ 
/*     */     
/*     */     public Counters.Counter getDirectoryCounter() {
/*  74 */       return this.directoryCounter;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Counters.Counter getFileCounter() {
/*  84 */       return this.fileCounter;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  89 */       return Objects.hash(new Object[] { this.byteCounter, this.directoryCounter, this.fileCounter });
/*     */     }
/*     */ 
/*     */     
/*     */     public void reset() {
/*  94 */       this.byteCounter.reset();
/*  95 */       this.directoryCounter.reset();
/*  96 */       this.fileCounter.reset();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 101 */       return String.format("%,d files, %,d directories, %,d bytes", new Object[] { Long.valueOf(this.fileCounter.get()), 
/* 102 */             Long.valueOf(this.directoryCounter.get()), Long.valueOf(this.byteCounter.get()) });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class BigIntegerCounter
/*     */     implements Counter
/*     */   {
/* 112 */     private BigInteger value = BigInteger.ZERO;
/*     */ 
/*     */     
/*     */     public void add(long val) {
/* 116 */       this.value = this.value.add(BigInteger.valueOf(val));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 122 */       if (this == obj) {
/* 123 */         return true;
/*     */       }
/* 125 */       if (!(obj instanceof Counters.Counter)) {
/* 126 */         return false;
/*     */       }
/* 128 */       Counters.Counter other = (Counters.Counter)obj;
/* 129 */       return Objects.equals(this.value, other.getBigInteger());
/*     */     }
/*     */ 
/*     */     
/*     */     public long get() {
/* 134 */       return this.value.longValueExact();
/*     */     }
/*     */ 
/*     */     
/*     */     public BigInteger getBigInteger() {
/* 139 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long getLong() {
/* 144 */       return Long.valueOf(this.value.longValueExact());
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 149 */       return Objects.hash(new Object[] { this.value });
/*     */     }
/*     */ 
/*     */     
/*     */     public void increment() {
/* 154 */       this.value = this.value.add(BigInteger.ONE);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 159 */       return this.value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public void reset() {
/* 164 */       this.value = BigInteger.ZERO;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private BigIntegerCounter() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class BigIntegerPathCounters
/*     */     extends AbstractPathCounters
/*     */   {
/*     */     protected BigIntegerPathCounters() {
/* 177 */       super(Counters.bigIntegerCounter(), Counters.bigIntegerCounter(), Counters.bigIntegerCounter());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface Counter
/*     */   {
/*     */     void add(long param1Long);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long get();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     BigInteger getBigInteger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Long getLong();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void increment();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default void reset() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class LongCounter
/*     */     implements Counter
/*     */   {
/*     */     private long value;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private LongCounter() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(long add) {
/* 238 */       this.value += add;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 244 */       if (this == obj) {
/* 245 */         return true;
/*     */       }
/* 247 */       if (!(obj instanceof Counters.Counter)) {
/* 248 */         return false;
/*     */       }
/* 250 */       Counters.Counter other = (Counters.Counter)obj;
/* 251 */       return (this.value == other.get());
/*     */     }
/*     */ 
/*     */     
/*     */     public long get() {
/* 256 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public BigInteger getBigInteger() {
/* 261 */       return BigInteger.valueOf(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public Long getLong() {
/* 266 */       return Long.valueOf(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 271 */       return Objects.hash(new Object[] { Long.valueOf(this.value) });
/*     */     }
/*     */ 
/*     */     
/*     */     public void increment() {
/* 276 */       this.value++;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 281 */       return Long.toString(this.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void reset() {
/* 286 */       this.value = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class LongPathCounters
/*     */     extends AbstractPathCounters
/*     */   {
/*     */     protected LongPathCounters() {
/* 299 */       super(Counters.longCounter(), Counters.longCounter(), Counters.longCounter());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class NoopCounter
/*     */     implements Counter
/*     */   {
/* 309 */     static final NoopCounter INSTANCE = new NoopCounter();
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(long add) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public long get() {
/* 318 */       return 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     public BigInteger getBigInteger() {
/* 323 */       return BigInteger.ZERO;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long getLong() {
/* 328 */       return Long.valueOf(0L);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void increment() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class NoopPathCounters
/*     */     extends AbstractPathCounters
/*     */   {
/* 343 */     static final NoopPathCounters INSTANCE = new NoopPathCounters();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private NoopPathCounters() {
/* 349 */       super(Counters.noopCounter(), Counters.noopCounter(), Counters.noopCounter());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface PathCounters
/*     */   {
/*     */     Counters.Counter getByteCounter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Counters.Counter getDirectoryCounter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Counters.Counter getFileCounter();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default void reset() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Counter bigIntegerCounter() {
/* 395 */     return new BigIntegerCounter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PathCounters bigIntegerPathCounters() {
/* 404 */     return new BigIntegerPathCounters();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Counter longCounter() {
/* 413 */     return new LongCounter();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PathCounters longPathCounters() {
/* 422 */     return new LongPathCounters();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Counter noopCounter() {
/* 432 */     return NoopCounter.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PathCounters noopPathCounters() {
/* 442 */     return NoopPathCounters.INSTANCE;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\io\file\Counters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */