/*    */ package org.apache.commons.compress.archivers.zip;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CircularBuffer
/*    */ {
/*    */   private final int size;
/*    */   private final byte[] buffer;
/*    */   private int readIndex;
/*    */   private int writeIndex;
/*    */   
/*    */   CircularBuffer(int size) {
/* 42 */     this.size = size;
/* 43 */     this.buffer = new byte[size];
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean available() {
/* 50 */     return (this.readIndex != this.writeIndex);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void put(int value) {
/* 57 */     this.buffer[this.writeIndex] = (byte)value;
/* 58 */     this.writeIndex = (this.writeIndex + 1) % this.size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int get() {
/* 65 */     if (available()) {
/* 66 */       int value = this.buffer[this.readIndex];
/* 67 */       this.readIndex = (this.readIndex + 1) % this.size;
/* 68 */       return value & 0xFF;
/*    */     } 
/* 70 */     return -1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void copy(int distance, int length) {
/* 80 */     int pos1 = this.writeIndex - distance;
/* 81 */     int pos2 = pos1 + length;
/* 82 */     for (int i = pos1; i < pos2; i++) {
/* 83 */       this.buffer[this.writeIndex] = this.buffer[(i + this.size) % this.size];
/* 84 */       this.writeIndex = (this.writeIndex + 1) % this.size;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\zip\CircularBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */