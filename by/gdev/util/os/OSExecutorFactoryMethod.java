/*    */ package by.gdev.util.os;
/*    */ 
/*    */ import by.gdev.util.OSInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OSExecutorFactoryMethod
/*    */ {
/*    */   public String toString() {
/* 10 */     return "OSExecutorFactoryMethod(osType=" + getOsType() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object $osType = getOsType(); return result * 59 + (($osType == null) ? 43 : $osType.hashCode()); } protected boolean canEqual(Object other) { return other instanceof OSExecutorFactoryMethod; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof OSExecutorFactoryMethod)) return false;  OSExecutorFactoryMethod other = (OSExecutorFactoryMethod)o; if (!other.canEqual(this)) return false;  Object this$osType = getOsType(), other$osType = other.getOsType(); return !((this$osType == null) ? (other$osType != null) : !this$osType.equals(other$osType)); } public void setOsType(OSInfo.OSType osType) { this.osType = osType; }
/*    */   
/* 12 */   private OSInfo.OSType osType = OSInfo.getOSType(); public OSInfo.OSType getOsType() { return this.osType; }
/*    */   
/*    */   public OSExecutor createOsExecutor() {
/* 15 */     switch (this.osType)
/*    */     
/*    */     { default:
/* 18 */         return new WindowsExecutor();
/*    */       case LINUX:
/* 20 */         return new LinuxExecutor();
/*    */       case MACOSX:
/* 22 */         break; }  return new MacExecutor();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\by\gde\\util\os\OSExecutorFactoryMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */