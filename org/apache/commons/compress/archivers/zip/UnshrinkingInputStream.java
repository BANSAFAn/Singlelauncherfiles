/*     */ package org.apache.commons.compress.archivers.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteOrder;
/*     */ import org.apache.commons.compress.compressors.lzw.LZWInputStream;
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
/*     */ class UnshrinkingInputStream
/*     */   extends LZWInputStream
/*     */ {
/*     */   private static final int MAX_CODE_SIZE = 13;
/*     */   private static final int MAX_TABLE_SIZE = 8192;
/*     */   private final boolean[] isUsed;
/*     */   
/*     */   public UnshrinkingInputStream(InputStream inputStream) {
/*  43 */     super(inputStream, ByteOrder.LITTLE_ENDIAN);
/*  44 */     setClearCode(9);
/*  45 */     initializeTables(13);
/*  46 */     this.isUsed = new boolean[getPrefixesLength()];
/*  47 */     for (int i = 0; i < 256; i++) {
/*  48 */       this.isUsed[i] = true;
/*     */     }
/*  50 */     setTableSize(getClearCode() + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int addEntry(int previousCode, byte character) throws IOException {
/*  55 */     int tableSize = getTableSize();
/*  56 */     while (tableSize < 8192 && this.isUsed[tableSize]) {
/*  57 */       tableSize++;
/*     */     }
/*  59 */     setTableSize(tableSize);
/*  60 */     int idx = addEntry(previousCode, character, 8192);
/*  61 */     if (idx >= 0) {
/*  62 */       this.isUsed[idx] = true;
/*     */     }
/*  64 */     return idx;
/*     */   }
/*     */   
/*     */   private void partialClear() {
/*  68 */     boolean[] isParent = new boolean[8192]; int i;
/*  69 */     for (i = 0; i < this.isUsed.length; i++) {
/*  70 */       if (this.isUsed[i] && getPrefix(i) != -1) {
/*  71 */         isParent[getPrefix(i)] = true;
/*     */       }
/*     */     } 
/*  74 */     for (i = getClearCode() + 1; i < isParent.length; i++) {
/*  75 */       if (!isParent[i]) {
/*  76 */         this.isUsed[i] = false;
/*  77 */         setPrefix(i, -1);
/*     */       } 
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
/*     */   protected int decompressNextSymbol() throws IOException {
/*  96 */     int code = readNextCode();
/*  97 */     if (code < 0) {
/*  98 */       return -1;
/*     */     }
/* 100 */     if (code != getClearCode()) {
/* 101 */       boolean addedUnfinishedEntry = false;
/* 102 */       int effectiveCode = code;
/* 103 */       if (!this.isUsed[code]) {
/* 104 */         effectiveCode = addRepeatOfPreviousCode();
/* 105 */         addedUnfinishedEntry = true;
/*     */       } 
/* 107 */       return expandCodeToOutputStack(effectiveCode, addedUnfinishedEntry);
/*     */     } 
/* 109 */     int subCode = readNextCode();
/* 110 */     if (subCode < 0) {
/* 111 */       throw new IOException("Unexpected EOF;");
/*     */     }
/* 113 */     if (subCode == 1) {
/* 114 */       if (getCodeSize() >= 13) {
/* 115 */         throw new IOException("Attempt to increase code size beyond maximum");
/*     */       }
/* 117 */       incrementCodeSize();
/* 118 */     } else if (subCode == 2) {
/* 119 */       partialClear();
/* 120 */       setTableSize(getClearCode() + 1);
/*     */     } else {
/* 122 */       throw new IOException("Invalid clear code subcode " + subCode);
/*     */     } 
/* 124 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\UnshrinkingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */