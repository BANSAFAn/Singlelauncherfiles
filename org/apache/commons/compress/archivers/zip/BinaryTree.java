/*     */ package org.apache.commons.compress.archivers.zip;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Arrays;
/*     */ import org.apache.commons.compress.utils.IOUtils;
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
/*     */ class BinaryTree
/*     */ {
/*     */   private static final int UNDEFINED = -1;
/*     */   private static final int NODE = -2;
/*     */   private final int[] tree;
/*     */   
/*     */   public BinaryTree(int depth) {
/*  49 */     if (depth < 0 || depth > 30) {
/*  50 */       throw new IllegalArgumentException("depth must be bigger than 0 and not bigger than 30 but is " + depth);
/*     */     }
/*     */     
/*  53 */     this.tree = new int[(int)((1L << depth + 1) - 1L)];
/*  54 */     Arrays.fill(this.tree, -1);
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
/*     */   public void addLeaf(int node, int path, int depth, int value) {
/*  66 */     if (depth == 0) {
/*     */       
/*  68 */       if (this.tree[node] != -1) {
/*  69 */         throw new IllegalArgumentException("Tree value at index " + node + " has already been assigned (" + this.tree[node] + ")");
/*     */       }
/*  71 */       this.tree[node] = value;
/*     */     } else {
/*     */       
/*  74 */       this.tree[node] = -2;
/*     */ 
/*     */       
/*  77 */       int nextChild = 2 * node + 1 + (path & 0x1);
/*  78 */       addLeaf(nextChild, path >>> 1, depth - 1, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(BitStream stream) throws IOException {
/*  89 */     int bit, value, currentIndex = 0;
/*     */     
/*     */     while (true) {
/*  92 */       bit = stream.nextBit();
/*  93 */       if (bit == -1) {
/*  94 */         return -1;
/*     */       }
/*     */       
/*  97 */       int childIndex = 2 * currentIndex + 1 + bit;
/*  98 */       value = this.tree[childIndex];
/*  99 */       if (value == -2)
/*     */       
/* 101 */       { currentIndex = childIndex; continue; }  break;
/* 102 */     }  if (value != -1) {
/* 103 */       return value;
/*     */     }
/* 105 */     throw new IOException("The child " + bit + " of node at index " + currentIndex + " is not defined");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static BinaryTree decode(InputStream inputStream, int totalNumberOfValues) throws IOException {
/* 115 */     if (totalNumberOfValues < 0) {
/* 116 */       throw new IllegalArgumentException("totalNumberOfValues must be bigger than 0, is " + totalNumberOfValues);
/*     */     }
/*     */ 
/*     */     
/* 120 */     int size = inputStream.read() + 1;
/* 121 */     if (size == 0) {
/* 122 */       throw new IOException("Cannot read the size of the encoded tree, unexpected end of stream");
/*     */     }
/*     */     
/* 125 */     byte[] encodedTree = IOUtils.readRange(inputStream, size);
/* 126 */     if (encodedTree.length != size) {
/* 127 */       throw new EOFException();
/*     */     }
/*     */ 
/*     */     
/* 131 */     int maxLength = 0;
/*     */     
/* 133 */     int[] originalBitLengths = new int[totalNumberOfValues];
/* 134 */     int pos = 0;
/* 135 */     for (byte b : encodedTree) {
/*     */       
/* 137 */       int numberOfValues = ((b & 0xF0) >> 4) + 1;
/* 138 */       if (pos + numberOfValues > totalNumberOfValues) {
/* 139 */         throw new IOException("Number of values exceeds given total number of values");
/*     */       }
/* 141 */       int bitLength = (b & 0xF) + 1;
/*     */       
/* 143 */       for (int n = 0; n < numberOfValues; n++) {
/* 144 */         originalBitLengths[pos++] = bitLength;
/*     */       }
/*     */       
/* 147 */       maxLength = Math.max(maxLength, bitLength);
/*     */     } 
/*     */     
/* 150 */     int oBitLengths = originalBitLengths.length;
/*     */     
/* 152 */     int[] permutation = new int[oBitLengths];
/* 153 */     for (int k = 0; k < permutation.length; k++) {
/* 154 */       permutation[k] = k;
/*     */     }
/*     */     
/* 157 */     int c = 0;
/* 158 */     int[] sortedBitLengths = new int[oBitLengths];
/* 159 */     for (int j = 0; j < oBitLengths; j++) {
/*     */       
/* 161 */       for (int l = 0; l < oBitLengths; l++) {
/*     */         
/* 163 */         if (originalBitLengths[l] == j) {
/*     */           
/* 165 */           sortedBitLengths[c] = j;
/*     */ 
/*     */           
/* 168 */           permutation[c] = l;
/*     */           
/* 170 */           c++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 176 */     int code = 0;
/* 177 */     int codeIncrement = 0;
/* 178 */     int lastBitLength = 0;
/*     */     
/* 180 */     int[] codes = new int[totalNumberOfValues];
/*     */     
/* 182 */     for (int i = totalNumberOfValues - 1; i >= 0; i--) {
/* 183 */       code += codeIncrement;
/* 184 */       if (sortedBitLengths[i] != lastBitLength) {
/* 185 */         lastBitLength = sortedBitLengths[i];
/* 186 */         codeIncrement = 1 << 16 - lastBitLength;
/*     */       } 
/* 188 */       codes[permutation[i]] = code;
/*     */     } 
/*     */ 
/*     */     
/* 192 */     BinaryTree tree = new BinaryTree(maxLength);
/*     */     
/* 194 */     for (int m = 0; m < codes.length; m++) {
/* 195 */       int bitLength = originalBitLengths[m];
/* 196 */       if (bitLength > 0) {
/* 197 */         tree.addLeaf(0, Integer.reverse(codes[m] << 16), bitLength, m);
/*     */       }
/*     */     } 
/*     */     
/* 201 */     return tree;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\BinaryTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */