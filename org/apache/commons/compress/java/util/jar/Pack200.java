/*     */ package org.apache.commons.compress.java.util.jar;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.util.SortedMap;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import org.apache.commons.compress.harmony.archive.internal.nls.Messages;
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
/*     */ public abstract class Pack200
/*     */ {
/*     */   private static final String SYSTEM_PROPERTY_PACKER = "java.util.jar.Pack200.Packer";
/*     */   private static final String SYSTEM_PROPERTY_UNPACKER = "java.util.jar.Pack200.Unpacker";
/*     */   
/*     */   static Object newInstance(String systemProperty, String defaultClassName) {
/* 276 */     return AccessController.doPrivileged(() -> {
/*     */           String className = System.getProperty(systemProperty, defaultClassName);
/*     */           
/*     */           try {
/*     */             return Pack200.class.getClassLoader().loadClass(className).newInstance();
/* 281 */           } catch (Exception e) {
/*     */             throw new Error(Messages.getString("archive.3E", className), e);
/*     */           } 
/*     */         });
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
/*     */   public static Packer newPacker() {
/* 298 */     return (Packer)newInstance("java.util.jar.Pack200.Packer", "org.apache.commons.compress.harmony.pack200.Pack200PackerAdapter");
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
/*     */   public static Unpacker newUnpacker() {
/* 312 */     return (Unpacker)newInstance("java.util.jar.Pack200.Unpacker", "org.apache.commons.compress.harmony.unpack200.Pack200UnpackerAdapter");
/*     */   }
/*     */   
/*     */   public static interface Unpacker {
/*     */     public static final String DEFLATE_HINT = "unpack.deflate.hint";
/*     */     public static final String FALSE = "false";
/*     */     public static final String KEEP = "keep";
/*     */     public static final String PROGRESS = "unpack.progress";
/*     */     public static final String TRUE = "true";
/*     */     
/*     */     void addPropertyChangeListener(PropertyChangeListener param1PropertyChangeListener);
/*     */     
/*     */     SortedMap<String, String> properties();
/*     */     
/*     */     void removePropertyChangeListener(PropertyChangeListener param1PropertyChangeListener);
/*     */     
/*     */     void unpack(File param1File, JarOutputStream param1JarOutputStream) throws IOException;
/*     */     
/*     */     void unpack(InputStream param1InputStream, JarOutputStream param1JarOutputStream) throws IOException;
/*     */   }
/*     */   
/*     */   public static interface Packer {
/*     */     public static final String CLASS_ATTRIBUTE_PFX = "pack.class.attribute.";
/*     */     public static final String CODE_ATTRIBUTE_PFX = "pack.code.attribute.";
/*     */     public static final String DEFLATE_HINT = "pack.deflate.hint";
/*     */     public static final String EFFORT = "pack.effort";
/*     */     public static final String ERROR = "error";
/*     */     public static final String FALSE = "false";
/*     */     public static final String FIELD_ATTRIBUTE_PFX = "pack.field.attribute.";
/*     */     public static final String KEEP = "keep";
/*     */     public static final String KEEP_FILE_ORDER = "pack.keep.file.order";
/*     */     public static final String LATEST = "latest";
/*     */     public static final String METHOD_ATTRIBUTE_PFX = "pack.method.attribute.";
/*     */     public static final String MODIFICATION_TIME = "pack.modification.time";
/*     */     public static final String PASS = "pass";
/*     */     public static final String PASS_FILE_PFX = "pack.pass.file.";
/*     */     public static final String PROGRESS = "pack.progress";
/*     */     public static final String SEGMENT_LIMIT = "pack.segment.limit";
/*     */     public static final String STRIP = "strip";
/*     */     public static final String TRUE = "true";
/*     */     public static final String UNKNOWN_ATTRIBUTE = "pack.unknown.attribute";
/*     */     
/*     */     void addPropertyChangeListener(PropertyChangeListener param1PropertyChangeListener);
/*     */     
/*     */     void pack(JarFile param1JarFile, OutputStream param1OutputStream) throws IOException;
/*     */     
/*     */     void pack(JarInputStream param1JarInputStream, OutputStream param1OutputStream) throws IOException;
/*     */     
/*     */     SortedMap<String, String> properties();
/*     */     
/*     */     void removePropertyChangeListener(PropertyChangeListener param1PropertyChangeListener);
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\apache\commons\compress\jav\\util\jar\Pack200.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */