/*     */ package org.apache.commons.compress.compressors.lz4;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.commons.compress.compressors.lz77support.AbstractLZ77CompressorInputStream;
/*     */ import org.apache.commons.compress.utils.ByteUtils;
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
/*     */ public class BlockLZ4CompressorInputStream
/*     */   extends AbstractLZ77CompressorInputStream
/*     */ {
/*     */   static final int WINDOW_SIZE = 65536;
/*     */   static final int SIZE_BITS = 4;
/*     */   static final int BACK_REFERENCE_SIZE_MASK = 15;
/*     */   static final int LITERAL_SIZE_MASK = 240;
/*     */   private int nextBackReferenceSize;
/*  45 */   private State state = State.NO_BLOCK;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockLZ4CompressorInputStream(InputStream is) {
/*  54 */     super(is, 65536);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*     */     int litLen;
/*     */     int backReferenceLen;
/*  62 */     if (len == 0) {
/*  63 */       return 0;
/*     */     }
/*  65 */     switch (this.state) {
/*     */       case EOF:
/*  67 */         return -1;
/*     */       case NO_BLOCK:
/*  69 */         readSizes();
/*     */       
/*     */       case IN_LITERAL:
/*  72 */         litLen = readLiteral(b, off, len);
/*  73 */         if (!hasMoreDataInBlock()) {
/*  74 */           this.state = State.LOOKING_FOR_BACK_REFERENCE;
/*     */         }
/*  76 */         return (litLen > 0) ? litLen : read(b, off, len);
/*     */       case LOOKING_FOR_BACK_REFERENCE:
/*  78 */         if (!initializeBackReference()) {
/*  79 */           this.state = State.EOF;
/*  80 */           return -1;
/*     */         } 
/*     */       
/*     */       case IN_BACK_REFERENCE:
/*  84 */         backReferenceLen = readBackReference(b, off, len);
/*  85 */         if (!hasMoreDataInBlock()) {
/*  86 */           this.state = State.NO_BLOCK;
/*     */         }
/*  88 */         return (backReferenceLen > 0) ? backReferenceLen : read(b, off, len);
/*     */     } 
/*  90 */     throw new IOException("Unknown stream state " + this.state);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readSizes() throws IOException {
/*  95 */     int nextBlock = readOneByte();
/*  96 */     if (nextBlock == -1) {
/*  97 */       throw new IOException("Premature end of stream while looking for next block");
/*     */     }
/*  99 */     this.nextBackReferenceSize = nextBlock & 0xF;
/* 100 */     long literalSizePart = ((nextBlock & 0xF0) >> 4);
/* 101 */     if (literalSizePart == 15L) {
/* 102 */       literalSizePart += readSizeBytes();
/*     */     }
/* 104 */     if (literalSizePart < 0L) {
/* 105 */       throw new IOException("Illegal block with a negative literal size found");
/*     */     }
/* 107 */     startLiteral(literalSizePart);
/* 108 */     this.state = State.IN_LITERAL;
/*     */   }
/*     */   
/*     */   private long readSizeBytes() throws IOException {
/* 112 */     long accum = 0L;
/*     */     
/*     */     while (true) {
/* 115 */       int nextByte = readOneByte();
/* 116 */       if (nextByte == -1) {
/* 117 */         throw new IOException("Premature end of stream while parsing length");
/*     */       }
/* 119 */       accum += nextByte;
/* 120 */       if (nextByte != 255) {
/* 121 */         return accum;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean initializeBackReference() throws IOException {
/* 129 */     int backReferenceOffset = 0;
/*     */     try {
/* 131 */       backReferenceOffset = (int)ByteUtils.fromLittleEndian(this.supplier, 2);
/* 132 */     } catch (IOException ex) {
/* 133 */       if (this.nextBackReferenceSize == 0) {
/* 134 */         return false;
/*     */       }
/* 136 */       throw ex;
/*     */     } 
/* 138 */     long backReferenceSize = this.nextBackReferenceSize;
/* 139 */     if (this.nextBackReferenceSize == 15) {
/* 140 */       backReferenceSize += readSizeBytes();
/*     */     }
/*     */     
/* 143 */     if (backReferenceSize < 0L) {
/* 144 */       throw new IOException("Illegal block with a negative match length found");
/*     */     }
/*     */     try {
/* 147 */       startBackReference(backReferenceOffset, backReferenceSize + 4L);
/* 148 */     } catch (IllegalArgumentException ex) {
/* 149 */       throw new IOException("Illegal block with bad offset found", ex);
/*     */     } 
/* 151 */     this.state = State.IN_BACK_REFERENCE;
/* 152 */     return true;
/*     */   }
/*     */   
/*     */   private enum State {
/* 156 */     NO_BLOCK, IN_LITERAL, LOOKING_FOR_BACK_REFERENCE, IN_BACK_REFERENCE, EOF;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\compressors\lz4\BlockLZ4CompressorInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */