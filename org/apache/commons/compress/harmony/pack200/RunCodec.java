/*     */ package org.apache.commons.compress.harmony.pack200;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.compress.utils.ExactMath;
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
/*     */ public class RunCodec
/*     */   extends Codec
/*     */ {
/*     */   private int k;
/*     */   private final Codec aCodec;
/*     */   private final Codec bCodec;
/*     */   private int last;
/*     */   
/*     */   public RunCodec(int k, Codec aCodec, Codec bCodec) throws Pack200Exception {
/*  37 */     if (k <= 0) {
/*  38 */       throw new Pack200Exception("Cannot have a RunCodec for a negative number of numbers");
/*     */     }
/*  40 */     if (aCodec == null || bCodec == null) {
/*  41 */       throw new Pack200Exception("Must supply both codecs for a RunCodec");
/*     */     }
/*  43 */     this.k = k;
/*  44 */     this.aCodec = aCodec;
/*  45 */     this.bCodec = bCodec;
/*     */   }
/*     */ 
/*     */   
/*     */   public int decode(InputStream in) throws IOException, Pack200Exception {
/*  50 */     return decode(in, this.last);
/*     */   }
/*     */ 
/*     */   
/*     */   public int decode(InputStream in, long last) throws IOException, Pack200Exception {
/*  55 */     if (--this.k >= 0) {
/*  56 */       int value = this.aCodec.decode(in, this.last);
/*  57 */       this.last = (this.k == 0) ? 0 : value;
/*  58 */       return normalise(value, this.aCodec);
/*     */     } 
/*  60 */     this.last = this.bCodec.decode(in, this.last);
/*  61 */     return normalise(this.last, this.bCodec);
/*     */   }
/*     */   
/*     */   private int normalise(int value, Codec codecUsed) {
/*  65 */     if (codecUsed instanceof BHSDCodec) {
/*  66 */       BHSDCodec bhsd = (BHSDCodec)codecUsed;
/*  67 */       if (bhsd.isDelta()) {
/*  68 */         long cardinality = bhsd.cardinality();
/*  69 */         while (value > bhsd.largest()) {
/*  70 */           value = (int)(value - cardinality);
/*     */         }
/*  72 */         while (value < bhsd.smallest()) {
/*  73 */           value = ExactMath.add(value, cardinality);
/*     */         }
/*     */       } 
/*     */     } 
/*  77 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] decodeInts(int n, InputStream in) throws IOException, Pack200Exception {
/*  82 */     int[] band = new int[n];
/*  83 */     int[] aValues = this.aCodec.decodeInts(this.k, in);
/*  84 */     normalise(aValues, this.aCodec);
/*  85 */     int[] bValues = this.bCodec.decodeInts(n - this.k, in);
/*  86 */     normalise(bValues, this.bCodec);
/*  87 */     System.arraycopy(aValues, 0, band, 0, this.k);
/*  88 */     System.arraycopy(bValues, 0, band, this.k, n - this.k);
/*  89 */     this.lastBandLength = this.aCodec.lastBandLength + this.bCodec.lastBandLength;
/*  90 */     return band;
/*     */   }
/*     */   
/*     */   private void normalise(int[] band, Codec codecUsed) {
/*  94 */     if (codecUsed instanceof BHSDCodec) {
/*  95 */       BHSDCodec bhsd = (BHSDCodec)codecUsed;
/*  96 */       if (bhsd.isDelta()) {
/*  97 */         long cardinality = bhsd.cardinality();
/*  98 */         for (int i = 0; i < band.length; i++) {
/*  99 */           while (band[i] > bhsd.largest()) {
/* 100 */             band[i] = (int)(band[i] - cardinality);
/*     */           }
/* 102 */           while (band[i] < bhsd.smallest()) {
/* 103 */             band[i] = ExactMath.add(band[i], cardinality);
/*     */           }
/*     */         } 
/*     */       } 
/* 107 */     } else if (codecUsed instanceof PopulationCodec) {
/* 108 */       PopulationCodec popCodec = (PopulationCodec)codecUsed;
/* 109 */       int[] favoured = (int[])popCodec.getFavoured().clone();
/* 110 */       Arrays.sort(favoured);
/* 111 */       for (int i = 0; i < band.length; i++) {
/* 112 */         boolean favouredValue = (Arrays.binarySearch(favoured, band[i]) > -1);
/* 113 */         Codec theCodec = favouredValue ? popCodec.getFavouredCodec() : popCodec.getUnfavouredCodec();
/* 114 */         if (theCodec instanceof BHSDCodec) {
/* 115 */           BHSDCodec bhsd = (BHSDCodec)theCodec;
/* 116 */           if (bhsd.isDelta()) {
/* 117 */             long cardinality = bhsd.cardinality();
/* 118 */             while (band[i] > bhsd.largest()) {
/* 119 */               band[i] = (int)(band[i] - cardinality);
/*     */             }
/* 121 */             while (band[i] < bhsd.smallest()) {
/* 122 */               band[i] = ExactMath.add(band[i], cardinality);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 132 */     return "RunCodec[k=" + this.k + ";aCodec=" + this.aCodec + "bCodec=" + this.bCodec + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] encode(int value, int last) throws Pack200Exception {
/* 137 */     throw new Pack200Exception("Must encode entire band at once with a RunCodec");
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] encode(int value) throws Pack200Exception {
/* 142 */     throw new Pack200Exception("Must encode entire band at once with a RunCodec");
/*     */   }
/*     */   
/*     */   public int getK() {
/* 146 */     return this.k;
/*     */   }
/*     */   
/*     */   public Codec getACodec() {
/* 150 */     return this.aCodec;
/*     */   }
/*     */   
/*     */   public Codec getBCodec() {
/* 154 */     return this.bCodec;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\harmony\pack200\RunCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */