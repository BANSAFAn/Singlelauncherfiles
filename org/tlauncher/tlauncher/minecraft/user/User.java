/*    */ package org.tlauncher.tlauncher.minecraft.user;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*    */ import org.apache.commons.lang3.builder.ToStringStyle;
/*    */ 
/*    */ public abstract class User
/*    */ {
/*    */   public abstract String getUsername();
/*    */   
/*    */   public abstract String getDisplayName();
/*    */   
/*    */   public abstract UUID getUUID();
/*    */   
/*    */   public abstract String getType();
/*    */   
/*    */   protected abstract boolean equals(User paramUser);
/*    */   
/*    */   public abstract int hashCode();
/*    */   
/*    */   public abstract LoginCredentials getLoginCredentials();
/*    */   
/*    */   public boolean equals(Object obj) {
/* 24 */     if (!(obj instanceof User))
/* 25 */       return false; 
/* 26 */     User user = (User)obj;
/* 27 */     return (getType().equals(user.getType()) && equals(user));
/*    */   }
/*    */   
/*    */   protected ToStringBuilder toStringBuilder() {
/* 31 */     return (new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE))
/* 32 */       .append("username", getUsername())
/* 33 */       .append("displayName", getDisplayName())
/* 34 */       .append("uuid", getUUID())
/* 35 */       .append("type", getType());
/*    */   }
/*    */   
/*    */   public final String toString() {
/* 39 */     return toStringBuilder().build();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\minecraf\\user\User.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */