/*    */ package org.tlauncher.util;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import org.tlauncher.exceptions.ParseException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Reflect
/*    */ {
/*    */   public static Field getField0(Class<?> clazz, String name) throws NoSuchFieldException, SecurityException {
/* 14 */     if (clazz == null) {
/* 15 */       throw new NullPointerException("class is null");
/*    */     }
/* 17 */     if (name == null || name.isEmpty()) {
/* 18 */       throw new NullPointerException("name is null or empty");
/*    */     }
/* 20 */     return clazz.getField(name);
/*    */   }
/*    */   
/*    */   public static Field getField(Class<?> clazz, String name) {
/*    */     try {
/* 25 */       return getField0(clazz, name);
/* 26 */     } catch (Exception e) {
/* 27 */       U.log(new Object[] { "Error getting field", name, "from", clazz, e });
/*    */       
/* 29 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static <T> T getValue0(Field field, Class<T> classOfT, Object parent) throws IllegalArgumentException, IllegalAccessException {
/* 34 */     if (field == null) {
/* 35 */       throw new NullPointerException("field is null");
/*    */     }
/* 37 */     if (classOfT == null) {
/* 38 */       throw new NullPointerException("classOfT is null");
/*    */     }
/* 40 */     if (parent == null) {
/* 41 */       throw new NullPointerException("parent is NULL");
/*    */     }
/* 43 */     Class<?> fieldClass = field.getType();
/*    */     
/* 45 */     if (fieldClass == null)
/*    */     {
/* 47 */       throw new NullPointerException("field has no shell");
/*    */     }
/* 49 */     if (!fieldClass.equals(classOfT) && !fieldClass.isAssignableFrom(classOfT)) {
/* 50 */       throw new IllegalArgumentException("field is not assignable from return type class");
/*    */     }
/* 52 */     return (T)field.get(parent);
/*    */   }
/*    */   
/*    */   public static <T> T getValue(Field field, Class<T> classOfT, Object parent) {
/*    */     try {
/* 57 */       return getValue0(field, classOfT, parent);
/* 58 */     } catch (Exception e) {
/* 59 */       U.log(new Object[] { "Cannot get value of", field, "from", classOfT, parent, e });
/*    */       
/* 61 */       return null;
/*    */     } 
/*    */   }
/*    */   public static <T> T cast(Object o, Class<T> classOfT) {
/* 65 */     if (classOfT == null) {
/* 66 */       throw new NullPointerException();
/*    */     }
/* 68 */     return classOfT.isInstance(o) ? classOfT.cast(o) : null;
/*    */   }
/*    */   
/*    */   public static <T extends Enum<T>> T parseEnum0(Class<T> enumClass, String string) throws ParseException {
/* 72 */     if (enumClass == null) {
/* 73 */       throw new NullPointerException("class is null");
/*    */     }
/* 75 */     if (string == null) {
/* 76 */       throw new NullPointerException("string is null");
/*    */     }
/* 78 */     Enum[] arrayOfEnum = (Enum[])enumClass.getEnumConstants();
/* 79 */     for (Enum enum_ : arrayOfEnum) {
/* 80 */       if (string.equalsIgnoreCase(enum_.toString()))
/* 81 */         return (T)enum_; 
/*    */     } 
/* 83 */     throw new ParseException("Cannot parse value:\"" + string + "\"; enum: " + enumClass.getSimpleName());
/*    */   }
/*    */   
/*    */   public static <T extends Enum<T>> T parseEnum(Class<T> enumClass, String string) {
/*    */     try {
/* 88 */       return parseEnum0(enumClass, string);
/* 89 */     } catch (Exception e) {
/* 90 */       U.log(new Object[] { e });
/*    */       
/* 92 */       return null;
/*    */     } 
/*    */   }
/*    */   private Reflect() {
/* 96 */     throw new RuntimeException();
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\org\tlaunche\\util\Reflect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */