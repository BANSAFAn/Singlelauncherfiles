/*    */ package net.minecraft.common;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class NBTTagString
/*    */   extends NBTBase
/*    */ {
/*    */   public String data;
/*    */   
/*    */   public NBTTagString(String par1Str) {
/* 14 */     super(par1Str);
/*    */   }
/*    */   
/*    */   public NBTTagString(String par1Str, String par2Str) {
/* 18 */     super(par1Str);
/* 19 */     this.data = par2Str;
/*    */     
/* 21 */     if (par2Str == null) {
/* 22 */       throw new IllegalArgumentException("Empty string not allowed");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput par1DataOutput) throws IOException {
/* 32 */     par1DataOutput.writeUTF(this.data);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void load(DataInput par1DataInput, int par2) throws IOException {
/* 41 */     this.data = par1DataInput.readUTF();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 49 */     return 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "" + this.data;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 62 */     return new NBTTagString(getName(), this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object par1Obj) {
/* 67 */     if (!super.equals(par1Obj)) {
/* 68 */       return false;
/*    */     }
/*    */     
/* 71 */     NBTTagString var2 = (NBTTagString)par1Obj;
/* 72 */     return ((this.data == null && var2.data == null) || (this.data != null && this.data
/* 73 */       .equals(var2.data)));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 78 */     return super.hashCode() ^ this.data.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\common\NBTTagString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */