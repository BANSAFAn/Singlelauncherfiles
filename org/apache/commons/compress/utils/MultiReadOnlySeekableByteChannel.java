/*     */ package org.apache.commons.compress.utils;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.NonWritableChannelException;
/*     */ import java.nio.channels.SeekableByteChannel;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
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
/*     */ public class MultiReadOnlySeekableByteChannel
/*     */   implements SeekableByteChannel
/*     */ {
/*  48 */   private static final Path[] EMPTY_PATH_ARRAY = new Path[0];
/*     */ 
/*     */   
/*     */   private final List<SeekableByteChannel> channels;
/*     */ 
/*     */   
/*     */   private long globalPosition;
/*     */   
/*     */   private int currentChannelIdx;
/*     */ 
/*     */   
/*     */   public MultiReadOnlySeekableByteChannel(List<SeekableByteChannel> channels) {
/*  60 */     this.channels = Collections.unmodifiableList(new ArrayList<>(
/*  61 */           Objects.<Collection<? extends SeekableByteChannel>>requireNonNull(channels, "channels must not be null")));
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int read(ByteBuffer dst) throws IOException {
/*  66 */     if (!isOpen()) {
/*  67 */       throw new ClosedChannelException();
/*     */     }
/*  69 */     if (!dst.hasRemaining()) {
/*  70 */       return 0;
/*     */     }
/*     */     
/*  73 */     int totalBytesRead = 0;
/*  74 */     while (dst.hasRemaining() && this.currentChannelIdx < this.channels.size()) {
/*  75 */       SeekableByteChannel currentChannel = this.channels.get(this.currentChannelIdx);
/*  76 */       int newBytesRead = currentChannel.read(dst);
/*  77 */       if (newBytesRead == -1) {
/*     */         
/*  79 */         this.currentChannelIdx++;
/*     */         continue;
/*     */       } 
/*  82 */       if (currentChannel.position() >= currentChannel.size())
/*     */       {
/*  84 */         this.currentChannelIdx++;
/*     */       }
/*  86 */       totalBytesRead += newBytesRead;
/*     */     } 
/*  88 */     if (totalBytesRead > 0) {
/*  89 */       this.globalPosition += totalBytesRead;
/*  90 */       return totalBytesRead;
/*     */     } 
/*  92 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  97 */     IOException first = null;
/*  98 */     for (SeekableByteChannel ch : this.channels) {
/*     */       try {
/* 100 */         ch.close();
/* 101 */       } catch (IOException ex) {
/* 102 */         if (first == null) {
/* 103 */           first = ex;
/*     */         }
/*     */       } 
/*     */     } 
/* 107 */     if (first != null) {
/* 108 */       throw new IOException("failed to close wrapped channel", first);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpen() {
/* 114 */     return this.channels.stream().allMatch(Channel::isOpen);
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
/*     */   public long position() {
/* 126 */     return this.globalPosition;
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
/*     */   public synchronized SeekableByteChannel position(long channelNumber, long relativeOffset) throws IOException {
/* 138 */     if (!isOpen()) {
/* 139 */       throw new ClosedChannelException();
/*     */     }
/* 141 */     long globalPosition = relativeOffset;
/* 142 */     for (int i = 0; i < channelNumber; i++) {
/* 143 */       globalPosition += ((SeekableByteChannel)this.channels.get(i)).size();
/*     */     }
/*     */     
/* 146 */     return position(globalPosition);
/*     */   }
/*     */ 
/*     */   
/*     */   public long size() throws IOException {
/* 151 */     if (!isOpen()) {
/* 152 */       throw new ClosedChannelException();
/*     */     }
/* 154 */     long acc = 0L;
/* 155 */     for (SeekableByteChannel ch : this.channels) {
/* 156 */       acc += ch.size();
/*     */     }
/* 158 */     return acc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SeekableByteChannel truncate(long size) {
/* 166 */     throw new NonWritableChannelException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) {
/* 174 */     throw new NonWritableChannelException();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized SeekableByteChannel position(long newPosition) throws IOException {
/* 179 */     if (newPosition < 0L) {
/* 180 */       throw new IOException("Negative position: " + newPosition);
/*     */     }
/* 182 */     if (!isOpen()) {
/* 183 */       throw new ClosedChannelException();
/*     */     }
/*     */     
/* 186 */     this.globalPosition = newPosition;
/*     */     
/* 188 */     long pos = newPosition;
/*     */     
/* 190 */     for (int i = 0; i < this.channels.size(); i++) {
/* 191 */       long newChannelPos; SeekableByteChannel currentChannel = this.channels.get(i);
/* 192 */       long size = currentChannel.size();
/*     */ 
/*     */       
/* 195 */       if (pos == -1L) {
/*     */ 
/*     */         
/* 198 */         newChannelPos = 0L;
/* 199 */       } else if (pos <= size) {
/*     */         
/* 201 */         this.currentChannelIdx = i;
/* 202 */         long tmp = pos;
/* 203 */         pos = -1L;
/* 204 */         newChannelPos = tmp;
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 209 */         pos -= size;
/* 210 */         newChannelPos = size;
/*     */       } 
/*     */       
/* 213 */       currentChannel.position(newChannelPos);
/*     */     } 
/* 215 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SeekableByteChannel forSeekableByteChannels(SeekableByteChannel... channels) {
/* 226 */     if (((SeekableByteChannel[])Objects.requireNonNull((T)channels, "channels must not be null")).length == 1) {
/* 227 */       return channels[0];
/*     */     }
/* 229 */     return new MultiReadOnlySeekableByteChannel(Arrays.asList(channels));
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
/*     */   public static SeekableByteChannel forFiles(File... files) throws IOException {
/* 241 */     List<Path> paths = new ArrayList<>();
/* 242 */     for (File f : (File[])Objects.<File[]>requireNonNull(files, "files must not be null")) {
/* 243 */       paths.add(f.toPath());
/*     */     }
/*     */     
/* 246 */     return forPaths(paths.<Path>toArray(EMPTY_PATH_ARRAY));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static SeekableByteChannel forPaths(Path... paths) throws IOException {
/* 261 */     List<SeekableByteChannel> channels = new ArrayList<>();
/* 262 */     for (Path path : (Path[])Objects.<Path[]>requireNonNull(paths, "paths must not be null")) {
/* 263 */       channels.add(Files.newByteChannel(path, new OpenOption[] { StandardOpenOption.READ }));
/*     */     } 
/* 265 */     if (channels.size() == 1) {
/* 266 */       return channels.get(0);
/*     */     }
/* 268 */     return new MultiReadOnlySeekableByteChannel(channels);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compres\\utils\MultiReadOnlySeekableByteChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */