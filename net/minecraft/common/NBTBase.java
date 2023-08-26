/*     */ package net.minecraft.common;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class NBTBase {
/*   8 */   public static final String[] NBTTypes = new String[] { "END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void write(DataOutput paramDataOutput) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void load(DataInput paramDataInput, int paramInt) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract byte getId();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   NBTBase(String par1Str) {
/*  35 */     if (par1Str == null) {
/*  36 */       this.name = "";
/*     */     } else {
/*  38 */       this.name = par1Str;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   NBTBase setName(String par1Str) {
/*  46 */     if (par1Str == null) {
/*  47 */       this.name = "";
/*     */     } else {
/*  49 */       this.name = par1Str;
/*     */     } 
/*     */     
/*  52 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getName() {
/*  59 */     return (this.name == null) ? "" : this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NBTBase readNamedTag(DataInput par0DataInput) throws IOException {
/*  68 */     return func_130104_b(par0DataInput, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   static NBTBase func_130104_b(DataInput par0DataInput, int par1) throws IOException {
/*  73 */     byte var2 = par0DataInput.readByte();
/*     */     
/*  75 */     if (var2 == 0) {
/*  76 */       return new NBTTagEnd();
/*     */     }
/*  78 */     String var3 = par0DataInput.readUTF();
/*  79 */     NBTBase var4 = newTag(var2, var3);
/*     */     
/*  81 */     var4.load(par0DataInput, par1);
/*  82 */     return var4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeNamedTag(NBTBase par0NBTBase, DataOutput par1DataOutput) throws IOException {
/*  91 */     par1DataOutput.writeByte(par0NBTBase.getId());
/*     */     
/*  93 */     if (par0NBTBase.getId() != 0) {
/*  94 */       par1DataOutput.writeUTF(par0NBTBase.getName());
/*  95 */       par0NBTBase.write(par1DataOutput);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static NBTBase newTag(byte par0, String par1Str) {
/* 103 */     switch (par0) {
/*     */       case 0:
/* 105 */         return new NBTTagEnd();
/*     */       
/*     */       case 1:
/* 108 */         return new NBTTagByte(par1Str);
/*     */       
/*     */       case 2:
/* 111 */         return new NBTTagShort(par1Str);
/*     */       
/*     */       case 3:
/* 114 */         return new NBTTagInt(par1Str);
/*     */       
/*     */       case 4:
/* 117 */         return new NBTTagLong(par1Str);
/*     */       
/*     */       case 5:
/* 120 */         return new NBTTagFloat(par1Str);
/*     */       
/*     */       case 6:
/* 123 */         return new NBTTagDouble(par1Str);
/*     */       
/*     */       case 7:
/* 126 */         return new NBTTagByteArray(par1Str);
/*     */       
/*     */       case 8:
/* 129 */         return new NBTTagString(par1Str);
/*     */       
/*     */       case 9:
/* 132 */         return new NBTTagList(par1Str);
/*     */       
/*     */       case 10:
/* 135 */         return new NBTTagCompound(par1Str);
/*     */       
/*     */       case 11:
/* 138 */         return new NBTTagIntArray(par1Str);
/*     */     } 
/*     */     
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getTagName(byte par0) {
/* 150 */     switch (par0) {
/*     */       case 0:
/* 152 */         return "TAG_End";
/*     */       
/*     */       case 1:
/* 155 */         return "TAG_Byte";
/*     */       
/*     */       case 2:
/* 158 */         return "TAG_Short";
/*     */       
/*     */       case 3:
/* 161 */         return "TAG_Int";
/*     */       
/*     */       case 4:
/* 164 */         return "TAG_Long";
/*     */       
/*     */       case 5:
/* 167 */         return "TAG_Float";
/*     */       
/*     */       case 6:
/* 170 */         return "TAG_Double";
/*     */       
/*     */       case 7:
/* 173 */         return "TAG_Byte_Array";
/*     */       
/*     */       case 8:
/* 176 */         return "TAG_String";
/*     */       
/*     */       case 9:
/* 179 */         return "TAG_List";
/*     */       
/*     */       case 10:
/* 182 */         return "TAG_Compound";
/*     */       
/*     */       case 11:
/* 185 */         return "TAG_Int_Array";
/*     */     } 
/*     */     
/* 188 */     return "UNKNOWN";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract NBTBase copy();
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object par1Obj) {
/* 199 */     if (!(par1Obj instanceof NBTBase)) {
/* 200 */       return false;
/*     */     }
/* 202 */     NBTBase var2 = (NBTBase)par1Obj;
/* 203 */     return (getId() == var2.getId() && (this.name != null || var2.name == null) && (this.name == null || var2.name != null) && (this.name == null || this.name
/*     */ 
/*     */       
/* 206 */       .equals(var2.name)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 211 */     return this.name.hashCode() ^ getId();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\common\NBTBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */