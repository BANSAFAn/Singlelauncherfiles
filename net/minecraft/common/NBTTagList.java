/*     */ package net.minecraft.common;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NBTTagList
/*     */   extends NBTBase
/*     */ {
/*  15 */   private List tagList = new ArrayList();
/*     */ 
/*     */ 
/*     */   
/*     */   private byte tagType;
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagList() {
/*  24 */     super("");
/*     */   }
/*     */   
/*     */   public NBTTagList(String par1Str) {
/*  28 */     super(par1Str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void write(DataOutput par1DataOutput) throws IOException {
/*  37 */     if (!this.tagList.isEmpty()) {
/*  38 */       this.tagType = ((NBTBase)this.tagList.get(0)).getId();
/*     */     } else {
/*  40 */       this.tagType = 1;
/*     */     } 
/*     */     
/*  43 */     par1DataOutput.writeByte(this.tagType);
/*  44 */     par1DataOutput.writeInt(this.tagList.size());
/*     */     
/*  46 */     for (int var2 = 0; var2 < this.tagList.size(); var2++) {
/*  47 */       ((NBTBase)this.tagList.get(var2)).write(par1DataOutput);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void load(DataInput par1DataInput, int par2) throws IOException {
/*  57 */     if (par2 > 512) {
/*  58 */       throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
/*     */     }
/*     */ 
/*     */     
/*  62 */     this.tagType = par1DataInput.readByte();
/*  63 */     int var3 = par1DataInput.readInt();
/*  64 */     this.tagList = new ArrayList();
/*     */     
/*  66 */     for (int var4 = 0; var4 < var3; var4++) {
/*  67 */       NBTBase var5 = NBTBase.newTag(this.tagType, (String)null);
/*  68 */       var5.load(par1DataInput, par2 + 1);
/*  69 */       this.tagList.add(var5);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  78 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  83 */     return "" + this.tagList.size() + " entries of type " + 
/*  84 */       NBTBase.getTagName(this.tagType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendTag(NBTBase par1NBTBase) {
/*  92 */     this.tagType = par1NBTBase.getId();
/*  93 */     this.tagList.add(par1NBTBase);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTBase removeTag(int par1) {
/* 100 */     return this.tagList.remove(par1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTBase tagAt(int par1) {
/* 107 */     return this.tagList.get(par1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int tagCount() {
/* 114 */     return this.tagList.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTBase copy() {
/* 122 */     NBTTagList var1 = new NBTTagList(getName());
/* 123 */     var1.tagType = this.tagType;
/* 124 */     Iterator<NBTBase> var2 = this.tagList.iterator();
/*     */     
/* 126 */     while (var2.hasNext()) {
/* 127 */       NBTBase var3 = var2.next();
/* 128 */       NBTBase var4 = var3.copy();
/* 129 */       var1.tagList.add(var4);
/*     */     } 
/*     */     
/* 132 */     return var1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object par1Obj) {
/* 137 */     if (super.equals(par1Obj)) {
/* 138 */       NBTTagList var2 = (NBTTagList)par1Obj;
/*     */       
/* 140 */       if (this.tagType == var2.tagType) {
/* 141 */         return this.tagList.equals(var2.tagList);
/*     */       }
/*     */     } 
/*     */     
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 150 */     return super.hashCode() ^ this.tagList.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\common\NBTTagList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */