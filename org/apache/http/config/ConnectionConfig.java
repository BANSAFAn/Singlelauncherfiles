/*     */ package org.apache.http.config;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import org.apache.http.Consts;
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
/*     */ @Immutable
/*     */ public class ConnectionConfig
/*     */   implements Cloneable
/*     */ {
/*  45 */   public static final ConnectionConfig DEFAULT = (new Builder()).build();
/*     */ 
/*     */   
/*     */   private final int bufferSize;
/*     */   
/*     */   private final int fragmentSizeHint;
/*     */   
/*     */   private final Charset charset;
/*     */   
/*     */   private final CodingErrorAction malformedInputAction;
/*     */   
/*     */   private final CodingErrorAction unmappableInputAction;
/*     */   
/*     */   private final MessageConstraints messageConstraints;
/*     */ 
/*     */   
/*     */   ConnectionConfig(int bufferSize, int fragmentSizeHint, Charset charset, CodingErrorAction malformedInputAction, CodingErrorAction unmappableInputAction, MessageConstraints messageConstraints) {
/*  62 */     this.bufferSize = bufferSize;
/*  63 */     this.fragmentSizeHint = fragmentSizeHint;
/*  64 */     this.charset = charset;
/*  65 */     this.malformedInputAction = malformedInputAction;
/*  66 */     this.unmappableInputAction = unmappableInputAction;
/*  67 */     this.messageConstraints = messageConstraints;
/*     */   }
/*     */   
/*     */   public int getBufferSize() {
/*  71 */     return this.bufferSize;
/*     */   }
/*     */   
/*     */   public int getFragmentSizeHint() {
/*  75 */     return this.fragmentSizeHint;
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/*  79 */     return this.charset;
/*     */   }
/*     */   
/*     */   public CodingErrorAction getMalformedInputAction() {
/*  83 */     return this.malformedInputAction;
/*     */   }
/*     */   
/*     */   public CodingErrorAction getUnmappableInputAction() {
/*  87 */     return this.unmappableInputAction;
/*     */   }
/*     */   
/*     */   public MessageConstraints getMessageConstraints() {
/*  91 */     return this.messageConstraints;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ConnectionConfig clone() throws CloneNotSupportedException {
/*  96 */     return (ConnectionConfig)super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     StringBuilder builder = new StringBuilder();
/* 102 */     builder.append("[bufferSize=").append(this.bufferSize).append(", fragmentSizeHint=").append(this.fragmentSizeHint).append(", charset=").append(this.charset).append(", malformedInputAction=").append(this.malformedInputAction).append(", unmappableInputAction=").append(this.unmappableInputAction).append(", messageConstraints=").append(this.messageConstraints).append("]");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 109 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 113 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder copy(ConnectionConfig config) {
/* 117 */     Args.notNull(config, "Connection config");
/* 118 */     return (new Builder()).setCharset(config.getCharset()).setMalformedInputAction(config.getMalformedInputAction()).setUnmappableInputAction(config.getUnmappableInputAction()).setMessageConstraints(config.getMessageConstraints());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private int bufferSize;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     private int fragmentSizeHint = -1; private Charset charset;
/*     */     private CodingErrorAction malformedInputAction;
/*     */     
/*     */     public Builder setBufferSize(int bufferSize) {
/* 139 */       this.bufferSize = bufferSize;
/* 140 */       return this;
/*     */     }
/*     */     private CodingErrorAction unmappableInputAction; private MessageConstraints messageConstraints;
/*     */     public Builder setFragmentSizeHint(int fragmentSizeHint) {
/* 144 */       this.fragmentSizeHint = fragmentSizeHint;
/* 145 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCharset(Charset charset) {
/* 149 */       this.charset = charset;
/* 150 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMalformedInputAction(CodingErrorAction malformedInputAction) {
/* 154 */       this.malformedInputAction = malformedInputAction;
/* 155 */       if (malformedInputAction != null && this.charset == null) {
/* 156 */         this.charset = Consts.ASCII;
/*     */       }
/* 158 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setUnmappableInputAction(CodingErrorAction unmappableInputAction) {
/* 162 */       this.unmappableInputAction = unmappableInputAction;
/* 163 */       if (unmappableInputAction != null && this.charset == null) {
/* 164 */         this.charset = Consts.ASCII;
/*     */       }
/* 166 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMessageConstraints(MessageConstraints messageConstraints) {
/* 170 */       this.messageConstraints = messageConstraints;
/* 171 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectionConfig build() {
/* 175 */       Charset cs = this.charset;
/* 176 */       if (cs == null && (this.malformedInputAction != null || this.unmappableInputAction != null)) {
/* 177 */         cs = Consts.ASCII;
/*     */       }
/* 179 */       int bufSize = (this.bufferSize > 0) ? this.bufferSize : 8192;
/* 180 */       int fragmentHintSize = (this.fragmentSizeHint >= 0) ? this.fragmentSizeHint : bufSize;
/* 181 */       return new ConnectionConfig(bufSize, fragmentHintSize, cs, this.malformedInputAction, this.unmappableInputAction, this.messageConstraints);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\http\config\ConnectionConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */