/*     */ package org.apache.commons.compress.archivers.ar;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import org.apache.commons.compress.archivers.ArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.ArchiveOutputStream;
/*     */ import org.apache.commons.compress.utils.ArchiveUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArArchiveOutputStream
/*     */   extends ArchiveOutputStream
/*     */ {
/*     */   public static final int LONGFILE_ERROR = 0;
/*     */   public static final int LONGFILE_BSD = 1;
/*     */   private final OutputStream out;
/*     */   private long entryOffset;
/*     */   private ArArchiveEntry prevEntry;
/*     */   private boolean haveUnclosedEntry;
/*  49 */   private int longFileMode = 0;
/*     */   
/*     */   private boolean finished;
/*     */ 
/*     */   
/*     */   public ArArchiveOutputStream(OutputStream pOut) {
/*  55 */     this.out = pOut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLongFileMode(int longFileMode) {
/*  67 */     this.longFileMode = longFileMode;
/*     */   }
/*     */   
/*     */   private void writeArchiveHeader() throws IOException {
/*  71 */     byte[] header = ArchiveUtils.toAsciiBytes("!<arch>\n");
/*  72 */     this.out.write(header);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeArchiveEntry() throws IOException {
/*  77 */     if (this.finished) {
/*  78 */       throw new IOException("Stream has already been finished");
/*     */     }
/*  80 */     if (this.prevEntry == null || !this.haveUnclosedEntry) {
/*  81 */       throw new IOException("No current entry to close");
/*     */     }
/*  83 */     if (this.entryOffset % 2L != 0L) {
/*  84 */       this.out.write(10);
/*     */     }
/*  86 */     this.haveUnclosedEntry = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putArchiveEntry(ArchiveEntry pEntry) throws IOException {
/*  91 */     if (this.finished) {
/*  92 */       throw new IOException("Stream has already been finished");
/*     */     }
/*     */     
/*  95 */     ArArchiveEntry pArEntry = (ArArchiveEntry)pEntry;
/*  96 */     if (this.prevEntry == null) {
/*  97 */       writeArchiveHeader();
/*     */     } else {
/*  99 */       if (this.prevEntry.getLength() != this.entryOffset) {
/* 100 */         throw new IOException("Length does not match entry (" + this.prevEntry.getLength() + " != " + this.entryOffset);
/*     */       }
/*     */       
/* 103 */       if (this.haveUnclosedEntry) {
/* 104 */         closeArchiveEntry();
/*     */       }
/*     */     } 
/*     */     
/* 108 */     this.prevEntry = pArEntry;
/*     */     
/* 110 */     writeEntryHeader(pArEntry);
/*     */     
/* 112 */     this.entryOffset = 0L;
/* 113 */     this.haveUnclosedEntry = true;
/*     */   }
/*     */   
/*     */   private long fill(long pOffset, long pNewOffset, char pFill) throws IOException {
/* 117 */     long diff = pNewOffset - pOffset;
/*     */     
/* 119 */     if (diff > 0L) {
/* 120 */       for (int i = 0; i < diff; i++) {
/* 121 */         write(pFill);
/*     */       }
/*     */     }
/*     */     
/* 125 */     return pNewOffset;
/*     */   }
/*     */   
/*     */   private long write(String data) throws IOException {
/* 129 */     byte[] bytes = data.getBytes(StandardCharsets.US_ASCII);
/* 130 */     write(bytes);
/* 131 */     return bytes.length;
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeEntryHeader(ArArchiveEntry pEntry) throws IOException {
/* 136 */     long offset = 0L;
/* 137 */     boolean mustAppendName = false;
/*     */     
/* 139 */     String n = pEntry.getName();
/* 140 */     int nLength = n.length();
/* 141 */     if (0 == this.longFileMode && nLength > 16) {
/* 142 */       throw new IOException("File name too long, > 16 chars: " + n);
/*     */     }
/* 144 */     if (1 == this.longFileMode && (nLength > 16 || n
/* 145 */       .contains(" "))) {
/* 146 */       mustAppendName = true;
/* 147 */       offset += write("#1/" + nLength);
/*     */     } else {
/* 149 */       offset += write(n);
/*     */     } 
/*     */     
/* 152 */     offset = fill(offset, 16L, ' ');
/* 153 */     String m = "" + pEntry.getLastModified();
/* 154 */     if (m.length() > 12) {
/* 155 */       throw new IOException("Last modified too long");
/*     */     }
/* 157 */     offset += write(m);
/*     */     
/* 159 */     offset = fill(offset, 28L, ' ');
/* 160 */     String u = "" + pEntry.getUserId();
/* 161 */     if (u.length() > 6) {
/* 162 */       throw new IOException("User id too long");
/*     */     }
/* 164 */     offset += write(u);
/*     */     
/* 166 */     offset = fill(offset, 34L, ' ');
/* 167 */     String g = "" + pEntry.getGroupId();
/* 168 */     if (g.length() > 6) {
/* 169 */       throw new IOException("Group id too long");
/*     */     }
/* 171 */     offset += write(g);
/*     */     
/* 173 */     offset = fill(offset, 40L, ' ');
/* 174 */     String fm = "" + Integer.toString(pEntry.getMode(), 8);
/* 175 */     if (fm.length() > 8) {
/* 176 */       throw new IOException("Filemode too long");
/*     */     }
/* 178 */     offset += write(fm);
/*     */     
/* 180 */     offset = fill(offset, 48L, ' ');
/*     */     
/* 182 */     String s = String.valueOf(pEntry.getLength() + (mustAppendName ? nLength : 0L));
/*     */     
/* 184 */     if (s.length() > 10) {
/* 185 */       throw new IOException("Size too long");
/*     */     }
/* 187 */     offset += write(s);
/*     */     
/* 189 */     offset = fill(offset, 58L, ' ');
/*     */     
/* 191 */     offset += write("`\n");
/*     */     
/* 193 */     if (mustAppendName) {
/* 194 */       offset += write(n);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 201 */     this.out.write(b, off, len);
/* 202 */     count(len);
/* 203 */     this.entryOffset += len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*     */     try {
/* 212 */       if (!this.finished) {
/* 213 */         finish();
/*     */       }
/*     */     } finally {
/* 216 */       this.out.close();
/* 217 */       this.prevEntry = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ArchiveEntry createArchiveEntry(File inputFile, String entryName) throws IOException {
/* 224 */     if (this.finished) {
/* 225 */       throw new IOException("Stream has already been finished");
/*     */     }
/* 227 */     return new ArArchiveEntry(inputFile, entryName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArchiveEntry createArchiveEntry(Path inputPath, String entryName, LinkOption... options) throws IOException {
/* 237 */     if (this.finished) {
/* 238 */       throw new IOException("Stream has already been finished");
/*     */     }
/* 240 */     return new ArArchiveEntry(inputPath, entryName, options);
/*     */   }
/*     */ 
/*     */   
/*     */   public void finish() throws IOException {
/* 245 */     if (this.haveUnclosedEntry) {
/* 246 */       throw new IOException("This archive contains unclosed entries.");
/*     */     }
/* 248 */     if (this.finished) {
/* 249 */       throw new IOException("This archive has already been finished");
/*     */     }
/* 251 */     this.finished = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\archivers\ar\ArArchiveOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */