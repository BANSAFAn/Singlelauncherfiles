/*    */ package org.tlauncher.tlauncher.entity.server;
/*    */ 
/*    */ 
/*    */ public class Server {
/*    */   private String name;
/*    */   private String address;
/*    */   private String ip;
/*    */   
/*  9 */   public void setName(String name) { this.name = name; } private String port; private boolean hideAddress; private int acceptTextures; public void setIp(String ip) { this.ip = ip; } public void setPort(String port) { this.port = port; } public void setHideAddress(boolean hideAddress) { this.hideAddress = hideAddress; } public void setAcceptTextures(int acceptTextures) { this.acceptTextures = acceptTextures; } public String toString() { return "Server(name=" + getName() + ", address=" + getAddress() + ", ip=" + getIp() + ", port=" + getPort() + ", hideAddress=" + isHideAddress() + ", acceptTextures=" + getAcceptTextures() + ")"; }
/* 10 */   public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof Server)) return false;  Server other = (Server)o; if (!other.canEqual(this)) return false;  Object this$address = getAddress(), other$address = other.getAddress(); return !((this$address == null) ? (other$address != null) : !this$address.equals(other$address)); } protected boolean canEqual(Object other) { return other instanceof Server; } public int hashCode() { int PRIME = 59; result = 1; Object $address = getAddress(); return result * 59 + (($address == null) ? 43 : $address.hashCode()); }
/*    */    public String getName() {
/* 12 */     return this.name;
/*    */   }
/* 14 */   public String getAddress() { return this.address; }
/* 15 */   public String getIp() { return this.ip; }
/* 16 */   public String getPort() { return this.port; }
/* 17 */   public boolean isHideAddress() { return this.hideAddress; } public int getAcceptTextures() {
/* 18 */     return this.acceptTextures;
/*    */   }
/*    */   private static String[] splitAddress(String address) {
/* 21 */     String[] array = StringUtils.split(address, ':');
/*    */     
/* 23 */     switch (array.length) {
/*    */       case 1:
/* 25 */         return new String[] { address, null };
/*    */       case 2:
/* 27 */         return new String[] { array[0], array[1] };
/*    */     } 
/* 29 */     throw new ParseException("split incorrectly " + address);
/*    */   }
/*    */   
/*    */   public NBTTagCompound getNBT() {
/* 33 */     NBTTagCompound compound = new NBTTagCompound();
/*    */     
/* 35 */     compound.setString("name", this.name);
/* 36 */     compound.setString("ip", this.address);
/* 37 */     compound.setBoolean("hideAddress", this.hideAddress);
/*    */     
/* 39 */     if (this.acceptTextures != 0) {
/* 40 */       compound.setBoolean("acceptTextures", (this.acceptTextures == 1));
/*    */     }
/* 42 */     return compound;
/*    */   }
/*    */   
/*    */   public static Server loadFromNBT(NBTTagCompound nbt) {
/* 46 */     Server server = new Server();
/*    */     
/* 48 */     server.setName(nbt.getString("name"));
/* 49 */     server.setAddress(nbt.getString("ip"));
/* 50 */     server.hideAddress = nbt.getBoolean("hideAddress");
/*    */     
/* 52 */     if (nbt.hasKey("acceptTextures")) {
/* 53 */       server.acceptTextures = nbt.getBoolean("acceptTextures") ? 1 : -1;
/*    */     }
/* 55 */     return server;
/*    */   }
/*    */   
/*    */   public void setAddress(String address) {
/* 59 */     if (address == null) {
/* 60 */       this.ip = null;
/* 61 */       this.port = null;
/*    */     } else {
/* 63 */       String[] split = splitAddress(address);
/* 64 */       this.ip = split[0];
/* 65 */       this.port = split[1];
/*    */     } 
/*    */     
/* 68 */     this.address = address;
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\server\Server.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */