/*     */ package net.minecraft.common;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import org.tlauncher.util.U;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NBTTagCompound
/*     */   extends NBTBase
/*     */ {
/*  19 */   private Map tagMap = new HashMap<>();
/*     */   
/*     */   public NBTTagCompound() {
/*  22 */     super("");
/*     */   }
/*     */   
/*     */   public NBTTagCompound(String par1Str) {
/*  26 */     super(par1Str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void write(DataOutput par1DataOutput) throws IOException {
/*  35 */     Iterator<NBTBase> var2 = this.tagMap.values().iterator();
/*     */     
/*  37 */     while (var2.hasNext()) {
/*  38 */       NBTBase var3 = var2.next();
/*  39 */       NBTBase.writeNamedTag(var3, par1DataOutput);
/*     */     } 
/*     */     
/*  42 */     par1DataOutput.writeByte(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void load(DataInput par1DataInput, int par2) throws IOException {
/*  51 */     if (par2 > 512) {
/*  52 */       throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
/*     */     }
/*     */     
/*  55 */     this.tagMap.clear();
/*     */     
/*     */     NBTBase var3;
/*  58 */     while ((var3 = NBTBase.func_130104_b(par1DataInput, par2 + 1))
/*  59 */       .getId() != 0) {
/*  60 */       this.tagMap.put(var3.getName(), var3);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection getTags() {
/*  68 */     return this.tagMap.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  76 */     return 10;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTag(String par1Str, NBTBase par2NBTBase) {
/*  84 */     this.tagMap.put(par1Str, par2NBTBase.setName(par1Str));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setByte(String par1Str, byte par2) {
/*  92 */     this.tagMap.put(par1Str, new NBTTagByte(par1Str, par2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShort(String par1Str, short par2) {
/* 100 */     this.tagMap.put(par1Str, new NBTTagShort(par1Str, par2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInteger(String par1Str, int par2) {
/* 108 */     this.tagMap.put(par1Str, new NBTTagInt(par1Str, par2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLong(String par1Str, long par2) {
/* 116 */     this.tagMap.put(par1Str, new NBTTagLong(par1Str, par2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFloat(String par1Str, float par2) {
/* 124 */     this.tagMap.put(par1Str, new NBTTagFloat(par1Str, par2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDouble(String par1Str, double par2) {
/* 132 */     this.tagMap.put(par1Str, new NBTTagDouble(par1Str, par2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setString(String par1Str, String par2Str) {
/* 140 */     this.tagMap.put(par1Str, new NBTTagString(par1Str, par2Str));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setByteArray(String par1Str, byte[] par2ArrayOfByte) {
/* 148 */     this.tagMap.put(par1Str, new NBTTagByteArray(par1Str, par2ArrayOfByte));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIntArray(String par1Str, int[] par2ArrayOfInteger) {
/* 156 */     this.tagMap.put(par1Str, new NBTTagIntArray(par1Str, par2ArrayOfInteger));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompoundTag(String par1Str, NBTTagCompound par2NBTTagCompound) {
/* 164 */     this.tagMap.put(par1Str, par2NBTTagCompound.setName(par1Str));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBoolean(String par1Str, boolean par2) {
/* 172 */     setByte(par1Str, (byte)(par2 ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTBase getTag(String par1Str) {
/* 179 */     return (NBTBase)this.tagMap.get(par1Str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasKey(String par1Str) {
/* 187 */     return this.tagMap.containsKey(par1Str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte getByte(String par1Str) {
/*     */     try {
/* 196 */       return !this.tagMap.containsKey(par1Str) ? 0 : ((NBTTagByte)this.tagMap
/* 197 */         .get(par1Str)).data;
/* 198 */     } catch (ClassCastException var3) {
/* 199 */       throw new RuntimeException("Error parsing NBT:" + 
/* 200 */           U.toLog(new Object[] { par1Str, Integer.valueOf(1), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getShort(String par1Str) {
/*     */     try {
/* 210 */       return !this.tagMap.containsKey(par1Str) ? 0 : ((NBTTagShort)this.tagMap
/* 211 */         .get(par1Str)).data;
/* 212 */     } catch (ClassCastException var3) {
/* 213 */       throw new RuntimeException("Error parsing NBT:" + 
/* 214 */           U.toLog(new Object[] { par1Str, Integer.valueOf(2), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInteger(String par1Str) {
/*     */     try {
/* 224 */       return !this.tagMap.containsKey(par1Str) ? 0 : ((NBTTagInt)this.tagMap
/* 225 */         .get(par1Str)).data;
/* 226 */     } catch (ClassCastException var3) {
/* 227 */       throw new RuntimeException("Error parsing NBT:" + 
/* 228 */           U.toLog(new Object[] { par1Str, Integer.valueOf(3), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLong(String par1Str) {
/*     */     try {
/* 238 */       return !this.tagMap.containsKey(par1Str) ? 0L : ((NBTTagLong)this.tagMap
/* 239 */         .get(par1Str)).data;
/* 240 */     } catch (ClassCastException var3) {
/* 241 */       throw new RuntimeException("Error parsing NBT:" + 
/* 242 */           U.toLog(new Object[] { par1Str, Integer.valueOf(4), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFloat(String par1Str) {
/*     */     try {
/* 252 */       return !this.tagMap.containsKey(par1Str) ? 0.0F : ((NBTTagFloat)this.tagMap
/* 253 */         .get(par1Str)).data;
/* 254 */     } catch (ClassCastException var3) {
/* 255 */       throw new RuntimeException("Error parsing NBT:" + 
/* 256 */           U.toLog(new Object[] { par1Str, Integer.valueOf(5), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDouble(String par1Str) {
/*     */     try {
/* 266 */       return !this.tagMap.containsKey(par1Str) ? 0.0D : ((NBTTagDouble)this.tagMap
/* 267 */         .get(par1Str)).data;
/* 268 */     } catch (ClassCastException var3) {
/* 269 */       throw new RuntimeException("Error parsing NBT:" + 
/* 270 */           U.toLog(new Object[] { par1Str, Integer.valueOf(6), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(String par1Str) {
/*     */     try {
/* 280 */       return !this.tagMap.containsKey(par1Str) ? "" : ((NBTTagString)this.tagMap
/* 281 */         .get(par1Str)).data;
/* 282 */     } catch (ClassCastException var3) {
/* 283 */       throw new RuntimeException("Error parsing NBT:" + 
/* 284 */           U.toLog(new Object[] { par1Str, Integer.valueOf(8), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getByteArray(String par1Str) {
/*     */     try {
/* 294 */       return !this.tagMap.containsKey(par1Str) ? new byte[0] : ((NBTTagByteArray)this.tagMap
/* 295 */         .get(par1Str)).byteArray;
/* 296 */     } catch (ClassCastException var3) {
/* 297 */       throw new RuntimeException("Error parsing NBT:" + 
/* 298 */           U.toLog(new Object[] { par1Str, Integer.valueOf(7), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getIntArray(String par1Str) {
/*     */     try {
/* 308 */       return !this.tagMap.containsKey(par1Str) ? new int[0] : ((NBTTagIntArray)this.tagMap
/* 309 */         .get(par1Str)).intArray;
/* 310 */     } catch (ClassCastException var3) {
/* 311 */       throw new RuntimeException("Error parsing NBT:" + 
/* 312 */           U.toLog(new Object[] { par1Str, Integer.valueOf(11), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagCompound getCompoundTag(String par1Str) {
/*     */     try {
/* 322 */       return !this.tagMap.containsKey(par1Str) ? new NBTTagCompound(par1Str) : (NBTTagCompound)this.tagMap
/* 323 */         .get(par1Str);
/* 324 */     } catch (ClassCastException var3) {
/* 325 */       throw new RuntimeException("Error parsing NBT:" + 
/* 326 */           U.toLog(new Object[] { par1Str, Integer.valueOf(10), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagList getTagList(String par1Str) {
/*     */     try {
/* 336 */       return !this.tagMap.containsKey(par1Str) ? new NBTTagList(par1Str) : (NBTTagList)this.tagMap
/* 337 */         .get(par1Str);
/* 338 */     } catch (ClassCastException var3) {
/* 339 */       throw new RuntimeException("Error parsing NBT:" + 
/* 340 */           U.toLog(new Object[] { par1Str, Integer.valueOf(9), var3 }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String par1Str) {
/* 349 */     return (getByte(par1Str) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTag(String par1Str) {
/* 356 */     this.tagMap.remove(par1Str);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 361 */     String var1 = getName() + ":[";
/*     */ 
/*     */     
/* 364 */     for (Iterator<String> var2 = this.tagMap.keySet().iterator(); var2.hasNext(); 
/* 365 */       var1 = var1 + var3 + ":" + this.tagMap.get(var3) + ",") {
/* 366 */       String var3 = var2.next();
/*     */     }
/*     */     
/* 369 */     return var1 + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNoTags() {
/* 376 */     return this.tagMap.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTBase copy() {
/* 384 */     NBTTagCompound var1 = new NBTTagCompound(getName());
/* 385 */     Iterator<String> var2 = this.tagMap.keySet().iterator();
/*     */     
/* 387 */     while (var2.hasNext()) {
/* 388 */       String var3 = var2.next();
/* 389 */       var1.setTag(var3, ((NBTBase)this.tagMap.get(var3)).copy());
/*     */     } 
/*     */     
/* 392 */     return var1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object par1Obj) {
/* 397 */     if (super.equals(par1Obj)) {
/* 398 */       NBTTagCompound var2 = (NBTTagCompound)par1Obj;
/* 399 */       return this.tagMap.entrySet().equals(var2.tagMap.entrySet());
/*     */     } 
/* 401 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 406 */     return super.hashCode() ^ this.tagMap.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Map getTagMap(NBTTagCompound par0NBTTagCompound) {
/* 413 */     return par0NBTTagCompound.tagMap;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\common\NBTTagCompound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */