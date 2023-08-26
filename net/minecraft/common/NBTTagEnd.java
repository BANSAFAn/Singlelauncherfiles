/*    */ package net.minecraft.common;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class NBTTagEnd extends NBTBase {
/*    */   public NBTTagEnd() {
/*  9 */     super((String)null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void load(DataInput par1DataInput, int par2) throws IOException {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput par1DataOutput) throws IOException {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 33 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 38 */     return "END";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 46 */     return new NBTTagEnd();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\common\NBTTagEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */