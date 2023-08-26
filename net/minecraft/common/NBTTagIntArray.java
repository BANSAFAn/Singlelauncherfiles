/*    */ package net.minecraft.common;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ public class NBTTagIntArray
/*    */   extends NBTBase
/*    */ {
/*    */   public int[] intArray;
/*    */   
/*    */   public NBTTagIntArray(String par1Str) {
/* 15 */     super(par1Str);
/*    */   }
/*    */   
/*    */   public NBTTagIntArray(String par1Str, int[] par2ArrayOfInteger) {
/* 19 */     super(par1Str);
/* 20 */     this.intArray = par2ArrayOfInteger;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput par1DataOutput) throws IOException {
/* 29 */     par1DataOutput.writeInt(this.intArray.length);
/*    */     
/* 31 */     for (int var2 = 0; var2 < this.intArray.length; var2++) {
/* 32 */       par1DataOutput.writeInt(this.intArray[var2]);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void load(DataInput par1DataInput, int par2) throws IOException {
/* 42 */     int var3 = par1DataInput.readInt();
/* 43 */     this.intArray = new int[var3];
/*    */     
/* 45 */     for (int var4 = 0; var4 < var3; var4++) {
/* 46 */       this.intArray[var4] = par1DataInput.readInt();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 55 */     return 11;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return "[" + this.intArray.length + " bytes]";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 68 */     int[] var1 = new int[this.intArray.length];
/* 69 */     System.arraycopy(this.intArray, 0, var1, 0, this.intArray.length);
/* 70 */     return new NBTTagIntArray(getName(), var1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object par1Obj) {
/* 75 */     if (!super.equals(par1Obj)) {
/* 76 */       return false;
/*    */     }
/* 78 */     NBTTagIntArray var2 = (NBTTagIntArray)par1Obj;
/* 79 */     return ((this.intArray == null && var2.intArray == null) || (this.intArray != null && 
/*    */       
/* 81 */       Arrays.equals(this.intArray, var2.intArray)));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 86 */     return super.hashCode() ^ Arrays.hashCode(this.intArray);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\common\NBTTagIntArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */