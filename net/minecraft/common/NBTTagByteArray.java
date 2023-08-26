/*    */ package net.minecraft.common;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ public class NBTTagByteArray
/*    */   extends NBTBase
/*    */ {
/*    */   public byte[] byteArray;
/*    */   
/*    */   public NBTTagByteArray(String par1Str) {
/* 15 */     super(par1Str);
/*    */   }
/*    */   
/*    */   public NBTTagByteArray(String par1Str, byte[] par2ArrayOfByte) {
/* 19 */     super(par1Str);
/* 20 */     this.byteArray = par2ArrayOfByte;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput par1DataOutput) throws IOException {
/* 29 */     par1DataOutput.writeInt(this.byteArray.length);
/* 30 */     par1DataOutput.write(this.byteArray);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void load(DataInput par1DataInput, int par2) throws IOException {
/* 39 */     int var3 = par1DataInput.readInt();
/* 40 */     this.byteArray = new byte[var3];
/* 41 */     par1DataInput.readFully(this.byteArray);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 49 */     return 7;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "[" + this.byteArray.length + " bytes]";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 62 */     byte[] var1 = new byte[this.byteArray.length];
/* 63 */     System.arraycopy(this.byteArray, 0, var1, 0, this.byteArray.length);
/* 64 */     return new NBTTagByteArray(getName(), var1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object par1Obj) {
/* 69 */     return super.equals(par1Obj) ? Arrays.equals(this.byteArray, ((NBTTagByteArray)par1Obj).byteArray) : false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 75 */     return super.hashCode() ^ Arrays.hashCode(this.byteArray);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\common\NBTTagByteArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */