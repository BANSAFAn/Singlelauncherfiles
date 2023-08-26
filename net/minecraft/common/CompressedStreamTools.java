/*     */ package net.minecraft.common;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompressedStreamTools
/*     */ {
/*     */   public static NBTTagCompound readCompressed(InputStream par0InputStream) throws IOException {
/*     */     NBTTagCompound var2;
/*  25 */     DataInputStream var1 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(par0InputStream)));
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  30 */       var2 = read(var1);
/*     */     } finally {
/*  32 */       var1.close();
/*     */     } 
/*     */     
/*  35 */     return var2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeCompressed(NBTTagCompound par0NBTTagCompound, OutputStream par1OutputStream) throws IOException {
/*  43 */     DataOutputStream var2 = new DataOutputStream(new GZIPOutputStream(par1OutputStream));
/*     */ 
/*     */     
/*     */     try {
/*  47 */       write(par0NBTTagCompound, var2);
/*     */     } finally {
/*  49 */       var2.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static NBTTagCompound decompress(byte[] par0ArrayOfByte) throws IOException {
/*     */     NBTTagCompound var2;
/*  55 */     DataInputStream var1 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(par0ArrayOfByte))));
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  60 */       var2 = read(var1);
/*     */     } finally {
/*  62 */       var1.close();
/*     */     } 
/*     */     
/*  65 */     return var2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] compress(NBTTagCompound par0NBTTagCompound) throws IOException {
/*  70 */     ByteArrayOutputStream var1 = new ByteArrayOutputStream();
/*  71 */     DataOutputStream var2 = new DataOutputStream(new GZIPOutputStream(var1));
/*     */     
/*     */     try {
/*  74 */       write(par0NBTTagCompound, var2);
/*     */     } finally {
/*  76 */       var2.close();
/*     */     } 
/*     */     
/*  79 */     return var1.toByteArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void safeWrite(NBTTagCompound par0NBTTagCompound, File par1File) throws IOException {
/*  84 */     File var2 = new File(par1File.getAbsolutePath() + "_tmp");
/*     */     
/*  86 */     if (var2.exists()) {
/*  87 */       var2.delete();
/*     */     }
/*     */     
/*  90 */     write(par0NBTTagCompound, var2);
/*     */     
/*  92 */     if (par1File.exists()) {
/*  93 */       par1File.delete();
/*     */     }
/*     */     
/*  96 */     if (par1File.exists()) {
/*  97 */       throw new IOException("Failed to delete " + par1File);
/*     */     }
/*     */     
/* 100 */     var2.renameTo(par1File);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void write(NBTTagCompound par0NBTTagCompound, File par1File) throws IOException {
/* 105 */     DataOutputStream var2 = new DataOutputStream(new FileOutputStream(par1File));
/*     */ 
/*     */     
/*     */     try {
/* 109 */       write(par0NBTTagCompound, var2);
/*     */     } finally {
/* 111 */       var2.close();
/*     */     } 
/*     */   }
/*     */   public static NBTTagCompound read(File par0File) throws IOException {
/*     */     NBTTagCompound var2;
/* 116 */     if (!par0File.exists()) {
/* 117 */       return null;
/*     */     }
/*     */     
/* 120 */     DataInputStream var1 = new DataInputStream(new FileInputStream(par0File));
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 125 */       var2 = read(var1);
/*     */     } finally {
/* 127 */       var1.close();
/*     */     } 
/*     */     
/* 130 */     return var2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NBTTagCompound read(DataInput par0DataInput) throws IOException {
/* 138 */     NBTBase var1 = NBTBase.readNamedTag(par0DataInput);
/*     */     
/* 140 */     if (var1 instanceof NBTTagCompound) {
/* 141 */       return (NBTTagCompound)var1;
/*     */     }
/* 143 */     throw new IOException("Root tag must be a named compound tag");
/*     */   }
/*     */ 
/*     */   
/*     */   public static void write(NBTTagCompound par0NBTTagCompound, DataOutput par1DataOutput) throws IOException {
/* 148 */     NBTBase.writeNamedTag(par0NBTTagCompound, par1DataOutput);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\net\minecraft\common\CompressedStreamTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */