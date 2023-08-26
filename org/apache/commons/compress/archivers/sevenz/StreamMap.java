/*    */ package org.apache.commons.compress.archivers.sevenz;
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
/*    */ class StreamMap
/*    */ {
/*    */   int[] folderFirstPackStreamIndex;
/*    */   long[] packStreamOffsets;
/*    */   int[] folderFirstFileIndex;
/*    */   int[] fileFolderIndex;
/*    */   
/*    */   public String toString() {
/* 33 */     return "StreamMap with indices of " + this.folderFirstPackStreamIndex.length + " folders, offsets of " + this.packStreamOffsets.length + " packed streams, first files of " + this.folderFirstFileIndex.length + " folders and folder indices for " + this.fileFolderIndex.length + " files";
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\sevenz\StreamMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */