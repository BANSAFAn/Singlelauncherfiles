/*     */ package org.apache.http;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
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
/*     */ @Immutable
/*     */ public class ProtocolVersion
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 8950662842175091068L;
/*     */   protected final String protocol;
/*     */   protected final int major;
/*     */   protected final int minor;
/*     */   
/*     */   public ProtocolVersion(String protocol, int major, int minor) {
/*  71 */     this.protocol = (String)Args.notNull(protocol, "Protocol name");
/*  72 */     this.major = Args.notNegative(major, "Protocol minor version");
/*  73 */     this.minor = Args.notNegative(minor, "Protocol minor version");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getProtocol() {
/*  82 */     return this.protocol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMajor() {
/*  91 */     return this.major;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getMinor() {
/* 100 */     return this.minor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion forVersion(int major, int minor) {
/* 122 */     if (major == this.major && minor == this.minor) {
/* 123 */       return this;
/*     */     }
/*     */ 
/*     */     
/* 127 */     return new ProtocolVersion(this.protocol, major, minor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 138 */     return this.protocol.hashCode() ^ this.major * 100000 ^ this.minor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean equals(Object obj) {
/* 157 */     if (this == obj) {
/* 158 */       return true;
/*     */     }
/* 160 */     if (!(obj instanceof ProtocolVersion)) {
/* 161 */       return false;
/*     */     }
/* 163 */     ProtocolVersion that = (ProtocolVersion)obj;
/*     */     
/* 165 */     return (this.protocol.equals(that.protocol) && this.major == that.major && this.minor == that.minor);
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
/*     */ 
/*     */   
/*     */   public boolean isComparable(ProtocolVersion that) {
/* 182 */     return (that != null && this.protocol.equals(that.protocol));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareToVersion(ProtocolVersion that) {
/* 203 */     Args.notNull(that, "Protocol version");
/* 204 */     Args.check(this.protocol.equals(that.protocol), "Versions for different protocols cannot be compared: %s %s", new Object[] { this, that });
/*     */     
/* 206 */     int delta = getMajor() - that.getMajor();
/* 207 */     if (delta == 0) {
/* 208 */       delta = getMinor() - that.getMinor();
/*     */     }
/* 210 */     return delta;
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
/*     */   public final boolean greaterEquals(ProtocolVersion version) {
/* 225 */     return (isComparable(version) && compareToVersion(version) >= 0);
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
/*     */   public final boolean lessEquals(ProtocolVersion version) {
/* 240 */     return (isComparable(version) && compareToVersion(version) <= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 251 */     StringBuilder buffer = new StringBuilder();
/* 252 */     buffer.append(this.protocol);
/* 253 */     buffer.append('/');
/* 254 */     buffer.append(Integer.toString(this.major));
/* 255 */     buffer.append('.');
/* 256 */     buffer.append(Integer.toString(this.minor));
/* 257 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 262 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\ProtocolVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */