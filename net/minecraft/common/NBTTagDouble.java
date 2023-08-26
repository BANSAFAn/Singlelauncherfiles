/*    */ package net.minecraft.common;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class NBTTagDouble
/*    */   extends NBTBase
/*    */ {
/*    */   public double data;
/*    */   
/*    */   public NBTTagDouble(String par1Str) {
/* 14 */     super(par1Str);
/*    */   }
/*    */   
/*    */   public NBTTagDouble(String par1Str, double par2) {
/* 18 */     super(par1Str);
/* 19 */     this.data = par2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput par1DataOutput) throws IOException {
/* 28 */     par1DataOutput.writeDouble(this.data);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void load(DataInput par1DataInput, int par2) throws IOException {
/* 37 */     this.data = par1DataInput.readDouble();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 45 */     return 6;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 50 */     return "" + this.data;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 58 */     return new NBTTagDouble(getName(), this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object par1Obj) {
/* 63 */     if (super.equals(par1Obj)) {
/* 64 */       NBTTagDouble var2 = (NBTTagDouble)par1Obj;
/* 65 */       return (this.data == var2.data);
/*    */     } 
/* 67 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 72 */     long var1 = Double.doubleToLongBits(this.data);
/* 73 */     return super.hashCode() ^ (int)(var1 ^ var1 >>> 32L);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\common\NBTTagDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */