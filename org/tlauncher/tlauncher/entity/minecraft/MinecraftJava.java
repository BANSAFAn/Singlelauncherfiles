/*    */ package org.tlauncher.tlauncher.entity.minecraft;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class MinecraftJava
/*    */ {
/*    */   public String toString() {
/* 11 */     return "MinecraftJava(jvm=" + getJvm() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object<Long, CompleteMinecraftJava> $jvm = (Object<Long, CompleteMinecraftJava>)getJvm(); return result * 59 + (($jvm == null) ? 43 : $jvm.hashCode()); } protected boolean canEqual(Object other) { return other instanceof MinecraftJava; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof MinecraftJava)) return false;  MinecraftJava other = (MinecraftJava)o; if (!other.canEqual(this)) return false;  Object<Long, CompleteMinecraftJava> this$jvm = (Object<Long, CompleteMinecraftJava>)getJvm(), other$jvm = (Object<Long, CompleteMinecraftJava>)other.getJvm(); return !((this$jvm == null) ? (other$jvm != null) : !this$jvm.equals(other$jvm)); } public void setJvm(Map<Long, CompleteMinecraftJava> jvm) { this.jvm = jvm; }
/*    */   
/* 13 */   private Map<Long, CompleteMinecraftJava> jvm = Collections.synchronizedMap(new HashMap<>()); public Map<Long, CompleteMinecraftJava> getJvm() { return this.jvm; }
/*    */    public static class CompleteMinecraftJava {
/* 15 */     private Long id; private String name; public void setId(Long id) { this.id = id; } private String path; private List<String> args; public void setName(String name) { this.name = name; } public void setPath(String path) { this.path = path; } public void setArgs(List<String> args) { this.args = args; } public String toString() { return "MinecraftJava.CompleteMinecraftJava(id=" + getId() + ", name=" + getName() + ", path=" + getPath() + ", args=" + getArgs() + ")"; }
/* 16 */     public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof CompleteMinecraftJava)) return false;  CompleteMinecraftJava other = (CompleteMinecraftJava)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object this$path = getPath(), other$path = other.getPath(); if ((this$path == null) ? (other$path != null) : !this$path.equals(other$path)) return false;  Object<String> this$args = (Object<String>)getArgs(), other$args = (Object<String>)other.getArgs(); return !((this$args == null) ? (other$args != null) : !this$args.equals(other$args)); } protected boolean canEqual(Object other) { return other instanceof CompleteMinecraftJava; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object $path = getPath(); result = result * 59 + (($path == null) ? 43 : $path.hashCode()); Object<String> $args = (Object<String>)getArgs(); return result * 59 + (($args == null) ? 43 : $args.hashCode()); }
/*    */     
/* 18 */     public Long getId() { return this.id; }
/* 19 */     public String getName() { return this.name; }
/* 20 */     public String getPath() { return this.path; } public List<String> getArgs() {
/* 21 */       return this.args;
/*    */     }
/*    */     public static CompleteMinecraftJava create(Long id, String name, String path, List<String> args) {
/* 24 */       CompleteMinecraftJava m = new CompleteMinecraftJava();
/* 25 */       m.setId(id);
/* 26 */       m.setName(name);
/* 27 */       m.setPath(path);
/* 28 */       m.setArgs(args);
/* 29 */       return m;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlauncher\tlauncher\entity\minecraft\MinecraftJava.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */